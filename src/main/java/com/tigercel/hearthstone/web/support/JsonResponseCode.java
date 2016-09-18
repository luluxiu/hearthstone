package com.tigercel.hearthstone.web.support;

/**
 * Created by somebody on 2016/8/4.
 */
public class JsonResponseCode {

    public static final String SIMPLE_MSG =
            "{\"meta\":{\"retCode\":200, \"retInfo\":\"Success\",\"description\":\"\"}}";

    public static final String SIMPLE_SMS_MSG =
            "{\"meta\":{\"retCode\":200, \"retInfo\":\"Success\",\"description\":\"\"}, \"data\":{\"timeout\": 5}}";




    public static final String NOT_VALID_JSON =
            "{\"meta\":{\"retCode\": 400,\"retInfo\":\"Error\",\"description\":\"Bad Request\"}}";

    public static final String NOT_VALID_BIND =
            "{\"meta\":{\"retCode\": 400,\"retInfo\":\"Error\",\"description\":\"Device has been bound\"}}";

    public static final String NOT_VALID_REBIND =
            "{\"meta\":{\"retCode\": 400,\"retInfo\":\"Error\",\"description\":\"Repeat binding\"}}";

    public static final String NOT_VALID_PHONE =
            "{\"meta\":{\"retCode\": 400,\"retInfo\":\"Error\",\"description\":\"Invalid phone\"}}";

    public static final String NOT_VALID_SMSCODE =
            "{\"meta\":{\"retCode\": 400,\"retInfo\":\"Error\",\"description\":\"Invalid sms code\"}}";


    public static final String NOT_VALID_REQUEST =
            "{\"meta\":{\"retCode\": 401,\"retInfo\":\"Error\",\"description\":\"You must login first\"}}";

    public static final String NOT_VALID_USER =
            "{\"meta\":{\"retCode\": 403,\"retInfo\":\"Error\",\"description\":\"No matched username\"}}";

    public static final String NOT_VALID_PASSWORD =
            "{\"meta\":{\"retCode\": 403,\"retInfo\":\"Error\",\"description\":\"Error password\"}}";

    public static final String NOT_VALID_TOKEN =
            "{\"meta\":{\"retCode\": 403,\"retInfo\":\"Error\",\"description\":\"Invalid user token\"}}";

    public static final String NOT_ALLOWED =
            "{\"meta\":{\"retCode\": 403,\"retInfo\":\"Error\",\"description\":\"Forbidden\"}}";



    public static final String NO_NEW_VERSION =
            "{\"meta\":{\"retCode\": 404,\"retInfo\":\"Error\",\"description\":\"No new version\"}}";
    public static final String NO_MAT_UUID =
            "{\"meta\":{\"retCode\": 404,\"retInfo\":\"Error\",\"description\":\"No matching mac\"}}";



    public static final String CONFLICT_ERROR =
            "{\"meta\":{\"retCode\": 409,\"retInfo\":\"Error\",\"description\":\"Username is already exist\"}}";

    public static final String INTERNAl_ERROR =
            "{\"meta\":{\"retCode\": 500,\"retInfo\":\"Error\",\"description\":\"Internal Server Error\"}}";


}
