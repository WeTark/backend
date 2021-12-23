package com.wetark.main.model.event;

import com.wetark.main.constant.Errors;
import com.wetark.main.exception.CustomException;
import com.wetark.main.helper.FileUploadUtil;
import com.wetark.main.helper.PageHelper;
import com.wetark.main.model.BaseService;
import com.wetark.main.model.event.tag.Tag;
import com.wetark.main.model.event.tag.TagRepository;
import com.wetark.main.model.user.User;
import com.wetark.main.payload.request.CreatePersonalEventRequest;
import com.wetark.main.payload.request.EventRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.Pageable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService extends BaseService<Event> {
    private final EventRepository eventRepository;
    private final TagRepository tagRepository;

    public EventService(EventRepository eventRepository, TagRepository tagRepository) {
        super(eventRepository);
        this.eventRepository = eventRepository;
        this.tagRepository = tagRepository;
    }

    @Transactional
    public Event add(EventRequest entity){
        entity.setTags(entity.getTags().stream().map(tag -> tagRepository.findByNameIgnoreCase(tag.getName()).orElseGet(()->tag)).collect(Collectors.toSet()));
        return eventRepository.save(entity.createEvent());
    }

    public Event addNonPrivateEvent(Event event, MultipartFile multipartFile) throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        event.setPicture(fileName);
        event = eventRepository.save(event);

        String uploadDir = "user-photos/" + event.getId();
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        return event;
    }
    @Transactional
    public List<Event> findAllNonPrivate(String page, String size, String tagName) throws CustomException {
        return eventRepository.findByTagsNameAndIsPrivateOrderByCreatedAtDesc(tagName, false, PageHelper.pageable(page, size)).getContent();
    }

    @Transactional
    public List<Event> findAllByUser(User user, String page, String size) {
        return eventRepository.findByUserOrderByCreatedAtDesc(user, PageHelper.pageable(page, size));
    }


    public CreatePersonalEventRequest eventVariables(String eventId) throws CustomException {
        Event event = eventRepository.findById(eventId).orElseThrow(()->new CustomException(Errors.EVENT_NOT_FOUND, "400"));
        return new CreatePersonalEventRequest(event.titleWithVariable(), event.getEventVariableMap());
    }

    public String createPersonalEvent(CreatePersonalEventRequest createPersonalEventRequest, User user) throws CustomException {
        Event event = eventRepository.findById(createPersonalEventRequest.getEventId()).orElseThrow(()->new CustomException(Errors.EVENT_NOT_FOUND, "400"));
        Event personalEvent = new Event();
        personalEvent.setTitle(event.titleWithVariable());
        personalEvent.setDescription(event.getDescription());
//        personalEvent.setTags(event.getTags());
        personalEvent.setPicture(event.getPicture());
        personalEvent.setExpireAt(event.expireAt);

        personalEvent.setUser(user);
        personalEvent.setPrivate(true);
        personalEvent.setEventVariableMap(createPersonalEventRequest.getEventVariableMap());

        return eventRepository.save(personalEvent).getId();
    }

    public List<Tag> findAllTag(String page, String size) {
        return tagRepository.findAll(PageHelper.pageable(page,size)).getContent();
    }
}
