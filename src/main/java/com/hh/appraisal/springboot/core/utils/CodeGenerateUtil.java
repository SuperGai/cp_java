package com.hh.appraisal.springboot.core.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.ClassInfo;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.hh.appraisal.springboot.bean.system.*;
import com.hh.appraisal.springboot.constant.DictTypeNumConstant;
import com.hh.appraisal.springboot.core.baen.EntityClassInfo;
import com.hh.appraisal.springboot.core.baen.EntityFieldInfo;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.core.config.CodeGenerateConfig;
import com.hh.appraisal.springboot.core.config.SpringContextHolder;
import com.hh.appraisal.springboot.core.constant.DataValid;
import com.hh.appraisal.springboot.entity.system.SysDictType;
import com.hh.appraisal.springboot.entity.system.SysFunction;
import com.hh.appraisal.springboot.entity.system.SysFunctionApi;
import com.hh.appraisal.springboot.service.system.*;

import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * 代码生成器
 * @author gaigai
 * @date 2021/02/01
 */
@Slf4j
public class CodeGenerateUtil {

    /**
     * 模板文件名称 Bean 模板
     */
    private static final String TEMPLATE_NAME_BEAN = "BeanTemplate.ftl";

    /**
     * 模板文件名称 Dao 接口模板
     */
    private static final String TEMPLATE_NAME_DAO = "DaoTemplate.ftl";

    /**
     * 模板文件名称 Service 接口模板
     */
    private static final String TEMPLATE_NAME_SERVICE = "ServiceTemplate.ftl";

    /**
     * 模板文件名称 Service 实现类模板
     */
    private static final String TEMPLATE_NAME_SERVICE_IMPL = "ServiceImplTemplate.ftl";

    /**
     * 模板文件名称 Controller 模板
     */
    private static final String TEMPLATE_NAME_CONTROLLER = "ControllerTemplate.ftl";

    /**
     * 模板文件名称 前端配置 模板
     */
    private static final String TEMPLATE_NAME_PAGE_CONFIG = "PageTemplate-config.ftl";

    /**
     * 模板文件名称 前端-新增页面 模板
     */
    private static final String TEMPLATE_NAME_PAGE_ADD = "PageTemplate-Add.ftl";

    /**
     * 模板文件名称 前端-编辑页面 模板
     */
    private static final String TEMPLATE_NAME_PAGE_EDIT = "PageTemplate-Edit.ftl";

    /**
     * 模板文件名称 前端-列表页面 模板
     */
    private static final String TEMPLATE_NAME_PAGE_LIST = "PageTemplate-List.ftl";

    /**
     * 模板文件名称 sql 模板
     */
    private static final String TEMPLATE_NAME_SQL = "SqlTemplate.ftl";

    /**
     * 实体类字段类型与sql类型映射
     */
    private static final HashMap<String, String> FIELD_CLASS_SQL_TYPE = new HashMap<String, String>(){{
        put("boolean", "tinyint");
        put("int", "int");
        put("short", "int");
        put("long", "bigint");
        put("float", "decimal(10,4)");
        put("double", "decimal(10,4)");
        put("char", "varchar(255)");
        put("java.lang.Byte", "tinyint");
        put("java.lang.Integer", "int");
        put("java.lang.String", "varchar(255)");
        put("java.lang.byte[]", "blob");
        put("java.lang.Boolean", "tinyint(1)");
        put("java.lang.Long", "bigint");
        put("java.math.BigInteger", "bigint");
        put("java.lang.Float", "decimal(10,4)");
        put("java.lang.Double", "decimal(10,4)");
        put("java.sql.Date", "datetime");
        put("java.sql.Time", "time");
        put("java.sql.Timestamp", "datetime");
        put("java.util.Date", "datetime");
        put("java.math.BigDecimal", "decimal(10,4)");
    }};

