package com.hh.appraisal.springboot.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hh.appraisal.springboot.core.annotation.EntityDoc;
import com.hh.appraisal.springboot.core.entity.BasicEntity;
import com.wuwenze.poi.annotation.ExcelField;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 测评用户信息表
 * @author gaigai
 * @date 2021/06/29
 */
@EntityDoc(isClass = true, note = "测评用户信息表")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("evaluatoion_user")
public class EvaluatoionUser extends BasicEntity {

//	ID	INTEGER	
//	NAME	VARCHAR	姓名
//	PHONE	VARCHAR	手机号
//	SCHOOL	VARCHAR	学校
//	CLASS	VARCHAR	班级
//	EVALUATOION_CODE	VARCHAR	测评码
//	REPORT_URL	VARCHAR	测评报告链接
//	OWNERID	INTEGER	创建人ID
//	MODIFIERID	INTEGER	修改人ID
//	CREATIONDATE	DATETIME	创建时间
//	MODIFIEDDATE	DATETIME	修改时间
//	ISACTIVE	CHAR	是否可用
	
    /**
     * 姓名
     */
    @EntityDoc(note = "姓名")
    private String name;

    /**
     * 学校名称
     */
    @EntityDoc(note = "手机号")
    private String phone;
    
    /**
     * 学校
     */
    @EntityDoc(note = "学校")
    private String school;
    
    /**
     * 性别
     */
    @EntityDoc(note = "性别")
    private String sex;
    
    /**
     * 出生年月
     */
    @EntityDoc(note = "出生年月")
    private Date birthdate;
    
    /**
     * 班级
     */
    @EntityDoc(note = "班级")
    private String schoolClass;
    
    /**
     * 测评码
     */
    @EntityDoc(note = "测评码")
    private String evaluatoionCode; 
    
    /**
     * 测评报告链接
     */
    @EntityDoc(note = "测评报告链接")
    private String reportUrl;
    
    /**
     * 实际报告存放地址
     */
    @EntityDoc(note = "实际报告存放地址")
    private String url;
    
    /**
     * 是否完成
     */
    @EntityDoc(note = "是否完成")
    private String isComplete;
    
    /**
     * 答题时间
     */
    @EntityDoc(note = "答题时间(秒)")
    private double spendTime;
    
    
    /**
     * 邮箱
     */
    @EntityDoc(note = "邮箱")
    private String email;
    
    /**
     * 年龄
     */
    @EntityDoc(note = "年龄")
    private int age;

    
    /**
     * 公司
     */
    @EntityDoc(note = "公司名称")
    private String companyName;
    
    
    /**
     * 职务
     */
    @EntityDoc(note = "职务")
    private String position;
    
}
