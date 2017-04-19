/*************************************************************************
* CLASS 명      : RegularInspAction
* 작 업 자      : 김기현
* 작 업 일      : 2012년 6월 26일 
* 기    능      : 시스템 연계데이터 조회
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    김기현      2012년 6월 26일             최 초 작 업 
**************************************************************************/
package com.lexken.bsc.popup;
    
import java.util.ArrayList;
import java.util.HashMap;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.StringConstants;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.struts2.IsBoxActionSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.util.StaticUtil;

public class RegularInspAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 시스템 연계데이터 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap regularInspList(SearchMap searchMap) {
    	
    	if ("".equals(StaticUtil.nullToBlank((String)searchMap.get("findDeptId")))) {
			searchMap.put("findDeptId", getStr("bsc.popup.regularInsp.getScDeptMapping", searchMap));
		}
    	
    	//최상위 평가조직 조회
        searchMap.addList("deptInfo", getList("bsc.popup.regularInsp.getDeptInfo", searchMap));
        
        searchMap.addList("itemInfo", getList("bsc.popup.regularInsp.getItem", searchMap));

        return searchMap;
    }
    
    /**
     * 시스템 연계데이터 조회 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap regularInspList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.popup.regularInsp.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 시스템 연계데이터 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap regularInspProcess(SearchMap searchMap) {
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
    	String excelFileName = "가스사용시설정기검사처리율";
    	String excelTitle = "가스사용시설정기검사처리율";
    	
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
    	excelInfoList.add(new ExcelVO("부서명","DEPT_NM", "center"));
    	excelInfoList.add(new ExcelVO("허가구분","PERM_TC_NM", "center"));
    	excelInfoList.add(new ExcelVO("업소번호", "CO_NO", "center"));
    	excelInfoList.add(new ExcelVO("시설명", "CO_NM", "left"));
    	excelInfoList.add(new ExcelVO("주소","ADDR", "left"));
    	excelInfoList.add(new ExcelVO("검사종류", "INSP_NM", "center"));
    	excelInfoList.add(new ExcelVO("검사계획일자", "INSP_PLAN_DATE", "center"));
    	excelInfoList.add(new ExcelVO("검사실행일자", "REAL_INSP_DATE", "center"));
    	excelInfoList.add(new ExcelVO("비고", "PROC_NM", "left"));

    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.regularInsp.getList", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 시스템 연계데이터 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap regularInspList2(SearchMap searchMap) {
    	
    	if ("".equals(StaticUtil.nullToBlank((String)searchMap.get("findDeptId")))) {
			searchMap.put("findDeptId", getStr("bsc.popup.regularInsp.getScDeptMapping", searchMap));
		}
    	
    	//최상위 평가조직 조회
        searchMap.addList("deptInfo", getList("bsc.popup.regularInsp.getDeptInfo", searchMap));
        
        searchMap.addList("itemInfo", getList("bsc.popup.regularInsp.getItem", searchMap));

        return searchMap;
    }
    
    /**
     * 시스템 연계데이터 조회 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap regularInspList2_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.popup.regularInsp.getList2", searchMap));

        return searchMap;
    }
    
    /**
     * 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap excel2(SearchMap searchMap) {
    	String excelFileName = "가스사용시설정기검사처리율";
    	String excelTitle = "가스사용시설정기검사처리율";
    	
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
    	excelInfoList.add(new ExcelVO("부서명","DEPT_NM", "center"));
    	excelInfoList.add(new ExcelVO("허가구분","PERM_TC_NM", "center"));
    	excelInfoList.add(new ExcelVO("업소번호", "CO_NO", "center"));
    	excelInfoList.add(new ExcelVO("시설명", "CO_NM", "left"));
    	excelInfoList.add(new ExcelVO("주소","ADDR", "left"));
    	excelInfoList.add(new ExcelVO("검사종류", "INSP_NM", "center"));
    	excelInfoList.add(new ExcelVO("검사계획일자", "INSP_PLAN_DATE", "center"));
    	excelInfoList.add(new ExcelVO("검사실행일자", "REAL_INSP_DATE", "center"));
    	excelInfoList.add(new ExcelVO("비고", "PROC_NM", "left"));

    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.regularInsp.getList2", searchMap));
    	
    	return searchMap;
    }
    
}
