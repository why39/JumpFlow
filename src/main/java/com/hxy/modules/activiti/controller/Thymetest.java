package com.hxy.modules.activiti.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("the")
public class Thymetest {
    @RequestMapping(value = "greeting")
    public ModelAndView test(ModelAndView mv) {
        mv.setViewName("html/greeting");
        mv.addObject("title","欢迎使用Thymeleaf!");
        return mv;
    }
}
