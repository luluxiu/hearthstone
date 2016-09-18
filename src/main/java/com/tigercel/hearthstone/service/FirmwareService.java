package com.tigercel.hearthstone.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tigercel.hearthstone.model.HFDevRouterGroup;
import com.tigercel.hearthstone.model.app.HFApp;
import com.tigercel.hearthstone.model.router.HFDevRouter;
import com.tigercel.hearthstone.utils.DataValidationUtil;
import com.tigercel.hearthstone.web.support.JsonResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by somebody on 2016/8/22.
 */
@Service
public class FirmwareService {

    @Autowired
    private AppService appService;

    @Autowired
    private HFRouterService hfRouterService;

    public String appVersion(HttpServletRequest request) {
        HFApp           app = appService.findFirst();
        ObjectMapper    mapper;
        ObjectNode      root;
        ObjectNode      meta;
        ObjectNode      data;
        String          url = getRequestURL(request);

        if(app == null) {
            return JsonResponseCode.NO_NEW_VERSION;
        }

        try {
            mapper = new ObjectMapper();
            root = mapper.createObjectNode();
            meta = mapper.createObjectNode();
            data = mapper.createObjectNode();

            root.set("meta", meta);     //root.put("meta", meta);
            root.set("data", data);

            meta.put("retCode", 200);
            meta.put("retInfo", "Success");
            meta.put("description", "");

            data.put("appVersion", app.getVersion());
            data.put("appName", app.getFilename());
            data.put("appMD5", app.getMd5());
            data.put("appURL", url + app.getUrl());
        }
        catch (Exception e) {
            return JsonResponseCode.INTERNAl_ERROR;
        }

        return root.toString();
    }


    public String routerVersion(JsonNode root, HttpServletRequest request) {
        JsonNode    info;
        JsonNode    list;
        int         num;
        JsonNode   devices;



        info = root.get("accountInfo");
        list = root.get("deviceList");
        if(info == null || list == null) {
            return JsonResponseCode.NOT_VALID_JSON;
        }


        num = list.get("number").asInt(0);
        devices =list.get("devices");

        if(num > 0 && devices.isArray() && devices.size() == num) {
            String mac = devices.get(0).get("mac").asText();
            if(DataValidationUtil.isMACValid(mac) == false) {
                return JsonResponseCode.NOT_VALID_JSON;
            }

            HFDevRouter router = hfRouterService.findByUdid(mac);
            if(router == null || router.getGroup() == null) {
                return JsonResponseCode.NO_MAT_UUID;
            }

            return routerVersionResponse(router.getGroup(), request);

        }
        else {
            return JsonResponseCode.NOT_VALID_JSON;
        }
    }


    public String routerVersionResponse(HFDevRouterGroup group, HttpServletRequest request) {

        ObjectMapper    mapper;
        ObjectNode      root;
        ObjectNode      meta;
        ObjectNode      data;
        String          url = getRequestURL(request);

        try {
            mapper = new ObjectMapper();
            root = mapper.createObjectNode();
            meta = mapper.createObjectNode();
            data = mapper.createObjectNode();

            root.set("meta", meta);     //root.put("meta", meta);
            root.set("data", data);

            meta.put("retCode", 200);
            meta.put("retInfo", "Success");
            meta.put("description", "");

            data.put("routerVersion", group.getFirmwareVersion());
            data.put("routerName", group.getFirmwareFileName());
            data.put("routerMD5", group.getFirmwareMd5());
            data.put("routerURL", url + group.getFirmwareUrl());
        }
        catch (Exception e) {
            return JsonResponseCode.INTERNAl_ERROR;
        }
        return root.toString();
    }


    public String getRequestURL(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        int end = 0;

        //http:// or https://
        end = url.indexOf('/', 8);

        return url.substring(0, end);
    }
}
