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
 * 常模管理Bean
 * 用于控制层展示数据
 * @see com.hh.appraisal.springboot.entity.NormManage
 * @author gaigai
 * @date 2023/06/02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "常模管理实体")
public class NormManageBean implements Serializable {

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
     * 常模名称
     */
    @ApiModelProperty(value = "常模名称")
    private String name;

    /**
     * 常模描述
     */
    @ApiModelProperty(value = "常模描述")
    private String ndesc;

    /**
     * 常模管理 唯一标识 集合
     */
    @ApiModelProperty(value = "常模管理 唯一标识 集合")
    private List<String> normManageCodeList;

}
