<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.sql.*,java.util.*,javax.servlet.http.*"%>
<%
/******************************************************************************************
* CLASS 명  	: sourceBuilder.jsp
* 작 업 자  	: 하윤식
* 작 업 일  	: 2012년 5월 26일 
* 기    능  	: BSC 소스빌더
* ---------------------------- 변 경 이 력 -----------------------------------------------
* 번호  작 업 자    작    업    일                 변 경 내 용                  비   고
* ----  --------  --------------------    ---------------------------------  -------------
* 1.0    하윤식     2012년 05월 26일       최 초 작 업
* 1.1    하윤식     2012년 06월 21일       소스생성위치, 개발자명 쿠키처리
* 2.0    하윤식     2012년 10월 19일       dml wizard 추가
* 2.1    하윤식     2012년 10월 30일       필수입력 추가
* 2.2    하윤식     2012년 11월 02일       dml wizard 테이블명 TRIM처리 추가
******************************************************************************************/
%>

<!-- 환경설정 파일 분리 -->
<%@ include file="./sourceBuilderSet.jsp"%>

<%@ include file="./include/core/db_connect.jsp"%>

<%
	request.setCharacterEncoding("UTF-8");

	//소스 해당폴더 자동copy 여부(N으로 설정시 makecode/output 폴더 하위에 소스생성)
	String autoDirectoryCopy = "Y"; 

	String serverName = request.getServerName();
	
	boolean access = false;
	
	if("localhost".equals(serverName)) {
		access = true;
	}

	String sql     = nullChk((String)request.getParameter("sql"));
	String mode    = nullChk((String)request.getParameter("mode"));
	String sqlType = nullChk((String)request.getParameter("sqlType"));
	String txtName = nullChk((String)request.getParameter("txtName"),"query");
	String SYSNAME = nullChk((String)request.getParameter("SYSNAME"), "bsc");
	String PKGNAME = nullChk((String)request.getParameter("PKGNAME"));
	String CLASSNAME = nullChk((String)request.getParameter("CLASSNAME"));
	String DESC = nullChk((String)request.getParameter("DESC"));
	
	if("".equals(sql)) sql = "SELECT * FROM BSC_CODE_GRP ORDER BY CODE_GRP_ID ASC";
	
	ArrayList<String[]> list = new ArrayList();
	String[] columnComments = new String[0];
	String[] columnNames = new String[0];
	
	try {
		list = executeSql(driver, url, user, pass, sqlType, sql);
		columnComments = getColumnComments(driver, url, user, pass, sqlType, sql);
		columnNames = getColumnsNames(driver, url, user, pass, sqlType, sql);	
	} catch(Exception e) {
		out.println("에러 발생 "+e);
	} finally {
		
	}
	
	//dmlWizard
	String findTableNm = nullChk((String)request.getParameter("findTableNm"), "");
	
	StringBuffer tSql = new StringBuffer();
	tSql.append("SELECT TABLE_NAME \n");
	tSql.append("FROM USER_TABLES \n");
	tSql.append("WHERE TABLE_NAME LIKE '%'||TRIM(UPPER('").append(findTableNm).append("'))||'%' \n");
	tSql.append("ORDER BY TABLE_NAME ASC \n");
	
	StringBuffer columnSql = new StringBuffer();
	columnSql.append("SELECT A.COLUMN_NAME, NVL(B.COMMENTS, A.COLUMN_NAME), A.DATA_TYPE, A.DATA_LENGTH                    \n");
	columnSql.append("    , DECODE(C.COLUMN_NAME,NULL,NULL, 'Y') PK                                                       \n");
	columnSql.append("    , CASE WHEN A.COLUMN_NAME = 'CREATE_DT' THEN 'Y'                                                \n");
	columnSql.append("           WHEN A.COLUMN_NAME = 'MODIFY_DT' THEN 'Y'                                                \n");
	columnSql.append("           WHEN A.COLUMN_NAME = 'DELETE_DT' THEN 'Y'                                                \n");
	columnSql.append("      END DATECOL                                                                                   \n");
	columnSql.append("FROM USER_TAB_COLUMNS A                                                                             \n");
	columnSql.append("    LEFT OUTER JOIN USER_COL_COMMENTS B                                                             \n");
	columnSql.append("            ON A.TABLE_NAME = B.TABLE_NAME                                                          \n");
	columnSql.append("            AND A.COLUMN_NAME = B.COLUMN_NAME                                                       \n");
	columnSql.append("    LEFT OUTER JOIN (                                                                               \n");
	columnSql.append("                               SELECT A.TABLE_NAME, B.COLUMN_NAME                                   \n");
	columnSql.append("                               FROM USER_CONSTRAINTS A                                              \n");
	columnSql.append("                                        LEFT OUTER JOIN USER_CONS_COLUMNS B                         \n");
	columnSql.append("                                                ON A.CONSTRAINT_NAME = B.CONSTRAINT_NAME            \n");
	columnSql.append("                               WHERE A.TABLE_NAME = TRIM(UPPER('").append(findTableNm).append("'))  \n");
	columnSql.append("                                 AND A.CONSTRAINT_TYPE = 'P'                                        \n");
	columnSql.append("    ) C ON A.TABLE_NAME = C.TABLE_NAME AND A.COLUMN_NAME = C.COLUMN_NAME                            \n");
	columnSql.append("WHERE A.TABLE_NAME = TRIM(UPPER('").append(findTableNm).append("'))                                 \n");
	columnSql.append("ORDER BY A.COLUMN_ID ASC                                                                            \n");
	                                                                                                                     
	ArrayList<String[]> tableList = new ArrayList();
	ArrayList<String[]> columnList = new ArrayList();

	try {
		tableList = executeSql(driver, url, user, pass, sqlType, tSql.toString());
		columnList = executeSql(driver, url, user, pass, sqlType, columnSql.toString());
	} catch(Exception e) {
		out.println("에러 발생 "+e);
	} finally {
		
	}
	
	
