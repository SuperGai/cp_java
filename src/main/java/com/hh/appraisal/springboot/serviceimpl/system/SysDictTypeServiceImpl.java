package com.hh.appraisal.springboot.serviceimpl.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hh.appraisal.springboot.bean.system.SysDictTypeBean;
import com.hh.appraisal.springboot.core.baen.PageBean;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.core.constant.DataValid;
import com.hh.appraisal.springboot.core.constant.RestCode;
import com.hh.appraisal.springboot.dao.system.SysDictTypeMapper;
import com.hh.appraisal.springboot.entity.system.SysDictType;
import com.hh.appraisal.springboot.service.system.SysDictTypeService;

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
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType> implements SysDictTypeService {

    private final SysDictTypeMapper dictTypeMapper;

    public SysDictTypeServiceImpl(SysDictTypeMapper dictTypeMapper) {
        this.dictTypeMapper = dictTypeMapper;
    }

    @Override
    public List<SysDictTypeBean> findByCodeList(List<String> codeList){
        if(CollectionUtils.isEmpty(codeList)) {
            return Collections.EMPTY_LIST;
        }

        LambdaQueryWrapper<SysDictType> wrapper = createWrapper(null);
        wrapper.in(SysDictType::getCode, codeList);
        List<SysDictType> roles = dictTypeMapper.selectList(wrapper);

        List<SysDictTypeBean> roleBeans = new ArrayList<>();
        roles.forEach(v->{
            SysDictTypeBean item = new SysDictTypeBean();
            BeanUtils.copyProperties(v,item);
            roleBeans.add(item);
        });
        return roleBeans;
    }

    @Override
    public List<SysDictTypeBean> findList(SysDictTypeBean bean){
        LambdaQueryWrapper<SysDictType> wrapper = createWrapper(bean);

        // 分页查询
        List<SysDictType> list = dictTypeMapper.selectList(wrapper);
        if(CollectionUtils.isEmpty(list)) {
            return Collections.EMPTY_LIST;
        }

        List<SysDictTypeBean> beanList = list.stream().map(item -> {
            SysDictTypeBean srcBean = new SysDictTypeBean();
            BeanUtils.copyProperties(item,srcBean);
            return srcBean;
        }).collect(Collectors.toList());
        return beanList;
    }

    @Override
    public Page<SysDictTypeBean> findPage(SysDictTypeBean bean, PageBean pageBean){
        LambdaQueryWrapper<SysDictType> wrapper = createWrapper(bean);

        // 分页查询
        Page<SysDictType> page = new Page<>(pageBean.getCurrent(),pageBean.getSize());
        page = dictTypeMapper.selectPage(page,wrapper);

        if(CollectionUtils.isEmpty(page.getRecords())) {
            return new Page<>();
        }

        Page<SysDictTypeBean> restPage = new Page<>();
        BeanUtils.copyProperties(page,restPage,"records");
        restPage.setRecords(new ArrayList<>());
        page.getRecords().forEach(v->{
            SysDictTypeBean itemBean = new SysDictTypeBean();
            BeanUtils.copyProperties(v, itemBean);
            restPage.getRecords().add(itemBean);
        });
        return restPage;
    }

    @Transactional
    @Override
    public RestBean add(SysDictTypeBean bean){
        // 检查重复
        LambdaQueryWrapper<SysDictType> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysDictType::getNum, bean.getNum());
        wrapper.eq(SysDictType::getValid, DataValid.VALID);
        int iCount = dictTypeMapper.selectCount(wrapper);
        if(iCount > 0) {
            return RestBean.error("字典类型标识符与其他数据重复，请输入其他值");
        }

        SysDictType source = new SysDictType();
        BeanUtils.copyProperties(bean,source);

        dictTypeMapper.insert(source);
        BeanUtils.copyProperties(source, bean);
        return RestBean.ok(bean);
    }

    @Transactional
    @Override
    public RestBean updateByCode(SysDictTypeBean bean) {
        LambdaQueryWrapper<SysDictType> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysDictType::getNum, bean.getNum());
        wrapper.eq(SysDictType::getValid, DataValid.VALID);
        wrapper.ne(SysDictType::getCode, bean.getCode());
        int iCount = dictTypeMapper.selectCount(wrapper);
        if(iCount > 0) {
            return RestBean.error("字典类型标识符与其他数据重复，请输入其他值");
        }

        SysDictType source = new SysDictType();
        BeanUtils.copyProperties(bean,source);
        return RestBean.ok(dictTypeMapper.updateById(source));
    }

    @Transactional
    @Override
    public RestBean deleteByCode(String code) {
        if(ObjectUtils.isEmpty(code)){
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }

        SysDictType updateSource = new SysDictType();
        updateSource.setCode(code);
        updateSource.setValid(DataValid.INVALID);

        dictTypeMapper.updateById(updateSource);
        return RestBean.ok();
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
    public SysDictTypeBean findByCode(String code) {
        SysDictType entity = dictTypeMapper.selectById(code);
        if(entity == null) {
            return null;
        }

        SysDictTypeBean restBean = new SysDictTypeBean();
        BeanUtils.copyProperties(entity,restBean);
        return restBean;
    }

    @Override
    public SysDictTypeBean findOne(SysDictTypeBean bean) {
        List<SysDictType> sourceList = dictTypeMapper.selectList(createWrapper(bean));
        if(CollectionUtils.isEmpty(sourceList)){
            return null;
        }

        SysDictTypeBean restBean = new SysDictTypeBean();
        BeanUtils.copyProperties(sourceList.get(0), restBean);
        return restBean;
    }

    /**
     * 建立查询条件
     * 条件尽量都写在此方法
     * @param bean
     * @return
     */
    private LambdaQueryWrapper createWrapper(SysDictTypeBean bean){
        LambdaQueryWrapper<SysDictType> wrapper = Wrappers.lambdaQuery();
        if(bean == null || bean.getValid() == null){
            wrapper.eq(SysDictType::getValid,DataValid.VALID);
        }

        // 自定义条件
        if(bean != null) {
            if(bean.getValid() != null){
                wrapper.eq(SysDictType::getValid,bean.getValid());
            }
            if(!ObjectUtils.isEmpty(bean.getCode())){
                wrapper.eq(SysDictType::getCode, bean.getCode());
            }

            if(StringUtils.isNotBlank(bean.getName())){
                wrapper.like(SysDictType::getName, bean.getName());
            }
            if(StringUtils.isNotBlank(bean.getCode())){
                wrapper.eq(SysDictType::getCode, bean.getCode());
            }
            if(StringUtils.isNotBlank(bean.getNum())){
                wrapper.eq(SysDictType::getNum, bean.getNum());
            }
        }

        return wrapper;
    }
}
