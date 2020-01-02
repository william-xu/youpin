package com.hflw.vasp.admin.common.config.shiro;

import com.hflw.vasp.admin.modules.user.service.UserService;
import com.hflw.vasp.system.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Slf4j
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    @Qualifier("userService")
    private UserService userService;

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 自动生成的方法存根
        String username = (String) token.getPrincipal();
        String password = new String((char[]) token.getCredentials());
        SysUser user = userService.findByUsername(username);
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(
                user, //用户对象--数据库
                password, //前端密码
                ByteSource.Util.bytes(user.getSalt()),
                getName()  //realm name
        );
        return simpleAuthenticationInfo;
    }


    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
        // TODO 自动生成的方法存根
        //改掉null
        //查询数据库获取角色和权限信息
        //SimpleAuthorizationInfo a = new SimpleAuthorizationInfo();
//		a.setRoles(roles);
        return null;
    }

}
