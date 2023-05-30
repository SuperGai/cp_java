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
 * 系统功能表
 * 角色对一些功能有权限，每个功能内包含若干url接口资源
 * @author gaigai
 * @date 2019/05/15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "系统功能实体")
public class SysFunctionBean {

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
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 所属模块 从字典表取值
     */
    @ApiModelProperty(value = "所属模块")
    private String module;

    /**
     * 功能 code 集合
     */
    @ApiModelProperty(value = "功能 唯一标识 集合")
    private List<String> functionCodeList;

    /**
     * 所属模块描述
     */
    @ApiModelProperty(value = "所属模块描述")
    private String moduleDesc;

}
