package com.hh.appraisal.springboot.bean.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

/**
 * 系统功能与接口url对应表
 * @author gaigai
 * @date 2019/05/15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "系统功能与接口url对应实体")
public class SysFunctionApiBean {

    /**
     * 主键 唯一标识
     */
    @ApiModelProperty(value = "主键")
    private String code;

    /**
     * 数据是否有效
     */
    private Integer valid;

    /**
     * 记录创建时间
     */
    private Date createTime;

    /**
     * 记录更新时间
     */
    private Date updateTime;

    /**
     * 系统功能 唯一标识
     */
    @ApiModelProperty(value = "系统功能 唯一标识")
    private String functionCode;

    /**
     * 系统接口url 唯一标识
     */
    @ApiModelProperty(value = "系统接口url 唯一标识")
    private String apiCode;

    /**
     * 系统功能 code 集合
     */
    @ApiModelProperty(value = "系统功能 唯一标识 集合")
    private List<String> functionCodeList;

    /**
     * api code 集合
     */
    @ApiModelProperty(value = "api 唯一标识 集合")
    private List<String> apiCodeList;

}
