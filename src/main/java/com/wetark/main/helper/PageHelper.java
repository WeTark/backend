package com.wetark.main.helper;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PageHelper {
    public static Pageable pageable(String page, String size){
        return PageRequest.of(Integer.parseInt(page), Integer.parseInt(size));
    }
}
