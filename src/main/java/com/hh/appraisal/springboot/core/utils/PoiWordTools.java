package com.hh.appraisal.springboot.core.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.Units;
import org.apache.poi.xddf.usermodel.chart.XDDFBarChartData;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.Borders;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFChart;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.drawingml.x2006.chart.*;
import org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTParaRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHighlightColor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STUnderline;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;

import cn.hutool.json.JSONObject;

/**
 * poi生成word的工具类 针对于模板中的图表是静态的，也就是模板中的图表长什么样子不会根据数据而改变
 */
public class PoiWordTools {

	private static final BigDecimal bd2 = new BigDecimal("2");

	/**
	 * @Description: 替换段落和表格中
	 */
	public static void replaceAll(XWPFDocument doc, String logo, List<String> titleArr, List<String> fldNameArr,
			List<Map<String, String>> listItemsByType, Map<String, String> textMap, Map<String, String> imgMap,
			JSONArray divisors) throws Exception {
//		Map<String, POIXMLDocumentPart> chartsMap = new HashMap<String, POIXMLDocumentPart>();
//		// 动态刷新图表
//		List<POIXMLDocumentPart> relations = doc.getRelations();
//		for (POIXMLDocumentPart poixmlDocumentPart : relations) {
//			if (poixmlDocumentPart instanceof XWPFChart) { // 如果是图表元素
//				// 获取图表对应的表格数据里面的第一行第一列数据，可以拿来当作key值
//				String str = poixmlDocumentPart.toString();
//				System.out.println("str：" + str);
//				String key = str.replaceAll("Name: ", "").replaceAll(
//						" - Content Type: application/vnd\\.openxmlformats-officedocument\\.drawingml\\.chart\\+xml",
//						"").trim();
//				System.out.println("key：" + key);
//				chartsMap.put(key, poixmlDocumentPart);
//			}
//		}
		// 注意这里的key值
//		POIXMLDocumentPart poixmlDocumentPart = chartsMap.get("/word/charts/chart1.xml");
//		replaceBarCharts(poixmlDocumentPart, titleArr, fldNameArr, listItemsByType); // 处理图表数据，柱状图、折线图、饼图啊之类的

		createHeader(doc, logo);
		
		doParagraphs(doc, textMap, imgMap);

		dealDivisor(doc, divisors);

	

//		doExcel(doc,items);
	}

	/**
	 * 处理图表
	 *
	 * @param doc
	 * @throws IOException
	 */
	public static void doExcel(XWPFDocument doc) throws IOException {
		/** ----------------------------处理图表------------------------------------ **/
		// 获取word模板中的所有图表元素，用map存放
		// 为什么不用list保存：查看doc.getRelations()的源码可知，源码中使用了hashMap读取文档图表元素，
		// 对relations变量进行打印后发现，图表顺序和文档中的顺序不一致，也就是说relations的图表顺序不是文档中从上到下的顺序
		Map<String, POIXMLDocumentPart> chartsMap = new HashMap<String, POIXMLDocumentPart>();
		// 动态刷新图表
		List<POIXMLDocumentPart> relations = doc.getRelations();
		for (POIXMLDocumentPart poixmlDocumentPart : relations) {
			if (poixmlDocumentPart.toString().contains("/word/embeddings/Microsoft_Excel____1.xlsx")) {
				boolean result = true;
				File file = new File("/Users/gaigai/Documents/work/项目/精派咨询/model.xlsx");
				InputStream inp = new FileInputStream(file);
				Workbook wb = new XSSFWorkbook(inp);
				Sheet sheet = wb.getSheetAt(0);
				int rowNum = sheet.getLastRowNum();

				for (int i = 3; i < rowNum; i++) {
					Row row = sheet.getRow(i);
					Cell c = row.getCell(4);
					c.setCellValue(2);
				}
				OutputStream xlsOut = poixmlDocumentPart.getPackagePart().getOutputStream();
				try {
					wb.write(xlsOut);
					xlsOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (wb != null) {
						try {
							wb.close();
						} catch (IOException e) {
							e.printStackTrace();
							result = false;
						}
					}
				}

			}
		}

		System.out.println("\n图表数量：" + chartsMap.size() + "\n");

	}

	/**
	 * 读取文档中表格
	 * 
	 * @param filePath doc路径
	 * @param set      第几个表格
	 */
	public void read(XWPFDocument doc, String filePath, String tableName) {
		// 获取word中的所有段落与表格
		List<IBodyElement> elements = doc.getBodyElements();
		for (IBodyElement element : elements) {
			getTabelText((XWPFTable) element);
		}
	}

	/**
	 * 获取表格内容
	 * 
	 * @param table
	 */
	private void getTabelText(XWPFTable table) {
		List<XWPFTableRow> rows = table.getRows();
		for (XWPFTableRow row : rows) {
			List<XWPFTableCell> cells = row.getTableCells();
			for (XWPFTableCell cell : cells) {
				// 简单获取内容（简单方式是不能获取字体对齐方式的）
				// System.out.println(cell.getText());
				// 一个单元格可以理解为一个word文档，单元格里也可以加段落与表格
				List<XWPFParagraph> paragraphs = cell.getParagraphs();
				for (XWPFParagraph paragraph : paragraphs) {

				}
			}
		}
	}

