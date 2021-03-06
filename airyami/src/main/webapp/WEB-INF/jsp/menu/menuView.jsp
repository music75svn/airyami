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

	// 02. 데이터 조회
	$("#SRCH_MENU_TYPE").val('<c:out value="${MENU_TYPE}"/>');
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
	updateParam.sid 		= "updateMenu";
	updateParam.url 		= "/menu/updateMenu.do";
	updateParam.data 		= gfn_makeInputData($("#dataForm"));
	
	gfn_Transaction( updateParam );
}

function fn_callDelete(){

	if("Y" != $("#ISLEAF_YN").val()){
		// 자식이없는 메뉴만 삭제 가능합니다.
		alert("<spring:message code="errors.allowdeleteMenuChildMsg.msg"/>");
		return;
	}
	
	if(!confirm("<spring:message code="common.delete.msg"/>")){
		return false;
	}

	var updateParam = new Object();
	updateParam.sid 		= "deleteMenu";
	updateParam.url 		= "/menu/deleteMenu.do";
	updateParam.data 		= gfn_makeInputData($("#dataForm"));
	
	gfn_Transaction( updateParam );
}


function fn_callUserGroup(){
	var inputParam = new Object();
	
	inputParam.MENU_TYPE = $("#MENU_TYPE").val();	
	inputParam.MENU_CODE = $("#MENU_CODE").val();	
	inputParam.MENU_NAME = $("#MENU_NAME").val();
	
	gfn_commonGo("/menu/menuUserGrp", inputParam, "Y");
}


function fn_callRegist(){
	var registParam = new Object();
	
	registParam.NOW_MENU_TYPE = $("#NOW_MENU_TYPE").val();
	registParam.UPPER_MENU_CODE = $("#MENU_CODE").val();
	registParam.UPPER_MENU_NAME = $("#MENU_NAME").val();
	registParam.MENU_LEVEL = Number($("#MENU_LEVEL").val()) + 1;
	

	gfn_commonGo("/menu/goInsertMenu", registParam, "Y");
}

// 최상위 메뉴등록
function fn_callRegistFirst(){
	var registParam = new Object();
	
	registParam.NOW_MENU_TYPE = $("#NOW_MENU_TYPE").val();
	registParam.UPPER_MENU_CODE = "1";
	registParam.MENU_LEVEL = 1;
	

	gfn_commonGo("/menu/goInsertMenu", registParam, "Y");
}


////////////////////////////////////////////////////////////////////////////////////
// BUTTON_Click Event 정의
////////////////////////////////////////////////////////////////////////////////////

function fn_srch() {
	$("#menuTree").html("");
	var srchParam = new Object();
	srchParam.sid 		= "menuList";
	srchParam.url 		= "/menu/menuList.do";
	srchParam.data 		= gfn_makeInputData($("#scrhForm"));
	
	gfn_Transaction( srchParam );
}

