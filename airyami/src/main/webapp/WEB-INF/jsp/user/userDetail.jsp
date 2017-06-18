<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="egovframework.airyami.cmm.util.*"%>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html;application/json; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<%@ include file="/include/title.jsp"%>
<%@ include file="/include/admin_standard.jsp"%>
<jsp:include page="/include/common.jsp"/>

<script type="text/javascript">

$(function() {  //onready
	//여기에 최초 실행될 자바스크립트 코드를 넣어주세요
	gfn_OnLoad();
	fn_init();
	if('<c:out value="${MODE}"/>' == 'DETAIL'){
		fn_srch();	// 상세조회
	}
});

// 화면내 초기화 부분
function fn_init(){
	// 체크박스 초기화
	//gfn_setCheck($('#C001001'));
	// select 박스 초기화
	//gfn_setSelect($('#SYS_CD'), 'C001002');
}

////////////////////////////////////////////////////////////////////////////////////
// 호출부분 정의
// 상세조회 조회
function fn_srch(){
	var inputParam = new Object();
	inputParam.sid 				= "getUserDetail";
	inputParam.url 				= "/user/getUserDetail.do";
	inputParam.data 			= gfn_makeInputData($("#dataForm"));
	//inputParam.callback			= "fn_callBack";
	
	gfn_Transaction( inputParam );
}


////////////////////////////////////////////////////////////////////////////////////
// 콜백 함수
function fn_callBack(sid, result, data){
	if (!result.success) {
		alert(result.msg);
		return;
	}
	
	// fn_srch
	if(sid == "getUserDetail"){
		gfn_setDetails(result.ds_detail, $("#contents"));	// 지역내 상세 내용 셋업
	}else if(sid = "saveUser"){
		// 저장
		alert("<spring:message code="success.request.msg"/>");
		
		var inputParam = gfn_makeInputData($("#findForm"));
		gfn_commonGo("/user/userList", inputParam, "N");
	}
}

////////////////////////////////////////////////////////////////////////////////////
// Click evnet
// 저장버튼클릭
function fn_goSave(){
	
	if(!gfn_validationForm($("#dataForm"))){
		return;
	}
<c:choose>
	<c:when test="${MODE=='CREATE'}">
		if($('#USER_PSWD').val() != $('#USER_PSWD_CONFIRM').val()){
			alert("비밀번호와 비밀번호확인이 틀립니다.");
			return;
		}
	</c:when>
</c:choose>
	if(!confirm("<spring:message code="common.save.msg"/>")){
		return false;
	}
	
	var inputParam = gfn_makeInputData($("#dataForm"));
	inputParam.sid 				= "saveUser";
	inputParam.url 				= "/user/saveUser.do";
	
	if('<c:out value="${MODE}"/>' == 'DETAIL'){
		$('#PROC_MODE').val("UPDATE");
	}else{
		$('#PROC_MODE').val("CREATE");
	}
	inputParam.data 			= gfn_makeInputData($("#dataForm"));
	gfn_Transaction( inputParam );
}

function goDelete(){
	if(!confirm("<spring:message code="common.delete.msg"/>")){
		return false;
	}
	
	var inputParam = gfn_makeInputData($("#dataForm"));
	inputParam.sid 				= "saveUser";
	inputParam.url 				= "/user/saveUser.do";
	$('#PROC_MODE').val("DELETE");
	inputParam.data 			= gfn_makeInputData($("#dataForm"));
	
	gfn_Transaction( inputParam );
}

//목록 버튼
function fn_goBack(){
	var inputParam = gfn_makeInputData($("#findForm"));
	
	gfn_commonGo("/user/userList", inputParam, "N");
}

////////////////////////////////////////////////////////////////////////////////////
// 팝업 호출
function go_CompanyPop(){
	var inputParam				= {};
	inputParam.POP_COMP_NM 	= $('#BIZ_ENTITY_NM').val();
	inputParam.sid 	= "findCompany";

	gfn_commonGo("/user/companyFindListPop", inputParam, "Y");
}

//팝업콜백 함수
function fn_popCallBack(sid, data){
	// fn_srch
	if(sid == "findCompany"){
		$('#BIZ_ENTITY_ID').val(data.BIZ_ENTITY_ID);
		$('#BIZ_ENTITY_NM').val(data.BIZ_ENTITY_NM);
	}
}

