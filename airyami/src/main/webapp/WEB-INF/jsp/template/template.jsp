<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="egovframework.airyami.cmm.util.*"%>

<head>
<meta http-equiv="Content-Type" content="text/html;application/json; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<%@ include file="/include/title.jsp"%>
<%@ include file="/include/admin_standard.jsp"%>

<script type="text/javascript">

$(function() {  //onready
	//여기에 최초 실행될 자바스크립트 코드를 넣어주세요
	
	gfn_OnLoad();
	
	fn_init();
	
});

// 화면내 초기화 부분
function fn_init(){
	// 돌아가기로 온 경우 이전 파라미터 복원작업 필요
	debugger;
	
	// 체크박스 초기화
	//gfn_setCheck($('#C001001'));
	//$('#C001001').attr('checked', true);
	// select 박스 초기화
	//alert('<spring:message code="common.save.msg" />');
	gfn_setSelect($('#LANG_CD'), 'en');
	gfn_setSelect($('#GRP_CD'), 'C002');
	//$('#LANG_CD').val('C001002');
}

////////////////////////////////////////////////////////////////////////////////////
// 호출부분 정의
// 리스트 조회
function fn_srch(){
	
	if(!gfn_validationForm($("#srchForm"))){
		return;
	}
	
	
	var inputParam = new Object();
	inputParam.sid 				= "codeList";
	inputParam.url 				= "/commCode/codeList.do";
	inputParam.data 			= $("#srchForm").serialize();
	//inputParam.callback			= "fn_callBack";
	
	gfn_Transaction( inputParam );
}



// 연동 select 코드 조회
function fn_selectLink(code){
	if(gfn_isNull(code)){
		return;
	}
	
	gfn_GetCodeList(code, $('#SUB_CD'));
}

// file upload
function fn_excelSelect() {
	
	if(!confirm('파일이 선택되었습니다. 업로드를 실행하시겠습니까?')){
		$('#INFILE').val('');
		return;	
	}
	
	$('#PSITE_ID').val(1);
	$('#frmExcelUpload').ajaxSubmit({
	      url		: "<%=UrlUtil.getActionRoot(request)%>/MOP/insertFileTest.do"
	    , dataType	: "json"
	    , success	: function(result, textStatus, data) {
	        callbackExcelUpload( result );
	    }
	    , error		: function(xhr, errorName, error) {
	        alert("에러가 발생하였습니다.");
	    }
	});
}
////////////////////////////////////////////////////////////////////////////////////
// 콜백 함수
function fn_callBack(sid, result, data){
	//debugger;
	
	if (!result.success) {
		alert(result.msg);
		return;
	}
	
	
	// fn_srch
	if(sid == "codeList"){
		var tbHiddenInfo = ["GROUP_CODE", "RULE_CD"]; // row에 추가할 히든 컬럼 설정  없으면 삭제
		
		gfn_displayList(result.ds_list, "tb_list", tbHiddenInfo);
		gfn_displayTotCnt(result.totCnt);
//		debugger;
		gfn_addPaging(result.pageInfo, 'gfn_clickPageNo');
	}
	
}

//사용자 찾기 팝업 callback
function fn_CallbackPopupMember(inputParam){
	$('#USER_ID').val(inputParam.code);
	$('#USER_NM').val(inputParam.name);
}

function callbackExcelUpload(result) {
	$('#INFILE').val('');
    if(result.success) {
        alert( result.size + '건 업로드 하였습니다.');
        fn_srch();
    } else {
        alert('업로드에 실패하였습니다.');
        document.location.reload();
    }
}
////////////////////////////////////////////////////////////////////////////////////
// Click evnet
function fn_callPopup(pObj){
	debugger;
	var rowObj = $(pObj).parent();
	var GROUP_CODE = $('input[name=GROUP_CODE]', rowObj).val();
	
	var inputParam = new Object();
	inputParam.GROUP_CODE = GROUP_CODE;
	
	fn_popCommCodeInfo(inputParam);
}

function fn_test2(pObj){
	fn_popSusDomainInfo();
}

// 삭제 버튼
function fn_Delte(){
	alert("delete");
}

// 등록 버튼
function fn_Insert(){
	alert("fn_Insert");
}



// 화면전환
function fn_gopage(){
	var inputParam = {};
	
	inputParam.aaa = "111aaa";
	inputParam.aaaNm = "배수한";
	
	
	gfn_commonGo("/template/template_list", inputParam, "N");
}

////////////////////////////////////////////////////////////////////////////////////
// 팝업 호출	
	
function fn_popCommCodeInfo(inputParam){
	
	inputParam.SYS_DT = "20131017";
	inputParam.DOMAIN = "ppe.pe.kr";
	
	gfn_commonGo("/commcode/commcode_list_popup", inputParam, "Y");
	
}

