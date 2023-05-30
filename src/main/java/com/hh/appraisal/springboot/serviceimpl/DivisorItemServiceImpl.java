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

import com.hh.appraisal.springboot.bean.DivisorItemBean;
import com.hh.appraisal.springboot.entity.DivisorItem;
import com.hh.appraisal.springboot.dao.DivisorItemMapper;
import com.hh.appraisal.springboot.service.DivisorItemService;

/**
 * DivisorItem Service 实现类
 * @author gaigai
 * @date 2021/06/26
 */
@Slf4j
@Service
public class DivisorItemServiceImpl extends ServiceImpl<DivisorItemMapper, DivisorItem> implements DivisorItemService {

    private final DivisorItemMapper divisorItemMapper;

    public DivisorItemServiceImpl(DivisorItemMapper divisorItemMapper) {
            this.divisorItemMapper = divisorItemMapper;
    }

    @Override
    public DivisorItemBean findByCode(String code){
        if(ObjectUtils.isEmpty(code)){
            return null;
        }

        List<DivisorItem> sourceList = divisorItemMapper.selectList(
                createWrapper(DivisorItemBean.builder().code(code).build())
        );
        if(ObjectUtils.isEmpty(sourceList)){
            return null;
        }

        DivisorItemBean restBean = DivisorItemBean.builder().build();
        BeanUtils.copyProperties(sourceList.get(0), restBean);
        return restBean;
    }

    @Override
    public List<DivisorItemBean> findByCodeList(List<String> codeList){
        if(CollectionUtils.isEmpty(codeList)) {
            return Collections.EMPTY_LIST;
        }

        List<DivisorItem> sourceList = divisorItemMapper.selectList(
                createWrapper(DivisorItemBean.builder().divisorItemCodeList(codeList).build())
        );
        if(CollectionUtils.isEmpty(sourceList)) {
            return Collections.EMPTY_LIST;
        }

        List<DivisorItemBean> restBeanList = sourceList.stream().map(v -> {
            DivisorItemBean item = new DivisorItemBean();
            BeanUtils.copyProperties(v,item);
            return item;
        }).collect(Collectors.toList());
        return restBeanList;
    }

    @Override
    public List<DivisorItemBean> findList(DivisorItemBean bean){
        List<DivisorItem> list = divisorItemMapper.selectList(createWrapper(bean));
        if(CollectionUtils.isEmpty(list)) {
            return Collections.EMPTY_LIST;
        }

        List<DivisorItemBean> beanList = list.stream().map(item -> {
            DivisorItemBean srcBean = new DivisorItemBean();
            BeanUtils.copyProperties(item,srcBean);
            return srcBean;
        }).collect(Collectors.toList());
        return beanList;
    }

    @Override
    public Page<DivisorItemBean> findPage(DivisorItemBean bean, PageBean pageBean){
        Page<DivisorItem> sourcePage = divisorItemMapper.selectPage(
                new Page<>(pageBean.getCurrent(),pageBean.getSize()),
                createWrapper(bean)
        );
        if(CollectionUtils.isEmpty(sourcePage.getRecords())) {
            return new Page<>();
        }

        Page<DivisorItemBean> restPage = new Page<>();
        BeanUtils.copyProperties(sourcePage, restPage,"records");
        restPage.setRecords(new ArrayList<>(sourcePage.getRecords().size()));
        sourcePage.getRecords().forEach(v -> {
            DivisorItemBean itemBean = new DivisorItemBean();
            BeanUtils.copyProperties(v, itemBean);
            restPage.getRecords().add(itemBean);
        });
        return restPage;
    }

    @Transactional
    @Override
    public DivisorItemBean add(DivisorItemBean bean){
        DivisorItem source = new DivisorItem();
        BeanUtils.copyProperties(bean, source);

        divisorItemMapper.insert(source);
        BeanUtils.copyProperties(source, bean);
        return bean;
    }

    @Transactional
    @Override
    public int updateByCode(DivisorItemBean bean) {
        DivisorItem source = new DivisorItem();
        BeanUtils.copyProperties(bean,source);
        return divisorItemMapper.updateById(source);
    }

    @Transactional
    @Override
    public int deleteByCode(String code) {
        if(ObjectUtils.isEmpty(code)){
            return 0;
        }

        DivisorItem updateSource = new DivisorItem();
        updateSource.setCode(code);
        updateSource.setValid(DataValid.INVALID);
        return divisorItemMapper.updateById(updateSource);
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
    private LambdaQueryWrapper createWrapper(DivisorItemBean bean){
        LambdaQueryWrapper<DivisorItem> wrapper = Wrappers.lambdaQuery();
        if(bean == null || bean.getValid() == null){
            wrapper.eq(DivisorItem::getValid,DataValid.VALID);
        }

        // 自定义条件
        if(bean != null) {
            if(bean.getValid() != null){
                wrapper.eq(DivisorItem::getValid,bean.getValid());
            }
            if(!ObjectUtils.isEmpty(bean.getCode())){
                wrapper.eq(DivisorItem::getCode, bean.getCode());
            }
            if(CollectionUtils.isNotEmpty(bean.getDivisorItemCodeList())){
                wrapper.in(DivisorItem::getCode, bean.getDivisorItemCodeList());
            }
            if(!ObjectUtils.isEmpty(bean.getDivisorCode())){
                wrapper.eq(DivisorItem::getDivisorCode, bean.getDivisorCode());
            }
            // 编写条件逻辑....
            wrapper.orderByAsc(DivisorItem::getValueStart);
        }

        return wrapper;
    }
}
