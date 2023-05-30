package com.hh.appraisal.springboot.core.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import com.hh.appraisal.springboot.bean.system.SysUserBean;
import com.hh.appraisal.springboot.core.baen.JwtTokenBean;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.core.constant.DataValid;
import com.hh.appraisal.springboot.core.constant.RestCode;
import com.hh.appraisal.springboot.core.jwt.JwtService;
import com.hh.appraisal.springboot.service.system.SysUserService;

/**
 * 控制层基类
 * @author gaigai
 * @date 2019/05/15
 */
@Slf4j
@Component
public class BasicController {

    private final JwtService jwtService;
    private final SysUserService sysUserService;

    public BasicController(JwtService jwtService, SysUserService sysUserService) {
        this.jwtService = jwtService;
        this.sysUserService = sysUserService;
    }

    /**
     * 获取登录用户信息
     * 解析token
     * @return
     */
    public SysUserBean getLoginUser(String token){
        RestBean parseJwtRestBean = jwtService.parseJwt(token);
        if(parseJwtRestBean.getCode() != RestCode.DEFAULT_SUCCESS.getCode()){
            log.error("获取登录用户信息失败，token解析失败：" + parseJwtRestBean);
            return null;
        }
        JwtTokenBean jwtTokenBean = (JwtTokenBean) parseJwtRestBean.getData();
        if(jwtTokenBean == null){
            return null;
        }

        SysUserBean userBean = sysUserService.findOne(SysUserBean.builder()
                .code(jwtTokenBean.getId()).valid(DataValid.VALID)
                .build());
        if(userBean != null){
            userBean.setPassword("");
        }

        return userBean;
    }

}
