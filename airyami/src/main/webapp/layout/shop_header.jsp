<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>


<script type="text/javascript">

//카테고리리스트 조회
function fn_getHeadMenuList() {
	
	var inputParam = new Object();
	inputParam.sid 			= "headMenuList";
	inputParam.url 			= "/category/cateList.do";
	
	inputParam.callback		= fn_callBackHMenu;
	
	gfn_Transaction( inputParam );
	
}

function fn_selectInitInfo() {
	
	var inputParam = new Object();
	inputParam.sid 			= "selectInitInfo";
	inputParam.url 			= "/CMM/selectInitInfo.do";
	
	inputParam.callback		= fn_callBackInitInfo;
	
	gfn_Transaction( inputParam );
	
}

////////////////////////////////////////////////////////////////////////////////////
//콜백 함수
function fn_callBackInitInfo(sid, result, data){
	if (!result.success) {
		alert(result.msg);
		return;
	}
	
	//debugger;
	if(!gfn_isNull(result.ds_initInfo.cartCnt))
		$("#h_cartCnt").html(result.ds_initInfo.cartCnt);
}


function fn_callBackHMenu(sid, result, data){
	
	if (!result.success) {
		alert(result.msg);
		return;
	}

	var innerhtml = "";	// 카테고리 전체 
	var cateHtml_1 = "";	// 1레벨 카테고리별 내용
	var cateHtml_2 = "";	// 2레벨 카테고리별 내용
	var cateHtml_3 = "";	// 3레벨 카테고리별 내용
	var cateHtml_4 = "";	// 4레벨 카테고리별 내용
	var topCate = "";
	
	var list = result.ds_catelist;
	for(var i = 0; i < list.length ;i++)
	{
		var cateInfo = list[i];
		if(cateInfo.CATE_LEVEL ==1)	// 1레벨 이면
		{
			cateHtml_1  = "<li class=\"out\"><a href=\"#\">"+ cateInfo.CATE_NAME +"</a> | ";
			cateHtml_1 += "<div id=\"sub_"+cateInfo.CATE_CODE+"\">";
			cateHtml_1 += "cateHtml_2";
			cateHtml_1 += "</div>";
			cateHtml_1 += "</li>";
		}
		
		if(cateInfo.CATE_LEVEL ==2)	// 2레벨 이면 
		{
			cateHtml_2 += "<div class=\"part2\">";
			cateHtml_2 += "<h5><a href=\"#\">"+ cateInfo.CATE_NAME +"</a></h5>";
			cateHtml_2 += "<ul>";
			cateHtml_2 += "cateHtml_3";
			cateHtml_2 += "</ul>";
			cateHtml_2 += "</div>";
		}
		
		if(cateInfo.CATE_LEVEL ==3)	// 3레벨 이면 
		{
			cateHtml_3 += "<li><a href=\"#\">- "+ cateInfo.CATE_NAME +"</a>";
			cateHtml_3 += "<ul>";
			cateHtml_3 += "cateHtml_4";
			cateHtml_3 += "</ul>";
			cateHtml_3 += "</li>";
		}
		
		if(cateInfo.CATE_LEVEL ==4)	// 4레벨 이면 
		{
			cateHtml_4 += "<li><a href=\"#\">· "+ cateInfo.CATE_NAME +"</a></li>";
		}
		
		
		// 다음단계 레벨이 현재 보다 작아지면 정리해야한다.
		var nextLevel = 1;
		if( i+1 == list.length )
			nextLevel = 1;
		else
			nextLevel = list[i+1].CATE_LEVEL;
		
		if( cateInfo.CATE_LEVEL <= nextLevel)
			continue;
		
		
		if(nextLevel <= 3){
			cateHtml_3 = gfn_replaceAll(cateHtml_3, "cateHtml_4", cateHtml_4);
			cateHtml_4 = "";
		}
		
		if(nextLevel <= 2){
			cateHtml_2 = gfn_replaceAll(cateHtml_2, "cateHtml_3", cateHtml_3);
			cateHtml_3 = "";
		}
		
		if(nextLevel == 1){
			cateHtml_1 = gfn_replaceAll(cateHtml_1, "cateHtml_2", cateHtml_2);
			cateHtml_2 = "";
			cateHtml_3 = "";
			cateHtml_4 = "";
			innerhtml += cateHtml_1;
		}
		//-- 다음단계 레벨이 현재 보다 크면 정리해야한다.
	}
	
	$("#gnb").html(innerhtml);
	
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
				<!-- <li id="shortCut"></li> -->
			</ul>

			<select class="select">
				<option value="en-US">미국-영어</option> 
				<option selected="selected" value="ko-KR">한국</option>    
				<option value="zh-CN">중국</option>    
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