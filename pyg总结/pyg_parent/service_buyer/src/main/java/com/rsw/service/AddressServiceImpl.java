package com.rsw.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.rsw.dao.address.AddressDao;
import com.rsw.pojo.address.Address;
import com.rsw.pojo.address.AddressQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {

    @Autowired
    AddressDao addressDao;

    @Override
    public List<Address> findListByLoginUser(String userId) {
        AddressQuery query=new AddressQuery();
        AddressQuery.Criteria criteria = query.createCriteria();
        criteria.andUserIdEqualTo(userId);

        List<Address> list = addressDao.selectByExample(query);
        return list;
    }
}
