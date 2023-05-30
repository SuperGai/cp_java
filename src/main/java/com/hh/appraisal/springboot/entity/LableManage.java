package com.hh.appraisal.springboot.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hh.appraisal.springboot.core.annotation.EntityDoc;
import com.hh.appraisal.springboot.core.entity.BasicEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 标签管理
 * 
 * @author gaigai
 * @date 2021/06/29
 */
@EntityDoc(isClass = true, note = "标签管理")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("lable_manage")
public class LableManage extends BasicEntity {

	/**
	 * 字段
	 */
	@EntityDoc(note = "分类")
	private String cat_level;
	
	/**
	 * 字段
	 */
	@EntityDoc(note = "分类名称")
	private String cat_name;
	

	

}
