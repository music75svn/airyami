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
	inputParam.sid 				= "getCompanyDetail";
	inputParam.url 				= "/user/getCompanyDetail.do";
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
	if(sid == "getCompanyDetail"){
		gfn_setDetails(result.ds_detail, $("#contents"));	// 지역내 상세 내용 셋업
	}else if(sid = "saveCompany"){
		// 저장
		alert("<spring:message code="success.request.msg"/>");
		
		var inputParam = gfn_makeInputData($("#findForm"));
		gfn_commonGo("/user/companyList", inputParam, "N");
	}
}

////////////////////////////////////////////////////////////////////////////////////
// Click evnet
// 저장버튼클릭
function fn_goSave(){
	
	if(!gfn_validationForm($("#dataForm"))){
		return;
	}
	
	// 법인일 경우 법인등록번호, 법인명 필수체크
	if($('#BIZ_TYPE').val() == "2"){
		if($('#BIZ_LICENSE_NO').val() == ""){
			alert("<spring:message code="word.bizLicenseNo"/><spring:message code="common.required.msg"/>");
			$('#BIZ_LICENSE_NO').focus();
			return;
		}
		if($('#COMP_NM').val() == ""){
			alert("<spring:message code="word.compNm"/><spring:message code="common.required.msg"/>");
			$('#COMP_NM').focus();
			return;
		}
	}
	
	if(!confirm("<spring:message code="common.save.msg"/>")){
		return false;
	}
	
	var inputParam = gfn_makeInputData($("#dataForm"));
	inputParam.sid 				= "saveCompany";
	inputParam.url 				= "/user/saveCompany.do";
	
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
	inputParam.sid 				= "saveCompany";
	inputParam.url 				= "/user/saveCompany.do";
	$('#PROC_MODE').val("DELETE");
	inputParam.data 			= gfn_makeInputData($("#dataForm"));
	
	gfn_Transaction( inputParam );
}

//목록 버튼
function fn_goBack(){
	var inputParam = gfn_makeInputData($("#findForm"));
	
	gfn_commonGo("/user/companyList", inputParam, "N");
}

////////////////////////////////////////////////////////////////////////////////////
// 팝업 호출
function go_UserPop(){
	var inputParam				= {};
	inputParam.POP_USER_NM 	= $('#COMP_CEO_NM').val();
	inputParam.sid 	= "findUser";

	gfn_commonGo("/user/userFindListPop", inputParam, "Y");
}

