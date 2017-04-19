/*************************************************************************
* CLASS 명  	: CodeUtilVO
* 작 업 자  	: 박재현
* 작 업 일  	: 2009.07.15
* 기    능  	: 코드유틸
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    박재현		 2009.07.15			  최 초 작 업 
**************************************************************************/
package com.lexken.framework.codeUtil;
 
public class CodeUtilVO {
		
	private String code_grp_id;
    private String code_id;
    private String year;
    private String code_nm;
    private String sort_order;
    private String content;
    private String etc1;
    private String etc2;
    private String create_dt;
    private String delete_dt;
    private String code_grp_nm;
    private String code_def_id;
    private String year_yn; 
    
    public CodeUtilVO(){ 
        this.code_grp_id = "";
        this.code_id = "";
        this.year = "";
        this.code_nm = "";
        this.sort_order = "";
        this.content = "";
        this.etc1 = "";
        this.etc2 = "";
        this.create_dt = "";
        this.delete_dt = "";
        this.content = "";
        this.create_dt = "";
        this.delete_dt = "";
    }

	public String getCode_grp_id() {
		return code_grp_id;
	}

	public void setCode_grp_id(String code_grp_id) {
		this.code_grp_id = code_grp_id;
	}

	public String getCode_id() {
		return code_id;
	}

	public void setCode_id(String code_id) {
		this.code_id = code_id;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getCode_nm() {
		return code_nm;
	}

	public void setCode_nm(String code_nm) {
		this.code_nm = code_nm;
	}

	public String getSort_order() {
		return sort_order;
	}

	public void setSort_order(String sort_order) {
		this.sort_order = sort_order;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getEtc1() {
		return etc1;
	}

	public void setEtc1(String etc1) {
		this.etc1 = etc1;
	}

	public String getEtc2() {
		return etc2;
	}

	public void setEtc2(String etc2) {
		this.etc2 = etc2;
	}

	public String getCreate_dt() {
		return create_dt;
	}

	public void setCreate_dt(String create_dt) {
		this.create_dt = create_dt;
	}

	public String getDelete_dt() {
		return delete_dt;
	}

	public void setDelete_dt(String delete_dt) {
		this.delete_dt = delete_dt;
	}

	public String getCode_grp_nm() {
		return code_grp_nm;
	}

	public void setCode_grp_nm(String code_grp_nm) {
		this.code_grp_nm = code_grp_nm;
	}

	public String getCode_def_id() {
		return code_def_id;
	}

	public void setCode_def_id(String code_def_id) {
		this.code_def_id = code_def_id;
	}

	public String getYear_yn() {
		return year_yn;
	}

	public void setYear_yn(String year_yn) {
		this.year_yn = year_yn;
	}
    
}
