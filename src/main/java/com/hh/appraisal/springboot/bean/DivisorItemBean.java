package com.hh.appraisal.springboot.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.util.Date;

/**
 * 因子明细Bean
 * 用于控制层展示数据
 * @see com.hh.appraisal.springboot.entity.DivisorItem
 * @author gaigai
 * @date 2021/06/26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "因子明细实体")
@Excel
public class DivisorItemBean implements Serializable {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String code;

    /**
     * 数据是否有效
     */
    @ApiModelProperty(value = "数据是否有效")
    private Integer valid;

    /**
     * 记录创建时间
     */
    @ApiModelProperty(value = "记录创建时间")
    private Date createTime;

    /**
     * 记录更新时间
     */
    @ApiModelProperty(value = "记录更新时间")
    private Date updateTime;

    /**
     * 因子
     */
    @ExcelField
    @ApiModelProperty(value = "因子")
    private String divisorCode;

    /**
     * 因子分值起
     */
    @ExcelField
    @ApiModelProperty(value = "因子分值起")
    private Integer valueStart;

    /**
     * 因子分值止
     */
    @ExcelField
    @ApiModelProperty(value = "因子分值止")
    private Long valueEnd;

    /**
     * 表述中文描述
     */
    @ExcelField
    @ApiModelProperty(value = "表述中文描述")
    private String contentZh;

    /**
     * 表述英文描述
     */
    @ExcelField
    @ApiModelProperty(value = "表述英文描述")
    private String contentEn;

    /**
     * 因子明细 唯一标识 集合
     */
    @ApiModelProperty(value = "因子明细 唯一标识 集合")
    private List<String> divisorItemCodeList;

}