	/**
	 * 页眉插入图片
	 * 
	 * @param doc
	 * @param logoFilePath
	 * @throws Exception
	 */
	public static void createHeader(XWPFDocument doc, String logoFilePath) throws Exception {
		CTSectPr sectPr = doc.getDocument().getBody().addNewSectPr();
		XWPFHeaderFooterPolicy headerFooterPolicy = new XWPFHeaderFooterPolicy(doc, sectPr);
		XWPFHeader header = headerFooterPolicy.createHeader(headerFooterPolicy.DEFAULT);
		XWPFParagraph paragraph = header.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.LEFT);
		paragraph.setBorderBottom(Borders.THICK);
		XWPFRun run = paragraph.createRun();
		if (!StringUtils.isEmpty(logoFilePath)) {
			URL url = new URL(logoFilePath);
			InputStream is = url.openStream();
			BufferedImage sourceImg = ImageIO.read(url);
			int width=sourceImg.getWidth()/30;
			int height=sourceImg.getHeight()/30;
			System.out.println("width:"+width+";height:"+height);
			XWPFPicture picture = run.addPicture(is, XWPFDocument.PICTURE_TYPE_JPEG, logoFilePath, Units.toEMU(width),
					Units.toEMU(height));
//			XWPFPicture picture = run.addPicture(is, XWPFDocument.PICTURE_TYPE_JPEG, logoFilePath, Units.toEMU(140),
//					Units.toEMU(51));
			String blipID = "";
			for (XWPFPictureData picturedata : header.getAllPackagePictures()) { // 这段必须有，不然打开的logo图片不显示
				blipID = header.getRelationId(picturedata);
				picture.getCTPicture().getBlipFill().getBlip().setEmbed(blipID);
			}
			is.close();
		}
	}

	/**
	 * 处理段落文字
	 *
	 * @param doc
	 * @throws InvalidFormatException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void dealDivisor(XWPFDocument doc, JSONArray divisors) throws Exception {

		/** ----------------------------处理段落------------------------------------ **/

		/** ----------------------------处理段落------------------------------------ **/
		List<XWPFParagraph> paragraphList = doc.getParagraphs();
		if (paragraphList != null && paragraphList.size() > 0) {
			for (XWPFParagraph paragraph : paragraphList) {
				List<XWPFRun> runs = paragraph.getRuns();
				for (XWPFRun run : runs) {
					String text = run.getText(0);
					if (text != null) {
						if (text.contains("因子得分情况详细说明替换位置")) {
							for (int i = 0; i < divisors.size(); i++) {
								com.alibaba.fastjson.JSONObject divisor = divisors.getJSONObject(i);
								String divisorName = divisor.getString("divisorName");
								String divisorDesc = divisor.getString("divisorDesc");
								String divisorAverag = divisor.getString("divisorAverag");
								String contentZh = divisor.getString("contentZh");
								String contentEn = divisor.getString("contentEn");
//								2.	偏执
//								该因子的症状表现：比如说觉得有人在背后议论自己，不相信他人，认为别人在和自己作对
//								你在该因子的得分为（  A  ），表明该方面（     B     ），（     C     ）
//								XWPFParagraph newParagraph = newParagraph(paragraph);
//								newParagraph.setIndentationLeft(0);
//								newParagraph.setIndentationHanging(0);
//								newParagraph.setAlignment(ParagraphAlignment.LEFT);
//								newParagraph.setWordWrap(true);
								// 在段落中新插入一个run,这里的run我理解就是一个word文档需要显示的个体,里面可以放文字,参数0代表在段落的最前面插入
								XWPFRun runtext2 = paragraph.insertNewRun(1);
								// 设置run内容
								runtext2.setText("你在该因子的得分为" + divisorAverag + ",表明该方面" + contentZh + "。" + contentEn);
								runtext2.addCarriageReturn();// 硬回车
								runtext2.addCarriageReturn();// 硬回车
								runtext2.setKerning(1);

								XWPFRun runtext3 = paragraph.insertNewRun(1);
//								runtext3.getCTR().addNewRPr().addNewHighlight().setVal(STHighlightColor.LIGHT_GRAY);
								// 设置run内容
								runtext3.setText("该因子的症状表现：" + divisorDesc);
//								runtext3.setVal(STHighlightColor.DARK_GRAY);
								runtext3.addCarriageReturn();// 硬回车
								runtext3.setKerning(1);
//								runtext3.setColor("d2a112");
//								CTShd shd = runtext3.getCTR().addNewRPr().addNewShd();
//								shd.setFill("FFFF00");
//								runtext3.setText("底纹");
								

								XWPFRun runtext = paragraph.insertNewRun(1);
								// 设置run内容
								runtext.setText((divisors.size() - i) + "." + divisorName);
								runtext.addCarriageReturn();// 硬回车
								runtext.setBold(true);
								runtext.setKerning(2);
//								runtext.setUnderline(UnderlinePatterns.SINGLE);

							}
							run.setText(text.replace("因子得分情况详细说明替换位置", ""), 0);
							break;

						}
					}
				}
				for (XWPFRun run : runs) {
					String text = run.getText(0);
					if (text != null) {
						// 替换文本信息
						String tempText = text;
						String key = tempText.replaceAll("\\$", "");
						key = key.replaceAll("@", "");
						key = key.replaceAll("image", "");
						run.setText(key, 0);
					}
				}
			}
		}
	}

	public static XWPFParagraph newParagraph(XWPFParagraph paragraph) {
		XmlCursor xmlCursor = paragraph.getCTP().newCursor(); // 从段落中获取光标
		xmlCursor.toNextSibling(); // 将光标移动到下一个段落 ，
		// 因为光标生成是在段落的最开始位置 所以需要向下移动一个Xml的光标，也就是将光标移动到下一个段落之前，
		paragraph = paragraph.getDocument().insertNewParagraph(xmlCursor);// 在光标所在位置添加新的段落
		return paragraph;
	}

	/**
	 * 处理EXCEL
	 * 
	 * @param inxlsUrl
	 * @param outXlsUrl
	 * @throws IOException
	 */
	public static void dealExcel(String inxlsUrl, String outXlsUrl, JSONArray items) throws IOException {
		File modexls = new File(inxlsUrl);
		InputStream inp = new FileInputStream(modexls);
		Workbook wb = new XSSFWorkbook(inp);
		Sheet sheet = wb.getSheetAt(0);
		int rowNum = sheet.getPhysicalNumberOfRows();
		System.out.println("rowNum:"+rowNum);
		for (int i = 2; i < rowNum; i++) {
			Row row = sheet.getRow(i);
			// 设置实际值
			com.alibaba.fastjson.JSONObject obj = items.getJSONObject(items.size()-1-i+2);
			String key = obj.getString("key");
			double value = obj.getDouble("value");
			Cell c1 = row.getCell(2);
			System.out.println("c1:"+c1.getStringCellValue());
			c1.setCellValue(key);
			Cell c2 = row.getCell(3);
			c2.setCellValue(value);
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
	 * 处理段落文字
	 *
	 * @param doc
	 * @throws InvalidFormatException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void doParagraphs(XWPFDocument doc, Map<String, String> textMap, Map<String, String> imgMap)
			throws Exception {

		/** ----------------------------处理段落------------------------------------ **/

		/** ----------------------------处理段落------------------------------------ **/
		List<XWPFParagraph> paragraphList = doc.getParagraphs();
		if (paragraphList != null && paragraphList.size() > 0) {
			for (XWPFParagraph paragraph : paragraphList) {
				List<XWPFRun> runs = paragraph.getRuns();
				for (XWPFRun run : runs) {
					String text = run.getText(0);
					if (text != null) {
						// 替换文本信息
						String tempText = text;
						String key = tempText.replaceAll("@$", "").replaceAll("@$", "");
						if (!StringUtils.isEmpty(textMap.get(key))) {
							run.setText(textMap.get(key), 0);
						}
						String imgkey = tempText.replaceAll("@image", "");
						if (!StringUtils.isEmpty(imgMap.get(imgkey))) {		
							String imgPath = imgMap.get(imgkey);
							System.out.println("替换图片：" + imgPath);
							try {
								run.setText("", 0);
								run.addPicture(new FileInputStream(imgPath), Document.PICTURE_TYPE_PNG, "img.png",
										Units.toEMU(410), Units.toEMU(310));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}

			}
		}
	}

	/**
	 * 调用替换雷达图数据
	 */
	public static void replaceRadarCharts(POIXMLDocumentPart poixmlDocumentPart, List<String> titleArr,
			List<String> fldNameArr, List<Map<String, String>> listItemsByType) {
		XWPFChart chart = (XWPFChart) poixmlDocumentPart;
		chart.getCTChart();

		// 根据属性第一列名称切换数据类型
		CTChart ctChart = chart.getCTChart();
		CTPlotArea plotArea = ctChart.getPlotArea();

		// 设置标题
		// new PoiWordTitle().setBarTitle(ctChart, "我是雷达图标题");

		CTRadarChart radarChart = plotArea.getRadarChartArray(0);
		List<CTRadarSer> radarList = radarChart.getSerList(); // 获取雷达图单位

		// 刷新内置excel数据
		new PoiWordTools().refreshExcel(chart, listItemsByType, fldNameArr, titleArr);
		// 刷新页面显示数据
		refreshRadarStrGraphContent(radarChart, radarList, listItemsByType, fldNameArr, 1);

	}

	/**
	 * 调用替换柱状图数据
	 */
	/**
	 * 调用替换柱状图数据
	 */
	public static void replaceBarCharts(POIXMLDocumentPart poixmlDocumentPart, List<String> titleArr,
			List<String> fldNameArr, List<Map<String, String>> listItemsByType) {
		XWPFChart chart = (XWPFChart) poixmlDocumentPart;
		chart.getCTChart();

		// 根据属性第一列名称切换数据类型
		CTChart ctChart = chart.getCTChart();
		CTPlotArea plotArea = ctChart.getPlotArea();

		// 设置标题
//        new PoiWordTitle().setBarTitle(ctChart, "我是修改后的标题");

		CTBarChart barChart = plotArea.getBarChartArray(0);
		List<CTBarSer> BarSerList = barChart.getSerList(); // 获取柱状图单位

//        //刷新内置excel数据
		new PoiWordTools().refreshExcel(chart, listItemsByType, fldNameArr, titleArr);
		// 刷新页面显示数据
		refreshBarStrGraphContent(barChart, BarSerList, listItemsByType, fldNameArr, 1);
	}

	/**
	 * 调用替换折线图数据
	 */
	public static void replaceLineCharts(POIXMLDocumentPart poixmlDocumentPart, List<String> titleArr,
			List<String> fldNameArr, List<Map<String, String>> listItemsByType) {
		XWPFChart chart = (XWPFChart) poixmlDocumentPart;
		chart.getCTChart();

		// 根据属性第一列名称切换数据类型
		CTChart ctChart = chart.getCTChart();
		CTPlotArea plotArea = ctChart.getPlotArea();

		CTLineChart lineChart = plotArea.getLineChartArray(0);
		List<CTLineSer> lineSerList = lineChart.getSerList(); // 获取折线图单位

		// 刷新内置excel数据
		new PoiWordTools().refreshExcel(chart, listItemsByType, fldNameArr, titleArr);
		// 刷新页面显示数据
		new PoiWordTools().refreshLineStrGraphContent(lineChart, lineSerList, listItemsByType, fldNameArr, 1);

	}

	/**
	 * 调用替换饼图数据
	 */
	public static void replacePieCharts(POIXMLDocumentPart poixmlDocumentPart, List<String> titleArr,
			List<String> fldNameArr, List<Map<String, String>> listItemsByType) {
		XWPFChart chart = (XWPFChart) poixmlDocumentPart;
		chart.getCTChart();

		// 根据属性第一列名称切换数据类型
		CTChart ctChart = chart.getCTChart();
		CTPlotArea plotArea = ctChart.getPlotArea();

		CTPieChart pieChart = plotArea.getPieChartArray(0);
		List<CTPieSer> pieSerList = pieChart.getSerList(); // 获取饼图单位

		// 刷新内置excel数据
		new PoiWordTools().refreshExcel(chart, listItemsByType, fldNameArr, titleArr);
		// 刷新页面显示数据
		new PoiWordTools().refreshPieStrGraphContent(pieChart, pieSerList, listItemsByType, fldNameArr, 1);

	}

	/**
	 * 刷新折线图数据方法
	 *
	 * @param typeChart
	 * @param serList
	 * @param dataList
	 * @param fldNameArr
	 * @param position
	 * @return
	 */
	public static boolean refreshLineStrGraphContent(Object typeChart, List<?> serList,
			List<Map<String, String>> dataList, List<String> fldNameArr, int position) {

		boolean result = true;
		// 更新数据区域
		for (int i = 0; i < serList.size(); i++) {
			CTAxDataSource cat = null;
			CTNumDataSource val = null;
			CTLineSer ser = ((CTLineChart) typeChart).getSerArray(i);

			// 设置标题
			CTSerTx tx = ser.getTx();
			tx.getStrRef().getStrCache().getPtList().get(0).setV("阿里嘎痛"); // wps和office都能打开

			// Category Axis Data
			cat = ser.getCat();
			// 获取图表的值
			val = ser.getVal();
			// strData.set
			CTStrData strData = cat.getStrRef().getStrCache();
			CTNumData numData = val.getNumRef().getNumCache();
			strData.setPtArray((CTStrVal[]) null); // unset old axis text
			numData.setPtArray((CTNumVal[]) null); // unset old values

			// set model
			long idx = 0;
			for (int j = 0; j < dataList.size(); j++) {
				// 判断获取的值是否为空
				String value = "0";
				if (new BigDecimal(dataList.get(j).get(fldNameArr.get(i + position))) != null) {
					value = new BigDecimal(dataList.get(j).get(fldNameArr.get(i + position))).toString();
				}
				if (!"0".equals(value)) {
					CTNumVal numVal = numData.addNewPt();// 序列值
					numVal.setIdx(idx);
					numVal.setV(value);
				}
				CTStrVal sVal = strData.addNewPt();// 序列名称
				sVal.setIdx(idx);
				sVal.setV(dataList.get(j).get(fldNameArr.get(0)));
				idx++;
			}
			numData.getPtCount().setVal(idx);
			strData.getPtCount().setVal(idx);

			// 赋值横坐标数据区域
			String axisDataRange = new CellRangeAddress(1, dataList.size(), 0, 0).formatAsString("Sheet1", false);
			cat.getStrRef().setF(axisDataRange);

			// 数据区域
			String numDataRange = new CellRangeAddress(1, dataList.size(), i + position, i + position)
					.formatAsString("Sheet1", false);
			val.getNumRef().setF(numDataRange);

			// 设置系列生成方向

		}
		return result;
	}

	/**
	 * 刷新柱状图数据方法
	 *
	 * @param typeChart
	 * @param serList
	 * @param dataList
	 * @param fldNameArr
	 * @param position
	 * @return
	 */
	public static boolean refreshBarStrGraphContent(Object typeChart, List<?> serList,
			List<Map<String, String>> dataList, List<String> fldNameArr, int position) {
		boolean result = true;
		// 更新数据区域
		for (int i = 0; i < serList.size(); i++) {
			CTAxDataSource cat = null;
			CTNumDataSource val = null;
			CTBarSer ser = ((CTBarChart) typeChart).getSerArray(i);

			// 设置标题 用以下这个方式，可以兼容office和wps
			CTSerTx tx = ser.getTx();
			// tx.getStrRef().getStrCache().getPtList().get(0).setV("嘿嘿嘿");

//			bar.setVaryColors(true);
//			//条形图方向，纵向/横向：纵向
//			bar.setBarDirection(BarDirection.COL);
// 
//			//图表加载数据，条形图1
//			XDDFBarChartData.Series series1 = (XDDFBarChartData.Series) bar.addSeries(countries, area);
//			//条形图例标题
//			series1.setTitle("面积", null);
//			XDDFSolidFillProperties fill = new XDDFSolidFillProperties(XDDFColor.from(PresetColor.RED));
//			//条形图，填充颜色
//			series1.setFillProperties(fill);

			// Category Axis Data
			cat = ser.getCat();
			// 获取图表的值
			val = ser.getVal();
			// strData.set
			CTStrData strData = cat.getStrRef().getStrCache();
			CTNumData numData = val.getNumRef().getNumCache();
			strData.setPtArray((CTStrVal[]) null); // unset old axis text
			numData.setPtArray((CTNumVal[]) null); // unset old values

			// set model
			long idx = 0;
			for (int j = 0; j < dataList.size(); j++) {
				// 判断获取的值是否为空
				String value = "0";
				if (new BigDecimal(dataList.get(j).get(fldNameArr.get(i + position))) != null) {
					value = new BigDecimal(dataList.get(j).get(fldNameArr.get(i + position))).toString();
				}
				if (!"0".equals(value)) {
					CTNumVal numVal = numData.addNewPt();// 序列值
					numVal.setIdx(idx);
					numVal.setV(value);
				}
				CTStrVal sVal = strData.addNewPt();// 序列名称
				sVal.setIdx(idx);
				sVal.setV(dataList.get(j).get(fldNameArr.get(0)));
				// TODO: handle exception
				idx++;
			}
			numData.getPtCount().setVal(idx);
			strData.getPtCount().setVal(idx);

			// 赋值横坐标数据区域
			String axisDataRange = new CellRangeAddress(1, dataList.size(), 0, 0).formatAsString("Sheet1", true);
			cat.getStrRef().setF(axisDataRange);

			// 数据区域
			String numDataRange = new CellRangeAddress(1, dataList.size(), i + position, i + position)
					.formatAsString("Sheet1", true);
			val.getNumRef().setF(numDataRange);

		}
		return result;
	}

	/**
	 * 刷新饼图数据方法
	 *
	 * @param typeChart
	 * @param serList
	 * @param dataList
	 * @param fldNameArr
	 * @param position
	 * @return
	 */
	public static boolean refreshPieStrGraphContent(Object typeChart, List<?> serList,
			List<Map<String, String>> dataList, List<String> fldNameArr, int position) {

		boolean result = true;
		// 更新数据区域
		for (int i = 0; i < serList.size(); i++) {
			// CTSerTx tx=null;
			CTAxDataSource cat = null;
			CTNumDataSource val = null;
			CTPieSer ser = ((CTPieChart) typeChart).getSerArray(i);

			// tx.getStrRef().getStrCache().getPtList().get(0).setV("阿里嘎痛");
			// Category Axis Data
			cat = ser.getCat();
			// 获取图表的值
			val = ser.getVal();
			// strData.set
			CTStrData strData = cat.getStrRef().getStrCache();
			CTNumData numData = val.getNumRef().getNumCache();
			strData.setPtArray((CTStrVal[]) null); // unset old axis text
			numData.setPtArray((CTNumVal[]) null); // unset old values

			// set model
			long idx = 0;
			for (int j = 0; j < dataList.size(); j++) {
				// 判断获取的值是否为空
				String value = "0";
				if (new BigDecimal(dataList.get(j).get(fldNameArr.get(i + position))) != null) {
					value = new BigDecimal(dataList.get(j).get(fldNameArr.get(i + position))).toString();
				}
				if (!"0".equals(value)) {
					CTNumVal numVal = numData.addNewPt();// 序列值
					numVal.setIdx(idx);
					numVal.setV(value);
				}
				CTStrVal sVal = strData.addNewPt();// 序列名称
				sVal.setIdx(idx);
				sVal.setV(dataList.get(j).get(fldNameArr.get(0)));
				idx++;
			}
			numData.getPtCount().setVal(idx);
			strData.getPtCount().setVal(idx);

			// 赋值横坐标数据区域
			String axisDataRange = new CellRangeAddress(1, dataList.size(), 0, 0).formatAsString("Sheet1", true);
			cat.getStrRef().setF(axisDataRange);

			// 数据区域
			String numDataRange = new CellRangeAddress(1, dataList.size(), i + position, i + position)
					.formatAsString("Sheet1", true);
			val.getNumRef().setF(numDataRange);
		}
		return result;
	}

	/**
	 * 刷新内置excel数据
	 *
	 * @param chart
	 * @param dataList
	 * @param fldNameArr
	 * @param titleArr
	 * @return
	 */
	public static boolean refreshExcel(XWPFChart chart, List<Map<String, String>> dataList, List<String> fldNameArr,
			List<String> titleArr) {
		boolean result = true;
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("Sheet1");
		// 根据数据创建excel第一行标题行
		for (int i = 0; i < titleArr.size(); i++) {
			if (sheet.getRow(0) == null) {
				sheet.createRow(0).createCell(i).setCellValue(titleArr.get(i) == null ? "" : titleArr.get(i));
			} else {
				sheet.getRow(0).createCell(i).setCellValue(titleArr.get(i) == null ? "" : titleArr.get(i));
			}
		}

		// 遍历数据行
		for (int i = 0; i < dataList.size(); i++) {
			Map<String, String> baseFormMap = dataList.get(i);// 数据行
			// fldNameArr字段属性
			for (int j = 0; j < fldNameArr.size(); j++) {
				if (sheet.getRow(i + 1) == null) {
					if (j == 0) {
						try {
							sheet.createRow(i + 1).createCell(j)
									.setCellValue(baseFormMap.get(fldNameArr.get(j)) == null ? ""
											: baseFormMap.get(fldNameArr.get(j)));
						} catch (Exception e) {
							if (baseFormMap.get(fldNameArr.get(j)) == null) {
								sheet.createRow(i + 1).createCell(j).setCellValue("");
							} else {
								sheet.createRow(i + 1).createCell(j).setCellValue(baseFormMap.get(fldNameArr.get(j)));
							}
						}
					}
				} else {
					BigDecimal b = new BigDecimal(baseFormMap.get(fldNameArr.get(j)));
					double value = 0d;
					if (b != null) {
						value = b.doubleValue();
					}
					if (value == 0) {
						sheet.getRow(i + 1).createCell(j);
					} else {
						sheet.getRow(i + 1).createCell(j).setCellValue(b.doubleValue());
					}
				}
			}

		}
		// 更新嵌入的workbook
		List<POIXMLDocumentPart> pxdList = chart.getRelations();
		if (pxdList != null && pxdList.size() > 0) {
			for (int i = 0; i < pxdList.size(); i++) {
				if (pxdList.get(i).toString().contains("sheet")) {// 判断为sheet再去进行更新表格数据
					POIXMLDocumentPart xlsPart = pxdList.get(i);
					OutputStream xlsOut = xlsPart.getPackagePart().getOutputStream();
					try {
						wb.write(xlsOut);
						xlsOut.close();
					} catch (IOException e) {
						e.printStackTrace();
						result = false;
					} finally {
						if (wb != null) {
							try {
								wb.close();
							} catch (IOException e) {
								e.printStackTrace();
								result = false;
							}
						}
					}
					break;
				}
			}
		}
//        POIXMLDocumentPart xlsPart = chart.getRelations().get(0);
//        OutputStream xlsOut = xlsPart.getPackagePart().getOutputStream();

		return result;
	}

	/**
	 * 设置表格样式
	 *
	 * @param cell
	 * @param fontName
	 * @param fontSize
	 * @param fontBlod
	 * @param alignment
	 * @param vertical
	 * @param fontColor
	 * @param bgColor
	 * @param cellWidth
	 * @param content
	 */
	public static void setWordCellSelfStyle(XWPFTableCell cell, String fontName, String fontSize, int fontBlod,
			String alignment, String vertical, String fontColor, String bgColor, String cellWidth, String content) {

		// poi对字体大小设置特殊，不支持小数，但对原word字体大小做了乘2处理
		BigInteger bFontSize = new BigInteger("24");
		if (fontSize != null && !fontSize.equals("")) {
			// poi对字体大小设置特殊，不支持小数，但对原word字体大小做了乘2处理
			BigDecimal fontSizeBD = new BigDecimal(fontSize);
			fontSizeBD = bd2.multiply(fontSizeBD);
			fontSizeBD = fontSizeBD.setScale(0, BigDecimal.ROUND_HALF_UP);// 这里取整
			bFontSize = new BigInteger(fontSizeBD.toString());// 字体大小
		}

		// 设置单元格宽度
		cell.setWidth(cellWidth);

		// =====获取单元格
		CTTc tc = cell.getCTTc();
		// ====tcPr开始====》》》》
		CTTcPr tcPr = tc.getTcPr();// 获取单元格里的<w:tcPr>
		if (tcPr == null) {// 没有<w:tcPr>，创建
			tcPr = tc.addNewTcPr();
		}

		// --vjc开始-->>
		CTVerticalJc vjc = tcPr.getVAlign();// 获取<w:tcPr> 的<w:vAlign w:val="center"/>
		if (vjc == null) {// 没有<w:w:vAlign/>，创建
			vjc = tcPr.addNewVAlign();
		}
		// 设置单元格对齐方式
		vjc.setVal(vertical.equals("top") ? STVerticalJc.TOP
				: vertical.equals("bottom") ? STVerticalJc.BOTTOM : STVerticalJc.CENTER); // 垂直对齐

		CTShd shd = tcPr.getShd();// 获取<w:tcPr>里的<w:shd w:val="clear" w:color="auto" w:fill="C00000"/>
		if (shd == null) {// 没有<w:shd>，创建
			shd = tcPr.addNewShd();
		}
		// 设置背景颜色
		shd.setFill(bgColor.substring(1));
		// 《《《《====tcPr结束====

		// ====p开始====》》》》
		CTP p = tc.getPList().get(0);// 获取单元格里的<w:p w:rsidR="00C36068" w:rsidRPr="00B705A0" w:rsidRDefault="00C36068"
										// w:rsidP="00C36068">

		// ---ppr开始--->>>
		CTPPr ppr = p.getPPr();// 获取<w:p>里的<w:pPr>
		if (ppr == null) {// 没有<w:pPr>，创建
			ppr = p.addNewPPr();
		}
		// --jc开始-->>
		CTJc jc = ppr.getJc();// 获取<w:pPr>里的<w:jc w:val="left"/>
		if (jc == null) {// 没有<w:jc/>，创建
			jc = ppr.addNewJc();
		}
		// 设置单元格对齐方式
		jc.setVal(alignment.equals("left") ? STJc.LEFT : alignment.equals("right") ? STJc.RIGHT : STJc.CENTER); // 水平对齐
		// <<--jc结束--
		// --pRpr开始-->>
		CTParaRPr pRpr = ppr.getRPr(); // 获取<w:pPr>里的<w:rPr>
		if (pRpr == null) {// 没有<w:rPr>，创建
			pRpr = ppr.addNewRPr();
		}
		CTFonts pfont = pRpr.getRFonts();// 获取<w:rPr>里的<w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体"/>
		if (pfont == null) {// 没有<w:rPr>，创建
			pfont = pRpr.addNewRFonts();
		}
		// 设置字体
		pfont.setAscii(fontName);
		pfont.setEastAsia(fontName);
		pfont.setHAnsi(fontName);

		CTOnOff pb = pRpr.getB();// 获取<w:rPr>里的<w:b/>
		if (pb == null) {// 没有<w:b/>，创建
			pb = pRpr.addNewB();
		}
		// 设置字体是否加粗
		pb.setVal(fontBlod == 1 ? STOnOff.ON : STOnOff.OFF);

		CTHpsMeasure psz = pRpr.getSz();// 获取<w:rPr>里的<w:sz w:val="32"/>
		if (psz == null) {// 没有<w:sz w:val="32"/>，创建
			psz = pRpr.addNewSz();
		}
		// 设置单元格字体大小
		psz.setVal(bFontSize);
		CTHpsMeasure pszCs = pRpr.getSzCs();// 获取<w:rPr>里的<w:szCs w:val="32"/>
		if (pszCs == null) {// 没有<w:szCs w:val="32"/>，创建
			pszCs = pRpr.addNewSzCs();
		}
		// 设置单元格字体大小
		pszCs.setVal(bFontSize);
		// <<--pRpr结束--
		// <<<---ppr结束---

		// ---r开始--->>>
		List<CTR> rlist = p.getRList(); // 获取<w:p>里的<w:r w:rsidRPr="00B705A0">
		CTR r = null;
		if (rlist != null && rlist.size() > 0) {// 获取第一个<w:r>
			r = rlist.get(0);
		} else {// 没有<w:r>，创建
			r = p.addNewR();
		}
		// --rpr开始-->>
		CTRPr rpr = r.getRPr();// 获取<w:r w:rsidRPr="00B705A0">里的<w:rPr>
		if (rpr == null) {// 没有<w:rPr>，创建
			rpr = r.addNewRPr();
		}
		// ->-
		CTFonts font = rpr.getRFonts();// 获取<w:rPr>里的<w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体"
										// w:hint="eastAsia"/>
		if (font == null) {// 没有<w:rFonts>，创建
			font = rpr.addNewRFonts();
		}
		// 设置字体
		font.setAscii(fontName);
		font.setEastAsia(fontName);
		font.setHAnsi(fontName);

		CTOnOff b = rpr.getB();// 获取<w:rPr>里的<w:b/>
		if (b == null) {// 没有<w:b/>，创建
			b = rpr.addNewB();
		}
		// 设置字体是否加粗
		b.setVal(fontBlod == 1 ? STOnOff.ON : STOnOff.OFF);
		CTColor color = rpr.getColor();// 获取<w:rPr>里的<w:color w:val="FFFFFF" w:themeColor="background1"/>
		if (color == null) {// 没有<w:color>，创建
			color = rpr.addNewColor();
		}
		// 设置字体颜色
		if (content.contains("↓")) {
			color.setVal("43CD80");
		} else if (content.contains("↑")) {
			color.setVal("943634");
		} else {
			color.setVal(fontColor.substring(1));
		}
		CTHpsMeasure sz = rpr.getSz();
		if (sz == null) {
			sz = rpr.addNewSz();
		}
		sz.setVal(bFontSize);
		CTHpsMeasure szCs = rpr.getSzCs();
		if (szCs == null) {
			szCs = rpr.addNewSz();
		}
		szCs.setVal(bFontSize);
		// -<-
		// <<--rpr结束--
		List<CTText> tlist = r.getTList();
		CTText t = null;
		if (tlist != null && tlist.size() > 0) {// 获取第一个<w:r>
			t = tlist.get(0);
		} else {// 没有<w:r>，创建
			t = r.addNewT();
		}
		t.setStringValue(content);
		// <<<---r结束---
	}

	/**
	 * 获取内置表格数据，拿到第一行第一列格子数据
	 * 有时候模板设计太复杂，对于图表不能精准定位，可以通过设置图表表格数据的第一行第一列格子数据来区分，这个数据不影响图表显示，所以用来区分每个图表
	 */
	public static String getZeroData(POIXMLDocumentPart poixmlDocumentPart) {
		String text = "";
		try {
			XWPFChart chart = (XWPFChart) poixmlDocumentPart;
			chart.getCTChart();

			POIXMLDocumentPart xlsPart = chart.getRelations().get(0);
			InputStream xlsin = xlsPart.getPackagePart().getInputStream();

			Workbook workbook = new XSSFWorkbook(xlsin);
			// 获取第一个sheet
			Sheet sheet = workbook.getSheetAt(0);
			// 第一行
			Row row = sheet.getRow(0);
			// 第一列
			Cell cell = row.getCell(0);

			cell.setCellType(CellType.STRING); // 设置一下格子类型为字符串，不然如果是数字或者时间的话，获取很麻烦
			text = cell.getStringCellValue(); // 获取格子内容

			System.out.println("(0,0)格子值：" + text);

			// 关闭流
			xlsin.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return text;
	}

	/**
	 * 刷新雷达图数据方法
	 *
	 * @param typeChart
	 * @param serList
	 * @param dataList
	 * @param fldNameArr
	 * @param position
	 * @return
	 */
	public static boolean refreshRadarStrGraphContent(Object typeChart, List<?> serList,
			List<Map<String, String>> dataList, List<String> fldNameArr, int position) {
		boolean result = true;
		// 更新数据区域
		for (int i = 0; i < serList.size(); i++) {
			CTAxDataSource cat = null;
			CTNumDataSource val = null;
			CTRadarSer ser = ((CTRadarChart) typeChart).getSerArray(i);

			// 设置标题 用以下这个方式，可以兼容office和wps
			CTSerTx tx = ser.getTx();
			// tx.getStrRef().getStrCache().getPtList().get(0).setV("嘿嘿嘿");

			// Category Axis Data
			cat = ser.getCat();
			// 获取图表的值
			val = ser.getVal();

			// strData.set
			if (null != cat.getNumRef()) {
				cat.unsetNumRef();
			}
			if (null != cat.getStrRef()) {
				cat.unsetStrRef();
			}
			cat.addNewStrRef().addNewStrCache();

			CTStrData strData = cat.getStrRef().getStrCache();
			CTNumData numData = val.getNumRef().getNumCache();
			strData.setPtArray((CTStrVal[]) null); // unset old axis text
			numData.setPtArray((CTNumVal[]) null); // unset old values

			// set model
			long idx = 0;
			for (int j = 0; j < dataList.size(); j++) {
				// 判断获取的值是否为空
				String value = "0";
				if (new BigDecimal(dataList.get(j).get(fldNameArr.get(i + position))) != null) {
					value = new BigDecimal(dataList.get(j).get(fldNameArr.get(i + position))).toString();
				}
				if (!"0".equals(value)) {
					CTNumVal numVal = numData.addNewPt();// 序列值
					numVal.setIdx(idx);
					numVal.setV(value);
				}
				CTStrVal sVal = strData.addNewPt();// 序列名称
				sVal.setIdx(idx);
				sVal.setV(dataList.get(j).get(fldNameArr.get(0)));
				idx++;
			}
			strData.addNewPtCount().setVal(idx);
			numData.getPtCount().setVal(idx);

			// 赋值横坐标数据区域
			String axisDataRange = new CellRangeAddress(1, dataList.size(), 0, 0).formatAsString("Sheet1", true);
			cat.getStrRef().setF(axisDataRange);

			// 数据区域
			String numDataRange = new CellRangeAddress(1, dataList.size(), i + position, i + position)
					.formatAsString("Sheet1", true);
			val.getNumRef().setF(numDataRange);

		}
		return result;
	}

}
