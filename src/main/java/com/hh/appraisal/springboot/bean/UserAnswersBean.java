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
import java.util.Date;

/**
 * 题目选项表Bean
 * 用于控制层展示数据
 * @see com.hh.appraisal.springboot.entity.UserAnswers
 * @author gaigai
 * @date 2021/06/26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Excel("用户答题记录")
@ApiModel(description = "题目选项表实体")
public class UserAnswersBean implements Serializable {

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
     * 测评人信息表Code
     */
    @ExcelField("测评码")
    @ApiModelProperty(value = "测评人信息表Code")
    private String evaluationUserCode;
    

    /**
     * 产品表Code
     */
    @ExcelField("产品")
    @ApiModelProperty(value = "产品表Code")
    private String productCode;

    /**
     * 题库表Code
     */
    @ExcelField("题目")
    @ApiModelProperty(value = "题库表Code")
    private String questionCode;

    /**
     * 题目选项表Code
     */
    @ExcelField("题目选项")
    @ApiModelProperty(value = "题目选项表Code")
    private String questionOptionsCode;

    /**
     * 题目序号
     */
    @ExcelField("题目序号")
    @ApiModelProperty(value = "题目序号")
    private int questionNo;
    
    /**
     * 是否完成
     */
    @ExcelField("是否完成")
    @ApiModelProperty(value = "是否完成")
    private String isComplete;

    /**
     * 花费时间
     */
    @ExcelField("花费时间")
    @ApiModelProperty(value = "花费时间")
    private double spendTime;
    

    /**
     * 填空答案
     */
    @ExcelField("填空答案")
    @ApiModelProperty(value = "填空答案")
    private String value;
    
    @ApiModelProperty(value = "实际答题的产品code")
    private String productCodeReal;
      
    
    
    /**
     * 题目选项表 唯一标识 集合
     */
    @ApiModelProperty(value = "题目选项表 唯一标识 集合")
    private List<String> userAnswersCodeList;

}
