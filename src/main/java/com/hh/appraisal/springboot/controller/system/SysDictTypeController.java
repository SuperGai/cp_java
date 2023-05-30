package com.hh.appraisal.springboot.controller.system;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hh.appraisal.springboot.bean.system.SysDictTypeBean;
import com.hh.appraisal.springboot.core.baen.PageBean;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.core.constant.AuthConstant;
import com.hh.appraisal.springboot.core.constant.RestCode;
import com.hh.appraisal.springboot.service.system.SysDictService;
import com.hh.appraisal.springboot.service.system.SysDictTypeService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * 系统字典类型 控制器
 * @author gaigai
 * @date 2019/05/15
 */
@RestController
@RequestMapping("/sys/dict/type")
public class SysDictTypeController {

    private final SysDictTypeService dictTypeService;
    private final SysDictService dictService;

    public SysDictTypeController(SysDictTypeService dictTypeService, SysDictService dictService) {
        this.dictTypeService = dictTypeService;
        this.dictService = dictService;
    }

    /**
     * 下拉框列表
     * @param bean
     * @return
     */
    @RequestMapping(value = "/dropdownList")
    public RestBean dropdownList(SysDictTypeBean bean){
        return RestBean.ok(dictTypeService.findList(bean));
    }

    /**
     * 分页查询
     * @param bean
     * @return
     */
    @RequestMapping(value = "/listPage")
    public RestBean listPage(SysDictTypeBean bean, PageBean pageBean){
        Page<SysDictTypeBean> page = dictTypeService.findPage(bean,pageBean);
        if(page == null || CollectionUtils.isEmpty(page.getRecords())) {
            return RestBean.ok(new Page<>());
        }
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
        return RestBean.ok(dictTypeService.findByCode(code));
    }

    /**
     * 新增一条记录
     * @param bean
     * @return
     */
    @RequestMapping(value = "/add")
    public RestBean add(SysDictTypeBean bean){
        return dictTypeService.add(bean);
    }

    /**
     * 根据唯一标识更新一条记录
     * @param bean
     * @return
     */
    @RequestMapping(value = "/update")
    public RestBean update(SysDictTypeBean bean){
        if(bean == null || ObjectUtils.isEmpty(bean.getCode())) {
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }
        return dictTypeService.updateByCode(bean);
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
        return dictTypeService.deleteByCode(code);
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
        dictTypeService.deleteByCode(codeList);
        return RestBean.ok();
    }

}
