package com.mohey.basemodule.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class BaseModelDto<Model extends BaseModel> {
    private ObjectId id;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;


    public BaseModelDto(Model model) {
        this.id = model.getId();
        this.createdOn = model.getCreatedOn();
        this.updatedOn = model.getUpdatedOn();
    }

}
