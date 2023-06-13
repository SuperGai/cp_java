package com.hh.appraisal.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hh.appraisal.springboot.core.annotation.EntityDoc;
import com.hh.appraisal.springboot.core.entity.BasicEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 因子
 * @author gaigai
 * @date 2021/06/29
 */
@EntityDoc(isClass = true, note = "因子")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("divisor")
public class Divisor extends BasicEntity {

//	
//	ID	INTEGER	
//	DIVISOR_NAME	VARCHAR	因子名称
//	DIVISOR_DESC	VARCHAR	因子描述
//	CREATIONDATE	DATETIME	创建时间
//	MODIFIEDDATE	DATETIME	修改时间
//	ISACTIVE	CHAR	是否可用
	
    /**
     * 因子名称
     */
    @EntityDoc(note = "因子名称")
    private String divisorName;

    /**
     * 因子描述
     */
    @EntityDoc(note = "因子描述")
    private String divisorDesc;
    
    /**
     * 因子大类
     */
    @EntityDoc(note = "因子大类")
    private String divisorCat;

}
