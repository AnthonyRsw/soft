package com.itheima.dao;

import com.itheima.domain.Account;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public interface AccountDao {
    /**
     * 查询所有
     * @return
     */
    @Select("select * from account")
    public List<Account> findAll();

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Select("select * from account where id = #{id}")
    public Account findById(Integer id);

    /**
     * 保存账户
     * @param account
     */
    @Insert("insert into account values(null ,#{name},#{money})")
    public void save(Account account);

    /**
     * 更新账户
     * @param account
     */
    @Update("update account set name = #{name},money=#{money} where id = #{id}")
    public void update(Account account);

    /**
     * 删除一个账户
     * @param id
     */
    @Delete("delete from account where id = #{id}")
    public void del(Integer id);
}
