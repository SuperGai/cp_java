package com.hh.appraisal.springboot.core.utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.ConditionalFormatting;
import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspose.words.Cell;
import com.aspose.words.CellMerge;
import com.aspose.words.Chart;
import com.aspose.words.ChartSeriesCollection;
import com.aspose.words.ChartType;
import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.FontInfoCollection;
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
import com.aspose.words.Shape;
import com.aspose.words.ShapeType;
import com.aspose.words.Style;
import com.aspose.words.Table;
import com.aspose.words.VerticalAlignment;
import com.aspose.words.WrapType;
import com.hh.appraisal.springboot.entity.EvaluatoionUser;
import com.hh.appraisal.springboot.entity.ReportConfig;
import com.hh.appraisal.springboot.utils.WordToPdf;

import cn.hutool.core.util.ObjectUtil;

public class ReportModel2Utils {

	static private String STYLE_TITLE_VALUE = "Heading 1";
	static private String STYLE_TEXT_VALUE = "正文样式";

	static Document currentDocument = null;

	/**
	 * 初始化模板
	 * 
	 * @param modelPath
	 * @return
	 * @throws Exception
	 */
	public static Document initDocument(String modelPath) throws Exception {
//		/Users/gaigai/Documents/Study/UKM/03 index/report/测评报告模板-盖盖.docx
		currentDocument = new Document(modelPath);
		return currentDocument;
	}

	/**
	 * 生成用户栏信息
	 * 
	 * @param currentDocument
	 * @param userReportConfig
	 * @param evaluatoionUser
	 * @throws Exception
	 */
	public static void GenerateUserInfo(Document currentDocument, List<ReportConfig> userReportConfig,
			EvaluatoionUser evaluatoionUser) throws Exception {
		// 替换用户信息
		Table userInfoTable = (Table) currentDocument.getChild(NodeType.TABLE, 0, true);
		JSONArray userInfoArray = new JSONArray();
		JSONObject userInfo = (JSONObject) JSON.toJSON(evaluatoionUser);
		for (int i = 0; i < userReportConfig.size(); i++) {
			ReportConfig userReportCongfig = userReportConfig.get(i);
			// 姓名
			String reportConfigPartColName = userReportCongfig.getReportConfigPartColName();
			// name
			String reportConfigPartColValue = userReportCongfig.getReportConfigPartColValue();
			System.out.println("getReportConfigPartColNameen:"+reportConfigPartColValue);
			String value = (String) userInfo.get(userReportCongfig.getReportConfigPartColNameen());
			JSONObject userObjRow1 = new JSONObject();
			userObjRow1.put("@$userInfoSimple@$", reportConfigPartColName + "：");
			JSONObject userObjRow2 = new JSONObject();
			userObjRow2.put("@$userInfoSimple@$", value);
			userInfoArray.add(userObjRow1);
			userInfoArray.add(userObjRow2);
		}
		addRowAndReplaceCellText(userInfoTable, userInfoArray);
	}

	// 模板方式实现
	/**
	 * @throws Exception
	 */
	public static void formatDoc() throws Exception {
		Map<String, String> textMap = new HashMap<String, String>();
		textMap.put("@$reportName@$", "管理培训生测评");
		textMap.put("@$createTime@$", "2023-06-05");

		Document currentDocument = new Document("/Users/gaigai/Documents/Study/UKM/03 index/report/盖盖测试报告-Karen-JP20230820gWVAE8kB.docx");
		FontInfoCollection  FONT =currentDocument.getFontInfos();
		for (int i = 0; i < FONT.getCount(); i++) {
			System.out.println(FONT.get(i).getName());
		}
		
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
				+ "</p>" + "<p>" + "    <br/>" + "</p>",STYLE_TEXT_VALUE);

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
		addHtmlText(currentDocument, ydjy,STYLE_TEXT_VALUE);

		// 添加答题情况模块
		addText(currentDocument, "答题情况", STYLE_TITLE_VALUE);
		addModelTable(currentDocument, "/Users/gaigai/Documents/Study/UKM/03 index/report/答题情况.docx");
		addAllParagraphsFromModelFile(currentDocument, "/Users/gaigai/Documents/Study/UKM/03 index/report/答题情况.docx");

