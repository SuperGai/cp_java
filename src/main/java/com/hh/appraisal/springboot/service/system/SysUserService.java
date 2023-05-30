package com.hh.appraisal.springboot.service.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hh.appraisal.springboot.bean.system.SysApiBean;
import com.hh.appraisal.springboot.bean.system.SysMenuBean;
import com.hh.appraisal.springboot.bean.system.SysUserBean;
import com.hh.appraisal.springboot.core.baen.PageBean;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.entity.system.SysUser;

import java.util.List;

/**
 * 用户 Service接口
 * @author gaigai
 * @date 2019/05/15
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 根据唯一标识获取实体
     * @param code
     * @return
     */
    SysUserBean findByCode(String code);

    /**
     * 查询用户拥有权限的菜单树
     * @param bean
     * @return
     */
    List<SysMenuBean> findMenuTreeByUser(SysUserBean bean);

    /**
     * 查询用户所拥有的所有有权限api接口集合
     * @param bean
     * @return
     */
    List<SysApiBean> findPermitUrlsByUser(SysUserBean bean);

    /**
     * 更新登录用户信息
     * 可修改密码
     * @param bean
     * @return
     */
    RestBean loginUpdateInfo(SysUserBean bean);

    /**
     * 重置密码
     * @param bean
     * @return
     */
    RestBean passwordReset(SysUserBean bean);

    /**
     * 根据用户信息获取用户
     * @param bean
     * @return
     */
    SysUserBean findOne(SysUserBean bean);

    /**
     * 查询列表
     * @param bean
     * @return
     */
    RestBean findList(SysUserBean bean);

    /**
     * 分页查询
     * @param bean
     * @param pageBean
     * @return
     */
    Page<SysUserBean> findPage(SysUserBean bean, PageBean pageBean);

    /**
     * 添加记录
     * @param bean
     * @return
     */
    RestBean add(SysUserBean bean);

    /**
     * 根据id更新记录
     * @param bean
     * @return
     */
    RestBean updateByCode(SysUserBean bean);

    /**
     * 根据唯一标识删除记录
     * 软删除
     * @param code 唯一标识
     * @return
     */
    RestBean deleteByCode(String code);

    /**
     * 批量删除
     * @param codeList
     */
    void deleteByCode(List<String> codeList);
}
