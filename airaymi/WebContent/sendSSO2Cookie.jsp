<%
	String cookieval = request.getParameter("MYSAPSSO2");
	String server = request.getServerName();
	String domain = null;

	int dotPos = server.indexOf(".");
	int lastDotPos = server.lastIndexOf(".");
	String number = "\\d{1,3}";
	if (dotPos != -1) {
		if (server.substring(lastDotPos+1).matches(number))
			domain = server;
		else
			domain = server.substring(dotPos+1);
	}

	if (cookieval != null && !cookieval.equals("")) {
		Cookie cookie = new Cookie ("MYSAPSSO2", java.net.URLEncoder.encode(cookieval));
		
		if (domain != null) 
			cookie.setDomain(domain);

		cookie.setPath("/");
		response.addCookie(cookie);
%>
<html>
<head></head>
<body>
<h1>COOKIE에 SSO TICKET 셋팅 성공</h1>
</body>
</html>
<%
	}
	else {
		response.setStatus(403);
	}
%>
