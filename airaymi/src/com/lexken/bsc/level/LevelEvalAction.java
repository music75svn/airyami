/*************************************************************************
* CLASS 명      : LevelEvalAction
* 작 업 자      : 현걸욱
* 작 업 일      : 2012년 10월 26일 
* 기    능      : 평가실시
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    현걸욱      2012년 10월 26일         최 초 작 업 
**************************************************************************/
package com.lexken.bsc.level;
    
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

public class LevelEvalAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 평가실시 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap levelEvalList(SearchMap searchMap) {

    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
    	searchMap.addList("evalInputTermList", getList("bsc.level.levelEval.getEvalTerm", searchMap));
    	/**********************************
         * 디폴트 조회조건 설정(year)
         **********************************/
    	String findYear = (String)searchMap.get("findYear");
    	if("".equals(StaticUtil.nullToBlank(findYear))) {
    		searchMap.put("findYear", searchMap.getDefaultValue("evalInputTermList", "YEAR", 0));
    	}
    	
    	
    	/**********************************
         * 디폴트 조회조건 설정(evalUserId)
         **********************************/
    	
    	String findEvalUserId = "";
    	
    	if(loginVO.chkAuthGrp("07")) {
        	searchMap.put("authUserId",loginVO.getUser_id());
    	}
    	
    	searchMap.addList("evalUserList", getList("bsc.level.levelEval.getEvalUserList", searchMap));
		findEvalUserId = (String)searchMap.get("findEvalUserId");
    	if("".equals(StaticUtil.nullToBlank(findEvalUserId))) {
    		searchMap.put("findEvalUserId", (String)searchMap.getDefaultValue("evalUserList", "EVAL_USER_ID", 0));
    		//searchMap.put("evalUserId", (String)searchMap.getDefaultValue("evalUserList", "EVAL_USER_ID", 0));
    	} 
    	
    	
    	searchMap.addList("evalUserGrpList", getList("bsc.level.levelEval.getEvalUserGrpList", searchMap));
    	
    	/**********************************
         * 디폴트 조회조건 설정(evalUserGrpId)
         **********************************/
    	String findEvalUserGrpId = (String)searchMap.get("findEvalUserGrpId");
    	if("".equals(StaticUtil.nullToBlank(findEvalUserGrpId))) {
    		searchMap.put("findEvalUserGrpId", (String)searchMap.getDefaultValue("evalUserGrpList", "EVAL_USER_GRP_ID", 0));
    		//searchMap.put("evalUserGrpId", (String)searchMap.getDefaultValue("evalUserGrpList", "EVAL_USER_GRP_ID", 0));
    	}
    	
    	searchMap.addList("EVAL_START_DT", searchMap.getDefaultValue("evalInputTermList", "START_DT", 0) );
    	searchMap.addList("EVAL_END_DT", searchMap.getDefaultValue("evalInputTermList", "END_DT", 0) );
    	searchMap.addList("EVAL_TERM_YN", searchMap.getDefaultValue("evalInputTermList", "EVAL_TERM_YN", 0) );
    	
