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

import com.hh.appraisal.springboot.bean.NormManageItemBean;
import com.hh.appraisal.springboot.entity.NormManageItem;
import com.hh.appraisal.springboot.dao.NormManageItemMapper;
import com.hh.appraisal.springboot.service.NormManageItemService;

/**
 * NormManageItem Service 实现类
 * @author gaigai
 * @date 2023/06/02
 */
@Slf4j
@Service
public class NormManageItemServiceImpl extends ServiceImpl<NormManageItemMapper, NormManageItem> implements NormManageItemService {

    private final NormManageItemMapper normManageItemMapper;

    public NormManageItemServiceImpl(NormManageItemMapper normManageItemMapper) {
            this.normManageItemMapper = normManageItemMapper;
    }

    @Override
    public NormManageItemBean findByCode(String code){
        if(ObjectUtils.isEmpty(code)){
            return null;
        }

        List<NormManageItem> sourceList = normManageItemMapper.selectList(
                createWrapper(NormManageItemBean.builder().code(code).build())
        );
        if(ObjectUtils.isEmpty(sourceList)){
            return null;
        }

        NormManageItemBean restBean = NormManageItemBean.builder().build();
        BeanUtils.copyProperties(sourceList.get(0), restBean);
        return restBean;
    }

    @Override
    public List<NormManageItemBean> findByCodeList(List<String> codeList){
        if(CollectionUtils.isEmpty(codeList)) {
            return Collections.EMPTY_LIST;
        }

        List<NormManageItem> sourceList = normManageItemMapper.selectList(
                createWrapper(NormManageItemBean.builder().normManageItemCodeList(codeList).build())
        );
        if(CollectionUtils.isEmpty(sourceList)) {
            return Collections.EMPTY_LIST;
        }

        List<NormManageItemBean> restBeanList = sourceList.stream().map(v -> {
            NormManageItemBean item = new NormManageItemBean();
            BeanUtils.copyProperties(v,item);
            return item;
        }).collect(Collectors.toList());
        return restBeanList;
    }

    @Override
    public List<NormManageItemBean> findList(NormManageItemBean bean){
        List<NormManageItem> list = normManageItemMapper.selectList(createWrapper(bean));
        if(CollectionUtils.isEmpty(list)) {
            return Collections.EMPTY_LIST;
        }

        List<NormManageItemBean> beanList = list.stream().map(item -> {
            NormManageItemBean srcBean = new NormManageItemBean();
            BeanUtils.copyProperties(item,srcBean);
            return srcBean;
        }).collect(Collectors.toList());
        return beanList;
    }

    @Override
    public Page<NormManageItemBean> findPage(NormManageItemBean bean, PageBean pageBean){
        Page<NormManageItem> sourcePage = normManageItemMapper.selectPage(
                new Page<>(pageBean.getCurrent(),pageBean.getSize()),
                createWrapper(bean)
        );
        if(CollectionUtils.isEmpty(sourcePage.getRecords())) {
            return new Page<>();
        }

        Page<NormManageItemBean> restPage = new Page<>();
        BeanUtils.copyProperties(sourcePage, restPage,"records");
        restPage.setRecords(new ArrayList<>(sourcePage.getRecords().size()));
        sourcePage.getRecords().forEach(v -> {
            NormManageItemBean itemBean = new NormManageItemBean();
            BeanUtils.copyProperties(v, itemBean);
            restPage.getRecords().add(itemBean);
        });
        return restPage;
    }

    @Transactional
    @Override
    public NormManageItemBean add(NormManageItemBean bean){
        NormManageItem source = new NormManageItem();
        BeanUtils.copyProperties(bean, source);

        normManageItemMapper.insert(source);
        BeanUtils.copyProperties(source, bean);
        return bean;
    }

    @Transactional
    @Override
    public int updateByCode(NormManageItemBean bean) {
        NormManageItem source = new NormManageItem();
        BeanUtils.copyProperties(bean,source);
        return normManageItemMapper.updateById(source);
    }

    @Transactional
    @Override
    public int deleteByCode(String code) {
        if(ObjectUtils.isEmpty(code)){
            return 0;
        }

        NormManageItem updateSource = new NormManageItem();
        updateSource.setCode(code);
        updateSource.setValid(DataValid.INVALID);
        return normManageItemMapper.updateById(updateSource);
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
    private LambdaQueryWrapper createWrapper(NormManageItemBean bean){
        LambdaQueryWrapper<NormManageItem> wrapper = Wrappers.lambdaQuery();
        if(bean == null || bean.getValid() == null){
            wrapper.eq(NormManageItem::getValid,DataValid.VALID);
        }

        // 自定义条件
        if(bean != null) {
            if(bean.getValid() != null){
                wrapper.eq(NormManageItem::getValid,bean.getValid());
            }
            if(!ObjectUtils.isEmpty(bean.getCode())){
                wrapper.eq(NormManageItem::getCode, bean.getCode());
            }
            if(!ObjectUtils.isEmpty(bean.getNormCode())){
                wrapper.eq(NormManageItem::getNormCode, bean.getNormCode());
            }
            if(CollectionUtils.isNotEmpty(bean.getNormManageItemCodeList())){
                wrapper.in(NormManageItem::getCode, bean.getNormManageItemCodeList());
            }

            // 编写条件逻辑....

        }

        return wrapper;
    }
}
