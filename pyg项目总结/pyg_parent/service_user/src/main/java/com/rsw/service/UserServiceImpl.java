package com.rsw.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.rsw.dao.user.UserDao;
import com.rsw.pojo.user.User;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ActiveMQQueue smsDestination;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${template_code}")
    private String template_code;

    @Value("${sign_name}")
    private String sign_name;

    @Override
    public void sendCode(final String phone) {
        StringBuffer phoneCode =new StringBuffer();
        for (int i = 0; i < 7; i++) {
            int s  = new Random().nextInt(10);
            phoneCode.append(s);
        }
        redisTemplate.boundValueOps(phone).set(phoneCode,60*10, TimeUnit.MINUTES);
        final String smsCode=phoneCode.toString();
        //3. 将手机号, 短信内容, 模板编号, 签名封装成map消息发送给消息服务器
        jmsTemplate.send(smsDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage message = session.createMapMessage();
                message.setString("mobile", phone);//手机号
                message.setString("template_code", template_code);//模板编码
                message.setString("sign_name", sign_name);//签名
                Map map=new HashMap();
                map.put("code", smsCode);	//验证码
                message.setString("param", JSON.toJSONString(map));
                return (Message) message;
            }
        });


    }

    @Override
    public void add(User user) {
        userDao.insertSelective(user);
    }

    @Override
    public Boolean checkSmsCode(String phone, String smscode) {
        if (phone == null || smscode == null || "".equals(phone) || "".equals(smscode)) {
            return false;
        }

        String code = (String) redisTemplate.boundValueOps(phone).get();
        if (code.equals(smscode)){
            return true;
        }
            return false;

    }
}
