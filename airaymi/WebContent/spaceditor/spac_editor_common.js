/*
* Spac Editor v13.1.1
* 공식배포처 : http://www.webeditor.kr
* 제자자 이메일 : master@phpwork.kr
* 이프로그램은 개인/기업/영리/비영리 에 관계없이 웹개발에 무료로 자유롭게 사용할 수 있습니다.
* 제작자가 배포한상태 그대로인 경우에 한해 재배포를 허용하며 수정된 소스나, 수정된 소스가 포함된 프로그램, 소스중의 일부분을 타인에게 배포하거나 판매할 수 없습니다.
* 제작자와 별도의 상의없이 본프로그램의 버전및 공식사이트 정보를 보여주는 "Spac Editor는?"(제일 오른쪽에 "?" 형태의 아이콘으로 출력됨) 버튼을 임의로 안보이도록 할 없습니다.(모든 툴바를 감추는 경우는 자동으로 감추어지므로 관계없습니다.)
* 이프로그램의 사용으로 인해 발생한 어떠한 문제도 제작자는 책임지지 않습니다.
* 사용상의 문제나 건의사항은 공식 배포사이트의 게시판을 이용해주시거나 메일로 보내 주시기 바랍니다.
*/
var base_dir = "/kgsbsc/spaceditor";	//에디터 설치 경로

