/*************************************************************************
* CLASS 명      : ImponEvalStatusAction
* 작 업 자      : 김윤기
* 작 업 일      : 2012년 8월 13일 
* 기    능      : 비계량평가진행현황
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  --------  -----------------  -------------------------    --------
*   1    김윤기      2012년 8월 13일             최 초 작 업 
**************************************************************************/
package com.lexken.bsc.eval;
    
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

public class ImponEvalStatusAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 비계량평가진행현황 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap imponEvalStatusList(SearchMap searchMap) {

    	String findCycle = (String)searchMap.get("findCycle");
    	searchMap.addList("evalCycle", getList("bsc.eval.imponEvalGrp.getEvalCycle", searchMap));

    	//디폴트 조회조건 설정(findCycle)
    	if("".equals(StaticUtil.nullToBlank(findCycle))) {
    		searchMap.put("findCycle", searchMap.getDefaultValue("evalCycle", "CODE_ID", 0));
    	}

    	searchMap.addList("summary", getDetail("bsc.eval.imponEvalStatus.getSummary", searchMap));	
    	
        return searchMap;
    }
    
    /**
     * 비계량평가진행현황 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap imponEvalStatusList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.eval.imponEvalStatus.getList", searchMap));	
        return searchMap;
    }
    
    /**
     * 비계량평가진행현황 상세 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap imponEvalStatusModify_xml(SearchMap searchMap) {
    	
    	searchMap.addList("list", getList("bsc.eval.imponEvalStatus.getDetail", searchMap));	
    	return searchMap;
    }
    
    /**
     * 비계량평가진행현황 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap imponEvalStatusListExcel(SearchMap searchMap) {
    	String excelFileName = "비계량평가진행현황";
    	String excelTitle = "비계량평가진행현황 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("평가구분", (String)searchMap.get("cycleNm")));
    	excelSearchInfoList.add(new ExcelVO("완료여부", (String)searchMap.get("endYnNm")));
    	
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("부서명", "DEPT_NM", "center", "CNT"));
    	excelInfoList.add(new ExcelVO("평가자사번", "EVAL_EMP_ID", "center"));
    	excelInfoList.add(new ExcelVO("평가자명", "NAME_HAN", "center"));
    	excelInfoList.add(new ExcelVO("대상KPI", "TOT_CNT", "center"));
    	excelInfoList.add(new ExcelVO("평가KPI", "EVAL_CNT", "center"));
    	excelInfoList.add(new ExcelVO("완료여부", "END_YN", "center"));
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.eval.imponEvalStatus.getList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 비계량평가진행현황 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap imponEvalStatusModify(SearchMap searchMap) {
        return searchMap;
    }
    
    /**
     * 비계량평가진행현황 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap imponEvalStatusProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 무결성 체크
         **********************************/
        if(!"DEL".equals(stMode)) {
            returnMap = this.validChk(searchMap);
            
            if((Integer)returnMap.get("ErrorNumber") < 0 ){
                searchMap.addList("returnMap", returnMap);
                return searchMap;
            }
        }
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("ADD".equals(stMode)) {
            searchMap = insertDB(searchMap);
        } else if("MOD".equals(stMode)) {
            searchMap = updateDB(searchMap);
        } else if("DEL".equals(stMode)) {
            searchMap = deleteDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 비계량평가진행현황 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("bsc.eval.imponEvalStatus.insertData", searchMap);
        
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
     * 비계량평가진행현황 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("bsc.eval.imponEvalStatus.updateData", searchMap);
	        
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
     * 비계량평가진행현황 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    /*
	    try {
	        String[] imponEvalStatusIds = searchMap.getString("imponEvalStatusIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        for (int i = 0; i < imponEvalStatusIds.length; i++) {
	            searchMap.put("imponEvalStatusId", imponEvalStatusIds[i]);
	            returnMap = updateData("bsc.eval.imponEvalStatus.deleteData", searchMap);
	        }
	        
	    } catch (Exception e) {
        	logger.error(e.toString());
        	setRollBackTransaction();
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        	setEndTransaction();
        }
	    */   
        
        /**********************************
         * Return
         **********************************/
        searchMap.addList("returnMap", returnMap);
        return searchMap;    
    }

    /**
     *  Validation 체크(무결성 체크)
     * @param SearchMap
     * @return HashMap
     */
    private HashMap validChk(SearchMap searchMap) {
        HashMap returnMap         = new HashMap();
        int     resultValue        = 0;
        
        //Validation 체크 추가
        
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }
    
    
}
