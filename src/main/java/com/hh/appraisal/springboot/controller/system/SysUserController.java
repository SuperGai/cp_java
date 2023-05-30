package com.hh.appraisal.springboot.controller.system;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hh.appraisal.springboot.bean.system.SysUserBean;
import com.hh.appraisal.springboot.bean.system.SysUserRoleBean;
import com.hh.appraisal.springboot.core.baen.PageBean;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.core.constant.AuthConstant;
import com.hh.appraisal.springboot.core.constant.RestCode;
import com.hh.appraisal.springboot.service.system.SysUserRoleService;
import com.hh.appraisal.springboot.service.system.SysUserService;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统用户 控制器
 * @author gaigai
 * @date 2019/05/15
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    private final SysUserService sysUserService;
    private final SysUserRoleService userRoleService;

    public SysUserController(SysUserService sysUserService, SysUserRoleService userRoleService){
        this.sysUserService = sysUserService;
        this.userRoleService = userRoleService;
    }

    /**
     * 分页查询
     * @param bean
     * @return
     */
    @RequestMapping(value = "/listPage")
    public RestBean listPage(SysUserBean bean, PageBean pageBean){
        Page<SysUserBean> userBeanPage = sysUserService.findPage(bean,pageBean);
        return RestBean.ok(userBeanPage);
    }

    /**
     * 新增一条记录
     * @param bean
     * @return
     */
    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    public RestBean add(SysUserBean bean){
        return sysUserService.add(bean);
    }

    /**
     * 查看详情
     * @param code
     * @return
     */
    @RequestMapping(value = "/detail")
    public RestBean detail(String code) {
        if(ObjectUtils.isEmpty(code)){
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }
        SysUserBean bean = sysUserService.findByCode(code);

        if(bean != null){
            // 角色code集合
            List<SysUserRoleBean> userRoleBeanList = userRoleService.findAll(
                    SysUserRoleBean.builder()
                        .userCode(code)
                        .build()
            );
            if(!ObjectUtils.isEmpty(userRoleBeanList)){
                bean.setRoleCodeList(
                        userRoleBeanList.stream().map(v -> v.getRoleCode())
                                .collect(Collectors.toList())
                );
            }
        }

        return RestBean.ok(bean);
    }

    /**
     * 根据唯一标识更新一条记录
     * @param bean
     * @return
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public RestBean update(SysUserBean bean){
        return sysUserService.updateByCode(bean);
    }

    /**
     * 根据唯一标识删除一条记录
     * @param code
     * @return
     */
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public RestBean delete(String code) {
        if(ObjectUtils.isEmpty(code)){
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }
        return sysUserService.deleteByCode(code);
    }

    /**
     * 批量删除
     * @param codeList
     * @return
     */
    @RequestMapping(value = "/batchDelete", method = {RequestMethod.POST})
    public RestBean batchDelete(@RequestParam("codeList") List<String> codeList){
        if(ObjectUtils.isEmpty(codeList)){
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }
        sysUserService.deleteByCode(codeList);
        return RestBean.ok();
    }

    /**
     * 重置密码
     * @param bean
     * @return
     */
    @RequestMapping(value = "/password/reset", method = {RequestMethod.POST})
    public RestBean passwordReset(SysUserBean bean){
        if(bean == null || ObjectUtils.isEmpty(bean.getCode()) ||
                ObjectUtils.isEmpty(bean.getNewPassword())) {
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }
        return sysUserService.passwordReset(bean);
    }
}
