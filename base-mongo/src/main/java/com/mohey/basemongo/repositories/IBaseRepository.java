package com.mohey.basemongo.repositories;

import com.mohey.commonmodel.model.BaseModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface IBaseRepository<Model extends BaseModel>
        extends ReactiveMongoRepository<Model, String> {
    Flux<Model> findByIdNotNull(Pageable pageable);
}
