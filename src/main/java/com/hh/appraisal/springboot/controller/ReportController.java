package com.hh.appraisal.springboot.controller;

import io.swagger.annotations.*;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hh.appraisal.springboot.core.baen.PageBean;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.core.constant.RestCode;
import com.hh.appraisal.springboot.entity.ReportConfig;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.hh.appraisal.springboot.core.constant.AuthConstant;
import org.springframework.web.bind.annotation.RequestMethod;
import com.hh.appraisal.springboot.bean.ReportBean;
import com.hh.appraisal.springboot.bean.ReportConfigBean;
import com.hh.appraisal.springboot.bean.ReportConfigCatBean;
import com.hh.appraisal.springboot.service.ReportConfigService;
import com.hh.appraisal.springboot.service.ReportService;

/**
 * 报告 控制器
 * 
 * @author gaigai
 * @date 2023/06/02
 */
@Api(tags = "报告")
@RestController
@RequestMapping("/report")
public class ReportController {

	private final ReportService reportService;

	@Autowired
	private ReportConfigService reportConfigService;

	public ReportController(ReportService reportService) {
		this.reportService = reportService;
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
	public RestBean listPage(ReportBean bean, PageBean pageBean) {
		Page<ReportBean> page = reportService.findPage(bean, pageBean);
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
		ReportBean bean = reportService.findByCode(code);
		List<ReportConfig> reportConfigList = reportConfigService
				.list(new QueryWrapper<ReportConfig>().eq("REPORT_CODE", bean.getCode()));
		// 构建返回结构
		List<ReportConfigCatBean> listcat = new ArrayList<ReportConfigCatBean>();
		Map<String, List<ReportConfig>> map = new HashMap<String, List<ReportConfig>>();
		for (ReportConfig reportConfig : reportConfigList) {
			String name = reportConfig.getReportConfigPartName();
			if (map.containsKey(name)) {
				List<ReportConfig> list = map.get(name);
				list.add(reportConfig);
			} else {
				List<ReportConfig> list = new ArrayList<ReportConfig>();
				list.add(reportConfig);
				map.put(name, list);
			}
		}
		Iterator<Entry<String, List<ReportConfig>>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, List<ReportConfig>> next = iterator.next();
			ReportConfigCatBean cat = new ReportConfigCatBean();
			cat.setReportConfigPartName(next.getKey());
			cat.setReportConfigPartCode(next.getValue().get(0).getReportConfigPartCode());
			List<ReportConfigBean> listbean = new ArrayList<ReportConfigBean>();
			for (int i = 0; i < next.getValue().size(); i++) {
				ReportConfigBean restBean = ReportConfigBean.builder().build();
				BeanUtils.copyProperties(next.getValue().get(i), restBean);
				listbean.add(restBean);
			}
			cat.setReportConfig(listbean);
			listcat.add(cat);
		}
		bean.setReportConfigCatList(listcat);
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
	public RestBean add(ReportBean bean) {
		return RestBean.ok(reportService.add(bean));
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
	public RestBean update(@RequestBody JSONObject bean) {
		ReportBean reportBean= bean.toJavaObject(ReportBean.class);
		if (bean == null || ObjectUtils.isEmpty(reportBean.getCode())) {
			return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
		}
		return RestBean.ok(reportService.updateByCode(reportBean));
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
		return RestBean.ok(reportService.deleteByCode(code));
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
		reportService.deleteByCode(codeList);
		return RestBean.ok();
	}

}
