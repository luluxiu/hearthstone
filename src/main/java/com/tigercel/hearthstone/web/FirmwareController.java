package com.tigercel.hearthstone.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.tigercel.hearthstone.model.HFUser;
import com.tigercel.hearthstone.service.FirmwareService;
import com.tigercel.hearthstone.service.HFUserMgrService;
import com.tigercel.hearthstone.utils.ClutterUtils;
import com.tigercel.hearthstone.web.support.JsonResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created by somebody on 2016/8/22.
 */
@RestController
@RequestMapping("api/v1/device")
public class FirmwareController {


    @Autowired
    private HFUserMgrService hfUserMgrService;

    @Autowired
    private FirmwareService firmwareService;

    @RequestMapping(value = "app/version", method = RequestMethod.POST,
                    consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public String app(@Valid @RequestBody String body, HttpServletRequest request) {
        JsonNode    root    = ClutterUtils.stringToJson(body);
        JsonNode    info;
        String      name;
        HFUser      user;

         /* not a valid json string */
        if(root == null) {
            return JsonResponseCode.NOT_VALID_JSON;
        }

        info = root.get("accountInfo");
        if(info == null) {
            return JsonResponseCode.NOT_VALID_JSON;
        }

        name = info.get("name").asText();
        if(name == null) {
            return JsonResponseCode.NOT_VALID_JSON;
        }

        user = hfUserMgrService.findByName(name);
        if(user == null) {
            return JsonResponseCode.NOT_VALID_USER;
        }


        return firmwareService.appVersion(request);
    }


    @RequestMapping(value = "router/version", method = RequestMethod.POST,
                    consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public String router(@Valid @RequestBody String body, HttpServletRequest request) {
        JsonNode root    = ClutterUtils.stringToJson(body);

         /* not a valid json string */
        if(root == null) {
            return JsonResponseCode.NOT_VALID_JSON;
        }


        return firmwareService.routerVersion(root, request);
    }
}
