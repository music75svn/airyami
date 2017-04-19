// 비주얼박스 차트 스크립트

var isIE			= (navigator.appVersion.indexOf("MSIE") != -1) ? true : false;
var isWin		= (navigator.appVersion.toLowerCase().indexOf("win") != -1) ? true : false;
var isOpera		= (navigator.userAgent.indexOf("Opera") != -1) ? true : false;
var isFF			= (navigator.userAgent.indexOf("Firefox") != -1) ? true : false;
var isChrome	= (navigator.userAgent.indexOf("Chrome") != -1) ? true : false;
var isSafari		= (navigator.userAgent.indexOf("Safari") != -1) ? true : false;

var ChartPath ="/images/vbox/";// 플래쉬 경로설정 (온라인상에서는 절대경로)
var WebConfigPath="/";// webconfig 위치

// id		: html 오브젝트 id
// w		: 오브젝트 넓이
// h		: 오브젝트 높이
// kind	: 차트 종류
// arg		: 인자 값 넣을때 꼭 인코딩 후 넣으세요.
// isxml	: 자바스크립트 XML 값 사용유무

function VBOX_Chart(id,w,h,kind,arg,isxml)
{
	if(isIE && isWin){
		document.write("<object classid='clsid:D27CDB6E-AE6D-11cf-96B8-444553540000' codebase='http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0' width='"+w+"' height='"+h+"' id='"+id+"' align='bottom'>");
		document.write("<param name='movie' value='"+ChartPath+"VBOX_CHART.swf?_pPath="+WebConfigPath+"&_pkind="+kind+"&_cpath="+ChartPath+"&_pParam="+arg+"&_isXML="+isxml+"' />");
		document.write("<param name='allowScriptAccess' value='sameDomain' />");
		document.write("<param name='wmode' value='transparent' />");
		document.write("<param name='quality' value='high' />");
		document.write("<param name='bgcolor' value='#ffffff' />");
		document.write("</object>");
	}else{
		document.write("<embed src='"+ChartPath+"VBOX_CHART.swf?_pPath="+WebConfigPath+"&_pkind="+kind+"&_cpath="+ChartPath+"&_pParam="+arg+"&_isXML="+isxml+"' width='"+w+"' height='"+h+"' quality='high' bgcolor='#ffffff' name='"+id+"' align='middle' allowScriptAccess='sameDomain' pluginspage='http://www.macromedia.com/go/getflashplayer' type='application/x-shockwave-flash'></embed>");
	}

	window[id] = document.getElementById(id);
}


function createObject(id,type){
	this[id] = new VChart(type);
	this[id].className = id;

}

