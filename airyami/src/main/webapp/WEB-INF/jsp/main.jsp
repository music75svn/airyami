<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="egovframework.airyami.cmm.util.*"%>
<html lang="ko">
<head>

<%@ include file="/include/shop_standard.jsp"%>
<%@ include file="/include/title.jsp"%>

<script type="text/javascript">
var onload = true;
$(function() {  //onready
	//여기에 최초 실행될 자바스크립트 코드를 넣어주세요
	gfn_OnLoad(false);
	
	fn_init();
	
	//fn_srch();
});

// 화면내 초기화 부분
function fn_init(){
	// 메인 전시 조회
	var inputParam = new Object();
	inputParam.sid 				= "getMainDisplay";
	inputParam.url 				= "/main/getMainDisplay.do";
	inputParam.data 			= gfn_makeInputData($("#dataForm"));
	//inputParam.callback			= "fn_callBack";
	
	gfn_Transaction( inputParam );
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
	inputParam.sid 				= "codeList";
	inputParam.url 				= "/commCode/codeList.do";
	inputParam.data 			= $("#srchForm").serialize();
	//inputParam.callback			= fn_callBackA;
	
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
	if(sid == "codeList"){
		var tbHiddenInfo = ["GROUP_CODE", "CD","RULE_CD"]; // row에 추가할 히든 컬럼 설정  없으면 삭제
		
		gfn_displayList(result.ds_list, "tb_list", tbHiddenInfo);
		gfn_displayTotCnt(result.totCnt);
		
		gfn_addPaging(result.pageInfo, 'gfn_clickPageNo');
	}else if(sid == "getMainDisplay"){
		var recommandDIVStr = '';
		for(var i = 0; i < result.ds_recommandList.length; i++){
			recommandDIVStr += '<li class="item">';
			recommandDIVStr += '<span class="icon"><img src="../img/main/icon_recom02.png" alt="추천"></span>';
			recommandDIVStr += '<a href="/shop/shopProductDetail.do?PROD_NO='+result.ds_recommandList[i].PROD_NO+'">';
			recommandDIVStr += '	<p><img src="'+result.ds_recommandList[i].URL_PATH+result.ds_recommandList[i].SAVE_FILE_NAME+'" alt=""></p>';
			recommandDIVStr += '	<span>'+result.ds_recommandList[i].CATE_NAME+' <em>No.'+result.ds_recommandList[i].PROD_NO+'</em></span>';
			recommandDIVStr += '	<strong>'+result.ds_recommandList[i].PROD_NM+'</strong>';
			recommandDIVStr += '</a>';
			recommandDIVStr += '</li>';
		}
		
		$("#recommandDIV").html(recommandDIVStr);
		
		var newDIVStr = '';
		for(var i = 0; i < result.ds_newList.length; i++){
			if(i == 0){
				newDIVStr += '<li class="first">';
			}else{
				newDIVStr += '<li>';
			}
			newDIVStr += '<a href="/shop/shopProductDetail.do?PROD_NO='+result.ds_newList[i].PROD_NO+'">';
			newDIVStr += '	<p><img src="'+result.ds_newList[i].URL_PATH+result.ds_newList[i].SAVE_FILE_NAME+'" alt=""></p>';
			newDIVStr += '	<div>';
			newDIVStr += '		<span class="new_item">NEW ITEM</span>';
			newDIVStr += '		<span class="cate">'+result.ds_recommandList[i].CATE_NAME+' <em>No.'+result.ds_recommandList[i].PROD_NO+'</em></span>';
			newDIVStr += '		<strong class="title">'+result.ds_recommandList[i].PROD_NM+'</strong>';
			newDIVStr += '	</div>';
			newDIVStr += '</a>';
			newDIVStr += '</li>';
		}
		$("#newDIV").html(newDIVStr);
		
		var popularDIVStr = '';
		for(var i = 0; i < result.ds_popularList.length; i++){
			if(i == 0){
				popularDIVStr += '	<dt>';
				popularDIVStr += '		<a href="/shop/shopProductDetail.do?PROD_NO='+result.ds_popularList[i].PROD_NO+'">';
				popularDIVStr += '			<p class="photo">';
				popularDIVStr += '				<span><img src="../img/main/icon_top02.png" alt="인기상품"></span>';
				popularDIVStr += '				<img src="'+result.ds_popularList[i].URL_PATH+result.ds_popularList[i].SAVE_FILE_NAME+'" alt="">';
				popularDIVStr += '			</p>';
				popularDIVStr += '			<div class="desc">';
				popularDIVStr += '				<span class="red">인기 1위 상품</span>';
				popularDIVStr += '				<p class="txt">'+result.ds_popularList[i].PRODUCT_EXPL_TEXT+'</p>';
	
				popularDIVStr += '				<span class="cate">'+result.ds_popularList[i].CATE_NAME+' <em>No.'+result.ds_popularList[i].PROD_NO+'</em></span>';
				popularDIVStr += '				<strong class="title">'+result.ds_popularList[i].PROD_NM+'</strong>';
				popularDIVStr += '			</div>';
				popularDIVStr += '		</a>';
				popularDIVStr += '	</dt>';
			}else{
				if(i == 1){
					popularDIVStr += '<dd class="first">';
				}else{
					popularDIVStr += '<dd>';
				}
				popularDIVStr += '<a href="/shop/shopProductDetail.do?PROD_NO='+result.ds_popularList[i].PROD_NO+'">';
				popularDIVStr += '	<p><img src="'+result.ds_popularList[i].URL_PATH+result.ds_popularList[i].SAVE_FILE_NAME+'" alt=""></p>';
				popularDIVStr += '		<div>';
				popularDIVStr += '			<span class="cate">'+result.ds_popularList[i].CATE_NAME+' <em>No.'+result.ds_popularList[i].PROD_NO+'</em></span>';
				popularDIVStr += '			<strong class="title">'+result.ds_popularList[i].PROD_NM+'</strong>';
				popularDIVStr += '		</div>';
				popularDIVStr += '	</a>';
				popularDIVStr += '</dd>';
			}
		}
		$("#popularDIV").html(popularDIVStr);
		
	}
	
}


