package com.tigercel.hearthstone.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by somebody on 2016/8/17.
 */
@Controller
public class HomeController {


    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String index() {
        return "index";
    }

}
