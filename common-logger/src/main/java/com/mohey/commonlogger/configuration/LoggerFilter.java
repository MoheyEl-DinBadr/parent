package com.mohey.commonlogger.configuration;

import com.mohey.commonlogger.model.LogModel;
import com.mohey.commonlogger.model.LogType;
import com.mohey.commonlogger.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Component
public class LoggerFilter implements WebFilter {
    private final String serviceName;
    private final AtomicReference<String> remoteAddress = new AtomicReference<>();
    private final AtomicReference<String> path = new AtomicReference<>();
    private final AtomicReference<String> transactionId = new AtomicReference<>();

    private final AtomicReference<String> method = new AtomicReference<>();

    public LoggerFilter(@Value("${spring.application.name:application}") String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        var headers = new AtomicReference<HttpHeaders>();
        Optional.of(exchange.getRequest()).ifPresent(req -> {
            path.set(Optional.ofNullable(req.getPath().toString()).orElse(""));
            Optional.ofNullable(req.getRemoteAddress())
                    .ifPresent(address -> remoteAddress.set(address.toString()));
            headers.set(req.getHeaders());
            method.set(req.getMethodValue());
            transactionId.set(headers.get().containsKey(Constants.TRANSACTION_ID) ? Objects.requireNonNull(headers.get().get(Constants.TRANSACTION_ID)).toString() : UUID.randomUUID().toString());
        });

        return exchange.getSession().doOnNext(session -> {
                    var creationTime = session.getCreationTime().toString();
                    log.info(new LogModel()
                            .setSessionId(transactionId.get())
                            .setType(LogType.IN_BOUND)
                            .setTime(creationTime)
                            .setServiceName(serviceName)
                            .setPath(path.get())
                            .setRemoteAddress(remoteAddress.get())
                            .setHeaders(headers.get())
                            .setMethod(method.get())
                            .toString()
                    );
                    exchange.getResponse().getHeaders().add(Constants.TRANSACTION_ID, transactionId.get());
                }).flatMap(s -> chain.filter(exchange))
                .then(this.logResponse(exchange));
    }

    Mono<Void> logResponse(ServerWebExchange exchange) {
        return exchange.getSession()
                .flatMap(session -> {
                    var response = exchange.getResponse();
                    var headers = response.getHeaders();
                    var statusCode = response.getRawStatusCode();
                    log.info(new LogModel()
                            .setSessionId(transactionId.get())
                            .setTime(Instant.now().toString())
                            .setStatusCode(statusCode)
                            .setHeaders(headers)
                            .setType(LogType.OUT_BOUND)
                            .setRemoteAddress(remoteAddress.get())
                            .setPath(path.get())
                            .setMethod(method.get())
                            .toString());

                    return Mono.empty();
                });


    }
}
