package com.hh.appraisal.springboot.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

import com.hh.appraisal.springboot.bean.system.SysUserBean;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;

import java.io.Serializable;
import java.lang.Integer;
import java.util.Date;

/**
 * 问题Bean
 * 用于控制层展示数据
 * @see com.hh.appraisal.springboot.entity.Question
 * @author gaigai
 * @date 2021/06/26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "问题实体")
@Excel("问题")
public class QuestionBean implements Serializable {

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
     * 题库标识
     */
    @ExcelField(value="产品名称",required = true)
    @ApiModelProperty(value = "产品名称")
    private String questionBank;

    /**
     * 题目标识
     */
    @ExcelField("题目标识")
    @ApiModelProperty(value = "题目标识")
    private String questionCode;

    /**
     * 题目名称（中文）
     */
    @ExcelField("题目名称（中文）")
    @ApiModelProperty(value = "题目名称（中文）")
    private String questionZh;

    /**
     * 题目名称（英文）
     */
    @ExcelField("题目名称（英文）")
    @ApiModelProperty(value = "题目名称（英文）")
    private String questionEh;
    
    /**
     * 因子标识
     */
    @ExcelField("指标")
    @ApiModelProperty(value = "指标")
    private String divisorCode;
    /**
     * 问题 唯一标识 集合
     */
    @ApiModelProperty(value = "问题 唯一标识 集合")
    private List<String> questionCodeList;

    /**
     * 填空题答案
     */
    @ExcelField("题目类型")
    @ApiModelProperty(value = "题目类型")
    private String questionType;
}
