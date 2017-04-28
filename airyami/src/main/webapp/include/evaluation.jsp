<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<script type="text/javascript">
function gfn_setEvaluation(ds_detail, keyInfoArr, chargeInfo){
	
	if(!gfn_isNull(chargeInfo)){
		$('#ulCharge').show();
		gfn_setDetails(chargeInfo, $("#ulCharge"));	// 지역내 상세 내용 셋업
	}
	
	if(!gfn_isNull(keyInfoArr)){
		var keyhtml = "";
		for(_keyInfoArr in keyInfoArr){
			keyhtml += "<input type='hidden' name='"+keyInfoArr[_keyInfoArr]+"' value='"+eval( "ds_detail." + keyInfoArr[_keyInfoArr])+"'>";
		}
		$('#satisfaction').append(keyhtml);
	}
	
}

function fn_saveEvaluation(){
	var score = "0";
	var inputParam = new Object();
	inputParam.sid 				= "insertEvaluation";
	inputParam.url 				= "/CMM/insertEvaluation.do";
	inputParam.data 			= gfn_makeInputData($("#satisfaction"));
	inputParam.callback			= gfn_callBackEvaluation;
	
	gfn_Transaction( inputParam );
}


function gfn_callBackEvaluation(sid, result, data){
	$('#btnEvaluation').hide();
	gfn_makeObjDisable($("#satisfaction"));
	
	try{
		// 각자 화면에서 해야할 일이 있으면 호출해준다.
		fn_callBackEvaluation(sid, result, data);
	}catch(e){}
}

</script>
	<!-- 만족도 평가하기 -->
	<div id="satisfaction">
		<!-- 담당자  -->
		<ul id="ulCharge" class="charge" style="display:none">
			<li>담당자 : <span id="CHARGE_NAME"></li>
			<li>문의사항 : <span id="CHARGE_PHONE"></li>
			<li>이메일 : <span id="CHARGE_EMAIL"></li>
		</ul>
		<!--// 담당자  -->
		<!-- 만족도 -->
		<div class="evaluation">
		<span>내용에 만족하시나요?</span>
			<input type="radio" name="EVAL_GRADE" id="evaluation01" value="1" /><label for="evaluation01">1점</label>
			<input type="radio" name="EVAL_GRADE" id="evaluation02" value="2" /><label for="evaluation02">2점</label>
			<input type="radio" name="EVAL_GRADE" id="evaluation03" value="3" checked/><label for="evaluation03">3점</label>
			<input type="radio" name="EVAL_GRADE" id="evaluation04" value="4" /><label for="evaluation04">4점</label>
			<input type="radio" name="EVAL_GRADE" id="evaluation05" value="5" /><label for="evaluation05">5점</label>
			<input type="button" id="btnEvaluation" onclick="javascript:fn_saveEvaluation();" value="점수주기" />
		</div>
		<!--// 만족도 -->
	</div>
	<!--// 만족도 평가하기 -->