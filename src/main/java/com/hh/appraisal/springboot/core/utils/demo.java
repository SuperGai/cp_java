package com.hh.appraisal.springboot.core.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.util.Units;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspose.words.Cell;
import com.aspose.words.Chart;
import com.aspose.words.ChartDataLabel;
import com.aspose.words.ChartDataLabelCollection;
import com.aspose.words.ChartSeries;
import com.aspose.words.ChartSeriesCollection;
import com.aspose.words.ChartType;
import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.ImageData;
import com.aspose.words.ImportFormatMode;
import com.aspose.words.Node;
import com.aspose.words.NodeCollection;
import com.aspose.words.NodeImporter;
import com.aspose.words.NodeType;
import com.aspose.words.Paragraph;
import com.aspose.words.Range;
import com.aspose.words.Row;
import com.aspose.words.RowCollection;
import com.aspose.words.Run;
import com.aspose.words.Section;
import com.aspose.words.Shape;
import com.aspose.words.ShapeType;
import com.aspose.words.Style;
import com.aspose.words.Table;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;

public class demo {

	static private String STYLE_TITLE_VALUE = "Heading 1";
	static private String STYLE_TEXT_VALUE = "正文样式";
	static private String STYLE_LIST_GRA_VALUE = "List Paragraph";

	// 模板方式实现
	public static void formatDoc() throws Exception {
		Map<String, String> textMap = new HashMap<String, String>();
		textMap.put("@$reportName@$", "管理培训生测评");
		textMap.put("@$createTime@$", "2023-06-05");

		Document currentDocument = new Document("/Users/gaigai/Documents/Study/UKM/03 index/report/测评报告模板-盖盖.docx");

		// 替换用户信息
		Table userInfoTable = (Table) currentDocument.getChild(NodeType.TABLE, 0, true);
		JSONArray userInfoArray = new JSONArray();
		JSONObject userObjRow1 = new JSONObject();
		userObjRow1.put("@$userInfoSimple@$", "姓名：");
		JSONObject userObjRow2 = new JSONObject();
		userObjRow2.put("@$userInfoSimple@$", "徐文娟");
		userInfoArray.add(userObjRow1);
		userInfoArray.add(userObjRow2);
		addRowAndReplaceCellText(userInfoTable, userInfoArray);

		// 添加报告简介
		addText(currentDocument, "报告简介", STYLE_TITLE_VALUE);
		addHtmlText(currentDocument, "<p style=\"font-size: medium;white-space: normal\">"
				+ "    <span style=\"font-family: 方正兰亭细黑_GBK;font-size: 16px\">&nbsp; &nbsp; &nbsp; &nbsp;招聘选拔是企业保障人才供应的重要手段，主要包括社会招聘、校园招聘、内部竞聘等。社会招聘的人才往往拥有专业的技能和一定的工作实践经验，能为企业节省大量的培训成本，也有利于为企业注入新的活力。校园招聘的人才是刚刚走出校园的毕业生，尽管缺乏工作经验，但他们可塑性强，更容易接受公司的管理理念和文化。内部竞聘是在企业内部选拔合适的人才，相对与外部招聘，内部竞聘为员工搭建了实现个人价值的舞台，有利于激发内部员工的工作热情和上进心，减少人才流失。</span>"
				+ "</p>" + "<p style=\"font-size: medium;white-space: normal\">"
				+ "    <span style=\"font-family: 方正兰亭细黑_GBK;font-size: 16px\">&nbsp; &nbsp; &nbsp; &nbsp;无论是外部招聘还是内部竞聘，企业都需要对候选人进行精确、客观、公正的评价。人才素质与岗位胜任行为（</span><span style=\"font-family: 方正兰亭细黑_GBK;font-size: 16px\"><span style=\"font-family:方正兰亭细黑_GBK\">KCI</span><span style=\"font-family:方正兰亭细黑_GBK\">）高度相关，是岗位胜任行为的有力支撑，并且展现了人才的发展潜力。人才素质测评作为一种先进、高效、客观的人才选拔理念和工具，在招聘过程中，能够在短时间内对应聘者进行准确评价，减少面试的工作量和压力，提高招聘的效率和科学性。而在内部竞聘选拔过程中，能够依照岗位的不同，设置严格的、统一的标准对竞聘人员进行客观的定量考评，提高了竞聘的公平性和客观性。</span></span>"
				+ "</p>" + "<p style=\"font-size: medium;white-space: normal\">"
				+ "    <span style=\"font-family: 方正兰亭细黑_GBK;font-size: 16px\"><span style=\"font-family:方正兰亭细黑_GBK\">&nbsp; &nbsp; &nbsp; &nbsp;精派作为一家人才测评和人才管理的专业机构，通过结合多年的人才评估选拔拔经验以及雄厚的研发实力，针对不同行业、层级、岗位等特点，开发了招聘选拔测评系列产品。该系列产品充分体现了</span><span style=\"font-family:方正兰亭细黑_GBK\">“人岗匹配”的人才选拔观，为企业提供全面的员工能力、个性、动力数据和相应的面试建议和用人建议，为企业招聘和选拔优秀人才提供有效参考。</span></span>"
				+ "</p>" + "<p>" + "    <br/>" + "</p>");

//		// 添加阅读建议
		addText(currentDocument, "阅读建议", STYLE_TITLE_VALUE);
		String ydjy = "<p style=\"font-size: medium;white-space: normal\">\n"
				+ "    <span style=\"font-family: 方正兰亭细黑_GBK;font-size: 16px\"></span>\n" + "</p>\n"
				+ "<p style=\"font-size: medium; white-space: normal;  line-height: normal;\">\n"
				+ "    <span style=\"font-family: 方正兰亭细黑_GBK;font-size: 16px\">1.&nbsp;</span><span style=\"font-family: 方正兰亭细黑_GBK;font-size: 16px\">测评结果的准确性和可靠性依赖于被评价者在测验中态度是否认真、能否如</span><span style=\"font-family: 方正兰亭细黑_GBK;font-size: 16px\">实作答，是否答完题目以及答题所用时间是否在合理范围内等信息。</span>\n"
				+ "</p>\n" + "<p style=\"font-size: medium; white-space: normal;  line-height: normal;\">\n"
				+ "    <span style=\"font-family: 方正兰亭细黑_GBK;font-size: 16px\">2.&nbsp;</span><span style=\"font-family: 方正兰亭细黑_GBK;font-size: 16px\">人才素质测评遵循匹配原理、推断原理和误差原理。在使用测评结果时，需要与目标岗位具体情况相结合，并以科学的态度看待测评结果。</span>\n"
				+ "</p>\n" + "<p style=\"font-size: medium; white-space: normal;  line-height: normal;\">\n"
				+ "    <span style=\"font-size: 16px; font-family: 方正兰亭细黑_GBK;\">3.&nbsp;</span><span style=\"font-size: 16px; font-family: 方正兰亭细黑_GBK;\">若您是初次阅读此类报告，最好是在专业人士的指导下阅读，或请专业人士为您进行解释。</span>\n"
				+ "</p>\n" + "<p>\n" + "    <br/>\n" + "</p>";
		addHtmlText(currentDocument, ydjy);

		// 添加答题情况模块
		addText(currentDocument, "答题情况", STYLE_TITLE_VALUE);
		addModelTable(currentDocument, "/Users/gaigai/Documents/Study/UKM/03 index/report/答题情况.docx");
		addAllParagraphsFromModelFile(currentDocument, "/Users/gaigai/Documents/Study/UKM/03 index/report/答题情况.docx");

		// 综合评价
		addText(currentDocument, "综合评价", STYLE_TITLE_VALUE);
		addText(currentDocument,
				"盖盖与本次测评岗位的匹配程度较高。从总分来看，素质总体情况与岗位整体要求比较符合。如果他（她）从事此项工作，能较好地胜任，圆满完成工作任务。为了进一步提升绩效，建议对本次测评中得分相对较低的素质进行关注和提升，以更好地满足岗位素质要求。",
				STYLE_TEXT_VALUE);

		// 综合得分情况（基于测评答题情况成长得分
		addText(currentDocument, "综合得分情况（基于测评答题情况成长得分）", STYLE_TITLE_VALUE);
//		UpdateChartExample(currentDocument);
		addText(currentDocument, "", STYLE_TEXT_VALUE);

		
		Map<String, Double> shapeMap = new HashMap<String, Double>();
		shapeMap.put("逻辑推理", (double) 1);
		shapeMap.put("自我管理", (double)3);
		shapeMap.put("管理潜力", (double)5);
		shapeMap.put("个性", (double)7);
		shapeMap.put("动力", (double)9);
		
		UpdateChartExample(currentDocument, shapeMap);
		
//		//生成图表
//		AddColumnChart(currentDocument, 432, 252, shapeMap);
		
		
		// 替换文本
		replaceText(currentDocument, textMap);

		// 更新目录
		currentDocument.updateFields();

		// 保存文件
		currentDocument.save("/Users/gaigai/Documents/Study/UKM/03 index/report/测评报告模板-盖盖-test.docx");

	}

