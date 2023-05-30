package com.hh.appraisal.springboot.controller;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hh.appraisal.springboot.bean.ProductBean;
import com.hh.appraisal.springboot.bean.WxMiniAppUserBean;
import com.hh.appraisal.springboot.bean.system.SysApiBean;
import com.hh.appraisal.springboot.bean.system.SysDictBean;
import com.hh.appraisal.springboot.bean.system.SysFunctionApiBean;
import com.hh.appraisal.springboot.bean.system.SysFunctionBean;
import com.hh.appraisal.springboot.bean.system.SysRoleBean;
import com.hh.appraisal.springboot.bean.system.SysRoleFunctionBean;
import com.hh.appraisal.springboot.bean.system.SysUserBean;
import com.hh.appraisal.springboot.core.annotation.NoPermission;
import com.hh.appraisal.springboot.core.baen.JwtTokenBean;
import com.hh.appraisal.springboot.core.baen.PageBean;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.core.cache.EhcacheService;
import com.hh.appraisal.springboot.core.constant.DataValid;
import com.hh.appraisal.springboot.core.constant.RestCode;
import com.hh.appraisal.springboot.core.jwt.JwtService;
import com.hh.appraisal.springboot.core.utils.StrUtils;
import com.hh.appraisal.springboot.dao.SchoolMapper;
import com.hh.appraisal.springboot.dao.UserAnswersMapper;
import com.hh.appraisal.springboot.entity.EvaluatoionCode;
import com.hh.appraisal.springboot.entity.EvaluatoionUser;
import com.hh.appraisal.springboot.entity.School;
import com.hh.appraisal.springboot.entity.system.SysRole;
import com.hh.appraisal.springboot.service.EvaluatoionCodeService;
import com.hh.appraisal.springboot.service.EvaluatoionUserService;
import com.hh.appraisal.springboot.service.ProductService;
import com.hh.appraisal.springboot.service.WxMiniAppUserService;
import com.hh.appraisal.springboot.service.system.SysApiService;
import com.hh.appraisal.springboot.service.system.SysDictService;
import com.hh.appraisal.springboot.service.system.SysRoleService;
import com.hh.appraisal.springboot.service.system.SysUserService;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 微信小程序用户 控制器
 * 
 * @author gaigai
 * @date 2021/02/01
 */
@RestController
@RequestMapping("/wxMiniAppUser")
public class WxMiniAppUserController {

	private final JwtService jwtService;
	private final EvaluatoionCodeService evaluatoionCodeService;
	private final SysApiService apiService;
	private final UserAnswersMapper userAnswersMapper;
	private final EvaluatoionUserService evaluatoionUserService;
	private final SysDictService sysDictService;
	private final ProductService productService;

	@Autowired
	SchoolMapper schoolMapper;

	public WxMiniAppUserController(JwtService jwtService, EvaluatoionCodeService evaluatoionCodeService,
			SysUserService userService, SysRoleService roleService, SysApiService apiService,
			UserAnswersMapper userAnswersMapper, EvaluatoionUserService evaluatoionUserService,
			SysDictService sysDictService, ProductService productService) {
		this.jwtService = jwtService;
		this.evaluatoionCodeService = evaluatoionCodeService;
		this.apiService = apiService;
		this.userAnswersMapper = userAnswersMapper;
		this.evaluatoionUserService = evaluatoionUserService;
		this.sysDictService = sysDictService;
		this.productService = productService;
	}

	/**
	 * 微信小程序登录 输入授权码
	 * 
	 * @param code
	 * @param appid
	 * @return
	 */
	@NoPermission(noLogin = true)
	@RequestMapping("/login")
	public RestBean login(String code) {
		if (StringUtils.isBlank(code)) {
			return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR, "code为空 code=" + code);
		}

		EvaluatoionCode userInfo = evaluatoionCodeService
				.getOne(new QueryWrapper<EvaluatoionCode>().eq("evaluatoion_Code", code));
		if (userInfo == null || ObjectUtils.isEmpty(userInfo)) {
			return RestBean.error("测评码不存在！");
		}
		EvaluatoionUser info = evaluatoionUserService
				.getOne(new QueryWrapper<EvaluatoionUser>().eq("evaluatoion_Code", code));
		if (info != null && ("Y").equals(info.getIsComplete())) {
			return RestBean.error("测评码已被使用!");
		}
		// 开始日期结束日期
		long startData = Integer.parseInt(userInfo.getStartDate().replaceAll("-", ""));
		long endData = Integer.parseInt(userInfo.getEndDate().replaceAll("-", ""));
		// 当前日期
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		long nowData = Integer.parseInt(format.format(date));
		if (nowData > endData || nowData < startData) {
			return RestBean.error("测评码已过期或还没生效!");
		}
		// 将授权模设为使用
		userInfo.setIsused("Y");
		userInfo.setUpdateTime(new Date());
		evaluatoionCodeService.saveOrUpdate(userInfo);

		School school = schoolMapper.selectOne(new QueryWrapper<School>().eq("code", userInfo.getShoolCode()));
		// 用户所属角色列表
		SysApiBean bean = new SysApiBean();
		bean.setModule("小程序");
		List<String> permissionApiList = apiService.findList(bean).stream().map(v -> v.getUrl())
				.collect(Collectors.toList());
		HashMap<String, Object> restMap = new HashMap<String, Object>();
		// 制作JWT Token
		String jwtToken = jwtService.issueJwt(
				// 令牌id
				StrUtils.getRandomString(20),
				// 用户id
				userInfo.getCode(),
				// 访问角色
				"测评人员", JSONArray.toJSONString(permissionApiList));
		if (StringUtils.isBlank(jwtToken)) {
			return RestBean.error("业务系统登录失败");
		}
		// 获取当前题序
		int questionNo = userAnswersMapper.getMinUnCompleteNo(code);
		// 获取产品信息
		ProductBean product = productService.findByCode(userInfo.getProductCode());
		String colConfigName = "";
		// 如果产品信息为中小学心理测评，就获取这个产品需要填写的用户信息的字段
		if (product.getProductName().equals("中学生心理测评")) {
			colConfigName = "STUDENT_INFO";
		} else {
			colConfigName = "OTHER_PRODUCT_USER_INFO";
		}
		restMap.put("product", product);
		// 判断用户是否填写个人信息
		if (info == null) {
			restMap.put("isCompleteUserInfo", 0);
			List<String> infoColsName = new ArrayList<String>();
			// 获取需要填写的个人信息字段
			HashMap<String, SysDictBean> dictBean = sysDictService.findAllDictByType(colConfigName);
			for (String key : dictBean.keySet()) {
				String data = dictBean.get(key).getData();
				if (data != null && data.equalsIgnoreCase("Y")) {
					infoColsName.add(dictBean.get(key).getValue());
				}
			}
			// 个人信息需要填写哪些字段
			restMap.put("infocols", infoColsName);
		} else {
			restMap.put("isCompleteUserInfo", 1);
		}

		restMap.put("token", JwtTokenBean.JWT_TOKEN_PREFIX + jwtToken);
		restMap.put("logo", school.getLogo());
		restMap.put("EvaluatoionCode", userInfo.getEvaluatoionCode());
		restMap.put("questionNo", questionNo);

		return RestBean.ok(restMap);
	}

}
