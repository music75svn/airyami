/*************************************************************************
* CLASS 명      : ProEduAction
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

public class ComSupportActAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 시스템항목관리 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap comSupportActList(SearchMap searchMap) {
    	
    	if ("".equals(StaticUtil.nullToBlank((String)searchMap.get("findDeptId")))) {
			searchMap.put("findDeptId", getStr("bsc.popup.comSupportAct.getScDeptMapping", searchMap));
		}

    	searchMap.addList("deptList", getList("bsc.popup.comSupportAct.getDeptList", searchMap));

    	searchMap.addList("itemInfo", getList("bsc.popup.comSupportAct.getItem", searchMap));

        return searchMap;
    }

    /**
     * 시스템항목관리 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap comSupportActList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("bsc.popup.comSupportAct.getList", searchMap));

        return searchMap;
    }

    /**
     * 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap excel(SearchMap searchMap) {
    	String excelFileName = "기업지원(자율검사)실적";
    	String excelTitle = "기업지원(자율검사)실적";

    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	/*
    	{name:'YEAR'  		 		,index:'YEAR'  		   	 	,width:70   ,align:'center' },
        {name:'MON'      			,index:'MON'      		 	,width:60   ,align:'center' },
        {name:'DEPT_CD' 			,index:'DEPT_CD',width:60   ,align:'center' },
        {name:'PERM_TC'       		,index:'PERM_TC'     		 ,width:60   ,align:'center' },
        {name:'IMP_GUBUN'     		,index:'IMP_GUBUN' 	  		 ,width:60   ,align:'center' },
        {name:'CO_NM' 				,index:'CO_NM' 				,width:60   ,align:'center' },
        {name:'INSP_NM'  			,index:'INSP_NM'  			,width:60   ,align:'center' },
        {name:'INSP_PLAN_DATE'  	,index:'INSP_PLAN_DATE'  			,width:60   ,align:'center' },
        {name:'REAL_INSP_DATE'  	,index:'REAL_INSP_DATE'  		,width:60   ,align:'center' }
    	*/
    	excelSearchInfoList.add(new ExcelVO("년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("월", (String)searchMap.get("monNm")));
    	excelSearchInfoList.add(new ExcelVO("부서", (String)searchMap.get("deptNm")));
    	excelSearchInfoList.add(new ExcelVO("항목", (String)searchMap.get("itemNm")));

    	/****************************************************************************************************
    	 * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
    	 ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("년도","YEAR", "center"));
    	excelInfoList.add(new ExcelVO("월", "MON", "center"));
    	excelInfoList.add(new ExcelVO("허가구분", "COMM_C_NM", "left"));
    	excelInfoList.add(new ExcelVO("업소명", "CO_NM", "left"));
		excelInfoList.add(new ExcelVO("검사명", "INSP_NM", "center"));
		excelInfoList.add(new ExcelVO("검사실행일", "REAL_INSP_DATE", "center"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);

    	searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.comSupportAct.getList", searchMap));

    	return searchMap;
    }
}
