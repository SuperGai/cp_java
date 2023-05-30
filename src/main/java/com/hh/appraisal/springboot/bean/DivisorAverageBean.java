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
 * 因子Bean
 * 用于控制层展示数据
 * @see com.hh.appraisal.springboot.entity.Divisor
 * @author gaigai
 * @date 2021/06/26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "因子平均分")
public class DivisorAverageBean implements Serializable {

    /**
     * 因子名称
     */
    @ApiModelProperty(value = "因子Code")
    private String divisorCode;
	
    /**
     * 因子名称
     */
    @ApiModelProperty(value = "因子名称")
    private String divisorName;

    /**
     * 因子描述
     */
    @ApiModelProperty(value = "因子描述")
    private String divisorDesc;

    /**
     * 平均分
     */
    @ApiModelProperty(value = "因子平均分")
    private double divisorAverag;
    
    /**
     *  分数范围段描述
     */
    @ApiModelProperty(value = "描述1")
    private String contentZh;
    
    /**
     *  分数范围段描述
     */
    @ApiModelProperty(value = "描述2")
    private String contentEn;
    
    

}
