/*************************************************************************
* CLASS 명      : entSupportAct
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

public class EntSupportActAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 시스템항목관리 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap entSupportActList(SearchMap searchMap) {



     	if ("".equals(StaticUtil.nullToBlank((String)searchMap.get("findDeptId")))) {
			searchMap.put("findDeptId", getStr("bsc.popup.entSupportAct.getScDeptMapping", searchMap));
		}


    	searchMap.addList("deptList", getList("bsc.popup.entSupportAct.getDeptList", searchMap));

    	searchMap.addList("itemInfo", getList("bsc.popup.entSupportAct.getItem", searchMap));

        return searchMap;
    }

    /**
     * 시스템항목관리 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap entSupportActList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("bsc.popup.entSupportAct.getList", searchMap));

        return searchMap;
    }

    /**
     * 시스템 연계데이터 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap entSupportActProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");

        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("GET".equals(stMode)) {
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
    	String excelFileName = "기업지원실적";
    	String excelTitle = "기업지원실적";
    	
    	searchMap.put("findDeptId", searchMap.get("deptId"));
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO("년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("월", (String)searchMap.get("monNm")));
    	excelSearchInfoList.add(new ExcelVO("부서", (String)searchMap.get("deptNm")));
    	excelSearchInfoList.add(new ExcelVO("항목", (String)searchMap.get("itemNm")));
    	
    	/*
    	 * 			colNames     :['부서명', '업소번호','업소명', '주소', '검사종류', '검사시기'],
			colModel     :[
                            {name:'DEPT_NM' 		,index:'DEPT_NM'		,width:50   ,align:'center' },
                            {name:'CO_NO'  			,index:'CO_NO'  		,width:30   ,align:'center' },
                            {name:'CO_NM'  			,index:'CO_NM'  		,width:30   ,align:'center' },
                            {name:'ADDR'  			,index:'ADDR'  			,width:30   ,align:'center' },
                            {name:'INSP_NM'  		,index:'INSP_NM'  		,width:30   ,align:'center' },
                            {name:'REAL_INSP_DATE'  ,index:'REAL_INSP_DATE' ,width:40   ,align:'center' }
							],
    	 */
    	
    	/****************************************************************************************************
    	 * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
    	 ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("부서명","DEPT_NM", "center"));
    	excelInfoList.add(new ExcelVO("업소번호", "CO_NO", "center"));
    	excelInfoList.add(new ExcelVO("업소명", "CO_NM", "left"));
		excelInfoList.add(new ExcelVO("주소", "ADDR", "left"));
		excelInfoList.add(new ExcelVO("검사종류", "INSP_NM", "center"));
		excelInfoList.add(new ExcelVO("검사예정일", "REAL_INSP_DATE", "center"));
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.entSupportAct.getExcelList", searchMap));
    	
    	return searchMap;
    }
}
