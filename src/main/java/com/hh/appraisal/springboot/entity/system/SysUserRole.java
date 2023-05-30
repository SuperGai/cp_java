package com.hh.appraisal.springboot.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hh.appraisal.springboot.core.annotation.EntityDoc;
import com.hh.appraisal.springboot.core.entity.BasicEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统用户与角色对应表
 * @author gaigai
 * @date 2019/05/15
 */
@EntityDoc(note = "系统用户与角色对应", isClass = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user_role")
public class SysUserRole extends BasicEntity {

    /**
     * 用户 唯一标识
     */
    @EntityDoc(note = "用户code")
    private String userCode;

    /**
     * 角色 唯一标识
     */
    @EntityDoc(note = "角色code")
    private String roleCode;

}
