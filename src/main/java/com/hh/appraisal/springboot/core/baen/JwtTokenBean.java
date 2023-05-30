package com.hh.appraisal.springboot.core.baen;

import lombok.Data;

import java.util.List;

/**
 * 用于校验jwt
 * @author gaigai
 * @date 2019/05/15
 */
@Data
public class JwtTokenBean {

    /**
     * jwt规定token前需要加 Bearer
     */
    public static final String JWT_TOKEN_PREFIX = "Bearer ";

    /**
     * 请求时携带的token
     */
    private String token;

    /**
     * 用户id，用户在数据库里的id
     */
    private String id;

    /**
     * 用户权限集合
     */
    private List<String> permissions;

    /**
     * token字段是否被刷新
     */
    private Boolean isFlushed;

    /**
     * 检查 Permissions 集合中是否有参数url
     * 有说明用户拥有访问参数url的权限
     * @param url
     * @return
     */
    public boolean hasUrl(String url){
        if(permissions == null || permissions.size() == 0){
            return false;
        }
        for (String permission : permissions){
            if(url.endsWith(permission)){
                return true;
            }
        }
        return false;
    }
}
