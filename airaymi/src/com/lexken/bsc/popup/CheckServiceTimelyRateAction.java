/*************************************************************************
* CLASS 명      : checkServiceTimelyRate
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

public class CheckServiceTimelyRateAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 시스템항목관리 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap checkServiceTimelyRateList(SearchMap searchMap) {
    	
    	if ("".equals(StaticUtil.nullToBlank((String)searchMap.get("findDeptId")))) {
    		searchMap.put("findDeptId", getStr("bsc.popup.checkServiceTimelyRate.getScDeptMapping", searchMap));
    	}

    	searchMap.addList("deptList", getList("bsc.popup.checkServiceTimelyRate.getDeptList", searchMap));

    	searchMap.addList("itemInfo", getList("bsc.popup.checkServiceTimelyRate.getItem", searchMap));

        return searchMap;
    }

    /**
     * 시스템항목관리 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap checkServiceTimelyRateList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("bsc.popup.checkServiceTimelyRate.getList", searchMap));

        return searchMap;
    }

    /**
     * 시스템 연계데이터 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap checkServiceTimelyRateProcess(SearchMap searchMap) {
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
    	String excelFileName = "검사서비스적시제공률";
    	String excelTitle = "검사서비스적시제공률";
    	
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
 //   	excelInfoList.add(new ExcelVO("부서명", "DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("업소번호", "CO_NO", "left"));
    	excelInfoList.add(new ExcelVO("업소명", "EQUI_NM", "left"));
    	excelInfoList.add(new ExcelVO("실적기간", "INSP_DATE", "left"));
    	excelInfoList.add(new ExcelVO("허가구분", "PERM_TC_NM", "left"));
    	excelInfoList.add(new ExcelVO("비고", "PROC_YN_NM", "center"));
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	

    	ArrayList list = (ArrayList)getList("bsc.popup.checkServiceTimelyRate.getList", searchMap);
    	
    	for (int i = 0; i < list.size(); i++) {
    		if (((HashMap)list.get(i)).get("PROC_YN").equals("T")) {
    			((HashMap)list.get(i)).put("PROC_YN_NM", "적기");
    		} else {
    			((HashMap)list.get(i)).put("PROC_YN_NM", "미적기");
    		}
    	}
    	
    	searchMap.put("excelDataList", list);
    	
    	return searchMap;
    }

}
