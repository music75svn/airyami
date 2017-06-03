<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="egovframework.airyami.cmm.util.*"%>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html;application/json; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<%@ include file="/include/title.jsp"%>
<%@ include file="/include/admin_standard.jsp"%>

<!--  css -->
<script type="text/javascript">

////////////////////////////////////////////////////////////////////////////////////
// 화면 초기화 
////////////////////////////////////////////////////////////////////////////////////

$(function() {
	
	gfn_OnLoad();
	
	fn_init();
	
	fn_srch();
});

function fn_init(){
	//gfn_setSelect($('#SYS_CD'), 'C001002'); // 체크박스 초기화
	//$('#SYS_CD').val('C001002');
}

////////////////////////////////////////////////////////////////////////////////////
// 사용자 함수 정의 
////////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////////
// TABLE_Click Event 정의
////////////////////////////////////////////////////////////////////////////////////

function fn_callUpdate(){

	var updateParam = new Object();
	updateParam.sid 		= "updateCate";
	updateParam.url 		= "/category/updateCate.do";
	updateParam.data 		= gfn_makeInputData($("#dataForm"));
	
	gfn_Transaction( updateParam );
}

function fn_callDelete(){

	if("Y" != $("#ISLEAF_YN").val()){
		// 자식이없는 카테고리만 삭제 가능합니다.
		alert("<spring:message code="errors.allowdeleteCategoryChildMsg.msg"/>");
		return;
	}
	
	if(!confirm("<spring:message code="common.delete.msg"/>")){
		return false;
	}

	var updateParam = new Object();
	updateParam.sid 		= "deleteCate";
	updateParam.url 		= "/category/deleteCate.do";
	updateParam.data 		= gfn_makeInputData($("#dataForm"));
	
	gfn_Transaction( updateParam );
}



function fn_callRegist(){
	var registParam = new Object();
	
	registParam.UPPER_CATE_CODE = $("#CATE_CODE").val();
	registParam.UPPER_CATE_NAME = $("#CATE_NAME").val();
	registParam.CATE_LEVEL = Number($("#CATE_LEVEL").val()) + 1;
	

	gfn_commonGo("/category/goInsertCate", registParam, "Y");
}

// 최상위 카테고리등록
function fn_callRegistFirst(){
	var registParam = new Object();
	
	registParam.UPPER_CATE_CODE = "1";
	registParam.CATE_LEVEL = 1;
	

	gfn_commonGo("/category/goInsertCate", registParam, "Y");
}


////////////////////////////////////////////////////////////////////////////////////
// BUTTON_Click Event 정의
////////////////////////////////////////////////////////////////////////////////////

function fn_srch() {
	$("#cateTree").html("");
	var srchParam = new Object();
	srchParam.sid 		= "cateList";
	srchParam.url 		= "/category/cateList.do";
	srchParam.data 		= gfn_makeInputData($("#scrhForm"));
	
	gfn_Transaction( srchParam );
}

function fn_cateView(viewCode){
	$("#CATE_CODE").val(viewCode);
	
	var viewParam = new Object();
	viewParam.sid 		= "selectCate";
	viewParam.url 		= "/category/selectCate.do";
	viewParam.data 		= gfn_makeInputData($("#dataForm"));
	
	gfn_Transaction( viewParam );
}


////////////////////////////////////////////////////////////////////////////////////
// 콜백함수 정의
////////////////////////////////////////////////////////////////////////////////////

