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
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.hh.appraisal.springboot.bean.ReportBean;
import com.hh.appraisal.springboot.bean.ReportConfigBean;
import com.hh.appraisal.springboot.bean.ReportConfigCatBean;
import com.hh.appraisal.springboot.bean.ReportPanelBean;
import com.hh.appraisal.springboot.bean.system.SysDictBean;
import com.hh.appraisal.springboot.bean.system.SysDictTypeBean;
import com.hh.appraisal.springboot.entity.Report;
import com.hh.appraisal.springboot.dao.ReportMapper;
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
			restPage.getRecords().add(itemBean);
		});
		return restPage;
	}

	@Transactional
	@Override
	public ReportBean add(ReportBean bean) {
		List<String> productCodeList = bean.getProductCodeList();
		String productString = "";
		for (Iterator iterator = productCodeList.iterator(); iterator.hasNext();) {
			String product = (String) iterator.next();
			productString += product + ",";
		}
		if (productString.length() > 0) {
			productString = productString.substring(0, productString.length() - 1);
		}
		Report source = new Report();
		BeanUtils.copyProperties(bean, source);
		reportMapper.insert(source);
		BeanUtils.copyProperties(source, bean);
		addreportConfigBean("REPORT_TYPE", bean);
		addreportConfigBean("ALL_USER_INFO_COLUMN", bean);
		return bean;
	}

	public void addreportConfigBean(String dictTypeName,ReportBean bean) {
		SysDictTypeBean typeBean = new SysDictTypeBean();
		typeBean.setNum(dictTypeName);
		typeBean = sysDictTypeService.findOne(typeBean);
		List<SysDictBean> listSysDictBean = sysDictService
				.findList(SysDictBean.builder().typeCode(typeBean.getCode()).build());
		for (Iterator iterator = listSysDictBean.iterator(); iterator.hasNext();) {
			ReportConfigBean reportConfigBean=new ReportConfigBean();
			SysDictBean sysDictBean = (SysDictBean) iterator.next();
			reportConfigBean.setReportCode(bean.getCode());
			reportConfigBean.setReportConfigPartName(typeBean.getName());
			reportConfigBean.setReportConfigPartColName(sysDictBean.getName());
			reportConfigBean.setReportConfigPartColCode(sysDictBean.getCode());
			reportConfigBean.setReportConfigPartCode(typeBean.getNum());
			//默认为N
			reportConfigBean.setReportConfigPartColValue("N");
			reportConfigService.add(reportConfigBean);
		}
	}
	
	@Transactional
	@Override
	public int updateByCode(ReportBean bean) {
		Report source = new Report();
		BeanUtils.copyProperties(bean, source);
		List<ReportConfigCatBean> catlist = bean.getReportConfigCatList();
		for (Iterator iterator = catlist.iterator(); iterator.hasNext();) {
			ReportConfigCatBean reportConfigCatBean = (ReportConfigCatBean) iterator.next();
			List<ReportConfigBean> list = reportConfigCatBean.getReportConfig();
			for (Iterator iterator2 = list.iterator(); iterator2.hasNext();) {
				ReportConfigBean reportConfigBean = (ReportConfigBean) iterator2.next();
//				if (ObjectUtils.isEmpty(reportConfigBean.getCode())) {
//					reportConfigBean.setReportConfigPartName(reportConfigCatBean.getReportConfigPartName());
//					reportConfigBean.setReportCode(bean.getCode());
//					reportConfigService.add(reportConfigBean);
//				} else {
				reportConfigService.updateByCode(reportConfigBean);
//				}
			}
		}
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

}
