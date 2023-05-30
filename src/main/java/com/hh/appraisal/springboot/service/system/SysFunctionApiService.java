package com.hh.appraisal.springboot.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hh.appraisal.springboot.bean.system.SysApiBean;
import com.hh.appraisal.springboot.bean.system.SysFunctionApiBean;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.entity.system.SysFunction;
import com.hh.appraisal.springboot.entity.system.SysFunctionApi;

import java.util.HashMap;
import java.util.List;

/**
 * 功能与接口对应 Service接口
 * @author gaigai
 * @date 2019/05/15
 */
public interface SysFunctionApiService extends IService<SysFunctionApi> {

    /**
     * 添加一条记录
     * @param bean
     * @return
     */
    SysFunctionApiBean add(SysFunctionApiBean bean);

    /**
     * 更新功能接口树
     * @param apiCodeList api code 集合
     * @param functionCode 功能 code
     * @return
     */
    RestBean treeUpdate(List<String> apiCodeList, String functionCode);

    /**
     * 获取功能接口树
     * @param functionCode
     * @return
     */
    HashMap treeFunctionApi(String functionCode);

    /**
     * 获取API列表
     * @param bean
     * @return
     */
    List<SysApiBean> findApiListByFunctionApi(SysFunctionApiBean bean);

    /**
     * 列表查询
     * @param bean
     * @return
     */
    List<SysFunctionApiBean> findList(SysFunctionApiBean bean);

    /**
     * 删除多条记录
     * @param bean
     * @return
     */
    RestBean delete(SysFunctionApiBean bean);
}
