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

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.hh.appraisal.springboot.core.constant.AuthConstant;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hh.appraisal.springboot.bean.EvaluatoionUserBean;
import com.hh.appraisal.springboot.bean.QuestionAllBean;
import com.hh.appraisal.springboot.bean.UserAnswersBean;
import com.hh.appraisal.springboot.service.GeneraPdfAsyncTaskService;
import com.hh.appraisal.springboot.service.GeneraPdfAsyncTaskService2;
import com.hh.appraisal.springboot.service.GeneraPdfAsyncTaskService3;
import com.hh.appraisal.springboot.service.UserAnswersService;
import com.wuwenze.poi.ExcelKit;

/**
 * 测评用户答案表 控制器
 * 
 * @author gaigai
 * @date 2021/06/26
 */
@Api(tags = "测评用户答案表")
@RestController
@RequestMapping("/userAnswers")
public class UserAnswersController {

	private final UserAnswersService userAnswersService;

	@Autowired
	GeneraPdfAsyncTaskService generaPdfAsyncTaskService;
	
	@Autowired
	GeneraPdfAsyncTaskService2 generaPdfAsyncTaskService2;
	

	public UserAnswersController(UserAnswersService userAnswersService) {
		this.userAnswersService = userAnswersService;
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
	public RestBean listPage(UserAnswersBean bean, PageBean pageBean) {
		Page<UserAnswersBean> page = userAnswersService.findPage(bean, pageBean);
		if (page == null || CollectionUtils.isEmpty(page.getRecords())) {
			return RestBean.ok(new Page<>());
		}

		// 处理列表逻辑....

		return RestBean.ok(page);
	}
	
	/**
	 * 根据授权码获取集合
	 * 
	 * @param bean
	 * @return
	 */
	@NoPermission(noLogin = true)
	@ApiOperation(value = "根据授权码获取集合", response = RestBean.class)
	@ApiOperationSupport(ignoreParameters = { "code", "createTime", "updateTime", "valid" })
	@ApiImplicitParam(paramType = "header", dataType = "String", name = AuthConstant.TOKEN, value = "鉴权token", required = true)
	@RequestMapping(value = "/listByEvaluationCode", method = { RequestMethod.POST })
	public RestBean listByEvaluationCode(@RequestParam("evaluationUserCode") String evaluationUserCode) {
		UserAnswersBean bean=new UserAnswersBean();
		bean.setEvaluationUserCode(evaluationUserCode);
		List<UserAnswersBean> page = userAnswersService.findList(bean);
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
		UserAnswersBean bean = userAnswersService.findByCode(code);
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
	public RestBean add(UserAnswersBean bean) {
		return RestBean.ok(userAnswersService.add(bean));
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
	public RestBean update(UserAnswersBean bean) {
		if (bean == null || ObjectUtils.isEmpty(bean.getCode())) {
			return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
		}
		return RestBean.ok(userAnswersService.updateByCode(bean));
	}

	/**
	 * 根据唯一标识更新一条记录
	 * 
	 * @param bean
	 * @return
	 */
	@ApiOperation(value = "答题", response = RestBean.class)
	@ApiImplicitParam(paramType = "header", dataType = "String", name = AuthConstant.TOKEN, value = "鉴权token", required = true)
	@RequestMapping(value = "/updateAnswer", method = { RequestMethod.POST })
	public RestBean update(@RequestParam("code") String code,@RequestParam("questionOptionsCode")  String questionOptionsCode,@RequestParam("spendTime")  double spendTime) {
		if (code == null || ObjectUtils.isEmpty(code)) {
			return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
		}
		UserAnswersBean bean=userAnswersService.findByCode(code);
		bean.setQuestionOptionsCode(questionOptionsCode);
		bean.setIsComplete("Y");	
		bean.setSpendTime(spendTime);
		return RestBean.ok(userAnswersService.updateByCode(bean));
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
		return RestBean.ok(userAnswersService.deleteByCode(code));
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
		userAnswersService.deleteByCode(codeList);
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
	public void export(UserAnswersBean bean, HttpServletResponse response) throws IOException {
		List<UserAnswersBean> page = userAnswersService.findList(bean);
		ExcelKit.$Export(UserAnswersBean.class, response).downXlsx(page, false);
	}
	
	
	/**
	 * 完成答题
	 * 
	 * @param codeList
	 * @return
	 */
	@ApiOperation(value = "完成答题", response = RestBean.class)
	@ApiImplicitParam(paramType = "header", dataType = "String", name = AuthConstant.TOKEN, value = "鉴权token", required = true)
	@RequestMapping(value = "/completeAnswer", method = { RequestMethod.POST })
	public RestBean completeAnswer(@RequestParam("evaluationUserCode") String evaluationUserCode) {
		if (ObjectUtils.isEmpty(evaluationUserCode)) {
			return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
		}
//		UserAnswersBean bean = new UserAnswersBean();
//		bean.setEvaluationUserCode(evaluationUserCode);
//		List<UserAnswersBean> list = userAnswersService.findList(bean);
		try {
			generaPdfAsyncTaskService2.executeAsyncTask(evaluationUserCode);
//			generaPdfAsyncTaskService2.executeAsyncTask(evaluationUserCode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return RestBean.ok();
	}

	/**
	 * 开始答题
	 * 
	 * @param codeList
	 * @return
	 */
	@ApiOperation(value = "开始答题", response = RestBean.class)
	@ApiImplicitParam(paramType = "header", dataType = "String", name = AuthConstant.TOKEN, value = "鉴权token", required = true)
	@RequestMapping(value = "/startAnswer", method = { RequestMethod.POST })
	public RestBean startAnswer(@RequestParam("evaluationUserCode") String evaluationUserCode) {
		if (ObjectUtils.isEmpty(evaluationUserCode)) {
			return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
		}
		QuestionAllBean allbean = userAnswersService.getQuestion(evaluationUserCode);
		if (allbean == null) {
			return RestBean.ok();
		}
		return RestBean.ok(allbean);
	}
}
