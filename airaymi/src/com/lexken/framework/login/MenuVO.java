/*************************************************************************
* CLASS 명  	: MenuVO
* 작 업 자  	: 하윤식
* 작 업 일  	: 2012.07.21
* 기    능  	: 메뉴
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    하윤식		 2012.07.21			  최 초 작 업 
**************************************************************************/
package com.lexken.framework.login;

import java.io.Serializable;

public class MenuVO implements Serializable {
	
	private String pgmId;         
	private String pgmNm;         
	private String upPgmId;       
	private String levelId;       
	private String url;           
	private String param;         
	private String sortOrder;     
	private String subPgmId;      
	private String subPgmNm;      
	private String subSortOrder;  
	private String highPgmId;     
	private String highPgmNm;     
	private String highSortOrder;
	private String lastPgmId;
	
	public String getPgmId() {
		return pgmId;
	}
	
	public void setPgmId(String pgmId) {
		this.pgmId = pgmId;
	}

	public String getPgmNm() {
		return pgmNm;
	}

	public void setPgmNm(String pgmNm) {
		this.pgmNm = pgmNm;
	}

	public String getUpPgmId() {
		return upPgmId;
	}

	public void setUpPgmId(String upPgmId) {
		this.upPgmId = upPgmId;
	}

	public String getLevelId() {
		return levelId;
	}

	public void setLevelId(String levelId) {
		this.levelId = levelId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getSubPgmId() {
		return subPgmId;
	}

	public void setSubPgmId(String subPgmId) {
		this.subPgmId = subPgmId;
	}

	public String getSubPgmNm() {
		return subPgmNm;
	}

	public void setSubPgmNm(String subPgmNm) {
		this.subPgmNm = subPgmNm;
	}

	public String getSubSortOrder() {
		return subSortOrder;
	}

	public void setSubSortOrder(String subSortOrder) {
		this.subSortOrder = subSortOrder;
	}

	public String getHighPgmId() {
		return highPgmId;
	}

	public void setHighPgmId(String highPgmId) {
		this.highPgmId = highPgmId;
	}

	public String getHighPgmNm() {
		return highPgmNm;
	}

	public void setHighPgmNm(String highPgmNm) {
		this.highPgmNm = highPgmNm;
	}

	public String getHighSortOrder() {
		return highSortOrder;
	}

	public void setHighSortOrder(String highSortOrder) {
		this.highSortOrder = highSortOrder;
	}

	public String getLastPgmId() {
		return lastPgmId;
	}

	public void setLastPgmId(String lastPgmId) {
		this.lastPgmId = lastPgmId;
	}
	
}
