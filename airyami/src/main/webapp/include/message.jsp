<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>


<script type="text/javascript">
function gfn_getMsg(id, params){
	var msg = "";
	switch(id){
		// 해당 데이터가 없습니다.
		case "common.nodata.msg" : msg = "<spring:message code='common.nodata.msg' javaScriptEscape='true'/>";
			break;
		// {0}은(는) 날짜 유형이 아닙니다.
		case "errors.date" : msg = "<spring:message code='errors.date' javaScriptEscape='true'/>";
			break;
		// 에러가 발생했습니다!
		case "fail.common.msg" : msg = "<spring:message code='fail.common.msg' javaScriptEscape='true'/>"; 
			break;
		// (은)는 필수입력항목입니다.
		case "common.required.msg" : msg = "<spring:message code='common.required.msg' javaScriptEscape='true'/>"; 
			break;
		// {0}은(는) 영문, 숫자를 입력하셔야 합니다.
		case "errors.english_numeric" : msg = "<spring:message code='errors.english_numeric' javaScriptEscape='true'/>"; 
			break;
		// 유효하지 않은 이메일 주소입니다.
		case "errors.email" : msg = "<spring:message code='errors.email' javaScriptEscape='true'/>"; 
			break;
		// 조회에 실패하였습니다.
		case "fail.common.select" : msg = "<spring:message code='fail.common.select' javaScriptEscape='true'/>"; 
			break;
		// 코드
		case "word.code" : msg = "<spring:message code='word.code'/>";
			break;
	}
	
	// 존재하지 않는 메세지 코드면 코드값을 리턴시켜주자.
	if(gfn_isNull(msg))
		return id;
	
	// 파라미터가 있으면 내용을 변경해서 넘겨준다.
	if(!(typeof params == 'undefined')){
		var paramsArr = params.split(",");
		for(var i = 0 ; i < paramsArr.length; i++){
			msg = msg.replace("{"+i+"}", paramsArr[i]);
		}
	}
	
	return msg;
}
</script>