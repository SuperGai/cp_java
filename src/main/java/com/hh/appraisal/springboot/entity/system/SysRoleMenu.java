package com.hh.appraisal.springboot.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hh.appraisal.springboot.core.annotation.EntityDoc;
import com.hh.appraisal.springboot.core.entity.BasicEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统角色与菜单对应表
 * @author gaigai
 * @date 2019/05/15
 */
@EntityDoc(note = "系统角色与菜单对应", isClass = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_role_menu")
public class SysRoleMenu extends BasicEntity {

    /**
     * 角色 唯一标识
     */
    @EntityDoc(note = "角色code")
    private String roleCode;

    /**
     * 菜单 唯一标识
     */
    @EntityDoc(note = "菜单code")
    private String menuCode;

}
