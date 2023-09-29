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
import com.hh.appraisal.springboot.core.constant.DataValid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.hh.appraisal.springboot.bean.ProductBean;
import com.hh.appraisal.springboot.bean.ReportBean;
import com.hh.appraisal.springboot.bean.ReportConfigBean;
import com.hh.appraisal.springboot.bean.ReportConfigCatBean;
import com.hh.appraisal.springboot.bean.ReportPanelBean;
import com.hh.appraisal.springboot.bean.system.SysDictBean;
import com.hh.appraisal.springboot.bean.system.SysDictTypeBean;
import com.hh.appraisal.springboot.entity.Product;
import com.hh.appraisal.springboot.entity.Report;
import com.hh.appraisal.springboot.entity.system.SysDict;
import com.hh.appraisal.springboot.dao.ProductMapper;
import com.hh.appraisal.springboot.dao.ReportMapper;
import com.hh.appraisal.springboot.dao.system.SysDictMapper;
import com.hh.appraisal.springboot.service.ProductService;
import com.hh.appraisal.springboot.service.ReportConfigService;
import com.hh.appraisal.springboot.service.ReportService;
import com.hh.appraisal.springboot.service.system.SysDictService;
import com.hh.appraisal.springboot.service.system.SysDictTypeService;

/**
 * Report Service 实现类
 * 
 * @author gaigai
 * @date 2023/06/02
 */
