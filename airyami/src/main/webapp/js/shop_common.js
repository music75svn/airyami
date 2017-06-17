/* Desc 	: 화면 첫 로드시 호출. 파라미터 기본 셋팅 및 내부 컴포넌트? 셋팅작업 */

function gfn_OnLoad(bNeedLogin){
	if(typeof bNeedLogin == 'undefined')
		bNeedLogin = true;
	
	if(bNeedLogin)
		if(!gfn_checkLogin()) return;
	//gfn_setNavi();
	
	gfn_setType();		// 숫자, 영문 입력만 가능하도록 셋팅하는 부분
	
	gfn_SetCommInit();	// 검색조건중 연도,중 업체명, 사업자번호  

	gfn_SetTotCnt(); // LIST 중 총건수 구하는 부분 셋팅
	
	gfn_SetButton();

	try{
		// 팝업에선 메뉴가 필요없음
		if( !gfn_isNull(SES_USER_ID) ){
			fn_getHeadMenuList();
		}
	}catch(e){}
}

function gfn_setType(){
	$(".onlynum").keyup(function(){$(this).val( $(this).val().replace(/[^0-9]/g,"") );} );		// 숫자만
	$(".onlynum2").keyup(function(){$(this).val( $(this).val().replace(/[^\.0-9]/g,"") );} );	// 소수점 포함
	$(".onlyeng").keyup(function(){$(this).val( $(this).val().replace(/[^\!-z]/g,"") );} );		// 영문만
}


function gfn_checkLogin(){
	if(!gfn_isLogin())
		document.location.href= gfn_getApplication()+"/login/login.do";
	
	return gfn_isLogin();
}


