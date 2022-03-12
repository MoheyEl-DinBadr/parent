package com.mohey.basemongo.service.impl;

import com.mohey.commonmodel.filter.BaseFilter;
import com.mohey.basemongo.repositories.IBaseRepository;
import com.mohey.commonmodel.model.BaseModel;
import com.mohey.commonmodel.model.mapper.ModelMapper;
import com.mohey.commonmodel.repositories.IBaseCustomRepository;
import com.mohey.commonmodel.service.IBaseCustomService;
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
    public Flux<Model> findAllByFilterPageable(Filter filter, Pageable pageable) {
        return this.customRepository.queryForFilterPageable(filter, pageable);
    }
}
