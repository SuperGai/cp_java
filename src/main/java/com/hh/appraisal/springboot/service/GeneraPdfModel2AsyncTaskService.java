package com.hh.appraisal.springboot.service;

import java.text.SimpleDateFormat;
import java.util.*;

import com.aspose.words.BreakType;
import com.aspose.words.DocumentBuilder;
import com.hh.appraisal.springboot.core.utils.ChartUtils;
import com.hh.appraisal.springboot.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspose.words.Document;
import com.aspose.words.FontInfoCollection;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hh.appraisal.springboot.bean.DivisorCatBean;
import com.hh.appraisal.springboot.bean.DivisorCatItemBean;
import com.hh.appraisal.springboot.bean.ProductBean;
import com.hh.appraisal.springboot.bean.ReportDivisorInfoCatBean;
import com.hh.appraisal.springboot.core.config.FileConfig;
import com.hh.appraisal.springboot.core.report.config.Model2Config;
import com.hh.appraisal.springboot.core.report.config.ReportPart;
import com.hh.appraisal.springboot.core.utils.ReportModel2Utils;
import com.hh.appraisal.springboot.dao.DivisorMapper;
import com.hh.appraisal.springboot.dao.EvaluatoionCodeMapper;
import com.hh.appraisal.springboot.dao.EvaluatoionOrderMapper;
import com.hh.appraisal.springboot.dao.NormManageMapper;
import com.hh.appraisal.springboot.dao.UserAnswersMapper;
import com.hh.appraisal.springboot.utils.WordToPdf;

