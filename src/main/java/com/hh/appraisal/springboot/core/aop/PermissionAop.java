package com.hh.appraisal.springboot.core.aop;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hh.appraisal.springboot.core.annotation.NoPermission;
import com.hh.appraisal.springboot.core.baen.JwtTokenBean;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.core.constant.AuthConstant;
import com.hh.appraisal.springboot.core.constant.RestCode;
import com.hh.appraisal.springboot.core.jwt.JwtService;
import com.hh.appraisal.springboot.core.utils.ControllerUtil;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * AOP方式实现登录校验和权限校验
 * 在不需要权限校验的控制器方法上声明@NoPermission注解，否则默认进行权限和登录校验
 * @see NoPermission
 * @author gaigai
 * @date 2019/05/15
 */
@Slf4j
@Aspect
@Component
public class PermissionAop {

    private final JwtService jwtService;

    public PermissionAop(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Pointcut("execution(* com.hh.appraisal.springboot.controller..*.*(..))")
    public void aopMethod(){}

    /**
     * 校验登录以及权限
     */
    @Around("aopMethod()")
    public Object checkLoginAndPermission(ProceedingJoinPoint pjp) throws Throwable {
        // 获取方法注解
        NoPermission noPermission = ((MethodSignature)pjp.getSignature()).getMethod()
                .getAnnotation(NoPermission.class);

        HttpServletRequest request = ControllerUtil.getRequest();
        HttpServletResponse response = ControllerUtil.getResponse();

        JwtTokenBean jwtToken = null;
        if(noPermission == null || (noPermission != null && noPermission.noLogin() == false)){
            // ==========  验证是否登录（检查json token）  ==========
            if(StringUtils.isBlank(request.getHeader(AuthConstant.TOKEN))){
                return RestBean.error(RestCode.TOKEN_ERROR, "[" + AuthConstant.TOKEN +  "] 不能为空");
            }
            String token = request.getHeader(AuthConstant.TOKEN);
            // token 解析
            RestBean tokenParseRestBean = jwtService.parseJwt(token);
            if(tokenParseRestBean.getCode() != RestCode.DEFAULT_SUCCESS.getCode()){// 解析失败返回json
                return tokenParseRestBean;
            }
            jwtToken = (JwtTokenBean) tokenParseRestBean.getData();

            // ========== token自动刷新 ==========
            // 如果不使用自动刷新机制，则需要前端定时发送刷新token请求
            if(jwtToken.getIsFlushed()){//需要刷新token
                response.setHeader(AuthConstant.TOKEN, JwtTokenBean.JWT_TOKEN_PREFIX + jwtToken.getToken());// 更新到response
                response.setHeader("Access-Control-Expose-Headers", AuthConstant.TOKEN);// 让前端能读取token头
                log.debug("刷新了token");
            }
        }

        // 权限校验
        if(noPermission == null){
            if(!jwtToken.hasUrl(request.getRequestURI())){
                log.debug("权限验证未通过");
                return RestBean.error(RestCode.ROLE_ERROR);
            }else{//登录验证通过
                log.debug("权限验证通过");
            }
        }

        return pjp.proceed();
    }

}
