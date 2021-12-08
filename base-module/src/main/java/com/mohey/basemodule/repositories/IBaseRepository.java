package com.mohey.basemodule.repositories;

import com.mohey.basemodule.model.BaseModel;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface IBaseRepository<Model extends BaseModel>
        extends ReactiveMongoRepository<Model, ObjectId> {
    Flux<Model> findByIdNotNull(Pageable pageable);
}
