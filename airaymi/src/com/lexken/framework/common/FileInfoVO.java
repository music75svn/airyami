/*************************************************************************
 * CLASS 명  	: FileInfoVO
 * 작 업 자  	: 박재현
 * 작 업 일  	: 2009.07.12
 * 기    능  	: 게시판관리
 * ---------------------------- 변 경 이 력 --------------------------------
 * 번호  작 업 자     작     업     일        변 경 내 용                 비고
 * ----  --------  -----------------  -------------------------    --------
 *   1    박재현		 2009.07.12			  최 초 작 업 
 **************************************************************************/
package com.lexken.framework.common;

public class FileInfoVO {

	private String fileUploadPath;
	private String realFileName;
	private String maskFileName;
	private String fileObjectName;
	private String fileExtension;
	private int width;
	private int height;
	private long size;

	
	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public String getFileUploadPath() {
		return fileUploadPath;
	}

	public void setFileUploadPath(String fileUploadPath) {
		this.fileUploadPath = fileUploadPath;
	}

	public String getRealFileName() {
		return realFileName;
	}

	public void setRealFileName(String realFileName) {
		this.realFileName = realFileName;
	}

	public String getMaskFileName() {
		return maskFileName;
	}

	public void setMaskFileName(String maskFileName) {
		this.maskFileName = maskFileName;
	}

	public String getFileObjectName() {
		return fileObjectName;
	}

	public void setFileObjectName(String fileObjectName) {
		this.fileObjectName = fileObjectName;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

}
