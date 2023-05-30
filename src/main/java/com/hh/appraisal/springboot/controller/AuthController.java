package com.hh.appraisal.springboot.controller;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.hh.appraisal.springboot.bean.system.SysMenuBean;
import com.hh.appraisal.springboot.bean.system.SysUserBean;
import com.hh.appraisal.springboot.constant.UserApproveResults;
import com.hh.appraisal.springboot.constant.UserApproveStatus;
import com.hh.appraisal.springboot.constant.UserStatus;
import com.hh.appraisal.springboot.core.annotation.NoPermission;
import com.hh.appraisal.springboot.core.baen.JwtTokenBean;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.core.cache.EhcacheService;
import com.hh.appraisal.springboot.core.constant.AuthConstant;
import com.hh.appraisal.springboot.core.constant.CacheConstant;
import com.hh.appraisal.springboot.core.constant.DataValid;
import com.hh.appraisal.springboot.core.constant.RestCode;
import com.hh.appraisal.springboot.core.controller.BasicController;
import com.hh.appraisal.springboot.core.jwt.JwtService;
import com.hh.appraisal.springboot.core.utils.StrUtils;
import com.hh.appraisal.springboot.service.system.SysUserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 身份验证相关接口
 * @author gaigai
 * @date 2019/05/15
 */
@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController extends BasicController {

    private final JwtService jwtService;
    private final EhcacheService ehcacheService;
    private final SysUserService userService;
    public AuthController(JwtService jwtService, EhcacheService ehcacheService,
                          SysUserService userService, SysUserService sysUserService){
        super(jwtService, sysUserService);
        this.jwtService = jwtService;
        this.ehcacheService = ehcacheService;
        this.userService = userService;
    }

    /**
     * 用户登录
     * @return
     */
    @PostMapping(value = "/login")
    @NoPermission(noLogin = true)
    public RestBean login(SysUserBean bean, HttpServletResponse response) {
        // key
        String verifyCodeKey = ehcacheService.getAppName() + CacheConstant.IMAGE_CODE + bean.getKeyCode();
        log.debug("获取验证码缓存key：" + verifyCodeKey + "    请求验证码: " + bean.getVerifyCode());

        // 验证码
        String redisVerifyCode = ehcacheService.get(verifyCodeKey);
        if(redisVerifyCode == null){
            // 需要重新刷新验证码
            return RestBean.error(RestCode.EXPIRE_VCODE);
        }
        if(!redisVerifyCode.equalsIgnoreCase(bean.getVerifyCode())){
            return RestBean.error("验证码错误");
        }

        // 校验账号密码
        bean.setPassword(StrUtils.md5(bean.getPassword()));
        bean.setApproveResult(UserApproveResults.PASS);
        bean.setApproveStatus(UserApproveStatus.YES);
        bean.setStatus(UserStatus.ENABLE);
        SysUserBean loginUser = userService.findOne(bean);
        if(loginUser == null) {
            return RestBean.error(RestCode.ACCOUNT_PASS_ERROR);
        }
        if(!UserApproveResults.PASS.equals(loginUser.getApproveResult()) ||
            !UserApproveStatus.YES.equals(loginUser.getApproveStatus()) ||
            !UserStatus.ENABLE.equals(loginUser.getStatus())){
            return RestBean.error(RestCode.DISABLE_USER);
        }

        // 登录成功后，获取userid，查询该用户拥有权限的接口url集合
        List<String> permissionApiList = userService.findPermitUrlsByUser(SysUserBean.builder()
                .code(loginUser.getCode()).valid(DataValid.VALID)
                .build())
                .stream().map(v->v.getUrl()).collect(Collectors.toList());

        // 制作JWT Token
        String jwtToken = jwtService.issueJwt(
                //令牌id
                StrUtils.getRandomString(20),
                //用户id
                loginUser.getCode(),
                //访问角色
                null,
                //用户权限集合，json格式 Auth拦截器需要此数据来校验请求是否有权限
                JSONArray.toJSONString(permissionApiList)
        );

        //token存入 response里的Header jwt规定token需要加前缀
        response.setHeader(AuthConstant.TOKEN, JwtTokenBean.JWT_TOKEN_PREFIX + jwtToken);
        // 让前端能读取token头
        response.setHeader("Access-Control-Expose-Headers", AuthConstant.TOKEN);

        // 删除验证码存储
        ehcacheService.delete(verifyCodeKey);

        return RestBean.ok();
    }

    /**
     * 获取登录用户信息
     * @param request
     * @return
     */
    @NoPermission
    @GetMapping(value = "/login/info")
    public RestBean loginInfo(HttpServletRequest request){
        // 获取登录信息
        SysUserBean loginUser = getLoginUser(request.getHeader(AuthConstant.TOKEN));
        if(loginUser == null) {
            return RestBean.error(RestCode.EXPIRE_TOKEN);
        }

        loginUser.setPassword("");
        return RestBean.ok(loginUser);
    }

    /**
     * 获取登录用户可显示的菜单树
     * @param request
     * @return
     */
    @NoPermission
    @GetMapping("/menu/tree")
    public RestBean menuTree(HttpServletRequest request){
        // 获取登录信息
        SysUserBean loginUser = getLoginUser(request.getHeader(AuthConstant.TOKEN));
        if(loginUser == null) {
            return RestBean.error(RestCode.EXPIRE_TOKEN);
        }

        // 获取菜单权限数据
        List<SysMenuBean> tree = userService.findMenuTreeByUser(loginUser);

        return RestBean.ok(tree);
    }

    /**
     * 获取登录用户所拥有权限的接口集合
     * @param request
     * @return
     */
    @NoPermission
    @GetMapping("/permit/apis")
    public RestBean permitApis(HttpServletRequest request){

        // 获取登录信息
        SysUserBean loginUser = getLoginUser(request.getHeader(AuthConstant.TOKEN));
        if(loginUser == null) {
            return RestBean.error(RestCode.EXPIRE_TOKEN);
        }

        List<String> permissionApiList = userService.findPermitUrlsByUser(SysUserBean.builder()
                .code(loginUser.getCode()).valid(DataValid.VALID)
                .build())
                .stream().map(v->v.getUrl()).collect(Collectors.toList());

        return RestBean.ok(permissionApiList);
    }

    /**
     * 更新登录用户的信息
     * 可修改密码
     * @return
     */
    @NoPermission
    @PostMapping(value = "/login/update")
    public RestBean loginUpdateInfo(SysUserBean bean){
        if(bean == null || ObjectUtils.isEmpty(bean.getCode())) {
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }
        return userService.loginUpdateInfo(bean);
    }
}
