package com.wetark.main.model.event;

import com.wetark.main.model.BaseRepository;
import com.wetark.main.model.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends BaseRepository<Event> {
    List<Event> findByIsPrivateOrderByCreatedAtDesc(Boolean isPrivate, Pageable pageable);
    List<Event> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
}