function fn_menuView(viewCode){
	$("#MENU_CODE").val(viewCode);
	
	var viewParam = new Object();
	viewParam.sid 		= "selectMenu";
	viewParam.url 		= "/menu/selectMenu.do";
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

	if (sid == "menuList") {
		var menuHtml = "";		 
			
		var preLevel = -1;
		var nowLevel = 0;
		var menuName = "";
		var menuCode = ""; 
		
		for(var i = 0 ; i < result.ds_menulist.length; i++) {
			nowLevel = result.ds_menulist[i].MENU_LEVEL;
			menuName = result.ds_menulist[i].MENU_NAME;
			menuCode = result.ds_menulist[i].MENU_CODE;
			menuOrder = result.ds_menulist[i].MENU_ORDER;

			if(preLevel < nowLevel) {
				if(nowLevel <= 1) {
					menuHtml = menuHtml + "<ul>";
				} else {
					menuHtml = menuHtml + "<ul style='display:none;'>";
				}
			} else if(preLevel == nowLevel) {
				menuHtml = menuHtml + "</li>";
			} else {
				menuHtml = menuHtml + "</li>";	
				for(var j = 0; j < preLevel - nowLevel; j++) {					
					menuHtml = menuHtml + "</ul></li>";			
				}
			}
			
			if(i == result.ds_menulist.length - 1) {
				menuHtml = menuHtml + "<li>";
			} else {
				if(nowLevel < result.ds_menulist[i+1].MENU_LEVEL) {
					if(nowLevel == 0) {
						menuHtml = menuHtml + "<li class='open'><button class='toggle plus' type='button' onclick='javascript:fn_toggleAction(this)'>+</button>";
					} else if(nowLevel == 1) {
						menuHtml = menuHtml + "<li class='active'><button class='toggle plus' type='button' onclick='javascript:fn_toggleAction(this)'>+</button>";
					} else if(nowLevel == 2){
						menuHtml = menuHtml + "<li><button class='toggle plus' type='button' onclick='javascript:fn_toggleAction(this)'>+</button>";
					} else {
						menuHtml = menuHtml + "<li>";
					}	
				} else {
					menuHtml = menuHtml + "<li>";
				}
			}
			
			menuHtml = menuHtml + "<a href='javascript:fn_menuView(\"" + menuCode + "\")'>" + menuName + "(" + menuOrder + ")</a>";
			/*
			if(nowLevel == 0) {
				menuHtml = menuHtml + "<li class='open'><button class='toggle plus' type='button' onclick='javascript:fn_toggleAction(this)'>+</button><a href='javascript:fn_menuView(\"" + menuCode + "\")'>" + menuName + "</a>";
			} else if(nowLevel == 1) {
				menuHtml = menuHtml + "<li class='active'><button class='toggle plus' type='button' onclick='javascript:fn_toggleAction(this)'>+</button><a href='javascript:fn_menuView(\"" + menuCode + "\")'>" + menuName + "</a>";
			} else if(nowLevel == 2){
				menuHtml = menuHtml + "<li><button class='toggle plus' type='button' onclick='javascript:fn_toggleAction(this)'>+</button><a href='javascript:fn_menuView(\"" + menuCode + "\")'>" + menuName + "</a>";
			} else {
				menuHtml = menuHtml + "<li><a href='javascript:fn_menuView(\"" + menuCode + "\")'>" + menuName + "</a>";
			}	
			*/
			preLevel = nowLevel;
		}
		menuHtml = menuHtml + "</li>";	
		for(var j = 0; j < preLevel; j++) {
			menuHtml = menuHtml + "</ul></li>";			
		}
		menuHtml = menuHtml + "</ul>";
		//생성된 html로 트리를 생성하다.
		//alert(menuHtml);
		$("#menuTree").html(menuHtml);	
		
		fn_menuTreeLoad();	
		
		fn_menuView(result.ds_menulist[0].MENU_CODE);
	} else if (sid == "selectMenu") {
		gfn_setDetails(result.ds_detail, $("#dataForm"));
		
		var usergrpHtml = "";
		for(var i = 0; i < result.ds_menuUsergrplist.length; i++) {
			usergrpHtml = usergrpHtml + "<tr><td>" + result.ds_menuUsergrplist[i].USER_TYPE + "</td>";
			usergrpHtml = usergrpHtml + "<td>" + result.ds_menuUsergrplist[i].USER_TYPE_NAME + "</td>";
			usergrpHtml = usergrpHtml + "<td>" + result.ds_menuUsergrplist[i].USER_ROLE_NM + "</td></tr>";
		}
		$("#menuUsergrp").html(usergrpHtml);
	} else if(sid == "updateMenu") {
		alert("수정하였습니다.");
		fn_srch();
	} else if(sid == "deleteMenu") {
		alert("삭제하였습니다.");
		fn_srch();
	}
}

