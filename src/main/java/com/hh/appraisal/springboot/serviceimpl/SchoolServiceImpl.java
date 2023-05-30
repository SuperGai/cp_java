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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.hh.appraisal.springboot.bean.SchoolBean;
import com.hh.appraisal.springboot.entity.Divisor;
import com.hh.appraisal.springboot.entity.School;
import com.hh.appraisal.springboot.dao.SchoolMapper;
import com.hh.appraisal.springboot.service.SchoolService;

/**
 * School Service 实现类
 * 
 * @author gaigai
 * @date 2021/06/26
 */
@Slf4j
@Service
public class SchoolServiceImpl extends ServiceImpl<SchoolMapper, School> implements SchoolService {

	private final SchoolMapper schoolMapper;

	public SchoolServiceImpl(SchoolMapper schoolMapper) {
		this.schoolMapper = schoolMapper;
	}

	@Override
	public SchoolBean findByCode(String code) {
		if (ObjectUtils.isEmpty(code)) {
			return null;
		}

		List<School> sourceList = schoolMapper.selectList(createWrapper(SchoolBean.builder().code(code).build()));
		if (ObjectUtils.isEmpty(sourceList)) {
			return null;
		}

		SchoolBean restBean = SchoolBean.builder().build();
		BeanUtils.copyProperties(sourceList.get(0), restBean);
		return restBean;
	}

	@Override
	public List<SchoolBean> findByCodeList(List<String> codeList) {
		if (CollectionUtils.isEmpty(codeList)) {
			return Collections.EMPTY_LIST;
		}

		List<School> sourceList = schoolMapper
				.selectList(createWrapper(SchoolBean.builder().schoolCodeList(codeList).build()));
		if (CollectionUtils.isEmpty(sourceList)) {
			return Collections.EMPTY_LIST;
		}

		List<SchoolBean> restBeanList = sourceList.stream().map(v -> {
			SchoolBean item = new SchoolBean();
			BeanUtils.copyProperties(v, item);
			return item;
		}).collect(Collectors.toList());
		return restBeanList;
	}

	@Override
	public List<SchoolBean> findList(SchoolBean bean) {
		List<School> list = schoolMapper.selectList(createWrapper(bean));
		if (CollectionUtils.isEmpty(list)) {
			return Collections.EMPTY_LIST;
		}

		List<SchoolBean> beanList = list.stream().map(item -> {
			SchoolBean srcBean = new SchoolBean();
			BeanUtils.copyProperties(item, srcBean);
			return srcBean;
		}).collect(Collectors.toList());
		return beanList;
	}

	@Override
	public Page<SchoolBean> findPage(SchoolBean bean, PageBean pageBean) {
		Page<School> sourcePage = schoolMapper.selectPage(new Page<>(pageBean.getCurrent(), pageBean.getSize()),
				createWrapper(bean));
		if (CollectionUtils.isEmpty(sourcePage.getRecords())) {
			return new Page<>();
		}

		Page<SchoolBean> restPage = new Page<>();
		BeanUtils.copyProperties(sourcePage, restPage, "records");
		restPage.setRecords(new ArrayList<>(sourcePage.getRecords().size()));
		sourcePage.getRecords().forEach(v -> {
			SchoolBean itemBean = new SchoolBean();
			BeanUtils.copyProperties(v, itemBean);
			restPage.getRecords().add(itemBean);
		});
		return restPage;
	}

	@Transactional
	@Override
	public SchoolBean add(SchoolBean bean) {
		School source = new School();
		BeanUtils.copyProperties(bean, source);

		schoolMapper.insert(source);
		BeanUtils.copyProperties(source, bean);
		return bean;
	}

	@Transactional
	@Override
	public int updateByCode(SchoolBean bean) {
		School source = new School();
		BeanUtils.copyProperties(bean, source);
		return schoolMapper.updateById(source);
	}

	@Transactional
	@Override
	public int deleteByCode(String code) {
		if (ObjectUtils.isEmpty(code)) {
			return 0;
		}

		School updateSource = new School();
		updateSource.setCode(code);
		updateSource.setValid(DataValid.INVALID);
		return schoolMapper.updateById(updateSource);
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
	private LambdaQueryWrapper createWrapper(SchoolBean bean) {
		LambdaQueryWrapper<School> wrapper = Wrappers.lambdaQuery();
		if (bean == null || bean.getValid() == null) {
			wrapper.eq(School::getValid, DataValid.VALID);
		}

		// 自定义条件
		if (bean != null) {
			if (bean.getValid() != null) {
				wrapper.eq(School::getValid, bean.getValid());
			}
			if (!ObjectUtils.isEmpty(bean.getCode())) {
				wrapper.eq(School::getCode, bean.getCode());
			}
			if (CollectionUtils.isNotEmpty(bean.getSchoolCodeList())) {
				wrapper.in(School::getCode, bean.getSchoolCodeList());
			}
			if (!ObjectUtils.isEmpty(bean.getSchoolName())) {
				wrapper.like(School::getSchoolName, bean.getSchoolName());
			}
			if (!ObjectUtils.isEmpty(bean.getSchoolCode())) {
				wrapper.like(School::getSchoolCode, bean.getSchoolCode());
			}

			// 编写条件逻辑....

		}

		return wrapper;
	}
}
