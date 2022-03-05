package com.mohey.commonmodel.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;


@Data
@NoArgsConstructor
public class BaseModel implements Serializable {

    @Id
    private UUID id;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

    public BaseModel(UUID id) {
        this.id = id;
    }
}
