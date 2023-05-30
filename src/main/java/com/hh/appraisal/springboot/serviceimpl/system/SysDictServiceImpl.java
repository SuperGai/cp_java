package com.hh.appraisal.springboot.serviceimpl.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hh.appraisal.springboot.bean.system.SysDictBean;
import com.hh.appraisal.springboot.bean.system.SysDictTypeBean;
import com.hh.appraisal.springboot.core.baen.PageBean;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.core.constant.DataValid;
import com.hh.appraisal.springboot.core.constant.RestCode;
import com.hh.appraisal.springboot.dao.system.SysDictMapper;
import com.hh.appraisal.springboot.entity.system.SysDict;
import com.hh.appraisal.springboot.service.system.SysDictService;
import com.hh.appraisal.springboot.service.system.SysDictTypeService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service 实现
 * @author gaigai
 * @date 2019/05/15
 */
@Slf4j
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements SysDictService {

    private final SysDictMapper dictMapper;
    private final SysDictTypeService dictTypeService;

    public SysDictServiceImpl(SysDictMapper dictMapper, SysDictTypeService dictTypeService) {
        this.dictMapper = dictMapper;
        this.dictTypeService = dictTypeService;
    }

    @Override
    public HashMap<String, SysDictBean> findAllDictByType(String dictTypeNum){
        if(StringUtils.isBlank(dictTypeNum)) {
            return null;
        }

        // 获取字典值集合
        SysDictTypeBean typeBean = dictTypeService.findOne(SysDictTypeBean.builder().num(dictTypeNum).build());
        if(typeBean == null) {
            return new HashMap<>();
        }

        List<SysDictBean> dictList = findList(SysDictBean.builder()
                .typeCode(typeBean.getCode())
                .build());
        if(CollectionUtils.isEmpty(dictList)) {
            return new HashMap<>();
        }

        HashMap<String, SysDictBean> restMap = new HashMap<>();
        dictList.forEach(v->{
            restMap.put(v.getValue(), v);
        });
        return restMap;
    }

    @Override
    public List<SysDictBean> findByCodeList(List<String> codeList){
        if(CollectionUtils.isEmpty(codeList)) {
            return Collections.EMPTY_LIST;
        }

        LambdaQueryWrapper<SysDict> wrapper = createWrapper(null);
        wrapper.in(SysDict::getCode, codeList);
        List<SysDict> roles = dictMapper.selectList(wrapper);

        List<SysDictBean> roleBeans = new ArrayList<>();
        roles.forEach(v->{
            SysDictBean item = new SysDictBean();
            BeanUtils.copyProperties(v,item);
            roleBeans.add(item);
        });
        return roleBeans;
    }

    @Override
    public List<SysDictBean> findList(SysDictBean bean){
        LambdaQueryWrapper<SysDict> wrapper = createWrapper(bean);

        // 分页查询
        List<SysDict> list = dictMapper.selectList(wrapper);
        if(CollectionUtils.isEmpty(list)) {
            return Collections.EMPTY_LIST;
        }

        List<SysDictBean> beanList = list.stream().map(item -> {
            SysDictBean srcBean = new SysDictBean();
            BeanUtils.copyProperties(item,srcBean);
            return srcBean;
        }).collect(Collectors.toList());
        return beanList;
    }

    @Override
    public Page<SysDictBean> findPage(SysDictBean bean, PageBean pageBean){
        LambdaQueryWrapper<SysDict> wrapper = createWrapper(bean);

        // 分页查询
        Page<SysDict> page = new Page<>(pageBean.getCurrent(),pageBean.getSize());
        page = dictMapper.selectPage(page,wrapper);

        if(CollectionUtils.isEmpty(page.getRecords())) {
            return new Page<>();
        }

        Page<SysDictBean> beanPage = new Page<>();
        BeanUtils.copyProperties(page,beanPage,"records");
        beanPage.setRecords(new ArrayList<>());
        page.getRecords().forEach(v->{
            SysDictBean dictBean = new SysDictBean();
            BeanUtils.copyProperties(v, dictBean);
            beanPage.getRecords().add(dictBean);
        });
        return beanPage;
    }

    @Override
    public SysDictBean addWithOutRestBean(SysDictBean bean){
        RestBean restBean = this.add(bean);
        if(RestCode.DEFAULT_SUCCESS.getCode() != restBean.getCode()){
            return null;
        }
        return (SysDictBean) restBean.getData();
    }

    @Transactional
    @Override
    public RestBean add(SysDictBean bean){
        // 根据typeCode获取字典类型信息
        SysDictTypeBean typeBean = dictTypeService.findByCode(bean.getTypeCode());
        if(typeBean == null) {
            RestBean.error("找不到字典类型数据");
        }
        bean.setTypeName(typeBean.getName());
        bean.setTypeCode(typeBean.getCode());
        bean.setTypeCode(typeBean.getCode());

        // 检查同字典类型是否有value相同的字典
        LambdaQueryWrapper<SysDict> countWrapper = Wrappers.lambdaQuery();
        countWrapper.eq(SysDict::getTypeCode, typeBean.getCode());
        countWrapper.eq(SysDict::getValue, bean.getValue());
        countWrapper.eq(SysDict::getValid, DataValid.VALID);
        int iCount = this.dictMapper.selectCount(countWrapper);
        if(iCount > 0){
            return RestBean.error("重复的字典值，请输入其他字典值");
        }

        SysDict source = new SysDict();
        BeanUtils.copyProperties(bean,source);

        dictMapper.insert(source);
        BeanUtils.copyProperties(source,bean);
        return RestBean.ok(bean);
    }

    @Transactional
    @Override
    public RestBean updateByCode(SysDictBean bean) {

        if(ObjectUtils.isEmpty(bean.getCode()) || ObjectUtils.isEmpty(bean.getTypeCode())){
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }

        // 根据typeCode获取字典类型信息
        SysDictTypeBean typeBean = dictTypeService.findByCode(bean.getTypeCode());
        if(typeBean == null) {
            return null;
        }
        bean.setTypeName(typeBean.getName());
        bean.setTypeCode(typeBean.getCode());
        bean.setTypeCode(typeBean.getCode());

        // 检查同字典类型是否有value相同的字典
        LambdaQueryWrapper<SysDict> countWrapper = Wrappers.lambdaQuery();
        countWrapper.eq(SysDict::getTypeCode, bean.getTypeCode());
        countWrapper.eq(SysDict::getValue, bean.getValue());
        countWrapper.ne(SysDict::getCode, bean.getCode());// 不能是自己
        int iCount = this.dictMapper.selectCount(countWrapper);
        if(iCount > 0){
            return RestBean.error("重复的字典值，请输入其他字典值");
        }

        SysDict source = new SysDict();
        BeanUtils.copyProperties(bean,source);
        return RestBean.ok(dictMapper.updateById(source));
    }

    @Transactional
    @Override
    public RestBean deleteByCode(String code) {
        if(ObjectUtils.isEmpty(code)){
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }

        SysDict updateSource = new SysDict();
        updateSource.setCode(code);
        updateSource.setValid(DataValid.INVALID);

        return RestBean.ok(dictMapper.updateById(updateSource));
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
    public SysDictBean findByCode(String code) {
        SysDict source = dictMapper.selectById(code);
        if(source == null){
            return null;
        }

        SysDictBean restBean = new SysDictBean();
        BeanUtils.copyProperties(source, restBean);
        return restBean;
    }

    @Override
    public SysDictBean findOne(SysDictBean bean) {
        List<SysDict> sourceList = dictMapper.selectList(createWrapper(bean));
        if(CollectionUtils.isEmpty(sourceList)){
            return null;
        }

        SysDictBean restBean = new SysDictBean();
        BeanUtils.copyProperties(sourceList.get(0), restBean);
        return restBean;
    }

    /**
     * 建立查询条件
     * 条件尽量都写在此方法
     * @param bean
     * @return
     */
    private LambdaQueryWrapper createWrapper(SysDictBean bean){
        LambdaQueryWrapper<SysDict> wrapper = Wrappers.lambdaQuery();
        if(bean == null || bean.getValid() == null){
            wrapper.eq(SysDict::getValid,DataValid.VALID);
        }

        // 自定义条件
        if(bean != null) {
            if(bean.getValid() != null){
                wrapper.eq(SysDict::getValid,bean.getValid());
            }
            if(!ObjectUtils.isEmpty(bean.getCode())){
                wrapper.eq(SysDict::getCode, bean.getCode());
            }

            if(!ObjectUtils.isEmpty(bean.getTypeCode())){
                wrapper.eq(SysDict::getTypeCode, bean.getTypeCode());
            }

            if(StringUtils.isNotBlank(bean.getName())){
                wrapper.like(SysDict::getName, bean.getName());
            }

            if(StringUtils.isNotBlank(bean.getData())){
                wrapper.eq(SysDict::getData,bean.getData());
            }

            if(StringUtils.isNotBlank(bean.getValue())){
                wrapper.eq(SysDict::getValue, bean.getValue());
            }

            if(StringUtils.isNotBlank(bean.getNotes())){
                wrapper.like(SysDict::getNotes, bean.getNotes());
            }

            if(CollectionUtils.isNotEmpty(bean.getDictTypeCodeList())){
                wrapper.in(SysDict::getTypeCode, bean.getDictTypeCodeList());
            }
        }

        return wrapper;
    }
}
