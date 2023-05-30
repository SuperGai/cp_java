package com.hh.appraisal.springboot.core.exception.custom;

import com.hh.appraisal.springboot.core.exception.BasicException;

/**
 * 自定义异常类
 * 权限异常
 * 在登录异常、权限异常时可抛出
 * @author gaigai
 * @date 2019/05/15
 */
public class RoleException extends BasicException {
    public RoleException(String message){
        super(message);
    }
}
