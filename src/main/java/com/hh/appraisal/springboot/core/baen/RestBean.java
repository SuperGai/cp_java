package com.hh.appraisal.springboot.core.baen;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

import com.hh.appraisal.springboot.core.constant.RestCode;

/**
 * 通用控制层返回实体类
 * @see RestCode
 * @author gaigai
 * @date 2019/05/15
 */
@Data
@ApiModel(description = "通用返回实体")
public class RestBean {

    /**
     * 错误码
     */
    @ApiModelProperty("错误码")
    private long code;

    /**
     * 错误信息
     */
    @ApiModelProperty("错误信息")
    private String message;

    /**
     * 数据
     */
    @ApiModelProperty("数据")
    private Object data;

    public static RestBean ok(){
        RestBean bean = new RestBean();
        bean.setCode(RestCode.DEFAULT_SUCCESS.getCode());
        bean.setMessage(RestCode.DEFAULT_SUCCESS.getMessage());
        return bean;
    }

    public static RestBean ok(Object data){
        RestBean bean = new RestBean();
        bean.setData(data);
        bean.setCode(RestCode.DEFAULT_SUCCESS.getCode());
        bean.setMessage(RestCode.DEFAULT_SUCCESS.getMessage());
        return bean;
    }

    public static RestBean ok(String msg){
        RestBean bean = new RestBean();
        bean.setCode(RestCode.DEFAULT_SUCCESS.getCode());
        bean.setMessage(msg);
        return bean;
    }

    public static RestBean error(){
        RestBean bean = new RestBean();
        bean.setCode(RestCode.DEFAULT_ERROR.getCode());
        bean.setMessage(RestCode.DEFAULT_ERROR.getMessage());
        return bean;
    }

    public static RestBean error(Object data){
        RestBean bean = new RestBean();
        bean.setData(data);
        bean.setCode(RestCode.DEFAULT_ERROR.getCode());
        bean.setMessage(RestCode.DEFAULT_ERROR.getMessage());
        return bean;
    }

    public static RestBean error(String msg){
        RestBean bean = new RestBean();
        bean.setCode(RestCode.DEFAULT_ERROR.getCode());
        bean.setMessage(msg);
        return bean;
    }

    public static RestBean error(RestCode code,String msg){
        RestBean bean = new RestBean();
        bean.setCode(code.getCode());
        bean.setMessage(msg);
        return bean;
    }

    public static RestBean error(RestCode code){
        RestBean bean = new RestBean();
        bean.setCode(code.getCode());
        bean.setMessage(code.getMessage());
        return bean;
    }
}
