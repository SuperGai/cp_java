package com.hh.appraisal.springboot.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.io.Serializable;

/**
 * 产品明细Bean
 * 用于控制层展示数据
 * @see com.hh.appraisal.springboot.entity.ProductItem
 * @author gaigai
 * @date 2021/06/26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "产品明细实体")
public class ProductItemBean implements Serializable {
	
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
     * 产品
     */
    @ApiModelProperty(value = "产品")
    private String productCode;

    /**
     * 问题
     */
    @ApiModelProperty(value = "问题")
    private String questionCode;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 产品明细 唯一标识 集合
     */
    @ApiModelProperty(value = "产品明细 唯一标识 集合")
    private List<String> productItemCodeList;

}
