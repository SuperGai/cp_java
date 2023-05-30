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
@EntityDoc(isClass = true, note = "题目选项表")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("question_options")
public class QuestionOptions extends BasicEntity {

	
//	QUESIONS_ID	INTEGER	题库表ID
//	NAME	CHAR	选项标识
//	VALUE_ZH	VARCHAR	选项内容（中文
//	VALUE_EN	VARCHAR	选项内容（英文）
//	DIVISORID	INTEGER	因子ID
//	OWNERID	INTEGER	创建人ID
//	MODIFIERID	INTEGER	修改人ID
//	CREATIONDATE	DATETIME	创建时间
//	MODIFIEDDATE	DATETIME	修改时间
//	ISACTIVE	CHAR	是否可用
//	DIVISORValue	number	分值
	
	
    /**
     * 题库
     */
    @EntityDoc(note = "题库Code")
    private String questionCode;

    /**
     * 选项标识
     */
    @EntityDoc(note = "选项标识")
    private String name;

    /**
     * 选项内容（中文)
     */
    @EntityDoc(note = "选项内容（中文）")
    private String valueZh;
    
    /**
     * 选项内容（英文）
     */
    @EntityDoc(note = "选项内容（英文）")
    private String valueEn;
    
    /**
     * 因子Code
     */
    @EntityDoc(note = "因子Code")
    private String  divisorCode;
    
    /**
     * 分数
     */
    @EntityDoc(note = "分数")
    private Long  divisorValue;
    
    

}
