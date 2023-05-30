package com.hh.appraisal.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hh.appraisal.springboot.bean.WxMiniAppUserBean;
import com.hh.appraisal.springboot.core.baen.PageBean;
import com.hh.appraisal.springboot.entity.WxMiniAppUser;

import java.util.List;

/**
 * WxMiniAppUser Service 接口
 * @author gaigai
 * @date 2021/02/01
 */
public interface WxMiniAppUserService extends IService<WxMiniAppUser> {

    /**
     * 查询一条记录
     * @param searchBean
     * @return
     */
    WxMiniAppUserBean findOne(WxMiniAppUserBean searchBean);

    /**
     * 根据唯一标识获取实体
     * @param code 唯一标识
     * @return
     */
    WxMiniAppUserBean findByCode(String code);

    /**
     * 根据唯一标识集合获取实体
     * @param codeList 唯一标识集合
     * @return
     */
    List<WxMiniAppUserBean> findByCodeList(List<String> codeList);

    /**
     * 查询列表
     * @param bean
     * @return
     */
    List<WxMiniAppUserBean> findList(WxMiniAppUserBean bean);

    /**
     * 分页查询
     * @param bean
     * @param pageBean
     * @return
     */
    Page<WxMiniAppUserBean> findPage(WxMiniAppUserBean bean, PageBean pageBean);

    /**
     * 添加一条记录
     * @param bean
     * @return
     */
    WxMiniAppUserBean add(WxMiniAppUserBean bean);

    /**
     * 根据唯一标识更新一条记录
     * @param bean
     * @return
     */
    int updateByCode(WxMiniAppUserBean bean);

    /**
     * 根据唯一标识删除一条记录
     * 软删除
     * @param code 唯一标识
     * @return
     */
    int deleteByCode(String code);

    /**
     * 批量删除
     * 软删除
     * @param codeList 唯一标识集合
     */
    void deleteByCode(List<String> codeList);

}
