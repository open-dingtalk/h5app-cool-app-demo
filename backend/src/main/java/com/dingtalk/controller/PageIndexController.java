package com.dingtalk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PageIndexController {

    /**
     * 首页
     *
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        return "index";
    }

    /**
     * 公告页
     *
     * @return
     */
    @RequestMapping(value = "/Announcement", method = RequestMethod.GET)
    public String announcement() {
        return "index";
    }
}
