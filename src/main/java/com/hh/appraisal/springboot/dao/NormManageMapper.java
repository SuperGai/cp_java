package com.hh.appraisal.springboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import com.hh.appraisal.springboot.entity.NormManage;

/**
 * Mapper
 * @author gaigai
 * @date 2023/06/02
 */
@Repository
public interface NormManageMapper extends BaseMapper<NormManage> {
	
	@Select("SELECT"
			+ "	second_number "
			+ "FROM"
			+ "	("
			+ "	SELECT"
			+ "		SUBSTRING_INDEX( SUBSTRING_INDEX( VAL, ':', 1 ), '/', - 1 ) AS FIRST_NUMBER,"
			+ "		SUBSTRING_INDEX( SUBSTRING_INDEX( VAL, ':', - 1 ), '/', 1 ) AS SECOND_NUMBER "
			+ "	FROM"
			+ "		("
			+ "		SELECT"
			+ "			TRIM("
			+ "			SUBSTRING_INDEX( SUBSTRING_INDEX( NDESC, '/', NUMBERS.N ), '/', - 1 )) AS VAL "
			+ "		FROM"
			+ "			("
			+ "			SELECT"
			+ "				( A + B * 10 ) + 1 AS N "
			+ "			FROM"
			+ "				("
			+ "				SELECT"
			+ "					0 AS A UNION ALL"
			+ "				SELECT"
			+ "					1 UNION ALL"
			+ "				SELECT"
			+ "					2 UNION ALL"
			+ "				SELECT"
			+ "					3 UNION ALL"
			+ "				SELECT"
			+ "					4 UNION ALL"
			+ "				SELECT"
			+ "					5 UNION ALL"
			+ "				SELECT"
			+ "					6 UNION ALL"
			+ "				SELECT"
			+ "					7 UNION ALL"
			+ "				SELECT"
			+ "					8 UNION ALL"
			+ "				SELECT"
			+ "					9 "
			+ "				) AS TENS"
			+ "				CROSS JOIN ("
			+ "				SELECT"
			+ "					0 AS B UNION ALL"
			+ "				SELECT"
			+ "					1 UNION ALL"
			+ "				SELECT"
			+ "					2 UNION ALL"
			+ "				SELECT"
			+ "					3 UNION ALL"
			+ "				SELECT"
			+ "					4 UNION ALL"
			+ "				SELECT"
			+ "					5 UNION ALL"
			+ "				SELECT"
			+ "					6 UNION ALL"
			+ "				SELECT"
			+ "					7 UNION ALL"
			+ "				SELECT"
			+ "					8 UNION ALL"
			+ "				SELECT"
			+ "					9 "
			+ "				) AS ONES "
			+ "			) AS NUMBERS"
			+ "			JOIN NORM_MANAGE ON CHAR_LENGTH( NDESC ) - CHAR_LENGTH("
			+ "			REPLACE ( NDESC, '/', '' )) >= NUMBERS.N - 1 "
			+ "		WHERE"
			+ "			NORM_MANAGE.`CODE` = #{NORM_MANAGE_CODE} "
			+ "		) AS TBALE "
			+ "	ORDER BY"
			+ "	CONVERT ( FIRST_NUMBER, SIGNED ) "
			+ "	) AS T WHERE T.FIRST_NUMBER=#{VALUE} ")
	double getPercentileValue(@Param("NORM_MANAGE_CODE") String NORM_MANAGE_CODE,@Param("VALUE") String VALUE);
	
	
	
	
}
