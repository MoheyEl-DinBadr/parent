package com.mohey.basemodule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.ReactiveMongoTransactionManager;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.reactive.TransactionalOperator;


@SpringBootApplication
@EnableTransactionManagement
public class BaseModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaseModuleApplication.class, args);
    }


    @Bean
    TransactionalOperator transactionalOperator(ReactiveTransactionManager rtxm) {
        return TransactionalOperator.create(rtxm);
    }

    @Bean
    ReactiveTransactionManager transactionManager(ReactiveMongoDatabaseFactory databaseFactory) {
        return new ReactiveMongoTransactionManager(databaseFactory);
    }

}
