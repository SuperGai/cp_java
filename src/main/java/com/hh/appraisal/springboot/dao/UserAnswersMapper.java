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


	@Select("SELECT  I.CONTENT_ZH FROM DIVISOR N,DIVISOR_ITEM I WHERE N.DIVISOR_NAME=#{divisiorName} AND N.`CODE`=I.DIVISOR_CODE AND  #{score}>= I.VALUE_START  AND  #{score} < I.VALUE_END ")
	String getDivisionDesc(@Param("divisiorName") String divisiorName,@Param("score") long score);
	
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
	
	@Select("SELECT\n" +
			"\t* \n" +
			"FROM\n" +
			"\t(\n" +
			"\tSELECT\n" +
			"\t\tqo.divisor_value AS \n" +
			"\tVALUE\n" +
			"\t\t,\n" +
			"\t\td.divisor_cat,\n" +
			"\t\td.norm_code,\n" +
			"\t\td.divisor_name,\n" +
			"\t\td.orderno \n" +
			"\tFROM\n" +
			"\t\tuser_answers u,\n" +
			"\t\tquestion q,\n" +
			"\t\tquestion_options qo,\n" +
			"\t\tdivisor d \n" +
			"\tWHERE\n" +
			"\t\tu.question_code = q.`code` \n" +
			"\t\tAND q.`code` = qo.question_code \n" +
			"\t\tAND q.divisor_code = d.`code` \n" +
			"\t\tAND u.question_options_code = qo.`code` \n" +
			"\t\tAND q.question_type = 'SELECT' \n" +
			"\t\tAND u.evaluation_user_code = #{EVALUATOION_CODE}\n" +
			"\t\tAND u.is_complete = 'Y' \n" +
			"\t\tAND q.valid = 1 \n" +
			"\t\tAND qo.valid = 1 \n" +
			"\t\tAND d.valid = 1 UNION ALL\n" +
			"\tSELECT\n" +
			"\tCASE\n" +
			"\t\t\t\n" +
			"\t\tWHEN\n" +
			"\t\t\tu.`value` = qo.value_zh THEN\n" +
			"\t\t\t\t1 ELSE 0 \n" +
			"\t\t\tEND AS \n" +
			"\t\tVALUE\n" +
			"\t\t\t,\n" +
			"\t\t\td.divisor_cat,\n" +
			"\t\t\td.norm_code,\n" +
			"\t\t\td.divisor_name,\n" +
			"\t\t\td.orderno \n" +
			"\t\tFROM\n" +
			"\t\t\tuser_answers u\n" +
			"\t\t\tINNER JOIN question q ON u.question_code = q.`code`\n" +
			"\t\t\tINNER JOIN question_options qo ON q.`code` = qo.question_code\n" +
			"\t\t\tINNER JOIN divisor d ON q.divisor_code = d.`code` \n" +
			"\t\tWHERE\n" +
			"\t\t\tq.question_type = 'INPUT' \n" +
			"\t\t\tAND u.evaluation_user_code = #{EVALUATOION_CODE}\n" +
			"\t\t\tAND q.valid = 1 \n" +
			"\t\t\tAND qo.valid = 1 \n" +
			"\t\t\tAND d.valid = 1 UNION ALL\n" +
			"\t\t\t\n" +
			"\t\t\tselect value,divisor_cat,norm_code,divisor_name,orderno from (\n" +
			"\t\t\tSELECT \n" +
			"\t\t\t DISTINCT\n" +
			"\t\t\tcol2 AS \n" +
			"\t\tVALUE\n" +
			"\t\t\t,\n" +
			"\t\t\tn.divisor_cat,\n" +
			"\t\t\tn.norm_code,\n" +
			"\t\t\tn.divisor_name,\n" +
			"\t\t\tn.orderno ,question_code\n" +
			"\t\tFROM\n" +
			"\t\t\t(SELECT\n" +
			"\tSUBSTRING_INDEX( SUBSTRING_INDEX( bb.`value`, '&', 1 ), ':', 1 ) AS col1,\n" +
			"\tSUBSTRING_INDEX( SUBSTRING_INDEX( bb.`value`, '&', 1 ), ':', - 1 ) AS col2 \n" +
			"FROM\n" +
			"\t(\n" +
			"\tSELECT DISTINCT\n" +
			"\t\tu.`value`,\n" +
			"\t\tq.question_code,\n" +
			"\t\td.divisor_cat,\n" +
			"\t\td.norm_code,\n" +
			"\t\td.orderno \n" +
			"\tFROM\n" +
			"\t\tuser_answers u,\n" +
			"\t\tquestion q,\n" +
			"\t\tquestion_options qo,\n" +
			"\t\tdivisor d \n" +
			"\tWHERE\n" +
			"\t\tu.question_code = q.`code` \n" +
			"\t\tAND q.`code` = qo.question_code \n" +
			"\t\tAND q.question_type = 'OPTION_INPUT' \n" +
			"\t\tAND qo.divisor_code = d.`code` \n" +
			"\t\tAND u.evaluation_user_code = #{EVALUATOION_CODE} \n" +
			"\t\tAND q.valid = 1 \n" +
			"\t\tAND qo.valid = 1 \n" +
			"\t\tAND d.valid = 1 \n" +
			"\tORDER BY\n" +
			"\t\tq.question_code \n" +
			"\t) AS bb UNION ALL\n" +
			"SELECT\n" +
			"\tSUBSTRING_INDEX( SUBSTRING_INDEX( bb.`value`, '&', - 1 ), ':', 1 ) AS col1,\n" +
			"\tSUBSTRING_INDEX( SUBSTRING_INDEX( bb.`value`, '&', - 1 ), ':', - 1 ) AS col2 \n" +
			"FROM\n" +
			"\t(\n" +
			"\tSELECT DISTINCT\n" +
			"\t\tu.`value`,\n" +
			"\t\tq.question_code,\n" +
			"\t\td.orderno \n" +
			"\tFROM\n" +
			"\t\tuser_answers u,\n" +
			"\t\tquestion q,\n" +
			"\t\tquestion_options qo,\n" +
			"\t\tdivisor d \n" +
			"\tWHERE\n" +
			"\t\tu.question_code = q.`code` \n" +
			"\t\tAND q.`code` = qo.question_code \n" +
			"\t\tAND q.question_type = 'OPTION_INPUT' \n" +
			"\t\tAND qo.divisor_code = d.`code` \n" +
			"\t\tAND u.evaluation_user_code = #{EVALUATOION_CODE} \n" +
			"\t\tAND q.valid = 1 \n" +
			"\t\tAND qo.valid = 1 \n" +
			"\t\tAND d.valid = 1 \n" +
			"\tORDER BY\n" +
			"\t\tq.question_code \n" +
			"\t) AS bb \n" +
			"\t) AS cc,\n" +
			"\tquestion_options p,\n" +
			"\tdivisor n \n" +
			"WHERE\n" +
			"\tcc.col1 = p.`code` \n" +
			"\tAND p.divisor_code = n.`code`\n" +
			"\t) vv ) vv \n" +
			"\tORDER BY\n" +
			"\t\tvv.orderno ASC \n" +
			"\t ")
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