///////////////////////////////////////////////////////////////////////////////////
//
//	↓   VChart 생성자   ↓
//
///////////////////////////////////////////////////////////////////////////////////
function VChart(type){
	this.className;
	this.Type = type;
	this.Series = new VSeries(this.Type);
	this.Application = new Object();
	this.Table = new Object();
	this.Data = new Object();
	this.Category = new Object();

	this.Application.Width = "300";
	this.Application.Height = "200";

	// XML 변환함수
	this.getXmlString = function(startString,endString,valueObj) {
		var doc = "";
			doc += startString;
		for (var attr in valueObj)
		{
			if((valueObj[attr] == "") || (valueObj[attr] == null) || (valueObj[attr] == undefined) || (valueObj[attr] == "undefined")) {
			} else {
				doc += attr + "=\"" + valueObj[attr] + "\" ";
			}
		}
			doc += endString;

		return doc;
	}

	///////////////////////////////////////////////////////////////////////////////////
	//
	//	↓   차트 종류에 따라 Series XML 추출   ↓
	//
	///////////////////////////////////////////////////////////////////////////////////
	// Ten_Chart (AreaChart, ColumnAreaChart, BarChart, ColumnBarChart, LineChart, ColumnLineChart, CombinationChart, CombinationColumnChart, StackedBarChart, StackedColumnBarChart)
	this.getSeriesXml_TenChart = function() {
		var doc=" <Chart> \n";

		var seriesName = this.Series["Name"].split(",");
		var seriesColor = this.Series["Color"].split(",");
		var seriesType = this.Series["Type"].split(",");
		var seriesIconType = this.Series["IconType"].split(",");

		var seriesLabel = this.Series["Label"].split(",");

		var serieslen = seriesName.length;
		var seriesDatalen = seriesLabel.length;

		for(var i=0;i<serieslen;i++){
			doc+= "<Series Name=\""+seriesName[i]+"\" Color=\""+seriesColor[i]+"\"  Type=\""+seriesType[i]+"\" IconType=\""+seriesIconType[i]+"\" > \n"
			var seriesValue = this.Series["Value"][i].split(",");
			// Url 가져오는 부분 (생략가능하도록 IF문)
			if((this.Series["Url"][i] != undefined) && (this.Series["Url"][i] != "undefined")) {
				var seriesUrl = this.Series["Url"][i].split(",");
			} else {
				var seriesUrl = [];
				for (var z=0;z<seriesDatalen;z++) {
					seriesUrl[z] = "";
				}
			}
			// Target 가져오는 부분 (생략가능하도록 IF문)
			if((this.Series["Target"][i] != undefined) && (this.Series["Target"][i] != "undefined")) {
				var seriesTarget = this.Series["Target"][i].split(",");
			} else {
				var seriesTarget = [];
				for (var z=0;z<seriesDatalen;z++) {
					seriesTarget[z] = "";
				}
			}
			for(var j=0;j<seriesDatalen;j++){
				doc+=" <Data Label=\""+seriesLabel[j]+"\" Value=\""+seriesValue[j]+"\"  url=\""+seriesUrl[j]+"\" target=\""+seriesTarget[j]+"\" /> \n"		
			}
			doc+= "</Series>\n"
		}
		doc += "</Chart>\n\n"
		return doc;
	}

	// XY_Bubble_Chart
	this.getSeriesXml_XY_Bubble_Chart = function() {
		var doc=" <Chart> \n";

		var seriesName = this.Series["Name"].split(",");
		var seriesColor = this.Series["Color"].split(",");
		var seriesIconType = this.Series["IconType"].split(",");

		var ValueNumber = Number(this.Series.ValueNumber);

		var serieslen = seriesName.length;

		for(var i=0;i<serieslen;i++){
			doc+= "<Series Name=\""+seriesName[i]+"\" Color=\""+seriesColor[i]+"\" IconType=\""+seriesIconType[i]+"\" > \n"
			var seriesX = this.Series["X"][i].split(",");
			var seriesY = this.Series["Y"][i].split(",");
			var seriesScale = this.Series["Scale"][i].split(",");
			// Url 가져오는 부분 (생략가능하도록 IF문)
			if((this.Series["Url"][i] != undefined) && (this.Series["Url"][i] != "undefined")) {
				var seriesUrl = this.Series["Url"][i].split(",");
			} else {
				var seriesUrl = [];
				for (var z=0;z<ValueNumber;z++) {
					seriesUrl[z] = "";
				}
			}
			// Target 가져오는 부분 (생략가능하도록 IF문)
			if((this.Series["Target"][i] != undefined) && (this.Series["Target"][i] != "undefined")) {
				var seriesTarget = this.Series["Target"][i].split(",");
			} else {
				var seriesTarget = [];
				for (var z=0;z<ValueNumber;z++) {
					seriesTarget[z] = "";
				}
			}
			for(var j=0;j<ValueNumber;j++){
				doc+=" <Data X=\""+seriesX[j]+"\" Y=\""+seriesY[j]+"\" Scale=\""+seriesScale[j]+"\" url=\""+seriesUrl[j]+"\" target=\""+seriesTarget[j]+"\" /> \n"		
			}
			doc+= "</Series>\n"
		}
		doc += "</Chart>\n\n"
		return doc;
	}

	// Cylinder_Chart
	this.getSeriesXml_Cylinder_Chart = function() {
		var doc=" <Chart> \n";

		var seriesValue = this.Series["Value"];
		var seriesColor = this.Series["Color"];

		doc+= "<Series Value=\""+seriesValue+"\" Color=\""+seriesColor+"\" > \n"
		doc+= "</Series>\n"
		doc+= "</Chart>\n\n"
		return doc;
	}

	// Pie_Chart
	this.getSeriesXml_Pie_Chart = function() {
		var doc=" <Chart> \n";

		var seriesName = this.Series["Name"].split(",");
		var seriesLabel = this.Series["Label"].split(",");
		var seriesDatalen = seriesLabel.length;

		doc+= "<Series Name=\""+seriesName+"\" > \n"
		var seriesValue = this.Series["Value"][0].split(",");
		var seriesColor = this.Series["Color"][0].split(",");
			// Url 가져오는 부분 (생략가능하도록 IF문)
			if((this.Series["Url"][0] != undefined) && (this.Series["Url"][0] != "undefined")) {
				var seriesUrl = this.Series["Url"][0].split(",");
			} else {
				var seriesUrl = [];
				for (var z=0;z<seriesDatalen;z++) {
					seriesUrl[z] = "";
				}
			}
			// Target 가져오는 부분 (생략가능하도록 IF문)
			if((this.Series["Target"][0] != undefined) && (this.Series["Target"][0] != "undefined")) {
				var seriesTarget = this.Series["Target"][0].split(",");
			} else {
				var seriesTarget = [];
				for (var z=0;z<seriesDatalen;z++) {
					seriesTarget[z] = "";
				}
			}

			for(var j=0;j<seriesDatalen;j++){
				doc+=" <Data Label=\""+seriesLabel[j]+"\" Value=\""+seriesValue[j]+"\" Color=\""+seriesColor[j]+"\" url=\""+seriesUrl[j]+"\" target=\""+seriesTarget[j]+"\" /> \n"		
			}
		doc+= "</Series>\n"
		doc += "</Chart>\n\n"
		return doc;
	}

	// Radar_Chart
	this.getSeriesXml_Radar_Chart = function() {
		var doc=" <Chart> \n";

		var seriesName = this.Series["Name"].split(",");
		var seriesAreaLineColor = this.Series["AreaLineColor"].split(",");
		var seriesAreaColor = this.Series["AreaColor"].split(",");
		var seriesIconType = this.Series["IconType"].split(",");

		var seriesLabel = this.Series["Label"].split(",");

		var serieslen = seriesName.length;
		var seriesDatalen = seriesLabel.length;

		for(var i=0;i<serieslen;i++){
			doc+= "<Series Name=\""+seriesName[i]+"\" AreaLineColor=\""+seriesAreaLineColor[i]+"\" AreaColor=\""+seriesAreaColor[i]+"\" IconType=\""+seriesIconType[i]+"\" > \n"
			var seriesValue = this.Series["Value"][i].split(",");
			// Url 가져오는 부분 (생략가능하도록 IF문)
			if((this.Series["Url"][i] != undefined) && (this.Series["Url"][i] != "undefined")) {
				var seriesUrl = this.Series["Url"][i].split(",");
			} else {
				var seriesUrl = [];
				for (var z=0;z<seriesDatalen;z++) {
					seriesUrl[z] = "";
				}
			}
			// Target 가져오는 부분 (생략가능하도록 IF문)
			if((this.Series["Target"][i] != undefined) && (this.Series["Target"][i] != "undefined")) {
				var seriesTarget = this.Series["Target"][i].split(",");
			} else {
				var seriesTarget = [];
				for (var z=0;z<seriesDatalen;z++) {
					seriesTarget[z] = "";
				}
			}
			for(var j=0;j<seriesDatalen;j++){
				doc+=" <Data Label=\""+seriesLabel[j]+"\" Value=\""+seriesValue[j]+"\" url=\""+seriesUrl[j]+"\" target=\""+seriesTarget[j]+"\" /> \n"		
			}
			doc+= "</Series>\n"
		}
		doc += "</Chart>\n\n"
		return doc;
	}

	// Gauge_HalfGauge_Chart
	this.getSeriesXml_Gauge_Chart = function() {
		var doc=" <Chart> \n";

		var seriesValue = this.Series["Value"]
		var seriesColor = this.Series["Color"]

		var seriesLevelValue = this.Series["LevelValue"].split(",");

		var seriesDatalen = seriesLevelValue.length;

		doc+= "<Series Value=\""+seriesValue+"\" Color=\""+seriesColor+"\" > \n"
		var seriesLevelColor = this.Series["LevelColor"].split(",");
			for(var j=0;j<seriesDatalen;j++){
				doc+=" <Data LevelValue=\""+seriesLevelValue[j]+"\" LevelColor=\""+seriesLevelColor[j]+"\" /> \n"		
			}
		doc+= "</Series>\n"
		doc += "</Chart>\n\n"
		return doc;
	}
	

	///////////////////////////////////////////////////////////////////////////////////
	//
	//	↓   사용자가 사용한 Style   ↓
	//
	///////////////////////////////////////////////////////////////////////////////////
	this.getStypesXML = function() {
		var doc = "<Styles>\n";
			doc += this.getXmlString("<Application ","/>\n",this.Application);
			doc += this.getXmlString("<Table ","/>\n",this.Table);
			doc += this.getXmlString("<Data ","/>\n",this.Data);
			doc += this.getXmlString("<Category ","/>\n",this.Category);
		doc += "</Styles>\n\n";

		return doc;
	}

	///////////////////////////////////////////////////////////////////////////////////
	//
	//	↓   전체 XML 생성   ↓
	//
	///////////////////////////////////////////////////////////////////////////////////
	this.Bind = function() {
		VBOX_Chart(this.className+"_FlashID",this.Application["Width"],this.Application["Height"],this["Type"],this.className,"true");
	}

	this.flashBind = function (){
		if ((this.Type == "AreaChart") || (this.Type == "ColumnAreaChart") || (this.Type == "BarChart") ||
			(this.Type == "ColumnBarChart") || (this.Type == "LineChart") || 	(this.Type == "ColumnLineChart") ||
			(this.Type == "CombinationChart") || (this.Type == "CombinationColumnChart") || (this.Type == "StackedBarChart") ||
			(this.Type == "StackedColumnBarChart")) {
			var getSeriesXML = this.getSeriesXml_TenChart();
		} else if ((this.Type == "XYChart") || (this.Type == "BubbleChart")) {
			var getSeriesXML = this.getSeriesXml_XY_Bubble_Chart();
		} else if (this.Type == "CylinderChart") {
			var getSeriesXML = this.getSeriesXml_Cylinder_Chart();
		} else if (this.Type == "PieChart") {
			var getSeriesXML = this.getSeriesXml_Pie_Chart();
		} else if (this.Type == "RadarChart") {
			var getSeriesXML = this.getSeriesXml_Radar_Chart();
		} else if ((this.Type == "AQUAGauge") || (this.Type == "BASICGauge") || (this.Type == "BlackOSGauge") ||
					(this.Type == "AQUAHalfGauge") || (this.Type == "BASICHalfGauge") || (this.Type == "BlackOSHalfGauge")) {
			var getSeriesXML = this.getSeriesXml_Gauge_Chart();
		}

		var getXmlDoc = "<?xml version=\"1.0\" encoding=\"euc-kr\" ?> \n <VisualBox> \n\n";
		// Series XML
		getXmlDoc += getSeriesXML;
		getXmlDoc += this.getStypesXML();
		getXmlDoc += "</VisualBox>"

		return getXmlDoc;
	}
}

