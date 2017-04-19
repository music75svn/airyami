/*************************************************************************
* CLASS 명      : MemMeasGradeAction
* 작 업 자      : 유연주
* 작 업 일      : 2017년 03월 09일 
* 기    능      : 개인평가-직원업적평가체계-계량평가등급환산표
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일				변 경 내 용        	 비고
* ----  ---------   -----------------  	-------------------------  --------
*   1   유연주   2017년 03월 10일         	최 초 작 업 
**************************************************************************/
package com.lexken.mem.base;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.core.CommonService;

public class MemMeasGradeAction extends CommonService {
	private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 계량평가등급환산표 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memMeasGradeList(SearchMap searchMap) {
    	
        /**********************************
         * 평가구간대 조회
         **********************************/
    	String findYear = searchMap.getString("findYear");
    	String findGubun = searchMap.getString("findGubun");
    	
    	if(findGubun == null || findGubun.equals("")){
    		findGubun = "01";
    	}
    	
    	searchMap.put("year", findYear);
    	searchMap.put("gubun", findGubun);
        searchMap.addList("evalSectionList", getList("mem.base.memMeasGrade.evalSectionList", searchMap));
        
        return searchMap;
    }
    
    /**
     * 지표Pool관리 등록/수정/삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap memMeasGradeProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");

        /**********************************
         * 무결성 체크
         **********************************/
        returnMap = this.validChk(searchMap);

        if((Integer)returnMap.get("ErrorNumber") < 0 ){
            searchMap.addList("returnMap", returnMap);
            return searchMap;
        }

        /**********************************
         * 등록/수정/삭제
         **********************************/
        searchMap = saveDB(searchMap);

        /**********************************
         * Return
         **********************************/
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
        /**********************************
         * 등급구간 Parameter setting
         **********************************/
        String[] fromValues 		= searchMap.getStringArray("fromValues");
        String[] toValues   		= searchMap.getStringArray("toValues");
        String[] conversionScores   = searchMap.getStringArray("conversionScores");

        if( "01".equals(searchMap.get("scoreCalTypeGubun")) ) {
        	for( int i = 0; i < fromValues.length; i++){

        		returnMap = ValidationChk.emptyCheck(fromValues[i], "점수구간");
                if((Integer)returnMap.get("ErrorNumber") < 0 ){
                	return returnMap;
                }

                returnMap = ValidationChk.emptyCheck(toValues[i], "점수구간");
                if((Integer)returnMap.get("ErrorNumber") < 0 ){
                	return returnMap;
                }

            	if( Float.parseFloat(fromValues[i]) >= Float.parseFloat(toValues[i]) ){
            		returnMap.put("ErrorNumber",  -1);
            		returnMap.put("ErrorMessage", "상/하향 구간 점수가 잘못 되었습니다.");

                	return returnMap;
            	}

                returnMap = ValidationChk.emptyCheck(conversionScores[i], "환산점수");
                if((Integer)returnMap.get("ErrorNumber") < 0 ){
                	return returnMap;
                }

                returnMap = ValidationChk.emptyCheck(conversionScores[i], "환산점수");
                if((Integer)returnMap.get("ErrorNumber") < 0 ){
                	return returnMap;
                }

        	}

        }

        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;
    }
    
    /**
     * 계량평가등급환선표 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap saveDB(SearchMap searchMap) {
    	HashMap returnMap    = new HashMap();

    	String scoreCalTypeGubun = searchMap.getString("gubun");

        /**********************************
         * 구간대 Parameter setting
         **********************************/
        String[] evalSectionIds = searchMap.getStringArray("evalSectionIds");
        String[] fromValues = searchMap.getStringArray("fromValues");
        String[] toValues = searchMap.getStringArray("toValues");
        String[] conversionScores = searchMap.getStringArray("conversionScores");

        try {
	        setStartTransaction();

	        /**********************************
	         * 구간대 삭제
	         **********************************/
	        returnMap = updateData("mem.base.memMeasGrade.deleteEvalSection", searchMap, true);

	        /**********************************
	         * 구간대 등록
	         **********************************/
        	if("01".equals(scoreCalTypeGubun) || "02".equals(scoreCalTypeGubun)) {
		        for (int i = 0; i < evalSectionIds.length; i++) {
		            searchMap.put("evalSectionId", evalSectionIds[i]);
		            searchMap.put("fromValue", fromValues[i]);
		            searchMap.put("toValue", toValues[i]);
		            searchMap.put("conversionScore", conversionScores[i]);
		            returnMap = insertData("mem.base.memMeasGrade.insertEvalSection", searchMap);
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
}
