package com.hh.appraisal.springboot.controller;

import io.swagger.annotations.*;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hh.appraisal.springboot.core.annotation.NoPermission;
import com.hh.appraisal.springboot.core.baen.PageBean;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.core.constant.RestCode;
import com.hh.appraisal.springboot.entity.Question;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.hh.appraisal.springboot.bean.QuestionBean;
import com.hh.appraisal.springboot.bean.QuestionOptionsBean;
import com.hh.appraisal.springboot.service.QuestionOptionsService;
import com.hh.appraisal.springboot.service.QuestionService;
import com.wuwenze.poi.ExcelKit;
import com.wuwenze.poi.handler.ExcelReadHandler;
import com.wuwenze.poi.pojo.ExcelErrorField;

import cn.hutool.core.collection.CollectionUtil;

/**
 * 题目选项表 控制器
 * 
 * @author gaigai
 * @date 2021/06/26
 */
@Api(tags = "题目选项表")
@RestController
@RequestMapping("/questionOptions")
public class QuestionOptionsController {

	private final QuestionOptionsService questionOptionsService;

	@Autowired
	private QuestionService questionService;

	public QuestionOptionsController(QuestionOptionsService questionOptionsService) {
		this.questionOptionsService = questionOptionsService;
	}

	/**
	 * 分页查询
	 * 
	 * @param bean
	 * @return
	 */
	@ApiOperation(value = "分页查询", response = RestBean.class)
	@ApiOperationSupport(ignoreParameters = { "code", "createTime", "updateTime", "valid" })
	@ApiImplicitParam(paramType = "header", dataType = "String", name = AuthConstant.TOKEN, value = "鉴权token", required = true)
	@RequestMapping(value = "/listPage", method = { RequestMethod.POST })
	public RestBean listPage(QuestionOptionsBean bean, PageBean pageBean) {
		Page<QuestionOptionsBean> page = questionOptionsService.findPage(bean, pageBean);
		if (page == null || CollectionUtils.isEmpty(page.getRecords())) {
			return RestBean.ok(new Page<>());
		}

		
		// 处理列表逻辑....

		return RestBean.ok(page);
	}
	
	/**
	 * 根据问题查选项
	 * 
	 * @param bean
	 * @return
	 */
	@ApiOperation(value = "查询问题选项", response = RestBean.class)
	@ApiOperationSupport(ignoreParameters = { "code", "createTime", "updateTime", "valid" })
	@ApiImplicitParam(paramType = "header", dataType = "String", name = AuthConstant.TOKEN, value = "鉴权token", required = true)
	@RequestMapping(value = "/listByQuestion", method = { RequestMethod.POST })
	public RestBean listByQuestion(QuestionOptionsBean bean) {
		List<QuestionOptionsBean> list = questionOptionsService.findList(bean);
		if (list == null ||list.size()==0) {
			return RestBean.ok();
		}
		// 处理列表逻辑....
		return RestBean.ok(list);
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
		QuestionOptionsBean bean = questionOptionsService.findByCode(code);
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
	public RestBean add(QuestionOptionsBean bean) {
		return RestBean.ok(questionOptionsService.add(bean));
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
	public RestBean update(QuestionOptionsBean bean) {
		if (bean == null || ObjectUtils.isEmpty(bean.getCode())) {
			return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
		}
		return RestBean.ok(questionOptionsService.updateByCode(bean));
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
		return RestBean.ok(questionOptionsService.deleteByCode(code));
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
		questionOptionsService.deleteByCode(codeList);
		return RestBean.ok();
	}

	/**
	 * 导入问题选项
	 * @param file
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/importQuestionOption", method = RequestMethod.POST)
	@NoPermission(noLogin = true)
	public RestBean importUser(@RequestParam MultipartFile file) throws IOException {
		long beginMillis = System.currentTimeMillis();
		List<QuestionOptionsBean> successList = Lists.newArrayList();
		List<Map<String, Object>> errorList = Lists.newArrayList();
		ExcelKit.$Import(QuestionOptionsBean.class).readXlsx(file.getInputStream(),
				new ExcelReadHandler<QuestionOptionsBean>() {
					@Override
					public void onSuccess(int sheetIndex, int rowIndex, QuestionOptionsBean entity) {
						Question question = questionService
								.getOne(new QueryWrapper<Question>().eq("QUESTION_CODE", entity.getQuestionCode()));
						if(question==null) {
							List<ExcelErrorField> errorFields=new ArrayList<ExcelErrorField>();
							ExcelErrorField filed=new ExcelErrorField();
							filed.setColumn("题目标识");
							filed.setColumn("questionCode");
							filed.setErrorMessage("题目标识有误");
							Map<String, Object> errormap = new HashMap<String, Object>();
							errorFields.add(filed);
							errormap.put("sheetIndex", sheetIndex);
							errormap.put("rowIndex", rowIndex);
							errormap.put("errorFields", errorFields);
						}
						entity.setQuestionCode(question.getCode());
						successList.add(entity); // 单行读取成功，加入入库队列。
						questionOptionsService.add(entity);
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
		// TODO: 执行successList的入库操作。
		Map<String, Object> successMap = new HashMap<String, Object>();
		successMap.put("data", successList);
		successMap.put("haveError", !CollectionUtil.isEmpty(errorList));
		successMap.put("error", errorList);
		successMap.put("timeConsuming", (System.currentTimeMillis() - beginMillis) / 1000L);
		return RestBean.ok(successMap);
	}

}
