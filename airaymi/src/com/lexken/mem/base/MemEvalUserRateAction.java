/*************************************************************************
* CLASS 명      : MemEvalUserRateAction
* 작 업 자      : 유연주
* 작 업 일      : 2017년 03월 13일 
* 기    능      : 평가자비율설정
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    유연주      2017년 03월 13일          최 초 작 업 
**************************************************************************/
package com.lexken.mem.base;
    
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.core.CommonService;

public class MemEvalUserRateAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 평가자비율설정 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memEvalUserRateList(SearchMap searchMap) {
    	
    	searchMap.put("findCodeGrpId", "239");
    	searchMap.addList("evalYnList", getList("bsc.base.code.getList", searchMap));
    	
    	searchMap.put("findCodeGrpId", "238");
    	searchMap.addList("evalMethodList", getList("bsc.base.code.getList", searchMap));
    	
    	searchMap.addList("evalGiveMethodList", getList("mem.base.memEvalUserRate.getEvalGiveMethodList", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 평가자비율설정 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memEvalUserRateList_xml(SearchMap searchMap) {
    	
        
        searchMap.addList("list", getList("mem.base.memEvalUserRate.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가자비율설정 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memEvalUserRateProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 무결성 체크
         **********************************/
        if(!"DEL".equals(stMode) && !"SAVE".equals(stMode) && !"MODUSER".equals(stMode) ) {
            returnMap = this.validChk(searchMap);
            
            if((Integer)returnMap.get("ErrorNumber") < 0 ){
                searchMap.addList("returnMap", returnMap);
                return searchMap;
            }
        }
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
       if("SAVE".equals(stMode)) {
            searchMap = allSaveDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }

    /**
     * 평가자비율설정 정렬순서 일괄 저장 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap allSaveDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
			String[] evalUserGubunIds = searchMap.getString("evalUserGubunIds").split("\\|", -1);
			String[] evalYns = searchMap.getString("evalYns").split("\\|", -1);
			String[] evalMethods = searchMap.getString("evalMethods").split("\\|", -1);
			String[] evalGiveMethods = searchMap.getString("evalGiveMethods").split("\\|", -1);
			String[] evalRates = searchMap.getString("evalRates").split("\\|", -1);
			String[] sortOrders = searchMap.getString("sortOrders").split("\\|", -1);
	        
	        setStartTransaction();
	        
	        if(null != evalUserGubunIds && 0 < evalUserGubunIds.length) {
		        for (int i = 0; i < evalUserGubunIds.length - 1; i++) {
		            searchMap.put("sortOrder", sortOrders[i]);
		            searchMap.put("evalUserGubunId", evalUserGubunIds[i]);
		            searchMap.put("evalYn", evalYns[i]);
		            searchMap.put("evalMethod", evalMethods[i]);
		            searchMap.put("evalGiveMethod", evalGiveMethods[i]);
		            searchMap.put("evalRate", evalRates[i]);
		            returnMap = insertData("mem.base.memEvalUserRate.allSaveData", searchMap);
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
        String stMode = searchMap.getString("mode");
        /*
    	returnMap = ValidationChk.lengthCheck(searchMap.getString("jobEvalItemNm"), "평가자비율설정", 1, 200);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
    	returnMap = ValidationChk.lengthCheck(searchMap.getString("content"), "설명", 1, 4000);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
		*/
    	returnMap = ValidationChk.lengthCheck(searchMap.getString("sortOrder"), "정렬순서", 0, 5);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
    		
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }
}
