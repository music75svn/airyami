/*************************************************************************
* CLASS 명      : ProductivityBranchAction
* 작 업 자      : 김상용
* 작 업 일      : 2013년 8월 14일 
* 기    능      : 시스템 연계데이터 조회
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    김상용      2013년 8월 14일             최 초 작 업 
**************************************************************************/
package com.lexken.bsc.popup;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.util.StaticUtil;

public class ProductivityBranchAction extends CommonService {
    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 업무생산성(구노동생산성) 연계데이터 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap productivityBranchList(SearchMap searchMap) {
    	
    	if ("".equals(StaticUtil.nullToBlank((String)searchMap.get("findDeptId")))) {
			searchMap.put("findDeptId", getStr("bsc.popup.productivityBranch.getScDeptMapping", searchMap));
		}
    	
    	//최상위 평가조직 조회
        searchMap.addList("deptInfo", getList("bsc.popup.productivityBranch.getDeptInfo", searchMap));
        
        searchMap.addList("itemInfo", getList("bsc.popup.productivityBranch.getItem", searchMap));

        return searchMap;
    }
    
    /**
     * 업무생산성(구노동생산성) 연계데이터 조회 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap productivityBranchList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.popup.productivityBranch.getList", searchMap));

        return searchMap;
    }

    /**
     * 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap excel(SearchMap searchMap) {
    	String excelFileName = "업무생산성(구노동생산성)";
    	String excelTitle = "업무생산성(구노동생산성)";
    	
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
    	
    	if (searchMap.get("findItemCd").equals("S010D001")) {
    		excelInfoList.add(new ExcelVO("년도","YEAR", "center"));
        	excelInfoList.add(new ExcelVO("월", "MON", "center"));
        	excelInfoList.add(new ExcelVO("부서명", "DEPT_NM", "left"));
        	excelInfoList.add(new ExcelVO("업무구분", "ROLE_TC_NM", "center"));
        	excelInfoList.add(new ExcelVO("검사구분", "INSP_KIND_TC_NM", "center"));
        	excelInfoList.add(new ExcelVO("가스법구분", "LAW_TC_NM", "center"));
        	excelInfoList.add(new ExcelVO("대분류", "STIC_LAG_TC_NM", "left"));
        	excelInfoList.add(new ExcelVO("중분류", "STIC_MID_TC_NM", "left"));
        	excelInfoList.add(new ExcelVO("소분류", "STIC_SML_TC_NM", "left"));
        	excelInfoList.add(new ExcelVO("신규검사수", "INSP_QTY", "left"));
        	excelInfoList.add(new ExcelVO("재검수", "RE_INSP_QTY", "left"));
        	excelInfoList.add(new ExcelVO("배관검사길이", "LENG", "left"));
        	excelInfoList.add(new ExcelVO("적용건수", "TOTAL", "left"));
        	excelInfoList.add(new ExcelVO("상대값", "RLTV_VAL", "left"));
        	excelInfoList.add(new ExcelVO("상대값의합", "TOTAL_RLTV_VAL", "left"));
        	
    	} else {
			excelInfoList.add(new ExcelVO("년도","YEAR", "center"));
	    	excelInfoList.add(new ExcelVO("월", "MON", "center"));
	    	excelInfoList.add(new ExcelVO("부서명", "DEPT_NM", "left"));
	    	excelInfoList.add(new ExcelVO("직급", "CAST_TC_NM", "center"));
	    	excelInfoList.add(new ExcelVO("사번", "EMPN", "center"));
	    	excelInfoList.add(new ExcelVO("성명", "KOR_NM", "center"));
	    	excelInfoList.add(new ExcelVO("직위", "POS_TC_NM", "center"));
	    	excelInfoList.add(new ExcelVO("기술인력수", "TOTAL_CNT", "center"));
    	}

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.productivityBranch.getList", searchMap));
    	
    	return searchMap;
    }
}
