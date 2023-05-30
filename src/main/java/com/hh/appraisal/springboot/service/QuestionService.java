package com.hh.appraisal.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hh.appraisal.springboot.core.baen.PageBean;
import com.hh.appraisal.springboot.core.baen.RestBean;

import java.util.List;

import com.hh.appraisal.springboot.bean.QuestionAllBean;
import com.hh.appraisal.springboot.bean.QuestionBean;
import com.hh.appraisal.springboot.entity.Question;

/**
 * Question Service 接口
 * @author gaigai
 * @date 2021/06/26
 */
public interface QuestionService extends IService<Question> {

    /**
     * 根据唯一标识获取实体
     * @param code 唯一标识
     * @return
     */
    QuestionBean findByCode(String code);

    /**
     * 根据唯一标识集合获取实体
     * @param codeList 唯一标识集合
     * @return
     */
    List<QuestionBean> findByCodeList(List<String> codeList);

    /**
     * 查询列表
     * @param bean
     * @return
     */
    List<QuestionBean> findList(QuestionBean bean);
    

    /**
     * 分页查询
     * @param bean
     * @param pageBean
     * @return
     */
    Page<QuestionBean> findPage(QuestionBean bean, PageBean pageBean);

    /**
     * 添加一条记录
     * @param bean
     * @return
     */
    QuestionBean add(QuestionBean bean);

    /**
     * 根据唯一标识更新一条记录
     * @param bean
     * @return
     */
    int updateByCode(QuestionBean bean);

    /**
     * 根据唯一标识删除一条记录
     * 软删除
     * @param code 唯一标识
     * @return
     */
    int deleteByCode(String code);

    /**
     * 批量删除
     * 软删除
     * @param codeList 唯一标识集合
     */
    void deleteByCode(List<String> codeList);

    /**
     * EXCEL导入
     * @param bean
     * @return
     * @throws Exception 
     */
    RestBean addByExcel(QuestionBean bean) throws Exception;
    


}
