/*************************************************************************
* CLASS 명      : MemEvalGroupAction
* 작 업 자      : 유연주
* 작 업 일      : 2017년 03월 10일 
* 기    능      : 평가그룹
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    유연주      2017년 03월 10일          최 초 작 업 
**************************************************************************/
package com.lexken.mem.base;
    
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.util.StaticUtil;

public class MemEvalGroupAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 평가그룹 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memEvalGroupList(SearchMap searchMap) {
    	
    	return searchMap;
    }
    
    /**
     * 평가그룹 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memEvalGroupList_xml(SearchMap searchMap) {
    	
        
        searchMap.addList("list", getList("mem.base.memEvalGroup.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가그룹 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memEvalGroupModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("mem.base.memEvalGroup.getDetail", searchMap));
    	}
        
    	searchMap.put("findYear", searchMap.getString("year"));
    	searchMap.put("findCodeGrpId", "170");
    	searchMap.addList("castTcList", getList("bsc.base.code.getList", searchMap));
    	searchMap.put("findCodeGrpId", "171");
    	searchMap.addList("posTcList", getList("bsc.base.code.getList", searchMap));
    	searchMap.put("findCodeGrpId", "236");
    	searchMap.addList("yearGubunTcList", getList("bsc.base.code.getList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 평가그룹 대상 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memEvalGroupModifyList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("mem.base.memEvalGroup.getTargetList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가그룹 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memEvalGroupProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 무결성 체크
         **********************************/
        if(!"DEL".equals(stMode) && !"SAVE".equals(stMode) && !"MODUSER".equals(stMode) ) {
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
        } else if("SAVE".equals(stMode)) {
            searchMap = allSaveDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 평가그룹 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	// 평가그룹ID 생성
        	HashMap evalGrpIdMap = getDetail("mem.base.memEvalGroup.getEvalGrpId", searchMap);
        	searchMap.put("evalGrpId", evalGrpIdMap.get("EVAL_GRP_ID"));
        	
	        // 평가그룹 등록
	        returnMap = insertData("mem.base.memEvalGroup.insertData", searchMap);
	        
	        // 평가그룹대상 삭제
	        returnMap = deleteData("mem.base.memEvalGroup.deleteTargetData", searchMap, true);
	        
	        // 평가그룹대상 등록
			String[] castTcs = searchMap.getString("gCastTcs").split("\\|", -1);
			String[] posTcs = searchMap.getString("gPosTcs").split("\\|", -1);
			String[] yearTcs = searchMap.getString("gYearTcs").split("\\|", -1);
			String[] yearTcGubuns = searchMap.getString("gYearTcGubuns").split("\\|", -1);
			String[] applyDts = searchMap.getString("gApplyDts").split("\\|", -1);
			
	        if(null != castTcs && 0 < castTcs.length) {
		        for (int i = 0; i < castTcs.length - 1; i++) {
		            searchMap.put("castTc", castTcs[i]);
		            searchMap.put("posTc", posTcs[i]);
		            searchMap.put("yearTc", yearTcs[i]);
		            searchMap.put("yearTcGubun", yearTcGubuns[i]);
		            searchMap.put("applyDt", applyDts[i]);

		            returnMap = updateData("mem.base.memEvalGroup.insertTargetData", searchMap);
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
     * 평가그룹 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        // 평가그룹 수정
	        returnMap = updateData("mem.base.memEvalGroup.updateData", searchMap);
	        
	        // 평가그룹대상 삭제
	        returnMap = deleteData("mem.base.memEvalGroup.deleteTargetData", searchMap, true);
	        
	        // 평가그룹대상 등록
			String[] castTcs = searchMap.getString("gCastTcs").split("\\|",-1);
			String[] posTcs = searchMap.getString("gPosTcs").split("\\|", -1);
			String[] yearTcs = searchMap.getString("gYearTcs").split("\\|", -1);
			String[] yearTcGubuns = searchMap.getString("gYearTcGubuns").split("\\|", -1);
			String[] applyDts = searchMap.getString("gApplyDts").split("\\|", -1);
	        
	        if(null != castTcs && 0 < castTcs.length) {
		        for (int i = 0; i < castTcs.length - 1; i++) {
		            searchMap.put("castTc", castTcs[i]);
		            searchMap.put("posTc", posTcs[i]);
		            searchMap.put("yearTc", yearTcs[i]);
		            searchMap.put("yearTcGubun", yearTcGubuns[i]);
		            searchMap.put("applyDt", applyDts[i]);
		            returnMap = updateData("mem.base.memEvalGroup.insertTargetData", searchMap);
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
     * 평가그룹 정렬순서 일괄 저장 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap allSaveDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
			String[] evalGrpIds = searchMap.getString("evalGrpIds").split("\\|", -1);
			String[] sortOrders = searchMap.getString("sortOrders").split("\\|", -1);
	        
	        setStartTransaction();
	        
	        logger.debug("sortOrders count : "+sortOrders.length);
	        logger.debug("sortOrders : "+searchMap.getString("sortOrders"));
	        
	        if(null != evalGrpIds && 0 < evalGrpIds.length) {
		        for (int i = 0; i < evalGrpIds.length - 1; i++) {
		            searchMap.put("sortOrder", sortOrders[i]);
		            logger.debug("sortOrders[i] : "+sortOrders[i]);
		            searchMap.put("evalGrpId", evalGrpIds[i]);
		            returnMap = updateData("mem.base.memEvalGroup.allSaveData", searchMap);
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
     * 평가그룹 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
			String[] evalGrpIds = searchMap.getString("evalGrpIds").split("\\|", -1);
	        
	        setStartTransaction();
	        
	        if(null != evalGrpIds && 0 < evalGrpIds.length) {
		        for (int i = 0; i < evalGrpIds.length - 1; i++) {
		        	searchMap.put("evalGrpId", evalGrpIds[i]);
		            returnMap = updateData("mem.base.memEvalGroup.deleteData", searchMap);
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
        String stMode = searchMap.getString("mode");
        
    	returnMap = ValidationChk.lengthCheck(searchMap.getString("evalGrpNm"), "평가그룹", 1, 50);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
    	returnMap = ValidationChk.lengthCheck(searchMap.getString("sortOrder"), "정렬순서", 0, 5);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }
}
