package com.mohey.commonmodel.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class BaseModelDto<Model extends BaseModel> {
    private UUID id;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;


    public BaseModelDto(Model model) {
        this.id = model.getId();
        this.createdOn = model.getCreatedOn();
        this.updatedOn = model.getUpdatedOn();
    }

}
