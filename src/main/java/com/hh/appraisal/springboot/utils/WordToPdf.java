package com.hh.appraisal.springboot.utils;

import com.aspose.words.Document;
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
import java.io.PrintStream;
import javax.imageio.ImageIO;

public class WordToPdf {
	private static InputStream license;

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

	public static void doc2pdf(String inPath, String outPath) {
		if (!getLicense()) {
			return;
		}
		try {
			long old = System.currentTimeMillis();
			File file = new File(outPath);
			FileOutputStream os = new FileOutputStream(file);
			Document doc = new Document(inPath);
			doc.updateFields();
			doc.save(os, 40);

			long now = System.currentTimeMillis();
			System.out.println("共耗时：" + (now - old) / 1000.0D + "秒");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
