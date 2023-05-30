package com.hh.appraisal.springboot.serviceimpl.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hh.appraisal.springboot.bean.system.SysApiBean;
import com.hh.appraisal.springboot.core.baen.PageBean;
import com.hh.appraisal.springboot.core.constant.DataValid;
import com.hh.appraisal.springboot.dao.system.SysApiMapper;
import com.hh.appraisal.springboot.entity.system.SysApi;
import com.hh.appraisal.springboot.service.system.SysApiService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service 实现
 * @author gaigai
 * @date 2019/05/15
 */
@Slf4j
@Service
public class SysApiServiceImpl extends ServiceImpl<SysApiMapper, SysApi> implements SysApiService {

    private final SysApiMapper apiMapper;

    public SysApiServiceImpl(SysApiMapper apiMapper) {
        this.apiMapper = apiMapper;
    }

    @Override
    public SysApiBean findByCode(String code){
        if(ObjectUtils.isEmpty(code)){
            return null;
        }

        List<SysApi> sourceList = apiMapper.selectList(createWrapper(SysApiBean.builder().code(code).build()));
        if(ObjectUtils.isEmpty(sourceList)){
            return null;
        }

        SysApiBean restBean = SysApiBean.builder().build();
        BeanUtils.copyProperties(sourceList.get(0), restBean);
        return restBean;
    }

    @Override
    public List<SysApiBean> findByCodeList(List<String> codeList){
        if(CollectionUtils.isEmpty(codeList)) {
            return Collections.EMPTY_LIST;
        }

        List<SysApi> sourceList = apiMapper.selectList(createWrapper(SysApiBean.builder().apiCodeList(codeList).build()));
        if(CollectionUtils.isEmpty(sourceList)) {
            return Collections.EMPTY_LIST;
        }

        List<SysApiBean> restBeanList = sourceList.stream().map(v -> {
            SysApiBean item = new SysApiBean();
            BeanUtils.copyProperties(v,item);
            return item;
        }).collect(Collectors.toList());
        return restBeanList;
    }

    @Override
    public List<SysApiBean> findList(SysApiBean bean){
        List<SysApi> list = apiMapper.selectList(createWrapper(bean));
        if(CollectionUtils.isEmpty(list)) {
            return Collections.EMPTY_LIST;
        }

        List<SysApiBean> beanList = list.stream().map(item -> {
            SysApiBean srcBean = new SysApiBean();
            BeanUtils.copyProperties(item,srcBean);
            return srcBean;
        }).collect(Collectors.toList());
        return beanList;
    }

    @Override
    public Page<SysApiBean> findPage(SysApiBean bean, PageBean pageBean){
        Page<SysApi> sourcePage = apiMapper.selectPage(new Page<>(pageBean.getCurrent(),pageBean.getSize()), createWrapper(bean));
        if(CollectionUtils.isEmpty(sourcePage.getRecords())) {
            return new Page<>();
        }

        Page<SysApiBean> restPage = new Page<>();
        BeanUtils.copyProperties(sourcePage, restPage,"records");
        restPage.setRecords(new ArrayList<>(sourcePage.getRecords().size()));
        sourcePage.getRecords().forEach(v -> {
            SysApiBean itemBean = new SysApiBean();
            BeanUtils.copyProperties(v, itemBean);
            restPage.getRecords().add(itemBean);
        });
        return restPage;
    }

    @Transactional
    @Override
    public SysApiBean add(SysApiBean bean){
        // 处理斜杠方向，统一为 /
        if(StringUtils.isNotBlank(bean.getUrl())){
            bean.setUrl(bean.getUrl().replace("\\", "/"));
        }

        SysApi source = new SysApi();
        BeanUtils.copyProperties(bean, source);

        apiMapper.insert(source);
        BeanUtils.copyProperties(source, bean);
        return bean;
    }

    @Transactional
    @Override
    public int updateByCode(SysApiBean bean) {
        SysApi source = new SysApi();
        BeanUtils.copyProperties(bean,source);
        return apiMapper.updateById(source);
    }

    @Transactional
    @Override
    public int deleteByCode(String code) {
        if(org.springframework.util.ObjectUtils.isEmpty(code)){
            return 0;
        }

        SysApi updateSource = new SysApi();
        updateSource.setCode(code);
        updateSource.setValid(DataValid.INVALID);
        return apiMapper.updateById(updateSource);
    }

    @Transactional
    @Override
    public void deleteByCode(List<String> codeList) {
        if(org.springframework.util.ObjectUtils.isEmpty(codeList)){
            return;
        }

        codeList.forEach(v -> {
            deleteByCode(v);
        });
    }

    /**
     * 建立查询条件
     * 条件尽量都写在此方法
     * @param bean
     * @return
     */
    private LambdaQueryWrapper createWrapper(SysApiBean bean){
        LambdaQueryWrapper<SysApi> wrapper = Wrappers.lambdaQuery();
        if(bean == null || bean.getValid() == null){
            wrapper.eq(SysApi::getValid,DataValid.VALID);
        }

        // 自定义条件
        if(bean != null) {
            if(bean.getValid() != null){
                wrapper.eq(SysApi::getValid,bean.getValid());
            }
            if(!ObjectUtils.isEmpty(bean.getCode())){

                wrapper.eq(SysApi::getCode, bean.getCode());
            }

            if(StringUtils.isNotBlank(bean.getName())){
                wrapper.like(SysApi::getName,bean.getName());
            }
            if(StringUtils.isNotBlank(bean.getModule())){
                wrapper.eq(SysApi::getModule,bean.getModule());
            }
            if(StringUtils.isNotBlank(bean.getUrl())){
                wrapper.eq(SysApi::getUrl,bean.getUrl());
            }
            if(CollectionUtils.isNotEmpty(bean.getApiCodeList())){
                wrapper.in(SysApi::getCode, bean.getApiCodeList());
            }
        }

        return wrapper;
    }
}
