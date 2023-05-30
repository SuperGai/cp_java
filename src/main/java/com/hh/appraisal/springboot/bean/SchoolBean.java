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
 * 学校Bean
 * 用于控制层展示数据
 * @see com.hh.appraisal.springboot.entity.School
 * @author gaigai
 * @date 2021/06/26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "学校实体")
public class SchoolBean implements Serializable {

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
     * 学校简码
     */
    @ApiModelProperty(value = "学校简码")
    private String schoolCode;

    /**
     * 学校名称
     */
    @ApiModelProperty(value = "学校名称")
    private String schoolName;

    /**
     * 学校简介
     */
    @ApiModelProperty(value = "学校简介")
    private String intro;

    /**
     * 学校LOGO
     */
    @ApiModelProperty(value = "学校LOGO")
    private String logo;

    /**
     * 学校 唯一标识 集合
     */
    @ApiModelProperty(value = "学校 唯一标识 集合")
    private List<String> schoolCodeList;

}
