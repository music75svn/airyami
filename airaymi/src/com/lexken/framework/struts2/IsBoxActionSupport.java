package com.lexken.framework.struts2;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.lexken.framework.common.SearchMap;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.util.CalendarHelper;

import com.opensymphony.xwork2.ActionSupport;

public class IsBoxActionSupport extends ActionSupport 
		implements ServletRequestAware,ServletResponseAware {

	
	protected HttpServletRequest 	req = null;
	protected HttpServletResponse 	res = null;	
	
	public void setServletResponse(HttpServletResponse arg0) {
		// TODO Auto-generated method stub	
		this.res = arg0;
	}

	public void setServletRequest(HttpServletRequest arg0) {
		// TODO Auto-generated method stub
		this.req = arg0;
	}

	public String goLoginPage() {
		// ID저장 쿠키 체크
		String	ID_ADMIN_COOKIE_VALUE	=	"";
		int		h = 0;

		Cookie[] cookies = req.getCookies();
		if ( cookies != null ){
			for ( h = 0 ; h < cookies.length; h++ ){
				Cookie info = cookies[h];
				if ( "ID_ADMIN_COOKIE".equals(info.getName()) )
					ID_ADMIN_COOKIE_VALUE = info.getValue();
				else
					continue;
			}
		}

		req.setAttribute("ID_ADMIN_COOKIE_VALUE", ID_ADMIN_COOKIE_VALUE);	
		if (!ID_ADMIN_COOKIE_VALUE.equals(""))
			req.setAttribute("ID_ADMIN_COOKIE_CHECK", "checked");

		return "REQUEST_LOGIN";
	}

	public String goErrorPage() {
		return goErrorPage(-1, "오류가 발생하였습니다.");
	}
	
	public String goErrorPage(int nErrorNumber, String sErrorMessage) {
		req.setAttribute("ERROR_NUMBER", nErrorNumber);
		req.setAttribute("ERROR_MESSAGE", sErrorMessage);
		return "ERROR_PAGE";
	}
	
	public void setExcel(SearchMap searchMap, LoginManager login){
		searchMap.put("rowPerPage", 65000);
        searchMap.put("beginRow", 1);
        
        List pageNaviList = login.getLoginVO().getPageNaviList();
        
        String fileName = "ExcelDownLoad";
        
        if(pageNaviList != null && pageNaviList.size() > 0){
        	fileName = (String)pageNaviList.get(pageNaviList.size() -1);
        	fileName = fileName.replaceAll("/", "_");
        }
        CalendarHelper cal = new CalendarHelper();
        fileName += "_" + cal.getStrDate();
        
	    res.setContentType("application/vnd.ms-excel; charset=euc-kr");
	    res.setHeader("Content-Disposition", "attachment;  filename="+fileName+".xls");
	}
	
}
