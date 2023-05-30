package com.hh.appraisal.springboot.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hh.appraisal.springboot.core.annotation.EntityDoc;
import com.hh.appraisal.springboot.core.entity.BasicEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统功能表
 * 角色对一些功能有权限，每个功能内包含若干url接口资源
 * @author gaigai
 * @date 2019/05/15
 */
@EntityDoc(note = "系统功能", isClass = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_function")
public class SysFunction extends BasicEntity {

    /**
     * 名称
     */
    @EntityDoc(note = "名称")
    private String name;

    /**
     * 所属模块 从字典表取值
     */
    @EntityDoc(note = "所属模块")
    private String module;

}
