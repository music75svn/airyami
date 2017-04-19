/*************************************************************************
 * CLASS 명  	: ExcelDownloadVO
 * 작 업 자  	: 하윤식
 * 작 업 일  	: 2012.07.26
 * 기    능  	: 엑셀다운로드 관리
 * ---------------------------- 변 경 이 력 --------------------------------
 * 번호  작 업 자     작     업   일         변 경 내 용             비고
 * ----  --------  -----------------  -------------------------    --------
 *   1    하윤식		2012.07.26   		  최 초 작 업 
 **************************************************************************/
package com.lexken.framework.common;

import java.util.ArrayList;

public class ExcelVO {
	
	private String searchTitle;
	private String searchValue;
	private String header;
	private String column;
	private int width;
	private String align;
	private String rowspanColumn;
	
	public ExcelVO() {
		this.header = "";
		this.column = "";
		this.width = 0;
		this.align = "left";
	}

	public ExcelVO(String searchTitle, String searchValue) {
		this.searchTitle = searchTitle;
		this.searchValue = searchValue;
	}
	
	public ExcelVO(String header, String column, String align) {
		this.header = header;
		this.column = column;
		this.width = 0;
		this.align = align;
	}
	
	public ExcelVO(String header, String column, String align, int width) {
		this.header = header;
		this.column = column;
		this.width = width;
		this.align = align;
	}
	
	public ExcelVO(String header, String column, String align, String rowspanColumn) {
		this.header = header;
		this.column = column;
		this.width = 0;
		this.align = align;
		this.rowspanColumn = rowspanColumn;
	}
	
	public ExcelVO(String header, String column, String align, String rowspanColumn, int width) {
		this.header = header;
		this.column = column;
		this.width = width;
		this.align = align;
		this.rowspanColumn = rowspanColumn;
	}
	
	public String getSearchTitle() {
		return searchTitle;
	}

	public void setSearchTitle(String searchTitle) {
		this.searchTitle = searchTitle;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}

	public String getHeader() {
		return header;
	}
	
	public void setHeader(String header) {
		this.header = header;
	}
	
	public String getColumn() {
		return column;
	}
	
	public void setColumn(String column) {
		this.column = column;
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getAlign() {
		return align;
	}
	
	public void setAlign(String align) {
		this.align = align;
	}

	public String getRowspanColumn() {
		return rowspanColumn;
	}

	public void setRowspanColumn(String rowspanColumn) {
		this.rowspanColumn = rowspanColumn;
	}
	
}
