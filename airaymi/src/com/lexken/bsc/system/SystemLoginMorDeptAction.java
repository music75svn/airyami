/*************************************************************************
* CLASS 명      : SystemLoginMorDeptAction
* 작 업 자      : 김윤기
* 작 업 일      : 2012년 7월 11일 
* 기    능      : 시스템 사용현황(조직별)
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    김윤기      2012년 7월 11일             최 초 작 업 
**************************************************************************/
package com.lexken.bsc.system;
    
import java.util.HashMap;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.struts2.IsBoxActionSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.util.StaticUtil;

public class SystemLoginMorDeptAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 시스템 사용현황(조직별) 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap systemLoginMorDeptList(SearchMap searchMap) {

        return searchMap;
    }
    
    /**
     * 시스템 사용현황(조직별) 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap systemLoginMorDeptList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.system.systemLoginMorDept.getList", searchMap));

        return searchMap;
    }
    
    
    /**
     * 시스템사용현황(조직별) 차트 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap systemLoginMorDeptChart_xml(SearchMap searchMap) {

    	searchMap.addList("chartList", getList("bsc.system.systemLoginMorDept.getChartList", searchMap));
        return searchMap;
    }

    
    /**
     * 시스템 사용현황(조직별) 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap systemLoginMorDeptModify(SearchMap searchMap) {
        return searchMap;
    }
    
    /**
     * 시스템사용현황(일별) 상세 그리드 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap systemLoginMorDeptModify_xml(SearchMap searchMap) {
    	
    	searchMap.addList("detail", getList("bsc.system.systemLoginMorDept.getDetail", searchMap));
    	return searchMap;
    }
    
    
    /**
     * 시스템 사용현황(조직별) 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap systemLoginMorDeptProcess(SearchMap searchMap) {
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
     * 시스템 사용현황(조직별) 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("bsc.system.systemLoginMorDept.insertData", searchMap);
        
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
     * 시스템 사용현황(조직별) 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("bsc.system.systemLoginMorDept.updateData", searchMap);
	        
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
     * 시스템 사용현황(조직별) 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
        //PK 부분 추가
        /*
        String systemLoginMorDeptIds = searchMap.getString("systemLoginMorDeptIds").split("\\|", 0);
        
        setStartTransaction();
        
        for (int i = 0; i < systemLoginMorDeptIds.length; i++) {
            searchMap.put("systemLoginMorDeptId", systemLoginMorDeptIds[i]);
            returnMap = updateData("bsc.system.systemLoginMorDept.deleteData", searchMap);
        }
        
        setEndTransaction();
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
