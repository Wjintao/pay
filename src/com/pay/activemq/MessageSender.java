package com.pay.activemq;

import javax.jms.Destination;

import org.springframework.jms.core.JmsTemplate;

/**
 * 
 * ClassName: MessageSender 
 * @Description: TODO
 * @author wujintao
 * @date 2016-12-8
 */
public class MessageSender {

    private final JmsTemplate jmsTemplate;
    private final Destination destination;

    public MessageSender(final JmsTemplate jmsTemplate, final Destination destination) {
        this.jmsTemplate = jmsTemplate;
        this.destination = destination;
    }

    public void send(final Object text) {
        try {
            jmsTemplate.setDefaultDestination(destination);
            jmsTemplate.convertAndSend(text,new ScheduleMessagePostProcessor(10 * 60 * 1000));
            System.out.println("发送消息 : " + text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void sendObjectMessage(final Object pvuv) {
        try {
            jmsTemplate.setDefaultDestination(destination);
            jmsTemplate.convertAndSend(pvuv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

