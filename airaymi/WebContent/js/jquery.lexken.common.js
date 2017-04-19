/**
 * @author &lt;ys.ha@lexken.co.kr&gt;
 * @version 1.0, 2012/05/24, initial revision
 * @fileOverview
 * 
 * <p>Copyright (C) 2012 Lexken Corporation. All rights reserved.</p>
 * 
 */
	 
(function($) {

	$(document).ready(function(){
		
		try {
			$("input", ".blockButton" ).button(); //버튼 ui 적용
		} catch(err) { }
		
	});
	
	var hangulByteLenth = 2; //한글 한글자에 대한 byte 를 지정
	
	
	/********************************************************
	 * TextArea 텍스트 입력 byte 체크
	 ********************************************************/
	$.fn.checkbyte = function(d) {
		var e = {
			indicator : $("#indicator"),
			limit : 80,
			twice : false
		};
		if (d) {
			$.extend(e, d);
		};
		return this.each(function() {
			var c = $(this);
			if ($.browser.mozilla || $.browser.opera) {
				c.bind("textchange", function(a, b) {
					$.check(c, e.indicator, parseInt(e.limit), e.twice);
				});
			} else {
				c.bind("keyup", function(a) {
					$.check(c, e.indicator, parseInt(e.limit), e.twice);
				});
			}
		});
	};
	
	/********************************************************
	 * 최대 입력 문자열 길이 초과시 문자열 자름
	 ********************************************************/
	$.limitString = function(a, b) {
		var d = new String(a);
		var e = 0;
		for ( var i = 0; i < a.length; i++) {
			var c = escape(a.charAt(i));
			if (c.length == 1)
				e++;
			else if (c.indexOf("%u") != -1)
				e += hangulByteLenth;
			else if (c.indexOf("%") != -1)
				e += c.length / 3;
			if (e > b) {
				d = d.substring(0, i);
				break;
			}
		}
		return d;
	};
	
	/********************************************************
	 * 문자열 byte 체크
	 ********************************************************/
	$.byteString = function(a) {
		var b = 0;
		for ( var i = 0; i < a.length; i++) {
			var c = escape(a.charAt(i));
			if (c.length == 1)
				b++;
			else if (c.indexOf("%u") != -1)
				b += hangulByteLenth;
			else if (c.indexOf("%") != -1)
				b += c.length / 3;
		}
		return b;
	};
	
	/********************************************************
	 * 텍스트 입력 byte 제한
	 ********************************************************/
	$.check = function(a, b, c, d) {
		var e = $.byteString(a.val());
		if (e > c) {
			alert("내용은 " + c + "byte를 넘을 수 없습니다. 초과된 부분은 자동으로 삭제됩니다.");
			a.val($.limitString(a.val(), c));
		}
		b.html($.byteString(a.val()));
	};
	
	/********************************************************
	 * Confirm Dialog 설정
	 * parameter (메세지, 콜백함수, 확인후 포커스 객체)
	 ********************************************************/
	$.showConfirmBox = function(msg, callBackFunc) {
		$('#dialog').dialog({
			autoOpen: false,
			width: 300,
			modal: true,
			buttons: {
				"Ok": function() { 
					$(this).dialog("close"); 
					window[callBackFunc].apply(window, arguments); //CallBack 함수 호출
				},
				"Cancel": function() {
					$(this).dialog("close");
				}
			}
		});
		$('#dialog p').html(msg);
    	$('#dialog').dialog('open');
	};
	
	/********************************************************
	 * Alert Dialog 설정
	 * parameter (메세지, 콜백함수, 확인후 포커스 객체)
	 ********************************************************/
	$.showMsgBox = function(msg, callBackFunc, obj) {
		$('#dialog').dialog({
			autoOpen: false,
			width: 300,
			modal: true,
			buttons: {
				"Ok": function() { 
					$(this).dialog("close"); 
					if(callBackFunc != null) {
						window[callBackFunc].apply(window, arguments); //CallBack 함수 호출
					}
					if(obj != null) {
						try {
							$('#' + obj).focus(); //focus 설정
						} catch(err) {
							$(obj).focus(); //focus 설정
						}
					}
				}
			}
		});
		$('#dialog p').html(msg);
    	$('#dialog').dialog('open');
	};
	
	/********************************************************
	 * Alert Dialog 설정
	 * parameter (메세지)
	 ********************************************************/
	$.showMsgBoxModal = function(msg) {
		$('#dialog').dialog({
			autoOpen: false,
			width: 300,
			modal: true
		});
		$('#dialog p').html(msg);
		$(".ui-dialog-titlebar").hide();
    	$('#dialog').dialog('open');
	};
	
	/********************************************************
	 * input box 문자열 길이 체크
	 ********************************************************/
	$.checkLength = function(strId, strName, minLen, maxLen) {
		var text = $.trim($('#' + strId).val()); //문자열 trim 추가(하윤식 2012.10.24)
		var textLen = $.byteString(text);
		if(minLen > 0 && textLen == 0) {
			msg = "[" + strName + "] : 입력해주십시오";
			$.showMsgBox(msg, null, strId); 
			return false;
		}else if(textLen < minLen || textLen > maxLen) {
			if(minLen == maxLen) {
				msg = "[" + strName + "] : " + minLen + "Byte를 입력해주십시오\r\n\r\n (주의: 한글 1자는 " + hangulByteLenth + "Byte로 계산함.)";
				$.showMsgBox(msg, null, strId); 
				return false;
			} else {
				msg = "[" + strName + "] : " + minLen + " - " + maxLen + " Byte를  입력해주십시오. <br/>(주의: 한글 1자는 " + hangulByteLenth + "Byte로 계산함.)";
				$.showMsgBox(msg, null, strId);
				return false;
			} 
		} else {
			return true;
		}
	};

	/********************************************************
	 * jqGrid check box 선택여부 확인
	 ********************************************************/
	$.gridSelectChk = function(listId) {
		var id = $("#" + listId).getGridParam('selarrrow');
		var ids = $("#" + listId).jqGrid('getDataIDs');
		var isCheck = false;
		
		for (var i = 0; i < ids.length; i++) {
		    $.each(id, function (index, value) {
		        if (value == ids[i]) {
		        	isCheck = true;
		        }
		    });
		}
		return isCheck;
	};
	

	//배열의 중복 문자 제거..
	//배열명.unique(); <-- 사용법
	//var a = {1,2,3,4,5,1,2};
	//a.unique();
	//결과 [1,2,3,4,5]
	Array.prototype.unique = function() {
		var a = {};
		for(var i=0; i<this.length; i++)   {
			if(typeof a[this[i]] == "undefined")
				a[this[i]] = 1;
		}

		this.length = 0;
		for(var i in a)
			this[this.length] = i;

		return this;
	};
	
	// 주어진 패턴 공식에서 문자만 뽑아내서 갯수를 세고,
	// 각 A,B,C는 patChar[i]형태로 배열에 저장한다.
	//function extChar2(patt) {
	String.prototype.extChar = function() {
		var charReg;
		charReg = /\d/; // 삭제할 부분 숫자
		//charReg = /[a-z|A-Z]/; // 알파벳만 인정
		//charReg = /[a-z]/; // 알파벳 소문자만 인정
		charReg = /[A-Z]/; // 알파벳 대문자만 인정

		var pattern = this; // 패턴 공식 값 DB에서 읽어올 것
		var Char = "";
		var patChar = pattern.split("");

		for(var i=0; i<patChar.length; i++) {
			if(charReg.test(patChar[i])) {
				Char += patChar[i] + "/";
			}
		}

		return Char; //최종으로 뽑아낸 문자만 리턴해준다.
	};
	
	/********************************************************
	 * jQuery datepicker 한글설정
	 ********************************************************/
	$.datepicker.regional['ko'] = {
		closeText: '닫기',
		prevText: '이전달',
		nextText: '다음달',
		currentText: '오늘',
		monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
		monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
		dayNames: ['일','월','화','수','목','금','토'],
		dayNamesShort: ['일','월','화','수','목','금','토'],
		dayNamesMin: ['일','월','화','수','목','금','토'],
		weekHeader: 'Wk',
		dateFormat: 'yymmdd',
		firstDay: 0,
		isRTL: false,
		duration:200,
		showAnim:'show',
		showMonthAfterYear: false,
		yearSuffix: '년'
	};
	 
	$.datepicker.setDefaults($.datepicker.regional['ko']);
	
	
	/********************************************************
	 * 설정된 value 값 초기화
	 ********************************************************/
	$.setInit = function(initObj) {
		for (var i = 0; i < arguments.length; i++) {
			$("#" + arguments[i]).val("");
		}
	};
	
	
	
	
	$.fn.numericOnly = function(param) {

		/*
		var allowList = "";
		if(param && param.allow && param.allow.length > 0 ){
			for( var i = 0 ;i < param.allow.length;i++ ){
				if(param.allow[i] == '.'){
					allowList+= "\\.";
				}
			}
		}

		*/

	    var allowStr = "";

	    if(param && param.allow && param.allow.length > 0){
	    	var allowList = param.allow.split('');
			for (var i=0; i < allowList.length; i++) {
				allowStr += '\\' + allowList[i];
	        }
	    }

	    //alert(allowStr);

		return this.each(function() {
			var c = $(this);
			var regExp = new RegExp('[^0-9'+allowStr+']','g');
			c.keyup(function(a) {
				if (this.value != this.value.replace(regExp, '')) {
				       this.value = this.value.replace(regExp, '');
				    }
			});
			c.change(function(a) {
				if (this.value != this.value.replace(regExp, '')) {
				       this.value = this.value.replace(regExp, '');
				    }
			});
		});
	};

	$.fn.alphanumericOnly = function(param) {

		/*
		var allowList = "";
		if(param && param.allow && param.allow.length > 0 ){
			for( var i = 0 ;i < param.allow.length;i++ ){
				if(param.allow[i] == '.'){
					allowList+= "\\.";
				}
			}
		}
	
		*/
	
		var allowStr = "";
	
	    if(param && param.allow && param.allow.length > 0){
	    	var allowList = param.allow.split('');
			for (var i=0; i < allowList.length; i++) {
				allowStr += '\\' + allowList[i];
	        }
	    }
	
		//allowList
	
		return this.each(function() {
			var c = $(this);
			var regExp = new RegExp('[^a-zA-Z0-9'+allowStr+']','g');
			c.keyup(function(a) {
				if (this.value != this.value.replace(regExp, '')) {
				       this.value = this.value.replace(regExp, '');
				    }
			});
			c.change(function(a) {
				if (this.value != this.value.replace(regExp, '')) {
				       this.value = this.value.replace(regExp, '');
				    }
			});
		});
	
	};

	 
})(jQuery);