package com.hh.appraisal.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hh.appraisal.springboot.core.annotation.EntityDoc;
import com.hh.appraisal.springboot.core.entity.BasicEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微信小程序用户
 * 一般由前端使用 wx.getUserInfo 获取
 * @author gaigai
 * @date 2019/05/15
 */
@EntityDoc(isClass = true, note = "微信小程序用户")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("wx_miniapp_user")
public class WxMiniAppUser extends BasicEntity {

    /**
     * 昵称
     * 注意防止昵称出现表情符号，需要转义(使用hutool)
     */
    @EntityDoc(note = "昵称")
    private String nickName;

    /**
     * 性别
     */
    @EntityDoc(note = "性别")
    private Integer gender;

    /**
     * 语言
     */
    @EntityDoc(note = "语言")
    private String language;

    /**
     * 国家
     */
    @EntityDoc(note = "国家")
    private String country;

    /**
     * 省份
     */
    @EntityDoc(note = "省份")
    private String province;

    /**
     * 城市
     */
    @EntityDoc(note = "城市")
    private String city;

    /**
     * 头像链接
     */
    @EntityDoc(note = "头像")
    private String avatarUrl;

    /**
     * openid
     */
    @EntityDoc(note = "openid")
    private String openid;

    /**
     * 手机号
     */
    @EntityDoc(note = "手机号")
    private String phone;

}
