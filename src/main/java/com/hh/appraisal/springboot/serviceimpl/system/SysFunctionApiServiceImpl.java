package com.hh.appraisal.springboot.serviceimpl.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hh.appraisal.springboot.bean.system.SysApiBean;
import com.hh.appraisal.springboot.bean.system.SysDictBean;
import com.hh.appraisal.springboot.bean.system.SysDictTypeBean;
import com.hh.appraisal.springboot.bean.system.SysFunctionApiBean;
import com.hh.appraisal.springboot.constant.DictTypeNumConstant;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.core.constant.DataValid;
import com.hh.appraisal.springboot.dao.system.SysFunctionApiMapper;
import com.hh.appraisal.springboot.entity.system.SysFunctionApi;
import com.hh.appraisal.springboot.service.system.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service 实现
 * @author gaigai
 * @date 2019/05/15
 */
@Slf4j
@Service
public class SysFunctionApiServiceImpl extends ServiceImpl<SysFunctionApiMapper, SysFunctionApi> implements SysFunctionApiService {

    private final SysFunctionApiMapper functionApiMapper;
    private final SysApiService apiService;
    private final SysDictService dictService;
    private final SysFunctionService functionService;
    private final SysDictTypeService dictTypeService;

    public SysFunctionApiServiceImpl(SysFunctionApiMapper functionApiMapper, SysApiService apiService, SysDictService dictService, SysFunctionService functionService, SysDictTypeService dictTypeService){
        this.functionApiMapper = functionApiMapper;
        this.apiService = apiService;
        this.dictService = dictService;
        this.functionService = functionService;
        this.dictTypeService = dictTypeService;
    }

    @Override
    public SysFunctionApiBean add(SysFunctionApiBean bean) {
        SysFunctionApi source = new SysFunctionApi();
        BeanUtils.copyProperties(bean, source);
        functionApiMapper.insert(source);
        BeanUtils.copyProperties(source, bean);
        return bean;
    }

    @Transactional
    @Override
    public RestBean treeUpdate(List<String> apiCodeList, String functionCode) {
        // 删除原来的
        SysFunctionApi delFunApi = new SysFunctionApi();
        delFunApi.setValid(DataValid.INVALID);
        functionApiMapper.update(delFunApi, createWrapper(SysFunctionApiBean.builder().functionCode(functionCode).build()));

        // 插入新的
        if(CollectionUtils.isEmpty(apiCodeList)) {
            return RestBean.ok();
        }
        apiCodeList.forEach(apiCode -> {
            SysFunctionApi functionApi = new SysFunctionApi();
            functionApi.setApiCode(apiCode);
            functionApi.setFunctionCode(functionCode);
            functionApiMapper.insert(functionApi);
        });

        return RestBean.ok();
    }

