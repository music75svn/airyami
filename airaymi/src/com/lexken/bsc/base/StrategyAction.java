/*************************************************************************
* CLASS 명      : StrategyAction
* 작 업 자      : 박경태
* 작 업 일      : 2012년 11월 13일 
* 기    능      : 전략방향
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    박경태      2012년 11월 13일         최 초 작 업 
**************************************************************************/
package com.lexken.bsc.base;
    
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

public class StrategyAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 전략방향 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap strategyList(SearchMap searchMap) {
    	
        return searchMap;
    }
    
    /**
     * 전략방향 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap strategyList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.base.strategy.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 전략방향 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap strategyModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.base.strategy.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 전략방향 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap strategyProcess(SearchMap searchMap) {
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
     * 전략방향 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("bsc.base.strategy.insertData", searchMap);
        
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
     * 전략방향 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("bsc.base.strategy.updateData", searchMap);
	        
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
     * 전략방향 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] strategyIds = searchMap.getString("strategyIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != strategyIds && 0 < strategyIds.length) {
		        for (int i = 0; i < strategyIds.length; i++) {
		            searchMap.put("strategyId", strategyIds[i]);
		            returnMap = updateData("bsc.base.strategy.deleteData", searchMap);
		        }
		    }
	        
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
     *  Validation 체크(무결성 체크)
     * @param SearchMap
     * @return HashMap
     */
    private HashMap validChk(SearchMap searchMap) {
        HashMap returnMap         = new HashMap();
        int     resultValue        = 0;
        
		returnMap = ValidationChk.lengthCheck(searchMap.getString("strategyNm"), "전략과제", 1, 150);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
		
		returnMap = ValidationChk.onlyNumber(searchMap.getString("sortOrder"), "정렬순서");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}


        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }

    /**
     * 전략방향 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap strategyListExcel(SearchMap searchMap) {
    	String excelFileName = "전략과제";
    	String excelTitle = "전략과제 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("사용여부", (String)searchMap.get("useYnNm")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("전략과제코드", "STRATEGY_ID", "left"));
    	excelInfoList.add(new ExcelVO("전략과제", "STRATEGY_NM", "left"));
    	excelInfoList.add(new ExcelVO("정렬순서", "SORT_ORDER", "right"));
    	
    	if(!"".equals(StaticUtil.nullToBlank((String)searchMap.get("useYn")))) {
			searchMap.put("findUseYn", (String)searchMap.get("useYn"));
		}
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.base.strategy.getList", searchMap));
    	
        return searchMap;
    }
    
}
