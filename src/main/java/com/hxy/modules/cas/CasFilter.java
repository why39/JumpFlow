/*
package com.hxy.modules.cas;

import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.Ordered;
*/
/**
 14.	 * 创 建 人 ： wangsheng 创建日期：2019年11月
 15.	 *//*

@Configuration
@PropertySource("classpath:application.yml")
	public class  CasFilter{
            @Value("${cas.server-url}")
    private   String CAS_URL;
	    @Value("${cas.client-host}")
	    private   String APP_URL;

    @Bean
	    public ServletListenerRegistrationBean servletListenerRegistrationBean(){
        ServletListenerRegistrationBean  listenerRegistrationBean = new ServletListenerRegistrationBean();
        listenerRegistrationBean.setListener(new SingleSignOutHttpSessionListener());
        listenerRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
              return listenerRegistrationBean;
}
    */
/**
 33.	     * 单点登录退出
 34.	     * @return
 35.	     *//*

    @Bean
    public FilterRegistrationBean singleSignOutFilter(){
            FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new SingleSignOutFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.addInitParameter("casServerUrlPrefix", CAS_URL );
        registrationBean.setName("CAS Single Sign Out Filter");
        registrationBean.setOrder(2);
        return registrationBean;
        }
    */
/**
 48.	     * 单点登录认证
 49.	     * @return
 50.	     *//*

        @Bean
    public FilterRegistrationBean AuthenticationFilter(){
            FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new AuthenticationFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("CAS Filter");
        registrationBean.addInitParameter("casServerLoginUrl",CAS_URL);
        registrationBean.addInitParameter("serverName", APP_URL );
        registrationBean.setOrder(3);
        return registrationBean;
        }
        */
/**
 64.	     * 单点登录校验
 65.	     * @return
 66.	     *//*

        @Bean
    public FilterRegistrationBean cas20ProxyReceivingTicketValidationFilter(){
            FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new Cas20ProxyReceivingTicketValidationFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("CAS Validation Filter");
        registrationBean.addInitParameter("casServerUrlPrefix", CAS_URL );
                registrationBean.addInitParameter("serverName", APP_URL );
                registrationBean.setOrder(4);
        	        return registrationBean;
            }
	    */
/**
 80.	     * 单点登录请求包装
 81.	     * @return
 82.	     *//*

	    @Bean
    public FilterRegistrationBean httpServletRequestWrapperFilter(){
        	        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new HttpServletRequestWrapperFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("CAS HttpServletRequest Wrapper Filter");
        registrationBean.setOrder(5);
        return registrationBean;
        }
    */
/**
 94.	     * 单点登录本地用户信息
 95.	     * @return
 96.	     *//*

        @Bean
    public FilterRegistrationBean localUserInfoFilter(){
               FilterRegistrationBean registrationBean = new FilterRegistrationBean();
                registrationBean.setFilter(new LocalUserInfoFilter());
                registrationBean.addUrlPatterns("/*");
                registrationBean.setName("localUserInfoFilter");
                registrationBean.setOrder(6);
                return registrationBean;
        }

	}

*/
