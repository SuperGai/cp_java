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

import com.hh.appraisal.springboot.bean.DivisorWithQuesBean;
import com.hh.appraisal.springboot.entity.DivisorWithQues;
import com.hh.appraisal.springboot.dao.DivisorWithQuesMapper;
import com.hh.appraisal.springboot.service.DivisorWithQuesService;

/**
 * DivisorWithQues Service 实现类
 * @author gaigai
 * @date 2021/06/26
 */
@Slf4j
@Service
public class DivisorWithQuesServiceImpl extends ServiceImpl<DivisorWithQuesMapper, DivisorWithQues> implements DivisorWithQuesService {

    private final DivisorWithQuesMapper divisorWithQuesMapper;

    public DivisorWithQuesServiceImpl(DivisorWithQuesMapper divisorWithQuesMapper) {
            this.divisorWithQuesMapper = divisorWithQuesMapper;
    }

    @Override
    public DivisorWithQuesBean findByCode(String code){
        if(ObjectUtils.isEmpty(code)){
            return null;
        }

        List<DivisorWithQues> sourceList = divisorWithQuesMapper.selectList(
                createWrapper(DivisorWithQuesBean.builder().code(code).build())
        );
        if(ObjectUtils.isEmpty(sourceList)){
            return null;
        }

        DivisorWithQuesBean restBean = DivisorWithQuesBean.builder().build();
        BeanUtils.copyProperties(sourceList.get(0), restBean);
        return restBean;
    }

    @Override
    public List<DivisorWithQuesBean> findByCodeList(List<String> codeList){
        if(CollectionUtils.isEmpty(codeList)) {
            return Collections.EMPTY_LIST;
        }

        List<DivisorWithQues> sourceList = divisorWithQuesMapper.selectList(
                createWrapper(DivisorWithQuesBean.builder().divisorWithQuesCodeList(codeList).build())
        );
        if(CollectionUtils.isEmpty(sourceList)) {
            return Collections.EMPTY_LIST;
        }

        List<DivisorWithQuesBean> restBeanList = sourceList.stream().map(v -> {
            DivisorWithQuesBean item = new DivisorWithQuesBean();
            BeanUtils.copyProperties(v,item);
            return item;
        }).collect(Collectors.toList());
        return restBeanList;
    }

    @Override
    public List<DivisorWithQuesBean> findList(DivisorWithQuesBean bean){
        List<DivisorWithQues> list = divisorWithQuesMapper.selectList(createWrapper(bean));
        if(CollectionUtils.isEmpty(list)) {
            return Collections.EMPTY_LIST;
        }

        List<DivisorWithQuesBean> beanList = list.stream().map(item -> {
            DivisorWithQuesBean srcBean = new DivisorWithQuesBean();
            BeanUtils.copyProperties(item,srcBean);
            return srcBean;
        }).collect(Collectors.toList());
        return beanList;
    }

    @Override
    public Page<DivisorWithQuesBean> findPage(DivisorWithQuesBean bean, PageBean pageBean){
        Page<DivisorWithQues> sourcePage = divisorWithQuesMapper.selectPage(
                new Page<>(pageBean.getCurrent(),pageBean.getSize()),
                createWrapper(bean)
        );
        if(CollectionUtils.isEmpty(sourcePage.getRecords())) {
            return new Page<>();
        }

        Page<DivisorWithQuesBean> restPage = new Page<>();
        BeanUtils.copyProperties(sourcePage, restPage,"records");
        restPage.setRecords(new ArrayList<>(sourcePage.getRecords().size()));
        sourcePage.getRecords().forEach(v -> {
            DivisorWithQuesBean itemBean = new DivisorWithQuesBean();
            BeanUtils.copyProperties(v, itemBean);
            restPage.getRecords().add(itemBean);
        });
        return restPage;
    }

    @Transactional
    @Override
    public DivisorWithQuesBean add(DivisorWithQuesBean bean){
        DivisorWithQues source = new DivisorWithQues();
        BeanUtils.copyProperties(bean, source);

        divisorWithQuesMapper.insert(source);
        BeanUtils.copyProperties(source, bean);
        return bean;
    }

    @Transactional
    @Override
    public int updateByCode(DivisorWithQuesBean bean) {
        DivisorWithQues source = new DivisorWithQues();
        BeanUtils.copyProperties(bean,source);
        return divisorWithQuesMapper.updateById(source);
    }

    @Transactional
    @Override
    public int deleteByCode(String code) {
        if(ObjectUtils.isEmpty(code)){
            return 0;
        }

        DivisorWithQues updateSource = new DivisorWithQues();
        updateSource.setCode(code);
        updateSource.setValid(DataValid.INVALID);
        return divisorWithQuesMapper.updateById(updateSource);
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
    private LambdaQueryWrapper createWrapper(DivisorWithQuesBean bean){
        LambdaQueryWrapper<DivisorWithQues> wrapper = Wrappers.lambdaQuery();
        if(bean == null || bean.getValid() == null){
            wrapper.eq(DivisorWithQues::getValid,DataValid.VALID);
        }

        // 自定义条件
        if(bean != null) {
            if(bean.getValid() != null){
                wrapper.eq(DivisorWithQues::getValid,bean.getValid());
            }
            if(!ObjectUtils.isEmpty(bean.getCode())){
                wrapper.eq(DivisorWithQues::getCode, bean.getCode());
            }
            if(CollectionUtils.isNotEmpty(bean.getDivisorWithQuesCodeList())){
                wrapper.in(DivisorWithQues::getCode, bean.getDivisorWithQuesCodeList());
            }

            // 编写条件逻辑....

        }

        return wrapper;
    }
}
