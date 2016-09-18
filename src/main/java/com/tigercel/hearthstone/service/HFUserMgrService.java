package com.tigercel.hearthstone.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tigercel.hearthstone.web.support.JsonResponseCode;
import com.tigercel.hearthstone.model.HFUser;
import com.tigercel.hearthstone.model.router.HFDevRouter;
import com.tigercel.hearthstone.model.support.Gender;
import com.tigercel.hearthstone.model.support.UserRole;
import com.tigercel.hearthstone.repository.HFUserRepository;
import com.tigercel.hearthstone.utils.DataValidationUtil;
import com.tigercel.hearthstone.utils.IDGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

/**
 * Created by somebody on 2016/8/8.
 */
@Service
public class HFUserMgrService {

    @Autowired
    private HFUserRepository hfUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired()
    private SMSVCodeService smsvCodeService;

    @Autowired
    private HFRouterService hfRouterService;


    private static final Logger logger = LoggerFactory.getLogger(HFUserService.class);



    //@Cacheable(value = CacheNameSettings.CACHE_HS_USER, key = "#name")
    public HFUser findByName(String name) {
        return hfUserRepository.findByName(name);
    }

    //@CacheEvict(value = CacheNameSettings.CACHE_HS_USER, allEntries = true)
    public HFUser save(HFUser user) {
        return hfUserRepository.save(user);
    }


    /**
     * 用户注册 :
     *  生成 userToken
     * @param root  请求实体 json 格式
     * @return
     */
    public String signup(JsonNode root) {
        JsonNode    account;
        JsonNode    userBaseInfo;
        JsonNode    userExtInfo;
        String      name;
        String      password;
        String      userName;
        String      gender;
        String      cell;
        String      address;
        String      vid;

        HFUser      user;

        account = root.path("accountInfo");
        userBaseInfo = root.path("userBaseInfo");
        /* no 'accountInfo' field */
        if(account == null || userBaseInfo == null) {
            return JsonResponseCode.NOT_VALID_JSON;
        }

        name = account.path("name").asText();
        password = account.path("password").asText();
        /* name or password is empty */
        if(StringUtils.isEmpty(name) || StringUtils.isEmpty(password)) {
            return JsonResponseCode.NOT_ALLOWED;
        }

        user = findByName(name);
        /* invalid 'name' */
        if(user != null) {
            return JsonResponseCode.CONFLICT_ERROR;
        }

        userName = userBaseInfo.path("userName").asText();
        gender = userBaseInfo.path("sex").asText();
        cell = userBaseInfo.path("cell").asText();
        if(StringUtils.isEmpty(userName) || StringUtils.isEmpty(gender) || StringUtils.isEmpty(cell)) {
            return JsonResponseCode.NOT_VALID_JSON;
        }

        user = new HFUser();
        user.setName(name);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(UserRole.ROLE_USER);
        user.setUserGender(Gender.valueOf(gender.toUpperCase()));
        user.setUserCell(cell);
        user.setUsername(userName);

        userExtInfo = root.path("userExtInfo");
        if(userExtInfo != null) {
            address = userExtInfo.path("address").asText();
            vid = userExtInfo.path("ID").asText();
            if(StringUtils.isEmpty(address) == false) {
                user.setUserAddr(address);
            }

            if(StringUtils.isEmpty(vid) == false) {
                user.setUserVid(vid);
            }
        }

        user.setToken(IDGenerator.getID());
        save(user);

        return signupResponse(user);
    }

    public String signupResponse(HFUser user) {

        return loginResponse(user, null);
    }


