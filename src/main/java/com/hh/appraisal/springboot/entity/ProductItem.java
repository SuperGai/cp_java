package com.hh.appraisal.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hh.appraisal.springboot.core.annotation.EntityDoc;
import com.hh.appraisal.springboot.core.entity.BasicEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 产品明细管理
 * 
 * @author gaigai
 * @date 2021/06/29
 */
@EntityDoc(isClass = true, note = "产品明细")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("productitem")
public class ProductItem extends BasicEntity{

	/**
	 * 产品
	 */
	@EntityDoc(note = "产品")
	private String productCode;
	

	/**
	 * 问题
	 */
	@EntityDoc(note = "问题")
	private String questionCode;

	/**
	 * 备注
	 */
	@EntityDoc(note = "备注")
	private String remark;
	
	

}
