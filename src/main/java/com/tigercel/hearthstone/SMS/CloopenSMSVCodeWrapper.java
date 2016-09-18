package com.tigercel.hearthstone.SMS;

import com.cloopen.rest.sdk.CCPRestSmsSDK;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by somebody on 2016/8/8.
 */
@Component
@ConfigurationProperties(prefix = "custom.sms.cloopen", ignoreUnknownFields = false)
@Data
public class CloopenSMSVCodeWrapper implements SMSVCodeSender {

    private String server;

    private String port;

    private String accountSid;

    private String accountToken;

    private String appId;

    private String template;


    public void send(String phone, String code) {
        HashMap<String, Object> result = null;

        CCPRestSmsSDK restAPI = new CCPRestSmsSDK();
        restAPI.init(server, port);
        restAPI.setAccount(accountSid, accountToken);
        restAPI.setAppId(appId);
        result = restAPI.sendTemplateSMS(phone, template, new String[]{code, "5"});

        System.out.println("SDKTestGetSubAccounts result=" + result);
        if("000000".equals(result.get("statusCode"))){
            HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
            Set<String> keySet = data.keySet();
            for(String key:keySet){
                Object object = data.get(key);
                System.out.println(key +" = "+object);
            }
        }else{
            System.out.println("error code = " + result.get("statusCode") +" error msg = "+result.get("statusMsg"));
        }
    }
}
