package com.hh.appraisal.springboot.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.util.HSSFColor;
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
import com.hh.appraisal.springboot.entity.EvaluatoionUser;
import com.hh.appraisal.springboot.utils.WordToPdf;


@Service
//生成PDF线程类
public class GeneraPdfAsyncTaskService2 {

	@Autowired
	EvaluatoionUserService evaluatoionUserService;

	@Autowired
	EvaluatoionCodeMapper evaluatoionCodeMapper;

	@Autowired
	UserAnswersMapper userAnswersMapper;

	@Autowired
	SchoolService schoolService;
	
	@Autowired
	ProductService productService;

	@Autowired
	DivisorMapper divisorMapper;

	@Autowired
	FileConfig fileConfig;
	
	public static final String lowTime = "答题时间远未达到规定答题时间，测评过程异常，请谨慎使用此测评结果.";
	public static final String smallTime = "答题时间少于标准时间，请结合其日常行为综合评估.";
	public static final String middleTime = "答题时间在合理范围内，测评过程有效.";

//    全部完成，或完成95%（注：不含逻辑题），文本描述为：XXX本次答题数量在合理范围内。
//    低于95%，文本描述为：XXX本次答题数量低于合理值，其部分指标得分受到影响。
	public static final String answerMiddleNumber = "本次答题数量在合理范围内.";
	public static final String answerLowNumber = "本次答题数量低于合理值，其部分指标得分受到影响。";

	public static final String allComplete = "完成全部答题数量。";
	public static final String unComplete = "未完成问题，部分指标会受到影响。";



