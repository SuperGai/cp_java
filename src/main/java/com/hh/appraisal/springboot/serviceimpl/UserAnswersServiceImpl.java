package com.hh.appraisal.springboot.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hh.appraisal.springboot.core.baen.PageBean;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.core.constant.DataValid;
import com.hh.appraisal.springboot.core.utils.NumberUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.hh.appraisal.springboot.bean.ProductBean;
import com.hh.appraisal.springboot.bean.QuestionAllBean;
import com.hh.appraisal.springboot.bean.QuestionBean;
import com.hh.appraisal.springboot.bean.QuestionOptionsBean;
import com.hh.appraisal.springboot.bean.UserAnswersBean;
import com.hh.appraisal.springboot.entity.EvaluatoionCode;
import com.hh.appraisal.springboot.entity.UserAnswers;
import com.hh.appraisal.springboot.dao.UserAnswersMapper;
import com.hh.appraisal.springboot.service.EvaluatoionCodeService;
import com.hh.appraisal.springboot.service.GeneraPdfAsyncTaskService;
import com.hh.appraisal.springboot.service.ProductService;
import com.hh.appraisal.springboot.service.QuestionOptionsService;
import com.hh.appraisal.springboot.service.QuestionService;
import com.hh.appraisal.springboot.service.UserAnswersService;

/**
 * UserAnswers Service 实现类
 * 
 * @author gaigai
 * @date 2021/06/26
 */
@Slf4j
@Service
public class UserAnswersServiceImpl extends ServiceImpl<UserAnswersMapper, UserAnswers> implements UserAnswersService {

	private final UserAnswersMapper userAnswersMapper;
	
	@Autowired
	private ProductService productService;
	@Autowired
	private EvaluatoionCodeService evaluatoionCodeService;
	@Autowired 
	private QuestionService questionService;
	@Autowired 	
	private  QuestionOptionsService questionOptionsService;

	public UserAnswersServiceImpl(UserAnswersMapper userAnswersMapper) {
		this.userAnswersMapper = userAnswersMapper;
	}

	@Override
	public UserAnswersBean findByCode(String code) {
		if (ObjectUtils.isEmpty(code)) {
			return null;
		}

		List<UserAnswers> sourceList = userAnswersMapper
				.selectList(createWrapper(UserAnswersBean.builder().code(code).build()));
		if (ObjectUtils.isEmpty(sourceList)) {
			return null;
		}

		UserAnswersBean restBean = UserAnswersBean.builder().build();
		BeanUtils.copyProperties(sourceList.get(0), restBean);
		return restBean;
	}

	/**
	 * 找到题目
	 */
	@Override
	public QuestionAllBean findQuestion(Integer QUESTION_NO, String EVALUATOION_CODE) {
		if (ObjectUtils.isEmpty(EVALUATOION_CODE) || ObjectUtils.isEmpty(QUESTION_NO)) {
			return null;
		}
		QuestionAllBean source = userAnswersMapper.findQuestion(QUESTION_NO, EVALUATOION_CODE);
		if (ObjectUtils.isEmpty(source)) {
			return null;
		}
		return source;
	}

	@Override
	public List<UserAnswersBean> findByCodeList(List<String> codeList) {
		if (CollectionUtils.isEmpty(codeList)) {
			return Collections.EMPTY_LIST;
		}

		List<UserAnswers> sourceList = userAnswersMapper
				.selectList(createWrapper(UserAnswersBean.builder().userAnswersCodeList(codeList).build()));
		if (CollectionUtils.isEmpty(sourceList)) {
			return Collections.EMPTY_LIST;
		}

		List<UserAnswersBean> restBeanList = sourceList.stream().map(v -> {
			UserAnswersBean item = new UserAnswersBean();
			BeanUtils.copyProperties(v, item);
			return item;
		}).collect(Collectors.toList());
		return restBeanList;
	}

	@Override
	public List<UserAnswersBean> findList(UserAnswersBean bean) {
		List<UserAnswers> list = userAnswersMapper.selectListAll(createWrapper(bean));
		if (CollectionUtils.isEmpty(list)) {
			return Collections.EMPTY_LIST;
		}

		List<UserAnswersBean> beanList = list.stream().map(item -> {
			UserAnswersBean srcBean = new UserAnswersBean();
			BeanUtils.copyProperties(item, srcBean);
			return srcBean;
		}).collect(Collectors.toList());
		return beanList;
	}

	@Override
	public Page<UserAnswersBean> findPage(UserAnswersBean bean, PageBean pageBean) {
		Page<UserAnswers> sourcePage = userAnswersMapper
				.selectPageAll(new Page<>(pageBean.getCurrent(), pageBean.getSize()), createWrapper(bean));
		if (CollectionUtils.isEmpty(sourcePage.getRecords())) {
			return new Page<>();
		}

		Page<UserAnswersBean> restPage = new Page<>();
		BeanUtils.copyProperties(sourcePage, restPage, "records");
		restPage.setRecords(new ArrayList<>(sourcePage.getRecords().size()));
		sourcePage.getRecords().forEach(v -> {
			UserAnswersBean itemBean = new UserAnswersBean();
			BeanUtils.copyProperties(v, itemBean);
			restPage.getRecords().add(itemBean);
		});
		return restPage;
	}

	@Transactional
	@Override
	public UserAnswersBean add(UserAnswersBean bean) {
		UserAnswers source = new UserAnswers();
		BeanUtils.copyProperties(bean, source);

		userAnswersMapper.insert(source);
		BeanUtils.copyProperties(source, bean);
		return bean;
	}

	@Transactional
	@Override
	public int updateByCode(UserAnswersBean bean) {
		UserAnswers source = new UserAnswers();
		BeanUtils.copyProperties(bean, source);
		return userAnswersMapper.updateById(source);
	}

