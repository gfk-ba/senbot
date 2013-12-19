package com.gfk.senbot.framework.cucumber;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.masterthought.cucumber.ReportBuilder;

public class CucumberReportGenerator {
	
	public static void main(String[] args) throws Exception {
		generateReport(args[0]);
	}

	/**
	 * @param args
	 * @throws Exception
	 * @returns Path to generated report
	 */
	public static String generateReport(String reportBasePath) throws Exception {
		
		String ret = null;
		
		File reportOutputDirectory = new File(reportBasePath);
		
		List<String> findAllJSONFiles = findAllJSONFiles(reportOutputDirectory);
		
		if(!findAllJSONFiles.isEmpty()) {			
			String pluginUrlPath = "";
			String buildNumber = "1";
			String buildProjectName = "super_project";
			Boolean skippedFails = false;
			Boolean undefinedFails = false;
			Boolean flashCharts = true;
			Boolean runWithJenkins = false;
			Boolean artifactsEnabled = false;
			String artifactConfig = "";
			Boolean highChart = true;
			ReportBuilder reportBuilder = new ReportBuilder(findAllJSONFiles, 
					reportOutputDirectory,
					pluginUrlPath,
					buildNumber,
					buildProjectName,
					skippedFails,
					undefinedFails,
					flashCharts,
					runWithJenkins,
					artifactsEnabled,
					artifactConfig, 
					highChart);
			reportBuilder.generateReports();
			
			ret = reportOutputDirectory + "/feature-overview.html";
		}
		
		return ret;
	}
	
	
	private static List<String> findAllJSONFiles(File file) {
		List<String> ret = new ArrayList<String>();
		if(file.isDirectory()) {			
			File[] subFiles = file.listFiles();
			for(File childFile : subFiles) {
				ret.addAll(findAllJSONFiles(childFile));
			}
		}
		else if (file.getName().toLowerCase().endsWith(".json")) {
			ret.add(file.getAbsolutePath());
		}
		return ret;
	}

}
