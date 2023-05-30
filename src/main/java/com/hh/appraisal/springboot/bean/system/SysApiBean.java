package com.hh.appraisal.springboot.bean.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

/**
 * 系统接口资源
 * 存放系统所有的接口url资源
 * @author gaigai
 * @date 2019/05/15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "系统接口资源实体")
public class SysApiBean {

    /**
     * 主键 唯一标识
     */
    @ApiModelProperty(value = "主键")
    private String code;

    /**
     * 数据是否有效
     */
    private Integer valid;

    /**
     * 记录创建时间
     */
    private Date createTime;

    /**
     * 记录更新时间
     */
    private Date updateTime;

    /**
     * 接口名称
     */
    @ApiModelProperty(value = "接口名称")
    private String name;

    /**
     * 接口路径
     */
    @ApiModelProperty(value = "接口路径")
    private String url;

    /**
     * 所属模块 从字典表取值
     */
    @ApiModelProperty(value = "所属模块")
    private String module;

    /**
     * api code 集合
     */
    @ApiModelProperty(value = "api id 集合")
    private List<String> apiCodeList;

    /**
     * 所属模块描述
     */
    private String moduleDesc;
}
