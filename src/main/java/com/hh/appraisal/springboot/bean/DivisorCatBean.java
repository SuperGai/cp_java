package com.hh.appraisal.springboot.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "因子分类分数")
public class DivisorCatBean  implements Serializable {


    private double catValue;
	

    private String catName;

    
    private List<DivisorCatItemBean> items=new ArrayList<DivisorCatItemBean>();
	

}
