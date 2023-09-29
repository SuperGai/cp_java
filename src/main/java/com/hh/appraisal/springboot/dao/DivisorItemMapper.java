package com.hh.appraisal.springboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import com.hh.appraisal.springboot.entity.DivisorItem;

/**
 * Mapper
 * @author gaigai
 * @date 2021/06/26
 */
@Repository
public interface DivisorItemMapper extends BaseMapper<DivisorItem> {
	
	@Select("SELECT  I.CONTENT_ZH FROM DIVISOR N,DIVISOR_ITEM I WHERE N.DIVISOR_NAME='掩饰性' AND N.`CODE`=I.DIVISOR_CODE AND I.VALUE_START <= #{score} AND #{score} <= I.VALUE_END")
    String getYsxDesc(@Param("score") long score);
	
}
