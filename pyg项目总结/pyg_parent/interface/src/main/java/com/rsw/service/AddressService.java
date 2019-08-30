package com.rsw.service;

import com.rsw.pojo.address.Address;

import java.util.List;

public interface AddressService {

    public List<Address> findListByLoginUser(String userId);
}