function fn_callBack(sid, result, data){
	if (!result.success) {
		alert(result.msg);
		return;
	}
	
	//debugger;

	if (sid == "cateList") {
		var cateHtml = "";		 
			
		var preLevel = -1;
		var nowLevel = 0;
		var cateName = "";
		var cateCode = ""; 
		
		for(var i = 0 ; i < result.ds_catelist.length; i++) {
			nowLevel = result.ds_catelist[i].CATE_LEVEL;
			cateName = result.ds_catelist[i].CATE_NAME;
			cateCode = result.ds_catelist[i].CATE_CODE;
			cateOrder = result.ds_catelist[i].CATE_ORDER;

			if(preLevel < nowLevel) {
				if(nowLevel <= 1) {
					cateHtml = cateHtml + "<ul>";
				} else {
					cateHtml = cateHtml + "<ul style='display:none;'>";
				}
			} else if(preLevel == nowLevel) {
				cateHtml = cateHtml + "</li>";
			} else {
				cateHtml = cateHtml + "</li>";	
				for(var j = 0; j < preLevel - nowLevel; j++) {					
					cateHtml = cateHtml + "</ul></li>";			
				}
			}
			
			if(i == result.ds_catelist.length - 1) {
				cateHtml = cateHtml + "<li>";
			} else {
				if(nowLevel < result.ds_catelist[i+1].CATE_LEVEL) {
					if(nowLevel == 0) {
						cateHtml = cateHtml + "<li class='open'><button class='toggle plus' type='button' onclick='javascript:fn_toggleAction(this)'>+</button>";
					} else if(nowLevel == 1) {
						cateHtml = cateHtml + "<li class='active'><button class='toggle plus' type='button' onclick='javascript:fn_toggleAction(this)'>+</button>";
					} else if(nowLevel == 2 || nowLevel == 3){
						cateHtml = cateHtml + "<li><button class='toggle plus' type='button' onclick='javascript:fn_toggleAction(this)'>+</button>";
					} else {
						cateHtml = cateHtml + "<li>";
					}	
				} else {
					cateHtml = cateHtml + "<li>";
				}
			}
			
			cateHtml = cateHtml + "<a href='javascript:fn_cateView(\"" + cateCode + "\")'>" + cateName + "(" + cateOrder + ")</a>";
			
			preLevel = nowLevel;
		}
		cateHtml = cateHtml + "</li>";	
		for(var j = 0; j < preLevel; j++) {
			cateHtml = cateHtml + "</ul></li>";			
		}
		cateHtml = cateHtml + "</ul>";
		//생성된 html로 트리를 생성하다.
		//alert(cateHtml);
		$("#cateTree").html(cateHtml);	
		
		fn_cateTreeLoad();	
		
		fn_cateView(result.ds_catelist[0].CATE_CODE);
	} else if (sid == "selectCate") {
		gfn_setDetails(result.ds_detail, $("#dataForm"));
		
		
	} else if(sid == "updateCate") {
		alert("수정하였습니다.");
		fn_srch();
	} else if(sid == "deleteCate") {
		alert("삭제하였습니다.");
		fn_srch();
	}
}

function fn_cateTreeLoad() { 
	var tree = $('.tree'); 
	var togglePlus = '<button type="button" class="toggle plus">+</button>';
	var toggleMinus = '<button type="button" class="toggle minus">-</button>';

	// defalt
	tree.find('li>ul').css('display','none');
	tree.find('ul>li:last-child').addClass('last');
	tree.find('li>ul:hidden').parent('li').prepend(togglePlus);
	tree.find('li>ul:visible').parent('li').prepend(toggleMinus);

	// active
	tree.find('li.active').parents('li').addClass('open');
	tree.find('li.open').parents('li').addClass('open');
	tree.find('li.open>.toggle').text('-').removeClass('plus').addClass('minus');
	tree.find('li.open>ul').slideDown(100);
	
} // left 트리카테고리


function fn_toggleAction(pObj) {
	t = $(pObj);
	t.parent('li').toggleClass('open');
	if(t.parent('li').hasClass('open')){
		t.text('-').removeClass('plus').addClass('minus');
		t.parent('li').find('>ul').slideDown(100);
	} else {
		t.text('+').removeClass('minus').addClass('plus');
		t.parent('li').find('>ul').slideUp(100);
	}
}

