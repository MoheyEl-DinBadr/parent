package com.mohey.baser2dbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class BaseR2DBCApplication {
    public static void main(String[] args) {
        SpringApplication.run(BaseR2DBCApplication.class, args);
    }

}
