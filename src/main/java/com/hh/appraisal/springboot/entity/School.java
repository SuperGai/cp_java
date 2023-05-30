package com.hh.appraisal.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hh.appraisal.springboot.core.annotation.EntityDoc;
import com.hh.appraisal.springboot.core.entity.BasicEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 学校
 * @author gaigai
 * @date 2021/06/29
 */
@EntityDoc(isClass = true, note = "学校")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("school")
public class School extends BasicEntity {

//	ID	INTEGER	
//	CODE	VARCHAR	学校短码
//	NAME	VARCHAR	学校名称
//	INTRO	VARCHAR	学校简介
//	LOGO	VARCHAR	学校LOGO
//	OWNERID	INTEGER	创建人ID
//	MODIFIERID	INTEGER	修改人ID
//	CREATIONDATE	DATETIME	创建时间
//	MODIFIEDDATE	DATETIME	修改时间
//	ISACTIVE	CHAR	是否可用
	
    /**
     * 学校简码
     */
    @EntityDoc(note = "学校简码")
    private String schoolCode;

    /**
     * 学校名称
     */
    @EntityDoc(note = "学校名称")
    private String schoolName;
    
    /**
     * 学校简介
     */
    @EntityDoc(note = "学校简介")
    private String intro;
    
    /**
     * 学校LOGO
     */
    @EntityDoc(note = "学校LOGO")
    private String logo;
    

}
