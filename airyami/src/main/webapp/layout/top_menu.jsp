<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="egovframework.airyami.cmm.util.*"%>
<script type="text/javascript">
// 메뉴리스트 조회
$(function() {  //onready
	//여기에 최초 실행될 자바스크립트 코드를 넣어주세요
	fn_srchMenuList();
   
});


function fn_srchMenuList() {
    
	$.ajax({url: "<%=UrlUtil.getActionRoot(request)%>/menu/menuList.do",
          type: "post"
        , data: $("#menuForm").serialize()
        , dataType: "json"
        , async: false
        , success: function(result, textStatus, data) {
            if (result.success) {
            	//alert(result.ds_menu.length);
            	//alert(result.ds_menu[0].MENU_ID);
                fn_callbackMenuMake(result.ds_menu);
            } else {
                alert("메뉴 조회에 실패하였습니다.");
            }
        },
        error: function(xhr, errorName, error) {
            alert("메뉴 조회 중 에러가 발생하였습니다.");
        }
    });
	
}

function fn_callbackMenuMake(list){
	//return;
	//fn_clearMenu();
	var cur = '<%=UrlUtil.getMenuID(request)%>';
	
	var obj = $('#top_menu');

	var first_3 = false; 
	
	if(list && list.length > 0) {
		for(var i = 0; i < list.length; i++ ){
			
			if(cur == list[i].URL){
				fn_setNavi(list[i].MENU_PATH);
			}
			var level = list[i].LEVEL;
			
			if(level == 1)
				continue;
			
			if(level == 2){
				obj.append("<li id='"+list[i].MENU_ID+"' style='width:150px'><a href='#'>"+list[i].MENU_NM+"</a></li>");
				//obj.append("<li id='"+list[i].MENU_ID+"'>"+list[i].MENU_NM+"</li>");
				first_3 = true;
				continue;
			}
			
			if(level == 3){
				if(first_3){
					$('#' + list[i].UPPER_MENU_ID).append("<ul id='"+list[i].UPPER_MENU_ID+ "_ul" +"'></ul>");
					first_3 = false;
				}
				
				/* if( !gfn_isNull(list[i].URL) ){
				} */
					$('#' + list[i].UPPER_MENU_ID + "_ul").append("<li id='"+list[i].MENU_ID+"' ><a href='javascript:gfn_commonGo(\""+list[i].URL+"\", \"\", \"\")'>"+list[i].MENU_NM+"</a></li>");
					continue;
			}
		}
	}
	
	//alert(obj.html());
}

function fn_setNavi(spath){
	var naviArr = spath.split('/');
	
	
	var obj = $('#top_navi');
	
	
	obj.append("<li class=\"home\"><a href=\"#\"></a></li>");
	for(var i = 2; i<naviArr.length; i++){
		obj.append("<li class=\"break\">&#187;</li>");
		obj.append("<li>"+naviArr[i]+"</li>");
	}
	
	//alert(obj.html());
}

function fn_logOut(){
	$.ajax({url: "<%=UrlUtil.getActionRoot(request)%>/login/logout.do",
        type: "post"
      , data: $("#menuForm").serialize()
      , dataType: "json"
      , async: false
      , success: function(result, textStatus, data) {
			if (result.success) {
				//alert("안녕히 가세요~");
				document.location.href="/KPM/index.jsp";
			} else {
				alert("logout 실패하였습니다.");
			}
      },
      error: function(xhr, errorName, error) {
          alert("logout 중 에러가 발생하였습니다.");
      }
  });
}

function fn_clearMenu(){
	$('#top_menu li').remove();
}


</script>
<form id="menuForm" name="menuForm" method="POST">
	<input type="hidden" name="ADMIN_YN" id="ADMIN_YN" value="Y"/>
</form>

	<div class="logo-labels">
		<h1><a href="#">DreamWise 관리자</a></h1>
		<ul>
			<li class="logout"><a href="javascript:fn_logOut();"><span>Logout</span></a></li>
		</ul>
	</div>
	<div class="menu-search">
		<ul id="top_menu">
			<!-- <li id="KPM001"><a href="dashboard.html">운영</a>
				<ul id="KPM001_ul">
					<li id="KPM001001"><a href='javascript:gfn_commonGo("/MOP/MOP0010M", "", "")'>보호대상사이트관리</a></li>
					<li id="KPM001002"><a href='javascript:gfn_commonGo("/MOP/MOP0020M", "", "")'>알고리즘 관리</a></li>
					<li id="KPM001999"><a href='javascript:gfn_commonGo("/MOP/template_list", "", "")'>TEMPLATE</a></li>
				</ul>
			</li>
			<li id="KPM002"><a href="dashboard.html">현황</a></li>
			<li id="KPM003"><a href="dashboard.html">통계</a></li> -->
		</ul>
	</div>
	<div class="breadcrumbs">
		<ul id="top_navi">
			<!-- <li class="home"><a href="#"></a></li>
			<li class="break">&#187;</li>
			<li><a href="#">Menu item</a></li>
			<li class="break">&#187;</li>
			<li><a href="#">Menu item</a></li> -->
		</ul>
	</div>