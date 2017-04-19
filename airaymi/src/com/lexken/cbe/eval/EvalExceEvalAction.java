/*************************************************************************
* CLASS 명      : EvalExceEvalAction
* 작 업 자      : 현걸욱
* 작 업 일      : 2012년 12월 13일 
* 기    능      : 종합평가 예외평가
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    현걸욱      2012년 12월 13일         최 초 작 업 
**************************************************************************/
package com.lexken.cbe.eval;
    
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
import com.lexken.framework.util.StaticUtil;

public class EvalExceEvalAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 종합평가 예외평가 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalExceEvalList(SearchMap searchMap) {

    	/**********************************
         * 평가구분 조회
         **********************************/
    	searchMap.addList("evalDegreeList", getList("cbe.eval.evalExceEval.getDegreeList", searchMap));
    	searchMap.addList("evalGradeList", getList("cbe.eval.evalExceEval.getEvalGradeList", searchMap));	
    	
        return searchMap;
    }
    
    /**
     * 종합평가 예외평가 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalExceEvalList_xml(SearchMap searchMap) {
    	
        searchMap.addList("list", getList("cbe.eval.evalExceEval.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 종합평가 예외평가 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalExceEvalModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("cbe.eval.evalExceEval.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 종합평가 예외평가 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalExceEvalProcess(SearchMap searchMap) {
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
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 종합평가 예외평가 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("cbe.eval.evalExceEval.insertData", searchMap);
        
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
     * 종합평가 예외평가 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
    	HashMap returnMap = new HashMap(); 
	    
	    try {
			String[] evalMembUserIds = searchMap.getString("evalMembUserIds").split("\\|", 0);
			String[] conbiEvalGrades = searchMap.getString("conbiEvalGrades").split("\\|", 0);
			String[] conbiEvalGradeNms = searchMap.getString("conbiEvalGradeNms").split("\\|", 0);
	        
			String conbiEvalGrade= "";
			String conbiEvalGradeNm = "";
			
	        setStartTransaction();
	        
	        if(null != evalMembUserIds && 0 < evalMembUserIds.length) {
		        for (int i = 0; i < evalMembUserIds.length; i++) {
		        	
		        	conbiEvalGrade = conbiEvalGrades[i];
		        	conbiEvalGradeNm = conbiEvalGradeNms[i];
		        	if("none".equals(conbiEvalGrade)){conbiEvalGrade = "";}
		        	if("none".equals(conbiEvalGradeNm)){conbiEvalGradeNm = "";}
		        	
		            searchMap.put("evalMembUserId", evalMembUserIds[i]);
		            searchMap.put("conbiEvalGrade", conbiEvalGrade);
		            searchMap.put("conbiEvalGradeNm", conbiEvalGradeNm);
		            returnMap = updateData("cbe.eval.evalExceEval.updateData", searchMap, true);
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
     * 종합평가 예외평가 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] evalDegreeIds = searchMap.getString("evalDegreeIds").split("\\|", 0);
			String[] evalMembUserIds = searchMap.getString("evalMembUserIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != evalDegreeIds && 0 < evalDegreeIds.length) {
		        for (int i = 0; i < evalDegreeIds.length; i++) {
		            searchMap.put("evalDegreeId", evalDegreeIds[i]);
		            searchMap.put("evalMembUserId", evalMembUserIds[i]);
		            returnMap = updateData("cbe.eval.evalExceEval.deleteData", searchMap);
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
        
		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("frsDeptScore"), "사업소 점수");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("frsDeptConvertScore"), "사업소 환산점수");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("frsDeptWtReflConvertScore"), "사업소 가중치반영환산점수");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("secDeptScore"), "2차사업소 점수");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("secDeptConvertScore"), "2차사업소 환산점수");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("secDeptWtReflConvertScore"), "2차사업소 가중치반영환산점수");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("teamScore"), "팀 점수");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("teamConvertScore"), "팀 환산점수");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("teamWtReflConvertScore"), "팀 가중치반영환산점수");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("prsConvertScore"), "개인평가 환산점수");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("prsWtReflConvertScore"), "개인평가 가중치반영환산점수");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("conbiEvalScore"), "종합평가 점수");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("conbiEvalRank"), "종합평가 순위");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}


        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }

    /**
     * 종합평가 예외평가 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap evalExceEvalListExcel(SearchMap searchMap) {
    	String excelFileName = "종합평가 예외평가";
    	String excelTitle = "종합평가 예외평가 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("평가구분", (String)searchMap.get("degreeNm")));
    	
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가년도", "EVAL_MEMB_USER_ID", "left"));
    	excelInfoList.add(new ExcelVO("성명", "EVAL_MEMB_USER_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가조직", "SC_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("직급", "JIKGUB_NM", "left"));
    	excelInfoList.add(new ExcelVO("예외평가 사유", "EXCE_EVAL_REASON", "left"));
    	excelInfoList.add(new ExcelVO("사업소", "FRS_DEPT_RESULT", "left"));
    	excelInfoList.add(new ExcelVO("2차사업소", "SEC_DEPT_REUSLT", "left"));
    	excelInfoList.add(new ExcelVO("팀", "TEAM_RESULT", "left"));
    	excelInfoList.add(new ExcelVO("개인", "PRS_GRADE", "left"));
    	excelInfoList.add(new ExcelVO("종합평가등급", "CONBI_EVAL_GRADE", "left"));
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("cbe.eval.evalExceEval.getExcelList", searchMap));
    	
        return searchMap;
    }
    
}
