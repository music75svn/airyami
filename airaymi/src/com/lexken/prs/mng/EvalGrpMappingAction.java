/*************************************************************************
* CLASS 명      : evalGrpMappingAction
* 작 업 자      : 안요한
* 작 업 일      : 2013년 06월 04일 
* 기    능      : 평가군 매핑
* -------------------------------- 변 경 이 력 --------------------------------
* 번 호		작 업 자      	 작   업   일        변 경 내 용              비고
* -----		---------  		---------------  -------------------------	-------
*   1   	안 요 한		2013년 06월 04일      최 초 작 업 
**************************************************************************/
package com.lexken.prs.mng;
    
import java.util.ArrayList;
import java.util.HashMap;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.struts2.IsBoxActionSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.util.StaticUtil;

public class EvalGrpMappingAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 평가군 매핑 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalGrpMappingList(SearchMap searchMap) {

        return searchMap;
    }
    
    /**
     * 평가군 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalGrpMappingList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("prs.mng.evalGrpMapping.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가군 매핑 대상자 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalGrpMappingModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	searchMap.addList("insaUseList", getList("prs.mng.evalGrpMapping.getInsaUseList", searchMap));    	
        searchMap.addList("userList", getList("prs.mng.evalGrpMapping.getUseList", searchMap));
    	
        
        return searchMap;
    }
    
    /**
     * 평가군 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalGrpMappingProcess(SearchMap searchMap) {
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
     * 평가군 대상자관리 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        setStartTransaction();
        
        String empn    = searchMap.getString("empns");
        String[] empns = searchMap.getString("empns").split("\\|", 0);
        
        returnMap = deleteData("prs.mng.evalGrpMapping.deleteUseData", searchMap, true); 
        
        if(!"".equals(empn)) {
	        for (int i = 0; i < empns.length; i++) {
	        	searchMap.put("empn", empns[i]);
	        	returnMap = insertData("prs.mng.evalGrpMapping.insertUseData", searchMap);
	        	returnMap = updateData("prs.mng.evalGrpMapping.updateUseData", searchMap); //평가대상자 수정
	        }
        }
        
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
        
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }

}
