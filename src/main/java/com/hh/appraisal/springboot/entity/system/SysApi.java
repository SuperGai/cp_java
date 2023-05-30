package com.hh.appraisal.springboot.entity.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hh.appraisal.springboot.core.annotation.EntityDoc;
import com.hh.appraisal.springboot.core.entity.BasicEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统API接口
 * 存放系统所有的接口url
 * @author gaigai
 * @date 2019/05/15
 */
@EntityDoc(isClass = true, note = "系统API接口")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_api")
public class SysApi extends BasicEntity {

    /**
     * 接口名称
     */
    @EntityDoc(note = "接口名称")
    private String name;

    /**
     * 接口路径
     */
    @EntityDoc(note = "接口路径")
    private String url;

    /**
     * 所属模块 从字典表取值
     */
    @EntityDoc(note = "所属模块")
    private String module;

}
