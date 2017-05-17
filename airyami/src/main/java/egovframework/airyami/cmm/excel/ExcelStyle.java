/**
 * <pre>
 * Copyright (c) 2011 Samsung SDS Co., Ltd.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of Samsung SDS Co., Ltd.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with Samsung SDS Co., Ltd.
 *
 * Author		: SangHyeon.Kim
 * Date   		: 2012. 3. 6.
 * Description 	: 
 * <pre/>
 */
package egovframework.airyami.cmm.excel;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;

/**
 * Desc :
 * @Author : SangHyeon.Kim
 * @Date : 2012. 1. 25.
 * @Version :
 */
public class ExcelStyle {

	/**
	 * Desc : Excel 셀스타일 생성
	 * @Method Name : createStyles
	 * @param Workbook
	 * @return Map
	 */
	public Map<String, CellStyle> createStyles(HSSFWorkbook wb) {

		Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
		DataFormat fmt = wb.createDataFormat();

		CellStyle style1 = wb.createCellStyle();
		style1.setAlignment(CellStyle.ALIGN_RIGHT);
		style1.setDataFormat(fmt.getFormat("0.0%"));
		styles.put("percent", style1);

		CellStyle style2 = wb.createCellStyle();
		style2.setAlignment(CellStyle.ALIGN_CENTER);
		style2.setDataFormat(fmt.getFormat("0.0X"));
		styles.put("coeff", style2);

		CellStyle style3 = wb.createCellStyle();
		style3.setAlignment(CellStyle.ALIGN_RIGHT);
		style3.setDataFormat(fmt.getFormat("$#,##0.00"));
		styles.put("currency", style3);

		CellStyle style4 = wb.createCellStyle();
		style4.setAlignment(CellStyle.ALIGN_RIGHT);
		style4.setDataFormat(fmt.getFormat("mmm dd"));
		styles.put("date", style4);

		CellStyle style5 = wb.createCellStyle();
		Font headerFont = wb.createFont();
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style5.setAlignment(CellStyle.ALIGN_CENTER);
		style5.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style5.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
		style5.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style5.setFont(headerFont);
		styles.put("header", style5);

		CellStyle style6 = wb.createCellStyle();
		Font font = wb.createFont();
		font.setUnderline(font.U_SINGLE);
		font.setColor(IndexedColors.BLUE.getIndex());
		style6.setFont(font);
		styles.put("hlink", style6);
		
		CellStyle style7 = wb.createCellStyle();
		Font titleFont = wb.createFont();
		titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		titleFont.setFontHeightInPoints((short)20);
		style7.setAlignment(CellStyle.ALIGN_CENTER);
		style7.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style7.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style7.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style7.setFont(titleFont);
		styles.put("title", style7);
		
		CellStyle style8 = wb.createCellStyle();
		style8.setAlignment(CellStyle.ALIGN_CENTER);
		style8.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style8.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
		style8.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style8.setFont(headerFont);
		styles.put("titleCondition", style8);
		
		CellStyle style9 = wb.createCellStyle();
		style9.setAlignment(CellStyle.ALIGN_CENTER);
		style9.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style9.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
		style9.setFillPattern(CellStyle.SOLID_FOREGROUND);
		//style9.setFont(headerFont);
		styles.put("contentCondition", style9);

		CellStyle style10 = wb.createCellStyle();
		style10.setAlignment(CellStyle.ALIGN_CENTER);
		style10.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style10.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
		style10.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style10.setFont(headerFont);
		styles.put("titleSerch", style10);
		return styles;
	}

	/**
	 * Desc : Excel 셀스타일 적용(홀수행)
	 * @Method Name : setOddStyle
	 * @param CellStyle
	 * @return
	 */
	public CellStyle setOddStyle(CellStyle style) {

		style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);

		return style;
	}

	/**
	 * Desc : Excel 셀스타일 적용(짝수행)
	 * @Method Name : setEvenStyle
	 * @param CellStyle
	 * @return
	 */
	public CellStyle setEvenStyle(CellStyle style) {

		style.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);

		return style;
	}
	
	/**
	 * Desc : Excel 소계행 스타일
	 * @Method Name : setEvenStyle
	 * @param CellStyle
	 * @return
	 */
	public CellStyle setSubSumStyle(CellStyle style) {
	    
	    style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
	    style.setFillPattern(CellStyle.SOLID_FOREGROUND);
	    
	    return style;
	}
	
	/**
	 * Desc : Excel 총계행 스타일
	 * @Method Name : setEvenStyle
	 * @param CellStyle
	 * @return
	 */
	public CellStyle setTotSumStyle(CellStyle style) {
	    
	    style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
	    style.setFillPattern(CellStyle.SOLID_FOREGROUND);
	    
	    return style;
	}

	/**
	 * Desc : Excel 선스타일 적용
	 * @Method Name : setLineStyle
	 * @param CellStyle
	 * @return
	 */
	public CellStyle setLineStyle(CellStyle style) {

		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.BLUE_GREY.getIndex());
		style.setLeftBorderColor(IndexedColors.BLUE_GREY.getIndex());
		style.setRightBorderColor(IndexedColors.BLUE_GREY.getIndex());
		style.setBottomBorderColor(IndexedColors.BLUE_GREY.getIndex());

		return style;
	}
}
