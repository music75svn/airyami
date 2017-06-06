<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="egovframework.airyami.cmm.util.*"%>

<jsp:useBean id="idCk" class="egovframework.airyami.cmm.util.AuthCheck" scope="page" />
<jsp:useBean id="commonUtils" class="egovframework.airyami.cmm.util.CommonUtils" scope="page" />

<%
	
	System.out.println("getRequestURI :: " + request.getRequestURI());
	System.out.println("getContextPath :: " + request.getContextPath());
	
	idCk.init( request, response );	

	
	System.out.println("idCk.isLogin() :: " + idCk.isLogin());
	
	//로그인 체크
	if (!idCk.isLogin() ){
		out.println("<script>");
		out.println("if(document.location.pathname != (gfn_getApplication() + '/shop/main.do')){");
		//out.println("debugger;");
		out.println("document.location.href= gfn_getApplication()+\"/login/login.do?SITE_ID=\" + gfn_getSiteID();");
		out.println("}");
		out.println("</script>");
		return;
	}
	
	String loginId = commonUtils.NVL(idCk.getUser_id());
	//System.out.println("한글 : common : user_id :: " + user_id);
	
		
	String realYn = "N";
	// 운영이면
	if("http://localhost:8080".equals( UrlUtil.getUrlPath(request) )){
	    realYn = "Y";
	}
	else if("http://localhost:8088".equals( UrlUtil.getUrlPath(request) )){
	    realYn = "N";
	}
	
%>
<script type="text/javascript">
SES_LOGIN = '<%=idCk.isLogin()%>';
SES_USER_TYPE = '<%=idCk.getUser_type()%>';
SES_USER_NAME = '<%=idCk.getUser_nm()%>';
SES_USER_ID = '<%=idCk.getUser_id()%>';
</script>