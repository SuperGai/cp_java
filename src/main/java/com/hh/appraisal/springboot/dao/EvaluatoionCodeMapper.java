package com.hh.appraisal.springboot.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Repository;
import com.hh.appraisal.springboot.entity.EvaluatoionCode;

/**
 * Mapper
 * @author gaigai
 * @date 2021/06/26
 */
@Repository
public interface EvaluatoionCodeMapper extends BaseMapper<EvaluatoionCode> {
	
	@Select("SELECT  P.PRODUCT_NAME	AS PRODUCT_CODE,EVALUATOION_CODE.PERSON_TYPE,EVALUATOION_CODE.PRODUCT_CODE,EVALUATOION_CODE.CODE,EVALUATOION_CODE.VALID,EVALUATOION_CODE.UPDATE_TIME,EVALUATOION_CODE.START_DATE,EVALUATOION_CODE.ISUSED,	EVALUATOION_CODE.EVALUATOION_CODE,EVALUATOION_CODE.END_DATE,"
			+ "	EVALUATOION_CODE.CREATE_TIME,	S.SCHOOL_NAME AS SHOOL_CODE FROM EVALUATOION_CODE LEFT JOIN SCHOOL S ON EVALUATOION_CODE.SHOOL_CODE = S.CODE  "
			+ " LEFT JOIN PRODUCT P ON EVALUATOION_CODE.PRODUCT_CODE=P.CODE"
			+ " ${ew.customSqlSegment} ORDER BY EVALUATOION_CODE.CREATE_TIME DESC")
	Page<EvaluatoionCode> selectPageAll(Page<T> page, @Param(Constants.WRAPPER) Wrapper<EvaluatoionCode> queryWrapper);
	
	@Select("SELECT  P.PRODUCT_NAME	AS PRODUCT_CODE,EVALUATOION_CODE.PERSON_TYPE,EVALUATOION_CODE.PRODUCT_CODE,EVALUATOION_CODE.CODE,EVALUATOION_CODE.VALID,EVALUATOION_CODE.UPDATE_TIME,EVALUATOION_CODE.START_DATE,EVALUATOION_CODE.ISUSED,	EVALUATOION_CODE.EVALUATOION_CODE,EVALUATOION_CODE.END_DATE,"
			+ "	EVALUATOION_CODE.CREATE_TIME,	S.SCHOOL_NAME AS SHOOL_CODE FROM EVALUATOION_CODE LEFT JOIN SCHOOL S ON EVALUATOION_CODE.SHOOL_CODE = S.CODE  "
			+ " LEFT JOIN PRODUCT P ON EVALUATOION_CODE.PRODUCT_CODE=P.CODE"
			+ " ${ew.customSqlSegment} ORDER BY EVALUATOION_CODE.CREATE_TIME DESC")
    List<EvaluatoionCode> selectListAll(@Param(Constants.WRAPPER) Wrapper<EvaluatoionCode> queryWrapper);
}
