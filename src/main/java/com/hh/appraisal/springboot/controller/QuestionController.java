package com.hh.appraisal.springboot.controller;

import io.swagger.annotations.*;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hh.appraisal.springboot.core.annotation.NoPermission;
import com.hh.appraisal.springboot.core.baen.PageBean;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.core.constant.RestCode;
import com.hh.appraisal.springboot.entity.Question;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.google.common.collect.Lists;
import com.hh.appraisal.springboot.core.constant.AuthConstant;
import org.springframework.web.bind.annotation.RequestMethod;
import com.hh.appraisal.springboot.bean.QuestionBean;
import com.hh.appraisal.springboot.service.QuestionService;
import com.wuwenze.poi.ExcelKit;
import com.wuwenze.poi.handler.ExcelReadHandler;
import com.wuwenze.poi.pojo.ExcelErrorField;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;

/**
 * 问题 控制器
 * 
 * @author gaigai
 * @date 2021/06/26
 */
@Api(tags = "问题")
@RestController
@RequestMapping("/question")
public class QuestionController {

	private final QuestionService questionService;

	public QuestionController(QuestionService questionService) {
		this.questionService = questionService;
	}

	/**
	 * 分页查询
	 * 
	 * @param bean
	 * @return
	 */
	@NoPermission(noLogin = true)
	@ApiOperation(value = "分页查询", response = RestBean.class)
	@ApiOperationSupport(ignoreParameters = { "code", "createTime", "updateTime", "valid" })
//    @ApiImplicitParam(paramType = "header", dataType = "String", name = AuthConstant.TOKEN, value = "鉴权token", required = true)
	@RequestMapping(value = "/listPage", method = { RequestMethod.POST })
	public RestBean listPage(QuestionBean bean, PageBean pageBean) {
		Page<QuestionBean> page = questionService.findPage(bean, pageBean);
		if (page == null || CollectionUtils.isEmpty(page.getRecords())) {
			return RestBean.ok(new Page<>());
		}
		// 处理列表逻辑....
		return RestBean.ok(page);
	}

	/**
	 * 查看详情
	 * 
	 * @param code
	 * @return
	 */
	@ApiOperation(value = "查看详情", response = RestBean.class)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "header", dataType = "String", name = AuthConstant.TOKEN, value = "鉴权token", required = true),
			@ApiImplicitParam(dataType = "String", name = "code", value = "唯一标识", required = true) })
	@RequestMapping(value = "/detail", method = { RequestMethod.POST })
	public RestBean detail(String code) {
		if (ObjectUtils.isEmpty(code)) {
			return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
		}
		QuestionBean bean = questionService.findByCode(code);
		return RestBean.ok(bean);
	}

	/**
	 * 新增一条记录
	 * 
	 * @param bean
	 * @return
	 */
	@ApiOperation(value = "新增一条记录", response = RestBean.class)
	@ApiOperationSupport(ignoreParameters = { "code", "createTime", "updateTime", "valid" })
	@ApiImplicitParam(paramType = "header", dataType = "String", name = AuthConstant.TOKEN, value = "鉴权token", required = true)
	@RequestMapping(value = "/add", method = { RequestMethod.POST })
	public RestBean add(QuestionBean bean) {
		return RestBean.ok(questionService.add(bean));
	}

	/**
	 * 根据唯一标识更新一条记录
	 * 
	 * @param bean
	 * @return
	 */
	@ApiOperation(value = "根据唯一标识更新一条记录", response = RestBean.class)
	@ApiImplicitParam(paramType = "header", dataType = "String", name = AuthConstant.TOKEN, value = "鉴权token", required = true)
	@RequestMapping(value = "/update", method = { RequestMethod.POST })
	public RestBean update(QuestionBean bean) {
		if (bean == null || ObjectUtils.isEmpty(bean.getCode())) {
			return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
		}
		return RestBean.ok(questionService.updateByCode(bean));
	}

	/**
	 * 根据唯一标识删除一条记录
	 * 
	 * @param code
	 * @return
	 */
	@ApiOperation(value = "根据唯一标识删除一条记录", response = RestBean.class)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "header", dataType = "String", name = AuthConstant.TOKEN, value = "鉴权token", required = true),
			@ApiImplicitParam(dataType = "String", name = "code", value = "唯一标识", required = true) })
	@RequestMapping(value = "/deleteByCode", method = { RequestMethod.POST })
	public RestBean delete(String code) {
		if (ObjectUtils.isEmpty(code)) {
			return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
		}
		return RestBean.ok(questionService.deleteByCode(code));
	}

	/**
	 * 根据唯一标识集合批量删除
	 * 
	 * @param codeList
	 * @return
	 */
	@ApiOperation(value = "批量删除", response = RestBean.class)
	@ApiImplicitParam(paramType = "header", dataType = "String", name = AuthConstant.TOKEN, value = "鉴权token", required = true)
	@RequestMapping(value = "/batchDeleteByCodeList", method = { RequestMethod.POST })
	public RestBean batchDelete(@RequestParam("codeList") List<String> codeList) {
		if (ObjectUtils.isEmpty(codeList)) {
			return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
		}
		questionService.deleteByCode(codeList);
		return RestBean.ok();
	}

	/**
	 * 导出EXCEL
	 * 
	 * @param codeList
	 * @return
	 */
	@ApiOperation(value = "导出")
	@NoPermission(noLogin = true)
////    @ApiImplicitParam(paramType = "header", dataType = "String", name = AuthConstant.TOKEN, value = "鉴权token", required = true)
	@RequestMapping(value = "/excel", method = { RequestMethod.GET })
	public void export(QuestionBean bean, HttpServletResponse response) {
		List<QuestionBean> page = questionService.findList(bean);
		ExcelKit.$Export(QuestionBean.class, response).downXlsx(page, false);
	}

	@RequestMapping(value = "/importQuestion", method = RequestMethod.POST)
	@NoPermission(noLogin = true)
	public RestBean importUser(@RequestParam MultipartFile file) throws IOException {
		long beginMillis = System.currentTimeMillis();
		List<QuestionBean> successList = Lists.newArrayList();
		List<Map<String, Object>> errorList = Lists.newArrayList();
		ExcelKit.$Import(QuestionBean.class).readXlsx(file.getInputStream(), new ExcelReadHandler<QuestionBean>() {
			@Override
			public void onSuccess(int sheetIndex, int rowIndex, QuestionBean entity) {
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
			QuestionBean entity = successList.get(i);
			try {
				questionService.addByExcel(entity);
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
