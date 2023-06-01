package com.hh.appraisal.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hh.appraisal.springboot.core.annotation.EntityDoc;
import com.hh.appraisal.springboot.core.entity.BasicEntity;
import com.wuwenze.poi.annotation.ExcelField;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 问题
 * @author gaigai
 * @date 2021/06/29
 */
@EntityDoc(isClass = true, note = "测评用户答案表")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_answers")
public class UserAnswers extends BasicEntity {
//
//	ID	INTEGER	
//	EVALUATOION_USER_ID	INTEGER	测评人信息表ID
//	QUESTIONS_ID	INTEGER	题库表ID
//	QUESTIONS_OPTIONS_ID	INTEGER	题目选项表ID
//	OWNERID	INTEGER	创建人ID
//	MODIFIERID	INTEGER	修改人ID
//	CREATIONDATE	DATETIME	创建时间
//	MODIFIEDDATE	DATETIME	修改时间
//	ISACTIVE	CHAR	是否可用
//	Iscomplete	CHAR	是否完成	
	
    /**
     * 测评人信息表Code
     */
    @EntityDoc(note = "测评人信息表Code")
    private String evaluationUserCode;

    /**
     * 题库表Code
     */
    @EntityDoc(note = "题库表Code")
    private String questionCode;

    /**
     * 选项内容（中文)
     */
    @EntityDoc(note = "题目选项表Code")
    private String questionOptionsCode;
    
    /**
     * 选项内容（英文）
     */
    @EntityDoc(note = "选项内容（英文）")
    private String valueEn;
    
    /**
     * 是否完成
     */
    @EntityDoc(note = "是否完成")
    private String  isComplete;
    
    /**
     * 花费时间
     */
    @EntityDoc(note = "花费时间")
    private double spendTime;
    
    /**
     * 题目序号
     */
    @EntityDoc(note = "题目序号")
    private int questionNo;
    
    /**
     * 产品COde
     */
    @EntityDoc(note = "产品code")
    private String productCode;
    
    /**
     * 填空答案
     */
    @EntityDoc(note = "填空答案")
    private String value;

}
