package com.mohey.basemodule.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;

import java.util.function.Supplier;


public interface ReactiveConverter {

    <Model> Mono<Model> wrapToMonoDBOperation(Supplier<Model> supplier);

    <Model> Flux<Model> wrapToFluxDBOperation(Supplier<Iterable<Model>> supplier);

    <Model> ParallelFlux<Model> wrapForParallel(Iterable<Model> iterable);


    @Service
    class ReactiveConverterImpl implements ReactiveConverter {

        @Autowired
        protected ReactiveMongoTemplate transactionTemplate;

        @Override
        public <Model> Mono<Model> wrapToMonoDBOperation(Supplier<Model> supplier) {
            return Mono.fromSupplier(supplier).subscribeOn(Schedulers.boundedElastic());
        }

        @Override
        public <Model> Flux<Model> wrapToFluxDBOperation(Supplier<Iterable<Model>> supplier) {
            return this.wrapToMonoDBOperation(supplier).flatMapMany(Flux::fromIterable).subscribeOn(Schedulers.boundedElastic());
        }

        @Override
        public <Model> ParallelFlux<Model> wrapForParallel(Iterable<Model> iterable) {
            return this.wrapToFluxDBOperation(() -> iterable).parallel().runOn(Schedulers.boundedElastic());
        }
    }

}
