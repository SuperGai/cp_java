package com.hh.appraisal.springboot.serviceimpl;

import cn.hutool.extra.emoji.EmojiUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hh.appraisal.springboot.bean.WxMiniAppUserBean;
import com.hh.appraisal.springboot.core.baen.PageBean;
import com.hh.appraisal.springboot.core.constant.DataValid;
import com.hh.appraisal.springboot.dao.WxMiniAppUserMapper;
import com.hh.appraisal.springboot.entity.WxMiniAppUser;
import com.hh.appraisal.springboot.service.WxMiniAppUserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * WxMiniAppUser Service 实现类
 * @author gaigai
 * @date 2021/02/01
 */
@Slf4j
@Service
public class WxMiniAppUserServiceImpl extends ServiceImpl<WxMiniAppUserMapper, WxMiniAppUser> implements WxMiniAppUserService {

    private final WxMiniAppUserMapper wxMiniAppUserMapper;

    public WxMiniAppUserServiceImpl(WxMiniAppUserMapper wxMiniAppUserMapper) {
            this.wxMiniAppUserMapper = wxMiniAppUserMapper;
    }

    @Override
    public WxMiniAppUserBean findOne(WxMiniAppUserBean searchBean) {
        List<WxMiniAppUser> sourceList = wxMiniAppUserMapper.selectList(createWrapper(searchBean));
        if(CollectionUtils.isEmpty(sourceList)){
            return null;
        }

        WxMiniAppUserBean bean = new WxMiniAppUserBean();
        BeanUtils.copyProperties(sourceList.get(0), bean);

        // 处理表情字符串
        if(ObjectUtils.isNotEmpty(bean.getNickName())){
            bean.setNickName(EmojiUtil.toUnicode(bean.getNickName()));
        }

        return bean;
    }

    @Override
    public WxMiniAppUserBean findByCode(String code){
        if(ObjectUtils.isEmpty(code)){
            return null;
        }

        List<WxMiniAppUser> sourceList = wxMiniAppUserMapper.selectList(
                createWrapper(WxMiniAppUserBean.builder().code(code).build())
        );
        if(ObjectUtils.isEmpty(sourceList)){
            return null;
        }

        WxMiniAppUserBean restBean = WxMiniAppUserBean.builder().build();
        BeanUtils.copyProperties(sourceList.get(0), restBean);
        return restBean;
    }

    @Override
    public List<WxMiniAppUserBean> findByCodeList(List<String> codeList){
        if(CollectionUtils.isEmpty(codeList)) {
            return Collections.EMPTY_LIST;
        }

        List<WxMiniAppUser> sourceList = wxMiniAppUserMapper.selectList(
                createWrapper(WxMiniAppUserBean.builder().wxMiniAppUserCodeList(codeList).build())
        );
        if(CollectionUtils.isEmpty(sourceList)) {
            return Collections.EMPTY_LIST;
        }

        List<WxMiniAppUserBean> restBeanList = sourceList.stream().map(v -> {
            WxMiniAppUserBean item = new WxMiniAppUserBean();
            BeanUtils.copyProperties(v,item);
            return item;
        }).collect(Collectors.toList());
        return restBeanList;
    }

    @Override
    public List<WxMiniAppUserBean> findList(WxMiniAppUserBean bean){
        List<WxMiniAppUser> list = wxMiniAppUserMapper.selectList(createWrapper(bean));
        if(CollectionUtils.isEmpty(list)) {
            return Collections.EMPTY_LIST;
        }

        List<WxMiniAppUserBean> beanList = list.stream().map(item -> {
            WxMiniAppUserBean srcBean = new WxMiniAppUserBean();
            BeanUtils.copyProperties(item,srcBean);
            return srcBean;
        }).collect(Collectors.toList());
        return beanList;
    }

    @Override
    public Page<WxMiniAppUserBean> findPage(WxMiniAppUserBean bean, PageBean pageBean){
        Page<WxMiniAppUser> sourcePage = wxMiniAppUserMapper.selectPage(
                new Page<>(pageBean.getCurrent(),pageBean.getSize()),
                createWrapper(bean)
        );
        if(CollectionUtils.isEmpty(sourcePage.getRecords())) {
            return new Page<>();
        }

        Page<WxMiniAppUserBean> restPage = new Page<>();
        BeanUtils.copyProperties(sourcePage, restPage,"records");
        restPage.setRecords(new ArrayList<>(sourcePage.getRecords().size()));
        sourcePage.getRecords().forEach(v -> {
            WxMiniAppUserBean itemBean = new WxMiniAppUserBean();
            BeanUtils.copyProperties(v, itemBean);
            restPage.getRecords().add(itemBean);
        });
        return restPage;
    }

    @Transactional
    @Override
    public WxMiniAppUserBean add(WxMiniAppUserBean bean){
        WxMiniAppUser source = new WxMiniAppUser();
        BeanUtils.copyProperties(bean, source);

        wxMiniAppUserMapper.insert(source);
        BeanUtils.copyProperties(source, bean);
        return bean;
    }

    @Transactional
    @Override
    public int updateByCode(WxMiniAppUserBean bean) {
        WxMiniAppUser source = new WxMiniAppUser();
        BeanUtils.copyProperties(bean,source);
        return wxMiniAppUserMapper.updateById(source);
    }

    @Transactional
    @Override
    public int deleteByCode(String code) {
        if(ObjectUtils.isEmpty(code)){
            return 0;
        }

        WxMiniAppUser updateSource = new WxMiniAppUser();
        updateSource.setCode(code);
        updateSource.setValid(DataValid.INVALID);
        return wxMiniAppUserMapper.updateById(updateSource);
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
    private LambdaQueryWrapper createWrapper(WxMiniAppUserBean bean){
        LambdaQueryWrapper<WxMiniAppUser> wrapper = Wrappers.lambdaQuery();
        if(bean == null || bean.getValid() == null){
            wrapper.eq(WxMiniAppUser::getValid,DataValid.VALID);
        }

        // 自定义条件
        if(bean != null) {
            if(bean.getValid() != null){
                wrapper.eq(WxMiniAppUser::getValid,bean.getValid());
            }
            if(!ObjectUtils.isEmpty(bean.getCode())){
                wrapper.eq(WxMiniAppUser::getCode, bean.getCode());
            }
            if(CollectionUtils.isNotEmpty(bean.getWxMiniAppUserCodeList())){
                wrapper.in(WxMiniAppUser::getCode, bean.getWxMiniAppUserCodeList());
            }

            // 编写条件逻辑....

        }

        return wrapper;
    }
}
