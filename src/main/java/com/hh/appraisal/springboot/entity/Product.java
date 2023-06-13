package com.hh.appraisal.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hh.appraisal.springboot.core.annotation.EntityDoc;
import com.hh.appraisal.springboot.core.entity.BasicEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 产品
 * 
 * @author gaigai
 * @date 2021/06/29
 */
@EntityDoc(isClass = true, note = "产品管理")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("product")
public class Product extends BasicEntity {

	/**
	 * 产品名称
	 */
	@EntityDoc(note = "产品名称")
	private String productName;

	/**
	 * 产品描述
	 */
	@EntityDoc(note = "产品描述")
	private String productDesc;

	/**
	 * 产品简介
	 */
	@EntityDoc(note = "产品简介")
	private String productIntro;

	/**
	 * 针对人群
	 */
	@EntityDoc(note = "针对人群")
	private String crowdType;

	/**
	 * 题目数量
	 */
	@EntityDoc(note = "题目数量")
	private int questionNum;

	/**
	 * 答题时间
	 */
	@EntityDoc(note = "答题时间")
	private int answerTime;
	
	// add my gaigai 2023-04-26 
	/**
	 * 小程序的答题指导语
	 */
	@EntityDoc(note = "小程序的答题指导语")
	private String instruction;
	
	/**
	 * 小程序的答题指导语（英文）
	 */
	@EntityDoc(note = "小程序的答题指导语（英文）")
	private String instructionEn;
	
	/**
	 * 产品答题的背景图
	 */
	@EntityDoc(note = "产品答题的背景图")
	private String backgroundImage;
	
	/**
	 * 对应常模
	 */
	@EntityDoc(note = "对应常模")
	private String normId;
	
	/**
	 * 报告模板类型
	 */
	@EntityDoc(note = "报告模板类型")
	private String modelType;
	
	/**
	 * 报告导语
	 */
	@EntityDoc(note = "报告导语")
	private String reportIntroduce;
	
	/**
	 * 阅读建议(分数说明)
	 */
	@EntityDoc(note = "阅读建议(分数说明)")
	private String readSuggest;
	
	/**
	 * 关于报告
	 */
	@EntityDoc(note = "关于报告")
	private String aboutReport;
	

}