%>
<% if(access) { %>
	<!DOCTYPE HTML>
	<html>
	<head>
	<title>BSC Source Builder</title>
	<meta charset="utf-8">
	<link href="./include/common/main.css" media="screen" type="text/css" rel="stylesheet" />
	<link href="./include/common/fancybox/jquery.fancybox-1.3.4.css" media="screen" type="text/css" rel="stylesheet" />
	<link href="./include/redmond/jquery-ui-1.8.20.custom.css" media="screen" type="text/css" rel="stylesheet" />
	
	<script type="text/javascript" language="javascript" src="./include/common/jquery-1.7.2.min.js" ></script>
	<script type="text/javascript" language="javascript" src="./include/common/jquery-ui-1.8.20.custom.min.js"></script>
	<script type="text/javascript" language="javascript" src="./include/common/jquery.cookie.js"></script>
	<script type="text/javascript" language="javascript" src="./include/common/fancybox/jquery.easing-1.3.pack.js"></script>
	<script type="text/javascript" language="javascript" src="./include/common/fancybox/jquery.fancybox-1.3.4.js"></script>
	<script type="text/javascript" language="javascript" src="./include/common/fancybox/jquery.fancybox-1.3.4.pack.js"></script>
	<script type="text/javascript" language="javascript" src="./include/common/fancybox/jquery.mousewheel-3.0.4.pack.js"></script>
	<script type="text/javascript" language="javascript">
	//<![CDATA[
		$(document).ready(function() {
			$("#anchor").fancybox();  
			init(); 
		});
		
		function goSearch() {
			var mode = $.trim($("#sql").val()).split(" ")[0].toUpperCase();
	
			if(mode == "INSERT" || mode == "UPDATE" || mode == "DELETE") {
				$("#sqlType").val("DML");
			} else {
				$("#sqlType").val("SELECT");
			}
			
			setCookie();
			
			$("#form1").submit();
		}
		
		function goCreate() {
			if($("#SYSNAME").val() == "") {
				alert("SYSNAME 명을 선택해 주십시요.");
				$("#SYSNAME").focus();
				return;
			}
			
			if($("#PKGNAME").val() == "") {
				alert("PKGNAME 명을 선택해 주십시요.");
				$("#PKGNAME").focus();
				return;
			}
			
			if($("#CLASSNAME").val() == "") {
				alert("Action 명을 입력해 주십시요.");
				$("#CLASSNAME").focus();
				return;
			}
			
			if($("#USERNAME").val() == "") {
				alert("개발자명을 입력해 주십시요.");
				$("#USERNAME").focus();
				return;
			}
			
			if($("#DESC").val() == "") {
				alert("메뉴설명을 입력해 주십시요.");
				$("#DESC").focus();
				return;
			}
			
			if($('input:checkbox[id="overWrite"]').is(":checked")) {
				if(!confirm("기존 파일을 삭제하고 재생성하시겠습니까?\n(주의 : 기존파일 삭제됨)")) return;
			}
			
			setCookie();
			
			$("#form1 select").attr("disabled",false);
			
			$("#form1").attr("action", "<%=request.getContextPath()%>/makecode/include/core/file_create.jsp").submit();
		}
		
		function init() {
			var bscCreatorName = $.cookie("bscCreatorName");

			if(bscCreatorName != null && bscCreatorName != "") {
				$("#USERNAME").val(bscCreatorName);
			}
		}
		
		function setCookie() {
			var checkStatus = $("#chkSave").is(":checked");
			
			if(checkStatus){
				$.cookie('bscCreatorPath', $("#real_path").val());
				$.cookie('bscCreatorName', $("#USERNAME").val());
			} else {    
				$.cookie('bscCreatorPath', null); //cookie 삭제
			}
		}
	
		function goPop() {
			var newWin=window.open("wizard.jsp" ,"wizard","width=1000,height=650,left=10,top=10, scrollbars=yes, toolbar=no");
			newWin.focus();
		}
		
		function setColumnData(param) {
			$("#columnsDataArea").empty();
			$("#columnsDataArea").append($(param));
		}
		
		function show(obj) {
			if(obj == 'dataGrid') {
				$("#dataGrid").show();
				$("#dmlWizard").hide();
			} else {
				$("#dataGrid").hide();
				$("#dmlWizard").show();
			}
		}
		
		function goTableSearch() {
			$("#mode").val("wizard");
			$("#form1").submit();
		}
		
	//]]>
	</script>
	</head>
	<body>
	
	<form id="form1" name="form1" method="post"	action="<%=request.getRequestURI()%>">
	<input type="hidden" id="autoDirectoryCopy" name="autoDirectoryCopy" 	value="<%=autoDirectoryCopy %>" />
	<input type="hidden" id="sqlType"   		name="sqlType" />
	<input type="hidden" id="tableNm" 			name="tableNm" />
	<input type="hidden" id="mode" 				name="mode" />
	
	<% 
		if(0 < columnComments.length) {
			for(int i = 0; i < columnComments.length; i++) {
	%>
			<input type="hidden" id="columnComments" name="columnComments" value="<%=columnComments[i] %>"/>			
	<% 
			}
		} 
	%>
	<br/>
	<% 
		if(0 < columnNames.length) {
			for(int i = 0; i < columnNames.length; i++) {
	%>
			<input type="hidden" id="columnNames" name="columnNames" value="<%=columnNames[i] %>"/>			
	<% 
			}
		} 
	%>
	
		<div id="wrap">
			<header id="top_header">
				<h2>BSC 소스 Builder 2.2</h2>
			</header>
			<section id="contents">
				<table>
					<tr>
						<td>SQL 문장을 입력해 주십시요.</td>
						<td>&nbsp;</td>
						<td>소스파일 항목을 설정해 주십시요.</td>
					</tr> 
					<tr>
						<td> 
							<div id="sqlBox" class="box" style="width:700px;">
								<textarea id="sql" name="sql" rows="16" cols="50" style="width:98%;"><%=sql %></textarea>
							</div>
						</td>			
						<td>	
							<input type="button" class="input_btn2" value="SQL문 실행" onclick="javascript:goSearch();" style="height:70px;"/>
						</td>
						<td style="vertical-align:top;"> 
							<div id="box" class="box">
								생성위치 : <%=real_path %> <input type="hidden" id="real_path" name="real_path" value="<%=real_path %>" style="width:75%;" />
								<br/>
								<hr/>
								PACKAGE : com.lexken.
									<select id="SYSNAME" name="SYSNAME" style="width:50px;">
										<option value=''     <% if("".equals(SYSNAME)) out.println("selected"); %>>선택</option>
										<option value='bsc'  <% if("bsc".equals(SYSNAME)) out.println("selected"); %>>bsc</option>  <!-- 조직성과 -->
										<option value='str'  <% if("str".equals(SYSNAME)) out.println("selected"); %>>str</option>  <!-- 전략 -->
										<option value='gov'  <% if("gov".equals(SYSNAME)) out.println("selected"); %>>gov</option>  <!-- 정부경영평가 -->
										<option value='prs'  <% if("prs".equals(SYSNAME)) out.println("selected"); %>>prs</option>  <!-- 개인평가 -->
										<option value='tot'  <% if("cbe".equals(SYSNAME)) out.println("selected"); %>>cbe</option>  <!-- 개인종합 -->
									</select>.
									<select id="PKGNAME" name="PKGNAME" style="width:70px;">
										<option value=''          <% if("".equals(PKGNAME)) out.println("selected"); %>>선택</option>
										<option value='office'    <% if("office".equals(PKGNAME)) out.println("selected"); %>>office</option>
										<option value='measure'   <% if("measure".equals(PKGNAME)) out.println("selected"); %>>measure</option>
										<option value='unmeasure' <% if("unmeasure".equals(PKGNAME)) out.println("selected"); %>>unmeasure</option>
										<option value='bos'       <% if("bos".equals(PKGNAME)) out.println("selected"); %>>bos</option>
										<option value='result'    <% if("result".equals(PKGNAME)) out.println("selected"); %>>result</option>
										<option value='ahp'       <% if("ahp".equals(PKGNAME)) out.println("selected"); %>>ahp</option> <!-- AHP 평가 -->
										<option value='matrix'    <% if("matrix".equals(PKGNAME)) out.println("selected"); %>>matrix</option> <!-- 매트릭스 평가 -->
										<option value='level'     <% if("level".equals(PKGNAME)) out.println("selected"); %>>level</option>   <!-- 난이도 평가 -->
										<option value='mon'       <% if("mon".equals(PKGNAME)) out.println("selected"); %>>mon</option>
										<option value='base'      <% if("base".equals(PKGNAME)) out.println("selected"); %>>base</option>
										<option value='tam'       <% if("tam".equals(PKGNAME)) out.println("selected"); %>>tam</option>
										<option value='eval'      <% if("eval".equals(PKGNAME)) out.println("selected"); %>>eval</option>
										<option value='system'    <% if("system".equals(PKGNAME)) out.println("selected"); %>>system</option>
										<option value='sample'    <% if("sample".equals(PKGNAME)) out.println("selected"); %>>sample</option>
									</select>
				                    <br/>
								Action &nbsp;&nbsp;&nbsp;: <input type="text" id="CLASSNAME" name="CLASSNAME" value="<%=CLASSNAME %>" style="width:80px">Action&nbsp;&nbsp;<br/>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; (Camel기법으로 표기-소문자로 시작 ex: codeGrp)<br/>
								개발자명 : <input type="text" id="USERNAME" name="USERNAME" value="" style="width:200px"><br/>
								메뉴설명 : <input type="text" id="DESC" name="DESC" value="<%=DESC %>" style="width:200px">
								
								<div style="display:none;">
								(<input type="checkbox" id="chkSave" name="chkSave" checked/> <span title="쿠키저장 체크시 생성위치와 개발자명이 쿠키에 저장됩니다.">쿠키저장</span>)
								</div>
								<br/><br/>
								<div style="text-align:right">
									<input type="button" class="input_btn2" value="코드생성" onclick="javascript:goCreate();" style="height:30px;" />
								</div>
							</div>
						</td>
					</tr>
				</table>
				<input type="button" class="input_btn2" value="DataGrid" onclick="javascript:show('dataGrid');" style="height:30px;" />
				<input type="button" class="input_btn2" value="DmlWizard" onclick="javascript:show('dmlWizard');" style="height:30px;" />
				<div id="dataGrid" <% if("wizard".equals(mode)) { %> style="display:none" <% } %>>
					<table  bgcolor="#ffffff"  border="0" cellpadding="0" cellspacing="0" class="td_table">
						<tr>
							<td>
								<table class="tbl_type" border="1" cellspacing="0">
						 			<thead>  
					                    <tr>
					                    	<% for(int i = 0; i < columnComments.length; i++) { %>  
					                        <th scope="col"><%=columnComments[i] %></th>
					                        <% } %>
					                    </tr>  
				                	</thead>
				                	<tbody>
				                		<% 
				                			String[] values = null;
				                			for(int i = 0; i < list.size(); i++) {
				                				values = list.get(i);
				                		%>
				                		<tr>
											<% for(int j = 0; j < values.length; j++) { %>
												<td><%= values[j] %></td>
											<% } %>
										</tr>
				                		<% } %>
				                	</tbody>
				                </table>
								&nbsp;
							</td>
						</tr>
					</table>
				</div>
				<div id="dmlWizard" <% if(!"wizard".equals(mode)) { %> style="display:none" <% } %>>
					<table  bgcolor="#ffffff"  border="0" cellpadding="0" cellspacing="0" class="td_table">
						<tr>
							<td>
								Table : <input type="text" id="findTableNm" name="findTableNm" style="width:200px;" value="<%=findTableNm%>"/>
								<input type="button" class="input_btn2" value="조회" onclick="javascript:goTableSearch();" style="height:30px;" />
								&nbsp;&nbsp;<input type="checkbox" id="overWrite" name="overWrite" value="true"/>덮어쓰기<span style="color:red;">(주의:기존소스 덮어씀)</span>&nbsp;&nbsp;
							</td>
						</tr>
					</table>
					<br/>
					<table  bgcolor="#ffffff"  border="0" cellpadding="0" cellspacing="0" class="td_table">
						<tr>
							<td>
								<div id="colData">
									<table class="tbl_type" border="1" cellspacing="0" style="width:850px;">
							 			<thead>  
						                    <tr>
						                        <th scope="col">컬럼</th>
						                        <th scope="col">컬럼명</th>
						                        <th scope="col">PK</th>
						                        <th scope="col">TYPE</th>
						                        <th scope="col">LENGTH</th>
						                        <th scope="col">입력설정</th>
						                        <th scope="col">필수입력</th>
						                    </tr>  
					                	</thead>
					                	<tbody>
					                		<% 
					                			String[] dataVal = null;
					                			for(int i = 0; i < columnList.size(); i++) {
					                				dataVal = columnList.get(i);
					                		%>
					                		
					                			<tr <% if("Y".equals(dataVal[4]) || "Y".equals(dataVal[5])) { %> class="tbl_type_pk" <% } %>>
													<td style="text-align:left;"><%= dataVal[0] %> <input type="hidden" name="colNames" value="<%= dataVal[0] %>"></td>
													<td style="text-align:left;"><%= dataVal[1] %> <input type="hidden" name="comments" value="<%= dataVal[1] %>"></td>
													<td style="text-align:center;"><%= dataVal[4] %> <input type="hidden" name="colPks" value="<%= dataVal[4] %>"></td>
													<td style="text-align:center;"><%= dataVal[2] %> <input type="hidden" name="types" value="<%= dataVal[2] %>"></td>
													<td style="text-align:center;"><%= dataVal[3] %> <input type="hidden" name="colLength" value="<%= dataVal[3] %>"></td>
													<td style="text-align:center;">
														<select name="inputColType" <% if("Y".equals(dataVal[4]) || "Y".equals(dataVal[5])) { %> disabled <% } %>>
															<option value="">미사용</option>
															<option value="InputBox" selected>InputBox</option>
															<option value="SelectBox">SelectBox</option>
															<option value="TextArea">TextArea</option>
														</select>
													</td>
													<td>
														<select name="notNullType" <% if("Y".equals(dataVal[4]) || "Y".equals(dataVal[5])) { %> disabled <% } %>>
															<option value="N">N</option>
															<option value="Y">Y</option>
														</select>
													</td>
												</tr>
					                		<% 
					                			} 
					                		%>
					                	</tbody>
					                </table>
					        	</div>
							</td>
						</tr>
					</table>
				</div>
			</section> 
			<footer>
		        <p>&nbsp;</p>
		    </footer>
		</div>
		<div id="columnsDataArea" style="display:none;">
		</div>
		<a id="anchor"></a>
	</form>
	</body>
	</html>
<% } else { %>
접근제한
<% } %>
