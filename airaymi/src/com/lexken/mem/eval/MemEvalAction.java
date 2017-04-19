/*************************************************************************
* CLASS 명      : MemEvalAction
* 작 업 자      : 유연주
* 작 업 일      : 2017년 03월24일 
* 기    능      : 업무수행평가
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    유연주      2017년 03월 24일          최 초 작 업 
**************************************************************************/
package com.lexken.mem.eval;
    
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginVO;

public class MemEvalAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 업무수행평가
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memEvalList(SearchMap searchMap) {
    	
    	// 평가기간조회
    	searchMap.put("year", searchMap.getString("findYear"));
    	searchMap.addList("periodInfo", getDetail("mem.eval.memEval.getPeriodInfo", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 업무수행평가 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memEvalList_xml(SearchMap searchMap) {
    	
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
    	if(!loginVO.chkAuthGrp("01")) {
    		searchMap.put("sEmpNo", loginVO.getUser_id());
    	}
        
    	searchMap.put("year", searchMap.getString("findYear"));
    	
        // 평가그룹별(1차평가자) 평가여부 조회
    	String evallYnByGroup = "";
        searchMap.put("evalUserGubunId", "01");
        evallYnByGroup = (String)getDetail("mem.base.memUser.getEvalYnByGroup", searchMap).get("EVAL_YN_ID");
        searchMap.put("eval1YnByGroup",evallYnByGroup);
    	
        // 평가그룹별(2차평가자) 평가여부 조회
    	String eval2YnByGroup = "";
        searchMap.put("evalUserGubunId", "02");
        eval2YnByGroup = (String)getDetail("mem.base.memUser.getEvalYnByGroup", searchMap).get("EVAL_YN_ID");
        searchMap.put("eval2YnByGroup",eval2YnByGroup);
        
        // 평가그룹별(동료평가자) 평가여부 조회
    	String peerYnByGroup = "";
        searchMap.put("evalUserGubunId", "03");
        peerYnByGroup = (String)getDetail("mem.base.memUser.getEvalYnByGroup", searchMap).get("EVAL_YN_ID");
        searchMap.put("peerYnByGroup",peerYnByGroup);
    	
        searchMap.addList("list", getList("mem.eval.memEval.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 업무수행 평가화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memEvalModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");

    	// 평가기간조회
    	searchMap.addList("periodInfo", getDetail("mem.eval.memEval.getPeriodInfo", searchMap));
    	
    	// 평가상태조회
    	searchMap.addList("stateInfo", getDetail("mem.eval.memEval.getStateInfo", searchMap));
    	
    	// 평가자비율 조회
    	searchMap.put("evalUserGubunId", searchMap.getString("evalGubun"));
        searchMap.addList("evalRate",getDetail("mem.base.memUser.getEvalYnByGroup", searchMap));
    	
    	List evalItemList = getList("mem.eval.memEval.getEvalItemList", searchMap);
    	
    	searchMap.addList("evalItemList", evalItemList);
    	
    	String itemCnt = "";
    	
    	if("03".equals(searchMap.getString("evalGubun"))){
    		// 동료평가자
    		itemCnt = (String)getDetail("mem.eval.memEval.getPeerMemberCount", searchMap).get("MEMBER_COUNT");
    	}else{
    		// 1, 2차평가자
    		itemCnt = (String)getDetail("mem.eval.memEval.getEvalMemberCount", searchMap).get("MEMBER_COUNT");
    	}
    	
    	searchMap.put("itemCnt", itemCnt);
    	List gradeList = getList("mem.eval.memEval.getEvalGrade", searchMap);
    	searchMap.addList("evalGrade", gradeList);
    	
    	
        return searchMap;
    }
    
    /**
     * 업무수행 평가화면 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memEvalModify_xml(SearchMap searchMap) {
    	
    	List itemList = getList("mem.eval.memEval.getEvalItemList", searchMap);
    	searchMap.addList("evalItemList", itemList);
    	
    	String[] itemArray = new String[0]; 
    	if (null != itemList && 0 < itemList.size()) {
    		itemArray = new String[itemList.size()];
    		for (int i = 0; i < itemList.size(); i++) {
    			HashMap map = (HashMap)itemList.get(i);
    			itemArray[i] = (String)map.get("EVAL_ITEM_ID"); 
    		}
    	}
    	
    	searchMap.put("itemArray", itemArray);
    	
    	if("03".equals(searchMap.getString("evalGubun"))){
    		// 동료평가자
    		searchMap.addList("list", getList("mem.eval.memEval.getPeerMemberList", searchMap));
    	}else{
    		// 1, 2차평가자
    		searchMap.addList("list", getList("mem.eval.memEval.getEvalMemberList", searchMap));
    	}

        return searchMap;
    }
    
    /**
     * 등급배분표
     * @param searchMap
     * @return
     */
    public SearchMap gradeItemList_xml(SearchMap searchMap) {
    	
    	String itemCnt = "";
    	
    	if("03".equals(searchMap.getString("evalGubun"))){
    		// 동료평가자
    		itemCnt = (String)getDetail("mem.eval.memEval.getPeerMemberCount", searchMap).get("MEMBER_COUNT");
    	}else{
    		// 1, 2차평가자
    		itemCnt = (String)getDetail("mem.eval.memEval.getEvalMemberCount", searchMap).get("MEMBER_COUNT");
    	}
    	
    	logger.debug("itemCnt : "+itemCnt);
    	
    	searchMap.put("itemCnt", itemCnt);
    	
    	List gradeList = getList("mem.eval.memEval.getEvalGrade", searchMap);
    	searchMap.addList("evalGrade", gradeList);
    	
    	String[] itemArray = new String[0]; 
    	if (null != gradeList && 0 < gradeList.size()) {
    		itemArray = new String[gradeList.size()];
    		for (int i = 0; i < gradeList.size(); i++) {
    			HashMap map = (HashMap)gradeList.get(i);
    			itemArray[i] = (String)map.get("GRADE_ITEM_ID"); 
    		}
    	}
    	searchMap.put("itemArray", itemArray);
    	
    	List evalItemGradeCount = getList("mem.eval.memEval.getEvalItemGradeCount", searchMap);
    	logger.debug("evalItemGradeCount : "+evalItemGradeCount);
    	searchMap.addList("list", evalItemGradeCount);

        return searchMap;
    }
    
    /**
     * 평가의견 팝업
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popEvalOpinion(SearchMap searchMap) {
    	
    	return searchMap;
    }
    
    /**
     * 업무수행평가 저장
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memEvalProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 무결성 체크
         **********************************/
        if(!"CANCEL".equals(stMode) && !"COMPLETE".equals(stMode) ) {
            returnMap = this.validChk(searchMap);
            
            if((Integer)returnMap.get("ErrorNumber") < 0 ){
                searchMap.addList("returnMap", returnMap);
                return searchMap;
            }
        }
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("COMPLETE".equals(stMode)) {
            searchMap = complateEvalDB(searchMap);
        }else if("CANCEL".equals(stMode)) {
        	searchMap = cancelEvalDB(searchMap);
        }else if("SUBMIT".equals(stMode)) {
        	searchMap = saveEvalDB(searchMap);
        }
        
        return searchMap;
    }
    
    /**
     * 저장
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap saveEvalDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        	logger.debug("searchMap : "+searchMap);
			String[] empNos = searchMap.getStringArray("empNos");
			
			// 직원평가 삭제
			returnMap = deleteData("mem.eval.memEval.deleteEvalEmpData", searchMap);
			
			// 평가항목 삭제
			returnMap = deleteData("mem.eval.memEval.deleteEvalItemData", searchMap);
			
			SearchMap empMap = new SearchMap();
			if(null != empNos && 0 < empNos.length) {
 		        for (int i = 0; i < empNos.length; i++) {
		            // 평가항목조회
		            List evalItemList = getList("mem.eval.memEval.getEvalItemList", searchMap);
		            SearchMap evalItemMap = new SearchMap();
		            
		            for(int j = 0; j < evalItemList.size(); j++){
		            	Map evalItemMapTmp = (Map)evalItemList.get(j);
		            	evalItemMap.put("year", searchMap.getString("year"));
		            	evalItemMap.put("evalGubun", searchMap.getString("evalGubun"));
		            	evalItemMap.put("evalEmpNo", searchMap.getString("evalEmpNo"));
		            	evalItemMap.put("empNo", empNos[i]);
		            	evalItemMap.put("detailEvalMetricId", evalItemMapTmp.get("EVAL_ITEM_ID"));
		            	evalItemMap.put("evalGradeId", searchMap.getString("evalGrade"+empNos[i]+"_"+evalItemMapTmp.get("EVAL_ITEM_ID")));
	 		        	
		            	// 평가항목등록
		            	returnMap = insertData("mem.eval.memEval.insertEvalItemData", evalItemMap);
		            }
		            
		        	empMap.put("year", searchMap.getString("year"));
 		        	empMap.put("evalGubun", searchMap.getString("evalGubun"));
 		        	empMap.put("evalEmpNo", searchMap.getString("evalEmpNo"));
 		        	empMap.put("empNo", empNos[i]);
 		        	empMap.put("evalOpinion", searchMap.getString("opinion"+empNos[i]));
 		        	empMap.put("addScore", searchMap.getString("extra"+empNos[i]));
 		        	
 		        	// 직원평가등록
		            returnMap = insertData("mem.eval.memEval.insertEvalEmpData", empMap);
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
     * 완료
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap complateEvalDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	// 저장먼저 처리
        	saveEvalDB(searchMap);
        	
	        // 평가완료처리
        	searchMap.put("evalState", "Y");
	        returnMap = updateData("mem.eval.memEval.updateEvalStateData", searchMap);
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
     * 완료취소
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap cancelEvalDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
	        // 평가취소처리
        	searchMap.put("evalState", "N");
	        returnMap = updateData("mem.eval.memEval.updateEvalStateData", searchMap);
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
     * 실적조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memEvalActSearchList(SearchMap searchMap) {
    	
    	// 업무성과기술서파일조회
    	searchMap.addList("jobDescDetail", getDetail("mem.eval.memJobDesc.getDetail", searchMap));
    	
    	// 자기평가정보 조회
    	searchMap.addList("selfEvalDetail", getDetail("mem.eval.memEval.getSelfEval", searchMap));
    	
    	// 대상자명 조회
    	searchMap.addList("empInfo", getDetail("mem.eval.memEval.getEmpNm", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 면담조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memEvalMeetSearchList(SearchMap searchMap) {
    	// 대상자명 조회
    	searchMap.addList("empInfo", getDetail("mem.eval.memEval.getEmpNm", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 면담조회 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memEvalMeetSearchList_xml(SearchMap searchMap) {
    	
    	searchMap.addList("list", getList("mem.eval.memMeet.getDetailList", searchMap));

        return searchMap;
    } 
    
    /**
     * 지표별 면담조회 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memMeetMetricList(SearchMap searchMap) {
    	
    	searchMap.addList("list", getList("mem.eval.memMeet.getEvalItemList", searchMap));
    	
    	searchMap.addList("totOpinion", getDetail("mem.eval.memMeet.getTotOpinionInfo", searchMap));

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
        
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }
}