//팝업콜백 함수
function fn_popCallBack(sid, data){
	// fn_srch
	if(sid == "findUser"){
		$('#COMP_CEO_ID').val(data.USER_ID);
		$('#COMP_CEO_NM').val(data.USER_NM);
	}
}
////////////////////////////////////////////////////////////////////////////////////
// 기타 기능 함수
// 팝업 내용 변경시 초기화
function fn_userNmChange() {
    $('#COMP_CEO_ID').val('');
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
		<h3><spring:message code="word.companyDetail"/></h3>
		<form id="findForm" name="findForm">
			<ppe:makeHidden var="${findParams}" filter="FIND_"/>
		</form>
		<form id="dataForm" name="dataForm" method="post" action="#" onsubmit="return false;">
			<input type="hidden" name="SEARCH_BIZ_ENTITY_ID" id="SEARCH_BIZ_ENTITY_ID" value='<c:out value="${BIZ_ENTITY_ID}"/>'/>
			<input type="hidden" name="MODE" id="MODE" value='<c:out value="${MODE}"/>'/>
			<input type="hidden" name="PROC_MODE" id="PROC_MODE" value=''/>
		<table summary="<spring:message code="word.companyDetail"/>" cellspacing="0" border="0" class="tbl_list_type2">
			<colgroup>
			<col width="12%">
			<col width="15%">
			<col width="20%">
			<col width="15%">
			<col width="38%">
			</colgroup>
			<tr>
				<th colspan="2"><spring:message code="word.bizEntityId"/></th>
			<c:choose>
				<c:when test="${MODE=='DETAIL'}">
				<td colspan="3"><input type="text" name="BIZ_ENTITY_ID" id="BIZ_ENTITY_ID" disabled /></td>
				</c:when>
				<c:when test="${MODE=='CREATE'}">
				<td colspan="3">시스템 자동생성</td>
				</c:when>
			</c:choose>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.bizEntityType"/></th>
				<td colspan="3">
					<select id="BIZ_ENTITY_TYPE" name="BIZ_ENTITY_TYPE" title="<spring:message code="word.bizEntityType"/>" depends="required" style="width:150px">
						<option value=""><spring:message code="word.select"/></option>
                        <c:forEach var="bizEntityTypeList" items="${ds_bizEntityTypeList}">
                            <option value="${bizEntityTypeList.CD}">${bizEntityTypeList.CD_NM}</option>
                        </c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.bizType"/></th>
				<td colspan="3">
					<select id="BIZ_TYPE" name="BIZ_TYPE" title="<spring:message code="word.bizType"/>" depends="required" style="width:150px">
						<option value=""><spring:message code="word.select"/></option>
                        <c:forEach var="bizTypeList" items="${ds_bizTypeList}">
                            <option value="${bizTypeList.CD}">${bizTypeList.CD_NM}</option>
                        </c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.bizLicenseNo"/></th>
				<td colspan="3"><input type="text" name="BIZ_LICENSE_NO" id="BIZ_LICENSE_NO" maxlength="20" title="<spring:message code="word.bizLicenseNo"/>" depends="numeric"/></td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.compNm"/></th>
				<td colspan="3"><input type="text" name="COMP_NM" id="COMP_NM" maxlength="20" title="<spring:message code="word.compNm"/>" depends="required"/></td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.compCeoId"/></th>
				<td colspan="3">
					<input type="text" name="COMP_CEO_NM" id="COMP_CEO_NM" maxlength="20" title="<spring:message code="word.compCeoId"/>" depends="" onChange="fn_userNmChange();"/>
					<button type="button" id="btnW_userPop" onClick="javascript:go_UserPop()"><spring:message code="button.search"/></button>
					<input type="text" name="COMP_CEO_ID" id="COMP_CEO_ID" maxlength="20" title="<spring:message code="word.compCeoId"/>" depends="required" readOnly/>
				</td>
			</tr>
			<tr>
				<th rowspan="2"><spring:message code="cop.adres"/></th>
				<th><spring:message code="word.userAddrCountry"/></th>
				<td>
                   <select id="ADDR_COUNTRY" name="ADDR_COUNTRY" title="<spring:message code="word.userAddrCountry"/>" depends="" style="width:150px">
						<option value=""><spring:message code="word.select"/></option>
                        <c:forEach var="addrCountryList" items="${ds_addrCountryList}">
                            <option value="${addrCountryList.CD}">${addrCountryList.CD_NM}</option>
                        </c:forEach>
					</select>
				</td>
				<th><spring:message code="word.userAddrProvince"/></th>
				<td><input type="text" name="ADDR_PROVINCE" id="ADDR_PROVINCE" maxlength="20" title="<spring:message code="word.userAddrProvince"/>" depends=""/></td>
			</tr>
			<tr>
				<th><spring:message code="word.userAddrCity"/></th>
				<td><input type="text" name="ADDR_CITY" id="ADDR_CITY" maxlength="20" title="<spring:message code="word.userAddrCity"/>" depends=""/></td>
				<th><spring:message code="word.userAddrFull"/></th>
				<td><input type="text" name="ADDR_FULL" id="ADDR_FULL" maxlength="100" style="width:300px"  title="<spring:message code="word.userAddrFull"/>" depends=""/></td>
			</tr>
			<tr>
				<th rowspan="3"><spring:message code="word.phone"/></th>
				<th><spring:message code="word.countryNo"/></th>
				<td><input type="text" name="PHONE_COUNTRY_NO" id="PHONE_COUNTRY_NO" maxlength="3" style="width:50px" title="<spring:message code="word.countryNo"/>" depends="required,numeric"/></td>
				<th><spring:message code="cop.telNo"/></th>
				<td><input type="text" name="PHONE_NO" id="PHONE_NO" maxlength="11" style="width:100px" title="<spring:message code="cop.mbtlNum"/>" depends="required,numeric"/> <spring:message code="info.tel.msg"/></td>
			</tr>
			<tr>
				<th><spring:message code="cop.telNo"/></th>
				<td colspan="3"><input type="text" name="PHONE_NO" id="PHONE_NO" maxlength="11" style="width:100px" title="<spring:message code="cop.telNo"/>" depends="required,numeric"/> <spring:message code="info.tel.msg"/></td>
			</tr>
			<tr>
				<th><spring:message code="cop.mbtlNum"/></th>
				<td colspan="3"><input type="text" name="SMART_PHONE_NO" id="SMART_PHONE_NO" maxlength="11" style="width:100px" title="<spring:message code="cop.mbtlNum"/>" depends="required,numeric"/> <spring:message code="info.tel.msg"/></td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="cop.emailAdres"/></th>
				<td colspan="3"><input type="text" name="EMAIL_ID" id="EMAIL_ID" maxlength="30" style="width:200px" title="<spring:message code="cop.emailAdres"/>" depends="required,email"/></td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.snsType"/></th>
				<td>
					<select id="SNS_TYPE" name="SNS_TYPE" title="<spring:message code="word.snsType"/>" depends="" style="width:150px">
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
				<th colspan="2"><spring:message code="word.validYn"/></th>
				<td colspan="3">
					<select id="VALID_YN" name="VALID_YN" title="<spring:message code="word.validYn"/>" depends="required" style="width:150px">
						<option value=""><spring:message code="word.select"/></option>
						<option value="Y">Y</option>
						<option value="N">N</option>
					</select>
				</td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.expiredDate"/></th>
				<td colspan="3">
					<input type="text" style="width:100px" maxlength="10" readonly name="EXPIRED_DATE" id="EXPIRED_DATE" isDate="Y" class="datepicker" title="<spring:message code="word.expiredDate"/>" depends="">
				</td>
			</tr>
		</table>
		</form>
		
		<div class="btn_zone">
			<div class="left"><span id="preLink" onClick="javascript:fn_goBack()" title="<spring:message code="button.list"/>"></span></div>
		<c:choose>
			<c:when test="${MODE=='DETAIL'}">
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