/*************************************************************************
* CLASS 명      : EvalScheduleAction
* 작 업 자      : 김상용
* 작 업 일      : 2013년 04월 29일 
* 기    능      : 개인평가 평가일정
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일				변 경 내 용        	 비고
* ----  ---------   -----------------  	-------------------------  --------
*   1    김 상 용    2013년 04월 29일        	최 초 작 업 
**************************************************************************/
package com.lexken.prs.eval;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginVO;
import com.lexken.framework.util.StaticUtil;

public class EvalScheduleAction extends CommonService {
	private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 개인평가 평가일정 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalScheduleList(SearchMap searchMap) {
        return searchMap;
    }
    
    /**
     * 개인평가 평가일정 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalScheduleList_xml(SearchMap searchMap) {
    	searchMap.addList("list", getList("prs.eval.evalSchedule.getList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 평가일정 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalScheduleModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("prs.eval.evalSchedule.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 평가일정 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalScheduleProcess(SearchMap searchMap) {
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
     * 평가일정 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("prs.eval.evalSchedule.insertData", searchMap);
        
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
	        
	        returnMap = updateData("prs.eval.evalSchedule.updateData", searchMap);
	        
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
    		
    		String[] dirEvalStartDts = searchMap.getStringArray("dirEvalStartDts");
    		String[] dirEvalEndDts = searchMap.getStringArray("dirEvalEndDts");
    		String[] dirRptStartDts = searchMap.getStringArray("dirRptStartDts");
    		String[] dirRptEndDts = searchMap.getStringArray("dirRptEndDts");
    		String[] mngStartDts = searchMap.getStringArray("mngStartDts");
    		String[] mngEndDts = searchMap.getStringArray("mngEndDts");
    		String[] mngOrgStartDts = searchMap.getStringArray("mngOrgStartDts");
    		String[] mngOrgEndDts = searchMap.getStringArray("mngOrgEndDts");
    		String[] mngRptStartDts = searchMap.getStringArray("mngRptStartDts");
    		String[] mngRptEndDts = searchMap.getStringArray("mngRptEndDts");
    		String[] mngObjStartDts = searchMap.getStringArray("mngObjStartDts");
    		String[] mngObjEndDts = searchMap.getStringArray("mngObjEndDts");
    		String[] empStartDts = searchMap.getStringArray("empStartDts");
    		String[] empEndDts = searchMap.getStringArray("empEndDts");
    		String[] mngPlanStartDts = searchMap.getStringArray("mngPlanStartDts");
    		String[] mngPlanEndDts = searchMap.getStringArray("mngPlanEndDts");
    		
    		searchMap.put("dirEvalStartDt", dirEvalStartDts[0]);
    		searchMap.put("dirEvalEndDt", dirEvalEndDts[0]);
    		searchMap.put("dirRptStartDt", dirRptStartDts[0]);
    		searchMap.put("dirRptEndDt", dirRptEndDts[0]);
    		searchMap.put("mngStartDt", mngStartDts[0]);
    		searchMap.put("mngEndDt", mngEndDts[0]);
    		searchMap.put("mngOrgStartDt", mngOrgStartDts[0]);
    		searchMap.put("mngOrgEndDt", mngOrgEndDts[0]);
    		searchMap.put("mngRptStartDt", mngRptStartDts[0]);
    		searchMap.put("mngRptEndDt", mngRptEndDts[0]);
    		searchMap.put("mngObjStartDt", mngObjStartDts[0]);
    		searchMap.put("mngObjEndDt", mngObjEndDts[0]);
    		searchMap.put("empStartDt", empStartDts[0]);
    		searchMap.put("empEndDt", empEndDts[0]);
    		searchMap.put("mngPlanStartDt", mngPlanStartDts[0]);
    		searchMap.put("mngPlanEndDt", mngPlanEndDts[0]);
    		
    		returnMap = updateData("prs.eval.evalSchedule.updateData", searchMap);
    		
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
     * 평가일정 삭제 
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
		            returnMap = updateData("prs.eval.evalSchedule.deleteData", searchMap);
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
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("dirEvalStartDt"), "임원 평가 시작일자", 1, 8);
        if ((Integer)returnMap.get("ErrorNumber") < 0 ) {
        	return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("dirEvalEndDt"), "임원 평가 종료일자", 1, 8);
        if ((Integer)returnMap.get("ErrorNumber") < 0 ) {
        	return returnMap;
        }
        
        if (Integer.parseInt(searchMap.getString("dirEvalStartDt")) > Integer.parseInt(searchMap.getString("dirEvalEndDt"))) {
        	returnMap.put("ErrorNumber",  -1);
			returnMap.put("ErrorMessage", "임원 평가의 종료일자는 시작일자 보다 빠를 수 없습니다.");
	    	
	    	return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("dirRptStartDt"), "임원 직무성과기술서 제출 시작일자", 1, 8);
        if ((Integer)returnMap.get("ErrorNumber") < 0 ) {
        	return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("dirRptEndDt"), "임원 직무성과기술서 제출 종료일자", 1, 8);
        if ((Integer)returnMap.get("ErrorNumber") < 0 ) {
        	return returnMap;
        }
        
        if (Integer.parseInt(searchMap.getString("dirRptStartDt")) > Integer.parseInt(searchMap.getString("dirRptEndDt"))) {
        	returnMap.put("ErrorNumber",  -1);
			returnMap.put("ErrorMessage", "임원 직무성과기술서의 제출 종료일자는 제출 시작일자 보다 빠를 수 없습니다.");
	    	
	    	return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("mngStartDt"), "간부 개인평가 시작일자", 1, 8);
        if ((Integer)returnMap.get("ErrorNumber") < 0 ) {
        	return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("mngEndDt"), "간부 개인평가 종료일자", 1, 8);
        if ((Integer)returnMap.get("ErrorNumber") < 0 ) {
        	return returnMap;
        }
        
        if (Integer.parseInt(searchMap.getString("mngStartDt")) > Integer.parseInt(searchMap.getString("mngEndDt"))) {
        	returnMap.put("ErrorNumber",  -1);
			returnMap.put("ErrorMessage", "간부 개인평가 종료일자는 간부 개인평가 시작일자 보다 빠를 수 없습니다.");
	    	
	    	return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("mngOrgStartDt"), "간부 조직역량평가 시작일자", 1, 8);
        if ((Integer)returnMap.get("ErrorNumber") < 0 ) {
        	return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("mngOrgEndDt"), "간부 조직역량평가 종료일자", 1, 8);
        if ((Integer)returnMap.get("ErrorNumber") < 0 ) {
        	return returnMap;
        }
        
        if (Integer.parseInt(searchMap.getString("mngOrgStartDt")) > Integer.parseInt(searchMap.getString("mngOrgEndDt"))) {
        	returnMap.put("ErrorNumber",  -1);
        	returnMap.put("ErrorMessage", "간부 조직역량평가 종료일자는 간부 조직역량평가 시작일자 보다 빠를 수 없습니다.");
        	
        	return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("mngRptStartDt"), "간부 자기성과기술서 제출 시작일자", 1, 8);
        if ((Integer)returnMap.get("ErrorNumber") < 0 ) {
        	return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("mngRptEndDt"), "간부 자기성과기술서 제출 종료일자", 1, 8);
        if ((Integer)returnMap.get("ErrorNumber") < 0 ) {
        	return returnMap;
        }
        
        if (Integer.parseInt(searchMap.getString("mngRptStartDt")) > Integer.parseInt(searchMap.getString("mngRptEndDt"))) {
        	returnMap.put("ErrorNumber",  -1);
        	returnMap.put("ErrorMessage", "간부 자기성과기술서 제출 종료일자는 간부 자기성과기술서 제출 시작일자 보다 빠를 수 없습니다.");
        	
        	return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("empStartDt"), "직원 개인평가 시작일자", 1, 8);
        if ((Integer)returnMap.get("ErrorNumber") < 0 ) {
        	return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("empEndDt"), "직원 개인평가 종료일자", 1, 8);
        if ((Integer)returnMap.get("ErrorNumber") < 0 ) {
        	return returnMap;
        }
        
        if (Integer.parseInt(searchMap.getString("empStartDt")) > Integer.parseInt(searchMap.getString("empEndDt"))) {
        	returnMap.put("ErrorNumber",  -1);
        	returnMap.put("ErrorMessage", "직원 개인평가 종료일자는 직원 개인평가 시작일자 보다 빠를 수 없습니다.");
        	
        	return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("mngPlanStartDt"), "성과계획서 제출 시작일자", 1, 8);
        if ((Integer)returnMap.get("ErrorNumber") < 0 ) {
        	return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("mngPlanEndDt"), "성과계획서 제출 종료일자", 1, 8);
        if ((Integer)returnMap.get("ErrorNumber") < 0 ) {
        	return returnMap;
        }
        
        if (Integer.parseInt(searchMap.getString("mngPlanStartDt")) > Integer.parseInt(searchMap.getString("mngPlanEndDt"))) {
        	returnMap.put("ErrorNumber",  -1);
        	returnMap.put("ErrorMessage", "성과계획서 제출 종료일자는 성과계획서 제출 시작일자 보다 빠를 수 없습니다.");
        	
        	return returnMap;
        }
        
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }
}
