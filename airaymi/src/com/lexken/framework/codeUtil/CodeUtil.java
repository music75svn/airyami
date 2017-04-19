/*************************************************************************
* CLASS 명  	: CodeUtil
* 작 업 자  	: 박재현
* 작 업 일  	: 2009.07.15
* 기    능  	: 코드유틸
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    박재현		 2009.07.15			  최 초 작 업
*   2    하윤식		 2012.06.25			  년도구분 작업추가
**************************************************************************/
package com.lexken.framework.codeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import com.lexken.framework.util.HtmlHelper;

public class CodeUtil{

	private static HashMap<String, ArrayList<CodeUtilVO>> codeHash = null;

	/**
	 * reload 공통코드
	 * @param
	 * @return
	*/
	private static void reLoadCodeHash(){
		CodeUtilService codeUtilService = CodeUtilService.getInstance();
		codeHash = codeUtilService.reLoadCodeHash();
	}

	/**
	 * reload 공통코드
	 * @param
	 * @return
	*/
	public static void reSetCodeUtil(){
		synchronized (codeHash) {
			codeHash = null;
			reLoadCodeHash();
		}
	}

	/**
	 * 공통코드 상세 리스트 조회
	 * @param codeGrpId
	 * @return ArrayList
	*/
	public static ArrayList<CodeUtilVO> getCodeList(String codeGrpId){
		if(codeHash == null){
			reLoadCodeHash();
		}
		return codeHash.get(codeGrpId);
	}

	/**
	 * 코드그룹명 조회
	 * @param codeGrpId
	 * @return String
	*/
	public static String getCodeGrpName(String codeGrpId){
		String result = "";
		ArrayList<CodeUtilVO> list = getCodeList("CODE_GRP_LIST");

		if(list != null && list.size() > 0){
			for(int i=0; i<list.size(); i++){
				CodeUtilVO codeUtilVO = (CodeUtilVO)list.get(i);
				if(codeUtilVO.getCode_grp_id() != null){
					if(codeUtilVO.getCode_grp_id().equals(codeGrpId)){
						result = codeUtilVO.getCode_grp_nm();
						break;
					}
				}
			}
		}
		return result;
	}

