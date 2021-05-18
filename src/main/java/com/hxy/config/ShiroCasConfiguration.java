package com.hxy.config;

import com.hxy.component.redis.CachingShiroSessionDao;
import com.hxy.component.shiro.MyRealm;
import com.hxy.component.shiro.UserShiroCasRealm;
import org.apache.shiro.cas.CasFilter;
import org.apache.shiro.cas.CasSubjectFactory;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
/*import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;*/
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Date 2020/4/9 19:58
 * @Created by 王弘博
 */
@Configuration
public class ShiroCasConfiguration {

    // cas server地址
    public static final String casServerUrlPrefix = "https://143.176.22.84:8080/cas";
    // Cas登录页面地址
    public static final String casLoginUrl = casServerUrlPrefix + "/login";
    // Cas登出页面地址
    public static final String casLogoutUrl = casServerUrlPrefix + "/logout";
    // 当前工程对外提供的服务地址
    public static final String shiroServerUrlPrefix = "http://143.176.22.84:8083";
    // casFilter UrlPattern
    public static final String casFilterUrlPattern = "/cas";
    // 登录地址
    public static final String loginUrl = casLoginUrl + "?service=" + shiroServerUrlPrefix + casFilterUrlPattern;
    // 登出地址（casserver启用service跳转功能，需在webapps\cas\WEB-INF\cas.properties文件中启用cas.logout.followServiceRedirects=true）
    public static final String logoutUrl = casLogoutUrl + "?service=" + shiroServerUrlPrefix;
    // 登录成功地址
    public static final String loginSuccessUrl = "/hxyActiviti/";
    // 权限认证失败跳转地址
    public static final String unauthorizedUrl = "/403.html";


