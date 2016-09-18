package com.tigercel.hearthstone.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by somebody on 2016/8/9.
 */
@Data
public class VCode implements Serializable {

    private String target;
    private String code;

    public VCode(String t, String c) {
        target = t;
        code = c;
    }
}
