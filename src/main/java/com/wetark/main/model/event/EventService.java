package com.wetark.main.model.event;

import com.wetark.main.constant.Errors;
import com.wetark.main.exception.CustomException;
import com.wetark.main.helper.FileUploadUtil;
import com.wetark.main.helper.PageHelper;
import com.wetark.main.model.BaseService;
import com.wetark.main.model.user.User;
import com.wetark.main.payload.request.CreatePersonalEventRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.Pageable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class EventService extends BaseService<Event> {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        super(eventRepository);
        this.eventRepository = eventRepository;
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
    public List<Event> findAllNonPrivate(String page, String size) {
        return eventRepository.findByIsPrivateOrderByCreatedAtDesc(false, PageHelper.pageable(page, size));
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
        personalEvent.setTags(new ArrayList<String>(event.getTags()));
        personalEvent.setPicture(event.getPicture());
        personalEvent.setExpireAt(event.expireAt);

        personalEvent.setUser(user);
        personalEvent.setPrivate(true);
        personalEvent.setEventVariableMap(createPersonalEventRequest.getEventVariableMap());

        return eventRepository.save(personalEvent).getId();
    }
}
