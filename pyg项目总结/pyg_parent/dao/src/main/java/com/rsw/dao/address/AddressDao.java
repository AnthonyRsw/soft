package com.rsw.dao.address;

import com.rsw.pojo.address.Address;
import com.rsw.pojo.address.AddressQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AddressDao {
    int countByExample(AddressQuery example);

    int deleteByExample(AddressQuery example);

    int deleteByPrimaryKey(Long id);

    int insert(Address record);

    int insertSelective(Address record);

    List<Address> selectByExample(AddressQuery example);

    Address selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Address record, @Param("example") AddressQuery example);

    int updateByExample(@Param("record") Address record, @Param("example") AddressQuery example);

    int updateByPrimaryKeySelective(Address record);

    int updateByPrimaryKey(Address record);
}