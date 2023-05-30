package com.hh.appraisal.springboot.serviceimpl.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hh.appraisal.springboot.bean.system.SysUserRoleBean;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.core.constant.DataValid;
import com.hh.appraisal.springboot.core.constant.RestCode;
import com.hh.appraisal.springboot.dao.system.SysUserRoleMapper;
import com.hh.appraisal.springboot.entity.system.SysUserRole;
import com.hh.appraisal.springboot.service.system.SysUserRoleService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service 实现
 * @author gaigai
 * @date 2019/05/15
 */
@Slf4j
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

    private final SysUserRoleMapper sysUserRoleMapper;

    public SysUserRoleServiceImpl(SysUserRoleMapper sysUserRoleMapper) {
        this.sysUserRoleMapper = sysUserRoleMapper;
    }

    @Transactional
    @Override
    public SysUserRoleBean add(SysUserRoleBean bean){
        if(bean == null ||
                ObjectUtils.isEmpty(bean.getUserCode()) ||
                ObjectUtils.isEmpty(bean.getRoleCode())){
            return null;
        }

        SysUserRole source = new SysUserRole();
        BeanUtils.copyProperties(bean, source);
        sysUserRoleMapper.insert(source);

        BeanUtils.copyProperties(source, bean);
        return bean;
    }

    @Override
    public List<SysUserRoleBean> findAll(SysUserRoleBean bean) {
        LambdaQueryWrapper<SysUserRole> wrapper = createWrapper(bean);
        List<SysUserRole> userRoleList = sysUserRoleMapper.selectList(wrapper);
        List<SysUserRoleBean> userRoleBeanList = userRoleList.stream().map(item -> {
            SysUserRoleBean srcBean = new SysUserRoleBean();
            BeanUtils.copyProperties(item,srcBean);
            return srcBean;
        }).collect(Collectors.toList());
        return userRoleBeanList;
    }

    @Override
    public Integer count(SysUserRoleBean bean) {
        return sysUserRoleMapper.selectCount(createWrapper(bean));
    }

    @Transactional
    @Override
    public RestBean deleteByUser(String userCode){
        if(ObjectUtils.isEmpty(userCode)){
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }
        SysUserRole updateSource = new SysUserRole();
        updateSource.setValid(DataValid.INVALID);

        int iCount = sysUserRoleMapper.update(updateSource,
                createWrapper(SysUserRoleBean.builder()
                        .userCode(userCode)
                        .build())
        );
        return RestBean.ok(iCount);
    }

    /**
     * 建立查询条件
     * 条件尽量都写在此方法
     * @param bean
     * @return
     */
    private LambdaQueryWrapper createWrapper(SysUserRoleBean bean){
        LambdaQueryWrapper<SysUserRole> wrapper = Wrappers.lambdaQuery();
        if(bean == null || bean.getValid() == null){
            wrapper.eq(SysUserRole::getValid,DataValid.VALID);
        }

        // 自定义条件
        if(bean != null) {
            if(bean.getValid() != null){
                wrapper.eq(SysUserRole::getValid,bean.getValid());
            }

            if(!ObjectUtils.isEmpty(bean.getCode())){
                wrapper.eq(SysUserRole::getCode, bean.getCode());
            }

            if(!ObjectUtils.isEmpty(bean.getUserCode())){
                wrapper.eq(SysUserRole::getUserCode, bean.getUserCode());
            }

            if(!ObjectUtils.isEmpty(bean.getRoleCode())){
                wrapper.eq(SysUserRole::getRoleCode, bean.getRoleCode());
            }

            if(CollectionUtils.isNotEmpty(bean.getUserCodeList())){
                wrapper.in(SysUserRole::getUserCode, bean.getUserCodeList());
            }

            if(CollectionUtils.isNotEmpty(bean.getRoleCodeList())){
                wrapper.in(SysUserRole::getRoleCode, bean.getRoleCodeList());
            }

        }

        return wrapper;
    }

}
