/*************************************************************************
* CLASS 명      : BonSetAction
* 작 업 자      : 안요한
* 작 업 일      : 2013년 6월 11일 
* 기    능      : 성과급 설정
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    안요한      2013년 6월 11일             최 초 작 업 
**************************************************************************/
package com.lexken.prs.bonus;
    
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.StringConstants;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.util.HtmlHelper;
import com.lexken.framework.util.StaticUtil;
import com.lexken.framework.login.LoginVO;

public class BonSetAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 성과급 설정 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap bonSetModify(SearchMap searchMap) {
    	
    	searchMap.addList("detail", getDetail("prs.bonus.bonSet.getList", searchMap));
    	
    	searchMap.addList("gradeList", getList("prs.bonus.bonSet.getGradeList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 성과급 설정 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap bonSetProcess(SearchMap searchMap) {
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
        } 
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 성과급 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        	
        	returnMap = deleteData("prs.bonus.bonSet.deleteData", searchMap, true); //설정 삭제
        	
        	returnMap = insertData("prs.bonus.bonSet.insertData", searchMap); //설정 등록
        	
	        /**********************************
             * 직위별 등급 등록 
             **********************************/
	        returnMap = insertPosGrade(searchMap);
	                
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
     * 직위별등급 등록
     * @param      
     * @return String  
     * @throws 
     */
    public HashMap insertPosGrade(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        
        /**********************************
         * 직위별등급
         **********************************/
        String[] gradeS = searchMap.getStringArray("gradeS");
        String[] gradeA = searchMap.getStringArray("gradeA");
        String[] gradeB = searchMap.getStringArray("gradeB");
        String[] gradeC = searchMap.getStringArray("gradeC");
        String[] gradeD = searchMap.getStringArray("gradeD");
        
        try {
        	/**********************************
             * 직위별등급 삭제
             **********************************/
        	returnMap = deleteData("prs.bonus.bonSet.deleteGradeData", searchMap, true); 
        	
		        /**********************************
		         * S등급 직위별등급 등록
		         **********************************/
	            searchMap.put("chfRate", gradeS[0]);
	            searchMap.put("mngRate", gradeS[1]);
	            searchMap.put("empRate", gradeS[2]);
	            searchMap.put("grade", "S");
	            
	        	returnMap = insertData("prs.bonus.bonSet.insertGradeData", searchMap); //직위별 등급 등록
	        	
	        	/**********************************
	        	 * A등급 직위별등급 등록
	        	 **********************************/
	        	searchMap.put("chfRate", gradeA[0]);
	        	searchMap.put("mngRate", gradeA[1]);
	        	searchMap.put("empRate", gradeA[2]);
	        	searchMap.put("grade", "A");
	        	returnMap = insertData("prs.bonus.bonSet.insertGradeData", searchMap); //직위별 등급 등록
	        	
	        	/**********************************
	        	 * B등급 직위별등급 등록
	        	 **********************************/
	        	searchMap.put("chfRate", gradeB[0]);
	        	searchMap.put("mngRate", gradeB[1]);
	        	searchMap.put("empRate", gradeB[2]);
	        	searchMap.put("grade", "B");
	        	
	        	returnMap = insertData("prs.bonus.bonSet.insertGradeData", searchMap); //직위별 등급 등록
	        	
	        	/**********************************
	        	 * C등급 직위별등급 등록
	        	 **********************************/
	        	searchMap.put("chfRate", gradeC[0]);
	        	searchMap.put("mngRate", gradeC[1]);
	        	searchMap.put("empRate", gradeC[2]);
	        	searchMap.put("grade", "C");
	        	
	        	returnMap = insertData("prs.bonus.bonSet.insertGradeData", searchMap); //직위별 등급 등록
	        	
	        	/**********************************
	        	 * D등급 직위별등급 등록
	        	 **********************************/
	        	searchMap.put("chfRate", gradeD[0]);
	        	searchMap.put("mngRate", gradeD[1]);
	        	searchMap.put("empRate", gradeD[2]);
	        	searchMap.put("grade", "D");
	
	        	returnMap = insertData("prs.bonus.bonSet.insertGradeData", searchMap); //직위별 등급 등록
	        	
        } catch (Exception e) {
        	logger.error(e.toString());
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        }
        return returnMap;    
    }

    /**
     *  Validation 체크(무결성 체크)
     * @param SearchMap
     * @return HashMap
     */
    private HashMap validChk(SearchMap searchMap) {
        HashMap returnMap         = new HashMap();
        int     resultValue        = 0;
        
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }
    
}
