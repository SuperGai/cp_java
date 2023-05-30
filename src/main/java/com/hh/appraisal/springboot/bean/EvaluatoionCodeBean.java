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
 * 测评码Bean
 * 用于控制层展示数据
 * @see com.hh.appraisal.springboot.entity.EvaluatoionCode
 * @author gaigai
 * @date 2021/06/26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "测评码实体")
@Excel("测评码")
public class EvaluatoionCodeBean implements Serializable {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String code;
    
    /**
     * 产品Code
     */
    @ExcelField("产品名称")
    @ApiModelProperty(value = "产品名称")
    private String productCode;
    
    
    /**
     * 学校Code
     */
    @ExcelField("学校")
    @ApiModelProperty(value = "学校Code")
    private String shoolCode;
    
    /**
     * 人员类型
     */
//    @ExcelField("人员类型")
    @ApiModelProperty(value = "人员类型")
    private String personType;
    
    /**
     * 测评码
     */
    @ExcelField("测评码")
    @ApiModelProperty(value = "测评码")
    private String evaluatoionCode;

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
     * 是否被使用
     */
    @ApiModelProperty(value = "是否被使用")
    private String isused;

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
     * 测评码 唯一标识 集合
     */
    @ApiModelProperty(value = "测评码 唯一标识 集合")
    private List<String> evaluatoionCodeCodeList;

}