import ch.qos.logback.classic.Logger;

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

	JSONArray divisorJsonArrayObject = null;
	Map<String, DivisorCatBean> catMap = null;

	@Autowired
	EvaluatoionOrderMapper evaluatoionOrderMapper;


	@Autowired
	private NormManageMapper normManageMapper;

	@Async
	public void executeAsyncTask(String evaluationUserCode) throws Exception {
		divisorJsonArrayObject = null;

		// 需要替换的文本
		Map<String, String> textMap = new HashMap<String, String>();
		// 查询出此次测评对应的所有产品
		List<EvaluatoionOrder> orderList = evaluatoionOrderService
				.list(new QueryWrapper<EvaluatoionOrder>().eq("EVALUATOION_CODE", evaluationUserCode));
//		// 获取所有的测评产品信息
//		String productName = "";
//		for (int i = 0; i < orderList.size(); i++) {
//			EvaluatoionOrder order = orderList.get(i);
//			String productCode = order.getProductCode();
//			productName += productCode + ",";
//		}
//		if (productName.length() > 0) {
//			productName = productName.substring(0, productName.length() - 1);
//		}

		String reportCuName = evaluatoionOrderMapper.getReportNameByUserCode(evaluationUserCode, orderList.size());
		// 获取对应的报告名称
		Report report = reportService.getOne(new QueryWrapper<Report>().eq("report_name", reportCuName));
		textMap.put("reportName", report.getReportName());
		// 个人信息
		// 获取需要在报告体现的用户信息的配置
		List<ReportConfig> reportConfigUserInfoList = reportConfigService.list(new QueryWrapper<ReportConfig>()
				.eq("REPORT_CODE", report.getCode()).eq("REPORT_CONFIG_PART_COL_VALUE", "Y")
				.eq("REPORT_CONFIG_PART_CODE", "ALL_USER_INFO_COLUMN").orderByAsc("REPORT_CONFIG_PART_COL_ORDERNO"));
		String strDateFormat = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
		EvaluatoionUser evaluatoionUser = evaluatoionUserService
				.getOne(new QueryWrapper<EvaluatoionUser>().eq("EVALUATOION_CODE", evaluationUserCode));
		// 转化成json对象
		JSONObject json = (JSONObject) JSON.toJSON(evaluatoionUser);
		for (String key : json.keySet()) {
			String value = json.getString(key);
			if (key.equals("birthdate")) {
				if (value != null && value.length() > 0) {
					value = sdf.format(evaluatoionUser.getBirthdate());
				}
				// 测评日期
			} else if (key.equals("createTime")) {
				if (value != null && value.length() > 0) {
					value = sdf.format(evaluatoionUser.getCreateTime());
				}
			}
			if (value != null && value.length() > 0) {
				textMap.put(key, value);
			}
		}
		// 初始化模板
		Document currentDocument = ReportModel2Utils.initDocument(Model2Config.modelPath);
		FontInfoCollection FONT = currentDocument.getFontInfos();
		for (int i = 0; i < FONT.getCount(); i++) {
			System.out.println(FONT.get(i).getName());
		}
		// 生成用户信息
		ReportModel2Utils.GenerateUserInfo(currentDocument, reportConfigUserInfoList, evaluatoionUser);
		// 所有报表模块
		List<ReportConfig> reportConfigPageList = reportConfigService.list(new QueryWrapper<ReportConfig>()
				.eq("REPORT_CODE", report.getCode()).eq("REPORT_CONFIG_PART_COL_VALUE", "Y")
				.eq("REPORT_CONFIG_PART_CODE", "REPORT_TYPE").orderByAsc("REPORT_CONFIG_PART_COL_ORDERNO"));
		for (int i = 0; i < reportConfigPageList.size(); i++) {
			ReportConfig reportConfig = reportConfigPageList.get(i);
			// aboutReport
			String reportColNameen = reportConfig.getReportConfigPartColNameen();
			// 关于报告
			String reportColName = reportConfig.getReportConfigPartColName();
			// 插入关于报告模板
//			if (reportColNameen.equals(ReportPart.ABOUT_REPORT)) {
//				DocumentBuilder builder = new DocumentBuilder(currentDocument);
//				builder.moveToDocumentEnd();
//				builder.insertBreak(BreakType.PAGE_BREAK);
//				String aboutReport = report.getAboutReport();
//				ReportModel2Utils.insertTitleAndHtml(currentDocument, reportColName, aboutReport);
//				// 插入报告简介
//			}
//			else if (reportColNameen.equals(ReportPart.REPORT_INFO)) {
//				String reportInfo = report.getReportInfo();
//				ReportModel2Utils.insertTitleAndHtml(currentDocument, reportColName, reportInfo);
//				// 插入阅读建议
//			} else if (reportColNameen.equals(ReportPart.REPORT_READ_SUGGEST)) {
////				DocumentBuilder builder = new DocumentBuilder(currentDocument);
////				builder.moveToDocumentEnd();
////				builder.insertBreak(BreakType.PAGE_BREAK);
//				String readReportSuggest = report.getReportReadSuggest();
//				ReportModel2Utils.insertTitleAndHtml(currentDocument, reportColName, readReportSuggest);
//				// 插入答题情况
//			}
		 if (reportColNameen.equals(ReportPart.DTQK)) {
				getAllData(evaluationUserCode);
				String errorMsg = "";
				textMap.put("YSX_DEFAULT", Model2Config.YsxRange);
				// 答题花费的总时长
				long DTSJ_VALUE = userAnswersMapper.getAllProductSpendTime(evaluationUserCode);
				// 产品的总时长
				long DTSJ_DEFAULT = userAnswersMapper.getProductTime(evaluationUserCode);
				// 换算成为秒
				long DTSJ_DEFAULT_S = DTSJ_DEFAULT * 60;
				// 答题时间的描述
				String DTSJ_DESC = "";
				boolean isDtsjError = false;
				double timeRatio = (double) DTSJ_VALUE / (double) DTSJ_DEFAULT_S;
				if (timeRatio >= 0.6) {
					DTSJ_DESC = Model2Config.middleTime;
				} else if (timeRatio >= 0.4 && timeRatio <= 0.59) {
					DTSJ_DESC = Model2Config.smallTime;
					isDtsjError = true;
				} else {
					DTSJ_DESC = Model2Config.lowTime;
					isDtsjError = true;
				}
				// 如果答题时间异常
				if (isDtsjError) {
					errorMsg = evaluatoionUser.getName() + "答题时间异常，报告结果的准确性需要进一步考察。";
				} else {
					errorMsg = evaluatoionUser.getName() + "答题时间在正常范围，报告具有较高的参考价值。";
				}
				// 答题数量
				long questionNum = userAnswersMapper.getOrderAllQuestionNumExceptYsx(evaluationUserCode);
				// 完成的答题数量
				long completeQuestionNum = userAnswersMapper.getCompleteQuestionNumExceptYsx(evaluationUserCode);
				// 答题比例
				double AnswerRatio = (double) questionNum / (double) completeQuestionNum;
				String DTSL_DESC = "";
				boolean isDtslError = false;
				if (AnswerRatio >= 0.95) {
					DTSL_DESC = Model2Config.answerMiddleNumber;
				} else {
					DTSL_DESC = Model2Config.answerLowNumber;
					isDtslError = true;
				}
				if (isDtslError) {
					errorMsg += evaluatoionUser.getName() + "存在部分题未完成，涉及指标需要进一步评估。";
				}
				int defaultHours = (int) (DTSJ_DEFAULT_S / 3600);
				int defaultMinutes = (int) (DTSJ_DEFAULT_S % 3600) / 60;
				String DTSJ_DEFAULT_TXT = defaultHours + "小时" + defaultMinutes + "分钟";
				int answerHours = (int) (DTSJ_VALUE / 3600);
				int answerMinutes = (int) (DTSJ_VALUE % 3600) / 60;
				String DTSJ_VALUE_TXT = answerHours + "小时" + answerMinutes + "分钟";
				textMap.put("DTSJ_DEFAULT", DTSJ_DEFAULT_TXT);
				textMap.put("DTSJ_VALUE", DTSJ_VALUE_TXT);
				textMap.put("DTSL_DEFAULT", questionNum + "");
				textMap.put("DTSL_VALUE", completeQuestionNum + "");
				textMap.put("DTSJ_DESC", DTSJ_DESC);
				textMap.put("DTSL_DESC", DTSL_DESC);
				textMap.put("YSX_DEFAULT", Model2Config.YsxRange);
				// 获取掩饰性得分
				long ysxScore = (long) catMap.get("掩饰性").getCatValue();
				// 转换成9分制
//				long ysxLevel = userAnswersMapper.getDivisorNormLevel(evaluationUserCode, ysxScore, "掩饰性");
				textMap.put("YSX_VALUE", ysxScore + "");
				// 获取9分制对应的文本
				String ysxDesc = userAnswersMapper.getYsxDesc(evaluationUserCode, ysxScore);
				textMap.put("YSX_DESC", ysxDesc);
				textMap.put("CKJZ", errorMsg);
//				ReportModel2Utils.insertDTQK(reportColName, Model2Config.dtqkModelPath);
			} else if (reportColNameen.equals(ReportPart.ZHPJ)) {
				//插入空白行
				String zhpj = "";
				String zhpjDesc = "";
				JSONArray catArray = getAllData(evaluationUserCode);
				int count = 0;
				double ScoreSum = 0;
				for (int j = 0; j < catArray.size(); j++) {
					JSONObject catObj = catArray.getJSONObject(j);
					String catName = catObj.getString("catName");
					if (!catName.equals("逻辑推理") && !catName.equals("掩饰性")) {
						count++;
						double catVale = new Double(catObj.getString("catValue"));
						ScoreSum += catVale;
						textMap.put(catName+"_SCORE",catVale+"");
					}
				}
				double avgScore = ScoreSum / count;
				double level = avgScore / 9;
				if (level <= 0.11) {
					zhpj = "低";
					zhpjDesc = "不太符合";
				} else if (level>0.11&&level <= 0.24) {
					zhpj = "较低";
					zhpjDesc = "有所欠缺";
				} else if (level>0.24&&level <= 0.77) {
					zhpj = "中等";
					zhpjDesc = "比较符合";
				}else if(level>0.77&level<=0.89) {
					zhpj = "较高";
					zhpjDesc = "比较符合";
				}else if(level>=0.9) {
					zhpj = "高";
					zhpjDesc = "完全符合";
				}
				String zhpjText = evaluatoionUser.getName() + "与本次测评岗位的匹配程度:" +zhpj+
						"，从总分来看，素质总体情况与岗位整体要求:" +zhpjDesc+
						"。了进一步提升绩效，建议对本次测评中得分相对较低的素质进行关注和提升，以更好地满足岗位素质要求。";

				textMap.put("zhpj",zhpj);
				textMap.put("zhpjDesc",zhpjDesc);



				//综合评价
//				ReportModel2Utils.insertZHPJ(reportColName, zhpjText);
				} else if (reportColNameen.equals(ReportPart.FXBG)) {
//				DocumentBuilder builder = new DocumentBuilder(currentDocument);
//				builder.moveToDocumentEnd();
//				builder.insertBreak(BreakType.PAGE_BREAK);
					// 逻辑推理分析报告
					getAllData(evaluationUserCode);
					long LjtlScore = (long) catMap.get("逻辑推理").getCatValue();
//				long LjtlLevel = userAnswersMapper.getDivisorNormLevel(evaluationUserCode, LjtlScore, "逻辑推理");

				double level = new Double(LjtlScore) / 9;
				String desc="";
				if (level <= 0.11) {
					desc = "低";
				} else if (level>0.11&&level <= 0.24) {
					desc = "较低";
				} else if (level>0.24&&level <= 0.77) {
					desc = "中等";
				}else if(level>0.77&level<=0.89) {
					desc = "较高";
				}else if(level>=0.9) {
					desc = "高";
				}
				// 逻辑推理
//				addText(currentDocument, "", STYLE_TEXT_VALUE);
//				addText(currentDocument, tilte, STYLE_TITLE_VALUE);
//				addText(currentDocument,
//						"逻辑推理能是以敏锐的思考分析、快捷的反应、迅速地掌握问题的核心，在最短时间找出其事物内在的逻辑关系，从而推理出符合逻辑关系的结论的能力。具备了逻辑推理能力，才能对事物做出符合逻辑关系的正确判断，因此逻辑推理能力也是个人重要的基础素质之一。"
//								+ "在人才评估中，逻辑推理能力经常用于评估人才的智商/潜力。",
//						STYLE_TEXT_VALUE);
//				addText(currentDocument, "你的逻辑得分为:" + score+"，在样本中处于："+desc+"水平。", STYLE_TEXT_VALUE);
//				addText(currentDocument, "", STYLE_TEXT_VALUE);
//				addText(currentDocument,"说明：一般而言，逻辑得分越高代表测试者的该项能力越强，未来的潜力越高。",STYLE_TEXT_VALUE);





//				ReportModel2Utils.insertFXBG(reportColName, LjtlScore + "");

				textMap.put("LJTL_VALUE", LjtlScore + "");
				textMap.put("LJTL_DESC", desc);

					// 指标得分情况 （缺失得分数据）
				} else if (reportColNameen.equals(ReportPart.ZBDFQK)) {
					ReportModel2Utils.insertZBDFQK(reportColName, getAllData(evaluationUserCode));
					// 综合得分情况（基于测评答题情况成长得分）
				} else if (reportColNameen.equals(ReportPart.ZHDFQK)) {
//					ReportModel2Utils.insertZHDFQK(reportColName, catMap);
					// 能力分析报告(缺失报表模板)
				} else if (reportColNameen.equals(ReportPart.NLFXBG)) {
//					DocumentBuilder builder = new DocumentBuilder(currentDocument);
//					builder.moveToDocumentEnd();
//					builder.insertBreak(BreakType.PAGE_BREAK);
					ReportModel2Utils.insertNLFXBG(reportColName, getAllData(evaluationUserCode),
							Model2Config.nlfxModelXlsxPath);
				}
			}
			// 替换文本
			ReportModel2Utils.replaceText(currentDocument, textMap);
//			// 生成目录
//			currentDocument.updateFields();
			String reportName = report.getReportName() + "-" + evaluatoionUser.getName() + "-"
					+ evaluatoionUser.getEvaluatoionCode();
			String wordUrl = fileConfig.reportOutFolder + reportName +1+ ".docx";
			String pdfUrl = fileConfig.reportOutFolder + reportName + ".pdf";
			try {
				// 保存文件
//				currentDocument.getStyles().get("Normal").getParagraphFormat().setSpaceAfter(0);
				currentDocument.save(wordUrl);
				ChartUtils.dealChart(wordUrl,fileConfig.reportOutFolder + reportName+ ".docx",divisorJsonArrayObject);
				WordToPdf.doc2pdf(fileConfig.reportOutFolder + reportName+ ".docx", pdfUrl);
			} catch (Exception e) {
				e.printStackTrace();
			}
			evaluatoionUser.setReportUrl("file/report/" + reportName + ".pdf");
			evaluatoionUser.setUrl(pdfUrl);
			evaluatoionUserService.saveOrUpdate(evaluatoionUser);
	}

