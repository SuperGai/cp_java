package com.hh.appraisal.springboot.serviceimpl.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hh.appraisal.springboot.bean.system.SysMenuBean;
import com.hh.appraisal.springboot.core.baen.PageBean;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.core.constant.DataValid;
import com.hh.appraisal.springboot.core.constant.RestCode;
import com.hh.appraisal.springboot.dao.system.SysMenuMapper;
import com.hh.appraisal.springboot.entity.system.SysMenu;
import com.hh.appraisal.springboot.service.system.SysMenuService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

/**
 * Service 实现
 * @author gaigai
 * @date 2019/05/15
 */
@Slf4j
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    private final SysMenuMapper menuMapper;

    public SysMenuServiceImpl(SysMenuMapper menuMapper) {
        this.menuMapper = menuMapper;
    }

    @Override
    public List<SysMenuBean> tree(SysMenuBean bean) {
        if(bean == null || CollectionUtils.isEmpty(bean.getMenuCodeList())){
            return Collections.EMPTY_LIST;
        }
        List<SysMenuBean> allMenuList = findList(bean);
        return this.createTree(allMenuList);
    }

    @Override
    public LinkedList<SysMenuBean> createTree(List<SysMenuBean> list){
        if(CollectionUtils.isEmpty(list)) {
            return new LinkedList<>();
        }

        HashMap<String, SysMenuBean> parentMap = new HashMap<>();// key: 菜单唯一标识 code
        HashMap<String, LinkedList<SysMenuBean>> childrenMap = new HashMap<>();// key: 父菜单唯一标识 code
        for (SysMenuBean item : list) {
            if(ObjectUtils.isEmpty(item.getParentMenuCode())){
                parentMap.put(item.getCode(), item);
            }else{
                if(childrenMap.get(item.getParentMenuCode()) == null){
                    childrenMap.put(item.getParentMenuCode(), new LinkedList<SysMenuBean>(){{
                        add(item);
                    }});
                }else {
                    childrenMap.get(item.getParentMenuCode()).add(item);
                }
            }
        }

        // 将子菜单设置进父菜单实例
        childrenMap.forEach((parentCode, children) -> {
            if(parentMap.get(parentCode) == null){
                SysMenuBean parentMenuBean = findByCode(parentCode);
                parentMenuBean.setChildren(children);// 设置子菜单集合
                parentMap.put(parentCode, parentMenuBean);
            }else{
                parentMap.get(parentCode).setChildren(children);// 设置子菜单集合
            }
        });

        // 排序
        LinkedList<SysMenuBean> restList = new LinkedList<>(parentMap.values());
        Collections.sort(restList);
        restList.forEach(parent -> {
            if(CollectionUtils.isNotEmpty(parent.getChildren())){
                Collections.sort(parent.getChildren());
            }
        });

        return restList;
    }

    @Override
    public List<SysMenuBean> findList(SysMenuBean bean) {
        List<SysMenu> menus = menuMapper.selectList(createWrapper(bean));
        if(CollectionUtils.isEmpty(menus)) {
            return Collections.EMPTY_LIST;
        }

        List<SysMenuBean> beans = new ArrayList<>();
        menus.forEach(v->{
            SysMenuBean sysMenuBean = new SysMenuBean();
            BeanUtils.copyProperties(v,sysMenuBean);
            beans.add(sysMenuBean);
        });
        return beans;
    }

    @Override
    public Page<SysMenuBean> findPage(SysMenuBean bean, PageBean pageBean){
        LambdaQueryWrapper<SysMenu> wrapper = createWrapper(bean);

        // 分页查询
        Page<SysMenu> page = new Page<>(pageBean.getCurrent(),pageBean.getSize());
        page = menuMapper.selectPage(page,wrapper);

        if(CollectionUtils.isEmpty(page.getRecords())) {
            return new Page<>();
        }

        Page<SysMenuBean> beanPage = new Page<>();
        BeanUtils.copyProperties(page,beanPage,"records");
        beanPage.setRecords(new ArrayList<>());
        page.getRecords().forEach(v -> {
            SysMenuBean menuBean = new SysMenuBean();
            BeanUtils.copyProperties(v,menuBean);
            beanPage.getRecords().add(menuBean);
        });
        return beanPage;
    }

    @Transactional
    @Override
    public SysMenuBean add(SysMenuBean bean){
        SysMenu source = new SysMenu();
        BeanUtils.copyProperties(bean,source);

        if(ObjectUtils.isEmpty(source.getParentMenuCode())){
            source.setParentMenuCode(null);
        }

        menuMapper.insert(source);
        BeanUtils.copyProperties(source,bean);
        return bean;
    }

    @Transactional
    @Override
    public RestBean updateByCode(SysMenuBean bean) {
        SysMenu source = new SysMenu();
        BeanUtils.copyProperties(bean,source);

        if(ObjectUtils.isEmpty(source.getParentMenuCode())){
            source.setParentMenuCode(null);
        }

        return RestBean.ok(menuMapper.updateById(source));
    }

    @Transactional
    @Override
    public RestBean deleteByCode(String code) {
        if(org.springframework.util.ObjectUtils.isEmpty(code)){
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }

        SysMenu updateSource = new SysMenu();
        updateSource.setCode(code);
        updateSource.setValid(DataValid.INVALID);
        return RestBean.ok(
                menuMapper.updateById(updateSource)
        );
    }

    @Transactional
    @Override
    public void deleteByCode(List<String> codeList) {
        if(org.springframework.util.ObjectUtils.isEmpty(codeList)){
            return;
        }

        codeList.forEach(v -> {
            deleteByCode(v);
        });
    }

    @Override
    public SysMenuBean findByCode(String code) {
        SysMenu source = menuMapper.selectById(code);
        if(source == null) {
            return null;
        }

        SysMenuBean restBean = new SysMenuBean();
        BeanUtils.copyProperties(source,restBean);
        return restBean;
    }

    /**
     * 建立查询条件
     * 条件尽量都写在此方法
     * @param bean
     * @return
     */
    private LambdaQueryWrapper createWrapper(SysMenuBean bean){
        LambdaQueryWrapper<SysMenu> wrapper = Wrappers.lambdaQuery();
        if(bean == null || bean.getValid() == null){
            wrapper.eq(SysMenu::getValid,DataValid.VALID);
        }

        // 自定义条件
        if(bean != null) {
            if(!ObjectUtils.isEmpty(bean.getCode())){
                wrapper.eq(SysMenu::getCode, bean.getCode());
            }
            if(bean.getValid() != null){
                wrapper.eq(SysMenu::getValid,bean.getValid());
            }

            if(CollectionUtils.isNotEmpty(bean.getMenuCodeList())){
                wrapper.in(SysMenu::getCode, bean.getMenuCodeList());
            }
            if(!ObjectUtils.isEmpty(bean.getParentMenuCode())){
                wrapper.eq(SysMenu::getParentMenuCode, bean.getParentMenuCode());
            }
            if(StringUtils.isNotBlank(bean.getName())){
                wrapper.like(SysMenu::getName,bean.getName());
            }
            if(CollectionUtils.isNotEmpty(bean.getMenuCodeList())){
                wrapper.in(SysMenu::getCode, bean.getMenuCodeList());
            }
            if(bean.getNeedParentMenu() != null){
                if(bean.getNeedParentMenu()){
                    wrapper.isNull(SysMenu::getParentMenuCode);
                }else{
                    wrapper.isNotNull(SysMenu::getParentMenuCode);
                }
            }
        }

        return wrapper;
    }
}