    /**
     * 执行代码生成
     * @param config
     * @param chooseClassPackageList 手动选择需要处理的类, 不传则默认处理全部扫描到的实体类
     * @return 返回成功后，可获取实体类信息集合
     */
    public static RestBean generate(CodeGenerateConfig config, List<String> chooseClassPackageList) {
        // 获取配置
        Configuration fmConfig = SpringContextHolder.getBean(Configuration.class);

        if(fmConfig == null){
            log.error("配置失败");
            return RestBean.error("配置失败");
        }

        log.info("项目根目录: " + config.getProjectRootPath());

        String scanPackage = config.getProjectPackage() + "." + config.getScanPackage();
        log.info("实体类包扫描路径: " + scanPackage);

        String entityClassDicPath = config.getProjectRootPath() + File.separator +
                "src" + File.separator + "main" + File.separator + "java" + File.separator +
                (scanPackage.replaceAll("\\.", File.separator+File.separator));
        log.info("实体类包扫描全路径: " + entityClassDicPath);
        File entityClassDic = new File(entityClassDicPath);
        if(!entityClassDic.exists() || !entityClassDic.isDirectory()){
            log.error("实体类包目录下找不到文件: " + entityClassDicPath);
            return RestBean.error("实体类包目录下找不到文件: " + entityClassDicPath);
        }

        // 获取目标实体类对象集合
        List<EntityClassInfo> entityClassList = FileUtil.scanEntityClassList(entityClassDic, scanPackage, true);

        // 去除未被选择的类
        if(!CollectionUtils.isEmpty(chooseClassPackageList)){
            Iterator<EntityClassInfo> iterator = entityClassList.iterator();
            while (iterator.hasNext()){
                EntityClassInfo item = iterator.next();
              	System.out.println(item.getEntityClass().getName());
                if(!chooseClassPackageList.contains(item.getEntityClass().getName())){
                    iterator.remove();
                }
            }
        }

        String packagePath = "";
        for(EntityClassInfo entityClassInfo : entityClassList){
            if(entityClassInfo.getClass() == null){
                log.error("实体类信息无效: " + entityClassInfo);
                continue;
            }

            // 生成 Bean 类
            processTemplate(TEMPLATE_NAME_BEAN, entityClassInfo, fmConfig, config,
                    getOutPutPath(TEMPLATE_NAME_BEAN, config, entityClassInfo.getEntityClass()));

            // 生成 DAO 接口
            processTemplate(TEMPLATE_NAME_DAO, entityClassInfo, fmConfig, config,
                    getOutPutPath(TEMPLATE_NAME_DAO, config, entityClassInfo.getEntityClass()));

            // 生成 Service 接口
            processTemplate(TEMPLATE_NAME_SERVICE, entityClassInfo, fmConfig, config,
                    getOutPutPath(TEMPLATE_NAME_SERVICE, config, entityClassInfo.getEntityClass()));

            // 生成 Service 实现类
            processTemplate(TEMPLATE_NAME_SERVICE_IMPL, entityClassInfo, fmConfig, config,
                    getOutPutPath(TEMPLATE_NAME_SERVICE_IMPL, config, entityClassInfo.getEntityClass()));

            // 生成 Controller 类
            processTemplate(TEMPLATE_NAME_CONTROLLER, entityClassInfo, fmConfig, config,
                    getOutPutPath(TEMPLATE_NAME_CONTROLLER, config, entityClassInfo.getEntityClass()));

            // 生成 前端页面 包含 Add.vue、Edit.vue、List.vue、api和路由配置
            processTemplate(TEMPLATE_NAME_PAGE_ADD, entityClassInfo, fmConfig, config, null);
            processTemplate(TEMPLATE_NAME_PAGE_EDIT, entityClassInfo, fmConfig, config, null);
            processTemplate(TEMPLATE_NAME_PAGE_LIST, entityClassInfo, fmConfig, config, null);
        }

        // 生成sql
        processTemplate_SQL(TEMPLATE_NAME_SQL, entityClassList, fmConfig, config);

        // 生成 html config
        processTemplate_HtmlConfig(TEMPLATE_NAME_PAGE_CONFIG, entityClassList, fmConfig, config);

        return RestBean.ok(entityClassList);
    }