////////////////////////////////////////////////////////////////////////////////////
// Click evnet

////////////////////////////////////////////////////////////////////////////////////
// 팝업 호출

////////////////////////////////////////////////////////////////////////////////////
// 기타 기능 함수

////////////////////////////////////////////////////////////////////////////////////
</script>

</head>
<body class="main">
<!-- wrap -->
<div id="wrap">

<!--  header -->
<%@ include file="/layout/shop_header.jsp"%>
<!--//  header --> 

<!-- container -->
<div id="container">
  
	<div id="contents">
		<!-- 추천상품 -->
		<div class="recommend">

			<h2><img src="../img/main/icon_recom01.png" alt=""><span>AIRYAMI</span> 추천상품</h2>
			<span class="more"><a href="#">더보기+</a></span>

			<ul id="recommandDIV"></ul>

		</div>
		<!--// 추천상품 -->
	


		<!-- 인기상품 -->
		<div class="top_pro">
			<h3><img src="../img/main/icon_top01.png" alt=""> 인기상품</h3>
			<span class="more"><a href="#">더보기 +</a></span>

			<dl id="popularDIV">
				<dt>
					<a href="#">
						<p class="photo">
							<span><img src="../img/main/icon_top02.png" alt="인기상품"></span>
							<img src="../img/main/product_img01.jpg" alt="">
						</p>
						<div class="desc">
							<span class="red">인기 1위 상품</span>
							<p class="txt">당신의 피부에서 느껴지는 감촉을 바꿔보세요. 당신의 피부에서 느껴지는 감촉을 바꿔보세요.
								<span class="sm">당신의 피부도 달라질 수 있습니다.</span>
							</p>

							<span class="cate">스킨 테라피 <em>No.6617</em></span>
							<strong class="title">리뉴 인텐시브 스킨 로션 대용량</strong>
						</div>
					</a>
				</dt>
				
				<dd class="first">
					<a href="#">
						<p><img src="../img/main/product_img02.jpg" alt=""></p>
						<div>
							<span class="cate">가정 생활용품 <em>No.3854</em></span>
							<strong class="title">레몬브라이트</strong>
						</div>
					</a>
				</dd>
				<dd>
					<a href="#">
						<p><img src="../img/main/product_img03.jpg" alt=""></p>
						<div>
							<span class="cate">뷰티 <em>No.3031</em></span>
							<strong class="title">세이벨라 립 컬렉션 3종</strong>
						</div>
					</a>
				</dd>
				<dd>
					<a href="#">
						<p><img src="../img/main/product_img04.jpg" alt=""></p>
						<div>
							<span class="cate">스킨 테라피 <em>No.9279</em></span>
							<strong class="title">레몬 100% 에센셜 오일</strong>
						</div>
					</a>
				</dd>
			</dl>
		</div>
		<!--// 인기상품 -->

		<!-- new arrival -->
		<div class="arrival">
			<a href="#"><img src="../img/main/banner_new.gif" alt="New arrival"></a>
		</div>
		<!--// new Allival -->

		<!-- 신규상품 -->
		<div class="new_pro">
			<h3><img src="../img/main/icon_new01.png" alt=""> 신규상품</h3>
			<span class="more"><a href="#">더보기 +</a></span>

			<ul id="newDIV"></ul>
		</div>
		<!--// 신규상품 -->
	</div> 
</div>
<!--// container -->

</div>
<!-- wrap -->
<!--  footer -->
<%@ include file="/layout/shop_footer.jsp"%>
<!--  //footer -->
	
</body>  
</html>