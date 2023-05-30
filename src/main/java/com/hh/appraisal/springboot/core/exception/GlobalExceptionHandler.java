package com.hh.appraisal.springboot.core.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.core.constant.RestCode;
import com.hh.appraisal.springboot.core.exception.custom.RoleException;
import com.hh.appraisal.springboot.core.utils.ExceptionUtil;

/**
 * 自定义的异常处理
 * @author gaigai
 * @date 2019/05/15
 */
@Slf4j
@ControllerAdvice
@Component
public class GlobalExceptionHandler {

    /**
     * 拦截自定义的 RoleException 异常
     * 一般在登录时出现此异常
     * @param e
     * @return
     */
    @ExceptionHandler(RoleException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public RestBean roleException(RoleException e) {
        return RestBean.error(RestCode.ROLE_ERROR,e.getCause().getMessage());
    }

    /**
     * 拦截自定义的基类异常
     * @param e
     * @return
     */
    @ExceptionHandler(BasicException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public RestBean customerException(BasicException e) {
        log.error("捕获异常：" + ExceptionUtil.getDetailExceptionMsg(e));
        return RestBean.error(RestCode.DEFAULT_ERROR,e.getMessage());
    }

    /**
     * 拦截 Exception 异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public RestBean exception(Exception e) {
        log.error("捕获异常：" + ExceptionUtil.getDetailExceptionMsg(e));
        return RestBean.error(RestCode.UNKNOWN_ERROR,"捕获异常：" + e.getMessage());
    }
}
