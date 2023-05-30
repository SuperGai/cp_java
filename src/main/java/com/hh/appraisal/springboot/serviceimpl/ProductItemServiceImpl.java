package com.hh.appraisal.springboot.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hh.appraisal.springboot.core.baen.PageBean;
import com.hh.appraisal.springboot.core.constant.DataValid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.hh.appraisal.springboot.bean.ProductItemBean;
import com.hh.appraisal.springboot.entity.ProductItem;
import com.hh.appraisal.springboot.dao.ProductItemMapper;
import com.hh.appraisal.springboot.service.ProductItemService;

/**
 * ProductItem Service 实现类
 * @author gaigai
 * @date 2021/06/26
 */
@Slf4j
@Service
public class ProductItemServiceImpl extends ServiceImpl<ProductItemMapper, ProductItem> implements ProductItemService {

    private final ProductItemMapper productItemMapper;

    public ProductItemServiceImpl(ProductItemMapper productItemMapper) {
            this.productItemMapper = productItemMapper;
    }

    @Override
    public ProductItemBean findByCode(String code){
        if(ObjectUtils.isEmpty(code)){
            return null;
        }

        List<ProductItem> sourceList = productItemMapper.selectList(
                createWrapper(ProductItemBean.builder().code(code).build())
        );
        if(ObjectUtils.isEmpty(sourceList)){
            return null;
        }

        ProductItemBean restBean = ProductItemBean.builder().build();
        BeanUtils.copyProperties(sourceList.get(0), restBean);
        return restBean;
    }

    @Override
    public List<ProductItemBean> findByCodeList(List<String> codeList){
        if(CollectionUtils.isEmpty(codeList)) {
            return Collections.EMPTY_LIST;
        }

        List<ProductItem> sourceList = productItemMapper.selectList(
                createWrapper(ProductItemBean.builder().productItemCodeList(codeList).build())
        );
        if(CollectionUtils.isEmpty(sourceList)) {
            return Collections.EMPTY_LIST;
        }

        List<ProductItemBean> restBeanList = sourceList.stream().map(v -> {
            ProductItemBean item = new ProductItemBean();
            BeanUtils.copyProperties(v,item);
            return item;
        }).collect(Collectors.toList());
        return restBeanList;
    }

    @Override
    public List<ProductItemBean> findList(ProductItemBean bean){
        List<ProductItem> list = productItemMapper.selectList(createWrapper(bean));
        if(CollectionUtils.isEmpty(list)) {
            return Collections.EMPTY_LIST;
        }

        List<ProductItemBean> beanList = list.stream().map(item -> {
            ProductItemBean srcBean = new ProductItemBean();
            BeanUtils.copyProperties(item,srcBean);
            return srcBean;
        }).collect(Collectors.toList());
        return beanList;
    }

    @Override
    public Page<ProductItemBean> findPage(ProductItemBean bean, PageBean pageBean){
        Page<ProductItem> sourcePage = productItemMapper.selectPage(
                new Page<>(pageBean.getCurrent(),pageBean.getSize()),
                createWrapper(bean)
        );
        if(CollectionUtils.isEmpty(sourcePage.getRecords())) {
            return new Page<>();
        }

        Page<ProductItemBean> restPage = new Page<>();
        BeanUtils.copyProperties(sourcePage, restPage,"records");
        restPage.setRecords(new ArrayList<>(sourcePage.getRecords().size()));
        sourcePage.getRecords().forEach(v -> {
            ProductItemBean itemBean = new ProductItemBean();
            BeanUtils.copyProperties(v, itemBean);
            restPage.getRecords().add(itemBean);
        });
        return restPage;
    }

    @Transactional
    @Override
    public ProductItemBean add(ProductItemBean bean){
        ProductItem source = new ProductItem();
        BeanUtils.copyProperties(bean, source);

        productItemMapper.insert(source);
        BeanUtils.copyProperties(source, bean);
        return bean;
    }

    @Transactional
    @Override
    public int updateByCode(ProductItemBean bean) {
        ProductItem source = new ProductItem();
        BeanUtils.copyProperties(bean,source);
        return productItemMapper.updateById(source);
    }

    @Transactional
    @Override
    public int deleteByCode(String code) {
        if(ObjectUtils.isEmpty(code)){
            return 0;
        }

        ProductItem updateSource = new ProductItem();
        updateSource.setCode(code);
        updateSource.setValid(DataValid.INVALID);
        return productItemMapper.updateById(updateSource);
    }

    @Transactional
    @Override
    public void deleteByCode(List<String> codeList) {
        if(ObjectUtils.isEmpty(codeList)){
            return;
        }

        codeList.forEach(v -> {
            int restCount = deleteByCode(v);
            if(restCount <= 0){
                log.error("删除失败: code=" + v);
            }
        });
    }

    /**
     * 建立查询条件
     * 条件尽量都写在此方法
     * @param bean
     * @return
     */
    private LambdaQueryWrapper createWrapper(ProductItemBean bean){
        LambdaQueryWrapper<ProductItem> wrapper = Wrappers.lambdaQuery();
        if(bean == null || bean.getValid() == null){
            wrapper.eq(ProductItem::getValid,DataValid.VALID);
        }

        // 自定义条件
        if(bean != null) {
            if(bean.getValid() != null){
                wrapper.eq(ProductItem::getValid,bean.getValid());
            }
            if(!ObjectUtils.isEmpty(bean.getCode())){
                wrapper.eq(ProductItem::getCode, bean.getCode());
            }
            if(CollectionUtils.isNotEmpty(bean.getProductItemCodeList())){
                wrapper.in(ProductItem::getCode, bean.getProductItemCodeList());
            }

            // 编写条件逻辑....

        }

        return wrapper;
    }
}
