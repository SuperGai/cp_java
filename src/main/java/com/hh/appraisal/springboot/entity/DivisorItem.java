package com.hh.appraisal.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hh.appraisal.springboot.core.annotation.EntityDoc;
import com.hh.appraisal.springboot.core.entity.BasicEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 因子明细
 * @author gaigai
 * @date 2021/06/29
 */
@EntityDoc(isClass = true, note = "因子明细")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("divisor_item")
public class DivisorItem extends BasicEntity {

//	DIVISOR_ID	INTEGER	因子表ID
//	VALUE_STRAR	INTEGER	因子起
//	VALUE_END	INTEGER	因子止
//	CONTENT_ZH	VARCHAR2	中文描述
//	CONTENT_EN	VARCHAR2	英文描述
//	CREATIONDATE	DATETIME	创建时间
//	MODIFIEDDATE	DATETIME	修改时间
//	ISACTIVE	CHAR	是否可用
	
    /**
     * 因子
     */
    @EntityDoc(note = "因子")
    private String divisorCode;

    /**
     * 因子分值起
     */
    @EntityDoc(note = "因子分值起")
    private Integer valueStart;
    
    /**
     * 因子分值止
     */
    @EntityDoc(note = "因子分值止")
    private Long valueEnd;
    
    /**
     * 表述中文描述
     */
    @EntityDoc(note = "表述中文描述")
    private String contentZh;
    
    /**
     * 表述英文描述
     */
    @EntityDoc(note = "表述英文描述")
    private String contentEn;
    

}
