package com.mohey.basemodule.filter;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
public class BaseFilter {
    private ObjectId id;
    private List<ObjectId> excludedIds;
    private List<ObjectId> includedIds;

    private LocalDateTime createdOnEq;
    private LocalDateTime createdOnGt;
    private LocalDateTime createdOnLt;
    private LocalDateTime createdOnGoE;
    private LocalDateTime createdOnLoE;

    private LocalDateTime updatedOnEq;
    private LocalDateTime updatedOnGt;
    private LocalDateTime updatedOnLt;
    private LocalDateTime updatedOnGoE;
    private LocalDateTime updatedOnLoE;

    private Integer limit;

}
