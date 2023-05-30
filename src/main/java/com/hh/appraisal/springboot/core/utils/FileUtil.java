package com.hh.appraisal.springboot.core.utils;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.hh.appraisal.springboot.core.annotation.EntityDoc;
import com.hh.appraisal.springboot.core.baen.EntityClassInfo;
import com.hh.appraisal.springboot.core.baen.EntityFieldInfo;
import com.hh.appraisal.springboot.core.entity.BasicEntity;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 文件工具类
 * @author gaigai
 * @date 2019/05/15
 */
@Slf4j
public class FileUtil {

    /**
     * 扫描实体类
     * @param entityClassDic 实体类根目录
     * @param scanPackage 实体类包路径 
     * @return 返回实体类信息，包含实体类注释、字段信息集合、字段注释等
     */
    public static List<EntityClassInfo> scanEntityClassList(File entityClassDic, String scanPackage, boolean ignoreSystemEntity) {
        List<EntityClassInfo> classList = new ArrayList<>();
        List<Field> basicEntityFieldList = Arrays.asList(BasicEntity.class.getDeclaredFields());
        for (File file : entityClassDic.listFiles()) {
            if (file.isDirectory()) {
                classList.addAll(scanEntityClassList(file, scanPackage, ignoreSystemEntity));
                continue;
            }
            if(!file.exists()){
                log.error("文件不存在: " + file.getAbsolutePath());
                continue;
            }

            EntityClassInfo entityClassInfo = EntityClassInfo.builder().build();

            // 解析包路径
            String tempPackage = file.getAbsolutePath().replaceAll("//", ".").replaceAll("/", ".");
            if(tempPackage.indexOf(scanPackage) < 0) {
                continue;
            }
            String className = scanPackage + tempPackage.split(scanPackage)[1].replaceAll(".java", "");

            // 加载实体类
            try{
                entityClassInfo.setEntityClass(Class.forName(className));
                log.info("加载实体类: " + className);
            }catch (ClassNotFoundException cnE){
                log.error("加载实体类失败: " + className);
                continue;
            }

            // 加载表名称
            if(entityClassInfo.getEntityClass().isAnnotationPresent(TableName.class)){
                TableName tableName = (TableName) entityClassInfo.getEntityClass().getDeclaredAnnotation(TableName.class);
                entityClassInfo.setTableName(tableName.value());
            }

            // 检查是否为系统实体类
            if(ignoreSystemEntity){
                if(entityClassInfo.getTableName().startsWith("sys_")){
                    continue;
                }
            }

            // 加载类信息 包含注释
            entityClassInfo.setEntityFieldInfoList(new ArrayList<>());
            if(entityClassInfo.getEntityClass().isAnnotationPresent(EntityDoc.class)){
                EntityDoc entityDoc = (EntityDoc) entityClassInfo.getEntityClass().getDeclaredAnnotation(EntityDoc.class);
                entityClassInfo.setClassNote(ObjectUtils.isEmpty(entityDoc.note()) ? "" : entityDoc.note());
            }

            // 获取类字段列表
            List<Field> fieldList = new LinkedList<>();
            // 继承了BasicEntity的类，需要手动添加BasicEntity字段
            if(entityClassInfo.getEntityClass().getAnnotatedSuperclass().getType().getTypeName().equals(BasicEntity.class.getTypeName())){
                fieldList.addAll(basicEntityFieldList);
            }
            fieldList.addAll(Arrays.asList(entityClassInfo.getEntityClass().getDeclaredFields()));

            // 加载字段信息 包含注释
            for(Field field : fieldList){
                EntityFieldInfo entityFieldInfo = EntityFieldInfo.builder()
                        .field(field)
                        .name(field.getName()).typeName(field.getType().getTypeName())
                        .build();
                if(field.isAnnotationPresent(EntityDoc.class)){
                    EntityDoc entityDoc = (EntityDoc) field.getDeclaredAnnotation(EntityDoc.class);
                    entityFieldInfo.setNote(ObjectUtils.isEmpty(entityDoc.note()) ? "" : entityDoc.note());
                }
                entityClassInfo.getEntityFieldInfoList().add(entityFieldInfo);
            }

            classList.add(entityClassInfo);
        }
        return classList;
    }

    /**
     * 获取项目根路径
     * @return
     */
    public static String getProjectRootPath(){
        return FileUtil.class.getClassLoader().getResource("/").getPath();
    }

}
