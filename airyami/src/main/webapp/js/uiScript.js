
/**
	* sub UI behavior script
	* @author do yeon-su
**/

//<![CDATA[

$(document).ready(function($) {
	

	//검색페이지 input포커스 가면 검색팝업 띄우기
	$(".ttlSrch").bind('click keyup', function(){
		$('.popSerchArea').removeClass('hide');
	});
	
	//검색팝업닫기
	$(".btnClosePop2").bind('click keyup', function(){
		$('.popSerchArea').addClass('hide');
	});

	//음성검색버튼 클릭시 음성검색팝업 띄우기
	$(".voiceLink").bind('click keyup', function(){
		$('.popVoiceSearchArea').removeClass('hide');
	});

	//음성검색팝업닫기
	$(".btnClosePop3").bind('click keyup', function(){
		$('.popVoiceSearchArea').addClass('hide');
	});

	//검색팝업 최신검색어, 인기검색어 선택
	$(".srchListChoice>button").click(function(){
		$(this).addClass("on");
		$(this).next().removeClass("on");
		$(this).prev().removeClass("on");

		$(".srchListChoice>button").children('span').text( '선택하기' );

		 if($('.srchListChoice>button').hasClass('on')){
			$(this).children('span').text( '선택됨' );
		}else{
			$(this).children('span').text( '선택하기' );
		}
	});

	//콜정보리스트 하위내용 열거나 닫기
	$('.btnSubDetail').bind('click keyup', function(){
		$(this).toggleClass("on");
		$(this).parent().next().slideToggle();

		 if($(this).hasClass('on')){
			$(this).attr('title', '콜센터 정보 닫기' );
		}else{
			$(this).attr('title', '콜센터 정보 열기' );
		}

	});

	//전화기버튼 클릭시 전화연결팝업 열기
	$(".btnSubCallLink").bind('click keyup', function(){
		$('.popCallLinkArea').removeClass('hide');
	});

	//전화연결팝업닫기
	$(".btnGrey").bind('click keyup', function(){
		$('.popCallLinkArea').addClass('hide');
	});

	//공유하기 버튼 클릭시 공유팝업 뎔기
	$(".btnShare").bind('click keyup', function(){
		$('.popShareArea').removeClass('hide');
	});

	//공유하기팝업닫기
	$(".btnClosePop").bind('click keyup', function(){
		$('.popShareArea').addClass('hide');
	});

	//즐겨찾기선택및 해제시키기
	$(".btnSubFunLink").bind('click keyup', function(){
		$(this).toggleClass('on');

		 if($(this).hasClass('on')){
			 $(this).children('span').text( '즐겨찾기 해제하기' );
			$(".popFunkLinkArea").children('p').text( '즐겨찾기가 완료되었습니다.' );
		}else{
			$(this).children('span').text( '즐겨찾기 선택하기' );
			$(".popFunkLinkArea").children('p').text( '즐겨찾기가 해제되었습니다.' );
		}

		$('.popFunkLinkArea').show(100);

		 function move(){
			$(".popFunkLinkArea").hide(100);
		 }
		setTimeout(move, 4000);
	});

	/* 폰트크기 설정 */
	$(".fontSizeSelect").change(function(){
		var dd = $(".fontSizeSelect option:selected").val();
		$("html, body").css({'font-size':dd});
	}); 

	//분류설정 체크
	$("#typeSetting").bind('click keyup', function(){
		$(this).toggleClass('on');
		 if($(this).hasClass('on')){
			$(this).parent().next().find('input').removeAttr('disabled');
		}else{
			$(this).parent().next().find('input').removeAttr('checked');
			$(this).parent().next().find('input').attr("disabled","disabled");
		}
	});

	//메인설정 체크
	$("#mainSetting").bind('click keyup', function(){
		$(this).toggleClass('on');
		 if($(this).hasClass('on')){
			$(this).parent().next().find('input').removeAttr('disabled');
			$('#b1').attr("checked","checked");
		}else{
			$(this).parent().next().find('input').removeAttr('checked');
			$(this).parent().next().find('input').attr("disabled","disabled");
		}
	});


});
//]]>