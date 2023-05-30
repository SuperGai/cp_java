package com.hh.appraisal.springboot.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

import com.hh.appraisal.springboot.core.annotation.EntityDoc;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;

import java.io.Serializable;
import java.lang.Integer;
import java.util.Date;

/**
 * 测评用户信息表Bean
 * 用于控制层展示数据
 * @see com.hh.appraisal.springboot.entity.EvaluatoionUser
 * @author gaigai
 * @date 2021/06/26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "测评用户信息表实体")
@Excel("测评用户信息")
public class EvaluatoionUserBean implements Serializable {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String code;

    /**
     * 数据是否有效
     */
    @ApiModelProperty(value = "数据是否有效")
    private Integer valid;
    
    /**
     * 姓名
     */
    @ExcelField("姓名")
    @ApiModelProperty(value = "姓名")
    private String name;

    /**
     * 性别
     */
    @ExcelField("性别")
    @ApiModelProperty(value = "性别")
    private String sex;
    
    /**
     * 出生年月
     */
    @ExcelField(value = "出生年月", dateFormat = "yyyy/MM/dd HH:mm:ss")
    @ApiModelProperty(value = "出生年月")
    private Date birthdate;
    
    /**
     * 手机号
     */
    @ExcelField("手机号")
    @ApiModelProperty(value = "手机号")
    private String phone;

    /**
     * 学校
     */
    @ExcelField("学校")
    @ApiModelProperty(value = "学校")
    private String school;

    /**
     * 班级
     */
    @ExcelField("班级")
    @ApiModelProperty(value = "班级")
    private String schoolClass;

    /**
     * 测评码
     */
    @ExcelField("测评码")
    @ApiModelProperty(value = "测评码")
    private String evaluatoionCode;

    /**
     * 测评报告链接
     */
//    @ExcelField("测评报告链接")
    @ApiModelProperty(value = "测评报告链接")
    private String reportUrl;

    /**
     * 记录创建时间
     */
    @ExcelField(value = "创建时间", dateFormat = "yyyy/MM/dd HH:mm:ss")
    @ApiModelProperty(value = "记录创建时间")
    private Date createTime;

    /**
     * 记录更新时间
     */
    @ApiModelProperty(value = "记录更新时间")
    private Date updateTime;

    /**
     * 实际报告存放地址
     */
    @ApiModelProperty(value = "实际报告存放地址")
    private String url;
    
    
    /**
     * 是否完成
     */
    @ApiModelProperty(value = "是否完成")
    private String isComplete;
    

    /**
     * 测评用户信息表 唯一标识 集合
     */
    @ApiModelProperty(value = "测评用户信息表 唯一标识 集合")
    private List<String> evaluatoionUserCodeList;
    
    
    /**
     * 答题时间
     */
    @ExcelField("答题时间")
    @ApiModelProperty(value = "答题时间")
    private double spendTime;
    
    /**
     * 测评时间开始
     */
    private String createTimeStart;
    
    /**
     * 测评时间结束
     */
    private String createTimeEnd;
    
    /**
     * 测评人员类型
     */
    private String personType;
    
    /**
     * 邮箱
     */
    @ExcelField("邮箱")
    @ApiModelProperty(value = "邮箱")
    private String email;
    
    /**
     * 年龄
     */
    @ExcelField("年龄")
    @ApiModelProperty(value = "年龄")
    private int age;

    
    /**
     * 公司
     */
    @ExcelField("公司")
    @ApiModelProperty(value = "公司")
    private String companyName;
    
    
    /**
     * 公司
     */
    @ExcelField("职务")
    @ApiModelProperty(value = "职务")
    private String position;
    

}
