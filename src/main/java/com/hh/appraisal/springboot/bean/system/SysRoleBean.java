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
 * 系统角色表
 * @author gaigai
 * @date 2019/05/15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "系统角色实体")
public class SysRoleBean {

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
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称")
    private String name;

    /**
     * 角色备注
     */
    @ApiModelProperty(value = "角色备注")
    private String notes;

    /**
     * 该角色名下是否有员工
     */
    @ApiModelProperty(value = "该角色名下是否有员工")
    private Boolean hasUser;

    /**
     * 角色 code 集合
     */
    @ApiModelProperty(value = "角色 唯一标识 集合")
    private List<String> roleCodeList;

}
