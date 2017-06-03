//
function gfn_TransactionMultipart( inputParam ) {
	var frmObj = inputParam.form;
	
	var siteIdObj = frmObj.find("[name=SITE_ID]");
	if(!(siteIdObj.length > 0)){
		frmObj.append("<input type=\"hidden\" name=\"SITE_ID\" id=\"SITE_ID\" value=\""+ gfn_getSiteID() +"\"/>");
	}
	
	var callback = inputParam.callback;
				
	frmObj.ajaxSubmit({
	      url		: gfn_getApplication() + inputParam.url
	    , dataType	: "json"
	    , success	: function(result, textStatus, data) {
    		if(!gfn_isNull(result.pageInfo)){
    			result.pageInfo.responseURL = data.responseURL + "?" + inputParam.data;
		    }
		   
			if( typeof callback != "function"){
				if ( window.fn_callBack ) {
					fn_callBack(inputParam.sid, result, data);    
				}
			}
			else {
				callback(inputParam.sid, result, data);
			}
	    }
	    , error		: function(xhr, errorName, error) {
	    	//alert(gfn_getMsg("fail.common.msg") + xhr.statusText);	// 에러가 발생했습니다!
	    	alert("에러가 발생했습니다!" + xhr.statusText);	// 에러가 발생했습니다!
	    }
	});
}

function gfn_Transaction( inputParam ) {
	var callback = inputParam.callback;
	
	if(typeof inputParam.data == "string"){
		inputParam.data += "&SID=" + inputParam.sid;
		inputParam.data += "&AJAX=" + "Y";
		inputParam.data += "&SITE_ID=" + gfn_getSiteID();
	}
	if(typeof inputParam.data == "object"){
		inputParam.data.SID = inputParam.sid;
		inputParam.data.AJAX = "Y";
		inputParam.data.SITE_ID = gfn_getSiteID();
	}
	
	
	$.ajax({url: ((inputParam.application == false) ? "" : gfn_getApplication()) + inputParam.url
	   , type		: "post"
	   , data		: inputParam.data
	   , dataType	: "json"
	   , contentType: "application/x-www-form-urlencoded; charset=UTF-8"
	   , async		: false
	   , success	: function(result, textStatus, data) {
//		   debugger;
	   		if(!gfn_isNull(result.pageInfo)){
				result.pageInfo.responseURL = data.responseURL + "?" + inputParam.data;
		    }
		   
			if( typeof callback != "function"){
				if ( window.fn_callBack ) {
					fn_callBack(inputParam.sid, result, data);    
				}
			}
			else {
				callback(inputParam.sid, result, data);
			}
	   },
	   error		: function(xhr, errorName, error) {
		   debugger;
	       alert("에러가 발생하였습니다. " + xhr.statusText);
//	       alert(gfn_getMsg("fail.common.msg") + xhr.statusText);	// 에러가 발생했습니다!
	   }
	});
	
}

	
	
// form validation 
function gfn_validationForm(formObj){
	
	var strConArr = formObj.serialize();
	var conArr = strConArr.split('&');
	
	for(var i = 0 ; i < conArr.length; i++){
		var itemArr = conArr[i].split('=');
		var conObj = $("#" + itemArr[0]);

		var depends = conObj.attr("depends");
		var val = conObj.val();
		var title = gfn_strNvl(conObj.attr("title"));

		// depends check
		if( !gfn_isNull( depends ) ){
			depends = "|" + conObj.attr("depends").split(",").join("|") + "|";
			// 필수값체크
			if( depends.indexOf("|required|") > -1){
				if (gfn_isNull(val)) {
//					alert(title + " 필수값입니다.");
					alert(title + gfn_getMsg("common.required.msg"));
					
					conObj.focus();
					return false;
				}
			}

			// 숫자알파벳 체크
			if( depends.indexOf("|englishNumeric|") > -1){
				if (!gfn_checkEnglishNumeric(val)) {
					alert(gfn_getMsg("errors.english_numeric", title));
					conObj.focus();
					return false;
				}
			}
			
			// 알파벳 체크
			if( depends.indexOf("|english|") > -1){
				if (!gfn_checkEnglish(val)) {
					alert(gfn_getMsg("errors.english", title));
					conObj.focus();
					return false;
				}
			}
			
			// 숫자 체크
			if( depends.indexOf("|numeric|") > -1){
				if (!gfn_checkNumber(val)) {
					alert(gfn_getMsg("errors.numeric", title));
					conObj.focus();
					return false;
				}
			}
			
			// 이메일 주소 유효성 체크
			if( depends.indexOf("|email|") > -1){
				if(!gfn_checkEmail(val)) {
//					alert("올바른 형식의 이메일주소가 아닙니다.");
					alert(gfn_getMsg("errors.email", title));
					conObj.focus();
					return false;
				}
			}

		}
		
		// maxlength
		var maxlength = conObj.attr("maxlength");
		// maxlength 를 자체적으로 가지고 있는 경우가 있음 (ckEdit 처럼 -1이 default 값임) 이때는 maxlength가 0 이상일 경우만 처리한다. 
		if( !gfn_isNull( maxlength ) && maxlength > 0){
			// 필수체크는 위에서 처리하였기 때문에 존재할경우만 처리한다.
			if( !gfn_isNull( val ) ){
				if( val.length > maxlength){
					alert(title + " 최대입력길이 " + maxlength + "를 초과하였습니다.");
					conObj.focus();
					return false;
				}
			}
		}
	}
	
	return true;
}






//팝업스타일====================================================================================
var fa_POP_STYLE = new Object(); 

fa_POP_STYLE["DEFAULT"] = "width=700,height=660,center=true,scrollbars=true,status=yes";
fa_POP_STYLE["/board/ArticleHistoryList"] = "width=700,height=660,center=true,scrollbars=true,status=yes";
fa_POP_STYLE["/exchangeRate/exchangeRateDetailPop"] = "width=600,height=400,center=true,scrollbars=false,status=yes";
fa_POP_STYLE["/category/goInsertCate"] = "width=600,height=500,center=true,scrollbars=false,status=yes";

//fa_POP_STYLE["/MCS/MCS0011P"] = "status=yes,scrollbars=yes,resizable=yes,top=50,left=400,width=830,height=760";
//==============================================================================================

/**************************************************************** 
* Desc 	: 공통으로 사용되는 이동함수(이후 권한 관련 포함 예정)
* @Method Name : gfn_commonGo  
* @param   :	1. psUrl : 이동할 url 
* 				2. paParam : 팝업에 전달되는 파라미터(배열형태)
* 				3. psPopupYn : 팝업을 할건지에 대한 플래그
* @return   : 없음
****************************************************************/
function gfn_commonGo(psUrl, paParam, psPopupYn){
	var url = gfn_getApplication() + psUrl + ".do";
	
	// form 생성하자.
	var $form = $('<form></form>');
    $form.attr('action', url);
    $form.attr('method', 'post');
    $form.appendTo('body');
    
    if(psPopupYn == "Y"){
    	
    }
    else{
    	// 핸재 보내는곳의 주소 (돌아올곳.)
    	paParam.FIND_RETURNURL = gfn_getListUrl();
    	// 메뉴변수 셋팅
		gfn_setMenuParams(paParam);
    }
    
    
    for(_paParam in paParam){
    	$form.append($('<input type="hidden" value="'+paParam[_paParam]+'" name="'+_paParam+'">'));
    }
    
//    debugger;
    
    if(psPopupYn == "Y"){
    	var option = (gfn_isNull(fa_POP_STYLE[psUrl])) ? fa_POP_STYLE["DEFAULT"] : fa_POP_STYLE[psUrl];
    	var win = window.open("", psUrl, option);
    	
    	$form.attr('target', psUrl);
    	$form.submit();
    	win.focus();
    }
    else{
    	$form.submit();
    }
    
}

function gfn_commonGo_old(psUrl, paParam, psPopupYn){
	
	var url = gfn_getApplication() + psUrl + ".do";
	var params = "?";
	if(gfn_isNull(paParam))
		paParam = {};
	
	params += "SITE_ID=" + gfn_getSiteID() + "&";
	
	if(psPopupYn == "Y"){
		
		for(_paParam in paParam){
			params += _paParam+"="+paParam[_paParam]+"&";
		}
		params = encodeURI(params);
		
		var sPopStyle = fa_POP_STYLE[psUrl];

		if(sPopStyle == null){
			sPopStyle = fa_POP_STYLE["DEFAULT"];
		}
		
		openWindow(url+params, sPopStyle);
	}
	else if(psPopupYn == "E"){
		
		// 페이지이동에 관련한 변수 셋팅
		gfn_setPageParams(paParam);
		// 메뉴변수 셋팅
		gfn_setMenuParams(paParam);
		
		for(_paParam in paParam){
			params += _paParam+"="+paParam[_paParam]+"&";
		}
		params = encodeURI(params);
		location.href = url+params;
		
	}
	else if(psPopupYn == "N"){
		
		// 페이지이동에 관련한 변수 셋팅
		gfn_setPageParams(paParam);
		// 메뉴변수 셋팅
		gfn_setMenuParams(paParam);
		
		for(_paParam in paParam){
			params += _paParam+"="+paParam[_paParam]+"&";
		}
		params = encodeURI(params);
		window.open(url+params,'_blank');
		
	}
	else if(psPopupYn == "EXCEL"){
		
		for(_paParam in paParam){
			params += _paParam+"="+paParam[_paParam]+"&";
		}
		params = encodeURI(params);
		location.href = url+params;
	}
	else{	// 일반 이동 
		// 메뉴변수 셋팅
		gfn_setMenuParams(paParam);
		
		for(_paParam in paParam){
			params += _paParam+"="+paParam[_paParam]+"&";
		}
		params = encodeURI(params);
		location.href = url+params;
	}
}

// 목록 버튼 클릭시 사용할 내용 전달하기
function gfn_setPageParams(params){
	if ( $("#LISTPARAMS").length > 0 ) {
		params.LISTPARAMS = $("#LISTPARAMS").val(); 
	}
	if ( $("#LISTURL").length > 0 ) {
		params.LISTURL = $("#LISTURL").val(); 
	}
	
	// 목록 url을 변환시켜 컨트럴에 전달하고 이를 컨트럴에서 복원시켜준다.
	if(!gfn_isNull(params.LISTURL))
		params.LISTURL = gfn_replaceAll(params.LISTURL, "&", "|");
}

// 목록 버튼 클릭시 사용할 내용 전달하기
function gfn_setMenuParams(params){
	if ( $("#H_MENU_CD").length > 0 ) {
		params.H_MENU_CD = $("#H_MENU_CD").val(); 
	}
//	if ( $("#H_MENU_NM").length > 0 ) {
//		params.H_MENU_NM = $("#H_MENU_NM").val(); 
//	}
	if ( $("#L_MENU_CD").length > 0 ) {
		params.L_MENU_CD = $("#L_MENU_CD").val(); 
	}
}


function gfn_getListUrl(){
	
	return gfn_replaceAll(location.pathname, ".do", "");
	
	var search = location.search;
	if ( $("#MYPARAMS").length > 0 && !gfn_isNull(search) ) {
		var tmpArr = search.split("&");
		var searchArr = new Array();
		for(var i = 0; i<tmpArr.length ; i++){
			if(tmpArr[i].indexOf("MYPARAMS=") > -1){
				continue;
			}
			if(tmpArr[i].indexOf("MENUON=") > -1){
				continue;
			}
			searchArr.push(tmpArr[i]);
		}
		
		search = searchArr.join("&");
		
		if(search.substr(-1) == "&")
			search = search.substr(0, search.length-1);
	}
	
	return location.pathname + search;
} 
/**************************************************************** 
* Desc 	: 조회버튼 누를시 page 정보가 있는경우 1로 셋팅해준다.
*  ****************************************************************/ 
function gfn_fn_srch(){
	try{
		$('#pageNo').val(1);
		
		if ( $("#SORT_COL").length > 0 ) {
			$("#SORT_COL").val(GC_SORTCOL);
		}
		if ( $("#SORT_ACC").length > 0 ) {
			$("#SORT_ACC").val("");
		}
		
	}
	catch(e){}
	fn_srch();
}


//공통코드 조회  
function gfn_GetCodeList(code, mycombo, option){
	if (typeof option == 'undefined')
		option = ""; 
	
	$.ajax({url: GC_URL+"/commCode/selectCommCode.do",
        type: "post"
      , data: {CODE_GROUP_ID:code}
      , dataType: "json"
      , async: false
      , success: function(response, successName) {
          if (response.success) {
              gfn_callbackGetCodeList(code, mycombo, option, response.ds_list);
          } else {
//              alert("공통코드 조회에 실패하였습니다.");
              alert(gfn_getMsg("word.code") + " " + gfn_getMsg("fail.common.select"));
          }
      },
      error: function(xhr, errorName, error) {
    	  debugger;
          //alert("공통코드 조회 중 에러가 발생하였습니다.");
    	  alert(gfn_getMsg("word.code") + " " + gfn_getMsg("fail.common.select"));
      }
  });
}

// 공통코드 조회 콜백함수
function gfn_callbackGetCodeList(code, mycombo, option, codeList){

	mycombo.find('option').remove();
	// 첫째줄에 추가한다.
	if(!gfn_isNull(option))
		mycombo.append("<option value=''>"+option+"</option>");
	
	for(var i = 0 ; i < codeList.length; i++){
		mycombo.append("<option value='"+codeList[i].CD+"'>"+codeList[i].CD_NM+"</option>");
	}
	
	try{
		fn_callbackGetCodeList(code, mycombo, codeList);
	}catch(e){}
}

//카테고리 조회  
function gfn_GetCategoryList(cateCd, mycombo, option, valueCateCd){
	if (typeof option == 'undefined')
		option = ""; 
	
	$.ajax({url: GC_URL+"/category/selectLowerCateList.do",
        type: "post"
      , data: {UPPER_CATE_CODE:cateCd}
      , dataType: "json"
      , async: false
      , success: function(response, successName) {
          if (response.success) {
        	  gfn_callbackGetCategoryList(cateCd, mycombo, option, response.ds_list, valueCateCd);
          } else {
//              alert("상품분류 조회에 실패하였습니다.");
              alert(gfn_getMsg("word.category") + " " + gfn_getMsg("fail.common.select"));
          }
      },
      error: function(xhr, errorName, error) {
    	  debugger;
          //alert("상품분류 조회 중 에러가 발생하였습니다.");
    	  alert(gfn_getMsg("word.category") + " " + gfn_getMsg("fail.common.select"));
      }
  });
}

//카테고리코드 조회 콜백함수
function gfn_callbackGetCategoryList(cateCd, mycombo, option, cateList, valueCateCd){

	mycombo.find('option').remove();
	// 첫째줄에 추가한다.
	if(!gfn_isNull(option))
		mycombo.append("<option value=''>"+option+"</option>");
	
	for(var i = 0 ; i < cateList.length; i++){
		mycombo.append("<option value='"+cateList[i].CATE_CODE+"'>"+cateList[i].CATE_NAME+"</option>");
	}
	if(!gfn_isNull(valueCateCd)){
		mycombo.val(valueCateCd);
	}
	try{
		fn_callbackGetCategoryList(cateCd, mycombo, cateList, valueCateCd);
	}catch(e){}
}



