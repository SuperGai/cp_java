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
public class QuestionAllBean implements Serializable {

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
     * 用户答案表标识
     */
    @ExcelField("用户答案表标识")
    @ApiModelProperty(value = "用户答案表标识")
    private String userAnswerCode;
    
    /**
     * 题目序号
     */
    @ApiModelProperty(value = "题目序号")
    private int questionNo;
    
    /**
     * 题目总数
     */
    @ApiModelProperty(value = "题目总数")
    private int questionNum;
    
    /**
     * 答题时间
     */
    @ApiModelProperty(value = "答题时间")
    private int answerTime;
    
    /**
     * 问题的选项
     */
    @ApiModelProperty(value = "问题的选项")
    private List<QuestionOptionsBean> questionOptionsBean;

}
