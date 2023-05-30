package com.hh.appraisal.springboot.service.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hh.appraisal.springboot.bean.system.SysDictTypeBean;
import com.hh.appraisal.springboot.core.baen.PageBean;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.entity.system.SysDictType;

import java.util.List;

/**
 * 字典类型 Service接口
 * @author gaigai
 * @date 2019/05/15
 */
public interface SysDictTypeService extends IService<SysDictType> {

    /**
     * 根据id获取列表
     * @param codeList
     * @return
     */
    List<SysDictTypeBean> findByCodeList(List<String> codeList);

    /**
     * 查询列表
     * @param bean
     * @return
     */
    List<SysDictTypeBean> findList(SysDictTypeBean bean);

    /**
     * 分页查询
     * @param bean
     * @param pageBean
     * @return
     */
    Page<SysDictTypeBean> findPage(SysDictTypeBean bean, PageBean pageBean);

    /**
     * 添加一条记录
     * @param bean
     * @return
     */
    RestBean add(SysDictTypeBean bean);

    /**
     * 根据id更新一条记录
     * @param bean
     * @return
     */
    RestBean updateByCode(SysDictTypeBean bean);

    /**
     * 根据唯一标识删除一条记录
     * @param code 唯一标识
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
     * @param code
     * @return
     */
    SysDictTypeBean findByCode(String code);

    /**
     * 查询一条记录
     * @param bean
     * @return
     */
    SysDictTypeBean findOne(SysDictTypeBean bean);
}
