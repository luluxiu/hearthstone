package com.tigercel.hearthstone.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.tigercel.hearthstone.web.support.JsonResponseCode;
import com.tigercel.hearthstone.service.HFRouterService;
import com.tigercel.hearthstone.utils.ClutterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by somebody on 2016/8/11.
 */
@RestController
@RequestMapping("api/v1")
public class HFDeviceRouterController {

    @Autowired
    private HFRouterService hfRouterService;



    @RequestMapping(value = "account/shareDevice",
                    method = POST,
                    consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public String shareDevice(@Valid @RequestBody String body) {
        JsonNode    root    = ClutterUtils.stringToJson(body);

         /* not a valid json string */
        if(root == null) {
            return JsonResponseCode.NOT_VALID_JSON;
        }

        return hfRouterService.addDeviceShare(root);
    }


    @RequestMapping(value = "account/noShareDevice",
                    method = POST,
                    consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public String noShareDevice(@Valid @RequestBody String body) {
        JsonNode    root    = ClutterUtils.stringToJson(body);

         /* not a valid json string */
        if(root == null) {
            return JsonResponseCode.NOT_VALID_JSON;
        }

        return hfRouterService.delDeviceShare(root);
    }


    @RequestMapping(value = "account/addSharedDevice",
            method = POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String addSharedDevice(@Valid @RequestBody String body) {
        JsonNode    root    = ClutterUtils.stringToJson(body);

         /* not a valid json string */
        if(root == null) {
            return JsonResponseCode.NOT_VALID_JSON;
        }

        return hfRouterService.addSharedDevice(root);
    }


    @RequestMapping(value = "account/delSharedDevice",
            method = POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String delSharedDevice(@Valid @RequestBody String body) {
        JsonNode    root    = ClutterUtils.stringToJson(body);

         /* not a valid json string */
        if(root == null) {
            return JsonResponseCode.NOT_VALID_JSON;
        }

        return hfRouterService.delSharedDevice(root);
    }


    @RequestMapping(value = "device/getDevice",
            method = POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String getDevice(@Valid @RequestBody String body) {
        JsonNode    root    = ClutterUtils.stringToJson(body);

         /* not a valid json string */
        if(root == null) {
            return JsonResponseCode.NOT_VALID_JSON;
        }

        return hfRouterService.getDevice(root);
    }

}
