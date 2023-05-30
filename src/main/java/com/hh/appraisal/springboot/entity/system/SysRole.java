package com.hh.appraisal.springboot.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hh.appraisal.springboot.core.annotation.EntityDoc;
import com.hh.appraisal.springboot.core.entity.BasicEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统角色表
 * @author gaigai
 * @date 2019/05/15
 */
@EntityDoc(note = "系统角色", isClass = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_role")
public class SysRole extends BasicEntity {

    /**
     * 角色名称
     */
    @EntityDoc(note = "角色名称")
    private String name;

    /**
     * 角色备注
     */
    @EntityDoc(note = "角色备注")
    private String notes;

}