    /**
     * 修改密码
     * @param root
     * @return
     */
    public String changePassword(JsonNode root) {
        String      token;
        JsonNode    account;
        String      name;
        String      oldPassword;
        String      newPassword;
        HFUser      user;

        token = root.path("userToken").asText();
        account = root.path("accountInfo");

        if(StringUtils.isEmpty(token) || account == null) {
            return JsonResponseCode.NOT_VALID_JSON;
        }

        name = account.path("name").asText();
        oldPassword = account.path("oldPassword").asText();
        newPassword = account.path("newPassword").asText();

        if(StringUtils.isEmpty(name) || StringUtils.isEmpty(oldPassword)
                || StringUtils.isEmpty(newPassword)) {
            return JsonResponseCode.NOT_VALID_JSON;
        }

        user = findByName(name);
        if(user == null) {
            return JsonResponseCode.NOT_VALID_USER;
        }

        if(user.getToken().equals(token) == false) {
            return JsonResponseCode.NOT_VALID_TOKEN;
        }

        if(passwordEncoder.matches(oldPassword, user.getPassword()) == false) {
            return JsonResponseCode.NOT_VALID_PASSWORD;
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        save(user);

        return JsonResponseCode.SIMPLE_MSG;
    }


    /**
     * 调用短信平台 api, 发送手机验证码
     * @param root
     * @param sessionId
     * @return
     */
    public String sendSMSCode(JsonNode root, String sessionId) {
        String      smsCode;
        JsonNode    account;
        String      name;
        String      cell;
        HFUser      user;

        account = root.path("accountInfo");


        name = account.path("name").asText();
        cell = account.path("cell").asText();

        if(StringUtils.isEmpty(name) || StringUtils.isEmpty(cell)) {
            return JsonResponseCode.NOT_VALID_JSON;
        }

        user = findByName(name);
        if(user == null) {
            return JsonResponseCode.NOT_VALID_USER;
        }

        smsCode = RandomStringUtils.randomNumeric(6);
        smsvCodeService.save(sessionId, smsCode);
        smsvCodeService.SMSVCodeSender(cell, smsCode);

        return JsonResponseCode.SIMPLE_SMS_MSG;
    }


    /**
     *  重置密码
     * @param root
     * @param sessionId
     * @return
     */
    public String resetPassword(JsonNode root, String sessionId) {
        String      smsCode;
        JsonNode    account;
        String      name;
        String      newPassword;
        HFUser      user;

        smsCode = root.path("smsCode").asText();
        account = root.path("accountInfo");

        if(StringUtils.isEmpty(smsCode) || account == null) {
            if(StringUtils.isEmpty(smsCode)) {
                System.out.println("======== StringUtils.isEmpty(smsCode)");
            }

            if(account == null) {
                System.out.println("======== account == null");
            }

            return JsonResponseCode.NOT_VALID_JSON;
        }

        name = account.path("name").asText();
        newPassword = account.path("newPassword").asText();

        if(StringUtils.isEmpty(name) || StringUtils.isEmpty(newPassword)) {
            if(StringUtils.isEmpty(name)) {
                System.out.println("======== name");
            }

            if(StringUtils.isEmpty(newPassword)) {
                System.out.println("======== newPassword");
            }
            return JsonResponseCode.NOT_VALID_JSON;
        }

        if(smsvCodeService.search(sessionId, smsCode) == null) {
            return JsonResponseCode.NOT_VALID_SMSCODE;
        }

        user = findByName(name);
        if(user == null) {
            return JsonResponseCode.NOT_VALID_USER;
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        save(user);

        return JsonResponseCode.SIMPLE_MSG;
    }


    /**
     * 获取用户信息
     * @param root
     * @return
     */
    public String userInfo(JsonNode root) {
        String      userToken;
        JsonNode    account;
        String      name;
        HFUser      user;

        userToken = root.path("userToken").asText();
        account = root.path("accountInfo");

        if(StringUtils.isEmpty(userToken) || account == null) {
            return JsonResponseCode.NOT_VALID_JSON;
        }

        name = account.path("name").asText();

        if(StringUtils.isEmpty(name)) {
            return JsonResponseCode.NOT_VALID_JSON;
        }

        user = findByName(name);
        if(user == null) {
            return JsonResponseCode.NOT_VALID_USER;
        }

        if(user.getToken().equals(userToken) == false) {
            return JsonResponseCode.NOT_VALID_TOKEN;
        }

        return userInfoResponse(user);
    }


    public String userInfoResponse(HFUser user) {
        ObjectMapper    mapper;
        ObjectNode      root;
        ObjectNode      meta;
        ObjectNode      data;

        ObjectNode      accountInfo;
        ObjectNode      userBaseInfo;
        ObjectNode      deviceList;


        try {
            mapper = new ObjectMapper();
            root = mapper.createObjectNode();
            meta = mapper.createObjectNode();
            data = mapper.createObjectNode();
            accountInfo = mapper.createObjectNode();
            userBaseInfo = mapper.createObjectNode();
            deviceList = mapper.createObjectNode();

            root.set("meta", meta);
            root.set("data", data);

            meta.put("retCode", 200);
            meta.put("retInfo", "Success");
            meta.put("description", "");

            data.put("userToken", user.getToken());
            data.set("accountInfo", accountInfo);
            data.set("userBaseInfo", userBaseInfo);
            data.set("deviceList", deviceList);

            accountInfo.put("name", user.getName());

            userBaseInfo.put("userName", user.getUsername());
            userBaseInfo.put("sex", user.getUserGender().toString());
            userBaseInfo.put("address", user.getUserAddr());
            userBaseInfo.put("cell", user.getUserCell());

            deviceList.put("number", user.getRouters().size());

            ArrayNode arrayNode = mapper.createArrayNode();
            deviceList.set("devices", arrayNode);

            for(HFDevRouter router : user.getRouters()) {
                ObjectNode node = mapper.createObjectNode();
                node.put("mac", router.getUdid());
                node.put("deviceToken", router.getToken());
                arrayNode.add(node);
            }
        }
        catch (Exception e) {
            return JsonResponseCode.INTERNAl_ERROR;
        }

        return root.toString();
    }


    /**
     * 设备绑定
     * @param root
     * @return
     */
    public String addUserDevice(JsonNode root) {
        String      userToken;
        JsonNode    account;
        JsonNode    deviceList;
        JsonNode    devices;
        String      name;
        HFUser      user;
        int         num;

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

        user = findByName(name);
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

            if(DataValidationUtil.isMACValid(mac) == false) {
                return JsonResponseCode.NOT_VALID_JSON;
            }

            HFDevRouter router = hfRouterService.findByUdid(mac);
            if(router != null) {
                if(StringUtils.isEmpty(router.getOwner()) == false) {
                    if(router.getOwner().equals(user.getName())) {
                        return JsonResponseCode.NOT_VALID_REBIND;
                    }
                    else {
                        return JsonResponseCode.NOT_VALID_BIND;
                    }
                }

                if(router.getUsers().isEmpty() == false) {
                    router.getUsers().clear();
                }
            }
            else {
                router = new HFDevRouter();
                router.setUdid(mac);
            }
            router.setToken(IDGenerator.getID());
            router.getUsers().add(user);
            router.setOwner(user.getName());            // admin
            user.getRouters().add(router);
            save(user);

            return addUserDeviceResponse(user, router.getToken());
        }
        else {
            return JsonResponseCode.NOT_VALID_JSON;
        }
    }

    public String addUserDeviceResponse(HFUser user, String deviceToken) {
        ObjectMapper    mapper;
        ObjectNode      root;
        ObjectNode      meta;
        ObjectNode      data;

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

            data.put("userToken", user.getToken());
            data.put("deviceToken", deviceToken);
            //data.put("userGroup", user.getRole() == UserRole.ROLE_ADMIN ? 1 : 0);
        }
        catch (Exception e) {
            return JsonResponseCode.INTERNAl_ERROR;
        }

        return root.toString();
    }


