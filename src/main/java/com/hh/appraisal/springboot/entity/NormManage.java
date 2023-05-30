package com.hh.appraisal.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hh.appraisal.springboot.core.annotation.EntityDoc;
import com.hh.appraisal.springboot.core.entity.BasicEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 常模管理
 * 
 * @author gaigai
 * @date 2023/04/26
 */
@EntityDoc(isClass = true, note = "常模管理")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("norm_manage")
public class NormManage extends BasicEntity{

	/**
	 * 常模名称
	 */
	@EntityDoc(note = "常模名称")
	private String name;

	/**
	 * 常模描述
	 */
	@EntityDoc(note = "常模描述")
	private String ndesc;
	
}
