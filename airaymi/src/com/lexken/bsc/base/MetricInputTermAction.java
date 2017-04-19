/*************************************************************************
* CLASS 명      : MetricInputTermAction
* 작 업 자      : 김윤기
* 작 업 일      : 2012년 8월 8일 
* 기    능      : KPI입력일정관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  --------  -----------------  -------------------------    --------
*   1    김윤기      2012년 8월 8일             최 초 작 업 
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

public class MetricInputTermAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * KPI입력일정관리 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricInputTermList(SearchMap searchMap) {

        return searchMap;
    }
    
    /**
     * KPI입력일정관리 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricInputTermList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.base.metricInputTerm.getList", searchMap));

        return searchMap;
    }
    
    /**
     * KPI입력일정관리 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap metricInputTermListExcel(SearchMap searchMap) {
    	String excelFileName = "KPI입력일정관리";
    	String excelTitle = "KPI입력일정관리 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	/*
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("공통코드명", StaticUtil.nullToDefault((String)searchMap.get("codeGrpNm"), "전체")));
    	excelSearchInfoList.add(new ExcelVO("사용여부", (String)searchMap.get("useYnNm")));
    	*/
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가년도", "YEAR"));
    	excelInfoList.add(new ExcelVO("입력시작일", "START_DT"));
    	excelInfoList.add(new ExcelVO("입력종료일", "END_DT"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.base.metricInputTerm.getList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * KPI입력일정관리 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricInputTermModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("stMode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.base.metricInputTerm.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * KPI입력일정관리 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricInputTermProcess(SearchMap searchMap) {
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
     * KPI입력일정관리 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        	
        	/**********************************
	         * 해당 월 등록여부 확인
	         **********************************/
	        int cnt = getInt("bsc.base.metricInputTerm.getCount", searchMap);
	        
	        if(0 < cnt) {
	            returnMap.put("ErrorNumber", ErrorMessages.FAILURE_DUP2_CODE);
	            returnMap.put("ErrorMessage", ErrorMessages.format(ErrorMessages.FAILURE_DUP2_MESSAGE, "입력 년도"));
	        } else {
	        	returnMap = insertData("bsc.base.metricInputTerm.insertData", searchMap);
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
     * KPI입력일정관리 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("bsc.base.metricInputTerm.updateData", searchMap);
	        
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
     * KPI입력일정관리 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
        
	    try {
	        String[] years = searchMap.getString("years").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        for (int i = 0; i < years.length; i++) {
	            searchMap.put("year", years[i]);
	            returnMap = updateData("bsc.base.metricInputTerm.deleteData", searchMap);
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
        
        //Validation 체크 추가
        returnMap = ValidationChk.emptyCheck(searchMap.getString("year"), "입력년도");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("startDt"), "입력시작일", 1, 8);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
		
		returnMap = ValidationChk.lengthCheck(searchMap.getString("endDt"), "입력종료일", 1, 8);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
		
		if( Integer.parseInt(searchMap.getString("startDt"))  > Integer.parseInt(searchMap.getString("endDt")) ) {
			returnMap.put("ErrorNumber",  -1);
			returnMap.put("ErrorMessage", "입력시작일은 입력종료일보다 빠를수 없습니다.");
			
			return returnMap;
		}
        
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }
    
    
}
