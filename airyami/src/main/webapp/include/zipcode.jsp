<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<script type="text/javascript">
///////////////////////////////////////////////////////////////////
//우편번호 검색 함수
function fn_srchByDoroNM() { //도로명 또는 도로명+건물번호로 우편번호 검색
	debugger;
	if($("#SEARCH_KEYWORD").val() == null || $("#SEARCH_KEYWORD").val() ==""){
		alert("검색어를 입력해주세요.");
		return;
	}
	var inputParam = new Object();
	inputParam.sid = "srchByDoroNM";
	inputParam.url = "/common/getZipCode.do";
	inputParam.data = $("#srchByDoroNM").serialize();
	inputParam.callback = fn_callBackZipCode;
	
	gfn_Transaction( inputParam );
}
function fn_srchByDongNM() { //동 이름으로 우편번호 검색
	debugger;
	if($("#SEARCH_KEYWORD").val() == null || $("#SEARCH_KEYWORD").val() ==""){
		alert("검색어를 입력해주세요.");
		return;
	}
	var inputParam = new Object();
	inputParam.sid = "srchByDongNM";
	inputParam.url = "/common/getZipCode.do";
	inputParam.data = $("#srchByDongNM").serialize();
	inputParam.callback = fn_callBackZipCode;

	gfn_Transaction( inputParam );
}
/////////////////////////////////////////////////////////////////
// 우편번호 검색결과값 넘겨주기
function fn_getAddr(zipCode, addr1){
	$("#ZIP_CODE").attr("value", zipCode);
	$("#ADDRESS1").attr("value", addr1);
	$(".postList").empty();
	$(".findPostBox").addClass("hide");
}
/////////////////////////////////////////////////////////////////
//콜백 함수
function fn_callBackZipCode(sid, result, data){
	debugger;
	
	if (!result.success) {
		alert(result.msg);
		return;
	}
	
	if(sid == "srchByDoroNM" || sid == "srchByDongNM"){
		if(result.zip_code_count > 0){
			setZipCodeList(result.zip_code_list);	
		}else{
			var innerHtml = "<ul><li>찾는 내용이 없습니다.</li></ul>";
			$(".postList").html(innerHtml);
		}
		
	}
	
}
////////////////////////////////////////////////////////////////////
//기타기능
// 우편번호 검색 결과 셋팅
function setZipCodeList(zipCodeList){
	debugger;
	var innerHtml = "";
	
	for(var i = 0 ; i < zipCodeList.length; i++){
		if(i == 0){
			innerHtml = "<ul>";	
		}
		innerHtml = innerHtml + "<li><ul class=\"postArea\"><li><a href=\"javascript:fn_getAddr('"+ zipCodeList[i].ZIP_CODE +"','"+ zipCodeList[i].ADDRESS1 + " " + zipCodeList[i].DONG_BUILD +"');\"><em>" + zipCodeList[i].ZIP_CODE + "</em> <span> " + zipCodeList[i].OLD_ZIP_CODE + "</span></a></li>";
		innerHtml = innerHtml + "<li><strong>도로명</strong> <span><a href=\"javascript:fn_getAddr('"+ zipCodeList[i].ZIP_CODE +"','"+ zipCodeList[i].ADDRESS1 + " " + zipCodeList[i].DONG_BUILD +"');\">" + zipCodeList[i].ADDRESS1 + " " + zipCodeList[i].DONG_BUILD +"</a></span></li>";
		innerHtml = innerHtml + "<li><strong>지번</strong> <span><a href=\"javascript:fn_getAddr('"+ zipCodeList[i].ZIP_CODE +"','"+ zipCodeList[i].ADDRESS1 + " " + zipCodeList[i].DONG_BUILD +"');\">" + zipCodeList[i].OLD_ADDR + "</a></span></li></ul></li>";
	}
	innerHtml = innerHtml + "</ul>";
	
	$(".postList").html(innerHtml);
}
</script>

<!-- 우편번호 찾기 -->
	<div class="findPostBox hide">
		<div class="findPostIn">
			<h3 class="fontB">우편번호 찾기</h3>
			<h4 class="tab01 on"><a href="">도로명</a></h4>
				
			<!-- 검색영역 -->
			<div class="postSearch">
				<fieldset>
					<legend>검색</legend>
					<label for="">도로명 + 건물번호</label>
					<form id="srchByDoroNM" name="srchByDoroNM" method="post" action="<c:url value='/common/getZipCode.do'/>" onsubmit="return false">
						<input type="text" id="SEARCH_KEYWORD" name="SEARCH_KEYWORD" class="txt">
						<input type="hidden" id="SEARCH_TYPE" name="SEARCH_TYPE" value="1">
						<button type="button" onclick="fn_srchByDoroNM();">주소찾기</button>
					</form>
				</fieldset>
				<div>
					<p>
						<span>찾고자 하는 지역의 '도로명' 또는 '도로명+건물번호'를 입력해 주세요. </span>
						<span>예) 주소가 '서울특별시 서초구 서초대로45길 16'이라면 '서초대로45길' 또는 '서초대로45길 16'으로 검색</span>
					</p>
				</div>
			</div>
			<!-- // 검색영역 -->
				

			<h4 class="tab02"><a href="">지번</a></h4>
				
			<!-- 검색영역 -->
			<div class="postSearch hide">
				<fieldset>
					<legend>검색</legend>
					
					<label for="">동 이름</label>	
					<form id="srchByDongNM" name="srchByDongNM" method="post" action="<c:url value='/common/getZipCode.do'/>" onsubmit="return false">
						<input type="text" id="SEARCH_KEYWORD" name="SEARCH_KEYWORD" class="txt">
						<input type="hidden" id="SEARCH_TYPE" name="SEARCH_TYPE" value="2">
						<button type="button" onclick="fn_srchByDongNM();">주소찾기</button>
					</form>
				</fieldset>
				<div>
					<p>
						<span>찾고자 하는 지역의 '동 이름'을 입력해 주세요.</span>
						<span>예) 주소가 '서울특별시 서초구 서초동 1706-5'이라면 '서초동'으로 검색</span>
					</p>
				</div>
			</div>
			<!-- // 검색영역 -->

			
			<!--  검색리스트 -->
			<div class="postList"><!-- 
				<ul>
					<li>
						<ul class="postArea">
							<li><em>01869</em> <span>(139-050)</span></li>
							<li><strong>도로명</strong> <span>서울특별시 노원구 초안산로7길 20 (월계동,월계동각심재)</span></li>
							<li><strong>지번</strong> <span>서울특별시 노원구 월계동 766</span></li>
						</ul>
					</li>
					<li>
						<ul class="postArea">
							<li><em>01869</em> <span>(139-050)</span></li>
							<li><strong>도로명</strong> <span>서울특별시 노원구 초안산로7길 20 (월계동,월계동각심재)</span></li>
							<li><strong>지번</strong> <span>서울특별시 노원구 월계동 766</span></li>
						</ul>
					</li>
					<li>
						<ul class="postArea">
							<li><em>01869</em> <span>(139-050)</span></li>
							<li><strong>도로명</strong> <span>서울특별시 노원구 초안산로7길 20 (월계동,월계동각심재)</span></li>
							<li><strong>지번</strong> <span>서울특별시 노원구 월계동 766</span></li>
						</ul>
					</li>
				</ul> -->
			</div>
			<!--  //검색리스트 -->
			
			<button type="button" class="btnClosePopup"><img src="/images/board/btn_close_popup.png" alt="우편번호 팝업창 닫기"></button>
		</div>
	</div>
	<!-- //우편번호 찾기 -->