package com.hh.appraisal.springboot.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hh.appraisal.springboot.core.annotation.EntityDoc;
import com.hh.appraisal.springboot.core.entity.BasicEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统字典类型表
 * @author gaigai
 * @date 2019/05/15
 */
@EntityDoc(note = "系统字典类型", isClass = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_dict_type")
public class SysDictType extends BasicEntity {

    /**
     * 字典类型名称
     */
    @EntityDoc(note = "字典类型名称")
    private String name;

    /**
     * 字典类型编号
     */
    @EntityDoc(note = "字典类型编号")
    private String num;

    /**
     * 备注
     */
    @EntityDoc(note = "备注")
    private String notes;

}
