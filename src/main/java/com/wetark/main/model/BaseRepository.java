package com.wetark.main.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

@Component
public interface BaseRepository<Entity extends Base> extends CrudRepository<Entity, String >, PagingAndSortingRepository<Entity, String > {
}
