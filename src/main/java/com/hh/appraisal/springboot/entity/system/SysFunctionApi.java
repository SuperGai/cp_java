package com.hh.appraisal.springboot.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hh.appraisal.springboot.core.annotation.EntityDoc;
import com.hh.appraisal.springboot.core.entity.BasicEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统功能与接口url对应表
 * @author gaigai
 * @date 2019/05/15
 */
@EntityDoc(note = "系统功能与接口url对应", isClass = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_function_api")
public class SysFunctionApi extends BasicEntity {

    /**
     * 系统功能 唯一标识
     */
    @EntityDoc(note = "系统功能code")
    private String functionCode;

    /**
     * 系统接口url 唯一标识
     */
    @EntityDoc(note = "系统接口code")
    private String apiCode;

}