	// main
	public static void main(String[] args) throws Exception {
		// 读取模板方式写word
		formatDoc();
//	     // 自定义样式方式写word  
//	     writeSimpleDocxFile();  
	}

	/**
	 * 新增行并替换表格单元格里的文本
	 * 
	 * @param userInfoTable
	 * @param userInfoArray
	 * @throws Exception
	 */
	public static void addRowAndReplaceCellText(Table userInfoTable, JSONArray userInfoArray) throws Exception {
		for (int i = 0; i < userInfoArray.size() - 1; i++) {
			// 创建新行
			Row newRow = (Row) userInfoTable.getFirstRow().deepClone(true);
			// 在指定行的后面插入新行
			userInfoTable.insertAfter(newRow, userInfoTable.getLastRow());
		}

		ReplaceTableCellText(userInfoTable, userInfoArray);
	}

	/**
	 * 替换表格单元格里的文本
	 * 
	 * @param userInfoTable
	 * @param userInfoArray
	 * @throws Exception
	 */
	public static void ReplaceTableCellText(Table userInfoTable, JSONArray userInfoArray) throws Exception {
		// 获取表格所有的行，并对单元格进行替换
		RowCollection rowCollection = userInfoTable.getRows();
		for (int i = 0; i < rowCollection.getCount(); i++) {
			Row simpleRow = rowCollection.get(i);
			int j = 0;
			for (Map.Entry<String, Object> entry : userInfoArray.getJSONObject(i).entrySet()) {
				if (j > simpleRow.getCells().getCount()) {
					break;
				}
				Cell simpleCol = simpleRow.getCells().get(j);
				// 替换的键
				String key = entry.getKey();
				// 替换的值
				String value = (String) entry.getValue();
				simpleCol.getRange().replace(key, value, false, false);
				j++;
			}
		}
	}

