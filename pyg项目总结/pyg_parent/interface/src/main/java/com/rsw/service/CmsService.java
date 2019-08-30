package com.rsw.service;

import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.Map;

public interface CmsService {

    public Map<String, Object> findGoodsData(Long goodsId);

    public void createStaticPage(Long goodsId,Map<String,Object> rootMap) throws IOException, TemplateException;
}
