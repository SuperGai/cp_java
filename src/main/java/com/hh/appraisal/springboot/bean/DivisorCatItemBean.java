package com.hh.appraisal.springboot.bean;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "因子分类明细分数")
public class DivisorCatItemBean  implements Serializable {


    private String key;
	

    private double value;
	
    private String divisorCatName;
    
    
    private String normCode;

    private String desc;

}
