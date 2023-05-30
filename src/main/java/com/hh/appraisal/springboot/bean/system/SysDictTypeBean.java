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
 * 系统字典类型表
 * @author gaigai
 * @date 2019/05/15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "系统字典类型实体")
public class SysDictTypeBean{

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
     * 字典类型名称
     */
    @ApiModelProperty(value = "字典类型名称")
    private String name;

    /**
     * 字典类型编号
     */
    @ApiModelProperty(value = "字典类型编号")
    private String num;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String notes;

    /**
     * 字典类型 code 集合
     */
    @ApiModelProperty(value = "字典类型 唯一标识 集合")
    private List<String> dictTypeCodeList;

}
