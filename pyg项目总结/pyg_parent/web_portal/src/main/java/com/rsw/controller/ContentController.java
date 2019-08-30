package com.rsw.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.rsw.pojo.ad.Content;
import com.rsw.service.ContentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/content")
@RestController
public class ContentController {

    @Reference
    ContentService contentService;

    @RequestMapping("/findByCategoryId")
    public List<Content> findByCategoryId(Long categoryId){
        return contentService.findByCategoryId(categoryId);
    }



}



