package com.hh.appraisal.springboot.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hh.appraisal.springboot.core.annotation.EntityDoc;
import com.hh.appraisal.springboot.core.entity.BasicEntity;
import com.wuwenze.poi.annotation.ExcelField;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 测评订单
 * @author gaigai
 * @date 2023/06/31
 */
@EntityDoc(isClass = true, note = "测评码")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("evaluatoion_order")
public class EvaluatoionOrder extends BasicEntity {

    /**
     * 产品Code
     */
    @EntityDoc(note = "产品名称")
    private String productCode;
    
    /**
     * 测评码
     */
    @EntityDoc(note = "测评码")
    private String evaluatoionCode;

    /**
     * 测评码Code（关联测评码的主键）
     */
    @ApiModelProperty(value = "测评码Code")
    private String evaluatoionCodeCode;
    
    /**
     * 开始日期
     */
    @EntityDoc(note = "开始日期")
    private String startDate;

    /**
     * 结束日期
     */
    @EntityDoc(note = "结束日期")
    private String endDate;

    /**
     * 测评状态
     */
    @EntityDoc(note  = "测评状态")
    private String status="未开始";

    /**
     * 数据是否有效
     */
    @EntityDoc(note  = "数据是否有效")
    private Integer valid;

    /**
     * 掩饰性数量
     */
    @EntityDoc(note = "掩饰性数量")
    private int ysxNumber;
    
    
}