    /**
     * 根据指定的实体类构造功能、接口、菜单集合，并插入到数据库
     * 菜单、功能、接口、功能接口映射
     * @param entityClassInfoList
     * @param config
     * @return
     */
    public static RestBean generateOthers(List<EntityClassInfo> entityClassInfoList, CodeGenerateConfig config){
        // 获取Spring里注册的服务
        SysDictTypeService dictTypeService = SpringContextHolder.getBean(SysDictTypeService.class);
        SysDictService dictService = SpringContextHolder.getBean(SysDictService.class);
        SysApiService apiService = SpringContextHolder.getBean(SysApiService.class);
        SysMenuService menuService = SpringContextHolder.getBean(SysMenuService.class);
        SysFunctionService functionService = SpringContextHolder.getBean(SysFunctionService.class);
        SysFunctionApiService functionApiService = SpringContextHolder.getBean(SysFunctionApiService.class);
        if(dictTypeService == null || dictService == null || apiService == null || menuService == null
                || functionService == null || functionApiService == null){
            return RestBean.error("无法加载Spring Bean");
        }

        for(EntityClassInfo classInfo : entityClassInfoList){
            if(classInfo.getEntityClass() == null || ObjectUtils.isEmpty(classInfo.getEntityFieldInfoList())){
                log.error("类信息缺失，此类不处理: " + classInfo);
                continue;
            }
            String lowEntityName = StrUtils.strFirstLowerCase(classInfo.getEntityClass().getSimpleName());

            // entity包下的分目录
            String relativePackage = "";
            String entityClassPackage = classInfo.getEntityClass().getTypeName().replaceAll("\\." + classInfo.getEntityClass().getSimpleName(), "");
            if(entityClassPackage.indexOf(config.getScanPackage()) >= 0){
                relativePackage = entityClassPackage.replaceAll(config.getProjectPackage() + "." + config.getScanPackage(), "");
            }

            // 根据entity分包生成request mapping
            String relativeMapping = relativePackage.replaceAll("/", "").replaceAll("\\.", "/");
            relativeMapping = relativeMapping + "/" + lowEntityName;

            // 构造模块 模块是功能、api内必须的字段
            SysDictTypeBean moduleTypeBean = dictTypeService.findOne(SysDictTypeBean.builder().num(DictTypeNumConstant.SYSTEM_MODULE).build());
            if(moduleTypeBean == null){
                log.error("找不到系统模块信息: " + DictTypeNumConstant.SYSTEM_MODULE);
                continue;
            }
            SysDictBean moduleBean = dictService.addWithOutRestBean(SysDictBean.builder()
                    .typeCode(moduleTypeBean.getCode())
                    .name(classInfo.getClassNote() + "管理") // eg. 接口管理
                    .value(classInfo.getEntityClass().getSimpleName() + "_manage") // eg. SysApi_manage
                    .sort(1L)
                    .build());
            if(moduleBean == null || ObjectUtils.isEmpty(moduleBean.getCode())){
                log.error("插入新的模块记录失败，此类不处理: " + classInfo.getEntityClass().getName());
                continue;
            }

            // 构造菜单
            SysMenuBean menuBean = menuService.add(SysMenuBean.builder()
                    .icon("el-icon-grape")// 菜单图标class，这里先使用默认。需要更换请去https://element.eleme.cn/#/zh-CN/component/icon查看
                    .url("/" + lowEntityName + "/list") // eg. /sysApi/list
                    .sort(900L)
                    .name(classInfo.getClassNote() + "管理")
                    .build());
            log.info("菜单图标class暂时使用默认(el-icon-grape)，需要更换请访问: 菜单图标class，这里先使用默认。https://element.eleme.cn/#/zh-CN/component/icon");
            log.info("菜单排序暂时填900，请自行修改");
            if(menuBean == null || ObjectUtils.isEmpty(menuBean.getCode())){
                log.error("菜单构造失败: " + menuBean);
            }

            // 构造接口 - 新增
            SysApiBean addApiBean = apiService.add(SysApiBean.builder()
                    .name("新增" + classInfo.getClassNote())
                    .url(relativeMapping + "/add")
                    .module(moduleBean.getValue())
                    .build());
            if(addApiBean == null || ObjectUtils.isEmpty(addApiBean.getCode())){
                log.error("新增接口构造失败: " + addApiBean);
            }
            // 构造接口 - 删除
            SysApiBean deleteApiBean = apiService.add(SysApiBean.builder()
                    .name("根据唯一标识删除" + classInfo.getClassNote())
                    .url(relativeMapping + "/deleteByCode")
                    .module(moduleBean.getValue())
                    .build());
            if(deleteApiBean == null || ObjectUtils.isEmpty(deleteApiBean.getCode())){
                log.error("删除接口构造失败: " + deleteApiBean);
            }
            // 构造接口 - 修改
            SysApiBean updateApiBean  = apiService.add(SysApiBean.builder()
                    .name("更新" + classInfo.getClassNote())
                    .url(relativeMapping + "/update")
                    .module(moduleBean.getValue())
                    .build());
            if(updateApiBean == null || ObjectUtils.isEmpty(updateApiBean.getCode())){
                log.error("更新接口构造失败: " + updateApiBean);
            }
            // 构造接口 - 查看详情
            SysApiBean detailApiBean = apiService.add(SysApiBean.builder()
                    .name("查看" + classInfo.getClassNote() + "详情")
                    .url(relativeMapping + "/detail")
                    .module(moduleBean.getValue())
                    .build());
            if(detailApiBean == null || ObjectUtils.isEmpty(detailApiBean.getCode())){
                log.error("查看详情接口构造失败: " + detailApiBean);
            }
            // 构造接口 - 分页查询
            SysApiBean listPageApiBean = apiService.add(SysApiBean.builder()
                    .name("分页查询" + classInfo.getClassNote())
                    .url(relativeMapping + "/listPage")
                    .module(moduleBean.getValue())
                    .build());
            if(listPageApiBean == null || ObjectUtils.isEmpty(listPageApiBean.getCode())){
                log.error("分页查询接口构造失败: " + listPageApiBean);
            }
            // 构造接口 - 批量删除
            SysApiBean batchDeleteApiBean = apiService.add(SysApiBean.builder()
                    .name("批量删除" + classInfo.getClassNote())
                    .url(relativeMapping + "/batchDeleteByCodeList")
                    .module(moduleBean.getValue())
                    .build());
            if(batchDeleteApiBean == null || ObjectUtils.isEmpty(batchDeleteApiBean.getCode())){
                log.error("批量删除接口构造失败: " + batchDeleteApiBean);
            }

            // 构造功能 - 新增功能
            SysFunctionBean addFunc = functionService.add(SysFunctionBean.builder()
                    .name("新增" + classInfo.getClassNote())
                    .module(moduleBean.getValue())
                    .build());
            if(addFunc == null || ObjectUtils.isEmpty(addFunc.getCode())){
                log.error("新增功能构造失败: " + addFunc);
            }
            // 功能接口映射 - 新增功能
            SysFunctionApiBean functionApiBean = functionApiService.add(SysFunctionApiBean.builder()
                    .functionCode(addFunc.getCode())
                    .apiCode(addApiBean.getCode())
                    .build());
            if(functionApiBean == null || ObjectUtils.isEmpty(functionApiBean.getCode())){
                log.error("新增功能接口映射构造失败: " + functionApiBean);
            }

            // 构造功能 - 删除功能
            SysFunctionBean deleteFunc = functionService.add(SysFunctionBean.builder()
                    .name("删除" + classInfo.getClassNote())
                    .module(moduleBean.getValue())
                    .build());
            if(deleteFunc == null || ObjectUtils.isEmpty(deleteFunc.getCode())){
                log.error("删除功能构造失败: " + deleteFunc);
            }
            // 功能接口映射 - 删除功能
            functionApiBean = functionApiService.add(SysFunctionApiBean.builder()
                    .functionCode(deleteFunc.getCode())
                    .apiCode(deleteApiBean.getCode())
                    .build());
            if(functionApiBean == null || ObjectUtils.isEmpty(functionApiBean.getCode())){
                log.error("删除功能接口映射构造失败: " + functionApiBean);
            }
            functionApiBean = functionApiService.add(SysFunctionApiBean.builder()
                    .functionCode(deleteFunc.getCode())
                    .apiCode(batchDeleteApiBean.getCode())
                    .build());
            if(functionApiBean == null || ObjectUtils.isEmpty(functionApiBean.getCode())){
                log.error("删除功能接口映射构造失败: " + functionApiBean);
            }

            // 构造功能 - 更新功能
            SysFunctionBean updateFunc = functionService.add(SysFunctionBean.builder()
                    .name("更新" + classInfo.getClassNote())
                    .module(moduleBean.getValue())
                    .build());
            if(updateFunc == null || ObjectUtils.isEmpty(updateFunc.getCode())){
                log.error("更新功能构造失败: " + updateFunc);
            }
            functionApiBean = functionApiService.add(SysFunctionApiBean.builder()
                    .functionCode(updateFunc.getCode())
                    .apiCode(updateApiBean.getCode())
                    .build());
            if(functionApiBean == null || ObjectUtils.isEmpty(functionApiBean.getCode())){
                log.error("更新功能接口映射构造失败: " + functionApiBean);
            }
            functionApiBean = functionApiService.add(SysFunctionApiBean.builder()
                    .functionCode(updateFunc.getCode())
                    .apiCode(detailApiBean.getCode())
                    .build());
            if(functionApiBean == null || ObjectUtils.isEmpty(functionApiBean.getCode())){
                log.error("更新功能接口映射构造失败: " + functionApiBean);
            }

            // 构造功能 - 分页查询功能
            SysFunctionBean listPageFunc = functionService.add(SysFunctionBean.builder()
                    .name("分页查询" + classInfo.getClassNote())
                    .module(moduleBean.getValue())
                    .build());
            if(listPageFunc == null || ObjectUtils.isEmpty(listPageFunc.getCode())){
                log.error("分页查询功能构造失败: " + listPageFunc);
            }
            functionApiBean = functionApiService.add(SysFunctionApiBean.builder()
                    .functionCode(listPageFunc.getCode())
                    .apiCode(listPageApiBean.getCode())
                    .build());
            if(functionApiBean == null || ObjectUtils.isEmpty(functionApiBean.getCode())){
                log.error("分页查询功能接口映射构造失败: " + functionApiBean);
            }

            // 构造功能 - 查看详情功能
            SysFunctionBean detailFunc = functionService.add(SysFunctionBean.builder()
                    .name("查看" + classInfo.getClassNote() + "详情")
                    .module(moduleBean.getValue())
                    .build());
            if(detailFunc == null || ObjectUtils.isEmpty(detailFunc.getCode())){
                log.error("查看详情功能构造失败: " + detailFunc);
            }
            functionApiBean = functionApiService.add(SysFunctionApiBean.builder()
                    .functionCode(detailFunc.getCode())
                    .apiCode(detailApiBean.getCode())
                    .build());
            if(functionApiBean == null || ObjectUtils.isEmpty(functionApiBean.getCode())){
                log.error("分页查询功能接口映射构造失败: " + functionApiBean);
            }
        }

        return RestBean.ok();
    }

