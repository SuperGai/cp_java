package com.hh.appraisal.springboot.serviceimpl.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hh.appraisal.springboot.bean.system.*;
import com.hh.appraisal.springboot.core.baen.PageBean;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.core.constant.DataValid;
import com.hh.appraisal.springboot.core.constant.RestCode;
import com.hh.appraisal.springboot.core.utils.StrUtils;
import com.hh.appraisal.springboot.dao.system.SysUserMapper;
import com.hh.appraisal.springboot.entity.system.SysUser;
import com.hh.appraisal.springboot.service.system.*;

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
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final SysUserMapper sysUserMapper;
    private final SysRoleService roleService;
    private final SysRoleFunctionService roleFunctionService;
    private final SysFunctionApiService functionApiService;
    private final SysRoleMenuService roleMenuService;
    private final SysMenuService menuService;
    private final SysUserRoleService userRoleService;

    public SysUserServiceImpl(SysUserMapper sysUserMapper, SysRoleService roleService, SysRoleFunctionService roleFunctionService, SysFunctionApiService functionApiService, SysRoleMenuService roleMenuService, SysMenuService menuService, SysUserRoleService userRoleService) {
        this.sysUserMapper = sysUserMapper;
        this.roleService = roleService;
        this.roleFunctionService = roleFunctionService;
        this.functionApiService = functionApiService;
        this.roleMenuService = roleMenuService;
        this.menuService = menuService;
        this.userRoleService = userRoleService;
    }

    @Override
    public SysUserBean findByCode(String code){
        if(com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isEmpty(code)){
            return null;
        }

        List<SysUser> sourceList = sysUserMapper.selectList(createWrapper(SysUserBean.builder()
                .code(code)
                .build()));
        if(com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isEmpty(sourceList)){
            return null;
        }

        SysUserBean restBean = SysUserBean.builder().build();
        BeanUtils.copyProperties(sourceList.get(0), restBean);
        return restBean;
    }

    @Override
    public List<SysMenuBean> findMenuTreeByUser(SysUserBean bean) {
        SysRoleMenuBean queryRoleMenuBean = SysRoleMenuBean.builder().valid(DataValid.VALID).build();
        if(bean != null){   // 用户所属角色列表
            SysUserBean user = this.findOne(bean);
            List<SysRoleBean> roleList = roleService.findByUser(user);
            if(CollectionUtils.isEmpty(roleList)) {
                return Collections.EMPTY_LIST;
            }
            List<String> roleCodeList = roleList.stream().map(v->v.getCode()).collect(Collectors.toList());
            queryRoleMenuBean.setRoleCodeList(roleCodeList);
        }

        // 角色所属菜单id集合
        List<SysRoleMenuBean> roleMenuBeanList = roleMenuService.findList(queryRoleMenuBean);
        List<String> menuCodeList = roleMenuBeanList.stream().filter(v -> v.getMenuCode() != null)
                .map(v -> v.getMenuCode()).collect(Collectors.toList());

        // 构建菜单树
        return menuService.tree(SysMenuBean.builder()
                .menuCodeList(menuCodeList).valid(DataValid.VALID)
                .build());
    }

    @Override
    public List<SysApiBean> findPermitUrlsByUser(SysUserBean bean) {
        SysUserBean user = this.findOne(bean);

        // 用户所属角色列表
        List<SysRoleBean> roleList = roleService.findByUser(user);
        if(CollectionUtils.isEmpty(roleList)) {
            return Collections.EMPTY_LIST;
        }
        List<String> roleCodeList = roleList.stream().map(v->v.getCode()).collect(Collectors.toList());

        // 根据角色列表查询名下所有功能列表
        List<SysFunctionBean> functionList = roleFunctionService.findFunctionListByRoleFunction(SysRoleFunctionBean.builder()
                .roleCodeList(roleCodeList).valid(DataValid.VALID)
                .build());
        if(CollectionUtils.isEmpty(functionList)) {
            return Collections.EMPTY_LIST;
        }
        List<String> functionIdList = functionList.stream().map(v->v.getCode()).collect(Collectors.toList());

        // 根据功能列表查询名下所有api接口列表
        return functionApiService.findApiListByFunctionApi(SysFunctionApiBean.builder()
                .functionCodeList(functionIdList).valid(DataValid.VALID)
                .build());
    }

    @Override
    public RestBean loginUpdateInfo(SysUserBean userBean) {
        if(userBean == null || ObjectUtils.isEmpty(userBean.getCode())) {
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }

        SysUser oldUser = sysUserMapper.selectById(userBean.getCode());
        if(oldUser == null) {
            return RestBean.error("找不到用户记录");
        }

        // 检查修改密码
        if(StringUtils.isNotBlank(userBean.getNewPassword()) &&
                StringUtils.isNotBlank(userBean.getPassword())){
            if(StrUtils.md5(userBean.getPassword()).equals(oldUser.getPassword())){
                oldUser.setPassword(StrUtils.md5(userBean.getNewPassword()));
            }else{
                return RestBean.error("密码错误");
            }
        }

        // 检查其他字段
        if(StringUtils.isNotBlank(userBean.getName())){
            oldUser.setName(userBean.getName());
        }
        if(StringUtils.isNotBlank(userBean.getPhone())){
            oldUser.setPhone(userBean.getPhone());
        }

        sysUserMapper.updateById(oldUser);

//        BeanUtils.copyProperties(oldUser,userBean);
//        userBean.setPassword(null);
        return RestBean.ok();
    }

    @Override
    public RestBean passwordReset(SysUserBean bean){
        SysUserBean oldUser = this.findByCode(bean.getCode());
        if(oldUser == null){
            return RestBean.error("找不到用户记录");
        }

        bean.setPassword(StrUtils.md5(bean.getNewPassword()));

        return this.updateByCode(bean);
    }

    @Override
    public SysUserBean findOne(SysUserBean searchBean) {
        SysUser bean = sysUserMapper.selectOne(createWrapper(searchBean));
        if(bean == null) {
            return null;
        }
        SysUserBean userBean = new SysUserBean();
        BeanUtils.copyProperties(bean,userBean);
        return userBean;
    }

    @Transactional
    @Override
    public RestBean add(SysUserBean bean){
        if(bean == null) {
            return null;
        }

        // 检查账号是否已存在
        int hasAccountCount = sysUserMapper.selectCount(createWrapper(SysUserBean.builder()
                .account(bean.getAccount())
                .build()));
        if(hasAccountCount > 0) {
            return RestBean.error("账号已存在");
        }

        // 密码加密
        bean.setPassword(StrUtils.md5(bean.getPassword()));

        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(bean, sysUser);

        sysUserMapper.insert(sysUser);
        if(sysUser.getCode() == null) {
            return null;
        }

        // 新增角色信息
        if(!ObjectUtils.isEmpty(bean.getRoleCodeList())){
            bean.getRoleCodeList().forEach(v -> {
                userRoleService.add(SysUserRoleBean.builder()
                        .userCode(sysUser.getCode())
                        .roleCode(v)
                        .build());
            });
        }

        BeanUtils.copyProperties(sysUser,bean);
        return RestBean.ok(bean);
    }

    @Override
    public RestBean findList(SysUserBean bean) {
        LambdaQueryWrapper<SysUser> wrapper = createWrapper(bean);

        // 数据处理
        List<SysUser> list = sysUserMapper.selectList(wrapper);
        if(CollectionUtils.isEmpty(list)) {
            return RestBean.ok(new ArrayList<>());
        }
        List<SysUserBean> restList = new ArrayList<>();
        list.forEach(v->{
            SysUserBean item = new SysUserBean();
            BeanUtils.copyProperties(v,item);
            restList.add(item);
        });
        return RestBean.ok(restList);
    }

    @Override
    public Page<SysUserBean> findPage(SysUserBean bean, PageBean pageBean){
        LambdaQueryWrapper<SysUser> wrapper = createWrapper(bean);

        // 分页查询
        Page<SysUser> page = new Page<>(pageBean.getCurrent(),pageBean.getSize());
        page = sysUserMapper.selectPage(page,wrapper);

        if(CollectionUtils.isEmpty(page.getRecords())) {
            return new Page<>();
        }

        Page<SysUserBean> restPage = new Page<>();
        BeanUtils.copyProperties(page,restPage,"records");
        restPage.setRecords(new ArrayList<>());
        page.getRecords().forEach(v->{
            SysUserBean itemBean = new SysUserBean();
            BeanUtils.copyProperties(v, itemBean);
            restPage.getRecords().add(itemBean);
        });
        return restPage;
    }

    @Transactional
    @Override
    public RestBean updateByCode(SysUserBean bean) {
        SysUser user = sysUserMapper.selectById(bean.getCode());
        if(user == null) {
            return null;
        }

        BeanUtils.copyProperties(bean,user);
        int iCount = sysUserMapper.updateById(user);
        if(iCount <= 0) {
            return null;
        }
        BeanUtils.copyProperties(user,bean);
        return RestBean.ok(bean);
    }

    @Transactional
    @Override
    public RestBean deleteByCode(String code) {
        if(ObjectUtils.isEmpty(code)){
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }

        SysUser updateSource = new SysUser();
        updateSource.setCode(code);
        updateSource.setValid(DataValid.INVALID);
        int iCount = sysUserMapper.updateById(updateSource);

        // 删除用户角色对应记录
        userRoleService.deleteByUser(code);

        return RestBean.ok(iCount);
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

    /**
     * 建立查询条件
     * 条件尽量都写在此方法
     * @param bean
     * @return
     */
    private LambdaQueryWrapper createWrapper(SysUserBean bean){
        LambdaQueryWrapper<SysUser> wrapper = Wrappers.lambdaQuery();
        if(bean == null || bean.getValid() == null){
            wrapper.eq(SysUser::getValid,DataValid.VALID);
        }

        // 自定义条件
        if(bean != null) {
            if(bean.getValid() != null){
                wrapper.eq(SysUser::getValid,bean.getValid());
            }

            if(!ObjectUtils.isEmpty(bean.getCode())){
                wrapper.eq(SysUser::getCode, bean.getCode());
            }

            if(!ObjectUtils.isEmpty(bean.getName())){
                wrapper.like(SysUser::getName, bean.getName());
            }

            if(StringUtils.isNotBlank(bean.getAccount())){
                wrapper.eq(SysUser::getAccount,bean.getAccount());
            }
            if(StringUtils.isNotBlank(bean.getPassword())){
                wrapper.eq(SysUser::getPassword,bean.getPassword());
            }
            if(StringUtils.isNotBlank(bean.getStatus())){
                wrapper.eq(SysUser::getStatus,bean.getStatus());
            }
            if(StringUtils.isNotBlank(bean.getApproveStatus())){
                wrapper.eq(SysUser::getApproveStatus,bean.getApproveStatus());
            }
            if(StringUtils.isNotBlank(bean.getApproveResult())){
                wrapper.eq(SysUser::getApproveResult,bean.getApproveResult());
            }
            if(CollectionUtils.isNotEmpty(bean.getUserCodeList())){
                wrapper.in(SysUser::getCode, bean.getUserCodeList());
            }
        }

        return wrapper;
    }
}