///////////////////////////////////////////////////////////////////////////////////
// grid 관련 
function gfn_SetTotCnt(totCnt){
	if(totCnt == ""){
		totCnt = "0";
	}
	// 총건수
	$("td[id^='totCnt'], span[id^='totCnt'], div[id^='totCnt']").each(function() {
		var commTag = "<div class=\"txt\">전체  0건</div>";
		$(this).html(commTag);
	});
	
	// 출력건수
	$("td[id^='pageUnit'], span[id^='pageUnit'], div[id^='pageUnit']").each(function() {
		/*var commTag = "<label for=\"pageRowCnt\">출력건수</label>";
		    commTag +="<select id=\"pageRowCnt\" name=\"pageRowCnt\">";
		    commTag +="<option value=\"10\" selected=\"selected\">10건</option>";
		    commTag +="<option value=\"20\">20건</option>";
		    commTag +="<option value=\"30\">30건</option>";
		    commTag +="<option value=\"50\">50건</option>";
		    commTag +="</select>";*/
		
		var type = $(this).attr("type");
		
		
		var commTag = "<label for=\"\">페이지당 출력 줄수</label>";
			commTag +="<p class=\"styledSelect\">";
		    commTag +="<select id=\"pageRowCnt\" name=\"pageRowCnt\" class=\"custom\" style=\"width:70px;\">";
		if(type == "UCC"){
			commTag +="<option value=\"6\" selected=\"selected\">6개씩</option>";
			commTag +="<option value=\"9\">9개씩</option>";
			commTag +="<option value=\"12\">12개씩</option>";
			commTag +="<option value=\"15\">15개씩</option>";
		}
		else{
			commTag +="<option value=\"10\" selected=\"selected\">10개씩</option>";
			commTag +="<option value=\"20\">20개씩</option>";
			commTag +="<option value=\"30\">30개씩</option>";
			commTag +="<option value=\"50\">50개씩</option>";
		}
		commTag +="</select>";
		commTag +="</p>";
		$(this).html(commTag);
	});
	
	var ui = {
			"customSelectBox": function() {
				/**
				 * alternative to custom styling of the SELECT elements
				 */ 
				if (!$.browser.opera) {
					// select element styling
					$('select.custom').each(function(){
						var title = $(this).attr('title');
						var w = $(this).width() + 25;
						var w2 = $(this).width();
						$(this).width(w);
						if( $('option:selected', this).val() != ''  ) title = $('option:selected',this).text();
						$(this)
							.css({'z-index':10,'opacity':0,'-khtml-appearance':'none'})
							.after('<span class="select" style="width:' + w2 + 'px; min-width:' + w2 + 'px;padding-right:25px;">' + title + '</span>')
							.change(function(){
								val = $('option:selected',this).text();
								$(this).next().text(val);
							});
					});
				};//end if
			}, //customSelectBox
				
		};//end ui
	
	ui.customSelectBox();
	
}




/*
 * 답글 관련 토글하기
 * rowObj : 답글입력창이 달릴 로우
 * replayId : 답변입력창 디자인
 * lvl : 답변창의 뎃글 레벨
 */
function gfn_settingReply( rowObj, ulID, lvl, contentId ){
	var replayId = ulID + "_reply";
	if(gfn_isNull(lvl))
		lvl = 0;
	
	lvl = lvl * 1 + 1;
	
	// 수정가능한 답글 달아주기 and 저장, 수정버튼 생성하기
	var cont = "";
	if(!gfn_isNull(contentId)){
		cont = $("*[cid=" + contentId + "]", $(rowObj)).html();
		
		$("#btnCmntReply", $("#" + replayId)).hide();
		$("#btnCmntModify", $("#" + replayId)).show();
	}
	else{
		$("#btnCmntReply", $("#" + replayId)).show();
		$("#btnCmntModify", $("#" + replayId)).hide();
	}
	
	// 답글 영역 초기화 및 셋팅
	$("#REPLY_CONT", $("#" + replayId)).val(cont);
	
	var lClass = $("#" + replayId).attr("lClass");
	
	if(!gfn_isNull(lClass)){
		$("#" + replayId).attr("class", lClass + lvl);
	}
	
	var ulObj = $(rowObj).parent();
	$(rowObj).after($("#" + replayId));
	
	if($("#" + replayId).css("display") =="none"){
		$("#" + replayId).show();
		ulObj.children().each(function(){
			$(this).attr("replyOpen", "N");
		});
		$(rowObj).attr("replyOpen", "Y");
		
	}
	else{
		if( $(rowObj).attr("replyOpen") == "Y" ){
			$("#" + replayId).hide();
			$("#" + ulID+ "_header").after($("#" + replayId));
			$(rowObj).attr("replyOpen", "N");
		}
		else{
			ulObj.children().each(function(){
				$(this).attr("replyOpen", "N");
			});
			$(rowObj).attr("replyOpen", "Y");
		}
	}
	
	
}


var imgInfo = new Object();		// img 정보
imgInfo["FIRST"] = "<img src=\"/dWise/images/admin/paginate_first_FF.gif\" alt=\"처음\" border=\"0\"/>";
imgInfo["LOCK_IMG"] = "<img src=\"/images/common/icon_lock.gif\" alt=\"잠김\" border=\"0\"/>";
imgInfo["ATTACH_IMG"] = "<img src=\"/images/common/icon_file.gif\" alt=\"첨부\" border=\"0\"/>";
imgInfo["xls"] = "<img src=\"/images/common/icon_file.gif\" alt=\"엑셀\" border=\"0\"/>";
imgInfo["ppt"] = "<img src=\"/images/common/icon_file.gif\" alt=\"파워포인트\" border=\"0\"/>";
imgInfo["zip"] = "<img src=\"/images/common/icon_file.gif\" alt=\"압축파일\" border=\"0\"/>";

// 그리드 자동 생성 
function gfn_displayList_ul(list, ulID, HiddenInfo){
	//var ulObj = $("#"+ulID);
	
	var ulIDInfo = new Object();		// ul ID 정보
	var ulPtypeInfo = new Object();		// p , h3 ...
	var ulTypeInfo = new Object();		// TABLE 숫자(NUM), 날짜(DATE) 정보
	var ulClassInfo = new Object();		// class 정보
	var ulImgSrcInfo = new Object();	// 내용이 이미지 일 경우 이미지 경로
	var ulImgAltInfo = new Object();	// 내용이 이미지 일 경우 alt 내용
	var ulSrcInfo = new Object();		// 직접 화면에서 입력한 이미지 경로
	var ulAltInfo = new Object();		// 직접 화면에서 입력한 alt 내용
	var ulATypeInfo = new Object();		// type 정보
	var ulContInfo = new Object();		// 내용이 아이디가 아닌 여러 컬럼의 내용이 한줄로 여러개 표시되어야 할때 사용
	var ulContClassInfo = new Object();	// 여러컬럼의 내용에 각각의 클래스를 지정해야 할 경우
	var ulUrlInfo = new Object();		// 웹접근성용.. 클릭시 다른곳 연동시필요
	var ulClickInfo = new Object();		// click event 정보
	var ulTitleInfo = new Object();		// em title 정보
	var ulConstantInfo = new Object();	// 내용대신들어갈 내용
	var ulChekIDInfo = new Object();	// 입력된 아이디와 동일해야만 뿌려줘야 할 경우
	
	
	var ulParentCIDInfo = new Object();	// 부모 cid 정보
	var ulParentTagInfo = new Object();	// 부모 태그 정보
	
	var index = 0;
	
	$("#"+ ulID + "_header ").children().each(function(){
		setInfo($(this));
	});
	
	// 답글이 존재하는 화면에선 remove시 사라지지 않도록 초기화 해준다.
	if($("#" + ulID+ "_reply").length > 0){
		$("#" + ulID+ "_reply").hide();
		$("#" + ulID+ "_header").after($("#" + ulID+ "_reply"));
	}
	
	
	$("#"+ ulID).children().remove();
	
	
	function setInfo(Obj){
		ulIDInfo[index++] = Obj.attr("cid");
		ulPtypeInfo[Obj.attr("cid")] = gfn_isNull(Obj.attr("pType")) ? Obj.get(0).tagName : Obj.attr("pType");
		ulTypeInfo[Obj.attr("cid")] = Obj.attr("cType");
		ulClassInfo[Obj.attr("cid")] = gfn_isNull(Obj.attr("class")) ? Obj.attr("cClass") : Obj.attr("class");
		ulImgSrcInfo[Obj.attr("cid")] = Obj.attr("imgSrc");
		ulImgAltInfo[Obj.attr("cid")] = Obj.attr("imgAlt");
		ulSrcInfo[Obj.attr("cid")] = Obj.attr("src");
		ulAltInfo[Obj.attr("cid")] = Obj.attr("alt");
		ulATypeInfo[Obj.attr("cid")] = gfn_isNull(Obj.attr("type")) ? Obj.attr("cType") : Obj.attr("type");
		ulContInfo[Obj.attr("cid")] = Obj.attr("cCont");
		ulContClassInfo[Obj.attr("cid")] = Obj.attr("cContClass");
		ulUrlInfo[Obj.attr("cid")] = Obj.attr("url");
		ulClickInfo[Obj.attr("cid")] = Obj.attr("clickevent");
		ulTitleInfo[Obj.attr("cid")] = Obj.attr("cTitle");
		ulConstantInfo[Obj.attr("cid")] = Obj.attr("const");
		ulChekIDInfo[Obj.attr("cid")] = Obj.attr("chkId");
		
		Obj.children().each(function(){
			if( "|DIV|P|SPAN|EM|H1|H2|H3|H4|STRONG|BUTTON|A|IMG|".indexOf("|" + $(this).get(0).tagName + "|")  > -1  ){
				ulParentCIDInfo[$(this).attr("cid")] = Obj.attr("cid");
				ulParentTagInfo[$(this).attr("cid")] = Obj.get(0).tagName;
				setInfo($(this));
			}
		});
	}
	
	
	// 
	var lClass = $("#"+ ulID + "_header ").attr("lClass");
	var lLevel = $("#"+ ulID + "_header ").attr("lLevel");
	if( list ) {
		for( var i = 0; i < list.length; i++ ) {
			
			var liClassHtml = "";
			// level setting 댓글에서만 사용할듯. 레벨에 따라 <li>의 클래스를 변경해준다.
			if( !gfn_isNull(lClass) && !gfn_isNull(lLevel)){
				var level = eval("list["+i+"]." + lLevel) ;
				if( !gfn_isNull(level) ){
					if(level > 0)
						liClassHtml = " class='" + lClass + level + "' ";
				}
			}
			
			
			var arrChildHtml = new Array();	// 자식 html
			var liHtml = "";
			var pHtml = "";
			
			for(_ulIDInfo in ulIDInfo){
				var CID = ulIDInfo[_ulIDInfo];
				var pCid = " cid='" + CID + "' ";
				var pClass = "";
				var pStyle = "";
				var pType = "";
				var pClick = "";
				var pTooltip = "";
				var pTitle = "";
				var pSrc = "";
				var pAlt = "";
				
				// 웹접근성용        	
				var ahefsHtml = "";
				
				var valText = "";
				var rowHtml = "";
				
				
				
				// 로그인아이디 와 작성자아이디 체크 필요시 
				if(!gfn_isNull(ulChekIDInfo[CID])){
					var regId = eval("list["+i+"]." + ulChekIDInfo[CID]);
					if(!gfn_isNull(regId) && !(typeof SES_USER_ID == 'undefined')){
						if(regId != SES_USER_ID){
							continue;
						}
					}
				}
				
				
				// 글 또는 이미지가 된다.
				if(!gfn_isNull(ulImgSrcInfo[CID])){
					var imgSrc = eval("list["+i+"]." + ulImgSrcInfo[CID]) ;
					var imgAlt = eval("list["+i+"]." + ulImgAltInfo[CID]) ;
					if(!gfn_isNull(imgSrc))
						imgSrc = "src='" + imgSrc + "'";
					else
						imgSrc = "";
					
					if(!gfn_isNull(imgAlt))
						imgAlt = "alt='" + imgAlt + "'";
					else
						imgAlt = "";
					
					valText = "<img "  + imgSrc + " " + imgAlt + " />";
				}
				// 여러컬럼이 존재하면 span 으로 이여준다.
				else if(!gfn_isNull(ulContInfo[CID])){
					var arrSpans = ulContInfo[CID].split(",");
					var arrSpanClass = "";
					if(!gfn_isNull(ulContClassInfo[CID])){
						arrSpanClass = ulContClassInfo[CID].split(",");
					}
					
					for(_arrSpans in arrSpans){
						var spanClass = "";
						if(!gfn_isNull(ulContClassInfo[CID])){
							spanClass = " class='" + arrSpanClass[_arrSpans] + "'";
						}
						
						valText += "<span" + spanClass + ">" + eval("list["+i+"]." + arrSpans[_arrSpans]) +"</span>";
					}
				}
				// cid 컬럼 무시하고 상수 내용 체울때 
				else if(!gfn_isNull(ulConstantInfo[CID])){
					valText = ulConstantInfo[CID];
				}
				else{
					valText = gfn_isNull(eval("list["+i+"]." + CID)) ? "" : eval("list["+i+"]." + CID);
				}
				
				
				
				
				// type setting
        		if( !gfn_isNull(ulTypeInfo[CID]) && !gfn_isNull(valText) ){
        			if(ulTypeInfo[CID] == "NUM"){
        				valText = gfn_maskAmt(valText);
        			}
        			else if(ulTypeInfo[CID] == "DATE"){
        				valText = gfn_maskDate(valText);
        			}
        		}
				
				
				// 웹접근성 보안
				if(!gfn_isNull(ulUrlInfo[CID])){
					if(ulUrlInfo[CID] == "#"){
						ahefsHtml = "<a href='" + ulUrlInfo[CID];
					}
					else{
						ahefsHtml = "<a href='" + GC_URL;
						
						ahefsHtml += ulUrlInfo[CID] + "?";
						if(HiddenInfo !=null){
							for(var j = 0 ; j < HiddenInfo.length; j++){
								ahefsHtml += HiddenInfo[j]+"="+eval( "list["+i+"]." + HiddenInfo[j])+"&";
							}
						}
					}
					ahefsHtml += "'>";
					
					valText = ahefsHtml + valText + "</a>"; 
				}
				
				
				// click evnet setting
				if(!gfn_isNull(ulClickInfo[CID])){
					
					pClick = " onClick = \"javascript:"+ulClickInfo[CID]+";return false;\" ";
					pStyle += "cursor : pointer; ";
					
				}
				
				
				
				// class setting
				if(!gfn_isNull(ulClassInfo[CID])){
					pClass = " class='"+ulClassInfo[CID] + "' ";
				}
				
				// title setting
				if(!gfn_isNull(ulTitleInfo[CID])){
					pTitle = " title='"+ulTitleInfo[CID] + "' ";
				}
				
				if(!gfn_isNull(pStyle)){
					pStyle = " style = '"+pStyle+"' ";
        		}
				
				// type setting
				if(!gfn_isNull(ulATypeInfo[CID])){
					pType += " "+ulATypeInfo[CID] + "; ";
				}
				
				// type setting
				if(!gfn_isNull(pType)){
					pType = " type = '"+pType+"' ";
				}
				
				// src setting
				if(!gfn_isNull(ulSrcInfo[CID])){
					pSrc = " src='"+ulSrcInfo[CID] + "' ";
					pAlt = " alt='"+ulAltInfo[CID] + "' ";
				}
				
				// 최후의 생성
				rowHtml += "<"+ulPtypeInfo[CID]+pCid+pClass+pType+pTitle+pStyle+pSrc+pAlt+pClick+pTooltip+">"+ valText +"</"+ulPtypeInfo[CID]+">";
				
				// 부모가 존재하면 저장 자식레벨이면 뒤에서 부모에 추가. 
				if(!gfn_isNull(ulParentCIDInfo[CID])){
					var childInfo = new Object();
					childInfo.pCid = ulParentCIDInfo[CID]; 
					childInfo.pTag = ulParentTagInfo[CID];
					childInfo.mRowHtml = rowHtml;
					
					arrChildHtml.push(childInfo);
				}
				else{
					pHtml += rowHtml;
				}
				
			}// for
			
			// hidden 값 셋팅
			if(HiddenInfo !=null){
				for(var j = 0 ; j < HiddenInfo.length; j++){
					pHtml += "<input type='hidden' name='"+HiddenInfo[j]+"' value='"+eval( "list["+i+"]." + HiddenInfo[j])+"'>";
				}
			}
			
			liHtml = "<li" +liClassHtml+ ">"+pHtml+"</li>";
			
			$('#'+ulID).append(liHtml);
			
			for(var k = 0; k< arrChildHtml.length; k++){
				var pCid = arrChildHtml[k].pCid;
				var pTag = arrChildHtml[k].pTag;
				var mRowHtml = arrChildHtml[k].mRowHtml;
				$( pTag + "[cid=" + pCid + "]", $('#'+ulID + " > li:last")).append(mRowHtml);
			}
			
//        	if(i==1)
//        		debugger;
			
		}//for
		
		if(list.length == 0){
			liHtml = "<li><p class=\"none\">"+gfn_getMsg("common.nodata.msg")+"</p></li>";
			$('#'+ulID).append(liHtml);
        }
		
	}//if
	
	// footer 호출
	try{changeContentSize();}catch(e){}
}

