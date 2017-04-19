/*************************************************************************
* CLASS 명      : SignalStatusAction
* 작 업 자      : 정철수
* 작 업 일      : 2012년 5월 30일 
* 기    능      : 신호등상태관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    정철수      2012년 5월 30일            최 초 작 업 
**************************************************************************/
package com.lexken.bsc.base;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.core.CommonService;

public class SignalStatusAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 신호등상태 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap signalStatusList(SearchMap searchMap) {

        return searchMap;
    }
    
    /**
     * 신호등상태 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap signalStatusList_xml(SearchMap searchMap) {
    	
        searchMap.addList("list", getList("bsc.base.signalStatus.getList", searchMap));
        
        return searchMap;
    }
    
    /**
     * 신호등상태 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap signalStatusModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.base.signalStatus.getDetail", searchMap));
    	}
    	
        return searchMap;
    }
    
    /**
     * 신호등상태 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap signalStatusProcess(SearchMap searchMap) {
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
        if("MOD".equals(stMode)) {
            searchMap = updateDB(searchMap);
        } 
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
        
    /**
     * 신호등상태 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        setStartTransaction();
        
        returnMap = deleteData("bsc.base.signalStatus.deleteData", searchMap, true);
        
        returnMap = insertData("bsc.base.signalStatus.insertData", searchMap);
        
        setEndTransaction();
        
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
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("fromValue"), "시작구간대(이상)", 1, 20);
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
            return returnMap;
        }
        
        returnMap = ValidationChk.onlyNumberPoint(searchMap.getString("fromValue"), "시작구간대(이상)");
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
        	return returnMap;
        }
        
        if( searchMap.getString("fromValue").split("\\.").length > 2 ){
        	returnMap.put("ErrorNumber",  -1);
    		returnMap.put("ErrorMessage", "시작구간대(이상)은 숫자만 입력 가능합니다.");
        	
    		return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("toValue"), "종료구간대(미만)", 1, 20);
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
            return returnMap;
        }
        
        returnMap = ValidationChk.onlyNumberPoint(searchMap.getString("toValue"), "종료구간대(미만)");
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
        	return returnMap;
        }
        
        if( searchMap.getString("toValue").split("\\.").length > 2 ){
        	returnMap.put("ErrorNumber",  -1);
    		returnMap.put("ErrorMessage", "종료구간대(미만)은 숫자만 입력 가능합니다.");
        	
    		return returnMap;
        }
        
        if( Float.parseFloat(searchMap.getString("fromValue")) > Float.parseFloat(searchMap.getString("toValue"))){
        	returnMap.put("ErrorNumber",  -1);
    		returnMap.put("ErrorMessage", "시작구간대(이상)이 종료구간대(미만)보다 클 수 없습니다.");
        	
    		return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("color"), "색상", 1, 20);
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
            return returnMap;
        }
        
        if( "".equals(searchMap.getString("color")) || "rgb(240, 240, 240)".equals(searchMap.getString("color")) ) {
        	returnMap.put("ErrorNumber",  -1);
    		returnMap.put("ErrorMessage", "색상을 선택하십시오");
        	
    		return returnMap;
        }
        
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }
        
}
