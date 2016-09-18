package com.tigercel.hearthstone.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tigercel.hearthstone.web.support.JsonResponseCode;
import com.tigercel.hearthstone.model.HFUser;
import com.tigercel.hearthstone.model.router.HFDevRouter;
import com.tigercel.hearthstone.repository.HFRouterRepository;
import com.tigercel.hearthstone.utils.DataValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by somebody on 2016/8/10.
 */
@Service
public class HFRouterService {

    @Autowired
    private HFRouterRepository hfRouterRepository;


    @Autowired
    private HFUserMgrService hfUserMgrService;


    public HFDevRouter findByUdid(String mac) {
        return hfRouterRepository.findByUdid(mac);
    }

    public HFDevRouter save(HFDevRouter router) {
        return hfRouterRepository.save(router);
    }


    public String addDeviceShare(JsonNode root) {
        String      userToken;
        JsonNode    account;
        JsonNode    deviceList;
        JsonNode    devices;
        String      name;
        int         num;

        HFUser      user;


        userToken = root.get("userToken").asText();
        account = root.get("accountInfo");
        deviceList = root.get("deviceList");

        if(StringUtils.isEmpty(userToken) || account == null || deviceList == null) {
            return JsonResponseCode.NOT_VALID_JSON;
        }

        name = account.get("name").asText();
        if(StringUtils.isEmpty(name)) {
            return JsonResponseCode.NOT_VALID_JSON;
        }


        user = hfUserMgrService.findByName(name);
        if(user == null) {
            return JsonResponseCode.NOT_VALID_USER;
        }
        if(user.getToken().equals(userToken) == false) {
            return JsonResponseCode.NOT_VALID_TOKEN;
        }

        num = deviceList.get("number").asInt();
        devices = deviceList.get("devices");
        if(num == 1 && devices.isArray() && devices.size() == 1) {
            String mac = devices.get(0).get("mac").asText();
            String deviceToken = devices.get(0).get("deviceToken").asText();

            if(DataValidationUtil.isMACValid(mac) == false || StringUtils.isEmpty(deviceToken) ) {
                return JsonResponseCode.NOT_VALID_JSON;
            }
            HFDevRouter router = findByUdid(mac);
            if(router != null && StringUtils.isEmpty(router.getOwner()) == false
                    && router.getOwner().equals(user.getName())
                    && router.getToken().equals(deviceToken)) {

                router.setShared(true);
                save(router);
            }

            return JsonResponseCode.SIMPLE_MSG;
        }
        else {
            return JsonResponseCode.NOT_VALID_JSON;
        }
    }


    public String delDeviceShare(JsonNode root) {
        String      userToken;
        JsonNode    account;
        JsonNode    deviceList;
        JsonNode    devices;
        String      name;
        int         num;

        HFUser      user;


        userToken = root.get("userToken").asText();
        account = root.get("accountInfo");
        deviceList = root.get("deviceList");

        if(StringUtils.isEmpty(userToken) || account == null || deviceList == null) {
            return JsonResponseCode.NOT_VALID_JSON;
        }

        name = account.get("name").asText();
        if(StringUtils.isEmpty(name)) {
            return JsonResponseCode.NOT_VALID_JSON;
        }


        user = hfUserMgrService.findByName(name);
        if(user == null) {
            return JsonResponseCode.NOT_VALID_USER;
        }
        if(user.getToken().equals(userToken) == false) {
            return JsonResponseCode.NOT_VALID_TOKEN;
        }

        num = deviceList.get("number").asInt();
        devices = deviceList.get("devices");
        if(num == 1 && devices.isArray() && devices.size() == 1) {
            String mac = devices.get(0).get("mac").asText();
            String deviceToken = devices.get(0).get("deviceToken").asText();

            if(DataValidationUtil.isMACValid(mac) == false || StringUtils.isEmpty(deviceToken) ) {
                return JsonResponseCode.NOT_VALID_JSON;
            }
            HFDevRouter router = findByUdid(mac);
            if(router != null && StringUtils.isEmpty(router.getOwner()) == false
                    && router.getOwner().equals(user.getName())
                    && router.getToken().equals(deviceToken)) {

                for(HFUser u : router.getUsers()) {
                    if(router.getOwner().equals(u.getName())) {
                        continue;
                    }
                    router.getUsers().remove(u);
                }

                router.setShared(false);
                save(router);
            }

            return JsonResponseCode.SIMPLE_MSG;
        }
        else {
            return JsonResponseCode.NOT_VALID_JSON;
        }
    }




    public String addSharedDevice(JsonNode root) {
        String      userToken;
        JsonNode    account;
        JsonNode    deviceList;
        JsonNode    devices;
        String      name;
        int         num;

        HFUser      user;


        userToken = root.get("userToken").asText();
        account = root.get("accountInfo");
        deviceList = root.get("deviceList");

        if(StringUtils.isEmpty(userToken) || account == null || deviceList == null) {
            return JsonResponseCode.NOT_VALID_JSON;
        }

        name = account.get("name").asText();
        if(StringUtils.isEmpty(name)) {
            return JsonResponseCode.NOT_VALID_JSON;
        }


        user = hfUserMgrService.findByName(name);
        if(user == null) {
            return JsonResponseCode.NOT_VALID_USER;
        }
        if(user.getToken().equals(userToken) == false) {
            return JsonResponseCode.NOT_VALID_TOKEN;
        }

        num = deviceList.get("number").asInt();
        devices = deviceList.get("devices");
        if(num == 1 && devices.isArray() && devices.size() == 1) {
            String mac = devices.get(0).get("mac").asText();
            String deviceToken = devices.get(0).get("deviceToken").asText();

            if(DataValidationUtil.isMACValid(mac) == false || StringUtils.isEmpty(deviceToken) ) {
                return JsonResponseCode.NOT_VALID_JSON;
            }
            HFDevRouter router = findByUdid(mac);
            if(router != null && StringUtils.isEmpty(router.getOwner()) == false
                    && router.getOwner().equals(name) == false
                    && router.getToken().equals(deviceToken)
                    && router.getShared()) {

                for(HFUser u :router.getUsers()) {
                    if(u.getName().equals(name)) {
                        return JsonResponseCode.NOT_VALID_JSON;
                    }
                }
                router.getUsers().add(user);
                save(router);
            }

            return JsonResponseCode.SIMPLE_MSG;
        }
        else {
            return JsonResponseCode.NOT_VALID_JSON;
        }
    }



