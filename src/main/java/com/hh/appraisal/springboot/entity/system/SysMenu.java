package com.hh.appraisal.springboot.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hh.appraisal.springboot.core.annotation.EntityDoc;
import com.hh.appraisal.springboot.core.entity.BasicEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统菜单表
 * @author gaigai
 * @date 2019/05/15
 */
@EntityDoc(note = "系统菜单表", isClass = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_menu")
public class SysMenu extends BasicEntity {

    /**
     * 菜单图标class
     */
    @EntityDoc(note = "菜单图标class")
    private String icon;

    /**
     * 访问路径
     */
    @EntityDoc(note = "访问路径")
    private String url;

    /**
     * 父菜单 唯一标识
     */
    @EntityDoc(note = "父菜单code")
    private String parentMenuCode;

    /**
     * 排序
     */
    @EntityDoc(note = "排序")
    private Long sort;

    /**
     * 菜单名称
     */
    @EntityDoc(note = "菜单名称")
    private String name;

    /**
     * 菜单备注
     */
    @EntityDoc(note = "菜单备注")
    private String notes;

}