function fn_popSusDomainInfo(){
	var inputParam = new Object();
	inputParam.SYS_DT = "20131017";
	inputParam.DOMAIN = "DDD.NET";
	
	gfn_commonGo("/POP/POP0003P", inputParam, "Y");
	
}

//사용자 찾기 팝업
function fn_popMember(){
	var inputParam = new Object();
	inputParam.USER_NM = $('#USER_NM').val();
	
	gfn_commonGo("/POP/POP0011P", inputParam, "Y");
}

//사용자 찾기 팝업
function fn_popCommCode(){
	var inputParam = new Object();
	inputParam.GROUP_CODE = "GROUP";
	
	gfn_commonGo("/admin/commcode/commcode_list_popup", inputParam, "Y");
}



////////////////////////////////////////////////////////////////////////////////////
// 기타 기능 함수
//파일다운로드 클릭
function fn_clickFileDown() {
    var inputParam = new Object();
    //inputParam.REAL_NM	 = $("input[name=REAL_NM]").val();
    //inputParam.SAVE_PATH = $("input[name=SAVE_PATH]").val();
    // 패스를 알경우
    //inputParam.REAL_NM	 = "aaaaaaa.docx";
    //inputParam.SAVE_PATH = "D:/temp/upload/20131023043625110.docx";
    
    // MST_ID를 알경우
    inputParam.MST_ID	 = "81";
    
    gfn_fileDownload(inputParam);
}

// 팝업 내용 변경시 초기화
function fn_userNmChange() {
    $('#USER_ID').val('');
}


// chart
function fn_setChart(list){
	
	var inputParam = new Object();
	inputParam.CAPTION		= "TEST Chart";
	inputParam.RURL  		= '<%=UrlUtil.getActionRoot(request)%>';
	inputParam.LIST			= list;
	inputParam.CHART		= "Column3D";
	inputParam.xAxisName	= "분류";		// x축 캡션
	inputParam.yAxisName	= "건수";		// y축 캡션

	gfn_Chart(inputParam, $('#chartContainer'));
}

////////////////////////////////////////////////////////////////////////////////////
</script>

</head>

