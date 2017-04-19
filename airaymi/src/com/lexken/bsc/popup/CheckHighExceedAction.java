/*************************************************************************
* CLASS 명      : CheckHighExceedAction
* 작 업 자      : 김기현
* 작 업 일      : 2013년 8월 12일 
* 기    능      : 시스템 연계데이터 조회
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    김기현      2013년 8월 12일             최 초 작 업 
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

public class CheckHighExceedAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 시스템 연계데이터 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap checkHighExceedList(SearchMap searchMap) {
    	
    	if ("".equals(StaticUtil.nullToBlank((String)searchMap.get("findDeptId")))) {
    		searchMap.put("findDeptId", getStr("bsc.popup.checkHighExceed.getScDeptMapping", searchMap));
    	}
    	
    	searchMap.addList("deptList", getList("bsc.popup.checkHighExceed.getDeptList", searchMap));
    	
    	searchMap.addList("itemInfo", getList("bsc.popup.checkHighExceed.getItem", searchMap));

        return searchMap;
    }
    
    /**
     * 시스템 연계데이터 조회 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap checkHighExceedList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.popup.checkHighExceed.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 시스템 연계데이터 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap checkHighExceedProcess(SearchMap searchMap) {
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
    	String excelFileName = "계획검사정착률(검사처리상한초과건수)";
    	String excelTitle = "계획검사정착률(검사처리상한초과건수)";
    	
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
    	excelInfoList.add(new ExcelVO("년도", 			"YEAR", 		"center"));
    	excelInfoList.add(new ExcelVO("월", 				"MON", 			"center"));
    	excelInfoList.add(new ExcelVO("부서", 			"BR_NM", 		"center"));
    	excelInfoList.add(new ExcelVO("이름", 			"EMPN_NM", 		"center"));
    	excelInfoList.add(new ExcelVO("검사처리건수", 		"TOT_NVAL", 	"center"));
    	excelInfoList.add(new ExcelVO("검사처리상한초과수", 	"OVER_CNT", 	"center"));
    	excelInfoList.add(new ExcelVO("검사일", 			"INSP_DATE", 	"center"));
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	
    	String deptId = searchMap.getString("deptId");
    	searchMap.put("findDeptId", deptId);
    	
    	ArrayList list = (ArrayList)getList("bsc.popup.checkHighExceed.getList", searchMap);
    	
    	searchMap.put("excelDataList", list);
    	
    	return searchMap;
    }
    
    
}
