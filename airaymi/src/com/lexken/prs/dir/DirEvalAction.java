/*************************************************************************
* CLASS 명      : DirEvalAction
* 작 업 자      : 방승현
* 작 업 일      : 2013년 06월 24일 
* 기    능      : 임원평가-평가대상자 선정
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    방 승 현      2013년 06월 25일    최 초 작 업 
**************************************************************************/
package com.lexken.prs.dir;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.util.StaticUtil;

public class DirEvalAction extends CommonService {
	private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 평가대상자 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap dirEvalList(SearchMap searchMap) {
    	// 마감여부
    	searchMap.addList("leadershipYn", getDetail("prs.dir.dirEval.getList", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 평가대상자 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap dirEvalList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("prs.dir.dirEval.getList_xml", searchMap));

        return searchMap;
    }
    
    /**
     * 평가대상자선정 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap dirEvalProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("MOD".equals(stMode)) {
        	searchMap = updateDB(searchMap);
        } else if("CON".equals(stMode)) {
        	searchMap = updateDB2(searchMap);
        }
        
         return searchMap;
    }
    
    /**
     * 평가결과 입력
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
            String[] empns = searchMap.getStringArray("empn");
            String[] posTcs = searchMap.getStringArray("posTc");
            String[] leadershipScore1sts = searchMap.getStringArray("leadershipScore1st");
            String[] leadershipScore2nds = searchMap.getStringArray("leadershipScore2nd");
            String[] leadershipScores = searchMap.getString("leadershipScores").split("\\|", 0);
            
            for (int i = 0; i < empns.length; i++) {
            	if (null != empns[i]) {
            		searchMap.put("empn", empns[i]);
            		searchMap.put("leadershipScore1st", leadershipScore1sts[i]);
            		searchMap.put("leadershipScore2nd", leadershipScore2nds[i]);
            		
            		if (leadershipScores.length <= i)
            			searchMap.put("leadershipScore", "0");
            		else
            			searchMap.put("leadershipScore", leadershipScores[i]);

        			if (!"".equals(empns[i]) ) {
        				returnMap = updateData("prs.dir.dirEval.updateEvalData", searchMap);
        			}
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
     * 평가결과 입력 및 확정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB2(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
            String[] empns = searchMap.getStringArray("empn");
            String[] posTcs = searchMap.getStringArray("posTc");
            String[] leadershipScore1sts = searchMap.getStringArray("leadershipScore1st");
            String[] leadershipScore2nds = searchMap.getStringArray("leadershipScore2nd");
            String[] leadershipScores = searchMap.getString("leadershipScores").split("\\|", 0);
            String leadershipYn =  searchMap.getString("leadershipYn");
            
        	for(int i=0; i<empns.length; i++){
            	if(null != empns[i]){
            		searchMap.put("empn", empns[i]);            		
            		searchMap.put("posTc", posTcs[i]);            		
            		searchMap.put("leadershipScore1st", leadershipScore1sts[i]);            		
            		searchMap.put("leadershipScore2nd", leadershipScore2nds[i]);            		
            		searchMap.put("leadershipScore", leadershipScores[i]);

        			if(!"".equals(empns[i]) ){
        				returnMap = updateData("prs.dir.dirEval.updateEvalData", searchMap);
        			}
            	}
            }
            
            returnMap = updateData("prs.dir.dirEval.updateEvalConfirm", searchMap);
            
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
}
