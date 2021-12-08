package com.mohey.basemodule.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.Sort;

public class OrderImpl extends Sort.Order {

    public OrderImpl(@JsonProperty String direction, @JsonProperty String property) {
        super(direction.matches("i(.*)desc(.*)") ? Sort.Direction.DESC : Sort.Direction.ASC, property);
    }

    public OrderImpl(Sort.Direction direction, String property) {
        super(direction, property);
    }

    public OrderImpl(Sort.Direction direction, String property, Sort.NullHandling nullHandlingHint) {
        super(direction, property, nullHandlingHint);
    }
}
