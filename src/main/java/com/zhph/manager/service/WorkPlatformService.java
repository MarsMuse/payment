package com.zhph.manager.service;

import java.util.Map;

public interface WorkPlatformService {


    /**
     *
     * Author: zou yao
     * Description: {获取到密钥列表}
     * Date: 2017/8/3 16:28
     *
    **/
    Map<String , Object> getListForCipherKey(Map<String , Object> parameter);


    /**
     *
     * Author: zou yao
     * Description: {修改密钥信息}
     * Date: 2017/8/3 19:24
     *
    **/
    String updateCipherKeyInfo(Map<String , Object> parameter);
}
