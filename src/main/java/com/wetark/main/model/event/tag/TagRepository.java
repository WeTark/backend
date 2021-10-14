package com.wetark.main.model.event.tag;

import com.wetark.main.model.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends BaseRepository<Tag> {
    Optional<Tag> findByNameIgnoreCase(String name);
}
