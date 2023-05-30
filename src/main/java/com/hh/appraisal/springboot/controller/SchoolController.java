package com.hh.appraisal.springboot.controller;

import io.swagger.annotations.*;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hh.appraisal.springboot.core.annotation.NoPermission;
import com.hh.appraisal.springboot.core.baen.PageBean;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.core.config.FileConfig;
import com.hh.appraisal.springboot.core.constant.RestCode;
import com.hh.appraisal.springboot.model.FileResult;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.hh.appraisal.springboot.core.constant.AuthConstant;
import org.springframework.web.bind.annotation.RequestMethod;
import com.hh.appraisal.springboot.bean.SchoolBean;
import com.hh.appraisal.springboot.service.SchoolService;

/**
 * 学校 控制器
 * @author gaigai
 * @date 2021/06/26
 */
@Api(tags = "学校")
@RestController
@RequestMapping("/school")
public class SchoolController {

    private final SchoolService schoolService;
    private final FileConfig fileConfig;

    public SchoolController(SchoolService schoolService,FileConfig fileConfig) {
            this.schoolService = schoolService;
            this.fileConfig=fileConfig;
    }       
     /**
     * 分页查询
     * @param bean
     * @return
     */
    @ApiOperation(value="分页查询", response = RestBean.class)
    @ApiOperationSupport(ignoreParameters = {"code","createTime","updateTime","valid"})
    @ApiImplicitParam(paramType = "header", dataType = "String", name = AuthConstant.TOKEN, value = "鉴权token", required = true)
    @RequestMapping(value = "/listPage", method = {RequestMethod.POST})
    public RestBean listPage(SchoolBean bean, PageBean pageBean){
        Page<SchoolBean> page = schoolService.findPage(bean,pageBean);
        if(page == null || CollectionUtils.isEmpty(page.getRecords())) {
            return RestBean.ok(new Page<>());
        }

        // 处理列表逻辑....

        return RestBean.ok(page);
    }

    /**
     * 查看详情
     * @param code
     * @return
     */
    @ApiOperation(value="查看详情", response = RestBean.class)
    @ApiImplicitParams({
      @ApiImplicitParam(paramType = "header", dataType = "String", name = AuthConstant.TOKEN, value = "鉴权token", required = true),
      @ApiImplicitParam(dataType = "String", name = "code", value = "唯一标识", required = true)
    })
    @RequestMapping(value = "/detail", method = {RequestMethod.POST})
    public RestBean detail(String code) {
        if(ObjectUtils.isEmpty(code)){
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }
        SchoolBean bean = schoolService.findByCode(code);
        return RestBean.ok(bean);
    }

    /**
     * 新增一条记录
     * @param bean
     * @return
     */
    @ApiOperation(value="新增一条记录", response = RestBean.class)
    @ApiOperationSupport(ignoreParameters = {"code","createTime","updateTime","valid"})
    @ApiImplicitParam(paramType = "header", dataType = "String", name = AuthConstant.TOKEN, value = "鉴权token", required = true)
    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    public RestBean add(SchoolBean bean){
        return RestBean.ok(schoolService.add(bean));
    }

    /**
     * 根据唯一标识更新一条记录
     * @param bean
     * @return
     */
    @ApiOperation(value="根据唯一标识更新一条记录", response = RestBean.class)
    @ApiImplicitParam(paramType = "header", dataType = "String", name = AuthConstant.TOKEN, value = "鉴权token", required = true)
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public RestBean update(SchoolBean bean){
        if(bean == null || ObjectUtils.isEmpty(bean.getCode())) {
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }
        return RestBean.ok(schoolService.updateByCode(bean));
    }

    /**
     * 根据唯一标识删除一条记录
     * @param code
     * @return
     */
    @ApiOperation(value="根据唯一标识删除一条记录", response = RestBean.class)
    @ApiImplicitParams({
    @ApiImplicitParam(paramType = "header", dataType = "String", name = AuthConstant.TOKEN, value = "鉴权token", required = true),
    @ApiImplicitParam(dataType = "String", name = "code", value = "唯一标识", required = true)
    })
    @RequestMapping(value = "/deleteByCode", method = {RequestMethod.POST})
    public RestBean delete(String code){
        if(ObjectUtils.isEmpty(code)){
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }
        return RestBean.ok(
            schoolService.deleteByCode(code)
        );
    }

    /**
     * 根据唯一标识集合批量删除
     * @param codeList
     * @return
     */
    @ApiOperation(value="批量删除", response = RestBean.class)
    @ApiImplicitParam(paramType = "header", dataType = "String", name = AuthConstant.TOKEN, value = "鉴权token", required = true)
    @RequestMapping(value = "/batchDeleteByCodeList", method = {RequestMethod.POST})
    public RestBean batchDelete(@RequestParam("codeList") List<String> codeList){
        if(ObjectUtils.isEmpty(codeList)){
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }
        schoolService.deleteByCode(codeList);
        return RestBean.ok();
    }
    
    /**
     * 文件上传
     * @param picture
     * @param request
     * @return
     */
    @RequestMapping("/uploadlogo")
    @NoPermission(noLogin = true)
    @ApiOperation(value="上传logo", response = FileResult.class)
    public FileResult upload(@RequestParam("picture") MultipartFile picture, HttpServletRequest request) {
        //获取文件在服务器的储存位置
        String path =fileConfig.getFolderPath();
        File filePath = new File(path);
        if (!filePath.exists() && !filePath.isDirectory()) {
            filePath.mkdir();
        }
        //获取原始文件名称(包含格式)
        String originalFileName = picture.getOriginalFilename();
        //获取文件类型，以最后一个`.`为标识
        String type = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        //获取文件名称（不包含格式）
        String name = originalFileName.substring(0, originalFileName.lastIndexOf("."));
        String fileName = new Date().getTime() + name + "." + type;
        //在指定路径下创建一个文件
        File targetFile = new File(path, fileName);
        //将文件保存到服务器指定位置
        try {
            picture.transferTo(targetFile);
            //将文件在服务器的存储路径返回
            return new FileResult(true,"/file/logo/"+fileName,"/file/logo/"+fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new FileResult(false, "上传失败："+e.getMessage(),"");
        }
    }


}
