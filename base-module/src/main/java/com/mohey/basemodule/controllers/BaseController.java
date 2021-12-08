package com.mohey.basemodule.controllers;

import com.mohey.basemodule.model.BaseModel;
import com.mohey.basemodule.model.BaseModelDto;
import com.mohey.basemodule.service.IBaseService;
import com.mohey.basemodule.service.ReactiveConverter;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


public abstract class BaseController<Model extends BaseModel,
        ModelDto extends BaseModelDto<Model>,
        ModelPostDto,
        ModelUpdateDto,
        BaseService extends IBaseService<Model>> {

    protected final BaseService service;
    @Autowired
    protected ReactiveConverter reactiveConverter;

    public BaseController(BaseService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public Mono<ModelDto> getOneById(@PathVariable ObjectId id) {
        return this.service.findById(id)
                .flatMap(this::manipulateModelBeforeBecomingDto);
    }

    @GetMapping
    public Flux<ModelDto> findAll() {
        return this.manipulateModelBeforeBecomingDto(this.service.findAll());
    }

    @GetMapping("/pageable")
    public Flux<ModelDto> findAll(@RequestParam(defaultValue = "10") Integer pageSize,
                                  @RequestParam(defaultValue = "0") Integer pageNumber,
                                  @RequestParam(defaultValue = "createdOn") List<String> properties,
                                  @RequestParam(defaultValue = "desc") String direction) {

        Flux<Model> dataPageable = this.service.findAllPageable(PageRequest.of(pageNumber, pageSize,
                direction.matches("i(.*)asc(.*)") ? Sort.Direction.ASC : Sort.Direction.DESC,
                properties.toArray(new String[properties.size()])));

        return this.manipulateModelBeforeBecomingDto(dataPageable);
    }

    @PutMapping("/{id}")
    public Mono<ModelDto> updateOne(@PathVariable ObjectId id, @RequestBody ModelUpdateDto updateDto) {
        return this.manipulateUpdateDtoBeforeBecomingModel(updateDto)
                .flatMap(model -> this.service.updateOne(id, model))
                .flatMap(this::manipulateModelBeforeBecomingDto);
    }

    @PostMapping
    public Mono<ModelDto> addOne(@RequestBody ModelPostDto postDto) {
        return this.manipulatePostDtoBeforeBecomingModel(postDto)
                .flatMap(this.service::addOne)
                .flatMap(this::manipulateModelBeforeBecomingDto);
    }

    @DeleteMapping("/{id}")
    public Mono<ModelDto> deleteOneById(@PathVariable ObjectId id) {
        return this.service.deleteOneById(id)
                .flatMap(this::manipulateModelBeforeBecomingDto);
    }

    @PostMapping("/many")
    public Flux<ModelDto> addMany(@RequestBody List<ModelPostDto> modelPostDtos) {
        Flux<Model> data = this.manipulatePostDtoBeforeBecomingModel(this.reactiveConverter.wrapToFluxDBOperation(() -> modelPostDtos));
        return this.manipulateModelBeforeBecomingDto(this.service.addMany(data));
    }

    @PutMapping
    public Flux<ModelDto> updateMany(@RequestBody List<ModelUpdateDto> modelUpdateDtos) {
        Flux<Model> data = this.manipulateUpdateDtoBeforeBecomingModel(this.reactiveConverter.wrapToFluxDBOperation(() -> modelUpdateDtos));
        return this.manipulateModelBeforeBecomingDto(this.service.updateMany(data));
    }

    public Mono<ModelDto> manipulateModelBeforeBecomingDto(Model model) {
        return Mono.just(this.mapModelToModelDto(model));
    }

    public Flux<ModelDto> manipulateModelBeforeBecomingDto(Flux<Model> modelFlux) {
        return modelFlux.map(this::mapModelToModelDto);
    }

    public Mono<Model> manipulatePostDtoBeforeBecomingModel(ModelPostDto postDto) {
        return Mono.just(this.mapModelPostDtoToModel(postDto));
    }

    public Flux<Model> manipulatePostDtoBeforeBecomingModel(Flux<ModelPostDto> postFlux) {
        return postFlux.map(this::mapModelPostDtoToModel);
    }

    public Mono<Model> manipulateUpdateDtoBeforeBecomingModel(ModelUpdateDto updateDto) {
        return Mono.just(this.mapModelUpdateDtoToModel(updateDto));
    }

    public Flux<Model> manipulateUpdateDtoBeforeBecomingModel(Flux<ModelUpdateDto> updateFlux) {
        return updateFlux.map(this::mapModelUpdateDtoToModel);
    }

    public abstract ModelDto mapModelToModelDto(Model model);

    public abstract Model mapModelPostDtoToModel(ModelPostDto postDto);

    public abstract Model mapModelUpdateDtoToModel(ModelUpdateDto updateDto);

}
