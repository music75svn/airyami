<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="egovframework.airyami.cmm.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html;application/json; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<%@ include file="/include/title.jsp"%>
<%@ include file="/include/shop_standard.jsp"%>
<jsp:include page="/include/common.jsp"/>

<script type="text/javascript">
$(function() {  //onready
	//여기에 최초 실행될 자바스크립트 코드를 넣어주세요
	gfn_OnLoad();
	
	fn_init();
	fn_srch();	// 상세조회
	
	dfn_init();
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
	inputParam.sid 				= "getProductDetail";
	inputParam.url 				= "/shop/getShopProductDetail.do";
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
	if(sid == "getProductDetail"){
		gfn_setDetails(result.ds_detail, $("#contents"));	// 지역내 상세 내용 셋업
		
		if(!gfn_isNull(result.ds_detail.PROD_MCATE_CD)){
			fn_selectLCate(result.ds_detail.PROD_LCATE_CD, result.ds_detail.PROD_MCATE_CD);
		}
		if(!gfn_isNull(result.ds_detail.PROD_SCATE_CD)){
			fn_selectMCate(result.ds_detail.PROD_MCATE_CD, result.ds_detail.PROD_SCATE_CD);
		}
		if(!gfn_isNull(result.ds_detail.PROD_DCATE_CD)){
			fn_selectSCate(result.ds_detail.PROD_SCATE_CD, result.ds_detail.PROD_DCATE_CD);
		}
		
		for(var i = 0; i < result.ds_langNameList.length; i++){
			$('#PROD_NM_'+result.ds_langNameList[i].LANG_CD).val(result.ds_langNameList[i].PROD_NM);
			$('#PROD_SHORT_NM_'+result.ds_langNameList[i].LANG_CD).val(result.ds_langNameList[i].PROD_SHORT_NM);
			$('#PRODUCT_EXPL_TEXT_'+result.ds_langNameList[i].LANG_CD).val(result.ds_langNameList[i].PRODUCT_EXPL_TEXT);
		}
		
		if(!gfn_isNull(result.ds_detail)){
			if(!gfn_isNull(result.ds_detail.fileList)){
				$("#IMG_FIRST_INDEX").val(0);
				$("#IMG_LIST_LENGTH").val(result.ds_detail.fileList.length);
				g_fileList = result.ds_detail.fileList;
				dfn_initFileList();
				dfn_setFileList(result.ds_detail.fileList);// fileList 셋업
			}
			
		}
	}else if(sid = "saveCart"){
		// 저장
		alert("<spring:message code="success.request.msg"/>");
		return;
	}
}

////////////////////////////////////////////////////////////////////////////////////
// Click evnet

////////////////////////////////////////////////////////////////////////////////////
// 팝업 호출

////////////////////////////////////////////////////////////////////////////////////
// 기타 기능 함수
// 장바구니버튼 클릭
function fn_goCart(){
	
	if(!gfn_validationForm($("#cartForm"))){
		return;
	}
	
	var inputParam = gfn_makeInputData($("#cartForm"));
	inputParam.sid 				= "saveCart";
	inputParam.url 				= "/shop/saveCart.do";
	
	inputParam.data 			= gfn_makeInputData($("#cartForm"));
	gfn_Transaction( inputParam );
}

function fn_clearData(){
	
	gfn_clearData($("#contents"));
}

////////////////////////////////////////////////////////////////////////////////////
//사용자 함수

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
		<h3><spring:message code="word.productDetail"/></h3>
		<form id="findForm" name="findForm">
			<ppe:makeHidden var="${findParams}" filter="FIND_"/>
		</form>
		<form id="dataForm" name="dataForm" method="post" action="#" onsubmit="return false;">
			<input type="hidden" name="SEARCH_PROD_NO" id="SEARCH_PROD_NO" value='<c:out value="${PROD_NO}"/>'/>
			<input type="hidden" name="MODE" id="MODE" value='<c:out value="${MODE}"/>'/>
			<input type="hidden" name="PROC_MODE" id="PROC_MODE" value=''/>
			<input type="hidden" name="IMG_FIRST_INDEX" id="IMG_FIRST_INDEX" value="0"/>
			<input type="hidden" name="IMG_LIST_LENGTH" id="IMG_LIST_LENGTH" value="0"/>
			<input type="hidden" name="IMG_LIST_SIZE" id="IMG_LIST_SIZE" value="4"/>
		<table summary="<spring:message code="word.productDetail"/>" cellspacing="0" border="0" class="tbl_list_type2">
			<colgroup>
			<col width="50%">
			<col width="3">
			<col width="10%">
			<col width="10%">
			<col width="30%">
			</colgroup>
			<tr>
				<th rowspan="50">
					<iframe width="400px" height="450px" src="/product/productImgView.do?PROD_NO=<c:out value="${PROD_NO}"/>" frameborder="0"></iframe>
				</th>
				<td rowspan="50"></td>
				<th colspan="2"><spring:message code="word.prodNo"/></th>
				<td>
					<input type="text" name="PROD_NO" id="PROD_NO" disabled />
				</td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.brand"/></th>
				<td>
			        <select id="BRAND_CD" name="BRAND_CD" title="<spring:message code="word.brand"/>" depends="required" style="width:150px">
						<option value=""><spring:message code="word.select"/></option>
                        <c:forEach var="brandList" items="${ds_brandList}">
                            <option value="${brandList.CD}">${brandList.CD_NM}</option>
                        </c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<th colspan="2"><spring:message code="word.orgPrice"/></th>
				<td>
			        <input type="text" name="ORG_PRICE" id="ORG_PRICE" isNum="Y" class="onlynum2" maxlength="12" style="width:80px" title="<spring:message code="word.orgPrice"/>" depends="required"/>
			        <select id="SUPPLY_CURRENCY" name="SUPPLY_CURRENCY" title="<spring:message code="word.supplyCurrency"/>" depends="required" style="width:100px">
						<option value=""><spring:message code="word.select"/></option>
                        <c:forEach var="supplyCurrencyList" items="${ds_supplyCurrencyList}">
                            <option value="${supplyCurrencyList.CD}">${supplyCurrencyList.CD_NM}</option>
                        </c:forEach>
					</select>
				</td>
			</tr>
		</table>
		</form>
		
		<div class="btn_zone">
			<div class="right">
			<form id="cartForm" name="cartForm" method="post" onsubmit="return false;">
				<input type="hidden" name="SEARCH_PROD_NO" id="SEARCH_PROD_NO" value='<c:out value="${PROD_NO}"/>'/>
				<input type="text" name="PR_QTY" id="PR_QTY" isNum="Y" value="1" class="onlynum2" maxlength="4" style="width:80px" title="<spring:message code="word.qty"/>" depends="required"/>
				<button type="button" id="btnW_cart" onClick="javascript:fn_goCart()"><spring:message code="button.cart"/>
			</form>
			</button></div>
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