package com.rsw.test;

import com.rsw.dao.good.BrandDao;
import com.rsw.pojo.good.Brand;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring/applicationContext*.xml"})
public class Test {

    @Autowired
    BrandDao brandDao;

    @org.junit.Test
    public void test01(){
        Brand brand = brandDao.selectByPrimaryKey(1l);
        System.out.println(brand);
    }
}
