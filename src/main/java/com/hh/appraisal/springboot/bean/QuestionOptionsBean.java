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
 * 题目选项表Bean
 * 用于控制层展示数据
 * @see com.hh.appraisal.springboot.entity.QuestionOptions
 * @author gaigai
 * @date 2021/06/26
 */
@Data
@Excel
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "题目选项表实体")
public class QuestionOptionsBean implements Serializable {

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
     * 题目Code
     */
    @ExcelField
    @ApiModelProperty(value = "题目Code")
    private String questionCode;

    /**
     * 选项标识
     */
    @ExcelField
    @ApiModelProperty(value = "选项标识")
    private String name;

    /**
     * 选项内容（中文）
     */
    @ExcelField
    @ApiModelProperty(value = "选项内容（中文）")
    private String valueZh;

    /**
     * 选项内容（英文）
     */
    @ExcelField
    @ApiModelProperty(value = "选项内容（英文）")
    private String valueEn;

    /**
     * 分数
     */
    @ExcelField
    @ApiModelProperty(value = "分数")
    private Long divisorValue;

    /**
     * 题目选项表 唯一标识 集合
     */
    @ApiModelProperty(value = "题目选项表 唯一标识 集合")
    private List<String> questionOptionsCodeList;

}
