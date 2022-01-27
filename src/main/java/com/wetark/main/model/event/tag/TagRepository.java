package com.wetark.main.model.event.tag;

import com.wetark.main.model.BaseRepository;
import com.wetark.main.model.event.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends BaseRepository<Tag> {
    Optional<Tag> findByNameIgnoreCase(String name);
    Page<Tag> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Tag> findByIsHighlightOrderByCreatedAtDesc(Boolean isHighlight, Pageable pageable);

}
