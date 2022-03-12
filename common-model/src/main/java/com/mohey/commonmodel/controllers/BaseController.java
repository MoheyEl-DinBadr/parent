package com.mohey.commonmodel.controllers;

import com.mohey.commonmodel.model.BaseModel;
import com.mohey.commonmodel.model.BaseModelDto;
import com.mohey.commonmodel.service.IBaseService;
import com.mohey.commonmodel.service.ReactiveConverter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;


public abstract class BaseController<Model extends BaseModel,
        ModelDto extends BaseModelDto<Model>,
        ModelPostDto,
        ModelUpdateDto,
        BaseService extends IBaseService<Model>> {

    protected final BaseService service;

    protected final ReactiveConverter reactiveConverter;

    public BaseController(BaseService service, ReactiveConverter reactiveConverter) {
        this.service = service;
        this.reactiveConverter = reactiveConverter;
    }

    @GetMapping("/{id}")
    public Mono<ModelDto> getOneById(@PathVariable UUID id) {
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
                properties.toArray(new String[0])));

        return this.manipulateModelBeforeBecomingDto(dataPageable);
    }

    @PutMapping("/{id}")
    public Mono<ModelDto> updateOne(@PathVariable UUID id, @RequestBody ModelUpdateDto updateDto) {
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
    public Mono<ModelDto> deleteOneById(@PathVariable UUID id) {
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
