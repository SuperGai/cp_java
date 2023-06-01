package com.hh.appraisal.springboot.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hh.appraisal.springboot.core.annotation.EntityDoc;
import com.hh.appraisal.springboot.core.entity.BasicEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 测评码
 * @author gaigai
 * @date 2021/06/29
 */
@EntityDoc(isClass = true, note = "测评码")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("evaluatoion_code")
public class EvaluatoionCode extends BasicEntity {

//	ID	INTEGER	
//	SCHOOL_ID	INTEGER	学校信息表ID
//	CODE	VARCHAR	测评码
//	STAT_DATE	DATE	开始日期
//	END_DATE	DATE	结束日期
//	OWNERID	INTEGER	创建人ID
//	MODIFIERID	INTEGER	修改人ID
//	CREATIONDATE	DATETIME	创建时间
//	MODIFIEDDATE	DATETIME	修改时间
//	ISUSED	CHAR	是否被使用
//	ISACTIVE	CHAR	是否可用
	
//    /**
//     * 产品Code
//     */
//    @EntityDoc(note = "产品Code")
//    private String productCode;
	
    /**
     * 学校Code
     */
    @EntityDoc(note = "学校Code")
    private String shoolCode;
    
    /**
     * 人员类型
     */
    @EntityDoc(note = "人员类型")
    private String personType;

    /**
     * 测评码
     */
    @EntityDoc(note = "测评码")
    private String evaluatoionCode;
    
    /**
     * 开始日期
     */
    @EntityDoc(note = "开始日期")
    private String startDate;
    
    /**
     * 结束日期
     */
    @EntityDoc(note = "结束日期")
    private String endDate;
    
    /**
     * 是否被使用
     */
    @EntityDoc(note = "是否被使用")
    private String isused;
    
    

}
