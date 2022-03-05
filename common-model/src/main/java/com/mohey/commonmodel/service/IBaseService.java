package com.mohey.commonmodel.service;

import com.mohey.commonmodel.model.BaseModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


public interface IBaseService<Model extends BaseModel> {

    Mono<Model> findById(String id);

    Mono<Model> addOne(Model model);

    Flux<Model> findAll();

    Flux<Model> fetchAllByIds(List<String> ids);

    Flux<Model> findAllSorted(Sort sort);

    Flux<Model> findAllPageable(Pageable pageable);

    Flux<Model> addMany(Flux<Model> models);

    Flux<Model> updateMany(Flux<Model> models);

    Mono<Model> deleteOne(Model model);

    Mono<Model> deleteOneById(String id);

    Mono<Model> updateOne(String id, Model model);

    Mono<Model> doBeforeUpdate(Model model);

    Mono<Model> doBeforeAdd(Model model);

    Mono<Model> doAfterUpdate(Model model);

    Mono<Model> doAfterAdd(Model model);

    Mono<Model> doAfterAddOrUpdate(Model model);

    Mono<Model> doBeforeAddOrUpdate(Model model);

    Mono<Model> doBeforeDelete(Model model);

    Mono<Model> doAfterDelete(Model model);

    Mono<Model> beforeSaveValidations(Model model, boolean isAddingNewItem);
}
