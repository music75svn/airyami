/*************************************************************************
 * CLASS 명  	: ExcelUtil
 * 작 업 자  	: 하윤식
 * 작 업 일  	: 2012.07.26
 * 기    능  	: 엑셀다운로드 관리
 * ---------------------------- 변 경 이 력 --------------------------------
 * 번호  작 업 자     작    업   일         변 경 내 용             비고
 * ----  --------  -----------------  -------------------------    --------
 *   1    하윤식		2012.07.26          최 초 작 업 
 **************************************************************************/
package com.lexken.framework.common;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;

import com.lexken.framework.util.CalendarHelper;
import com.lexken.framework.util.StaticUtil;

public class ExcelUtil {
	
	private static ExcelUtil instance = null;
	private final Log logger = LogFactory.getLog(getClass());
	
	
	public static ExcelUtil getInstance() {
		if (instance == null) {
			instance = new ExcelUtil();
		}
		return instance;
	}
	
	
   /*************************************************
	* excel 다운로드 
	**************************************************/
	public void execlDown(HttpServletResponse res, SearchMap searchMap) throws Exception {
		if(searchMap.get("excelList") != null){
			execlMulitDown(res,searchMap);
			return ;
		}
		int index = 0;	//row count
		OutputStream out = null;
		
		CalendarHelper cal = new CalendarHelper();
		
		String excelFileName = (String)searchMap.get("excelFileName");  //파일명 설정
		String excelTitle = (String)searchMap.get("excelTitle");  //타이틀 설정
		ArrayList excelInfoList = (ArrayList)searchMap.get("excelInfoList"); //테이블 속성정보
		ArrayList excelSearchInfoList = (ArrayList)searchMap.get("excelSearchInfoList"); //테이블 조회조건 리스트
		ArrayList excelDataList = (ArrayList)searchMap.get("excelDataList"); //테이블 데이터 리스트
		
		excelFileName = excelFileName.replaceAll("\\+","%20"); //여백 설정
		
		int excelInfoSize = excelInfoList.size();
		
		res.reset(); 
		res.setHeader("Content-type", "application/xls");
		res.setHeader("Content-Transfer-Encoding", "binary");
		res.setHeader("Content-Disposition", "attachment;filename=\"" + URLEncoder.encode(excelFileName, "UTF-8").replaceAll("\\+","%20") + "_" + cal.getStrDate() + ".xls\"");
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(excelFileName);
		
		
		/**********************************
         * Cell 스타일 지정
         **********************************/
		HSSFCellStyle titleStyle = workbook.createCellStyle();      //타이틀 스타일
		HSSFCellStyle titleStyle2 = workbook.createCellStyle();     //타이틀2 스타일
		HSSFCellStyle headerStyle = workbook.createCellStyle();     //헤더 스타일	  
		HSSFCellStyle data = workbook.createCellStyle();       //데이터 스타일(left)
		HSSFCellStyle dataRight = workbook.createCellStyle();  //데이터 스타일(right)
		HSSFCellStyle dataCenter = workbook.createCellStyle(); //테이터 스타일(center)
		
		/**********************************
         * Cell 정렬기준 설정
         **********************************/
		titleStyle.setAlignment (titleStyle.ALIGN_CENTER);
		headerStyle.setAlignment (headerStyle.ALIGN_CENTER);
		data.setAlignment (data.ALIGN_LEFT);
		dataRight.setAlignment (dataRight.ALIGN_RIGHT);
		dataCenter.setAlignment (dataCenter.ALIGN_CENTER);
		
		titleStyle.setVerticalAlignment(titleStyle.VERTICAL_CENTER);
		titleStyle2.setVerticalAlignment(titleStyle.VERTICAL_CENTER);
		headerStyle.setVerticalAlignment(headerStyle.VERTICAL_CENTER);
		data.setVerticalAlignment(data.VERTICAL_CENTER);
		dataRight.setVerticalAlignment(dataRight.VERTICAL_CENTER);
		dataCenter.setVerticalAlignment(dataCenter.VERTICAL_CENTER);
		
        /**********************************
         * Cell 테두리 선긋기
         **********************************/
		headerStyle.setBorderTop(headerStyle.BORDER_THIN);
		headerStyle.setBorderBottom(headerStyle.BORDER_THIN);
		headerStyle.setBorderLeft(headerStyle.BORDER_THIN);
		headerStyle.setBorderRight(headerStyle.BORDER_THIN);
		
		data.setBorderBottom(data.BORDER_THIN);
		data.setBorderLeft(data.BORDER_THIN);
		data.setBorderRight(data.BORDER_THIN);
		data.setBorderTop(data.BORDER_THIN);
		
		dataRight.setBorderBottom(data.BORDER_THIN);
		dataRight.setBorderLeft(data.BORDER_THIN);
		dataRight.setBorderRight(data.BORDER_THIN);
		dataRight.setBorderTop(data.BORDER_THIN);
		
		dataCenter.setBorderBottom(data.BORDER_THIN);
		dataCenter.setBorderLeft(data.BORDER_THIN);
		dataCenter.setBorderRight(data.BORDER_THIN);
		dataCenter.setBorderTop(data.BORDER_THIN);
		
		/**********************************
         * Cell 색상설정
         **********************************/
		headerStyle.setFillBackgroundColor(HSSFColor.WHITE.index);
		headerStyle.setFillForegroundColor(HSSFColor.LEMON_CHIFFON.index);
		headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		
		/**********************************
         * font 설정
         **********************************/
		HSSFFont font1 = workbook.createFont(); 
		font1.setFontHeightInPoints((short)12);  
		font1.setFontName("돋음");
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
		titleStyle.setFont(font1);
		
		HSSFFont font2 = workbook.createFont(); 
		font2.setFontHeightInPoints((short)9);  
		font2.setFontName("돋음");   
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
		headerStyle.setFont(font2);
		
		HSSFFont font3 = workbook.createFont(); 
		font3.setFontHeightInPoints((short)9);  
		font3.setFontName("돋음"); 
		data.setFont(font3); 
		dataRight.setFont(font3);
		dataCenter.setFont(font3);
		titleStyle2.setFont(font3);
		
		
		/**********************************
         * 개행문자 설정
         **********************************/
		data.setWrapText(true);
		
		
		HSSFRow row = null;
		HSSFCell cell = null;
		/**********************************
         * 타이틀 설정
         **********************************/
		if(excelTitle != null && !"".equals(excelTitle)) { 
			row = sheet.createRow((short)index);
			row.setHeight((short)430);
			
			//row를 미리 생성하여 테두리 선 스타일 시트 처리
			for(int i = 0; i < excelInfoList.size(); i++){
				cell = row.createCell((short)i);	
				cell.setCellStyle (titleStyle);	  
			}
			
			cell = row.getCell((short)0);   //0셀 불러오기
			cell.setCellValue(excelTitle);
			cell.setCellStyle(titleStyle);
			sheet.addMergedRegion(new Region(0,(short)0,0,(short)(excelInfoList.size()-1))); //셀병합
		}
		
		/**********************************
         * 조회 조건 그리기
         **********************************/	
		if(null != excelSearchInfoList && 0 < excelSearchInfoList.size()) {
			for(int i = 0; i < excelSearchInfoList.size(); i++) {
				ExcelVO excelVO = (ExcelVO)excelSearchInfoList.get(i);
				index++;
				row = sheet.createRow((short)index);	
				row.setHeight((short)360);
				
				//row를 미리 생성하여 테두리 선 스타일 시트 처리
				for(int j = 0; j < excelInfoList.size(); j++){
					cell = row.createCell((short)j);	
					cell.setCellStyle (titleStyle2);	  
				}
				
				cell = row.getCell((short)0);   //0셀 불러오기
				cell.setCellValue("▣" + excelVO.getSearchTitle() + " : " + (String)StaticUtil.nullToDefault(excelVO.getSearchValue(),""));
				cell.setCellStyle(titleStyle2);
				sheet.addMergedRegion(new Region(0,(short)0,0,(short)(excelInfoList.size()-1))); //셀병합
			}
			index++;
		}
		
		/**********************************
         * 테이블 헤더 그리기
         **********************************/
		row = sheet.createRow((short)index);	
		row.setHeight((short)512);

		//셀에 데이터 추가
		for(int i = 0; i < excelInfoList.size(); i++) {
			ExcelVO excelVO = (ExcelVO)excelInfoList.get(i);
			cell = row.createCell((short)i);	 
			cell.setCellValue(excelVO.getHeader());
			cell.setCellStyle(headerStyle);
			
			//sheet.addMergedRegion(new Region(index,(short)i, index+2-1, (short)i)); //셀병합
			//sheet.addMergedRegion(new Region(index,(short)i, index+2-1, (short)i)); //셀병합
		}
		
		//index = index+1;
		
		String rowspan = "";
		int rowspanCount = 0;
		HashMap<String, Integer> countMap = new HashMap<String, Integer>();
		
		int[] colRowCnt = new int[excelInfoList.size()]; 
		for(int j = 0; j < excelInfoList.size(); j++) {
	    	colRowCnt[j] = 0;
		}
		
		/**********************************
         * 테이블 데이터 그리기
         **********************************/
		int dataStartIndex = 0 ;
		if(null != excelDataList && 0 < excelDataList.size()) {
			int maxRowNewlineCnt = 0;
			String tmpDataArray[] = new String[1];
			for(int i = 0; i < excelDataList.size(); i++) {
				HashMap map = (HashMap)excelDataList.get(i);
				
				index++;
				dataStartIndex = index;
			    row = sheet.createRow((short)index); //row 추가
			    row.setHeight((short)300);
			    
			    maxRowNewlineCnt = 1;
			    tmpDataArray = new String[1];
			    for(int j = 0; j < excelInfoList.size(); j++) {
			    	ExcelVO excelVO = (ExcelVO)excelInfoList.get(j);
			    	Object obj = (Object)map.get(excelVO.getColumn());
			    	if(obj == null) obj = "";
			    	String value = String.valueOf(obj);
			    	//value = value.replaceAll("\r\n", " ");
			    	value = value.replaceAll("\r", "");
			    	
			    	tmpDataArray = value.split("\n");
			    	if( maxRowNewlineCnt < tmpDataArray.length ){
			    		maxRowNewlineCnt = tmpDataArray.length;
			    	}
			    	/**********************************
			         * Rowspan 설정시작
			         **********************************/
			    	Object rowspanObj = (Object)map.get(excelVO.getRowspanColumn());
			    	if(rowspanObj != null) {
			    		rowspan = String.valueOf(rowspanObj);
				    	if(!"".equals(rowspan)) {
				    		rowspanCount = Integer.parseInt(rowspan);
				    	}
				    	
				    	if(colRowCnt[j] == 0) {
				    		sheet.addMergedRegion(new Region(index,(short)j, index+rowspanCount-1, (short)j)); //셀병합
				    	}
				    	
				    	colRowCnt[j]++;
				    	
				    	if(colRowCnt[j] == rowspanCount) {
				    		colRowCnt[j] = 0;
				    	}
			    	}
			    	/**********************************
			         * Rowspan 설정 끝
			         **********************************/
			    	
			    	cell = row.createCell((short)j); 
			    	cell.setCellValue(value);
			    	cell.setCellType( HSSFCell.CELL_TYPE_STRING ); 
				    
			    	if("right".equalsIgnoreCase(excelVO.getAlign())) {
			    		cell.setCellStyle(dataRight);
			    	} else if("center".equalsIgnoreCase(excelVO.getAlign())) {
			    		cell.setCellStyle(dataCenter);
			    	} else {
			    		cell.setCellStyle(data);
			    	}
			    }
			    
			    row.setHeight((short)(300 * maxRowNewlineCnt) );
			    
			}
		}
		

		/**********************************
         * Cell 너비설정
         **********************************/
		for(int i = 0; i < excelInfoList.size(); i++) {
			ExcelVO excelVO = (ExcelVO)excelInfoList.get(i);
			
			if(0 != excelVO.getWidth()) {
				sheet.setColumnWidth((short) i, (short) excelVO.getWidth());
			} else {
				sheet.autoSizeColumn(i);
				sheet.setColumnWidth((short)i, (short)((sheet.getColumnWidth(i)) + 512) );
				if(sheet.getColumnWidth(i) > 10000) sheet.setColumnWidth(i, 10000);
			}
		}
		
		
		try {
			out = res.getOutputStream();
			workbook.write(out);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if(out != null) try {out.close();} catch(Exception e){}
		}
	}
	
