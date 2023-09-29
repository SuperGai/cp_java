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
 * 报告Bean
 * 用于控制层展示数据
 * @see com.hh.appraisal.springboot.entity.Report
 * @author gaigai
 * @date 2023/06/02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "报告实体")
public class ReportBean implements Serializable {

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
     * 报告名称
     */
    @ApiModelProperty(value = "报告名称")
    private String reportName;

    /**
     * 产品名称
     */
    @ApiModelProperty(value = "产品名称")
    private String productCode;
    
    /**
     * 产品名称
     */
    @ApiModelProperty(value = "产品名称")
    private String productName;
    
    /**
     * 报告模型
     */
    @ApiModelProperty(value = "报告模型")
    private String reportType;
    
    /**
     * 产品名称
     */
    @ApiModelProperty(value = "产品名称")
    private List<String> productCodeList;
    

    /**
     * 报告简介
     */
    @ApiModelProperty(value = "报告简介")
    private String reportInfo;

    /**
     * 阅读建议
     */
    @ApiModelProperty(value = "阅读建议")
    private String reportReadSuggest;

    /**
     * 关于报告
     */
    @ApiModelProperty(value = "关于报告")
    private String aboutReport;

    /**
     * 报告 唯一标识 集合
     */
    @ApiModelProperty(value = "报告 唯一标识 集合")
    private List<String> reportCodeList;
    
    /**
     * 报告配置
     */
    @ApiModelProperty(value = "报告配置")
    private List<ReportConfigCatBean> ReportConfigCatList;

}
