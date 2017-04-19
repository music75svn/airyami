/*************************************************************************
* CLASS 명  	: LoginVO
* 작 업 자  	: 하윤식
* 작 업 일  	: 2012.06.01
* 기    능  	: 인증관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    하윤식		 2012.06.01			  최 초 작 업 
**************************************************************************/
package com.lexken.framework.login;

import java.util.ArrayList;
import java.util.List;

public class LoginVO {
	
	private String sabun;
    private String user_id;
    private String user_reg_id;
    private String name_han;
    private String user_nm;
    private String passwd;
    private String dept_cd;
    private String dept_id;
    private String dept_nm;
    private String jikgub_cd;
    private String jikgub_nm;
    private String pos_cd;
    private String pos_nm;
    private String email;
    private String being_yn;
    private String sort_order;
    private String create_dt;
    private String phone_no;
    private String retire_dt;
    private String profile_file_nm;
    private String profile_file_path;
    private String appointment_gbn_id;
    private String appointment_gbn_nm;
    private String appointment_dt;
    private String join_dt;
    
    private String sc_dept_id;
    private String sc_dept_nm;
    private String sc_dept_grp_id;
    private String up_sc_dept_id;
    private String up_sc_dept_nm;
	
    private String login_ip;        // 로그인 ip
	private boolean	bLogin;			// 로그인 여부(true, false)
	private boolean	bFailure;		// 인증실패 or 메뉴권한 미존재

	private String returnUrl;		// 로그인후 이동페이지
	private String adminType;
	private List pageNaviList;
	private ArrayList authList;		// 권한 정보
	
	private String dashboard_url;   // 사용자권한별 메인대쉬보드(경영진, 처/실장, 2차사업소장, 개인사용자)
	
	
	private String dept_slump_id;   // 부진부서 년도별 관리	

	public LoginVO(){ 
        this.sabun = "";
        this.user_id = "";
        this.user_reg_id = "";
        this.name_han = "";
        this.user_nm = "";
        this.passwd = "";
        this.dept_cd = "";
        this.dept_id = "";
        this.dept_nm = "";
        this.jikgub_cd = "";
        this.jikgub_nm = "";
        this.pos_cd = "";
        this.pos_nm = "";
        this.email = "";
        this.being_yn = "";
        this.sort_order = "";
        this.create_dt = "";
        this.phone_no = "";
        this.retire_dt = "";
        this.profile_file_nm = "";
        this.profile_file_path = "";
        this.appointment_gbn_id = "";
        this.appointment_gbn_nm = "";
        this.appointment_dt = "";
        this.join_dt = "";
        this.login_ip = "";
        this.sc_dept_id = "";
        this.sc_dept_nm = "";
        this.sc_dept_grp_id = "";
        this.up_sc_dept_id = "";
        this.up_sc_dept_nm = "";
        this.dashboard_url = "";
        this.dept_slump_id = "";
    }

	public boolean isLogin() {
		return bLogin;
	}

	public String getSabun() {
		return sabun;
	}


	public void setSabun(String sabun) {
		this.sabun = sabun;
	}


