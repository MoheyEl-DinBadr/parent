package com.mohey.resourceserver.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Stream;

public class KeycloakJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final String ROLE_PREFIX = "ROLE_";
    private static final String SCOPE_PREFIX = "SCOPE_";

    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        var realmAccess = this.mapRealmAccess(source.getClaim("realm_access"));
        var scopesObject = source.getClaim("scope");
        Collection<String> scopes;
        if (scopesObject instanceof String && StringUtils.hasText((String) scopesObject)) scopes = Arrays.asList(((String) scopesObject).split(" "));
        else if (scopesObject instanceof Collection) scopes = this.mapToStringCollection(scopesObject);
        else scopes = List.of();
        if (realmAccess == null || realmAccess.isEmpty()) return new ArrayList<>();

        var rolesObject =realmAccess.get("roles");
        var roles = rolesObject != null? this.mapToStringCollection(rolesObject) : List.<String>of();

        var mappedRoles = this.getMappedAuthority(roles, ROLE_PREFIX);
        var mappedScopes = this.getMappedAuthority(scopes, SCOPE_PREFIX);

        return Stream.concat(mappedRoles, mappedScopes).toList();
    }

    @SuppressWarnings("unchecked")
    private Collection<String> mapToStringCollection(Object object){
        return (Collection<String>) object;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> mapRealmAccess(Object realmAccess){
        return (Map<String, Object>) realmAccess;
    }

    private Stream<GrantedAuthority> getMappedAuthority(Collection<String> authorities, String prefix) {
        return authorities
                .stream().map(roleName -> prefix + roleName)
                .map(SimpleGrantedAuthority::new);
    }
}
