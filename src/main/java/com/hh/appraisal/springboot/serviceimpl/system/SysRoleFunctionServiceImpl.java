package com.hh.appraisal.springboot.serviceimpl.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hh.appraisal.springboot.bean.system.SysDictBean;
import com.hh.appraisal.springboot.bean.system.SysDictTypeBean;
import com.hh.appraisal.springboot.bean.system.SysFunctionBean;
import com.hh.appraisal.springboot.bean.system.SysRoleFunctionBean;
import com.hh.appraisal.springboot.constant.DictTypeNumConstant;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.core.constant.DataValid;
import com.hh.appraisal.springboot.dao.system.SysRoleFunctionMapper;
import com.hh.appraisal.springboot.entity.system.SysRoleFunction;
import com.hh.appraisal.springboot.service.system.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service 实现
 * @author gaigai
 * @date 2019/05/15
 */
@Slf4j
@Service
public class SysRoleFunctionServiceImpl extends ServiceImpl<SysRoleFunctionMapper, SysRoleFunction> implements SysRoleFunctionService {

    private final SysRoleFunctionMapper roleFunctionMapper;
    private final SysFunctionService functionService;
    private final SysRoleService roleService;
    private final SysDictService dictService;
    private final SysDictTypeService dictTypeService;

    public SysRoleFunctionServiceImpl(SysRoleFunctionMapper roleFunctionMapper, SysFunctionService functionService, SysRoleService roleService, SysDictService dictService, SysDictTypeService dictTypeService){
        this.roleFunctionMapper = roleFunctionMapper;
        this.functionService = functionService;
        this.roleService = roleService;
        this.dictService = dictService;
        this.dictTypeService = dictTypeService;
    }

    @Transactional
    @Override
    public RestBean treeUpdate(List<String> functionCodeList, String roleCode) {
        // 删除原来的
        SysRoleFunction delFunApi = new SysRoleFunction();
        delFunApi.setValid(DataValid.INVALID);
        roleFunctionMapper.update(delFunApi, createWrapper(
                SysRoleFunctionBean.builder().roleCode(roleCode).build())
        );

        // 插入新的
        if(CollectionUtils.isEmpty(functionCodeList)) {
            return RestBean.ok();
        }
        functionCodeList.forEach(funCode -> {
            SysRoleFunction roleFun = new SysRoleFunction();
            roleFun.setFunctionCode(funCode);
            roleFun.setRoleCode(roleCode);
            roleFunctionMapper.insert(roleFun);
        });

        return RestBean.ok();
    }

    @Override
    public HashMap roleFunctionTree(String roleCode){
        List<SysRoleFunctionBean> allRoleFunList = findList(null);

        HashMap<String, HashMap<String, Object>> allList = new HashMap<>();// key: roleId, value
        List<Object> checkedNameList = new ArrayList<>();//被选中的function id
        for (SysRoleFunctionBean roleFun : allRoleFunList) {
            SysFunctionBean fun = functionService.findOne(
                    SysFunctionBean.builder()
                            .code(roleFun.getFunctionCode())
                            .build()
            );
            if(fun == null) {
                continue;
            }

            // 设置被选中的
            if(roleFun.getRoleCode().equals(roleCode)){
                checkedNameList.add(fun.getCode());
            }
        }

        SysDictTypeBean systemModuleDictTypeBean = dictTypeService.findOne(SysDictTypeBean.builder()
                .num(DictTypeNumConstant.SYSTEM_MODULE)
                .build());
        if(systemModuleDictTypeBean == null){
            return null;
        }

        // 设置全部
        List<SysFunctionBean> allFunBeanList = functionService.findList(null);
        for (SysFunctionBean fun : allFunBeanList) {
            SysDictBean dictBean = dictService.findOne(SysDictBean.builder()
                    .value(fun.getModule())
                    .typeCode(systemModuleDictTypeBean.getCode())
                    .build()
            );
            if(dictBean == null) {
                continue;
            }

            if(CollectionUtils.isEmpty(allList.get("module_" + fun.getModule()))){
                allList.put("module_" + fun.getModule(), new HashMap<String, Object>(){{
                    put("code", "module_" + fun.getModule());
                    put("name", dictBean.getName());
                    put("children", new ArrayList<SysFunctionBean>(){{
                        add(fun);
                    }});
                }});
            }else{
                List tempList = ((List)(allList.get("module_" + fun.getModule()).get("children")));
                if(!tempList.contains(fun)) {
                    tempList.add(fun);
                }
            }
        }
        List<HashMap<String, Object>> allRest = new ArrayList<>();
        for(HashMap<String, Object> item : allList.values()){
            allRest.add(item);
        }

        return new HashMap<String, Object>(){{
            put("treeAllData", allRest);
            put("treeData", checkedNameList);
        }};
    }