    	//난이도평가마감구분 마감'Y'
		searchMap.addList("evalCloseYn", getStr("bsc.level.levelEval.getEvalCloseYn", searchMap));
		//난이도평가제출구분 제출'Y'
		searchMap.addList("evalSubmitYn", getStr("bsc.level.levelEval.getEvalSubmitYn", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 평가실시 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap levelEvalList_xml(SearchMap searchMap) {
        
    	String[][] convertArray =  {{"Y", "완료"} , {"N","미완료"}};
        
    	searchMap.addList("list", getList("bsc.level.levelEval.getList", searchMap));
        searchMap.addList("CONVERT_ARRAY", convertArray);
    	

        return searchMap;
    }
    
    /**
     * 평가항목상세 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popLevelEvalList(SearchMap searchMap) {
        
    	searchMap.addList("itemList", getList("bsc.level.levelEval.getEvalItemDetailList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 평가항목상세 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popLevelEvalList_xml(SearchMap searchMap) {
    	
    	ArrayList itemList = (ArrayList)getList("bsc.level.levelEval.getEvalItemDetailList", searchMap);
    	
    	String[] ItemIdArray = new String[0]; 
    	if(null != itemList && 0 < itemList.size()) {
    		ItemIdArray = new String[itemList.size()];
    		for(int i=0; i<itemList.size(); i++) {
    			HashMap map = (HashMap)itemList.get(i);
    			ItemIdArray[i] = (String)map.get("ITEM_ID"); 
    		}
    	}
    	searchMap.put("itemIdList", ItemIdArray);
    	searchMap.addList("itemList", itemList);
    	
    	searchMap.addList("evallevelEvalList", getList("bsc.level.levelEval.getPopList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가실시 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popLevelItemDetail(SearchMap searchMap) {
        
        return searchMap;
    }
    
    /**
     * 평가실시 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popLevelItemDetail_xml(SearchMap searchMap) {
        
    	searchMap.addList("evalItemDetailList", getList("bsc.level.levelEval.getEvalItemDetailList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가단 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalUserGrp_ajax(SearchMap searchMap) {
        
    	searchMap.addList("evalUserGrpList", getList("bsc.level.levelEval.getEvalUserGrpList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가단 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap levelEvalUser_ajax(SearchMap searchMap) {
        
    	searchMap.addList("evalUserList", getList("bsc.level.levelEval.getEvalUserList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가실시 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap levelEvalModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	
    	searchMap.put("detailMetricNm", getStr("bsc.level.levelEval.getMetricNm", searchMap));
        
    	searchMap.addList("detail", getList("bsc.level.levelEval.getDetail", searchMap));
        
        return searchMap;
    }
    
    /**
     * 평가실시 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap levelEvalProcess(SearchMap searchMap) {
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
        if("MOD".equals(stMode)) {
            searchMap = updateDB(searchMap);
        } else if("SUB".equals(stMode)) {
            searchMap = submitDB(searchMap);
        } else if("RESUB".equals(stMode)) {
            searchMap = rejectSubmitDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 평가실시 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        /**********************************
         * 구간대
         **********************************/
        String[] itemIds = searchMap.getStringArray("itemIds");
        String modItemIds= (String) searchMap.getString("modItemIds");
        String metricId= (String) searchMap.getString("metricId");
        String evalUserMetricId= (String) searchMap.getString("evalUserMetricId");
        String evalUserId= (String) searchMap.getString("evalUserId");
        String itemId = "";
        
        String[] modItemId = modItemIds.split("\\|", 0);
        
        try {
        	
        	setStartTransaction();
        	
        	/**********************************
             * 평가항목 점수  삭제
             **********************************/
	        returnMap = updateData("bsc.level.levelEval.deleteItemScore", searchMap, true);

	        /**********************************
	         * 평가점수 항목 등록
	         **********************************/
	        
	        if(0<itemIds.length){
	        	
	        	searchMap.put("metricId", metricId);
	        	searchMap.put("findMetricId", metricId);
	        	searchMap.put("evalUserMetricId", evalUserMetricId);
	        	searchMap.put("evalUserId", evalUserId);
	        	
	        	
	        	for(int i=0 ; i<itemIds.length ; i++){
	        		
	        		 searchMap.put("score", itemIds[i]);
	        		 searchMap.put("itemId", modItemId[i]);
	        		 returnMap = insertData("bsc.level.levelEval.insertItemScore", searchMap);
	        	}
	        	
	        }
	       
        } catch (Exception e) {
        	logger.error(e.toString());
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
	        returnMap = updateData("bsc.level.levelEval.submitData", searchMap);
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
    public SearchMap rejectSubmitDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        returnMap = updateData("bsc.level.levelEval.rejectSubmitData", searchMap);
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
    public SearchMap levelEvalListExcel(SearchMap searchMap) {
    	String excelFileName = "평가실시";
    	String excelTitle = "평가실시 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	/*
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("공통코드명", StaticUtil.nullToDefault((String)searchMap.get("codeGrpNm"), "전체")));
    	excelSearchInfoList.add(new ExcelVO("사용여부", (String)searchMap.get("useYnNm")));
    	*/
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가년도", "YEAR", "left"));
    	excelInfoList.add(new ExcelVO("평가단 ID", "EVAL_USER_GRP_ID", "left"));
    	excelInfoList.add(new ExcelVO("평가자 ID", "EVAL_USER_ID", "left"));
    	excelInfoList.add(new ExcelVO("지표코드", "METRIC_ID", "left"));
    	excelInfoList.add(new ExcelVO("평가항목 ID", "ITEM_ID", "left"));
    	excelInfoList.add(new ExcelVO("점수", "SCORE", "left"));
    	excelInfoList.add(new ExcelVO("생성일", "CREATE_DT", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.level.levelEval.getList", searchMap));
    	
        return searchMap;
    }
    
}
