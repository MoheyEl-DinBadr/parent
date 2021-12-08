package com.mohey.basemodule.service;

import com.mohey.basemodule.filter.BaseFilter;
import com.mohey.basemodule.model.BaseModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IBaseCustomService<Model extends BaseModel, Filter extends BaseFilter>
        extends IBaseService<Model> {

    Flux<Model> findAllByFilter(Filter filter);

    Flux<Model> FindAllByFilterSorted(Filter filter, Sort sort);

    Mono<Page<Model>> findAllByFilterPageable(Filter filter, Pageable pageable);
}
