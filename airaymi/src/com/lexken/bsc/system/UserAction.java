/*************************************************************************
* CLASS 명      : UserAction
* 작 업 자      : 정철수
* 작 업 일      : 2012년 6월 7일 
* 기    능      : 사용자관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    정철수      사용자관리            최 초 작 업 
**************************************************************************/
package com.lexken.bsc.system;
    
import java.util.ArrayList;
import java.util.HashMap;

import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.struts2.IsBoxActionSupport;
import com.lexken.framework.util.StaticUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.login.LoginVO;

public class UserAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 사용자관리 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap userList(SearchMap searchMap) {
    	
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	String findDeptOpt = StaticUtil.nullToDefault((String) searchMap.get("findDeptOpt"),"");
    	String findDeptId = StaticUtil.nullToDefault((String) searchMap.get("findDeptId"),"");
    	
    	if("".equals(findDeptId) && "".equals(findDeptOpt)){
    		searchMap.put("findDeptId", loginVO.getDept_id());
    		searchMap.put("findDeptNm", loginVO.getDept_nm());
    	}
    	
    	/*
    	String isPageFirstLoadingYN = (String)searchMap.getString("isPageFirstLoadingYN", "");
    	if("Y".equals( isPageFirstLoadingYN ) ){
	    	ArrayList tmpList = (ArrayList)getList("bsc.module.commModule.getTopDeptInfo", searchMap);
	    	
	    	if(null != tmpList && 0 < tmpList.size() ){
	    		HashMap tmpHash = (HashMap)tmpList.get(0);
	    		searchMap.put("findDeptNm", (String)tmpHash.get("DEPT_NM") );
	    		searchMap.put("isPageFirstLoadingYN", "N");
	    	}
    	}
    	*/
    	
        return searchMap;
    }
    
    /**
     * 사용자관리 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap userList_xml(SearchMap searchMap) {
    	
        searchMap.addList("list", getList("bsc.system.user.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 사용자관리 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap userModify(SearchMap searchMap) {

        searchMap.addList("detail", getDetail("bsc.system.user.getDetail", searchMap));
        
        return searchMap;
    }
    
    /**
     * 사용자관리 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap userProcess(SearchMap searchMap) {
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
     * 사용자관리 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        setStartTransaction();
        
        returnMap = insertData("bsc.system.user.insertData", searchMap);
        
        setEndTransaction();
        
        /**********************************
         * Return
         **********************************/
        searchMap.addList("returnMap", returnMap);
        return searchMap;    
    }
    
    /**
     * 사용자관리 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        setStartTransaction();
        
        returnMap = updateData("bsc.system.user.updateData", searchMap);
        
        setEndTransaction();
        
        /**********************************
         * Return
         **********************************/
        searchMap.addList("returnMap", returnMap);
        return searchMap;    
    }
    
    /**
     * 사용자관리 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
        //PK 부분 추가
        /*
        String userIds = searchMap.getString("userIds");
        String[] keyArray = userIds.split("\\|", 0);
 
        setStartTransaction();
        
        for (int i=0; i<keyArray.length; i++) {
            searchMap.put("userId", keyArray[i]);
            returnMap = updateData("bsc.system.user.deleteData", searchMap);
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
