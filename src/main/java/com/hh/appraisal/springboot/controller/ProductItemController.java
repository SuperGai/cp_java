package com.hh.appraisal.springboot.controller;

import io.swagger.annotations.*;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hh.appraisal.springboot.core.baen.PageBean;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.core.constant.RestCode;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.List;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.hh.appraisal.springboot.core.constant.AuthConstant;
import org.springframework.web.bind.annotation.RequestMethod;
import com.hh.appraisal.springboot.bean.ProductItemBean;
import com.hh.appraisal.springboot.service.ProductItemService;

/**
 * 产品明细 控制器
 * @author gaigai
 * @date 2021/06/26
 */
@Api(tags = "产品明细")
@RestController
@RequestMapping("/productItem")
public class ProductItemController {

    private final ProductItemService productItemService;

    public ProductItemController(ProductItemService productItemService) {
            this.productItemService = productItemService;
    }

    /**
     * 分页查询
     * @param bean
     * @return
     */
    @ApiOperation(value="分页查询", response = RestBean.class)
    @ApiOperationSupport(ignoreParameters = {"code","createTime","updateTime","valid"})
    @ApiImplicitParam(paramType = "header", dataType = "String", name = AuthConstant.TOKEN, value = "鉴权token", required = true)
    @RequestMapping(value = "/listPage", method = {RequestMethod.POST})
    public RestBean listPage(ProductItemBean bean, PageBean pageBean){
        Page<ProductItemBean> page = productItemService.findPage(bean,pageBean);
        if(page == null || CollectionUtils.isEmpty(page.getRecords())) {
            return RestBean.ok(new Page<>());
        }

        // 处理列表逻辑....

        return RestBean.ok(page);
    }

    /**
     * 查看详情
     * @param code
     * @return
     */
    @ApiOperation(value="查看详情", response = RestBean.class)
    @ApiImplicitParams({
      @ApiImplicitParam(paramType = "header", dataType = "String", name = AuthConstant.TOKEN, value = "鉴权token", required = true),
      @ApiImplicitParam(dataType = "String", name = "code", value = "唯一标识", required = true)
    })
    @RequestMapping(value = "/detail", method = {RequestMethod.POST})
    public RestBean detail(String code) {
        if(ObjectUtils.isEmpty(code)){
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }
        ProductItemBean bean = productItemService.findByCode(code);
        return RestBean.ok(bean);
    }

    /**
     * 新增一条记录
     * @param bean
     * @return
     */
    @ApiOperation(value="新增一条记录", response = RestBean.class)
    @ApiOperationSupport(ignoreParameters = {"code","createTime","updateTime","valid"})
    @ApiImplicitParam(paramType = "header", dataType = "String", name = AuthConstant.TOKEN, value = "鉴权token", required = true)
    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    public RestBean add(ProductItemBean bean){
        return RestBean.ok(productItemService.add(bean));
    }

    /**
     * 根据唯一标识更新一条记录
     * @param bean
     * @return
     */
    @ApiOperation(value="根据唯一标识更新一条记录", response = RestBean.class)
    @ApiImplicitParam(paramType = "header", dataType = "String", name = AuthConstant.TOKEN, value = "鉴权token", required = true)
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public RestBean update(ProductItemBean bean){
        if(bean == null || ObjectUtils.isEmpty(bean.getCode())) {
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }
        return RestBean.ok(productItemService.updateByCode(bean));
    }

    /**
     * 根据唯一标识删除一条记录
     * @param code
     * @return
     */
    @ApiOperation(value="根据唯一标识删除一条记录", response = RestBean.class)
    @ApiImplicitParams({
    @ApiImplicitParam(paramType = "header", dataType = "String", name = AuthConstant.TOKEN, value = "鉴权token", required = true),
    @ApiImplicitParam(dataType = "String", name = "code", value = "唯一标识", required = true)
    })
    @RequestMapping(value = "/deleteByCode", method = {RequestMethod.POST})
    public RestBean delete(String code){
        if(ObjectUtils.isEmpty(code)){
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }
        return RestBean.ok(
            productItemService.deleteByCode(code)
        );
    }

    /**
     * 根据唯一标识集合批量删除
     * @param codeList
     * @return
     */
    @ApiOperation(value="批量删除", response = RestBean.class)
    @ApiImplicitParam(paramType = "header", dataType = "String", name = AuthConstant.TOKEN, value = "鉴权token", required = true)
    @RequestMapping(value = "/batchDeleteByCodeList", method = {RequestMethod.POST})
    public RestBean batchDelete(@RequestParam("codeList") List<String> codeList){
        if(ObjectUtils.isEmpty(codeList)){
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR);
        }
        productItemService.deleteByCode(codeList);
        return RestBean.ok();
    }

}
