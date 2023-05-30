package com.hh.appraisal.springboot.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.io.Serializable;
import java.lang.Integer;
import java.util.Date;

/**
 * 微信小程序用户实体类
 * 用于控制层展示数据
 * @see com.hh.appraisal.springboot.entity.WxMiniAppUser
 * @author gaigai
 * @date 2021/02/01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "微信小程序用户实体")
public class WxMiniAppUserBean implements Serializable {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String code;

    /**
     * 数据是否有效
     */
    private Integer valid;

    /**
     * 记录创建时间
     */
    private Date createTime;

    /**
     * 记录更新时间
     */
    private Date updateTime;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickName;

    /**
     * 性别
     */
    @ApiModelProperty(value = "性别")
    private Integer gender;

    /**
     * 语言
     */
    @ApiModelProperty(value = "语言")
    private String language;

    /**
     * 国家
     */
    @ApiModelProperty(value = "国家")
    private String country;

    /**
     * 省份
     */
    @ApiModelProperty(value = "省份")
    private String province;

    /**
     * 城市
     */
    @ApiModelProperty(value = "城市")
    private String city;

    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String avatarUrl;

    /**
     * openid
     */
    @ApiModelProperty(value = "openid")
    private String openid;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String phone;

    /**
     * WxMiniAppUser 唯一标识 集合
     */
    @ApiModelProperty(value = "微信小程序用户 唯一标识 集合")
    private List<String> wxMiniAppUserCodeList;

    /**
     * 该用户登录token
     */
    @ApiModelProperty(value = "该用户登录token")
    private String token;

}