	/**
	 * 替换文本
	 * 
	 * @param currentDocument
	 * @param textMap
	 * @throws Exception
	 */
	public static void replaceText(Document currentDocument, Map<String, String> textMap) throws Exception {
		for (String key : textMap.keySet()) {
			String value = textMap.get(key);
			Range range = currentDocument.getRange();
			range.replace(key, value, false, false);
		}
	}

	/**
	 * 添加模版表格
	 * 
	 * @param currentDocument
	 * @param modelFileUrl
	 * @throws Exception
	 */
	public static void addModelTable(Document currentDocument, String modelFileUrl) throws Exception {
		// 打开另一个Word文档
		FileInputStream anotherFileInputStream = new FileInputStream(modelFileUrl);
		Document anotherDocument = new Document(anotherFileInputStream);
		// 获取另一个文档中的第一个表格
		Table sourceTable = (Table) anotherDocument.getChild(NodeType.TABLE, 0, true);
		// 创建节点导入器
		NodeImporter importer = new NodeImporter(anotherDocument, currentDocument,
				ImportFormatMode.KEEP_SOURCE_FORMATTING);
		// 复制表格到当前文档
		Node importedNode = importer.importNode(sourceTable, true);
		currentDocument.getFirstSection().getBody().appendChild(importedNode);
		anotherFileInputStream.close();
	}

	/**
	 * 
	 * @param modelFileUrl
	 * @param currentDocument
	 * @param text
	 * @throws Exception
	 */
	public static void addHtmlText(Document currentDocument, String text) throws Exception {
		// Create an HTML builder
		Document modeDocument = new Document();
		DocumentBuilder builder = new DocumentBuilder(modeDocument);
		// Insert HTML content
		String htmlContent = text;
		builder.insertHtml(htmlContent);
		addAllParagraphsFromModelFile(modeDocument, currentDocument);
	}

	/**
	 * 添加所有内容从模版文件
	 * 
	 * @param currentDocument
	 * @param modelFileUrl
	 * @throws Exception
	 */
	public static void addAllParagraphsFromModelFile(Document currentDocument, String modelFileUrl) throws Exception {
		Document modeDocument = null;
		if (ObjectUtil.isNotEmpty(modelFileUrl)) {
			modeDocument = new Document(modelFileUrl);
		} else {
			modeDocument = new Document();
		}
		addAllParagraphsFromModelFile(modeDocument, currentDocument);
	}

