/*************************************************************************
* CLASS 명      : EvalResultAction
* 작 업 자      : 박경태
* 작 업 일      : 2012년 11월 28일 
* 기    능      : 평가결과
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    박경태      2012년 11월 28일         최 초 작 업 
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
import com.lexken.framework.util.HtmlHelper;
import com.lexken.framework.util.StaticUtil;

public class EvalResultAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 평가결과 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalResultList(SearchMap searchMap) {
    	/**********************************
         * 평가구분 조회
         **********************************/
    	//searchMap.addList("evalDegreeList", getList("prs.eval.evalGroup.getDegreeList", searchMap));
    	
    	ArrayList evalDegreeList = new ArrayList();
    	evalDegreeList = (ArrayList)getList("prs.eval.evalGroup.getDegreeList", searchMap); 
        searchMap.addList("evalDegreeList", evalDegreeList);
        
        String evalDegreeId = "";
        if(null != evalDegreeList && 0 < evalDegreeList.size()) {
	        for (int i = 0; i < evalDegreeList.size(); i++) {
	        	HashMap<String, String> t = (HashMap<String, String>)evalDegreeList.get(i);
	        	if(i==0){
	        		evalDegreeId = (String)t.get("EVAL_DEGREE_ID");
	        	}
			}
        }
        
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.get("findEvalDegreeId")))) {
			searchMap.put("findEvalDegreeId", evalDegreeId);
		}
    	
    	/**********************************
         * 평가단 조회
         **********************************/
    	ArrayList evalGrpList = new ArrayList();
    	evalGrpList = (ArrayList)getList("prs.eval.evalResult.getGrpList", searchMap); 
        searchMap.addList("evalGrpList", evalGrpList);
        
        String evalGrpId = "";
        if(null != evalGrpList && 0 < evalGrpList.size()) {
	        for (int i = 0; i < evalGrpList.size(); i++) {
	        	HashMap<String, String> t = (HashMap<String, String>)evalGrpList.get(i);
	        	if(i==0){
	        		evalGrpId = (String)t.get("EVAL_GRP_ID");
	        	}
			}
        }
        
        if("".equals(StaticUtil.nullToBlank((String)searchMap.get("findEvalGrpId")))) {
			searchMap.put("findEvalGrpId", evalGrpId);
		}
    	
    	/**********************************
         * 평가등급 조회
         **********************************/
    	searchMap.addList("evalGradeList", getList("prs.eval.evalResult.getGradeList", searchMap));
    	
    	/**********************************
         * 마감,확정 조회
         **********************************/
    	searchMap.addList("closeYn", getStr("prs.eval.evalResult.getCloseYn", searchMap));
    	searchMap.addList("confirmYn", getStr("prs.eval.evalResult.getConfirmYn", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popFeedDetail(SearchMap searchMap) {
    	
    	ArrayList evalFeedList = new ArrayList();
    	evalFeedList = (ArrayList)getList("prs.eval.evalResult.getFeedList", searchMap); 
        searchMap.addList("evalFeedList", evalFeedList);
        
        String year 		= "";
        String evalDegreeNm = "";
        String evalGrpNm 	= "";
        String userNm 		= "";
        String gradeNm 		= "";
        
        if(null != evalFeedList && 0 < evalFeedList.size()) {
	        for (int i = 0; i < evalFeedList.size(); i++) {
	        	HashMap<String, String> t = (HashMap<String, String>)evalFeedList.get(i);
	        	if(i==0){
	        		year 			= (String)t.get("YEAR");
	        		evalDegreeNm 	= (String)t.get("EVAL_DEGREE_NM");
	        		evalGrpNm 		= (String)t.get("EVAL_GRP_NM");
	        		userNm 			= (String)t.get("USER_NM");
	        		gradeNm 		= (String)t.get("FINAL_GRADE_ITEM_NM");
	        	}
			}
        }
        
        searchMap.addList("year", 			year);
        searchMap.addList("evalDegreeNm", 	evalDegreeNm);
        searchMap.addList("evalGrpNm", 		evalGrpNm);
        searchMap.addList("userNm", 		userNm);
        searchMap.addList("gradeNm", 		gradeNm);
        
    	
        return searchMap;
    }
    
    /**
     * 평가결과 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalResultList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("prs.eval.evalResult.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 년도별 평가단 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap getGrpList_ajax(SearchMap searchMap) {
    	
    	/**********************************
         * 평가단 조회
         **********************************/
    	searchMap.addList("evalGrpList", getList("prs.eval.evalResult.getGrpList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가결과 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalResultModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("prs.eval.evalResult.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 평가결과 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalResultProcess(SearchMap searchMap) {
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
        } else if("FINAL".equals(stMode)) {
            searchMap = finalDB(searchMap);
        } else if("CONFIRM".equals(stMode)) {
            searchMap = confirmDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 평가확정 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap confirmDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        	
        	returnMap = updateData("prs.eval.evalResult.confirmUpdateData", searchMap);
	        
	        
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
     * 최종등급 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap finalDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        
	        String[] evalDegreeIds 		= searchMap.getString("evalDegreeIds").split("\\|", 0);
			String[] evalGrpIds 		= searchMap.getString("evalGrpIds").split("\\|", 0);
			String[] evalMembUserIds 	= searchMap.getString("evalMembUserIds").split("\\|", 0);
			String[] finalGradeIds 		= searchMap.getString("finalGradeId").split("\\|", 0);
			String[] finalGradeNms 		= searchMap.getString("finalGradeNm").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != evalDegreeIds && 0 < evalDegreeIds.length) {
		        for (int i = 0; i < evalDegreeIds.length; i++) {
		            searchMap.put("evalDegreeId", evalDegreeIds[i]);
		            searchMap.put("evalGrpId", evalGrpIds[i]);
		            searchMap.put("evalMembUserId", evalMembUserIds[i]);
		            searchMap.put("finalGradeItemId", finalGradeIds[i]);
		            searchMap.put("finalGradeItemNm", finalGradeNms[i]);
		            returnMap = updateData("prs.eval.evalResult.updateData", searchMap);
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
     * 평가결과 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("prs.eval.evalResult.insertData", searchMap);
        
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
     * 평가결과 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("prs.eval.evalResult.updateData", searchMap);
	        
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
     * 평가결과 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] evalDegreeIds = searchMap.getString("evalDegreeIds").split("\\|", 0);
			String[] evalGrpIds = searchMap.getString("evalGrpIds").split("\\|", 0);
			String[] evalMembUserIds = searchMap.getString("evalMembUserIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != evalDegreeIds && 0 < evalDegreeIds.length) {
		        for (int i = 0; i < evalDegreeIds.length; i++) {
		            searchMap.put("evalDegreeId", evalDegreeIds[i]);
			searchMap.put("evalGrpId", evalGrpIds[i]);
			searchMap.put("evalMembUserId", evalMembUserIds[i]);
		            returnMap = updateData("prs.eval.evalResult.deleteData", searchMap);
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
        
		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("score"), "평가점수");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("rankId"), "RANK_ID");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}


        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }

    /**
     * 평가결과 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap evalResultListExcel(SearchMap searchMap) {
    	String excelFileName = "평가결과";
    	String excelTitle = "평가결과 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("평가구분", (String)searchMap.get("evalDegreeNm")));
    	excelSearchInfoList.add(new ExcelVO("평가군", (String)searchMap.get("evalGrpNm")));
    	
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가군", "EVAL_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가조직", "DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("성명", "EVAL_MEMB_USER_NM", "left"));
    	excelInfoList.add(new ExcelVO("직위", "POS_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가점수", "SCORE", "left"));
    	excelInfoList.add(new ExcelVO("평가등급", "GRADE_ITEM_NM", "left"));
    	excelInfoList.add(new ExcelVO("최종등급", "FINAL_GRADE_ITEM_NM", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("prs.eval.evalResult.getList", searchMap));
    	
        return searchMap;
    }
    
}