    /*@Value("${jedis.pool.password}")
    private String password;*/
    /*@Value("${jedis.pool.database}")
    private int database;*/

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager defaultSecurityManager = new DefaultWebSecurityManager();
        defaultSecurityManager.setRealm(userRealm());
        //defaultSecurityManager.setCacheManager(cacheManager());
        defaultSecurityManager.setSessionManager(sessionManager());
        defaultSecurityManager.setRememberMeManager(rememberMeManager());
        return defaultSecurityManager;
    }

    @Bean
    public UserShiroCasRealm userRealm() {
        UserShiroCasRealm userShiroCasRealm = new UserShiroCasRealm();
        //userShiroCasRealm.setCacheManager(cacheManager());
        //userShiroCasRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        userShiroCasRealm.setCachingEnabled(false);
        userShiroCasRealm.setCasServerUrlPrefix(ShiroCasConfiguration.casServerUrlPrefix);
        userShiroCasRealm.setCasService(ShiroCasConfiguration.shiroServerUrlPrefix + ShiroCasConfiguration.casFilterUrlPattern);
        return userShiroCasRealm;
    }

    /**
     * 注册单点登出listener
     *
     * @return
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ServletListenerRegistrationBean singleSignOutHttpSessionListener() {
        ServletListenerRegistrationBean bean = new ServletListenerRegistrationBean();
        bean.setListener(new SingleSignOutHttpSessionListener());
        bean.setEnabled(true);
        return bean;
    }

    /**
     * 注册单点登出filter
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean singleSignOutFilter() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setName("singleSignOutFilter");
        bean.setFilter(new SingleSignOutFilter());
        bean.addUrlPatterns("/*");
        bean.setEnabled(true);
        return bean;
    }

    /**
     * 注册DelegatingFilterProxy（Shiro）
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean delegatingFilterProxy() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
        //  该值缺省为false,表示生命周期由SpringApplicationContext管理,设置为true则表示由ServletContainer管理
        filterRegistration.addInitParameter("targetFilterLifecycle", "true");
        filterRegistration.setEnabled(true);
        filterRegistration.addUrlPatterns("/*");
        return filterRegistration;
    }

    /**
     * CAS过滤器
     *
     * @return
     */
    @Bean(name = "casFilter")
    public CasFilter getCasFilter() {
        CasFilter casFilter = new CasFilter();
        casFilter.setName("casFilter");
        casFilter.setEnabled(true);
        // 登录失败后跳转的URL，也就是 Shiro 执行 CasRealm 的 doGetAuthenticationInfo 方法向CasServer验证tiket
        casFilter.setFailureUrl(loginUrl);// 我们选择认证失败后再打开登录页面
        casFilter.setSuccessUrl(loginSuccessUrl);
        return casFilter;
    }

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager, CasFilter casFilter) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        shiroFilterFactoryBean.setLoginUrl(loginUrl);
        //shiroFilterFactoryBean.setSuccessUrl(loginSuccessUrl);
        shiroFilterFactoryBean.setUnauthorizedUrl(unauthorizedUrl);

        Map<String, Filter> filters = new HashMap<>();
        filters.put("casFilter", casFilter);
        shiroFilterFactoryBean.setFilters(filters);

        //anon为不用登陆就可以访问的，authc 是必须登陆才可以访问，filterChainDefinitionMap.put("/**", "authc") 这个一定要放在最后
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        //1.shiro集成cas后，首先添加该规则
        filterChainDefinitionMap.put(casFilterUrlPattern, "casFilter");
        filterChainDefinitionMap.put("/logout", "anon");
        filterChainDefinitionMap.put("/login", "anon");
        //filterChainDefinitionMap.put("/index", "anon");
        filterChainDefinitionMap.put("/**", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 给static，不然Redis注入不进来
     *
     * @return
     */
    @Bean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions)
     * 需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)和
     * AuthorizationAttributeSourceAdvisor)即可实现此功能
     */
    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 用户注册的时候，程序将明文通过加密方式加密，存到数据库的是密文，
     * 登录时将密文取出来，再通过shiro将用户输入的密码进行加密对比，一样则成功，不一样则失败。
     *
     * @return
     */
    /*@Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法:这里使用md5算法;
        hashedCredentialsMatcher.setHashIterations(1024);//散列的次数，比如散列两次，相当于 md5( md5(""));
        return hashedCredentialsMatcher;
    }*/

    /**
     * cacheManager 缓存 redis实现
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    /*@Bean
    public RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }*/

    /**
     * 配置shiro redisManager
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
   /* @Bean
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host);
        redisManager.setPort(port);
        //redisManager.setPassword(password);
        //redisManager.setDatabase(database);
        return redisManager;
    }
*/
    /**
     * Session Manager
     * 使用的是shiro-redis开源插件
     */
    /*@Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO());
        return sessionManager;
    }*/

    /**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis
     * 使用的是shiro-redis开源插件
     */
    @Bean("sessionManager")
    public SessionManager sessionManager(){
        CachingShiroSessionDao sessionDAO = new CachingShiroSessionDao();
        sessionDAO.setPrefix("shiro-session:");
        //注意中央缓存有效时间要比本地缓存有效时间长
        sessionDAO.setSeconds(1800);
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        //设置session过期时间为1小时(单位：毫秒)，默认为30分钟
        sessionManager.setGlobalSessionTimeout(60 * 60 * 1000);
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionDAO(sessionDAO);
        return sessionManager;
    }

    /**
     * 记住我
     *
     * @return
     */
    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        //rememberme cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度（128 256 512 位），通过以下代码可以获取
        //KeyGenerator keygen = KeyGenerator.getInstance("AES");
        //SecretKey deskey = keygen.generateKey();
        //System.out.println(Base64.encodeToString(deskey.getEncoded()));
        byte[] cipherKey = Base64.decode("wGiHplamyXlVB11UXWol8g==");
        cookieRememberMeManager.setCipherKey(cipherKey);
        cookieRememberMeManager.setCookie(rememberMeCookie());
        return cookieRememberMeManager;
    }

    @Bean
    public SimpleCookie rememberMeCookie() {
        //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //如果httyOnly设置为true，则客户端不会暴露给客户端脚本代码，使用HttpOnly cookie有助于减少某些类型的跨站点脚本攻击；
        simpleCookie.setHttpOnly(true);
        //记住我cookie生效时间,默认30天 ,单位秒：60 * 60 * 24 * 30
        simpleCookie.setMaxAge(60 * 60 * 7);
        return simpleCookie;
    }
}