package com.hh.appraisal.springboot.bean.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 系统菜单表
 * @author gaigai
 * @date 2019/05/15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "系统菜单实体")
public class SysMenuBean implements Comparable<SysMenuBean>{

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
     * 菜单图标
     */
    @ApiModelProperty(value = "菜单图标")
    private String icon;

    /**
     * 访问路径
     */
    @ApiModelProperty(value = "访问路径")
    private String url;

    /**
     * 父菜单 唯一标识
     */
    @ApiModelProperty(value = "父菜单 唯一标识")
    private String parentMenuCode;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Long sort;

    /**
     * 菜单名称
     */
    @ApiModelProperty(value = "菜单名称")
    private String name;

    /**
     * 菜单备注
     */
    @ApiModelProperty(value = "菜单备注")
    private String notes;

    /**
     * 子菜单
     */
    @ApiModelProperty(value = "子菜单")
    @Builder.Default
    private List<SysMenuBean> children = new LinkedList<>();

    /**
     * 父菜单名称
     */
    @ApiModelProperty(value = "父菜单名称")
    private String parentName;

    /**
     * 菜单 code 集合
     */
    @ApiModelProperty(value = "菜单 唯一标识 集合")
    private List<String> menuCodeList;

    /**
     * 只需要父类菜单
     */
    @ApiModelProperty(value = "只需要父类菜单")
    private Boolean needParentMenu;

    @Override
    public int compareTo(SysMenuBean o) {
        Long me = (this.sort == null) ? 0 : this.sort;
        Long him = (o.getSort() == null) ? 0 : o.getSort();
        return new Long(me - him).intValue();
    }
}
