/*************************************************************************
* CLASS 명      : CommissionEducation
* 작 업 자      : 안요한
* 작 업 일      : 2013년 8월 14일
* 기    능      : 안전관리교육실적(가중치) 조회
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    안요한      2013년 8월 14일             최 초 작 업
**************************************************************************/
package com.lexken.bsc.popup;

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

public class CommissionEducationAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 안전관리교육 실적(가중치합) 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap commissionEducationList(SearchMap searchMap) {

    	if ("".equals(StaticUtil.nullToBlank((String)searchMap.get("findDeptId")))) {
			searchMap.put("findDeptId", getStr("bsc.popup.commissionEducation.getScDeptMapping", searchMap));
		}

        searchMap.addList("deptInfo", getList("bsc.popup.commissionEducation.getDeptInfo", searchMap));

        searchMap.addList("itemInfo", getList("bsc.popup.commissionEducation.getItem", searchMap));

        return searchMap;
    }

    /**
     * 안전관리교육 실적(가중치합) 조회 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap commissionEducationList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("bsc.popup.commissionEducation.getList", searchMap));

        return searchMap;
    }

    /**
     * 안전관리교육실적(가중치합) 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap commissionEducationProcess(SearchMap searchMap) {
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
    	String excelFileName = "위탁교육실적";
    	String excelTitle = "위탁교육실적";

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
    	excelInfoList.add(new ExcelVO("구분", "INSP_KIND_TC_NM", "left"));
    	excelInfoList.add(new ExcelVO("가스법 구분", "LAW_TC_NM", "left"));
    	excelInfoList.add(new ExcelVO("대분류", "STIC_LAG_TC_NM", "left"));
    	excelInfoList.add(new ExcelVO("소분류", "STIC_SML_TC_NM", "left"));
    	excelInfoList.add(new ExcelVO("실적", "INSP_QTY", "left"));


    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);

    	searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.commissionEducation.getList", searchMap));

    	return searchMap;
    }


}
