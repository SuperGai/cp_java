package com.hh.appraisal.springboot.core.utils;

import com.alibaba.fastjson.JSONArray;
import com.google.common.base.Strings;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;

import java.io.*;
import java.util.Collections;
import java.util.List;

import org.apache.poi.xwpf.usermodel.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author gaigai
 * @description TODO
 * @date 2023/12/27 21:34
 */
public class ChartUtils {


    public static void dealChart(String modelFilePath, String outPath, JSONArray catArray) throws Exception {
        //获取word模板
        InputStream is = new FileInputStream(modelFilePath);
//        FileInputStream("/Users/gaigai/Documents/Study/UKM/03 index/report/测评报告模板-盖盖");
        XWPFDocument doc = new XWPFDocument(is);

        //获取word中所有图表对象
        List<XWPFChart> charts = doc.getCharts();
        XWPFChart chart = charts.get(0);
        XSSFWorkbook workbook = chart.getWorkbook();
        XSSFSheet sheet = workbook.getSheetAt(0);

//        //获取第一个单系列柱状图
        XWPFChart singleBarChar = charts.get(0);
        //系列信息
        String[] singleBarSeriesNames = {"运动情况"};
        //分类信息
        refreshExcel(sheet,catArray);
        wordExportChar(singleBarChar, "运动情况", singleBarSeriesNames,sheet );
        try (FileOutputStream fos = new FileOutputStream(outPath)) {
            doc.write(fos);
        }
        is.close();
        doc.close();
    }
    public static void refreshExcel(XSSFSheet sheet,JSONArray catArray){
        int startIndex=0;
        for (int i = 0; i < catArray.size(); i++) {
            if(catArray.getJSONObject(i).getString("catName").equals("掩饰性")){
                continue;
            }
            sheet.getRow(startIndex+1).getCell(0).setCellValue(catArray.getJSONObject(i).getString("catName"));
            sheet.getRow(startIndex+1).getCell(1).setCellValue(catArray.getJSONObject(i).getDoubleValue("catValue"));
            startIndex++;
        }
    }

    /**
     * 根据word模板导出 针对图表（柱状图，折线图，饼图等）的处理
     *
     * @param docChart    图表对象
     * @param title       图表标题
     * @param seriesNames 系列名称数组
     * @return {@link XWPFChart}
     * @author LCheng
     * @date 2020/12/10 11:08
     */
    public static XWPFChart wordExportChar(XWPFChart docChart, String title, String[] seriesNames, XSSFSheet sheet) {
        //获取图表数据对象
        XDDFChartData chartData = docChart.getChartSeries().get(0);

        //word图表均对应一个内置的excel，用于保存图表对应的数据
        //excel中 第一列第二行开始的数据为分类信息
        //CellRangeAddress(1, categories.size(), 0, 0) 四个参数依次为 起始行 截止行 起始列 截止列。
        //根据分类信息的范围创建分类信息的数据源
        XDDFDataSource catDataSource = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(1,5,0,0));
        //更新数据
        for (int i = 0; i < seriesNames.length; i++) {
            //excel中各系列对应的数据的范围
            //根据数据的范围创建值的数据源
            XDDFNumericalDataSource<Double> valDataSource = XDDFDataSourcesFactory.fromNumericCellRange(sheet,  new CellRangeAddress(1,5,1,1));
            //获取图表系列的数据对象
            XDDFChartData.Series series = chartData.getSeries().get(i);
            //替换系列数据对象中的分类和值
            series.replaceData(catDataSource, valDataSource);
            //修改系列数据对象中的标题
            CellReference cellReference = docChart.setSheetTitle(seriesNames[i], 1);
            series.setTitle(seriesNames[i], cellReference);
        }
        //更新图表数据对象
        docChart.plot(chartData);
        //图表整体的标题 传空值则不替换标题
//        if (!Strings.isNullOrEmpty(title)) {
//            docChart.setTitleText(title);
//            docChart.setTitleOverlay(false);
//        }
        return docChart;
    }


}
