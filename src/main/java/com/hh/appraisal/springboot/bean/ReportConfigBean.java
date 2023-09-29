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
 * 报告配置Bean
 * 用于控制层展示数据
 * @see com.hh.appraisal.springboot.entity.ReportConfig
 * @author gaigai
 * @date 2023/06/02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "报告配置实体")
public class ReportConfigBean implements Serializable {

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
     * 报告配置模块字段
     */
    @ApiModelProperty(value = "报告配置模块字段名")
    private String reportConfigPartColName;
    
    /**
     * 报告配置模块字段
     */
    @ApiModelProperty(value = "报告配置模块字段Code")
    private String reportConfigPartColCode;

    /**
     * 报告配置模块字段值
     */
    @ApiModelProperty(value = "报告配置模块字段值")
    private String reportConfigPartColValue;
    
    
	  /**
     * 报告配置模块字段名英文
     */
    @ApiModelProperty(value = "报告配置模块字段英文名")
    private String reportConfigPartColNameen;
    
    /**
     * 报告配置模块字段名排序
     */
    @ApiModelProperty(value = "报告配置模块字段排序")
    private long reportConfigPartColOrderno;

    /**
     * 报告Code
     */
    @ApiModelProperty(value = "报告Code")
    private String reportCode;

    /**
     * 报告配置 唯一标识 集合
     */
    @ApiModelProperty(value = "报告配置 唯一标识 集合")
    private List<String> reportConfigCodeList;

}
