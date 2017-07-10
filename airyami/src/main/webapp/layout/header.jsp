<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>


<script type="text/javascript">

//상단메뉴조회
//메뉴리스트 조회
function fn_getHeadMenuList() {
	
	var inputParam = new Object();
	inputParam.sid 			= "headMenuList";
	inputParam.url 			= "/CMM/hMenuList.do";
	inputParam.data 		= gfn_makeInputData($("#menuForm"));
	//inputParam.data.MENU_TYPE = //gfn_getSiteID();
	inputParam.data.MENU_TYPE = "A";
	inputParam.callback		= fn_callBackHMenu;
	
	gfn_Transaction( inputParam );
	
}

////////////////////////////////////////////////////////////////////////////////////
//콜백 함수
function fn_callBackHMenu(sid, result, data){
	//debugger;
	if (!result.success) {
		alert(result.msg);
		return;
	}

	var H_MENU_CD = $('#H_MENU_CD').val();
	if(sid == "headMenuList"){
		var innerhtml = "<h2><img src=\"/images/admin/hTitle_admin.png\" alt=\"관리자 모드\"></h2><ul>";
		
		for(var i = 0 ; i < result.ds_head_list.length; i++){
			if(H_MENU_CD == result.ds_head_list[i].MENU_CODE) {
				$('#H_MENU_NM').val(result.ds_head_list[i].MENU_NAME);
			}
			innerhtml = innerhtml + "<li> <a href=\"javascript:fn_callLeftMenu('" + result.ds_head_list[i].MENU_CODE + "', '" + result.ds_head_list[i].MENU_NAME + "');\"><span>" + result.ds_head_list[i].MENU_NAME + "</span> </a> </li>";
		}
		innerhtml = innerhtml + "</ul>";
	}
	
	$("#gnb").html(innerhtml);
	
	if(gfn_isNull(H_MENU_CD)) {
		$('#H_MENU_CD').val(result.ds_head_list[0].MENU_CODE);
		$('#H_MENU_NM').val(result.ds_head_list[0].MENU_NAME);
	}

	fn_setLeftMenu(result.ds_left_list);
}

</script>
<div id="header">
	<div id="logo">
		<h1><a href="#"><img src="/images/admin/airyamiLogo.png" alt="Airyami"></a></h1>
		<span id="userLogo"></span>
	</div>
	<div id="top_link"></div>
	<div id="gnb">
	<!-- 
		<h2><img src="/images/admin/hTitle_admin.png" alt="관리자 모드"></h2>
		<ul>
			<li> <a href=""><span>시스템</span> </a> </li>
			<li> <a href="" ><span>프로그램 관리</span> </a> </li>
			<li> <a href="" ><span>아이디어 등록실</span> </a> </li>
			<li> <a href="" ><span>무한상상 전시관</span> </a> </li>
			<li> <a href="" ><span>알림방</span> </a> </li>
			<li> <a href="" ><span>무한상상 운영</span> </a> </li>
			<li> <a href="" class="on"><span>장비 관리</span> </a> </li>
		</ul>
		-->
	</div>
</div>