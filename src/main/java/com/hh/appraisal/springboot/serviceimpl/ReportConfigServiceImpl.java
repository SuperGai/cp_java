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

import com.hh.appraisal.springboot.bean.ReportConfigBean;
import com.hh.appraisal.springboot.entity.ReportConfig;
import com.hh.appraisal.springboot.dao.ReportConfigMapper;
import com.hh.appraisal.springboot.service.ReportConfigService;

/**
 * ReportConfig Service 实现类
 * @author gaigai
 * @date 2023/06/02
 */
@Slf4j
@Service
public class ReportConfigServiceImpl extends ServiceImpl<ReportConfigMapper, ReportConfig> implements ReportConfigService {

    private final ReportConfigMapper reportConfigMapper;

    public ReportConfigServiceImpl(ReportConfigMapper reportConfigMapper) {
            this.reportConfigMapper = reportConfigMapper;
    }

    @Override
    public ReportConfigBean findByCode(String code){
        if(ObjectUtils.isEmpty(code)){
            return null;
        }

        List<ReportConfig> sourceList = reportConfigMapper.selectList(
                createWrapper(ReportConfigBean.builder().code(code).build())
        );
        if(ObjectUtils.isEmpty(sourceList)){
            return null;
        }

        ReportConfigBean restBean = ReportConfigBean.builder().build();
        BeanUtils.copyProperties(sourceList.get(0), restBean);
        return restBean;
    }

    @Override
    public List<ReportConfigBean> findByCodeList(List<String> codeList){
        if(CollectionUtils.isEmpty(codeList)) {
            return Collections.EMPTY_LIST;
        }

        List<ReportConfig> sourceList = reportConfigMapper.selectList(
                createWrapper(ReportConfigBean.builder().reportConfigCodeList(codeList).build())
        );
        if(CollectionUtils.isEmpty(sourceList)) {
            return Collections.EMPTY_LIST;
        }

        List<ReportConfigBean> restBeanList = sourceList.stream().map(v -> {
            ReportConfigBean item = new ReportConfigBean();
            BeanUtils.copyProperties(v,item);
            return item;
        }).collect(Collectors.toList());
        return restBeanList;
    }

    @Override
    public List<ReportConfigBean> findList(ReportConfigBean bean){
        List<ReportConfig> list = reportConfigMapper.selectList(createWrapper(bean));
        if(CollectionUtils.isEmpty(list)) {
            return Collections.EMPTY_LIST;
        }

        List<ReportConfigBean> beanList = list.stream().map(item -> {
            ReportConfigBean srcBean = new ReportConfigBean();
            BeanUtils.copyProperties(item,srcBean);
            return srcBean;
        }).collect(Collectors.toList());
        return beanList;
    }

    @Override
    public Page<ReportConfigBean> findPage(ReportConfigBean bean, PageBean pageBean){
        Page<ReportConfig> sourcePage = reportConfigMapper.selectPage(
                new Page<>(pageBean.getCurrent(),pageBean.getSize()),
                createWrapper(bean)
        );
        if(CollectionUtils.isEmpty(sourcePage.getRecords())) {
            return new Page<>();
        }

        Page<ReportConfigBean> restPage = new Page<>();
        BeanUtils.copyProperties(sourcePage, restPage,"records");
        restPage.setRecords(new ArrayList<>(sourcePage.getRecords().size()));
        sourcePage.getRecords().forEach(v -> {
            ReportConfigBean itemBean = new ReportConfigBean();
            BeanUtils.copyProperties(v, itemBean);
            restPage.getRecords().add(itemBean);
        });
        return restPage;
    }

    @Transactional
    @Override
    public ReportConfigBean add(ReportConfigBean bean){
        ReportConfig source = new ReportConfig();
        BeanUtils.copyProperties(bean, source);

        reportConfigMapper.insert(source);
        BeanUtils.copyProperties(source, bean);
        return bean;
    }

    @Transactional
    @Override
    public int updateByCode(ReportConfigBean bean) {
        ReportConfig source = new ReportConfig();
        BeanUtils.copyProperties(bean,source);
        return reportConfigMapper.updateById(source);
    }

    @Transactional
    @Override
    public int deleteByCode(String code) {
        if(ObjectUtils.isEmpty(code)){
            return 0;
        }

        ReportConfig updateSource = new ReportConfig();
        updateSource.setCode(code);
        updateSource.setValid(DataValid.INVALID);
        return reportConfigMapper.updateById(updateSource);
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
    private LambdaQueryWrapper createWrapper(ReportConfigBean bean){
        LambdaQueryWrapper<ReportConfig> wrapper = Wrappers.lambdaQuery();
        if(bean == null || bean.getValid() == null){
            wrapper.eq(ReportConfig::getValid,DataValid.VALID);
        }

        // 自定义条件
        if(bean != null) {
            if(bean.getValid() != null){
                wrapper.eq(ReportConfig::getValid,bean.getValid());
            }
            if(!ObjectUtils.isEmpty(bean.getCode())){
                wrapper.eq(ReportConfig::getCode, bean.getCode());
            }
            if(CollectionUtils.isNotEmpty(bean.getReportConfigCodeList())){
                wrapper.in(ReportConfig::getCode, bean.getReportConfigCodeList());
            }

            // 编写条件逻辑....

        }

        return wrapper;
    }
}
