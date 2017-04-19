/*************************************************************************
* CLASS 명      : MenuAction
* 작 업 자      : 하윤식
* 작 업 일      : 2012년 6월 6일 
* 기    능      : 메뉴관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    하윤식      메뉴관리            최 초 작 업 
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

public class MenuAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 메뉴트리 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap menuList(SearchMap searchMap) {
    	
    	String pgmId = (String)searchMap.get("pgmId");
    	
    	if("".equals(StaticUtil.nullToBlank(pgmId))) {
    		searchMap.put("pgmId", "PGM0000"); //전체메뉴
    	}

    	searchMap.addList("menuList", getList("bsc.system.menu.getList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 메뉴관리 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap menuListData(SearchMap searchMap) {

    	searchMap.addList("detail", getDetail("bsc.system.menu.getDetail", searchMap));
    	
    	searchMap.addList("adminList", getList("bsc.system.menu.getAdminList", searchMap));
    	
    	searchMap.addList("menuSortList", getList("bsc.system.menu.getMenuSortList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 메뉴관리 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap menuModify(SearchMap searchMap) {
        
        return searchMap;
    }
    
    /**
     * 메뉴관리 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap menuProcess(SearchMap searchMap) {
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
        } else if("ADMIN_MOD".equals(stMode)) {
            searchMap = insertAdminDB(searchMap);
        } else if("SORT_MOD".equals(stMode)) {
            searchMap = updateMenuSortDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 메뉴관리 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = insertData("bsc.system.menu.insertData", searchMap);
	        
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
     * 메뉴관리 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("bsc.system.menu.updateData", searchMap);
	        
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
     * 메뉴관리 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("bsc.system.menu.deleteData", searchMap);
	        
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
     * 메뉴 권한 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertAdminDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
	        
        try {
	        String[] adminGubuns = searchMap.getStringArray("adminGubuns");
	        
	        setStartTransaction();
	        
	        returnMap = deleteData("bsc.system.menu.deleteAdminData", searchMap, true);
	        
	        if(adminGubuns != null && 0 < adminGubuns.length) {
		        for (int i = 0; i < adminGubuns.length; i++) {
		            searchMap.put("adminGubun", adminGubuns[i]);
		            returnMap = insertData("bsc.system.menu.insertAdminData", searchMap);
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
     * 메뉴 정렬순서 저장
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateMenuSortDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
	        
        try {
	        String[] pgmIds = searchMap.getStringArray("pgmIds");
	        String[] sortOrders = searchMap.getStringArray("sortOrders");
	        
	        setStartTransaction();
	        
	        if(pgmIds != null && 0 < pgmIds.length) {
		        for (int i=0; i < pgmIds.length; i++) {
		            searchMap.put("pgmIdKey",	pgmIds[i]);
		            searchMap.put("sortOrder",	sortOrders[i]);
		            returnMap = insertData("bsc.system.menu.updateSortOrderData", searchMap);
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
        
        //Validation 체크 추가
        if( "ADD".equals(searchMap.get("mode")) ){
            
        	returnMap = ValidationChk.lengthCheck(searchMap.getString("pgmNm"), "메뉴", 1, 70);
            if((Integer)returnMap.get("ErrorNumber") < 0 ){
            	return returnMap;
            }
             
            returnMap = ValidationChk.lengthCheck(searchMap.getString("url"), "URL", 0, 1500);
            if((Integer)returnMap.get("ErrorNumber") < 0 ){
            	return returnMap;
            }
             
            returnMap = ValidationChk.lengthCheck(searchMap.getString("param"), "PARAM", 0, 500);
            if((Integer)returnMap.get("ErrorNumber") < 0 ){
            	return returnMap;
            }
             
            returnMap = ValidationChk.lengthCheck(searchMap.getString("content"), "비고", 0, 500);
            if((Integer)returnMap.get("ErrorNumber") < 0 ){
            	return returnMap;
            }
        	
        } else if( "MOD".equals(searchMap.get("mode")) ) {
        	
        	returnMap = ValidationChk.lengthCheck(searchMap.getString("pgmNmU"), "메뉴", 1, 70);
            if((Integer)returnMap.get("ErrorNumber") < 0 ){
            	return returnMap;
            }
             
            returnMap = ValidationChk.lengthCheck(searchMap.getString("urlU"), "URL", 0, 1500);
            if((Integer)returnMap.get("ErrorNumber") < 0 ){
            	return returnMap;
            }
             
            returnMap = ValidationChk.lengthCheck(searchMap.getString("param"), "PARAM", 0, 500);
            if((Integer)returnMap.get("ErrorNumber") < 0 ){
            	return returnMap;
            }
             
            returnMap = ValidationChk.lengthCheck(searchMap.getString("contentU"), "비고", 0, 500);
            if((Integer)returnMap.get("ErrorNumber") < 0 ){
            	return returnMap;
            }
        	
        } else if( "SORT_MOD".equals(searchMap.get("mode")) ) {
        	
        	String sortCnt [] = searchMap.getStringArray("sortOrders");
        	
        	for( int i = 0; i < sortCnt.length; i++){
        		
        		returnMap = ValidationChk.onlyNumber(sortCnt[i], "정렬순서");
                if((Integer)returnMap.get("ErrorNumber") < 0 ){
                	return returnMap;
                }
        		
        	}
        	
        }
        
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }
    
    
}
