package com.hh.appraisal.springboot.service.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hh.appraisal.springboot.bean.system.SysFunctionBean;
import com.hh.appraisal.springboot.core.baen.PageBean;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.entity.system.SysFunction;

import java.util.List;

/**
 * 功能 Service接口
 * @author gaigai
 * @date 2019/05/15
 */
public interface SysFunctionService extends IService<SysFunction> {

    /**
     * 根据id获取列表
     * @param codeList
     * @return
     */
    List<SysFunctionBean> findByCodeList(List<String> codeList);

    /**
     * 查询列表
     * @param bean
     * @return
     */
    List<SysFunctionBean> findList(SysFunctionBean bean);

    /**
     * 分页查询
     * @param bean
     * @param pageBean
     * @return
     */
    Page<SysFunctionBean> findPage(SysFunctionBean bean, PageBean pageBean);

    /**
     * 检查是否有重复记录
     * @param bean
     * @return
     */
    Boolean repetition(SysFunctionBean bean);

    /**
     * 添加一条记录
     * @param bean
     * @return
     */
    SysFunctionBean add(SysFunctionBean bean);

    /**
     * 根据id更新一条记录
     * @param bean
     * @return
     */
    RestBean updateByCode(SysFunctionBean bean);

    /**
     * 批量删除
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
     * 详情
     * @param bean
     * @return
     */
    SysFunctionBean findOne(SysFunctionBean bean);
}
