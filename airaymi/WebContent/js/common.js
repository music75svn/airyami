	/**
]	 * Copyright (c) 2009 by Lexken
	 * All rights reserved.
	 *
	 * 자바스크립트 공통함수
	 *	 
	 * @version 1.0, 2009/07/06
	 * @author 
	 */
	//var context = "/BSC_PROJECT";
	var context = "";
	
	 /**
	 * string String::cut(int len)
	 * 글자를 앞에서부터 원하는 바이트만큼 잘라 리턴합니다.
	 * 한글의 경우 2바이트로 계산하며, 글자 중간에서 잘리지 않습니다.
	 */
	 String.prototype.cut = function(len) {
	  var str = this;
	  var l = 0;
	  for (var i=0; i<str.length; i++) {
	   l += (str.charCodeAt(i) > 128) ? 2 : 1;
	   if (l > len) return str.substring(0,i);
	  }
	  return str;
	 }
	
	 /**
	 * bool String::bytes(void)
	 * 해당스트링의 바이트단위 길이를 리턴합니다. (기존의 length 속성은 2바이트 문자를 한글자로 간주합니다)
	 */
	 String.prototype.bytes = function() {
	  var str = this;
	  var l = 0;
	  for (var i=0; i<str.length; i++) l += (str.charCodeAt(i) > 128) ? 2 : 1;
	  return l;
	 }
	
	// 엔터키를 누를때 이벤트 발생하기
	function enterDownPost()
	{
		if(event.keyCode == 13)
		{
			btn_postid_chk_click();
		}
	}
	function enterDownId()
	{
		if(event.keyCode == 13)
		{
			btn_userid_chk_click();
			return;
		}
	}
	
	//숫자만입력가능하게  onkeypress에 사용하면됨. 숫자가 아닌 다른글자 입력시 입력안됨.
	function onlyNum(name){
		
		flag = false;
		//alert("event.keyCode = " +event.keyCode);
		
		if(((event.keyCode >= 48) && (event.keyCode <= 57)) || ((event.keyCode >= 96) && (event.keyCode <= 105))){
			//숫자
			flag = true;
		}else if((event.keyCode == 46) || (event.keyCode == 8) || (event.keyCode == 27) || (event.keyCode == 13) || (event.keyCode == 45)){
			//delete, backspace, esc, f5, enter
			flag = true;
		}else if((event.keyCode == 9) || (event.keyCode == 37) || (event.keyCode == 39) || (event.keyCode == 35) || (event.keyCode == 36) || (event.keyCode == 38) || (event.keyCode == 40)){
			//tab, <-, -> , home, end, 위, 아
		//	alert(3);
		    flag = true;
		//}else if((event.keyCode == 16) || (event.keyCode == 17) || (event.keyCode == 18) || (event.keyCode == 20) || (event.keyCode == 25)){
		//	//shift, Lctrl, Lalt, caps lock, Rctrl
		//	flag = true;
		//	alert(4);
		}else{
			alert("숫자만 입력가능합니다");
			//name.value="";
			name.focus();
			flag = false;
		}
		event.returnValue=flag;
	}
	
	//숫자/영문만 입력가능하게  onkeypress에 사용하면됨. 숫자/영문이 아닌 다른글자 입력시 입력안됨.
	function onlyNumEng(name){
		flag = false;
		if(((event.keyCode >= 48) && (event.keyCode <= 57)) || ((event.keyCode >= 96) && (event.keyCode <= 105))){
			//숫자
			flag = true;
		}else if((event.keyCode >= 65) && (event.keyCode <= 90)){
			//영문
			flag = true;
		}else if((event.keyCode == 46) || (event.keyCode == 8) || (event.keyCode == 27) || (event.keyCode == 116)){
			//delete, backspace, esc, f5
			flag = true;
		}else if((event.keyCode == 9) || (event.keyCode == 37) || (event.keyCode == 39) || (event.keyCode == 35) || (event.keyCode == 36) || (event.keyCode == 38) || (event.keyCode == 40)){
			//tab, <-, -> , home, end, 위, 아
			flag = true;
		}else if((event.keyCode == 16) || (event.keyCode == 17) || (event.keyCode == 18) || (event.keyCode == 20) || (event.keyCode == 25)){
			//shift, Lctrl, Lalt, caps lock, Rctrl
			flag = true;
		}else{
			alert("숫자만 사용하실 수 있습니다.");
			name.value="";
			name.focus();
			flag = false;
		}
		event.returnValue=flag;
	}
	
	// 영문만 가능하도록
	function onlyEng(name){
		flag = false;
		if((event.keyCode >= 65) && (event.keyCode <= 90)){
			//영문
			flag = true;
		}else if((event.keyCode == 46) || (event.keyCode == 8) || (event.keyCode == 27) || (event.keyCode == 116)){
			//delete, backspace, esc, f5
			flag = true;
		}else if((event.keyCode == 9) || (event.keyCode == 37) || (event.keyCode == 39) || (event.keyCode == 35) || (event.keyCode == 36) || (event.keyCode == 38) || (event.keyCode == 40)){
			//tab, <-, -> , home, end, 위, 아
			flag = true;
		}else if((event.keyCode == 16) || (event.keyCode == 17) || (event.keyCode == 18) || (event.keyCode == 20) || (event.keyCode == 25)){
			//shift, Lctrl, Lalt, caps lock, Rctrl
			flag = true;
		}else{
			alert("영문만 사용하실 수 있습니다.");
			name.value="";
			name.focus();
			flag = false;
		}
		event.returnValue=flag;
	}
	
	function onlyEng1(objValue, strName){
		var tmp = objValue;
		
		for (i = 0; i < tmp.length; i++) {
			if (tmp.charAt(i) >= 'a' && tmp.charAt(i) <= 'z')
				continue;
			else if (tmp.charAt(i) >= 'A' && tmp.charAt(i) <= 'Z')
				continue;
			else if (tmp.charAt(i) == '_' || tmp.charAt(i) == '-')
				continue;
			else if (tmp.charAt(i) == ' ')
				continue;
			else {
				alert(strName + "은(는) 영문만 사용하실 수 있습니다.");
				return false;
			}
		}
		return true;
	}
	
	
	//한글만가능하도록
	function onlyKor(){
		//alert(event.keyCode);
		flag = false;
		if((event.keyCode >= 48) && (event.keyCode <= 57) || (event.keyCode >= 96) && (event.keyCode <= 105)){
			//숫자
			flag = false;
		}else if((event.keyCode >= 65) && (event.keyCode <= 90)){
			//영문
			flag = false;
		}else if((event.keyCode >= 186) && (event.keyCode <= 192) || (event.keyCode >= 219) && (event.keyCode <= 222)){
			//특수문자~_+|}{:">?
			flag = false;
		}else if((event.keyCode >= 110) && (event.keyCode <= 111) || (event.keyCode >= 106) && (event.keyCode <= 109)){
			//특수문자/*-+
			flag = false;
		}else if((event.keyCode == 46) || (event.keyCode == 8)){
			//delete, backspace
			flag = true;
		}else if((event.keyCode == 9) || (event.keyCode == 37) || (event.keyCode == 39)){
			//tab, <-, ->
			flag = true;
		}else{
			flag = true;
		}
		event.returnValue=flag;
	}
	
	//인자의 val과 같은 오브젝트 checked 
	function setCheckedValue(obj, val) {
	    
	    for ( i=0; i<obj.length; i++ )  {
	        if ( obj[i].value == val )  {
	            obj[i].checked = true;
	            break;
	        }
	    }
	}
	
	//인자의 val과 같은 오브젝트 checked 
	function setCheckedValueName(obj, name) {
	    
	    for ( i=0; i<obj.length; i++ )  {
	        if ( obj[i].name == name )  {
	            obj[i].checked = true;
	            break;
	        }
	    }
	}
	
	//배열의 공통코드명에 해당하는 코드 찾기
	function findArrCode(arrCd, arrNm, name) {
	    var retValue = "";
	    for ( i=0; i<arrNm.length; i++ )  {
	        	
	        if ( arrNm[i] == name )  {
	            retValue = arrCd[i];
	            break;
	        }
	    }
	    
	    return retValue;
	}
	
	//라디오 버튼의 체크된 값 알아오기
	function getRadioCheckedValue(obj) {
	    var checkedValue = "";
	    for ( i=0; i<obj.length; i++ )  {
	        if ( obj[i].checked == true && obj[i].disabled != true )  {
	            checkedValue = obj[i].value;
	            break;
	        }
	    }
	    return checkedValue;
	}
	
	//체크박스에 체크된 값 알아오기 콤마로구분
	function getCheckboxCheckedValue(obj) {
	    var checkedValue = "";
	    
	    try {
	   		if(obj.length == undefined) {
	   			 if ( obj.checked == true ) {
	   			 	checkedValue = obj.value;
	   			 }

	   		} else {
			    for ( i=0; i<obj.length; i++ )  {
			        if ( obj[i].checked == true )  {
						if (checkedValue.length>0){
							checkedValue = checkedValue+","+obj[i].value;
						}else{
							checkedValue = obj[i].value;
						}
			
			        }
			    }
			}
		} catch(err) {
			checkedValue = "";
		}
		 
	    return checkedValue;
	}
	
	
	/**
	 * 입력값이 NULL인지 체크
	 */
	function movePgae(obj, target, action) {
	    obj.target = target;
	    obj.action = action;
	    obj.submit();
	}
	
	
	/**
	 * 입력값이 NULL인지 체크
	 */
	function isNull(input) {
	    if (input.value == null || input.value == "") {
	        return true;
	    }
	    return false;
	}
	
	/**
	 * 입력값에 스페이스 이외의 의미있는 값이 있는지 체크
	 */
	function isEmpty(input) {
	    if (input.value == null || input.value.replace(/ /gi,"") == "") {
	        return true;
	    }
	    return false;
	}
	
	/**
	 * 입력값에 특정 문자(chars)가 있는지 체크
	 * 특정 문자를 허용하지 않으려 할 때 사용
	 * ex) if (containsChars(form.name,"!,*&^%$#@~;")) {
	 *         alert("이름 필드에는 특수 문자를 사용할 수 없습니다.");
	 *     }
	 */
	function containsChars(input,chars) {
	    for (var inx = 0; inx < input.value.length; inx++) {
	       if (chars.indexOf(input.value.charAt(inx)) != -1)
	           return true;
	    }
	    return false;
	}
	
	/**
	 * 입력값이 특정 문자(chars)만으로 되어있는지 체크
	 * 특정 문자만 허용하려 할 때 사용
	 * ex) if (!containsCharsOnly(form.blood,"ABO")) {
	 *         alert("혈액형 필드에는 A,B,O 문자만 사용할 수 있습니다.");
	 *     }
	 */
	function containsCharsOnly(input,chars) {
	    for (var inx = 0; inx < input.length; inx++) {
	       if (chars.indexOf(input.charAt(inx)) == -1)
	           return false;
	    }
	    return true;
	}
	
	function containsCharsOnlyMe(input,chars) {
	    for (var inx = 0; inx < input.value.length; inx++) {
	       if (chars.indexOf(input.value.charAt(inx))  == -1){
	           return false;
	       }
	    }
	    return true;
	}
	
	/**
	 * 문자가 들어있는지 체크
	 * 특정 문자를 사용불가할때
	 * ex) if (containsChars(form.blood,"ABO")) {
	 *         alert("혈액형 필드에는 A,B,O는 사용불가 합니다.");
	 *     }
	 */
	function containsChars(input,chars) {
	    for (var inx = 0; inx < input.value.length; inx++) {
	       if (chars.indexOf(input.value.charAt(inx)) > -1)
	           return true;
	    }
	    return false;
	}
	
	/**
	 * 입력값이 알파벳인지 체크
	 * 아래 isAlphabet() 부터 isNumComma()까지의 메소드가
	 * 자주 쓰이는 경우에는 var chars 변수를
	 * global 변수로 선언하고 사용하도록 한다.
	 * ex) var uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	 *     var lowercase = "abcdefghijklmnopqrstuvwxyz";
	 *     var number    = "0123456789";
	 *     function isAlphaNum(input) {
	 *         var chars = uppercase + lowercase + number;
	 *         return containsCharsOnly(input,chars);
	 *     }
	 */
	function isAlphabet(input) {
	    var chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	    return containsCharsOnly(input,chars);
	}
	
	/**
	 * 입력값이 알파벳 대문자인지 체크
	 */
	function isUpperCase(input) {
	    var chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	    return containsCharsOnly(input,chars);
	}

	/**
	 * 입력값이 알파벳 대문자인지,언더바(_)로 되어있는지 체크
	 */
	
	function isUpperCaseUnderbar(input) {
	    var chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ_";
	    return containsCharsOnlyMe(input,chars);
	}
	
	/**
	 * 입력값이 알파벳 소문자인지 체크
	 */
	function isLowerCase(input) {
	    var chars = "abcdefghijklmnopqrstuvwxyz";
	    return containsCharsOnly(input,chars);
	}
	
	/**
	 * 입력값에 숫자만 있는지 체크
	 */
	function isNumber(input) {
	    var chars = "0123456789";
	    return containsCharsOnly(input,chars);
	}
	
	/**
	 * 입력값에 특정 문자(chars)가 있는지 체크
	 * 특정 문자를 허용하지 않으려 할 때 사용
	 * ex) if (containsChars(form.name,"!,*&^%$#@~;")) {
	 *         alert("이름 필드에는 특수 문자를 사용할 수 없습니다.");
	 *     }
	 */
	function isNumberCnt(input) {
		var chars = "0123456789.";
		var temp = "";
	    for (var inx = 0; inx < input.value.length; inx++) {
	       if (chars.indexOf(input.value.charAt(inx)) != -1){
	           temp = temp + input.value.charAt(inx);
	       }
	    }
	    
	    if(temp == "") temp = "0";
	    input.value = temp;
	}
	
	/**
	 * 입력값이 알파벳,숫자로 되어있는지 체크
	 */
	function isAlphaNum(input) {
	    var chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	    return containsCharsOnly(input,chars);
	}
	
	/**
	 * 입력값이 알파벳,숫자,:,/,~,. 로 되어있는지 체크
	 */
	function isAlphaNumPlus(input) {
	    var chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789:/~.";
	    return containsCharsOnly(input,chars);
	}
	
	/**
	 * 입력값이 숫자,대시(-)로 되어있는지 체크
	 */
	function isNumDash(input) {
	    var chars = "-0123456789";
	    return containsCharsOnly(input,chars);
	}
	
	/**
	 * 입력값이 숫자,콤마(,)로 되어있는지 체크
	 */
	function isNumComma(input) {
	    var chars = ",0123456789";
	    return containsCharsOnly(input,chars);
	}
	
	/**
	 * 입력값이 숫자,콤마(,),포인트(.)로 되어있는지 체크
	 */
	function isNumCommaPoint(input) {
	    var chars = ",.0123456789";
	    return containsCharsOnly(input,chars);
	}
	
	/**
	 * 입력값이 오류문자인지  체크
	 */
	function isErrorChar(input) {
	    var chars = "?";
	    return containsCharsOnly(input,chars);
	}
	
	/**
	 * 입력값에서 콤마를 없앤다.
	 */
	function removeComma(input) {
	    return input.value.replace(/,/gi,"");
	}
	
	/**
	 * 입력값이 사용자가 정의한 포맷 형식인지 체크
	 * 자세한 format 형식은 자바스크립트의 'regular expression'을 참조
	 */
	function isValidFormat(input,format) { 
		alert(input);
	    if (input.value.search(format) != -1) {
	        return true; //올바른 포맷 형식
	    }
	    return false;
	}
	
	/**
	 * 입력값이 이메일 형식인지 체크
	 */
	function isValidEmail(input) {
	//  var format = /^(\S+)@(\S+)\.([A-Za-z]+)$/;
	   var format = /^((\w|[\-\.])+)@((\w|[\-\.])+)\.([A-Za-z]+)$/;
	   return isValidFormat(input,format);
	}
	
	/**
	 * 입력값이 전화번호 형식(숫자-숫자-숫자)인지 체크
	 */
	function isValidPhone(input) {
	    var format = /^(\d+)-(\d+)-(\d+)$/;
	    return isValidFormat(input,format);
	}
	
	/**
	 * 선택된 라디오버튼이 있는지 체크
	 */
	function hasCheckedRadio(input) {
	    if (input.length > 1) {
	        for (var inx = 0; inx < input.length; inx++) {
	            if (input[inx].checked) return true;
	        }
	    } else {
	        if (input.checked) return true;
	    }
	    return false;
	}
	
	/**
	 * 선택된 체크박스가 있는지 체크
	 */
	function hasCheckedBox(input) {
	    return hasCheckedRadio(input);
	}
	
	/**
	 * 입력값의 바이트 길이를 리턴
	 * Author : Wonyoung Lee
	 */
	function getByteLength(input) {
	    var byteLength = 0;
	    for (var inx = 0; inx < input.value.length; inx++) {
	        var oneChar = escape(input.value.charAt(inx));
	        if ( oneChar.length == 1 ) {
	            byteLength ++;
	        } else if (oneChar.indexOf("%u") != -1) {
	            byteLength += 2;
	        } else if (oneChar.indexOf("%") != -1) {
	            byteLength += oneChar.length/3;
	        }
	    }
	    return byteLength;
	}
	
	/**
	 * 입력한 값이 날짜유형인지 체크
	 */
	function checkDateType(v_date, flag)
	{
	    return true;
	}
	
	/**
	 * 유효한 날짜인지 체크
	 */
	function checkDate(v_year,v_month,v_day )
	{
		var err=0
		if ( v_year.length != 4) err=1
		if ( v_month.length != 1 &&  v_month.length !=  2 ) err=1
		if ( v_day.length != 1  &&  v_day.length !=  2) err=1
	
	
		r_year = eval(v_year) ;
		r_month = eval(v_month);
		r_day = eval(v_day)  ;
	
		if (r_month<1 || r_month>12) err = 1
		if (r_day<1 || r_day>31) err = 1
		if (r_year<0 ) err = 1
	
	
		if (r_month==4 || r_month==6 || r_month==9 || r_month==11){
			if (r_day==31) err=1
		}
	
		// 2,윤년체크
		if (r_month==2){
			var g=parseInt(r_year/4)
	
			if (isNaN(g)) {
				err=1
			}
			if (r_day>29) err=1
			if (r_day==29 && ((r_year/4)!=parseInt(r_year/4))) err=1
		}
	
		if (err==1)
		{
			return false
		}
		else
		{
	   return true;
		}
	}
	
	/**
	 * 유효한 날짜인지 체크(신규)
	 */
	function checkDateNew(tmpDate)
	{
		if(tmpDate.value != ""){
			var v_date	 = tmpDate.value.replace(/\./gi,"");
			
			var v_year   = v_date.substring(0, 4);
			var v_month  = v_date.substring(4, 6);
			var v_day    = v_date.substring(6, 8);
			
			var err=0
			if ( v_year.length != 4) err=1
			if ( v_month.length != 1 &&  v_month.length !=  2 ) err=1
			if ( v_day.length != 1  &&  v_day.length !=  2) err=1
		
		
			r_year = eval(v_year) ;
			r_month = eval(v_month);
			r_day = eval(v_day)  ;
		
			if (r_month<1 || r_month>12) err = 1
			if (r_day<1 || r_day>31) err = 1
			if (r_year<0 ) err = 1
		
		
			if (r_month==4 || r_month==6 || r_month==9 || r_month==11){
				if (r_day==31) err=1
			}
		
			// 2,윤년체크
			if (r_month==2){
				var g=parseInt(r_year/4)
		
				if (isNaN(g)) {
					err=1
				}
				if (r_day>29) err=1
				if (r_day==29 && ((r_year/4)!=parseInt(r_year/4))) err=1
			}
		
			if (err==1){
				alert("유효하지 않은 날짜입니다.");
				tmpDate.value = "";
				tmpDate.focus();
				return false;
			}else{
				return true;
			}
		}
	}	
	
	
	//사업자등록번호체크
	function isRegNumber(input){
		var chk		= true;
		var del = /[^(0-9)]/gi;
		var inVal = input.value.replace(del,"");

		if(GetLength(inVal) == 10){
			var num= inVal;
		    var w_c,w_e,w_f,w_tot
		    w_c=num.charAt(8)*5       // 9번째자리의 숫자에 5를 곱한다.
		    w_e=parseInt((w_c/10),10) // 10으로 나누고 10진수 형태의 숫자형으로 만든당..나눈몫
		    w_f=w_c % 10              // 10으로 나눈 나머지.....
		    
		    w_tot=num.charAt(0)*1
		    w_tot+=num.charAt(1)*3
		    w_tot+=num.charAt(2)*7
		    w_tot+=num.charAt(3)*1
		    w_tot+=num.charAt(4)*3
		    w_tot+=num.charAt(5)*7
		    w_tot+=num.charAt(6)*1
		    w_tot+=num.charAt(7)*3
		    w_tot+=num.charAt(9)*1
		    w_tot+=(w_e+w_f)
			
		    if ((w_tot % 10) == 0){ // 10으로 나누어 지면 false를 그렇지 않으면 true를 반환합니당..
		    	chk = false;
		    }
		}
		return chk;
	}
	
	
	// desc     : 주민번호 유효성 체크한다.
	// input    : [jumin1, jumin2]
	// return   : boolean
	function chkJumin(jumin1, jumin2) 
	{
		var	i;
		var IDtot = 0;
		var chkJuminNo = false;

		for (i=0 ; i<=5 ; i++)
			IDtot = IDtot + ((i%8+2) * parseInt(jumin1.substring(i,i+1))) ;
		for (i=6 ; i<=11 ; i++)
			IDtot = IDtot + ((i%8+2) * parseInt(jumin2.substring(i-6,i-5))) ;

		IDtot = 11 - (IDtot % 11);
		IDtot = IDtot % 10;

		if (IDtot != jumin2.substring(6,7)) {
			chkJuminNo = isRegNo_fgnno(jumin1 + "" + jumin2);
			if (!chkJuminNo) {
				msg = "주민등록번호가 유효하지 않습니다.\r\n다시 입력하세요.";
				alert(msg);
			}
		} else {
		    chkJuminNo = true; 
		}
		
		return chkJuminNo;
	}

	//주민등록번호 체크
	function regitNumberCheck(regitNum1, regitNum2)
	{
	    errfound = false;
	    var str_jumin1 = regitNum1.value;
	    var str_jumin2 = regitNum2.value;
	    var checkImg='';
	
	
	    var i3=0
	    for (var i=0;i<str_jumin1.length;i++)
	    {
	        var ch1 = str_jumin1.substring(i,i+1);
	        if (ch1<'0' || ch1>'9') { i3=i3+1 }
	    }
	    if ((str_jumin1 == '') || ( i3 != 0 ))
	    {
	//        error(regitNum1,'없는 주민등록번호 입니다. 다시 입력해 주세요!!');
	        return false;
	    }
	
	
	
	    var i4=0
	    for (var i=0;i<str_jumin2.length;i++)
	    {
	        var ch1 = str_jumin2.substring(i,i+1);
	        if (ch1<'0' || ch1>'9') { i4=i4+1 }
	    }
	    if ((str_jumin2 == '') || ( i4 != 0 ))
	    {
	//      error(regitNum2,'없는 주민등록번호 입니다. 다시 입력해 주세요!!');
	        return false;
	    }
	
	    if(str_jumin1.substring(0,1) < 4)
	    {
	//        error(regitNum2,'없는 주민등록번호 입니다. 다시 입력해 주세요!!');
	        return false;
	    }
	
	    if(str_jumin2.substring(0,1) > 2)
	    {
	//        error(regitNum2,'없는 주민등록번호 입니다. 다시 입력해 주세요!!');
	        return false;
	    }
	
	    if((str_jumin1.length > 7) || (str_jumin2.length > 8))
	    {
	//        error(regitNum2,'없는 주민등록번호 입니다. 다시 입력해 주세요!!');
	        return false;
	    }
	
	    if ((str_jumin1 == '72') || ( str_jumin2 == '18'))
	    {
	//        error(regitNum1,'없는 주민등록번호 입니다. 다시 입력해 주세요!!');
	        return false;
	    }
	
	    var f1=str_jumin1.substring(0,1)
	    var f2=str_jumin1.substring(1,2)
	    var f3=str_jumin1.substring(2,3)
	    var f4=str_jumin1.substring(3,4)
	    var f5=str_jumin1.substring(4,5)
	    var f6=str_jumin1.substring(5,6)
	    var hap=f1*2+f2*3+f3*4+f4*5+f5*6+f6*7
	    var l1=str_jumin2.substring(0,1)
	    var l2=str_jumin2.substring(1,2)
	    var l3=str_jumin2.substring(2,3)
	    var l4=str_jumin2.substring(3,4)
	    var l5=str_jumin2.substring(4,5)
	    var l6=str_jumin2.substring(5,6)
	    var l7=str_jumin2.substring(6,7)
	    hap=hap+l1*8+l2*9+l3*2+l4*3+l5*4+l6*5
	    hap=hap%11
	    hap=11-hap
	    hap=hap%10
	    if (hap != l7)
	    {
	      error(regitNum1,'없는 주민등록번호 입니다. 다시 입력해 주세요!!');
	        return false;
	    }
	
	
	    var i9=0
	
	    return true;
	    //if (!errfound)
	    //        submit();
	}
	
	
	// 주민등록번호를 한줄에 입력받았을경우 ' - '없이
	// 예) 7911020000000 인자값 문자로 줄것
	function regitNumberCheck2(juminNumber)
	{
	    errfound = false;
	    var str_jumin1 = juminNumber.substring(0,6);
	    var str_jumin2 = juminNumber.substring(6,juminNumber.length);
	
	    var checkImg='';
	
	    var i3=0
	    for (var i=0;i<str_jumin1.length;i++)
	    {
	        var ch1 = str_jumin1.substring(i,i+1);
	        if (ch1<'0' || ch1>'9') { i3=i3+1 }
	    }
	    if ((str_jumin1 == '') || ( i3 != 0 ))
	    {
	        //alert(str_jumin1 + "-" + str_jumin2 + ' 은 없는 주민등록번호 입니다. 다시 입력해 주세요!!');
	        return false;
	    }
	
	
	
	    var i4=0
	    for (var i=0;i<str_jumin2.length;i++)
	    {
	        var ch1 = str_jumin2.substring(i,i+1);
	        if (ch1<'0' || ch1>'9') { i4=i4+1 }
	    }
	    if ((str_jumin2 == '') || ( i4 != 0 ))
	    {
	        //alert(str_jumin1 + "-" + str_jumin2 + ' 은 없는 주민등록번호 입니다. 다시 입력해 주세요!!');
	        return false;
	    }
	
	    /*if(str_jumin1.substring(0,1) < 4)
	    {
	        alert(str_jumin1 + "-" + str_jumin2 + ' 은 없는 주민등록번호 입니다. 다시 입력해 주세요!!');
	        return false;
	    }*/
	
	    if(str_jumin2.substring(0,1) > 4)
	    {
	        //alert(str_jumin1 + "-" + str_jumin2 + ' 은 없는 주민등록번호 입니다. 다시 입력해 주세요!!');
	        return false;
	    }
	
	    if((str_jumin1.length > 7) || (str_jumin2.length > 8))
	    {
	        //alert(str_jumin1 + "-" + str_jumin2 + ' 은 없는 주민등록번호 입니다. 다시 입력해 주세요!!');
	        return false;
	    }
	
	    if ((str_jumin1 == '72') || ( str_jumin2 == '18'))
	    {
	        //alert(str_jumin1 + "-" + str_jumin2 + ' 은 없는 주민등록번호 입니다. 다시 입력해 주세요!!');
	        return false;
	    }
	
	    var f1=str_jumin1.substring(0,1)
	    var f2=str_jumin1.substring(1,2)
	    var f3=str_jumin1.substring(2,3)
	    var f4=str_jumin1.substring(3,4)
	    var f5=str_jumin1.substring(4,5)
	    var f6=str_jumin1.substring(5,6)
	    var hap=f1*2+f2*3+f3*4+f4*5+f5*6+f6*7
	    var l1=str_jumin2.substring(0,1)
	    var l2=str_jumin2.substring(1,2)
	    var l3=str_jumin2.substring(2,3)
	    var l4=str_jumin2.substring(3,4)
	    var l5=str_jumin2.substring(4,5)
	    var l6=str_jumin2.substring(5,6)
	    var l7=str_jumin2.substring(6,7)
	    hap=hap+l1*8+l2*9+l3*2+l4*3+l5*4+l6*5
	    hap=hap%11
	    hap=11-hap
	    hap=hap%10
	    if (hap != l7)
	    {
	        //alert(str_jumin1 + "-" + str_jumin2 + ' 은 없는 주민등록번호 입니다. 다시 입력해 주세요!!');
	        return false;
	    }
	
	
	    var i9=0
	
	    return true;
	    //if (!errfound)
	    //        submit();
	}
	
	//외국인 주민등록번호 체크
	function isRegNo_fgnno(fgnno)
	{
		var sum=0;
		var odd=0;
		var msg="주민등록번호가 유효하지 않습니다.\r\n다시 입력하세요.";
		buf = new Array(13);
		
		for(i=0; i<13; i++) { buf[i]=parseInt(fgnno.charAt(i)); }
		odd = buf[7]*10 + buf[8];
		if(odd%2 != 0) { 
			alert(msg);
			return false; 
		}
		if( (buf[11]!=6) && (buf[11]!=7) && (buf[11]!=8) && (buf[11]!=9) )
		{
			alert(msg);
			return false;
		}
		multipliers = [2,3,4,5,6,7,8,9,2,3,4,5];
		for(i=0, sum=0; i<12; i++) { sum += (buf[i] *= multipliers[i]); }
		sum = 11 - (sum%11);
		if(sum >= 10) { sum -= 10; }
		sum += 2;
		if(sum >= 10) { sum -= 10; }
		if(sum != buf[12]) { 
			alert(msg);
			return false 
		}
		return true;
	}
	
	function error (elem,text) {
	    if (errfound) return;
	    window.alert(text);
	    elem.select();
	    elem.focus();
	    errfound=true;
	}
	
	function ck_tag(text) {
		var tmp  = /</gi;
		var temp = text.replace(tmp,"%lt;");
		return temp;
	}
