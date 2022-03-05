package com.mohey.baser2dbc.repositories;

import com.mohey.commonmodel.model.BaseModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.ReactiveQuerydslPredicateExecutor;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface IBaseRepository<Model extends BaseModel>
        extends R2dbcRepository<Model, UUID>, ReactiveQuerydslPredicateExecutor<Model> {
    Flux<Model> findByIdNotNull(Pageable pageable);
}