    @Override
    public HashMap treeFunctionApi(String functionCode) {
        List<SysFunctionApiBean> allfunApiList = findList(null);

        HashMap<String, HashMap<String, Object>> allList = new HashMap<>();// key: roleId, value
        List<Object> checkedNameList = new ArrayList<>();//被选中的function id
        for (SysFunctionApiBean funApi : allfunApiList) {
            SysApiBean api = apiService.findByCode(funApi.getApiCode());
            if(api == null) {
                continue;
            }

            // 设置被选中的
            if(funApi.getFunctionCode().equals(functionCode)){
                checkedNameList.add(api.getCode());
            }
        }


        SysDictTypeBean systemModuleDictTypeBean = dictTypeService.findOne(SysDictTypeBean.builder()
                .num(DictTypeNumConstant.SYSTEM_MODULE)
                .build());
        if(systemModuleDictTypeBean == null){
            return null;
        }

        // 设置全部
        List<SysApiBean> allApiBeanList = apiService.findList(null);
        for (SysApiBean api : allApiBeanList) {
            SysDictBean dictBean = dictService.findOne(SysDictBean.builder()
                    .value(api.getModule())
                    .typeCode(systemModuleDictTypeBean.getCode())
                    .build()
            );
            if(dictBean == null) {
                continue;
            }
            if(CollectionUtils.isEmpty(allList.get("module_" + api.getModule()))){
                allList.put("module_" + api.getModule(), new HashMap<String, Object>(){{
                    put("code", "" + "module_" + dictBean.getCode());
                    put("name", dictBean.getName());
                    put("children", new ArrayList<SysApiBean>(){{
                        add(api);
                    }});
                }});
            }else{
                List tempList = ((List)(allList.get("module_" + api.getModule()).get("children")));
                if(!tempList.contains(api)) {
                    tempList.add(api);
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
    public List<SysApiBean> findApiListByFunctionApi(SysFunctionApiBean bean){
        LambdaQueryWrapper<SysFunctionApi> wrapper = createWrapper(bean);
        List<SysFunctionApi> funcApiList = functionApiMapper.selectList(wrapper);

        if(CollectionUtils.isEmpty(funcApiList)) {
            return Collections.EMPTY_LIST;
        }
        List<String> apiCodeList = funcApiList.stream().filter(v -> v.getApiCode() != null)
                .map(v->v.getApiCode()).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(apiCodeList)) {
            return Collections.EMPTY_LIST;
        }
        List<SysApiBean> apiList = apiService.findList(SysApiBean.builder()
                .apiCodeList(apiCodeList).valid(DataValid.VALID)
                .build());
        return apiList;
    }

    @Override
    public List<SysFunctionApiBean> findList(SysFunctionApiBean bean){
        List<SysFunctionApi> list = functionApiMapper.selectList(createWrapper(bean));
        if(CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        List<SysFunctionApiBean> beanList = list.stream().map(item -> {
            SysFunctionApiBean restBean = new SysFunctionApiBean();
            BeanUtils.copyProperties(item, restBean);
            return restBean;
        }).collect(Collectors.toList());
        return beanList;
    }

    @Transactional
    @Override
    public RestBean delete(SysFunctionApiBean bean) {
        LambdaQueryWrapper<SysFunctionApi> wrapper = createWrapper(bean);

        SysFunctionApi updateEntity = new SysFunctionApi();
        updateEntity.setValid(DataValid.INVALID);

        return RestBean.ok(
                functionApiMapper.update(updateEntity, wrapper)
        );
    }

    /**
     * 建立查询条件
     * 条件尽量都写在此方法
     * @param bean
     * @return
     */
    private LambdaQueryWrapper createWrapper(SysFunctionApiBean bean){
        LambdaQueryWrapper<SysFunctionApi> wrapper = Wrappers.lambdaQuery();
        if(bean == null || bean.getValid() == null){
            wrapper.eq(SysFunctionApi::getValid,DataValid.VALID);
        }

        // 自定义条件
        if(bean != null) {
            if(bean.getValid() != null){
                wrapper.eq(SysFunctionApi::getValid,bean.getValid());
            }
            if(!ObjectUtils.isEmpty(bean.getCode())){
                wrapper.eq(SysFunctionApi::getCode, bean.getCode());
            }

            if(!ObjectUtils.isEmpty(bean.getFunctionCode())){
                wrapper.eq(SysFunctionApi::getFunctionCode, bean.getFunctionCode());
            }
            if(!ObjectUtils.isEmpty(bean.getApiCode())){
                wrapper.eq(SysFunctionApi::getApiCode, bean.getApiCode());
            }

            if(CollectionUtils.isNotEmpty(bean.getFunctionCodeList())){
                wrapper.in(SysFunctionApi::getFunctionCode, bean.getFunctionCodeList());
            }
            if(CollectionUtils.isNotEmpty(bean.getApiCodeList())){
                wrapper.in(SysFunctionApi::getApiCode, bean.getApiCodeList());
            }
        }

        return wrapper;
    }
}
