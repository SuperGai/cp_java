package com.hh.appraisal.springboot.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Data
@Configuration
public class FileConfig {

	@Value("${zip.folder}")
	public String folderPath;
	
	
	@Value("${www.url}")
	public String pdfUrl;
	
//	//模版地址
	@Value("${report.model}")
	public String reportModel;
//	="/Users/gaigai/Documents/work/项目/精派咨询/心理健康测评报告模板.docx";
//	//报表输出地址
	@Value("${report.out.folder}")
	public String reportOutFolder;
//	="/Users/gaigai/Documents/work/项目/精派咨询/";
	
	
	
	
	
	
}
