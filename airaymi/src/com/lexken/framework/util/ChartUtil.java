package com.lexken.framework.util;

import java.awt.Color;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.servlet.ServletUtilities;


public class ChartUtil {
	/**
	 * 차트 이미지 생성 
	 * @param req, jfreechart, iWidth, iHeight
	 * @return String  
	 * @throws IOException  
	*/
	public static String createChartImage(HttpServletRequest req, JFreeChart jfreechart, int iWidth, int iHeight) throws IOException {	
		String savePath = "";
		String chartFileName = "";
		String chartImagePath = "";
		
		ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
		
		ServletContext servletContext = req.getSession().getServletContext();
		savePath    = servletContext.getRealPath("/data/chart");
		System.setProperty("java.io.tmpdir", savePath);
		
		String saveChartAsPNG = ServletUtilities.saveChartAsPNG(jfreechart, iWidth, iHeight, info, null);
		chartFileName = saveChartAsPNG;
		chartImagePath = "/jsp/DisplayChart.jsp?chartname=" + chartFileName;

		return chartImagePath;
	}


	/**
	 * 차트 색상 순번
	 * @param colorNumber
	 * @return Color  
	 * @throws   
	*/
	public static Color getColorSeq(int colorNumber) {
		Color returnColor = null;
		
		switch(colorNumber) {
			case 0:	returnColor = new Color(210, 77, 77); break;
			case 1:	returnColor = new Color(72, 209, 206); break;
			case 2:	returnColor = new Color(77, 77, 210); break;
			case 3:	returnColor = new Color(210, 78, 207); break;
			case 4:	returnColor = new Color(78, 210, 78); break;
			case 5:	returnColor = new Color(220, 125, 67); break;
			case 6:	returnColor = new Color(78, 147, 210); break;
			case 7:	returnColor = new Color(135, 78, 210); break;
			case 8:	returnColor = new Color(66, 90, 204); break;
			case 9:	returnColor = new Color(202, 226, 63); break;
			case 10: returnColor = new Color(137, 137, 137); break;
		}
		
		
		
		return returnColor;
	}
}
