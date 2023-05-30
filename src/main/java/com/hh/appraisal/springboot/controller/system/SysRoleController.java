package com.hh.appraisal.springboot.controller.system;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.hh.appraisal.springboot.bean.system.SysRoleBean;
import com.hh.appraisal.springboot.bean.system.SysRoleFunctionBean;
import com.hh.appraisal.springboot.bean.system.SysRoleMenuBean;
import com.hh.appraisal.springboot.core.baen.PageBean;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.core.constant.DataValid;
import com.hh.appraisal.springboot.core.constant.RestCode;
import com.hh.appraisal.springboot.service.system.SysRoleFunctionService;
import com.hh.appraisal.springboot.service.system.SysRoleMenuService;
import com.hh.appraisal.springboot.service.system.SysRoleService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 角色接口 控制器
 * @author gaigai
 * @date 2019/05/15
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController {

    private final SysRoleService sysRoleService;
    private final SysRoleMenuService roleMenuService;
    private final SysRoleFunctionService roleFunctionService;

    public SysRoleController(SysRoleService sysRoleService, SysRoleMenuService roleMenuService, SysRoleFunctionService roleFunctionService) {
        this.sysRoleService = sysRoleService;
        this.roleMenuService = roleMenuService;
        this.roleFunctionService = roleFunctionService;
    }

    /**
     * 下拉框列表
     * @return
     */
    @RequestMapping("/dropdownList")
    public RestBean dropdownList(){
        return RestBean.ok(
                sysRoleService.findList(null)
        );
    }

    /**
     * 分页查询
     * @param bean
     * @return
     */
    @RequestMapping("/listPage")
    public RestBean listPage(SysRoleBean bean, PageBean pageBean){
        return RestBean.ok(sysRoleService.findPage(bean,pageBean));
    }

    /**
     * 角色详情
     * @param bean
     * @return
     */
    @RequestMapping("/detail")
    public RestBean detail(String code) {
        if(ObjectUtils.isEmpty(code)){
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }
        return RestBean.ok(sysRoleService.findByCode(code));
    }

    /**
     * 新增一条记录
     * @param bean
     * @return
     */
    @RequestMapping("/add")
    public RestBean add(SysRoleBean bean){
        if(bean == null || ObjectUtils.isEmpty(bean.getName())){
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }

        // 是否重名
        int iCount = sysRoleService.findCount(SysRoleBean.builder()
                .name(bean.getName())
                .valid(DataValid.VALID)
                .build());
        if(iCount > 0){
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR, "名称重复");
        }

        return RestBean.ok(sysRoleService.add(bean));
    }

    /**
     * 根据唯一标识更新一条记录
     * @param bean
     * @return
     */
    @RequestMapping("/update")
    public RestBean update(SysRoleBean bean){
        if(bean == null || ObjectUtils.isEmpty(bean.getCode())) {
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }
        return RestBean.ok(sysRoleService.updateByCode(bean));
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

        // 删除角色
        RestBean delMenuRest = sysRoleService.deleteByCode(code);

        if(delMenuRest.getCode() == RestCode.DEFAULT_SUCCESS.getCode()){
            // 删除角色菜单对应记录
            roleMenuService.delete(SysRoleMenuBean.builder()
                    .roleCode(code)
                    .build());
            // 删除角色功能对应记录
            roleFunctionService.delete(SysRoleFunctionBean.builder()
                    .roleCode(code)
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
        sysRoleService.deleteByCode(codeList);
        return RestBean.ok();
    }
}
