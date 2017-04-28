<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<script type="text/javascript">

function fn_callLeftMenu(menuCode, menuName) {
	$('#H_MENU_CD').val(menuCode);
	$('#H_MENU_NM').val(menuName);
	
	//레프트메뉴 호출
	fn_getLeftMenuList();
	
}

//레프트 메뉴리스트 조회
function fn_getLeftMenuList() {
	var inputParam = new Object();
	inputParam.sid 			= "leftMenuList";
	inputParam.url 			= "/CMM/lMenuList.do";
	inputParam.data 		= gfn_makeInputData($("#menuForm"));
	inputParam.data.MENU_SITE_ID = gfn_getSiteID();
	
	inputParam.callback		= fn_callBackLMenu;
	
	gfn_Transaction( inputParam );
	
}

////////////////////////////////////////////////////////////////////////////////////
//콜백 함수
function fn_callBackLMenu(sid, result, data){
	//debugger;
	
	if (!result.success) {
		alert(result.msg);
		return;
	}
	
	if(sid == "leftMenuList"){
		fn_setLeftMenu(result.ds_left_list);
	
	}
}

function fn_setLeftMenu(menu_list) {

	var innerhtml = "<h3><span>" + $('#H_MENU_NM').val() + "</span></h3><ul>";
	var nowLevel = 0;
	var L_MENU_CD = $("#L_MENU_CD").val();
	
	var site_id = "&SITE_ID=" + gfn_getSiteID();
	
	if(gfn_isNull(L_MENU_CD)) {
		L_MENU_CD = menu_list[0].MENU_CODE;
	}
	for(var i = 0 ; i < menu_list.length; i++){
		var linkUrl = menu_list[i].LINK_URL + "?H_MENU_CD=" + $("#H_MENU_CD").val() + "&L_MENU_CD=" + menu_list[i].MENU_CODE + site_id + "&MENUON=Y&" + menu_list[i].LINK_PARAM;
		var selected = "";
		if(L_MENU_CD == menu_list[i].MENU_CODE) {
			selected = "on";
		}
		innerhtml = innerhtml + "<li class=\"d1\"><a href=\"" + linkUrl + "\" class=\"d1a " + selected + "\"> <b><span>" + menu_list[i].MENU_NAME + "</span></b></a></li>";
	}
	innerhtml = innerhtml + "</li>";
	
	$("#aside").html(innerhtml);
}
</script>

<form id="menuForm" name="menuForm" action="#" method="post"  onsubmit="return false;">
	<input type="hidden" name="H_MENU_CD" id="H_MENU_CD" value="<c:out value="${H_MENU_CD}"/>"/>
	<input type="hidden" name="H_MENU_NM" id="H_MENU_NM" value="<c:out value="${H_MENU_NM}"/>"/>
	<input type="hidden" name="L_MENU_CD" id="L_MENU_CD" value="<c:out value="${L_MENU_CD}"/>"/>
</form>
<div id="aside">
<!-- 
	<h4><span>시스템</span></h4>
    <ul>
    	<li class="d1"> 
      		<a href="" class="d1a on"> <b><span>장비 관리</span></b> </a> 
      		<ul>
	      		<li><a href="" class="on">장비관리1</a></li>
	      		<li><a href="">장비관리 장비관리 장비관리 장비관리2</a></li>
      		</ul>
		</li>
		<li class="d1"> 
      		<a href="" class="d1a"> <b><span>장비 예약 관리</span></b> </a> 
	      	<ul>
	      		<li><a href="">장비 예약 관리1</a></li>
	      		<li><a href="">장비 예약 관리2</a></li>
	      	</ul>
		</li>
		<li class="d1"> <a href="" class="d1a"> <b><span>장비 예약 통계</span></b> </a> </li>
	</ul>
	 -->
</div>