package com.mohey.basemongo.repositories;

import com.mohey.commonmodel.model.BaseModel;
import com.mohey.commonmodel.repositories.IBaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface IBaseMongoRepository<Model extends BaseModel>
        extends IBaseRepository<Model> {
}
