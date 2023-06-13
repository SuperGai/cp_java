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
 * 常模管理明细Bean
 * 用于控制层展示数据
 * @see com.hh.appraisal.springboot.entity.NormManageItem
 * @author gaigai
 * @date 2023/06/02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "常模管理明细实体")
public class NormManageItemBean implements Serializable {

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
     * 常模主表
     */
    @ApiModelProperty(value = "常模主表")
    private String normCode;

    /**
     * 等级
     */
    @ApiModelProperty(value = "等级")
    private String level;

    /**
     * 分值
     */
    @ApiModelProperty(value = "分值")
    private String scores;

    /**
     * 常模管理明细 唯一标识 集合
     */
    @ApiModelProperty(value = "常模管理明细 唯一标识 集合")
    private List<String> normManageItemCodeList;

}