function fn_menuTreeLoad() { 
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
	
} // left 트리메뉴


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
		  <h4>관리자 메뉴 관리</h4>

			<!-- 검색영역 -->
			<div id="search">
				<dl>
					<dt>&nbsp;</dt>
					<dd>
					<form id="scrhForm" name="scrhForm" method="post" action="#" onsubmit="return false;">
						<select id="SRCH_MENU_TYPE" name="SRCH_MENU_TYPE">
	                        <c:forEach var="MENU_TYPE" items="${ds_cd_MENU_TYPE}">
	                            <option value="${MENU_TYPE.CD}">${MENU_TYPE.CD_NM}</option>
	                        </c:forEach>
						</select>						
						<select id="SRCH_USER_TYPE" name="SRCH_USER_TYPE">
							<option value ="">전체</option>
	                        <c:forEach var="USER_TYPE" items="${ds_cd_USER_TYPE}">
	                            <option value="${USER_TYPE.CD}">${USER_TYPE.CD_NM}</option>
	                        </c:forEach>
						</select>						
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
					<h4 class="font">메뉴목록</h4>					
					<div class="treetype">					
						<!-- 트리메뉴 --> 
						<div id="menuTree" class="tree"> 
							
						</div>
						 <!--//트리메뉴 -->
					</div>
				</div>
				<!-- //트리타입 -->
				
				
				<!-- 상세내용 -->
				<form id="dataForm" name="dataForm" method="post" action="#" onsubmit="return false;">
				<div class="adminBoardIn">
					<h4 class="font">상세정보</h4>
					<ul>
						<li>
							<label for="">메뉴타입</label>
							<input type="hidden" id="NOW_MENU_TYPE" name="NOW_MENU_TYPE"/>
							<input type="hidden" id="ISLEAF_YN" name="ISLEAF_YN"/>
							<select id="MENU_TYPE" name="MENU_TYPE">
		                        <c:forEach var="MENU_TYPE" items="${ds_cd_MENU_TYPE}">
		                            <option value="${MENU_TYPE.CD}">${MENU_TYPE.CD_NM}</option>
		                        </c:forEach>
							</select>
						</li>
						<li>
							<label for="">메뉴코드</label>
							<input type="text" class="txt" style="width:100px;"  id="MENU_CODE" name="MENU_CODE" readonly/>							
						</li>
						<li>
							<label for="">메뉴명</label>
							<input type="text" class="txt" style="width:330px;"  id="MENU_NAME" name="MENU_NAME"/>
						</li>
						<li>
							<label for="">상위메뉴코드</label>
							<input type="text" class="txt" style="width:100px;"  id="UPPER_MENU_CODE" name="UPPER_MENU_CODE" readonly/>
						</li>
						<li>
							<label for="">메뉴 LEVEL</label>
							<input type="text" class="txt" style="width:20px;"  id="MENU_LEVEL" name="MENU_LEVEL" readonly/>
						</li>
						<li>
							<label for="">링크 URL</label>
							<input type="text" class="txt" style="width:330px;"  id="LINK_URL" name="LINK_URL" />
						</li>
						<li>
							<label for="">링크 PARAMETER</label>
							<input type="text" class="txt" style="width:330px;"  id="LINK_PARAM" name="LINK_PARAM" />
						</li>
						<li>
							<label for="">메뉴순서</label>
							<input type="text" class="txt" style="width:40px;"  id="MENU_ORDER" name="MENU_ORDER" />
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
					<div class="tblLi">
						<strong>사용자그룹</strong>
						<table summary="관련내용">
							<caption>사용자그룹</caption>
							<colgroup>
								<col width="100">
								<col width="100">
								<col width="100">
							</colgroup>
							<thead>
								<tr>
									<th scope="col">사용자그룹</th>
									<th scope="col">사용자그룹명</th>
									<th scope="col">사용자역할</th>
								</tr>
							</thead>
							<tbody id="menuUsergrp">
							</tbody>
						</table>
					</div>
					<div class="btnArea2">
						<button type="button" class="btn3" onclick="javascript:fn_callRegistFirst()"><span>최상위등록</span></button>&nbsp;
						<button type="button" class="btn3" onclick="javascript:fn_callRegist()"><span>하위등록</span></button>&nbsp;
						<button type="button" class="btn3" onclick="javascript:fn_callUserGroup()"><span>사용자그룹</span></button>&nbsp;
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
