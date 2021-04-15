package com.hxy.modules.cas;

import com.hxy.modules.common.utils.RedisUtil;
import com.hxy.modules.common.utils.ShiroUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

/**
8.	 * 创 建 人 ： wangsheng 创建日期：2019年11月
9.	 */
public class LocalUserInfoFilter implements Filter {
Logger logger =  LoggerFactory.getLogger(LocalUserInfoFilter.class);
@Autowired
private RedisUtil redisUtil;
@Autowired
private SessionIdGenerator generate;

@Override
public void init(FilterConfig filterConfig) throws ServletException {
}
@Override
@Bean
public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request_ = (HttpServletRequest)request;
		String loginName = CasutilLogin.getAccountNameFromCas(request_);
		if(StringUtils.isNotEmpty(loginName)){
				logger.info("访问者 ：" +loginName);
				request_.getSession().setAttribute("loginName", loginName);
				Subject subject = ShiroUtils.getSubject();
				UsernamePasswordToken token = new UsernamePasswordToken(loginName, "");
				token.setRememberMe(false);
				subject.login(token);
			}
		chain.doFilter(request, response);
		}
		@Override
public void destroy() {

		}
}

