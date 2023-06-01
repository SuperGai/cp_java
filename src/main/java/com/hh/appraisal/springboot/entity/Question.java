package com.hh.appraisal.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hh.appraisal.springboot.core.annotation.EntityDoc;
import com.hh.appraisal.springboot.core.entity.BasicEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 问题
 * @author gaigai
 * @date 2021/06/29
 */
@EntityDoc(isClass = true, note = "问题")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("question")
public class Question extends BasicEntity {

	
//	ID	INTEGER	
//	QUESTION_BANK	INTEGER	题库标识
//	QUESTION_CODE	INTEGER	题目标识
//	QUESTION_ZH	VARCHAR	题目名称（中文）
//	QUESTION_EN	VARCHAR	题目名称（英文）
//	OWNERID	INTEGER	创建人ID
//	MODIFIERID	INTEGER	修改人ID
//	CREATIONDATE	DATETIME	创建时间
//	MODIFIEDDATE	DATETIME	修改时间
//	ISACTIVE	CHAR	是否可用
	
    /**
     * 题库标识
     */
    @EntityDoc(note = "题库标识")
    private String questionBank;

    /**
     * 题目标识
     */
    @EntityDoc(note = "题目标识")
    private String questionCode;

    /**
     * 题目名称（中文）
     */
    @EntityDoc(note = "题目名称（中文）")
    private String questionZh;

    /**
     * 题目名称（英文）
     */
    @EntityDoc(note = "题目名称（英文）")
    private String questionEh;
    
    /**
     * 因子
     */
    @EntityDoc(note = "因子")
    private String divisorCode;
    
    /**
     * 题目类型
     */
    @EntityDoc(note = "题目类型")
    private String questionType;
}