////////////////////////////////////////////////////////////////////////////////////
// 기타 기능 함수
// 팝업 내용 변경시 초기화
function fn_companyNmChange() {
    $('#BIZ_ENTITY_ID').val('');
}


function fn_clearData(){
	
	gfn_clearData($("#contents"));
}

////////////////////////////////////////////////////////////////////////////////////
</script>

</head>
<body>
<!-- wrap -->
<div id="wrap">

<!--  header -->
<%@ include file="/layout/header.jsp"%>
<!--//  header --> 

<!-- container -->
<div id="container">
	<!-- menu -->
<%@ include file="/layout/menu_left.jsp"%>
	<!--// menu -->
  
	<div id="contents">
		<h3><spring:message code="word.userDetail"/></h3>
		<form id="findForm" name="findForm">
			<ppe:makeHidden var="${findParams}" filter="FIND_"/>
		</form>
		<form id="dataForm" name="dataForm" method="post" action="#" onsubmit="return false;">
			<input type="hidden" name="SEARCH_USER_ID" id="SEARCH_USER_ID" value='<c:out value="${USER_ID}"/>'/>
			<input type="hidden" name="MODE" id="MODE" value='<c:out value="${MODE}"/>'/>
			<input type="hidden" name="PROC_MODE" id="PROC_MODE" value=''/>
		<table summary="<spring:message code="word.userDetail"/>" cellspacing="0" border="0" class="tbl_list_type2">
			<colgroup>
			<col width="12%">
			<col width="15%">
			<col width="20%">
			<col width="15%">
			<col width="38%">
			</colgroup>
			<tr>
				<th colspan="2"><spring:message code="word.userId"/></th>
			<c:choose>
				<c:when test="${MODE=='DETAIL'}">
				<td colspan="3"><input type="text" name="USER_ID" id="USER_ID" disabled /></td>
				</c:when>
				<c:when test="${MODE=='CREATE'}">
				<td colspan="3"><input type="text" name="USER_ID" id="USER_ID" maxlength="20" title="<spring:message code="word.userId"/>" depends="required,englishNumeric"/></td>
				</c:when>
			</c:choose>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.userNm"/></th>
				<td colspan="3"><input type="text" name="ORIGINAL_NM" id="ORIGINAL_NM" maxlength="20" title="<spring:message code="word.userNm"/>" depends="required"/></td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.passportNm"/></th>
				<td colspan="3"><input type="text" name="PASSPORT_NM" id="PASSPORT_NM" maxlength="20" title="<spring:message code="word.passportNm"/>" depends="required,english" /></td>
			</tr>
	<c:choose>
		<c:when test="${MODE=='CREATE'}">
			<tr>
				<th colspan="2"><spring:message code="word.userPass"/></th>
				<td colspan="3"><input type="password" name="USER_PSWD" id="USER_PSWD" maxlength="20" title="<spring:message code="word.userPass"/>" depends="required"/></td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.userPassConfirm"/></th>
				<td colspan="3"><input type="password" name="USER_PSWD_CONFIRM" id="USER_PSWD_CONFIRM" maxlength="20" title="<spring:message code="word.userPassConfirm"/>" depends="required"/></td>
			</tr>
		</c:when>
	</c:choose>
			<tr>
				<th colspan="2"><spring:message code="word.userTypeRole"/></th>
				<td colspan="3">
					<select id="USER_TYPE_ROLE" name="USER_TYPE_ROLE" title="<spring:message code="word.userTypeRole"/>" depends="required" style="width:150px">
						<option value=""><spring:message code="word.select"/></option>
                        <c:forEach var="typeRoleList" items="${ds_typeRoleList}">
                            <option value="${typeRoleList.USER_TYPE_ROLE}">${typeRoleList.USER_ROLE_NM}</option>
                        </c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.bizEntityId"/></th>
				<td colspan="3">
					<input type="text" name="BIZ_ENTITY_NM" id="BIZ_ENTITY_NM" maxlength="20" title="<spring:message code="word.bizEntityId"/>" depends="" onChange="fn_companyNmChange();"/>
					<button type="button" id="btnW_companyPop" onClick="javascript:go_CompanyPop()"><spring:message code="button.search"/></button>
					<input type="text" name="BIZ_ENTITY_ID" id="BIZ_ENTITY_ID" maxlength="8" title="<spring:message code="word.bizEntityId"/>" depends="required" readOnly/>
				</td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.userSex"/></th>
				<td colspan="3">
					<select id="SEX" name="SEX" title="<spring:message code="word.userSex"/>" depends="required" style="width:80px">
						<option value=""><spring:message code="word.select"/></option>
                        <option value="M"><spring:message code="word.man"/></option>
                        <option value="F"><spring:message code="word.female"/></option>
					</select>
				</td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.userBirth"/></th>
				<td colspan="3">
					<select id="BIRTH_YEAR" name="BIRTH_YEAR" title="<spring:message code="word.year"/>" depends="required" style="width:80px">
						<option value=""><spring:message code="word.select"/></option>
                        <c:forEach var="birthYearList" items="${ds_birthYearList}">
                            <option value="${birthYearList.YEAR}">${birthYearList.YEAR}<spring:message code="word.year"/></option>
                        </c:forEach>
					</select>
					<select id="BIRTH_MONTH" name="BIRTH_MONTH" title="<spring:message code="word.month"/>" depends="required" style="width:80px">
						<option value=""><spring:message code="word.select"/></option>
                        <c:forEach var="birthMonthList" items="${ds_birthMonthList}">
                            <option value="${birthMonthList.MONTH}">${birthMonthList.MONTH}<spring:message code="word.month"/></option>
                        </c:forEach>
					</select>
					<select id="BIRTH_DAY" name="BIRTH_DAY" title="<spring:message code="word.day"/>" depends="required" style="width:80px">
						<option value=""><spring:message code="word.select"/></option>
                        <c:forEach var="birthDayList" items="${ds_birthDayList}">
                            <option value="${birthDayList.DAY}">${birthDayList.DAY}<spring:message code="word.day"/></option>
                        </c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<th rowspan="2"><spring:message code="word.userHomeAddr"/></th>
				<th><spring:message code="word.userAddrCountry"/></th>
				<td>
                   <select id="HOME_ADDR_COUNTRY" name="HOME_ADDR_COUNTRY" title="<spring:message code="word.userAddrCountry"/>" depends="" style="width:150px">
						<option value=""><spring:message code="word.select"/></option>
                        <c:forEach var="addrCountryList" items="${ds_addrCountryList}">
                            <option value="${addrCountryList.CD}">${addrCountryList.CD_NM}</option>
                        </c:forEach>
					</select>
				</td>
				<th><spring:message code="word.userAddrProvince"/></th>
				<td><input type="text" name="HOME_ADDR_PROVINCE" id="HOME_ADDR_PROVINCE" maxlength="20" title="<spring:message code="word.userAddrProvince"/>" depends=""/></td>
			</tr>
			<tr>
				<th><spring:message code="word.userAddrCity"/></th>
				<td><input type="text" name="HOME_ADDR_CITY" id="HOME_ADDR_CITY" maxlength="20" title="<spring:message code="word.userAddrCity"/>" depends=""/></td>
				<th><spring:message code="word.userAddrFull"/></th>
				<td><input type="text" name="HOME_ADDR_FULL" id="HOME_ADDR_FULL" maxlength="100" style="width:300px"  title="<spring:message code="word.userAddrFull"/>" depends=""/></td>
			</tr>
			<tr>
				<th rowspan="2"><spring:message code="word.userCompAddr"/></th>
				<th><spring:message code="word.userAddrCountry"/></th>
				<td>
                   <select id="COMP_ADDR_COUNTRY" name="COMP_ADDR_COUNTRY" title="<spring:message code="word.userAddrCountry"/>" depends="" style="width:150px">
						<option value=""><spring:message code="word.select"/></option>
                        <c:forEach var="addrCountryList" items="${ds_addrCountryList}">
                            <option value="${addrCountryList.CD}">${addrCountryList.CD_NM}</option>
                        </c:forEach>
					</select>
				</td>
				<th><spring:message code="word.userAddrProvince"/></th>
				<td><input type="text" name="COMP_ADDR_PROVINCE" id="COMP_ADDR_PROVINCE" maxlength="20" title="<spring:message code="word.userAddrProvince"/>" depends=""/></td>
			</tr>
			<tr>
				<th><spring:message code="word.userAddrCity"/></th>
				<td><input type="text" name="COMP_ADDR_CITY" id="COMP_ADDR_CITY" maxlength="20" title="<spring:message code="word.userAddrCity"/>" depends=""/></td>
				<th><spring:message code="word.userAddrFull"/></th>
				<td><input type="text" name="COMP_ADDR_FULL" id="COMP_ADDR_FULL" maxlength="100" style="width:300px"  title="<spring:message code="word.userAddrFull"/>" depends=""/></td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.useLanguageCd"/></th>
				<td colspan="3">
					<select id="USE_LANGUAGE_CD" name="USE_LANGUAGE_CD" title="<spring:message code="word.useLanguageCd"/>" depends="required" style="width:150px">
						<option value=""><spring:message code="word.select"/></option>
                        <c:forEach var="useLanguageList" items="${ds_useLanguageList}">
                            <option value="${useLanguageList.CD}">${useLanguageList.CD_NM}</option>
                        </c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.snsType"/></th>
				<td>
					<select id="SNS_TYPE" name=""SNS_TYPE"" title="<spring:message code="word.snsType"/>" depends="" style="width:150px">
						<option value=""><spring:message code="word.select"/></option>
                        <c:forEach var="snsTypeList" items="${ds_snsTypeList}">
                            <option value="${snsTypeList.CD}">${snsTypeList.CD_NM}</option>
                        </c:forEach>
					</select>
				</td>
				<th><spring:message code="word.snsId"/></th>
				<td><input type="text" name="SNS_ID" id="SNS_ID" maxlength="20" title="<spring:message code="word.snsId"/>" depends="englishNumeric"/></td>
			</tr>
			<tr>
				<th><spring:message code="word.phone"/></th>
				<th><spring:message code="word.countryNo"/></th>
				<td><input type="text" name="PHONE_COUNTRY_NO" id="PHONE_COUNTRY_NO" maxlength="3" style="width:50px" title="<spring:message code="word.countryNo"/>" depends="required,numeric"/></td>
				<th><spring:message code="cop.mbtlNum"/></th>
				<td><input type="text" name="PHONE_NO" id="PHONE_NO" maxlength="11" style="width:100px" title="<spring:message code="cop.mbtlNum"/>" depends="required,numeric"/> <spring:message code="info.tel.msg"/></td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="cop.emailAdres"/></th>
				<td colspan="3"><input type="text" name="EMAIL_ID" id="EMAIL_ID" maxlength="30" style="width:200px" title="<spring:message code="cop.emailAdres"/>" depends="required,email"/></td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.bankNm"/></th>
				<td colspan="3"><input type="text" name="BANK_NAME" id="BANK_NAME" maxlength="80" style="width:500px" title="<spring:message code="word.bankNm"/>" depends=""/></td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.accountNo"/></th>
				<td colspan="3"><input type="text" name="ACCOUNT_NO" id="ACCOUNT_NO" maxlength="20" style="width:150px" title="<spring:message code="word.accountNo"/>" depends="englishNumeric"/></td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.recommenderId"/></th>
				<td colspan="3"><input type="text" name="RECOMMENDER_ID" id="RECOMMENDER_ID" maxlength="20" title="<spring:message code="word.recommenderId"/>" depends="englishNumeric"/></td>
			</tr>
		<c:choose>
			<c:when test="${MODE=='DETAIL'}">
			<tr>
				<th colspan="2"><spring:message code="word.lastOrderDate"/></th>
				<td colspan="3"><input type="text" name="LAST_ORDER_DATE" id="LAST_ORDER_DATE" title="<spring:message code="word.lastOrderDate"/>" disabled/></td>
			</tr>
			</c:when>
		</c:choose>
		</table>
		</form>
		
		<div class="btn_zone">
		<c:choose>
			<c:when test="${SELF_SEARCH!='Y'}">
			<div class="left"><span id="preLink" onClick="javascript:fn_goBack()" title="<spring:message code="button.list"/>"></span></div>
			</c:when>
		</c:choose>
		<c:choose>
			<c:when test="${MODE=='DETAIL' && SELF_SEARCH!='Y'}">
			<div class="right"><button type="button" id="btnW_delete" onClick="javascript:goDelete()"><spring:message code="button.delete"/></button></div>
			</c:when>
			<c:when test="${MODE=='CREATE'}">
			<div class="right"><button type="button" id="btnW_clear" onClick="javascript:fn_clearData()"><spring:message code="button.clear"/></button></div>
			</c:when>
		</c:choose>
			<div class="right"><button type="button" id="btnW_save" onClick="javascript:fn_goSave()"><spring:message code="button.save"/></button></div>
		</div>

	</div> 
</div>


<!--  footer -->
<%@ include file="/layout/footer.jsp"%>
<!--  //footer -->
	
</div>
<!-- wrap -->
</body>
</html>