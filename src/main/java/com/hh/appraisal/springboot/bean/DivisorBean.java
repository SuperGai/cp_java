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
@ApiModel(description = "因子实体")
public class DivisorBean implements Serializable {

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
     * 因子 唯一标识 集合
     */
    @ApiModelProperty(value = "因子 唯一标识 集合")
    private List<String> divisorCodeList;

}
