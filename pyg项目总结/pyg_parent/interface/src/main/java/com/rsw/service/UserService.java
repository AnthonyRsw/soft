package com.rsw.service;


import com.rsw.pojo.user.User;

public interface UserService {

    void sendCode(String phone);

    void add(User user);

    Boolean checkSmsCode(String phone, String smscode);
}