	/**
	 * 添加所有内容从模版文件
	 * 
	 * @param currentDocument
	 * @param text
	 * @throws Exception
	 */
	public static void addAllParagraphsFromModelFile(Document anotherDocument, Document currentDocument)
			throws Exception {
//		 Get the paragraphs from the source document
		NodeCollection srcParagraphs = anotherDocument.getChildNodes(NodeType.PARAGRAPH, true);
		// 创建节点导入器
		NodeImporter importer = new NodeImporter(anotherDocument, currentDocument,
				ImportFormatMode.KEEP_SOURCE_FORMATTING);
		// Insert each paragraph into the destination document
		for (Paragraph srcPara : (Iterable<Paragraph>) srcParagraphs) {
			if (srcPara.getParentNode().getNodeType() == NodeType.BODY) {
				Node importedNode = importer.importNode(srcPara, true);
				currentDocument.getFirstSection().getBody().appendChild(importedNode);
			}
		}
	}

	/**
	 * 添加文本
	 * 
	 * @param currentDocument
	 * @param text
	 * @throws Exception
	 */
	public static void addText(Document currentDocument, String text, String styleName) throws Exception {
//		// 创建新的标题段落
		Paragraph title = new Paragraph(currentDocument);
		Run run = new Run(currentDocument);
		run.setText(text);
		title.appendChild(run);
		// 获取当前文档的样式
		Style currentStyle = currentDocument.getStyles().get(styleName);
		// 设置新的标题段落样式为当前文档的样式
		title.getParagraphFormat().setStyle(currentStyle);
		// 将标题段落添加到文档中
		currentDocument.getFirstSection().getBody().appendChild(title);
	}

	/**
	 * 生成图表
	 * @param currentDocument
	 * @param width
	 * @param height
	 * @param valueMap
	 * @throws Exception
	 */
	public static void AddColumnChart(Document  currentDocument,int width,int height,Map<String, Double> valueMap) throws Exception {
		DocumentBuilder builder = new DocumentBuilder(currentDocument);
		builder.moveToDocumentEnd(); //252 432
		Shape shape = builder.insertChart(ChartType.COLUMN, width , height );
		// Chart property of Shape contains all chart related options.
		Chart chart = shape.getChart();
		chart.getTitle().setShow(false);
		// Get chart series collection.
		ChartSeriesCollection seriesColl = chart.getSeries();
		// Delete default generated series.
		seriesColl.clear();
		// Create category names array
		String[] categories = new String[] { "" };
		// Adding new series. Please note, data arrays must not be empty and arrays must
		// be the same size.
		//赋值
		for (String key : valueMap.keySet()) {
			double value = valueMap.get(key);
			seriesColl.add(key, categories, new double[] { value }).getMarker();
		}
		// 设置数据标签样式
		for (int i = 0; i < seriesColl.getCount(); i++) {
			seriesColl.get(i).getDataLabels().add(0);
			seriesColl.get(i).getDataLabels().get(0).setShowValue(true);
		}
	}

	public static void UpdateChartExample(Document destDoc,Map<String, Double> valueMap) throws Exception {
		// Load the source Word document
		Document srcDoc = new Document("/Users/gaigai/Documents/Study/UKM/03 index/report/综合得分情况.docx");
		// Create a document builder for the destination document
		DocumentBuilder builder = new DocumentBuilder(destDoc);
		// Extract charts from the source document, update their data, and insert them
		// into the destination document
		NodeCollection nodeCollection = srcDoc.getChildNodes(NodeType.SHAPE, true);

		// Import the node from the source document to the destination document
		NodeImporter importer = new NodeImporter(srcDoc, destDoc, ImportFormatMode.KEEP_SOURCE_FORMATTING);

		for (int i = 0; i < nodeCollection.getCount(); i++) {
			Shape srcShape = (Shape) nodeCollection.get(i);
			Chart srcChart = srcShape.getChart();
			ChartSeriesCollection chartSeriesCollection = srcShape.getChart().getSeries();
			chartSeriesCollection.clear();
			//赋值
			for (String key : valueMap.keySet()) {
				double value = valueMap.get(key);
				chartSeriesCollection.add(key,new double[] {0}, new double[] { value });
			}
//			// Insert the imported node into the destination document
			Node newNode = importer.importNode(srcShape, true);
			builder.moveToDocumentEnd();
			builder.insertNode(newNode);
		}
	}

}
