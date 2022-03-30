package com.mohey.basemongo.repositories;

import com.mohey.commonmodel.model.BaseModel;
import com.mohey.commonmodel.repositories.IBaseRepository;

public interface IBaseMongoRepository<Model extends BaseModel>
        extends IBaseRepository<Model> {
}
