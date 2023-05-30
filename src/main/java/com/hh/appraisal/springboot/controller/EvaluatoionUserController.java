package com.hh.appraisal.springboot.controller;

import io.swagger.annotations.*;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hh.appraisal.springboot.core.annotation.NoPermission;
import com.hh.appraisal.springboot.core.baen.PageBean;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.core.constant.RestCode;
import com.hh.appraisal.springboot.dao.EvaluatoionUserMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.hh.appraisal.springboot.core.constant.AuthConstant;
import org.springframework.web.bind.annotation.RequestMethod;
import com.hh.appraisal.springboot.bean.EvaluatoionUserBean;
import com.hh.appraisal.springboot.bean.QuestionBean;
import com.hh.appraisal.springboot.bean.ReportBean;
import com.hh.appraisal.springboot.service.EvaluatoionUserService;
import com.hh.appraisal.springboot.service.UserAnswersService;
import com.hh.appraisal.springboot.utils.ZipUtils;
import com.wuwenze.poi.ExcelKit;

import cn.hutool.core.util.ZipUtil;

/**
 * 测评用户信息表 控制器
 * 
 * @author gaigai
 * @date 2021/06/26
 */
@Api(tags = "测评用户信息表")
@RestController
@RequestMapping("/evaluatoionUser")
public class EvaluatoionUserController {

	private final EvaluatoionUserService evaluatoionUserService;

	@Autowired EvaluatoionUserMapper evaluatoionUserMapper;
	
	public EvaluatoionUserController(EvaluatoionUserService evaluatoionUserService) {
		this.evaluatoionUserService = evaluatoionUserService;
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
	public RestBean listPage(EvaluatoionUserBean bean, PageBean pageBean) {
		Page<EvaluatoionUserBean> page = evaluatoionUserService.findPage(bean, pageBean);
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
		EvaluatoionUserBean bean = evaluatoionUserService.findByCode(code);
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
	public RestBean add(EvaluatoionUserBean bean) {
		return RestBean.ok(evaluatoionUserService.add(bean));
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
	public RestBean update(EvaluatoionUserBean bean) {
		if (bean == null || ObjectUtils.isEmpty(bean.getCode())) {
			return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
		}
		if(bean.getSchool().length()>200) {
			return RestBean.error("学校填写长度不超过200字符！");
		}
		if(bean.getSchoolClass().length()>200) {
			return RestBean.error("班级填写长度不超过200字符！");
		}
		return RestBean.ok(evaluatoionUserService.updateByCode(bean));
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
		return RestBean.ok(evaluatoionUserService.deleteByCode(code));
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
		evaluatoionUserService.deleteByCode(codeList);
		return RestBean.ok();
	}

	/**
	 * 导出EXCEL
	 * 
	 * @param codeList
	 * @return
	 * @throws IOException 
	 */
	@ApiOperation(value = "导出")
    @NoPermission(noLogin = true)
	@ApiImplicitParam(paramType = "header", dataType = "String", name = AuthConstant.TOKEN, value = "鉴权token", required = true)
	@RequestMapping(value = "/excel", method = { RequestMethod.GET })
	public void export(EvaluatoionUserBean bean, HttpServletResponse response) throws IOException {
		List<EvaluatoionUserBean> page = evaluatoionUserService.findList(bean);
		ExcelKit.$Export(EvaluatoionUserBean.class, response).downXlsx(page, false);
	}

	/**
	 * 批量下载
	 * 
	 * @param codeList
	 * @return
	 */
	@ApiOperation(value = "批量下载")
//	@NoPermission(noLogin = true)
	@ApiImplicitParam(paramType = "header", dataType = "String", name = AuthConstant.TOKEN, value = "鉴权token", required = true)
	@RequestMapping(value = "/batchDownload", method = { RequestMethod.GET })
	@CrossOrigin
	public RestBean batchDownload(@RequestParam("codeList") List<String> codeList, HttpServletRequest request,
			HttpServletResponse response) {
		if (ObjectUtils.isEmpty(codeList)) {
			return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
		}
		List<String> list = new ArrayList<>();
		List<EvaluatoionUserBean> evaluatoionUserBeanList = evaluatoionUserService.findByCodeList(codeList);
		if (evaluatoionUserBeanList != null && evaluatoionUserBeanList.size() > 0) {
			for (int i = 0; i < evaluatoionUserBeanList.size(); i++) {
				EvaluatoionUserBean bean = evaluatoionUserBeanList.get(i);
				if (bean.getUrl() != null) {
					list.add(bean.getUrl());
				}
			}
			if (list != null && list.size() > 0) {
				ZipUtils.downloadZipFiles(response, list, "report" + System.currentTimeMillis() + ".zip");
			}
		}
		return null;
	}
	
	

	/**
	 * 报表数据
	 * 
	 * @param code
	 * @return
	 */
	@ApiOperation(value = "报表数据", response = RestBean.class)
//	@NoPermission(noLogin = true)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "header", dataType = "String", name = AuthConstant.TOKEN, value = "鉴权token", required = true),
			@ApiImplicitParam(dataType = "String", name = "code", value = "唯一标识", required = true) })
	@RequestMapping(value = "/report", method = { RequestMethod.POST })
	public RestBean report() {
		ReportBean reportBean=evaluatoionUserMapper.getReport();
		return RestBean.ok(reportBean);
	}
	

}
