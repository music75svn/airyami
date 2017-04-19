<%@ page language="java" %>

<html>
<head><title>SSO Test</title></head>
<body>
<% 
	String	KEYSTORE_PATH = "/home/bscadm/keystore/portal.store"; // ex)"C:\\keystore\\portal.store";;
	String	KEYSTORE_PASSWORD = "bscadm";

	try {
		// SSO로 로그인 처리할 사용자의 아이디를 쿠키로부터 가져온다.
		String sapEpUserId = new com.sap.portal.ticket.SAPSingleSignOn(application, KEYSTORE_PATH, KEYSTORE_PASSWORD).getSapEpUserId(request);
		out.println("SAP EP User' ID is ["+sapEpUserId+"]");

	} catch (Exception e) {
		// Exception 발생시 로그인 페이지로 이동한다.
		out.println("Login Fail : "+e.toString());
	}
%>
</body>
</html>