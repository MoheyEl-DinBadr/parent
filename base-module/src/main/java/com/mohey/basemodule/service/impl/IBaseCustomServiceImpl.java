package com.mohey.basemodule.service.impl;

import com.mohey.basemodule.filter.BaseFilter;
import com.mohey.basemodule.model.BaseModel;
import com.mohey.basemodule.model.mapper.ModelMapper;
import com.mohey.basemodule.repositories.IBaseCustomRepository;
import com.mohey.basemodule.repositories.IBaseRepository;
import com.mohey.basemodule.service.IBaseCustomService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public abstract class IBaseCustomServiceImpl<
        Model extends BaseModel,
        Filter extends BaseFilter,
        IRepository extends IBaseRepository<Model>,
        ICustomRepository extends IBaseCustomRepository<Model, Filter>,
        Mapper extends ModelMapper<Model>
        >
        extends IBaseServiceImpl<Model, IRepository, Mapper>
        implements IBaseCustomService<Model, Filter> {

    private final ICustomRepository customRepository;

    public IBaseCustomServiceImpl(ICustomRepository customRepository, IRepository repository, Mapper mapper) {
        super(repository, mapper);
        this.customRepository = customRepository;
    }

    @Override
    public Flux<Model> findAllByFilter(Filter filter) {
        return this.customRepository.queryForFilter(filter);
    }

    @Override
    public Flux<Model> FindAllByFilterSorted(Filter filter, Sort sort) {
        return this.customRepository.queryForFilterAndSort(filter, sort);
    }

    @Override
    public Mono<Page<Model>> findAllByFilterPageable(Filter filter, Pageable pageable) {
        return this.customRepository.queryForFilterPageable(filter, pageable);
    }
}
