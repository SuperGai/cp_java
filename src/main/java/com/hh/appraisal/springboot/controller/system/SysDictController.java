package com.hh.appraisal.springboot.controller.system;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hh.appraisal.springboot.bean.system.SysDictBean;
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
import java.util.stream.Collectors;

/**
 * 系统字典 控制器
 * @author gaigai
 * @date 2019/05/15
 */
@RestController
@RequestMapping("/sys/dict")
public class SysDictController {

    private final SysDictService dictService;
    private final SysDictTypeService dictTypeService;

    public SysDictController(SysDictService dictService, SysDictTypeService dictTypeService) {
        this.dictService = dictService;
        this.dictTypeService = dictTypeService;
    }

    /**
     * 下拉框列表
     * @param typeNum 字典类型编号
     * @param value 字典值
     * @return
     */
    @RequestMapping(value = "/dropdownList")
    public RestBean dropdownList(String typeNum, String value){
        SysDictTypeBean typeBean = dictTypeService.findOne(SysDictTypeBean.builder()
                .num(typeNum)
                .build());
        if(typeBean == null){
            return RestBean.ok();
        }

        return RestBean.ok(
                dictService.findList(SysDictBean.builder()
                        .typeCode(typeBean.getCode())
                        .value(ObjectUtils.isEmpty(value) ? null : value)
                        .build())
        );
    }

    /**
     * 分页查询
     * @param bean
     * @return
     */
    @RequestMapping(value = "/listPage")
    public RestBean listPage(SysDictBean bean, PageBean pageBean){
        Page<SysDictBean> page = dictService.findPage(bean,pageBean);
        if(page == null || CollectionUtils.isEmpty(page.getRecords())) {
            return RestBean.ok(new Page<>());
        }

        page.getRecords().stream().map(v->{
            if(v.getTypeCode() != null){
                SysDictTypeBean typeBean = dictTypeService.findByCode(v.getTypeCode());
                if(typeBean != null){
                    v.setTypeName(typeBean.getName());
                    v.setTypeNum(typeBean.getNum());
                }else{
                    v.setTypeName("--");
                    v.setTypeNum("--");
                }
            }
            return v;
        }).collect(Collectors.toList());

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

        SysDictBean bean = dictService.findByCode(code);

        if(bean != null && bean.getTypeCode() != null){
            SysDictTypeBean typeBean = dictTypeService.findByCode(bean.getTypeCode());
            bean.setTypeCode(typeBean == null ? "" : typeBean.getCode());
        }
        return RestBean.ok(bean);
    }

    /**
     * 新增一条记录
     * @param bean
     * @return
     */
    @RequestMapping(value = "/add")
    public RestBean add(SysDictBean bean){
        return dictService.add(bean);
    }

    /**
     * 根据唯一标识更新一条记录
     * @param bean
     * @return
     */
    @RequestMapping(value = "/update")
    public RestBean update(SysDictBean bean){
        if(bean == null || ObjectUtils.isEmpty(bean.getCode())) {
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }
        return dictService.updateByCode(bean);
    }

    /**
     * 根据唯一标识删除一条记录
     * @param code
     * @return
     */
    @RequestMapping(value = "/delete")
    public RestBean delete(String code){
        if(ObjectUtils.isEmpty(code)){
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }
        return dictService.deleteByCode(code);
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
        dictService.deleteByCode(codeList);
        return RestBean.ok();
    }
}
