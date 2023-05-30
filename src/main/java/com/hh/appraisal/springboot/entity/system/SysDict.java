package com.hh.appraisal.springboot.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hh.appraisal.springboot.core.annotation.EntityDoc;
import com.hh.appraisal.springboot.core.entity.BasicEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统字典表
 * @author gaigai
 * @date 2019/05/15
 */
@EntityDoc(note = "系统字典", isClass = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_dict")
public class SysDict extends BasicEntity {

    /**
     * 字典类型 唯一标识
     */
    @EntityDoc(note = "字典类型code")
    private String typeCode;

    /**
     * 名称
     */
    @EntityDoc(note = "名称")
    private String name;

    /**
     * 字典值
     */
    @EntityDoc(note = "字典值")
    private String value;

    /**
     * 自定数据
     */
    @EntityDoc(note = "自定数据")
    private String data;

    /**
     * 字典描述
     */
    @EntityDoc(note = "字典描述")
    private String notes;

    /**
     * 排序
     */
    @EntityDoc(note = "排序")
    private Long sort;

}
