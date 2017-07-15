<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html;application/json; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<%@ include file="/include/title.jsp"%>
<%@ include file="/include/admin_standard.jsp"%>
<jsp:include page="/include/common.jsp"/>

<script type="text/javascript">
var onload = true;
$(function() {  //onready
	//여기에 최초 실행될 자바스크립트 코드를 넣어주세요
	//alert("LOGIN_ID " + '<c:out value="${LOGIN_ID}"/>');
	
	gfn_OnLoad();
	
	fn_init();
	
	fn_srch();
});

// 화면내 초기화 부분
function fn_init(){
	// 그리드에 소트 기능 추가한다.
	gfn_addGridSort("tb_list");
	
	// myParams 에 넘어온 값이 있으면 이전 검색조건 셋팅한다.
	gfn_setMyParams();
}

////////////////////////////////////////////////////////////////////////////////////
// 호출부분 정의
// 리스트 조회
function fn_srch(){
	if(gfn_isNull($("#SEARCH_BIZ_ENTITY_ID").val()))
		return;
	
	var inputParam = new Object();
	inputParam.sid 				= "accountList";
	inputParam.url 				= "/account/selectAccountList.do";
	inputParam.data 			= gfn_makeInputData($("#srchForm"));
	//inputParam.callback			= fn_callBackA;
	gfn_Transaction( inputParam );
}

//저장
function fn_save(){
	// 필수체크
	if(!gfn_validationForm($("#dataForm"))){
		return;
	}
	
	if(!confirm("<spring:message code="common.save.msg"/>")){
		return false;
	}
	
	var inputParam = new Object();
	inputParam.sid 				= "saveAccount";
	inputParam.url 				= "/account/saveAccount.do";
	inputParam.data 			= gfn_makeInputData($("#dataForm"));
	
	gfn_Transaction( inputParam );
}

//저장
function fn_delete(){
	if($("#PROC_MODE").val() != 'UPDATE'){
		alert("<spring:message code="common.rowClickReq.msg"/>");
		return false;
	}
	
	if(!confirm("<spring:message code="common.delete.msg"/>")){
		return false;
	}
	
	$("#PROC_MODE").val("DELETE");
	
	var inputParam = new Object();
	inputParam.sid 				= "deleteAccount";
	inputParam.url 				= "/account/saveAccount.do";
	inputParam.data 			= gfn_makeInputData($("#dataForm"));
	
	
	gfn_Transaction( inputParam );
}

////////////////////////////////////////////////////////////////////////////////////
// 콜백 함수
function fn_callBack(sid, result){
	//debugger;
	
	if (!result.success) {
		alert(result.msg);
		return;
	}
	
	
	// fn_srch
	if(sid == "accountList"){
		var tbHiddenInfo = ["BANK_NM", "SEQ", "COUNTRY", "ACCT_NO", "CURRENCY", "VALID_YN", "IMG_FILE"]; // row에 추가할 히든 컬럼 설정  없으면 삭제
		
		gfn_displayList(result.ds_list, "tb_list", tbHiddenInfo);
		gfn_displayTotCnt(result.totCnt);
		
		gfn_addPaging(result.pageInfo, 'gfn_clickPageNo');
		gfn_addRowClickEvent("tb_list", "fn_clickRow"); // ==>동일하다
	} else if(sid == "saveAccount"){
		// 창을 닫던지. 부모 재조회를 하던지 
		alert('<spring:message code="success.common.save" />');
		fn_new();
		fn_srch();
	} else if(sid == "deleteAccount"){
		// 창을 닫던지. 부모 재조회를 하던지 
		alert('<spring:message code="success.common.delete" />');
		fn_new();
		fn_srch();
	}
	
	// fn_srch
	if(sid == "delCd"){
		alert(sid);
	}
	
}

//row click event
function fn_clickRow(rowObj){
	var BANK_NM = $('input[name=BANK_NM]', rowObj).val();
	var SEQ = $('input[name=SEQ]', rowObj).val();
	var COUNTRY = $('input[name=COUNTRY]', rowObj).val();
	var ACCT_NO = $('input[name=ACCT_NO]', rowObj).val();
	var CURRENCY = $('input[name=CURRENCY]', rowObj).val();
	var VALID_YN = $('input[name=VALID_YN]', rowObj).val();
	var IMG_FILE = $('input[name=IMG_FILE]', rowObj).val();
	
	$("#BANK_NM").val(BANK_NM);
	$("#SEQ").val(SEQ);
	$("#COUNTRY").val(COUNTRY);
	$("#ACCT_NO").val(ACCT_NO);
	$("#CURRENCY").val(CURRENCY);
	$("#VALID_YN").val(VALID_YN);
	
	$("#PROC_MODE").val("UPDATE");
}

function fn_new(){
	$("#BANK_NM").val("");
	$("#SEQ").val("");
	$("#COUNTRY").val("");
	$("#ACCT_NO").val("");
	$("#CURRENCY").val("");
	$("#VALID_YN").val("");
	
	$("#PROC_MODE").val("CREATE");
}

