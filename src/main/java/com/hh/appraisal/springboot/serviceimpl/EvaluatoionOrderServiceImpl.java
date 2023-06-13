package com.hh.appraisal.springboot.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
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

import com.hh.appraisal.springboot.bean.EvaluatoionOrderBean;
import com.hh.appraisal.springboot.bean.ProductBean;
import com.hh.appraisal.springboot.constant.CpStatus;
import com.hh.appraisal.springboot.dao.EvaluatoionOrderMapper;
import com.hh.appraisal.springboot.entity.EvaluatoionOrder;
import com.hh.appraisal.springboot.service.EvaluatoionOrderService;
import com.hh.appraisal.springboot.service.ProductService;

/**
 * EvaluatoionOrder Service 实现类
 * 
 * @author gaigai
 * @date 2023/06/31
 */
@Slf4j
@Service
public class EvaluatoionOrderServiceImpl extends ServiceImpl<EvaluatoionOrderMapper, EvaluatoionOrder>
		implements EvaluatoionOrderService {

	private final EvaluatoionOrderMapper evaluatoionOrderMapper;

	public EvaluatoionOrderServiceImpl(EvaluatoionOrderMapper evaluatoionOrderMapper) {
		this.evaluatoionOrderMapper = evaluatoionOrderMapper;
	}
	
	@Autowired
	private ProductService productService;

	@Override
	public EvaluatoionOrderBean findByCode(String code) {
		if (ObjectUtils.isEmpty(code)) {
			return null;
		}

		List<EvaluatoionOrder> sourceList = evaluatoionOrderMapper
				.selectList(createWrapper(EvaluatoionOrderBean.builder().code(code).build()));
		if (ObjectUtils.isEmpty(sourceList)) {
			return null;
		}

		EvaluatoionOrderBean restBean = EvaluatoionOrderBean.builder().build();
		BeanUtils.copyProperties(sourceList.get(0), restBean);
		return restBean;
	}

	@Override
	public List<EvaluatoionOrderBean> findByCodeList(List<String> codeList) {
		if (CollectionUtils.isEmpty(codeList)) {
			return Collections.EMPTY_LIST;
		}

		List<EvaluatoionOrder> sourceList = evaluatoionOrderMapper
				.selectList(createWrapper(EvaluatoionOrderBean.builder().evaluatoionOrderList(codeList).build()));
		if (CollectionUtils.isEmpty(sourceList)) {
			return Collections.EMPTY_LIST;
		}

		List<EvaluatoionOrderBean> restBeanList = sourceList.stream().map(v -> {
			EvaluatoionOrderBean item = new EvaluatoionOrderBean();
			BeanUtils.copyProperties(v, item);
			return item;
		}).collect(Collectors.toList());
		return restBeanList;
	}

	@Override
	public List<EvaluatoionOrderBean> findList(EvaluatoionOrderBean bean) {
		List<EvaluatoionOrder> list = evaluatoionOrderMapper.selectList(createWrapper(bean));
		if (CollectionUtils.isEmpty(list)) {
			return Collections.EMPTY_LIST;
		}
		List<EvaluatoionOrderBean> beanList = list.stream().map(item -> {
			EvaluatoionOrderBean srcBean = new EvaluatoionOrderBean();
			BeanUtils.copyProperties(item, srcBean);
			ProductBean product=productService.findByCode(item.getProductCode());
			srcBean.setProductName(product.getProductName());
			return srcBean;
		}).collect(Collectors.toList());
		return beanList;
	}

	@Override
	public Page<EvaluatoionOrderBean> findPage(EvaluatoionOrderBean bean, PageBean pageBean) {
		Page<EvaluatoionOrder> sourcePage = evaluatoionOrderMapper
				.selectPage(new Page<>(pageBean.getCurrent(), pageBean.getSize()), createWrapper(bean));
		if (CollectionUtils.isEmpty(sourcePage.getRecords())) {
			return new Page<>();
		}
		Page<EvaluatoionOrderBean> restPage = new Page<>();
		BeanUtils.copyProperties(sourcePage, restPage, "records");
		restPage.setRecords(new ArrayList<>(sourcePage.getRecords().size()));
		sourcePage.getRecords().forEach(v -> {
			EvaluatoionOrderBean itemBean = new EvaluatoionOrderBean();
			BeanUtils.copyProperties(v, itemBean);
			restPage.getRecords().add(itemBean);
		});
		return restPage;
	}

	@Transactional
	@Override
	public EvaluatoionOrderBean add(EvaluatoionOrderBean bean) {
		EvaluatoionOrder source = new EvaluatoionOrder();
		BeanUtils.copyProperties(bean, source);
		evaluatoionOrderMapper.insert(source);
		BeanUtils.copyProperties(source, bean);
		return bean;
	}

	@Transactional
	@Override
	public int updateByCode(EvaluatoionOrderBean bean) {
		EvaluatoionOrder source = new EvaluatoionOrder();
		BeanUtils.copyProperties(bean, source);
		return evaluatoionOrderMapper.updateById(source);
	}

	@Transactional
	@Override
	public int deleteByCode(String code) {
		if (ObjectUtils.isEmpty(code)) {
			return 0;
		}

		EvaluatoionOrder updateSource = new EvaluatoionOrder();
		updateSource.setCode(code);
		updateSource.setValid(DataValid.INVALID);
		return evaluatoionOrderMapper.updateById(updateSource);
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
	private LambdaQueryWrapper createWrapper(EvaluatoionOrderBean bean) {
		LambdaQueryWrapper<EvaluatoionOrder> wrapper = Wrappers.lambdaQuery();
		if (bean == null || bean.getValid() == null) {
//			wrapper.eq(EvaluatoionCode::getValid, DataValid.VALID);
			wrapper.apply("evaluatoion_order.valid= {0}", DataValid.VALID);
		}

		// 自定义条件
		if (bean != null) {
			if (bean.getValid() != null) {
//				wrapper.eq(EvaluatoionCode::getValid, bean.getValid());
				wrapper.apply("evaluatoion_order.valid= {0}", bean.getValid());
			}
			if (!ObjectUtils.isEmpty(bean.getStatus())) {
				if (bean.getStatus().equals("!" + CpStatus.COMPLETE)) {
					List<String> statusFilter = new ArrayList<String>();
					statusFilter.add(CpStatus.INIT);
					statusFilter.add(CpStatus.ING);
					wrapper.in(EvaluatoionOrder::getStatus, statusFilter);
				} else {
					wrapper.eq(EvaluatoionOrder::getStatus, bean.getStatus());
				}
			}
			if (!ObjectUtils.isEmpty(bean.getCode())) {
				wrapper.eq(EvaluatoionOrder::getCode, bean.getCode());
			}
			if (!ObjectUtils.isEmpty(bean.getEvaluatoionCodeCode())) {
				wrapper.eq(EvaluatoionOrder::getEvaluatoionCodeCode, bean.getEvaluatoionCodeCode());
			}
			if (CollectionUtils.isNotEmpty(bean.getEvaluatoionOrderList())) {
				wrapper.in(EvaluatoionOrder::getCode, bean.getEvaluatoionOrderList());
			}
			if (!ObjectUtils.isEmpty(bean.getEvaluatoionCode())) {
				wrapper.like(EvaluatoionOrder::getEvaluatoionCode, bean.getEvaluatoionCode());
//				wrapper.apply("evaluatoion_code.evaluatoion_code like {0}", bean.getEvaluatoionCode());
//				wrapper.eq(EvaluatoionCode::getEvaluatoionCode, bean.getEvaluatoionCode());
			}
			if (!ObjectUtils.isEmpty(bean.getStartDate())) {
				wrapper.apply("evaluatoion_order.start_date>='" + bean.getStartDate() + " 00:00:00'");
			}
			if (!ObjectUtils.isEmpty(bean.getEndDate())) {
				wrapper.apply("evaluatoion_order.end_date<='" + bean.getEndDate() + " 00:00:00'");
			}
			if (!ObjectUtils.isEmpty(bean.getProductCode())) {
				wrapper.apply("evaluatoion_order.product_code={0}", bean.getProductCode());
			}
			// 编写条件逻辑....

		}

		return wrapper;
	}

}
