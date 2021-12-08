package com.mohey.basemodule.service.impl;

import com.mohey.basemodule.model.BaseModel;
import com.mohey.basemodule.model.mapper.ModelMapper;
import com.mohey.basemodule.repositories.IBaseRepository;
import com.mohey.basemodule.service.IBaseService;
import com.mohey.basemodule.service.ReactiveConverter;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Data
public abstract class IBaseServiceImpl<Model extends BaseModel,
        IRepository extends IBaseRepository<Model>, Mapper extends ModelMapper<Model>> implements IBaseService<Model> {

    protected final Mapper modelMapper;
    private final IRepository repository;
    private ReactiveConverter reactiveConverter;

    public IBaseServiceImpl(IRepository repository, Mapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }


    @Override
    public Mono<Model> findById(ObjectId id) {
        return this.repository.findById(id);
    }

    @Override
    @Transactional
    public Mono<Model> addOne(Model model) {
        return Mono.zip(this.doBeforeAdd(model), this.doBeforeAddOrUpdate(model))
                .flatMap(objects -> this.beforeSaveValidations(objects.getT1(), true))
                .flatMap(this.repository::save)
                .flatMap(addedModel -> Mono.zip(this.doAfterAdd(addedModel), this.doAfterAddOrUpdate(addedModel)).thenReturn(addedModel));
    }

    @Override
    public Flux<Model> findAll() {
        return this.repository.findAll();
    }

    @Override
    public Flux<Model> fetchAllByIds(List<ObjectId> ids) {
        return Flux.fromIterable(ids)
                .map(objectId -> this.repository.findById(objectId))
                .collectList()
                .flatMapMany(monos -> Mono.zip(monos, objects -> Arrays.stream(objects).map(o -> (Model) o).toList())
                        .flatMapMany(Flux::fromIterable));
    }

    @Override
    public Flux<Model> findAllSorted(Sort sort) {
        return this.repository.findAll(sort);
    }

    @Override
    public Flux<Model> findAllPageable(Pageable pageable) {
        return this.repository.findByIdNotNull(pageable);
    }

    @Override
    @Transactional
    public Flux<Model> addMany(Flux<Model> models) {

        return models
                .map(model -> Mono.zip(this.doBeforeAddOrUpdate(model), this.doAfterAdd(model)).flatMap(objects -> this.beforeSaveValidations(model, true)))
                .collectList()
                .flatMap(monos -> Mono.zip(monos, objects -> Arrays.stream(objects).map(o -> (Model) o).toList()))
                .flatMapMany(this.repository::saveAll)
                .concatMap(model -> Mono.zip(this.doAfterAdd(model), this.doAfterAddOrUpdate(model))
                        .thenReturn(model)
                );

    }

    @Override
    @Transactional
    public Flux<Model> updateMany(Flux<Model> models) {
        return models
                .map(model -> Mono.zip(this.doBeforeAddOrUpdate(model), this.doBeforeUpdate(model)).flatMap(objects -> this.beforeSaveValidations(model, false)))
                .collectList()
                .flatMap(monos -> Mono.zip(monos, objects -> Arrays.stream(objects).map(o -> (Model) o).toList()))
                .flatMapMany(this.repository::saveAll)
                .concatMap(model -> Mono.zip(this.doAfterAddOrUpdate(model), this.doBeforeUpdate(model))
                        .thenReturn(model)
                );
    }

    @Override
    @Transactional
    public Mono<Model> deleteOne(Model model) {
        return this.doBeforeDelete(model)
                .flatMap(this.repository::delete)
                .flatMap(unused -> this.doAfterDelete(model));
    }

    @Override
    @Transactional
    public Mono<Model> deleteOneById(ObjectId id) {
        return this.findById(id)
                .flatMap(this::deleteOne);
    }

    @Override
    @Transactional
    public Mono<Model> updateOne(ObjectId id, Model model) {
        return this.findById(id)
                .map(toUpdate -> {
                            this.modelMapper.updateModel(model, toUpdate);
                            return toUpdate;
                        }
                )
                .flatMap(toUpdate -> Mono.zip(doBeforeAddOrUpdate(toUpdate), doBeforeUpdate(toUpdate)).flatMap(objects -> this.beforeSaveValidations(toUpdate, false)))
                .flatMap(this.repository::save)
                .flatMap(updatedModel -> Mono.zip(this.doAfterAddOrUpdate(updatedModel), this.doAfterUpdate(updatedModel)))
                .map(Tuple2::getT1);
    }

    @Override
    public Mono<Model> doBeforeUpdate(Model model) {
        model.setUpdatedOn(LocalDateTime.now());
        return Mono.just(model);
    }

    @Override
    public Mono<Model> doBeforeAdd(Model model) {
        model.setCreatedOn(LocalDateTime.now());
        return Mono.just(model);
    }

    @Override
    public Mono<Model> doAfterUpdate(Model model) {
        return Mono.just(model);
    }

    @Override
    public Mono<Model> doAfterAdd(Model model) {
        return Mono.just(model);
    }

    @Override
    public Mono<Model> doAfterAddOrUpdate(Model model) {
        return Mono.just(model);
    }

    @Override
    public Mono<Model> doBeforeAddOrUpdate(Model model) {
        return Mono.just(model);
    }

    @Override
    public Mono<Model> doBeforeDelete(Model model) {
        return Mono.just(model);
    }

    @Override
    public Mono<Model> doAfterDelete(Model model) {
        return Mono.just(model);
    }

    @Override
    public Mono<Model> beforeSaveValidations(Model model, boolean isAddingNewItem) {
        return Mono.just(model);
    }
}
