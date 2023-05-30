package com.hh.appraisal.springboot.controller.system;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.core.constant.RestCode;
import com.hh.appraisal.springboot.service.system.SysFunctionApiService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统功能接口 控制器
 * @author gaigai
 * @date 2019/05/15
 */
@RestController
@RequestMapping("/sys/function/api")
public class SysFunctionApiController {

    private final SysFunctionApiService functionApiService;

    public SysFunctionApiController(SysFunctionApiService functionApiService) {
        this.functionApiService = functionApiService;
    }

    /**
     * 查询指定功能的API接口列表树
     * @param functionCode
     * @return
     */
    @RequestMapping("/tree")
    public RestBean tree(String functionCode){
        return RestBean.ok(
                functionApiService.treeFunctionApi(functionCode)
        );
    }

    /**
     * 更新功能API树
     * @param apiCodeList
     * @param functionCode
     * @return
     */
    @RequestMapping("/tree/update")
    public RestBean treeUpdate(@RequestParam(value = "apiCodeList", required = false) List<String> apiCodeList,
                               String functionCode){
        if(ObjectUtils.isEmpty(functionCode)) {
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }
        return functionApiService.treeUpdate(apiCodeList, functionCode);
    }
}
