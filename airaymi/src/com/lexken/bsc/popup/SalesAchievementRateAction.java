/*************************************************************************
* CLASS 명      : SalesAchievementRateAction
* 작 업 자      : 김상용
* 작 업 일      : 2013년 8월 23일
* 기    능      : 매출액 달성률 시스템항목관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    김상용      2013년 8월 23일		시스템항목관리            최 초 작 업
**************************************************************************/
package com.lexken.bsc.popup;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.util.StaticUtil;

public class SalesAchievementRateAction extends CommonService {
	
private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 시스템 연계데이터 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap salesAchievementRateList(SearchMap searchMap) {
    	
    	if ("".equals(StaticUtil.nullToBlank((String)searchMap.get("findDeptId")))) {
			searchMap.put("findDeptId", getStr("bsc.popup.salesAchievementRate.getScDeptMapping", searchMap));
		}
    	
    	searchMap.addList("deptList", getList("bsc.popup.salesAchievementRate.deptList", searchMap));
    	
    	searchMap.addList("itemInfo", getList("bsc.popup.salesAchievementRate.getItem", searchMap));

        return searchMap;
    }
    
    /**
     * 시스템 연계데이터 제도개선 조회 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap salesAchievementRateList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.popup.salesAchievementRate.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 시스템 연계데이터 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap salesAchievementRateProcess(SearchMap searchMap) {
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
    	String excelFileName = "매출액 달성율";
    	String excelTitle = "매출액 달성율";
    	
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
		excelInfoList.add(new ExcelVO("부서코드", "DEPT_NM", "left"));
		excelInfoList.add(new ExcelVO("사업분류", "RPT_TC_NM", "center"));
		excelInfoList.add(new ExcelVO("업무구분", "ROLE_TC_NM", "center"));
		excelInfoList.add(new ExcelVO("검사종류", "INSP_KIND_TC_NM", "center"));
		excelInfoList.add(new ExcelVO("법구분", "LAW_TC_NM", "center"));
		excelInfoList.add(new ExcelVO("대분류", "STIC_LAG_TC_NM", "center"));
		excelInfoList.add(new ExcelVO("중분류", "STIC_MID_TC_NM", "center"));
		excelInfoList.add(new ExcelVO("소분류", "STIC_SML_TC_NM", "center"));
		excelInfoList.add(new ExcelVO("신청수량(누계신규)", "CUM_REQ_QTY", "center"));
		excelInfoList.add(new ExcelVO("신청금액(누계신규)", "CUM_REQ_AMT", "center"));
		excelInfoList.add(new ExcelVO("신청수량(누계재검)", "CUM_RE_REQ_QTY", "center"));
		excelInfoList.add(new ExcelVO("신청금액(누계재검)", "CUM_RE_REQ_AMT", "center"));
    	 
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	
    	
    		searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.salesAchievementRate.getList", searchMap));
    		
    	return searchMap;
    }
    
}
