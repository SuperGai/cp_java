package com.hh.appraisal.springboot.controller.system;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hh.appraisal.springboot.bean.system.SysDictBean;
import com.hh.appraisal.springboot.bean.system.SysFunctionApiBean;
import com.hh.appraisal.springboot.bean.system.SysFunctionBean;
import com.hh.appraisal.springboot.bean.system.SysRoleFunctionBean;
import com.hh.appraisal.springboot.constant.DictTypeNumConstant;
import com.hh.appraisal.springboot.core.baen.PageBean;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.core.constant.DataValid;
import com.hh.appraisal.springboot.core.constant.RestCode;
import com.hh.appraisal.springboot.service.system.SysDictService;
import com.hh.appraisal.springboot.service.system.SysFunctionApiService;
import com.hh.appraisal.springboot.service.system.SysFunctionService;
import com.hh.appraisal.springboot.service.system.SysRoleFunctionService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * 系统功能 控制器
 * @author gaigai
 * @date 2019/05/15
 */
@RestController
@RequestMapping("/sys/function")
public class SysFunctionController {

    private final SysFunctionService functionService;
    private final SysFunctionApiService functionApiService;
    private final SysRoleFunctionService roleFunctionService;
    private final SysDictService dictService;
    public SysFunctionController(SysFunctionService functionService, SysFunctionApiService functionApiService, SysRoleFunctionService roleFunctionService, SysDictService dictService) {
        this.functionService = functionService;
        this.functionApiService = functionApiService;
        this.roleFunctionService = roleFunctionService;
        this.dictService = dictService;
    }

    /**
     * 下拉框列表
     * @param bean
     * @return
     */
    @RequestMapping("/dropdownList")
    public RestBean dropdownList(SysFunctionBean bean){
        return RestBean.ok(functionService.findList(bean));
    }

    /**
     * 分页查询
     * @param bean
     * @return
     */
    @RequestMapping("/listPage")
    public RestBean listPage(SysFunctionBean bean, PageBean pageBean){
        Page<SysFunctionBean> page = functionService.findPage(bean,pageBean);

        // 所有字典值
        HashMap<String, SysDictBean> allDictMap = dictService.findAllDictByType(DictTypeNumConstant.SYSTEM_MODULE);

        page.getRecords().forEach(item -> {
            // 设置字典值描述
            if(allDictMap.get(item.getModule()) != null){
                item.setModuleDesc(allDictMap.get(item.getModule()).getName());
            }
        });

        return RestBean.ok(page);
    }

    /**
     * 查看详情
     * @param code
     * @return
     */
    @RequestMapping("/detail")
    public RestBean detail(String code) {
        SysFunctionBean bean = functionService.findOne(SysFunctionBean.builder()
                .code(code).valid(DataValid.VALID)
                .build());
        return RestBean.ok(bean);
    }

    /**
     * 新增一条记录
     * @param bean
     * @return
     */
    @RequestMapping("/add")
    public RestBean add(SysFunctionBean bean){
        if(functionService.repetition(bean)){
            return RestBean.error("记录重复");
        }
        functionService.add(bean);
        return RestBean.ok();
    }

    /**
     * 根据唯一标识更新一条记录
     * @param bean
     * @return
     */
    @RequestMapping("/update")
    public RestBean update(SysFunctionBean bean){
        if(bean == null || ObjectUtils.isEmpty(bean.getCode())) {
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }
        return RestBean.ok(functionService.updateByCode(bean));
    }

    /**
     * 根据唯一标识删除一条记录
     * @param code
     * @return
     */
    @RequestMapping("/delete")
    public RestBean delete(String code) {
        // 不传入id不允许删除
        if(ObjectUtils.isEmpty(code)){
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }

        // 删除功能
        RestBean delMenuRest = functionService.deleteByCode(code);

        if(delMenuRest.getCode() == RestCode.DEFAULT_SUCCESS.getCode()){
            // 删除功能角色对应记录
            functionApiService.delete(SysFunctionApiBean.builder()
                    .functionCode(code)
                    .build());
            // 删除功能接口对应记录
            roleFunctionService.delete(SysRoleFunctionBean.builder()
                    .functionCode(code)
                    .build());
        }

        return delMenuRest;
    }

    /**
     * 批量删除
     * @param codeList
     * @return
     */
    @RequestMapping("/batchDelete")
    public RestBean batchDelete(@RequestParam("codeList") List<String> codeList){
        if(ObjectUtils.isEmpty(codeList)){
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }
        functionService.deleteByCode(codeList);
        return RestBean.ok();
    }
}