    /**
     * 生成dao模板的数据
     * 模板文件 DaoTemplate.ftl
     * @param config
     * @param entityClass 实体类
     * @return
     */
    private static HashMap<String, Object> getTemplateData(String templateFileName, CodeGenerateConfig config,
                                                           EntityClassInfo entityClassInfo){
        HashMap<String, Object> map = new HashMap<>();

        // 注释 - 作者
        map.put("author", config.getDocAuthor());
        // 注释 - 日期 2019/01/09
        map.put("date", config.getDocDate());
        // 实体类名
        map.put("entityName", entityClassInfo.getEntityClass().getSimpleName());
        // 实体类 中文注释名
        map.put("entityNameNote", entityClassInfo.getClassNote());
        // 实体类包名
        map.put("entityPackage", entityClassInfo.getEntityClass().getName());
        // 首字母小写的实体类名
        String lowEntityName = StrUtils.strFirstLowerCase(entityClassInfo.getEntityClass().getSimpleName());
        map.put("lowEntityName", lowEntityName);

        // entity包下的分目录
        String relativePackage = "";
        String entityClassPackage = entityClassInfo.getEntityClass().getTypeName().replaceAll("\\." + entityClassInfo.getEntityClass().getSimpleName(), "");
        if(entityClassPackage.indexOf(config.getScanPackage()) >= 0){
            relativePackage = entityClassPackage.replaceAll(config.getProjectPackage() + "." + config.getScanPackage(), "");
        }

        // 根据entity分包生成request mapping
        String relativeMapping = relativePackage.replaceAll("/", "").replaceAll("\\.", "/");
        relativeMapping = relativeMapping + "/" + lowEntityName;

        switch (templateFileName){
            // Bean 生成
            case TEMPLATE_NAME_BEAN: {
                if(ObjectUtils.isEmpty(entityClassInfo.getEntityFieldInfoList())){
                    break;
                }
                List<HashMap<String, String>> fieldList = new ArrayList<>();
                Set<String> otherImportSet = new HashSet<>();
                for(EntityFieldInfo entityFieldInfo : entityClassInfo.getEntityFieldInfoList()){
                    // import
                    String importStr = StrUtils.getImportStr(entityFieldInfo.getField());
                    if(importStr != null){
                        otherImportSet.add(importStr);
                    }
                    // 字段
                    fieldList.add(new HashMap<String, String>(){{
                        put("note", entityFieldInfo.getNote());
                        put("type", entityFieldInfo.getField().getType().getSimpleName());
                        put("name", entityFieldInfo.getField().getName());
                    }});
                }
                map.put("otherImportList", otherImportSet);
                map.put("fieldList", fieldList);
                map.put("packageName", config.getProjectPackage() + "." + config.getOutPackageBean() + relativePackage);
                break;
            }
            // Dao接口生成
            case TEMPLATE_NAME_DAO: {
                map.put("packageName", config.getProjectPackage() + "." + config.getOutPackageDao() + relativePackage);
                break;
            }
            // Service 接口生成
            case TEMPLATE_NAME_SERVICE: {
                map.put("packageName", config.getProjectPackage() + "." + config.getOutPackageService() + relativePackage);
                map.put("entityBeanPackage", config.getProjectPackage() + "." + config.getOutPackageBean() + relativePackage + "." + entityClassInfo.getEntityClass().getSimpleName() + "Bean");
                break;
            }
            // ServiceImpl 实现类生成
            case TEMPLATE_NAME_SERVICE_IMPL: {
                map.put("packageName", config.getProjectPackage() + "." + config.getOutPackageServiceimpl() + relativePackage);
                map.put("entityBeanPackage", config.getProjectPackage() + "." + config.getOutPackageBean() + relativePackage + "." + entityClassInfo.getEntityClass().getSimpleName() + "Bean");
                map.put("entityMapperPackage", config.getProjectPackage() + "." + config.getOutPackageDao() + relativePackage + "." + entityClassInfo.getEntityClass().getSimpleName() + "Mapper");
                map.put("entityServicePackage", config.getProjectPackage() + "." + config.getOutPackageService() + relativePackage + "." + entityClassInfo.getEntityClass().getSimpleName() + "Service");
                break;
            }
            // Controller 生成
            case TEMPLATE_NAME_CONTROLLER: {
                map.put("packageName", config.getProjectPackage() + "." + config.getOutPackageController() + relativePackage);
                map.put("entityBeanPackage", config.getProjectPackage() + "." + config.getOutPackageBean() + relativePackage + "." + entityClassInfo.getEntityClass().getSimpleName() + "Bean");
                map.put("entityServicePackage", config.getProjectPackage() + "." + config.getOutPackageService() + relativePackage + "." + entityClassInfo.getEntityClass().getSimpleName() + "Service");
                map.put("relativeMapping", relativeMapping);
                break;
            }
            // 前端 - Add、Edit、List页面 生成
            case TEMPLATE_NAME_PAGE_ADD:
            case TEMPLATE_NAME_PAGE_EDIT:
            case TEMPLATE_NAME_PAGE_LIST: {
                if(ObjectUtils.isEmpty(entityClassInfo.getEntityFieldInfoList())){
                    entityClassInfo.setEntityFieldInfoList(new ArrayList<>());
                }
                List<HashMap<String, String>> fieldList = new ArrayList<>();
                Set<String> otherImportSet = new HashSet<>();
                for(EntityFieldInfo entityFieldInfo : entityClassInfo.getEntityFieldInfoList()){
                    if("createTime".equals(entityFieldInfo.getName()) || "updateTime".equals(entityFieldInfo.getName()) ||
                            "valid".equals(entityFieldInfo.getName()) || "code".equals(entityFieldInfo.getName())){
                        continue;
                    }
                    // 字段
                    fieldList.add(new HashMap<String, String>(){{
                        put("note", entityFieldInfo.getNote());
                        put("name", entityFieldInfo.getField().getName());
                    }});
                }
                map.put("fieldList", fieldList);
                break;
            }
        }

        return map;
    }

