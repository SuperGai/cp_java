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

import com.hh.appraisal.springboot.bean.EvaluatoionUserBean;
import com.hh.appraisal.springboot.entity.EvaluatoionUser;
import com.hh.appraisal.springboot.entity.Question;
import com.hh.appraisal.springboot.dao.EvaluatoionUserMapper;
import com.hh.appraisal.springboot.service.EvaluatoionUserService;

/**
 * EvaluatoionUser Service 实现类
 * 
 * @author gaigai
 * @date 2021/06/26
 */
@Slf4j
@Service
public class EvaluatoionUserServiceImpl extends ServiceImpl<EvaluatoionUserMapper, EvaluatoionUser>
		implements EvaluatoionUserService {

	private final EvaluatoionUserMapper evaluatoionUserMapper;

	public EvaluatoionUserServiceImpl(EvaluatoionUserMapper evaluatoionUserMapper) {
		this.evaluatoionUserMapper = evaluatoionUserMapper;
	}

	@Override
	public EvaluatoionUserBean findByCode(String code) {
		if (ObjectUtils.isEmpty(code)) {
			return null;
		}

		List<EvaluatoionUser> sourceList = evaluatoionUserMapper
				.selectList(createWrapper(EvaluatoionUserBean.builder().code(code).build()));
		if (ObjectUtils.isEmpty(sourceList)) {
			return null;
		}

		EvaluatoionUserBean restBean = EvaluatoionUserBean.builder().build();
		BeanUtils.copyProperties(sourceList.get(0), restBean);
		return restBean;
	}

	@Override
	public List<EvaluatoionUserBean> findByCodeList(List<String> codeList) {
		if (CollectionUtils.isEmpty(codeList)) {
			return Collections.EMPTY_LIST;
		}

		List<EvaluatoionUser> sourceList = evaluatoionUserMapper
				.selectList(createWrapper(EvaluatoionUserBean.builder().evaluatoionUserCodeList(codeList).build()));
		if (CollectionUtils.isEmpty(sourceList)) {
			return Collections.EMPTY_LIST;
		}

		List<EvaluatoionUserBean> restBeanList = sourceList.stream().map(v -> {
			EvaluatoionUserBean item = new EvaluatoionUserBean();
			BeanUtils.copyProperties(v, item);
			return item;
		}).collect(Collectors.toList());
		return restBeanList;
	}

	@Override
	public List<EvaluatoionUserBean> findList(EvaluatoionUserBean bean) {
		List<EvaluatoionUser> list = evaluatoionUserMapper.selectList(createWrapper(bean));
		if (CollectionUtils.isEmpty(list)) {
			return Collections.EMPTY_LIST;
		}

		List<EvaluatoionUserBean> beanList = list.stream().map(item -> {
			EvaluatoionUserBean srcBean = new EvaluatoionUserBean();
			BeanUtils.copyProperties(item, srcBean);
			return srcBean;
		}).collect(Collectors.toList());
		return beanList;
	}

	@Override
	public Page<EvaluatoionUserBean> findPage(EvaluatoionUserBean bean, PageBean pageBean) {
		Page<EvaluatoionUser> sourcePage = evaluatoionUserMapper
				.selectPage(new Page<>(pageBean.getCurrent(), pageBean.getSize()), createWrapper(bean));
		if (CollectionUtils.isEmpty(sourcePage.getRecords())) {
			return new Page<>();
		}

		Page<EvaluatoionUserBean> restPage = new Page<>();
		BeanUtils.copyProperties(sourcePage, restPage, "records");
		restPage.setRecords(new ArrayList<>(sourcePage.getRecords().size()));
		sourcePage.getRecords().forEach(v -> {
			EvaluatoionUserBean itemBean = new EvaluatoionUserBean();
			BeanUtils.copyProperties(v, itemBean);
			restPage.getRecords().add(itemBean);
		});
		return restPage;
	}

	@Transactional
	@Override
	public EvaluatoionUserBean add(EvaluatoionUserBean bean) {
		EvaluatoionUser source = new EvaluatoionUser();
		BeanUtils.copyProperties(bean, source);

		evaluatoionUserMapper.insert(source);
		BeanUtils.copyProperties(source, bean);
		return bean;
	}

	@Transactional
	@Override
	public int updateByCode(EvaluatoionUserBean bean) {
		EvaluatoionUser source = new EvaluatoionUser();
		BeanUtils.copyProperties(bean, source);
		return evaluatoionUserMapper.updateById(source);
	}

	@Transactional
	@Override
	public int deleteByCode(String code) {
		if (ObjectUtils.isEmpty(code)) {
			return 0;
		}

		EvaluatoionUser updateSource = new EvaluatoionUser();
		updateSource.setCode(code);
		updateSource.setValid(DataValid.INVALID);
		return evaluatoionUserMapper.updateById(updateSource);
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
	private LambdaQueryWrapper createWrapper(EvaluatoionUserBean bean) {
		LambdaQueryWrapper<EvaluatoionUser> wrapper = Wrappers.lambdaQuery();
		if (bean == null || bean.getValid() == null) {
			wrapper.eq(EvaluatoionUser::getValid, DataValid.VALID);
		}

		// 自定义条件
		if (bean != null) {
			if (bean.getValid() != null) {
				wrapper.eq(EvaluatoionUser::getValid, bean.getValid());
			}
			if (!ObjectUtils.isEmpty(bean.getCode())) {
				wrapper.eq(EvaluatoionUser::getCode, bean.getCode());
			}
			if (CollectionUtils.isNotEmpty(bean.getEvaluatoionUserCodeList())) {
				wrapper.in(EvaluatoionUser::getCode, bean.getEvaluatoionUserCodeList());
			}
			if (!ObjectUtils.isEmpty(bean.getName())) {
				wrapper.like(EvaluatoionUser::getName, bean.getName());
			}
			if (!ObjectUtils.isEmpty(bean.getSchool())) {
				wrapper.like(EvaluatoionUser::getSchool, bean.getSchool());
			}
			if (!ObjectUtils.isEmpty(bean.getSchoolClass())) {
				wrapper.like(EvaluatoionUser::getSchoolClass, bean.getSchoolClass());
			}
			if (!ObjectUtils.isEmpty(bean.getEvaluatoionCode())) {
				wrapper.like(EvaluatoionUser::getEvaluatoionCode, bean.getEvaluatoionCode());
			}
			if (!ObjectUtils.isEmpty(bean.getPhone())) {
				wrapper.like(EvaluatoionUser::getPhone, bean.getPhone());
			}
			if(!ObjectUtils.isEmpty(bean.getIsComplete())) {
				wrapper.eq(EvaluatoionUser::getIsComplete, bean.getIsComplete());
			}if(!ObjectUtils.isEmpty(bean.getCreateTimeStart())){
				wrapper.apply("evaluatoion_user.create_time>='"+bean.getCreateTimeStart()+" 00:00:00'");
			}if(!ObjectUtils.isEmpty(bean.getCreateTimeEnd())){
				wrapper.apply("evaluatoion_user.create_time<='"+bean.getCreateTimeEnd()+" 23:59:59'");
			}if(!ObjectUtils.isEmpty(bean.getPersonType())) {
				wrapper.apply("evaluatoion_user.Evaluatoion_Code in  (select evaluatoion_Code from evaluatoion_code where Person_Type='"+bean.getPersonType()+"')");
			}

			// 编写条件逻辑....

		}

		return wrapper;
	}
}