// 그리드 자동 생성 
function gfn_displayListAll(list, rowID, HiddenInfo){
	var rowTagName = $("#"+ rowID + "_header ").get(0).tagName.toLowerCase();
	
	var rowIDInfo = new Object();			// ul ID 정보
	var rowTagNameInfo = new Object();		// p , h3 ...
	var rowTypeInfo = new Object();			// TABLE 숫자(NUM), 날짜(DATE) 정보
	var rowClassInfo = new Object();		// class 정보
	var rowImgSrcInfo = new Object();		// 내용이 이미지 일 경우 이미지 경로
	var rowImgAltInfo = new Object();		// 내용이 이미지 일 경우 alt 내용
	var rowHrefInfo = new Object();			// a tag href
	var rowSrcInfo = new Object();			// 직접 화면에서 입력한 이미지 경로
	var rowAOnclickInfo = new Object();		// attr onclick
	var rowANameInfo = new Object();		// attr Name
	var rowAltInfo = new Object();			// 직접 화면에서 입력한 alt 내용
	var rowATypeInfo = new Object();		// type 정보
	var rowAStyleInfo = new Object();		// style 정보
	var rowContInfo = new Object();			// 내용이 아이디가 아닌 여러 컬럼의 내용이 한줄로 여러개 표시되어야 할때 사용
	var rowContClassInfo = new Object();	// 여러컬럼의 내용에 각각의 클래스를 지정해야 할 경우
	var rowUrlInfo = new Object();			// 웹접근성용.. 클릭시 다른곳 연동시필요
	var rowClickInfo = new Object();		// click event 정보
	var rowTitleInfo = new Object();		// em title 정보
	var rowConstantInfo = new Object();		// 내용대신들어갈 내용
	var rowChekIDInfo = new Object();		// 입력된 아이디와 동일해야만 뿌려줘야 할 경우
	
	var rowAlignInfo = new Object();		// 내용 align
	var rowExtInfo = new Object();			// 튀에 붙을 단어
	var rowSImgInfo = new Object();			// 앞에 붙을 이미지
	var rowEImgInfo = new Object();			// 뒤에 붙을 이미지
	var rowDelTagInfo = new Object();		// 내용을 <del></del> 테그로 감싸기
	var rowLevelInfo = new Object();		// 답변의 경우 들어쓰기
	var rowBtnInfo = new Object();			// <button 정보
	
	var rowParentCIDInfo = new Object();	// 부모 cid 정보
	var rowParentTagInfo = new Object();	// 부모 태그 정보
	
	var index = 0;
	var uniq = 0;							// 아이디가 필요없는 디자인용 구분자 순번
	
	// tr에 적용할 부분
	var rClass = $("#"+ rowID + "_header ").attr("rClass");
	// tr에 적용할 부분 end
	
	$("#"+ rowID + "_header ").children().each(function(){
		setInfo($(this));
	});
	
	// 답글이 존재하는 화면에선 remove시 사라지지 않도록 초기화 해준다.
	if($("#" + rowID+ "_reply").length > 0){
		$("#" + rowID+ "_reply").hide();
		$("#" + rowID+ "_header").after($("#" + rowID+ "_reply"));
	}
	
	
	$("#"+ rowID).children().remove();
	
	function getRowID(Obj){
		if( gfn_isNull(Obj.attr("cid")) ){
			Obj.attr("cid", "UNIQUE" + ++uniq);
			return "UNIQUE" + uniq;
		}
		
		return Obj.attr("cid");
	}
	
	function setInfo(Obj){
		rowIDInfo[index++] = getRowID(Obj);
		rowTagNameInfo[Obj.attr("cid")] = gfn_isNull(Obj.attr("pType")) ? Obj.get(0).tagName : Obj.attr("pType");
		rowTypeInfo[Obj.attr("cid")] = Obj.attr("cType");
		rowClassInfo[Obj.attr("cid")] = gfn_isNull(Obj.attr("class")) ? Obj.attr("cClass") : Obj.attr("class");
		rowImgSrcInfo[Obj.attr("cid")] = Obj.attr("imgSrc");
		rowImgAltInfo[Obj.attr("cid")] = Obj.attr("imgAlt");
		rowHrefInfo[Obj.attr("cid")] = Obj.attr("href");
		rowSrcInfo[Obj.attr("cid")] = Obj.attr("src");
		rowAOnclickInfo[Obj.attr("cid")] = Obj.attr("onclick");
		rowANameInfo[Obj.attr("cid")] = Obj.attr("name");
		rowAltInfo[Obj.attr("cid")] = Obj.attr("alt");
		rowATypeInfo[Obj.attr("cid")] = Obj.attr("type");
		rowAStyleInfo[Obj.attr("cid")] = Obj.attr("style");
		rowContInfo[Obj.attr("cid")] = Obj.attr("cCont");
		rowContClassInfo[Obj.attr("cid")] = Obj.attr("cContClass");
		rowUrlInfo[Obj.attr("cid")] = Obj.attr("url");
		rowClickInfo[Obj.attr("cid")] = Obj.attr("clickevent");
		rowTitleInfo[Obj.attr("cid")] = Obj.attr("cTitle");
		rowConstantInfo[Obj.attr("cid")] = Obj.attr("const");
		rowChekIDInfo[Obj.attr("cid")] = Obj.attr("chkId");
		
		rowAlignInfo[Obj.attr("cid")] = Obj.attr("alg");
		rowExtInfo[Obj.attr("cid")] = Obj.attr("cExt");
		rowSImgInfo[Obj.attr("cid")] = Obj.attr("sImg");
		rowEImgInfo[Obj.attr("cid")] = Obj.attr("eImg");
		
		rowDelTagInfo[Obj.attr("cid")] = Obj.attr("delTag");
		rowLevelInfo[Obj.attr("cid")] = Obj.attr("level");
		rowBtnInfo[Obj.attr("cid")] = Obj.attr("cBtnInfo");
		
		Obj.children().each(function(){
			if( "|TD|DIV|P|SPAN|EM|H1|H2|H3|H4|STRONG|BUTTON|A|IMG|INPUT|".indexOf("|" + $(this).get(0).tagName + "|")  > -1  ){
				var childCid = getRowID($(this));
				rowParentCIDInfo[childCid] = Obj.attr("cid");
				rowParentTagInfo[childCid] = Obj.get(0).tagName;
				setInfo($(this));
			}
		});
	}
	
	
	// 
	var lClass = $("#"+ rowID + "_header ").attr("lClass");
	var lLevel = $("#"+ rowID + "_header ").attr("lLevel");
	if( list ) {
		for( var i = 0; i < list.length; i++ ) {
			
        	
			var rowClassHtml = "";
			// level setting 댓글에서만 사용할듯. 레벨에 따라 <li>의 클래스를 변경해준다.
			if( !gfn_isNull(lClass) && !gfn_isNull(lLevel)){
				var level = eval("list["+i+"]." + lLevel) ;
				if( !gfn_isNull(level) ){
					if(level > 0)
						rowClassHtml = " class='" + lClass + level + "' ";
				}
			}
			
			// 공지 게시판 작용용...
			if(!gfn_isNull(rClass)){
				var rClassCnt = eval("list["+i+"]." + rClass);
				if(!gfn_isNull(rClassCnt))
					rowClassHtml = " class=\"" + rClassCnt + "\" ";
			}
			
			
			var arrChildHtml = new Array();	// 자식 html
			var arrCol = new Array();	// 컬럼내 속성 ( clickevent 먼저 구현 )
			var rowHtml = "";
			var pHtml = "";
			
			for(_rowIDInfo in rowIDInfo){
				var CID = rowIDInfo[_rowIDInfo];
				var pCid = " cid='" + CID + "' ";
				var pClass = "";
				var pStyle = "";
				var pType = "";
				var pClick = "";
				var pTooltip = "";
				var pTitle = "";
				var pHref = "";
				var pOnclick = "";
				var pName = "";
				var pSrc = "";
				var pAlt = "";
				
				// 웹접근성용        	
				var ahefsHtml = "";
				
				var valText = "";
				var curHtml = "";
				
				
				
				// 로그인아이디 와 작성자아이디 체크 필요시 
				if(!gfn_isNull(rowChekIDInfo[CID])){
					var regId = eval("list["+i+"]." + rowChekIDInfo[CID]);
					if(!gfn_isNull(regId) && !(typeof SES_USER_ID == 'undefined')){
						if(regId != SES_USER_ID){
							continue;
						}
					}
				}
				
				
				// 글 또는 이미지가 된다.
				if(!gfn_isNull(rowImgSrcInfo[CID])){
					var imgSrc = eval("list["+i+"]." + rowImgSrcInfo[CID]) ;
					var imgAlt = eval("list["+i+"]." + rowImgAltInfo[CID]) ;
					if(!gfn_isNull(imgSrc))
						imgSrc = "src='" + imgSrc + "'";
					else
						imgSrc = "";
					
					if(!gfn_isNull(imgAlt))
						imgAlt = "alt='" + imgAlt + "'";
					else
						imgAlt = "";
					
					valText = "<img "  + imgSrc + " " + imgAlt + " />";
				}
				// 여러컬럼이 존재하면 span 으로 이여준다.
				else if(!gfn_isNull(rowContInfo[CID])){
					var arrSpans = rowContInfo[CID].split(",");
					var arrSpanClass = "";
					if(!gfn_isNull(rowContClassInfo[CID])){
						arrSpanClass = rowContClassInfo[CID].split(",");
					}
					
					for(_arrSpans in arrSpans){
						var spanClass = "";
						if(!gfn_isNull(rowContClassInfo[CID])){
							spanClass = " class='" + arrSpanClass[_arrSpans] + "'";
						}
						
						valText += "<span" + spanClass + ">" + eval("list["+i+"]." + arrSpans[_arrSpans]) +"</span>";
					}
				}
				// cid 컬럼 무시하고 상수 내용 체울때 
				else if(!gfn_isNull(rowConstantInfo[CID])){
					valText = rowConstantInfo[CID];
				}
				// cid 컬럼 무시하고 버튼 내용으로 변경 
				else if(!gfn_isNull(rowBtnInfo[CID])){
					var btnYn = eval("list["+i+"]." + rowBtnInfo[CID] + "_YN");
        			var btnNm = eval("list["+i+"]." + rowBtnInfo[CID] + "_NM");
        			if(btnYn == "Y"){
        				valText += btnNm;
        			}
        			else{
        				valText += "";
        			}
				}
				else{
					valText = gfn_isNull(eval("list["+i+"]." + CID)) ? "" : eval("list["+i+"]." + CID);
				}
				
				
				// level tag setting
        		if( !gfn_isNull(rowLevelInfo[CID]) ){
        			var level = eval("list["+i+"]." + rowLevelInfo[CID]) ;
        			var space = 0;
        			if( !gfn_isNull(level) ){
        				for(var j = 0; j <level-1;j++){
        					space += 20;
        				}
        				
        				pStyle += "padding-left: "+space+"px; ";
        			}
        		}
        		
        		// algin style setting
        		if(!gfn_isNull(rowAlignInfo[CID])){
        			pStyle += " text-align : "+rowAlignInfo[CID] + ";";
        		}
				
				
				// type setting
				if( !gfn_isNull(rowTypeInfo[CID]) && !gfn_isNull(valText) ){
					if(rowTypeInfo[CID] == "NUM"){
						valText = gfn_maskAmt(valText);
					}
					else if(rowTypeInfo[CID] == "DATE"){
						valText = gfn_maskDate(valText);
					}
				}
				
				// del tag setting
        		if( !gfn_isNull(rowDelTagInfo[CID]) && !gfn_isNull(valText) ){
        			var delTag = eval("list["+i+"]." + rowDelTagInfo[CID]) ;
        			if( delTag == "Y" )
        				valText = "<font style=\"color:red;\">" + valText + "</font>";
        		}
        		
        		// tbExt setting
        		if( !gfn_isNull(rowExtInfo[CID]) && !gfn_isNull(valText) ){
        			var ext = eval("list["+i+"]." + rowExtInfo[CID]) ;
        			if(!gfn_isNull(ext))
        				valText = valText + ext;
        		}
        		
        		// start img setting
        		if( !gfn_isNull(rowSImgInfo[CID]) ){
        			try{
        				var imgsrc = eval("list[i]." + rowSImgInfo[CID]);
        				if(!gfn_isNull(imgsrc)){
        					var imgsrcArr = imgsrc.split(",");
        					var imgHtml = "";
        					for(_imgsrcArr in imgsrcArr){
        						imgHtml += imgInfo[imgsrcArr[_imgsrcArr]];
        					}
        						
        					valText = imgHtml + valText;
        				}
        			}catch(e){console.log(rowSImgInfo[CID] + " 에 해당하는 이미지가 없습니다.");}
        		}
        		
        		// end img setting
        		if( !gfn_isNull(rowEImgInfo[CID])  ){
        			try{
        				var imgsrc = eval("list[i]." + rowEImgInfo[CID]);
        				if(!gfn_isNull(imgsrc)){
        					var imgsrcArr = imgsrc.split(",");
        					var imgHtml = "";
        					for(_imgsrcArr in imgsrcArr){
        						imgHtml += imgInfo[imgsrcArr[_imgsrcArr]];
        					}
        						
        					valText = imgHtml + valText;
        				}
        			}catch(e){console.log(rowEImgInfo[CID] + " 에 해당하는 이미지가 없습니다.");}
        		}
				
				
				// 웹접근성 보안
				if(!gfn_isNull(rowUrlInfo[CID])){
					if(rowUrlInfo[CID] == "#"){
						ahefsHtml = "<a href='" + rowUrlInfo[CID];
					}
					else{
						ahefsHtml = "<a href='" + GC_URL;
						
						ahefsHtml += rowUrlInfo[CID] + "?";
						if(HiddenInfo !=null){
							for(var j = 0 ; j < HiddenInfo.length; j++){
								ahefsHtml += HiddenInfo[j]+"="+eval( "list["+i+"]." + HiddenInfo[j])+"&";
							}
						}
					}
					ahefsHtml += "'>";
					
					valText = ahefsHtml + valText + "</a>"; 
				}
				
				
				// click evnet setting
				if(!gfn_isNull(rowClickInfo[CID])){
					
					pClick = " onClick = \"javascript:"+rowClickInfo[CID]+";return false;\" ";
					pStyle += "cursor : pointer; ";
					
				}
				
				
				
				// class setting
				if(!gfn_isNull(rowClassInfo[CID])){
					pClass = " class='"+rowClassInfo[CID] + "' ";
				}
				
				// title setting
				if(!gfn_isNull(rowTitleInfo[CID])){
					pTitle = " title='"+rowTitleInfo[CID] + "' ";
				}
				
				// style setting
				if(!gfn_isNull(rowAStyleInfo[CID])){
					pStyle = " style='"+rowAStyleInfo[CID] + "' ";
				}
				else if(!gfn_isNull(pStyle)){
					pStyle = " style = '"+pStyle+"' ";
				}
				
				// type setting
				if(!gfn_isNull(rowATypeInfo[CID])){
					pType = " type='"+rowATypeInfo[CID] + "' ";
				}
				else if(!gfn_isNull(pType)){
					pType = " type = '"+pType+"' ";
				}
				
				
				
				// name setting
				if(!gfn_isNull(rowANameInfo[CID])){
					pName = " name='"+rowANameInfo[CID] + "' ";
				}
				
				// href setting
				if(!gfn_isNull(rowHrefInfo[CID])){
					pHref = " href='"+rowHrefInfo[CID] + "' ";
				}
				
				// src setting
				if(!gfn_isNull(rowSrcInfo[CID])){
					pSrc = " src='"+rowSrcInfo[CID] + "' ";
					pAlt = " alt='"+rowAltInfo[CID] + "' ";
				}
				
				// 최후의 생성
				curHtml += "<"+rowTagNameInfo[CID]+pCid+pClass+pType+pTitle+pStyle+pOnclick+pName+pHref+pSrc+pAlt+pClick+pTooltip+">"+ valText +"</"+rowTagNameInfo[CID]+">";
				
				// onclick setting
				if(!gfn_isNull(rowAOnclickInfo[CID])){
//					pOnclick = " onclick='"+rowAOnclickInfo[CID] + "' ";
					var attrInfo = new Object();
					attrInfo.pCid = CID;
					attrInfo.pTag = rowTagNameInfo[CID];
					attrInfo.pAttr = "onclick";
					attrInfo.pValue = rowAOnclickInfo[CID];
					
					arrCol.push(attrInfo);
				}
				
				// 부모가 존재하면 저장 자식레벨이면 뒤에서 부모에 추가. 
				if(!gfn_isNull(rowParentCIDInfo[CID])){
					var childInfo = new Object();
					childInfo.pCid = rowParentCIDInfo[CID]; 
					childInfo.pTag = rowParentTagInfo[CID];
					childInfo.mRowHtml = curHtml;
					
					arrChildHtml.push(childInfo);
				}
				else{
					pHtml += curHtml;
				}
				
			}// for
			
			// hidden 값 셋팅
			if(HiddenInfo !=null){
        		for(var j = 0 ; j < HiddenInfo.length; j++){
        			pHtml += "<input type='hidden' name='"+HiddenInfo[j]+"' />";
        		}
        	}
			
			rowHtml = "<" +rowTagName+rowClassHtml+ ">"+pHtml+"</"+rowTagName+">";
			
			$('#'+rowID).append(rowHtml);
			
			// 자식 만든기
			for(var k = 0; k< arrChildHtml.length; k++){
				var pCid = arrChildHtml[k].pCid;
				var pTag = arrChildHtml[k].pTag;
				var mRowHtml = arrChildHtml[k].mRowHtml;
				$( pTag + "[cid=" + pCid + "]", $('#'+rowID + " > "+rowTagName+":last")).append(mRowHtml);
			}
			
			// 속성 추가
			for(var k = 0; k< arrCol.length; k++){
				var pCid = arrCol[k].pCid;
				var pTag = arrCol[k].pTag;
				var pAttr = arrCol[k].pAttr;
				var pValue = arrCol[k].pValue;
				
				if(pAttr.toLowerCase() == "onclick"){
					$( pTag + "[cid=" + pCid + "]", $('#'+rowID + " > "+rowTagName+":last")).click(pValue);
				}
				else{
					$( pTag + "[cid=" + pCid + "]", $('#'+rowID + " > "+rowTagName+":last")).attr(pAttr, pValue);
				}
			}
			
			// hidden value 셋팅
			if(HiddenInfo !=null){
        		for(var j = 0 ; j < HiddenInfo.length; j++){
        			$("input[name=" + HiddenInfo[j] + "]", $('#'+rowID + " > "+rowTagName+":last")).val(eval( "list["+i+"]." + HiddenInfo[j]));
        		}
        	}
			
//        	if(i==1)
//        		debugger;
			
		}//for
		
		if(list.length == 0){
			rowHtml = "<"+rowTagName+"><p class=\"none\">"+gfn_getMsg("common.nodata.msg")+"</p></"+rowTagName+">";
			$('#'+rowID).append(rowHtml);
		}
		
	}//if
	
	// footer 호출
	try{changeContentSize();}catch(e){}
}

