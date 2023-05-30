package com.hh.appraisal.springboot.core.baen;

import lombok.Builder;
import lombok.Data;
import java.util.List;

/**
 * 实体类信息
 * @author gaigai
 * @date 2019/05/15
 */
@Data
@Builder
public class EntityClassInfo {

    /**
     * 实体类对象
     */
    private Class entityClass;

    /**
     * 类注释
     */
    private String classNote;

    /**
     * 实体类对应的数据库表名
     */
    private String tableName;

    /**
     * 字段信息
     */
    private List<EntityFieldInfo> entityFieldInfoList;

}
