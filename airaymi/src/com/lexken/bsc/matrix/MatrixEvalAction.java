/*************************************************************************
* CLASS 명      : MatrixEvalAction
* 작 업 자      : 정철수
* 작 업 일      : 2012년 10월 23일 
* 기    능      : 평가실시
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    정철수      2012년 10월 23일         최 초 작 업 
**************************************************************************/
package com.lexken.bsc.matrix;
    
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
import com.lexken.framework.login.LoginVO;
import com.lexken.framework.util.StaticUtil;

public class MatrixEvalAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 평가실시 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap matrixEvalList(SearchMap searchMap) {
    	
    	searchMap.addList("evalInputTermList", getList("bsc.matrix.matrixEval.getEvalTermList", searchMap));
    	/**********************************
         * 디폴트 조회조건 설정(year)
         **********************************/
    	String findYear = (String)searchMap.get("findYear");
    	if("".equals(StaticUtil.nullToBlank(findYear))) {
    		searchMap.put("findYear", searchMap.getDefaultValue("evalInputTermList", "YEAR", 0));
    	}
    	
    	/**********************************
         * 로그인정보 (권한 체크)
         **********************************/
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
    	/**********************************
         * 디폴트 조회조건 설정(evalUserId)
         **********************************/
    	String findEvalUserId = (String)searchMap.get("findEvalUserId");
    	if(loginVO.chkAuthGrp("01") || loginVO.chkAuthGrp("60")) {  //전체관리자
	    	searchMap.addList("evalUserList", getList("bsc.matrix.matrixEval.getEvalUserList", searchMap));
	    	if("".equals(StaticUtil.nullToBlank(findEvalUserId))) {
	    		searchMap.put("findEvalUserId", searchMap.getDefaultValue("evalUserList", "EVAL_USER_ID", 0));
	    	}
    	}else {
    		if("".equals(StaticUtil.nullToBlank(findEvalUserId))) {
	    		searchMap.put("findEvalUserId", loginVO.getUser_id() );
	    	}
    	}
    	
    	
    	searchMap.addList("evalUserGrpList", getList("bsc.matrix.matrixEval.getEvalUserGrpList", searchMap));
    	/**********************************
         * 디폴트 조회조건 설정(evalUserGrpId)
         **********************************/
    	String findEvalUserGrpId = (String)searchMap.get("findEvalUserGrpId");
    	if("".equals(StaticUtil.nullToBlank(findEvalUserGrpId))) {
    		searchMap.put("findEvalUserGrpId", searchMap.getDefaultValue("evalUserGrpList", "EVAL_USER_GRP_ID", 0));
    	}
    	
    	
    	searchMap.addList("startegyList", getList("bsc.matrix.matrixEval.getStrategyList", searchMap));
    	
    	
    	searchMap.addList("evalInputTerm", getList("bsc.matrix.matrixEval.getEvalTerm", searchMap));
    	searchMap.addList("EVAL_START_DT", searchMap.getDefaultValue("evalInputTerm", "START_DT", 0) );
    	searchMap.addList("EVAL_END_DT", searchMap.getDefaultValue("evalInputTerm", "END_DT", 0) );
    	searchMap.addList("EVAL_TERM_YN", searchMap.getDefaultValue("evalInputTerm", "EVAL_TERM_YN", 0) );
    	
    	searchMap.addList("EVAL_SUBMIT_YN", getStr("bsc.matrix.matrixEval.getEvalSubmitYn", searchMap));
    	searchMap.addList("EVAL_CLOSING_YN", getStr("bsc.matrix.matrixEval.getEvalClosingYn", searchMap));
    	
