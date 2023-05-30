package com.hh.appraisal.springboot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import com.hh.appraisal.springboot.core.annotation.NoPermission;
import com.hh.appraisal.springboot.core.cache.EhcacheService;
import com.hh.appraisal.springboot.core.constant.CacheConstant;
import com.hh.appraisal.springboot.core.controller.BasicController;
import com.hh.appraisal.springboot.core.jwt.JwtService;
import com.hh.appraisal.springboot.core.utils.ControllerUtil;
import com.hh.appraisal.springboot.core.utils.StrUtils;
import com.hh.appraisal.springboot.core.utils.VerifyCodeUtil;
import com.hh.appraisal.springboot.service.system.SysUserService;

import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;

/**
 * 根接口路径
 * @author gaigai
 * @date 2019/05/15
 */
@Slf4j
@RestController
@RequestMapping("")
public class IndexController extends BasicController {

    private final EhcacheService ehcacheService;

    public IndexController(JwtService jwtService, SysUserService sysUserService, EhcacheService ehcacheService) {
        super(jwtService, sysUserService);
        this.ehcacheService = ehcacheService;
    }

    /**
     * 生成图片验证码
     *
     * 关于keyCode：前端需要获取用户机器的唯一标识，如果无法获取，则需要前端生成一个随机不重复的字符串
     *              后台在生成验证码后，将以keyCode为key将验证码文字存储在缓存里
     *              前端在发起一次存在验证码参数的请求时，需要将keyCode再次作为请求参数发送，因为后台需要以此为key从缓存里获取验证码文字
     *              这样可以规避不同机器的验证码标识问题。
     * @param keyCode 唯一机器标识
     */
    @NoPermission(noLogin = true)
    @GetMapping(value = "/verifyCode/{keyCode}")
    public void getVerifyCode(@PathVariable String keyCode, HttpServletResponse response) {
        // 缓存key
        String verifyCodeKey = ehcacheService.getAppName() + CacheConstant.IMAGE_CODE + keyCode;

        // 生成验证码
        String code = StrUtils.getRandomString(4);
        log.debug("生成验证码缓存key：" + verifyCodeKey + "    code: " + code);

        // 删除缓存（如果存在）
        ehcacheService.delete(verifyCodeKey);

        // 存入缓存 最多生存120秒，如果期间超过60秒无访问则失效
        ehcacheService.set(verifyCodeKey, code, 60,120);

        // 输出到输出流
        BufferedImage image = VerifyCodeUtil.getImageCode(60,20,code);
        if(!ControllerUtil.outPutImage(image,"png",response)){
            log.error("图片输出失败");
        }
    }

}
