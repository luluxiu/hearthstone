package com.tigercel.hearthstone.service;

import com.tigercel.hearthstone.bean.VCode;
import com.tigercel.hearthstone.config.RabbitmqConfiguration;
import com.tigercel.hearthstone.service.support.CacheNameSettings;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Created by somebody on 2016/8/8.
 */
@Service
public class SMSVCodeService {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Cacheable(value = CacheNameSettings.CACHE_HS_VCODE, key = "(#sessionId).concat(#code)")
    public String save(String sessionId, String code) {
        return code;
    }

    @Cacheable(value = CacheNameSettings.CACHE_HS_VCODE, key = "(#sessionId).concat(#code)")
    public String search(String sessionId, String code) {
        return null;
    }

    /****************************************************************/
    @Async
    public void SMSVCodeSender(String phone, String code) {
        rabbitTemplate.convertAndSend(RabbitmqConfiguration.QUEUE_SEND_VCODE, new VCode(phone, code));
    }

}
