package com.hh.appraisal.springboot.core.wxmini;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaKefuMessage;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import cn.binarywang.wx.miniapp.message.WxMaMessageHandler;
import cn.binarywang.wx.miniapp.message.WxMaMessageRouter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hh.appraisal.springboot.core.baen.RestBean;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 微信小程序配置类
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Slf4j
@Component
public class WxMiniAppConfiguration {

    /**
     * 微信小程序配置
     */
    private WxMiniAppProperties properties;

    private static final Map<String, WxMaMessageRouter> routers = Maps.newHashMap();

    /**
     * 存放appid对应的Router
     */
    private static Map<String, WxMaService> maServices;

    @Autowired
    public WxMiniAppConfiguration(WxMiniAppProperties properties){
        if(properties != null && properties.getEnable() != null && properties.getEnable()){
            this.properties = properties;
            RestBean restBean = this.init();
            log.info("初始化微信小程序功能");
        }
    }

    /**
     * 根据 appid 获取 WxMaService
     * @param appid
     * @return
     */
    public WxMaService getMaService(String appid) {
        return maServices.get(appid);
    }

    /**
     * 只获取一个WxMaService
     * @return
     */
    public WxMaService getMaService() {
        for(String key : maServices.keySet()){
            return maServices.get(key);
        }
        return null;
    }

    /**
     * 根据 appid 获取 WxMaMessageRouter
     * @param appid
     * @return
     */
    public static WxMaMessageRouter getRouter(String appid) {
        return routers.get(appid);
    }

    /**
     * 初始化
     * @return
     */
    public RestBean init() {
        List<WxMiniAppProperties.Config> configs = this.properties.getConfigs();
        if (configs == null) {
            return RestBean.error("缺少微信相关配置");
        }

        maServices = configs.stream()
            .map(a -> {
                WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
//                WxMaDefaultConfigImpl config = new WxMaRedisConfigImpl(new JedisPool());
                // 使用上面的配置时，需要同时引入jedis-lock的依赖，否则会报类无法找到的异常
                config.setAppid(a.getAppid());
                config.setSecret(a.getSecret());
                config.setToken(a.getToken());
                config.setAesKey(a.getAesKey());
                config.setMsgDataFormat(a.getMsgDataFormat());

                WxMaService service = new WxMaServiceImpl();
                service.setWxMaConfig(config);
                routers.put(a.getAppid(), this.newRouter(service));
                return service;
            }).collect(Collectors.toMap(s -> s.getWxMaConfig().getAppid(), a -> a));
        return RestBean.ok();
    }

    private WxMaMessageRouter newRouter(WxMaService service) {
        final WxMaMessageRouter router = new WxMaMessageRouter(service);
        router
            .rule().handler(logHandler).next()
            .rule().async(false).content("订阅消息").handler(subscribeMsgHandler).end()
            .rule().async(false).content("文本").handler(textHandler).end()
            .rule().async(false).content("图片").handler(picHandler).end()
            .rule().async(false).content("二维码").handler(qrcodeHandler).end();
        return router;
    }

    private final WxMaMessageHandler subscribeMsgHandler = (wxMessage, context, service, sessionManager) -> {
        service.getMsgService().sendSubscribeMsg(WxMaSubscribeMessage.builder()
            .templateId("此处更换为自己的模板id")
            .data(Lists.newArrayList(
                new WxMaSubscribeMessage.Data("keyword1", "339208499")))
            .toUser(wxMessage.getFromUser())
            .build());
        return null;
    };

    private final WxMaMessageHandler logHandler = (wxMessage, context, service, sessionManager) -> {
        log.info("收到消息：" + wxMessage.toString());
        service.getMsgService().sendKefuMsg(WxMaKefuMessage.newTextBuilder().content("收到信息为：" + wxMessage.toJson())
            .toUser(wxMessage.getFromUser()).build());
        return null;
    };

    private final WxMaMessageHandler textHandler = (wxMessage, context, service, sessionManager) -> {
        service.getMsgService().sendKefuMsg(WxMaKefuMessage.newTextBuilder().content("回复文本消息")
            .toUser(wxMessage.getFromUser()).build());
        return null;
    };

    private final WxMaMessageHandler picHandler = (wxMessage, context, service, sessionManager) -> {
        try {
            WxMediaUploadResult uploadResult = service.getMediaService()
                .uploadMedia("image", "png",
                    ClassLoader.getSystemResourceAsStream("tmp.png"));
            service.getMsgService().sendKefuMsg(
                WxMaKefuMessage
                    .newImageBuilder()
                    .mediaId(uploadResult.getMediaId())
                    .toUser(wxMessage.getFromUser())
                    .build());
        } catch (WxErrorException e) {
            e.printStackTrace();
        }

        return null;
    };

    private final WxMaMessageHandler qrcodeHandler = (wxMessage, context, service, sessionManager) -> {
        try {
            final File file = service.getQrcodeService().createQrcode("123", 430);
            WxMediaUploadResult uploadResult = service.getMediaService().uploadMedia("image", file);
            service.getMsgService().sendKefuMsg(
                WxMaKefuMessage
                    .newImageBuilder()
                    .mediaId(uploadResult.getMediaId())
                    .toUser(wxMessage.getFromUser())
                    .build());
        } catch (WxErrorException e) {
            e.printStackTrace();
        }

        return null;
    };

    /**
     * 是否启用微信小程序服务
     * @return
     */
    public boolean isEnable(){
        if(this.properties == null || this.properties.getEnable() == null
                || !this.properties.getEnable()){
            return false;
        }
        return this.properties.getEnable();
    }
}
