package com.hh.appraisal.springboot.core.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.hh.appraisal.springboot.core.annotation.EntityDoc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础实体类
 * @author gaigai
 * @date 2019/05/15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasicEntity implements Serializable {

    /**
     * 主键 唯一标识
     * mybatis-plus自动生成UUID格式id
     */
    @EntityDoc(note = "主键")
    @TableId(type = IdType.ASSIGN_UUID)
    protected String code;

    /**
     * 数据是否有效
     */
    @EntityDoc(note = "数据是否有效")
    @TableField(fill = FieldFill.INSERT)
    protected Integer valid;

    /**
     * 记录创建时间
     */
    @EntityDoc(note = "记录创建时间")
    @TableField(fill = FieldFill.INSERT)
    protected Date createTime;

    /**
     * 记录更新时间
     */
    @EntityDoc(note = "记录更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected Date updateTime;

}
