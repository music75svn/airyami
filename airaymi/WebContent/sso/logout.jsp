<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
	String AUTHORIZATION_URL = session.getAttribute("AUTHORIZATION_URL") == null ? "" : session.getAttribute("AUTHORIZATION_URL").toString();
	String SSID = session.getAttribute("SSID") == null ? "" : session.getAttribute("SSID").toString();
	String checkServer = session.getAttribute("checkServer") == null ? "" : session.getAttribute("checkServer").toString();
	String SUCCESS_CODE = session.getAttribute("SUCCESS_CODE") == null ? "" : session.getAttribute("SUCCESS_CODE").toString();
	String errMsg = session.getAttribute("errMsg") == null ? "" : session.getAttribute("errMsg").toString();
	
	/*
		예외 처리
		-> 인증서버의 상태가 N이거나 null or 빈상태일 때 index 및 business 페이지로 send
	*/
	if (checkServer.equals("") || true == checkServer.equals("N")) {
		String SERVICE_BUSINESS_PAGE = session.getAttribute("SERVICE_BUSINESS_PAGE") == null ? "/index.jsp" : session.getAttribute("SERVICE_BUSINESS_PAGE").toString();
		session.invalidate();
		response.sendRedirect(SERVICE_BUSINESS_PAGE);
		return;
	}
	/*
		예외 처리
	*/
	
	//CSRF(Cross-Site Request Forgery) 방어코드	
	//head 의 referer 값이 http://domain:port/sso/business.jsp 인지 비교.
	//설치하는 서버에 맞게 수정.
	//등록되어 있는 서버 목록을 통해서 접속하는 것만을 허용함.
	//business.jsp 에서 접근하는 것을 생각해 볼수 있으나, logout.jsp에서도 동일한 코드로 방어를 하기 위해서 
	//whitelist 방식으로 작성함.
	//logout.jsp 일 경우 통합로그아웃으로 통해서 인증서버에서 logout 요청이 들어올수도 있기 때문에, 접근 페이지 비교를 하지 않음.
	String serverWhiteList[] = {"업무서버", "인증서버"};	
	
	String referer = request.getHeader("referer");
	
	if(referer != null){
		// referer 에서 http:// 또는 https:// 제거 
		int index = referer.indexOf("://") + 3;
		referer = referer.substring(index, referer.length());		
		
		// 도메인을 제외한 포트 및 서브 주소 제거.
		index = referer.indexOf(":");		
		if(index > 0)
			referer = referer.substring(0, index);	
		
		index = referer.indexOf("/");		
		if(index > 0)
			referer = referer.substring(0, index);
		
		boolean checkReferer = false;
		for(String serverName : serverWhiteList){
			if(referer.equals(serverName)){			
				checkReferer = true;
			}
		}
		
		if(checkReferer == false){
			out.println("<h1> referer 가 잘못되었습니다. 확인해주세요. </h1>");
			session.invalidate();
			return;
		}
	}
	
	
	String domain = request.getServerName();
	domain = domain.substring(domain.indexOf("."));
	
	session.invalidate();
	String sendUrl = AUTHORIZATION_URL + "isignplus/logout.jsp";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<META HTTP-EQUIV="Content-Type"  CONTENT="text/html; charset=utf-8">
		<META HTTP-EQUIV="Pragma" CONTENT ="no-cache">
		<META HTTP-EQUIV="Cache-control" CONTENT="no-cache">
	</head>
<body>
<form name="sendForm" method="post">
	<input type="hidden" name="ssid" value="<%=SSID%>" />
	<input type="hidden" name="domain" value="<%=domain%>" />
</form>
<script>
	var SUCCESS_CODE = "<%=SUCCESS_CODE%>";
	if(SUCCESS_CODE == "2"){
		alert('errMsg : <%=errMsg%>');
	}
	
	var sendUrl = "<%=sendUrl%>";
	var sendForm = document.sendForm;
	sendForm.action = sendUrl;
	sendForm.submit();
</script>

</body>
</html>