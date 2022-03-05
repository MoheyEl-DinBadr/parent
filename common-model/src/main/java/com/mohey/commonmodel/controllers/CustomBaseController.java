package com.mohey.commonmodel.controllers;

import com.mohey.commonmodel.filter.BaseFilter;
import com.mohey.commonmodel.model.BaseModel;
import com.mohey.commonmodel.model.BaseModelDto;
import com.mohey.commonmodel.service.IBaseCustomService;
import com.mohey.commonmodel.service.ReactiveConverter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;


public abstract class CustomBaseController<Model extends BaseModel,
        ModelDto extends BaseModelDto<Model>,
        ModelPostDto,
        ModelUpdateDto,
        Filter extends BaseFilter,
        BaseCustomService extends IBaseCustomService<Model, Filter>>
        extends BaseController<Model, ModelDto, ModelPostDto, ModelUpdateDto, BaseCustomService> {

    protected final BaseCustomService service;
    protected final ReactiveConverter reactiveConverter;

    public CustomBaseController(BaseCustomService service, ReactiveConverter reactiveConverter) {
        super(service, reactiveConverter);
        this.service = service;
        this.reactiveConverter = reactiveConverter;
    }

    @PostMapping("/find/filter")
    public Flux<ModelDto> findAllByFilter(@RequestBody Filter filter) {
        return this.manipulateModelBeforeBecomingDto(this.filterDependentsFirst(filter));
    }

    /*@PostMapping
    public Mono<Page<ModelDto>> findAllByFilterPageable(@RequestBody Filter filter,
                                                        @RequestParam(defaultValue = "10") Integer pageSize,
                                                        @RequestParam(defaultValue = "0") Integer pageNumber,
                                                        @RequestParam(defaultValue = "createdOn") List<String> properties,
                                                        @RequestParam(defaultValue = "desc") String direction){

        this.service.findAllByFilterPageable(filter, PageRequest.of(pageNumber, pageSize,
                direction.matches("i(.*)asc(.*)")? Sort.Direction.ASC : Sort.Direction.DESC,
                properties.toArray(new String[properties.size()])))
                .flatMap(modelsPage -> {
                    this.manipulateModelBeforeBecomingDto(Flux.fromIterable(modelsPage.getContent()))
                            .map(modelDto -> )
                });
    }*/

    public Flux<Model> filterDependentsFirst(Filter filter) {
        return this.service.findAllByFilter(filter);
    }
}