		// 综合评价
//		addText(currentDocument, "综合评价", STYLE_TITLE_VALUE);
//		addText(currentDocument,
//				"盖盖与本次测评岗位的匹配程度较高。从总分来看，素质总体情况与岗位整体要求比较符合。如果他（她）从事此项工作，能较好地胜任，圆满完成工作任务。为了进一步提升绩效，建议对本次测评中得分相对较低的素质进行关注和提升，以更好地满足岗位素质要求。",
//				STYLE_TEXT_VALUE);

		// 综合得分情况（基于测评答题情况成长得分
		addText(currentDocument, "综合得分情况（基于测评答题情况成长得分）", STYLE_TITLE_VALUE);
		addText(currentDocument, "", STYLE_TEXT_VALUE);
		Map<String, Double> shapeMap = new HashMap<String, Double>();
		shapeMap.put("逻辑推理", (double) 1);
		shapeMap.put("自我管理", (double) 3);
		shapeMap.put("管理潜力", (double) 5);
		shapeMap.put("个性", (double) 7);
		shapeMap.put("动力", (double) 9);
//		//生成图表
		AddColumnChart(currentDocument, 432, 252, shapeMap);

		// 指标得分情况
		addText(currentDocument, "", STYLE_TEXT_VALUE);
		addText(currentDocument, "指标得分情况", STYLE_TITLE_VALUE);

		// 添加表格
		Table table = new Table(currentDocument);
		currentDocument.getFirstSection().getBody().appendChild(table);

		// 添加表头行
		Row headerRow = new Row(currentDocument);
		table.appendChild(headerRow);
		headerRow.appendChild(createCell(currentDocument, "指标分类"));
		headerRow.appendChild(createCell(currentDocument, ""));
		headerRow.appendChild(createCell(currentDocument, "指标"));
		headerRow.appendChild(createCell(currentDocument, "得分"));
		headerRow.appendChild(createCell(currentDocument, "得分"));
		headerRow.appendChild(createCell(currentDocument, "得分"));
		headerRow.appendChild(createCell(currentDocument, "得分"));
		headerRow.appendChild(createCell(currentDocument, "得分"));
		headerRow.appendChild(createCell(currentDocument, "得分"));
		headerRow.appendChild(createCell(currentDocument, "得分"));
		headerRow.appendChild(createCell(currentDocument, "得分"));
		headerRow.appendChild(createCell(currentDocument, "得分"));
		// 处理表头
		int tableIndex = 2;
		mergeHorizontalRange(currentDocument, tableIndex, 0, 0, 1);
		mergeHorizontalRange(currentDocument, tableIndex, 0, 3, 11);

//		// 添加数据行
//		String[][] data = { { "自我管理", "6", "时间管理", "4.5" }, { "自我管理", "6", "问题分析与解决", "3" },
//				{ "自我管理", "6", "学习能力", "2" }, };

		JSONArray catItems = new JSONArray();
		JSONObject catItem0 = new JSONObject();
		catItem0.put("catName", "自我管理能力1");
		catItem0.put("catValue", "2");
		catItem0.put("catDesc", "你很棒！！！！！！！");

		JSONObject catItem1 = new JSONObject();
		catItem1.put("catName", "自我管理能力2");
		catItem1.put("catValue", "3");
		catItem1.put("catDesc", "你很DDDD棒！！！！！！！");

		JSONArray catItem0Itmes = new JSONArray();
		JSONObject catItem0Itmes0 = new JSONObject();
		catItem0Itmes0.put("key", "时间管理");
		catItem0Itmes0.put("value", "1");
		catItem0Itmes0.put("desc", "测试1111");
		JSONObject catItem0Itmes1 = new JSONObject();
		catItem0Itmes1.put("key", "问题分析与解决");
		catItem0Itmes1.put("value", "2");
		catItem0Itmes1.put("desc", "测试222");
		JSONObject catItem0Itmes2 = new JSONObject();
		catItem0Itmes2.put("key", "问题分析与解决333");
		catItem0Itmes2.put("value", "3");
		catItem0Itmes2.put("desc", "测试333");
		catItem0Itmes.add(catItem0Itmes0);
		catItem0Itmes.add(catItem0Itmes1);
		catItem0Itmes.add(catItem0Itmes2);
		catItem0.put("items", catItem0Itmes);
		catItem1.put("items", catItem0Itmes);
		catItems.add(catItem0);
		catItems.add(catItem1);

