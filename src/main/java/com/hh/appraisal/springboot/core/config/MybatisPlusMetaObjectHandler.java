package com.hh.appraisal.springboot.core.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.hh.appraisal.springboot.core.constant.DataValid;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Mybatis Plus 字段自动填充
 * @doc https://mp.baomidou.com/guide/auto-fill-metainfo.html
 * @author gaigai
 * @date 2019/05/15
 */
@Slf4j
@Component
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入时字段自动填充
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        Date nowTime = new Date();

        if(metaObject.getValue("createTime") == null){
            this.strictInsertFill(metaObject, "createTime", Date.class, nowTime);
        }
        if(metaObject.getValue("updateTime") == null){
            this.strictInsertFill(metaObject, "updateTime", Date.class, nowTime);
        }
        if(metaObject.getValue("valid") == null){
            this.strictInsertFill(metaObject, "valid", Integer.class, DataValid.VALID);
        }
    }

    /**
     * 更新时字段自动填充
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime", new Date());
    }

}