	public String getUser_id() {
		return user_id;
	}


	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}


	public String getUser_reg_id() {
		return user_reg_id;
	}


	public void setUser_reg_id(String user_reg_id) {
		this.user_reg_id = user_reg_id;
	}


	public String getName_han() {
		return name_han;
	}


	public void setName_han(String name_han) {
		this.name_han = name_han;
	}


	public String getUser_nm() {
		return user_nm;
	}


	public void setUser_nm(String user_nm) {
		this.user_nm = user_nm;
	}


	public String getPasswd() {
		return passwd;
	}


	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}


	public String getDept_cd() {
		return dept_cd;
	}


	public void setDept_cd(String dept_cd) {
		this.dept_cd = dept_cd;
	}


	public String getDept_id() {
		return dept_id;
	}


	public void setDept_id(String dept_id) {
		this.dept_id = dept_id;
	}


	public String getDept_nm() {
		return dept_nm;
	}


	public void setDept_nm(String dept_nm) {
		this.dept_nm = dept_nm;
	}


	public String getJikgub_cd() {
		return jikgub_cd;
	}


	public void setJikgub_cd(String jikgub_cd) {
		this.jikgub_cd = jikgub_cd;
	}


	public String getJikgub_nm() {
		return jikgub_nm;
	}


	public void setJikgub_nm(String jikgub_nm) {
		this.jikgub_nm = jikgub_nm;
	}


	public String getPos_cd() {
		return pos_cd;
	}


	public void setPos_cd(String pos_cd) {
		this.pos_cd = pos_cd;
	}


	public String getPos_nm() {
		return pos_nm;
	}


	public void setPos_nm(String pos_nm) {
		this.pos_nm = pos_nm;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getBeing_yn() {
		return being_yn;
	}


	public void setBeing_yn(String being_yn) {
		this.being_yn = being_yn;
	}


	public String getSort_order() {
		return sort_order;
	}


	public void setSort_order(String sort_order) {
		this.sort_order = sort_order;
	}


	public String getCreate_dt() {
		return create_dt;
	}


	public void setCreate_dt(String create_dt) {
		this.create_dt = create_dt;
	}


	public String getPhone_no() {
		return phone_no;
	}


	public void setPhone_no(String phone_no) {
		this.phone_no = phone_no;
	}


	public String getRetire_dt() {
		return retire_dt;
	}


	public void setRetire_dt(String retire_dt) {
		this.retire_dt = retire_dt;
	}


	public String getProfile_file_nm() {
		return profile_file_nm;
	}


	public void setProfile_file_nm(String profile_file_nm) {
		this.profile_file_nm = profile_file_nm;
	}


	public String getProfile_file_path() {
		return profile_file_path;
	}


	public void setProfile_file_path(String profile_file_path) {
		this.profile_file_path = profile_file_path;
	}


	public String getAppointment_gbn_id() {
		return appointment_gbn_id;
	}


	public void setAppointment_gbn_id(String appointment_gbn_id) {
		this.appointment_gbn_id = appointment_gbn_id;
	}


	public String getAppointment_gbn_nm() {
		return appointment_gbn_nm;
	}


	public void setAppointment_gbn_nm(String appointment_gbn_nm) {
		this.appointment_gbn_nm = appointment_gbn_nm;
	}


	public String getAppointment_dt() {
		return appointment_dt;
	}


	public void setAppointment_dt(String appointment_dt) {
		this.appointment_dt = appointment_dt;
	}


	public String getJoin_dt() {
		return join_dt;
	}


	public void setJoin_dt(String join_dt) {
		this.join_dt = join_dt;
	}


	public void setLogin(boolean login) {
		bLogin = login;
	}

	public boolean isFailure() {
		return bFailure;
	}

	public void setFailure(boolean failure) {
		bFailure = failure;
	}

	public String getReturnUrl() {
		return returnUrl;
	}
	
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
	
	public String getAdminType() {
		return adminType;
	}
	
	public void setAdminType(String adminType) {
		this.adminType = adminType;
	}
	
	public List getPageNaviList() {
		return pageNaviList;
	}
	
	public void setPageNaviList(List pageNaviList) {
		this.pageNaviList = pageNaviList;
	}
	
	public void addPageNaviList(String addName){
		this.pageNaviList.add(addName);
	}
	
	public ArrayList getAuthList() {
		return authList;
	}
	
	public void setAuthList(ArrayList authList) {
		this.authList = authList;
	}

	public boolean isBLogin() {
		return bLogin;
	}

	public void setBLogin(boolean login) {
		bLogin = login;
	}

	public boolean isBFailure() {
		return bFailure;
	}

	public void setBFailure(boolean failure) {
		bFailure = failure;
	}

	public String getSc_dept_id() {
		return sc_dept_id;
	}

	public void setSc_dept_id(String sc_dept_id) {
		this.sc_dept_id = sc_dept_id;
	}

	public String getSc_dept_nm() {
		return sc_dept_nm;
	}

	public void setSc_dept_nm(String sc_dept_nm) {
		this.sc_dept_nm = sc_dept_nm;
	}
	
	public String getSc_dept_grp_id() {
		return sc_dept_grp_id;
	}

	public void setSc_dept_grp_id(String sc_dept_grp_id) {
		this.sc_dept_grp_id = sc_dept_grp_id;
	}
	
	public String getUp_sc_dept_id() {
		return up_sc_dept_id;
	}

	public void setUp_sc_dept_id(String up_sc_dept_id) {
		this.up_sc_dept_id = up_sc_dept_id;
	}

	public String getUp_sc_dept_nm() {
		return up_sc_dept_nm;
	}

	public void setUp_sc_dept_nm(String up_sc_dept_nm) {
		this.up_sc_dept_nm = up_sc_dept_nm;
	}
	
	public String getDashboard_url() {
		return dashboard_url;
	}

	public void setDashboard_url(String dashboard_url) {
		this.dashboard_url = dashboard_url;
	}

	public String getLogin_ip() {
		return login_ip;
	}

	public void setLogin_ip(String login_ip) {
		this.login_ip = login_ip;
	}
	
	public boolean chkAuthGrp(String authGrp){
		boolean result = false;
		
		if(authGrp != null){
			if(authList != null && authList.size() > 0){
				for (int i=0; i<authList.size(); i++){
					if(authGrp.equals((String)authList.get(i))){
						result = true;
						break;
					}
				
				}
			}
		}
		return result;
	}
	
	public String getDept_slump_id() {
		return dept_slump_id;
	}

	public void setDept_slump_id(String deptSlumpId) {
		dept_slump_id = deptSlumpId;
	}
}
