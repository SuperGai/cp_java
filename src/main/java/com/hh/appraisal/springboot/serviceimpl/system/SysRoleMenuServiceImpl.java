package com.hh.appraisal.springboot.serviceimpl.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hh.appraisal.springboot.bean.system.SysMenuBean;
import com.hh.appraisal.springboot.bean.system.SysRoleMenuBean;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.core.constant.DataValid;
import com.hh.appraisal.springboot.dao.system.SysRoleMenuMapper;
import com.hh.appraisal.springboot.entity.system.SysRoleMenu;
import com.hh.appraisal.springboot.service.system.SysMenuService;
import com.hh.appraisal.springboot.service.system.SysRoleMenuService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service 实现
 * @author gaigai
 * @date 2019/05/15
 */
@Slf4j
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {

    private final SysRoleMenuMapper roleMenuMapper;
    private final SysMenuService menuService;

    public SysRoleMenuServiceImpl(SysRoleMenuMapper roleMenuMapper, SysMenuService menuService) {
        this.roleMenuMapper = roleMenuMapper;
        this.menuService = menuService;
    }

    @Transactional
    @Override
    public RestBean roleMenuTreeUpdate(List<String> menuCodeList, String roleCode) {
        // 删除原来的
        delete(SysRoleMenuBean.builder().roleCode(roleCode).build());
        if(CollectionUtils.isEmpty(menuCodeList)) {
            return RestBean.ok();
        }

        // 插入记录
        menuCodeList.forEach(menuCode -> {
            add(SysRoleMenuBean.builder()
                    .roleCode(roleCode)
                    .menuCode(menuCode)
                    .build()
            );
        });

        return RestBean.ok();
    }

    @Override
    public HashMap findMenuTreeByRole(String roleCode) {
        // 获取全部菜单
        List<SysMenuBean> allMenuList = menuService.findList(null);
        if(CollectionUtils.isEmpty(allMenuList)) {
            return null;
        }

        // 构造全部菜单树
        LinkedList<SysMenuBean> allRest = menuService.createTree(allMenuList);

        // 获取指定roleid的菜单树
        List<String> checkedMenuIdList = new ArrayList<>();
        if(!ObjectUtils.isEmpty(roleCode)){
            List<SysRoleMenuBean> roleMenuList = findList(SysRoleMenuBean.builder().roleCode(roleCode).build());
            checkedMenuIdList = roleMenuList.stream().map(v->{
                return v.getMenuCode();
            }).collect(Collectors.toList());
        }

        HashMap<String, Object> rest = new HashMap<>();
        rest.put("treeAllData", allRest);
        rest.put("treeData", checkedMenuIdList);
        return rest;
    }

    @Override
    public List<SysRoleMenuBean> findList(SysRoleMenuBean bean) {
        LambdaQueryWrapper<SysRoleMenu> wrapper = createWrapper(bean);
        List<SysRoleMenu> roleMenuList = roleMenuMapper.selectList(wrapper);
        if(CollectionUtils.isEmpty(roleMenuList)) {
            return Collections.EMPTY_LIST;
        }
        List<SysRoleMenuBean> roleMenuBeanList = roleMenuList.stream().map(item -> {
            SysRoleMenuBean srcBean = new SysRoleMenuBean();
            BeanUtils.copyProperties(item,srcBean);
            return srcBean;
        }).collect(Collectors.toList());
        return roleMenuBeanList;
    }

    @Transactional
    @Override
    public SysRoleMenuBean add(SysRoleMenuBean bean){
        SysRoleMenu source = new SysRoleMenu();
        BeanUtils.copyProperties(bean,source);

        roleMenuMapper.insert(source);
        BeanUtils.copyProperties(source,bean);
        return bean;
    }

    @Transactional
    @Override
    public RestBean delete(SysRoleMenuBean bean) {
        LambdaQueryWrapper<SysRoleMenu> wrapper = createWrapper(bean);

        SysRoleMenu updateEntity = new SysRoleMenu();
        updateEntity.setValid(DataValid.INVALID);

        return RestBean.ok(
                roleMenuMapper.update(updateEntity, wrapper)
        );
    }

    /**
     * 建立查询条件
     * 条件尽量都写在此方法
     * @param bean
     * @return
     */
    private LambdaQueryWrapper createWrapper(SysRoleMenuBean bean){
        LambdaQueryWrapper<SysRoleMenu> wrapper = Wrappers.lambdaQuery();
        if(bean == null || bean.getValid() == null){
            wrapper.eq(SysRoleMenu::getValid,DataValid.VALID);
        }

        // 自定义条件
        if(bean != null) {
            if(bean.getValid() != null){
                wrapper.eq(SysRoleMenu::getValid,bean.getValid());
            }
            if(!ObjectUtils.isEmpty(bean.getCode())){
                wrapper.eq(SysRoleMenu::getCode, bean.getCode());
            }

            if(!ObjectUtils.isEmpty(bean.getRoleCode())){
                wrapper.eq(SysRoleMenu::getRoleCode, bean.getRoleCode());
            }
            if(CollectionUtils.isNotEmpty(bean.getMenuCodeList())){
                wrapper.in(SysRoleMenu::getCode, bean.getMenuCodeList());
            }
            if(!ObjectUtils.isEmpty(bean.getMenuCode())){
                wrapper.eq(SysRoleMenu::getMenuCode, bean.getMenuCode());
            }
            if(CollectionUtils.isNotEmpty(bean.getRoleCodeList())){
                wrapper.in(SysRoleMenu::getRoleCode, bean.getRoleCodeList());
            }
            if(CollectionUtils.isNotEmpty(bean.getMenuCodeList())){
                wrapper.in(SysRoleMenu::getMenuCode, bean.getMenuCodeList());
            }
        }

        return wrapper;
    }
}
