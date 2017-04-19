<%@page import="org.apache.commons.httpclient.MultiThreadedHttpConnectionManager"%>
<%@page import="org.apache.commons.httpclient.methods.PostMethod"%>
<%@page import="org.apache.commons.httpclient.HttpClient"%>
<%@page errorPage="error.jsp" %>
<%
	String AUTHORIZATION_URL = session.getAttribute("AUTHORIZATION_URL") == null ? "" : session.getAttribute("AUTHORIZATION_URL").toString();
	String SSID = session.getAttribute("SSID") == null ? "" : session.getAttribute("SSID").toString();
	
	/*
		예외 처리
		-> 인증서버 url에 값이 없다면 index 및 business 페이지로 send
	*/
	String SERVICE_BUSINESS_PAGE = session.getAttribute("SERVICE_BUSINESS_PAGE") == null ? "/index.jsp" : session.getAttribute("SERVICE_BUSINESS_PAGE").toString();
	if(AUTHORIZATION_URL.length() < 5){
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
	
	String sendUrl = AUTHORIZATION_URL + "LoginServlet" + "?method=checkLogin" + "&ssid=" + SSID;
	try{
		/*
			인증서버에 해당 Web-Agent의 인증방식 및 통합로그인 정보를 요청하기 위해 httpclient를 사용하여 전달 
		*/
	    int conTimeOut = 5000;
		int soTimeOut = 5000;
		MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
		HttpClient client = new HttpClient(connectionManager);
		client.setConnectionTimeout(conTimeOut);
		client.setTimeout(soTimeOut);
		connectionManager.setMaxTotalConnections(5000);
		PostMethod post = new PostMethod(sendUrl);
		int httpResult = client.executeMethod(post);
		
		// sResult는 httpclient를 통하여 결과를 받는 변수
		String sResult = post.getResponseBodyAsString().trim();
		post.releaseConnection();
		
		/*
			인증서버에 해당 Web-Agent의 인증방식 및 통합로그인 정보를 요청하기 위해 httpclient를 사용하여 전달 
		*/
		
		if(sResult != null){
			// 인증서버로 부터 받은 결과를 파싱
			String[] data = sResult.split(":");
			session.setAttribute("authMethod", data[0]);
			session.setAttribute("USEISIGNPAGE", data[1]);
			session.setAttribute("checkServer", "Y");
		}else{
			// sResult 값이 없는 경우는 네트워크 통신에 문제가 있거나 서버에서 예외가 발생한 경우이다.
			session.setAttribute("checkServer", "N");
			session.setAttribute("USEISIGNPAGE", "N");
			session.setAttribute("Exception", "Y");
			String SERVICE_LOGIN_PAGE = session.getAttribute("SERVICE_LOGIN_PAGE") == null ? "" : session.getAttribute("SERVICE_LOGIN_PAGE").toString();
			response.sendRedirect(SERVICE_LOGIN_PAGE);
			return;
		}
	}catch (Exception e){
		/*
			예외처리
			-> httpclient 도중 통신에 문제가 발생하였을 때 로그인 페이지로 send
		*/
		session.setAttribute("checkServer", "N");
		session.setAttribute("USEISIGNPAGE", "N");
		session.setAttribute("Exception", "Y");
		String SERVICE_LOGIN_PAGE = session.getAttribute("SERVICE_LOGIN_PAGE") == null ? "" : session.getAttribute("SERVICE_LOGIN_PAGE").toString();
		response.sendRedirect(SERVICE_LOGIN_PAGE);
		return;
		/*
			예외처리
		*/
	}
	
	sendUrl = AUTHORIZATION_URL + "isignplus/index.jsp";
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	</head>
<body>
<form name="sendForm" method="post">
	<input type="hidden" name="ssid" value="<%=SSID%>" />
</form>
<script>
	var sendUrl = "<%=sendUrl%>";
	var sendForm = document.sendForm;
	sendForm.action = sendUrl;
	sendForm.submit();
</script>
</body>
</html>