package com.mohey.basemodule.model;

import org.springframework.data.domain.Sort;

import java.util.List;

public class SortImpl extends Sort {

    protected SortImpl(List<OrderImpl> orders) {
        super(orders.stream().map(order -> (Order) order).toList());
    }
}
