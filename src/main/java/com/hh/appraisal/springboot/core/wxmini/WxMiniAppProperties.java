package com.hh.appraisal.springboot.core.wxmini;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 微信小程序配置
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "wx.miniapp")
public class WxMiniAppProperties {

    /**
     * 是否启用
     */
    private Boolean enable = false;

    private List<Config> configs;

    @Data
    public static class Config {
        /**
         * 设置微信小程序的appid
         */
        private String appid;

        /**
         * 设置微信小程序的Secret
         */
        private String secret;

        /**
         * 设置微信小程序消息服务器配置的token
         */
        private String token;

        /**
         * 设置微信小程序消息服务器配置的EncodingAESKey
         */
        private String aesKey;

        /**
         * 消息格式，XML或者JSON
         */
        private String msgDataFormat;
    }

}
