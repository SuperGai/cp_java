package com.hh.appraisal.springboot.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hh.appraisal.springboot.core.baen.PageBean;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.core.constant.DataValid;
import com.hh.appraisal.springboot.core.constant.RestCode;
import com.hh.appraisal.springboot.core.utils.NumberUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.hh.appraisal.springboot.bean.DivisorBean;
import com.hh.appraisal.springboot.bean.ProductBean;
import com.hh.appraisal.springboot.bean.QuestionAllBean;
import com.hh.appraisal.springboot.bean.QuestionBean;
import com.hh.appraisal.springboot.bean.QuestionOptionsBean;
import com.hh.appraisal.springboot.bean.UserAnswersBean;
import com.hh.appraisal.springboot.entity.Divisor;
import com.hh.appraisal.springboot.entity.EvaluatoionCode;
import com.hh.appraisal.springboot.entity.Product;
import com.hh.appraisal.springboot.entity.Question;
import com.hh.appraisal.springboot.entity.QuestionOptions;
import com.hh.appraisal.springboot.dao.DivisorMapper;
import com.hh.appraisal.springboot.dao.QuestionMapper;
import com.hh.appraisal.springboot.dao.UserAnswersMapper;
import com.hh.appraisal.springboot.service.DivisorService;
import com.hh.appraisal.springboot.service.EvaluatoionCodeService;
import com.hh.appraisal.springboot.service.ProductService;
import com.hh.appraisal.springboot.service.QuestionOptionsService;
import com.hh.appraisal.springboot.service.QuestionService;

/**
 * Question Service 实现类
 * 
 * @author gaigai
 * @date 2021/06/26
 */