    /**
     * 根据实体类获取建表sql
     * 此方法会写死 code、valid、createTime、updateTime
     * @param classInfo
     * @return
     */
    private static String getSqlByEntityClass(EntityClassInfo classInfo){
        if(ObjectUtils.isEmpty(classInfo.getEntityFieldInfoList())){
            log.error("实体类内无字段: " + classInfo.getTableName());
            return "";
        }

        StringBuilder tableSql = new StringBuilder();
        tableSql.append("DROP TABLE IF EXISTS `").append(classInfo.getTableName()).append("`;\n");
        tableSql.append("CREATE TABLE `").append(classInfo.getTableName()).append("` (\n");

        // 写死的默认字段
        tableSql.append("  `code` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '唯一标识(主键ID)',\n");
        tableSql.append("  `valid` tinyint(1) NULL DEFAULT 1 COMMENT '数据是否删除',\n");
        tableSql.append("  `create_time` datetime NULL COMMENT '记录创建时间',\n");
        tableSql.append("  `update_time` datetime NULL COMMENT '记录修改时间',\n");

        // 其他字段
        for(EntityFieldInfo fieldInfo : classInfo.getEntityFieldInfoList()){
            if(fieldInfo.getField() == null ||
                    "code".equals(fieldInfo.getName()) || "valid".equals(fieldInfo.getName()) ||
                    "createTime".equals(fieldInfo.getName()) || "updateTime".equals(fieldInfo.getName())){
                continue;
            }
            // `icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '图标',
            String sqlType = FIELD_CLASS_SQL_TYPE.get(fieldInfo.getField().getType().getName());
            if(ObjectUtils.isEmpty(sqlType)){ // 找不到类型，则用varchar
                sqlType = "varchar(255)";
            }
            if(sqlType.indexOf("varchar") >= 0){// varchar类型 加入编码
                sqlType += " CHARACTER SET utf8 COLLATE utf8_unicode_ci ";
            }
            tableSql.append("  `").append(BeanUtil.underFieldName(fieldInfo.getName())).append("` ")
                    .append(sqlType)
                    .append(" NULL DEFAULT NULL ")
                    .append("COMMENT '").append(fieldInfo.getNote()).append("',\n");
        }

        //
        tableSql.append("  PRIMARY KEY (`code`) USING BTREE\n");
        tableSql.append(") ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '").append(classInfo.getClassNote()).append("' ROW_FORMAT = Dynamic;\n");
        return tableSql.toString();
    }

