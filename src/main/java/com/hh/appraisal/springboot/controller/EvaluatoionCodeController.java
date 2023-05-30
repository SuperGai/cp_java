package com.hh.appraisal.springboot.controller;

import io.swagger.annotations.*;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hh.appraisal.springboot.core.annotation.NoPermission;
import com.hh.appraisal.springboot.core.baen.PageBean;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.core.constant.RestCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.hh.appraisal.springboot.core.constant.AuthConstant;
import org.springframework.web.bind.annotation.RequestMethod;
import com.hh.appraisal.springboot.bean.EvaluatoionCodeBean;
import com.hh.appraisal.springboot.bean.EvaluatoionUserBean;
import com.hh.appraisal.springboot.service.EvaluatoionCodeService;
import com.hh.appraisal.springboot.service.GeneraPdfAsyncTaskService;
import com.wuwenze.poi.ExcelKit;

/**
 * 测评码 控制器
 * 
 * @author gaigai
 * @date 2021/06/26
 */
@Api(tags = "测评码")
@RestController
@RequestMapping("/evaluatoionCode")
public class EvaluatoionCodeController {

	private final EvaluatoionCodeService evaluatoionCodeService;
	
	public EvaluatoionCodeController(EvaluatoionCodeService evaluatoionCodeService) {
		this.evaluatoionCodeService = evaluatoionCodeService;
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
	@ApiImplicitParam(paramType = "header", dataType = "String", name = AuthConstant.TOKEN, value = "鉴权token", required = true)
	@RequestMapping(value = "/listPage", method = { RequestMethod.POST })
	public RestBean listPage(EvaluatoionCodeBean bean, PageBean pageBean) {
		Page<EvaluatoionCodeBean> page = evaluatoionCodeService.findPage(bean, pageBean);
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
		EvaluatoionCodeBean bean = evaluatoionCodeService.findByCode(code);
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
	public RestBean add(EvaluatoionCodeBean bean) {
		return RestBean.ok(evaluatoionCodeService.add(bean));
	}

	/**
	 * 批量新增
	 * 
	 * @param bean
	 * @return
	 */
	@ApiOperation(value = "批量新增", response = RestBean.class)
	@ApiOperationSupport(ignoreParameters = { "code", "createTime", "updateTime", "valid" })
	@ApiImplicitParam(paramType = "header", dataType = "String", name = AuthConstant.TOKEN, value = "鉴权token", required = true)
	@RequestMapping(value = "/listAdd", method = { RequestMethod.POST })
	public RestBean addList(EvaluatoionCodeBean bean, int number) {
		return RestBean.ok(evaluatoionCodeService.addList(bean, number));
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
	public RestBean update(EvaluatoionCodeBean bean) {
		if (bean == null || ObjectUtils.isEmpty(bean.getCode())) {
			return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
		}
		return RestBean.ok(evaluatoionCodeService.updateByCode(bean));
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
		return RestBean.ok(evaluatoionCodeService.deleteByCode(code));
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
		evaluatoionCodeService.deleteByCode(codeList);
		return RestBean.ok();
	}
	
    /**
     * 导出EXCEL
     * @param codeList
     * @return
     */
    @ApiOperation(value="导出")
    @NoPermission(noLogin = true)
////    @ApiImplicitParam(paramType = "header", dataType = "String", name = AuthConstant.TOKEN, value = "鉴权token", required = true)
    @RequestMapping(value = "/excel", method = {RequestMethod.GET})
    public void export(EvaluatoionCodeBean bean, HttpServletResponse response) {
    	 List<EvaluatoionCodeBean> page = evaluatoionCodeService.findList(bean);
         ExcelKit.$Export(EvaluatoionCodeBean.class, response).downXlsx(page, false);
    }

}
