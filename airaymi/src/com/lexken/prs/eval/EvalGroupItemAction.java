/*************************************************************************
* CLASS 명      : EvalGroupItemAction
* 작 업 자      : 박종호
* 작 업 일      : 2013년 05월 15일 
* 기    능      : 평가군_평가항목
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    박종호      2013년 05월 15일         최 초 작 업 
**************************************************************************/
package com.lexken.prs.eval;
    
import java.util.HashMap;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.SearchMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.util.StaticUtil;

public class EvalGroupItemAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 평가군_평가항목 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalGroupItemList(SearchMap searchMap) {
    	
    	if(!"".equals(StaticUtil.nullToBlank((String)searchMap.get("evalGrpId")))) {
			searchMap.put("findEvalGrpId", (String)searchMap.get("evalGrpId"));
		}
    	
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.get("year")))) {
			searchMap.put("year", (String)searchMap.get("findYear"));
		}
    	
    	if(!"".equals(StaticUtil.nullToBlank((String)searchMap.get("evalGrpNm")))) {
			searchMap.put("findEvalGrpNm", (String)searchMap.get("evalGrpNm"));
		}
    	
    	return searchMap;
    }
    
    /**
     * 평가군_평가항목 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalGroupItemList_xml(SearchMap searchMap) {
    	
        searchMap.addList("list", getList("prs.eval.evalGroupItem.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가군_평가항목 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalGroupItemModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	
    	searchMap.addList("evalScore", getDetail("prs.eval.evalGroupItem.getEvalScore", searchMap));//분류조회
    	
    	/**********************************
         * 평가방법
         **********************************/
    	searchMap.addList("evalMethodList", getList("prs.eval.evalItem.getEvalMethodList", searchMap));

    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("prs.eval.evalGroupItem.getDetail", searchMap));
    	}
        
    	
    	return searchMap;
    }
    
    /**
     * 평가군_평가항목 등록/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalGroupItemProcess(SearchMap searchMap) {
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
         * 등록
         **********************************/
        if("ADD".equals(stMode)) {
            searchMap = insertDB(searchMap);
        }else if("DEL".equals(stMode)) {
            searchMap = deleteDB(searchMap);
        }else if("MOD".equals(stMode)) {
    	searchMap = updateDB(searchMap);
        } 
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 평가군_평가항목 등록 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        
	        
	        setStartTransaction();
	        
	        //평가항목 등록
	        returnMap = insertData("prs.eval.evalGroupItem.insertItemData", searchMap);
	        //평가군 평가항목 매핑 등록
	        returnMap = insertData("prs.eval.evalGroupItem.insertMappingData", searchMap);
	        
	        
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
     * 평가항목 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] evalItemIds = searchMap.getString("evalItemIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != evalItemIds ) {
		        for (int i = 0; i < evalItemIds.length; i++) {
		            searchMap.put("evalItemId", evalItemIds[i]);
		            
		            //평가군-평가항목 매핑 삭제
		            returnMap = deleteData("prs.eval.evalGroupItem.deleteData", searchMap, true);
		            
		            //평가항목 삭제
		            returnMap = deleteData("prs.eval.evalGroupItem.deleteItemData", searchMap);
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
     * 평가항목 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("prs.eval.evalGroupItem.updateData", searchMap);
	        
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

        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }
    
}