		int startRow = 1;
		for (int i = 0; i < catItems.size(); i++) {
			JSONObject rowData = catItems.getJSONObject(i);
			JSONArray items = rowData.getJSONArray("items");
			for (int j = 0; j < items.size(); j++) {
				JSONObject item = items.getJSONObject(j);
				Row dataRow = new Row(currentDocument);
				table.appendChild(dataRow);
				dataRow.appendChild(createCell(currentDocument, rowData.getString("catName")));
				dataRow.appendChild(createCell(currentDocument, rowData.getString("catValue")));
				dataRow.appendChild(createCell(currentDocument, item.getString("key")));
				// 根据得分设置单元格样式
				long score = Math.round(new Double(item.getString("value"))); // 四舍五入为最接近的整数
				for (int k = 1; k < 10; k++) {
					Cell scoreCell = createCell(currentDocument, k + "");
					dataRow.appendChild(scoreCell);
					if (score == k) {
						scoreCell.getCellFormat().getShading().setBackgroundPatternColor(Color.YELLOW);
					}
				}
			}
			int endRow = (startRow + items.size() - 1);
			mergeVerticalRange(currentDocument, tableIndex, 0, startRow, endRow);
			mergeVerticalRange(currentDocument, tableIndex, 1, startRow, endRow);
			startRow = startRow + items.size();
		}

		// 逻辑推理
		addText(currentDocument, "", STYLE_TEXT_VALUE);
		addText(currentDocument, "逻辑推理分析报告", STYLE_TITLE_VALUE);
		addText(currentDocument,
				"逻辑推理能是以敏锐的思考分析、快捷的反应、迅速地掌握问题的核心，在最短时间找出其事物内在的逻辑关系，从而推理出符合逻辑关系的结论的能力。具备了逻辑推理能力，才能对事物做出符合逻辑关系的正确判断，因此逻辑推理能力也是个人重要的基础素质之一。"
						+ "在人才评估中，逻辑推理能力经常用于评估人才的智商/潜力。",
				STYLE_TEXT_VALUE);
		addText(currentDocument, "你的逻辑得分为X。", STYLE_TEXT_VALUE);

		// 能力分析报告
		addText(currentDocument, "", STYLE_TEXT_VALUE);
		addText(currentDocument, "能力分析报告", STYLE_TITLE_VALUE);
//		addModelTable(currentDocument, "/Users/gaigai/Documents/Study/UKM/03 index/report/测评报告模板_自我管理能力.docx");
//		addAllParagraphsFromModelFile(currentDocument,
//				"/Users/gaigai/Documents/Study/UKM/03 index/report/测评报告模板_自我管理能力.docx");

		dealExcel("/Users/gaigai/Documents/Study/UKM/03 index/report/table.xlsx",
				"/Users/gaigai/Documents/Study/UKM/03 index/report/tablegaigai.xlsx", catItems);
//		WordToPdf.xls2image("/Users/gaigai/Documents/Study/UKM/03 index/report/tablegaigai.xlsx",
//				"/Users/gaigai/Documents/Study/UKM/03 index/report/tablegaigai.png");
		// 替换文本
		replaceText(currentDocument, textMap);
		// 添加段落
		Paragraph para = new Paragraph(currentDocument);
		currentDocument.getFirstSection().getBody().appendChild(para);
		// 插入图片
		insertImage(para, "/Users/gaigai/Documents/Study/UKM/03 index/report/tablegaigai.png");

