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

import com.hh.appraisal.springboot.bean.DivisorBean;
import com.hh.appraisal.springboot.entity.Divisor;
import com.hh.appraisal.springboot.entity.EvaluatoionUser;
import com.hh.appraisal.springboot.dao.DivisorMapper;
import com.hh.appraisal.springboot.service.DivisorService;

/**
 * Divisor Service 实现类
 * @author gaigai
 * @date 2021/06/26
 */
@Slf4j
@Service
public class DivisorServiceImpl extends ServiceImpl<DivisorMapper, Divisor> implements DivisorService {

    private final DivisorMapper divisorMapper;

    public DivisorServiceImpl(DivisorMapper divisorMapper) {
            this.divisorMapper = divisorMapper;
    }

    @Override
    public DivisorBean findByCode(String code){
        if(ObjectUtils.isEmpty(code)){
            return null;
        }

        List<Divisor> sourceList = divisorMapper.selectList(
                createWrapper(DivisorBean.builder().code(code).build())
        );
        if(ObjectUtils.isEmpty(sourceList)){
            return null;
        }

        DivisorBean restBean = DivisorBean.builder().build();
        BeanUtils.copyProperties(sourceList.get(0), restBean);
        return restBean;
    }

    @Override
    public List<DivisorBean> findByCodeList(List<String> codeList){
        if(CollectionUtils.isEmpty(codeList)) {
            return Collections.EMPTY_LIST;
        }

        List<Divisor> sourceList = divisorMapper.selectList(
                createWrapper(DivisorBean.builder().divisorCodeList(codeList).build())
        );
        if(CollectionUtils.isEmpty(sourceList)) {
            return Collections.EMPTY_LIST;
        }

        List<DivisorBean> restBeanList = sourceList.stream().map(v -> {
            DivisorBean item = new DivisorBean();
            BeanUtils.copyProperties(v,item);
            return item;
        }).collect(Collectors.toList());
        return restBeanList;
    }

    @Override
    public List<DivisorBean> findList(DivisorBean bean){
        List<Divisor> list = divisorMapper.selectList(createWrapper(bean));
        if(CollectionUtils.isEmpty(list)) {
            return Collections.EMPTY_LIST;
        }

        List<DivisorBean> beanList = list.stream().map(item -> {
            DivisorBean srcBean = new DivisorBean();
            BeanUtils.copyProperties(item,srcBean);
            return srcBean;
        }).collect(Collectors.toList());
        return beanList;
    }

    @Override
    public Page<DivisorBean> findPage(DivisorBean bean, PageBean pageBean){
        Page<Divisor> sourcePage = divisorMapper.selectPage(
                new Page<>(pageBean.getCurrent(),pageBean.getSize()),
                createWrapper(bean)
        );
        if(CollectionUtils.isEmpty(sourcePage.getRecords())) {
            return new Page<>();
        }

        Page<DivisorBean> restPage = new Page<>();
        BeanUtils.copyProperties(sourcePage, restPage,"records");
        restPage.setRecords(new ArrayList<>(sourcePage.getRecords().size()));
        sourcePage.getRecords().forEach(v -> {
            DivisorBean itemBean = new DivisorBean();
            BeanUtils.copyProperties(v, itemBean);
            restPage.getRecords().add(itemBean);
        });
        return restPage;
    }

    @Transactional
    @Override
    public DivisorBean add(DivisorBean bean){
        Divisor source = new Divisor();
        BeanUtils.copyProperties(bean, source);

        divisorMapper.insert(source);
        BeanUtils.copyProperties(source, bean);
        return bean;
    }

    @Transactional
    @Override
    public int updateByCode(DivisorBean bean) {
        Divisor source = new Divisor();
        BeanUtils.copyProperties(bean,source);
        return divisorMapper.updateById(source);
    }

    @Transactional
    @Override
    public int deleteByCode(String code) {
        if(ObjectUtils.isEmpty(code)){
            return 0;
        }

        Divisor updateSource = new Divisor();
        updateSource.setCode(code);
        updateSource.setValid(DataValid.INVALID);
        return divisorMapper.updateById(updateSource);
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
    private LambdaQueryWrapper createWrapper(DivisorBean bean){
        LambdaQueryWrapper<Divisor> wrapper = Wrappers.lambdaQuery();
        if(bean == null || bean.getValid() == null){
            wrapper.eq(Divisor::getValid,DataValid.VALID);
        }

        // 自定义条件
        if(bean != null) {
            if(bean.getValid() != null){
                wrapper.eq(Divisor::getValid,bean.getValid());
            }
            if(!ObjectUtils.isEmpty(bean.getCode())){
                wrapper.eq(Divisor::getCode, bean.getCode());
            }
            if(CollectionUtils.isNotEmpty(bean.getDivisorCodeList())){
                wrapper.in(Divisor::getCode, bean.getDivisorCodeList());
            }if (!ObjectUtils.isEmpty(bean.getDivisorDesc())) {
				wrapper.like(Divisor::getDivisorDesc,bean.getDivisorDesc());
			}if (!ObjectUtils.isEmpty(bean.getDivisorName())) {
				wrapper.like(Divisor::getDivisorName,bean.getDivisorName());
			}

            // 编写条件逻辑....

        }

        return wrapper;
    }
}
