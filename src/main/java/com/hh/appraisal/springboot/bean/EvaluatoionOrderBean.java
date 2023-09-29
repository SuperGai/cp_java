package com.hh.appraisal.springboot.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

import com.hh.appraisal.springboot.core.annotation.EntityDoc;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;

import java.io.Serializable;
import java.lang.Integer;
import java.util.Date;

/**
 * 测评订单Bean
 * 用于控制层展示数据
 * @see com.hh.appraisal.springboot.entity.EvaluatoionOrderBean
 * @author gaigai
 * @date 2023/06/31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "测评订单实体")
@Excel("测评订单")
public class EvaluatoionOrderBean implements Serializable {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String code;
    
    /**
     * 产品Code
     */
    @ApiModelProperty(value = "产品")
    private String productCode;
    
    /**
     * 产品名称
     */
    @ExcelField("产品名称")
    @ApiModelProperty(value = "产品名称")
    private String productName;
    
    /**
     * 测评码
     */
    @ExcelField("测评码")
    @ApiModelProperty(value = "测评码")
    private String evaluatoionCode;
    
    /**
     * 测评码Code（关联测评码的主键）
     */
    @ApiModelProperty(value = "测评码Code")
    private String evaluatoionCodeCode;
    

    /**
     * 开始日期
     */
    @ExcelField(value = "开始日期")
    @ApiModelProperty(value = "开始日期")
    private String startDate;

    /**
     * 结束日期
     */
    @ExcelField(value = "结束日期")
    @ApiModelProperty(value = "结束日期")
    private String endDate;

    /**
     * 状态
     */
    @ExcelField(value = "测评状态")
    @ApiModelProperty(value = "测评状态")
    private String status;

    
    /**
     * 掩饰性数量
     */
    @ApiModelProperty(value = "掩饰性数量")
    private int ysxNumber;
    
    
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
     * 订单 唯一标识 集合
     */
    @ApiModelProperty(value = "订单 唯一标识 集合")
    private List<String> evaluatoionOrderList;


}
