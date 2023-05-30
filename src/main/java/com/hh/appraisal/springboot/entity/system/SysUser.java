package com.hh.appraisal.springboot.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hh.appraisal.springboot.core.annotation.EntityDoc;
import com.hh.appraisal.springboot.core.entity.BasicEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统用户表
 * @author gaigai
 * @date 2019/05/15
 */
@EntityDoc(note = "系统用户", isClass = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user")
public class SysUser extends BasicEntity {

    /**
     * 账号 唯一
     */
    @EntityDoc(note = "账号")
    private String account;

    /**
     * 密码
     */
    @EntityDoc(note = "密码")
    private String password;

    /**
     * 昵称
     */
    @EntityDoc(note = "昵称")
    private String name;

    /**
     * 用于加密密码的盐
     */
    @EntityDoc(note = "密码盐")
    private String salt;

    /**
     * 手机号
     */
    @EntityDoc(note = "手机号")
    private String phone;

    /**
     * 状态
     */
    @EntityDoc(note = "状态")
    private String status;

    /**
     * 审核状态
     */
    @EntityDoc(note = "审核状态")
    private String approveStatus;

    /**
     * 审核结果
     */
    @EntityDoc(note = "审核结果")
    private String approveResult;

}
