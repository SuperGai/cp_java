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

import com.hh.appraisal.springboot.bean.DivisorAverageBean;
import com.hh.appraisal.springboot.bean.QuestionAllBean;
import com.hh.appraisal.springboot.entity.EvaluatoionCode;
import com.hh.appraisal.springboot.entity.UserAnswers;

/**
 * Mapper
 * 
 * @author gaigai
 * @date 2021/06/26
 */
@Repository
public interface UserAnswersMapper extends BaseMapper<UserAnswers> {

	@Select("SELECT USER_ANSWERS.CODE,USER_ANSWERS.CREATE_TIME,USER_ANSWERS.QUESTION_NO, p.product_name as PRODUCT_CODE,"
			+ "USER_ANSWERS.IS_COMPLETE, S.EVALUATOION_CODE AS EVALUATION_USER_CODE, "
			+ "Q.QUESTION_CODE AS QUESTION_CODE, T.NAME AS QUESTION_OPTIONS_CODE,USER_ANSWERS.SPEND_TIME "
			+ "FROM USER_ANSWERS USER_ANSWERS LEFT JOIN EVALUATOION_CODE S ON "
			+ "USER_ANSWERS.EVALUATION_USER_CODE = S.EVALUATOION_CODE LEFT JOIN QUESTION Q ON USER_ANSWERS.QUESTION_CODE = Q.CODE LEFT "
			+ " JOIN PRODUCT P ON USER_ANSWERS.PRODUCT_CODE = P.CODE LEFT JOIN QUESTION_OPTIONS T ON USER_ANSWERS.QUESTION_OPTIONS_CODE = T.CODE "
			+ " ${ew.customSqlSegment}  ORDER BY s.create_time, user_answers.CREATE_TIME DESC")
	Page<UserAnswers> selectPageAll(Page<T> page, @Param(Constants.WRAPPER) Wrapper<UserAnswers> queryWrapper);

	@Select("SELECT USER_ANSWERS.CODE,USER_ANSWERS.CREATE_TIME,USER_ANSWERS.QUESTION_NO,p.product_name as PRODUCT_CODE,"
			+ "USER_ANSWERS.IS_COMPLETE, S.EVALUATOION_CODE AS EVALUATION_USER_CODE, "
			+ "Q.QUESTION_CODE AS QUESTION_CODE, T.NAME AS QUESTION_OPTIONS_CODE,USER_ANSWERS.SPEND_TIME "
			+ "FROM USER_ANSWERS USER_ANSWERS LEFT JOIN EVALUATOION_CODE S ON "
			+ "USER_ANSWERS.EVALUATION_USER_CODE = S.EVALUATOION_CODE LEFT JOIN QUESTION Q ON USER_ANSWERS.QUESTION_CODE = Q.CODE LEFT JOIN PRODUCT P ON USER_ANSWERS.PRODUCT_CODE = P.CODE LEFT JOIN QUESTION_OPTIONS T ON USER_ANSWERS.QUESTION_OPTIONS_CODE = T.CODE "
			+ " ${ew.customSqlSegment} ORDER BY s.create_time, user_answers.CREATE_TIME DESC")
	List<UserAnswers> selectListAll(@Param(Constants.WRAPPER) Wrapper<UserAnswers> queryWrapper);

