package com.hh.appraisal.springboot.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.spire.xls.FileFormat;
import com.spire.xls.Workbook;
import com.spire.xls.Worksheet;

import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;

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

//	  /**
//     * aspose转换excel
//     * @throws Exception 
//     */
//    private static void transitionExcel() throws Exception{
//        if(!getLicense()){
//            throw new Exception();
//        }
//        
//        //用于存储excel转换的图片
//        File pngFile = new File("/Users/gaigai/Documents/work/项目/精派咨询/2222.png");
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(pngFile);
//            //使用aspose读取excel
//            com.aspose.cells.Workbook wb = new com.aspose.cells.Workbook(new FileInputStream("/Users/gaigai/Documents/work/项目/精派咨询/111.xlsx"));
//            com.aspose.cells.Worksheet sheet = wb.getWorksheets().get(0);
//            //获取图片写入对象
//            com.aspose.cells.ImageOrPrintOptions imgOption = new  com.aspose.cells.ImageOrPrintOptions();
//            //imgOption.setImageFormat(ImageFormat.getJpeg());
//            imgOption.setCellAutoFit(true);
//            imgOption.setOnePagePerSheet(true);
//            //将sheet写入到图片对象中
//            com.aspose.cells.SheetRender render = new  com.aspose.cells.SheetRender(sheet, imgOption);
//            //将图片写入到输出文件中
//            render.toImage(0, fos);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                //关闭流
//                fos.flush();
//                fos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        
//    }

	public static void xls2image(String inPath, String outPath) throws IOException {
//		com.spire.xls.Workbook workbook =  new Workbook();
//		// 加载Excel文档
//		workbook.loadFromFile(inPath);
//
//		// 获取第一张工作表
//		Worksheet sheet = workbook.getWorksheets().get(0);
//
//		workbook.getConverterSetting().setSheetFitToPage(false);
		// 保存到图片
//		BufferedImage bufferedImage = sheet.toImage(1, 1, sheet.getLastRow(), sheet.getLastColumn());
//		
//		sheet.saveToImage(outPath,1, 1, sheet.getLastRow(), sheet.getLastColumn());

		// 加载Excel文档
		Workbook wb = new Workbook();
		wb.loadFromFile(inPath);

		// 获取第2个工作表
		Worksheet sheet = wb.getWorksheets().get(0);

		// 调用方法保存为PDF格式
		sheet.saveToPdf("/Users/gaigai/Documents/work/项目/敏捷供应链2022年项目/test222.pdf");

		// 写出图片到文件
//		ImageIO.write(bufferedImage, "PNG", new File(outPath));
	}

	public static void main(String[] args) throws Exception {
		xls2image("/Users/gaigai/Documents/work/项目/敏捷供应链2022年项目/工作簿1.xlsx", "");

//		com.spire.xls.Workbook workbook = new com.spire.xls.Workbook();
//	        //加载Excel文档

//	        Workbook workbook = new Workbook();
//		// 加载Excel文档
//		workbook.loadFromFile("/Users/gaigai/Documents/work/项目/精派咨询/111.xlsx");
//
//		// 获取第一张工作表
//		Worksheet sheet = workbook.getWorksheets().get(0);
//
//		// 保存到图片
//		BufferedImage bufferedImage = sheet.toImage(1, 1, sheet.getLastRow(), sheet.getLastColumn());
//
//		// 写出图片到文件
////	        ImageIO.write(bufferedImage, "PNG", new File("sheetToImage.png"));
//
//		// 写出图片到文件
//		ImageIO.write(bufferedImage, "PNG", new File("/Users/gaigai/Documents/work/项目/精派咨询/2222.png"));

//		transitionExcel();
//		doc2pdf("/Users/gaigai/Desktop/keys/说的是.docx",
//				"/Users/gaigai/Desktop/keys/test.pdf");
	}

	public static void doc2pdf(String inPath, String outPath) {
		if (!getLicense()) { // 验证License 若不验证则转化出的pdf文档会有水印产生
			return;
		}
		try {
			long old = System.currentTimeMillis();
			File file = new File(outPath); // 新建一个空白pdf文档
			FileOutputStream os = new FileOutputStream(file);
			Document doc = new Document(inPath); // Address是将要被转化的word文档
			doc.updateFields();// 更新域
			doc.save(os, SaveFormat.PDF);// 全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF,
											// EPUB, XPS, SWF 相互转换
			long now = System.currentTimeMillis();
			System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒"); // 转化用时
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