    /**
     * 模板处理和生成文件保存 - sql
     * @param templateName
     * @param entityClassInfoList
     * @param fmConfig
     * @param codeGenerateConfig
     */
    private static void processTemplate_SQL(String templateName, List<EntityClassInfo> entityClassInfoList,
                                            Configuration fmConfig, CodeGenerateConfig codeGenerateConfig){
        Template template;
        try{
            template = fmConfig.getTemplate(templateName);
        }catch (Exception e){
            log.error("无法获取模板: " + e.getMessage());
            return;
        }

        if(ObjectUtils.isEmpty(entityClassInfoList)){
            return;
        }

        List<String> tableSqlList = new ArrayList<>();
        for(EntityClassInfo classInfo : entityClassInfoList){
            tableSqlList.add(getSqlByEntityClass(classInfo));
        }

        boolean rest = FreemarkerUtil.templateProcessAndSave(
                new HashMap<String, Object>(){{put("tableSqlList", tableSqlList);}},
                fmConfig, template, codeGenerateConfig.getOutPath() + CodeGenerateConfig.OUT_PATH_RELATIVE_SQL,
                "tableSqlList.sql"
        );

        if(!rest){
            log.info("sql文件输出失败");
        }
    }

    /**
     * 模板处理和生成文件保存 - 页面配置
     * @param templateName
     * @param entityClassInfoList
     * @param fmConfig
     * @param config
     */
    private static void processTemplate_HtmlConfig(String templateName, List<EntityClassInfo> entityClassInfoList,
                                                   Configuration fmConfig, CodeGenerateConfig config){
        Template template;
        try{
            template = fmConfig.getTemplate(templateName);
        }catch (Exception e){
            log.error("无法获取模板: " + e.getMessage());
            return;
        }

        if(ObjectUtils.isEmpty(entityClassInfoList)){
            return;
        }

        List<HashMap<String, String>> configMapList = new ArrayList<>();

        for(EntityClassInfo entityClassInfo : entityClassInfoList){
            HashMap<String, String> data = new HashMap<>();

            data.put("entityName", entityClassInfo.getEntityClass().getSimpleName());
            // 实体类包名
            data.put("entityPackage", entityClassInfo.getEntityClass().getName());
            // 首字母小写的实体类名
            String lowEntityName = StrUtils.strFirstLowerCase(entityClassInfo.getEntityClass().getSimpleName());
            data.put("lowEntityName", lowEntityName);

            // 类中文名称
            data.put("classNote", entityClassInfo.getClassNote());

            // entity包下的分目录
            String relativePackage = "";
            String entityClassPackage = entityClassInfo.getEntityClass().getTypeName().replaceAll("\\." + entityClassInfo.getEntityClass().getSimpleName(), "");
            if(entityClassPackage.indexOf(config.getScanPackage()) >= 0){
                relativePackage = entityClassPackage.replaceAll(config.getProjectPackage() + "." + config.getScanPackage(), "");
            }

            // 根据entity分包生成request mapping
            String relativeMapping = relativePackage.replaceAll("/", "").replaceAll("\\.", "/");
            relativeMapping = relativeMapping + "/" + lowEntityName;
            data.put("relativeMapping", relativeMapping);

            configMapList.add(data);
        }

        boolean rest = FreemarkerUtil.templateProcessAndSave(
                new HashMap<String, Object>(){{put("configMapList", configMapList);}},
                fmConfig, template,
                config.getOutPath() + CodeGenerateConfig.OUT_PATH_RELATIVE_HTML_CONFIG,
                "htmlConfig.js"
        );

        if(!rest){
            log.info("html config 文件输出失败");
        }
    }

