<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- <%@ taglib prefix="dw" uri="/WEB-INF/tlds/taglib.tld" %> --%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="/WEB-INF/tlds/ppeUtil.tld" prefix="ppe" %>
<jsp:include page="/include/common.jsp"/>
<%@ include file="/include/message.jsp"%>

<%@ page import="egovframework.airyami.cmm.util.UrlUtil"%>

<meta http-equiv="X-UA-Compatible" content="IE=Edge"/>

<link rel="stylesheet" href="/css/admin/admin.css" type="text/css">
<link href="<c:url value='/'/>css/common.css" rel="stylesheet" type="text/css" >
<link href="<c:url value='/'/>css/admin/admin.css" rel="stylesheet" type="text/css" >
<!-- 달력관련 -->
<link rel="stylesheet" href="/js/lib/DatePicker/themes/base/jquery.ui.all.css"  media="screen" />
	
<!-- for 공통함수 -->
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/jquery-1.6.2.min.js" ></script>
<script type="text/javascript" src="/js/jquery.min.js"></script>

<script type="text/javascript" src="/js/jquery.json-2.4.min.js" ></script>
<script type="text/javascript" src="/js/jquery.form.js" ></script>
<script type="text/javascript" src="/js/jquery-migrate-1.2.1.min.js" ></script>

<script type="text/javascript" src="/js/constant.js"></script>
<script type="text/javascript" src="/js/common.js"></script>
<script type="text/javascript" src="<%=UrlUtil.getActionRoot(request)%>/js/airyami_common.js"></script>
<%-- <script type="text/javascript" src="<%=UrlUtil.getActionRoot(request)%>/js/airyami_constant.js"></script> --%>
<script type="text/javascript" src="/js/date.js"></script>
<script type="text/javascript" src="/js/paging.js"></script>
<!-- <script type="text/javascript" src="/js/map.js"></script> -->

<!-- <script type="text/javascript" src="/js/uiScript.js"></script> -->
<script src="http://code.jquery.com/ui/1.8.18/jquery-ui.min.js" ></script>
<!-- <script type="text/javascript" src="/js/lib/modernizr.custom.js"></script> -->

<script type="text/javascript">
SES_MENU_TYPE = "A";
</script>