//	/**
//	 * 获取指标数据
//	 * 
//	 * @param evaluationUserCode
//	 * @return
//	 */
//	public JSONArray getData(String evaluationUserCode) {
//		if (divisorJsonArrayObject == null) {
//			List<DivisorCatBean> divisorCatBeanList = userAnswersMapper.getDivisorCatScoreInfo(evaluationUserCode);
//			for (int j = 0; j < divisorCatBeanList.size(); j++) {
//				DivisorCatBean divisorCatBean = divisorCatBeanList.get(j);
//				List<DivisorCatItemBean> items = userAnswersMapper.getDivisorInfo(evaluationUserCode,
//						divisorCatBean.getCatName());
//				divisorCatBean.setItems(items);
//			}
//			return JSONArray.parseArray(JSON.toJSONString(divisorCatBeanList));
//		} else {
//			return divisorJsonArrayObject;
//		}
//	}

	public JSONArray getAllData(String  evaluationUserCode) {
		if (divisorJsonArrayObject == null) {
			List<ReportDivisorInfoCatBean> reportDivisorInfoCatBean = userAnswersMapper
					.getDivisorInfo(evaluationUserCode);
			catMap = new LinkedHashMap<String, DivisorCatBean>();
			LinkedHashMap<String, DivisorCatItemBean> catItemMap = new LinkedHashMap<String, DivisorCatItemBean>();
			for (int i = 0; i < reportDivisorInfoCatBean.size(); i++) {
				ReportDivisorInfoCatBean allbean = reportDivisorInfoCatBean.get(i);
				DivisorCatItemBean divisorCatItemBean = null;
				// 获取分类
				String divisorCat = allbean.getDivisorCat();
				// 常模的Code
				String normCode = allbean.getNormCode();
				int divisorValue = allbean.getValue();
				String divisorName = allbean.getDivisorName();
				if (catItemMap.containsKey(divisorName)) {
					divisorCatItemBean = catItemMap.get(divisorName);
				} else {
					divisorCatItemBean = new DivisorCatItemBean();
					catItemMap.put(divisorName, divisorCatItemBean);
				}
				divisorCatItemBean.setNormCode(normCode);
				divisorCatItemBean.setKey(divisorName);
				divisorCatItemBean.setValue(divisorCatItemBean.getValue() + divisorValue);
				divisorCatItemBean.setDivisorCatName(divisorCat);
			}

			for (Map.Entry<String, DivisorCatItemBean> entry : catItemMap.entrySet()) {
				DivisorCatItemBean divisorCatItemBean = entry.getValue();
				String divisorCatName = divisorCatItemBean.getDivisorCatName();
				DivisorCatBean divisorCatBean = null;
				if (catMap.containsKey(divisorCatName)) {
					divisorCatBean = catMap.get(divisorCatName);
				} else {
					divisorCatBean = new DivisorCatBean();
					catMap.put(divisorCatName, divisorCatBean);
				}
				divisorCatBean.setCatName(divisorCatName);
				divisorCatBean.setCatDesc(productService.getOne(new QueryWrapper<Product>().eq("PRODUCT_NAME", divisorCatName)).getProductIntro());
				divisorCatBean.setCatValue(divisorCatBean.getCatValue() + divisorCatItemBean.getValue());
				divisorCatBean.getItems().add(divisorCatItemBean);
			}

			List<DivisorCatBean> divisorCatBeanList = new ArrayList<DivisorCatBean>();
			for (Map.Entry<String, DivisorCatBean> entry : catMap.entrySet()) {
				// 转换成9分制
				List<DivisorCatItemBean> catItems = entry.getValue().getItems();
				System.out.println(catItems.toString());
				int catTotValue = 0;
				for (int i = 0; i < catItems.size(); i++) {
					DivisorCatItemBean item = catItems.get(i);
					String normCode = item.getNormCode();
					double percentileValue = 0;
					int niceValue = 0;
					if (item.getKey().equals("逻辑推理")) {
						int qcount = userAnswersMapper.getQuestionCount(evaluationUserCode, item.getKey());
						percentileValue = (item.getValue() / qcount) * 100;
						niceValue = getNiceValue((int) Math.ceil(percentileValue));
						item.setValue(niceValue);
					} else if (entry.getValue().getCatName().equals("掩饰性")) {
						// 转换成9分制
						long ysxLevel = userAnswersMapper.getDivisorNormLevel(evaluationUserCode,
								(int) Math.ceil(item.getValue()), "掩饰性");
						niceValue = (int) ysxLevel;
						item.setValue(niceValue);
					} else if (entry.getValue().getCatName().equals("动力需求")) {
						int qcount = userAnswersMapper.getQuestionOptionCount(evaluationUserCode, item.getKey());
						percentileValue = (item.getValue() / (qcount * 3)) * 100;
						System.out.println(
								item.getKey() + "," + item.getValue() + ",qcount:" + qcount + "," + percentileValue);
						niceValue = getNiceValue((int) Math.ceil(percentileValue));
						item.setValue(niceValue);
					} else {
						percentileValue = (normManageMapper.getPercentileValue(normCode,
								(int) Math.ceil(item.getValue()) + ""));
						niceValue=getNiceValue((int) Math.ceil(percentileValue));
						item.setValue(niceValue);
					}
					String desc=userAnswersMapper.getDivisionDesc(item.getKey(), niceValue);
					item.setDesc(desc);
					catTotValue += niceValue;
				}
				double catValue = catTotValue / catItems.size();
				entry.getValue().setCatValue(catValue);
				divisorCatBeanList.add(entry.getValue());
			}
			divisorJsonArrayObject = (JSONArray) JSON.toJSON(divisorCatBeanList);
			return divisorJsonArrayObject;
		} else {
			return divisorJsonArrayObject;
		}
	}

	/**
	 * 换算成9分制
	 * 
	 * @param value
	 * @return
	 */
	public int getNiceValue(int value) {
		int niceValue = 0;
		if (value >= 1 && value < 4) {
			niceValue = 1;
		} else if (value >= 4 && value < 11) {
			niceValue = 2;
		} else if (value >= 11 && value < 23) {
			niceValue = 3;
		} else if (value >= 23 && value <= 40) {
			niceValue = 4;
		} else if (value >= 40 && value < 60) {
			niceValue = 5;
		} else if (value >= 60 && value < 77) {
			niceValue = 6;
		} else if (value >= 77 && value < 89) {
			niceValue = 7;
		} else if (value >= 89 && value < 96) {
			niceValue = 8;
		} else if (value >= 96 && value <= 100) {
			niceValue = 9;
		}
		return niceValue;

	}

}