		// 添加关于报告
		addText(currentDocument, "关于报告", STYLE_TITLE_VALUE);
		String aboutReport = "<p><span>本报告由精派咨询开发的人才素质测评系统自动生成。本报告是基于答题者对测验的回答结果所生成，并且充分体现了答题者所选答案反映的各项素质的水平。</span></p>\n"
				+ "<p><span>本测验的结果只是为了解被评价者提供不同角度的信息，但不能作为唯一的依据。应该与其他有关的信息，如教育背景、日常工作行为、工作经历和工作业绩等结合分析，以更好地提高人才评价的准确性。</span></p>\n"
				+ "<p><span>本报告内容属于个人隐私，请注意保密。</span></p>\n"
				+ "<p><span>&nbsp;</span></p>\n"
				+ "<p><span>若有任何疑问，请咨询精派咨询专业咨询顾问。</span></p>\n"
				+ "<p><span>&nbsp;</span></p>\n"
				+ "<p style=\"padding-left: 40px; text-align: right;\"><span>精派咨询</span></p>";
		addHtmlText(currentDocument, aboutReport,STYLE_TEXT_VALUE);

		
		
		
		// 更新目录
		currentDocument.updateFields();

		// 保存文件
		currentDocument.save("/Users/gaigai/Documents/Study/UKM/03 index/report/测评报告模板-盖盖-test.docx");

	}

	/**
	 * 插入标题以及一段HTML话
	 * 
	 * @param currentDocument
	 * @param title
	 * @param aboutReport
	 * @throws Exception
	 */
	public static void insertTitleAndHtml(Document currentDocument, String title, String aboutReport) throws Exception {
		// 添加关于报告
		addText(currentDocument, title, STYLE_TITLE_VALUE);
		addHtmlText(currentDocument, aboutReport,STYLE_TEXT_VALUE);
	}

	// 插入图片
	private static void insertImage(Paragraph para, String imagePath) throws Exception {
		// 加载图片文件到字节数组
		byte[] imageBytes = loadImageToByteArray(imagePath);		
	    File inputFile = new File(imagePath); // 替换为你的原始图像文件路径
        BufferedImage originalImage = ImageIO.read(inputFile);
        // 目标宽度
        int targetWidth = 600;
        // 计算缩放比例
        double scaleFactor = (double) targetWidth / originalImage.getWidth();
        // 计算缩放后的高度
        int scaledHeight = (int) (originalImage.getHeight() * scaleFactor);	
		// 将字节数组中的图片插入到文档
		// 创建形状，设置图片大小和布局
		Shape shape = new Shape(para.getDocument(), ShapeType.IMAGE);
		shape.getImageData().setImage(imageBytes);
		shape.setWidth(targetWidth); // 设置图片宽度，单位为磅（points）
		shape.setHeight(scaledHeight); // 设置图片高度，单位为磅（points）
		shape.setWrapType(WrapType.SQUARE); // 设置图片环绕方式
		// 将形状插入段落
		para.appendChild(shape);
		shape.getImageData().setImage(imageBytes);
	}

	// 将图片加载到字节数组
	private static byte[] loadImageToByteArray(String imagePath) throws Exception {
		java.io.FileInputStream fis = new java.io.FileInputStream(imagePath);
		byte[] buffer = new byte[fis.available()];
		fis.read(buffer);
		fis.close();
		return buffer;
	}

	// 创建一个带有指定文本的单元格
	private static Cell createCell(Document doc, String text) {
		Cell cell = new Cell(doc);
		cell.appendChild(new Paragraph(cell.getDocument()));
		cell.getCellFormat().setVerticalAlignment(VerticalAlignment.CENTER); // 设置垂直对齐
		Run run = new Run(cell.getDocument(), text);
		cell.getFirstParagraph().appendChild(run);
		return cell;
	}

	// 合并指定横向范围的单元格
	private static void mergeHorizontalRange(Document currentDocument, int tableIndex, int rowIndex, int startColIndex,
			int endColIndex) throws Exception {
		DocumentBuilder builder = new DocumentBuilder(currentDocument);
		// 移动至第一行第一列
		builder.moveToCell(tableIndex, rowIndex, startColIndex, 0);
		builder.getCellFormat().setHorizontalMerge(CellMerge.FIRST);// 合并的第一个单元格
		for (int colIndex = startColIndex + 1; colIndex <= endColIndex; colIndex++) {
			builder.moveToCell(tableIndex, rowIndex, colIndex, 0);
			builder.getCellFormat().setHorizontalMerge(CellMerge.PREVIOUS);

		}
	}

	// 合并指定纵向范围的单元格
	private static void mergeVerticalRange(Document currentDocument, int tableIndex, int colIndex, int startRowIndex,
			int endRowIndex) throws Exception {
		DocumentBuilder builder = new DocumentBuilder(currentDocument);
		// 移动至第一行第一列
		builder.moveToCell(tableIndex, startRowIndex, colIndex, 0);
		builder.getCellFormat().setVerticalMerge(CellMerge.FIRST);// 合并的第一个单元格
		for (int rowIndex = startRowIndex + 1; rowIndex <= endRowIndex; rowIndex++) {
			builder.moveToCell(tableIndex, rowIndex, colIndex, 0);
			builder.getCellFormat().setVerticalMerge(CellMerge.PREVIOUS);// 被合并的单元格
		}
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
			    System.out.println("key:"+key+",value:"+value);
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
			if(value==null) {
				value="空值";
			}
			range.replace("@$"+key+"@$", value, false, false);
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
	public static void addHtmlText(Document currentDocument, String text,String style) throws Exception {
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
	 * 
	 * @param currentDocument
	 * @param width
	 * @param height
	 * @param valueMap
	 * @throws Exception
	 */
	public static void AddColumnChart(Document currentDocument, int width, int height, Map<String, Double> valueMap)
			throws Exception {
		DocumentBuilder builder = new DocumentBuilder(currentDocument);
		builder.moveToDocumentEnd(); // 252 432
		Shape shape = builder.insertChart(ChartType.COLUMN, width, height);
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
		// 赋值
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

	/**
	 * UpdateChartExample
	 * 
	 * @param destDoc
	 * @param valueMap
	 * @throws Exception
	 */
	public static void UpdateChartExample(Document destDoc, Map<String, Double> valueMap) throws Exception {
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
			// 赋值
			for (String key : valueMap.keySet()) {
				double value = valueMap.get(key);
				chartSeriesCollection.add(key, new double[] { 0 }, new double[] { value });
			}
//			// Insert the imported node into the destination document
			Node newNode = importer.importNode(srcShape, true);
			builder.moveToDocumentEnd();
			builder.insertNode(newNode);
		}
	}

	/**
	 * 处理EXCEL
	 * 
	 * @param inxlsUrl
	 * @param outXlsUrl
	 * @throws IOException
	 */
	public static void dealExcel(String inxlsUrl, String outXlsUrl, JSONArray catItems) throws IOException {
		File modexls = new File(inxlsUrl);
		InputStream inp = new FileInputStream(modexls);
		Workbook wb = new XSSFWorkbook(inp);
		Sheet sheet = wb.getSheetAt(0);
		int rowNum = sheet.getPhysicalNumberOfRows();
		int firstNum = 1;
		for (int i = 0; i < catItems.size(); i++) {
			if (i > 0) {
				for (int j = 0; j < 3; j++) {
					System.out.println(j + "test111");
					copyRow(wb, sheet, j, sheet.getPhysicalNumberOfRows());
				}
				firstNum = sheet.getPhysicalNumberOfRows() - 2;
				System.out.println(firstNum + "test");
			}
			org.apache.poi.ss.usermodel.Row catRow = sheet.getRow(firstNum);
			JSONObject catItem = catItems.getJSONObject(i);
			System.out.println(catItem);
			String catName = catItem.getString("catName");
			double catValue = catItem.getDouble("catValue");
			String catDesc = catItem.getString("catDesc");
			catRow.getCell(0).setCellValue(catName);
			catRow.getCell(1).setCellValue(catValue);
			org.apache.poi.ss.usermodel.Row descRow = sheet.getRow(firstNum + 1);
			descRow.getCell(0).setCellValue(catDesc);
			JSONArray items = catItem.getJSONArray("items");
			boolean firstRow = true;
			for (int j = 0; j < items.size(); j++) {
				JSONObject obj = items.getJSONObject(j);
				org.apache.poi.ss.usermodel.Row itemRow = sheet.getRow(firstNum + 1 + 1 + j);
				if (itemRow == null || itemRow.getRowNum() > sheet.getPhysicalNumberOfRows()) {
					System.out.println("111111111");
					if (firstRow) {
						itemRow = copyRow(wb, sheet, 3, sheet.getPhysicalNumberOfRows());
						firstRow = false;
					} else {
						itemRow = copyRow(wb, sheet, 4, sheet.getPhysicalNumberOfRows());
						firstRow = true;
					}
				}
				String key = obj.getString("key");
				double value = obj.getDouble("value");
				String desc = obj.getString("desc");

				System.out.println(itemRow.getRowNum());
				org.apache.poi.ss.usermodel.Cell c1 = itemRow.getCell(0);
				c1.setCellValue(key);
				org.apache.poi.ss.usermodel.Cell c2 = itemRow.getCell(1);
				c2.setCellValue(value);
				org.apache.poi.ss.usermodel.Cell c3 = itemRow.getCell(2);
				c3.setCellValue(desc);
			}

		}
		sheet.setForceFormulaRecalculation(true);// 刷新公式
		wb.setForceFormulaRecalculation(true);
		XSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
		FileOutputStream o = new FileOutputStream(outXlsUrl);
		try {
			wb.write(o);
			o.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (wb != null) {
				try {
					wb.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 复制单元格poi
	 * 
	 * @param wb
	 * @param sourceSheet
	 * @param sourceRowNumber
	 * @param targetRowNumber
	 */
	public static org.apache.poi.ss.usermodel.Row copyRow(Workbook wb, Sheet sourceSheet, int sourceRowNumber,
			int targetRowNumber) {
		org.apache.poi.ss.usermodel.Row sourceRow = sourceSheet.getRow(sourceRowNumber);
		org.apache.poi.ss.usermodel.Row targetRow = sourceSheet.createRow(targetRowNumber);
		for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
			org.apache.poi.ss.usermodel.Cell sourceCell = sourceRow.getCell(i);
			org.apache.poi.ss.usermodel.Cell targetCell = targetRow.createCell(i);
			if (sourceCell != null) {
				if (sourceCell.getCellType().getCode() == CellType.NUMERIC.getCode()) {
					targetCell.setCellValue(sourceCell.getNumericCellValue());
				} else if (sourceCell.getCellType().getCode() == CellType.FORMULA.getCode()) {
					targetCell.setCellFormula(sourceCell.getCellFormula().replace("2", (targetRowNumber + 1) + ""));
				} else {
					targetCell.setCellValue(sourceCell.getStringCellValue());
				}
				// 复制源单元格的样式
				CellStyle sourceCellStyle = sourceCell.getCellStyle();
				CellStyle targetCellStyle = wb.createCellStyle();
				targetCellStyle.cloneStyleFrom(sourceCellStyle);
				targetCell.setCellStyle(targetCellStyle);

				// 处理合并单元格
				for (int j = 0; j < sourceSheet.getNumMergedRegions(); j++) {
					CellRangeAddress mergedRegion = sourceSheet.getMergedRegion(j);
					if (mergedRegion.isInRange(sourceRowNumber, i) && !sourceSheet
							.getMergedRegion(sourceSheet.getNumMergedRegions() - 1).isInRange(targetRowNumber, i)) {
						int firstRow = mergedRegion.getFirstRow() + targetRowNumber - sourceRowNumber;
						int lastRow = mergedRegion.getLastRow() + targetRowNumber - sourceRowNumber;
						int firstCol = mergedRegion.getFirstColumn();
						int lastCol = mergedRegion.getLastColumn();
						System.out.println(firstRow + ";" + lastRow + ";" + firstCol + ";" + lastCol);
						CellRangeAddress newMergedRegion = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
						sourceSheet.addMergedRegion(newMergedRegion);
					}
				}
			}
			// 复制源行的条件格式
			ConditionalFormatting cf = hasConditionalFormatting(sourceSheet, sourceRowNumber, i);
			if (cf != null) {
				CellRangeAddress[] sourceRanges = cf.getFormattingRanges();
				CellRangeAddress[] targetRanges = new CellRangeAddress[sourceRanges.length];
				for (int k = 0; k < sourceRanges.length; k++) {
					CellRangeAddress sourceRange = sourceRanges[k];
					CellRangeAddress targetRange = new CellRangeAddress(targetRowNumber, targetRowNumber,
							sourceRange.getFirstColumn(), sourceRange.getLastColumn());
					targetRanges[k] = targetRange;
				}
				for (int j = 0; j < cf.getNumberOfRules(); j++) {
					ConditionalFormattingRule conditionalFormattingRule = cf.getRule(j);
					sourceSheet.getSheetConditionalFormatting().addConditionalFormatting(targetRanges,
							conditionalFormattingRule);
				}

			}
		}

		return targetRow;
	}

	/**
	 * 插入答题情况
	 * 
	 * @param tilte
	 * @param dtqkModelPath
	 * @throws Exception
	 */
	public static void insertDTQK(String tilte, String dtqkModelPath) throws Exception {
		// 添加答题情况模块
		addText(currentDocument, tilte, STYLE_TITLE_VALUE);
		addModelTable(currentDocument, dtqkModelPath);
		addAllParagraphsFromModelFile(currentDocument, dtqkModelPath);
	}

	/**
	 * 插入逻辑分析报告
	 * 
	 * @param tilte
	 * @param score
	 * @throws Exception
	 */
	public static void insertFXBG(String tilte, String score) throws Exception {
		// 逻辑推理
		addText(currentDocument, "", STYLE_TEXT_VALUE);
		addText(currentDocument, tilte, STYLE_TITLE_VALUE);
		addText(currentDocument,
				"逻辑推理能是以敏锐的思考分析、快捷的反应、迅速地掌握问题的核心，在最短时间找出其事物内在的逻辑关系，从而推理出符合逻辑关系的结论的能力。具备了逻辑推理能力，才能对事物做出符合逻辑关系的正确判断，因此逻辑推理能力也是个人重要的基础素质之一。"
						+ "在人才评估中，逻辑推理能力经常用于评估人才的智商/潜力。",
				STYLE_TEXT_VALUE);
		addText(currentDocument, "你的逻辑得分为:" + score, STYLE_TEXT_VALUE);
	}

	/**
	 * 插入指标得分情况
	 * 
	 * @param title
	 * @param catItems
	 * @throws Exception
	 */
	public static void insertZBDFQK(String title, JSONArray catItems) throws Exception {
		// 指标得分情况
		addText(currentDocument, "", STYLE_TEXT_VALUE);
		addText(currentDocument, title, STYLE_TITLE_VALUE);
		// 添加表格
		Table table = new Table(currentDocument);
		currentDocument.getFirstSection().getBody().appendChild(table);
		// 添加表头行
		Row headerRow = new Row(currentDocument);
		table.appendChild(headerRow);
		headerRow.appendChild(createCell(currentDocument, "指标分类"));
		headerRow.appendChild(createCell(currentDocument, ""));
		headerRow.appendChild(createCell(currentDocument, "指标"));
		headerRow.appendChild(createCell(currentDocument, "得分"));
		headerRow.appendChild(createCell(currentDocument, "得分"));
		headerRow.appendChild(createCell(currentDocument, "得分"));
		headerRow.appendChild(createCell(currentDocument, "得分"));
		headerRow.appendChild(createCell(currentDocument, "得分"));
		headerRow.appendChild(createCell(currentDocument, "得分"));
		headerRow.appendChild(createCell(currentDocument, "得分"));
		headerRow.appendChild(createCell(currentDocument, "得分"));
		headerRow.appendChild(createCell(currentDocument, "得分"));
		// 处理表头
		int tableIndex = 2;
		mergeHorizontalRange(currentDocument, tableIndex, 0, 0, 1);
		mergeHorizontalRange(currentDocument, tableIndex, 0, 3, 11);
		// 处理表体
		int startRow = 1;
		for (int i = 0; i < catItems.size(); i++) {
			JSONObject rowData = catItems.getJSONObject(i);
			JSONArray items = rowData.getJSONArray("items");
			for (int j = 0; j < items.size(); j++) {
				JSONObject item = items.getJSONObject(j);
				Row dataRow = new Row(currentDocument);
				table.appendChild(dataRow);
				dataRow.appendChild(createCell(currentDocument, rowData.getString("catName")));
				dataRow.appendChild(createCell(currentDocument, rowData.getString("catValue")));
				dataRow.appendChild(createCell(currentDocument, item.getString("key")));
				// 根据得分设置单元格样式
				long score = Math.round(new Double(item.getString("value"))); // 四舍五入为最接近的整数
				for (int k = 1; k < 10; k++) {
					Cell scoreCell = createCell(currentDocument, k + "");
					dataRow.appendChild(scoreCell);
					if (score == k) {
						scoreCell.getCellFormat().getShading().setBackgroundPatternColor(Color.YELLOW);
					}
				}
			}
			int endRow = (startRow + items.size() - 1);
			mergeVerticalRange(currentDocument, tableIndex, 0, startRow, endRow);
			mergeVerticalRange(currentDocument, tableIndex, 1, startRow, endRow);
			startRow = startRow + items.size();
		}
	}

	/**
	 * 综合得分情况（基于测评答题情况成长得分）
	 * 
	 * @param title
	 * @param catArray
	 * @throws Exception
	 */
	public static void insertZHDFQK(String title, JSONArray catArray) throws Exception {
		addText(currentDocument, title, STYLE_TITLE_VALUE);
		addText(currentDocument, "", STYLE_TEXT_VALUE);
		Map<String, Double> shapeMap = new HashMap<String, Double>();
		for (int i = 0; i < catArray.size(); i++) {
			JSONObject catObj = catArray.getJSONObject(i);
			String catName = catObj.getString("catName");
			double catVale = new Double(catObj.getString("catValue"));
			shapeMap.put(catName, catVale);
		}
		// 生成图表
		AddColumnChart(currentDocument, 432, 252, shapeMap);
	}

	/**
	 * 插入能力分析报告
	 * @param title
	 * @param catItems
	 * @param modelPath
	 * @throws Exception
	 */
	public static void insertNLFXBG(String title, JSONArray catItems, String modelPath) throws Exception {
		// 能力分析报告
		addText(currentDocument, "", STYLE_TEXT_VALUE);
		addText(currentDocument, title, STYLE_TITLE_VALUE);
		String path = modelPath.split("\\.")[0];
		UUID uuid = UUID.randomUUID();
		String excelOutPath =
//				"/Users/gaigai/Documents/Study/UKM/03 index/report/tablegaigai";
				path + System.currentTimeMillis() + uuid;
		dealExcel(modelPath, excelOutPath + ".xlsx", catItems);
		WordToPdf.xls2image(excelOutPath + ".xlsx", excelOutPath + ".png");
//		// 添加段落
		Paragraph para = new Paragraph(currentDocument);
		currentDocument.getFirstSection().getBody().appendChild(para);
//		// 插入图片
		insertImage(para, excelOutPath + ".png");
	}

	/**
	 * hasConditionalFormatting
	 * 
	 * @param sheet
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 */
	private static ConditionalFormatting hasConditionalFormatting(Sheet sheet, int rowIndex, int columnIndex) {
		SheetConditionalFormatting sheetCF = sheet.getSheetConditionalFormatting();
		for (int j = 0; j < sheetCF.getNumConditionalFormattings(); j++) {
			ConditionalFormatting cf = sheetCF.getConditionalFormattingAt(j);
			CellRangeAddress[] ranges = cf.getFormattingRanges();
			for (CellRangeAddress range : ranges) {
				if (range.isInRange(rowIndex, columnIndex)) {
					return cf;
				}
			}
		}
		return null;
	}

}