	/**
	 * 코드그룹 년도별 관리여부 조회
	 * 년도별 관리시 true 반환
	 * @param codeGrpId
	 * @return boolean
	*/
	public static boolean getYearYn(String codeGrpId){
		boolean result = false;
		ArrayList<CodeUtilVO> list = getCodeList("CODE_GRP_LIST");

		if(list != null && list.size() > 0){
			for(int i = 0; i < list.size(); i++){
				CodeUtilVO codeUtilVO = (CodeUtilVO)list.get(i);
				if(codeUtilVO.getCode_grp_id() != null){
					if(codeUtilVO.getCode_grp_id().equals(codeGrpId)){
						if("Y".equals(codeUtilVO.getYear_yn())) {
							result = true;
							break;
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * 코드명 조회
	 * @param codeGrpId, codeId
	 * @return String
	*/
	public static String getCodeName(String codeGrpId, String codeId){
//		String result = "";
//		ArrayList<CodeUtilVO> list = getCodeList(codeGrpId);
//
//		boolean yearYn = getYearYn(codeGrpId);
//		if(yearYn) return "";
//
//		if(list != null && list.size() > 0){
//			for(int i = 0; i < list.size(); i++){
//				CodeUtilVO codeUtilVO = (CodeUtilVO)list.get(i);
//				if(codeUtilVO.getCode_id() != null){
//					if(codeUtilVO.getCode_id().equals(codeId)){
//						result = codeUtilVO.getCode_nm();
//						break;
//					}
//				}
//			}
//		}
//		return result;
		return getCodeName(codeGrpId, codeId, "9999");
	}

	/**
	 * 코드명 조회(년도별)
	 * @param codeGrpId, codeId
	 * @return String
	*/
	public static String getCodeName(String codeGrpId, String codeId, String year){
//		String result = "";
//		ArrayList<CodeUtilVO> list = getCodeList(codeGrpId);
//
//		boolean yearYn = getYearYn(codeGrpId);
//		if(!yearYn) year = "9999";
//
//		if(list != null && list.size() > 0){
//			for(int i = 0; i < list.size(); i++){
//				CodeUtilVO codeUtilVO = (CodeUtilVO)list.get(i);
//				if(codeUtilVO.getCode_id() != null){
//					if(codeUtilVO.getCode_id().equals(codeId) && codeUtilVO.getYear().equals(year)){
//						result = codeUtilVO.getCode_nm();
//						break;
//					}
//				}
//			}
//		}
//		return result;
		return getCodeName(codeGrpId, codeId, year, "");
	}
	
	/**
	 * 코드명 조회(년도별)
	 * @param codeGrpId, codeId
	 * @return String
	*/
	public static String getCodeName(String codeGrpId, String codeId, String year, String defaultName){
		String result = "";
		ArrayList<CodeUtilVO> list = getCodeList(codeGrpId);

		boolean yearYn = getYearYn(codeGrpId);
		if(!yearYn) year = "9999";

		if(list != null && list.size() > 0){
			for(int i = 0; i < list.size(); i++){
				CodeUtilVO codeUtilVO = (CodeUtilVO)list.get(i);
				if(codeUtilVO.getCode_id() != null){
					if(codeUtilVO.getCode_id().equals(codeId) && codeUtilVO.getYear().equals(year)){
						result = codeUtilVO.getCode_nm();
						break;
					}
				}
			}
		}
		if("".equals(result)) result = defaultName;
		return result;
	}

	/**
	 * etc1 조회
	 * @param codeGrpId, codeId
	 * @return String
	*/
	public static String getCodeEtc1(String codeGrpId, String codeId){
		String result = "";
		ArrayList<CodeUtilVO> list = getCodeList(codeGrpId);

		boolean yearYn = getYearYn(codeGrpId);
		if(yearYn) return "";

		if(list != null && list.size() > 0){
			for(int i = 0; i < list.size(); i++){
				CodeUtilVO codeUtilVO = (CodeUtilVO)list.get(i);
				if(codeUtilVO.getCode_id() != null){
					if(codeUtilVO.getCode_id().equals(codeId)){
						result = codeUtilVO.getEtc1();
						break;
					}
				}
			}
		}
		return result;
	}

	/**
	 * etc1 조회(년도별)
	 * @param codeGrpId, codeId
	 * @return String
	*/
	public static String getCodeEtc1(String codeGrpId, String codeId, String year){
		String result = "";
		ArrayList<CodeUtilVO> list = getCodeList(codeGrpId);

		boolean yearYn = getYearYn(codeGrpId);
		if(!yearYn) year = "9999";

		if(list != null && list.size() > 0){
			for(int i = 0; i < list.size(); i++){
				CodeUtilVO codeUtilVO = (CodeUtilVO)list.get(i);
				if(codeUtilVO.getCode_id() != null){
					if(codeUtilVO.getCode_id().equals(codeId) && codeUtilVO.getYear().equals(year)){
						result = codeUtilVO.getEtc1();
						break;
					}
				}
			}
		}
		return result;
	}

	/**
	 * etc2 조회
	 * @param codeGrpId, codeId
	 * @return String
	*/
	public static String getCodeEtc2(String codeGrpId, String codeId){
		String result = "";
		ArrayList<CodeUtilVO> list = getCodeList(codeGrpId);

		boolean yearYn = getYearYn(codeGrpId);
		if(yearYn) return "";

		if(list != null && list.size() > 0){
			for(int i = 0; i < list.size(); i++){
				CodeUtilVO codeUtilVO = (CodeUtilVO)list.get(i);
				if(codeUtilVO.getCode_id() != null){
					if(codeUtilVO.getCode_id().equals(codeId)){
						result = codeUtilVO.getEtc2();
						break;
					}
				}
			}
		}
		return result;
	}

	/**
	 * etc2 조회(년도별)
	 * @param codeGrpId, codeId
	 * @return String
	*/
	public static String getCodeEtc2(String codeGrpId, String codeId, String year){
		String result = "";
		ArrayList<CodeUtilVO> list = getCodeList(codeGrpId);

		boolean yearYn = getYearYn(codeGrpId);
		if(!yearYn) year = "9999";

		if(list != null && list.size() > 0){
			for(int i = 0; i < list.size(); i++){
				CodeUtilVO codeUtilVO = (CodeUtilVO)list.get(i);
				if(codeUtilVO.getCode_id() != null){
					if(codeUtilVO.getCode_id().equals(codeId) && codeUtilVO.getYear().equals(year)){
						result = codeUtilVO.getEtc2();
						break;
					}
				}
			}
		}
		return result;
	}

	/**
	 * 비고 조회
	 * @param codeGrpId, codeId
	 * @return String
	*/
	public static String getCodeContent(String codeGrpId, String codeId){
		String result = "";
		ArrayList<CodeUtilVO> list = getCodeList(codeGrpId);

		boolean yearYn = getYearYn(codeGrpId);
		if(yearYn) return "";

		if(list != null && list.size() > 0){
			for(int i = 0; i < list.size(); i++){
				CodeUtilVO codeUtilVO = (CodeUtilVO)list.get(i);
				if(codeUtilVO.getCode_id() != null){
					if(codeUtilVO.getCode_id().equals(codeId)){
						result = codeUtilVO.getContent();
						break;
					}
				}
			}
		}
		return result;
	}

	/**
	 * 비고 조회(년도별)
	 * @param codeGrpId, codeId
	 * @return String
	*/
	public static String getCodeContent(String codeGrpId, String codeId, String year){
		String result = "";
		ArrayList<CodeUtilVO> list = getCodeList(codeGrpId);

		boolean yearYn = getYearYn(codeGrpId);
		if(!yearYn) year = "9999";

		if(list != null && list.size() > 0){
			for(int i = 0; i < list.size(); i++){
				CodeUtilVO codeUtilVO = (CodeUtilVO)list.get(i);
				if(codeUtilVO.getCode_id() != null){
					if(codeUtilVO.getCode_id().equals(codeId) && codeUtilVO.getYear().equals(year)){
						result = codeUtilVO.getContent();
						break;
					}
				}
			}
		}
		return result;
	}

	/**
	 * 그룹코드 상세코드 select option 생성
	 * @param codeGrpId, selected
	 * @return String
	*/
	public static String getCodeOption(String codeGrpId, String selected){
		String result = "";
		ArrayList<CodeUtilVO> list = getCodeList(codeGrpId);

		if(list != null && list.size() > 0){
			for(int i = 0; i < list.size(); i++){
				CodeUtilVO codeUtilVO = (CodeUtilVO)list.get(i);

				if(selected != null && selected.equals(codeUtilVO.getCode_id())){
					result += "<option value=\"" + codeUtilVO.getCode_id() + "\" selected=\"selected\">" + HtmlHelper.txt2html(codeUtilVO.getCode_nm()) + "</option>\n";
				}else{
					result += "<option value=\"" + codeUtilVO.getCode_id() + "\">" + HtmlHelper.txt2html(codeUtilVO.getCode_nm()) + "</option>\n";
				}
			}
		}

		return result;
	}

	/**
	 * 그룹코드 상세코드 select option 생성(년도별)
	 * @param codeGrpId, selected
	 * @return String
	*/
	public static String getCodeOption(String codeGrpId, String selected, String year){
		String result = "";
		ArrayList<CodeUtilVO> list = getCodeList(codeGrpId);

		boolean yearYn = getYearYn(codeGrpId);
		if(!yearYn) year = "9999";

		if(list != null && list.size() > 0){
			for(int i = 0; i < list.size(); i++){
				CodeUtilVO codeUtilVO = (CodeUtilVO)list.get(i);
				if(!year.equals(codeUtilVO.getYear())) continue;  //해당년도만 출력

				if(selected != null && selected.equals(codeUtilVO.getCode_id())){
					result += "<option value=\"" + codeUtilVO.getCode_id() + "\" selected=\"selected\">" + HtmlHelper.txt2html(codeUtilVO.getCode_nm()) + "</option>\n";
				}else{
					result += "<option value=\"" + codeUtilVO.getCode_id() + "\">" + HtmlHelper.txt2html(codeUtilVO.getCode_nm()) + "</option>\n";
				}
			}
		}

		return result;
	}

	/**
	 * 그룹코드 상세코드 checkbox 생성
	 * @param String objName, String codeGrpId, String checked, String delimiter(구분자)
	 * @return String
	*/
	public static String getCodeCheckBox(String objName, String codeGrpId, String checked, String delimiter, String year){
		String result = "";
		String checkstr = "";
		ArrayList<CodeUtilVO> list = getCodeList(codeGrpId);

		boolean yearYn = getYearYn(codeGrpId);
		if(!yearYn) year = "9999";

		if(list != null && list.size() > 0){
			for(int i=0; i<list.size(); i++){
				CodeUtilVO codeUtilVO = (CodeUtilVO)list.get(i);
				if(!year.equals(codeUtilVO.getYear())) continue;  //해당년도만 출력

				if(checked != null && checked.equals(codeUtilVO.getCode_id())){
					checkstr = "checked=\"checked\"";
				}else{
					checkstr = "";
				}
				if("".equals(result)){
					result += "<input type=\"checkbox\" name=\""+objName+"\" value=\"" + codeUtilVO.getCode_id() + "\" " + checkstr + " />&nbsp;" + HtmlHelper.txt2html(codeUtilVO.getCode_nm());
				}else{
					result += "&nbsp;" + delimiter + "&nbsp;<input type=\"checkbox\" name=\""+objName+"\" value=\"" + codeUtilVO.getCode_id() + "\" " + checkstr + " />&nbsp;" + HtmlHelper.txt2html(codeUtilVO.getCode_nm());

				}
			}
		}
		return result;
	}

	/**
	 * 그룹코드 상세코드 checkbox 생성
	 * @param String objName, String codeGrpId, String checkeds, String delimiter(구분자)
	 * @return String
	*/
	public static String getCodeCheckBox(String objName, String codeGrpId, String[] checkeds, String delimiter, String year){
		String result = "";
		String checkstr = "";
		ArrayList<CodeUtilVO> list = getCodeList(codeGrpId);

		boolean yearYn = getYearYn(codeGrpId);
		if(!yearYn) year = "9999";

		if(list != null && list.size() > 0){
			for(int i=0; i<list.size(); i++){
				CodeUtilVO codeUtilVO = (CodeUtilVO)list.get(i);
				if(!year.equals(codeUtilVO.getYear())) continue;  //해당년도만 출력

				checkstr = "";
				for(int j=0; j<checkeds.length; j++){
					if(codeUtilVO.getCode_id().equals(checkeds[j])){
						checkstr = "checked=\"checked\"";
						break;
					}
				}

				if("".equals(result)){
					result += "<input type=\"checkbox\" name=\""+objName+"\" value=\"" + codeUtilVO.getCode_id() + "\" " + checkstr + " />&nbsp;" + HtmlHelper.txt2html(codeUtilVO.getCode_nm());
				}else{
					result += "&nbsp;" + delimiter + "&nbsp;<input type=\"checkbox\" name=\""+objName+"\" value=\"" + codeUtilVO.getCode_id() + "\" " + checkstr + " />&nbsp;" + HtmlHelper.txt2html(codeUtilVO.getCode_nm());

				}
			}
		}
		return result;
	}

	/**
	 * 그룹코드 상세코드 checkbox 생성
	 * @param String objName, String codeGrpId, String checked
	 * @return String
	*/
	public static String getCodeCheckBox(String objName, String codeGrpId, String checked, String year){
		String result = getCodeCheckBox(objName, codeGrpId, checked, "&nbsp;&nbsp;", year);
		return result;
	}

	/**
	 * 그룹코드 상세코드 checkbox 생성
	 * @param String objName, String codeGrpId, String[] checkeds
	 * @return String
	*/
	public static String getCodeCheckBox(String objName, String codeGrpId, String[] checkeds, String year){
		String result = getCodeCheckBox(objName, codeGrpId, checkeds, "&nbsp;&nbsp;", year);
		return result;
	}


	/**
	 * 그룹코드 상세코드 radio 생성
	 * @param String objName, String codeGrpId, String checkeds, String delimiter(구분자)
	 * @return String
	*/
	public static String getCodeRadio(String objName, String codeGrpId, String checked, String delimiter, String year){
		String result = "";
		String checkstr = "";
		ArrayList<CodeUtilVO> list = getCodeList(codeGrpId);

		boolean yearYn = getYearYn(codeGrpId);
		if(!yearYn) year = "9999";

		if(list != null && list.size() > 0){
			for(int i=0; i<list.size(); i++){
				CodeUtilVO codeUtilVO = (CodeUtilVO)list.get(i);
				if(!year.equals(codeUtilVO.getYear())) continue;  //해당년도만 출력

				if(checked != null && checked.equals(codeUtilVO.getCode_id())){
					checkstr = "checked=\"checked\"";
				} else {
					checkstr = "";
					if(checked == null || "".equals(checked)){
						//if(i==0) checkstr = "checked=\"checked\"";
					}
				}
				if("".equals(result)){
					result += "<input type=\"radio\" name=\""+objName+"\" id=\""+objName+i+"\" value=\"" + codeUtilVO.getCode_id() + "\" " + checkstr + " />&nbsp;<label for=\""+objName+i+"\">" + HtmlHelper.txt2html(codeUtilVO.getCode_nm()) + "</label>";
				}else{
					result += "&nbsp;" + delimiter + "&nbsp;<input type=\"radio\" name=\""+objName+"\" id=\""+objName+i+"\" value=\"" + codeUtilVO.getCode_id() + "\" " + checkstr + " />&nbsp;<label for=\""+objName+i+"\">" + HtmlHelper.txt2html(codeUtilVO.getCode_nm()) + "</label>";

				}

			}
		}
		return result;
	}

	/**
	 * 그룹코드 상세코드 radio 생성
	 * @param String objName, String codeGrpId, String checkeds, String delimiter(구분자)
	 * @return String
	*/
	public static String getCodeRadio(String objName, String codeGrpId, String checked, String year){
		String result = getCodeRadio(objName, codeGrpId, checked, "&nbsp;", year);
		return result;
	}

	/**
	 * 그룹코드 상세코드 스크립트 배열 객체 생성
	 * @param String codeGrpId, String arrCd, String arrNm
	 * @return String
	*/
	public static String getCodeScriptArrayObject(String codeGrpId, String arrCd, String arrNm, String year){
		String result = "";
		ArrayList<CodeUtilVO> list = getCodeList(codeGrpId);

		boolean yearYn = getYearYn(codeGrpId);
		if(!yearYn) year = "9999";

		result += "var " + arrCd + " = new Array();\n";
		result += "var " + arrNm + " = new Array();\n";

		if(list != null && list.size() > 0){
			for(int i=0; i<list.size(); i++){
				CodeUtilVO codeUtilVO = (CodeUtilVO)list.get(i);
				if(!year.equals(codeUtilVO.getYear())) continue;  //해당년도만 출력

				result += arrCd + "[" + i + "] = '" + codeUtilVO.getCode_id() + "';\n";
				result += arrNm + "[" + i + "] = '" + codeUtilVO.getCode_nm() + "';\n";

			}
		}
		return result;
	}

	/**
	 * 지표실적 입력/승인 범례
	 * @return
	 */
	public static String getCodeKpiLegend(String actType) {
		String result = "";
		String year = "";

		ArrayList<CodeUtilVO> list = getCodeList("015");

		boolean yearYn = getYearYn("015");
		if(!yearYn) year = "9999";

		if(list != null && list.size() > 0){
			result += "<ul>";
			result += "<li class='strong'>[목표승인상태 범례]</li>";
			for(int i=0; i<list.size(); i++){
				CodeUtilVO codeUtilVO = (CodeUtilVO)list.get(i);
				if(!year.equals(codeUtilVO.getYear())) continue;  //해당년도만 출력
				if ("04".equals(codeUtilVO.getCode_id())) {
					if (!"input".equals(actType) && !"check".equals(actType)) {
						result += "<li>&nbsp;(" + codeUtilVO.getEtc1() + ") : " + codeUtilVO.getCode_nm() + " </li>";
					}
				} else {
					result += "<li>&nbsp;(" + codeUtilVO.getEtc1() + ") : " + codeUtilVO.getCode_nm() + " </li>";
				}
			}
			result += "</ul>";
		}
		return result;
	}

}
