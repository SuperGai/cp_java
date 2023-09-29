package com.hh.appraisal.springboot.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlLike;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hh.appraisal.springboot.core.baen.PageBean;
import com.hh.appraisal.springboot.core.constant.DataValid;
import com.hh.appraisal.springboot.core.utils.NumberUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.hh.appraisal.springboot.bean.EvaluatoionCodeBean;
import com.hh.appraisal.springboot.bean.EvaluatoionOrderBean;
import com.hh.appraisal.springboot.bean.ProductBean;
import com.hh.appraisal.springboot.bean.QuestionBean;
import com.hh.appraisal.springboot.bean.SchoolBean;
import com.hh.appraisal.springboot.bean.UserAnswersBean;
import com.hh.appraisal.springboot.constant.CpStatus;
import com.hh.appraisal.springboot.entity.EvaluatoionCode;
import com.hh.appraisal.springboot.entity.EvaluatoionOrder;
import com.hh.appraisal.springboot.entity.Product;
import com.hh.appraisal.springboot.dao.EvaluatoionCodeMapper;
import com.hh.appraisal.springboot.dao.SchoolMapper;
import com.hh.appraisal.springboot.service.EvaluatoionCodeService;
import com.hh.appraisal.springboot.service.EvaluatoionOrderService;
import com.hh.appraisal.springboot.service.ProductService;
import com.hh.appraisal.springboot.service.QuestionService;
import com.hh.appraisal.springboot.service.SchoolService;
import com.hh.appraisal.springboot.service.UserAnswersService;
import com.hh.appraisal.springboot.utils.GenerateCodeUtils;

/**
 * EvaluatoionCode Service 实现类
 * 
 * @author gaigai
 * @date 2021/06/26
 */
