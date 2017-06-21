<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="egovframework.airyami.cmm.util.*"%>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html;application/json; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<%@ include file="/include/title.jsp"%>
<%@ include file="/include/shop_standard.jsp"%>

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
	
	// 파라미터 validation
	if(!gfn_validationForm($("#srchForm"))){
		return;
	}
	
	var inputParam = new Object();
	inputParam.sid 				= "cartList";
	inputParam.url 				= "/shop/selectShopCartList.do";
	inputParam.data 			= gfn_makeInputData($("#srchForm"));
	//inputParam.callback			= fn_callBackA;
	
	gfn_Transaction( inputParam );
}

// 장바구니 삭제
function fn_goDelete(){
	var checkInfo = gfn_getCheckOk("tb_list");
	
	if(gfn_isNull(checkInfo))
		return;
	
	if(!confirm("<spring:message code="common.delete.msg"/>")){
		return false;
	}
	
	var inputParam = new Object();
	inputParam.sid 				= "cartDelete";
	inputParam.url 				= "/shop/deleteCart.do";
	inputParam.data 			= { "PR_NO"  : checkInfo};
	
	gfn_Transaction( inputParam );
}

// 구매
function fn_goPurchase(){
	var checkInfo = gfn_getCheckOk("tb_list");
	
	if(gfn_isNull(checkInfo))
		return;
	
	if(!gfn_validationForm($("#dataForm"))){
		return;
	}
	
	if(!confirm("<spring:message code="common.purchase.msg"/>")){
		return false;
	}
	
	var inputParam = new Object();
	inputParam.sid 				= "purchase";
	inputParam.url 				= "/shop/savePurchase.do";
	inputParam.data 			= gfn_makeInputData($("#dataForm"));
	inputParam.data.prods		= { "PR_NO"  : checkInfo};
	
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
	if(sid == "cartList"){
		var tbHiddenInfo = ["PR_NO"]; // row에 추가할 히든 컬럼 설정  없으면 삭제
		
		gfn_displayList(result.ds_list, "tb_list", tbHiddenInfo);
		gfn_displayTotCnt(result.totCnt);
		
		gfn_addPaging(result.pageInfo, 'gfn_clickPageNo');
		
		//gfn_addRowClickEvent("tb_list", "fn_clickRow"); // ==>동일하다
	}
	
	// fn_srch
	if(sid == "cartDelete"){
		alert("<spring:message code="success.request.msg"/>");
		fn_srch();
	}
	if(sid == "purchase"){
		alert("<spring:message code="success.request.msg"/>");
		fn_srch();
	}
}

////////////////////////////////////////////////////////////////////////////////////
// Click evnet
function fn_preShipAddr(){
	$('#SHIP_TO_COUNTRY').attr("disabled", true);
	$('#SHIP_TO_PROVINCE').attr("disabled", true);
	$('#SHIP_TO_CITY').attr("disabled", true);
	$('#SHIP_TO_ADDRESS').attr("disabled", true);
	$('#SHIP_TO_COUNTRY').val('<c:out value="${ds_preShipAddrInfo.SHIP_TO_COUNTRY}"/>');
	$('#SHIP_TO_PROVINCE').val('<c:out value="${ds_preShipAddrInfo.SHIP_TO_PROVINCE}"/>');
	$('#SHIP_TO_CITY').val('<c:out value="${ds_preShipAddrInfo.SHIP_TO_CITY}"/>');
	$('#SHIP_TO_ADDRESS').val('<c:out value="${ds_preShipAddrInfo.SHIP_TO_ADDRESS}"/>');
	$('#SHIP_TO_SEQ').val('');
}

function fn_directShipAddr(){
	$('#SHIP_TO_COUNTRY').attr("disabled", false);
	$('#SHIP_TO_PROVINCE').attr("disabled", false);
	$('#SHIP_TO_CITY').attr("disabled", false);
	$('#SHIP_TO_ADDRESS').attr("disabled", false);
	$('#SHIP_TO_COUNTRY').val('');
	$('#SHIP_TO_PROVINCE').val('');
	$('#SHIP_TO_CITY').val('');
	$('#SHIP_TO_ADDRESS').val('');
	$('#SHIP_TO_SEQ').val('');
}
////////////////////////////////////////////////////////////////////////////////////
// 팝업 호출

////////////////////////////////////////////////////////////////////////////////////
</script>

</head>
<body>
<!-- wrap -->
<div id="wrap">

<!--  header -->
<%@ include file="/layout/shop/header.jsp"%>
<!--//  header --> 

<!-- container -->
<div id="container">
	<!-- menu -->
