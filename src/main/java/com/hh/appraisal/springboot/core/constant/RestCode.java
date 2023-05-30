package com.hh.appraisal.springboot.core.constant;

/**
 * 通用控制层返回状态码
 * @author gaigai
 * @date 2019/05/15
 */
public enum RestCode {

    // 权限有关错误
    ROLE_ERROR(4001,"没有权限"),
    ACCOUNT_PASS_ERROR(4002,"账号或密码错误"),
    TOKEN_ERROR(4003,"token错误"),
    EXPIRE_TOKEN(4004,"token过期"),
    EXPIRE_VCODE(4005,"验证码过期"),
    DISABLE_USER(4006, "该用户未启用"),

    // 默认
    DEFAULT_SUCCESS(2000, "操作成功"),// 请求成功
    DEFAULT_ERROR(5000, "操作失败"),//默认的错误码
    UNKNOWN_ERROR(5001, "操作失败，未知错误"),//未知错误
    DEFAULT_PARAMS_ERROR(5002, "参数不合法");//默认的参数错误返回码

    private long code;
    private String message;

    RestCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