//공통으로 사용되는 컴퍼넌트들을 셋팅해준다.
function gfn_SetCommInit(){
	
	$("td[id^='userLogo'], span[id^='userLogo'], div[id^='userLogo']").each(function() {
		if (!(typeof SES_USER_NAME == 'undefined')) {
			if(!gfn_isNull(SES_USER_NAME)){
				var commTag = "<p class=\"userAdmin\">" + SES_USER_NAME + "</p>";
				$(this).html(commTag);
			}
		}
	});
	
	//var site_id = site_id = "?SITE_ID=" + gfn_getSiteID();
	var site_id = "?";
	
	$("td[id^='top_link'], span[id^='top_link'], div[id^='top_link']").each(function() {
		
		var commTag = "<ul>";
		
		commTag += "<li><a href=\""+ gfn_getApplication() +"/product/shopProductDetail.do?PROD_NO=CN00002\">상품상세</a></li>";

		try{
			alert(SES_USER_TYPE);
			if(SES_USER_TYPE != "C")
				commTag += "<li><a href=\""+ gfn_getApplication() +"/admin/main.do"+site_id+"\">Manager</a></li>";
		}catch(e){};
		
		if(gfn_isLogin()){
			commTag += "<li><a href=\""+ gfn_getApplication() +"/login/logout.do"+site_id+"\">로그아웃</a></li>";
			commTag += "<li><a href=\""+ gfn_getApplication() +"/adminuser/adminUserChgPswd.do"+site_id+"&mode=M\">비밀번호 변경</a></li>";
		}
		else
			commTag += "<li><a href=\""+ gfn_getApplication() +"/login/login.do"+site_id+"\">로그인</a></li>";
			
		commTag += "</ul>";
			
		
		$(this).html(commTag);
	});
	
	
	// 목록 버튼 생성
	$("td[id^='list_link'], span[id^='list_link'], div[id^='list_link']").each(function() {
		var listUrl = "";
		var connect = "?";
		if(!gfn_isNull($('#LISTPARAMS').val()) && !gfn_isNull($('#LISTURL').val())){
			listUrl = location.origin + $('#LISTURL').val();
			if(listUrl.indexOf("?") > -1)
				connect = "&";
			listUrl += connect + "MYPARAMS=" + $('#LISTPARAMS').val();
		}
		else{
			// 리스트 목록이 없는경우
			//var aObj = $('a' , $(this));
			var H_MENU_CD = "";
			var L_MENU_CD = "";
			
			if($('#H_MENU_CD').length > 0){
				H_MENU_CD = "&H_MENU_CD=" + $('#H_MENU_CD').val();
			}
			
			if($('#L_MENU_CD').length > 0){
				L_MENU_CD = "&L_MENU_CD=" + $('#L_MENU_CD').val();
			}
			
			listUrl = $('#LISTURL').val() + "?SITE_ID=" + gfn_getSiteID() + H_MENU_CD + L_MENU_CD;
		}
		
		var title = $(this).attr("title");
		
		if (gfn_isNull(title)) {
			title = "목록";
		}
		
		var commTag = "<a href=\""+listUrl+"\">" + title + " </a>";
		
		$(this).html(commTag);
	});
	
	// 이전 페이지 목록 버튼 생성
	$("td[id^='preLink'], span[id^='preLink'], div[id^='preLink']").each(function() {
		if( $('#findForm').length > 0 ){
			var title = $(this).attr("title");
			
			if (gfn_isNull(title)) {
				title = "이전";
			}
			
			var commTag = "<button type='button' id='btnR_Return'>" + title + "</button>";
			
			$(this).html(commTag);
		}
	});
	
	
	// 돌아가기 버튼
	$("#btnR_Return").click(function() {
		if( $('#findForm').length > 0 ){
			var inputParam = gfn_makeInputData($("#findForm"));
		
			gfn_commonGo(inputParam.FIND_RETURNURL, inputParam, "N");
		}
	});
	
	
	// 이전 페이지 목록 버튼 생성
	$("td[id^='pre_link'], span[id^='pre_link'], div[id^='pre_link']").each(function() {
		if( $('#REFPATH').length > 0 ){
			var title = $(this).attr("title");
			
			if (gfn_isNull(title)) {
				title = "이전";
			}
			
			var commTag = "<a href=\""+ gfn_getApplication() + $('#REFPATH').val()+"\">" + title + " </a>";
			
			$(this).html(commTag);
		}
	});
	
	
	// 이전화면에서 던저준 목폭페이지 조건 셋팅
	if(!gfn_isNull($('#MYPARAMS').val())){
		var arrParam = $('#MYPARAMS').val().split("|");
		var params = new Object();
		
		try{
			for(var i = 0 ; i < arrParam.length; i++){
				var arrPa = arrParam[i].split("=");
				eval("params." + arrPa[0] + " = '" + arrPa[1] + "'");
			}
			gfn_setDetails(params);
		}catch(e){console.log("MYPARAMS setting error!!");}
		
		//$('#MYPARAMS').val("");
	}
	
	
	$( ".datepicker" ).datepicker({
	    dateFormat: 'yy-mm-dd',
	    prevText: '이전 달',
	    nextText: '다음 달',
	    monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
	    monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
	    dayNames: ['일','월','화','수','목','금','토'],
	    dayNamesShort: ['일','월','화','수','목','금','토'],
	    dayNamesMin: ['일','월','화','수','목','금','토'],
	    showMonthAfterYear: true,
	    yearSuffix: '년'
	  });
}


// myParams 에 넘어온 값이 있으면 이전 검색조건 셋팅한다.
function gfn_setMyParams(){
	// 돌아오기로 넘어온 이전 검색조건 셋팅
	if ( $("#myParams").length > 0 ) {
		var myParams = gfn_makeInputData($("#myParams"));
		
		for(_myParams in myParams){
			var key = gfn_replaceAll(_myParams, "FIND_", "");
			$("#"+key).val(myParams[_myParams]);
		}
	}
}

//myParams 에 넘어온 값이 있으면 이전 검색조건 셋팅한다.
function gfn_setMyParams2(){
	// 돌아오기로 넘어온 이전 검색조건 셋팅
	if ( $("#myParams").length > 0 ) {
		var myParams = gfn_makeInputData($("#myParams"));
		
		for(_myParams in myParams){
			var key = gfn_replaceAll(_myParams, "FIND2_", "");
			$("#"+key).val(myParams[_myParams]);
		}
	}
}


