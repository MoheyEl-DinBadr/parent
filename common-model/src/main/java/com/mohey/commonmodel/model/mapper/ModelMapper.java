package com.mohey.commonmodel.model.mapper;

import com.mohey.commonmodel.model.BaseModel;
import org.mapstruct.MappingTarget;

public interface ModelMapper<Model extends BaseModel> {

    void updateModel(Model includeUpdates, @MappingTarget Model toUpdateModel);
}