// 그리드 자동 생성 
function gfn_displayList(list, tbID, HiddenInfo){
	var tbObj = $("#"+tbID);
	
	var tbIDInfo = new Object();		// table ID 정보
	var tbAlignInfo = new Object();		// table Align 정보
	var tbTypeInfo = new Object();		// TABLE 숫자(NUM), 날짜(DATE) 정보
	var tbClickInfo = new Object();		// click event 정보
	var tbClassInfo = new Object();		// class 정보
	var tbBtnNMInfo = new Object();		// button 명
	var tbBtnClickInfo = new Object();	// button Clickevent 정보
	var tbTdTypeInfo = new Object();	// td / th 정보
	var tbExtInfo = new Object();		// 튀에 붙을 단어
	var tbSImgInfo = new Object();		// 앞에 붙을 이미지
	var tbEImgInfo = new Object();		// 뒤에 붙을 이미지
	var tbTooltipInfo = new Object();	// 툴팁 설정
	var tbUrlInfo = new Object();		// 웹접근성용.. 클릭시 다른곳 연동시필요
	var tbDelTagInfo = new Object();	// 내용을 <del></del> 테그로 감싸기
	var tbLevelInfo = new Object();		// 답변의 경우 들어쓰기
	
	var index = 0;

	// tr에 적용할 부분
	var rClass = $("#"+tbID+ " > thead > tr ").attr("rClass");
	// tr에 적용할 부분 end
	
	
	$("#"+tbID+ " > thead > tr > th").each(function(){
		if( !gfn_isNull($(this).attr("cid")) ){
			tbIDInfo[index++] = $(this).attr("cid");
			tbAlignInfo[$(this).attr("cid")] = $(this).attr("alg");
			tbTypeInfo[$(this).attr("cid")] = $(this).attr("cType");
			tbClickInfo[$(this).attr("cid")] = $(this).attr("clickevent");
			tbClassInfo[$(this).attr("cid")] = $(this).attr("cClass");
			tbBtnNMInfo[$(this).attr("cid")] = $(this).attr("cBtnNM");
			tbBtnClickInfo[$(this).attr("cid")] = $(this).attr("cBtnClick");
			tbTdTypeInfo[$(this).attr("cid")] = $(this).attr("cTdType");
			tbExtInfo[$(this).attr("cid")] = $(this).attr("cExt");
			tbSImgInfo[$(this).attr("cid")] = $(this).attr("sImg");
			tbEImgInfo[$(this).attr("cid")] = $(this).attr("eImg");
			tbTooltipInfo[$(this).attr("cid")] = $(this).attr("cTooltipId");
			tbUrlInfo[$(this).attr("cid")] = $(this).attr("url");
			tbDelTagInfo[$(this).attr("cid")] = $(this).attr("delTag");
			tbLevelInfo[$(this).attr("cid")] = $(this).attr("level");
		}
	});
	
	$('#'+tbID+ " > tbody > tr").remove();
	
	if( list ) {
        for( var i = 0; i < list.length; i++ ) {
        	var trHtml = "";
        	var tdHtml = "";
        	
        	var rClassHtml = "";
        	if(!gfn_isNull(rClass)){
        		var rClassCnt = eval("list["+i+"]." + rClass);
        		if(!gfn_isNull(rClassCnt))
        			rClassHtml = " class=\"" + rClassCnt + "\" ";
        	}
        	
        	
        	for(_tbIDInfo in tbIDInfo){
        		var CID = tbIDInfo[_tbIDInfo];
        		
        		//if(CID == "BTN111")
//        			debugger;
        		
        		var tdStyle = "";
        		var tdClick = "";
        		var tdClass = "";
        		var tdTooltip = "";
        		var valText = "";
        		// 웹접근성용        	
        		var ahefsHtml = "";
        		
        		valText = eval("list["+i+"]." + CID) ;
        		
        		// class setting
        		if(!gfn_isNull(tbClassInfo[CID])){
        			tdClass = " class='"+tbClassInfo[CID] + "' ";
        		}
        		
        		// style setting
        		if(!gfn_isNull(tbAlignInfo[CID])){
        			tdStyle += " text-align : "+tbAlignInfo[CID] + ";";
        		}
        		
        		// checkbox setting
        		if(CID == "CK"){
        			var useYn = eval("list["+i+"]." + "CK_USE_YN");
        			var disabled = "";
        			if(useYn == "N"){
        				disabled = " disabled ";
        			}
        			tdHtml += "<td"+tdClass+"><input type=\"checkbox\" name=\"check\" "+disabled+" /></td>";
        			continue;
        		}
        		
        		// radio setting
        		if(CID == "RADIO"){
        			tdHtml += "<td"+tdClass+"><input type=\"radio\" name=\"radio\" /></td>";
        			continue;
        		}
        		
        		// button setting
        		// 버튼이거나 비어있거나 (버튼명도 셋팅해야할때) BTN_YN 을 쿼리에서 가지고 온다.
        		if(CID == "BTN"){
        			var btnYn = eval("list["+i+"]." + CID + "_YN");
        			var btnNm = eval("list["+i+"]." + CID + "_NM");
        			if(btnYn == "Y"){
        				tdHtml += "<td"+tdClass+tdStyle+"><span class='btn' style='cursor:pointer;' onClick='"+tbBtnClickInfo[CID]+"'><a>"+btnNm+"</a></span></td>";
        			}
        			else{
        				tdHtml += "<td></td>";
        			}
        			
        			continue;
        		}
        		
        		// 일반적..(버튼 또는 텍스트일경우)
        		if(!gfn_isNull(tbBtnNMInfo[CID])){
        			
        			if(gfn_isNull(valText)){
        				tdHtml += "<td"+tdClass+tdStyle+"><span class='btn' style='cursor:pointer;' onClick='"+tbBtnClickInfo[CID]+"'>"+tbBtnNMInfo[CID]+"</span></td>";
        				continue;
        			}
        		}
        		// button setting END
        		
        		// click evnet setting
        		if(!gfn_isNull(tbClickInfo[CID])){
        			
        			tdClick = " onClick = \"javascript:"+tbClickInfo[CID]+";return false;\" ";
        			tdStyle += "cursor : pointer; ";
        			
        		}
        		
        		
        		// level tag setting
        		if( !gfn_isNull(tbLevelInfo[CID]) ){
        			var level = eval("list["+i+"]." + tbLevelInfo[CID]) ;
        			var space = 0;
        			if( !gfn_isNull(level) ){
        				for(var j = 0; j <level-1;j++){
        					space += 20;
        				}
        				
        				tdStyle += "padding-left: "+space+"px; ";
        			}
        		}
        		
        		
        		if(!gfn_isNull(tdStyle)){
        			tdStyle = " style = '"+tdStyle+"' ";
        		}
        		
        		// tooltip
        		if(!gfn_isNull(tbTooltipInfo[CID])){
        			var tooltipText = eval("list["+i+"]." + tbTooltipInfo[CID]) ;
        			tdTooltip = " title = '"+tooltipText+"' ";
        		}
        		
        		// type setting
        		if( !gfn_isNull(tbTypeInfo[CID]) && !gfn_isNull(valText) ){
        			if(tbTypeInfo[CID] == "CHECK"){
        				var checked = "";
        				if(valText == "Y")
        					checked = "checked='checked'";
        				tdHtml += "<td style='align:center'><input type='checkbox' id='"+CID+"' name='"+CID+"' "+checked+" /></td>";
            			continue;
        			}
        			if(tbTypeInfo[CID] == "NUM"){
        				valText = gfn_maskAmt(valText, true);
        			}
        			else if(tbTypeInfo[CID] == "DATE"){
        				valText = gfn_maskDate(valText);
        			}
        		}
        		
        		// del tag setting
        		if( !gfn_isNull(tbDelTagInfo[CID]) && !gfn_isNull(valText) ){
        			var delTag = eval("list["+i+"]." + tbDelTagInfo[CID]) ;
        			if( delTag == "Y" )
        				valText = "<font style=\"color:red;\">" + valText + "</font>";
        		}
        		
        		// tbExt setting
        		if( !gfn_isNull(tbExtInfo[CID]) && !gfn_isNull(valText) ){
        			var ext = eval("list["+i+"]." + tbExtInfo[CID]) ;
        			if(!gfn_isNull(ext))
        				valText = valText + ext;
        		}
        		
        		// start img setting
        		if( !gfn_isNull(tbSImgInfo[CID]) && !gfn_isNull(valText) ){
        			try{
        				var imgsrc = imgInfo[ eval("list["+i+"]." + tbSImgInfo[CID]) ] ;
        				if(!gfn_isNull(imgsrc))
        					valText = imgsrc + valText;
        			}catch(e){console.log(tbSImgInfo[CID] + " 에 해당하는 이미지가 없습니다.");}
        		}
        		
        		// end img setting
        		if( !gfn_isNull(tbEImgInfo[CID]) && !gfn_isNull(valText) ){
        			try{
        				var imgsrc = imgInfo[ eval("list["+i+"]." + tbEImgInfo[CID]) ] ;
        				if(!gfn_isNull(imgsrc))
        					valText = valText + imgsrc;
        			}catch(e){console.log(tbEImgInfo[CID] + " 에 해당하는 이미지가 없습니다.");}
        		}
        		
        		valText = gfn_strNvl( valText );
        		
        		// 웹접근성 보안
        		if(!gfn_isNull(tbUrlInfo[CID])){
        			ahefsHtml = "<a href='" + GC_URL;
        			
        			ahefsHtml += tbUrlInfo[CID] + "?";
        			if(HiddenInfo !=null){
        				for(var j = 0 ; j < HiddenInfo.length; j++){
        					ahefsHtml += HiddenInfo[j]+"="+eval( "list["+i+"]." + HiddenInfo[j])+"&";
        				}
        			}
        			ahefsHtml += "'>";
        			
        			valText = ahefsHtml + valText + "</a>"; 
        		}
        		
        		if(tbTdTypeInfo[CID] == "th"){
        			tdHtml += "<th"+tdClass+tdStyle+tdClick+tdTooltip+">"+ valText +"</th>";
        		}
        		else{
        			tdHtml += "<td"+tdClass+tdStyle+tdClick+tdTooltip+">"+ valText +"</td>";
        		}
        		
        		
        	}
        	
        	if(HiddenInfo !=null){
        		for(var j = 0 ; j < HiddenInfo.length; j++){
        			tdHtml += "<input type='hidden' name='"+HiddenInfo[j]+"' />";
        		}
        	}
        	
        	trHtml = "<tr "+rClassHtml+">"+tdHtml+"</tr>";
        	
        	//alert(trHtml);
        	//if(i==1)
        	//	debugger;
        	
        	$('#'+tbID+ " > tbody").append(trHtml);
        	
        	if(HiddenInfo !=null){
        		for(var j = 0 ; j < HiddenInfo.length; j++){
        			$("input[name=" + HiddenInfo[j] + "]", $('#'+tbID + " > tbody > tr:last")).val(eval( "list["+i+"]." + HiddenInfo[j]));
        		}
        	}
        	
        }// for
        
        if(list.length == 0){
        	trHtml = "<tr><td colspan=\""+index+"\" style=\"text-align : center;\">"+gfn_getMsg("common.nodata.msg")+"</td></tr>";
        	$('#'+tbID+ " > tbody").append(trHtml);
        }
	}
	
	// footer 호출
	try{changeContentSize();}catch(e){}
}

