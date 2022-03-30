package com.mohey.commonmodel.service;


import com.mohey.commonmodel.filter.BaseFilter;
import com.mohey.commonmodel.model.BaseModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;

public interface IBaseCustomService<Model extends BaseModel, Filter extends BaseFilter>
        extends IBaseService<Model> {

    Flux<Model> findAllByFilter(Filter filter);

    Flux<Model> FindAllByFilterSorted(Filter filter, Sort sort);

    Flux<Model> findAllByFilterPageable(Filter filter, Pageable pageable);
}
