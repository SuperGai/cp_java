package com.hh.appraisal.springboot.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hh.appraisal.springboot.bean.system.SysUserRoleBean;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.entity.system.SysUserRole;

import java.util.List;

/**
 * 用户角色对应 Service接口
 * @author gaigai
 * @date 2019/05/15
 */
public interface SysUserRoleService extends IService<SysUserRole> {

    /**
     * 插入一条记录
     * @param bean
     * @return
     */
    SysUserRoleBean add(SysUserRoleBean bean);

    /**
     * 查询列表
     * @param bean
     * @return
     */
    List<SysUserRoleBean> findAll(SysUserRoleBean bean);

    /**
     * 查询记录数量
     * @param bean
     * @return
     */
    Integer count(SysUserRoleBean bean);

    /**
     * 根据用户标识批量删除
     * @param userCode
     * @return
     */
    RestBean deleteByUser(String userCode);
}
