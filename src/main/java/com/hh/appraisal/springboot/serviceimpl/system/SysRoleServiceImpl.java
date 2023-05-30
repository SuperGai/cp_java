package com.hh.appraisal.springboot.serviceimpl.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hh.appraisal.springboot.bean.system.SysRoleBean;
import com.hh.appraisal.springboot.bean.system.SysUserBean;
import com.hh.appraisal.springboot.bean.system.SysUserRoleBean;
import com.hh.appraisal.springboot.core.baen.PageBean;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.core.constant.DataValid;
import com.hh.appraisal.springboot.core.constant.RestCode;
import com.hh.appraisal.springboot.dao.system.SysRoleMapper;
import com.hh.appraisal.springboot.entity.system.SysRole;
import com.hh.appraisal.springboot.service.system.SysRoleService;
import com.hh.appraisal.springboot.service.system.SysUserRoleService;

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
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final SysRoleMapper roleMapper;
    private final SysUserRoleService userRoleService;

    public SysRoleServiceImpl(SysRoleMapper roleMapper, SysUserRoleService userRoleService) {
        this.roleMapper = roleMapper;
        this.userRoleService = userRoleService;
    }

    @Override
    public List<SysRoleBean> findByUser(SysUserBean bean) {
        List<SysUserRoleBean> sysUserRoleBeanList = userRoleService.findAll(SysUserRoleBean.builder()
                .userCode(bean.getCode()).valid(DataValid.VALID)
                .build());

        List<String> roleCodeList = new ArrayList<>();
        sysUserRoleBeanList.forEach(v -> roleCodeList.add(v.getRoleCode()));

        List<SysRoleBean> roleList = this.findByCodeList(roleCodeList);
        return roleList;
    }

    @Override
    public List<SysRoleBean> findByCodeList(List<String> roleCodeList) {
        if(CollectionUtils.isEmpty(roleCodeList)) {
            return Collections.EMPTY_LIST;
        }

        LambdaQueryWrapper<SysRole> wrapper = createWrapper(null);
        wrapper.in(SysRole::getCode, roleCodeList);
        List<SysRole> roles = roleMapper.selectList(wrapper);

        List<SysRoleBean> roleBeans = new ArrayList<>();
        roles.forEach(v->{
            SysRoleBean item = new SysRoleBean();
            BeanUtils.copyProperties(v,item);
            roleBeans.add(item);
        });
        return roleBeans;
    }

    @Override
    public List<SysRoleBean> findList(SysRoleBean bean){
        List<SysRole> list = roleMapper.selectList(createWrapper(bean));
        if(CollectionUtils.isEmpty(list)) {
            return Collections.EMPTY_LIST;
        }

        List<SysRoleBean> beanList = list.stream().map(item -> {
            SysRoleBean srcBean = new SysRoleBean();
            BeanUtils.copyProperties(item, srcBean);
            return srcBean;
        }).collect(Collectors.toList());
        return beanList;
    }

    @Override
    public Page<SysRoleBean> findPage(SysRoleBean bean, PageBean pageBean){
        LambdaQueryWrapper<SysRole> wrapper = createWrapper(bean);

        // 分页查询
        Page<SysRole> page = new Page<>(pageBean.getCurrent(),pageBean.getSize());
        page = roleMapper.selectPage(page,wrapper);

        if(CollectionUtils.isEmpty(page.getRecords())) {
            return new Page<>();
        }

        Page<SysRoleBean> restPage = new Page<>();
        BeanUtils.copyProperties(page,restPage,"records");
        restPage.setRecords(new ArrayList<>());
        page.getRecords().forEach(v->{
            SysRoleBean itemBean = new SysRoleBean();
            BeanUtils.copyProperties(v, itemBean);
            restPage.getRecords().add(itemBean);
        });
        return restPage;
    }

    @Override
    public Integer findCount(SysRoleBean bean){
        return roleMapper.selectCount(createWrapper(bean));
    }

    @Transactional
    @Override
    public SysRoleBean add(SysRoleBean bean){
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(bean,sysRole);

        roleMapper.insert(sysRole);
        BeanUtils.copyProperties(sysRole,bean);
        return bean;
    }

    @Transactional
    @Override
    public RestBean updateByCode(SysRoleBean bean) {
        SysRole source = new SysRole();
        BeanUtils.copyProperties(bean,source);
        return RestBean.ok(roleMapper.updateById(source));
    }

    @Transactional
    @Override
    public RestBean deleteByCode(String code){
        if(ObjectUtils.isEmpty(code)){
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }

        // 待删除记录
        SysRole oldRole = roleMapper.selectById(code);
        if(oldRole == null) {
            return RestBean.ok();
        }

        // 检查角色名下是否有未删除的用户
        Integer iCount = userRoleService.count(SysUserRoleBean.builder()
                .roleCode(code)
                .build());
        if(iCount > 0){
            return RestBean.error("该角色名下存在用户，不可删除");
        }

        // 软删除
        SysRole updateSource = new SysRole();
        updateSource.setCode(code);
        updateSource.setValid(DataValid.INVALID);
        return RestBean.ok(
                roleMapper.updateById(updateSource)
        );
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
    public SysRoleBean findByCode(String code) {
        SysRole source = roleMapper.selectById(code);
        if(source == null) {
            return null;
        }

        SysRoleBean restBean = new SysRoleBean();
        BeanUtils.copyProperties(source, restBean);
        return restBean;
    }

    /**
     * 建立查询条件
     * 条件尽量都写在此方法
     * @param bean
     * @return
     */
    private LambdaQueryWrapper createWrapper(SysRoleBean bean){
        LambdaQueryWrapper<SysRole> wrapper = Wrappers.lambdaQuery();
        if(bean == null || bean.getValid() == null){
            wrapper.eq(SysRole::getValid,DataValid.VALID);
        }

        // 自定义条件
        if(bean != null) {
            if(bean.getValid() != null){
                wrapper.eq(SysRole::getValid,bean.getValid());
            }
            if(!ObjectUtils.isEmpty(bean.getCode())){
                wrapper.eq(SysRole::getCode, bean.getCode());
            }

            if(StringUtils.isNotBlank(bean.getName())){
                wrapper.like(SysRole::getName,bean.getName());
            }
            if(StringUtils.isNotBlank(bean.getNotes())){
                wrapper.like(SysRole::getNotes,bean.getNotes());
            }
        }

        return wrapper;
    }
}
