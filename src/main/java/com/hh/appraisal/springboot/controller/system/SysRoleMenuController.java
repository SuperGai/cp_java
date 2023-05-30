package com.hh.appraisal.springboot.controller.system;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.core.constant.RestCode;
import com.hh.appraisal.springboot.service.system.SysRoleMenuService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 角色菜单对应 控制器
 * @author gaigai
 * @date 2019/05/15
 */
@RestController
@RequestMapping("/sys/role/menu")
public class SysRoleMenuController {

    private final SysRoleMenuService roleMenuService;

    public SysRoleMenuController(SysRoleMenuService roleMenuService) {
        this.roleMenuService = roleMenuService;
    }

    /**
     * 获取菜单树
     *
     * @return
     */
    @GetMapping("/tree")
    public RestBean menuTree(String roleCode) {
        return RestBean.ok(
                roleMenuService.findMenuTreeByRole(roleCode)
        );
    }

    /**
     * 更新角色菜单树
     * @param menuCodeList
     * @param roleCode
     * @return
     */
    @RequestMapping("/tree/update")
    public RestBean treeUpdate(@RequestParam(value = "menuCodeList", required = false) List<String> menuCodeList,
                               String roleCode){
        if(ObjectUtils.isEmpty(roleCode)) {
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }
        return roleMenuService.roleMenuTreeUpdate(menuCodeList, roleCode);
    }
}
