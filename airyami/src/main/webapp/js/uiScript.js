
/**
 * UI behavior script
 */

//<![CDATA[
//$(document).ready(function(){
var customui = {
			"uicommunity": function() {

//ie 7,8
	if(!Modernizr.mq('only all')) {
		$("html").addClass("no-mq");
	} else {
		$("html").addClass("mq");
	}

	$(".questionArea").find("a").click(function(){
		$(this).parent().parent().parent().children(".answerArea").slideToggle();
		$(this).parent().parent().toggleClass("on");
	});//FAQ 열고닫기

	$(".closeNoticeAll").click(function(){
		$(".noticeAll").slideUp();
	});//전체공지 닫기
	
	ww = $(window).width();

		if(ww <=1024){
				$(".aboutcafe").css({"display":"block"});
				$("#lnb").find("nav").css({"display":"none"});
				//$(".btn_allcategory").html('All Category <span></span>');
				$("#lnb").find("span").removeClass("ov") ;
			 }else{
				 $(".aboutcafe").css({"display":"block"});
				$("#lnb").find("nav").css({"display":"block"});
				
				var lnbHeight = $("#lnb").height();
				$("#contents").css({'min-height':lnbHeight});
				//$(".btn_allcategory").text('고객센타');
			 } //로드시 서브메뉴 형태설정

		$(".btn_allcategory").click(function(){
			 if(ww <=1024){
				$(".aboutcafe").slideToggle();
				$("#lnb").find("nav").slideToggle();
				$("#lnb").find("span").toggleClass("ov") ;
			 }else{
				$(".aboutcafe").css({"display":"block"});
				$("#lnb").find("nav").css({"display":"block"});
				$("#lnb").find("span").removeClass("ov") ;
			 }
		});//서브메뉴 이벤트 설정


	jQuery(function($){ 
		var tree = $('.tree'); 
		var togglePlus = '<button type="button" class="toggle plus">+</button>';
		var toggleMinus = '<button type="button" class="toggle minus">-</button>';

		// defalt
		tree.find('li>ul').css('display','none');
		tree.find('ul>li:last-child').addClass('last');
		tree.find('li>ul:hidden').parent('li').prepend(togglePlus);
		tree.find('li>ul:visible').parent('li').prepend(toggleMinus);

		// active
		tree.find('li.active').parents('li').addClass('open');
		tree.find('li.open').parents('li').addClass('open');
		tree.find('li.open>.toggle').text('-').removeClass('plus').addClass('minus');
		tree.find('li.open>ul').slideDown(100);

		// click toggle 
		$('.tree .toggle').click(function(){
			t = $(this);
			t.parent('li').toggleClass('open');
			if(t.parent('li').hasClass('open')){
				t.text('-').removeClass('plus').addClass('minus');
				t.parent('li').find('>ul').slideDown(100);
			} else {
				t.text('+').removeClass('minus').addClass('plus');
				t.parent('li').find('>ul').slideUp(100);
			}
		});
	}); // left 트리메뉴

	 $('.urlButton').click(function() { 
		 url = $('.urlSelect').val(); 
		 
		 window.open(url); 
	}); 
	
	 
	 $('#btnAccectBreak').click(function() { 
		 $(".pop_coercion").removeClass('hide');
	 });
	 
	 $(".findPostIn>h4").find("a").click(function(){
		 $(".postList").empty();
		 $(".findPostIn>h4").removeClass("on");	
		 $(this).parent().addClass("on");
		 $(".findPostIn>h4").next().addClass("hide");
		 	$(this).parent().next().removeClass("hide");
		 	$(".postSearch").find("input[type=text]").attr("value", "");
			return false;
		});//우편번호 도로명 지번검색선택
	 
	 $(".btnOpenPopup").click(function(){
		 $(".findPostBox").removeClass("hide");
	 });//우편번호 팝업창 열기
	 
	 $(".btnClosePopup").click(function(){
		 $(".postList").empty();
		 $(".findPostBox").addClass("hide");
	 });//우편번호 팝업창 닫기
	 
}};
//});

if(!Modernizr.touch){

	$(window).on("resize", function(){
		//location.reload();
		ww = $(window).width();
	
		if(ww <=1024){
				$(".aboutcafe").css({"display":"block"});
				$("#lnb").find("nav").css({"display":"none"});
				//$(".btn_allcategory").html('All Category <span></span>');
				$("#lnb").find("span").removeClass("ov") ;
				
				$("#contents").css({'min-height':'200px'});
				
			 }else{
				$(".aboutcafe").css({"display":"block"});
				$("#lnb").find("nav").css({"display":"block"});
				//$(".btn_allcategory").text('고객센타');
				
				var lnbHeight = $("#lnb").height();	
				$("#contents").css({'min-height':lnbHeight});
			 }
	
	});// 사이즈 변경시 서브메뉴 이벤트

}
//]]>
var tree ={
	"treeLeftMenu" : function() {
		var tree = $('.tree'); 
		var togglePlus = '<button type="button" class="toggle plus">+</button>';
		var toggleMinus = '<button type="button" class="toggle minus">-</button>';
	
		// defalt
		tree.find('li>ul').css('display','none');
		tree.find('ul>li:last-child').addClass('last');
		tree.find('li>ul:hidden').parent('li').prepend(togglePlus);
		tree.find('li>ul:visible').parent('li').prepend(toggleMinus);
	
		// active
		tree.find('li.active').parents('li').addClass('open');
		tree.find('li.open').parents('li').addClass('open');
		tree.find('li.open>.toggle').text('-').removeClass('plus').addClass('minus');
		tree.find('li.open>ul').slideDown(100);
	
		// click toggle 
		$('.tree .toggle').click(function(){
			t = $(this);
			t.parent('li').toggleClass('open');
			if(t.parent('li').hasClass('open')){
				t.text('-').removeClass('plus').addClass('minus');
				t.parent('li').find('>ul').slideDown(100);
			} else {
				t.text('+').removeClass('minus').addClass('plus');
				t.parent('li').find('>ul').slideUp(100);
			}
		});			
	}
};

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
				/*if( $('option:selected', this).val() != ''  ) 
				*/	
					title = $('option:selected',this).text();
				$(this)
					.css({'z-index':10,'opacity':0,'-khtml-appearance':'none'})
					.after('<span class="select" style="width:' + w2 + 'px; min-width:' + w2 + 'px;padding-right:25px;">' + title + '</span>')
					.change(function(){
						val = $('option:selected',this).text();
						$(this).next().text(val);
					});
			});
		};//end if
	} //customSelectBox
		
};//end ui

$(function() {
		ui.customSelectBox(); //script for styling selectbox
});