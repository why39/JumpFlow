package com.hxy.modules.cas;

/**
 *
 */

import com.hxy.modules.common.utils.ShiroUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class DemoController {
    //进入系统首页方法，如果没有登录，会跳转到CAS统一登录页面，登录成功后会回调该方法。
    @RequestMapping("/")
    public String index() {
        return "index.html";
    }

    //登出
    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        ShiroUtils.logout();
        return "redirect:http://localhost:8080/cas/logout?service=http://localhost:8080/cas";
    }
}

