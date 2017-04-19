/*************************************************************************
* CLASS 명      : MemEvalScheduleAction
* 작 업 자      : 유연주
* 작 업 일      : 2017년 03월 09일 
* 기    능      : 개인평가-직원업적평가체계-직원평가일정
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일				변 경 내 용        	 비고
* ----  ---------   -----------------  	-------------------------  --------
*   1   유연주   2017년 03월 09일         	최 초 작 업 
**************************************************************************/
package com.lexken.mem.base;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.core.CommonService;

public class MemEvalScheduleAction extends CommonService {
	private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 직원 평가일정 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memEvalScheduleList(SearchMap searchMap) {
        return searchMap;
    }
    
    /**
     * 직원 평가일정 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memEvalScheduleList_xml(SearchMap searchMap) {
    	searchMap.addList("list", getList("mem.base.memEvalSchedule.getList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 평가일정 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memEvalScheduleModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("mem.base.memEvalSchedule.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 직원 평가일정 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memEvalScheduleProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 무결성 체크
         **********************************/
        if(!"DEL".equals(stMode)) {
        	if(!"MODLIST".equals(stMode)){
	            returnMap = this.validChk(searchMap);
	            
	            if((Integer)returnMap.get("ErrorNumber") < 0 ){
	                searchMap.addList("returnMap", returnMap);
	                return searchMap;
	            }
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
        } else if("MODLIST".equals(stMode)) {
    	searchMap = updateListDB(searchMap);
    }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 직원 평가일정 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("mem.base.memEvalSchedule.insertData", searchMap);
        
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
     * 평가일정 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("mem.base.memEvalSchedule.updateData", searchMap);
	        
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
     * 평가일정 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateListDB(SearchMap searchMap) {
    	HashMap returnMap    = new HashMap();    
    	
    	try {
    		setStartTransaction();
    		
    		String[] indStartDts = searchMap.getStringArray("indStartDts");
    		String[] indEndDts = searchMap.getStringArray("indEndDts");
    		String[] actStartDts = searchMap.getStringArray("actStartDts");
    		String[] actEndDts = searchMap.getStringArray("actEndDts");
    		//String[] staStartDts = searchMap.getStringArray("staStartDts");
    		//String[] staEndDts = searchMap.getStringArray("staEndDts");
    		String[] evalStartDts = searchMap.getStringArray("evalStartDts");
    		String[] evalEndDts = searchMap.getStringArray("evalEndDts");
    		String[] objStartDts = searchMap.getStringArray("objStartDts");
    		String[] objEndDts = searchMap.getStringArray("objEndDts");
    		String[] lastRstStartDts = searchMap.getStringArray("lastRstStartDts");
    		String[] lastRstEndDts = searchMap.getStringArray("lastRstEndDts");
    		
    		searchMap.put("indStartDt", indStartDts[0]);
    		searchMap.put("indEndDt", indEndDts[0]);
    		searchMap.put("actStartDt", actStartDts[0]);
    		searchMap.put("actEndDt", actEndDts[0]);
    		//searchMap.put("staStartDt", staStartDts[0]);
    		//searchMap.put("staEndDt", staEndDts[0]);
    		searchMap.put("evalStartDt", evalStartDts[0]);
    		searchMap.put("evalEndDt", evalEndDts[0]);
    		searchMap.put("objStartDt", objStartDts[0]);
    		searchMap.put("objEndDt", objEndDts[0]);
    		searchMap.put("lastRstStartDt", lastRstStartDts[0]);
    		searchMap.put("lastRstEndDt", lastRstEndDts[0]);
    		
    		returnMap = updateData("mem.base.memEvalSchedule.updateData", searchMap);
    		
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
     * 직원 평가일정 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        
	    	String[] evalSchedules = searchMap.getString("evalSchedules").split("\\|", 0);
	        setStartTransaction();
	        
	        if (null != evalSchedules && 0 < evalSchedules.length) {
		        for (int i = 0; i < evalSchedules.length; i++) {
		        	searchMap.put("year", evalSchedules[i]);
		            returnMap = updateData("mem.base.memEvalSchedule.deleteData", searchMap);
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
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("indStartDt"), "지표등록/승인 시작일자", 1, 8);
        if ((Integer)returnMap.get("ErrorNumber") < 0 ) {
        	return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("indEndDt"), "지표등록/승인 종료일자", 1, 8);
        if ((Integer)returnMap.get("ErrorNumber") < 0 ) {
        	return returnMap;
        }
        
        if (Integer.parseInt(searchMap.getString("indStartDt")) > Integer.parseInt(searchMap.getString("indEndDt"))) {
        	returnMap.put("ErrorNumber",  -1);
			returnMap.put("ErrorMessage", "지표등록/승인 종료일자는 지표등록/승인 시작일자 보다 빠를 수 없습니다.");
	    	
	    	return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("actStartDt"), "실적등록/승인 시작일자", 1, 8);
        if ((Integer)returnMap.get("ErrorNumber") < 0 ) {
        	return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("actEndDt"), "실적등록/승인 종료일자", 1, 8);
        if ((Integer)returnMap.get("ErrorNumber") < 0 ) {
        	return returnMap;
        }
        
        if (Integer.parseInt(searchMap.getString("actStartDt")) > Integer.parseInt(searchMap.getString("actEndDt"))) {
        	returnMap.put("ErrorNumber",  -1);
			returnMap.put("ErrorMessage", "임원 실적등록/승인 종료일자는 임원 실적등록/승인 시작일자 보다 빠를 수 없습니다.");
	    	
	    	return returnMap;
        }
        /*
        returnMap = ValidationChk.lengthCheck(searchMap.getString("staStartDt"), "자기성과기술서등록 시작일자", 1, 8);
        if ((Integer)returnMap.get("ErrorNumber") < 0 ) {
        	return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("staEndDt"), "자기성과기술서등록 종료일자", 1, 8);
        if ((Integer)returnMap.get("ErrorNumber") < 0 ) {
        	return returnMap;
        }
        */
        if (Integer.parseInt(searchMap.getString("staStartDt")) > Integer.parseInt(searchMap.getString("staEndDt"))) {
        	returnMap.put("ErrorNumber",  -1);
			returnMap.put("ErrorMessage", "자기성과기술서등록 종료일자는 자기성과기술서등록 시작일자 보다 빠를 수 없습니다.");
	    	
	    	return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("evalStartDt"), "평가기간 시작일자", 1, 8);
        if ((Integer)returnMap.get("ErrorNumber") < 0 ) {
        	return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("evalEndDt"), "평가기간 종료일자", 1, 8);
        if ((Integer)returnMap.get("ErrorNumber") < 0 ) {
        	return returnMap;
        }
        
        if (Integer.parseInt(searchMap.getString("evalStartDt")) > Integer.parseInt(searchMap.getString("evalEndDt"))) {
        	returnMap.put("ErrorNumber",  -1);
        	returnMap.put("ErrorMessage", "평가기간 종료일자는 평가기간 시작일자 보다 빠를 수 없습니다.");
        	
        	return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("objStartDt"), "이의신청기간 시작일자", 1, 8);
        if ((Integer)returnMap.get("ErrorNumber") < 0 ) {
        	return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("objEndDt"), "이의신청기간 종료일자", 1, 8);
        if ((Integer)returnMap.get("ErrorNumber") < 0 ) {
        	return returnMap;
        }
        
        if (Integer.parseInt(searchMap.getString("objStartDt")) > Integer.parseInt(searchMap.getString("objEndDt"))) {
        	returnMap.put("ErrorNumber",  -1);
        	returnMap.put("ErrorMessage", "이의신청기간 종료일자는 이의신청기간 시작일자 보다 빠를 수 없습니다.");
        	
        	return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("lastRstStartDt"), "최종결과조회기간 시작일자", 1, 8);
        if ((Integer)returnMap.get("ErrorNumber") < 0 ) {
        	return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("lastRstEndDt"), "최종결과조회기간 종료일자", 1, 8);
        if ((Integer)returnMap.get("ErrorNumber") < 0 ) {
        	return returnMap;
        }
        
        if (Integer.parseInt(searchMap.getString("lastRstStartDt")) > Integer.parseInt(searchMap.getString("lastRstEndDt"))) {
        	returnMap.put("ErrorNumber",  -1);
        	returnMap.put("ErrorMessage", "최종결과조회기간 종료일자는 최종결과조회기간 시작일자 보다 빠를 수 없습니다.");
        	
        	return returnMap;
        }
        
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }
}
