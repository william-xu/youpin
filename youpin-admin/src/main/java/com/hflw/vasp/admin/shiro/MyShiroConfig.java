package com.hflw.vasp.admin.shiro;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyShiroConfig {


    /**
     * 一、在认证中：
     *    1.1，将加密算法定义好后扔到 MyShiroRealm中 也就是自己定义的realm中
     *    1.2，将MyShiroRealm定义后扔到SecurityManager中。
     *    1.3，后期用到session什么的，都被SecurityManager管理
     *
     * @return
     */


    /**
     * 二、配置session（用Redis存储）
     * 2.1 需要配置session，就需要将sessionManager配置在SecurityManager中。
     * 2.2 sessionManager需要交给Redis来管理，所以定义了RedisSessionDAO
     * 2.3 RedisSessionDAO中需要配置Redis的信息，所以定义RedisManager
     *
     * @return
     */


    @Value("${spring.datasource.redis.host}")
    private String host;
    @Value("${spring.datasource.redis.port}")
    private int port;
    @Value("${spring.datasource.redis.timeout}")
    private int timeout;
    @Value("${spring.datasource.redis.password}")
    private String password;


    //-------------------------认证---------------------------
    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm());
        securityManager.setSessionManager(sessionManager());
//		 // 自定义缓存实现 使用redis
//        securityManager.setCacheManager(cacheManager());
        return securityManager;
    }

    @Bean
    public MyShiroRealmService myShiroRealm() {
        MyShiroRealmService myShiroRealm = new MyShiroRealmService();
        myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return myShiroRealm;
    }

//    @Bean("hashedCredentialsMatcher")
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        // 指定加密方式为MD5
        credentialsMatcher.setHashAlgorithmName("MD5");
        // 加密次数
        credentialsMatcher.setHashIterations(2);
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        return credentialsMatcher;
    }


//-------------------------redis-session----------------------

    //自定义sessionManager
    @Bean
    public SessionManager sessionManager() {
        MySessionManager mySessionManager = new MySessionManager();
        mySessionManager.setSessionDAO(new EnterpriseCacheSessionDAO());
        return mySessionManager;
    }
}