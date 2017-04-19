/*************************************************************************
* CLASS 명      : CodeAction
* 작 업 자      : 하윤식
* 작 업 일      : 2012년 5월 17일 
* 기    능      : 공통코드관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    하윤식      2012년 5월 17일            최 초 작 업 
**************************************************************************/
package com.lexken.bsc.base;
    
import java.util.HashMap;

import com.lexken.framework.codeUtil.CodeUtil;
import com.lexken.framework.codeUtil.CodeUtilReLoad;
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

public class CodeAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 공통코드 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap codeList(SearchMap searchMap) {

    	if("".equals(StaticUtil.nullToBlank((String)searchMap.get("findCodeGrpId")))) {
			searchMap.put("findCodeGrpId", (String)searchMap.get("codeGrpId"));
		}
    	
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.get("codeGrpId")))) {
			searchMap.put("codeGrpId", (String)searchMap.get("findCodeGrpId"));
		}
    	
        searchMap.addList("codeGrpDatail", getDetail("bsc.base.codeGrp.getDetail", searchMap));
        
        return searchMap;
    }
    
    /**
     * 공통코드 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap codeList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.base.code.getList", searchMap));
        
        return searchMap;
    }
    
    /**
     * 공통코드 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap codeModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
        searchMap.addList("codeGrpDatail", getDetail("bsc.base.codeGrp.getDetail", searchMap));

        if("MOD".equals(stMode)) {
        	searchMap.addList("detail", getDetail("bsc.base.code.getDetail", searchMap));
        }
        
        return searchMap;
    }
    
    /**
     * 코드 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap codeProcess(SearchMap searchMap) {
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
        
        /*****************************************
		 * 세션에 등록되어 있는 코드 정보 reflash
		 *****************************************/
		CodeUtilReLoad codeUtilReLoad = new CodeUtilReLoad();
		codeUtilReLoad.codeUtilReLoad();
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
   
    
    /**
     * 코드 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        	
	        /**********************************
	         * 코드ID 등록여부 확인
	         **********************************/
	        int cnt = getInt("bsc.base.code.getCodeCount", searchMap);
	        
	        if(0 < cnt) {
	            returnMap.put("ErrorNumber", ErrorMessages.FAILURE_DUP2_CODE);
	            returnMap.put("ErrorMessage", ErrorMessages.format(ErrorMessages.FAILURE_DUP2_MESSAGE, "코드ID"));
	        } else {
	        	
	        	String codeDefId = searchMap.getString("codeDefId");
	        	
	        	if("01".equals(codeDefId)) {
	        		/**********************************
	    	         * 코드ID 채번
	    	         **********************************/
			        String codeId = getStr("bsc.base.code.getCodeId", searchMap);
			        searchMap.put("codeId", codeId);
	        	}
		        
	            returnMap = insertData("bsc.base.code.insertData", searchMap);
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
     * 공통코드 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = updateData("bsc.base.code.updateData", searchMap);
        
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
     * 공통코드 삭제 
     * @param
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    

        try {
        	setStartTransaction();
        	
	        String[] codeIds = searchMap.getString("codeIds").split("\\|", 0);
	        
	        for (int i=0; i < codeIds.length; i++) {
	            searchMap.put("codeId", codeIds[i]);
	            returnMap = updateData("bsc.base.code.deleteData", searchMap);
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
        int     resultValue       = 0;
        
        if( "02".equals(searchMap.getString("codeDefId"))) {
        	returnMap = ValidationChk.lengthCheck(searchMap.getString("codeId"), "코드ID", 1, 7);
            if((Integer)returnMap.get("ErrorNumber") < 0 ){
                return returnMap;
            }
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("codeNm"), "코드", 1, 75);
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
            return returnMap;
        }
        
        returnMap = ValidationChk.onlyNumber(searchMap.getString("sortOrder"), "정렬순서");
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
        	return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("etc1"), "ETC1", 0, 75);
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
        	return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("etc2"), "ETC2", 0, 75);
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
        	return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("content"), "비고", 0, 1000);
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
        	return returnMap;
        }
        
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }
    
    
}
