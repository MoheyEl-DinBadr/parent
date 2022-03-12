package com.mohey.baser2dbc.repositories;

import com.infobip.spring.data.r2dbc.QuerydslR2dbcFragment;
import com.mohey.baser2dbc.model.BaseSQLModel;
import com.mohey.commonmodel.model.BaseModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.ReactiveQuerydslPredicateExecutor;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface IBaseRepository<Model extends BaseSQLModel>
        extends R2dbcRepository<Model, UUID>, ReactiveQuerydslPredicateExecutor<Model>, QuerydslR2dbcFragment<Model> {
    Flux<Model> findByIdNotNull(Pageable pageable);
}
