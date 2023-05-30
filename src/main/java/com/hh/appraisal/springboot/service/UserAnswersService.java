package com.hh.appraisal.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hh.appraisal.springboot.core.baen.PageBean;
import java.util.List;

import com.hh.appraisal.springboot.bean.QuestionAllBean;
import com.hh.appraisal.springboot.bean.UserAnswersBean;
import com.hh.appraisal.springboot.entity.UserAnswers;

/**
 * UserAnswers Service 接口
 * @author gaigai
 * @date 2021/06/26
 */
public interface UserAnswersService extends IService<UserAnswers> {

    /**
     * 根据唯一标识获取实体
     * @param code 唯一标识
     * @return
     */
    UserAnswersBean findByCode(String code);

    /**
     * 根据唯一标识集合获取实体
     * @param codeList 唯一标识集合
     * @return
     */
    List<UserAnswersBean> findByCodeList(List<String> codeList);

    /**
     * 查询列表
     * @param bean
     * @return
     */
    List<UserAnswersBean> findList(UserAnswersBean bean);

    /**
     * 分页查询
     * @param bean
     * @param pageBean
     * @return
     */
    Page<UserAnswersBean> findPage(UserAnswersBean bean, PageBean pageBean);

    /**
     * 添加一条记录
     * @param bean
     * @return
     */
    UserAnswersBean add(UserAnswersBean bean);

    /**
     * 根据唯一标识更新一条记录
     * @param bean
     * @return
     */
    int updateByCode(UserAnswersBean bean);

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
     * 找到题目
     */
    QuestionAllBean findQuestion(Integer QUESTION_NO,String EVALUATOION_CODE);
    
    /**
     * 获取下一题
     * @param evaluationUserCode
     * @return
     * @throws Exception
     */
    QuestionAllBean getQuestion(String evaluationUserCode);

}
