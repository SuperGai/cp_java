package com.hh.appraisal.springboot.controller.system;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.core.constant.RestCode;
import com.hh.appraisal.springboot.service.system.SysRoleFunctionService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统角色功能对应 控制器
 * @author gaigai
 * @date 2019/05/15
 */
@RestController
@RequestMapping("/sys/role/function")
public class SysRoleFunctionController {

    private final SysRoleFunctionService roleFunctionService;

    public SysRoleFunctionController(SysRoleFunctionService roleFunctionService) {
        this.roleFunctionService = roleFunctionService;
    }

    /**
     * 角色功能树
     * @param roleCode
     * @return
     */
    @RequestMapping("/tree")
    public RestBean tree(String roleCode){
        return RestBean.ok(
                roleFunctionService.roleFunctionTree(roleCode)
        );
    }

    /**
     * 更新角色功能树
     * @param funCodeList
     * @param roleCode
     * @return
     */
    @RequestMapping("/tree/update")
    public RestBean treeUpdate(@RequestParam(value = "funCodeList", required = false) List<String> funCodeList,
                               String roleCode){
        if(ObjectUtils.isEmpty(roleCode)) {
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }
        return roleFunctionService.treeUpdate(funCodeList, roleCode);
    }

}
