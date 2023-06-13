package com.hh.appraisal.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hh.appraisal.springboot.core.annotation.EntityDoc;
import com.hh.appraisal.springboot.core.entity.BasicEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 报告
 * 
 * @author gaigai
 * @date 2023/06/03
 */
@EntityDoc(isClass = true, note = "报告")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("report")
public class Report extends BasicEntity {

	/**
	 * 报告名称
	 */
	@EntityDoc(note = "报告名称")
	private String reportName;
	
	/**
	 * 产品名称（多选）
	 */
	@EntityDoc(note = "产品名称")
	private String productCode;
	
	
	/**
	 * 报告简介
	 */
	@EntityDoc(note = "报告简介")
	private String reportInfo;
	
	/**
	 * 阅读建议
	 */
	@EntityDoc(note = "阅读建议")
	private String reportReadSuggest;
	
	/**
	 * 关于报告
	 */
	@EntityDoc(note = "关于报告")
	private String aboutReport;
	

}
