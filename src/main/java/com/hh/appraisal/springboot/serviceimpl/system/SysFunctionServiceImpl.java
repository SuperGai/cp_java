package com.hh.appraisal.springboot.serviceimpl.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hh.appraisal.springboot.bean.system.SysFunctionBean;
import com.hh.appraisal.springboot.core.baen.PageBean;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.core.constant.DataValid;
import com.hh.appraisal.springboot.core.constant.RestCode;
import com.hh.appraisal.springboot.dao.system.SysFunctionMapper;
import com.hh.appraisal.springboot.entity.system.SysFunction;
import com.hh.appraisal.springboot.service.system.SysFunctionService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

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
public class SysFunctionServiceImpl extends ServiceImpl<SysFunctionMapper, SysFunction> implements SysFunctionService {

    private final SysFunctionMapper functionMapper;

    public SysFunctionServiceImpl(SysFunctionMapper functionMapper) {
        this.functionMapper = functionMapper;
    }

    @Override
    public List<SysFunctionBean> findByCodeList(List<String> codeList){
        if(CollectionUtils.isEmpty(codeList)) {
            return Collections.EMPTY_LIST;
        }

        LambdaQueryWrapper<SysFunction> wrapper = createWrapper(null);
        wrapper.in(SysFunction::getCode, codeList);
        List<SysFunction> roles = functionMapper.selectList(wrapper);

        List<SysFunctionBean> roleBeans = new ArrayList<>();
        roles.forEach(v->{
            SysFunctionBean item = new SysFunctionBean();
            BeanUtils.copyProperties(v,item);
            roleBeans.add(item);
        });
        return roleBeans;
    }

    @Override
    public List<SysFunctionBean> findList(SysFunctionBean bean){
        LambdaQueryWrapper<SysFunction> wrapper = createWrapper(bean);

        // 分页查询
        List<SysFunction> list = functionMapper.selectList(wrapper);
        if(CollectionUtils.isEmpty(list)) {
            return Collections.EMPTY_LIST;
        }

        List<SysFunctionBean> beanList = list.stream().map(item -> {
            SysFunctionBean srcBean = new SysFunctionBean();
            BeanUtils.copyProperties(item,srcBean);
            return srcBean;
        }).collect(Collectors.toList());
        return beanList;
    }

    @Override
    public Page<SysFunctionBean> findPage(SysFunctionBean bean, PageBean pageBean){
        LambdaQueryWrapper<SysFunction> wrapper = createWrapper(bean);

        // 分页查询
        Page<SysFunction> page = new Page<>(pageBean.getCurrent(),pageBean.getSize());
        page = functionMapper.selectPage(page,wrapper);

        if(CollectionUtils.isEmpty(page.getRecords())) {
            return new Page<>(pageBean.getCurrent(), pageBean.getSize());
        }

        Page<SysFunctionBean> restPage = new Page<>();
        BeanUtils.copyProperties(page,restPage,"records");
        restPage.setRecords(new ArrayList<>());
        page.getRecords().forEach(v->{
            SysFunctionBean itemBean = new SysFunctionBean();
            BeanUtils.copyProperties(v, itemBean);
            restPage.getRecords().add(itemBean);
        });
        return restPage;
    }

    @Override
    public Boolean repetition(SysFunctionBean bean){
        if( functionMapper.selectCount(createWrapper(bean)) > 0 ){
            return true;
        }else{
            return false;
        }
    }

    @Transactional
    @Override
    public SysFunctionBean add(SysFunctionBean bean){


        SysFunction function = new SysFunction();
        BeanUtils.copyProperties(bean,function);

        functionMapper.insert(function);
        BeanUtils.copyProperties(function,bean);
        return bean;
    }

    @Transactional
    @Override
    public RestBean updateByCode(SysFunctionBean bean) {
        SysFunction source = new SysFunction();
        BeanUtils.copyProperties(bean,source);
        return RestBean.ok(functionMapper.updateById(source));
    }

    @Transactional
    @Override
    public RestBean deleteByCode(String code) {
        if(ObjectUtils.isEmpty(code)){
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }

        SysFunction updateSource = new SysFunction();
        updateSource.setCode(code);
        updateSource.setValid(DataValid.INVALID);

        return RestBean.ok(functionMapper.updateById(updateSource));
    }

    @Transactional
    @Override
    public void deleteByCode(List<String> codeList) {
        if(ObjectUtils.isEmpty(codeList)){
            return;
        }

        codeList.forEach(v -> {
            deleteByCode(v);
        });
    }

    @Override
    public SysFunctionBean findOne(SysFunctionBean bean) {
        SysFunction entity = functionMapper.selectOne(createWrapper(bean));
        if(entity == null) {
            return null;
        }

        SysFunctionBean restBean = new SysFunctionBean();
        BeanUtils.copyProperties(entity,restBean);
        return restBean;
    }

    /**
     * 建立查询条件
     * 条件尽量都写在此方法
     * @param bean
     * @return
     */
    private LambdaQueryWrapper createWrapper(SysFunctionBean bean){
        LambdaQueryWrapper<SysFunction> wrapper = Wrappers.lambdaQuery();
        if(bean == null || bean.getValid() == null){
            wrapper.eq(SysFunction::getValid,DataValid.VALID);
        }

        // 自定义条件
        if(bean != null) {
            if(bean.getValid() != null){
                wrapper.eq(SysFunction::getValid,bean.getValid());
            }
            if(!ObjectUtils.isEmpty(bean.getCode())){
                wrapper.eq(SysFunction::getCode, bean.getCode());
            }

            if(StringUtils.isNotBlank(bean.getName())){
                wrapper.like(SysFunction::getName,bean.getName());
            }
            if(CollectionUtils.isNotEmpty(bean.getFunctionCodeList())){
                wrapper.in(SysFunction::getCode, bean.getFunctionCodeList());
            }
            if(StringUtils.isNotBlank(bean.getModule())){
                wrapper.eq(SysFunction::getModule, bean.getModule());
            }
        }

        return wrapper;
    }
}
