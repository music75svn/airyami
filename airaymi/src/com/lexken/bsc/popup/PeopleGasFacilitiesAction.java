/*************************************************************************
* CLASS 명      : peopleGasFacilities
* 작 업 자      : 안요한
* 작 업 일      : 2013년 08월 13일
* 기    능      : 서민층 가스시설 개선 실적
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    안요한      해외사업 수입            최 초 작 업
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

public class PeopleGasFacilitiesAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 서민층 가스시설 개선 실적 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap peopleGasFacilitiesList(SearchMap searchMap) {

    	if ("".equals(StaticUtil.nullToBlank((String)searchMap.get("findDeptId")))) {
			searchMap.put("findDeptId", getStr("bsc.popup.peopleGasFacilities.getScDeptMapping", searchMap));
		}

        searchMap.addList("deptInfo", getList("bsc.popup.peopleGasFacilities.getDeptInfo", searchMap));

    	searchMap.addList("itemInfo", getList("bsc.popup.peopleGasFacilities.getItem", searchMap));

        return searchMap;
    }

    /**
     * 서민층 가스시설 개선 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap peopleGasFacilitiesList_xml(SearchMap searchMap) {

    	String itemCd = searchMap.getString("findItemCd");

    	if("S049D001".equals(itemCd)) {

    		searchMap.addList("list", getList("bsc.popup.peopleGasFacilities.getList", searchMap));

    	} else {

    		searchMap.addList("list", getList("bsc.popup.peopleGasFacilities.getTargetList", searchMap));

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
    	String excelFileName = "서민층가스시설개선실적";
    	String excelTitle = "서민층가스시설개선실적";

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

    	if (searchMap.get("findItemCd").equals("S049D001")) {	// 가스시설개선실적
    		excelInfoList.add(new ExcelVO("월", "MON", "center"));
    	}
    	excelInfoList.add(new ExcelVO("부서명", "DEPT_NM", "left"));

    	if (searchMap.get("findItemCd").equals("S049D001")) {	// 가스시설개선실적
    		excelInfoList.add(new ExcelVO("가스시설개선실적", "RSLT_CNT", "center"));
    	} else {												// 가스시설개선목표(년)
    		excelInfoList.add(new ExcelVO("가스시설개선목표(년)", "PLAN_AMT", "center"));
    	}

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);

    	if (searchMap.get("findItemCd").equals("S049D001")) {	// 가스시설개선실적
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.peopleGasFacilities.getList", searchMap));
    	} else {
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.peopleGasFacilities.getTargetList", searchMap));
    	}
    	return searchMap;
    }
    
    /**
     * 안전장치 보급(타이머콕,다기능계량기)  실적 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap peopleGasFacilitiesList2(SearchMap searchMap) {

    	if ("".equals(StaticUtil.nullToBlank((String)searchMap.get("findDeptId")))) {
			searchMap.put("findDeptId", getStr("bsc.popup.peopleGasFacilities.getScDeptMapping", searchMap));
		}

        searchMap.addList("deptInfo", getList("bsc.popup.peopleGasFacilities.getDeptInfo", searchMap));

    	searchMap.addList("itemInfo", getList("bsc.popup.peopleGasFacilities.getItem", searchMap));

        return searchMap;
    }

    /**
     * 안전장치 보급(타이머콕,다기능계량기)  데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap peopleGasFacilitiesList2_xml(SearchMap searchMap) {

    	String itemCd = searchMap.getString("findItemCd");

    		searchMap.addList("list", getList("bsc.popup.peopleGasFacilities.getList2", searchMap));

    	
        return searchMap;
    }


    /**
     * 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap excel2(SearchMap searchMap) {
    	String excelFileName = "안전장치 보급(타이머콕,다기능계량기) 실적";
    	String excelTitle = "안전장치 보급(타이머콕,다기능계량기) 실적";

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

    	if (searchMap.get("findItemCd").equals("S049D001")) {	// 가스시설개선실적
    		excelInfoList.add(new ExcelVO("월", "MON", "center"));
    	}
    	excelInfoList.add(new ExcelVO("부서명", "DEPT_NM", "left"));

		excelInfoList.add(new ExcelVO("안전장치 보급(타이머콕,다기능계량기) 실적", "RSLT_CNT", "center"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);

    	searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.peopleGasFacilities.getList2", searchMap));
    	return searchMap;
    }
    
}