<%@ include file="/layout/shop/menu_left.jsp"%>
	<!--// menu -->
  
	<div id="contents">
		<h3><spring:message code="word.cart"/></h3>
		<form id="myParams" name="myParams">
			<ppe:makeHidden var="${findParams}" filter="FIND_" />
		</form>
		<form id="srchForm" name="srchForm" method="post" action="<c:url value='/commCode/codeList.do'/>" onsubmit="return false;">
			<input type="hidden" name="pageNo" id="pageNo" value="1"/>
			<input type="hidden" name="EXCEL_YN" id="EXCEL_YN" />
			<input type="hidden" name="SORT_COL" id="SORT_COL" value="INSERT_DT DESC"/>
			<input type="hidden" name="SORT_ACC" id="SORT_ACC" />
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
		<table id="tb_list" summary="<spring:message code="button.cart"/>">
			<caption>리스트</caption>
			<colgroup>
				<col width="5%"/>
				<col width="15%"/>
				<col width=""/>
				<col width="15%"/>
				<col width="10%"/>
				<col width="15%"/>
			</colgroup>
			<thead>
				<tr>
					<th cid="CK" alg="center"><input type="checkbox" name="check"  onclick="javascript:gfn_checkAll('tb_list', this);"/></th>
					<th cid="PROD_NO" alg="center"><spring:message code="word.prodNo"/></th>
					<th cid="PROD_NM" alg="left"><spring:message code="word.prodNm"/></th>
					<th cid="ORG_PRICE" alg="right"><spring:message code="word.orgPrice"/></th>
					<th cid="PR_QTY" alg="right"><spring:message code="word.qty"/></th>
					<th cid="INSERT_DT" alg="center"><spring:message code="sts.regDate"/></th>
				</tr> 
			</thead> 
			<tbody>
				<tr></tr>
			</tbody>
		</table>
		</div>
		<!--// table list -->
		<span id="pagingNav"></span>
		<form id="dataForm" name="dataForm" method="post" action="#" onsubmit="return false;">
		<input type="hidden" name="SHIP_TO_SEQ" id="SHIP_TO_SEQ" />
		<table summary="<spring:message code="word.userDetail"/>" cellspacing="0" border="0" class="tbl_list_type2">
			<colgroup>
			<col width="12%">
			<col width="15%">
			<col width="20%">
			<col width="15%">
			<col width="38%">
			</colgroup>
			<tr>
				<th colspan="2"><spring:message code="word.shipAddrSel"/></th>
				<td colspan="3">
					<button type="button" id="btnPreShipAddr" onClick="javascript:fn_preShipAddr()"><spring:message code="word.shipAddrPre"/></button>
					<button type="button" id="btnShipAddrPopList" onClick="javascript:fn_goPurchase()"><spring:message code="word.shipAddrList"/></button>
					<button type="button" id="btnShipAddrPopList" onClick="javascript:fn_directShipAddr()"><spring:message code="word.shipAddrDirect"/></button>
				</td>
			</tr>
			<tr>
				<th rowspan="2"><spring:message code="word.shipAddr"/></th>
				<th><spring:message code="word.userAddrCountry"/></th>
				<td>
                   <select id="SHIP_TO_COUNTRY" name="SHIP_TO_COUNTRY" title="<spring:message code="word.userAddrCountry"/>" depends="required" style="width:150px">
						<option value=""><spring:message code="word.select"/></option>
                        <c:forEach var="addrCountryList" items="${ds_addrCountryList}">
                            <option value="${addrCountryList.CD}">${addrCountryList.CD_NM}</option>
                        </c:forEach>
					</select>
				</td>
				<th><spring:message code="word.userAddrProvince"/></th>
				<td><input type="text" name="SHIP_TO_PROVINCE" id="SHIP_TO_PROVINCE" maxlength="20" title="<spring:message code="word.userAddrProvince"/>" depends=""/></td>
			</tr>
			<tr>
				<th><spring:message code="word.userAddrCity"/></th>
				<td><input type="text" name="SHIP_TO_CITY" id="SHIP_TO_CITY" maxlength="20" title="<spring:message code="word.userAddrCity"/>" depends=""/></td>
				<th><spring:message code="word.userAddrFull"/></th>
				<td><input type="text" name="SHIP_TO_ADDRESS" id="SHIP_TO_ADDRESS" maxlength="100" style="width:300px"  title="<spring:message code="word.userAddrFull"/>" depends="required"/></td>
			</tr>
			<tr>
				<th><spring:message code="word.phone"/></th>
				<th><spring:message code="word.countryNo"/></th>
				<td><input type="text" name="PHONE_COUNTRY_NO" id="PHONE_COUNTRY_NO" value="<c:out value="${ds_userInfo.PHONE_COUNTRY_NO}"/>" maxlength="3" style="width:50px" title="<spring:message code="word.countryNo"/>" depends="required,numeric"/></td>
				<th><spring:message code="cop.mbtlNum"/></th>
				<td><input type="text" name="PHONE_NO" id="PHONE_NO" value="<c:out value="${ds_userInfo.PHONE_NO}"/>" maxlength="11" style="width:100px" title="<spring:message code="cop.mbtlNum"/>" depends="required,numeric"/> <spring:message code="info.tel.msg"/></td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.paymentMethod"/></th>
				<td colspan="3">
                   <select id="PAYMENT_TERMS" name="PAYMENT_TERMS" title="<spring:message code="word.paymentMethod"/>" depends="required" style="width:250px">
						<option value=""><spring:message code="word.select"/></option>
                        <c:forEach var="paymentMethodList" items="${ds_paymentMethodList}">
                            <option value="${paymentMethodList.CD}">${paymentMethodList.CD_NM}</option>
                        </c:forEach>
					</select>
				</td>
			</tr>
		</table>
		</form>
		<div class="btn_zone">
			<button type="button" id="btnW_delete" onClick="javascript:fn_goDelete()"><spring:message code="button.delete"/></button>
			<button type="button" id="btnReg" onClick="javascript:fn_goPurchase()"><spring:message code="button.purchase"/></button>
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