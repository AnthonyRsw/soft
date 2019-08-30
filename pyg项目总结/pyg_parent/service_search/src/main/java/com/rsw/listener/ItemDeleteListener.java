package com.rsw.listener;

import com.rsw.service.SolrManagerService;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

public class ItemDeleteListener implements MessageListener{

    @Autowired
    private SolrManagerService solrManagerService;

    @Override
    public void onMessage(Message message) {
        ActiveMQTextMessage activeMQTextMessage= (ActiveMQTextMessage) message;
        try {
            String goodsId = activeMQTextMessage.getText();
            solrManagerService.deleteItemFromSolr(Long.parseLong(goodsId));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
