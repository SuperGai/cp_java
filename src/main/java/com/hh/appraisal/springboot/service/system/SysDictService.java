package com.hh.appraisal.springboot.service.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hh.appraisal.springboot.bean.system.SysDictBean;
import com.hh.appraisal.springboot.core.baen.PageBean;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.entity.system.SysDict;

import java.util.HashMap;
import java.util.List;

/**
 * 字典 Service接口
 * @author gaigai
 * @date 2019/05/15
 */
public interface SysDictService extends IService<SysDict> {

    /**
     * 根据字典类型编号获取字典集合
     * @param dictTypeNum 字典类型编号
     * @return key: 字典值  value: 字典对象
     */
    HashMap<String, SysDictBean> findAllDictByType(String dictTypeNum);

    /**
     * 根据id获取列表
     * @param codeList
     * @return
     */
    List<SysDictBean> findByCodeList(List<String> codeList);

    /**
     * 查询列表
     * @param bean
     * @return
     */
    List<SysDictBean> findList(SysDictBean bean);

    /**
     * 分页查询
     * @param bean
     * @param pageBean
     * @return
     */
    Page<SysDictBean> findPage(SysDictBean bean, PageBean pageBean);

    /**
     * 添加一条记录
     * 此方法不返回RestBean实例
     * @param bean
     * @return
     */
    SysDictBean addWithOutRestBean(SysDictBean bean);

    /**
     * 添加一条记录
     * @param bean
     * @return
     */
    RestBean add(SysDictBean bean);

    /**
     * 根据id更新一条记录
     * @param bean
     * @return
     */
    RestBean updateByCode(SysDictBean bean);

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
     * 查询一条记录
     * @param code
     * @return
     */
    SysDictBean findByCode(String code);

    /**
     * 查询一条记录
     * @param bean
     * @return
     */
    SysDictBean findOne(SysDictBean bean);
}