@Slf4j
@Service
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements ReportService {

	private final ReportMapper reportMapper;
	


	@Autowired
	private ReportConfigService reportConfigService;

	@Autowired
	private SysDictTypeService sysDictTypeService;

	@Autowired
	private SysDictService sysDictService;
	
	@Autowired
	private SysDictMapper sysDictMapper;

	@Autowired
	private ProductService productService;

	public ReportServiceImpl(ReportMapper reportMapper) {
		this.reportMapper = reportMapper;
	}
	
	

	@Override
	public ReportBean findByCode(String code) {
		if (ObjectUtils.isEmpty(code)) {
			return null;
		}

		List<Report> sourceList = reportMapper.selectList(createWrapper(ReportBean.builder().code(code).build()));
		if (ObjectUtils.isEmpty(sourceList)) {
			return null;
		}

		ReportBean restBean = ReportBean.builder().build();
		BeanUtils.copyProperties(sourceList.get(0), restBean);
		return restBean;
	}

	@Override
	public List<ReportBean> findByCodeList(List<String> codeList) {
		if (CollectionUtils.isEmpty(codeList)) {
			return Collections.EMPTY_LIST;
		}

		List<Report> sourceList = reportMapper
				.selectList(createWrapper(ReportBean.builder().reportCodeList(codeList).build()));
		if (CollectionUtils.isEmpty(sourceList)) {
			return Collections.EMPTY_LIST;
		}

		List<ReportBean> restBeanList = sourceList.stream().map(v -> {
			ReportBean item = new ReportBean();
			BeanUtils.copyProperties(v, item);
			return item;
		}).collect(Collectors.toList());
		return restBeanList;
	}

	@Override
	public List<ReportBean> findList(ReportBean bean) {
		List<Report> list = reportMapper.selectList(createWrapper(bean));
		if (CollectionUtils.isEmpty(list)) {
			return Collections.EMPTY_LIST;
		}

		List<ReportBean> beanList = list.stream().map(item -> {
			ReportBean srcBean = new ReportBean();
			BeanUtils.copyProperties(item, srcBean);
			return srcBean;
		}).collect(Collectors.toList());
		return beanList;
	}

	@Override
	public Page<ReportBean> findPage(ReportBean bean, PageBean pageBean) {
		Page<Report> sourcePage = reportMapper.selectPage(new Page<>(pageBean.getCurrent(), pageBean.getSize()),
				createWrapper(bean));
		if (CollectionUtils.isEmpty(sourcePage.getRecords())) {
			return new Page<>();
		}
		Page<ReportBean> restPage = new Page<>();
		BeanUtils.copyProperties(sourcePage, restPage, "records");
		restPage.setRecords(new ArrayList<>(sourcePage.getRecords().size()));
		sourcePage.getRecords().forEach(v -> {
			ReportBean itemBean = new ReportBean();
			BeanUtils.copyProperties(v, itemBean);
			List<String> codelist = Arrays.asList(itemBean.getProductCode().split(","));
			List<ProductBean> productList = productService.findByCodeList(codelist);
			String productNameString = "";
			for (int i = 0; i < productList.size(); i++) {
				ProductBean product = productList.get(i);
				String productName = product.getProductName();
				productNameString += productName + ",";
			}
			if (productNameString.length() > 0) {
				productNameString = productNameString.substring(0, productNameString.length() - 1);
			}
			itemBean.setProductName(productNameString);
			restPage.getRecords().add(itemBean);
		});
		return restPage;
	}

	@Transactional
	@Override
	public ReportBean add(ReportBean bean) {
		List<String> productCodeList = bean.getProductCodeList();
		String productString = "";
		String productNameString = "";
		for (Iterator iterator = productCodeList.iterator(); iterator.hasNext();) {
			String product = (String) iterator.next();
			productString += product + ",";
		}
		if (productString.length() > 0) {
			productString = productString.substring(0, productString.length() - 1);
		}
		bean.setProductCode(productString);
		Report source = new Report();
		BeanUtils.copyProperties(bean, source);
		reportMapper.insert(source);
		BeanUtils.copyProperties(source, bean);
		addreportConfigBean("REPORT_TYPE", bean);
		addreportConfigBean("ALL_USER_INFO_COLUMN", bean);
		return bean;
	}

	public void addreportConfigBean(String dictTypeName, ReportBean bean) {
		SysDictTypeBean typeBean = new SysDictTypeBean();
		typeBean.setNum(dictTypeName);
		typeBean = sysDictTypeService.findOne(typeBean);
		SysDictBean SysDictBean=new SysDictBean();
		SysDictBean.setTypeCode(typeBean.getCode());
		List<SysDict> listSysDictBean = sysDictMapper.selectList(new QueryWrapper<SysDict>().eq("TYPE_CODE", typeBean.getCode()).orderByAsc("SORT"));
		for (Iterator iterator = listSysDictBean.iterator(); iterator.hasNext();) {
			ReportConfigBean reportConfigBean = new ReportConfigBean();
			SysDict sysDictBean = (SysDict) iterator.next();
			reportConfigBean.setReportCode(bean.getCode());
			reportConfigBean.setReportConfigPartName(typeBean.getName());
			reportConfigBean.setReportConfigPartColName(sysDictBean.getName());
			reportConfigBean.setReportConfigPartColCode(sysDictBean.getCode());
			reportConfigBean.setReportConfigPartColNameen(sysDictBean.getValue());
			reportConfigBean.setReportConfigPartCode(typeBean.getNum());
			// 默认为N
			reportConfigBean.setReportConfigPartColValue("N");
			reportConfigBean.setReportConfigPartColOrderno(sysDictBean.getSort());			
			reportConfigService.add(reportConfigBean);
		}
	}

	@Transactional
	@Override
	public int updateByCode(ReportBean bean) {
		Report source = new Report();
		BeanUtils.copyProperties(bean, source);
		return reportMapper.updateById(source);
	}

	@Transactional
	@Override
	public int deleteByCode(String code) {
		if (ObjectUtils.isEmpty(code)) {
			return 0;
		}

		Report updateSource = new Report();
		updateSource.setCode(code);
		updateSource.setValid(DataValid.INVALID);
		return reportMapper.updateById(updateSource);
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
	private LambdaQueryWrapper createWrapper(ReportBean bean) {
		LambdaQueryWrapper<Report> wrapper = Wrappers.lambdaQuery();
		if (bean == null || bean.getValid() == null) {
			wrapper.eq(Report::getValid, DataValid.VALID);
		}

		// 自定义条件
		if (bean != null) {
			if (bean.getValid() != null) {
				wrapper.eq(Report::getValid, bean.getValid());
			}
			if (!ObjectUtils.isEmpty(bean.getCode())) {
				wrapper.eq(Report::getCode, bean.getCode());
			}
			if (CollectionUtils.isNotEmpty(bean.getReportCodeList())) {
				wrapper.in(Report::getCode, bean.getReportCodeList());
			}

			// 编写条件逻辑....

		}

		return wrapper;
	}

	@Override
	@Transactional
	public int updateReportConfigByCode(List<ReportConfigCatBean> catlist) {
		// TODO Auto-generated method stub
		int count = 0;
		for (Iterator iterator = catlist.iterator(); iterator.hasNext();) {
			ReportConfigCatBean reportConfigCatBean = (ReportConfigCatBean) iterator.next();
			List<ReportConfigBean> list = reportConfigCatBean.getReportConfig();
			for (Iterator iterator2 = list.iterator(); iterator2.hasNext();) {
				ReportConfigBean reportConfigBean = (ReportConfigBean) iterator2.next();
				count += reportConfigService.updateByCode(reportConfigBean);
			}
		}
		return count;
	}

}
