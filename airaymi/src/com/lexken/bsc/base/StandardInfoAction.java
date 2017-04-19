/*************************************************************************
* CLASS 명      : StandardInfoAction
* 작 업 자      : 하윤식
* 작 업 일      : 2012년 11월 28일 
* 기    능      : 평가기준
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    하윤식      2012년 11월 28일         최 초 작 업 
**************************************************************************/
package com.lexken.bsc.base;
    
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

public class StandardInfoAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 평가기준 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap standardInfoList(SearchMap searchMap) {

        return searchMap;
    }
    
    /**
     * 평가기준 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap standardInfoList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.base.standardInfo.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가기준 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap standardInfoModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.base.standardInfo.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 평가기준 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap standardInfoProcess(SearchMap searchMap) {
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
     * 평가기준 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	/**********************************
	         * 등록여부 확인
	         **********************************/
	        int cnt = getInt("bsc.base.standardInfo.getKeyCount", searchMap);
	        
	        if(0 < cnt) {
	            returnMap.put("ErrorNumber", ErrorMessages.FAILURE_DUP2_CODE);
	            returnMap.put("ErrorMessage", ErrorMessages.format(ErrorMessages.FAILURE_DUP2_MESSAGE, "평가년도"));
	        } else {
	        	returnMap = insertData("bsc.base.standardInfo.insertData", searchMap);
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
     * 평가기준 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("bsc.base.standardInfo.updateData", searchMap);
	        
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
     * 평가기준 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        setStartTransaction();
	        
	        returnMap = updateData("bsc.base.standardInfo.deleteData", searchMap);
	        
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
        
		returnMap = ValidationChk.lengthCheck(searchMap.getString("commMetricAdjustPer"), "공통지표 조정변별력 적용비율", 1, 5);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
		
		returnMap = ValidationChk.onlyNumber(searchMap.getString("commMetricAdjustPer"), "공통지표 조정변별력 적용비율");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.lengthCheck(searchMap.getString("commMetricAdjustPer"), "고유지표 조정변별력 적용비율", 1, 5);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.onlyNumber(searchMap.getString("uniqMetricAdjustPer"), "고유지표 조정변별력 적용비율");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }

    /**
     * 평가기준 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap standardInfoListExcel(SearchMap searchMap) {
    	String excelFileName = "평가기준";
    	String excelTitle = "평가기준 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO(StringConstants.YEAR, "YEAR", "left"));
    	excelInfoList.add(new ExcelVO("공통지표 조정변별력", "COMM_METRIC_ADJUST_PER", "left"));
    	excelInfoList.add(new ExcelVO("고유지표 조정변별력", "UNIQ_METRIC_ADJUST_PER", "left"));
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.base.standardInfo.getList", searchMap));
    	
        return searchMap;
    }
    
}
