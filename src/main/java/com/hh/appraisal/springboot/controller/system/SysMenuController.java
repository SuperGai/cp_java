package com.hh.appraisal.springboot.controller.system;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hh.appraisal.springboot.bean.system.SysMenuBean;
import com.hh.appraisal.springboot.bean.system.SysRoleMenuBean;
import com.hh.appraisal.springboot.core.baen.PageBean;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.core.constant.RestCode;
import com.hh.appraisal.springboot.service.system.SysMenuService;
import com.hh.appraisal.springboot.service.system.SysRoleMenuService;
import com.hh.appraisal.springboot.service.system.SysUserService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统菜单 控制器
 * @author gaigai
 * @date 2019/05/15
 */
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController {

    private final SysMenuService sysMenuService;
    private final SysUserService userService;
    private final SysRoleMenuService roleMenuService;

    public SysMenuController(SysMenuService sysMenuService, SysUserService userService, SysRoleMenuService roleMenuService) {
        this.sysMenuService = sysMenuService;
        this.userService = userService;
        this.roleMenuService = roleMenuService;
    }

    /**
     * 下拉框列表
     * @param bean
     * @return
     */
    @RequestMapping("/dropdownList")
    public RestBean dropdownList(SysMenuBean bean){
        return RestBean.ok(sysMenuService.findList(bean));
    }

    /**
     * 分页查询
     * @param bean
     * @return
     */
    @RequestMapping("/listPage")
    public RestBean listPage(SysMenuBean bean, PageBean pageBean){
        Page<SysMenuBean> page = sysMenuService.findPage(bean,pageBean);
        if(page == null || CollectionUtils.isEmpty(page.getRecords())) {
            return RestBean.ok(new Page<>());
        }

        page.getRecords().forEach(v->{
            if(v.getParentMenuCode() != null) {
                SysMenuBean parent = sysMenuService.findByCode(v.getParentMenuCode());
                if (parent != null) {
                    v.setParentName(parent.getName());
                } else {
                    v.setParentName("--");
                }
            }
        });

        return RestBean.ok(page);
    }

    /**
     * 查看详情
     * @param code
     * @return
     */
    @RequestMapping("/detail")
    public RestBean detail(String code) {
        if(ObjectUtils.isEmpty(code)){
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }
        return RestBean.ok(sysMenuService.findByCode(code));
    }

    /**
     * 新增一条记录
     * @param bean
     * @return
     */
    @RequestMapping("/add")
    public RestBean add(SysMenuBean bean){
        return RestBean.ok(sysMenuService.add(bean));
    }

    /**
     * 根据唯一标识更新一条记录
     * @param bean
     * @return
     */
    @RequestMapping("/update")
    public RestBean update(SysMenuBean bean){
        if(bean == null || ObjectUtils.isEmpty(bean.getCode())){
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }
        return RestBean.ok(sysMenuService.updateByCode(bean));
    }

    /**
     * 根据唯一标识删除一条记录
     * @param code
     * @return
     */
    @RequestMapping("/delete")
    public RestBean delete(String code) {
        // 不传入id不允许删除
        if(ObjectUtils.isEmpty(code)){
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }

        // 删除菜单
        RestBean delMenuRest = sysMenuService.deleteByCode(code);

        // 删除 角色与菜单对应表记录
        if(delMenuRest.getCode() == RestCode.DEFAULT_SUCCESS.getCode()){
            roleMenuService.delete(SysRoleMenuBean.builder()
                    .menuCode(code)
                    .build());
        }

        return delMenuRest;
    }

    /**
     * 批量删除
     * @param codeList
     * @return
     */
    @RequestMapping("/batchDelete")
    public RestBean batchDelete(@RequestParam("codeList") List<String> codeList){
        if(ObjectUtils.isEmpty(codeList)){
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }
        sysMenuService.deleteByCode(codeList);
        return RestBean.ok();
    }
}
