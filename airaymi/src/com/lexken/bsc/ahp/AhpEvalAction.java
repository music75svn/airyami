/*************************************************************************
* CLASS 명      : AhpEvalAction
* 작 업 자      : 하윤식
* 작 업 일      : 2012년 11월 29일 
* 기    능      : AHP 평가실시
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    하윤식      2012년 11월 29일         최 초 작 업 
**************************************************************************/
package com.lexken.bsc.ahp;
    
import java.util.ArrayList;
import java.util.HashMap;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.StringConstants;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.struts2.IsBoxActionSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.login.LoginVO;
import com.lexken.framework.util.StaticUtil;

public class AhpEvalAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * AHP 평가실시 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap ahpEvalList(SearchMap searchMap) {
    	/**********************************
         * 로그인정보 (권한 체크)
         **********************************/
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
    	/**********************************
         * 평가자 조회
         **********************************/
    	searchMap.addList("evalUserList", getList("bsc.ahp.ahpEval.getEvalUserList", searchMap));
    	
    	/**********************************
         * evalUserId 디폴트 조회조건 설정
         **********************************/
    	String findEvalUserId = (String)searchMap.get("findEvalUserId");
    	
    	if(loginVO.chkAuthGrp("01") || loginVO.chkAuthGrp("60")) {
    		if("".equals(StaticUtil.nullToBlank(findEvalUserId))) {
        		searchMap.put("findEvalUserId", searchMap.getDefaultValue("evalUserList", "EVAL_USER_ID", 0));
        	}
    	} else {
    		searchMap.put("findEvalUserId", loginVO.getUser_id());
    	}
    	
    	
    	/**********************************
         * 평가단 조회
         **********************************/
    	searchMap.addList("evalUserGrpList", getList("bsc.ahp.ahpEval.getEvalUserGrpList", searchMap));
    	
    	/**********************************
         * 평가일정 조회
         **********************************/
    	searchMap.addList("evalTerm", getDetail("bsc.ahp.ahpEval.getEvalTerm", searchMap));
    	
