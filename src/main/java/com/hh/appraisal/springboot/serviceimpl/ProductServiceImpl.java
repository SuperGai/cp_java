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

import com.hh.appraisal.springboot.bean.ProductBean;
import com.hh.appraisal.springboot.entity.Product;
import com.hh.appraisal.springboot.entity.School;
import com.hh.appraisal.springboot.dao.ProductMapper;
import com.hh.appraisal.springboot.service.ProductService;

/**
 * Product Service 实现类
 * @author gaigai
 * @date 2021/06/26
 */
@Slf4j
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductMapper productMapper) {
            this.productMapper = productMapper;
    }

    @Override
    public ProductBean findByCode(String code){
        if(ObjectUtils.isEmpty(code)){
            return null;
        }

        List<Product> sourceList = productMapper.selectList(
                createWrapper(ProductBean.builder().code(code).build())
        );
        if(ObjectUtils.isEmpty(sourceList)){
            return null;
        }

        ProductBean restBean = ProductBean.builder().build();
        BeanUtils.copyProperties(sourceList.get(0), restBean);
        return restBean;
    }

    @Override
    public List<ProductBean> findByCodeList(List<String> codeList){
        if(CollectionUtils.isEmpty(codeList)) {
            return Collections.EMPTY_LIST;
        }

        List<Product> sourceList = productMapper.selectList(
                createWrapper(ProductBean.builder().productCodeList(codeList).build())
        );
        if(CollectionUtils.isEmpty(sourceList)) {
            return Collections.EMPTY_LIST;
        }

        List<ProductBean> restBeanList = sourceList.stream().map(v -> {
            ProductBean item = new ProductBean();
            BeanUtils.copyProperties(v,item);
            return item;
        }).collect(Collectors.toList());
        return restBeanList;
    }

    @Override
    public List<ProductBean> findList(ProductBean bean){
        List<Product> list = productMapper.selectList(createWrapper(bean));
        if(CollectionUtils.isEmpty(list)) {
            return Collections.EMPTY_LIST;
        }

        List<ProductBean> beanList = list.stream().map(item -> {
            ProductBean srcBean = new ProductBean();
            BeanUtils.copyProperties(item,srcBean);
            return srcBean;
        }).collect(Collectors.toList());
        return beanList;
    }

    @Override
    public Page<ProductBean> findPage(ProductBean bean, PageBean pageBean){
        Page<Product> sourcePage = productMapper.selectPage(
                new Page<>(pageBean.getCurrent(),pageBean.getSize()),
                createWrapper(bean)
        );
        if(CollectionUtils.isEmpty(sourcePage.getRecords())) {
            return new Page<>();
        }

        Page<ProductBean> restPage = new Page<>();
        BeanUtils.copyProperties(sourcePage, restPage,"records");
        restPage.setRecords(new ArrayList<>(sourcePage.getRecords().size()));
        sourcePage.getRecords().forEach(v -> {
            ProductBean itemBean = new ProductBean();
            BeanUtils.copyProperties(v, itemBean);
            restPage.getRecords().add(itemBean);
        });
        return restPage;
    }

    @Transactional
    @Override
    public ProductBean add(ProductBean bean){
        Product source = new Product();
        BeanUtils.copyProperties(bean, source);

        productMapper.insert(source);
        BeanUtils.copyProperties(source, bean);
        return bean;
    }

    @Transactional
    @Override
    public int updateByCode(ProductBean bean) {
        Product source = new Product();
        BeanUtils.copyProperties(bean,source);
        return productMapper.updateById(source);
    }

    @Transactional
    @Override
    public int deleteByCode(String code) {
        if(ObjectUtils.isEmpty(code)){
            return 0;
        }

        Product updateSource = new Product();
        updateSource.setCode(code);
        updateSource.setValid(DataValid.INVALID);
        return productMapper.updateById(updateSource);
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
    private LambdaQueryWrapper createWrapper(ProductBean bean){
        LambdaQueryWrapper<Product> wrapper = Wrappers.lambdaQuery();
        if(bean == null || bean.getValid() == null){
            wrapper.eq(Product::getValid,DataValid.VALID);
        }

        // 自定义条件
        if(bean != null) {
            if(bean.getValid() != null){
                wrapper.eq(Product::getValid,bean.getValid());
            }
            if(!ObjectUtils.isEmpty(bean.getCode())){
                wrapper.eq(Product::getCode, bean.getCode());
            }
            if(CollectionUtils.isNotEmpty(bean.getProductCodeList())){
                wrapper.in(Product::getCode, bean.getProductCodeList());
            }
            if (!ObjectUtils.isEmpty(bean.getProductName())) {
				wrapper.like(Product::getProductName, bean.getProductName());
			}

            // 编写条件逻辑....

        }

        return wrapper;
    }
}