function gfn_displayTotCnt(totCnt, pType){
	var commTag = "";
	if(pType == "dab")
		commTag = "덧글달기 (" + totCnt + ")";
	else 
		commTag = "<div class=\"txt\">전체  "+totCnt+"건</div>";
	$('#totCnt').html(commTag);
}

var GC_SORTCOL = "";					// 화면에서 사용하는 초기 소팅 조건
function gfn_addGridSort(tbID){
	$("#"+tbID+ " > thead > tr > th").click(function(){
		var CID = $(this).attr("cid");
		if(!("|CK|ROWNUM|ROWNUM_D|BTN|".indexOf("|" + CID + "|") > -1)){
			var sortType = "ASC";
			
			if(!gfn_isNull($(this).attr("sortType"))){
				sortType = $(this).attr("sortType") == "ASC" ? "DESC" : "ASC";
			}
			$(this).attr("sortType",sortType);
			
			gfn_SetSortGrid(CID, sortType);
		}	
	});
	
	if ( $("#SORT_COL").length > 0 ) {
		GC_SORTCOL = gfn_strNvl($("#SORT_COL").val());
	}
}

// 테이블 해더 클릭시 소트 조회
function gfn_SetSortGrid(sortColumn, accInfo){
	
	try{
		$("#SORT_COL").val(sortColumn);
		$("#SORT_ACC").val(accInfo);
		
		fn_srch();
	}catch(e){}
}


//row click evnet
function gfn_addRowClickEvent(tid, fnNm){
	$("#"+tid+" tr").click(function(event) {
		if (event.target.type == 'checkbox') return;
		
		if(!gfn_isNull(fnNm))
			eval( fnNm + "(this);" );
		else
			fn_clickRow(this);
	});
	
	$("#"+tid+" tr").css({"cursor": "pointer"});
}


//Grid 해더 정보 생성
function gfn_setGridHead(tObj, inputParam, gridInfo){
    var tableHtml = "";
    var tbHtml = "";
    var thHtml = "";
    var trHtml = "";
	var tdHtml = "";
	
	var giganInfo = gfn_setGiganInfo(inputParam);
	
	// 해더의 높이 만큼 로우 생성
	for(var row=1 ; row <= gridInfo.headRowCnt; row++){
		tdHtml = "";
		// 기간 길이만큼 생성
		for(var gigan = 0; gigan < giganInfo.giganArray.length; gigan++){
			var obj = eval( eval("gridInfo.headCol.row" + row) );
			if(row==1) obj.text = [giganInfo.labelArray[gigan]];
			// 컬럼 생성
			for(var col=0 ; col < obj.width.length; col++){
				var width = "";
				var colspan = "";
				var text = "";
				var cid = "";
				var alg = "";
				var cType = "";
				var clickevent = "";
				var cClass = "";
				if(!gfn_isNull(obj.width[col])) width = " width='"+obj.width[col]+"'";
				if(!gfn_isNull(obj.colspan[col])) colspan = " colspan='"+obj.colspan[col]+"'";
				if(!gfn_isNull(obj.text[col])) text = obj.text[col];
				if(!gfn_isNull(obj.cid[col])) cid = " cid='"+obj.cid[col]+"_"+giganInfo.giganArray[gigan]+"'";
				if(!gfn_isNull(obj.alg[col])) alg = " alg='"+obj.alg[col]+"'";
				if(!gfn_isNull(obj.cType[col])) cType = " cType='"+obj.cType[col]+"'";
				if(!gfn_isNull(obj.clickevent[col])) clickevent = " clickevent='"+obj.clickevent[col]+"'";
				if(!gfn_isNull(obj.cClass[col])) cClass = " cClass='"+obj.cClass[col]+"'";
					
				tdHtml += "<th"+width+colspan+cid+alg+cType+clickevent+cClass+">"+text+"</th>";
			}
		}
		trHtml += "<tr>"+tdHtml+"</tr>";
		//alert(trHtml);
	}
	
	thHtml = "<thead class='tc'>"+trHtml+"</thead>";
	tbHtml = "<tbody class='tr'><tr></tr></tbody>";
	//alert(thHtml);
	tObj.html(thHtml + tbHtml);
	
	return giganInfo;
}


function gfn_checkAll(pObj, eventSrc, pCheckName) {
	var tbl = $("#"+pObj);
	var name = "";
	if(!gfn_isNull(pCheckName)){
		name = "[name="+pCheckName+"]";
	}
	
	if($(eventSrc).is(":checked")) {
		$(":checkbox" + name, tbl).attr("checked", "checked");
	} else {
		$(":checkbox"+ name, tbl).removeAttr("checked");
	}
	
	try{
		fn_checkAllCallBack(pObj, eventSrc); 
	}
	catch(e){}
}

function gfn_getRadioOk(tbID, pRadioName){
	var SEL_CNT = 0;
	var reqKey = "";
	
	var name = "";
	if(!gfn_isNull(pRadioName)){
		name = "[name="+pRadioName+"]";
	}
	
	$("#"+tbID+ " :radio" + name).each(function(){
		if( $(this).is(":checked") ) {
			var objPrnt = $(this).parent().parent();
			
			SEL_CNT ++;	
			
			reqKey += gfn_replaceAll($('input[type=hidden]', objPrnt).serialize(), "&", ";");
		}
	});
	
	if(SEL_CNT == 0){
		alert('1건 이상 선택후 버튼을 클릭하여 주십시오.');
		return;
	}
	
	return reqKey;
}

function gfn_getCheckOk(tbID, pCheckName){
	var SEL_CNT = 0;
   	var reqKey = "";
   	
   	var name = "";
	if(!gfn_isNull(pCheckName)){
		name = "[name="+pCheckName+"]";
	}
   	
   	$("#"+tbID+ " :checkbox"+name).each(function(){
   		if( $(this).is(":checked") ) {
   			var objPrnt = $(this).parent().parent();
   			
   			SEL_CNT ++;	
   			
   			if(SEL_CNT > 1) {
   				reqKey += '@@';
   			}
   			
   			reqKey += gfn_replaceAll($('input[type=hidden]', objPrnt).serialize(), "&", ";");
   		}
   	});
   	
   	if(SEL_CNT == 0){
   		alert('1건 이상 선택후 버튼을 클릭하여 주십시오.');
   		return;
   	}
   	
   	return reqKey;
}


function gfn_getCheckOkInsrt(tbID, hiddenInfo, pCheckName){
	var SEL_CNT = 0;
	var reqKey = "";
	var name = "";
	if(!gfn_isNull(pCheckName)){
		name = "[name="+pCheckName+"]";
	}
	
	$("#"+tbID+ " :checkbox"+name).each(function(){
		if( $(this).is(":checked") ) {
			var objPrnt = $(this).parent().parent();
			
			SEL_CNT ++;	
			
			if(SEL_CNT > 1) {
				reqKey += '@@';
			}
			
			for(_hiddenInfo in hiddenInfo){
				var hidInfoType = $("[name="+hiddenInfo[_hiddenInfo]+"]",objPrnt).attr("type");
				
				if (hidInfoType == "hidden" || hidInfoType == "text" ) {
					reqKey += hiddenInfo[_hiddenInfo]+"="+$('input[name='+ hiddenInfo[_hiddenInfo] +']', objPrnt).val() + ";";
				} else if (hidInfoType.indexOf("select") >= 0) {
					reqKey += hiddenInfo[_hiddenInfo]+"="+$('select[name='+ hiddenInfo[_hiddenInfo] +']', objPrnt).val() + ";";
				} else
					reqKey += hiddenInfo[_hiddenInfo]+"=;";
			}
		}
	});
	
	if(SEL_CNT == 0){
		alert('1건 이상 선택후 버튼을 클릭하여 주십시오.');
		return;
	}
	
	return reqKey;
}

// table 내의 입력내용을 축출하여 json 으로 리턴한다.
function gfn_getTableData(tbID, pCheckName){
	var inputDataArr = new Array();
	$("#"+tbID+ " > tbody > tr").each(function(){
		if(typeof pCheckName != 'undefined'){
			if(! $('input[name='+pCheckName+']', this).is(":checked") )
				return;
		}
		
		var inputData = {};
		inputDataArr.push(gfn_makeInputData($(this), inputData));
	});
	
	return $.toJSON(inputDataArr);
}

//table 내의 입력내용을 축출하여 json 으로 리턴한다.
function gfn_getTableDataArray(tbID, pCheckName){
	var inputDataArr = new Array();
	$("#"+tbID+ " > tbody > tr").each(function(){
		if(typeof pCheckName != 'undefined'){
			if(! $('input[name='+pCheckName+']', this).is(":checked") )
				return;
		}
		
		var inputData = {};
		inputDataArr.push(gfn_makeInputData($(this), inputData));
	});
	
	return inputDataArr;
}

// 특정영역내 데이터 클리어
function gfn_clearData(pArea){
	pArea.find('input,select,combo,textarea').each(function(i){
		if($(this).attr('type') == 'checkbox' || $(this).attr('type') == 'radio'){
			if($(this).attr('checked')){
				$(this).removeAttr("checked");
			}
		}else{
			$(this).val("");
		}
	});	
}

/**************************************************************** 
 * Desc 	: 목록 상세조회 값 셋팅
 * @Method Name : gfn_setDetails  
 * @param   
 * @return   : 없음
 ****************************************************************/
function gfn_setDetails(oJsonObj, pArea){
	var objs = null;
	if(gfn_isNull(pArea))
		pArea = $('html');
		
	if(oJsonObj == null){
		return;
	}
	
	objs = pArea.find("input");
	for(var i = 0; i< objs.length; i++){
		var skey = objs[i].name;
//		var skey = !gfn_isNull(objs[i].id) ? objs[i].id : objs[i].name;
		
		
		if(skey != null && skey != ''){
			try{
				if(eval("oJsonObj." + skey) || eval("oJsonObj." + skey) == 0 ){
					var svalue = eval("oJsonObj." + skey);
					if(svalue == "null" || svalue == "[object Object]"){
						continue;
					}
					if(objs[i].type == 'radio'){
//						svalue = eval("oJsonObj." + skey);
//						skey = !gfn_isNull(objs[i].name) ? objs[i].name : objs[i].id;
						$(':input:radio[name="'+objs[i].name+'"]:input[value="'+svalue+'"]').attr("checked", true);
					}
					else if(objs[i].type == 'checkbox'){
//						svalue = eval("oJsonObj." + skey);
						var checkArr = svalue.split(",");
						for(var k= 0; k<checkArr.length ; k++){
							$(':input:checkbox[name="'+objs[i].name+'"]:input[value="'+checkArr[k]+'"]').attr("checked", true);
						}
					}
					else{
						objs[i].value = svalue;
					}
					
					if($('#' + skey).attr("isNum") == "Y"){
						//$('#' + skey).toPrice();
						$('#' + skey).val(gfn_maskAmt(svalue, ture));
					}
					
					if($('#' + skey).attr("isDate") == "Y"){
						$('#' + skey).val(gfn_maskDate(svalue));
					}
				}
			}
			catch(e){}
		}
	}
			
	objs = pArea.find("textarea");
	for(var i = 0; i< objs.length; i++){
		var skey = objs[i].id;
		if(skey != null && skey != ''){
			if(eval("oJsonObj." + skey) || eval("oJsonObj." + skey) == 0 ){
				
				var svalue = eval("oJsonObj." + skey);
				if(svalue == "null" || svalue == "[object Object]"){
					continue;
				}
				objs[i].value = svalue;
			}
		}
	}
	
	objs = pArea.find("select");
	for(var i = 0; i< objs.length; i++){
		var skey = objs[i].id;
		if(skey != null && skey != ''){
			if(eval("oJsonObj." + skey) || eval("oJsonObj." + skey) == 0 ){
	
				var svalue = eval("oJsonObj." + skey);
				if(svalue == "null" || svalue == "[object Object]"){
					continue;
				}
				// original
				objs[i].value = svalue;
				// css 특수
				//gfn_setSelect($("#"+skey), svalue);
			}
		}
	}
	
	objs = pArea.find("span");
	for(var i = 0; i< objs.length; i++){
		var skey = objs[i].id;
		if(skey != null && skey != ''){
			try{
				if(eval("oJsonObj." + skey) || eval("oJsonObj." + skey) == 0 ){
	
					var svalue = eval("oJsonObj." + skey);
					if(svalue == "null" || svalue == "[object Object]"){
						continue;
					}
					
					if($('#' + skey).attr("isNum") == "Y"){
						svalue = gfn_maskAmt(svalue, true);
					}
					if($('#' + skey).attr("isDate") == "Y"){
						svalue =  gfn_maskDate(svalue);
					}
					objs[i].innerHTML = svalue;
				}
			}catch(e){}
		}
	}
	
	objs = pArea.find("img");
	for(var i = 0; i< objs.length; i++){
		var skey = objs[i].id;
		var altKey = objs[i].id + "_ALT";
		
		if((skey != null && skey != '')){
			try{
				var svalue = "";
				var altValue = "IMAGE";
				
				if(eval("oJsonObj." + skey) || eval("oJsonObj." + skey) == 0 ){
					svalue = eval("oJsonObj." + skey);
					if(svalue == "null" || svalue == "[object Object]"){
						continue;
					}
					objs[i].src = svalue;
				}
				
				if(eval("oJsonObj." + altKey) || eval("oJsonObj." + altKey) == 0 ){
					altValue = eval("oJsonObj." + altKey);
					objs[i].alt = altValue;
				} else {
					objs[i].alt = altValue;
				}
			}catch(e){}
		}
	}
	
	objs = pArea.find("td,th");
	for(var i = 0; i< objs.length; i++){
		var skey = objs[i].id;
		if(skey != null && skey != ''){
			if(eval("oJsonObj." + skey) || eval("oJsonObj." + skey) == 0 ){
				
				var svalue = eval("oJsonObj." + skey);
				if(svalue == "null" || svalue == "[object Object]"){
					continue;
				}
				
				if($('#' + skey).attr("isNum") == "Y"){
					svalue = gfn_maskAmt(svalue);
				}
				if($('#' + skey).attr("isDate") == "Y"){
					svalue =  gfn_maskDate(svalue);
				}
				
				objs[i].innerHTML = svalue;
			}
		}
	}
	
	objs = pArea.find("a");
	for(var i = 0; i< objs.length; i++){
		var skey = objs[i].id;
		if(skey != null && skey != ''){
			try{
				if(eval("oJsonObj." + skey) || eval("oJsonObj." + skey) == 0 ){
	
					var svalue = eval("oJsonObj." + skey);
					if(svalue == "null" || svalue == "[object Object]"){
						continue;
					}
					$(objs[i]).attr("href", svalue);
				}
			}catch(e){}
		}
	}
	
	
	// footer 호출
	try{changeContentSize();}catch(e){}
}

