
var context = "";
var IE = (navigator.userAgent.indexOf('MSIE') != -1) ? true : false;


/******************************************
 * 조직성과도 조회
 ******************************************/
// 호출방식 변경으로 사용 안 함. - 2012.09.24.형광민
/*
function orgtotal_flash(context, imgurl, findYear, findMon, findAnalCycle, findScDeptId){
	var bgNum = findYear;
	
	document.write("<object classid='clsid:D27CDB6E-AE6D-11cf-96B8-444553540000' codebase='http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,19,0' width='100%' height='330' id='mchart' align='middle' >");
	document.write("<param name='movie' value='"+imgurl+"/flash/VBOX_ORGANIZATIONS.swf?bgNum="+bgNum+"&_pPath="+context+"&_pSignal="+context+"/bsc/base/scDeptDiagMng/trafficSignal_xml.vw?suffix=vm%26findYear="+findYear+"&_pParam="+context+"/bsc/base/scDeptDiagMng/scDeptDiagMng_xml.vw?suffix=vm%26findYear="+findYear+"%26findMon="+findMon+"%26findAnalCycle="+findAnalCycle+"%26findScDeptId="+findScDeptId+"' />");
	document.write("<param name='allowScriptAccess' value='sameDomain' />");
	document.write("<param name='wmode' value='transparent' />");
	document.write("<param name='quality' value='high' />");
	document.write("<embed src='"+imgurl+"/flash/VBOX_ORGANIZATION.swf?_pPath="+context+"&_pParam="+context+"/bsc/base/scDeptDiagMng/scDeptDiagMng_xml.vw' width='600' height='489' quality='high' pluginspage='http://www.macromedia.com/go/getflashplayer' type='application/x-shockwave-flash'></embed>");
	document.write("</object>"); 
}
*/

/******************************************
 * 조직성과도 관리
 ******************************************/
// 호출방식 변경으로 사용 안 함. - 2012.09.24.형광민
/*
function orgtotal_flash_admin(context, imgurl, findYear, findMon, findAnalCycle, findScDeptId){
	var bgNum = findYear;
	
	document.write("<object classid='clsid:D27CDB6E-AE6D-11cf-96B8-444553540000' codebase='http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,19,0' width='100%' height='330' id='mchart' align='middle' >");
	document.write("<param name='movie' value='"+imgurl+"/flash/VBOX_ORGANIZATIONS.swf?bgNum="+bgNum+"&_pPath="+context+"&_pSignal="+context+"/bsc/base/scDeptDiagMng/trafficSignal_xml.vw?suffix=vm%26findYear="+findYear+"&_pParam="+context+"/bsc/base/scDeptDiagMng/scDeptDiagMng_xml.vw?suffix=vm%26findYear="+findYear+"%26findMon="+findMon+"%26findAnalCycle="+findAnalCycle+"%26findScDeptId="+findScDeptId+"&_pControl=admin_bsc_0001&_pPostUrl="+context+"/bsc/base/scDeptDiagMng/scDeptDiagMngProcess.vw?mode=MOD%26findYear="+findYear+"%26findScDeptId="+findScDeptId+"' />");
	document.write("<param name='allowScriptAccess' value='sameDomain' />");
	document.write("<param name='wmode' value='transparent' />");
	document.write("<param name='quality' value='high' />");
	document.write("<embed src='"+imgurl+"/flash/VBOX_ORGANIZATION.swf?_pPath="+context+"&_pParam="+context+"/bsc/base/scDeptDiagMng/scDeptDiagMng_xml.vw' width='600' height='489' quality='high' pluginspage='http://www.macromedia.com/go/getflashplayer' type='application/x-shockwave-flash'></embed>");
	document.write("</object>"); 
}
*/

/******************************************
 * 전략체계도 조회/관리
 ******************************************/
// 호출방식 변경으로 사용 안 함. - 2012.09.24.형광민
/*
function strategymap_flash(context, imgurl, url, sabun, sc_dept_id, sc_dept_nm, admin_gubun, admin, gubun)
{
	document.write("<object classid='clsid:D27CDB6E-AE6D-11cf-96B8-444553540000' codebase='http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,19,0' width='1147' height='450'>");
	document.write("<param name='movie' value='"+imgurl+"/flash/VBOX_STRATEGYMAPS.swf?_pPath="+context+"&AdminBool="+admin+"&requestURL="+context+"/bsc/base/deptStrategyDiagMng/deptStrategyDiagMng_xml.vw?"+url+"%26findScDeptId=" + sc_dept_id + "%26findScDeptNm=" + sc_dept_nm + "%26admin_bool="+admin+"%26gubun="+gubun+"%26img_url="+imgurl+"%26context_url="+context+"' />");
	document.write("<param name='wmode' value='transparent' />");
	document.write("<param name='quality' value='high' />");
	document.write("<embed src='"+imgurl+"/flash/VBOX_STRATEGYMAPS.swf?_pPath="+context+"&AdminBool="+admin+"&requestURL="+context+"/bsc/base/deptStrategyDiagMng/deptStrategyDiagMng_xml.vw' width='800' height='530' quality='high' pluginspage='http://www.macromedia.com/go/getflashplayer' type='application/x-shockwave-flash'></embed>");
	document.write("</object>");
}
*/

function VBOX2Swf(id,w,h,path,Varible,trans){
	var doc = "";
	var docw = "";
	
	//w ="990";
	//h = "500";
	//alert('start');
	doc += "<object classid='clsid:D27CDB6E-AE6D-11cf-96B8-444553540000' codebase='http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=9,0,0,0' width='"+w+"' height='"+h+"' id='"+id+"' align='bottom'  base='.' >";
	doc += "<param name='movie' value='"+path+"' />";
	doc += "<param name='flashVars' value='"+Varible+"' />";
	doc += "<param name='allowScriptAccess' value='always' />";
	doc += "<param name='allowFullScreen' value='true' />";
	if(trans){
		doc += "<param name='wmode' value='transparent' />";
		docw = " WMODE='transparent' "
	}
	doc += "<param name='quality' value='high' />";
	doc += "<embed src='"+path+"' flashVars='"+Varible+"' width='"+w+"' height='"+h+"'  "+docw+" quality='high' base='.'  name='"+id+"' align='middle' allowScriptAccess='always' pluginspage='http://www.macromedia.com/go/getflashplayer' type='application/x-shockwave-flash'></embed>";
	doc += "</object>";
    document.write(doc); 
    window[id] = document.getElementById(id);
}

