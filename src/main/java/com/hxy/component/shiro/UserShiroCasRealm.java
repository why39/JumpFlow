package com.hxy.component.shiro;



import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

/**
 * @Description UserShiroCasRealm
 * @Date 2020/4/9 19:59
 * @Created by 王弘博
 */
public class UserShiroCasRealm extends CasRealm {

    /**
     * 权限授权
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        //表明当前登录者的角色(真实项目中这里会去查询DB，拿到用户的角色，存到redis里)
        info.addRole("admin");

        //表明当前登录者的角色(真实项目中这里会去查询DB，拿到该角色的资源权限，存到redis里)
        info.addStringPermission("admin:manage");

        return info;
    }

    /**
     * 登录认证
     *
     * @param authenticationToken
     * @return
     */
    /*@Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {
        *//*UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        //通过token去查询DB，获取用户的密码，这里密码直接写死
        User user = new User();
        user.setUsername(token.getUsername());
        return new SimpleAuthenticationInfo(user, "26bfdfe8689183e9235b1f0beb7a6f46",
                ByteSource.Util.bytes(user.getUsername()), getName());*//*
    }*/

    /**
     * 密码(123456) + salt(maple)，得出存进数据库里的密码：26bfdfe8689183e9235b1f0beb7a6f46
     *
     * @param args
     */


}
