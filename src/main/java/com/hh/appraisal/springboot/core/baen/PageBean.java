package com.hh.appraisal.springboot.core.baen;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分页bean
 * @author gaigai
 * @date 2019/05/15
 */
@Data
@ApiModel(description = "分页查询实体")
public class PageBean {

    /**
     * 每页显示条数，默认 10
     */
    @ApiModelProperty(value = "每页显示条数", required = true)
    private long size = 10;

    /**
     * 当前页 默认1
     */
    @ApiModelProperty(value = "当前页", required = true)
    private long current = 1;

}
