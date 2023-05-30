package com.hh.appraisal.springboot.service.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hh.appraisal.springboot.bean.system.SysRoleBean;
import com.hh.appraisal.springboot.bean.system.SysUserBean;
import com.hh.appraisal.springboot.core.baen.PageBean;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.entity.system.SysRole;

import java.util.List;

/**
 * 角色 Service接口
 * @author gaigai
 * @date 2019/05/15
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 根据用户查询角色列表
     * @param bean
     * @return
     */
    List<SysRoleBean> findByUser(SysUserBean bean);

    /**
     * 根据角色唯一标识集合获取角色列表
     * @param roleCodeList
     * @return
     */
    List<SysRoleBean> findByCodeList(List<String> roleCodeList);

    /**
     * 查询列表
     * @param bean
     * @return
     */
    List<SysRoleBean> findList(SysRoleBean bean);

    /**
     * 分页查询
     * @param bean
     * @param pageBean
     * @return
     */
    Page<SysRoleBean> findPage(SysRoleBean bean, PageBean pageBean);

    /**
     * 查询记录条数
     * @param bean
     * @return
     */
    Integer findCount(SysRoleBean bean);

    /**
     * 添加一条记录
     * @param bean
     * @return
     */
    SysRoleBean add(SysRoleBean bean);

    /**
     * 根据唯一标识更新一条记录
     * @param bean
     * @return
     */
    RestBean updateByCode(SysRoleBean bean);

    /**
     * 根据唯一标识删除一条记录
     * @param code
     * @return
     */
    RestBean deleteByCode(String code);

    /**
     * 批量删除
     * @param codeList
     */
    void deleteByCode(List<String> codeList);

    /**
     * 根据唯一标识查询一条记录
     * @param code 唯一标识
     * @return
     */
    SysRoleBean findByCode(String code);
}
