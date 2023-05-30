package com.hh.appraisal.springboot.core.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Map;

/**
 * freemarker工具
 * @author gaigai
 * @date 2021/02/01
 */
@Slf4j
public class FreemarkerUtil {

    /**
     * 根据模板生成文件
     * @param dataMap 模板对应数据
     * @param configuration
     * @param template
     * @param saveFilePath 模板文件路径
     * @param saveFileName 模板文件名称
     */
    public static boolean templateProcessAndSave(Map<String,Object> dataMap,
                                         Configuration configuration,Template template,
                                         String saveFilePath,String saveFileName){
        boolean rest = false;
        Writer out = null;
        try {
            // 建立目录
            new File(saveFilePath).mkdirs();

            //输出文件路径及名称
            File outFile = new File(saveFilePath + File.separator + saveFileName);
            if(outFile.exists()){
                log.error("已有同名文件存在，不予处理: " + outFile.getName());
                return true;
            }

            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "utf-8"), 10240);
            template.process(dataMap, out);
            rest = true;
        } catch (Exception e) {
            log.error("freemarker处理文件异常: " + e);
        }finally {
            try{
                if(out != null){
                    out.close();
                }
            }catch (Exception e){
                log.error("freemarker处理文件，释放资源异常: " + e);
            }
        }
        return rest;
    }

}
