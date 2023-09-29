package com.hh.appraisal.springboot.controller;

import io.swagger.annotations.*;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hh.appraisal.springboot.core.annotation.NoPermission;
import com.hh.appraisal.springboot.core.baen.PageBean;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.core.constant.RestCode;
import com.hh.appraisal.springboot.core.utils.StrUtils;
import com.hh.appraisal.springboot.dao.UserAnswersMapper;
import com.hh.appraisal.springboot.entity.EvaluatoionOrder;
import com.hh.appraisal.springboot.entity.EvaluatoionUser;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.hh.appraisal.springboot.core.constant.AuthConstant;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hh.appraisal.springboot.bean.EvaluatoionOrderBean;
import com.hh.appraisal.springboot.bean.EvaluatoionUserBean;
import com.hh.appraisal.springboot.bean.ProductBean;
import com.hh.appraisal.springboot.bean.QuestionAllBean;
import com.hh.appraisal.springboot.bean.QuestionBean;
import com.hh.appraisal.springboot.bean.UserAnswersBean;
import com.hh.appraisal.springboot.constant.CpStatus;
import com.hh.appraisal.springboot.service.EvaluatoionOrderService;
import com.hh.appraisal.springboot.service.EvaluatoionUserService;
import com.hh.appraisal.springboot.service.GeneraPdfAsyncTaskService;
import com.hh.appraisal.springboot.service.GeneraPdfAsyncTaskService3;
import com.hh.appraisal.springboot.service.GeneraPdfModel2AsyncTaskService;
import com.hh.appraisal.springboot.service.ProductService;
import com.hh.appraisal.springboot.service.QuestionService;
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

	private final EvaluatoionOrderService evaluatoionOrderService;

	@Autowired
	GeneraPdfAsyncTaskService generaPdfAsyncTaskService;

	@Autowired
	GeneraPdfAsyncTaskService3 generaPdfAsyncTaskService2;

	@Autowired
	GeneraPdfModel2AsyncTaskService generaPdfModel2AsyncTaskService;

	@Autowired
	ProductService productService;

	@Autowired
	EvaluatoionUserService evaluatoionUserService;

	@Autowired
	UserAnswersMapper userAnswersMapper;

	@Autowired
	QuestionService questionService;

	public UserAnswersController(UserAnswersService userAnswersService,
			EvaluatoionOrderService evaluatoionOrderService) {
		this.userAnswersService = userAnswersService;
		this.evaluatoionOrderService = evaluatoionOrderService;
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
		UserAnswersBean bean = new UserAnswersBean();
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
	public RestBean update(@RequestParam("code") String code,
			@RequestParam("questionOptionsCode") String questionOptionsCode,
			@RequestParam("spendTime") double spendTime, @RequestParam("value") String value) {
		if (code == null || ObjectUtils.isEmpty(code)) {
			return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
		}
		UserAnswersBean bean = userAnswersService.findByCode(code);
		if (questionOptionsCode != null && questionOptionsCode.length() > 0) {
			bean.setQuestionOptionsCode(questionOptionsCode);
		}
		if (value != null && value.length() > 0) {
			QuestionBean questionBean = questionService.findByCode(bean.getQuestionCode());
			System.out.println("value:" + value);
			if (questionBean.getQuestionType().equals("OPTION_INPUT")) {
				String valueArray[] = value.split("&");
				int sum = 0;
				for (int i = 0; i < valueArray.length; i++) {
					String numValue = valueArray[i].split(":")[1];
					if (StrUtils.isNumeric(numValue)) {
						int number = Integer.parseInt(numValue);
						sum += number;
						if (!(number >= 0 && number <= 3)) {
							return RestBean.error("不在0-3范围内");
						}
					} else {
						return RestBean.error("不是数字");
					}
				}
				if (sum != 3) {
					return RestBean.error("各项之和应该为3!");
				}
			} else if (questionBean.getQuestionType().equals("INPUT")) {
				if (!StrUtils.isNumeric(value)) {
					return RestBean.error("不是数字");
				}
			}
			bean.setValue(value);
		}
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
	 * @throws Exception
	 */
	@ApiOperation(value = "完成答题", response = RestBean.class)
	@ApiImplicitParam(paramType = "header", dataType = "String", name = AuthConstant.TOKEN, value = "鉴权token", required = true)
	@RequestMapping(value = "/completeAnswer", method = { RequestMethod.POST })
	public RestBean completeAnswer(@RequestParam("evaluationUserCode") String evaluationUserCode,
			@RequestParam("productCode") String productCode) throws Exception {
		if (ObjectUtils.isEmpty(evaluationUserCode)) {
			return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
		}
		if (ObjectUtils.isEmpty(productCode)) {
			return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
		}
		// 获取当前测评的订单,更新成为进行中
		EvaluatoionOrder order = evaluatoionOrderService.getOne(new QueryWrapper<EvaluatoionOrder>()
				.eq("product_Code", productCode).eq("evaluatoion_code", evaluationUserCode));
		EvaluatoionOrderBean restBean = EvaluatoionOrderBean.builder().build();
		BeanUtils.copyProperties(order, restBean);
		restBean.setStatus(CpStatus.COMPLETE);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		restBean.setEndDate(sdf.format(new Date()));
		evaluatoionOrderService.updateByCode(restBean);

		// 获取待答题的订单清单,如果不为空开始下一次答题
		EvaluatoionOrderBean queryOrderBean = new EvaluatoionOrderBean();
		queryOrderBean.setEvaluatoionCode(evaluationUserCode);
		queryOrderBean.setStatus("!" + CpStatus.COMPLETE);
		List<EvaluatoionOrderBean> orderList = evaluatoionOrderService.findList(queryOrderBean);

		// 不存在未完成的题目代表已经完成全部答题
		if (orderList == null || orderList.size() == 0) {
//			//是否完成答题改成是
			EvaluatoionUser evaluatoionUser = evaluatoionUserService
					.getOne(new QueryWrapper<EvaluatoionUser>().eq("EVALUATOION_CODE", evaluationUserCode));
			evaluatoionUser.setIsComplete("Y");
			// 答题时间
			long spendTime = userAnswersMapper.getAllProductSpendTime(evaluationUserCode);
			evaluatoionUser.setSpendTime(spendTime);
			evaluatoionUserService.saveOrUpdate(evaluatoionUser);
			ProductBean productBean = productService.findByCode(productCode);
			if (productBean.getProductName().equals("中学生心理测评")) {
				try {
					generaPdfAsyncTaskService.executeAsyncTask(evaluationUserCode);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				generaPdfModel2AsyncTaskService.executeAsyncTask(evaluationUserCode);
			}
		} else {
			String nextProductCode = orderList.get(0).getProductCode();
			JSONObject result = new JSONObject();
			result.put("nextProductCode", nextProductCode);
			return RestBean.ok(result);
		}

		return RestBean.ok();
	}

	/**
	 * 开始答题 ------需要修改传入产品参数
	 * 
	 * @param codeList
	 * @return
	 */
	@ApiOperation(value = "开始答题", response = RestBean.class)
	@ApiImplicitParam(paramType = "header", dataType = "String", name = AuthConstant.TOKEN, value = "鉴权token", required = true)
	@RequestMapping(value = "/startAnswer", method = { RequestMethod.POST })
	public RestBean startAnswer(@RequestParam("evaluationUserCode") String evaluationUserCode,
			@RequestParam("productCode") String productCode) {
		if (ObjectUtils.isEmpty(evaluationUserCode)) {
			return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
		}
		if (ObjectUtils.isEmpty(productCode)) {
			return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
		}

		// 获取需要答的题目
		QuestionAllBean allbean = userAnswersService.getQuestion(evaluationUserCode, productCode);
		if (allbean == null) {
			return RestBean.ok();
		}
		return RestBean.ok(allbean);
	}
}
