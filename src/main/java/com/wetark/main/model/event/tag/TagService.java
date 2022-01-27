package com.wetark.main.model.event.tag;

import com.wetark.main.helper.PageHelper;
import com.wetark.main.model.BaseRepository;
import com.wetark.main.model.BaseService;
import com.wetark.main.model.event.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService extends BaseService<Tag> {
    @Autowired
    private TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        super(tagRepository);
    }

    public List<Tag> findAllHighlight(String page, String size) {
        return tagRepository.findByIsHighlightOrderByCreatedAtDesc(true, PageHelper.pageable(page, size)).getContent();
    }

    public List<Tag> findAllTag(String page, String size) {
        return tagRepository.findAllByOrderByCreatedAtDesc(PageHelper.pageable(page,size)).getContent();
    }

    public Tag updateTag(Tag tag) {
        Tag tagData = tagRepository.findById(tag.getId()).get();
        if(tag.getName() != null){
            tagData.setName(tag.getName());
        }
        if(tag.getHighlight() != null){
            tagData.setHighlight(tag.getHighlight());
        }
        if(tag.getImageUrl() != null){
            tagData.setImageUrl(tag.getImageUrl());
        }
        return tagRepository.save(tagData);
    }
}
