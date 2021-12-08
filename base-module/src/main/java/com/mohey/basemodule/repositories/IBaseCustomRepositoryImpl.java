package com.mohey.basemodule.repositories;

import com.mohey.basemodule.filter.BaseFilter;
import com.mohey.basemodule.model.BaseModel;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class IBaseCustomRepositoryImpl<Model extends BaseModel, Filter extends BaseFilter>
        implements IBaseCustomRepository<Model, Filter> {

    public ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public Flux<Model> queryForFilter(Filter filter) {
        return this.reactiveMongoTemplate.aggregateAndReturn(this.getClassFromLookup())
                .by(Aggregation.newAggregation(this.getClassFromLookup(), this.buildQueryForFilter(filter)))
                .all();
    }

    @Override
    public Flux<Model> queryForFilterAndSort(Filter filter, Sort sort) {
        return this.reactiveMongoTemplate.aggregateAndReturn(this.getClassFromLookup())
                .by(Aggregation.newAggregation(this.getClassFromLookup(), this.buildQueryForSortAndFilter(filter, sort)))
                .all();
    }

    @Override
    public Mono<Page<Model>> queryForFilterPageable(Filter filter, Pageable pageable) {
        List<AggregationOperation> countOperations = this.buildQueryForFilter(filter);
        countOperations.add(Aggregation.group("_id").count().as("count"));
        return Mono.zip(this.reactiveMongoTemplate.aggregateAndReturn(this.getClassFromLookup())
                                .by(Aggregation.newAggregation(this.getClassFromLookup(),this.buildQueryForFilterPageable(filter, pageable)))
                                .all().collectList(),
                        this.reactiveMongoTemplate.aggregate(Aggregation.newAggregation(this.getClassFromLookup(), countOperations), this.getClassFromLookup(), Long.class).next())
                .map(objects -> new PageImpl<>(objects.getT1(), pageable, objects.getT2()));
    }

    private List<AggregationOperation> buildQueryForFilterPageable(Filter filter, Pageable pageable) {
        List<AggregationOperation> aggregationOperations = this.buildQueryForFilter(filter);
        aggregationOperations.add(Aggregation.skip(pageable.getOffset()));
        aggregationOperations.add(Aggregation.limit(pageable.getPageSize()));
        return aggregationOperations;
    }

    protected List<AggregationOperation> buildQueryForFilter(Filter filter) {
        List<AggregationOperation> aggregationOperations = new ArrayList<>();

        if (filter.getId() != null)
            aggregationOperations.add(Aggregation.match(Criteria.where("_id").is(filter.getId())));
        if (filter.getExcludedIds() != null)
            aggregationOperations.add(Aggregation.match(Criteria.where("_id").nin(filter.getExcludedIds())));
        if (filter.getIncludedIds() != null)
            aggregationOperations.add(Aggregation.match(Criteria.where("_id").in(filter.getIncludedIds())));
        if (filter.getCreatedOnEq() != null)
            aggregationOperations.add(Aggregation.match(Criteria.where("createdOn").is(filter.getCreatedOnEq())));
        if (filter.getCreatedOnGt() != null)
            aggregationOperations.add(Aggregation.match(Criteria.where("createdOn").gt(filter.getCreatedOnGt())));
        if (filter.getCreatedOnLt() != null)
            aggregationOperations.add(Aggregation.match(Criteria.where("createdOn").lt(filter.getCreatedOnLt())));
        if (filter.getCreatedOnGoE() != null)
            aggregationOperations.add(Aggregation.match(Criteria.where("createdOn").gte(filter.getCreatedOnGoE())));
        if (filter.getCreatedOnLoE() != null)
            aggregationOperations.add(Aggregation.match(Criteria.where("createdOn").lte(filter.getCreatedOnLoE())));

        if (filter.getUpdatedOnEq() != null)
            aggregationOperations.add(Aggregation.match(Criteria.where("updatedOn").is(filter.getUpdatedOnEq())));
        if (filter.getUpdatedOnGt() != null)
            aggregationOperations.add(Aggregation.match(Criteria.where("updatedOn").gt(filter.getUpdatedOnGt())));
        if (filter.getUpdatedOnLt() != null)
            aggregationOperations.add(Aggregation.match(Criteria.where("updatedOn").lt(filter.getUpdatedOnLt())));
        if (filter.getUpdatedOnGoE() != null)
            aggregationOperations.add(Aggregation.match(Criteria.where("updatedOn").gte(filter.getUpdatedOnGoE())));
        if (filter.getUpdatedOnLoE() != null)
            aggregationOperations.add(Aggregation.match(Criteria.where("updatedOn").lte(filter.getUpdatedOnLoE())));

        if (filter.getLimit() != null)
            aggregationOperations.add(Aggregation.limit(filter.getLimit()));

        return aggregationOperations;
    }


    private List<AggregationOperation> buildQueryForSortAndFilter(Filter filter, Sort sort) {
        List<AggregationOperation> aggregationOperations = null;
        if (filter != null)
            aggregationOperations = this.buildQueryForFilter(filter);
        if (sort != null) {
            if (aggregationOperations == null)
                aggregationOperations = new ArrayList<>();

            aggregationOperations.add(Aggregation.sort(sort));
        }


        return aggregationOperations == null ? new ArrayList<>() : aggregationOperations;
    }


}
