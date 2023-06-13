package com.hh.appraisal.springboot.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hh.appraisal.springboot.bean.DivisorAverageBean;
import com.hh.appraisal.springboot.bean.ProductBean;
import com.hh.appraisal.springboot.bean.SchoolBean;
import com.hh.appraisal.springboot.core.config.FileConfig;
import com.hh.appraisal.springboot.core.utils.PoiWordTools;
import com.hh.appraisal.springboot.dao.DivisorMapper;
import com.hh.appraisal.springboot.dao.EvaluatoionCodeMapper;
import com.hh.appraisal.springboot.dao.UserAnswersMapper;
import com.hh.appraisal.springboot.entity.EvaluatoionCode;
import com.hh.appraisal.springboot.entity.EvaluatoionOrder;
import com.hh.appraisal.springboot.entity.EvaluatoionUser;
import com.hh.appraisal.springboot.entity.Report;
import com.hh.appraisal.springboot.entity.ReportConfig;
import com.hh.appraisal.springboot.utils.WordToPdf;

@Service
//生成PDF线程类
public class GeneraPdfModel2AsyncTaskService {

	@Autowired
	EvaluatoionUserService evaluatoionUserService;

	@Autowired
	EvaluatoionCodeMapper evaluatoionCodeMapper;

	@Autowired
	UserAnswersMapper userAnswersMapper;

	@Autowired
	SchoolService schoolService;

	@Autowired
	DivisorMapper divisorMapper;

	@Autowired
	FileConfig fileConfig;
	
	@Autowired
    ReportService reportService;
	
	@Autowired
	EvaluatoionOrderService evaluatoionOrderService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	ReportConfigService reportConfigService;

