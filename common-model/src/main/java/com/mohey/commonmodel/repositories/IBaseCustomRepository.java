package com.mohey.commonmodel.repositories;

import com.mohey.commonmodel.filter.BaseFilter;
import com.mohey.commonmodel.model.BaseModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IBaseCustomRepository<Model extends BaseModel, Filter extends BaseFilter> {
    Flux<Model> queryForFilter(Filter filter);

    Flux<Model> queryForFilterAndSort(Filter filter, Sort sort);

    Flux<Model> queryForFilterPageable(Filter filter, Pageable pageable);

    Class<Model> getClassFromLookup();
}
