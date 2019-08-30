package com.rsw.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.rsw.dao.good.GoodsDao;
import com.rsw.dao.good.GoodsDescDao;
import com.rsw.dao.item.ItemCatDao;
import com.rsw.dao.item.ItemDao;
import com.rsw.pojo.good.Goods;
import com.rsw.pojo.good.GoodsDesc;
import com.rsw.pojo.item.Item;
import com.rsw.pojo.item.ItemQuery;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import javax.servlet.ServletContext;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CmsServiceImpl implements CmsService, ServletContextAware {

    @Autowired
    GoodsDao goodsDao;

    @Autowired
    GoodsDescDao goodsDescDao;

    @Autowired
    ItemCatDao itemCatDao;

    @Autowired
    ItemDao itemDao;

    @Autowired
    FreeMarkerConfig freeMarkerConfig;

    ServletContext servletContext;

    /**
     * 根据商品id获得商品数据
     *
     * @param goodsId
     * @return
     */
    @Override
    public Map<String, Object> findGoodsData(Long goodsId) {
        Map<String, Object> resultMap = new HashMap<>();
        //1.根据商品id获得商品信息
        Goods goods = goodsDao.selectByPrimaryKey(goodsId);
        //2.根据商品id获得商品详情信息
        GoodsDesc goodsDesc = goodsDescDao.selectByPrimaryKey(goodsId);
        String itemImages = goodsDesc.getItemImages();
        List<Map> imageList = JSON.parseArray(itemImages, Map.class);
        //3.根据商品id获得库存集
        ItemQuery itemQuery = new ItemQuery();
        ItemQuery.Criteria criteria = itemQuery.createCriteria();
        criteria.andGoodsIdEqualTo(goodsId);
        List<Item> itemsList = itemDao.selectByExample(itemQuery);
        //4.根据类别id获得商品分类信息
        if (goods != null) {
            Long category1Id = goods.getCategory1Id();
            Long category2Id = goods.getCategory2Id();
            Long category3Id = goods.getCategory3Id();
            String itemCat1 = itemCatDao.selectByPrimaryKey(category1Id).getName();
            String itemCat2 = itemCatDao.selectByPrimaryKey(category2Id).getName();
            String itemCat3 = itemCatDao.selectByPrimaryKey(category3Id).getName();
            resultMap.put("itemCat1", itemCat1);
            resultMap.put("itemCat2", itemCat2);
            resultMap.put("itemCat3", itemCat3);
        }
        //5. 将商品所有数据封装成Map返回
        resultMap.put("goods", goods);
        resultMap.put("goodsDesc", goodsDesc);
        resultMap.put("itemList", itemsList);
        return resultMap;
    }

    /**
     * 根据数据和商品id通过模板引擎生成商品详情
     *
     * @param goodsId
     * @param rootMap
     */
    @Override
    public void createStaticPage(Long goodsId, Map<String, Object> rootMap) throws IOException, TemplateException {
        Configuration configuration = freeMarkerConfig.getConfiguration();
        Template template = configuration.getTemplate("item.ftl");

        String path = goodsId + ".html";
        String realPath = servletContext.getRealPath(path);

        Writer writer = new OutputStreamWriter(new FileOutputStream(realPath), "utf-8");
        template.process(rootMap, writer);
        writer.close();

    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
