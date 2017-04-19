/*************************************************************************
* CLASS 명      : MessageAction
* 작 업 자      : 안요한
* 작 업 일      : 2013년 8월 10일 
* 기    능      : 메세지 보내기
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  --------  -----------------  -------------------------    --------
*   1    안요한      2013년 8월 10일             최 초 작 업 
**************************************************************************/
package com.lexken.bsc.message;
    
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.NoteSendManager;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginVO;
import com.lexken.framework.util.StaticUtil;

public class MessageAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 메세지 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap messageList(SearchMap searchMap) {
    	/**********************************
         * 로그인정보 (권한 체크)
         **********************************/
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
    	String findYear = (String)searchMap.get("findYear");
    	
		if( "".equals(StaticUtil.nullToBlank(findYear)) ) {
    		searchMap.put("findYear", (String)searchMap.get("year") );
    	}
    	
        return searchMap;
    }
    
    /**
     * 메세지 사용자 데이터 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap userList(SearchMap searchMap) {
        
    	searchMap.addList("treeList", getList("bsc.message.message.getDeptList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 조직별 사용자 리스트 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap userInfo_ajax(SearchMap searchMap) {
    	
    	searchMap.addList("userList", getList("bsc.message.message.getUserList", searchMap));;
        
        return searchMap;
    }
    
    /**
     * 메세지 보내기 입력/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap messageProcess(SearchMap searchMap) {
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
        
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;
    }
    
    
    /**
     * 메세지 보내기 입력/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popMessageProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("SEND".equals(stMode)) {
            searchMap = insertDB(searchMap);
        } 
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 메세지 발송
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
    	NoteSendManager send = new NoteSendManager();
    	
    	String recipientId = searchMap.getString("recipientId");
    	String content = searchMap.getString("content");
    	
    	String[] recipientIds  = recipientId.split(",");
    	
    	for(int i=0; i<recipientIds.length; i++) {
    		searchMap.put("targetId", recipientIds[i]);
        	searchMap.put("message", content);
        	
        	send.NoteSend(searchMap);
    	}
    	
        return searchMap;    
    }
    
}
