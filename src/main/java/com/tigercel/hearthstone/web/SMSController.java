package com.tigercel.hearthstone.web;

import com.tigercel.hearthstone.web.support.JsonResponseCode;
import com.tigercel.hearthstone.service.SMSVCodeService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by somebody on 2016/8/9.
 */
@RestController
@RequestMapping("api/sms")
public class SMSController {

    @Autowired
    private SMSVCodeService smsvCodeService;

    @RequestMapping(value = "phone/{phone}", method = RequestMethod.GET)
    public String smsSender(@PathVariable(value = "phone") String phone, HttpServletRequest request) {
        String code;

        if(StringUtils.isEmpty(phone)) {
            return JsonResponseCode.NOT_VALID_PHONE;
        }

        code = RandomStringUtils.randomNumeric(5);
        smsvCodeService.save(request.getSession().getId(), code);
        smsvCodeService.SMSVCodeSender(phone, code);

        return "{\"meta\":{\"retCode\":200, \"retInfo\":\"Success\",\"description\":\"300\"}}";
    }
}
