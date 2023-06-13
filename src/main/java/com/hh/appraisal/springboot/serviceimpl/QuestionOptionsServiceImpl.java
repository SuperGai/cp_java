package com.hh.appraisal.springboot.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hh.appraisal.springboot.core.baen.PageBean;
import com.hh.appraisal.springboot.core.constant.DataValid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.hh.appraisal.springboot.bean.DivisorBean;
import com.hh.appraisal.springboot.bean.QuestionOptionsBean;
import com.hh.appraisal.springboot.entity.QuestionOptions;
import com.hh.appraisal.springboot.dao.QuestionOptionsMapper;
import com.hh.appraisal.springboot.service.DivisorService;
import com.hh.appraisal.springboot.service.QuestionOptionsService;

/**
 * QuestionOptions Service 实现类
 * 
 * @author gaigai
 * @date 2021/06/26
 */
@Slf4j
@Service
public class QuestionOptionsServiceImpl extends ServiceImpl<QuestionOptionsMapper, QuestionOptions>
		implements QuestionOptionsService {

	private final QuestionOptionsMapper questionOptionsMapper;

	@Autowired
	private DivisorService divisorService;

	public QuestionOptionsServiceImpl(QuestionOptionsMapper questionOptionsMapper) {
		this.questionOptionsMapper = questionOptionsMapper;
	}

	@Override
	public QuestionOptionsBean findByCode(String code) {
		if (ObjectUtils.isEmpty(code)) {
			return null;
		}

		List<QuestionOptions> sourceList = questionOptionsMapper
				.selectList(createWrapper(QuestionOptionsBean.builder().code(code).build()));
		if (ObjectUtils.isEmpty(sourceList)) {
			return null;
		}

		QuestionOptionsBean restBean = QuestionOptionsBean.builder().build();
		BeanUtils.copyProperties(sourceList.get(0), restBean);
		return restBean;
	}

	@Override
	public List<QuestionOptionsBean> findByCodeList(List<String> codeList) {
		if (CollectionUtils.isEmpty(codeList)) {
			return Collections.EMPTY_LIST;
		}

		List<QuestionOptions> sourceList = questionOptionsMapper
				.selectList(createWrapper(QuestionOptionsBean.builder().questionOptionsCodeList(codeList).build()));
		if (CollectionUtils.isEmpty(sourceList)) {
			return Collections.EMPTY_LIST;
		}

		List<QuestionOptionsBean> restBeanList = sourceList.stream().map(v -> {
			QuestionOptionsBean item = new QuestionOptionsBean();
			BeanUtils.copyProperties(v, item);
			return item;
		}).collect(Collectors.toList());
		return restBeanList;
	}

	@Override
	public List<QuestionOptionsBean> findList(QuestionOptionsBean bean) {
		List<QuestionOptions> list = questionOptionsMapper.selectList(createWrapper(bean));
		if (CollectionUtils.isEmpty(list)) {
			return Collections.EMPTY_LIST;
		}

		List<QuestionOptionsBean> beanList = list.stream().map(item -> {
			QuestionOptionsBean srcBean = new QuestionOptionsBean();
			BeanUtils.copyProperties(item, srcBean);
			if (ObjectUtils.isNotEmpty(srcBean.getDivisorCode())) {
				DivisorBean divisor = divisorService.findByCode(srcBean.getDivisorCode());
				srcBean.setDivisorCode(divisor.getDivisorName());
			}
			return srcBean;
		}).collect(Collectors.toList());
		return beanList;
	}

	@Override
	public Page<QuestionOptionsBean> findPage(QuestionOptionsBean bean, PageBean pageBean) {
		Page<QuestionOptions> sourcePage = questionOptionsMapper
				.selectPage(new Page<>(pageBean.getCurrent(), pageBean.getSize()), createWrapper(bean));
		if (CollectionUtils.isEmpty(sourcePage.getRecords())) {
			return new Page<>();
		}

		Page<QuestionOptionsBean> restPage = new Page<>();
		BeanUtils.copyProperties(sourcePage, restPage, "records");
		restPage.setRecords(new ArrayList<>(sourcePage.getRecords().size()));
		sourcePage.getRecords().forEach(v -> {
			QuestionOptionsBean itemBean = new QuestionOptionsBean();
			BeanUtils.copyProperties(v, itemBean);
			restPage.getRecords().add(itemBean);
		});
		return restPage;
	}

	@Transactional
	@Override
	public QuestionOptionsBean add(QuestionOptionsBean bean) {
		QuestionOptions source = new QuestionOptions();
		BeanUtils.copyProperties(bean, source);

		questionOptionsMapper.insert(source);
		BeanUtils.copyProperties(source, bean);
		return bean;
	}

	@Transactional
	@Override
	public int updateByCode(QuestionOptionsBean bean) {
		QuestionOptions source = new QuestionOptions();
		BeanUtils.copyProperties(bean, source);
		return questionOptionsMapper.updateById(source);
	}

	@Transactional
	@Override
	public int deleteByCode(String code) {
		if (ObjectUtils.isEmpty(code)) {
			return 0;
		}

		QuestionOptions updateSource = new QuestionOptions();
		updateSource.setCode(code);
		updateSource.setValid(DataValid.INVALID);
		return questionOptionsMapper.updateById(updateSource);
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
	private LambdaQueryWrapper createWrapper(QuestionOptionsBean bean) {
		LambdaQueryWrapper<QuestionOptions> wrapper = Wrappers.lambdaQuery();
		if (bean == null || bean.getValid() == null) {
			wrapper.eq(QuestionOptions::getValid, DataValid.VALID);
		}

		// 自定义条件
		if (bean != null) {
			if (bean.getValid() != null) {
				wrapper.eq(QuestionOptions::getValid, bean.getValid());
			}
			if (!ObjectUtils.isEmpty(bean.getCode())) {
				wrapper.eq(QuestionOptions::getCode, bean.getCode());
			}
			if (CollectionUtils.isNotEmpty(bean.getQuestionOptionsCodeList())) {
				wrapper.in(QuestionOptions::getCode, bean.getQuestionOptionsCodeList());
			}
			if (!ObjectUtils.isEmpty(bean.getQuestionCode())) {
				wrapper.eq(QuestionOptions::getQuestionCode, bean.getQuestionCode());
			}
			// 编写条件逻辑....

		}
		wrapper.orderByAsc(QuestionOptions::getDivisorValue);
		return wrapper;
	}
}
