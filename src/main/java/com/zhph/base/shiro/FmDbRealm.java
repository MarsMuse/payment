package com.zhph.base.shiro;


import com.zhph.base.application.RequestContext;
import com.zhph.base.entity.Service;
import com.zhph.base.entity.User;
import com.zhph.base.service.CommonService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.pam.UnsupportedTokenException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * Author: Zou Yao
 * Description: (数据库安全域)
 * Time: 2017/8/2 10:42
 *
**/
public class FmDbRealm extends AuthorizingRealm {

    private CommonService commonService;

    /**
     *
     * Author: zou yao
     * Description: {获取到用户身份认证认证}
     * Date: 2017/8/2 10:43
     *
    **/
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {


        String userAccount = (String)principals.getPrimaryPrincipal();
        User user = new User();
        user.setAccount(userAccount);
        Set<String> stringPerms = new HashSet<String>();
        List<Service> permServices = commonService.getPermServices(user);
        for(Service sev : permServices){
            stringPerms.add(sev.getUrl());
        }
        SimpleAuthorizationInfo authInfo = new SimpleAuthorizationInfo();
        authInfo.setStringPermissions(stringPerms);
        return authInfo;
    }


    /**
     *
     * Author: zou yao
     * Description: {获取到鉴权}
     * Date: 2017/8/2 10:43
     *
    **/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        if(!(token instanceof UsernamePasswordToken)){
            throw new UnsupportedTokenException();
        }
        User user = (User) RequestContext.getRequest().getAttribute(User.SESSION_USER_ATTR);
        if(user != null){
            if(user.getLocked() == 1){   //账号被锁定
                throw  new LockedAccountException();
            }
            return new SimpleAuthenticationInfo(user.getAccount(), user.getPassword(),
                    ByteSource.Util.bytes(Base64.decode(user.getSalt())), this.getName());
        }else{   //账号不存在
            throw new UnknownAccountException();
        }
    }
}
