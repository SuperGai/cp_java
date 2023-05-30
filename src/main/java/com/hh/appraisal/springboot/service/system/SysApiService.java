package com.hh.appraisal.springboot.service.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hh.appraisal.springboot.bean.system.SysApiBean;
import com.hh.appraisal.springboot.core.baen.PageBean;
import com.hh.appraisal.springboot.entity.system.SysApi;

import java.util.List;

/**
 * 接口 Service接口
 * @author gaigai
 * @date 2019/05/15
 */
public interface SysApiService extends IService<SysApi> {

    /**
     * 根据唯一标识获取实体
     * @param code
     * @return
     */
    SysApiBean findByCode(String code);

    /**
     * 根据唯一标识获取实体
     * @param codeList
     * @return
     */
    List<SysApiBean> findByCodeList(List<String> codeList);

    /**
     * 查询列表
     * @param bean
     * @return
     */
    List<SysApiBean> findList(SysApiBean bean);

    /**
     * 分页查询
     * @param bean
     * @param pageBean
     * @return
     */
    Page<SysApiBean> findPage(SysApiBean bean, PageBean pageBean);

    /**
     * 添加一条记录
     * @param bean
     * @return
     */
    SysApiBean add(SysApiBean bean);

    /**
     * 根据唯一标识更新一条记录
     * @param bean
     * @return
     */
    int updateByCode(SysApiBean bean);

    /**
     * 根据唯一标识删除一条记录
     * 软删除
     * @param code 唯一标识
     * @return
     */
    int deleteByCode(String code);

    /**
     * 批量删除
     * @param codeList
     */
    void deleteByCode(List<String> codeList);
}