    /**
     * 解除绑定
     * @param root
     * @return
     */
    public String delUserDeivce(JsonNode root) {
        String      userToken;
        JsonNode    account;
        JsonNode    deviceList;
        JsonNode    devices;
        String      name;
        HFUser      user;
        int         num;

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

        user = findByName(name);
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

            HFDevRouter router = hfRouterService.findByUdid(mac);
            if(router != null && StringUtils.isEmpty(router.getOwner()) == false
                    && router.getOwner().equals(user.getName())
                    && router.getToken().equals(deviceToken)) {
                router.getUsers().clear();
                router.setOwner(null);            // admin
                router.setShared(false);
                router.setToken(null);
                hfRouterService.save(router);
            }

            return JsonResponseCode.SIMPLE_MSG;
        }
        else {
            return JsonResponseCode.NOT_VALID_JSON;
        }
    }



    /*****************************************************************/

    public String loginResponse(HFUser user, HttpSession session) {
        ObjectMapper    mapper;
        ObjectNode      root;
        ObjectNode      meta;
        ObjectNode      data;

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

            data.put("userName", user.getName());
            data.put("userToken", user.getToken());
            //data.put("userGroup", user.getRole() == UserRole.ROLE_ADMIN ? 1 : 0);

            if(session != null) {
                session.setAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, user.getToken());
            }
        }
        catch (Exception e) {
            return JsonResponseCode.INTERNAl_ERROR;
        }

        return root.toString();
    }


    public String loginResponse(Authentication authentication, HttpSession session) {
        HFUser user = findByName(((UserDetails)authentication.getPrincipal()).getUsername());

        //logger.warn("============ :" + ((UserDetails)authentication.getPrincipal()).getUsername());

        if(user == null) {
            return JsonResponseCode.NOT_ALLOWED;
        }

        return loginResponse(user, session);
    }



}
