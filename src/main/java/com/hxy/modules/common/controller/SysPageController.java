package com.hxy.modules.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 系统页面视图
 * 
 * @author hxy
 * @date 2017年3月24日 下午11:05:27
 */
@Controller
public class SysPageController {

	@RequestMapping("{module}/{url}.html")
	public String page(@PathVariable("module") String module, @PathVariable("url") String url){
		return  "modules/"+ module + "/" + url + ".html";
	}

	@RequestMapping("/")
	public String index(){
		return "login.html";
	}
}