	@Async
	public void executeAsyncTask(String evaluationUserCode) throws Exception {
		
		// 需要替换的文本
		Map<String, String> textMap = new HashMap<String, String>();
		
		//查询出此次测评对应的所有产品
		List<EvaluatoionOrder> orderList= evaluatoionOrderService.list(new QueryWrapper<EvaluatoionOrder>().eq("EVALUATOION_CODE", evaluationUserCode));
		//获取所有的测评产品信息
		Map<String, ProductBean> productMap=new HashMap<String, ProductBean>();
		String productName="";
		for (int i = 0; i < orderList.size(); i++) {
			EvaluatoionOrder order = orderList.get(i);
			String productCode=order.getProductCode();
			ProductBean product=productService.findByCode(productCode);
			productMap.put(product.getCode(), product);
			productName+=product.getProductName()+",";
		}
		if(productName.length()>0) {
			productName=productName.substring(0,productName.length()-1);
		}
		//获取对应的报告名称
		Report report= reportService.getOne(new QueryWrapper<Report>().eq("PRODUCT_CODE", productName));
		textMap.put("reportName", report.getReportName());
		//个人信息
		//获取需要在报告体现的用户信息的配置
		List<ReportConfig> reportConfigUserInfoList=reportConfigService.list(new QueryWrapper<ReportConfig>().eq("REPORT_CODE", report.getCode()).eq("REPORT_CONFIG_PART_COL_VALUE", "Y").eq("REPORT_CONFIG_PART_CODE", "ALL_USER_INFO_COLUMN"));
		String strDateFormat = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
		EvaluatoionUser evaluatoionUser = evaluatoionUserService
				.getOne(new QueryWrapper<EvaluatoionUser>().eq("EVALUATOION_CODE", evaluationUserCode));
		// 转化成json对象
		JSONObject json = (JSONObject) JSON.toJSON(evaluatoionUser);
		for (String key : json.keySet()) {
			String value = json.getString(key);
			if (key.equals("birthdate")) {
				value = sdf.format(evaluatoionUser.getBirthdate());
				//测评日期
			} else if (key.equals("createTime")) {
				value = sdf.format(evaluatoionUser.getCreateTime());
			}
			textMap.put(key, value);
		}
		
		//目录
		List<ReportConfig> reportConfigPageList=reportConfigService.list(new QueryWrapper<ReportConfig>().eq("REPORT_CODE", report.getCode()).eq("REPORT_CONFIG_PART_COL_VALUE", "Y").eq("REPORT_CONFIG_PART_CODE", "ALL_USER_INFO_COLUMN"));
		
		
		
		
		
		//接下来的模块配置 
		//报告简介、阅读建议
		
		//答题情况（有表格）
		
		
		
		
		
		
		//获取展示在报告里的配置
		
		
		
		
		
		
		
		
		
		
		
		
		
//		// 获取logo地址
//		EvaluatoionCode evaluatoionCode = evaluatoionCodeMapper
//				.selectOne(new QueryWrapper<EvaluatoionCode>().eq("evaluatoion_Code", evaluationUserCode));
//		SchoolBean school = schoolService.findByCode(evaluatoionCode.getShoolCode());
//		// 用户个人信息
//		// 替换文本
//		Map<String, String> textMap = new HashMap<String, String>();
//		for (String key : json.keySet()) {
//			String value = json.getString(key);
//			if (key.equals("birthdate")) {
//				value = sdf.format(evaluatoionUser.getBirthdate());
//			} else if (key.equals("createTime")) {
//				value = sdf.format(evaluatoionUser.getCreateTime());
//			}
//			textMap.put(key, value);
//		}
//		textMap.put("school", school.getSchoolName());
//		String logo = fileConfig.pdfUrl + school.getLogo();
//		// 替换logo
//		Map<String, String> imgMap = new HashMap<String, String>();
//		List<DivisorAverageBean> divisorAverageBean = userAnswersMapper.getDivisorListInfo(evaluationUserCode);
//		InputStream is = new FileInputStream(new File(fileConfig.reportModel));
//		XWPFDocument doc = new XWPFDocument(is);
//		JSONArray divisors = (JSONArray) JSON.toJSON(divisorAverageBean);
//		// 数据准备
//		List<String> titleArr = new ArrayList<String>();// 标题
//		titleArr.add("");
//		titleArr.add("指标偏向图表");
//		List<String> fldNameArr = new ArrayList<String>();// 字段名(数据有多少列，就多少个)
//		fldNameArr.add("item1");
//		fldNameArr.add("item2");
//		// 数据集合
//		List<Map<String, String>> listItemsByType = new ArrayList<Map<String, String>>();
//		JSONArray items = new JSONArray();
//		for (int i = 0; i < divisorAverageBean.size(); i++) {
//			DivisorAverageBean bean = divisorAverageBean.get(i);
//			// 第一行数据
//			Map<String, String> base = new HashMap<String, String>();
//			base.put("item1", bean.getDivisorName());
//			base.put("item2", bean.getDivisorAverag() + "");
//			JSONObject item = new JSONObject();
//			item.put("key", bean.getDivisorName());
//			item.put("value", bean.getDivisorAverag());
//			items.add(item);
//			listItemsByType.add(base);
//		}
//
////        XWPFDocument doc, List<String> titleArr, List<String> fldNameArr, List<Map<String, String>> listItemsByType,Map<String, String> textMap, Map<String, String> imgMap
//		// 替换word模板数据
//
//		String reportName = "中学生心理健康测评报告-" + evaluatoionUser.getName() + "-" + evaluatoionUser.getEvaluatoionCode();
//		String wordUrl = fileConfig.reportOutFolder + reportName + ".docx";
//		String pdfUrl = fileConfig.reportOutFolder + reportName + ".pdf";
//		String imageURL = fileConfig.reportOutFolder + reportName + ".png";
//		String xlsModel = fileConfig.reportOutFolder + "/model.xlsx";
//		String xlsOutPath = fileConfig.reportOutFolder + reportName + ".xlsx";
//		imgMap.put("整体测评情况图示参照下图表替换位置", imageURL);
//		// 处理EXCEL
//		PoiWordTools.dealExcel(xlsModel, xlsOutPath, items);
//		WordToPdf.xls2image(xlsOutPath, imageURL);
//		PoiWordTools.replaceAll(doc, logo, titleArr, fldNameArr, listItemsByType, textMap, imgMap, divisors);
//		// 保存结果文件
//		try {
//			FileOutputStream fos = new FileOutputStream(wordUrl);
//			doc.write(fos);
//			fos.close();
//			WordToPdf.doc2pdf(wordUrl, pdfUrl);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		evaluatoionUser.setReportUrl("file/report/" + reportName + ".pdf");
//		evaluatoionUser.setUrl(pdfUrl);
//		evaluatoionUserService.saveOrUpdate(evaluatoionUser);

	}

}