    public String delSharedDevice(JsonNode root) {
        String      userToken;
        JsonNode    account;
        JsonNode    deviceList;
        JsonNode    devices;
        String      name;
        int         num;

        HFUser      user;


        userToken = root.get("userToken").asText();
        account = root.get("accountInfo");
        deviceList = root.get("deviceList");

        if(StringUtils.isEmpty(userToken) || account == null || deviceList == null) {
            return JsonResponseCode.NOT_VALID_JSON;
        }

        name = account.get("name").asText();
        if(StringUtils.isEmpty(name)) {
            return JsonResponseCode.NOT_VALID_JSON;
        }


        user = hfUserMgrService.findByName(name);
        if(user == null) {
            return JsonResponseCode.NOT_VALID_USER;
        }
        if(user.getToken().equals(userToken) == false) {
            return JsonResponseCode.NOT_VALID_TOKEN;
        }

        num = deviceList.get("number").asInt();
        devices = deviceList.get("devices");
        if(num == 1 && devices.isArray() && devices.size() == 1) {
            String mac = devices.get(0).get("mac").asText();
            String deviceToken = devices.get(0).get("deviceToken").asText();

            if(DataValidationUtil.isMACValid(mac) == false || StringUtils.isEmpty(deviceToken) ) {
                return JsonResponseCode.NOT_VALID_JSON;
            }
            HFDevRouter router = findByUdid(mac);
            if(router != null && StringUtils.isEmpty(router.getOwner()) == false
                    && router.getOwner().equals(name) == false
                    && router.getToken().equals(deviceToken)
                    && router.getShared()) {

                for(HFUser u :router.getUsers()) {
                    if(u.getName().equals(name)) {
                        router.getUsers().remove(u);
                        save(router);
                        break;
                    }
                }
            }

            return JsonResponseCode.SIMPLE_MSG;
        }
        else {
            return JsonResponseCode.NOT_VALID_JSON;
        }
    }


    public String getDevice(JsonNode root) {

        String      userToken;
        JsonNode    account;
        JsonNode    deviceList;
        JsonNode    devices;
        String      name;

        ObjectMapper    mapper;
        ObjectNode      roo;
        ObjectNode      meta;
        ObjectNode      data;
        ObjectNode      on;
        ArrayNode       nodes;
        //int         num;

        HFUser      user;


        userToken = root.get("userToken").asText();
        account = root.get("accountInfo");
        deviceList = root.get("deviceList");

        if(StringUtils.isEmpty(userToken) || account == null || deviceList == null) {
            return JsonResponseCode.NOT_VALID_JSON;
        }

        name = account.get("name").asText();
        if(StringUtils.isEmpty(name)) {
            return JsonResponseCode.NOT_VALID_JSON;
        }


        user = hfUserMgrService.findByName(name);
        if(user == null) {
            return JsonResponseCode.NOT_VALID_USER;
        }
        if(user.getToken().equals(userToken) == false) {
            return JsonResponseCode.NOT_VALID_TOKEN;
        }

        //num = deviceList.get("number").asInt();
        devices = deviceList.get("devices");
        if(devices.isArray()) {

            try {
                mapper = new ObjectMapper();
                roo = mapper.createObjectNode();
                meta = mapper.createObjectNode();
                data = mapper.createObjectNode();
                on = mapper.createObjectNode();
                nodes = mapper.createArrayNode();

                roo.set("meta", meta);
                roo.set("data", data);

                meta.put("retCode", 200);
                meta.put("retInfo", "Success");
                meta.put("description", "");

                data.set("deviceList", on);

                for(JsonNode node : devices) {
                    ObjectNode tmp = mapper.createObjectNode();
                    ArrayNode sharedUsers = mapper.createArrayNode();
                    String mac = node.get("mac").asText();

                    if(DataValidationUtil.isMACValid(mac) == false) {
                        continue;
                    }
                    HFDevRouter router = findByUdid(mac);
                    if(router == null) {
                        continue;
                    }

                    for(HFUser u : router.getUsers()) {
                        if(u.getName().equals(name)) {  //用户是否在设备的用户列表内
                            tmp.put("mac", mac);

                            if(router.getStatus() != null &&
                                    StringUtils.isEmpty(router.getStatus().getVersion()) == false) {
                                tmp.put("version", router.getStatus().getVersion());
                            }
                            tmp.put("binding_user", router.getOwner());

                            for(HFUser ur : router.getUsers()) {
                                if(ur.getName().equals(router.getOwner()) == false) {
                                    sharedUsers.add(ur.getName());
                                }
                            }
                            tmp.set("shared_users", sharedUsers);
                            nodes.add(tmp);
                            break;
                        }
                    }
                }

                on.put("number", nodes.size());
                on.set("devices", nodes);

            }
            catch (Exception e) {
                return JsonResponseCode.INTERNAl_ERROR;
            }

            return roo.toString();
        }
        else {
            return JsonResponseCode.NOT_VALID_JSON;
        }
    }
}
