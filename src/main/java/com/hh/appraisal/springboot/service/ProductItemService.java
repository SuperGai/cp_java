package com.hh.appraisal.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hh.appraisal.springboot.core.baen.PageBean;
import java.util.List;
import com.hh.appraisal.springboot.bean.ProductItemBean;
import com.hh.appraisal.springboot.entity.ProductItem;

/**
 * ProductItem Service 接口
 * @author gaigai
 * @date 2021/06/26
 */
public interface ProductItemService extends IService<ProductItem> {

    /**
     * 根据唯一标识获取实体
     * @param code 唯一标识
     * @return
     */
    ProductItemBean findByCode(String code);

    /**
     * 根据唯一标识集合获取实体
     * @param codeList 唯一标识集合
     * @return
     */
    List<ProductItemBean> findByCodeList(List<String> codeList);

    /**
     * 查询列表
     * @param bean
     * @return
     */
    List<ProductItemBean> findList(ProductItemBean bean);

    /**
     * 分页查询
     * @param bean
     * @param pageBean
     * @return
     */
    Page<ProductItemBean> findPage(ProductItemBean bean, PageBean pageBean);

    /**
     * 添加一条记录
     * @param bean
     * @return
     */
    ProductItemBean add(ProductItemBean bean);

    /**
     * 根据唯一标识更新一条记录
     * @param bean
     * @return
     */
    int updateByCode(ProductItemBean bean);

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
