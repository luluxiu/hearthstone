package com.tigercel.hearthstone.SMS;

/**
 * Created by somebody on 2016/8/8.
 */
public interface SMSVCodeSender {
    void send(String phone, String code);
}
