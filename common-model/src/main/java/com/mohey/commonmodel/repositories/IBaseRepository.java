package com.mohey.commonmodel.repositories;

import com.mohey.commonmodel.model.BaseModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@NoRepositoryBean
public interface IBaseRepository <Model extends BaseModel> extends
         ReactiveSortingRepository<Model, UUID>, ReactiveQueryByExampleExecutor<Model> {
    Flux<Model> findByIdNotNull(Pageable pageable);
}
