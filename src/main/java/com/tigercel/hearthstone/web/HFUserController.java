package com.tigercel.hearthstone.web;

/**
 * Created by somebody on 2016/8/4.
 */
import com.fasterxml.jackson.databind.JsonNode;
import com.tigercel.hearthstone.web.support.JsonResponseCode;
import com.tigercel.hearthstone.service.HFUserMgrService;
import com.tigercel.hearthstone.utils.ClutterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by somebody on 2016/8/2.
 */
@RestController
@RequestMapping("api/v1/account")
public class HFUserController {


    @Autowired
    private HFUserMgrService hfUserMgrService;


    @RequestMapping(value = "signup",
                    method = POST,
                    consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public String signup(@Valid @RequestBody String body) {
        JsonNode    root    = ClutterUtils.stringToJson(body);

        /* not a valid json string */
        if(root == null) {
            return JsonResponseCode.NOT_VALID_JSON;
        }

        return hfUserMgrService.signup(root);
    }




    @RequestMapping(value = "edit", method = POST,
                    consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public String set(@Valid @RequestBody String body) {
        JsonNode    root    = ClutterUtils.stringToJson(body);

         /* not a valid json string */
        if(root == null) {
            return JsonResponseCode.NOT_VALID_JSON;
        }

        return hfUserMgrService.changePassword(root);
    }

    @RequestMapping(value = "smscode", method = POST,
                    consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public String smsCode(@Valid @RequestBody String body, HttpServletRequest request) {
        JsonNode    root    = ClutterUtils.stringToJson(body);

         /* not a valid json string */
        if(root == null) {
            return JsonResponseCode.NOT_VALID_JSON;
        }

        return hfUserMgrService.sendSMSCode(root, request.getSession().getId());
    }


    @RequestMapping(value = "reset", method = POST,
                    consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public String reset(@Valid @RequestBody String body, HttpServletRequest request) {
        JsonNode    root    = ClutterUtils.stringToJson(body);

         /* not a valid json string */
        if(root == null) {
            return JsonResponseCode.NOT_VALID_JSON;
        }

        return hfUserMgrService.resetPassword(root, request.getSession().getId());
    }


    @RequestMapping(value = "info", method = POST,
                    consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public String info(@Valid @RequestBody String body) {
        JsonNode    root    = ClutterUtils.stringToJson(body);

         /* not a valid json string */
        if(root == null) {
            return JsonResponseCode.NOT_VALID_JSON;
        }

        return hfUserMgrService.userInfo(root);
    }


    @RequestMapping(value = "addUserDevice", method = POST,
                    consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public String addUserDevice(@Valid @RequestBody String body) {
        JsonNode    root    = ClutterUtils.stringToJson(body);

         /* not a valid json string */
        if(root == null) {
            return JsonResponseCode.NOT_VALID_JSON;
        }


        return hfUserMgrService.addUserDevice(root);
    }


    @RequestMapping(value = "delUserDeivce", method = POST,
                    consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public String delUserDeivce(@Valid @RequestBody String body) {
        JsonNode    root    = ClutterUtils.stringToJson(body);

         /* not a valid json string */
        if(root == null) {
            return JsonResponseCode.NOT_VALID_JSON;
        }

        return hfUserMgrService.delUserDeivce(root);
    }

}
