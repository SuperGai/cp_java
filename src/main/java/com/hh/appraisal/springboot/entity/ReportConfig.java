package com.hh.appraisal.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hh.appraisal.springboot.core.annotation.EntityDoc;
import com.hh.appraisal.springboot.core.entity.BasicEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * 
 * @author gaigai
 * @date 2023/06/03
 */
@EntityDoc(isClass = true, note = "报告配置")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("report_config")
public class ReportConfig extends BasicEntity {

	/**
	 * 配置模块名称
	 */
	@EntityDoc(note = "报告配置模块名称")
	private String reportConfigPartName;
	
	/**
	 * 配置模块名称
	 */
	@EntityDoc(note = "报告配置模块Code")
	private String reportConfigPartCode;

	/**
	 * 报告配置模块字段
	 */
	@EntityDoc(note = "报告配置模块字段中文名")
	private String reportConfigPartColName;

	/**
	 * 报告配置模块字段值
	 */
	@EntityDoc(note = "报告配置模块字段值")
	private String reportConfigPartColValue;

    /**
     * 报告配置模块字段
     */
	@EntityDoc(note = "报告配置模块字段Code")
    private String reportConfigPartColCode;
	
	  /**
     * 报告配置模块字段
     */
	@EntityDoc(note = "报告配置模块字段英文名")
    private String reportConfigPartColNameen;
	
    /**
     * 报告配置模块字段名排序
     */
	@EntityDoc(note = "报告配置模块字段排序")
    private long reportConfigPartColOrderno;

	/**
	 * 报告Code
	 */
	@EntityDoc(note = "报告Code")
	private String reportCode;
	

}
