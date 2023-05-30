package com.hh.appraisal.springboot.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hh.appraisal.springboot.bean.system.SysRoleMenuBean;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.entity.system.SysRoleMenu;

import java.util.HashMap;
import java.util.List;

/**
 * 角色菜单对应 Service接口
 * @author gaigai
 * @date 2019/05/15
 */
public interface SysRoleMenuService extends IService<SysRoleMenu> {

    /**
     * 更新角色菜单树
     * @param menuCodeList
     * @param roleCode
     * @return
     */
    RestBean roleMenuTreeUpdate(List<String> menuCodeList, String roleCode);

    /**
     * 获取菜单树
     * @param roleCode
     * @return 获取全部菜单树，以及指定角色所拥有权限的菜单id集合
     */
    HashMap findMenuTreeByRole(String roleCode);

    /**
     * 查询列表
     * @param bean
     * @return
     */
    List<SysRoleMenuBean> findList(SysRoleMenuBean bean);

    /**
     * 添加记录
     * @param bean
     * @return
     */
    SysRoleMenuBean add(SysRoleMenuBean bean);

    /**
     * 删除多条记录
     * @param bean
     * @return
     */
    RestBean delete(SysRoleMenuBean bean);
}
