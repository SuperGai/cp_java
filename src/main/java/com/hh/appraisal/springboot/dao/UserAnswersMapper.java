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
import com.hh.appraisal.springboot.bean.DivisorCatBean;
import com.hh.appraisal.springboot.bean.DivisorCatItemBean;
import com.hh.appraisal.springboot.bean.QuestionAllBean;
import com.hh.appraisal.springboot.bean.ReportDivisorInfoCatBean;
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
			+ "Q.QUESTION_CODE AS QUESTION_CODE, T.NAME AS QUESTION_OPTIONS_CODE,USER_ANSWERS.SPEND_TIME,USER_ANSWERS.`VALUE` "
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
			+ " WHERE USER_ANSWERS.QUESTION_NO =#{QUESTION_NO} AND USER_ANSWERS.EVALUATION_USER_CODE=#{EVALUATOION_CODE} AND USER_ANSWERS.PRODUCT_CODE=#{PRODUCT_CODE}  ORDER BY T.`NAME` ASC ")
	QuestionAllBean findQuestion(@Param("QUESTION_NO") Integer QUESTION_NO,
			@Param("EVALUATOION_CODE") String EVALUATOION_CODE,@Param("PRODUCT_CODE") String PRODUCT_CODE);
	
	
	@Select("SELECT USER_ANSWERS.QUESTION_NO," + "	Q.CODE AS QUESTION_CODE,"
			+ "	Q.QUESTION_ZH,Q.QUESTION_EH,USER_ANSWERS.CODE USERANSWER_CODE,Q.QUESTION_TYPE FROM " + "	USER_ANSWERS USER_ANSWERS"
			+ "	LEFT JOIN EVALUATOION_CODE S ON USER_ANSWERS.EVALUATION_USER_CODE = S.CODE "
			+ "	LEFT JOIN QUESTION Q ON USER_ANSWERS.QUESTION_CODE = Q.CODE "
			+ "	LEFT JOIN PRODUCT P ON USER_ANSWERS.PRODUCT_CODE = P.CODE "
			+ "	LEFT JOIN QUESTION_OPTIONS T ON USER_ANSWERS.QUESTION_OPTIONS_CODE = T.CODE "
			+ " WHERE USER_ANSWERS.QUESTION_NO =#{QUESTION_NO} AND USER_ANSWERS.EVALUATION_USER_CODE=#{EVALUATOION_CODE} AND USER_ANSWERS.PRODUCT_CODE_REAL=#{PRODUCT_CODE}  ORDER BY T.`NAME` ASC ")
	QuestionAllBean findYsxQuestion(@Param("QUESTION_NO") Integer QUESTION_NO,
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

	@Select("SELECT IFNULL( MIN(QUESTION_NO),0 ) from USER_ANSWERS where EVALUATION_USER_CODE=#{EVALUATOION_CODE} AND PRODUCT_CODE_REAL=#{PRODUCT_CODE}  AND IS_COMPLETE='N' ")
	int getMinUnCompleteNoYsx(@Param("EVALUATOION_CODE") String EVALUATOION_CODE,@Param("PRODUCT_CODE") String PRODUCT_CODE);

	@Select("SELECT CAST(SUM(SPEND_TIME) AS DECIMAL(15,2)) from USER_ANSWERS where EVALUATION_USER_CODE=#{EVALUATOION_CODE}  AND IS_COMPLETE='Y' ")
	long getAllProductSpendTime(@Param("EVALUATOION_CODE") String EVALUATOION_CODE);
	
	@Select("SELECT CAST(SUM(SPEND_TIME) AS DECIMAL(15,2)) from USER_ANSWERS where EVALUATION_USER_CODE=#{EVALUATOION_CODE} AND PRODUCT_CODE=#{PRODUCT_CODE}  AND IS_COMPLETE='Y' ")
	long getSpendTime(@Param("EVALUATOION_CODE") String EVALUATOION_CODE,@Param("PRODUCT_CODE") String PRODUCT_CODE);

	@Select("SELECT	SUM(PRODUCT.ANSWER_TIME) FROM EVALUATOION_ORDER,PRODUCT WHERE EVALUATOION_ORDER.EVALUATOION_CODE = #{EVALUATOION_CODE} AND EVALUATOION_ORDER.PRODUCT_CODE = PRODUCT.`CODE`")
	long getProductTime(@Param("EVALUATOION_CODE") String EVALUATOION_CODE);
	
	@Select("SELECT SUM(PRODUCT.QUESTION_NUM) FROM EVALUATOION_ORDER,PRODUCT WHERE EVALUATOION_ORDER.EVALUATOION_CODE = #{EVALUATOION_CODE} AND EVALUATOION_ORDER.PRODUCT_CODE = PRODUCT.`CODE`")
	long getOrderAllQuestionNumExceptYsx(@Param("EVALUATOION_CODE") String EVALUATOION_CODE);

	@Select("SELECT COUNT( U.`CODE` ) FROM EVALUATOION_ORDER, PRODUCT,USER_ANSWERS U WHERE EVALUATOION_ORDER.EVALUATOION_CODE = #{EVALUATOION_CODE} AND EVALUATOION_ORDER.PRODUCT_CODE = PRODUCT.`CODE` AND U.PRODUCT_CODE=PRODUCT.`CODE` AND U.EVALUATION_USER_CODE=EVALUATOION_ORDER.EVALUATOION_CODE  AND U.IS_COMPLETE='Y'")
    long getCompleteQuestionNumExceptYsx(@Param("EVALUATOION_CODE") String EVALUATOION_CODE);

	@Select("SELECT   SUM(P.DIVISOR_VALUE) FROM EVALUATOION_ORDER, PRODUCT,USER_ANSWERS U,QUESTION_OPTIONS P"
			+ " WHERE EVALUATOION_ORDER.EVALUATOION_CODE = #{EVALUATOION_CODE} AND "
			+ " EVALUATOION_ORDER.PRODUCT_CODE = PRODUCT.`CODE` AND U.PRODUCT_CODE=PRODUCT.`CODE` AND U.EVALUATION_USER_CODE=EVALUATOION_ORDER.EVALUATOION_CODE AND PRODUCT.PRODUCT_NAME = '掩饰性'"
			+ "	AND U.IS_COMPLETE='Y' AND U.QUESTION_OPTIONS_CODE=P.`CODE` ")
	 long getYsxScore(@Param("EVALUATOION_CODE") String EVALUATOION_CODE);

	@Select("SELECT MAX( CASE WHEN SUBSTRING_INDEX(I.`SCORES`, '-', 1) <= #{score} AND #{score} <= SUBSTRING_INDEX(I.SCORES, '-', -1)THEN I.`LEVEL` ELSE 0 END ) AS ILEVEL FROM NORM_MANAGE N,NORM_MANAGE_ITEM I WHERE N.`NAME`= #{DIVISOR_NAME} AND N.`CODE`=I.NORM_CODE")
	long getDivisorNormLevel(@Param("EVALUATOION_CODE") String EVALUATOION_CODE,@Param("score") long score,@Param("DIVISOR_NAME") String DIVISOR_NAME);
	
	@Select("SELECT  I.CONTENT_ZH FROM DIVISOR N,DIVISOR_ITEM I WHERE N.DIVISOR_NAME='掩饰性' AND N.`CODE`=I.DIVISOR_CODE AND I.VALUE_START <=  #{score} AND  #{score} <= I.VALUE_END ")
	String getYsxDesc(@Param("EVALUATOION_CODE") String EVALUATOION_CODE,@Param("score") long score);

	
//	@Select("SELECT SUM(QUESTION_OPTIONS.DIVISOR_VALUE) AS DIVISOR_SCORE DIVISOR.DIVISOR_DESC  "
//			+ "FROM USER_ANSWERS JOIN QUESTION ON USER_ANSWERS.QUESTION_CODE = QUESTION.CODE "
//			+ "JOIN QUESTION_OPTIONS ON CASE WHEN QUESTION.question_type = 'INPUT' THEN USER_ANSWERS.`value` = QUESTION_OPTIONS.value_zh END "
//			+ "JOIN DIVISOR ON QUESTION.DIVISOR_CODE = DIVISOR.`CODE` WHERE "
//			+ "    USER_ANSWERS.EVALUATION_USER_CODE = #{EVALUATOION_CODE} AND USER_ANSWERS.IS_COMPLETE = 'Y' "
//			+ "		AND DIVISOR.divisor_name='逻辑推理' GROUP BY DIVISOR.CODE, DIVISOR.DIVISOR_NAME, DIVISOR.DIVISOR_DESC "
//			+ "")
//	long getLgtlScore(@Param("EVALUATOION_CODE") String EVALUATOION_CODE);
//	
	
	@Select("SELECT"
			+ "	SUM( QUESTION_OPTIONS.DIVISOR_VALUE ) AS cat_Value,"
			+ "	DIVISOR.divisor_cat AS cat_Name "
			+ " FROM USER_ANSWERS"
			+ "	JOIN QUESTION ON USER_ANSWERS.QUESTION_CODE = QUESTION."
			+ "	CODE JOIN QUESTION_OPTIONS ON"
			+ " CASE WHEN QUESTION.question_type = 'INPUT' THEN"
			+ "		USER_ANSWERS.`value` = QUESTION_OPTIONS.value_zh ELSE USER_ANSWERS.QUESTION_OPTIONS_CODE = QUESTION_OPTIONS."
			+ "	CODE  END JOIN DIVISOR ON QUESTION.DIVISOR_CODE = DIVISOR.`CODE` WHERE"
			+ "	USER_ANSWERS.IS_COMPLETE = 'Y' "
			+ "	AND USER_ANSWERS.evaluation_user_code = #{EVALUATOION_CODE} GROUP BY "
			+ "	DIVISOR.divisor_cat")
	List<DivisorCatBean> getDivisorCatScoreInfo(@Param("EVALUATOION_CODE") String EVALUATOION_CODE);
	
//	
//	@Select("SELECT"
//			+ "	SUM( QUESTION_OPTIONS.DIVISOR_VALUE ) AS \"value\","
//			+ "	DIVISOR.divisor_name as \"key\""
//			+ "FROM"
//			+ "	USER_ANSWERS"
//			+ "	JOIN QUESTION ON USER_ANSWERS.QUESTION_CODE = QUESTION."
//			+ "	CODE JOIN QUESTION_OPTIONS ON"
//			+ "CASE"
//			+ "		WHEN QUESTION.question_type = 'INPUT' THEN"
//			+ "		USER_ANSWERS.`value` = QUESTION_OPTIONS.value_zh ELSE USER_ANSWERS.QUESTION_OPTIONS_CODE = QUESTION_OPTIONS."
//			+ "	CODE "
//			+ "	END JOIN DIVISOR ON QUESTION.DIVISOR_CODE = DIVISOR.`CODE` "
//			+ "WHERE"
//			+ "	USER_ANSWERS.IS_COMPLETE = 'Y' "
//			+ "	AND USER_ANSWERS.evaluation_user_code = #{EVALUATOION_CODE}  and DIVISOR.divisor_cat=#{DIVISOR_CAT_NAME}  "
//			+ "GROUP BY"
//			+ "	DIVISOR.divisor_name")
//	List<DivisorCatItemBean> getDivisorInfo(@Param("EVALUATOION_CODE") String EVALUATOION_CODE,@Param("DIVISOR_CAT_NAME") String DIVISOR_CAT_NAME);
//	
	
	@Select("SELECT\n"
			+ "	qo.divisor_value AS \n"
			+ "VALUE\n"
			+ "	,\n"
			+ "	d.divisor_cat,d.norm_code,\n"
			+ "	d.divisor_name \n"
			+ "FROM\n"
			+ "	user_answers u,\n"
			+ "	question q,\n"
			+ "	question_options qo,\n"
			+ "	divisor d \n"
			+ "WHERE\n"
			+ "	u.question_code = q.`code` \n"
			+ "	AND q.`code` = qo.question_code \n"
			+ "	AND q.divisor_code = d.`code` \n"
			+ "	AND u.question_options_code = qo.`code` \n"
			+ "	AND q.question_type = 'SELECT' \n"
			+ "	AND u.evaluation_user_code = #{EVALUATOION_CODE} \n"
			+ "	AND u.is_complete = 'Y' \n"
			+ "	AND q.valid = 1 \n"
			+ "	AND qo.valid = 1 \n"
			+ "	AND d.valid = 1 UNION ALL\n"
			+ "SELECT\n"
			+ "CASE\n"
			+ "		\n"
			+ "	WHEN\n"
			+ "		u.`value` = qo.value_zh THEN\n"
			+ "			1 ELSE 0 \n"
			+ "		END AS VALUE,d.divisor_cat,d.norm_code,d.divisor_name \n"
			+ "	FROM\n"
			+ "		user_answers u\n"
			+ "		INNER JOIN question q ON u.question_code = q.`code`\n"
			+ "		INNER JOIN question_options qo ON q.`code` = qo.question_code\n"
			+ "		INNER JOIN divisor d ON q.divisor_code = d.`code` \n"
			+ "	WHERE\n"
			+ "		q.question_type = 'INPUT' \n"
			+ "		AND u.evaluation_user_code = #{EVALUATOION_CODE} \n"
			+ "		AND q.valid = 1 \n"
			+ "		AND qo.valid = 1 \n"
			+ "		AND d.valid = 1\n"
			+ "	UNION ALL\n"
			+ "SELECT\n"
			+ "	col2 AS \n"
			+ "VALUE ,\n"
			+ "	n.divisor_cat,\n"
			+ "	n.norm_code,\n"
			+ "	n.divisor_name \n"
			+ "FROM\n"
			+ "	(\n"
			+ "	SELECT\n"
			+ "		SUBSTRING_INDEX( SUBSTRING_INDEX( bb.`value`, '&', 1 ), ':', 1 ) AS col1,\n"
			+ "		SUBSTRING_INDEX( SUBSTRING_INDEX( bb.`value`, '&', 1 ), ':', - 1 ) AS col2 \n"
			+ "	FROM\n"
			+ "		(\n"
			+ "		SELECT DISTINCT\n"
			+ "			u.`value`,\n"
			+ "			q.question_code,\n"
			+ "			d.divisor_cat,\n"
			+ "			d.norm_code \n"
			+ "		FROM\n"
			+ "			user_answers u,\n"
			+ "			question q,\n"
			+ "			question_options qo,\n"
			+ "			divisor d \n"
			+ "		WHERE\n"
			+ "			u.question_code = q.`code` \n"
			+ "			AND q.`code` = qo.question_code \n"
			+ "			AND q.question_type = 'OPTION_INPUT' \n"
			+ "			AND qo.divisor_code = d.`code` \n"
			+ "			AND u.evaluation_user_code = #{EVALUATOION_CODE} \n"
			+ "			AND q.valid = 1 \n"
			+ "			AND qo.valid = 1 \n"
			+ "			AND d.valid = 1 \n"
			+ "		ORDER BY\n"
			+ "			q.question_code \n"
			+ "		) AS bb \n"
			+ "	UNION ALL\n"
			+ "	SELECT\n"
			+ "		SUBSTRING_INDEX( SUBSTRING_INDEX( bb.`value`, '&', -1 ), ':', 1 ) AS col1,\n"
			+ "		SUBSTRING_INDEX( SUBSTRING_INDEX( bb.`value`, '&', -1 ), ':', - 1 ) AS col2\n"
			+ "	FROM\n"
			+ "		(\n"
			+ "		SELECT DISTINCT\n"
			+ "			u.`value`,\n"
			+ "			q.question_code \n"
			+ "		FROM\n"
			+ "			user_answers u,\n"
			+ "			question q,\n"
			+ "			question_options qo,\n"
			+ "			divisor d \n"
			+ "		WHERE\n"
			+ "			u.question_code = q.`code` \n"
			+ "			AND q.`code` = qo.question_code \n"
			+ "			AND q.question_type = 'OPTION_INPUT' \n"
			+ "			AND qo.divisor_code = d.`code` \n"
			+ "			AND u.evaluation_user_code = #{EVALUATOION_CODE} \n"
			+ "			AND q.valid = 1 \n"
			+ "			AND qo.valid = 1 \n"
			+ "			AND d.valid = 1 \n"
			+ "		ORDER BY\n"
			+ "			q.question_code \n"
			+ "		) AS bb \n"
			+ "	) AS cc,\n"
			+ "	question_options p,\n"
			+ "	divisor n \n"
			+ "WHERE\n"
			+ "	cc.col1 = p.`code` \n"
			+ "	AND p.divisor_code = n.`code`")
	List<ReportDivisorInfoCatBean> getDivisorInfo(@Param("EVALUATOION_CODE") String EVALUATOION_CODE);
	
	@Select("SELECT"
			+ "	count(u.code) "
			+ "FROM"
			+ "	user_answers u,"
			+ "	divisor d,"
			+ "	question q "
			+ " WHERE"
			+ "	u.question_code = q.`code` "
			+ "	AND q.divisor_code = d.`code` "
			+ "	AND d.divisor_name = #{DIVISOR_NAME} "
			+ "	and u.evaluation_user_code=#{EVALUATOION_CODE} ")
	int getQuestionCount(@Param("EVALUATOION_CODE") String EVALUATOION_CODE,@Param("DIVISOR_NAME") String DIVISOR_NAME);
	
	
	@Select("SELECT"
			+ "	count(u.code) "
			+ "FROM"
			+ "	user_answers u,"
			+ "	divisor d,"
			+ "	question q ,question_options p WHERE"
			+ "	u.question_code = q.`code` "
			+ "	and q.`code`=p.question_code"
			+ "	and d.`code`=p.divisor_code AND d.divisor_name= #{DIVISOR_NAME} and u.evaluation_user_code=#{EVALUATOION_CODE} ")
	int getQuestionOptionCount(@Param("EVALUATOION_CODE") String EVALUATOION_CODE,@Param("DIVISOR_NAME") String DIVISOR_NAME);
	
}
