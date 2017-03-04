package com.pay.activemq;


import javax.jms.JMSException;
import javax.jms.Message;
 

import org.apache.activemq.ScheduledMessage;
import org.apache.commons.lang.StringUtils;

import org.springframework.jms.core.MessagePostProcessor;

 
/**
 * 
 * ClassName: ScheduleMessagePostProcessor 
 * @Description: TODO MQ延时投递处理器（注：ActiveMQ的配置文件中，要配置schedulerSupport="true"，否则不起作用）
 * @author wujintao
 * @date 2016-11-30
 */
public class ScheduleMessagePostProcessor implements MessagePostProcessor {
 
    private long delay = 0l;
 
    private String corn = null;
 
    public ScheduleMessagePostProcessor(long delay) {
        this.delay = delay;
    }
 
    public ScheduleMessagePostProcessor(String cron) {
        this.corn = cron;
    }
 
    public Message postProcessMessage(Message message) throws JMSException {
        if (delay > 0) {
            message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delay);
        }
        if (!StringUtils.isEmpty(corn)) {
            message.setStringProperty(ScheduledMessage.AMQ_SCHEDULED_CRON, corn);
        }
        return message;
    }
 
}