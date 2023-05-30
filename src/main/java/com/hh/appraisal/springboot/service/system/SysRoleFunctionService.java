package com.hh.appraisal.springboot.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hh.appraisal.springboot.bean.system.SysFunctionBean;
import com.hh.appraisal.springboot.bean.system.SysRoleFunctionBean;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.entity.system.SysRoleFunction;

import java.util.HashMap;
import java.util.List;

/**
 * 角色功能对应 Service接口
 * @author gaigai
 * @date 2019/05/15
 */
public interface SysRoleFunctionService extends IService<SysRoleFunction> {

    /**
     * 角色功能树更新
     * @param functionCodeList
     * @param roleCode
     * @return
     */
    RestBean treeUpdate(List<String> functionCodeList, String roleCode);

    /**
     * 获取角色功能树
     * @param roleCode
     * @return
     */
    HashMap roleFunctionTree(String roleCode);

    /**
     * 列表查询
     * @param bean
     * @return
     */
    List<SysRoleFunctionBean> findList(SysRoleFunctionBean bean);

    /**
     * 查询功能列表
     * @param bean
     * @return
     */
    List<SysFunctionBean> findFunctionListByRoleFunction(SysRoleFunctionBean bean);

    /**
     * 删除多条记录
     * @param bean
     * @return
     */
    RestBean delete(SysRoleFunctionBean bean);
}
