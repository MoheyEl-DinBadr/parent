package com.mohey.basemodule.repositories;

import com.mohey.commonmodel.filter.BaseFilter;
import com.mohey.commonmodel.model.BaseModel;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@WebFluxTest
class IBaseCustomRepositoryImplTest {

    @MockBean
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @MockBean
    private MockClassCustomRepo mockClassCustomRepo;

    @MockBean
    private ReactiveMongoDatabaseFactory reactiveMongoDatabaseFactory;

    //@BeforeEach
    private void initData() {
        var mockClass1 = new MockClass();
        mockClass1.setData("Test1");
        this.reactiveMongoTemplate.insert(mockClass1).block();
        var mockClass2 = new MockClass();
        mockClass2.setData("Test2");
        this.reactiveMongoTemplate.insert(mockClass2).block();
        var mockClass3 = new MockClass();
        mockClass3.setData("Test3");
        this.reactiveMongoTemplate.insert(mockClass3).block();
        var mockClass4 = new MockClass();
        mockClass4.setData("Test4");
        this.reactiveMongoTemplate.insert(mockClass4).block();
        var mockClass5 = new MockClass();
        mockClass5.setData("Test5");
        this.reactiveMongoTemplate.insert(mockClass5).block();
        var mockClass6 = new MockClass();
        mockClass6.setData("Test6");
        this.reactiveMongoTemplate.insert(mockClass6).block();
        var mockClass = new MockClass();
        mockClass.setData("Test7");
        this.reactiveMongoTemplate.insert(mockClass).block();
    }

    @Test
    void queryForFilter() {
        var mockClass2 = new MockClass();
        mockClass2.setData("Test2");

        var mockFilter = new MockFilter();
        mockFilter.setData("2");

        Mockito.when(this.mockClassCustomRepo.queryForFilter(mockFilter))
                .thenReturn(Flux.fromIterable(List.of(mockClass2)));
        this.mockClassCustomRepo.queryForFilter(mockFilter)
                .doOnNext(mockClass -> {
                    assertEquals(mockClass.getData(), "Test2");
                }).blockFirst();
    }

    @Test
    void queryForFilterAndSort() {
    }

    @Test
    void queryForFilterPageable() {
    }

    @Test
    void buildQueryForFilter() {
    }
}

@Data
class MockClass extends BaseModel {
    private String data;

    public MockClass() {
    }

    public MockClass(UUID id) {
        super(id);
    }
}

@Data
class MockFilter extends BaseFilter {
    private String data;
}

@Repository
class MockClassCustomRepo extends IBaseCustomRepositoryImpl<MockClass, MockFilter> {

    @Override
    public Class<MockClass> getClassFromLookup() {
        return MockClass.class;
    }

    @Override
    protected List<AggregationOperation> buildQueryForFilter(MockFilter filter) {
        var aggs = super.buildQueryForFilter(filter);
        if (filter.getData() != null)
            aggs.add(Aggregation.match(Criteria.where("(.*)" + filter.getData() + "(.*)")));
        return aggs;
    }
}
