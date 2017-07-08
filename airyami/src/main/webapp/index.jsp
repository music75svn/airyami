<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
//window.location = "/login/Login.do?req_site_id=HOMEPAGE&req_menu_code=02010100";

//window.location = "/kef/login/template.do";
</script>
</head>
<body>
index.jsp 페이지

<form id='login' action="/login/login.do" method="post">
<input type="submit" value="user login"/>
</form>

<form id='login' action="/login/adminLogin.do" method="post">
<input type="submit" value="admin login"/>
</form>

<form id='login' action="/template/template.do" method="post">
<input type="submit" value="template"/>
</form>

<form id='login' action="/template/template_list.do?ppe=aa" method="post">
<input type="submit" value="template_list"/>
</form>

<form id='login' action="/cop/bbs/addBBSMaster.do" method="post">
<input type="submit" value="join"/>
</form>

디자인<br>
<a href="/html/basic.html">basic.html</a><br>  
<a href="/html/detail.html">detail.html</a><br>
<a href="/html/list01.html">list01.html</a><br>  
<a href="/html/list02.html">list02.html</a><br>  
<a href="/html/main.html">main.html</a><br>  

</body>
</html>