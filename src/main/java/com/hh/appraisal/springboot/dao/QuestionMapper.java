package com.hh.appraisal.springboot.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Repository;

import com.hh.appraisal.springboot.entity.EvaluatoionCode;
import com.hh.appraisal.springboot.entity.Question;

/**
 * Mapper
 * @author gaigai
 * @date 2021/06/26
 */
@Repository
public interface QuestionMapper extends BaseMapper<Question> {
	
	
	@Select("SELECT"
			+ "  QUESTION.CODE,"
			+ "  QUESTION.CREATE_TIME,"
			+ "  S.DIVISOR_NAME AS DIVISORCODE,"
			+ "  P.PRODUCT_NAME AS QUESTIONBANK,"
			+ "  QUESTION.QUESTION_EH,"
			+ "  QUESTION.QUESTION_ZH,"
			+ "  QUESTION.UPDATE_TIME,"
			+ "  QUESTION.QUESTION_CODE,"
			+ "  QUESTION.VALID,QUESTION.QUESTION_TYPE  "
			+ "FROM"
			+ "	QUESTION"
			+ "	LEFT JOIN DIVISOR S ON QUESTION.DIVISOR_CODE=S.CODE"
			+ "  LEFT JOIN PRODUCT P ON QUESTION.QUESTION_BANK=P.CODE "
			+ " ${ew.customSqlSegment} ORDER BY P.PRODUCT_NAME,QUESTION.QUESTION_CODE ASC ")
	Page<Question> selectPageAll(Page<T> page, @Param(Constants.WRAPPER) Wrapper<Question> queryWrapper);
	
	
	@Select("SELECT"
			+ "  QUESTION.CODE,"
			+ "  QUESTION.CREATE_TIME,"
			+ "  S.DIVISOR_NAME AS DIVISORCODE,"
			+ "  P.PRODUCT_NAME AS QUESTIONBANK,"
			+ "  QUESTION.QUESTION_EH,"
			+ "  QUESTION.QUESTION_ZH,"
			+ "  QUESTION.UPDATE_TIME,"
			+ "  QUESTION.QUESTION_CODE,"
			+ "  QUESTION.VALID,QUESTION.QUESTION_TYPE  "
			+ "FROM"
			+ "	QUESTION"
			+ "	LEFT JOIN DIVISOR S ON QUESTION.DIVISOR_CODE=S.CODE"
			+ "  LEFT JOIN PRODUCT P ON QUESTION.QUESTION_BANK=P.CODE"
			+ " ${ew.customSqlSegment} ORDER BY P.PRODUCT_NAME,QUESTION.QUESTION_CODE ASC ")
    List<Question> selectListAll(@Param(Constants.WRAPPER) Wrapper<Question> queryWrapper);
	
}