/**************************************************************** 
 * Desc 	: 로그인 후.. 접속할 메인 페이지
 *  ****************************************************************/ 
function gfn_goMain(RULE){
	var url = "/shop/main";

	gfn_commonGo(url, "", "");
}


// 상단 네비게이션 생성 -dream 에선 사용할지 아직 미정.
function gfn_setNavi(){
	$("td[id^='NAVI_DIR'], span[id^='NAVI_DIR'], div[id^='NAVI_DIR']").each(function() {
		for(var i =0; i < fn_NAVI_DIR.length; i++){
			if(fn_NAVI_DIR[i][0] == SI_naviDir){
				var commTag = fn_NAVI_DIR[i][1];
				$(this).html(commTag);

				break;
			}
		}
	});
	
	$("td[id^='NAVI_MENU'], span[id^='NAVI_MENU'], div[id^='NAVI_MENU']").each(function() {
		for(var i =0; i < fn_NAVI_SCREEN.length; i++){
			if(fn_NAVI_SCREEN[i][0] == SI_naviMenu){
				var commTag = fn_NAVI_SCREEN[i][1];
				$(this).html(commTag);
				
				break;
			}
		}
	});
}


function gfn_SetButton(){
	if($("#PAGERIGHT").length > 0){

		$('#contents').find('button,span.btn').each(function() {
			var btnId = $(this).attr("id");
			if( btnId.indexOf("btn") == 0 )
				$(this).hide();
		});
		
		var pageRight = $('#PAGERIGHT').val();
		if(gfn_isNull(pageRight)){
			return;
		}
		var rightArr = pageRight.split(",");
		
		
		for(_rightArr in rightArr){
			if(gfn_isNull(rightArr[_rightArr]))
				continue;
			
			$('#contents').find('button,span.btn').each(function() {
				var btnId = $(this).attr("id");
				if( btnId.indexOf("btn" + rightArr[_rightArr]) == 0 )
					$(this).show();
			});
		}
	}
}




// 기간별 사이 정보 생성
function gfn_setGiganInfo(inputParam){
	var giganInfo = new Object();
	var giganArray = new Array();
	var labelArray = new Array();
	
	var jugi = inputParam.JUGI;
	var fDt = inputParam.FDT;
	var tDt = inputParam.TDT;
	
	//TOTAL 셋팅
	var index = 0;
	giganArray[index] = "TOT";
	labelArray[index++] = "전체";
	
	if(jugi == "D"){
		while(1){
			giganArray[index] = gfn_replaceAll(fDt, "-", "");
			labelArray[index++] = fDt;
			fDt = gfn_addDay( fDt, 1, true );
			
			if(fDt > tDt)
				break;
		}
	}
	else if(jugi == "W"){
		fDt = gfn_setWeek( fDt, true);
		tDt = gfn_setWeek( tDt, true);
		var week = 1;
		var preWeek = fDt;
		while(1){
			// 달이 변경되면 week 초기화
			if(preWeek.substring(0,7) != fDt.substring(0,7)){
				week = 1;
				preWeek = fDt;
			}
			
			giganArray[index] = gfn_replaceAll(fDt.substring(0,7), "-", "") + week;
			//labelArray[index++] = fDt.substring(0,4) + "년 "+ fDt.substring(5,7) + "월 " + week++ + "주";
			labelArray[index++] = fDt.substring(5,7) + "월 " + week++ + "주";
			fDt = gfn_addDay( fDt, 7, true );
			
			if(fDt == tDt)
				break;
		}
	}
	else if(jugi == "M"){
		while(1){
			giganArray[index] = gfn_replaceAll(fDt.substring(0,7), "-", "");
			//labelArray[index++] = fDt.substring(0,4) + "년 "+ fDt.substring(5,7) + "월";
			labelArray[index++] = fDt.substring(5,7) + "월";
			fDt = gfn_addMonth( fDt, 1, true );
			
			if(fDt > tDt)
				break;
		}
	}
	else if(jugi == "Y"){
		while(1){
			giganArray[index] = fDt.substring(0,4);
			labelArray[index++] = fDt.substring(0,4) + "년 ";
			fDt = gfn_addMonth( fDt, 12, true );
			
			if(fDt > tDt)
				break;
		}
	}
	
	//TOTAL 셋팅
	//giganArray[index] = "TOT";
	//labelArray[index++] = "전체";
	
	giganInfo.giganArray = giganArray;
	giganInfo.labelArray = labelArray;
	return giganInfo;
}




