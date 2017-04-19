/*************************************************************************
* CLASS 명      : busManageRation
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

public class BusManageRationAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 시스템항목관리 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap busManageRationList(SearchMap searchMap) {

    	if ("".equals(StaticUtil.nullToBlank((String)searchMap.get("findDeptId")))) {
			searchMap.put("findDeptId", getStr("bsc.popup.busManageRation.getScDeptMapping", searchMap));
		}

    	//최상위 평가조직 조회
        searchMap.addList("deptInfo", getList("bsc.popup.busManageRation.getDeptInfo", searchMap));

        searchMap.addList("itemInfo", getList("bsc.popup.busManageRation.getItem", searchMap));

        return searchMap;
    }

    /**
     * 시스템항목관리 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap busManageRationList_xml(SearchMap searchMap) {

    		searchMap.addList("list", getList("bsc.popup.busManageRation.getList", searchMap));

        return searchMap;
    }
    /**
     * 시스템항목관리 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap busManageRationList1_xml(SearchMap searchMap) {

    		searchMap.addList("list", getList("bsc.popup.busManageRation.getList1", searchMap));

        return searchMap;
    }
    /**
     * 시스템항목관리 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap busManageRationList2_xml(SearchMap searchMap) {

    		searchMap.addList("list", getList("bsc.popup.busManageRation.getList2", searchMap));
        return searchMap;
    }
    /**
     * 시스템항목관리 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap busManageRationList3_xml(SearchMap searchMap) {

    		searchMap.addList("list", getList("bsc.popup.busManageRation.getList3", searchMap));
        return searchMap;
    }

    /**
     * 부서 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap deptList_ajax(SearchMap searchMap) {

    	searchMap.addList("deptList", getList("bsc.popup.busManageRation.getDeptList", searchMap));

    	if("".equals(searchMap.getString("findDeptId"))) {
        	searchMap.put("findDeptId", searchMap.getDefaultValue("deptList", "DEPT_ID", 0));
        }

        return searchMap;
    }

    /**
     * 시스템 연계데이터 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap busManageRationProcess(SearchMap searchMap) {
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
    	String excelFileName = "경영관리합리성";
    	String excelTitle = "경영관리합리성";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
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
    	excelInfoList.add(new ExcelVO("부서명", "DEPT_NM", "left"));
    	
    	if (searchMap.get("findItemCd").equals("S020D001")) {	
    		excelInfoList.add(new ExcelVO("신청금액", "CUM_REQ_AMT", "center"));
    		excelInfoList.add(new ExcelVO("재검신청금액", "CUM_RE_REQ_AMT", "center"));
    		excelInfoList.add(new ExcelVO("이월금액", "L_YEAR_AMT", "center"));
    		excelInfoList.add(new ExcelVO("현재년월이월금액", "N_YEAR_AMT", "center"));
    	} else if(searchMap.get("findItemCd").equals("S020D002")){												
    		excelInfoList.add(new ExcelVO("집행액", "BGR_RSLT_QUAT", "center"));
    	} else if(searchMap.get("findItemCd").equals("S020D003")){												
    		excelInfoList.add(new ExcelVO("대분류", "STIC_LAG_TC_NM", "center"));
    		excelInfoList.add(new ExcelVO("소분류", "STIC_SML_TC_NM", "center"));
    		excelInfoList.add(new ExcelVO("목표", "PLAN_AMT", "center"));
    	} else {
    		excelInfoList.add(new ExcelVO("배정액", "APRV_QUAT", "center"));
    	}
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	
    	if (searchMap.get("findItemCd").equals("S020D001")) {
    		searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.busManageRation.getList", searchMap));
    	} else if(searchMap.get("findItemCd").equals("S020D002")) {
    		searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.busManageRation.getList1", searchMap));
    	} else if (searchMap.get("findItemCd").equals("S020D003")) {
    		searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.busManageRation.getList2", searchMap));
    	} else {
    		searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.busManageRation.getList3", searchMap));
    	}
    	return searchMap;
    }


}