/**------------------------------------------------------------------------------------------**/




	/*===============================================*/
	// 일반 텍스트 확인
	/*===============================================*/
	// 공백 존재여부 확인
	function check_TextBlank(strName,objText,strContent){
	
		if (char_trim(objText.value) == ""){
			alert(strName + "을(를) 입력하십시오." + "\n\n" + strContent);
			objText.focus();
			return false;
		}
		return true;
	}
	/*===============================================*/
	// 텍스트 길이 확인
	/*===============================================*/
	function check_length(sourceOBJ,targetOBJ){
		var str = sourceOBJ.value;
		targetOBJ.value = str.length;
	}
	/*===============================================*/
	// 공백 없애기
	//
	function char_trim(str){
	
		if (str == 0){
			result = str;
		}
		else{
			v_len = str.length;
			result="";
			for (var i=0; i<v_len; i++){
				schar = str.charAt(i);
				if (schar != " ") {
					result = result + schar;
				}
	
			}
		}
		return result;
	}
	
	
	//문자열에 공백이 포함되었는지 검사하는 함수
	function ckBlank(getValue)
	{
	    var temp = "";
	    for(var i = 0 ; i < getValue.length ; i++)
	    {
	        temp = getValue.substring(i,i+1);
	        if(temp == " ") return true;
	    }
	    return false;
	}

	//문자열에 특수문자가 포함되었는지 검사하는 함수
	function ckFieldStr(getValue)
	{
	    var temp = 0;
	    for(var i = 0 ; i < getValue.length ; i++)
	    {
	        temp = getValue.substring(i,i+1).charCodeAt();
	        if(((temp < 97 || temp > 122) && (temp < 48 || temp > 57)) && (temp != 95))
	        {
	            return true;
	        }
	    }
	    return false;
	}



	// desc     : 창이름으로 새창 popup
	function openWinByName(uri,name,width,height,scroll)
	{
		var left = (screen.width-width)/2;
		var top = (screen.height-height)/2;
		var newWin=window.open(uri,name,'width='+width+',height='+height+',left='+left+',top='+top+', scrollbars='+scroll+', toolbar=no');
		newWin.focus();
	}


	function openWinByFullScreen(uri, name, scroll) {

		var left = (screen.width)/2;
		var top = (screen.height)/2;
		newWin=window.open(uri, name,'left='+left+',top='+top+', scrollbars=auto, fullscreen=yes, toolbar=no, scrollbars='+scroll);
		newWin.focus();
	}


	// desc     : 문구 선택시 라디오버튼까지 선택
	// input    : [obj]
	// return   : boolean
	function captionclick(obj) {
		if ("INPUT" == event.srcElement.tagName)
			event.cancelBubble = true;
		else {
			obj.children[0].click();
			return false;
		}
	}

	/* 필수 길이 체크 */
	function essentialityLength(strValue, num, strMsg)
	{
		var cnt  = strValue.bytes();
		
		if ( cnt !=  num ) {
			msg = "[" + strMsg + "] : " + num + "자리를 입력하십시오.";
			alert(msg);
			return false;
		}

		return true;
	}

	/* 전화번호 숫자/- 허용 */
	function checkPhoneNumber(strValue, num, strMsg)
	{
		var chars = "0123456789-";
		var rvalue = "";

		for (var inx = 0; inx < strValue.length; inx++) {
		   if (chars.indexOf(strValue.charAt(inx)) == -1) {
			   alert(strMsg + "는 숫자와 '-'만 입력할 수 있습니다." );
			   return false;
		   }
		}
		
		if ( strValue.length != 0 && strValue.length > num ) {
			msg = "[" + strValue + "]를 " + num + "자리 이하로 입력하십시오.";
			alert(msg);
			return false;
		}

		return true;
	}

	/* 숫자/, 허용 */
	function checkCommaNumber(strValue, num, strMsg)
	{
		if (!isNumComma(strValue)) {
		   alert(strMsg + "는 숫자와 ','만 입력할 수 있습니다." );
		   return false;
	    }
	    
		if ( num > 0 ) {
			if ( strValue.length != 0 && strValue.length > num ) {
				msg = "[" + strValue + "]를 " + num + "자리 이하로 입력하십시오.";
				alert(msg);
				return false;
			}
		}

		return true;
	}

	/* 숫자/,/. 허용 */
	function checkCommaPointNumber(strValue, num, strMsg)
	{
		if (!isNumCommaPoint(strValue)) {
		   alert(strMsg + "는 숫자와 '콤마(,)', '포인트(.)'만 입력할 수 있습니다." );
		   return false;
	    }
	    
		if ( num > 0 ) {
			if ( strValue.length != 0 && strValue.length > num ) {
				msg = "[" + strValue + "]를 " + num + "자리 이하로 입력하십시오.";
				alert(msg);
				return false;
			}
		}

		return true;
	}


	// 숫자 체크
	function chkNumber(){
	   if((event.keyCode < 48)||(event.keyCode > 57))
	      event.returnValue = false;
	}

	// desc  : 문자열의 길이가 원하는 길이보다 작을때 부족한 길이만큼 특정 문자를 더하여 반환하는 함
	// input  : [ strValue, strLength, val ]
	// return : string
	function intToString( strValue, strLength, val )
	{
		var nsize = 0;
		var param = "";

		nsize = GetLength(strValue);

		if ( nsize < strLength ) {
			for (var i=0; i<strLength-nsize ;i++ )
			{
				param += val;
			}
		}

		var result = param+strValue;

		return result;
	}

	// Email Check
	function EmailCheck(strEmail) {
		if(strEmail == "") return true;
		var regDoNot = /(@.*@)|(\.\.)|(@\.)|(\.@)|(^\.)/;
		var regMust = /^[a-zA-Z0-9\-\.\_]+\@[a-zA-Z0-9\-\.]+\.([a-zA-Z]{2,3})$/;

		if ( !regDoNot.test(strEmail) && regMust.test(strEmail) ) {
			return true;
		} else {
			msg = "이메일 주소를 확인해 주세요.";
			alert(msg);
			return false;
		}
	}

	// desc  : 문자 뒤의 공백문자를 제거하는 함수
	// input  : string
	// return : string
	function rTrim ( str ) {
		str = str + "";
		var len = str.length;

		for(var i = (len - 1); (str.charAt(i) == ' '); i--);
		str = str.substring(0, i + 1);

		return str;
	}

	// desc  : 문자 맨 앞의 공백문자를 제거하는 함수
	// input  : string
	// return : string
	function lTrim ( str ) {
		var len = str.length;
		var i = 0;

		for(i = 0; str.charAt(i) == ' '; i++);
		str = str.substring(i, len);

		return str;
	}

	// desc  : 문자 앞뒤의 공백문자를 제거하는 함수
	// input  : string
	// return : string
	function Trim( strValue ) {
	 		strValue = lTrim(rTrim(strValue));
	 		return strValue;
	}
	
	// desc     : 숫자만 허용하기
	// input    : [str, strName]
	// return   : boolean
	function onlyNumber(str, strName)
	{
		var regMust = /[^_(0-9)]/;
		
		if ( regMust.test(str) ) {
			msg = "[" + strName + "] : 숫자만 입력하실 수 있습니다!\r\n\r\n다시 한번 확인하십시오.";
			alert(msg);
			return false;
		}
		return true;
	}

	// desc     : 숫자만 허용하기
	// input    : [str, strName]
	// return   : boolean
	function onlyNumberHyphen(str, strName)
	{
		var regMust = /[^_(0-9)-]/;

		if ( regMust.test(str) ) {
			msg = "[" + strName + "] : 숫자만 입력하실 수 있습니다!\r\n\r\n다시 한번 확인하십시오.";
			alert(msg);
			return false;
		}
		return true;
	}
	
	// desc     : 숫자, 소숫점 허용, 콤마 허용
	// input    : [str, strName]
	// return   : boolean
	function onlyNumberPoint(str, strName)
	{
		var regMust = /[^_(0-9)\.,]/;
		if ( regMust.test(str) ) {
			msg = "[" + strName + "] : 숫자만 입력하실 수 있습니다!\r\n\r\n다시 한번 확인하십시오.";
			alert(msg);
			return false;
		}
		return true;
	}
	

	// desc     : 숫자로만 된 스트링 허용 안함
	// input    : [str, strName]
	// return   : boolean
	function notOnlyNumber(str, strName)
	{
		var regMust = /[^_(0-9)]/;

		if ( !regMust.test(str) ) {
			msg = "[" + strName + "] : 숫자만 입력하실 수 없습니다!\r\n\r\n다시 한번 확인하십시오.";
			alert(msg);
			return false;
		}
		return true;
	}

	// desc     : 첫글자를 숫자로만 된 스트링 허용 안함
	// input    : [str, strName]
	// return   : boolean
	function firstnotOnlyNumber(str, strName)
	{
		var regMust = /[^_(0-9)]/;

		if ( !regMust.test(str.substring(0,1)) ) {
			msg = "[" + strName + "] : 첫글자는 숫자로 입력하실 수 없습니다!\r\n\r\n다시 한번 확인하십시오.";
			alert(msg);
			return false;
		}
		return true;
	}	

	// desc     : 첫글자를 영문으로만 된 스트링 허용
	// input    : [str, strName]
	// return   : boolean
	function firstOnlyEng(str, strName)
	{
		var regMust = /[^(a-zA-Z)]/;

		if ( regMust.test(str.substring(0,1)) ) {
			msg = "[" + strName + "] : 첫글자는 영문만 입력하실 수 있습니다!\r\n\r\n다시 한번 확인하십시오.";
			alert(msg);
			return false;
		}
		return true;
	}


	// desc     : 입력여부 체크하기
	// input    : [str, strName]
	// return   : boolean
	function EmptyCheck( strValue, strName ) {
		var strlength = char_trim(strValue).length;

		if ( strlength == 0 ) {
			msg = "[" + strName + "] : 입력해주십시오!";
			alert(msg);
			return false;
		} else
			return true;
	}

	// desc     : 선택여부 체크하기
	// input    : [str, strName]
	// return   : boolean
	function SelEmptyCheck( strValue, strName ) {
		var strlength = strValue.length;

		if ( strlength == 0 ) {
			msg = "[" + strName + "] : 선택해주십시오!";
			alert(msg);
			return false;
		} else
			return true;
	}
	

	// desc     : 일정 체크하기
	// input    : [str, strName]
	// return   : boolean
	function SchdulCheck( schdulStuus, strName ) {

		if ( schdulStuus == 'N' ) {
			msg = strName + " 의  등록기간이 아닙니다.";
			alert(msg);
			return false;
		} else
			return true;
	}


	// desc     : 문자열의 최저, 최대 길이를 체크하여 결과를 리턴한다.
	// input    : [strValue, strName, lowLength, highLength]
	// return   : boolean
	function LengthCheck( strValue, strName, lowLength, highLength )
	{
		var nsize = 0;
		nsize = GetLength(strValue);
		if ( lowLength > 0 && nsize == 0 ) {
			msg = "[" + strName + "] : 입력해주십시오!";
			alert(msg);
			return false;
		}

		if ( nsize < lowLength || nsize > highLength ) {
			if ( lowLength == highLength ) {
				msg = "[" + strName + "] : " + lowLength + "Byte를 입력해주십시오!\r\n\r\n (주의: 한글 1자는 2Byte로 계산함.)";
				alert(msg);
				return false;
			} else {
				msg = "[" + strName + "] : " + lowLength + " - " + highLength + " Byte를  입력해주십시오!\r\n\r\n (주의: 한글 1자는 2Byte로 계산함.)";
				alert(msg);
				return false;
			}
		}
		else
			return true;
	}

	// desc     : 문자열의 길이를 리턴한다.
	// input    : [strValue]
	// return   : int
	function GetLength( strValue )
	{
		var nsize = 0;
		var chrOrig;
		var charEscaped;

		for( var intinx = 0; intinx <= strValue.length -1 ; intinx++ ){
			chrOrig = strValue.substring(intinx,intinx+1);
			chrEscaped = escape(chrOrig);
			if ( chrEscaped.substring(0,2) == "%u" )
				nsize = nsize + 2;
			else
				nsize++;
		}
		return nsize;
	}

	/**
	title 	: 영문/숫자만 허용
	desc 	: 영문/숫자만 허용하기
	**/
	function onlyEngNumber(str, strName) {
		//var regMust = /[^_(a-zA-Z0-9)]/;
		var regMust = /[^(a-zA-Z0-9)]/;

		if ( regMust.test(str) ) {
			msg = "[" + strName + "] : 영문과 숫자만 입력하실 수 있습니다!\r\n\r\n  다시 한번 확인하십시오.";
			alert(msg);
			return false;
		}
		return true;
	}

	// desc     : 영문소문자/숫자만 허용하기 
	// input    : [str, strName]
	// return   : boolean
	function onlyLowEngNumber(str, strName)
	{
		var regMust = /[^_(a-z0-9)]/;

		if ( regMust.test(str) ) {
			msg = "[" + strName + "] : 영문 소문자와 숫자만 입력하실 수 있습니다!\r\n\r\n다시 한번 확인하십시오.";
			alert(msg);
			return false;
		}
		return true;
	}

	// desc     : 영문 반드시 포함하기 
	// input    : [xObj, sStr]
	// return   : boolean
	function ifChrChk(xObj, sStr)
	{
		var temp;
		var icount;
		icount = 0;
		len = xObj.value.length;
		for(k=0;k<len;k++)
		{
			temp = xObj.value.charAt(k);
			if((temp >= "A" && temp <= "Z") || (temp >= "a" && temp <= "z"))
			{	
				icount++;
			}
		}
	
		if (icount < 1)
		{
			alert(sStr + "는 영문자를 반드시 포함해야 합니다.");
			xObj.focus();
			return false;
		}

		return true;
	}
	
	// desc     : 한글, 특수문자 제한하기 
	// input    : [xObj, sStr]
	// return   : boolean
	function ifKoChrChk(xObj, sStr)
	{	
		var temp;
		var icount;
		icount = 0;
		len = xObj.length;
		for(k=0;k<len;k++)
		{
			temp = xObj.charAt(k);
			if((temp >= "A" && temp <= "Z") || (temp >= "a" && temp <= "z") || (temp == "_") || (temp == "-") || (temp == ".") || (temp >= "0" && temp <= "9"))
			{
			}
			else
			{		
				icount+= 1;
			}
		}
	
		if (icount > 0)
		{
			alert(sStr + "는 한글 또는 특수문자는 사용불가능합니다...");
			//xObj.focus();
			return false;
		}	

		return true;
	}
	
	// desc     : 회원가입시 아이디, 비밀번호 입력 제한 
	// input    : [xObj, sStr]
	// return   : boolean
	function userJoinInputChk(xObj, sStr, lowLength, highLength)
	{	
		var temp;
		var icount = 0;
		var nsize = xObj.length;
		var msg = "";
		
		if ( lowLength > 0 && nsize == 0 ) {
			msg = "[" + sStr + "] : 입력해주십시오!";
			alert(msg);
			return false;
		}
			
		for(k=0;k<nsize;k++)
		{
			temp = xObj.charAt(k);
			if((temp >= "A" && temp <= "Z") || (temp >= "a" && temp <= "z") || (temp == "_") || (temp >= "0" && temp <= "9"))
			{
			}
			else
			{		
				icount+= 1;
			}
		}
	
		if (icount > 0)
		{
			alert(sStr + "는 한글 또는 특수문자는 사용불가능합니다.");
			//xObj.focus();
			return false;
		}	
		
		if ( nsize < lowLength || nsize > highLength ) {
			if ( lowLength == highLength ) {
				msg = "[" + sStr + "] : " + lowLength + "자를 입력해주십시오!";
				alert(msg);
				return false;
			} else {
				msg = "[" + sStr + "] : " + lowLength + " - " + highLength + " 자를  입력해주십시오!";
				alert(msg);
				return false;
			}
		}

		return true;
	}
	
	// desc     : 회원가입시 비밀번호 입력 제한 
	// input    : [xObj, sStr]
	// return   : boolean
	function userPwChk(xObj, sStr, lowLength, highLength)
	{	
		var temp;
		var icount = 0;
		var nsize = xObj.length;
		var msg = "";
		var jcount = 0;
		var kcount = 0;
		var lcount = 0;
		
		if ( lowLength > 0 && nsize == 0 ) {
			msg = "[" + sStr + "] : 입력해주십시오!";
			alert(msg);
			return false;
		}
			
		for(k=0;k<nsize;k++)
		{
			temp = xObj.charAt(k);
			if((temp >= "A" && temp <= "Z") || (temp >= "a" && temp <= "z")){		
				jcount = 1;
			} else if ((temp >= "0" && temp <= "9")) {		
				kcount = 1;
			} else if ((temp == "~") || (temp == "!") || (temp == "@") || (temp == "#") || (temp == "$") || (temp == "%") || (temp == "^") || (temp == "*") || (temp == "(") || (temp == ")")) {		
				lcount = 1;
			} else if ((temp == "_") || (temp == "{") || (temp == "}") || (temp == "[") || (temp == "]")) {		
				lcount = 1;
			} else {		
				icount+= 1;
			}
		}
	
		if (icount > 0) {
			alert(sStr + "는 한글 또는 특수문자는 사용불가능합니다.");
			return false;
		}
	
		if (jcount < 1) {
			alert(sStr + "는 영문 필수 입력사항입니다.");
			return false;
		}
	
		if (kcount < 1) {
			alert(sStr + "는 숫자 필수 입력사항입니다.");
			return false;
		}
	
		if (lcount < 1) {
			alert(sStr + "는 특수문자 필수 입력사항입니다.");
			return false;
		}	
		
		
		if ( nsize < lowLength || nsize > highLength ) {
			if ( lowLength == highLength ) {
				msg = "[" + sStr + "] : " + lowLength + "자를 입력해주십시오!";
				alert(msg);
				return false;
			} else {
				msg = "[" + sStr + "] : " + lowLength + " - " + highLength + " 자를  입력해주십시오!";
				alert(msg);
				return false;
			}
		}

		return true;
	}
	
	/**
	title 	: 사업자 등록번호 체크
	desc 	: 사업자 등록번호 체크하기
	**/
	function check_companynum(saup1,saup2,saup3) {
		 var checkID = new Array(1, 3, 7, 1, 3, 7, 1, 3, 5, 1);
		 var bizID = "" + saup1 + saup2 + saup3;
		 var i, Sum=0, c2, remander;

		 for (i=0; i<=7; i++) Sum += checkID[i] * bizID.charAt(i);

		 c2 = "0" + (checkID[8] * bizID.charAt(8));
		 c2 = c2.substring(c2.length - 2, c2.length);

		 Sum += Math.floor(c2.charAt(0)) + Math.floor(c2.charAt(1));

		 remander = (10 - (Sum % 10)) % 10 ;

		 if (Math.floor(bizID.charAt(9)) != remander) {
			  alert("정확한 사업자 등록번호를 입력하세요");
			  return false;
		 } else{
			return true;
		 }
	}
	
	// desc     : 사업자 등록번호 길이 체크
	// input    : [strValue, lowLength, companyLength]
	// return   : boolean
	function LengthCheck_companynum(strValue, lowLength, companyLength)
	{
		var nsize = 0;
		nsize = GetLength(strValue);
		if ( lowLength > 0 && nsize == 0 ) {
			msg = "사업자 등록번호 " + companyLength + "자리를 입력해주십시오!";
			alert(msg);
			return false;
		}

		if ( nsize < companyLength || nsize > companyLength ) {
			msg = "사업자 등록번호 " + companyLength + "자리를 입력해주십시오!";
			alert(msg);
			return false;
		}
		else
			return true;
	}

	/**
	title 	: 체크 박스  전체 선택 /해제
	desc 	: 체크 박스  전체 선택 /해제
	**/
	function CheckAll(obj_this, obj){
		var b_chk = false;
		if (obj_this.checked == true)
			b_chk = true;

		if (obj) {
			if (obj.length) {
				for (var i=0; i <obj.length; i++ ) {
					obj[i].checked = b_chk ;
				}
			} else {
				obj.checked = b_chk;
			}
		} else {
			//alert("선택할 항목이  없습니다.");
		}
	}

	/* 전화번호 숫자/- 허용 */
	function CheckMenuId(strValue, num, strMsg)
	{
		var chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-";
		var rvalue = "";

		for (var inx = 0; inx < strValue.length; inx++) {
		   if (chars.indexOf(strValue.charAt(inx)) == -1) {
			   alert(strMsg + "는 영문과 숫자와 '-'만 입력할 수 있습니다." );
			   return false;
		   }
		}

		if ( strValue.length != 0 && strValue.length > num ) {
			msg = "[" + strValue + "]를 " + num + "자리 이하로 입력하십시오.";
			alert(msg);
			return false;
		}

		return true;
	}

	/* 한글, 영문, 숫자 허용 */
	function CheckMenuName(strValue, strMsg)
	{
		var pattern = new RegExp("[^가-힝\x20a-zA-Z0-9&/'()]", "i");   
		if (pattern.exec(strValue) == null) {
			return true;
		} else {
		    alert("[" + strMsg + "] : 한글과 영문과 숫자와 특수문자(&, /, ', -)만 입력할 수 있습니다." );
		    return false;
		}
	}
	  
		
	/******************************************
	 * 년월 날짜 체크
	 ******************************************/	
	function yymmCheck(val, len, strName){
		if(val == "") return true;
		//var tmpDate = val.replace(/-/gi,"");
		var tmpDate = val;
		var tmpMM = tmpDate.substring(tmpDate.length-2);
		var msg;
		
		if (!onlyNumber(val, strName)) {
			return false;
		}
		
		if (tmpDate.length != len || tmpMM < 1 || tmpMM > 12) {
			msg = "[" + strName + "] : 유효하지 않은 날짜 형식입니다.";
			alert(msg);
			return false;
		}
		
		return true;
	} 
		
	/******************************************
	 * 두 날짜 비교하여 결과를 리턴
	 ******************************************/	
	function comparePeriod(fromValue, toValue, fromName, toName){
		if(fromValue == "" || toValue == "") return true;
		var del = /[^(0-9)]/gi;
		var fromDate = Number(fromValue.replace(del,""));
		var toDate = Number(toValue.replace(del,""));
		var msg;

		if (fromDate > toDate) {
			msg = fromName + "이  " + toName + "보다 이후일 수 없습니다.";
			alert(msg);
			return false;
		}
		
		return true;
	}
	
	/******************************************
	 * 두 값을 비교하여 결과 리턴
	 ******************************************/	
	function compareValue(fromValue, toValue, fromName, toName){
		if(fromValue == "" || toValue == "") return true;
		var del = /,/gi;
		var from = Number(fromValue.replace(del,""));
		var to= Number(toValue.replace(del,""));
		var msg;

		if (from > to) {
			msg = fromName + "이  " + toName + "보다 클 수 없습니다.";
			alert(msg);
			return false;
		}
		
		return true;
	}


	/* 특수문자 처리*/
	function ChangeSpecialCharacter(str)
	{
		var returnStr;

		returnStr = str;

		returnStr = returnStr.replace(/\</g,		"&lt;");
		returnStr = returnStr.replace(/\>/g,		"&gt;");
		returnStr = returnStr.replace(/\ /g,		"&nbsp;");
		returnStr = returnStr.replace(/\"/g,		"&quot;");
		returnStr = returnStr.replace(/\'/g,		"&#39;");

		return returnStr;
	}


   function truncateString(str, max) {
		if(blen(str) > max) {
			return bsubstring(str, max - 2) + ".."
		} else {
			return str
		}
	}

	function bsubstring(str, to) {
		var bindex = 0
		for(var i = 0; i < str.length; i++) {
			bindex += str.charAt(i) >= 'ㄱ' ? 2 : 1
			if(bindex > to) {
				return str.substring(0, i)
			}
		}
		return str
	}

	function blen(str) {
		if(typeof str == "undefined" || str == null) return str;

		var result = 0
		for(var i = 0; i < str.length; i++) {
			result += str.charAt(i) >= 'ㄱ' ? 2 : 1
		}

		return result
	}

	/* URL에서 확장자 검색 */
	function GetFileExter(URL) {
		var sUrl;
		var fileExiter;
		var idxNumber;
		var sArray;

		if (sUrl == "")	{
			return "";
		}

		sUrl = URL;

		// ? 이후 제거
		if(sUrl.indexOf("?") > 0) {
			sUrl = sUrl.substr(1, sUrl.indexOf("?")-1);
		}

		// 파일 확장자 추출
		if(sUrl.indexOf(".") > 0) {
			sArray = sUrl.split(".");
			fileExiter = sArray[sArray.length-1].toLowerCase();
		} else {
			fileExiter = "";
		}

		return fileExiter;
	}


	 function _loadingHtml() {
	 	var strHtml = "";
 		strHtml = strHtml + "<table width='100%' height='100%'>";
 		strHtml = strHtml + "<tr><td valign='middle' align='center'><img src='/images/loading/loading.gif'></td></tr>";
 		strHtml = strHtml + "</table>";
  		return strHtml;
 	}





	function allBlur() {
		for (i = 0; i < document.links.length; i++){
			document.links[i].onfocus = document.links[i].blur;
		}
	}
	
	function OpenWin(url,w,h) {
		window.open (url,"win",'toolbar=no,location=no,directory=no,status=no,menubar=no,scrollbars=yes,resizable=no,copyhistory=no,width=' + w +',height=' + h + "'");
	}
	function OpenWin2(url,w,h) {
		window.open (url,"win",'toolbar=no,location=no,directory=no,status=no,menubar=no,scrollbars=no,resizable=no,copyhistory=no,width=' + w +',height=' + h + "'");
	}
	
	
	var bExplorer5 = (navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion.substring(navigator.appVersion.indexOf("MSIE")+5,navigator.appVersion.indexOf("MSIE")+8) < "5.5");
	var bExplorer5Plus = (navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion.substring(navigator.appVersion.indexOf("MSIE")+5,navigator.appVersion.indexOf("MSIE")+8) >= "5.5");
	
	function InitMenu(){
		var bar = menuBar.children
	
		for(var i=0;i < bar.length;i++)	{
			var menu=eval(bar[i].menu)
			menu.style.visibility = "hidden"
			bar[i].onmouseover = new Function("ShowMenu("+bar[i].id+")")
			var Items = menu.children
			for(var j=0; j<Items.length; j++)	{
				var menuItem = eval(Items[j].id)
	
				if(menuItem.menu != null){
					menuItem.innerHTML += "<Span Id="+menuItem.id+"_Arrow class='Arrow'>4</Span>"
					FindSubMenu(menuItem.menu)
				}
	
				if(menuItem.cmd != null) {
					menuItem.onclick = new Function("Do("+menuItem.id+")")
				}
	
				menuItem.onmouseover = new Function("highlight("+Items[j].id+")")
			}
		}
	}

	function FindSubMenu(subMenu){
		var menu=eval(subMenu)
		var Items = menu.children
	
		for(var j=0; j<Items.length; j++){
			menu.style.visibility = "hidden"
			var menuItem = eval(Items[j].id)
	
			if(menuItem.menu!= null){
				menuItem.innerHTML += "<Span Id="+menuItem.id+"_Arrow class='Arrow'>4</Span>"
				//var tmp = eval(menuItem.id+"_Arrow")
				//tmp.style.pixelLeft = 35
				//menuItem.getBoundingClientRect().right - tmp.offsetWidth - 15
				FindSubMenu(menuItem.menu)
			}
	
			if(menuItem.cmd != null){
				menuItem.onclick = new Function("Do("+menuItem.id+")")
			}
			menuItem.onmouseover = new Function("highlight("+Items[j].id+")")
		}
	}
	
	function ShowMenu(obj){
		 HideMenu(menuBar)
		 var menu = eval(obj.menu)
		var bar = eval(obj.id)
		bar.className="barOver"
		menu.style.visibility = "visible"
	
		if (bExplorer5) {
			var a=0;
			for(var i=0;i < menuBar.children.length;i++)	{
				a+=1;
			}
	
			menu.style.pixelTop =  obj.getBoundingClientRect().top + obj.offsetHeight + Bdy.scrollTop + 59
			menu.style.pixelLeft = (122)*a + obj.getBoundingClientRect().left
		}
		else if(bExplorer5Plus) {
			menu.style.pixelTop =  obj.getBoundingClientRect().top + obj.offsetHeight + Bdy.scrollTop+3
			menu.style.pixelLeft = obj.getBoundingClientRect().left + Bdy.scrollLeft - 2
		}
	
	}

	function highlight(obj){
		var PElement = eval(obj.parentElement.id)
		if(PElement.hasChildNodes() == true){
			var Elements = PElement.children
				for(var i=0;i<Elements.length;i++){
				TE = eval(Elements[i].id)
				TE.className = "menuItem"
				}
		}
		obj.className="ItemMouseOver"
		window.defaultStatus = obj.title
		ShowSubMenu(obj)
	}
	
	function Do(obj){
		var cmd = eval(obj).cmd
		window.navigate(cmd)
	}
	
	function HideMenu(obj){
		if(obj.hasChildNodes()==true){
			var child = obj.children
			for(var j =0;j<child.length;j++){
				if (child[j].className=="barOver"){
					var bar = eval(child[j].id)
					bar.className="Bar"
				}
	
				if(child[j].menu != null){
					var childMenu = eval(child[j].menu)
					if(childMenu.hasChildNodes()==true){
						HideMenu(childMenu)
					}
					childMenu.style.visibility = "hidden"
				}
			}
		}
	}
	
	function ShowSubMenu(obj){
		PMenu = eval(obj.parentElement.id)
		HideMenu(PMenu)
	
		if(obj.menu != null){
			var menu = eval(obj.menu)
			menu.style.visibility = "visible"
			menu.style.pixelTop =  obj.getBoundingClientRect().top + Bdy.scrollTop
			menu.style.pixelLeft = obj.getBoundingClientRect().right + Bdy.scrollLeft
			if(menu.getBoundingClientRect().right > window.screen.availWidth )
			menu.style.pixelLeft = obj.getBoundingClientRect().left - menu.offsetWidth
		}
	}
	
	// ******************************************
	// 전체 클릭시(allChk(this, 'chk명'))
	// ******************************************
	function allChk(obj, chk){
		var chkObj = document.getElementsByName(chk);
	
		if(typeof(chkObj) == 'undefined'){
			return;
		}else{
			if(typeof(chkObj.length) == 'undefined'){
				chkObj.checked = obj.checked;
			}else{
				for(var i=0; i < chkObj.length; i++){
					chkObj[i].checked = obj.checked;
				}
			}
		}
	}
	
	
	/* 통화 표시 */
	function getFormatMoney(value)  {
	    if(value == null || value == "" || typeof value == "undefined") return "0";
		 var num  = String(value);
	
		 re   = /^\$|,/g;
		 num   = num.replace(re, "");
		 fl   = "";
	
		 if(num < 0) {
		    num  = num * (-1);
		    fl  = "-";
		 } else  {
		    num  = num * 1;
		 }
	 
		 num   = new String(num);
	
		 var temp = "";
		 var co  = 3;
		 var num_len = num.length;
	
		 while (num_len > 0) {
		     num_len = num_len - co;
	
		   if(num_len < 0) {
		     co = num_len + co;
		     num_len = 0;
		   }
	
		   temp = "," + num.substr(num_len, co) + temp;
		 }
	
		 return fl + temp.substr(1);
	}
	
	/* 주민번호로 나이 리턴 */
	function getAgeInfo(cus_sec_no){
	    var regex = new RegExp(/(\d{2})(\d{2})(\d{2})-(\d{1})\d{6}/);
	    var matches = regex.exec(cus_sec_no);
	    if (!matches || matches.length != 5) {
	        alert("주민번호가 잘못되었습니다.");
	        return;
	        //return {age: 0, sex: null };
	    }
	
	    var year = parseInt(((
	        matches[4] == "3" ||
	        matches[4] == "4" ||
	        matches[4] == "7" ||
	        matches[4] == "8") ? "20" : "19") + matches[1]);
	    var month = parseInt(matches[2]);
	    var day = parseInt(matches[3]);
	    var birthDay = new Date(year, month, day);
	    var toDay = new Date();
	
		var age = toDay.getFullYear() - birthDay.getFullYear() + 1;
	
	    return age;
	}
	
	
	function dtFormat(dt, type, delim){
		var dtFormat = "";

		 if (dt == null || dt == "")
				return "";
	
		 if(typeof delim == "undefined" || delim == null) delim = "/";
	
		 if (type == "Y") {
			dtFormat = dt.substring(0, 4);
		} else if (type =="YM") {
			dtFormat = dt.substring(0, 4) + delim + dt.substring(4, 6);
		} else if (type =="MD") {
			dtFormat = dt.substring(4, 6) + delim + dt.substring(6, 8);
		} else if (type == "YMD") {
			dtFormat = dt.substring(0, 4) + delim + dt.substring(4, 6) + delim + dt.substring(6, 8);
		} else if (type == "YMDHI") {
			dtFormat = dt.substring(0, 4) + delim + dt.substring(4, 6) + delim + dt.substring(6, 8) + " " + dt.substring(8, 10) + ":"  + dt.substring(10, 12);
		} else if (type == "YMDH") {
			dtFormat = dt.substring(0, 4) + delim + dt.substring(4, 6) + delim + dt.substring(6, 8) + " " + dt.substring(8, 10);
		} else if (type =="MDHI") {
			dtFormat = dt.substring(4, 6) + delim + dt.substring(6, 8) + " " + dt.substring(8, 10) + ":" + dt.substring(10, 12);
		} else if (type =="YMDHIS") {
			dtFormat = dt.substring(0, 4) + delim + dt.substring(4, 6) + delim + dt.substring(6, 8) + " " + dt.substring(8, 10) + ":" + dt.substring(10, 12) + ":" + dt.substring(12, 14);
		} else {
			dtFormat = dt;
		}
	
		return dtFormat;
	}

	// 엔터키를 누를때 이벤트 발생하기 - 목록 검색
	function commonEnterCheck()
	{
		if(event.keyCode == 13)
		{
			goSearch();
		}
	}

	// 이미지 파일 체크
	function imageCheck(imageFullName)
	{
		imageFullName = Trim(imageFullName);
		var imageFile  = imageFullName.split(".");
		var imageExte  = imageFile[imageFile.length-1].toLowerCase();    
		if ( imageExte != "gif" && imageExte != "jpg"  && imageExte != "bmp" && imageExte != "png"){
			alert("이미지 파일은 gif, jpg, bmp, png 파일만 등록할 수 있습니다.");
			return false;
		} else {
			return true;
		}
	}	
		
	// 포커스 자동 변경
	function autoFocusChange(checkTag, nextTag, len)
	{
		if ( checkTag.value.length >= len )
		{
			checkTag.blur();
			nextTag.focus();
		}
	}

	// DRM 적용
	function popDrmApplication(type, totalCount, xmlFilePath)
	{
		var url = context + "/drm/popDrmApplication.vw?type=" + type + "&totalCount=" + totalCount + "&xmlFilePath=" + xmlFilePath;
		openWinByName(url,'popDrmApplication','500','200','no');
	}

	// Excel DRM 적용
	function popDrmExcelApplication(type, totalCount, xmlFilePath)
	{
		var url = context + "/drm/popDrmExcelApplication.vw?type=" + type + "&totalCount=" + totalCount + "&xmlFilePath=" + xmlFilePath;
		var name 	= "popDrmExcelApplication";
		var width 	= "600";
		var height	= "600";
		var scroll 	= "no";
		var left 	= (screen.width-width)/2;
		var top 	= (screen.height-height)/2;
				
		returnWin = window.open(url,name,'width='+width+',height='+height+',left='+left+',top='+top+', scrollbars='+scroll+', toolbar=no');
		returnWin.focus();
		return returnWin;
		
	}
	
	// DRM Lodding
	function popDrmLoading(msg)
	{
		var url 	= context + "/drm/popDrmLoading.vw?msg=" + msg;
		var name 	= "popDrmLoading";
		var width 	= "517";
		var height	= "115";
		var scroll 	= "no";
		var left 	= (screen.width-width)/2;
		var top 	= (screen.height-height)/2;
				
		returnWin = window.open(url,name,'width='+width+',height='+height+',left='+left+',top='+top+', scrollbars='+scroll+', toolbar=no');
		returnWin.focus();
		return returnWin;
	}
	
	// ******************************************
	// 연속된 문자/숫자 체크
	// ******************************************		
	function continutyCheck(str, strName)
	{
		var temp 	= "";
		var temp2 	= str.substring(0,1);	//문자열 첫째 자리 값을 할당합니다.
		var con 	= 1; 					//연속3번이 되어야만 con값이 3이 될 수 있습니다.
		var chk		= true;
		
		for(i = 1; i < str.length; i++){
			temp = str.substring(i,i+1);//입력된 값을 하나하나 새로담는다
		
			if(temp == temp2){
				con++; //temp2은 그대로고...con값만 증가
			}else{
				con = 1; //연속된 값이 아니었으므로 다시 첨부터 1
				temp2 = temp;  //temp2 또한 연속된 값이 아니었으므로 다시 temp의 값을 하나 받아서 시작
			}
		
			if(con == 3){ //연속 3번
				alert("["+strName+"] 연속해서 입력하시면 안 됩니다("+temp2+").");
				chk = false;
				break; 
			}
		}
		return chk;
	}
    
    // desc  : 문자 뒤의 공백문자를 제거하는 함수
	// input  : string
	// return : string
	function rTrim ( str ) {
		str = str + "";
		var len = str.length;

		for(var i = (len - 1); (str.charAt(i) == ' '); i--);
		str = str.substring(0, i + 1);

		return str;
	}
                             
	// ===================================================================                             
	// 파일 다운로드 공통모듈
	// ===================================================================                             
	function goDownload(file_name) {
		var frm = document.getElementById("file_download_from");
		var file_name_obj = document.getElementById("file_name");
		file_name_obj.value = file_name;
		frm.action = context + "/commonModule/fileDownload.vw";
		frm.submit();
//		var url = context + "/commonModule/fileDownload.vw?file_name=" + file_name;
//		location.replace(url);
	} 
	
	/******************************************
	 * 체크박스 선택시 전체 선택/해제됨 
	 ******************************************/
	function checkBoxSel(mode, obj) { 
		
		if(obj){
		   if(obj.length){
		   		for(var i = 0 ; obj.length > i ; i ++ ) { 
		     		obj[i].checked = mode; 
		    	}//for
		   }else{
		    	obj.checked = mode;    
		   }//if
		}//if
	}     

	/******************************************
	 * 삭제시 CheckBox 선택여부 확인
	 ******************************************/
	function checkBoxCnt(obj, msg) {
		var ckb = obj;
		var cnt = 0;
		
		if(ckb){  
		 	if(ckb.length){
		  		for(var i = 0 ; i < ckb.length ; i++) {
		   			if(ckb[i].checked) {
		    			cnt++;
		   			}//if
		  		}//for
			}else {
		  		if(ckb.checked==true) {
		   			cnt++
		  		}//if
		 	}//if   
		}//if
		
		if(cnt > 0){
			return true;
		}else{
			alert(msg + " 항목을 선택하십시오");
			return false;
		}
	}

	/******************************************
	 * TextArea의 글자수 체크
	 ******************************************/		
	function countBytes(obj, len, target){
		var cnt  = obj.value.bytes();
		
		if(cnt <= len){
			target.value = cnt;
		}else{
			alert("허용 글자수를 초과하였습니다");
			obj.value = obj.value.cut(len);
			target.value = len;
		}
	}	 	
	
	/******************************************
	 * Select 초기화
	 ******************************************/
	function initSelect(form, targetNm){
		var target = "document.getElementById('"+form+"')."+targetNm;	
		var obj = eval(target);
		if(obj.length){
			for (var i=obj.length-1; i >= 1 ; i--) {
				obj[i] = null;
			}
		}
	}	 	
	
	/******************************************
	 * Select 초기화
	 ******************************************/
	function initSelectAll(form, targetNm){
		var target = "document.getElementById('"+form+"')."+targetNm;	
		var obj = eval(target);
		if(obj.length){
			for (var i=obj.length-1; i >= 0 ; i--) {
				obj[i] = null;
			}
		}
	}
	
	/******************************************
	 * chkbox 채크된 row 삭제
	 ******************************************/
	function deleteRowByChkBox(checkBoxName){
		var target = "document.getElementsByName('"+checkBoxName+"')";	
		var chkObj = eval(target);
		//var chkobj = document.getElementsByName(checkBoxName);	
		var sourceTr;
		
		if(chkObj.length == 0){
			alert("삭제할 정보가 없습니다.");
			return false;
		}
		
		
		var moveOk = false;
		for(var i = chkObj.length; i > 0; i--){
			if( typeof(chkObj.length) == 'undefined'){
				if(chkObj.checked){
					sourceTr = chkObj.parentNode.parentNode;
					sourceTr.parentNode.deleteRow(sourceTr.sectionRowIndex);
					moveOk = true;
				}
			}else{
				if(chkObj[i-1].checked){
					sourceTr = chkObj[i-1].parentNode.parentNode;
					sourceTr.parentNode.deleteRow(sourceTr.sectionRowIndex);
					moveOk = true;
				}
			}
		}
		
		if(!moveOk){
			alert("삭제할  정보를 선택하여 주십시요.");
			return false;
		}
		
	}
	
	/******************************************
	 * chkbox 채크된 row 삭제
	 ******************************************/
	function deleteRowByChkBox2(checkBoxName){
		var target = "document.getElementsByName('"+checkBoxName+"')";	
		var chkObj = eval(target);
		//var chkobj = document.getElementsByName(checkBoxName);	
		var sourceTr;
		var sourceTr2;
		
		if(chkObj.length == 0){
			alert("삭제할 정보가 없습니다.");
			return false;
		}
		
		
		var moveOk = false;
		for(var i = chkObj.length; i > 0; i--){
			if( typeof(chkObj.length) == 'undefined'){
				if(chkObj.checked){
					sourceTr = chkObj.parentNode.parentNode;
					sourceTr2 = sourceTr.nextSibling;
					sourceTr.parentNode.deleteRow(sourceTr.sectionRowIndex);
					sourceTr2.parentNode.deleteRow(sourceTr2.sectionRowIndex); 
					moveOk = true;
				}
			}else{
				if(chkObj[i-1].checked){
					sourceTr = chkObj[i-1].parentNode.parentNode;
					sourceTr2 = sourceTr.nextSibling;
					sourceTr.parentNode.deleteRow(sourceTr.sectionRowIndex);
					sourceTr2.parentNode.deleteRow(sourceTr2.sectionRowIndex); 
					moveOk = true;
				}
			}
		}
		
		if(!moveOk){
			alert("삭제할  정보를 선택하여 주십시요.");
			return false;
		}
		
	}
	
	/******************************************
	 * file명 추출
	 ******************************************/
	function getFileNm(str) {
		var fileNm	= "";
		var startIdx = 0;
		var endIdx = 0;
		
		for(var i=str.length-1; i>-1; i--) { 
            if(str.charAt(i) == ".") {
            	endIdx = i;
            	break;
            }
        }
		
		for(var i=str.length-1; i>-1; i--) { 
            if(str.charAt(i) == "\\") {
            	startIdx = i;
            	break;
            }
        }
        
        if(endIdx == 0) {
        	endIdx = str.length;
        }
        
        for(var i=startIdx+1; i<endIdx; i++) {
        	fileNm += str.charAt(i);
        }
        
        return fileNm;
	}
	
	/******************************************
	 * checkbox 동기화
	 ******************************************/	
	function checkBoxSync(sObj, tObj){
		if(sObj){
		   if(sObj.length){
		   		for(var i = 0 ; sObj.length > i ; i ++ ) { 
		     		tObj[i].checked = sObj[i].checked; 
		    	}//for
		   }else{
		    	tObj.checked = sObj.checked;    
		   }//if
		}//if
		
	}
	
	/******************************************
	 * 금액처리
	 ******************************************/	
	function FormatDecimalAsNumberNoComma(c)
	{var max=new Number("100000000");if(c.value>max){c.value=max}var min=new Number("-100000000");if(c.value<min){c.value=min}c.value=DecimalToNumber(c.value,2).replace(',','');c.style.color=(c.value.match(/\x2D/)==null?c.getAttribute("positiveColor"):c.getAttribute("negativeColor"));}
	
	function FormatDecimalAsNumberComma(c)
	{var max=new Number("100000000");if(c.value>max){c.value=max}var min=new Number("-100000000");if(c.value<min){c.value=min}c.value=DecimalToNumber(c.value,2);c.style.color=(c.value.match(/\x2D/)==null?c.getAttribute("positiveColor"):c.getAttribute("negativeColor"));}
	
	function FormatDecimalAsNumberCommaP3(c)
	{
		var max=new Number("100000000");
		if(c.value>max){c.value=max}
		
		var min=new Number("-100000000");
		if(c.value<min){c.value=min}
		
		/*소수점 3자리이하 절사*/
		var valMath = Number(NumberToDecimal(c.value));
		var tmpVal = (String(valMath)).replace(',','');
		var tmpLen = (String(tmpVal)).length;
		var tmpBool = isNumber(tmpVal);
					
		if(tmpBool){
			if(tmpLen > 3){
				tmpVal = tmpVal.substr(0, tmpLen-3) + "000";
			}
		}else{
	        var num1 = tmpVal.split(".")[0];
	        var num2 = tmpVal.split(".")[1];
	        
	        num2 = num2.substr(0,3);
	        tmpVal = num1 + "." + num2;
		}
		
		/*
		if(valMath == 64.60) {
			valMath = 64600;
		} else if(valMath == 1.005) {
			valMath = 1005; 
		} else {
			valMath = Math.floor(valMath * 1000);
		}
		valMath = valMath / 1000
		c.value = valMath;
		*/
		c.value = tmpVal;
		c.value=DecimalToNumber(c.value,3);
		c.style.color=(c.value.match(/\x2D/)==null?c.getAttribute("positiveColor"):c.getAttribute("negativeColor"));
		
	}
	
	function FormatNumberAsDecimal(c)
	{c.value=NumberToDecimal(c.value);}
	
	function FormatNumberAsDecimal1000000(c)
	{c.value=Number(NumberToDecimal(c.value)) * 1000000;}
	
	function FormatNumberAsDecimal1000(c)
	{c.value=Number(NumberToDecimal(c.value)) * 1000;}
	
	function NumberToDecimal(n)
	{n=n.toString();n=n.replace(/[^\d\x2D\x2E]/g,'');return n;}
	
	function DecimalToNumber(n,p)
	{
		//원본
		n=n.toString();if(p==null){p=2;}var sy=new Array('-','');var neg=(n.match(/\x2D/)!=null?true:false);n=n.replace(/[^\d\x2E]/g,'');var m=n.match(/(\d*)(\x2E*)(\d*)/);var f=m[3];if(f.length>p){f=f/Math.pow(10,(f.length-p));f=Math.round(f);while(f.toString().length<p){f='0'+f};}else{while(f.toString().length<p){f+='0'};}var w=new Number(m[1]);if(f==Math.pow(10,p)){w+=1;f=f.toString().substr(1);}w=w.toString();var s=3;var l=w.length-s;while(l>0){w=w.substr(0,l)+'\x2C'+w.substr(l);l-=s;}if(p==0){m[2]='';f=''}else{m[2]='\x2E'}return (neg?sy[0]+w+m[2]+f+sy[1]:w+m[2]+f);
		
		//수정본
	}

	function FormatRemoveComma(c) {
	    var data = removeComma(c);
	    c.value = data;
	}
	
	function FormatNumberComma(c) {
	    if( c.value == null ) c.value = "";
	    var data = removeComma(c);
	    var sdata = "";
	    var dataLen = data.length;
	    var splitnum = dataLen % 3;
	    var returnVal = ""; 
	    for (i=0; i<dataLen; i++) {
	        sdata = data.substr(i,1);
	        returnVal = returnVal+sdata;1
	        if (((i+1)%3) == splitnum && i<( dataLen-1 )) {
	            returnVal = returnVal+",";
	        }
	    }
	    if( returnVal.indexOf("-,") == 0 ){
	        returnVal = "-" + returnVal.substring(2);
	    }
	    c.value = returnVal;
	}
	
	function FormatNumberComma1(c) {
	    if( c.value == null || c.value == "" ){
	    	returnVal = "0";
	    } else{
	    	var data = removeComma(c);
		    var sdata = "";
		    var dataLen = data.length;
		    var splitnum = dataLen % 3;
		    var returnVal = ""; 
		    for (i=0; i<dataLen; i++) {
		        sdata = data.substr(i,1);
		        returnVal = returnVal+sdata;1
		        if (((i+1)%3) == splitnum && i<( dataLen-1 )) {
		            returnVal = returnVal+",";
		        }
		    }
	    }
	    
	    c.value = returnVal;
	}	

	//이미지 swap 관련
	function MM_swapImgRestore() { //v3.0
	  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
	}
	 
	function MM_preloadImages() { //v3.0
	  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
	    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
	    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
	}
	 
	function MM_findObj(n, d) { //v4.01
	  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
	    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
	  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
	  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
	  if(!x && d.getElementById) x=d.getElementById(n); return x;
	}
	 
	function MM_swapImage() { //v3.0
	  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
	   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
	}
	
	/******************************************
	 * 년수계산
	 ******************************************/
	function yearCnt(startdt, enddt){
		var result = 0;
		if(startdt == "" || enddt == ""){
			return "";
		}
		
		var v1=startdt.split(".");
		var v2=enddt.split(".");
		
		var a1=new Date(v1[0],v1[1] -1 ,v1[2]).getTime();
		var a2=new Date(v2[0],v2[1] -1 ,v2[2]).getTime();
		result=(a2-a1)/(1000*60*60*24);
		result = result / 365;
		return result; 
	}
	
	
	/******************************************
	 * 년월 포맷 제거
	 ******************************************/
	function replaceYYMMForm(str){
		var result = "";
		
		result = str.replace(/\-/gi, "");
		return result; 
	}	
	
	/******************************************
	 * 쿠키 생성
	 ******************************************/
	function setCookie(name, value, expiredays) {
		var today = new Date();
    	today.setDate(today.getDate() + expiredays);

	    document.cookie = name + '=' + escape(value) + '; path=/; expires=' + today.toGMTString() + ';'
	}
		
	/******************************************
	 * 쿠키 가져오기
	 ******************************************/
	function getCookie(name) {
	    var nameOfCookie = name+"=";
	    var x=0;
	    while (x <= document.cookie.length) {
	        var y = (x+nameOfCookie.length);
	        if (document.cookie.substring(x, y) == nameOfCookie) {
	            if((endOfCookie=document.cookie.indexOf(";", y)) == -1) endOfCookie = document.cookie.length;
	            return unescape(document.cookie.substring(y, endOfCookie));
	        }
	        x = document.cookie.indexOf(" ", x) + 1;
	        if (x == 0) break;
	    }
	    return "";
	}
	
	
	/******************************************
	 * 프로그래스바 만들기
	 ******************************************/
	var imgObj = document.createElement("img");
	imgObj.setAttribute("id", "img_progress");
	imgObj.setAttribute("src", context + "/images/prog.gif");
	imgObj.border = 0;
	imgObj.width = "220";
	imgObj.height = "8";

	function show_progress() {
		if(!document.getElementById("div_progress")){
			var divObj = document.createElement("div");
			divObj.setAttribute("id", "div_progress");
			var width = 300;
			var height = 100;
			var text = "<font style='font-size:9pt;font-family:굴림;color:#25407B;'>작업 중 입니다. 잠시만 기다려 주십시요.</font><br><br>";
			divObj.innerHTML = text;
			divObj.appendChild(imgObj);
			divObj.style.position="absolute";
			divObj.style.backgroundColor="#FFFFFF";
			divObj.style.border="2px solid #c3c3c3";
			divObj.style.textAlign="center";
			divObj.style.paddingTop="30px";
			divObj.style.zIndex="10000";
			divObj.style.width=width;
			divObj.style.height=height;
			//divObj.style.left=(document.body.scrollWidth - width) / 2;
			divObj.style.left="50%";
			//divObj.style.top=(document.body.scrollHeight - height) / 2;
			divObj.style.top="50%";
			divObj.style.marginLeft="-150px";
			divObj.style.marginTop="-50px";
			document.body.appendChild(divObj);
		}
	}

	function show_progress_op() {
		if(!document.getElementById("div_progress")){
			var divObj = document.createElement("div");
			divObj.setAttribute("id", "div_progress");
			var width = 300;
			var height = 100;
			var text = "<font style='font-size:9pt;font-family:굴림;color:#25407B;'>작업 중 입니다. 잠시만 기다려 주십시요.</font><br><br>";
			divObj.innerHTML = text;
			divObj.appendChild(imgObj);
			divObj.style.position="absolute";
			divObj.style.backgroundColor="#FFFFFF";
			divObj.style.border="2px solid #c3c3c3";
			divObj.style.textAlign="center";
			divObj.style.paddingTop="30px";
			divObj.style.zIndex="10000";
			divObj.style.width=width;
			divObj.style.height=height;
			//divObj.style.left=(document.body.scrollWidth - width) / 2;
			divObj.style.left="50%";
			//divObj.style.top=(document.body.scrollHeight - height) / 2;
			divObj.style.top="50%";
			divObj.style.marginLeft="-150px"; 
			divObj.style.marginTop="-50px";      
			document.getElementById("divProgress").appendChild(divObj);
		}
	}

	function show_progress_pop() {
		
		if(!document.getElementById("div_progress")){
			var divObj = document.createElement("div");
			divObj.setAttribute("id", "div_progress");
			var width = 300;
			var height = 100;
			
			var text = "<font style='font-size:9pt;font-family:굴림;color:#25407B;'>작업 중 입니다. 잠시만 기다려 주십시요.</font><br><br>";
			divObj.innerHTML = text;
			divObj.appendChild(imgObj);
			divObj.style.position="absolute";
			divObj.style.backgroundColor="#FFFFFF";
			divObj.style.border="2px solid #c3c3c3";
			divObj.style.textAlign="center";
			divObj.style.paddingTop="30px";
			divObj.style.zIndex="10"; 
			divObj.style.width=width;
			divObj.style.height=height;
			divObj.style.left=(document.body.scrollWidth - width) / 2;
			divObj.style.top=(document.body.scrollHeight - height) / 2;
			document.body.appendChild(divObj);
		}
		
		
	}

	function remove_progress() {
		try{
			var divObj = document.getElementById("div_progress");
			var imgObj = document.getElementById("img_progress");
			divObj.removeChild(imgObj);
			document.body.removeChild(divObj);
			
		}catch(error){
			
		}
	}

	function remove_progress_op() {
		try{
		    if (document.getElementById("div_progress")) {
				var divObj = document.getElementById("div_progress");
				var imgObj = document.getElementById("img_progress");
				var upObj = document.getElementById("divProgress");
				divObj.removeChild(imgObj);
				upObj.removeChild(divObj);
			}
			
		}catch(error){
			
		}
	}

function setCheckComma( chkVal , intLen , commLen , msg)
{
    var comma=0 ;
    var commaNum;   // 컴마 위치
    var ch;
    var i=1;     // 컴마 갯수
    if(chkVal.value == '')
    {
    	return true;
    }
    for(var x = 0; x < chkVal.value.length ; x++)
    {
           ch= chkVal.value.substring(x,x+1);
           if( ch == ".")
           {
                 commaNum=x+1;
                 comma=i;
                i++;
           }
     }

    if(comma==0)	/*"."가 없을 경우*/
    {
        if(chkVal.value.length > Number(intLen - commLen ))
        {
            alert( "["+msg +"]의 정수는 "+ Number(intLen -commLen ) +"자리까지만 입력하세요.");
            return false;
        }
    }else if(comma==1)
    {
        var commaMaxLength=chkVal.value.length;
        var commaStirng=chkVal.value.substring(commaNum,commaMaxLength);
        var commalength=commaStirng.length;
        var intStirng=chkVal.value.substring(0,commaNum-1);
        var intlength=intStirng.length;

        // 소수점만 입력한경우
        if( commalength == 0 ){
            alert( "["+msg +"]을(를) 확인하십시요.");
            return false;
        }

        if( commalength > commLen )
        {
            alert( "["+msg +"]의 소숫점은 "+ commLen +"자리 이하만 입력하세요");
            return false;
        }
        
        if(intlength > Number(intLen - commLen ))
        {
            alert( "["+msg +"]의 정수는 "+ Number(intLen -commLen ) +"자리까지만 입력하세요.");
            return false;
        }
    }else if(comma > 1)  {
        
        alert( "["+msg +"]을(를) 확인하십시요.");
        return false;

    }
    return true;

}

/**
 * 파일확장자 추출
 * @param str
 * @return
 */
function getFileExtension(str){
	var fileNm	= "";
	var startIdx = 0;
	var endIdx = 0;
	
	var extName = "";
	
	if(str != null && str != "") {
		var filePath = str.split("\\");
		
		fileNm = filePath[filePath.length - 1];
		for(var i=fileNm.length-1; i>-1; i--) { 
		    if(fileNm.charAt(i) == ".") {
		    	endIdx = i;
		    	break;
		    }
		}
		
		if(endIdx == 0){
			extName = "";
		}else{
			extName = fileNm.substr(endIdx + 1);
		}
	}
	
	return extName;
}

/******************************************
 * 개월수계산
 ******************************************/
function monthCnt(startdt, enddt){
	var result = 0;
	if(startdt == "" || enddt == ""){
		return "";
	}
	
	var v1=startdt.split(".");
	var v2=enddt.split(".");

	var strtYear = parseInt(v1[0]); 
	var strtMonth = parseInt(v1[1]); 

	var endYear = parseInt(v2[0]); 
	var endMonth = parseInt(v2[1]); 
	
	result = (endYear - strtYear)* 12 + (endMonth - strtMonth + 1); 
	
	return result; 
}

/******************************************
 * 개월수계산
 ******************************************/
function monthCntSet(startdt, enddt){
	var result = 0;
	if(startdt == "" || enddt == ""){
		return "";
	}
	
	var v1=startdt.split(".");
	var v2=enddt.split(".");
	
	var date1 = new Date(v1[0] , v1[1]-1 , v1[2]);
	var date2 = new Date(v2[0] , v2[1]-1 , v2[2]);
	
	var interval = date2 - date1;
	var day   = 1000*60*60*24;
	var month = day*30;
	
	result = parseInt(interval/month); 
	
	return result; 
}

	/******************************************
	 * 날짜 포맷 세팅
	 ******************************************/
	function setFormatDate(str){
	    var returnStr = "";
	    if (str == null || str == "") {
	    	return returnStr;
	    }
	    
	    str = str.replace(/\./gi,"");
	    returnStr = str.substring(0, 4) + "." + str.substring(4, 6) + "." + str.substring(6, 8);
	    
	    return returnStr;    
	}
	
	/******************************************
	 * 등록자번호 포맷 세팅
	 ******************************************/
	function setFormatRegistorSsn(str, type){
	    var returnStr = "";
	    if (str == null || str == "") {
	    	return returnStr;
	    }
	    
	    str = str.replace(/-/gi,"");
	    if (type == "01") {
	    	returnStr = str.substring(0, 2) + "-" + str.substring(2, 6) + "-" + str.substring(6);
	    } else { 
	    	returnStr = str.substring(0, 2) + "-" + str.substring(2, 9) + "-" + str.substring(9);
	    }
	    	    
	    return returnStr;    
	}
	
	/******************************************
	 * 프로그램등록번호 포맷 세팅
	 ******************************************/
	function setFormatProgCode(str){
	    var returnStr = "";
	    if (str == null || str == "") {
	    	return returnStr;
	    }
	    
	    str = str.replace(/-/gi,"");
	    returnStr = str.substring(0, 4) + "-" + str.substring(4, 6) + "-" + str.substring(6, 9) + "-" + str.substring(9);
	    
	    return returnStr;    
	}
	
	/******************************************
	 * 소수점 반올림
	 ******************************************/
	function calRoundSosu(val, len){
	    if (val == "" || val == 0) {
	        return "";
	    }
	    
	    var result;
	    var strNum = String(val);
	    var i = strNum.indexOf(".");
	    if (isNaN(val)) return 0;
	    if (!isFinite(val)) return 0;
	    if(i == -1){
	        return Math.round(val);
	    } else {
	        var num1 = strNum.split(".")[0];
	        var num2 = strNum.split(".")[1];
	        var num3;

	        if(len > num2.length) {
	            return strNum;
	        } else {
	            var tmpNum;
	            var max = 1;
	            if(num2.substring(0,1) == "0") {
	            	tmpNum = String("0" + Math.round(num2.substring(0,2) + "." + num2.substring(len)));
	            } else {
	            	tmpNum = String(Math.round(num2.substring(0,2) + "." + num2.substring(len)));
	            }
	            for(var i=0; i<len; i++){
	                max = max * 10; 
	            } 
	            if((tmpNum.substring(0,len)).length == len-1){
	                num3 = (tmpNum.substring(0,len)) + "1";
	            } else if((tmpNum.substring(0,len)).length == len){
	            	if(num2.substring(0,1) == "0") {
	            		num3 = Number((tmpNum.substring(0,len))) + Number(1);
	            		num3 = "0" + num3; 
	            	} else {
	                	num3 = Number((tmpNum.substring(0,len)));
	                }
	            }
	            
	            if(tmpNum == max){
	                num1 = Number(num1) + Number(1);
	                result = num1;
	            } else {
	                if(num3 == 100){
		                num1 = Number(num1) + Number(1);
		                result = num1;
	                } else {
	                	result = parseFloat(num1 + "." + num3);
	                }
	            } 
	            return result;
	        }
	    }
	}
	
	/******************************************
	 * grid column의 합계 계산
	 ******************************************/
	function calculateSumGridColumn(grid, column_id) {
        var totalAmount = 0;
        var columnIndex = getColumnIndexByName(grid, column_id);
		var rows = grid[0].rows;
		var rowsCount = rows.length;
		var row = "";
		var ob = [];
    
        for(var i = 0; i < rowsCount; i++) {
            row = rows[i];
            if (row.className.indexOf("jqgrow") != -1) {
                totalAmount += Number(getTextFromCell(row.cells[columnIndex]));
            }
        }
        totalAmount = Math.round(totalAmount*1000)/1000;
		ob[column_id] = totalAmount;
        grid.jqGrid("footerData", "set", ob);
    }
	
	/******************************************
	 * grid column의 합계 계산
	 ******************************************/
	function calculateSumGridColumn2(grid, column_id, column_id2) {
        var totalAmount = 0;
        var columnIndex = getColumnIndexByName(grid, column_id2);
		var rows = grid[0].rows;
		var rowsCount = rows.length;
		var row = "";
		var ob = [];
    
        for(var i = 0; i < rowsCount; i++) {
            row = rows[i];
            if (row.className.indexOf("jqgrow") != -1) {
                totalAmount += Number(getTextFromCell(row.cells[columnIndex]));
            }
        }
        totalAmount = Math.round(totalAmount*1000)/1000;
		ob[column_id] = totalAmount;
        grid.jqGrid("footerData", "set", ob);
    }
	
	/******************************************
	 * grid에서 해당컬럼이 몇 번째 컬럼인지 카운트
	 ******************************************/
	function getColumnIndexByName(grid, column_id) {
        var cm = grid.jqGrid('getGridParam','colModel');
		var len = cm.length;
		
        for (var i = 0; i < len; i++) {
            if (cm[i].name == column_id) {
                return i;
            }
        }
		
        return -1;
    }
	
	/******************************************
	 * grid cell 안에 있는 값 조회
	 ******************************************/
	function getTextFromCell(cellNode) {
		if(cellNode.textContent) {
			return cellNode.textContent;
		} else {
        	return cellNode.innerText;
		}
    }
	
	/******************************************
	 * RGB(255, 255, 255)코드를 HEX 코드로 변환시킴
	 ******************************************/
	function rgb2hex(rgb){
	 rgb = rgb.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);
	 return "#" +
	  ("0" + parseInt(rgb[1],10).toString(16)).slice(-2) +
	  ("0" + parseInt(rgb[2],10).toString(16)).slice(-2) +
	  ("0" + parseInt(rgb[3],10).toString(16)).slice(-2);
	}
