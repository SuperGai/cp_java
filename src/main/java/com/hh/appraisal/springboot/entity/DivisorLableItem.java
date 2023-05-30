package com.hh.appraisal.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hh.appraisal.springboot.core.annotation.EntityDoc;
import com.hh.appraisal.springboot.core.entity.BasicEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 指标与标签关系表
 * 
 * @author gaigai
 * @date 2023/04/26
 */
@EntityDoc(isClass = true, note = "指标与标签关系表")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("divisor_lable_item")
public class DivisorLableItem extends BasicEntity {

	/**
	 * 指标
	 */
	@EntityDoc(note = "指标")
	private String divisorCode;

	/**
	 * 题目标识
	 */
	@EntityDoc(note = "标签")
	private String lableManagerCode;

}
