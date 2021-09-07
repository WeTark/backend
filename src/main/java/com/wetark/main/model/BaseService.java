package com.wetark.main.model;

import com.wetark.main.helper.PageHelper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class BaseService<Entity extends Base> {
    private final BaseRepository<Entity> repository;

    public BaseService(BaseRepository<Entity> repository) {
        this.repository = repository;
    }

    public boolean existsById(String id) {
        return repository.existsById(id);
    }

    public List<Entity> findAll() {
        return (List<Entity>) repository.findAll();
    }

    public List<Entity> findAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page,size);
        return repository.findAll(pageable).getContent();
    }

    public List<Entity> findAll(String page, String size){
        return repository.findAll(PageHelper.pageable(page,size)).getContent();
    }

    public HashSet<Entity> findAllById(HashSet<String> ids){
        if(ids==null) return new HashSet<Entity>();
        return new HashSet<>((List<Entity>)repository.findAllById(ids));
    }
    
    public Entity findByIdOrNull(String id) {
        Optional<Entity> anyEntity = repository.findById(id);
        if (anyEntity.isPresent()) return anyEntity.get();
        else return null;
    }

    public Entity findById(String id){
        Optional<Entity> anyEntity = repository.findById(id);
        if(anyEntity.isPresent()) return anyEntity.get();
        else throw new RuntimeException("Not found by ID");
    }

    public Entity add(Entity entity) {
        entity = repository.save(entity);
        return entity;
    }
    public Collection<Entity> addRange(Collection<Entity> entities) {
        return (Collection<Entity>) repository.saveAll(entities);
    } //todo test this

    public Entity update(Entity entity) {
        return repository.save(entity);
    }

    public Entity deleteById(String id) {
        Entity entity = this.findById(id);
        return delete(entity);
    }

    public Entity delete(Entity entity){
        repository.delete(entity);
        return entity;
    }

    public void deleteRange(Collection<Entity> entities){
        repository.deleteAll(entities);
    }

}
