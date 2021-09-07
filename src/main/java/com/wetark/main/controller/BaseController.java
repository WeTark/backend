package com.wetark.main.controller;

import com.wetark.main.model.Base;
import com.wetark.main.model.BaseService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class BaseController<Entity extends Base> {

    private final BaseService<Entity> service;

    public BaseController(BaseService<Entity> service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public Entity add(@RequestBody Entity entity) {
        return service.add(entity);
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasRole('USER')")
    public Entity findById(@PathVariable String id){
        return service.findByIdOrNull(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public List<Entity> findAll(){
        return service.findAll();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public boolean deleteById(@PathVariable String id){
        return service.deleteById(id) != null;
    }
}
