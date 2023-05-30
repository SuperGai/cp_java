package com.hh.appraisal.springboot.controller;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hh.appraisal.springboot.bean.WxMiniAppUserBean;
import com.hh.appraisal.springboot.core.annotation.NoPermission;
import com.hh.appraisal.springboot.core.baen.JwtTokenBean;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.core.constant.RestCode;
import com.hh.appraisal.springboot.core.jwt.JwtService;
import com.hh.appraisal.springboot.core.utils.StrUtils;
import com.hh.appraisal.springboot.core.wxmini.WxMiniAppService;
import com.hh.appraisal.springboot.service.WxMiniAppUserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * 微信小程序相关接口
 */
@Slf4j
@RestController
@RequestMapping("/wxMini")
public class WxMiniController {

    private final WxMiniAppService wxMiniAppService;
    private final WxMiniAppUserService wxMiniAppUserService;
    private final JwtService jwtService;
    public WxMiniController(WxMiniAppService wxMiniAppService, WxMiniAppUserService wxMiniAppUserService, JwtService jwtService) {
        this.wxMiniAppService = wxMiniAppService;
        this.wxMiniAppUserService = wxMiniAppUserService;
        this.jwtService = jwtService;
    }

    /**
     * 注册用户并登录
     * 用于注册微信小程序用户
     * @param bean
     * @return
     */
    @NoPermission(noLogin = true)
    @RequestMapping("/registerUser")
    public RestBean registerUserAndLogin(WxMiniAppUserBean bean){
        if(ObjectUtils.isEmpty(bean.getOpenid())){
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }

        bean = wxMiniAppUserService.add(bean);

        if(ObjectUtils.isEmpty(bean.getCode())){
            return RestBean.error();
        }

        // 登录业务系统 获取token
        String jwtToken = jwtService.issueJwt(
                //令牌id
                StrUtils.getRandomString(20),
                //用户id
                bean.getCode(),
                //访问角色
                null,
                null
        );
        if(StringUtils.isBlank(jwtToken)){
            return RestBean.error("业务系统登录失败");
        }
        bean.setToken(JwtTokenBean.JWT_TOKEN_PREFIX + jwtToken);

        return RestBean.ok(bean);
    }

    /**
     * 微信小程序登录
     * 微信登录成功，但是业务系统里没有该openid的用户，则只返回前端需要的session信息，否则同时返回业务系统用户信息(token在用户信息里)
     * @param code
     * @param appid
     * @return
     */
    @NoPermission(noLogin = true)
    @RequestMapping("/login")
    public RestBean login(String code){
        if (StringUtils.isBlank(code)) {
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR, "appid或code为空 code=" + code);
        }

        // 微信登录
        WxMaJscode2SessionResult wxLoginRest = wxMiniAppService.login(code);
        if(wxLoginRest == null || ObjectUtils.isEmpty(wxLoginRest.getOpenid())){
            return RestBean.error("微信登录失败");
        }

        // 检查该openid是否业务系统里也存在
        WxMiniAppUserBean wxUser = wxMiniAppUserService.findOne(WxMiniAppUserBean.builder()
                .openid(wxLoginRest.getOpenid())
                .build());

        HashMap<String, Object> restMap = new HashMap<String, Object>();
        restMap.put("wxLoginRest", wxLoginRest);

        // 用户不存在，则自动注册
        if(wxUser != null){
            // 制作JWT Token
            String jwtToken = jwtService.issueJwt(
                    //令牌id
                    StrUtils.getRandomString(20),
                    //用户id
                    wxUser.getCode(),
                    //访问角色
                    null,
                    null
            );
            if(StringUtils.isBlank(jwtToken)){
                return RestBean.error("业务系统登录失败");
            }
            wxUser.setToken(JwtTokenBean.JWT_TOKEN_PREFIX + jwtToken);
            restMap.put("wxUser", wxUser);
        }

        return RestBean.ok(restMap);
    }

}