    @Override
    public List<SysRoleFunctionBean> findList(SysRoleFunctionBean bean){
        List<SysRoleFunction> list = roleFunctionMapper.selectList(createWrapper(bean));
        if(CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        List<SysRoleFunctionBean> beanList = list.stream().map(item -> {
            SysRoleFunctionBean restBean = new SysRoleFunctionBean();
            BeanUtils.copyProperties(item, restBean);
            return restBean;
        }).collect(Collectors.toList());
        return beanList;
    }

    @Override
    public List<SysFunctionBean> findFunctionListByRoleFunction(SysRoleFunctionBean bean){
        LambdaQueryWrapper<SysRoleFunction> wrapper = createWrapper(bean);
        List<SysRoleFunction> roleFunctionList = roleFunctionMapper.selectList(wrapper);
        if(CollectionUtils.isEmpty(roleFunctionList)) {
            return Collections.EMPTY_LIST;
        }
        List<String> funtionCodeList = roleFunctionList.stream()
                .filter(v -> v.getFunctionCode() != null)
                .map(v -> v.getFunctionCode()).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(funtionCodeList)) {
            return Collections.EMPTY_LIST;
        }
        return functionService.findList(SysFunctionBean.builder()
                .functionCodeList(funtionCodeList)
                .valid(DataValid.VALID)
                .build());
    }

    @Transactional
    @Override
    public RestBean delete(SysRoleFunctionBean bean) {
        LambdaQueryWrapper<SysRoleFunction> wrapper = createWrapper(bean);

        SysRoleFunction updateSource = new SysRoleFunction();
        updateSource.setValid(DataValid.INVALID);

        return RestBean.ok(
                roleFunctionMapper.update(updateSource, wrapper)
        );
    }

    /**
     * 建立查询条件
     * 条件尽量都写在此方法
     * @param bean
     * @return
     */
    private LambdaQueryWrapper createWrapper(SysRoleFunctionBean bean){
        LambdaQueryWrapper<SysRoleFunction> wrapper = Wrappers.lambdaQuery();
        if(bean == null || bean.getValid() == null){
            wrapper.eq(SysRoleFunction::getValid,DataValid.VALID);
        }

        // 自定义条件
        if(bean != null) {
            if(bean.getValid() != null){
                wrapper.eq(SysRoleFunction::getValid,bean.getValid());
            }
            if(!ObjectUtils.isEmpty(bean.getCode())){
                wrapper.eq(SysRoleFunction::getCode, bean.getCode());
            }

            if(!ObjectUtils.isEmpty(bean.getFunctionCode())){
                wrapper.eq(SysRoleFunction::getFunctionCode, bean.getFunctionCode());
            }
            if(CollectionUtils.isNotEmpty(bean.getFunctionCodeList())){
                wrapper.in(SysRoleFunction::getFunctionCode, bean.getFunctionCodeList());
            }
            if(!ObjectUtils.isEmpty(bean.getRoleCode())){
                wrapper.eq(SysRoleFunction::getRoleCode, bean.getRoleCode());
            }
            if(CollectionUtils.isNotEmpty(bean.getRoleCodeList())){
                wrapper.in(SysRoleFunction::getRoleCode, bean.getRoleCodeList());
            }
        }

        return wrapper;
    }
}