	@Select("SELECT USER_ANSWERS.QUESTION_NO," + "	Q.CODE AS QUESTION_CODE,"
			+ "	Q.QUESTION_ZH,Q.QUESTION_EH,USER_ANSWERS.CODE USERANSWER_CODE,Q.QUESTION_TYPE FROM " + "	USER_ANSWERS USER_ANSWERS"
			+ "	LEFT JOIN EVALUATOION_CODE S ON USER_ANSWERS.EVALUATION_USER_CODE = S.CODE "
			+ "	LEFT JOIN QUESTION Q ON USER_ANSWERS.QUESTION_CODE = Q.CODE "
			+ "	LEFT JOIN PRODUCT P ON USER_ANSWERS.PRODUCT_CODE = P.CODE "
			+ "	LEFT JOIN QUESTION_OPTIONS T ON USER_ANSWERS.QUESTION_OPTIONS_CODE = T.CODE "
			+ " WHERE USER_ANSWERS.QUESTION_NO =#{QUESTION_NO} AND USER_ANSWERS.EVALUATION_USER_CODE=#{EVALUATOION_CODE} AND USER_ANSWERS.PRODUCT_CODE=#{PRODUCT_CODE}  ")
	QuestionAllBean findQuestion(@Param("QUESTION_NO") Integer QUESTION_NO,
			@Param("EVALUATOION_CODE") String EVALUATOION_CODE,@Param("PRODUCT_CODE") String PRODUCT_CODE);

	@Select("SELECT DIVISOR_ITEM.*,TRUNCATE(DIVISOR_AVERAG,2) AS DIVISOR_AVERAG,DIVISOR_NAME,DIVISOR_DESC FROM DIVISOR_ITEM,(SELECT"
			+ "		SUM( QUESTION_OPTIONS.DIVISOR_VALUE ) / COUNT( * ) AS DIVISOR_AVERAG," + "		DIVISOR.CODE,"
			+ "		DIVISOR.DIVISOR_NAME,DIVISOR.DIVISOR_DESC  FROM USER_ANSWERS,QUESTION_OPTIONS,QUESTION,"
			+ "		DIVISOR  WHERE USER_ANSWERS.QUESTION_CODE = QUESTION.CODE AND USER_ANSWERS.QUESTION_OPTIONS_CODE = QUESTION_OPTIONS.CODE "
			+ "		AND QUESTION.DIVISOR_CODE = DIVISOR.`CODE` "
			+ "		AND USER_ANSWERS.EVALUATION_USER_CODE = #{EVALUATOION_CODE} "
			+ "		AND USER_ANSWERS.IS_COMPLETE='Y' GROUP BY DIVISOR.CODE,DIVISOR.DIVISOR_NAME,DIVISOR.DIVISOR_DESC ) AS ALLINFO "
			+ " WHERE DIVISOR_ITEM.DIVISOR_CODE = ALLINFO.CODE AND ALLINFO.DIVISOR_AVERAG >= DIVISOR_ITEM.VALUE_START "
			+ "	AND ALLINFO.DIVISOR_AVERAG < DIVISOR_ITEM.VALUE_END ORDER BY DIVISOR_ITEM.DIVISOR_CODE ASC")
	List<DivisorAverageBean> getDivisorListInfo(@Param("EVALUATOION_CODE") String EVALUATOION_CODE);

	@Select("SELECT IFNULL( MIN(QUESTION_NO),0 ) from USER_ANSWERS where EVALUATION_USER_CODE=#{EVALUATOION_CODE} AND PRODUCT_CODE=#{PRODUCT_CODE}  AND IS_COMPLETE='N' ")
	int getMinUnCompleteNo(@Param("EVALUATOION_CODE") String EVALUATOION_CODE,@Param("PRODUCT_CODE") String PRODUCT_CODE);

	@Select("SELECT CAST(SUM(SPEND_TIME) AS DECIMAL(15,2)) from USER_ANSWERS where EVALUATION_USER_CODE=#{EVALUATOION_CODE}  AND IS_COMPLETE='Y' ")
	long getAllProductSpendTime(@Param("EVALUATOION_CODE") String EVALUATOION_CODE);
	
	@Select("SELECT CAST(SUM(SPEND_TIME) AS DECIMAL(15,2)) from USER_ANSWERS where EVALUATION_USER_CODE=#{EVALUATOION_CODE} AND PRODUCT_CODE=#{PRODUCT_CODE}  AND IS_COMPLETE='Y' ")
	long getSpendTime(@Param("EVALUATOION_CODE") String EVALUATOION_CODE,@Param("PRODUCT_CODE") String PRODUCT_CODE);
	
}
