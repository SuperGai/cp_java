package com.hh.appraisal.springboot.controller;

import io.swagger.annotations.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hh.appraisal.springboot.core.annotation.NoPermission;
import com.hh.appraisal.springboot.core.baen.PageBean;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.core.constant.RestCode;
import com.hh.appraisal.springboot.entity.Divisor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.google.common.collect.Lists;
import com.hh.appraisal.springboot.core.constant.AuthConstant;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hh.appraisal.springboot.bean.DivisorBean;
import com.hh.appraisal.springboot.bean.DivisorItemBean;
import com.hh.appraisal.springboot.bean.QuestionBean;
import com.hh.appraisal.springboot.service.DivisorItemService;
import com.hh.appraisal.springboot.service.DivisorService;
import com.wuwenze.poi.ExcelKit;
import com.wuwenze.poi.handler.ExcelReadHandler;
import com.wuwenze.poi.pojo.ExcelErrorField;

import cn.hutool.core.collection.CollectionUtil;

/**
 * 因子明细 控制器
 * @author gaigai
 * @date 2021/06/26
 */
@Api(tags = "因子明细")
@RestController
@RequestMapping("/divisorItem")
public class DivisorItemController {

    private final DivisorItemService divisorItemService;
    
    private final DivisorService divisorService;

    public DivisorItemController(DivisorItemService divisorItemService,DivisorService divisorService) {
            this.divisorItemService = divisorItemService;
            this.divisorService=divisorService;
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
    public RestBean listPage(DivisorItemBean bean, PageBean pageBean){
        Page<DivisorItemBean> page = divisorItemService.findPage(bean,pageBean);
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
        DivisorItemBean bean = divisorItemService.findByCode(code);
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
    public RestBean add(DivisorItemBean bean){
        return RestBean.ok(divisorItemService.add(bean));
    }
    
    
	/**
	 * 根据指标查指标明细
	 * 
	 * @param bean
	 * @return
	 */
	@ApiOperation(value = "根据指标查指标明细", response = RestBean.class)
	@ApiOperationSupport(ignoreParameters = { "code", "createTime", "updateTime", "valid" })
	@ApiImplicitParam(paramType = "header", dataType = "String", name = AuthConstant.TOKEN, value = "鉴权token", required = true)
	@RequestMapping(value = "/listByDivisor", method = { RequestMethod.POST })
	public RestBean listByQuestion(DivisorItemBean bean) {
		List<DivisorItemBean> list = divisorItemService.findList(bean);
		if (list == null ||list.size()==0) {
			return RestBean.ok();
		}
		// 处理列表逻辑....
		return RestBean.ok(list);
	}

    /**
     * 根据唯一标识更新一条记录
     * @param bean
     * @return
     */
    @ApiOperation(value="根据唯一标识更新一条记录", response = RestBean.class)
    @ApiImplicitParam(paramType = "header", dataType = "String", name = AuthConstant.TOKEN, value = "鉴权token", required = true)
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public RestBean update(DivisorItemBean bean){
        if(bean == null || ObjectUtils.isEmpty(bean.getCode())) {
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }
        return RestBean.ok(divisorItemService.updateByCode(bean));
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
            divisorItemService.deleteByCode(code)
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
        divisorItemService.deleteByCode(codeList);
        return RestBean.ok();
    }
    
	@RequestMapping(value = "/importDivisor", method = RequestMethod.POST)
	@NoPermission(noLogin = true)
	public RestBean importUser(@RequestParam MultipartFile file) throws IOException {
		long beginMillis = System.currentTimeMillis();
		List<DivisorItemBean> successList = Lists.newArrayList();
		List<Map<String, Object>> errorList = Lists.newArrayList();
		ExcelKit.$Import(DivisorItemBean.class).readXlsx(file.getInputStream(), new ExcelReadHandler<DivisorItemBean>() {
			@Override
			public void onSuccess(int sheetIndex, int rowIndex, DivisorItemBean entity) {
				successList.add(entity); // 单行读取成功，加入入库队列。
			}
			@Override
			public void onError(int sheetIndex, int rowIndex, List<ExcelErrorField> errorFields) {
				// 读取数据失败，记录了当前行所有失败的数据
				Map<String, Object> errormap = new HashMap<String, Object>();
				errormap.put("sheetIndex", sheetIndex);
				errormap.put("rowIndex", rowIndex);
				errormap.put("errorFields", errorFields);
				errorList.add(errormap);
			}
		});
		for (int i = 0; i < successList.size(); i++) {
			DivisorItemBean entity = successList.get(i);
			try {
				Divisor divisor=divisorService.getOne(new QueryWrapper<Divisor>().eq("divisor_Name", entity.getDivisorCode()));
				entity.setDivisorCode(divisor.getCode());
				divisorItemService.add(entity);
			} catch (Exception e) {
				// TODO: handle exception
				Map<String, Object> errormap = new HashMap<String, Object>();
				errormap.put("sheetIndex", 0);
				errormap.put("rowIndex", i+1);
				List<ExcelErrorField>  error=new ArrayList<ExcelErrorField>();
				ExcelErrorField excelErrorField=new ExcelErrorField();
				excelErrorField.setColumn(e.getMessage());
				excelErrorField.setErrorMessage(e.getMessage());
				error.add(excelErrorField);
				errormap.put("errorFields", error);
				errorList.add(errormap);
			}
		}
		// TODO: 执行successList的入库操作。
		Map<String, Object> successMap = new HashMap<String, Object>();
		successMap.put("data", successList);
		successMap.put("haveError", !CollectionUtil.isEmpty(errorList));
		successMap.put("error", errorList);
		successMap.put("timeConsuming", (System.currentTimeMillis() - beginMillis) / 1000L);
		return RestBean.ok(successMap);
	}

}