function fn_callbackAdminGroup(param) {
	alert(param.ADMIN_GROUP + " || " + param.ADMIN_GROUP_NAME);
	
}
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
		
		<!-- contents -->
		<div id="contents">
		  <h4>관리자 카테고리 관리</h4>

			<!-- 검색영역 -->
			<div id="search">
				<dl>
					<dt>&nbsp;</dt>
					<dd>
					<form id="scrhForm" name="scrhForm" method="post" action="#" onsubmit="return false;">
						<%-- <select id="SRCH_MENU_TYPE" name="SRCH_MENU_TYPE">
	                        <c:forEach var="MENU_TYPE" items="${ds_cd_MENU_TYPE}">
	                            <option value="${MENU_TYPE.CD}">${MENU_TYPE.CD_NM}</option>
	                        </c:forEach>
						</select>						
						<select id="SRCH_USER_TYPE" name="SRCH_USER_TYPE">
							<option value ="">전체</option>
	                        <c:forEach var="USER_TYPE" items="${ds_cd_USER_TYPE}">
	                            <option value="${USER_TYPE.CD}">${USER_TYPE.CD_NM}</option>
	                        </c:forEach>
						</select> --%>						
						<input type="submit" value="검색" onclick="javascript:gfn_fn_srch(); return false;"/>
						</form>
					</dd>
				</dl>
			</div>
			<!-- //검색영역 -->		  
	
			<!-- 게시판관리본문 -->
			<div class="adminBoardArea">
							
				<!-- 트리타입 -->				
				<div class="treetypeArea">
					<h4 class="font">카테고리목록</h4>					
					<div class="treetype">					
						<!-- 트리카테고리 --> 
						<div id="cateTree" class="tree"> 
							
						</div>
						 <!--//트리카테고리 -->
					</div>
				</div>
				<!-- //트리타입 -->
				
				
				<!-- 상세내용 -->
				<form id="dataForm" name="dataForm" method="post" action="#" onsubmit="return false;">
				<input type="hidden" id="ISLEAF_YN" name="ISLEAF_YN"/>
				<div class="adminBoardIn">
					<h4 class="font">상세정보</h4>
					<ul>
						<li>
							<label for="">상위카테고리</label>
							<input type="text" name="UPPER_CATE_NAME" id="UPPER_CATE_NAME" readonly/>
							<input type="hidden" id="UPPER_CATE_CODE" name="UPPER_CATE_CODE" readonly/>
						</li>
						<li>
							<label for="">카테고리코드</label>
							<input type="text" class="txt" style="width:100px;"  id="CATE_CODE" name="CATE_CODE" readonly/>							
						</li>
						<li>
							<label for="">카테고리명</label>
							<input type="text" class="txt" style="width:330px;"  id="CATE_NAME" name="CATE_NAME"/>
						</li>
						<li>
							<label for="">카테고리 LEVEL</label>
							<input type="text" class="txt" style="width:20px;"  id="CATE_LEVEL" name="CATE_LEVEL" readonly/>
						</li>
						<li>
							<label for="">카테고리순서</label>
							<input type="text" class="txt" style="width:40px;"  id="CATE_ORDER" name="CATE_ORDER" />
						</li>
						<li>
							<label for="">사용여부</label>
							<p class="styledSelect">
								<select class="custom" style="width:110px;" id="USE_YN" name="USE_YN">
									<option value="Y">사용</option>
									<option value="N">미사용</option>
								</select>
							</p>
						</li>
					</ul>					
					<div class="btnArea2">
						<button type="button" class="btn3" onclick="javascript:fn_callRegistFirst()"><span>최상위등록</span></button>&nbsp;
						<button type="button" class="btn3" onclick="javascript:fn_callRegist()"><span>하위등록</span></button>&nbsp;
						<button type="button" class="btn3" style="width:46px;" onclick="javascript:fn_callDelete()"><span>삭제</span></button>
						<button type="button" class="btn3" style="width:46px;" onclick="javascript:fn_callUpdate()"><span>수정</span></button>
					</div>				
				</div>
				</form>
				<!-- //상세내용 -->
			
			</div>
			<!-- //게시판관리본문 -->
					
		</div>
		<!--// contents -->
	</div>
	<!--// container -->
	<!--  footer -->
	<%@ include file="/layout/footer.jsp"%>
	<!--  //footer -->

</div>
<!--  //wrap -->
</body>
</html>
