package com.hh.appraisal.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hh.appraisal.springboot.core.annotation.EntityDoc;
import com.hh.appraisal.springboot.core.entity.BasicEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 因子和问题关联关系
 * 
 * @author gaigai
 * @date 2021/06/29
 */
@EntityDoc(isClass = true, note = "因子与问题关联关系")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("divisorwithques")
public class DivisorWithQues extends BasicEntity{

	/**
	 * 因子code
	 */
	@EntityDoc(note = "因子")
	private String divisorCode;

	/**
	 * 问题Code
	 */
	@EntityDoc(note = "问题")
	private String questionCode;
	
	/**
	 * 备注
	 */
	@EntityDoc(note = "备注")
	private String remark;
	
	

}
