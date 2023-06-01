package com.hh.appraisal.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hh.appraisal.springboot.core.baen.PageBean;
import java.util.List;
import com.hh.appraisal.springboot.bean.EvaluatoionOrderBean;
import com.hh.appraisal.springboot.entity.EvaluatoionOrder;

/**
 * EvaluatoionCode Service 接口
 * @author gaigai
 * @date 2021/06/26
 */
public interface EvaluatoionOrderService extends IService<EvaluatoionOrder> {

    /**
     * 根据唯一标识获取实体
     * @param code 唯一标识
     * @return
     */
    EvaluatoionOrderBean findByCode(String code);

    /**
     * 根据唯一标识集合获取实体
     * @param codeList 唯一标识集合
     * @return
     */
    List<EvaluatoionOrderBean> findByCodeList(List<String> codeList);

    /**
     * 查询列表
     * @param bean
     * @return
     */
    List<EvaluatoionOrderBean> findList(EvaluatoionOrderBean bean);

    /**
     * 分页查询
     * @param bean
     * @param pageBean
     * @return
     */
    Page<EvaluatoionOrderBean> findPage(EvaluatoionOrderBean bean, PageBean pageBean);

    /**
     * 添加一条记录
     * @param bean
     * @return
     */
    EvaluatoionOrderBean add(EvaluatoionOrderBean bean);

    /**
     * 根据唯一标识更新一条记录
     * @param bean
     * @return
     */
    int updateByCode(EvaluatoionOrderBean bean);

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
