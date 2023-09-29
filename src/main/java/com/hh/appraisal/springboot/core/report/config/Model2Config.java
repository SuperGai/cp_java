package com.hh.appraisal.springboot.core.report.config;

/**
 * 报告2的模板配置
 * 
 * @author gaigai
 *
 */
public class Model2Config {

	/**
	 * 模板地址
	 */
	public static final String modelPath = "C:\\Program Files\\java\\nginx-1.6.3\\html\\file\\model\\管培生测评报模板.docx";

	/**
	 * 能力分析报告 excel 模板地址
	 */
	public static final String nlfxModelXlsxPath = "C:\\Program Files\\java\\nginx-1.6.3\\html\\file\\model\\table.xlsx";

	/**
	 * 答题情况Excel模板地址
	 */
	public static final String dtqkModelPath = "C:\\Program Files\\java\\nginx-1.6.3\\html\\file\\model\\答题情况.docx";

//	 /**
//     * 模板地址
//     */
//    public static final String modelPath = "/Users/gaigai/Documents/Study/UKM/03 index/report/测评报告模板-盖盖.docx";
//
//	/**
//	 * 能力分析报告 excel 模板地址
//	 */
//    public static final String nlfxModelXlsxPath = "/Users/gaigai/Documents/Study/UKM/03 index/report/table.xlsx";
//
//    /**
//     * 答题情况Excel模板地址
//     */
//    public static final String dtqkModelPath = "/Users/gaigai/Documents/Study/UKM/03 index/report/答题情况.docx";

//  答题时间，在标准时间的0.6-1.6倍为合理时间，文本描述为：XXX答题时间在合理范围内，测评过程有效
//  耗时为给定时间的0.4-0.59倍，文本描述为：XXX答题时间少于标准时间，请结合其日常行为综合评估
//  耗时为给定时间的0.39及以下，文本描述为：：XXX答题时间远未达到规定答题时间，测评过程异常，请谨慎使用此测评结果。
	public static final String lowTime = "答题时间远未达到规定答题时间，测评过程异常，请谨慎使用此测评结果.";
	public static final String smallTime = "答题时间少于标准时间，请结合其日常行为综合评估.";
	public static final String middleTime = "答题时间在合理范围内，测评过程有效.";

//    全部完成，或完成95%（注：不含逻辑题），文本描述为：XXX本次答题数量在合理范围内。
//    低于95%，文本描述为：XXX本次答题数量低于合理值，其部分指标得分受到影响。
	public static final String answerMiddleNumber = "本次答题数量在合理范围内.";
	public static final String answerLowNumber = "本次答题数量低于合理值，其部分指标得分受到影响。";

	/**
	 * 掩饰性分值范围
	 */
	public static final String YsxRange = "1-9";

}