// 
function gfn_isAdmin(){
	if(SI_admin_yn == 'Y')
		return true;
	else
		return false;
} 







// 체크박스 셋팅..
function gfn_setCheck(obj){
	//var type = obj.attr('type');
	/*
	if(type == 'checkbox'){
	}
	if(type == 'radio'){
		
	}*/
	
	obj.attr('checked', true);
	var label = $('label[for='+obj.attr('id')+']');
	label.addClass('checked');
}

function gfn_setSelect(obj, svalue){
	obj.val(svalue);
	var skey = obj.attr('id');
	
	$dropdown = $("#"+skey).siblings('.sb-dropdown');
    $sbSelect = $("#"+skey).siblings('.sb-select');
    text = $("#"+skey+" > option[value='"+svalue+"']").text();
      
    $sbSelect.val(text);
  
    $dropdown.children().removeClass('selected')
      .filter(':contains(' +text + ')').addClass('selected');
    
    obj.trigger('change'); 
}

function gfn_setRadio(rname, rvalue){
	$(':input:radio[name="'+rname+'"]:input[value="'+rvalue+'"]').attr("checked", true);
	var label = $('label[for='+rname+'_'+rvalue+']');
	label.addClass('checked');
}

/**************************************************************** 
 * Desc 	         						: 화면에서 배너를 호출.
 * @param cWidth					: 배너 img width속성 (default : 100%)
 * 				 cHeight					: 배너 img height속성 (default: auto )
 * 				 cSubstr					: substr 과 같은기능.. 불러오는 배너컨트롤 
 * @return  .append(html)
 * @example
 * <div cid="BANNER"></div> // 전체 조회
 * <div cid="BANNER" cSubstr="2"></div> // 처음~2 조회
 * <div cid="BANNER" cSubstr="2,2"></div> // 2 조회
 * <div cid="BANNER" cSubstr="3,5"></div> // 3 ~ 5 조회 
 * <td class="right" cid="BANNER" cWidth="30px" cHeight="30px" cSubstr="3,3"></div> ..
 ****************************************************************/
