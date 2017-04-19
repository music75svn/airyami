/*************************************************************************
* CLASS 명      : UserGrpAction
* 작 업 자      : 하윤식
* 작 업 일      : 2012년 6월 6일 
* 기    능      : 사용자그룹 매핑
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    하윤식      2012년 6월 6일            최 초 작 업 
**************************************************************************/
package com.lexken.bsc.system;
    
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

public class UserGrpAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 사용자그룹 매핑 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap userGrpList(SearchMap searchMap) {

        return searchMap;
    }
    
    /**
     * 사용자그룹 매핑 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap userGrpList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.system.userGrp.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 사용자그룹 매핑 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap userGrpModify(SearchMap searchMap) {
    	
    	searchMap.addList("treeList", getList("bsc.module.commModule.getDeptList", searchMap));
    	
        searchMap.addList("userList", getList("bsc.system.userGrp.getUserList", searchMap));
        
        return searchMap;
    }
    
    /**
     * 조직별 사용자 리스트 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap userList_ajax(SearchMap searchMap) {
    	
    	searchMap.addList("userList", getList("bsc.module.commModule.getUserList", searchMap));
        
        return searchMap;
    }
    
    
    /**
     * 사용자그룹 매핑 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap userGrpProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
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
     * 사용자그룹 매핑 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        setStartTransaction();
        
        String userId    = searchMap.getString("userIds");
        String[] userIds = searchMap.getString("userIds").split("\\|", 0);
        
        returnMap = updateData("bsc.system.userGrp.deleteData", searchMap, true); 
        
        if(!"".equals(userId)) {
	        for (int i = 0; i < userIds.length; i++) {
	        	searchMap.put("userId", userIds[i]);
	        	//searchMap.put("deptId", deptIds[i]);
	        	returnMap = insertData("bsc.system.userGrp.insertData", searchMap);
	        }
        }
        
        setEndTransaction();
        
        /**********************************
         * Return
         **********************************/
        searchMap.addList("returnMap", returnMap);
        return searchMap;    
    }
     
    
}