	@Transactional
	@Override
	public int deleteByCode(String code) {
		if (ObjectUtils.isEmpty(code)) {
			return 0;
		}

		UserAnswers updateSource = new UserAnswers();
		updateSource.setCode(code);
		updateSource.setValid(DataValid.INVALID);
		return userAnswersMapper.updateById(updateSource);
	}

	@Transactional
	@Override
	public void deleteByCode(List<String> codeList) {
		if (ObjectUtils.isEmpty(codeList)) {
			return;
		}

		codeList.forEach(v -> {
			int restCount = deleteByCode(v);
			if (restCount <= 0) {
				log.error("删除失败: code=" + v);
			}
		});
	}

	/**
	 * 建立查询条件 条件尽量都写在此方法
	 * 
	 * @param bean
	 * @return
	 */
	private LambdaQueryWrapper createWrapper(UserAnswersBean bean) {
		LambdaQueryWrapper<UserAnswers> wrapper = Wrappers.lambdaQuery();
		if (bean == null || bean.getValid() == null) {
//            wrapper.eq(UserAnswers::getValid,DataValid.VALID);
			wrapper.apply("user_answers.valid= {0}", DataValid.VALID);
		}

		// 自定义条件
		if (bean != null) {
			if (bean.getValid() != null) {
//                wrapper.eq(UserAnswers::getValid,bean.getValid());
				wrapper.apply("user_answers.valid= {0}", bean.getValid());
			}
			if (!ObjectUtils.isEmpty(bean.getCode())) {
				wrapper.apply("user_answers.code= {0}", bean.getCode());
//                wrapper.eq(UserAnswers::getCode, bean.getCode());
			}
			if (CollectionUtils.isNotEmpty(bean.getUserAnswersCodeList())) {
				wrapper.in(UserAnswers::getCode, bean.getUserAnswersCodeList());
			}
			// 测评码搜索
			if (!ObjectUtils.isEmpty(bean.getEvaluationUserCode())) {
				wrapper.apply("user_answers.evaluation_user_code= {0}", bean.getEvaluationUserCode());
			}
			// 题序搜索
			if (!ObjectUtils.isEmpty(bean.getQuestionNo())&&bean.getQuestionNo()!=0) {
				wrapper.apply("user_answers.Question_No= {0}", bean.getQuestionNo());
			}
			// 产品搜索
			if (!ObjectUtils.isEmpty(bean.getProductCode())) {
				wrapper.apply("user_answers.Product_Code= {0}", bean.getProductCode());
			}
			// 是否完成
			if (!ObjectUtils.isEmpty(bean.getIsComplete())) {
				wrapper.apply("user_answers.Is_Complete= {0}", bean.getIsComplete());
			}
			// 编写条件逻辑....

		}

		return wrapper;
	}
	

	@Override
	public QuestionAllBean getQuestion(String evaluationUserCode) {
		// TODO Auto-generated method stub
		EvaluatoionCode evaluatoionCode = evaluatoionCodeService
				.getOne(new QueryWrapper<EvaluatoionCode>().eq("EVALUATOION_CODE", evaluationUserCode));
		UserAnswersBean bean = new UserAnswersBean();
		bean.setEvaluationUserCode(evaluationUserCode);
		ProductBean product = productService.findByCode(evaluatoionCode.getProductCode());
		List<UserAnswersBean> list = findList(bean);
		if (list == null || list.size() == 0) {
			QuestionBean question = new QuestionBean();
			question.setQuestionBank(product.getCode());
			List<QuestionBean> questionList = questionService.findList(question);
			int numbers[] = NumberUtils.randomArray(1, product.getQuestionNum(), product.getQuestionNum());
			for (int i = 0; i < questionList.size(); i++) {
				QuestionBean questionBean = questionList.get(i);
				UserAnswersBean userAnswersBean = new UserAnswersBean();
				userAnswersBean.setEvaluationUserCode(evaluationUserCode);
				userAnswersBean.setProductCode(evaluatoionCode.getProductCode());
				userAnswersBean.setQuestionCode(questionBean.getCode());
				userAnswersBean.setQuestionNo(numbers[i]);
				userAnswersBean.setIsComplete("N");
				add(userAnswersBean);
			}
			QuestionAllBean allbean = findQuestion(1, evaluationUserCode);
			allbean.setQuestionNum(product.getQuestionNum());
			allbean.setAnswerTime(product.getAnswerTime());
//			(new QueryWrapper<QuestionOptions>().eq("QUESTION_CODE", allbean.getQuestionCode()));
			QuestionOptionsBean questionOptionsBean = new QuestionOptionsBean();
			questionOptionsBean.setQuestionCode(allbean.getQuestionCode());
			List<QuestionOptionsBean> options = questionOptionsService.findList(questionOptionsBean);
			allbean.setQuestionOptionsBean(options);
			return allbean;
		} else {
			int minNum=userAnswersMapper.getMinUnCompleteNo(evaluationUserCode);
			if(minNum==0) {
				return null;
			}
			QuestionAllBean allbean = findQuestion(minNum, evaluationUserCode);
			allbean.setQuestionNum(product.getQuestionNum());
			allbean.setAnswerTime(product.getAnswerTime());
//			(new QueryWrapper<QuestionOptions>().eq("QUESTION_CODE", allbean.getQuestionCode()));
			QuestionOptionsBean questionOptionsBean = new QuestionOptionsBean();
			questionOptionsBean.setQuestionCode(allbean.getQuestionCode());
			List<QuestionOptionsBean> options = questionOptionsService.findList(questionOptionsBean);
			allbean.setQuestionOptionsBean(options);
			return allbean;
		}
	}
}