function gfn_fn_banner(){
	var siteId = $("#MENU_SITE_ID").val();
	var cidObj = $("[cid=BANNER]");	
	var imgWidth = gfn_isNull(cidObj.attr("cWidth")) ? "100%" : cidObj.attr("cWidth");
	var imgHeight = gfn_isNull(cidObj.attr("cHeight")) ? "auto" : cidObj.attr("cHeight");
	
	var inputParam = new Object();
	inputParam.inputUrl = "/banner/getBannerMainList.do";
	inputParam.data += "&SITE_ID="+siteId;
	
	if (cidObj.length == 0) { 
		return false;
	}
	
	$.ajax({
	    url : gfn_getApplication() + inputParam.inputUrl
	    , type: "post"
	    , data : inputParam.data
	    , dataType	: "json"
	    , contentType: "application/x-www-form-urlencoded; charset=UTF-8"
	    , async		: false
	    , success : function(result, textStatus, data){

	    	var resultList = result.bannerList;
	        var minLimit = resultList[0].RNUM; // 배너 최소값
	        var maxLimit = resultList.length; // 배너 최대값
	        
	        for ( _cidObj in cidObj ) { 
	        	
	        	var subStr			= "";
	    		var cidEle 			= cidObj[_cidObj];
	    		var eleDataVal 	= $(cidEle).attr("cSubstr"); 
	    		var eleImgAltVal	= "IMAGE";
	    		
	    			/** CASE 1 : 배너 부분조회 */ 
	    		if (!gfn_isNull(eleDataVal)) { 
	    			subStr = eleDataVal.split(',');
	    					// 1) MIN ~ MAX 로 배너조회
	    			if (subStr.length == 2) { 
						if (subStr[0] > subStr[1]) {
							var temp = subStr[1];	subStr[1] = subStr[0];	subStr[0] = temp;
		    			}
	    				subStr[0] = subStr[0] < minLimit ? minLimit : subStr[0]; 	 // 첫번째 인자가 최소값보다 적을때  
	    				subStr[0] = subStr[0] > maxLimit ? maxLimit : subStr[0]; // 첫번째 인자가 최대값보다 클때
	    				subStr[1] = subStr[1] > maxLimit ? maxLimit : subStr[1]; // 두번째 인자가 최대값보다 클때
		    			
	    				for (var i = subStr[0]; i <= subStr[1]; i++) { 
	    					$(cidEle).append('<a href="'+resultList[i-1].LINK_URL+'"><img src= "'+resultList[i-1].URL_PATH+'" alt="'+eleImgAltVal+'" width="'+imgWidth+'" height="'+imgHeight+'"/></a>');
	    				}
	    					// 2) ~ MAX 로 배너조회
		    		} else if ( subStr.length == 1) { 
		    			subStr[0] = subStr[0] < 0 ? 0 : subStr[0];
		    			subStr[0] = subStr[0] > maxLimit ? maxLimit : subStr[0];
		    			
		    			for (var i = 1; i <= subStr[0]; i++) { 
	    					$(cidEle).append('<a href="'+resultList[i-1].LINK_URL+'"><img src= "'+resultList[i-1].URL_PATH+'" alt="'+eleImgAltVal+'" width="'+imgWidth+'" height="'+imgHeight+'"/></a>');
	    				}
		    		} 
	    		} else { 
	    			/** CASE 2 : 배너 전체조회 */
	    			for (_resultList in resultList) { 
	    				if ( !gfn_isNull(resultList[_resultList].IMG_ALT)) {
	    					eleImgAltVal = resultList[_resultList].IMG_ALT;
	    				}
	    				$(cidEle).append('<a href="'+resultList[_resultList].LINK_URL+'"><img src="'+resultList[_resultList].URL_PATH+'" alt="'+eleImgAltVal+'" width="'+imgWidth+'" height="'+imgHeight+'"/></a>');	
		    		}
	    		}
	        }
	    },
		error		: function(xhr, errorName, error) {
		    alert("에러가 발생하였습니다. " + xhr.statusText);
		}
	 });
}

/**************************************************************** 
 * Desc 	         						: 싸이트별 메인 화면에서 팝업을 호출
 * @param cWidth					: 배너 img width속성 (default : 100%)
 * @return  .append(html)
 * @example
 * <td class="right" cid="BANNER" cWidth="30px" cHeight="30px" cSubstr="3,3"></div> ..
 ****************************************************************/