    /**
     * 模板处理和生成文件保存
     * @param templateName
     * @param entityClass
     * @param fmConfig
     * @param codeGenerateConfig
     */
    private static void processTemplate(String templateName, EntityClassInfo entityClassInfo,
                                        Configuration fmConfig, CodeGenerateConfig codeGenerateConfig,
                                        String outPutPath){
        Template template;
        try{
            template = fmConfig.getTemplate(templateName);
        }catch (Exception e){
            log.error("无法获取模板: " + e.getMessage());
            return;
        }

        String lowEntityName = StrUtils.strFirstLowerCase(entityClassInfo.getEntityClass().getSimpleName());

        String htmlOutPutPath = codeGenerateConfig.getOutPath() + CodeGenerateConfig.OUT_PATH_RELATIVE_HTML + File.separator
                + lowEntityName + File.separator;

        // 根据模板生成文件并保存
        boolean rest = false;
        StringBuilder fileName = new StringBuilder(entityClassInfo.getEntityClass().getSimpleName());
        switch (templateName){
            // Bean生成
            case TEMPLATE_NAME_BEAN: {
                fileName.append("Bean.java");
                log.info("输出文件: " + outPutPath + File.separator + fileName);
                rest = FreemarkerUtil.templateProcessAndSave(
                        getTemplateData(templateName, codeGenerateConfig, entityClassInfo),
                        fmConfig, template,
                        outPutPath,
                        fileName.toString()
                );
                break;
            }
            // Dao接口生成
            case TEMPLATE_NAME_DAO: {
                fileName.append("Mapper.java");
                log.info("输出文件: " + outPutPath + File.separator + fileName);
                rest = FreemarkerUtil.templateProcessAndSave(
                        getTemplateData(templateName, codeGenerateConfig, entityClassInfo),
                        fmConfig, template,
                        outPutPath,
                        fileName.toString()
                );
                break;
            }
            // Service 接口生成
            case TEMPLATE_NAME_SERVICE: {
                fileName.append("Service.java");
                log.info("输出文件: " + outPutPath + File.separator + fileName);
                rest = FreemarkerUtil.templateProcessAndSave(
                        getTemplateData(templateName, codeGenerateConfig, entityClassInfo),
                        fmConfig, template,
                        outPutPath,
                        fileName.toString()
                );
                break;
            }
            // ServiceImpl 接口实现类生成
            case TEMPLATE_NAME_SERVICE_IMPL: {
                fileName.append("ServiceImpl.java");
                log.info("输出文件: " + outPutPath + File.separator + fileName);
                rest = FreemarkerUtil.templateProcessAndSave(
                        getTemplateData(templateName, codeGenerateConfig, entityClassInfo),
                        fmConfig, template,
                        outPutPath,
                        fileName.toString()
                );
                break;
            }
            // 控制器生成
            case TEMPLATE_NAME_CONTROLLER: {
                fileName.append("Controller.java");
                log.info("输出文件: " + outPutPath + File.separator + fileName);
                rest = FreemarkerUtil.templateProcessAndSave(
                        getTemplateData(templateName, codeGenerateConfig, entityClassInfo),
                        fmConfig, template,
                        outPutPath,
                        fileName.toString()
                );
                break;
            }
            // 前端 - Add页面 生成
            case TEMPLATE_NAME_PAGE_ADD: {
                fileName = new StringBuilder("Add.vue");
                log.info("输出文件: " + htmlOutPutPath + fileName);
                rest = FreemarkerUtil.templateProcessAndSave(
                        getTemplateData(templateName, codeGenerateConfig, entityClassInfo),
                        fmConfig, template,
                        htmlOutPutPath,
                        fileName.toString()
                );
                break;
            }
            // 前端 - Edit页面 生成
            case TEMPLATE_NAME_PAGE_EDIT: {
                fileName = new StringBuilder("Edit.vue");
                log.info("输出文件: " + htmlOutPutPath + fileName);
                rest = FreemarkerUtil.templateProcessAndSave(
                        getTemplateData(templateName, codeGenerateConfig, entityClassInfo),
                        fmConfig, template,
                        htmlOutPutPath,
                        fileName.toString()
                );
                break;
            }
            // 前端 - List页面 生成
            case TEMPLATE_NAME_PAGE_LIST: {
                fileName = new StringBuilder("List.vue");
                log.info("输出文件: " + htmlOutPutPath + fileName);
                rest = FreemarkerUtil.templateProcessAndSave(
                        getTemplateData(templateName, codeGenerateConfig, entityClassInfo),
                        fmConfig, template,
                        htmlOutPutPath,
                        fileName.toString()
                );
                break;
            }
        }

        if(!rest){
            log.info("输出文件失败: " + fileName);
        }
    }

