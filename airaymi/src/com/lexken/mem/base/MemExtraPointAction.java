/*************************************************************************
* CLASS 명      : ExtraPointAction
* 작 업 자      : 안요한
* 작 업 일      : 2013년 05월 28일 
* 기    능      : 공모직 대상자
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일				변 경 내 용        	 비고
* ----  ---------   -----------------  	-------------------------  --------
*   1    안 요 한    2013년 05월 28일        	최 초 작 업 
**************************************************************************/
package com.lexken.mem.base;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.util.StaticUtil;

public class MemExtraPointAction extends CommonService {
	private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 공모직 대상자 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memExtraPointList(SearchMap searchMap) {
    	
        return searchMap;
    }
    
    /**
     * 공모직 대상자 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memExtraPointList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("mem.base.memExtraPoint.getList", searchMap));
        
        return searchMap;
    }
    
    /**
     * 공모직 대상자 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memExtraPointModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("stMode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("mem.base.memExtraPoint.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    
    /**
     * 공모직대상자 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memExtraPointProcess(SearchMap searchMap) {
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
        } else if("DEL".equals(stMode)) {
            searchMap = deleteDB(searchMap);
        } else if("MOD".equals(stMode)) {
            searchMap = updateDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
         return searchMap;
    }

    /**
     * 공모직대상자 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        /**********************************
	         * 공모직 대상자수 확인
	         **********************************/
	        int cnt = getInt("mem.base.memExtraPoint.getExtraPointCount", searchMap);
	        
	        if(0 < cnt) {
	            returnMap.put("ErrorNumber", ErrorMessages.FAILURE_DUP_CODE);
	            returnMap.put("ErrorMessage", ErrorMessages.format(ErrorMessages.FAILURE_DUP_MESSAGE, "공모직대상"));
	        } else {
	        
	        	returnMap = insertData("mem.base.memExtraPoint.insertExtraPontData", searchMap);
	        }
	        	
	        
        } catch (Exception e) {
        	setRollBackTransaction();
        	logger.error(e.toString());
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
     * 공모직대상자 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] empNos = searchMap.getString("empNos").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != empNos && 0 < empNos.length) {
		        for (int i = 0; i < empNos.length; i++) {
		            searchMap.put("empNo", empNos[i]);
		            returnMap = deleteData("mem.base.memExtraPoint.deleteExtraPontData", searchMap);
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
     * 대상자 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        /**********************************
	         * 공모직 대상자수 확인
	         **********************************/
	        int cnt = getInt("mem.base.memExtraPoint.getExtraPointCount", searchMap);
	        
	        String empNoOld = searchMap.getString("empNoOld");
	        
	        if(0 < cnt) {
	            returnMap.put("ErrorNumber", ErrorMessages.FAILURE_DUP_CODE);
	            returnMap.put("ErrorMessage", ErrorMessages.format(ErrorMessages.FAILURE_DUP_MESSAGE, "공모직대상자"));
	        } else {
	        
	        	if(!"".equals(empNoOld)) {
	        	
	        		returnMap = updateData("mem.base.memExtraPoint.updateData", searchMap);
	        	}else {
	        	
	        	returnMap = insertData("mem.base.memExtraPoint.insertExtraPontData", searchMap);
	        	
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
     * 조직별 사용자 리스트 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deptUserInfo_ajax(SearchMap searchMap) {
    	
    	searchMap.addList("userList", getList("mem.base.memExtraPoint.getUserList", searchMap));
        
        return searchMap;
    }
    
    /**
     * 사람찾기 팝업
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popSearchUser(SearchMap searchMap) {
    	
    	searchMap.addList("treeList", getList("mem.base.memExtraPoint.getDeptList", searchMap));

        return searchMap;
    }
    
	/**
	 * Validation 체크(무결성 체크)
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
