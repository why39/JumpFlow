//package com.hxy.modules.activiti.config;
//
//import com.zhuozhengsoft.pageoffice.poserver.Server;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.web.servlet.ServletRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@ConfigurationProperties
//public class Cfg_PageOffice {
//    @Value("${pageoffice.posyspath}")
//    private String posyspath;
//    @Value("${pageoffice.popassword}")
//    private String poPassWord;
//
//    @Bean
//    public ServletRegistrationBean servletRegistrationBean() {
//        Server poserver = new Server();
//        //设置PageOffice注册成功后,license.lic文件存放的目录
//        poserver.setSysPath(posyspath);
//        ServletRegistrationBean srb = new ServletRegistrationBean(poserver,"/hxyActiviti/plib/*");
//        srb.addUrlMappings("/poserver.zz", "/posetup.exe", "/pageoffice.js", "/jquery.min.js", "/pobstyle.css", "/sealsetup.exe");
//        return srb;
//    }
//
//    public void setPosyspath(String posyspath) {
//        this.posyspath = posyspath;
//    }
//}
