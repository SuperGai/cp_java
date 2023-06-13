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

import com.hh.appraisal.springboot.bean.NormManageBean;
import com.hh.appraisal.springboot.entity.NormManage;
import com.hh.appraisal.springboot.dao.NormManageMapper;
import com.hh.appraisal.springboot.service.NormManageService;

/**
 * NormManage Service 实现类
 * @author gaigai
 * @date 2023/06/02
 */
@Slf4j
@Service
public class NormManageServiceImpl extends ServiceImpl<NormManageMapper, NormManage> implements NormManageService {

    private final NormManageMapper normManageMapper;

    public NormManageServiceImpl(NormManageMapper normManageMapper) {
            this.normManageMapper = normManageMapper;
    }

    @Override
    public NormManageBean findByCode(String code){
        if(ObjectUtils.isEmpty(code)){
            return null;
        }

        List<NormManage> sourceList = normManageMapper.selectList(
                createWrapper(NormManageBean.builder().code(code).build())
        );
        if(ObjectUtils.isEmpty(sourceList)){
            return null;
        }

        NormManageBean restBean = NormManageBean.builder().build();
        BeanUtils.copyProperties(sourceList.get(0), restBean);
        return restBean;
    }

    @Override
    public List<NormManageBean> findByCodeList(List<String> codeList){
        if(CollectionUtils.isEmpty(codeList)) {
            return Collections.EMPTY_LIST;
        }

        List<NormManage> sourceList = normManageMapper.selectList(
                createWrapper(NormManageBean.builder().normManageCodeList(codeList).build())
        );
        if(CollectionUtils.isEmpty(sourceList)) {
            return Collections.EMPTY_LIST;
        }

        List<NormManageBean> restBeanList = sourceList.stream().map(v -> {
            NormManageBean item = new NormManageBean();
            BeanUtils.copyProperties(v,item);
            return item;
        }).collect(Collectors.toList());
        return restBeanList;
    }

    @Override
    public List<NormManageBean> findList(NormManageBean bean){
        List<NormManage> list = normManageMapper.selectList(createWrapper(bean));
        if(CollectionUtils.isEmpty(list)) {
            return Collections.EMPTY_LIST;
        }

        List<NormManageBean> beanList = list.stream().map(item -> {
            NormManageBean srcBean = new NormManageBean();
            BeanUtils.copyProperties(item,srcBean);
            return srcBean;
        }).collect(Collectors.toList());
        return beanList;
    }

    @Override
    public Page<NormManageBean> findPage(NormManageBean bean, PageBean pageBean){
        Page<NormManage> sourcePage = normManageMapper.selectPage(
                new Page<>(pageBean.getCurrent(),pageBean.getSize()),
                createWrapper(bean)
        );
        if(CollectionUtils.isEmpty(sourcePage.getRecords())) {
            return new Page<>();
        }

        Page<NormManageBean> restPage = new Page<>();
        BeanUtils.copyProperties(sourcePage, restPage,"records");
        restPage.setRecords(new ArrayList<>(sourcePage.getRecords().size()));
        sourcePage.getRecords().forEach(v -> {
            NormManageBean itemBean = new NormManageBean();
            BeanUtils.copyProperties(v, itemBean);
            restPage.getRecords().add(itemBean);
        });
        return restPage;
    }

    @Transactional
    @Override
    public NormManageBean add(NormManageBean bean){
        NormManage source = new NormManage();
        BeanUtils.copyProperties(bean, source);

        normManageMapper.insert(source);
        BeanUtils.copyProperties(source, bean);
        return bean;
    }

    @Transactional
    @Override
    public int updateByCode(NormManageBean bean) {
        NormManage source = new NormManage();
        BeanUtils.copyProperties(bean,source);
        return normManageMapper.updateById(source);
    }

    @Transactional
    @Override
    public int deleteByCode(String code) {
        if(ObjectUtils.isEmpty(code)){
            return 0;
        }

        NormManage updateSource = new NormManage();
        updateSource.setCode(code);
        updateSource.setValid(DataValid.INVALID);
        return normManageMapper.updateById(updateSource);
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
    private LambdaQueryWrapper createWrapper(NormManageBean bean){
        LambdaQueryWrapper<NormManage> wrapper = Wrappers.lambdaQuery();
        if(bean == null || bean.getValid() == null){
            wrapper.eq(NormManage::getValid,DataValid.VALID);
        }

        // 自定义条件
        if(bean != null) {
            if(bean.getValid() != null){
                wrapper.eq(NormManage::getValid,bean.getValid());
            }
            if(!ObjectUtils.isEmpty(bean.getCode())){
                wrapper.eq(NormManage::getCode, bean.getCode());
            }
            if(CollectionUtils.isNotEmpty(bean.getNormManageCodeList())){
                wrapper.in(NormManage::getCode, bean.getNormManageCodeList());
            }
            if(!ObjectUtils.isEmpty(bean.getName())){
                wrapper.like(NormManage::getName, bean.getName());
            }
            if(!ObjectUtils.isEmpty(bean.getNdesc())){
                wrapper.like(NormManage::getNdesc, bean.getNdesc());
            }
            // 编写条件逻辑....

        }

        return wrapper;
    }
}
