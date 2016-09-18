package com.tigercel.hearthstone.service;

import com.tigercel.hearthstone.SMS.SMSVCodeSender;
import com.tigercel.hearthstone.bean.VCode;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by somebody on 2016/8/9.
 */
public class RabbitConsumer {

    @Autowired
    private SMSVCodeSender smsvCodeSender;

    public void receiveMessage(VCode content) {
        smsvCodeSender.send(content.getTarget(), content.getCode());
    }
}