	public void execlMulitDown(HttpServletResponse res, SearchMap searchMap) throws Exception {

		
		OutputStream out = null;
		
		String excelFileName = (String)searchMap.get("excelFileName");  //파일명 설정
		
		CalendarHelper cal = new CalendarHelper();
		
		res.reset(); 
		res.setHeader("Content-type", "application/xls");
		res.setHeader("Content-Transfer-Encoding", "binary");
		res.setHeader("Content-Disposition", "attachment;filename=\"" + URLEncoder.encode(excelFileName, "UTF-8").replaceAll("\\+","%20") + "_" + cal.getStrDate() + ".xls\"");
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		
		ArrayList<HashMap> excelList = (ArrayList)searchMap.get("excelList");
		for(HashMap excelMap : excelList){
			int index = 0;	//row count
			
			String excelTitle = (String)excelMap.get("excelTitle");  //타이틀 설정
			ArrayList excelInfoList = (ArrayList)excelMap.get("excelInfoList"); //테이블 속성정보
			ArrayList excelSearchInfoList = (ArrayList)excelMap.get("excelSearchInfoList"); //테이블 조회조건 리스트
			ArrayList excelDataList = (ArrayList)excelMap.get("excelDataList"); //테이블 데이터 리스트

			excelFileName = excelFileName.replaceAll("\\+","%20"); //여백 설정

			int excelInfoSize = excelInfoList.size();
			
			HSSFSheet sheet = workbook.createSheet(excelTitle);


			/**********************************
			 * Cell 스타일 지정
			 **********************************/
			HSSFCellStyle titleStyle = workbook.createCellStyle();      //타이틀 스타일
			HSSFCellStyle titleStyle2 = workbook.createCellStyle();     //타이틀2 스타일
			HSSFCellStyle headerStyle = workbook.createCellStyle();     //헤더 스타일	  
			HSSFCellStyle data = workbook.createCellStyle();       //데이터 스타일(left)
			HSSFCellStyle dataRight = workbook.createCellStyle();  //데이터 스타일(right)
			HSSFCellStyle dataCenter = workbook.createCellStyle(); //테이터 스타일(center)

			/**********************************
			 * Cell 정렬기준 설정
			 **********************************/
			titleStyle.setAlignment (titleStyle.ALIGN_CENTER);
			headerStyle.setAlignment (headerStyle.ALIGN_CENTER);
			data.setAlignment (data.ALIGN_LEFT);
			dataRight.setAlignment (dataRight.ALIGN_RIGHT);
			dataCenter.setAlignment (dataCenter.ALIGN_CENTER);

			titleStyle.setVerticalAlignment(titleStyle.VERTICAL_CENTER);
			titleStyle2.setVerticalAlignment(titleStyle.VERTICAL_CENTER);
			headerStyle.setVerticalAlignment(headerStyle.VERTICAL_CENTER);
			data.setVerticalAlignment(data.VERTICAL_CENTER);
			dataRight.setVerticalAlignment(dataRight.VERTICAL_CENTER);
			dataCenter.setVerticalAlignment(dataCenter.VERTICAL_CENTER);

			/**********************************
			 * Cell 테두리 선긋기
			 **********************************/
			headerStyle.setBorderTop(headerStyle.BORDER_THIN);
			headerStyle.setBorderBottom(headerStyle.BORDER_THIN);
			headerStyle.setBorderLeft(headerStyle.BORDER_THIN);
			headerStyle.setBorderRight(headerStyle.BORDER_THIN);

			data.setBorderBottom(data.BORDER_THIN);
			data.setBorderLeft(data.BORDER_THIN);
			data.setBorderRight(data.BORDER_THIN);
			data.setBorderTop(data.BORDER_THIN);

			dataRight.setBorderBottom(data.BORDER_THIN);
			dataRight.setBorderLeft(data.BORDER_THIN);
			dataRight.setBorderRight(data.BORDER_THIN);
			dataRight.setBorderTop(data.BORDER_THIN);

			dataCenter.setBorderBottom(data.BORDER_THIN);
			dataCenter.setBorderLeft(data.BORDER_THIN);
			dataCenter.setBorderRight(data.BORDER_THIN);
			dataCenter.setBorderTop(data.BORDER_THIN);

			/**********************************
			 * Cell 색상설정
			 **********************************/
			headerStyle.setFillBackgroundColor(HSSFColor.WHITE.index);
			headerStyle.setFillForegroundColor(HSSFColor.LEMON_CHIFFON.index);
			headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			/**********************************
			 * font 설정
			 **********************************/
			HSSFFont font1 = workbook.createFont(); 
			font1.setFontHeightInPoints((short)12);  
			font1.setFontName("돋음");
			font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
			titleStyle.setFont(font1);

			HSSFFont font2 = workbook.createFont(); 
			font2.setFontHeightInPoints((short)9);  
			font2.setFontName("돋음");   
			font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
			headerStyle.setFont(font2);

			HSSFFont font3 = workbook.createFont(); 
			font3.setFontHeightInPoints((short)9);  
			font3.setFontName("돋음"); 
			data.setFont(font3); 
			dataRight.setFont(font3);
			dataCenter.setFont(font3);
			titleStyle2.setFont(font3);


			/**********************************
			 * 개행문자 설정
			 **********************************/
			data.setWrapText(true);


			HSSFRow row = null;
			HSSFCell cell = null;
			/**********************************
			 * 타이틀 설정
			 **********************************/
			if(excelTitle != null && !"".equals(excelTitle)) { 
				row = sheet.createRow((short)index);
				row.setHeight((short)430);

				//row를 미리 생성하여 테두리 선 스타일 시트 처리
				for(int i = 0; i < excelInfoList.size(); i++){
					cell = row.createCell((short)i);	
					cell.setCellStyle (titleStyle);	  
				}

				cell = row.getCell((short)0);   //0셀 불러오기
				cell.setCellValue(excelTitle);
				cell.setCellStyle(titleStyle);
				//sheet.addMergedRegion(new Region(0,(short)0,0,(short)(excelInfoList.size()-1))); //셀병합
			}

			/**********************************
			 * 조회 조건 그리기
			 **********************************/	
			if(null != excelSearchInfoList && 0 < excelSearchInfoList.size()) {
				for(int i = 0; i < excelSearchInfoList.size(); i++) {
					ExcelVO excelVO = (ExcelVO)excelSearchInfoList.get(i);
					index++;
					row = sheet.createRow((short)index);	
					row.setHeight((short)360);

					//row를 미리 생성하여 테두리 선 스타일 시트 처리
					for(int j = 0; j < excelInfoList.size(); j++){
						cell = row.createCell((short)j);	
						cell.setCellStyle (titleStyle2);	  
					}

					cell = row.getCell((short)0);   //0셀 불러오기
					cell.setCellValue("▣" + excelVO.getSearchTitle() + " : " + (String)StaticUtil.nullToDefault(excelVO.getSearchValue(),""));
					cell.setCellStyle(titleStyle2);
					//sheet.addMergedRegion(new Region(0,(short)0,0,(short)(excelInfoList.size()-1))); //셀병합
				}
				index++;
			}

			/**********************************
			 * 테이블 헤더 그리기
			 **********************************/
			row = sheet.createRow((short)index);	
			row.setHeight((short)512);
			
			//셀에 데이터 추가
			for(int i = 0; i < excelInfoList.size(); i++) {
				
				ExcelVO excelVO = (ExcelVO)excelInfoList.get(i);
				cell = row.createCell((short)i);	 
				cell.setCellValue(excelVO.getHeader());
				cell.setCellStyle(headerStyle);
				
				//sheet.addMergedRegion(new Region(index,(short)i, index+2-1, (short)i)); //셀병합
				//sheet.addMergedRegion(new Region(index,(short)i, index+2-1, (short)i)); //셀병합
			}

			//index = index+1;

			String rowspan = "";
			int rowspanCount = 0;
			HashMap<String, Integer> countMap = new HashMap<String, Integer>();

			int[] colRowCnt = new int[excelInfoList.size()]; 
			for(int j = 0; j < excelInfoList.size(); j++) {
				colRowCnt[j] = 0;
			}

			/**********************************
			 * 테이블 데이터 그리기
			 **********************************/
			int dataStartIndex = 0 ;
			if(null != excelDataList && 0 < excelDataList.size()) {
				int maxRowNewlineCnt = 0;
				String tmpDataArray[] = new String[1];
				for(int i = 0; i < excelDataList.size(); i++) {
					HashMap map = (HashMap)excelDataList.get(i);

					index++;
					dataStartIndex = index;
					row = sheet.createRow((short)index); //row 추가
					row.setHeight((short)300);

					maxRowNewlineCnt = 1;
					tmpDataArray = new String[1];
					for(int j = 0; j < excelInfoList.size(); j++) {
						ExcelVO excelVO = (ExcelVO)excelInfoList.get(j);
						Object obj = (Object)map.get(excelVO.getColumn());
						if(obj == null) obj = "";
						String value = String.valueOf(obj);
						//value = value.replaceAll("\r\n", " ");
						value = value.replaceAll("\r", "");

						tmpDataArray = value.split("\n");
						if( maxRowNewlineCnt < tmpDataArray.length ){
							maxRowNewlineCnt = tmpDataArray.length;
						}
						/**********************************
						 * Rowspan 설정시작
						 **********************************/
						Object rowspanObj = (Object)map.get(excelVO.getRowspanColumn());
						if(rowspanObj != null) {
							rowspan = String.valueOf(rowspanObj);
							if(!"".equals(rowspan)) {
								rowspanCount = Integer.parseInt(rowspan);
							}

							if(colRowCnt[j] == 0) {
								sheet.addMergedRegion(new Region(index,(short)j, index+rowspanCount-1, (short)j)); //셀병합
							}

							colRowCnt[j]++;

							if(colRowCnt[j] == rowspanCount) {
								colRowCnt[j] = 0;
							}
						}
						/**********************************
						 * Rowspan 설정 끝
						 **********************************/

						cell = row.createCell((short)j); 
						cell.setCellValue(value);
						cell.setCellType( HSSFCell.CELL_TYPE_STRING ); 

						if("right".equalsIgnoreCase(excelVO.getAlign())) {
							cell.setCellStyle(dataRight);
						} else if("center".equalsIgnoreCase(excelVO.getAlign())) {
							cell.setCellStyle(dataCenter);
						} else {
							cell.setCellStyle(data);
						}
					}

					row.setHeight((short)(300 * maxRowNewlineCnt) );

				}
			}


			/**********************************
			 * Cell 너비설정
			 **********************************/
			for(int i = 0; i < excelInfoList.size(); i++) {
				ExcelVO excelVO = (ExcelVO)excelInfoList.get(i);

				if(0 != excelVO.getWidth()) {
					sheet.setColumnWidth((short) i, (short) excelVO.getWidth());
				} else {
					sheet.autoSizeColumn(i);
					sheet.setColumnWidth((short)i, (short)((sheet.getColumnWidth(i)) + 512) );
					if(sheet.getColumnWidth(i) > 10000) sheet.setColumnWidth(i, 10000);
				}
			}

		}
		try {
			out = res.getOutputStream();
			workbook.write(out);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if(out != null) try {out.close();} catch(Exception e){}
		}
	}

}
