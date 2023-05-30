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
 * 报表类
 * @author gaigai
 * @date 2021/06/26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "报表类")
public class ReportBean implements Serializable {

    /**
     * 参加测评人数
     */
    @ApiModelProperty(value = "参加测评人数")
    private int joinNum;
    
    /**
     * 已完成人数
     */
    @ApiModelProperty(value = "已完成人数")
    private int completeNum;
    
    /**
     * 未完成人数
     */
    @ApiModelProperty(value = "未完成人数")
    private int uncompletedNum;
    
    /**
     * 当月测评总数
     */
    @ApiModelProperty(value = "当月测评总数")
    private int monthNum;
    
    
    /**
     * 当月测评总数
     */
    @ApiModelProperty(value = "年度测评总数")
    private int yearNum;

}
