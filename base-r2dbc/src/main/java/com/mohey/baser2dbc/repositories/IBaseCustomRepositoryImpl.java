package com.mohey.baser2dbc.repositories;

import com.mohey.baser2dbc.model.BaseSQLModel;
import com.mohey.baser2dbc.model.QBaseSQLModel;
import com.mohey.commonmodel.filter.BaseFilter;
import com.mohey.commonmodel.repositories.IBaseCustomRepository;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.sql.SQLQuery;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;

import java.util.Objects;


@Data
@Slf4j
public abstract class IBaseCustomRepositoryImpl<Model extends BaseSQLModel, Filter extends BaseFilter, BaseRepository extends IBaseSQLRepository<Model>>
        implements IBaseCustomRepository<Model, Filter> {

    @Autowired
    public BaseRepository repository;

    public <S extends QBaseSQLModel> S getPathToEntity() {
        return (S) QBaseSQLModel.baseSQLModel;
    }


    @Override
    public Flux<Model> queryForFilter(Filter filter) {
        return this.repository.query(sqlQuery -> this.buildQueryForFilter(filter, sqlQuery)).all().cast(this.getClassFromLookup());
    }

    @Override
    public Flux<Model> queryForFilterAndSort(Filter filter, Sort sort) {
        return this.repository.query(sqlQuery -> this.buildQueryForSortAndFilter(filter, sort, sqlQuery)
        ).all().cast(this.getClassFromLookup());
    }

    @Override
    public Flux<Model> queryForFilterPageable(Filter filter, Pageable pageable) {
        return this.repository.query(sqlQuery -> this.buildQueryForSortAndFilter(filter, pageable.getSort(), sqlQuery)
                        .offset(pageable.getOffset()).limit(pageable.getPageSize()))
                .all().cast(this.getClassFromLookup());
    }

    private <E> SQLQuery<E> buildQueryForSortAndFilter(Filter filter, Sort sort, SQLQuery<E> query) {
        query = this.buildQueryForFilter(filter, query)
                .orderBy(sort.stream().map(order -> {
                            Path<Model> fieldPath = Expressions.path(
                                    this.getClassFromLookup(),
                                    this.getPathToEntity(),
                                    order.getProperty());
                            Order orderType = Order.ASC;
                            if (order.isDescending())
                                orderType = Order.DESC;

                            return new OrderSpecifier(orderType, fieldPath);
                        }).toArray(OrderSpecifier[]::new)
                );
        return query;
    }

    protected <E> SQLQuery<E> buildQueryForFilter(Filter filter, SQLQuery<E> query) {
        var entity = QBaseSQLModel.baseSQLModel;

        if (Objects.nonNull(filter.getId()))
            query = query.where(entity.id.eq(filter.getId()));
        if (Objects.nonNull(filter.getIncludedIds()))
            query = query.where(entity.id.in(filter.getIncludedIds()));
        if (Objects.nonNull(filter.getExcludedIds()))
            query = query.where(entity.id.notIn(filter.getExcludedIds()));

        if (Objects.nonNull(filter.getCreatedOnEq()))
            query = query.where(entity.createdOn.eq(filter.getCreatedOnEq()));
        if (Objects.nonNull(filter.getCreatedOnGoE()))
            query = query.where(entity.createdOn.goe(filter.getCreatedOnGoE()));
        if (Objects.nonNull(filter.getCreatedOnLoE()))
            query = query.where(entity.createdOn.loe(filter.getCreatedOnLoE()));
        if (Objects.nonNull(filter.getCreatedOnGt()))
            query = query.where(entity.createdOn.gt(filter.getCreatedOnGt()));
        if (Objects.nonNull(filter.getCreatedOnLt()))
            query = query.where(entity.createdOn.lt(filter.getCreatedOnLt()));

        if (Objects.nonNull(filter.getUpdatedOnEq()))
            query = query.where(entity.updatedOn.eq(filter.getCreatedOnEq()));
        if (Objects.nonNull(filter.getUpdatedOnGoE()))
            query = query.where(entity.updatedOn.goe(filter.getCreatedOnGoE()));
        if (Objects.nonNull(filter.getUpdatedOnLoE()))
            query = query.where(entity.updatedOn.loe(filter.getCreatedOnLoE()));
        if (Objects.nonNull(filter.getUpdatedOnGt()))
            query = query.where(entity.updatedOn.gt(filter.getCreatedOnGt()));
        if (Objects.nonNull(filter.getUpdatedOnLt()))
            query = query.where(entity.updatedOn.lt(filter.getCreatedOnLt()));

        if (Objects.nonNull(filter.getLimit()))
            query = query.limit(filter.getLimit());

        return query;
    }
}
