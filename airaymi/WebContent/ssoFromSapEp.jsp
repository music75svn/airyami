<%@ page language="java" %>

<html>
<head><title>Login check</title></head>
<body>
<% 
	String	KEYSTORE_PATH = "/home/bscadm/keystore/portal.store"; // ex)"C:\\keystore\\portal.store";;
	String	KEYSTORE_PASSWORD = "bscadm";

	try {
		// SSO로 로그인 처리할 사용자의 아이디를 쿠키로부터 가져온다.
		String sapEpUserId = new com.sap.portal.ticket.SAPSingleSignOn(application, KEYSTORE_PATH, KEYSTORE_PASSWORD).getSapEpUserId(request);
		
		// 해당 ID를 로그인 처리한다.
		// out.println("SAP EP User' ID is ["+sapEpUserId+"]");
		
		/* 개발이 필요한 부분 */
		response.sendRedirect("http://n-bsc.westernpower.co.kr/BSC_PROJECT/secure/loginDo.vw?userId="+sapEpUserId);
		
	} catch (Exception e) {
		out.println("Login Fail : "+e.toString());
	}
%>
</body>
</html>