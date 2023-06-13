package com.hh.appraisal.springboot.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import com.aspose.words.Document;
import com.aspose.words.SaveFormat;

public class PoiWordUtilsNew {

	public static void main(String[] args) throws Exception {
		// 加载已设置大纲级别的测试文档
		long start = System.currentTimeMillis();
		Document doc = new Document("D:\\project\\util\\src\\main\\resources\\poi\\report.docx");
		doc.updateFields();
		doc.save("33333.pdf", SaveFormat.PDF);// 这里执行操作 //
		restWord("目录33331221.docx");
		System.out.println((System.currentTimeMillis() - start) / 1000);
	}

	private static void restWord(String docFilePath) {
		try (FileInputStream in = new FileInputStream(docFilePath)) {
			XWPFDocument doc = new XWPFDocument(OPCPackage.open(in));
			List<XWPFParagraph> paragraphs = doc.getParagraphs();
			if (paragraphs.size() < 1) {
				return;
			}
			XWPFParagraph firstParagraph = paragraphs.get(0);
			XWPFParagraph lastParagraph = paragraphs.get(paragraphs.size() - 1);
			if (firstParagraph.getText().contains("Aspose.Words")) {
				doc.removeBodyElement(doc.getPosOfParagraph(firstParagraph));
				doc.removeBodyElement(doc.getPosOfParagraph(lastParagraph));
			}
			OutputStream out = new FileOutputStream(docFilePath);
			doc.write(out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