	@Async
	public void executeAsyncTask(String evaluationUserCode) throws Exception {
//		//是否完成答题改成是
		EvaluatoionUser evaluatoionUser = evaluatoionUserService
				.getOne(new QueryWrapper<EvaluatoionUser>().eq("EVALUATOION_CODE", evaluationUserCode));
		evaluatoionUser.setIsComplete("Y");
		//答题时间
		long spendTime=userAnswersMapper.getSpendTime(evaluationUserCode);
		evaluatoionUser.setSpendTime(spendTime);
		evaluatoionUserService.saveOrUpdate(evaluatoionUser);
		String strDateFormat = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
		// 转化成json对象
		JSONObject json = (JSONObject) JSON.toJSON(evaluatoionUser);
		// 获取logo地址
		EvaluatoionCode evaluatoionCode = evaluatoionCodeMapper
				.selectOne(new QueryWrapper<EvaluatoionCode>().eq("evaluatoion_Code", evaluationUserCode));
		SchoolBean school= schoolService.findByCode(evaluatoionCode.getShoolCode());
		//用户个人信息
		// 替换文本
		Map<String, String> textMap = new HashMap<String, String>();
	
		textMap.put("createTime", sdf.format(evaluatoionUser.getCreateTime()));
		textMap.put("name",json.getString("name"));
		
		// 答题花费的总时长
		long DTSJ_VALUE = spendTime;
		int answerMinutes = (int) (DTSJ_VALUE % 3600) / 60;
		int answerSeconds =  (int )DTSJ_VALUE % 60;  // 这应该返回 12
		
		ProductBean product = productService.findByCode(evaluatoionCode.getProductCode());
		//实际答题数量
		int answerNum= userAnswersMapper.getCompleteQuestionCount(evaluationUserCode);
		textMap.put("answer_num_sj", answerNum+"");
		//答题描述
		String answerNumDesc="";
		if(answerNum==product.getQuestionNum()) {
			answerNumDesc=allComplete;
		}else {
			DTSJ_VALUE= product.getAnswerTime()* 60;
			answerNumDesc=unComplete;
		}		
		String DTSJ_VALUE_TXT = answerMinutes + "分"+answerSeconds+"秒";	
		long DTSJ_DEFAULT = product.getAnswerTime();
		//默认时长
		long DTSJ_DEFAULT_S = DTSJ_DEFAULT * 60;
		int defaultMinutes = (int) (DTSJ_DEFAULT_S % 3600) / 60;
		String DTSJ_DEFAULT_TXT =  defaultMinutes + "分";

		// 答题时间的描述
		String DTSJ_DESC = "";
		boolean isDtsjError = false;
		double timeRatio = (double) DTSJ_VALUE / (double) DTSJ_DEFAULT_S;
		if (timeRatio >= 0.6) {
			DTSJ_DESC = middleTime;
		} else if (timeRatio >= 0.4 && timeRatio <= 0.59) {
			DTSJ_DESC = smallTime;
			isDtsjError = true;
		} else {
			DTSJ_DESC = lowTime;
			isDtsjError = true;
		}
		// 如果答题时间异常
		if (isDtsjError) {
			DTSJ_DESC = evaluatoionUser.getName() +DTSJ_DESC;
		}
		//默认答题时间
		textMap.put("answer_default_time", DTSJ_DEFAULT_TXT);
		//实际答题时间
		textMap.put("answer_time_sj", DTSJ_VALUE_TXT);
		//答题时间描述
		textMap.put("answer_time_desc", DTSJ_DESC);
		
		//答题数量
		textMap.put("answer_default_num", product.getQuestionNum()+"");
	
		textMap.put("answer_num_desc",evaluatoionUser.getName()+answerNumDesc);
		//总得分
		textMap.put("total_mask", userAnswersMapper.getTotMusk(evaluationUserCode)+"");
		//平均分
		textMap.put("avg_mask", userAnswersMapper.getAvgMusk(evaluationUserCode)+"");
		//超过两分的个数
		textMap.put("two_num", userAnswersMapper.getMoreThan2QuestionNum(evaluationUserCode)+"");
		
		String logo = "https://www.jingyingpai.cc/file/logo/1634190617421JP-LOGO.png";
//				fileConfig.pdfUrl + school.getLogo();
		// 替换logo
		Map<String, String> imgMap = new HashMap<String, String>();
		List<DivisorAverageBean> divisorAverageBean = userAnswersMapper.getDivisorListInfo2ByXlcp(evaluationUserCode);
		InputStream is = new FileInputStream(new File(fileConfig.reportModel));
		XWPFDocument doc = new XWPFDocument(is);
		JSONArray divisors = (JSONArray) JSON.toJSON(divisorAverageBean);
		// 数据准备
		List<String> titleArr = new ArrayList<String>();// 标题
		titleArr.add("");
		titleArr.add("指标偏向图表");
		List<String> fldNameArr = new ArrayList<String>();// 字段名(数据有多少列，就多少个)
		fldNameArr.add("item1");
		fldNameArr.add("item2");
		// 数据集合
		List<Map<String, String>> listItemsByType = new ArrayList<Map<String, String>>();
		JSONArray items=new JSONArray();
		for (int i = 0; i < divisorAverageBean.size(); i++) {
			DivisorAverageBean bean = divisorAverageBean.get(i);
			// 第一行数据
			Map<String, String> base = new HashMap<String, String>();
			base.put("item1", bean.getDivisorName());
			base.put("item2", bean.getDivisorAverag() + "");
			JSONObject item=new JSONObject();
			item.put("key", bean.getDivisorName());
			item.put("value",bean.getDivisorAverag());
			items.add(item);
			listItemsByType.add(base);
		}

        // 替换word模板数据

		String reportName = "心理健康测评报告-" + evaluatoionUser.getName() + "-" + evaluatoionUser.getEvaluatoionCode();
		String wordUrl = fileConfig.reportOutFolder + reportName + ".docx";
		String wordReplaceUrl = fileConfig.reportOutFolder + reportName + "2.docx";
		String pdfUrl = fileConfig.reportOutFolder + reportName + ".pdf";
		PoiWordTools.replaceAll(doc, logo, titleArr, fldNameArr, listItemsByType, textMap, imgMap, divisors);
		// 保存结果文件
		try {
			//先保存文件
			FileOutputStream fos = new FileOutputStream(wordUrl);
			doc.write(fos);
			//做替换
			doc.close();
			fos.close();
			System.out.println("关闭了！！！！！："+wordReplaceUrl);
			WordToPdf.relaceAlltext(wordUrl,wordReplaceUrl,textMap);	
			Thread.sleep(1000);
			System.out.println("开始转换PDF ！！！！！");
			WordToPdf.doc2pdf(wordReplaceUrl, pdfUrl);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		evaluatoionUser.setReportUrl("file/report/" + reportName+".pdf");
		evaluatoionUser.setUrl(pdfUrl);
		evaluatoionUserService.saveOrUpdate(evaluatoionUser);
		

	}
	
}
