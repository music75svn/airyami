/*************************************************************************
* CLASS 명      : GovActualStatusAction
* 작 업 자      : 하윤식
* 작 업 일      : 2012년 11월 13일 
* 기    능      : 정평 계량지표 실적입력현황
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    하윤식      2012년 11월 13일         최 초 작 업 
**************************************************************************/
package com.lexken.gov.measure;
    
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

public class GovActualStatusAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 정평 계량지표 실적입력현황 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap govActualStatusList(SearchMap searchMap) {
    	
    	String gubun = (String)searchMap.get("gubun");
    	
    	if("".equals(StaticUtil.nullToBlank(gubun))) {
    		searchMap.put("gubun", "01"); //입력현황
    	}

        return searchMap;
    }
    
    /**
     * 정평 계량지표 실적입력현황 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap govActualStatusList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("gov.measure.govActualStatus.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 정평 계량지표 실적입력현황 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap govActualStatus2List_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("gov.measure.govActualStatus.getSubCalList", searchMap));

        return searchMap;
    }
    
    /**
     * 정평 계량지표 실적입력현황 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap govActualStatusModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("gov.measure.govActualStatus.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 정평 계량지표 실적입력현황 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap govActualStatusProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("ADD".equals(stMode)) {
            searchMap = insertDB(searchMap);
        } else if("MOD".equals(stMode)) {
            searchMap = updateDB(searchMap);
        } 
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 정평 계량지표 실적입력현황 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("gov.measure.govActualStatus.insertData", searchMap);
        
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
    
    /**
     * 정평 계량지표 실적입력현황 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("gov.measure.govActualStatus.updateData", searchMap);
	        
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
    
   

    /**
     * 정평 계량지표 실적입력현황 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap govActualStatusListExcel(SearchMap searchMap) {
    	String excelFileName = "정평 계량지표 실적입력현황";
    	String excelTitle = "정평 계량지표 실적입력현황";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("기준월", (String)searchMap.get("monNm")));
    	excelSearchInfoList.add(new ExcelVO("처리상태", (String)searchMap.get("statusNm")));

    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("사번", "INSERT_USER_ID", "left"));
    	excelInfoList.add(new ExcelVO("입력담당자", "INSERT_USER_NM", "left"));
    	excelInfoList.add(new ExcelVO("부서명", "DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("전체", "TOT_CNT", "center"));
    	excelInfoList.add(new ExcelVO("미입력", "CNT_01", "center"));
    	excelInfoList.add(new ExcelVO("입력", "CNT_02", "center"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("gov.measure.govActualStatus.getList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 정평 계량지표 실적입력현황 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap govActualStatusList2Excel(SearchMap searchMap) {
    	String excelFileName = "정부경영평가 세부요소 입력현황";
    	String excelTitle = "정부경영평가 세부요소 입력현황 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("기준월", (String)searchMap.get("monNm")));
    	excelSearchInfoList.add(new ExcelVO("처리상태", (String)searchMap.get("statusNm")));

    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("사번", "INSERT_USER_ID", "left"));
    	excelInfoList.add(new ExcelVO("입력담당자", "INSERT_USER_NM", "left"));
    	excelInfoList.add(new ExcelVO("부서명", "DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("전체", "TOT_CNT", "center"));
    	excelInfoList.add(new ExcelVO("미입력", "CNT_01", "center"));
    	excelInfoList.add(new ExcelVO("입력", "CNT_02", "center"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("gov.measure.govActualStatus.getSubCalList", searchMap));
    	
        return searchMap;
    }
}
