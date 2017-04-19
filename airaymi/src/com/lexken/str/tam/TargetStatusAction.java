/*************************************************************************
* CLASS 명      : TargetStatusAction
* 작 업 자      : 박경태
* 작 업 일      : 2012년 10월 30일 
* 기    능      : 목표입력/승인현황
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    박경태      2012년 10월 30일         최 초 작 업 
**************************************************************************/
package com.lexken.str.tam;
    
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

public class TargetStatusAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 목표입력/승인현황 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap targetStatusList(SearchMap searchMap) {
    	String gubun = (String)searchMap.get("gubun");
    	
    	if("".equals(StaticUtil.nullToBlank(gubun))) {
    		searchMap.put("gubun", "01"); //입력현황
    	}
	
        return searchMap;
    }
    
    /**
     * 목표입력현황 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap targetStatusInsertList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("str.tam.targetStatus.getInsertList", searchMap));

        return searchMap;
    }
    
    /**
     * 목표승인현황 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap targetStatusApproveList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("str.tam.targetStatus.getApproveList", searchMap));

        return searchMap;
    }
    
    /**
     * 목표입력현황 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap targetInsertStatusListExcel(SearchMap searchMap) {
    	String excelFileName = "목표입력현황";
    	String excelTitle = "목표입력현황 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("처리상태", (String)searchMap.get("statusNm")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("입력담당자", "CHARGE_USER_NM", "left"));
    	excelInfoList.add(new ExcelVO("담당부서", "CHARGE_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("전체", "TOT_CNT", "left"));
    	excelInfoList.add(new ExcelVO("미입력", "CNT01", "left"));
    	excelInfoList.add(new ExcelVO("입력완료", "CNT02", "left"));
    	excelInfoList.add(new ExcelVO("승인요청", "CNT03", "left"));
    	excelInfoList.add(new ExcelVO("승인", "CNT04", "left"));
    	excelInfoList.add(new ExcelVO("반려", "CNT05", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("str.tam.targetStatus.getInsertList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 목표승인현황 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap targetApproveStatusListExcel(SearchMap searchMap) {
    	String excelFileName = "목표승인현황";
    	String excelTitle = "목표승인현황 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("처리상태", (String)searchMap.get("statusNm")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("승인담당자", "APPROVE_USER_NM", "left"));
    	excelInfoList.add(new ExcelVO("승인부서", "APPROVE_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("전체", "TOT_CNT", "left"));
    	excelInfoList.add(new ExcelVO("미입력", "CNT01", "left"));
    	excelInfoList.add(new ExcelVO("입력완료", "CNT02", "left"));
    	excelInfoList.add(new ExcelVO("승인요청", "CNT03", "left"));
    	excelInfoList.add(new ExcelVO("승인", "CNT04", "left"));
    	excelInfoList.add(new ExcelVO("반려", "CNT05", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("str.tam.targetStatus.getApproveList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 실적입력승인현황 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap targetStatusProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("CALL".equals(stMode)) {
            searchMap = callDB(searchMap);
        }        
            
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 데이터집계관리 프로시저 실행
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap callDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        	
        	returnMap = insertData("str.tam.targetStatus.callSpTargetProc", searchMap);
        	
        } catch (Exception e) {
        	logger.error(e.toString());
        	setRollBackTransaction();
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        	setEndTransaction();
        }
        
        /**********************************
         * Return
         **********************************/
        searchMap.addList("returnMap", returnMap);
        return searchMap;    
    }
    
}
