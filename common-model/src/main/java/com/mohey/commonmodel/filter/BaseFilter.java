package com.mohey.commonmodel.filter;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Data
@NoArgsConstructor
public class BaseFilter {
    private UUID id;
    private List<UUID> excludedIds;
    private List<UUID> includedIds;

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

    private Long limit;

}
