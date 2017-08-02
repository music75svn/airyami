<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="egovframework.airyami.cmm.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html;application/json; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<%@ include file="/include/shop_standard.jsp"%>
<%@ include file="/include/title.jsp"%>
<jsp:include page="/include/common.jsp"/>

<script type="text/javascript">
$(function() {  //onready
   	$('.bxslider').bxSlider({
	  	pagerCustom: '#bx-pager'
	});
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
function fn_callBack(sid, result){
	if (!result.success) {
		alert(result.msg);
		return;
	}
	
	// fn_srch
	if(sid == "getProductDetail"){
		$("#CATEGORY_NM_DIV").html(result.ds_detail.CATEGORY_NM);
		$("#PROD_NO_DIV").html(result.ds_detail.PROD_NO);
		$("#PROD_NM_DIV").html(result.ds_detail.PROD_NM);
		$("#PRODUCT_EXPL_TEXT_DIV").html(result.ds_detail.PRODUCT_EXPL_TEXT);
		$("#ORIGINAL_PRICE_DIV").html(result.ds_detail.ORG_PRICE+result.ds_detail.SUPPLY_CURRENCY_NM);
		$("#SALE_PRICE_DIV").html(result.ds_detail.SALE_PRICE+result.ds_detail.SUPPLY_CURRENCY_NM);
		$("#TOTAL_PRICE_DIV").html(result.ds_detail.SALE_PRICE+result.ds_detail.SUPPLY_CURRENCY_NM);
		$("#SALE_PRICE").val(result.ds_detail.SALE_PRICE);
		$("#CURRENCY").val(result.ds_detail.SUPPLY_CURRENCY_NM);
		
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
	
	if(!gfn_validationForm($("#dataForm"))){
		return;
	}
	
	var inputParam = gfn_makeInputData($("#dataForm"));
	inputParam.sid 				= "saveCart";
	inputParam.url 				= "/shop/saveCart.do";
	
	inputParam.data 			= gfn_makeInputData($("#dataForm"));
	gfn_Transaction( inputParam );
}

function fn_clearData(){
	
	gfn_clearData($("#contents"));
}

////////////////////////////////////////////////////////////////////////////////////
//사용자 함수
function calTotal(){
	var totalPrice = $("#SALE_PRICE").val() * $("#PR_QTY").val();
	$("#TOTAL_PRICE_DIV").html(totalPrice+$("#CURRENCY").val());
}
////////////////////////////////////////////////////////////////////////////////////
</script>

</head>
<body>
<!-- wrap -->
<div id="wrap">

<!--  header -->
<%@ include file="/layout/shop_header.jsp"%>
<!--//  header --> 
<form id="dataForm" name="dataForm" method="post" action="#" onsubmit="return false;">
	<input type="hidden" name="SEARCH_PROD_NO" id="SEARCH_PROD_NO" value='<c:out value="${PROD_NO}"/>'/>
	<input type="hidden" name="PROD_NO" id="PROD_NO"/>
	<input type="hidden" name="MODE" id="MODE" value='<c:out value="${MODE}"/>'/>
	<input type="hidden" name="PROC_MODE" id="PROC_MODE" value=''/>
	<input type="hidden" name="SALE_PRICE" id="SALE_PRICE" value=''/>
	<input type="hidden" name="CURRENCY" id="CURRENCY" value=''/>
	<!-- container -->
	<div id="container">
	
		<!-- product -->
		<div class="product">
			<nav><span id="CATEGORY_NM_DIV"></span></nav>
			
			<!-- top -->
			<div class="top">
				<!-- left -->
				<div class="left">
					<iframe width="600px" height="480px" src="/product/productImgView.do?PROD_NO=<c:out value="${PROD_NO}"/>" frameborder="0"></iframe>
				</div>
				<!--// left -->
				
				<!-- right -->
				<div class="right">
					<h3 id="PROD_NM_DIV"></h3>
					<ul>
						<li class="m_pro"><img src="../img/product/detail_img01.jpg" /></li>
						<li>
							<span class="title">제품번호</span> 
							<span class="txt" id="PROD_NO_DIV"></span>
						</li>
						<li>
							<span class="title">수량</span> 
								<input type="text" name="PR_QTY" id="PR_QTY" isNum="Y" value="1" maxlength="4" style="width:80px" title="<spring:message code="word.qty"/>" depends="required" onChange="calTotal();"/>
						</li>
						<li>
							<span class="title">원가</span> 
							<span class="txt" id="ORIGINAL_PRICE_DIV"></span>
						</li>
						<li>
							<span class="title">판매가격</span> 
							<span class="txt" id="SALE_PRICE_DIV"></span>
						</li>
						<li class="total">
							<span class="title">TOTAL</span>
							<span class="txt" id="TOTAL_PRICE_DIV"></span>
						</li>
					</ul>
					
					<div class="btn_zone">
						<a href="javascript:fn_goCart()"><img src="../img/product/btn_basket.gif" alt="장바구니"></a>
					</div>
				</div>
				<!--// right -->
			</div>
			<!--// top -->
	
			
			
			<dl>
				<dt>제품정보</dt>
				<dd id="PRODUCT_EXPL_TEXT_DIV"></dd>
			</dl>
			
			
			<dl style="display:none">
				<dt>추천제품</dt>
				<dd>
					<ul>
						<li><a href="#"><img src="../img/product/product_img.jpg" alt=""><p><span>픽 퍼포먼스(맨) 픽 퍼포먼스(맨) 픽 퍼포먼스(맨) 픽 퍼포먼스(맨) 픽 퍼포먼스(맨) 픽 퍼포먼스(맨) 픽 퍼포먼스(맨) 픽 퍼포먼스(맨) 픽 퍼포먼스(맨) 픽 퍼포먼스(맨) </span>No.860 | 60캡슐</p></a></li>
						<li><a href="#"><img src="../img/product/product_img.jpg" alt=""><p><span>픽 퍼포먼스(맨)</span>No.860 | 60캡슐</p></a></li>
						<li><a href="#"><img src="../img/product/product_img.jpg" alt=""><p><span>픽 퍼포먼스(맨)</span>No.860 | 60캡슐</p></a></li>
						<li><a href="#"><img src="../img/product/product_img.jpg" alt=""><p><span>픽 퍼포먼스(맨)</span>No.860 | 60캡슐</p></a></li>
						<li><a href="#"><img src="../img/product/product_img.jpg" alt=""><p><span>픽 퍼포먼스(맨)</span>No.860 | 60캡슐</p></a></li>
						<li><a href="#"><img src="../img/product/product_img.jpg" alt=""><p><span>픽 퍼포먼스(맨)</span>No.860 | 60캡슐</p></a></li>
					</ul>
				</dd>
			</dl>
			
	
	
			
		</div>
		<!--// product -->
		
		
	</div>
	<!--// container -->
</div>
</form>

<!--  footer -->
<%@ include file="/layout/shop_footer.jsp"%>
<!--  //footer -->
	
</div>
<!-- wrap -->
</body>
</html>