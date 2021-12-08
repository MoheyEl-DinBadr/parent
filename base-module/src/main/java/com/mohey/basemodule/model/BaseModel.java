package com.mohey.basemodule.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
public class BaseModel implements Serializable {

    @Id
    private ObjectId id;
    @Field
    private LocalDateTime createdOn;
    @Field
    private LocalDateTime updatedOn;

    public BaseModel(ObjectId id) {
        this.id = id;
    }
}
