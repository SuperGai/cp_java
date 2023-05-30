package com.hh.appraisal.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hh.appraisal.springboot.core.annotation.EntityDoc;
import com.hh.appraisal.springboot.core.entity.BasicEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 常模管理明细
 * 
 * @author gaigai
 * @date 2023/04/26
 */
@EntityDoc(isClass = true, note = "常模管理明细")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("norm_manage_item")
public class NormManageItem extends BasicEntity{

	/**
	 * 常模主表
	 */
	@EntityDoc(note = "常模主表")
	private String normCode;

	/**
	 * 等级
	 */
	@EntityDoc(note = "等级")
	private String level;
	
	/**
	 * 等级
	 */
	@EntityDoc(note = "分值")
	private String scores;
}
