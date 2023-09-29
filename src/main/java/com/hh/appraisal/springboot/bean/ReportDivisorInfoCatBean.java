package com.hh.appraisal.springboot.bean;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "报告因子分数数据")
public class ReportDivisorInfoCatBean implements Serializable {


	// 因子分类
	private String divisorCat;

	// 因子名称
	private String divisorName;


	// 分值
	private int value;



	//常模
	private String normCode;

}
