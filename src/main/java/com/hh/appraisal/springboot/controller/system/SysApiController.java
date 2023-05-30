package com.hh.appraisal.springboot.controller.system;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hh.appraisal.springboot.bean.system.SysApiBean;
import com.hh.appraisal.springboot.bean.system.SysDictBean;
import com.hh.appraisal.springboot.constant.DictTypeNumConstant;
import com.hh.appraisal.springboot.core.baen.PageBean;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.core.constant.AuthConstant;
import com.hh.appraisal.springboot.core.constant.RestCode;
import com.hh.appraisal.springboot.service.system.SysApiService;
import com.hh.appraisal.springboot.service.system.SysDictService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.List;

/**
 * 系统接口 控制器
 * @author gaigai
 * @date 2019/05/15
 */
@RestController
@RequestMapping("/sys/api")
public class SysApiController {

    private final SysApiService apiService;
    private final SysDictService dictService;
    public SysApiController(SysApiService apiService, SysDictService dictService) {
        this.apiService = apiService;
        this.dictService = dictService;
    }

    /**
     * 分页查询
     * @param bean
     * @return
     */
    @RequestMapping(value = "/listPage")
    public RestBean listPage(SysApiBean bean, PageBean pageBean){
        Page<SysApiBean> page = apiService.findPage(bean,pageBean);
        if(page == null || CollectionUtils.isEmpty(page.getRecords())) {
            return RestBean.ok(new Page<>());
        }

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
    @RequestMapping(value = "/detail")
    public RestBean detail(String code) {
        if(ObjectUtils.isEmpty(code)){
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }
        SysApiBean bean = apiService.findByCode(code);
        return RestBean.ok(bean);
    }

    /**
     * 新增一条记录
     * @param bean
     * @return
     */
    @RequestMapping(value = "/add")
    public RestBean add(SysApiBean bean){
        return RestBean.ok(apiService.add(bean));
    }

    /**
     * 根据唯一标识更新一条记录
     * @param bean
     * @return
     */
    @RequestMapping(value = "/update")
    public RestBean update(SysApiBean bean){
        if(bean == null || ObjectUtils.isEmpty(bean.getCode())) {
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }
        return RestBean.ok(apiService.updateByCode(bean));
    }

    /**
     * 根据唯一标识删除一条记录
     * @param code
     * @return
     */
    @RequestMapping(value = "/delete")
    public RestBean delete(String code){
        // 不传入id不允许删除
        if(ObjectUtils.isEmpty(code)){
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }
        return RestBean.ok(
                apiService.deleteByCode(code)
        );
    }

    /**
     * 批量删除
     * @param codeList
     * @return
     */
    @RequestMapping(value = "/batchDelete")
    public RestBean batchDelete(@RequestParam("codeList") List<String> codeList){
        if(ObjectUtils.isEmpty(codeList)){
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }
        apiService.deleteByCode(codeList);
        return RestBean.ok();
    }
}
