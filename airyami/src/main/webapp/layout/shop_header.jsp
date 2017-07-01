<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>


<script type="text/javascript">

//상단메뉴조회
//메뉴리스트 조회
function fn_getHeadMenuList() {
	
	return;
	
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
		var innerhtml = "<h2><img src=\"/images/admin/hTitle_admin.gif\" alt=\"관리자 모드\"></h2><ul>";
		
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

	//fn_setLeftMenu(result.ds_left_list);
}

</script>
<!-- header -->
	<div id="header">
		<span id="top_link"></span>

		<div class="top">
			<h1><img src="../img/common/logo.png" alt="Airyami"></h1>

			<fieldset>
				<input type="text" placeholder="검색어를 입력하세요">
				<a href="#"><img src="../img/common/sch_btn.png" alt="검색"></a>
			</fieldset>

			<ul>
				<li><a href="#">제품 스토어</a> / </li>
				<li id="biz">
					<a href="#">비즈니스 센터</a> <c:choose><c:when test="${ds_userInfo.USER_TYPE != 'C'}">/</c:when></c:choose>
					<div>
						<ul>
							<li><a href="#">등록센터</a> | </li>
							<li><a href="#">뉴스&amp;이벤트</a> | </li>
							<li><a href="#">리포트&amp;도구</a></li>
						</ul>
					</div>
				</li>
		<c:choose>
			<c:when test="${ds_userInfo.USER_TYPE != 'C'}">
				 <li><a href="/admin/main.do">관리자 바로가기</a></li>
			</c:when>
		</c:choose>
			</ul>


			<select class="select">
				<option value="en-US">미국-영어</option>    
				<option value="es-US">미국-스페인어</option>    
				<option value="en-CA">캐나다-영어</option>    
				<option value="fr-CA">캐나다-프랑스어</option>    
				<option value="es-MX">멕시코</option>    
				<option value="en-AU">호주</option>    
				<option value="en-NZ">뉴질랜드</option>    
				<option value="en-GB">영국</option>    
				<option value="en-IE">아일랜드</option>    
				<option value="nl-NL">네덜란드</option>    
				<option value="de-DE">독일</option>    
				<option value="de-AT">오스트리아</option>    
				<option value="pl-PL">폴란드</option>    
				<option value="en-SG">싱가폴</option>    
				<option value="en-MY">말레이시아</option>    
				<option value="ja-JP">일본</option>    
				<option selected="selected" value="ko-KR">한국</option>    
				<option value="zh-CN">중국</option>    
				<option value="zh-TW">대만</option>    
				<option value="zh-HK">홍콩</option>    
			</select> 

		</div>







				

		<ul id="gnb">
			<li class="out"><a href="#">건강기능ㆍ일반 식품</a> | 
				<div id="sub_a">
					<div class="part2">
						<h5><a href="#">비타민 &amp; 영양보충용식품</a></h5>
						<ul>
							<li><a href="#">- 특정영양식품</a>
								<ul>
									<li><a href="#">&middot; 혈행 및 콜레스테롤</a></li>
									<li><a href="#">&middot; 뼈 &amp; 관절 건강</a></li>
									<li><a href="#">&middot; 항산화 건강</a></li>
									<li><a href="#">&middot; 장 건강</a></li>
									<li><a href="#">&middot; 눈 건강</a></li>
									<li><a href="#">&middot; 중년 남성 건강</a></li>
									<li><a href="#">&middot; 면역건강</a></li>
									<li><a href="#">&middot; 기억력 건강</a></li>
									<li><a href="#">&middot; 중년 여성 건강</a></li>
									<li><a href="#">&middot; 요로 건강</a></li>
								</ul>
							</li>
							<li><a href="#">- 멀티비타민</a>
								<ul>
									<li><a href="#">&middot; 여성용</a></li>
									<li><a href="#">&middot; 남성용</a></li>
									<li><a href="#">&middot; 50대 이상</a></li>
									<li><a href="#">&middot; 어린이</a></li>
								</ul>
							</li>
							<li><a href="#">-알뜰 구성 팩</a></li>
						</ul>
					</div>

					<div class="part2">
						<h5><a href="#">일반 식품 &amp; 체중 관리 제품</a></h5>
						<ul>
							<li><a href="#">- 다이터트 &amp; 체중 조절</a>
								<ul>									
									<li><a href="#">&middot; 식이섬유</a></li>
									<li><a href="#">&middot; 체지방 감소</a></li>
								</ul>
							</li>
							<li><a href="#">- 일반식품</a></li>
							<li><a href="#">- 알뜰 구성 팩</a></li>
						</ul>
					</div>
				</div>	

			</li>

			<li class="out"><a href="#">스킨 테라피</a> | 
				<div id="sub_b">
					<div>
						<h5><a href="#">스킨 테라피</a></h5>
						<ul>
							<li><a href="#">- 건성 피부 관리</a></li>
							<li><a href="#">- 멜라루카 오일</a></li>
							<li><a href="#">- 알뜰 구성 팩</a></li>
						</ul>	
					</div>
					
				</div>
			</li>

			<li class="out"><a href="#">뷰티</a> | 
				<div id="sub_c">
					<div>
						<h5><a href="#">뷰티</a></h5>
						<ul>
							<li><a href="#">- 럭셔리 스킨 케어</a></li>
							<li><a href="#">- 기초 관리</a>
								<ul>
									<li><a href="#">&middot; 중ㆍ건성용</a></li>
									<li><a href="#">&middot; 중ㆍ지성용</a></li>
								</ul>
							</li>
							<li><a href="#">- 에센스 &amp; 마스크</a></li>
							<li><a href="#">- 럭셔리 헤어 케어</a>
								<ul>
									<li><a href="#">&middot; 샴푸 &amp; 컨디셔너</a></li>
									<li><a href="#">&middot; 스타일링</a></li>
									<li><a href="#">&middot; 트리트먼트</a></li>
								</ul>
							</li>
							<li><a href="#">- BB 크림 &amp; 컨실러</a></li>
							<li><a href="#">- 파운데이션 &amp; 파우더</a></li>
							<li><a href="#">- 아이 컬러</a></li>
							<li><a href="#">- 블러셔</a></li>
							<li><a href="#">- 립</a></li>
							<li><a href="#">- 뷰티 보조 도구</a></li>
							<li><a href="#">- 알뜰 구성 팩</a></li>
						</ul>
					</div>
				</div>
			</li>

			<li class="out"><a href="#">가정 생활용품</a> | 
				<div id="sub_d">
					<div>
						<h5><a href="#">청소 및 세탁제품</a></h5>
						<ul>
							<li><a href="#">- 가정용 세정 제품</a>
								<ul>
									<li><a href="#">&middot; 다목적 세정 제품</a></li>
									<li><a href="#">&middot; 욕실 청소 제품</a></li>
									<li><a href="#">&middot; 살균 및 위생 제품</a></li>
									<li><a href="#">&middot; 유리 청소 제품</a></li>
									<li><a href="#">&middot; 얼룩 제거 제품</a></li>
								</ul>
							</li>
							<li class="wide"><a href="#">- 주방 세제 &amp; 야채과일 세정제</a></li>
							<li><a href="#">- 세탁 제품</a>
								<ul>
									<li><a href="#">&middot; 세탁세제</a></li>
									<li><a href="#">&middot; 표백세제</a></li>
									<li><a href="#">&middot; 섬유유연제</a></li>
									<li><a href="#">&middot; 세탁전 얼룩제거제</a></li>
								</ul>
							</li>
							<li><a href="#">- 알뜰 구성 팩</a></li>
						</ul>
					</div>
				</div>
			</li>

			<li class="out"><a href="#">배스 &amp; 바디</a> | 
				<div id="sub_e">
					<div class="part3">
						<h5><a href="#">바쓰 &amp; 바디</a></h5>						
						<ul>
							<li><a href="#">- 고급 비누</a></li>
							<li><a href="#">- 바디 워시 &amp; 소품</a></li>
							<li><a href="#">- 손 세정 제품</a></li>
							<li><a href="#">- 남성용</a></li>
							<li><a href="#">- 립밤</a></li>
							<li><a href="#">- 알뜰 구성 팩</a></li>
						</ul>

						<h5><a href="#">페이셜 케어</a></h5>
						<ul>
							<li><a href="#">어휘니아 스킨 케어</a></li>
						</ul>
					</div>
					<div class="part3">
						<h5><a href="#">헤어 케어</a></h5>						
						<ul>
							<li><a href="#">- 샴푸 &amp; 컨디셔너</a>
								<ul>
									<li><a href="#">&middot; 가늘고 약한 지성 모발</a></li>
									<li><a href="#">&middot; 악건성 &amp; 손상 모발</a></li>
								</ul>
							</li>							
							<li><a href="#">- 스타일링</a>
								<ul>
									<li><a href="#">&middot; 프렙 스타일</a></li>
									<li><a href="#">&middot; 피니시 스타일</a></li>
								</ul>
							</li>							
							<li><a href="#">- 트리트먼트</a>
								<ul>
									<li><a href="#">&middot; 데일리 트리트먼트</a></li>
									<li><a href="#">&middot; 인텐시브 트리트먼트</a></li>
								</ul>
							</li>							
							<li><a href="#">- 알뜰 구성 팩</a></li>
						</ul>
					</div>
					<div class="part3">
						<h5><a href="#">스킨 케어</a></h5>						
						<ul>
							<li><a href="#">- 건성 피부 관리</a></li>
							<li><a href="#">- 크림 &amp; 로션</a></li>
							<li><a href="#">- 풋케어</a></li>
						</ul>				



						<h5><a href="#">구강케어</a></h5>						
						<ul>
							<li><a href="#">- 치약</a></li>
							<li><a href="#">- 마우스 린스(가글액)</a></li>
							<li><a href="#">- 구강 구취 제품</a></li>
							<li><a href="#">- 칫솔 &amp; 기타</a></li>
							<li><a href="#">- 알뜰 구성 팩</a></li>
						</ul>
					</div>
				</div>
			</li>

			<li class="out"><a href="#">에센셜 오일</a>
				<div id="sub_f">
					<div>
						<h5><a href="#">에센셜 오일</a></h5>						
						<ul>
							<li><a href="#">- 싱글 오일</a></li>
							<li><a href="#">- 블렌드 오일</a></li>
							<li><a href="#">- 캐리어 오일</a></li>
							<li><a href="#">- 오일 팩</a></li>
							<li><a href="#">- 액세서리</a></li>
						</ul>
					</div>
				</div>
			</li>

		</ul>

		<!--// gnb --> 
	</div>

	<!--// header -->