/////////////////////////////////////////////////////////////////////

//각 화면의 소팅 컬럼 지정및 acc 정보 입력
function gfn_SetSortGrid(sortColumn, accInfo){
	
	try{
		$("#SORT_COL").val(sortColumn);
		$("#SORT_ACC").val(accInfo);
		
		fn_srch();
	}catch(e){}
}


//
function gfn_sortSrch(){
	var $sortingTable = $(".sorting");
	var obj = $sortingTable;
}



/****************************************************************
 * Desc 	: 날짜 필드 validation  (날짜 유효성 + 크로스체크 + 기간체크)
 * 작성일 	: 2014-03-19 (limalove)
 * @param   psStartDate : 시작일
 * 			psEndDate : 종료일
 * 			psType : (D =일 M= 월) 필요없으면 안써도됨.
 * 			psGigan : (1 : 15일 or 2 : 30일 or 3 : 2 개월 or 4 : 3개월 or 5 : 1년 or  6 : 6개월 )
 * 			psisAlert : 알럿창을 띄울것인가?  true/false    필요없으면 안써도됨.
 * @return  bValid : validateion 결과
 * 사용 예 	:
	if(!gfn_DateCrossCheckAll("CalFrom", "CalTo"))					return false;	// 유효성 + 크로스체크만 체크
	if(!gfn_DateCrossCheckAll("CalFrom", "CalTo", "D"))				return false;	// 유효성 + 크로스체크만 체크
	if(!gfn_DateCrossCheckAll("CalFrom", "CalTo", "D", 1))			return false;	// 유효성 + 크로스체크 + 기간체크
	if(!gfn_DateCrossCheckAll("CalFrom", "CalTo", "D", 1, false))	return false;	// 유효성 + 크로스체크 + 기간체크 알럿은 필요없을경우 사용
****************************************************************/
function gfn_DateCrossCheckAll(psStartDate, psEndDate, psType, psGigan, psisAlert) {
	var Option = 0;
	var isAlert = true;
	var extraDay = "";
	var fromObj = null;
	var toObj = null;
	
	if(!gfn_isNull(psType)){
		Option = (psType == "M" ? 1 : 0);
		if(psType == "M") extraDay = "01";
	}
	
	if(!gfn_isNull(psisAlert)){
		isAlert = psisAlert;
	}
	
	if (typeof psStartDate == "object")
		fromObj = psStartDate;
	else
		fromObj = $("#" + psStartDate);
	
	if (typeof psEndDate == "object")
		toObj = psEndDate;
	else
		toObj = $("#" + psEndDate);
	
	// 날짜유효성 체크
	if(!gfn_isValidDateCheck(fromObj, fromObj.val() + extraDay, isAlert))	return false;
	if(!gfn_isValidDateCheck(toObj, toObj.val()  + extraDay, isAlert))	return false;
	
	
	// 날짜 cross 체크
	if(!gfn_dateCrossCheck(fromObj.val() + extraDay, toObj.val()  + extraDay, false, isAlert)){
		fromObj.focus();
		return false;
	}
	
	// 기간체크값이 있으면 아래를 호출, 일자체크일 경우에만 기간체크가 가능하다.
	if(!gfn_isNull(psGigan) && Option == 0 ){
		if(!gfn_calcDayCheck(psStartDate, psEndDate, psGigan, isAlert))	return false;
	}
	
	return true;
}

// 날짜 입력값 이 정상인지 유효성 확인 
function gfn_isValidDateCheck(dtObj, value, psisAlert){
	var isAlert = true;
	if (!gfn_isNull(psisAlert)) isAlert = psisAlert; 
	if(!gfn_isNull(value)){
		value = gfn_replaceAll(value, "-", "");
		if (value.length == 8) {
			vDate = new Date();
			vDate.setFullYear(value.substring(0, 4));
			vDate.setMonth(value.substring(4, 6));
			vDate.setDate(value.substring(6));

			if (vDate.getFullYear() != value.substring(0, 4)
					|| vDate.getMonth() != value.substring(4, 6)
					|| vDate.getDate() != value.substring(6)) {
				if(isAlert){
					alert(dtObj.attr("title") + " 날짜형식이 틀렸습니다.");
//					alert(gfn_getMsg("errors.date", dtObj.attr("title")));
					dtObj.focus();
				}
				
				return false;
			}
			return true;
		}
	}
}


/*******************************************************************************
 * Desc : 날짜 필드 validation
 * 
 * @param psStartDate :
 *            시작일, psEndDate : 종료일, pfAllneed : 모두 필수값 여부
 *             		psisAlert						: 알렷 여부 디폴트 true
 * @return bValid : validateion 결과
 ******************************************************************************/
function gfn_dateCrossCheck(psStartDate, psEndDate, pfAllneed, psisAlert) {
	var isAlert = true;
	if (!gfn_isNull(psisAlert)) isAlert = psisAlert; 
	var bValid = true;
	if( !gfn_isNull(psStartDate) || !gfn_isNull(psEndDate) ){
		if(!gfn_isNull(psStartDate) && !gfn_isNull(psEndDate)){
			bValid = gfn_compareDateValue(psStartDate, psEndDate, psisAlert);
		}else{
			if(isAlert)	alert("시작, 종료일 모두 입력하셔야합니다.");
			bValid = false;
		}
	}else{
		if(pfAllneed){
			if(isAlert)	alert("시작, 종료일 모두 입력하셔야합니다.");
			bValid = false;
		}
	}
	
	return bValid;
}


/**************************************************************** 
 * Desc 	         						: 날짜크기비교
 * @param  입력인자  						:
 * 			fromDate 						: 시작일
 * 			toDate							: 종료일
 * 			psisAlert						: 알렷 여부 디폴트 true
 * @return  Boolean
 ****************************************************************/
function gfn_compareDateValue(fromDate, toDate, psisAlert){
	var isAlert = true;
	if (!gfn_isNull(psisAlert)) isAlert = psisAlert; 
	var from_dt;
    var to_dt; 
    var fromDate = gfn_replaceAll(fromDate, "-", "");
    var toDate = gfn_replaceAll(toDate, "-", "");
    
    //년월일체크 
    if (fromDate.length != 8 || toDate.length != 8){
    	if(isAlert) alert("날짜 형식이 잘못 되었습니다.");
        return false;
    }
    
    var s_year = fromDate.substring(0,4);
    var s_month = (Number(fromDate.substring(4,6)) - 1) + "";
    var s_day = fromDate.substring(6,8);
    var e_year = toDate.substring(0,4);
    var e_month = (Number(toDate.substring(4,6)) - 1) + "";
    var e_day = toDate.substring(6,8);
    
    from_dt = new Date(s_year, s_month, s_day);
    to_dt = new Date(e_year, e_month, e_day);
    
    if( ( to_dt.getTime() - from_dt.getTime() ) < 0 ){
    	if(isAlert)	alert('시작일 또는 종료일을 다시 확인해주시길 바랍니다.');
        return false;
    }
    else {
        return true;
    }
}

/**************************************************************** 
 * 조회일자 기간 체크
 * 
 * @param strObj 시작일자 Component의 ID
 * @param endObj 종료일자 Component의 ID
 * @param term 기간 구분 (D7 : 7일, D15 : 15일, M1 : 한달, M3 : 석달)
 * @param psisAlert alert 사용여부
 * @returns {Boolean}
****************************************************************/ 
function gfn_calcDayCheck(psStartDate, psEndDate, pTerm, psisAlert) {
	var isAlert = true;
	if (!gfn_isNull(psisAlert)) isAlert = psisAlert; 
	var fromObj = null;
	var toObj = null;
	var Option = "";
	var term = 0;
	var fromDt = "";
	
	if(!gfn_isNull(pTerm)){
		Option = pTerm[0];
		term = Number(pTerm.substr(1));
	}
	
	if (typeof psStartDate == "object")
		fromObj = psStartDate;
	else
		fromObj = $("#" + psStartDate);
	
	if (typeof psEndDate == "object")
		toObj = psEndDate;
	else
		toObj = $("#" + psEndDate);
	
	if(Option == "D") {
		fromDt = gfn_addDay( fromObj.val(), term, true );
	}
	else if(Option == "M"){
		fromDt = gfn_addMonth( fromObj.val(), term, true );
	}
	
	
	if(!gfn_isNull(fromDt)){
		if(gfn_compareDateValue(fromDt, toObj.val(), false)){
			if(isAlert)	alert('허용된 검색기간을 초과하였습니다.');
			fromObj.focus();
			return false;
		}
	}
	
	return true;
}



/**************************************************************** 
 * Desc 	: 오늘 날짜 반화
 * @Method Name : fnGetToday
 *@조건      
 * @param    bool : '-'포함여부
 * @return   Y == true 나머지 false 
 ****************************************************************/ 
function gfn_getToday(bool) {
	var sToday = new Date();
	var y = sToday.getFullYear();
	var m = parseInt(sToday.getMonth())+1;
	var d = sToday.getDate();
	var sReturn = null;
	
	if(bool)
		sReturn =   y + "-" + m.toString().lpad(2, 0) + "-" + d.toString().lpad(2, 0); 
	else
		sReturn =   y + m.toString().lpad(2, 0) + d.toString().lpad(2, 0); 
	
	return sReturn;
}


/**************************************************************** 
 * Desc 	         						: 몇일 후 날짜 계산 
 * @Method Name      						: fn_addDay
 * @param  입력인자  						:
 * 			dateTime						: 년월일
 * 			addDay							: 일수
 * 			bool							: '-'으로 구분할지의 여부
 * @return  String
 ****************************************************************/
function gfn_addDay( yyyyMMdd, addDay, bool )
{

   if( yyyyMMdd == "" ){
       return;
   }
   
   if( yyyyMMdd.indexOf("-") != -1){
       yyyyMMdd = yyyyMMdd.replace(/(\,|\-|\:)/g,""); 
   }
   
   var y = yyyyMMdd.substring(0,4);
   var m = yyyyMMdd.substring(4,6);
   var d = yyyyMMdd.substring(6);

   //1970.01.01 날짜 계산이 시작되는 기준일
   //기준일
   var basic = new Date(0);
   
   //더할 날짜 
   var now = new Date(y,m-1,d);

   //오늘부터 몇일후 long 형태 
   var count;

   count = Number(addDay);

   //기준일부터 몇일후의 날짜를 알 수 있다
   //1000*60*60*24 는 하루를 나타냄
   //(now - basic) / (1000*60*60*24) 오늘부터 기준일까지의 날짜수
   //즉 기준일로부터 몇일까지의 날짜수를 Long형태로 만들어 날짜를 만들어낸다.
   var day = new Date((1000*60*60*24*(count+((now-basic)/(1000*60*60*24)))));

   var mm = (day.getMonth()+1);
   var dd = day.getDate();
   if(mm < 10) mm = "0" + mm;
   if(dd < 10) dd = "0" + dd;
   
   if(!fn_isEmpty(bool)){
	   if(bool){
		   return day.getFullYear() + '-' + mm + '-' + dd;
	   }
   }
   
   return day.getFullYear() + mm + dd;
}

/**************************************************************** 
 * Desc 	         						: 입력한날의 다음주 첫번째날을 리턴한다. 
 * @Method Name      						: fn_addDay
 * @param  입력인자  						:
 * 			dateTime						: 년월일
 * 			bool							: '-'으로 구분할지의 여부
 * @return  String
 ****************************************************************/
function gfn_setWeek( yyyyMMdd, bool )
{
	
	if( yyyyMMdd == "" ){
		return;
	}
	
	if( yyyyMMdd.indexOf("-") != -1){
		yyyyMMdd = yyyyMMdd.replace(/(\,|\-|\:)/g,""); 
	}
	
	var y = yyyyMMdd.substring(0,4);
	var m = yyyyMMdd.substring(4,6);
	var d = yyyyMMdd.substring(6);
	
	//1970.01.01 날짜 계산이 시작되는 기준일
	//기준일
	var basic = new Date(0);
	
	//더할 날짜 
	var now = new Date(y,m-1,d);
	
	var week = now.getDay();
	
	if( week == 0 )
		return gfn_addDay( yyyyMMdd, 0, bool );
	else
		return gfn_addDay( yyyyMMdd, 7-week, bool );
}


/**************************************************************** 
 * Desc 	         						: 몇개월 후 날짜 계산 
 * @Method Name      						: fn_addMonth
 * @param  입력인자  							:
 * 			dateTime						: 년월일
 * 			addDay							: 개월수
 * 			bool							: '-'으로 구분할지의 여부
 * @return  String
 ****************************************************************/
function gfn_addMonth( yyyyMMdd, addMonth, bool )
{

   if( yyyyMMdd == "" ){
       return;
   }
   
   if( yyyyMMdd.indexOf("-") != -1){
       yyyyMMdd = yyyyMMdd.replace(/(\,|\-|\:)/g,""); 
   }
   
   var y = yyyyMMdd.substring(0,4);
   var m = yyyyMMdd.substring(4,6);
   var d = yyyyMMdd.substring(6);

   //더할 날짜 
   var count = Number(addMonth);
   var day = new Date(y,m-1+count,d);

   var mm = (day.getMonth()+1);
   var dd = day.getDate();
   if(mm < 10) mm = "0" + mm;
   if(dd < 10) dd = "0" + dd;
   
   if(!fn_isEmpty(bool)){
	   if(bool){
		   return day.getFullYear() + '-' + mm + '-' + dd;
	   }
   }
          
   return day.getFullYear() + mm + dd + "";
}

