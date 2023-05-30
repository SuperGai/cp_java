package com.hh.appraisal.springboot.core.exception;

/**
 * 自定义异常基类
 * @author gaigai
 * @date 2019/05/15
 */
public class BasicException extends RuntimeException {
    public BasicException(String message){
        super(message);
    }
}
