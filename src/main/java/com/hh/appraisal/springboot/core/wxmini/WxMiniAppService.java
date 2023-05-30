package com.hh.appraisal.springboot.core.wxmini;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.core.constant.RestCode;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 微信小程序服务类
 * @author gaigai
 * @date 2021/02/01
 */
@Slf4j
@Component
public class WxMiniAppService {

    private final WxMiniAppConfiguration wxMiniAppConfiguration;
    private WxMaService wxService;
    public WxMiniAppService(WxMiniAppConfiguration wxMiniAppConfiguration) {
        this.wxMiniAppConfiguration = wxMiniAppConfiguration;
        if(wxMiniAppConfiguration != null && wxMiniAppConfiguration.isEnable()){
            this.wxService = wxMiniAppConfiguration.getMaService();
            if(this.wxService == null){
                log.error("无法获取微信实例");
            }
        }
    }

    /**
     * 微信小程序登录
     * 用于只有一个小程序的项目场景
     *
     * @param code
     * @return
     */
    public WxMaJscode2SessionResult login(String code) {
        try {
            WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(code);
            return session;
        } catch (WxErrorException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 微信小程序登录
     *
     * @return WxMaJscode2SessionResult 对象
     * @see WxMaJscode2SessionResult
     */
    public WxMaJscode2SessionResult login(String appid, String code) {
        WxMaService wxService = wxMiniAppConfiguration.getMaService(appid);
        if (wxService == null) {
            log.error("找不到appid对应的配置, appid=" + appid);
            return null;
        }

        try {
            WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(code);
            return session;
        } catch (WxErrorException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 发送微信订阅消息
     * 原来的模板消息
     * 注意小程序端必须使用按钮调起 uni.requestSubscribeMessage 方法或 wx.requestSubscribeMessage 方法来获取用户授权，不然无法发送通知
     * @param pageUrl 点击跳转的页面
     * @param openid 目标用户openid
     * @param templateId 模板ID，从公众平台获取
     * @param dataMap 模板数据
     *                key: {{thing11.DATA}}, value: 数据
     *
     * 举例：比如申请的订阅消息模板内容如下:
     *          就诊科室 {{thing12.DATA}}
     *          就诊医生 {{thing11.DATA}}
     *          就诊人   {{thing8.DATA}}
     *          订单状态 {{phrase10.DATA}}
     *          挂号时间 {{time9.DATA}}
     *      则调用时按如下方法调用：
     *      RestBean restBean = wxMiniAppService.sendSubscribeMessage("点击跳转的页面路径", "目标用户openid", "模板id",
     *             new HashMap<String, String>(){{
     *                 put("thing12", "xxx科室");
     *                 put("thing11", "张医生");
     *                 put("thing8", "王五");
     *                 put("phrase10", "已完成");
     *                 put("time9", "2020-01-03 23:23:01");
     *        }});
     * @return
     */
    public RestBean sendSubscribeMessage(String pageUrl, String openid, String templateId,
                                         HashMap<String, String> dataMap) {
        if(CollectionUtils.isEmpty(dataMap)) {
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR, "数据不能为空");
        }
        if(ObjectUtils.isEmpty(openid)){
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR, "openid不能为空");
        }
        if(ObjectUtils.isEmpty(templateId)){
            return RestBean.error(RestCode.DEFAULT_PARAMS_ERROR, "模板id不能为空");
        }

        WxMaSubscribeMessage subscribeMessage = new WxMaSubscribeMessage();

        //跳转小程序页面路径
        subscribeMessage.setPage(pageUrl);// pages/index/index
        //模板消息id
        subscribeMessage.setTemplateId(templateId);
        //给谁推送 用户的openid
        subscribeMessage.setToUser(openid);

        List<WxMaSubscribeMessage.Data> dataList = new ArrayList<>();
        dataMap.forEach((key, value) -> {
            WxMaSubscribeMessage.Data data = new WxMaSubscribeMessage.Data();
            data.setName(key);
            data.setValue(value);
            dataList.add(data);
        });
        subscribeMessage.setData(dataList);

        try {
            wxService.getMsgService().sendSubscribeMsg(subscribeMessage);
        } catch (Exception e) {
            return RestBean.error("微信订阅消息推送失败: " + e.getMessage());
        }
        return RestBean.ok();
    }
}
