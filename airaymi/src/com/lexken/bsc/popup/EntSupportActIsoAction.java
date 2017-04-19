/*************************************************************************
* CLASS 명      : entSupportActIso
* 작 업 자      : 김기현
* 작 업 일      : 2012년 6월 18일
* 기    능      : 시스템항목관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    김기현      시스템항목관리            최 초 작 업
**************************************************************************/
package com.lexken.bsc.popup;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.struts2.IsBoxActionSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.util.StaticUtil;

public class EntSupportActIsoAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 시스템항목관리 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap entSupportActIsoList(SearchMap searchMap) {
    	
    	if ("".equals(StaticUtil.nullToBlank((String)searchMap.get("findDeptId")))) {
			searchMap.put("findDeptId", getStr("bsc.popup.entSupportActIso.getScDeptMapping", searchMap));
		}

    	//최상위 평가조직 조회
        searchMap.addList("deptInfo", getList("bsc.popup.entSupportActIso.getDeptList", searchMap));

        searchMap.addList("itemInfo", getList("bsc.popup.entSupportActIso.getItem", searchMap));

        return searchMap;
    }

    /**
     * 시스템항목관리 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap entSupportActIsoList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("bsc.popup.entSupportActIso.getList", searchMap));

        return searchMap;
    }

    /**
     * 부서 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap deptList_ajax(SearchMap searchMap) {

    	searchMap.addList("deptList", getList("bsc.popup.entSupportActIso.getDeptList", searchMap));

    	if("".equals(searchMap.getString("findDeptId"))) {
        	searchMap.put("findDeptId", searchMap.getDefaultValue("deptList", "DEPT_ID", 0));
        }

        return searchMap;
    }

    /**
     * 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap excel(SearchMap searchMap) {
    	String excelFileName = "기업지원실적(ISO인증)";
    	String excelTitle = "기업지원실적(ISO인증)";

    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();

    	excelSearchInfoList.add(new ExcelVO("년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("월", (String)searchMap.get("monNm")));
    	excelSearchInfoList.add(new ExcelVO("부서", (String)searchMap.get("deptNm")));
    	excelSearchInfoList.add(new ExcelVO("항목", (String)searchMap.get("itemNm")));

    	/*
		colNames     :['부서명', '심사', '연계항목', '인증종류', '세부인증', '실적건수'],
		colModel     :[
                        {name:'DEPT_NM'       		,index:'DEPT_NM'     		,width:60   ,align:'center' },
                        {name:'INSP_KIND_TC_NM' 	,index:'INSP_KIND_TC_NM' 	,width:60   ,align:'center' },
                        {name:'LAW_TC_NM'  			,index:'LAW_TC_NM'  		,width:60   ,align:'center' },
                        {name:'STIC_LAG_TC_NM'  	,index:'STIC_LAG_TC_NM'  	,width:60   ,align:'center' },
                        {name:'STIC_MID_TC_NM'  	,index:'STIC_MID_TC_NM'  	,width:60   ,align:'center' },
                        {name:'INSP_QTY'  			,index:'INSP_QTY'  			,width:60   ,align:'center' }
						],
		*/

    	/****************************************************************************************************
    	 * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
    	 ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("부서명", "DEPT_NM", "left"));
		excelInfoList.add(new ExcelVO("심사", "INSP_KIND_TC_NM", "center"));
		excelInfoList.add(new ExcelVO("연계항목", "LAW_TC_NM", "center"));
		excelInfoList.add(new ExcelVO("인증종류", "STIC_LAG_TC_NM", "center"));
		excelInfoList.add(new ExcelVO("세부인증", "STIC_MID_TC_NM", "center"));
		excelInfoList.add(new ExcelVO("실적건수", "INSP_QTY", "center"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);

    	searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.entSupportActIso.getList", searchMap));

    	return searchMap;
    }
}
