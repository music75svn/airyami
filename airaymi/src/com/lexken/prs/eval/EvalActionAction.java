/*************************************************************************
* CLASS 명      : EvalActionAction
* 작 업 자      : 현걸욱
* 작 업 일      : 2012년 11월 26일 
* 기    능      : 개인평가 평가실시 (현황)
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    현걸욱      2012년 11월 26일         최 초 작 업 
**************************************************************************/
package com.lexken.prs.eval;
    
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

public class EvalActionAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 개인평가 평가실시 (현황) 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalActionList(SearchMap searchMap) {

    	searchMap.addList("evalDegreeList", getList("prs.eval.evalAction.getEvalDegreeList", searchMap));
    	
    	/**********************************
         * 디폴트 조회조건 설정(findEvalDegreeId)
         **********************************/
    	String findEvalDegreeId = (String)searchMap.get("findEvalDegreeId");
    	if("".equals(StaticUtil.nullToBlank(findEvalDegreeId))) {
    		searchMap.put("findEvalDegreeId", (String)searchMap.getDefaultValue("evalDegreeList", "EVAL_DEGREE_ID", 0));
    	}
    	
    	searchMap.addList("evalUserList", getList("prs.eval.evalAction.getEvalUserList", searchMap));
    	
    	/**********************************
         * 디폴트 조회조건 설정(findEvalUserId)
         **********************************/
    	String findEvalUserId = (String)searchMap.get("findEvalUserId");
    	if("".equals(StaticUtil.nullToBlank(findEvalUserId))) {
    		searchMap.put("findEvalUserId", (String)searchMap.getDefaultValue("evalUserList", "EVAL_USER_ID", 0));
    	}
    	
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
    	if(!loginVO.chkAuthGrp("01") && !loginVO.chkAuthGrp("60") && loginVO.chkAuthGrp("30")) {
    		searchMap.put("findEvalUserId", loginVO.getUser_id());
    		searchMap.put("findEvalUserNm", loginVO.getUser_nm());
    	}
    	
    	searchMap.addList("getEvalPeriodYn", getStr("prs.eval.evalAction.getEvalPeriodYn", searchMap));
    	
    	/**********************************
         * 마감,확정 조회
         **********************************/
    	searchMap.addList("closeYn", getStr("prs.eval.evalStatus.getCloseYn", searchMap));
    	searchMap.addList("confirmYn", getStr("prs.eval.evalStatus.getConfirmYn", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 개인평가 평가실시 (현황) 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalActionList_xml(SearchMap searchMap) {
    	
    	String[][] convertArray =  {{"Y","평가제출"} , {"N","진행중"}};
        
        searchMap.addList("CONVERT_ARRAY", convertArray);
        
        searchMap.addList("list", getList("prs.eval.evalAction.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가구분조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalDegree_ajax(SearchMap searchMap) {
        
    	searchMap.addList("evalDegreeList", getList("prs.eval.evalAction.getEvalDegreeList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가자조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalUser_ajax(SearchMap searchMap) {
    	
    	searchMap.addList("evalUserList", getList("prs.eval.evalAction.getEvalUserList", searchMap));

        return searchMap;
    }
    
    /**
     * 개인평가 평가실시 (현황) 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    @SuppressWarnings("unchecked")
	public SearchMap evalActionModify(SearchMap searchMap) {
        
    	searchMap.put("evalDegreeNm", getStr("prs.eval.evalAction.getEvalDegreeNm", searchMap));
    	searchMap.put("evalUserNm", getStr("prs.eval.evalAction.getEvalUserNm", searchMap));
    	searchMap.put("evalGrpNm", getStr("prs.eval.evalAction.getEvalGrpNm", searchMap));
    	searchMap.put("totCnt", getStr("prs.eval.evalAction.getTotCnt", searchMap));
    	searchMap.put("evalCnt", getStr("prs.eval.evalAction.getEvalCnt", searchMap));
    	
    	searchMap.addList("gradeList", getList("prs.eval.evalAction.getEvalGradeList", searchMap));
    	
    	/**********************************
         * 마감,확정 조회
         **********************************/
    	searchMap.addList("closeYn", getStr("prs.eval.evalStatus.getCloseYn", searchMap));
    	searchMap.addList("confirmYn", getStr("prs.eval.evalStatus.getConfirmYn", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 개인평가 평가실시 (현황) 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalActionModify_xml(SearchMap searchMap) {
    	
    	ArrayList gradeList = (ArrayList)getList("prs.eval.evalAction.getEvalGradeList", searchMap);
    	
    	String[] gradeIdArray = new String[0]; 
    	if(null != gradeList && 0 < gradeList.size()) {
    		gradeIdArray = new String[gradeList.size()];
    		for(int i=0; i<gradeList.size(); i++) {
    			HashMap map = (HashMap)gradeList.get(i);
    			gradeIdArray[i] = (String)map.get("GRADE_ITEM_ID"); 
    		}
    	}
    	searchMap.put("gradeIdArray", gradeIdArray);
    	searchMap.addList("gradeList", gradeList);
    	
    	searchMap.addList("evalList", getList("prs.eval.evalAction.getEvalList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 개인평가 평가실시 (현황) 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalActionProcess(SearchMap searchMap) {
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
        if("SUB".equals(stMode)) {
            searchMap = submitUpdateDB(searchMap);
        } else if("MOD".equals(stMode)) {
            searchMap = updateDB(searchMap);
        } 
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 개인평가 평가실시 (현황) 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("prs.eval.evalAction.insertData", searchMap);
        
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
     * 개인평가 평가실시 (현황) 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap submitUpdateDB(SearchMap searchMap) {
    	HashMap returnMap = new HashMap(); 
	    
	    try {
	        
	        setStartTransaction();
	        
	        returnMap = updateData("prs.eval.evalAction.submitUpdateData", searchMap, true);
	        
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
     * 개인평가 평가실시 (현황) 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
    	HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] userIds = searchMap.getString("userIds").split("\\|", 0);
			String[] gradeIds = searchMap.getString("gradeIds").split("\\|", 0);
			String[] feeds = searchMap.getStringArray("feeds");
	        
	        setStartTransaction();
	        
	        returnMap = updateData("prs.eval.evalAction.deleteData", searchMap, true);
	        
	        if(null != userIds && 0 < userIds.length) {
		        for (int i = 0; i < userIds.length; i++) {
		            searchMap.put("userId", userIds[i]);
					searchMap.put("gradeId", gradeIds[i]);
					searchMap.put("feed", feeds[i]);
		            returnMap = insertData("prs.eval.evalAction.insertData", searchMap);
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
        
		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("weight"), "가중치");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("evalSeq"), "평가 순번");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}


        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }

    /**
     * 개인평가 평가실시 (현황) 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap evalActionListExcel(SearchMap searchMap) {
    	String excelFileName = "개인평가 평가실시 (현황)";
    	String excelTitle = "개인평가 평가실시 (현황) 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("평가구분", (String)searchMap.get("findEvalDegreeNm")));
    	excelSearchInfoList.add(new ExcelVO("평가자", (String)searchMap.get("findEvalUserNm")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가군", 		"EVAL_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가대상인원", 	"MEMB_CNT", "center"));
    	excelInfoList.add(new ExcelVO("설명", 		"CONTENT", "left"));
    	excelInfoList.add(new ExcelVO("평가상태", 		"EVAL_SUBMIT_NM", "center"));
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("prs.eval.evalAction.getList", searchMap));
    	
        return searchMap;
    }
    
}
