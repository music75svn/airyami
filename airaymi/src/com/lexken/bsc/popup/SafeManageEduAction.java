/*************************************************************************
* CLASS 명      : safeManageEdu
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

public class SafeManageEduAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 시스템항목관리 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap safeManageEduList(SearchMap searchMap) {

    	if ("".equals(StaticUtil.nullToBlank((String)searchMap.get("findDeptId")))) {
			searchMap.put("findDeptId", getStr("bsc.popup.safeManageEdu.getDept", searchMap));
		}

    	searchMap.addList("deptList", getList("bsc.popup.safeManageEdu.getDeptList", searchMap));

    	searchMap.addList("itemInfo", getList("bsc.popup.safeManageEdu.getItem", searchMap));

        return searchMap;
    }

    /**
     * 시스템항목관리 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap safeManageEduList_xml(SearchMap searchMap) {

    	if("S021D001".equals(searchMap.get("findItemCd"))){
    		searchMap.addList("list", getList("bsc.popup.safeManageEdu.getList", searchMap));
    	}else{
    		searchMap.addList("list", getList("bsc.popup.safeManageEdu.getList1", searchMap));
    	}
        return searchMap;
    }

    /**
     * 시스템 연계데이터 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap planCheckSettleProProcess(SearchMap searchMap) {
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
    	String excelFileName = "안전관리교육실적";
    	String excelTitle = "안전관리교육실적";

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
    	excelInfoList.add(new ExcelVO("부서명", "DEPT_NM", "left"));

    	if (searchMap.get("findItemCd").equals("S021D001")) {	// 집행액
    		excelInfoList.add(new ExcelVO("교육일자", "EDU_DATE", "center"));
    		excelInfoList.add(new ExcelVO("교육", "EDU", "center"));
    		excelInfoList.add(new ExcelVO("교육장소", "EDU_SITE", "center"));
    		excelInfoList.add(new ExcelVO("교육내용", "RMK", "center"));
    		excelInfoList.add(new ExcelVO("교육인원", "ACTUAL", "center"));
    	} else {												// 배정액
    		excelInfoList.add(new ExcelVO("상세교육", "EDU", "center"));
    		excelInfoList.add(new ExcelVO("대분류", "STIC_LAG_TC_NM", "center"));
    		excelInfoList.add(new ExcelVO("교육인원", "ACTUAL", "center"));
    	}

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);

    	if (searchMap.get("findItemCd").equals("S021D001")) {
    		searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.safeManageEdu.getList", searchMap));
    	} else {
    		searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.safeManageEdu.getList1", searchMap));
    	}

    	return searchMap;
    }

}
