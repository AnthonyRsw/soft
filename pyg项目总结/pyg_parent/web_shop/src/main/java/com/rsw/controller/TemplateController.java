package com.rsw.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.rsw.pojo.entity.PageResult;
import com.rsw.pojo.entity.Result;
import com.rsw.pojo.item.ItemCat;
import com.rsw.pojo.template.TypeTemplate;
import com.rsw.service.TypeTemplateService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequestMapping("/typeTemplate")
@RestController
public class TemplateController {

    @Reference
    TypeTemplateService typeTemplateService;


    @RequestMapping("/findBySpecList")
    public List<Map> findBySpecList(Long id) {
        List<Map> list= typeTemplateService.findBySpecList(id);
        return list;

    }

    @RequestMapping("/findOne")
    public TypeTemplate findOne(Long id) {
        TypeTemplate one = typeTemplateService.findOne(id);
        return one;

    }

}
