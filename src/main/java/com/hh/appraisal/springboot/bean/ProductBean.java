package com.hh.appraisal.springboot.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

import com.hh.appraisal.springboot.core.annotation.EntityDoc;

import java.io.Serializable;
import java.lang.Integer;
import java.util.Date;

/**
 * 产品管理Bean 用于控制层展示数据
 * 
 * @see com.hh.appraisal.springboot.entity.Product
 * @author gaigai
 * @date 2021/06/26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "产品管理实体")
public class ProductBean implements Serializable {

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	private String code;

	/**
	 * 数据是否有效
	 */
	@ApiModelProperty(value = "数据是否有效")
	private Integer valid;

	/**
	 * 记录创建时间
	 */
	@ApiModelProperty(value = "记录创建时间")
	private Date createTime;

	/**
	 * 记录更新时间
	 */
	@ApiModelProperty(value = "记录更新时间")
	private Date updateTime;

	/**
	 * 产品名称
	 */
	@ApiModelProperty(value = "产品名称")
	private String productName;

	/**
	 * 产品描述
	 */
	@ApiModelProperty(value = "产品描述")
	private String productDesc;

	/**
	 * 产品简介
	 */
	@ApiModelProperty(value = "产品简介")
	private String productIntro;

	/**
	 * 针对人群
	 */
	@ApiModelProperty(value = "针对人群")
	private String crowdType;

	/**
	 * 题目数量
	 */
	@ApiModelProperty(value = "题目数量")
	private int questionNum;

	/**
	 * 答题时间
	 */
	@ApiModelProperty(value = "答题时间")
	private int answerTime;

	/**
	 * 产品管理 唯一标识 集合
	 */
	@ApiModelProperty(value = "产品管理 唯一标识 集合")
	private List<String> productCodeList;

}
