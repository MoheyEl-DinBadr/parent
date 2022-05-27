package com.mohey.baser2dbc.repositories;

import com.mohey.baser2dbc.model.BaseSQLModel;
import com.mohey.commonmodel.repositories.IBaseRepository;
import org.springframework.data.querydsl.ReactiveQuerydslPredicateExecutor;

public interface IBaseSQLRepository<Model extends BaseSQLModel>
        extends IBaseRepository<Model>, ReactiveQuerydslPredicateExecutor<Model> {
}