</script>
</head>
<body class="popup">
<h1><spring:message code="button.companyAccountAdm"/>&nbsp;<a onClick="self.close();" class="close">X</a></h1>
	<div class="content">
		<form id="myParams" name="myParams">
			<ppe:makeHidden var="${findParams}" filter="FIND_" />
		</form>
		<form id="srchForm" name="srchForm" method="post" action="<c:url value='/commCode/codeList.do'/>" onsubmit="return false;">
			<input type="hidden" name="pageNo" id="pageNo" value="1"/>
			<input type="hidden" name="EXCEL_YN" id="EXCEL_YN" />
			<input type="hidden" name="SORT_COL" id="SORT_COL" value="A.INSERT_DT DESC"/>
			<input type="hidden" name="SORT_ACC" id="SORT_ACC" />
			<input type="hidden" name="SEARCH_POP_YN" id="Y" />
			<input type="hidden" name="SEARCH_BIZ_ENTITY_ID" id="SEARCH_BIZ_ENTITY_ID" value='<c:out value="${BIZ_ENTITY_ID}"/>'/>
			<input type="hidden" name="sid" id="sid" value='<c:out value="${sid}"/>'/>
		<div class="tableInfoArea">
			<!-- total 총건수 -->
	        <span id="totCnt"></span>
		    <!--// total 총건수 -->
			<!-- 출력건수 -->
			<div class="tablelistQuantity" id="pageUnit"></div>
			<!--// 출력건수 -->
		</div>
		<!-- //출력건수및 리스트 페이지당 갯수 -->
		</form>
		
		<!-- table list -->
		<div class="aTypeListTbl">
		<table id="tb_list" summary="<spring:message code="word.productList"/>">
			<caption>리스트</caption>
			<colgroup>
				<col width="5%"/>
				<col width=""/>
				<col width="30%"/>
				<col width="25%"/>
				<col width="10%"/>
				<col width="10%"/>
			</colgroup>
			<thead>
				<tr>
					<th cid="ROWNUM" cClass="num" cType="NUM"><spring:message code="word.num"/></th>
					<th cid="BANK_NM" alg="left"><spring:message code="word.bankNm"/></th>
					<th cid="COUNTRY_NM" alg="left"><spring:message code="word.acctCountry"/></th>
					<th cid="ACCT_NO" alg="left"><spring:message code="word.acctNo"/></th>
					<th cid="CURRENCY_NM" alg="center"><spring:message code="word.currency"/></th>
					<th cid="VALID_YN" alg="center"><spring:message code="word.validYn"/></th>
				</tr> 
			</thead> 
			<tbody>
				<tr></tr>
			</tbody>
		</table>
		</div>
		<!--// table list -->
		<span id="pagingNav"></span>
		</form>
		<form id="dataForm" name="dataForm" method="post" action="<c:url value='/commCode/codeList.do'/>" onsubmit="return false;">
			<input type="hidden" name="BIZ_ENTITY_ID" id="BIZ_ENTITY_ID" value='<c:out value="${BIZ_ENTITY_ID}"/>'/>
			<input type="hidden" name="SEQ" id="SEQ" value=''/>
			<input type="hidden" name="PROC_MODE" id="PROC_MODE" value='CREATE'/>
		<table cellspacing="0" border="0" class="tbl_list_type2">
			<colgroup>
			<col width="30%">
			<col width="70%">
			</colgroup>
			<tr>
				<th><spring:message code="word.bankNm"/></th>
				<td>
			        <input type="text" name="BANK_NM" id="BANK_NM" maxlength="80" style="width:400px" title="<spring:message code="word.bankNm"/>" depends="required"/>
				</td>
			</tr>
			<tr>
				<th><spring:message code="word.acctCountry"/></th>
				<td>
					<select id="COUNTRY" name="COUNTRY" style="width:300px" title="<spring:message code="word.acctCountry"/>" depends="required">
						<option value=""><spring:message code="word.select"/></option>
                        <c:forEach var="addrCountryList" items="${ds_addrCountryList}">
                            <option value="${addrCountryList.CD}">${addrCountryList.CD_NM}</option>
                        </c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<th><spring:message code="word.acctNo"/></th>
				<td>
			        <input type="text" name="ACCT_NO" id="ACCT_NO" maxlength="20" style="width:300px" title="<spring:message code="word.acctNo"/>" depends="englishNumeric,required"/>
				</td>
			</tr>
			<tr>
				<th><spring:message code="word.currency"/></th>
				<td>
					<select id="CURRENCY" name="CURRENCY" style="width:100px" title="<spring:message code="word.currency"/>" depends="required">
						<option value=""><spring:message code="word.select"/></option>
                        <c:forEach var="currencyList" items="${ds_currencyList}">
                            <option value="${currencyList.CD}">${currencyList.CD_NM}</option>
                        </c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<th><spring:message code="word.validYn"/></th>
				<td>
			        <select id="VALID_YN" name="VALID_YN" style="width:100px" title="<spring:message code="word.validYn"/>" depends="required">
                        <option value="Y"><spring:message code="word.yes"/></option>
                        <option value="N"><spring:message code="word.no"/></option>
					</select>
				</td>
			</tr>
			<tr>
				<th><spring:message code="word.acctImgFile"/></th>
				<td>
			        <input type="file" name="IMG_FILE" id="IMG_FILE" style="width:500px" title="<spring:message code="word.acctImgFile"/>" depends=""/>
				</td>
			</tr>
		</table>
		</form>
		<div class="btn_zone">
			<button type="button" onClick="javascript:fn_new()"><spring:message code="button.new"/></button>
			<button type="button" onClick="javascript:fn_save()"><spring:message code="button.save"/></button>
			<button type="button" onClick="javascript:fn_delete()"><spring:message code="button.delete"/></button>
	    </div>
</body>