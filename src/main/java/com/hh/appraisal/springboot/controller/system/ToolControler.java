package com.hh.appraisal.springboot.controller.system;

import com.hh.appraisal.springboot.core.annotation.NoPermission;
import com.hh.appraisal.springboot.core.baen.EntityClassInfo;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.core.config.CodeGenerateConfig;
import com.hh.appraisal.springboot.core.constant.RestCode;
import com.hh.appraisal.springboot.core.utils.CodeGenerateUtil;
import com.hh.appraisal.springboot.service.system.SysApiService;
import com.hh.appraisal.springboot.service.system.SysFunctionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 工具控制器
 * @author gaigai
 * @createTime 2021/06/08
 */
@Slf4j
@RestController
@RequestMapping("/tool")
public class ToolControler {

    @Autowired
    private SysFunctionService functionService;

    @Autowired
    private SysApiService apiService;

    @Autowired
    private CodeGenerateConfig codeGenerateConfig;

    /**
     * 代码生成
     * @param needSqlInsert 是否需要生成sql，第一次执行传true，再次执行必须传false否则会造成数据重复
     * @return
     */
    @NoPermission(noLogin = true)
    @RequestMapping("/codeGenerate")
    public RestBean codeGenerate(Boolean needSqlInsert){
//    	needSqlInsert=true;
        /**
         * 生成相关代码以及实体类建表语句
         * 该方法生成文件时，遇到重名文件，不会执行覆盖，而是跳过
         */
        RestBean genRest = CodeGenerateUtil.generate(codeGenerateConfig, new ArrayList<String>(){{
            /**
             * 如果只需要处理部分实体类，则这里添加需要处理的实体类，未声明的工具类不会处理
             * 如果不添加任何类，工具类将处理扫描到的全部实体类(除系统实体类以外)
             * 下面的WxMiniAppUser为案例，系统已有
             */
        	add("com.hh.appraisal.springboot.entity.Report");
//        	add("com.hh.appraisal.springboot.entity.ReportConfig");
        }});
        if(RestCode.DEFAULT_SUCCESS.getCode() != genRest.getCode()){
            log.error("生成代码失败: " + genRest);
            return RestBean.error("生成代码失败: " + genRest.getMessage());
        }

        /**
         * 将菜单、功能、api的数据插入数据库
         * TODO: 注意该方法只能执行一次，多次执行将造成重复数据！
         */
        if(needSqlInsert != null && needSqlInsert == true){
            List<EntityClassInfo> entityClassInfoList = (List<EntityClassInfo>) genRest.getData();
            RestBean otherGenRest = CodeGenerateUtil.generateOthers(entityClassInfoList, codeGenerateConfig);
            if(RestCode.DEFAULT_SUCCESS.getCode() != otherGenRest.getCode()){
                log.error("插入数据进数据库失败: " + otherGenRest);
                return RestBean.error("插入数据进数据库失败: " + otherGenRest.getMessage());
            }
        }

        return RestBean.ok();
    }

}
