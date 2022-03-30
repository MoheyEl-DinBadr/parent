package com.mohey.resourceserver.aop;

import com.mohey.resourceserver.annotations.UserScope;
import com.mohey.resourceserver.exceptions.UserDontHaveRequiredScope;
import com.mohey.resourceserver.exceptions.UserScopeMustBeReactive;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import reactor.core.CorePublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Aspect
@Component
@AllArgsConstructor
public class ScopeHandler {


    @Around("@annotation(com.mohey.resourceserver.annotations.UserScope)")
    public Object checkUserScope(ProceedingJoinPoint joinPoint) throws Throwable {

        Object proceed = joinPoint.proceed();
        if (!(proceed instanceof CorePublisher))
            return Mono.error(UserScopeMustBeReactive::new);

        var classScopeAnnotation = joinPoint.getTarget().getClass().getAnnotation(UserScope.class);
        var methodScopeAnnotation = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(UserScope.class);

        if ((classScopeAnnotation == null) &&
                (methodScopeAnnotation == null)
        ) return joinPoint.proceed();

        Mono<?> verify = ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .cast(Jwt.class)
                .map(Jwt::getClaims)
                .map(claims -> claims.get("scope"))
                .cast(String.class)
                .flatMapMany(scopes -> Flux.fromArray(scopes.split(" ")))
                .filter(scope -> this.scopeIn(scope, classScopeAnnotation))
                .filter(scope -> this.scopeIn(scope, methodScopeAnnotation))
                .switchIfEmpty(Mono.error(UserDontHaveRequiredScope::new))
                .collectList();


        return proceed instanceof Mono<?> ? verify.then((Mono<?>) proceed) : verify.thenMany((Flux<?>) proceed);

    }

    public boolean scopeIn(String scope, UserScope userScope) {
        if (userScope == null) return true;
        var scopes = userScope.scope();
        for (String data : scopes) if (data.equalsIgnoreCase(scope)) return true;

        return false;
    }
}
