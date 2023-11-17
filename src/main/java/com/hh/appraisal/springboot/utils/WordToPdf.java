package com.hh.appraisal.springboot.utils;

import com.aspose.words.Document;
import com.aspose.words.Field;
import com.aspose.words.FieldType;
import com.aspose.words.License;
import com.spire.xls.ConverterSetting;
import com.spire.xls.Workbook;
import com.spire.xls.Worksheet;
import com.spire.xls.collections.WorksheetsCollection;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WordToPdf {
	private static InputStream license;
	private static final Logger logger = LoggerFactory.getLogger(WordToPdf.class);

	public static boolean getLicense() {
		boolean result = false;
		try {
			license = WordToPdf.class.getClassLoader().getResourceAsStream("license.xml");
			License aposeLic = new License();
			aposeLic.setLicense(license);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void xls2image(String inPath, String outPath) throws IOException {
		Workbook workbook = new Workbook();

		workbook.loadFromFile(inPath);

		Worksheet sheet = workbook.getWorksheets().get(0);

		workbook.getConverterSetting().setSheetFitToPage(true);

		sheet.saveToImage(outPath, 1, 1, sheet.getLastRow(), sheet.getLastColumn());
	}

	public static void main(String[] args) throws Exception {
		Workbook workbook = new Workbook();

		workbook.loadFromFile("/Users/gaigai/Documents/work/项目/精派咨询/111.xlsx");

		Worksheet sheet = workbook.getWorksheets().get(0);

		BufferedImage bufferedImage = sheet.toImage(1, 1, sheet.getLastRow(), sheet.getLastColumn());

		ImageIO.write(bufferedImage, "PNG", new File("/Users/gaigai/Documents/work/项目/精派咨询/2222.png"));
	}

	
	public static void relaceAlltext(String inPath,  String outPath,  Map<String,String> textMap) {
		if (!getLicense()) {
			return;
		}
		try {
	    System.out.println("开始替换！！！！！");
		Document doc = new Document(inPath);
		System.out.println("内容文本："+doc.getText());
	       // 遍历 textMap 并替换文本
        for (Map.Entry<String, String> entry : textMap.entrySet()) {
            String oldText = entry.getKey();
            String newText = entry.getValue();
            logger.debug("oldText:"+oldText);
            logger.debug("newText:"+newText);
            doc.getRange().replace(oldText, newText, true, false);           
        }
        doc.save(outPath);
        System.out.println("替换结束以及保存！！！！！");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			System.out.println("错误信息："+e.getMessage());
		}
	}
	
	
	public static void doc2pdf(String inPath, String outPath) {
		if (!getLicense()) {
			return;
		}
		try {
		    System.out.println("开始转换pdf！！！！！");
			long old = System.currentTimeMillis();
			File file = new File(outPath);
			FileOutputStream os = new FileOutputStream(file);
			Document doc = new Document(inPath);
			  // Create an object that contains options for the update.
			 // Alternatively, if you want to update only TOC fields, you can do:
	         for (Field field : doc.getRange().getFields()) {
	             if (field.getType() == FieldType.FIELD_TOC) {
	                 field.update();
	             }
	         }
			doc.save(os, 40);
			doc.deepClone();
			os.close();
			long now = System.currentTimeMillis();
			System.out.println("共耗时：" + (now - old) / 1000.0D + "秒");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
