package com.wetark.main.controller;

import com.wetark.main.exception.CustomException;
import com.wetark.main.model.BaseService;
import com.wetark.main.model.event.Event;
import com.wetark.main.model.event.tag.Tag;
import com.wetark.main.model.event.tag.TagService;
import com.wetark.main.model.user.role.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagController extends BaseController<Tag>{

    @Autowired
    private TagService tagService;

    public TagController(TagService tagService) {
        super(tagService);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/highlight")
    public List<Tag> findAllHighlight(String page, String size) {
        return tagService.findAllHighlight(page, size);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/all")
    public List<Tag> findAllTag(String page, String size) throws CustomException {
        return tagService.findAllTag(page, size);
    }


    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/update")
    public Tag updateTag(@RequestBody Tag tag) {
        return tagService.updateTag(tag);
    }
}
