package com.wetark.main.model.event;

import com.wetark.main.model.BaseRepository;
import com.wetark.main.model.event.tag.Tag;
import com.wetark.main.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface EventRepository extends BaseRepository<Event> {
    List<Event> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
    Page<Event> findByTagsNameAndIsPrivateOrderByCreatedAtDesc(String name, Boolean isPrivate, Pageable pageable);
}