function gfn_fn_goPreview( inputParam ){
	if ( inputParam.mode == "view" ) { // 목록&상세용 미리보기
		$.ajax({
		    url : gfn_getApplication() + inputParam.inputUrl
		    , type: "post"
		    , data : inputParam
		    , dataType	: "json"
		    , contentType: "application/x-www-form-urlencoded; charset=UTF-8"
		    , async		: false
		    , success : function(result, textStatus, data){
		    	var popInfo 	= result.ds_list[0];
		    	var popUrl 		= gfn_getApplication() + "/popup/popupPreview.do?popupId=" + popInfo.POPUP_ID + "&mode=view";
		    	var popName 	= popInfo.POPUP_ID;
		    	var popOption = "";
		    	if (popInfo.SIZE_X == "0") {
		    		popInfo.SIZE_X = fa_POP_STYLE["DEFAULT"].width;
		    	}
		    	if (popInfo.SIZE_Y == "0") {
		    		popInfo.SIZE_Y = fa_POP_STYLE["DEFAULT"].height;
		    	} 
		    	popOption 			+= "width=" + popInfo.SIZE_X + ",";
		    	popOption			+= "height=" + popInfo.SIZE_Y + ","; 
		    	popOption			+= popInfo.POSITION_X == "0" ? "left="+Math.ceil(window.screen.width/2 - popInfo.SIZE_X*1) + "," : "left=" + popInfo.POSITION_X + ",";
		    	popOption			+= popInfo.POSITION_Y == "0" ? "top="+Math.ceil(window.screen.width/2 - popInfo.SIZE_Y*1) + "," : "top=" + popInfo.POSITION_Y + ",";

		    	window.open(popUrl, popName, popOption);
		    },
			error		: function(xhr, errorName, error) {
			    alert("에러가 발생하였습니다. " + xhr.statusText);
			}
		 });
		
	} else if ( inputParam.mode == "form" ) { //등록용 미리보기
    	var popUrl 		= gfn_getApplication() + "/popup/popupPreview.do?popupId=popPreview&mode=form";
    	var popName 	= "popPreview";
    	
    	var popOption = "";
    	if (inputParam.SIZE_X == "0" || inputParam.SIZE_X == "") {
    		inputParam.SIZE_X = fa_POP_STYLE["DEFAULT"].width;
    	}
    	if (inputParam.SIZE_Y == "0" || inputParam.SIZE_Y == "") {
    		inputParam.SIZE_Y = fa_POP_STYLE["DEFAULT"].height;
    	} 
    	popOption 			+= "width=" + inputParam.SIZE_X + ",";
    	popOption			+= "height=" + inputParam.SIZE_Y + ","; 
    	popOption			+= inputParam.POSITION_X == "0" ? "left="+Math.ceil(window.screen.width/2 - inputParam.SIZE_X*1) + "," : "left=" + inputParam.POSITION_X + ",";
    	popOption			+= inputParam.POSITION_Y == "0" ? "top="+Math.ceil(window.screen.width/2 - inputParam.SIZE_Y*1) + "," : "top=" + inputParam.POSITION_Y + ",";
    	
    	window.open(popUrl, popName, popOption);
	} else if ( inputParam.mode == "list" ) {
		// @Todo : 메인용 layer Pop 구현
	}
}


/* ********************************************************
 * 트위터 SNS
 * - 트위터 : 트윗내용 140자 이내여야 함
 ******************************************************** */
function fn_cmm_twitter(v_title, v_url) {
	var url = encodeURIComponent(v_url);
	var title = encodeURIComponent(v_title);
	
	var twiturl = "https://twitter.com/?status="+ title + url;
	
	//alert(twiturl);
	
	window.open(twiturl);
	
}

/* ********************************************************
 * SNS 기능
 ******************************************************** */
$(function(){
	
	// 페이스북
	$(".snsFacebook").click(function(e) {
		var thispageurl = document.location.href;
		var url = encodeURIComponent(thispageurl);
		var facebookurl = "http://www.facebook.com/sharer.php?u="+url;
		window.open(facebookurl);
	});
	
	// 구글+
	$(".snsGoogle").click(function(e) {
		var thispageurl = document.location.href;
		var url = encodeURIComponent(thispageurl);
		var google_url = "https://plus.google.com/share?url=" + url;
		window.open(google_url, '', 'menubar=no,toolbar=no,height=600,width=600');
	});
	
	// 트위터 (140자 미만)
	$(".snsTwitter").click(function(e) {
		var thispageTitle = "";
		var thispageurl 	= document.location.href;
		var cutListParam = thispageurl.split("LISTPARAMS"); // 140자 이내라.. param 값 최소한으로 
		thispageurl = cutListParam[0];
			// 태그 내 title 조회 
		if ( $(this).attr("title") == "" ) {
			// 문서 내 title 조회
			var documentTitle 	= $("#title").val();
			var docTitleLen 		= documentTitle == null ? 0 : documentTitle.length;
			
			if ( docTitleLen > 0) {
				thispageTitle = documentTitle;
			} else // 임시제목 
				thispageTitle = "기업가정신";
		} else {
			thispageTitle = $(this).attr("title");
		}
		
		var url = encodeURIComponent(thispageurl);
		var title = encodeURIComponent(thispageTitle);
		var twiturl = "https://twitter.com/?status="+ title + url;
		window.open(twiturl);
	});
});
