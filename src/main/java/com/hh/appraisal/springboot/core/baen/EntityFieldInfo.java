package com.hh.appraisal.springboot.core.baen;

import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.List;

import com.hh.appraisal.springboot.core.utils.FileUtil;

/**
 * 实体类字段信息
 * @author gaigai
 * @date 2019/05/15
 */
@Data
@Builder
public class EntityFieldInfo {

    /**
     * 字段对象
     */
    private Field field;

    /**
     * 字段名称
     */
    private String name;

    /**
     * 字段所属类名
     */
    private String typeName;

    /**
     * 字段注释
     */
    private String note;

}
