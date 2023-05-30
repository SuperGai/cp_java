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
 * 系统字典表
 * @author gaigai
 * @date 2019/05/15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "系统字典实体")
public class SysDictBean {

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
     * 字典类型 唯一标识
     */
    @ApiModelProperty(value = "字典类型唯一标识")
    private String typeCode;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 字典值
     */
    @ApiModelProperty(value = "字典值")
    private String value;

    /**
     * 自定数据
     */
    @ApiModelProperty(value = "自定数据")
    private String data;

    /**
     * 字典描述
     */
    @ApiModelProperty(value = "字典描述")
    private String notes;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Long sort;

    /**
     * 字典类型名称
     */
    @ApiModelProperty(value = "字典类型名称")
    private String typeName;

    /**
     * 字典类型编号
     */
    @ApiModelProperty(value = "字典类型编号")
    private String typeNum;

    /**
     * 字典 code 集合
     */
    @ApiModelProperty(value = "字典 唯一标识 集合")
    private List<String> dictCodeList;

    /**
     * 字典类型 code 集合
     */
    @ApiModelProperty(value = "字典类型 唯一标识 集合")
    private List<String> dictTypeCodeList;
}
