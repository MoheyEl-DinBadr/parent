package com.mohey.basemongo.model;

import com.mohey.commonmodel.model.BaseModel;
import com.querydsl.core.annotations.QueryEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data @Document @QueryEntity
public class MongoModel extends BaseModel {

    private BaseModel model;
}