@Slf4j
@Service
public class EvaluatoionCodeServiceImpl extends ServiceImpl<EvaluatoionCodeMapper, EvaluatoionCode>
		implements EvaluatoionCodeService {

	private final EvaluatoionCodeMapper evaluatoionCodeMapper;
	@Autowired
	private SchoolService schoolService;

	@Autowired
	private ProductService productService;

	@Autowired
	private EvaluatoionOrderService evaluatoionOrderService;

	@Autowired
	private QuestionService questionService;

	@Autowired
	private UserAnswersService userAnswersService;

	public EvaluatoionCodeServiceImpl(EvaluatoionCodeMapper evaluatoionCodeMapper) {
		this.evaluatoionCodeMapper = evaluatoionCodeMapper;
	}

	@Override
	public EvaluatoionCodeBean findByCode(String code) {
		if (ObjectUtils.isEmpty(code)) {
			return null;
		}

		List<EvaluatoionCode> sourceList = evaluatoionCodeMapper
				.selectList(createWrapper(EvaluatoionCodeBean.builder().code(code).build()));
		if (ObjectUtils.isEmpty(sourceList)) {
			return null;
		}

		EvaluatoionCodeBean restBean = EvaluatoionCodeBean.builder().build();
		BeanUtils.copyProperties(sourceList.get(0), restBean);
		return restBean;
	}

	@Override
	public List<EvaluatoionCodeBean> findByCodeList(List<String> codeList) {
		if (CollectionUtils.isEmpty(codeList)) {
			return Collections.EMPTY_LIST;
		}

		List<EvaluatoionCode> sourceList = evaluatoionCodeMapper
				.selectList(createWrapper(EvaluatoionCodeBean.builder().evaluatoionCodeCodeList(codeList).build()));
		if (CollectionUtils.isEmpty(sourceList)) {
			return Collections.EMPTY_LIST;
		}

		List<EvaluatoionCodeBean> restBeanList = sourceList.stream().map(v -> {
			EvaluatoionCodeBean item = new EvaluatoionCodeBean();
			BeanUtils.copyProperties(v, item);
			return item;
		}).collect(Collectors.toList());
		return restBeanList;
	}

	@Override
	public List<EvaluatoionCodeBean> findList(EvaluatoionCodeBean bean) {
		List<EvaluatoionCode> list = evaluatoionCodeMapper.selectListAll(createWrapper(bean));
		if (CollectionUtils.isEmpty(list)) {
			return Collections.EMPTY_LIST;
		}

		List<EvaluatoionCodeBean> beanList = list.stream().map(item -> {
			EvaluatoionCodeBean srcBean = new EvaluatoionCodeBean();
			BeanUtils.copyProperties(item, srcBean);
			return srcBean;
		}).collect(Collectors.toList());
		return beanList;
	}

	@Override
	public Page<EvaluatoionCodeBean> findPage(EvaluatoionCodeBean bean, PageBean pageBean) {
		Page<EvaluatoionCode> sourcePage = evaluatoionCodeMapper
				.selectPageAll(new Page<>(pageBean.getCurrent(), pageBean.getSize()), createWrapper(bean));
//				.selectPage(new Page<>(pageBean.getCurrent(), pageBean.getSize()), createWrapper(bean));
		if (CollectionUtils.isEmpty(sourcePage.getRecords())) {
			return new Page<>();
		}
		Page<EvaluatoionCodeBean> restPage = new Page<>();
		BeanUtils.copyProperties(sourcePage, restPage, "records");
		restPage.setRecords(new ArrayList<>(sourcePage.getRecords().size()));
		sourcePage.getRecords().forEach(v -> {
			EvaluatoionCodeBean itemBean = new EvaluatoionCodeBean();
			BeanUtils.copyProperties(v, itemBean);
			restPage.getRecords().add(itemBean);
		});
		return restPage;
	}

	@Transactional
	@Override
	public EvaluatoionCodeBean add(EvaluatoionCodeBean bean) {
		// 授权码生成
		SchoolBean schoolBean = schoolService.findByCode(bean.getShoolCode());
		String code = GenerateCodeUtils.generateShortUuid(schoolBean.getSchoolCode().toUpperCase());
		bean.setEvaluatoionCode(code);
		bean.setIsused("N");
		EvaluatoionCode source = new EvaluatoionCode();
		BeanUtils.copyProperties(bean, source);
		evaluatoionCodeMapper.insert(source);
		BeanUtils.copyProperties(source, bean);
		// 获取对应的产品集合
		List<String> productList = bean.getProductCode();
		// 在构建掩饰性时不需要放进去的逻辑推理跟掩饰性、动力需求。
		ProductBean ysxProduct = null;
		List<String> removeList = new ArrayList<String>();
		for (Iterator iterator = productList.iterator(); iterator.hasNext();) {
			String productCode = (String) iterator.next();
			EvaluatoionOrderBean orderBean = new EvaluatoionOrderBean();
			orderBean.setProductCode(productCode);
			orderBean.setEvaluatoionCodeCode(bean.getCode());
			orderBean.setEvaluatoionCode(code);
			ProductBean product = productService.findByCode(productCode);
			if (product.getProductName().equals("掩饰性")) {
				source.setIsHaveYsx(product.getCode());
				ysxProduct = product;
			} else {
				if (product.getProductName().equals("动力需求") || product.getProductName().equals("逻辑推理部分")) {
					removeList.add(product.getCode());
				}
				orderBean.setStatus(CpStatus.INIT);
				evaluatoionOrderService.add(orderBean);
			}

		}
		// 判断是否含有掩饰性
		evaluatoionCodeMapper.updateById(source);

		// 获取当前测评的订单，判断是否存在掩饰性产品。
		if (ysxProduct != null) {
			// 查询题目
			QuestionBean question = new QuestionBean();
			question.setQuestionBank(ysxProduct.getCode());
			List<QuestionBean> questionList = questionService.findList(question);
			int totalQuantity = ysxProduct.getQuestionNum();
			QueryWrapper q = new QueryWrapper<EvaluatoionOrder>().eq("evaluatoion_code", code).ne("product_code",
					ysxProduct.getCode());
			for (int i = 0; i < removeList.size(); i++) {
				q.ne("product_code", removeList.get(i));
			}
			List<EvaluatoionOrder> allOrder = evaluatoionOrderService.list(q);
			int[] baskets = new int[allOrder.size()]; // 3个篮子
			Random random = new Random();
			for (int i = 0; i < totalQuantity; i++) {
				int basketIndex = random.nextInt(allOrder.size()); // 生成0到2之间的随机数
				baskets[basketIndex]++; // 将数量放入选中的篮子
			}
			// 检查总数量是否与待分配的数量一致
			int finalTotalQuantity = 0;
			for (int basket : baskets) {
				finalTotalQuantity += basket;
			}
			// 如果总数量与待分配的数量不一致，将多余的数量放入篮子1
			while (finalTotalQuantity != totalQuantity) {
				baskets[0]++;
				finalTotalQuantity++;
			}
			int numbers[] = NumberUtils.randomArray(1, ysxProduct.getQuestionNum(), ysxProduct.getQuestionNum());
			int orderIndex = 0;
			for (int i = 0; i < allOrder.size(); i++) {
				EvaluatoionOrder eorder = allOrder.get(i);
				int count = baskets[i];
				int nowcount = 0;
				ProductBean product = productService.findByCode(eorder.getProductCode());
				int productNo = product.getQuestionNum();
				for (int j = orderIndex; j < numbers.length; j++) {
					productNo = productNo + 1;
					QuestionBean questionBean = questionList.get(j);
					UserAnswersBean userAnswersBean = new UserAnswersBean();
					userAnswersBean.setEvaluationUserCode(code);
					userAnswersBean.setProductCode(ysxProduct.getCode());
					userAnswersBean.setQuestionCode(questionBean.getCode());
					userAnswersBean.setQuestionNo(productNo);
					userAnswersBean.setIsComplete("N");
					userAnswersBean.setProductCodeReal(eorder.getProductCode());
					userAnswersService.add(userAnswersBean);
					nowcount++;
					orderIndex++;
					if (nowcount >= count) {
						break;
					}
				}
				eorder.setYsxNumber(nowcount);
				evaluatoionOrderService.saveOrUpdate(eorder);
			}
		}
		return bean;
	}

	@Transactional
	@Override
	public int updateByCode(EvaluatoionCodeBean bean) {
		EvaluatoionCode source = new EvaluatoionCode();
		BeanUtils.copyProperties(bean, source);
		return evaluatoionCodeMapper.updateById(source);
	}

	@Transactional
	@Override
	public int deleteByCode(String code) {
		if (ObjectUtils.isEmpty(code)) {
			return 0;
		}

		EvaluatoionCode updateSource = new EvaluatoionCode();
		updateSource.setCode(code);
		updateSource.setValid(DataValid.INVALID);
		return evaluatoionCodeMapper.updateById(updateSource);
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
	private LambdaQueryWrapper createWrapper(EvaluatoionCodeBean bean) {
		LambdaQueryWrapper<EvaluatoionCode> wrapper = Wrappers.lambdaQuery();
		if (bean == null || bean.getValid() == null) {
//			wrapper.eq(EvaluatoionCode::getValid, DataValid.VALID);
			wrapper.apply("evaluatoion_code.valid= {0}", DataValid.VALID);
		}

		// 自定义条件
		if (bean != null) {
			if (bean.getValid() != null) {
//				wrapper.eq(EvaluatoionCode::getValid, bean.getValid());
				wrapper.apply("evaluatoion_code.valid= {0}", bean.getValid());
			}
			if (!ObjectUtils.isEmpty(bean.getCode())) {
				wrapper.eq(EvaluatoionCode::getCode, bean.getCode());
			}
			if (CollectionUtils.isNotEmpty(bean.getEvaluatoionCodeCodeList())) {
				wrapper.in(EvaluatoionCode::getCode, bean.getEvaluatoionCodeCodeList());
			}
			if (!ObjectUtils.isEmpty(bean.getShoolCode())) {
				wrapper.apply("evaluatoion_code.shool_code= {0}", bean.getShoolCode());
//				wrapper.eq(EvaluatoionCode::getShoolCode, bean.getShoolCode());
			}
			if (!ObjectUtils.isEmpty(bean.getEvaluatoionCode())) {
				wrapper.like(EvaluatoionCode::getEvaluatoionCode, bean.getEvaluatoionCode());
//				wrapper.apply("evaluatoion_code.evaluatoion_code like {0}", bean.getEvaluatoionCode());
//				wrapper.eq(EvaluatoionCode::getEvaluatoionCode, bean.getEvaluatoionCode());
			}
			if (!ObjectUtils.isEmpty(bean.getIsused())) {
				wrapper.apply("evaluatoion_code.isused= {0}", bean.getIsused());
//				wrapper.eq(EvaluatoionCode::getIsused, bean.getIsused());
			}
			if (!ObjectUtils.isEmpty(bean.getStartDate())) {
				wrapper.apply("evaluatoion_code.start_date>='" + bean.getStartDate() + " 00:00:00'");
			}
			if (!ObjectUtils.isEmpty(bean.getEndDate())) {
				wrapper.apply("evaluatoion_code.end_date<='" + bean.getEndDate() + " 00:00:00'");
			}
			if (!ObjectUtils.isEmpty(bean.getProductCode())) {
				wrapper.apply("evaluatoion_code.product_code={0}", bean.getProductCode());
			}
			if (!ObjectUtils.isEmpty(bean.getPersonType())) {
				wrapper.like(EvaluatoionCode::getPersonType, bean.getPersonType());
			}
			// 编写条件逻辑....

		}

		return wrapper;
	}

	@Transactional
	@Override
	public int addList(EvaluatoionCodeBean bean, int number) {
		// TODO Auto-generated method stub
		for (int i = 0; i < number; i++) {
			SchoolBean schoolBean = schoolService.findByCode(bean.getShoolCode());
			String code = GenerateCodeUtils.generateShortUuid(schoolBean.getSchoolCode().toUpperCase());
			bean.setEvaluatoionCode(code);
			bean.setIsused("N");
			EvaluatoionCode source = new EvaluatoionCode();
			BeanUtils.copyProperties(bean, source);
			evaluatoionCodeMapper.insert(source);
			// 获取对应的产品集合
			List<String> productList = bean.getProductCode();
			for (Iterator iterator = productList.iterator(); iterator.hasNext();) {
				String productCode = (String) iterator.next();
				EvaluatoionOrderBean orderBean = new EvaluatoionOrderBean();
				orderBean.setProductCode(productCode);
				orderBean.setEvaluatoionCodeCode(bean.getCode());
				orderBean.setEvaluatoionCode(code);
				orderBean.setStatus(CpStatus.INIT);
				evaluatoionOrderService.add(orderBean);
			}
		}
		return number;
	}
}
