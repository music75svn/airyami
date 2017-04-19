/*************************************************************************
* CLASS 명      : MenuRightAction
* 작 업 자      : 하윤식
* 작 업 일      : 2012년 6월 7일 
* 기    능      : 사용자 그룹별 권한
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    하윤식      사용자 그룹별 권한            최 초 작 업 
**************************************************************************/
package com.lexken.bsc.system;
    
import java.util.HashMap;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.struts2.IsBoxActionSupport;
import com.lexken.framework.util.StaticUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginManager;

public class MenuRightAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 사용자 그룹별 권한 조회
     * @param
     * @return String  
     * @throws 
     */
    public SearchMap menuRightList(SearchMap searchMap) {

    	String findTopUpId = (String)searchMap.get("findTopUpId");
    	
    	searchMap.addList("menuLevel1List", getList("bsc.system.menuRight.getMenuLevel1List", searchMap));
    	
    	/**********************************
         * 디폴트 조회조건 설정(topPgmId)
         **********************************/
    	if("".equals(StaticUtil.nullToBlank(findTopUpId))) {
    		searchMap.put("findTopUpId", searchMap.getDefaultValue("menuLevel1List", "PGM_ID", 0));
    	}
    	
    	searchMap.addList("menuLevel2List", getList("bsc.system.menuRight.getMenuLevel2List", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 사용자 그룹별 권한 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap menuRightList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.system.menuRight.getList", searchMap));

        return searchMap;
    }
    
    /**
     * Top 레벨 메뉴조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap menuLevel1_ajax(SearchMap searchMap) {
        
    	searchMap.addList("menuLevel1List", getList("bsc.system.menuRight.getMenuLevel1List", searchMap));

        return searchMap;
    }
    
    /**
     * Top 하위레벨 메뉴조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap menuLevel2_ajax(SearchMap searchMap) {
        
    	searchMap.addList("menuLevel2List", getList("bsc.system.menuRight.getMenuLevel2List", searchMap));

        return searchMap;
    }
    
    /**
     * 사용자 그룹별 권한 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap menuRightModify(SearchMap searchMap) {
    	
    	searchMap.addList("topPgmNm", getStr("bsc.system.menuRight.getTopPgmNm", searchMap));
    	
    	searchMap.addList("subPgmNm", getStr("bsc.system.menuRight.getSubPgmNm", searchMap));

    	searchMap.addList("totList", getList("bsc.system.menuRight.getTotalMenuList", searchMap));
    	
    	searchMap.addList("selList", getList("bsc.system.menuRight.getSelectedMenuList", searchMap));
        
        return searchMap;
    }
    
    /**
     * 사용자 그룹별 권한 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap menuRightProcess(SearchMap searchMap) {
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
     * 사용자 그룹별 권한 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    

        String[] pgmIds = searchMap.getStringArray("pgmIds");
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("bsc.system.menuRight.deleteData", searchMap, true); //기존 권한 삭제
		        
	        if(null != pgmIds) {
		        for (int i = 0; i < pgmIds.length; i++) {
		        	searchMap.put("pgmId", pgmIds[i]);
		        	returnMap = insertData("bsc.system.menuRight.insertData", searchMap);
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
        
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }
    
    
}