        return searchMap;
    }
    
    
    /**
     * 평가자 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalUserList_ajax(SearchMap searchMap) {
        
    	searchMap.addList("evalUserList", getList("bsc.matrix.matrixEval.getEvalUserList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가단 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalUserGrp_ajax(SearchMap searchMap) {
        
    	searchMap.addList("evalUserGrpList", getList("bsc.matrix.matrixEval.getEvalUserGrpList", searchMap));

        return searchMap;
    }
    
    /**
     * 전략과제 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap strategyList_ajax(SearchMap searchMap) {
        
    	searchMap.addList("startegyList", getList("bsc.matrix.matrixEval.getStrategyList", searchMap));

        return searchMap;
    }
    
    
    /**
     * 평가실시 결과 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap matrixEvalList_xml(SearchMap searchMap) {
        
    	String[][] convertArray =  {{"Y", "완료"} , {"N","미완료"}};
    	
    	         
        searchMap.addList("list", getList("bsc.matrix.matrixEval.getList", searchMap));
        searchMap.addList("CONVERT_ARRAY", convertArray);
        
        return searchMap;
    }
    
    
    /**
     * 평가실시결과 데이터 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap matrixEvalResultList(SearchMap searchMap) {
        
        searchMap.addList("itemIdList", getList("bsc.matrix.matrixEval.getItemList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가실시결과 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap matrixEvalResultList_xml(SearchMap searchMap) {
        
    	ArrayList itemList = (ArrayList)getList("bsc.matrix.matrixEval.getItemList", searchMap);
      	 
    	String[] ItemIdArray = new String[0]; 
    	if(null != itemList && 0 < itemList.size()) {
    		ItemIdArray = new String[itemList.size()];
    		for(int i=0; i<itemList.size(); i++) {
    			HashMap map = (HashMap)itemList.get(i);
    			ItemIdArray[i] = (String)map.get("ITEM_ID"); 
    		}
    	}
    	searchMap.put("itemIdList", ItemIdArray);
    	searchMap.addList("itemIdList", itemList);
    	
        searchMap.addList("list", getList("bsc.matrix.matrixEval.getResultList", searchMap));

        return searchMap;
    }
    
    
    /**
     * 평가실시 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap matrixEvalModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("stMode");
    	String isEvalSubmit = searchMap.getString("isEvalSubmit");
    	
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getList("bsc.matrix.matrixEval.getDetail", searchMap));
    		searchMap.addList("itemScore", getList("bsc.matrix.matrixEval.getItemScore", searchMap));
    		
    		searchMap.addList("searchMapMetricGrpNm", searchMap.getDefaultValue("detail", "METRIC_GRP_NM", 0) );
    		searchMap.addList("IS_EVAL_SUBMIT", isEvalSubmit );
    	}
    	
        return searchMap;
    }
    
    /**
     * 평가실시 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap matrixEvalProcess(SearchMap searchMap) {
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
         * 평가 저장/제출
         **********************************/
        if("MOD".equals(stMode)) {
            searchMap = updateDB(searchMap);
        } else if("SUBMIT".equals(stMode)) {
            searchMap = submitDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    
    /**
     * 평가실시 저장
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        String modYear = (String)searchMap.getString("modYear", "");
	        String modEvalUserGrpId = (String)searchMap.getString("modEvalUserGrpId", "");
	        String modEvalUserId = (String)searchMap.getString("modEvalUserId", "");
	        String modMetricGrpId = (String)searchMap.getString("modMetricGrpId", "");
	        String modItemIds = (String)searchMap.getString("modItemIds", "");
	        String modItemScores = (String)searchMap.getString("modItemScores", "");
	        
	        
	        returnMap = updateData("bsc.matrix.matrixEval.deleteData", searchMap, true);
	        
	        
	        String[] modItemId = modItemIds.split("\\|", 0);
	        String[] modItemScore = modItemScores.split("\\|", 0);
	        
	        if( null != modItemId && 0 < modItemId.length){
	        	searchMap.put("modYear", modYear);
		        searchMap.put("modEvalUserGrpId", modEvalUserGrpId); 
		        searchMap.put("modEvalUserId", modEvalUserId); 
		        searchMap.put("modMetricGrpId", modMetricGrpId); 
		        
		        for (int i = 0; i < modItemId.length; i++) {
		            searchMap.put("modItemId", modItemId[i]); 
		            searchMap.put("modItemScore", modItemScore[i]);
		            returnMap = insertData("bsc.matrix.matrixEval.updateData", searchMap);
		        }
	        }
	        searchMap.addList("findModMetricGrpId", modMetricGrpId );
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
     * 평가제출
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap submitDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        returnMap = insertData("bsc.matrix.matrixEval.evalSubmitData", searchMap);
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

    /**
     * 평가실시 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap matrixEvalResultListExcel(SearchMap searchMap) {
    	String excelFileName = "평가내역 리스트";
    	String excelTitle = "평가내역 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("popupYear")));
    	excelSearchInfoList.add(new ExcelVO("평가단", (String)searchMap.get("popupEvalUserGrpNm")));
    	excelSearchInfoList.add(new ExcelVO("평가자", (String)searchMap.get("popupEvalUserNm")));
    	
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	
    	excelInfoList.add(new ExcelVO("전략과제 명", "STRATEGY_NM", "left"));
    	excelInfoList.add(new ExcelVO("POOL 구분", "POOL_GUBUN_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가대상지표 POOL 명", "METRIC_GRP_NM", "left"));
    	ArrayList itemList = (ArrayList)getList("bsc.matrix.matrixEval.getItemList", searchMap);
    	 
    	String[] ItemIdArray = new String[0]; 
    	if(null != itemList && 0 < itemList.size()) {
    		ItemIdArray = new String[itemList.size()];
    		
    		String headerId = "";
    		String headerNm = "";
    		
    		for(int i=0; i<itemList.size(); i++) {
    			HashMap map = (HashMap)itemList.get(i);
    			ItemIdArray[i] = (String)map.get("ITEM_ID"); 
    			
    			headerId = (String)map.get("ITEM_ID");
    			headerNm = (String)map.get("ITEM_NM") + "(" + (map.get("ITEM_WEIGHT")) + ")";
    			if( headerNm.equals("TOTAL") ){
    				headerNm = ((String)map.get("ITEM_GRP_NM")).replace("ALL","전체") + " 평점" + "(" + (map.get("ITEM_WEIGHT")) + ")";
    			}
    			excelInfoList.add(new ExcelVO( headerNm, headerId, "left"));
    		}
    	}
    	searchMap.put("itemIdList", ItemIdArray);
    	
 
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	
    	searchMap.addList("itemIdList", itemList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.matrix.matrixEval.getResultList", searchMap));
    	
        return searchMap;
    }
    
}
