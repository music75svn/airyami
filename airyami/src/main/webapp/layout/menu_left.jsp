<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<script type="text/javascript">
$(document).ready(function(){
	var icons = {
	  header: "ui-icon-circle-arrow-e",
	  activeHeader: "ui-icon-circle-arrow-s"
	};
	$( "#admin_leftmenu" ).accordion({
	  heightStyle: "content",
	  // event: "mouseover",
	  collapsible: true,
	  active: false,
	  icons: icons
	});
	$( "#toggle" ).button().on( "click", function() {
		alert($("#H_MENU_CD").val());
	  if ( $( "#admin_leftmenu" ).accordion( "option", "icons" ) ) {
		$( "#admin_leftmenu" ).accordion( "option", "icons", null );
	  } else {
		$( "#admin_leftmenu" ).accordion( "option", "icons", icons );
	  }
	});
  });
  
function fn_callLeftMenu(menuCode, menuName) {
	$('#H_MENU_CD').val(menuCode);
	$('#H_MENU_NM').val(menuName);
	
	//레프트메뉴 호출
	fn_getLeftMenuList();
	
}

//레프트 메뉴리스트 조회
function fn_getMenuList() {
	var inputParam = new Object();
	inputParam.sid 		= "leftMenuList";
	inputParam.url 		= "/menu/menuList.do";
	inputParam.data 	= gfn_makeInputData($("#menuForm"));
	
	inputParam.data.MENU_TYPE = SES_MENU_TYPE;
	
	inputParam.callback		= fn_callBackLMenu;
	
	gfn_Transaction( inputParam );
	
}

////////////////////////////////////////////////////////////////////////////////////
//콜백 함수
function fn_callBackLMenu(sid, result, data){
	
	if (!result.success) {
		alert(result.msg);
		return;
	}
	
	if(sid == "leftMenuList"){
		fn_setLeftMenu(result.ds_menulist);
	}
}

function fn_setLeftMenu(menu_list) {

	var innerhtml = "";	// menu 전체 
	var menuHtml_1 = "";	// 1레벨 카테고리별 내용
	var menuHtml_2 = "";	// 2레벨 카테고리별 내용
	
	var H_MENU_CD = $("#H_MENU_CD").val();
	
	for(var i = 0 ; i < menu_list.length; i++){
		
		
		var menuInfo = menu_list[i];
		if(menuInfo.MENU_LEVEL ==1)	// 1레벨 이면
		{
			var 
			
			menuHtml_1  = "<div class=\"menu-title\">"+ menuInfo.MENU_NAME +"</div>";
			menuHtml_1 += "<div class=\"menu-contents\" id=\""+ menuInfo.UPPER_MENU_CODE +"\"><ul>";
			menuHtml_1 += "menuHtml_2";
			menuHtml_1 += "</ul></div>";
			menuHtml_1 += "</div>";
		}
		
		if(menuInfo.MENU_LEVEL ==2)	// 2레벨 이면 
		{
			var linkUrl = menuInfo.LINK_URL + "?H_MENU_CD=" + menuInfo.UPPER_MENU_CODE + "&L_MENU_CD=" + menuInfo.MENU_CODE + "&MENUON=Y&MENU_TYPE="+ menuInfo.MENU_TYPE +"&" + menuInfo.LINK_PARAM;
			menuHtml_2 += "<li><a href=\"" + linkUrl + "\">"+ menuInfo.MENU_NAME +"</a></li>";
		}
		
		
		// 다음단계 레벨이 현재 보다 작아지면 정리해야한다.
		var nextLevel = 1;
		if( i+1 != menu_list.length )
			nextLevel = menu_list[i+1].MENU_LEVEL;
		
		if( menuInfo.MENU_LEVEL <= nextLevel)
			continue;
		
		if(nextLevel == 1){
			menuHtml_1 = gfn_replaceAll(menuHtml_1, "menuHtml_2", menuHtml_2);
			menuHtml_2 = "";
			innerhtml += menuHtml_1;
		}
	}
	
	$("#admin_leftmenu").html(innerhtml);
	
	debugger;
	
	//$("#"+H_MENU_CD).css("display", "");
	
}
function fn_setLeftMenu_back(menu_list) {

	var innerhtml = "<h3><span>" + $('#H_MENU_NM').val() + "</span></h3><ul>";
	var nowLevel = 0;
	var L_MENU_CD = $("#L_MENU_CD").val();
	
	var site_id = "&SITE_ID=" + gfn_getSiteID();
	
	if(gfn_isNull(L_MENU_CD)) {
		L_MENU_CD = menu_list[0].MENU_CODE;
	}
	for(var i = 0 ; i < menu_list.length; i++){
		var linkUrl = menu_list[i].LINK_URL + "?H_MENU_CD=" + $("#H_MENU_CD").val() + "&L_MENU_CD=" + menu_list[i].MENU_CODE + site_id + "&MENUON=Y&MENU_TYPE="+ menu_list[i].MENU_TYPE +"&" + menu_list[i].LINK_PARAM;
		var selected = "";
		if(L_MENU_CD == menu_list[i].MENU_CODE) {
			selected = "on";
		}
		innerhtml = innerhtml + "<li class=\"d1\"><a href=\"" + linkUrl + "\" class=\"d1a " + selected + "\"> <b><span>" + menu_list[i].MENU_NAME + "</span></b></a></li>";
	}
	innerhtml = innerhtml + "</li>";
	
	$("#admin_leftmenu").html(innerhtml);
	
}
</script>

<form id="menuForm" name="menuForm" action="#" method="post"  onsubmit="return false;">
	<input type="hidden" name="H_MENU_CD" id="H_MENU_CD" value="<c:out value="${H_MENU_CD}"/>"/>
	<input type="hidden" name="H_MENU_NM" id="H_MENU_NM" value="<c:out value="${H_MENU_NM}"/>"/>
	<input type="hidden" name="L_MENU_CD" id="L_MENU_CD" value="<c:out value="${L_MENU_CD}"/>"/>
	<input type="hidden" name="SRCH_MENU_TYPE" id="SRCH_MENU_TYPE" value="A"/>
</form>
	<div id="admin_leftmenu">
	</div>