        return searchMap;
    }
    
    /**
     * AHP 평가자 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap ahpEvalUserList_ajax(SearchMap searchMap) {
        
    	searchMap.addList("evalUserList", getList("bsc.ahp.ahpEval.getEvalUserList", searchMap));

        return searchMap;
    }
    
    /**
     * AHP 평가단 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap ahpEvalUserGrpList_ajax(SearchMap searchMap) {
        
    	searchMap.addList("evalUserGrpList", getList("bsc.ahp.ahpEval.getEvalUserGrpList", searchMap));

        return searchMap;
    }
    
    /**
     * AHP 평가실시 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap ahpEvalList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.ahp.ahpEval.getList", searchMap));

        return searchMap;
    }
    
    /**
     * AHP 평가실시 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap ahpEvalModify(SearchMap searchMap) {
        
    	/************************************************************************************
    	 * 평가자조회
    	 ************************************************************************************/
    	searchMap.addList("evalUserNm", getStr("bsc.ahp.ahpEval.getEvalUserNm", searchMap));
    	
    	/************************************************************************************
    	 * 평가단조회
    	 ************************************************************************************/
    	searchMap.addList("evalUserGrpNm", getStr("bsc.ahp.ahpEval.getEvalUserGrpNm", searchMap));
    	
    	/************************************************************************************
    	 * 평가대상그룹조회
    	 ************************************************************************************/
    	searchMap.addList("itemGrpNm", getStr("bsc.ahp.ahpEval.getItemGrpNm", searchMap));
    	
    	/************************************************************************************
    	 * 평가제출 여부조회
    	 ************************************************************************************/
    	searchMap.addList("evalStatus", getDetail("bsc.ahp.ahpEval.getEvalStatus", searchMap));
    	
    	
    	searchMap.addList("evalScorelist", getList("bsc.ahp.ahpEval.getEvalScoreList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * AHP 평가실시 상세 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap ahpEvalExecList_xml(SearchMap searchMap) {
        
    	searchMap.addList("evalScorelist", getList("bsc.ahp.ahpEval.getEvalScoreList", searchMap));
    	
        searchMap.addList("list", getList("bsc.ahp.ahpEval.getEvalList", searchMap));

        return searchMap;
    }
    
    /**
     * AHP 평가실시 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap ahpEvalProcess(SearchMap searchMap) {
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
            searchMap = insertDB(searchMap);
        } else if("SUBMIT".equals(stMode)) {
            searchMap = submitDB(searchMap);
            searchMap = insertDB(searchMap);
        } else if("SUBMIT_REJECT".equals(stMode)) {
            searchMap = submitDB(searchMap);
        } 
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * AHP 평가제출
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap submitDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        	
        	int cnt = getInt("bsc.ahp.ahpEval.getEvalStatusCount", searchMap);
        	
        	if(0 < cnt) {
        		returnMap = updateData("bsc.ahp.ahpEval.updateEvalStatusData", searchMap, true);
	        } else {
	        	returnMap = updateData("bsc.ahp.ahpEval.insertEvalStatusData", searchMap, true);
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
     * AHP 평가실시 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        /**********************************
         * Parameter setting
         **********************************/
        String[] itemIds = searchMap.getStringArray("itemIds");
        String[] perItemIds = searchMap.getStringArray("perItemIds");
        String evalScoreParam = "";
        
        try {
        	setStartTransaction();
        
        	/**********************************
             * 평가 삭제
             **********************************/
        	returnMap = updateData("bsc.ahp.ahpEval.deleteData", searchMap, true);
        	
        	/**********************************
             * 평가 입력
             **********************************/
        	if(null != itemIds && 0 < itemIds.length) {
 		        for (int i = 0; i < itemIds.length; i++) {
 		        	evalScoreParam = "evalScore_" + itemIds[i] + "_" + perItemIds[i];
 		        	
 		        	searchMap.put("evalScoreId", (String)searchMap.getString(evalScoreParam));
 					searchMap.put("itemId", itemIds[i]);
 					searchMap.put("perItemId", perItemIds[i]);
 					
 					returnMap = insertData("bsc.ahp.ahpEval.insertData", searchMap);
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
        int     resultValue       = 0;
        

        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }

    /**
     * AHP 평가실시 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap ahpEvalListExcel(SearchMap searchMap) {
    	String excelFileName = "AHP 평가";
    	String excelTitle = "AHP 평가 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("평가자", (String)searchMap.get("evalUserNm")));
    	excelSearchInfoList.add(new ExcelVO("평가단", (String)searchMap.get("evalUserGrpNm")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
  
    	excelInfoList.add(new ExcelVO("평가대상 그룹 명", "ITEM_GRP_NM", "left"));
    	//excelInfoList.add(new ExcelVO("평가문항", "EVAL_CNT", "center"));
      	excelInfoList.add(new ExcelVO("평가문항/전체문항", "ALL_CNT", "center"));
    	excelInfoList.add(new ExcelVO("비고", "CONTENT", "left"));
      	excelInfoList.add(new ExcelVO("평가제출 여부", "EVAL_SUBMIT_YN_NM", "center"));

    	//excelInfoList.add(new ExcelVO("정렬순서", "SORT_ORDER", "left"));
    	

  
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.ahp.ahpEval.getList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * AHP 평가실시 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap ahpEvalDetailExcel(SearchMap searchMap) {
    	String excelFileName = "AHP 평가";
    	String excelTitle = "AHP 평가 리스트";
    	
    	String findYear = searchMap.getString("findYear");
    	String findItemGrpId = searchMap.getString("findItemGrpId");
    	String findEvalUserGrpId = searchMap.getString("findEvalUserGrpId");
    	String findEvalUserId = searchMap.getString("findEvalUserId");
    	
    	searchMap.put("year", findYear);
    	searchMap.put("itemGrpId", findItemGrpId);
    	searchMap.put("evalUserGrpId", findEvalUserGrpId);
    	searchMap.put("evalUserId", findEvalUserId);
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("findYear")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("기준대상", "ITEM_NM", "left"));
    	excelInfoList.add(new ExcelVO("피비교대상", "PER_ITEM_NM", "left"));
    	excelInfoList.add(new ExcelVO("점수", "EVAL_SCORE", "center"));
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.ahp.ahpEval.getEvalList", searchMap));
    	
        return searchMap;
    }
    
}
