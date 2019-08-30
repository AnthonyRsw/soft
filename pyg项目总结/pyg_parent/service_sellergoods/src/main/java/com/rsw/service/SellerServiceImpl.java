package com.rsw.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.rsw.dao.seller.SellerDao;
import com.rsw.pojo.entity.PageResult;
import com.rsw.pojo.seller.Seller;
import com.rsw.pojo.seller.SellerQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SellerServiceImpl implements SellerService {

    @Autowired
    SellerDao sellerDao;

    @Override
    public void add(Seller seller) {
        seller.setCreateTime(new Date());
        //审核状态注册的时候默认为0 ,未审核
        seller.setStatus("0");
        sellerDao.insertSelective(seller);
    }

    @Override
    public Seller findOne(String id) {
        Seller seller = sellerDao.selectByPrimaryKey(id);
        return seller;
    }

    @Override
    public PageResult findPage(Seller seller, Integer page, Integer rows) {
        PageHelper.startPage(page,rows);
        SellerQuery sellerQuery=new SellerQuery();
        SellerQuery.Criteria criteria = sellerQuery.createCriteria();
        if (seller!=null){
            if (seller.getStatus() != null && !"".equals(seller.getStatus())) {
                criteria.andStatusEqualTo(seller.getStatus());
            }
            if (seller.getName()!=null && !"".equals(seller.getName())){
                criteria.andNameLike("%"+seller.getName()+"%");
            }
            if (seller.getNickName()!=null && !"".equals(seller.getNickName())){
                criteria.andNickNameLike("%"+seller.getNickName()+"%");
            }
        }
        Page<Seller> sellers = (Page<Seller>) sellerDao.selectByExample(sellerQuery);
        return new PageResult(sellers.getTotal(),sellers.getResult());

    }

    @Override
    public void updateStatus(String sellerId, String status) {
        Seller seller = new Seller();
        seller.setStatus(status);
        seller.setSellerId(sellerId);
        sellerDao.updateByPrimaryKeySelective(seller);
    }
}