<body>
<div class="container">
	<%-- <%@ include file="/layout/top_menu.jsp"%> --%>
	<!-- 실제 코딩 시작 -->
    <div class="section">
		<div class="box">
		    <div class="content">
				<!-- 검색조건 START -->
				<div class="title"><h2>Select 박스 적용</h2><span class="hide"></span></div>
				<div class="search_bar">
				<form id="myParams" name="myParams">
					<ppe:makeHidden var="${findParams}" filter="FIND_" exclude="FIND_RETURNURL"/>
				</form>
				<form id="srchForm" name="srchForm" method="post" onsubmit="return false;">
					<input type="hidden" name="pageNo" id="pageNo" value="1"/>
					<input type="hidden" name="USER_ID" id="USER_ID" />
				</form>
					
					<span class="tt"><label>Selectbox</label></span>
					<select id="LANG_CD" name="LANG_CD">
                        <option value="">-- <spring:message code="word.all" /> --</option>
                        <c:forEach var="LANG" items="${ds_cd_LANG}">
                            <option value="${LANG.CD}">${LANG.CD_NM}</option>
                        </c:forEach>
                    </select>
                    <span class="tt ml20"><label>Selectbox3</label></span>
					<select id="GRP_CD" name="GRP_CD" onchange="javascript:fn_selectLink(this.value);">
                        <c:forEach var="GRP_CD" items="${ds_cd_GRP_CD}">
                            <option value="${GRP_CD.CD}">${GRP_CD.CD_NM}</option>
                        </c:forEach>
					</select>
					<select id="SUB_CD" name="SUB_CD">
					</select>
				<div class="title"><h2>체크박스</h2><span class="hide"></span></div>
					<!-- 체크박스 -->
					<span class="tt"><spring:message code="word.lang" /></span>
					<c:forEach var="LANG" items="${ds_cd_LANG}">
						<input type="checkbox" id="${LANG.CD}" name="${LANG.CD}" value="${LANG.CD}"/>
						<label for="${LANG.CD}">${LANG.CD_NM}</label>
					</c:forEach>
					
					<input type="text" id="search_word" name="search_word" value="" title="검색어 입력" style="width:160px;" depends="required, englishNumeric" maxlength=12>
				
				<div class="title"><h2>라디오박스</h2><span class="hide"></span></div>
					<!-- 체크박스 -->
					<span class="tt"><spring:message code="word.lang" /></span>
					<c:forEach var="LANG" items="${ds_cd_LANG}">
						<input type="radio" id="${LANG.CD}" name="CHK_LANG" value="${LANG.CD}"/>
						<label for="${LANG.CD}">${LANG.CD_NM}</label>
					</c:forEach>
				
				<div class="title"><h2>그리드 조회</h2><span class="hide"></span></div>
					<button type="submit" class="red" onClick="gfn_fn_srch();" ><span>조회</span></button>
					<!-- 출력건수 -->
					<div class="tablelistQuantity" id="pageUnit"></div>
					<!--// 출력건수 -->
			 	<!-- 가로 스크롤 -->
				<div class="scroll"> 
					<table id="tb_list" cellspacing="0" cellpadding="0" border="0" class="sorting">
						<colgroup>
							<col width="20%" />
							<col width="30%" />
							<col width="30%" />
							<col width="30%" />
							<col width="30%" />
							<col width="30%" />
						</colgroup> 
						<thead> 
							<tr> 
								<th cid="GROUP_CODE" alg="center" clickevent="fn_callPopup(this);" url="/admin/commcode/commcode_list_popup.do">GROUPCODE</th>
								<th cid="CD" alg="center" clickevent="fn_test2(this);">CODE</th>
								<th cid="CD_NM" alg="center"><</th>
								<th cid="CODE_DESC" alg="center">코드설명</th>
								<th cid="ETC" alg="center">기타</th>
							</tr> 
						</thead> 
						<tbody>
							<tr></tr>
						</tbody> 
					</table>
				</div>
				
				<!-- total -->
		        <div class="total" id="totCnt">총건수 : 건</div>
		        <!--// total -->
		        
				<%-- <div class="paginate">
					<c:set var="pageParam" value="pageUnit=${pageUnit}&#38;" />
					<dw:pagination paginationInfo="${paginationInfo}" type="image" jsFunction="linkPage" pageParam="${pageParam}"  />
				</div> --%>
				<%-- <div class="paginate">
					<ui:pagination paginationInfo = "${paginationInfo}" type="image" jsFunction="linkPage"/>
				</div> --%>
				<div><span id="pagingNav" class="pagenate"></span></div>
				
				<div class="title"><h2>화면전환 호출</h2><span class="hide"></span></div>
					<input type="button" class="input" value="화면전환" onclick="javascript:fn_gopage();return false;"/>
					<input type="text" id="aaa" />
					<input type="text" id="aaaNm" />
				
				<div class="title"><h2>popup 호출</h2><span class="hide"></span></div>
					<span class="tt ml20">
					<label>관리자</label>
					<input type="text" id="USER_NM" name="USER_NM" onkeydown="javascript:fn_userNmChange();"/>
					<a id="ahref" href="/commcode/commcode_list_popup.do?GROUP_CODE=GROUP&RULE_CD=;" onclick="javascript:alert('화면전환');return false;"><img src="/img/icon_search.gif" alt="화면전환" /></a>
					<a href="/dWise/admin/commcode/commcode_list_popup.do?GROUP_CODE=GROUP&RULE_CD=;" onclick="javascript:fn_popCommCode();return false;" target="_blank">popup</a>
					<a href="/dWise/admin/commcode/commcode_list_popup.do?GROUP_CODE=GROUP&RULE_CD=;" target="_blank">popup no script</a>
					
					<br/>
				</div>
				<!-- 검색조건 END -->
				
				<%-- 
				<form:form commandName="adminAdmin" name="adminAdmin" method="post">
					<form:input path="admin_id" maxlength="20" id="admin_id" />
					<form:errors path="admin_id" />
					<input type="submit" value="등록" onClick="javascript:fnInsert(document.adminAdmin); return false;" />
				</form:form>
				 --%>
				 
				<!-- 그래프 자리 -->
				<div id="chartContainer" class="graphic" style="width:500px;height:200px;">
				</div>
				<!--// 그래프 자리 -->
				
				<!-- fileUpload -->
				<span>
				    <form id="frmExcelUpload" name="frmExcelUpload" method="post" enctype="multipart/form-data" onsubmit="return false;">
				        <input type="file" id='INFILE' name='INFILE' onchange="javascript:fn_excelSelect();">
				        <input type="hidden" id="PSITE_ID" name="PSITE_ID">
				    </form>
				</span>
				<!-- //fileUpload -->
				
				<!-- 테이블 정보 -->
				<div class="list_info">
					<div class="left"> <span>전체 : 940,</span> <span>공공기관 : 400,</span> <span>금융기관 : 500,</span> <span>기타 : 0</span> </div>
					<div class="right">
						<button type="submit" class="grey" style="" onClick="fn_clickFileDown();"><span>파일다운로드</span></button>
						<button type="submit" class="grey" style="" onClick="fn_Delte();"><span>삭제</span></button>
						<button type="submit" class="blue" onClick="fn_Insert();"><span>저장</span></button>
					</div>
				</div>
				<!-- 테이블 정보 END --> 
				
			</div>
	 	</div>
	 	<form action="/dWise/login/login.do" method="post">
		아이디 : <input type="text" name="id"/><br/>
		비밀번호 : <input type="text" name="passwd"><br/>
		<input type="submit" value="로그인"/>
		</form>
	 	
	</div>
	<!-- 실제 코딩 end -->

	<%@ include file="/layout/footer.jsp"%>
</div>
</body>