function isIE() {
	var returnVal = false;
	var IE_VER_ARR = Array("MSIE 6","MSIE 7","MSIE 8","MSIE 9","MSIE 10");
	for(var i = 0; i < IE_VER_ARR.length; i++) {
		if( navigator.appVersion.indexOf(IE_VER_ARR[i]) > -1) {
			returnVal = true;
			break;
		}
	}
	return returnVal;
}
function isFF() {
	var browser = navigator.userAgent.toLowerCase();
	var Exp = /firefox/i;
	if ( Exp.test( browser ) ) {
		return true;
	} else {
		return false;
	}
}
function isOpera() {
	var browser = navigator.userAgent.toLowerCase();
	var Exp = /opera/i;
	if ( Exp.test( browser ) ) {
		return true;
	} else {
		return false;
	}
}
function getScrollSize(where) {
	if(where == 'top') {
		if(isIE() || isFF() || isOpera()) {
			return document.documentElement.scrollTop;
		} else {
			return document.body.scrollTop;
		}
	}
	if(where == 'left') {
		if(isIE() || isFF() || isOpera()) {
			return document.documentElement.scrollLeft;
		} else {
			return document.body.scrollLeft;
		}
	}
	if(where == 'width') {
		if(isIE() || isFF() || isOpera()) {
			return document.documentElement.scrollWidth;
		} else {
			return document.body.scrollWidth;
		}
	}
	if(where == 'height') {
		if(isIE() || isFF() || isOpera()) {
			return document.documentElement.scrollHeight;
		} else {
			return document.body.scrollHeight;
		}
	}
}
function getScrollSizeP(where) {
	if(where == 'top') {
		if(isIE() || isFF() || isOpera()) {
			return parent.document.documentElement.scrollTop;
		} else {
			return parent.document.body.scrollTop;
		}
	}
	if(where == 'left') {
		if(isIE() || isFF() || isOpera()) {
			return parent.document.documentElement.scrollLeft;
		} else {
			return parent.document.body.scrollLeft;
		}
	}
	if(where == 'width') {
		if(isIE() || isFF() || isOpera()) {
			return parent.document.documentElement.scrollWidth;
		} else {
			return parent.document.body.scrollWidth;
		}
	}
	if(where == 'height') {
		if(isIE() || isFF() || isOpera()) {
			return parent.document.documentElement.scrollHeight;
		} else {
			return parent.document.body.scrollHeight;
		}
	}
}
window.onresize = function() {
	var oDiv = document.getElementById('theScreenLayer');
	if(oDiv) {
		if(getScrollSize('top')>0) {
			oDiv.style.height = document.body.offsetHeight+16+"px";
		} else {
			oDiv.style.height = "100%";
		}
		if(getScrollSize('left')>0) {
			oDiv.style.width = document.body.offsetWidth+getScrollSize('left')+16+"px";
		} else {
			oDiv.style.width = "100%";
		}
	}
	var oDiv2 = parent.document.getElementById('theScreenLayer2');
	if(oDiv2) {
		if(getScrollSizeP('top')>0) {
			oDiv2.style.height = parent.document.body.offsetHeight+getScrollSizeP('top')+16+"px";
		} else {
			oDiv2.style.height = "100%";
		}
		if(getScrollSizeP('left')>0) {
			oDiv2.style.width = parent.document.body.offsetWidth+getScrollSizeP('left')+16+"px";
		} else {
			oDiv2.style.width = "100%";
		}
	}
}
window.onscroll = function() {
	var oDiv = document.getElementById('theScreenLayer');
	if(oDiv) {
		if(getScrollSize('top')>0) {
			oDiv.style.height = document.body.offsetHeight+16+"px";
		} else {
			oDiv.style.height = "100%";
		}
		if(getScrollSize('left')>0) {
			oDiv.style.width = document.body.offsetWidth+getScrollSize('left')+16+"px";
		} else {
			oDiv.style.width = "100%";
		}
	}
	var oDiv2 = parent.document.getElementById('theScreenLayer2');
	if(oDiv2) {
		if(getScrollSizeP('top')>0) {
			oDiv2.style.height = parent.document.body.offsetHeight+getScrollSizeP('top')+16+"px";
		} else {
			oDiv2.style.height = "100%";
		}
		if(getScrollSizeP('left')>0) {
			oDiv2.style.width = parent.document.body.offsetWidth+getScrollSizeP('left')+16+"px";
		} else {
			oDiv2.style.width = "100%";
		}
	}
}
function viewBlindScreen(html,event,left) {
	var oDiv		= document.createElement("div");
	oDiv.id			= 'theScreenLayer';
	oDiv.className	= "blindScreen";
	oDiv.innerHTML	= "<div id='theScreenLayerCover' class='blindScreenCover' onClick='hideBlindScreen()'></div>";
	document.body.appendChild(oDiv);
	if(getScrollSize('top')>0) {
		oDiv.style.height = document.body.offsetHeight+16+"px";
	} else {
		oDiv.style.height = "100%";
	}
	if(getScrollSize('left')>0) {
		oDiv.style.width = document.body.offsetWidth+getScrollSize('left')+16+"px";
	} else {
		oDiv.style.width = "100%";
	}
	var htmlDiv		= document.createElement("div");
	htmlDiv.id		= "theContentsLayer";
	htmlDiv.className = 'blindScreen2';
	if(left) {
		htmlDiv.style.top = (getScrollSize('top') + event.clientY - 10)+"px";
		htmlDiv.style.left = left+"px";
	} else {
		htmlDiv.style.top = (getScrollSize('top') + event.clientY - 10)+"px";
		htmlDiv.style.left = (getScrollSize('left') + event.clientX - 10)+"px";
	}
	htmlDiv.innerHTML = html;
	oDiv.appendChild(htmlDiv);
	document.oncontextmenu = function (e) {
		return false;
	}
}
function hideBlindScreen() {
	var oDiv = document.getElementById('theScreenLayer');
	if(oDiv) {
		document.body.removeChild(oDiv);
		document.oncontextmenu = function (e) {
			return true;
		}
		editor.document.oncontextmenu = function (e) {
			return true;
		}
	} else {
		hideContextMenu();
	}
}
function viewBlindScreenTarget(target_id) {
	var obj			= document.getElementById(target_id);
	var oDiv		= document.createElement("div");
	oDiv.id			= 'theScreenLayer';
	oDiv.className	= "blindScreenTarget";
	obj.appendChild(oDiv);
}
function viewBlindScreenFullHtml(html) {
	var oDiv		= document.createElement("div");
	oDiv.id			= 'theScreenLayer';
	oDiv.className	= "blindScreen";
	oDiv.innerHTML	= "<div id='theScreenLayerCover' class='blindScreenCover' onClick='hideBlindScreen()'></div>";
	document.body.appendChild(oDiv);
	var htmlDiv		= document.createElement("div");
	htmlDiv.id		= "theContentsLayer";
	htmlDiv.className = 'blindScreen2';
	htmlDiv.style.left = 0;
	htmlDiv.style.top = 0;
	htmlDiv.innerHTML = html;
	oDiv.appendChild(htmlDiv);
	document.oncontextmenu = function (e) {
		return false;
	}
}
function viewContextMenu(menu_array,e) {//menu_array의 각 배열값 ("아이콘|실행함수|보여질메뉴명")
	document.oncontextmenu = function (e) {
	 return false;
	}
	var btn = event.button;
	if(btn == 2) {
		var menu_html		= '';
		var menu_unit_array = '';
		for(var i = 0; i < menu_array.length; i++) {
			menu_unit_array		= menu_array[i].split("|");
			menu_html += "<div class='context_menu_unit' onMouseOver=\"this.className='context_menu_unit_up'\" onMouseOut=\"this.className='context_menu_unit'\" title='"+menu_unit_array[2]+"' onClick=\""+menu_unit_array[1]+"\">"+menu_unit_array[0]+" "+menu_unit_array[2]+"</div>";
		}
		var html  = "<div class='context_menu_box'>";
			html += menu_html;
			html += "</div>";
		viewBlindScreen(html,e);
		return;
	}
}
function viewBlindScreenP(html,event) {
	var obj		= parent.document.getElementById('spaceditor_blank_div');
	obj.style.display = 'block';
	obj.style.height = "0";
	obj.style.textAlign = "left";
	var oDiv2	= parent.document.createElement("div");
	oDiv2.id	= 'theScreenLayer2';
	oDiv2.className = 'blindScreenCover';
	oDiv2.innerHTML	= "<div onmouseup='hideContextMenu()' style='width:100%;height:100%'></div>";
	obj.appendChild(oDiv2);

	if(getScrollSizeP('top')>0) {
		oDiv2.style.height = parent.document.body.offsetHeight+16+"px";
	} else {
		oDiv2.style.height = "100%";
	}
	if(getScrollSizeP('left')>0) {
		oDiv2.style.width = parent.document.body.offsetWidth+getScrollSizeP('left')+16+"px";
	} else {
		oDiv2.style.width = "100%";
	}
	
	var oDiv	= parent.document.createElement("div");
	oDiv.id		= 'theScreenLayer3';
	oDiv.style.width = "auto";
	oDiv.style.height = "auto";
	oDiv.style.zIndex = 105;
	oDiv.style.position = "absolute";
	oDiv.style.backgroundColor = "yellow";
	oDiv.style.marginLeft = (event.clientX - 10)+"px";
	oDiv.style.marginTop = (event.clientY - 10) + "px";
	oDiv.innerHTML	= html;
	obj.appendChild(oDiv);
	editor.document.oncontextmenu = function (e) {
		return false;
	}
	parent.document.oncontextmenu = function (e) {
		return false;
	}
}
function hideContextMenu(e) {
	if(!e) e = event;
	var oDiv = document.getElementById('spaceditor_blank_div');
	if(oDiv) {
		oDiv.innerHTML = "";
		oDiv.style.display = 'none';
	}
	document.oncontextmenu = function (e) {
	 return true;
	}
	editor.document.oncontextmenu = function (e) {
	 return true;
	}
}
var bdown = false;
var x, y;
var sElem;
function mdown() {
	if(event.srcElement.className == 'property') {
		bdown = true;
		sElem = event.srcElement;
		x = event.clientX;
		y = event.clientY;
	}
}
function mup() {
    bdown = false;
}
function moveimg() {
	if(bdown) {
		var distX = event.clientX - x;
		var distY = event.clientY - y;
		if(isFF()) {
			sElem.style.pixelLeft += distX + "px";
			sElem.style.pixelTop += distY + "px";
		} else {
			sElem.style.pixelLeft += distX;
			sElem.style.pixelTop += distY;
		}
		x = event.clientX;
		y = event.clientY;
		return false;
	}
}
document.onmousedown = mdown;
document.onmouseup   = mup;
document.onmousemove = moveimg;
function createRequest(){
	var request = null;
	try{
		request = new XMLHttpRequest();
	} catch(ms){
		try{
			request = new ActiveXObject("Msxml2.XMLHTTP");
		} catch(otherms){
			try{
				request = new ActiveXObject("Microsoft.XMLHTTP");
			} catch(failed){}
		}
	}
	if(request == null){
		alert("다시 시도하여 주세요.");
	}
	return request;
}
if (!document.getElementsByClassName) {
	  document.getElementsByClassName = function (cn) {
		var rx = new RegExp("\\b" + cn + "\\b"), allT = document.getElementsByTagName("*"), allCN = [], i = 0, a;
			while (a = allT[i++]) {
			  if (a.className && a.className.indexOf(cn) + 1) {
				if(a.className===cn){ allCN[allCN.length] = a; continue;   }
				rx.test(a.className) ? (allCN[allCN.length] = a) : 0;
			  }
			}
		return allCN;
	}
}
function setRightButtonEvent(e) {
	if(!e) e = event;
	setImageContextMenu(e);
	setTableContextMenu(e);
	setSourceBoxContextMenu(e);
}
function setImageContextMenu(e) {
	var img_obj = editor.document.getElementsByTagName('img');
	for(var i = 0; i < img_obj.length; i++) {
		if(isIE()) {
			img_obj[i].onmouseup = function() {
				viewImageContextMenu(this,e);
			}
		} else {
			img_obj[i].onmouseup = function(e) {
				viewImageContextMenu(this,e);
			}
		}
	}
}
function setTableContextMenu(e) {
	var tbl_obj = editor.document.getElementsByTagName('table');
	for(var i = 0; i < tbl_obj.length; i++) {
		if(isIE()) {
			tbl_obj[i].onmouseup = function() {
				viewTableContextMenu(this,e);
			}
		} else {
			tbl_obj[i].onmouseup = function(e) {
				viewTableContextMenu(this,e);
			}
		}
	}
}
function setSourceBoxContextMenu(e) {
	if(editor.document.getElementsByClassName == undefined) return;
	var srcbox_obj = editor.document.getElementsByClassName('source');
	for(var i = 0; i < srcbox_obj.length; i++) {
		if(isIE()) {
			srcbox_obj[i].onmouseup = function() {
				viewSourceBoxContextMenu(this,e);
			}
		} else {
			srcbox_obj[i].onmouseup = function(e) {
				viewSourceBoxContextMenu(this,e);
			}
		}
	}
}
var image_obj;
function viewImageContextMenu(tmp_obj,e) {
	if(e.button == 2) {
		image_obj = tmp_obj;
		var html  = "<div class='context_menu_box'>";
			html += "<div class='context_menu_unit' onMouseOver=\"this.className='context_menu_unit_up'\" onMouseOut=\"this.className='context_menu_unit'\" onClick=\"viewImageProperty()\"><img src='"+base_dir+"/images/image_property.png' width='16' height='16' align='absmiddle'> 이미지 속성</div>";
			html += "</div>";
		viewBlindScreenP(html,e);
	}
}
var sourcebox_obj;
function viewSourceBoxContextMenu(tmp_obj,e) {
	SelectString = getSelectedEditorHtmlText();
	if(SelectString == "") {
		if(e.button == 2) {
			sourcebox_obj = tmp_obj;
			var html  = "<div class='context_menu_box'>";
				html += "<div class='context_menu_unit' onMouseOver=\"this.className='context_menu_unit_up'\" onMouseOut=\"this.className='context_menu_unit'\" onClick=\"setSource()\"> 줄바꿈 정리</div>";
				html += "<div class='context_menu_unit' onMouseOver=\"this.className='context_menu_unit_up'\" onMouseOut=\"this.className='context_menu_unit'\" onClick=\"viewSourceContents()\"> 소스만 보기</div>";
				html += "</div>";
			viewBlindScreenP(html,e);
		}
	}
}
function viewTableContextMenu(tmp_obj,e) {
	if(e.button == 2) {
		table_obj = tmp_obj;
		setCurrCellIdx();
		var html  = "<div class='context_menu_box'>";
			html += "<div class='context_menu_unit' onMouseOver=\"this.className='context_menu_unit_up'\" onMouseOut=\"this.className='context_menu_unit'\" onClick=\"addRow(1)\"><img src='"+base_dir+"/images/table_row_insert_up.png' width='16' height='16' align='absmiddle'/> 윗쪽에 행삽입</div>";
			html += "<div class='context_menu_unit' onMouseOver=\"this.className='context_menu_unit_up'\" onMouseOut=\"this.className='context_menu_unit'\" onClick=\"addRow(2)\"><img src='"+base_dir+"/images/table_row_insert_down.png' width='16' height='16' align='absmiddle'/> 아래쪽에 행삽입</div>";
			html += "<div class='context_menu_unit' onMouseOver=\"this.className='context_menu_unit_up'\" onMouseOut=\"this.className='context_menu_unit'\" onClick=\"addCell(1)\"><img src='"+base_dir+"/images/table_col_insert_left.png' width='16' height='16' align='absmiddle'/> 왼쪽에 열삽입</div>";
			html += "<div class='context_menu_unit' onMouseOver=\"this.className='context_menu_unit_up'\" onMouseOut=\"this.className='context_menu_unit'\" onClick=\"addCell(2)\"><img src='"+base_dir+"/images/table_col_insert_right.png' width='16' height='16' align='absmiddle'/> 오른쪽에 열삽입</div>";
			html += "<div class='context_menu_unit' onMouseOver=\"this.className='context_menu_unit_up'\" onMouseOut=\"this.className='context_menu_unit'\" onClick=\"deleteRow()\"><img src='"+base_dir+"/images/table_row_delete.png' width='16' height='16' align='absmiddle'/> 현재행 삭제</div>";
			html += "<div class='context_menu_unit' onMouseOver=\"this.className='context_menu_unit_up'\" onMouseOut=\"this.className='context_menu_unit'\" onClick=\"deleteCol()\"><img src='"+base_dir+"/images/table_col_delete.png' width='16' height='16' align='absmiddle'/> 현재열 삭제</div>";
			html += "<div class='context_menu_unit' onMouseOver=\"this.className='context_menu_unit_up'\" onMouseOut=\"this.className='context_menu_unit'\" onClick=\"mergeCell(1)\"><img src='"+base_dir+"/images/table_col_merge.png' width='16' height='16' align='absmiddle'/> 오른쪽셀과 병합</div>";
			html += "<div class='context_menu_unit' onMouseOver=\"this.className='context_menu_unit_up'\" onMouseOut=\"this.className='context_menu_unit'\" onClick=\"mergeCell(2)\"><img src='"+base_dir+"/images/table_row_merge.png' width='16' height='16' align='absmiddle'/> 아래쪽셀과 병합</div>";
			html += "<div class='context_menu_unit' onMouseOver=\"this.className='context_menu_unit_up'\" onMouseOut=\"this.className='context_menu_unit'\" onClick=\"devideCell()\"><img src='"+base_dir+"/images/table_cancel_merge.png' width='16' height='16' align='absmiddle'/> 셀 병합 취소</div>";
			html += "</div>";
		viewBlindScreenP(html,e);
	}
}