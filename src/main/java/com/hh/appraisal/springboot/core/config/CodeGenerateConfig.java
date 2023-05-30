package com.hh.appraisal.springboot.core.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 代码生成器相关配置
 * @author gaigai
 * @date 2021/02/01
 */
@Data
@Component
public class CodeGenerateConfig {

    /**
     * SQL文件输出相对路径
     */
    public static final String OUT_PATH_RELATIVE_SQL = "/sql";

    /**
     * 前端文件输出相对路径
     */
    public static final String OUT_PATH_RELATIVE_HTML = "/html";

    /**
     * 前端配置文件输出相对路径
     */
    public static final String OUT_PATH_RELATIVE_HTML_CONFIG = "/htmlConfig";

    /**
     * 类注释 作者姓名
     */
    @Value("${generate.doc.author:gaigai}")
    private String docAuthor;

    /**
     * 类注释 日期
     */
    @Value("${generate.doc.date:2021/06/29}")
    private String docDate;

    /**
     * 项目根目录
     * 即pom.xml所在根目录
     */
    @Value("${generate.project.rootPath:}")
    private String projectRootPath;

    /**
     * 项目包根路径
     * 即为项目启动类所在包路径
     */
    @Value("${generate.project.package:com.hh.appraisal.springboot}")
    private String projectPackage;

    /**
     * 实体扫描包路径
     */
    @Value("${generate.scan.package:entity}")
    private String scanPackage;

    /**
     * 生成bean代码存放包路径
     */
    @Value("${generate.out.package.bean:bean}")
    private String outPackageBean;

    /**
     * 生成控制器代码存放包路径
     */
    @Value("${generate.out.package.controller:controller}")
    private String outPackageController;

    /**
     * 生成dao接口代码存放包路径
     */
    @Value("${generate.out.package.dao:dao}")
    private String outPackageDao;

    /**
     * 生成Service接口代码存放包路径
     */
    @Value("${generate.out.package.service:service}")
    private String outPackageService;

    /**
     * 生成Service实现类代码存放包路径
     */
    @Value("${generate.out.package.serviceimpl:service.impl}")
    private String outPackageServiceimpl;

    /**
     * 前端代码、前端配置、sql文件的保存路径
     */
    @Value("${generate.out.path:/Users/gaigai/Desktop/test}")
    private String outPath;

}
