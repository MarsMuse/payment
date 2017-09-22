package com.zhph.base.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

public class Test {

    public static void main(String[] args) {
        Factory<SecurityManager> factory = new Factory<SecurityManager>() {
            @Override
            public SecurityManager getInstance() {
                return new DefaultSecurityManager();
            }
        };
        SecurityManager sm = factory.getInstance();
        SecurityUtils.setSecurityManager(sm);

        Subject subject = SecurityUtils.getSubject();
    }

}
