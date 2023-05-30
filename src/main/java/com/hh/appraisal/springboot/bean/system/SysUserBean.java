package com.hh.appraisal.springboot.bean.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

/**
 * 系统用户表
 * @author gaigai
 * @date 2019/05/15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "系统用户实体")
public class SysUserBean {

    /**
     * 主键 唯一标识
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
     * 账号 唯一
     */
    @ApiModelProperty(value = "账号")
    private String account;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String name;

    /**
     * 用于加密密码的盐
     */
    private String salt;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String phone;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private String status;

    /**
     * 审核状态
     */
    @ApiModelProperty(value = "审核状态")
    private String approveStatus;

    /**
     * 审核结果
     */
    @ApiModelProperty(value = "审核结果")
    private String approveResult;

    /**
     * 验证码 登录用
     */
    @ApiModelProperty(value = "验证码")
    private String verifyCode;

    /**
     * 客户端机器标识 校验登录验证码用
     */
    @ApiModelProperty(value = "客户端机器标识")
    private String keyCode;

    /**
     * 用户 code 集合
     */
    @ApiModelProperty(value = "用户 唯一标识 集合")
    private List<String> userCodeList;

    /**
     * 新密码
     */
    @ApiModelProperty(value = "新密码")
    private String newPassword;

    /**
     * 该用户所属角色列表
     */
    @ApiModelProperty(value = "该用户所属角色列表")
    private List<String> roleCodeList;
}