    /**
     * 获取文件输出路径
     * @param config
     * @param entityClass
     * @return
     */
    private static String getOutPutPath(String templateName, CodeGenerateConfig config, Class entityClass){
        // Entity 扫描 包路径
        String entityScanBackage = config.getProjectPackage() + "." + config.getScanPackage();

        // entity包下的分目录
        String relativePath = "";
        String entityClassPackage = entityClass.getTypeName().replaceAll("\\." + entityClass.getSimpleName(), "");
        if(entityClassPackage.indexOf(entityScanBackage) >= 0){
            relativePath = entityClassPackage.replaceAll(entityScanBackage, "")
                    .replaceAll("\\.", File.separator + File.separator);
        }

        // 构造输出路径 eg. 项目根目录/src/main/java/
        String outPutPath = config.getProjectRootPath() + File.separator +
                "src" + File.separator + "main" + File.separator + "java" + File.separator;

        // 文件类型路径
        String fileTypePath = "";
        switch (templateName){
            case TEMPLATE_NAME_BEAN: {
                fileTypePath = config.getOutPackageBean();
                break;
            }
            case TEMPLATE_NAME_DAO: {
                fileTypePath = config.getOutPackageDao();
                break;
            }
            case TEMPLATE_NAME_SERVICE: {
                fileTypePath = config.getOutPackageService();
                break;
            }
            case TEMPLATE_NAME_SERVICE_IMPL: {
                fileTypePath = config.getOutPackageServiceimpl();
                break;
            }
            case TEMPLATE_NAME_CONTROLLER: {
                fileTypePath = config.getOutPackageController();
                break;
            }
            case TEMPLATE_NAME_PAGE_ADD:
            case TEMPLATE_NAME_PAGE_EDIT:
            case TEMPLATE_NAME_PAGE_LIST: {
                return config.getOutPath() + CodeGenerateConfig.OUT_PATH_RELATIVE_HTML + relativePath;
            }
            case TEMPLATE_NAME_SQL: {
                return config.getOutPath() + CodeGenerateConfig.OUT_PATH_RELATIVE_SQL;
            }
        }

        // 包路径
        String packagePath = (config.getProjectPackage() + "." + fileTypePath)
                .replaceAll("\\.", File.separator + File.separator);

        return outPutPath + packagePath + relativePath;
    }

    /**
     * 初始化
     * @param prop
     * @throws IllegalArgumentException
     */
    private static Configuration init(CodeGenerateConfig config){
        if(config == null){
            log.error("配置不能为空");
            return null;
        }

        // doc date
        if(ObjectUtils.isEmpty(config.getDocDate())){
            config.setDocDate(TimeUtil.DATE_FORMAT_DAY_1.format(new Date()));
        }

        log.info("配置信息: " + config);

        Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);

        configuration.setEncoding(Locale.CHINA, "utf-8");

        return configuration;
    }
}