/******************************************************************************
 * Desc 	         						: 윤년여부 확인 
 * @Method Name      						: fn_isLeapYear
 * @param  입력인자  							: yyyyMMdd형태의 날짜 ( 예 : "20121122" )
 * @return  boolean							: - sDate가 윤년인 경우 = true
 *				  							  - sDate가 윤년이 아닌 경우 = false
 *   										  - sDate가 입력되지 않은 경우 = false
 *******************************************************************************/
function gfn_isLeapYear(sDate){
    var ret;
    var nY;

    if( fn_isEmpty(sDate) )    
    	return false;

    nY = parseInt(sDate.substring(0,4), 10);

    if ((nY % 4) == 0) {
        if ((nY % 100) != 0 || (nY % 400) == 0) 
            ret = true;
        else 
            ret = false;
    }else 
        ret = false;

    return ret;
}

/******************************************************************************
 * Desc 	         						: 해당월의 마지막 날짜를 숫자로 구하기 
 * @Method Name      						: fn_lastDateNum
 * @param  입력인자  							: yyyyMMdd형태의 날짜 ( 예 : "20121122" )
 * @return  boolean							: - 성공 = 마지막 날짜 숫자값 ( 예 : 30 )
 *				  							  - 실패 = -1
 *******************************************************************************/
function gfn_lastDateNum(sDate)
{
    var nMonth, nLastDate;

    if( fn_isEmpty(sDate) )		
		return -1;

    nMonth = parseInt(sDate.substr(4,2), 10);
    if( nMonth == 1 || nMonth == 3 || nMonth == 5 || nMonth == 7  || nMonth == 8 || nMonth == 10 || nMonth == 12 )
        nLastDate = 31;
    else if( nMonth == 2 )  
    {
        if( gfn_isLeapYear(sDate) == true )
            nLastDate = 29;
        else
            nLastDate = 28;
    } 
    else 
        nLastDate = 30;
        
    return nLastDate;
}

/////////////////////////////////////////////////////////////////////

/**************************************************************** 
 * Desc 	: 달력 onload 에서 처리
 *  ****************************************************************/
//function gfn_setCalender(objArr){
//	var calObjArr = objArr.split(",");
//	var calObjs = "";
//	
//	for(_calObjArr in calObjArr){
//		calObjs += "#" + calObjArr[_calObjArr] + " , ";
//	}
//	
//	$( calObjs ).datepicker({
//	  dateFormat: 'yy-mm-dd',
//	  prevText: '이전 달',
//	  nextText: '다음 달',
//	  monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
//	  monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
//	  dayNames: ['일','월','화','수','목','금','토'],
//	  dayNamesShort: ['일','월','화','수','목','금','토'],
//	  dayNamesMin: ['일','월','화','수','목','금','토'],
//	  showMonthAfterYear: true,
//	  yearSuffix: '년'
//	});
//}

/**************************************************************** 
 * Desc 	: 엑셀 다운로드 숨김 팝업 호출
 *  ****************************************************************/ 
function gfn_isLogin(){
	var isLogin = false; 
	
	try{
		return SES_LOGIN;
	}catch(e){}
	
	return isLogin;
}

/**************************************************************** 
 * Desc 	: 엑셀 다운로드 숨김 팝업 호출
 *  ****************************************************************/ 
function gfn_excelDownload(psUrl, paParam){
	gfn_commonGo(psUrl, paParam, "EXCEL");
}

/**************************************************************** 
 * Desc 	: 파일 다운로드 숨김 팝업 호출
 *  ****************************************************************/
function gfn_fileDownload(paParam){
	var url = "/COM/download";
	gfn_commonGo(url, paParam, "EXCEL");
}

function gfn_downFile(mstId, dtlId){
	var inputParam = new Object();
	inputParam.FILE_MST_SEQ		= mstId;
	
	if(!(typeof(dtlId) == 'undefined'))
		inputParam.FILE_DTL_SEQ		= dtlId;
	
	gfn_fileDownload(inputParam);
}

/**************************************************************** 
 * Desc 	: 검색조건내 Div 내의 input type을 검색하여 검색조건을 만든다.
 *  ****************************************************************/
function gfn_makeSearchCondition(oSearchArea) {
	var arrCondition = new Array();
	var sCondition = "";
	
	var j = 0;	// 실제 배열의 크기
	
	oSearchArea.find('input,select,combo,textarea').each(function(i){
		if(!gfn_isNull($(this).attr('title')) && !gfn_isNull($(this).val()) ){
			if($(this).attr('type') == 'checkbox' || $(this).attr('type') == 'radio'){
				if($(this).attr('checked')){
					arrCondition[j++] = $(this).attr('title') + "=" + $(this).val();
				}
			}
			else if($(this).get(0).tagName == "SELECT"){
				arrCondition[j++] = $(this).attr('title') + "=" + $('option:selected',this).text();
			}
			else{
				arrCondition[j++] = $(this).attr('title') + "=" + $(this).val();
			}
		}
		
	});
	
	
	/*
	var objs = oSearchArea.find("label");
	for(var i=0; i < objs.length; i++){
		var lid = objs[i].id;
		var skey = $('#' + lid).attr("for");

		if( $('#' + skey).length > 0 && !gfn_isNull($('#' + skey).val()) ){
			arrCondition[j++] = objs[i].innerText + "=" + $('#' + skey).val();
		}
	}
	*/
	
	sCondition = arrCondition.join("|");

	return sCondition;
}

/**************************************************************** 
 * Desc 	: 검색조건내 Div 내의 input type을 검색하여 검색조건을 만든다.
 *  ****************************************************************/
function gfn_makeInputData(oSearchArea, inputData, prefix) {
	if( gfn_isNull(inputData) ){
		inputData = new Object();
	}
	inputData = $.extend(inputData, oSearchArea.fn_getInputdata(prefix));
	
	oSearchArea.find('input[isNum="Y"]').each(function(i){
		inputData[$(this).attr('id')] = $(this).getOnlyNumeric();
	});
	
	return inputData;
}

/**************************************************************** 
 * Desc 	: 검색조건내 Div 내의 input type을 검색하여 query string으로 만들기
 *  ****************************************************************/
function gfn_serialize(oSearchArea, inputParam){
	inputParam = gfn_makeInputData(oSearchArea, inputParam);
	return jQuery.param(inputParam);
}


/**************************************************************** 
 * Desc 	: 검색조건내 Div 내의 input type을 검색하여 disabled 상태로 만든다.
 *  ****************************************************************/
function gfn_makeObjDisable(oSearchArea, bDisabled) {
	var disable = gfn_isNull(bDisabled) ? true :  bDisabled;
	oSearchArea.find('input,select,combo,textarea').each(function(i){
		$(this).attr("disabled",disable);
	});
}

/**************************************************************** 
 * Desc 		: jquery object 내의 input,select,combo,textarea를 검색하여 (id or name):value의 json을 반환한다.
 * @Method Name : $.fn.fn_getInputdata  ex) var data = $('#id').fn_getInputdata();
 * @return  json 
 ****************************************************************/
$.fn.fn_getInputdata = function(prefix){
	prefix = gfn_null(prefix, "");
	var data = {};
	var setVal = function(obj){
		//var id = (gfn_isNull(obj.attr('id')))? obj.attr('name'):obj.attr('id');
		var id = (gfn_isNull(obj.attr('name')))? obj.attr('id'):obj.attr('name');
		var val = obj.val();
		
		if(obj.attr('type') == 'checkbox'){
			val = ($(obj).attr('checked')) ? "Y" : "N";
		}
		
		if(!gfn_isNull(val) && !gfn_isNull(id)){
			data[prefix + id] = val;
		}
	};	
	
	$(this).find('input,select,combo,textarea').each(function(i){
//		if($(this).attr('type') == 'checkbox' || $(this).attr('type') == 'radio'){
		if($(this).attr('type') == 'radio'){
			if($(this).attr('checked')){
				setVal($(this));
			}
		}else{
			setVal($(this));
		}
	});
	
	return data;
}



/**************************************************************** 
 * Desc 		: 구글차트 관련 공통함수들
 ****************************************************************/
//구글 차트 초기화
function gfn_initChart(pFunc){
	// Load the Visualization API and the piechart package.
    google.load('visualization', '1.0', {'packages':['corechart']});
}

// 구글 차트  그리기
function gfn_Chart(inputParam, chartObj){
	var chartData = new google.visualization.DataTable();
    
    gfn_setChartData(inputParam, chartData);
    
    var options = {};
    options = inputParam.OPTIONS;
    
    var type = inputParam.CHARTTYPE;
    
    var chartId = chartObj.attr("id");
    
    google.setOnLoadCallback(function() {
    	var chart = null;
    	
    	if( type == "PIE" )
	    	chart = new google.visualization.PieChart(document.getElementById(chartId));
    	else if( type == "COLUMN" )
	    	chart = new google.visualization.ColumnChart(document.getElementById(chartId));
    	else if( type == "LINE" )
	    	chart = new google.visualization.LineChart(document.getElementById(chartId));
    	else if( type == "BAR" ){
	    	chart = new google.visualization.BarChart(document.getElementById(chartId));
    	}
    	else if( type == "AREA" ){
	    	chart = new google.visualization.AreaChart(document.getElementById(chartId));
    	}
    	else
	    	chart = new google.visualization.PieChart(document.getElementById(chartId));
    	
	    chart.draw(chartData, options);
	});
}



// value 와 label 컬럼값 축출하여 차트용 데이터를 만든다.
function gfn_setChartData(inputParam, chartData){
	var list = inputParam.DATA;
	var valueIDArr = inputParam.VALUEID.split(",");
	var valueIDNmArr = inputParam.VALUEIDNM.split(",");
	var labelID = inputParam.LABELID;
	
	chartData.addColumn('string', 'Topping');
	
	for(_valueIDNmArr in valueIDNmArr){
		chartData.addColumn('number', valueIDNmArr[_valueIDNmArr]);
	}
	
	//var data = new google.visualization.DataTable();
	
	for(var i = 0; i < list.length ;i++){
		var rowArry = new Array();
		var lableTemp = eval("list[i]."+labelID);
		
		rowArry.push(lableTemp);
		for(_valueIDArr in valueIDArr){
			rowArry.push(eval("list[i]."+valueIDArr[_valueIDArr]));
		}
			
		chartData.addRow(rowArry);
	}
	
	//return chartData;
}
















/////////////////////////////////////////////////////////////////////
//공통 함수들....
function openWindow(anchor, options) {

	var args = '';
	options = fn_nvl2(options, {});

	if (typeof(options) == 'undefined') { var options = new Object(); }
	if (typeof(options.name) == 'undefined') { options.name = 'win' + Math.round(Math.random()*100000); }

	if (typeof(options.height) != 'undefined' && typeof(options.fullscreen) == 'undefined') {
		args += "height=" + options.height + ",";
	}

	if (typeof(options.width) != 'undefined' && typeof(options.fullscreen) == 'undefined') {
		args += "width=" + options.width + ",";
	}

	if (typeof(options.fullscreen) != 'undefined') {
		args += "width=" + screen.availWidth + ",";
		args += "height=" + screen.availHeight + ",";
	}

	if (typeof(options.center) == 'undefined') {
		options.x = 0;
		options.y = 0;
		args += "screenx=" + options.x + ",";
		args += "screeny=" + options.y + ",";
		args += "left=" + options.x + ",";
		args += "top=" + options.y + ",";
	}

	if (typeof(options.center) != 'undefined' && typeof(options.fullscreen) == 'undefined') {
		options.y=Math.floor((screen.availHeight-(options.height || screen.height))/2)-(screen.height-screen.availHeight);
		options.x=Math.floor((screen.availWidth-(options.width || screen.width))/2)-(screen.width-screen.availWidth);
		args += "screenx=" + options.x + ",";
		args += "screeny=" + options.y + ",";
		args += "left=" + options.x + ",";
		args += "top=" + options.y + ",";
	}

	if (typeof(options.scrollbars) != 'undefined') { args += "scrollbars=1,"; }
	if (typeof(options.menubar) != 'undefined') { args += "menubar=1,"; }
	if (typeof(options.locationbar) != 'undefined') { args += "location=1,"; }
	if (typeof(options.resizable) != 'undefined') { args += "resizable=1,"; }
	
	//args += "toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=no,resizable=no";

	//alert(args);

	var win = window.open(anchor, options.name, args);
	return false;
}

/**************************************************************** 
 * Desc 	: 현재사이트 루트경로 가지고 오기
 *  ****************************************************************/ 
function gfn_getApplication(){
	var arrPath = location.pathname.split("/");
	//return "/" + arrPath[1];
	return "";
}
/*Param 값가져오기*/
function gfn_getParaVal(objVal){
	var param="";
	var arrPath = location.search.split("?");
	
	if(!gfn_isNull(arrPath)){
		param = arrPath[1].split("&");
	}
	for (var i = 0; i < param.length; i++) {
		//for ( var x in param) {
		p = param[i].split("=");
		if(p[0] == objVal){
			return p[1];
			break;
		}
	}
	return "";
}	


function gfn_getMenuCd(){
	var param;
	var arrPath = location.search.split("?");
	
	if(!gfn_isNull(arrPath)){
		param = arrPath[1].split("&");
	}
		for ( var x in param) {
			p = param[x].split("=");
			
			if(p[0] == "H_MENU_CD"){
				return p[1];
				break;
			}else{
				return "";
			}
		}
}	    

/**************************************************************** 
 * Desc 	: 현재사이트 사이트 아이디 가지고 오기
 *          : hostname과 application에 따라 사이트 아이디는 정해진다.
 *  ****************************************************************/ 
function gfn_getSiteID(){
//	debugger;
	var appName = gfn_getApplication();	
	var hostname = location.hostname;
	var menuCd = gfn_getMenuCd();
	
	if("localhost" == hostname){
		if("/dWise" == appName)
			return "ADMIN"; 
		if("/kef" == appName)
			return "GEW";
		if("/eship" == appName){
			var siteId = gfn_siteIdChange($("#menuForm"));				
			return siteId;
		}
	}
	else{
		if("/dWise" == appName)
			return "ADMIN"; 
		if("/kef" == appName)
			return "GEW";
		if("/eship" == appName){
			var siteId = gfn_siteIdChange($("#menuForm"));				
			return siteId;
		}
	}
	
	return "";
}

/*한글 영문 으로 사용하는 SITEID를 가져온다.
 * SITE_ID를 비교한후 SITE_ID조건이 맞지않으면 MENU_C로 비교하여 가져온다.
 * */
function gfn_siteIdChange(formObj){
	var strConArr = formObj.serialize();
	var conArr = strConArr.split('&');
	var siteIdVal = gfn_getParaVal("SITE_ID");//param SiteId확인
	var conMenuCd ="GEW_EN";
	
	//path m이 오는지 확인하여 모바일 확인
	var arrPath = location.pathname.split("/");
	var appNameNext = "/" + arrPath[2];	
	if("/m" == appNameNext){
		conMenuCd ="M_GEW_EN";
	}
	
	/*siteID 및 menu Cd비교*/
	if(siteIdVal =="GEW"){
		conMenuCd ="GEW";
	}else if(siteIdVal =="GEW_EN"){
		conMenuCd ="GEW_EN";
	}else if(siteIdVal =="M_GEW"){
		conMenuCd ="M_GEW";
	}else if(siteIdVal =="M_GEW_EN"){
		conMenuCd ="M_GEW_EN";
	}else{
		for(var i = 0 ; i < conArr.length; i++){
			var itemArr = conArr[i].split('=');
			if(itemArr[0] =="H_MENU_CD"){
				var conObj = $("#" + itemArr[0]).val();
				conObj = conObj.substring(0,4);
				if("1002" == conObj){
					conMenuCd ="GEW";
				}else if("1005" == conObj){
					conMenuCd="GEW_EN";
				}else if("1003" == conObj){
					conMenuCd="M_GEW";
				}else if("1006" == conObj){
					conMenuCd="M_GEW_EN";
				}
				break;
			}				
		}
	}
	return conMenuCd;
}


