package com.mohey.basemodule.model.mapper;

import com.mohey.basemodule.model.BaseModel;
import org.mapstruct.MappingTarget;

public interface ModelMapper<Model extends BaseModel> {

    void updateModel(Model includeUpdates, @MappingTarget Model toUpdateModel);
}
