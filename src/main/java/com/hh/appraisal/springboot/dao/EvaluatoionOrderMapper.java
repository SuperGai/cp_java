package com.hh.appraisal.springboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import com.hh.appraisal.springboot.entity.EvaluatoionOrder;

/**
 * Mapper
 * 
 * @author gaigai
 * @date 2023/06/31
 */
@Repository
public interface EvaluatoionOrderMapper extends BaseMapper<EvaluatoionOrder> {

	@Select("select c.report_name from (SELECT\n"
			+ "	count( 1 ) AS countnum,\n"
			+ " report_name  AS report_name from  evaluatoion_order o,(\n"
			+ "	SELECT\n"
			+ "		substring_index( substring_index( a.product_code, ',', b.help_topic_id + 1 ), ',', - 1 ) AS productCode,\n"
			+ "		a.report_name \n"
			+ "	FROM\n"
			+ "		report a\n"
			+ "		JOIN mysql.help_topic b ON b.help_topic_id < LENGTH( a.product_code ) - LENGTH(\n"
			+ "		REPLACE ( a.product_code, ',', '' )) + 1 \n"
			+ "	) b \n"
			+ "WHERE\n"
			+ "	o.evaluatoion_code = #{EVALUATOION_CODE} \n"
			+ "	AND o.product_code = b.productCode \n"
			+ "	group by report_name) as  c where countnum=#{COUNTNUM}  ")
	String getReportNameByUserCode(@Param("EVALUATOION_CODE") String EVALUATOION_CODE,@Param("COUNTNUM") int COUNTNUM);

}
