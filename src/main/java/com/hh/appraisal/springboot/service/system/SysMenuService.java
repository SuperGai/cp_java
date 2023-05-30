package com.hh.appraisal.springboot.service.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hh.appraisal.springboot.bean.system.SysMenuBean;
import com.hh.appraisal.springboot.core.baen.PageBean;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.entity.system.SysMenu;

import java.util.LinkedList;
import java.util.List;

/**
 * 菜单 Service接口
 * @author gaigai
 * @date 2019/05/15
 */
public interface SysMenuService extends IService<SysMenu> {

    /**
     * 获取菜单树
     * @param bean
     * @return
     */
    List<SysMenuBean> tree(SysMenuBean bean);

    /**
     * 构造菜单树
     * @param list
     * @return
     */
    LinkedList<SysMenuBean> createTree(List<SysMenuBean> list);

    /**
     * 列表查询
     * @param bean
     * @return
     */
    List<SysMenuBean> findList(SysMenuBean bean);

    /**
     * 分页查询
     * @param bean
     * @param pageBean
     * @return
     */
    Page<SysMenuBean> findPage(SysMenuBean bean, PageBean pageBean);

    /**
     * 添加一条记录
     * @param bean
     * @return
     */
    SysMenuBean add(SysMenuBean bean);

    /**
     * 更新一条记录
     * @param bean
     * @return
     */
    RestBean updateByCode(SysMenuBean bean);

    /**
     * 删除多条记录
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
    SysMenuBean findByCode(String code);
}