@Slf4j
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

	private final QuestionMapper questionMapper;
	@Autowired
	private DivisorService divisorService;

	@Autowired
	private ProductService productService;

	public QuestionServiceImpl(QuestionMapper questionMapper) {
		this.questionMapper = questionMapper;
	}

	@Override
	public QuestionBean findByCode(String code) {
		if (ObjectUtils.isEmpty(code)) {
			return null;
		}

		List<Question> sourceList = questionMapper.selectList(createWrapper(QuestionBean.builder().code(code).build()));
		if (ObjectUtils.isEmpty(sourceList)) {
			return null;
		}

		QuestionBean restBean = QuestionBean.builder().build();
		BeanUtils.copyProperties(sourceList.get(0), restBean);
		return restBean;
	}

	@Override
	public List<QuestionBean> findByCodeList(List<String> codeList) {
		if (CollectionUtils.isEmpty(codeList)) {
			return Collections.EMPTY_LIST;
		}
		List<Question> sourceList = questionMapper
				.selectList(createWrapper(QuestionBean.builder().questionCodeList(codeList).build()));
		if (CollectionUtils.isEmpty(sourceList)) {
			return Collections.EMPTY_LIST;
		}
		List<QuestionBean> restBeanList = sourceList.stream().map(v -> {
			QuestionBean item = new QuestionBean();
			BeanUtils.copyProperties(v, item);
			return item;
		}).collect(Collectors.toList());
		return restBeanList;
	}

	@Override
	public List<QuestionBean> findList(QuestionBean bean) {
		List<Question> list = questionMapper.selectListAll(createWrapper(bean));
		if (CollectionUtils.isEmpty(list)) {
			return Collections.EMPTY_LIST;
		}
		List<QuestionBean> beanList = list.stream().map(item -> {
			QuestionBean srcBean = new QuestionBean();
			BeanUtils.copyProperties(item, srcBean);
			return srcBean;
		}).collect(Collectors.toList());
		return beanList;
	}

	@Override
	public Page<QuestionBean> findPage(QuestionBean bean, PageBean pageBean) {
		Page<Question> sourcePage = questionMapper.selectPageAll(new Page<>(pageBean.getCurrent(), pageBean.getSize()),
				createWrapper(bean));
		if (CollectionUtils.isEmpty(sourcePage.getRecords())) {
			return new Page<>();
		}
		Page<QuestionBean> restPage = new Page<>();
		BeanUtils.copyProperties(sourcePage, restPage, "records");
		restPage.setRecords(new ArrayList<>(sourcePage.getRecords().size()));
		sourcePage.getRecords().forEach(v -> {
			QuestionBean itemBean = new QuestionBean();
			BeanUtils.copyProperties(v, itemBean);
			restPage.getRecords().add(itemBean);
		});
		return restPage;
	}

	@Transactional
	@Override
	public QuestionBean add(QuestionBean bean) {
		Question source = new Question();
		BeanUtils.copyProperties(bean, source);

		questionMapper.insert(source);
		BeanUtils.copyProperties(source, bean);
		return bean;
	}

	@Transactional
	@Override
	public RestBean addByExcel(QuestionBean bean) throws Exception {
		Question source = new Question();
		Product product = productService.getOne(new QueryWrapper<Product>().eq("product_Name", bean.getQuestionBank()));
		if (ObjectUtils.isEmpty(product)) {
			throw new Exception("产品名称有误!");
		}
		bean.setQuestionBank(product.getCode());
		if (ObjectUtils.isNotEmpty(bean.getDivisorCode())) {
			Divisor divisor = divisorService.getOne(new QueryWrapper<Divisor>()
					.eq("divisor_Name", bean.getDivisorCode()).or().eq("divisor_Desc", bean.getDivisorCode()));
			if (ObjectUtils.isEmpty(divisor)) {
				throw new Exception("指标库填写有误!");
			}
			bean.setDivisorCode(divisor.getCode());
		}
		BeanUtils.copyProperties(bean, source);
		questionMapper.insert(source);
		BeanUtils.copyProperties(source, bean);
		return RestBean.ok();
	}

	@Transactional
	@Override
	public int updateByCode(QuestionBean bean) {
		Question source = new Question();
		BeanUtils.copyProperties(bean, source);
		return questionMapper.updateById(source);
	}

	@Transactional
	@Override
	public int deleteByCode(String code) {
		if (ObjectUtils.isEmpty(code)) {
			return 0;
		}

		Question updateSource = new Question();
		updateSource.setCode(code);
		updateSource.setValid(DataValid.INVALID);
		return questionMapper.updateById(updateSource);
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
	private LambdaQueryWrapper createWrapper(QuestionBean bean) {
		LambdaQueryWrapper<Question> wrapper = Wrappers.lambdaQuery();
		if (bean == null || bean.getValid() == null) {
//			wrapper.eq(Question::getValid, DataValid.VALID);
			wrapper.apply("question.valid= {0}", DataValid.VALID);
		}
		// 自定义条件
		if (bean != null) {
			if (bean.getValid() != null) {
//				wrapper.eq(Question::getValid, bean.getValid());
				wrapper.apply("question.valid= {0}", bean.getValid());
			}
			if (!ObjectUtils.isEmpty(bean.getCode())) {
//				wrapper.eq(Question::getCode, bean.getCode());
				wrapper.apply("question.code= {0}", bean.getCode());
			}
			if (CollectionUtils.isNotEmpty(bean.getQuestionCodeList())) {
				wrapper.in(Question::getCode, bean.getQuestionCodeList());
			}
			if (!ObjectUtils.isEmpty(bean.getDivisorCode())) {
				wrapper.apply("question.divisor_code= {0}", bean.getDivisorCode());
//				wrapper.eq(Question::getDivisorCode, bean.getDivisorCode());
			}
			if (!ObjectUtils.isEmpty(bean.getQuestionBank())) {
				wrapper.apply("question.question_Bank= {0}", bean.getQuestionBank());
//				wrapper.eq(Question::getQuestionBank, bean.getQuestionBank());
			}
			if (!ObjectUtils.isEmpty(bean.getQuestionCode())) {
				wrapper.like(Question::getQuestionCode, bean.getQuestionCode());
//				wrapper.apply("question.question_code= {0}", bean.getQuestionCode());
//				wrapper.eq(Question::getQuestionCode, bean.getQuestionCode());
			}
			if (!ObjectUtils.isEmpty(bean.getQuestionEh())) {
				wrapper.like(Question::getQuestionEh, bean.getQuestionEh());
//				wrapper.apply("question.question_en={0}", bean.getQuestionEh());
//				wrapper.eq(Question::getQuestionEh, bean.getQuestionEh());
			}
			if (!ObjectUtils.isEmpty(bean.getQuestionZh())) {
				wrapper.like(Question::getQuestionZh, bean.getQuestionZh());
//				wrapper.apply("question.question_zh={0}", bean.getQuestionZh());
//				wrapper.eq(Question::getQuestionEh, bean.getQuestionZh());
			}
//			wrapper.orderByAsc(Question::getQuestionCode);
		}
		return wrapper;
	}

//	@Override
//	public List<Question> findList2(QuestionBean bean) {
//		// TODO Auto-generated method stub
//		List<Question> list = questionMapper.selectList(createWrapper(bean));
//		if (CollectionUtils.isEmpty(list)) {
//			return Collections.EMPTY_LIST;
//		}
//		return list;
//	}
}