function fn_nvl2(nullObj, trueObj, falseObj){
	if(fn_isEmpty(nullObj))
		return trueObj;
	else{
		if(typeof falseObj == 'undefined' || falseObj == null){
			return nullObj;
		}else{
			return falseObj;
		}
	}
}


function fn_isEmpty( obj ) {
    var isBlack = function(string){
    	return (!string || $.trim(string) === "");
    }
    	
	if(typeof obj == 'string'){
    	return isBlack(obj);
	}else if(typeof obj == 'number' || typeof obj == 'boolean'){
		var tmp = obj.toString();
		return isBlack(tmp);
	}else if( obj instanceof Array ){	//배열일 때 배열안의 값을 체크하여 공백이면 empty다.
		if( obj.length < 1 )
			return true;
		else{
			var rtn = true;
			$.each( obj, function(i, v){
				if( '' != $.trim(v) )
					rtn = false;
			} );

			return rtn;
		}
    }else{
    	return $.isEmptyObject(obj);
    }
}


function gfn_objectCopy(obj)
{
	 return JSON.parse(JSON.stringify(obj));
}

function gfn_isNull(sParam){
	if(typeof(sParam) == "number" || typeof(sParam) == "boolean")
		return false;
	
	if(sParam == '' || sParam == null || sParam == undefined || sParam == 'undefined'){
		return true;
	}else{
		return false;
	}
}

function gfn_null(sParam, sDefalut){
	if(gfn_isNull(sParam))
		return sDefalut;
	else 
		return sParam;
}

//replaceAll
function gfn_replaceAll(value, patten, repatten){
	 var returnValue = value;
	 try{
		 if(value.length > 1){
			 returnValue = value.split(patten).join(repatten);
		 }
	 }
	 catch(e){}
	 return returnValue;
}

//999,999,999 => 9999999999
//useDot : 소수점이하 사용여부
function gfn_maskAmt( pAmt, useDot ) {
	var sAmt = "";
	if( pAmt == 0 ) return pAmt;
	
	if( isNaN(pAmt) ) {
		return '';
	}
	
	if( pAmt ) {
		if( useDot ) {
			sAmt = Number(pAmt).toLocaleString();
		} else {
			sAmt = Number(pAmt).toLocaleString().split('.')[0];
		}	
	}
	
	
	return sAmt;
}

//999,999,999 => 9999999999
function gfn_unMaskAmt( pAmt ) {
	var nAmt = 0;
	
	nAmt = parseFloat( gfn_replaceAll( pAmt, ',', '' ) );
	
	if( isNaN(nAmt) ) {
		return '';
	}
	return nAmt;
}

function gfn_strNvl( pStr ) {
	if( gfn_isNull(pStr) ) {
		return '&nbsp';
	}
	return pStr;
}

function gfn_numNvl( pStr ) {
	if( gfn_isNull(pStr) ) {
		return '0';
	}
	return pStr;
}

function gfn_maskDate( pStr) {
	if( !gfn_isNull(pStr) ){
		if( pStr.length != 8 ) return pStr;
		
		return pStr.substring(0, 4) + '-' + pStr.substring(4, 6) + '-' + pStr.substring(6, 8);
	} else {
		return ;
	} 
}

function gfn_unMaskDate( pStr) {
	return gfn_replaceAll(pStr, '-', '');
}

function gfn_maskBizNo( pStr ) {
	if( pStr ) {
		return pStr.substring(0, 3) + '-' + pStr.substring(3, 5) + '-' + pStr.substring(5, 10);
	} else {
		return;
	}
}

function gfn_unMaskBizNo( pStr ) {
	return gfn_replaceAll(pStr, '-', '');
}

function gfn_maskCorpNo( pStr ) {
	if( pStr ) {
		return pStr.substring(0, 6) + '-' + pStr.substring(6, 13);
	} else {
		return;
	}
}


function fn_isNumber(str){
	var rtnBool = true;
	var isValidChar = function(ch){
		var numUnicode = ch.charCodeAt(0);                                                                                   
		if ( 48 <= numUnicode && numUnicode <= 57 ) 
			return true;           
		
		return false;
	};
	
	if(!fn_isEmpty(str)){
		var arrayOfStr = str.split('');
		for(var k in arrayOfStr){
			var ch = arrayOfStr[k];
			
			if(!isValidChar(ch)){
				rtnBool = false;
				break;
			}
		}
	}
	return rtnBool;
}

function E(evt) { 
    var e = window.event || evt; 
    if (!e) return; 

    e.element = e.target || e.srcElement; 
    return e; 
}

/**
 * num_only 클래스를 가진 입력필드에 대해서
 * 숫자만 입력가능하도록 한다.
 */
restrictNumOnly = function() {
	try{
		$('.num_only').each(function() {
			if (!$(this).data("restrictNumOnly")) {
				$(this).data("restrictNumOnly", true);
				
				$(this).css('imeMode','disabled')
					.on("keypress", function(event) {
						if (event.which && (event.which < 48 || event.which > 57)) {
							event.preventDefault();
						}
					}).valueChanged(function(event) {
						var str = $(this).val();
						if (str) {
							if (str.match(/[^0-9]/)) {
								$(this).blur().focus();
								$(this).val(str.replace(/[^0-9]/g,''));
							}
						}
					});
			}
		});		
	} catch(e){}

};

/**
 * data-maxlength 애트리뷰트를 가진 입력필드에 대해서
 * data-maxlength에 지정된 최대 바이트수까지만 입력가능하도록 한다.
 */
restrictDataMaxLength = function() {
	$("[data-maxlength]").each(function() {
		if (!$(this).data("restrictDataMaxLength")) {
			$(this).data("restrictDataMaxLength", true);
			
			var dataMaxLength = $(this).attr("data-maxlength");
			if (Number(dataMaxLength) > 0) {
				$(this).maxBytes(Number(dataMaxLength), function(maxBytes) {
					alert(maxBytes+"바이트까지만 입력가능합니다.");
				});
			}
		}
	});
};
String.prototype.byteLength = function() {
	var len = 0;
	
	for (var i=0; i < this.length; i++) {
		var ch = encodeURI(this.charAt(i));
		
		if( ch.length==1 ) len++;
		else if( ch.indexOf("%u")!=-1 ) len += 2;
		else if( ch.indexOf("%")!=-1 ) len += ch.length/3;
	}
	
	return len;
};
$.fn.maxBytes = function(maxBytes, onOverflow) {
	this.on("keypress", function(event) {
			if ($(this).val().byteLength() >= maxBytes) {
				if (onOverflow) {
					onOverflow.call(this, maxBytes);
				}
				
				event.preventDefault();
			}
		}).valueChanged(function(event) {
			var str = $(this).val();
			if (str && str.byteLength() > maxBytes) {
				$(this).blur().focus();
				
				while ($(this).val().byteLength() > maxBytes) {
					str = $(this).val();
					$(this).val(str.substring(0, str.length-1));
					$(this).change();
				}
				
				if (onOverflow) {
					onOverflow.call(this, maxBytes);
				}
			}
		});
		
	return this;
};
$.fn.valueChanged = function(handler) {
	this.on("keyup", function(event) {
			handler.apply(this, arguments);
		})
		.on("paste", function(event) {
			var element = this;
			var args = arguments;
			setTimeout(function() {
				handler.apply(element, args);
			}, 100);
		});
		return this;
};
// tooltip
function getAbsolutePos(obj) {
	var position = new Object;
	position.x = 0;
	position.y = 0;

	if (obj) {
		position.x = obj.offsetLeft;
		position.y = obj.offsetTop;

		if (obj.offsetParent) {
			var parentpos = getAbsolutePos(obj.offsetParent);
			position.x += parentpos.x;
			position.y += parentpos.y;
		}

	}

	return position;
}

var tip=new Array();
// 보여줄 툴팁을 설정 하세요
// HTML 태그 사용이 가능합니다
tip[0]='직접입력시 체크박스를 선택해주세요';
	   
   
function showtip(current,e,num, curMainDivID)
{
	if (document.layers) // Netscape 4.0+
	{
		theString = "<DIV CLASS='ttip'>" + tip[num] + "</DIV>";
		document.tooltip.document.write(theString);
		document.tooltip.document.close();
		document.tooltip.left = e.pageX + 14;
		document.tooltip.top = e.pageY + 2;
		document.tooltip.visibility = "show";
	} else {
		if (document.getElementById) // Netscape 6.0+ , Internet Explorer 5.0+
		{
			var position = getAbsolutePos(current);
			if(!gfn_isNull(curMainDivID)){
				elmPar = document.getElementById(curMainDivID);
				var position2 = getAbsolutePos(elmPar);
				
				position.x -= position2.x;
				position.y -= position2.y;
			}
			
			elm = document.getElementById("tooltip");
			elml = current;
			
			
			if( gfn_isNull(num) )
				elm.innerHTML = current.value;
			else
				elm.innerHTML = tip[num];
			
			elm.style.height = elml.style.height;
			/*elm.style.top = parseInt(elml.offsetTop + elml.offsetHeight);
			elm.style.left = parseInt(elml.offsetLeft + elml.offsetWidth + 10);*/
			elm.style.top = parseInt(position.y);
			elm.style.left = parseInt(position.x + elml.offsetWidth / 2 + 10);
			elm.style.visibility = "visible";
		}
	}
}
function hidetip(){
if (document.layers) // Netscape 4.0+
   {
    document.tooltip.visibility="hidden";
   }
else
  {
   if(document.getElementById) // Netscape 6.0+ , Internet Explorer 5.0+
     {
      elm.style.visibility="hidden";
     }
  } 
}

// 필수값 체크
function gfn_isCheckOk(fieldlist){
	
	for (var i = 0; i < fieldlist.length; i++) {
        val = $('#'+fieldlist[i][0]).val();
        if (gfn_isNull(val)) {
            alert(fieldlist[i][1] + " 항목을 반드시 입력해 주십시오.");
            $('#'+fieldlist[i][0]).focus();
            return false;
        } // end if
    } // end for
	
	return true;
}

function gfn_checkEnglishNumeric(englishNumericStr){
	if(englishNumericStr == ''){
		return true;
	}
	var alpha_numeric = /^[a-zA-Z0-9_-]+$/;
	
	if(!alpha_numeric.test(englishNumericStr)){
		return false;
	}
	return true;
}

function gfn_checkEnglish(englishStr){
	if(englishStr == ''){
		return true;
	}
	var alpha = /^[a-zA-Z ]+$/;
	
	if(!alpha.test(englishStr)){
		return false;
	}
	return true;
}

function gfn_checkNumber(numericStr){
	if(numericStr == ''){
		return true;
	}
	var alpha = /^[0-9]+$/;
	
	if(!alpha.test(numericStr)){
		return false;
	}
	return true;
}

//이메일 유효성 체크
function gfn_checkEmail(emailAddr){
	if(emailAddr == ''){
		return true;
	}
	var format = /^((\w|[\-\.])+)@((\w|[\-\.])+)\.([A-Za-z]+)$/;
	
	if (!format.test(emailAddr)) {
		return false;
	}
	return true;
}


function gfn_clickPageNo(pgNo) {
    $('#pageNo').val(pgNo);
    fn_srch();
}

function isNumeric(data) {
	 var len, chrTmp;

	 len = data.length;
	 for(var i=0; i<len; ++i) {
		 chrTmp = str.charCodeAt(i);
		 if((chrTmp <= 47 && chrTmp > 31) || chrTmp >= 58) {
			 return false;
		 }
	 }

	 return true;
}

/**************************************************************** 
 * Desc 	         						: input 에서 숫자 입력서 자동 콤마 찍어주기( EX: 건설기계 작업계획서 SMSSQ0020JS01 에서 사용하고 있음)
 * @Method Name      						: toNumber
 * @param
 * @return
 ****************************************************************/
$.fn.toPrice = function(cipher) {
	  var strb, len, revslice;
	  
	  strb = $(this).val().toString();
	  strb = strb.replace(/,/g, '');
	  strb = $(this).getOnlyNumeric();
	  strb = parseInt(strb, 10);
	  if(isNaN(strb))
	   return $(this).val('');
	   
	  strb = strb.toString();
	  len = strb.length;
	 
	  if(len < 4)
	   return $(this).val(strb);
	 
	  if(cipher == undefined || !isNumeric(cipher))
	   cipher = 3;
	 
	  count = len/cipher;
	  slice = new Array();
	 
	  for(var i=0; i<count; ++i) {
	   if(i*cipher >= len)
	    break;
	   slice[i] = strb.slice((i+1) * -cipher, len - (i*cipher));
	  }
	 
	  revslice = slice.reverse();
	  return $(this).val(revslice.join(','));
	 };
	 
 $.fn.getOnlyNumeric = function(data) {
	 debugger;
	  var chrTmp, strTmp;
	  var len, str;
	  
	  if(data == undefined) {
	   str = $(this).val();
	  }
	  else {
	   str = data;
	  }
	 
	  len = str.length;
	  strTmp = '';
	  
	  
	  for(var i=0; i<len; ++i) {
	   chrTmp = str.charCodeAt(i);
	   //if((chrTmp > 47 || chrTmp <= 31) && chrTmp < 58) {
		if((chrTmp > 47 || chrTmp <= 31 || chrTmp==46) && chrTmp < 58) { // 소수점포함
	    strTmp = strTmp + String.fromCharCode(chrTmp);
	   }
	  }
	  
	  if(data == undefined)
	   return strTmp;
	  else 
	   return $(this).val(strTmp);
	 };	 
	 
	 
/**************************************************************** 
 * Desc 	         						: string의 left pad함수
 * @Method Name      						: rpad
 * @param  len  							: 리턴받을 string의 길이
 * 			char 							: pad할 char
 * @return  Boolean
 ****************************************************************/
String.prototype.lpad = function(len, char){
	var rtnStr = "";
	var addLen = len - this.length;
	
	for(var i=0; i<addLen; i++){
 		rtnStr += char;
 	}
 	return rtnStr + this;
}

/**************************************************************** 
 * Desc 	         						: string의 right pad함수
 * @Method Name      						: rpad
 * @param  len  							: 리턴받을 string의 길이
 * 			char 							: pad할 char
 * @return  Boolean
 ****************************************************************/
String.prototype.rpad = function(len, char){
 	var rtnStr = "";
 	var addLen = len - this.length;
 	
 	for(var i=0; i<addLen; i++){
 		rtnStr += char;
 	}
 	return this + rtnStr;
}


$.fn.onEnter = function(fn){
	var selfObj = $(this);
	$(this).keyup(function(e){
		if(e.keyCode == 13){
			fn(selfObj, e);
		}
	});
}
	
