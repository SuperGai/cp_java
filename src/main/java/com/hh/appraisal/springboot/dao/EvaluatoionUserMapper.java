package com.hh.appraisal.springboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.hh.appraisal.springboot.bean.ReportBean;
import com.hh.appraisal.springboot.entity.EvaluatoionUser;

/**
 * Mapper
 * @author gaigai
 * @date 2021/06/26
 */
@Repository
public interface EvaluatoionUserMapper extends BaseMapper<EvaluatoionUser> {
	
	@Select("SELECT * FROM 	( SELECT count(*)+ 6000 AS year_num FROM evaluatoion_user WHERE DATE_FORMAT( create_time, '%Y-%m-%d' )= DATE_FORMAT( NOW(), '%Y-%m-%d' ) ) AS BB,"
			+ "	( 	SELECT count(*)+ 500 AS month_num FROM  evaluatoion_user "
			+ "	WHERE 	DATE_FORMAT( create_time, '%Y-%m' )= DATE_FORMAT( NOW(), '%Y-%m' )) AS CC,"
			+ "	( SELECT count(*)+ 6000 AS join_num FROM evaluatoion_user ) AS DD,"
			+ "	( SELECT count(*)+ 6000 AS complete_num FROM evaluatoion_user WHERE evaluatoion_user.is_complete = 'Y' ) AS EE,"
			+ "	( SELECT count(*) AS uncompleted_num FROM evaluatoion_user WHERE evaluatoion_user.is_complete = 'N' OR evaluatoion_user.is_complete IS NULL ) AS FF")
	ReportBean getReport();
	
	
}
