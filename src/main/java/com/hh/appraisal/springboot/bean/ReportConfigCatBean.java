package com.hh.appraisal.springboot.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.io.Serializable;
import java.lang.Integer;
import java.util.Date;

/**
 * 报表配置分类类
 * @author gaigai
 * @date 2023/06/03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "报表配置分类类")
public class ReportConfigCatBean implements Serializable {

	 /**
     * 报告配置模块名称
     */
    @ApiModelProperty(value = "报告配置模块名称")
    private String reportConfigPartName;
    
	 /**
     * 报告配置模块名称
     */
    @ApiModelProperty(value = "报告配置模块Code")
    private String reportConfigPartCode;
    
    /**
     * 报告配置模块名称
     */
    @ApiModelProperty(value = "报告配置")
    private List<ReportConfigBean> reportConfig;

}