function getFlashXml(id){
		var doc = this[id].flashBind();
		return doc;
}

///////////////////////////////////////////////////////////////////////////////////
//
//	↓   차트에 따른 초기 값 변수 지정   ↓
//
///////////////////////////////////////////////////////////////////////////////////
function VSeries(type){
	this.Type = type;
	if ((this.Type == "AreaChart") || (this.Type == "ColumnAreaChart") || (this.Type == "BarChart") ||
		(this.Type == "ColumnBarChart") || (this.Type == "LineChart") || 	(this.Type == "ColumnLineChart") ||
		(this.Type == "CombinationChart") || (this.Type == "CombinationColumnChart") || (this.Type == "StackedBarChart") ||
		(this.Type == "StackedColumnBarChart")) {
			this.Name="";
			this.Color="";
			this.Label="";
			
			this.Type="";
			this.IconType="";

			this.Value=[];
			this.Url=[];
			this.Target=[];
		}
		else if ((this.Type == "XYChart") || (this.Type == "BubbleChart")) {
			this.Name="";
			this.Color="";
			this.ValueNumber="";
			this.IconType="";

			this.X=[];
			this.Y=[];
			this.Scale=[];
			this.Url=[];
			this.Target=[];
		}
		else if (this.Type == "CylinderChart") {
			this.Color="";
			this.Value="";
		}
		else if (this.Type == "PieChart") {
			this.Name="";
			this.Label="";

			this.Value=[];
			this.Color=[];
			this.Url=[];
			this.Target=[];
		}
		else if (this.Type == "RadarChart") {
			this.Name="";
			this.AreaLineColor="";
			this.AreaColor="";
			this.IconType="";
			this.Label="";

			this.Value=[];
			this.Url=[];
			this.Target=[];
		}
		else if ((this.Type == "AQUAGauge") || (this.Type == "BASICGauge") || (this.Type == "BlackOSGauge") ||
					(this.Type == "AQUAHalfGauge") || (this.Type == "BASICHalfGauge") || (this.Type == "BlackOSHalfGauge")) {
			this.Value="";
			this.Color="";
			this.LevelValue="";
			this.LevelColor="";
		}
}
