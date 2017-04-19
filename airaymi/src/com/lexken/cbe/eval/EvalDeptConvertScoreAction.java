/*************************************************************************
* CLASS 명      : EvalDeptConvertScoreAction
* 작 업 자      : 정철수
* 작 업 일      : 2012년 12월 17일 
* 기    능      : 조직환산점수
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    정철수      2012년 12월 17일         최 초 작 업 
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

public class EvalDeptConvertScoreAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 조직환산점수 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalDeptConvertScoreList(SearchMap searchMap) {
    	/**********************************
         * 평가구분 조회
         **********************************/
    	searchMap.addList("evalDegreeList", getList("cbe.eval.evalExceDept.getDegreeList", searchMap));
    	searchMap.addList("insaEvalDegreelist", getList("cbe.eval.evalDeptConvertScore.getInsaEvalDegreeList", searchMap));
    	
    	String findEvalDegreeId = (String)searchMap.get("findEvalDegreeId");
    	if( null == findEvalDegreeId || "".equals(findEvalDegreeId) ){
    		searchMap.put("findEvalDegreeId", searchMap.getDefaultValue("evalDegreeList", "EVAL_DEGREE_ID", 0));
    	}
    	searchMap.addList("evalInitInfo", getDetail("cbe.eval.evalDeptConvertScore.getInitInfoDetail", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 조직환산점수 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalDeptConvertScoreList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("cbe.eval.evalDeptConvertScore.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 조직환산점수 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalDeptConvertScoreModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("cbe.eval.evalDeptConvertScore.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 조직환산점수 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalDeptConvertScoreProcess(SearchMap searchMap) {
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
        } else if("CALL".equals(stMode)) {
            searchMap = callDB(searchMap);
        } else if("INIT".equals(stMode)) {
            searchMap = initDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 조직환산점수 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = updateData("cbe.eval.evalDeptConvertScore.deleteEvalInitData", searchMap, true);
        	
        	returnMap = insertData("cbe.eval.evalDeptConvertScore.insertEvalInitData", searchMap);
        	
        	String[] scDeptIds = searchMap.getString("scDeptIds").split("\\|", 0);
			String[] cbeFixScores = searchMap.getString("cbeFixScores").split("\\|", 0);
			
			if(null != scDeptIds && 0 < scDeptIds.length) {
	            
	        	for (int i = 0; i < scDeptIds.length; i++) {
		            searchMap.put("scDeptId", scDeptIds[i]);
		            searchMap.put("cbeFixScore", cbeFixScores[i].replace("none", "" ) );
		            returnMap = updateData("cbe.eval.evalDeptConvertScore.updateData", searchMap, true);
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
     * 조직환산점수 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("cbe.eval.evalDeptConvertScore.updateData", searchMap);
	        
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

    /**
     * 조직환산점수 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap evalDeptConvertScoreListExcel(SearchMap searchMap) {
    	String excelFileName = "조직환산점수";
    	String excelTitle = "조직환산점수 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("평가구분", (String)searchMap.get("evalDegreeNm")));
    	
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	
    	excelInfoList.add(new ExcelVO("상위평가조직", "UP_SC_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("조직평가그룹", "SC_DEPT_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가조직", "SC_DEPT_NM", "left"));

    	excelInfoList.add(new ExcelVO("조직점수", "SCORE", "right"));
    	excelInfoList.add(new ExcelVO("평가조직순위", "DEPT_RANK", "center"));
    	excelInfoList.add(new ExcelVO("상위평가조직순위", "UP_SC_DEPT_RANK", "center"));
    	excelInfoList.add(new ExcelVO("환산조직점수", "CONVERT_SCORE", "right"));
    	excelInfoList.add(new ExcelVO("확정환산조직점수", "CONVERT_SCORE", "right"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("cbe.eval.evalDeptConvertScore.getList", searchMap));
    	
        return searchMap;
    }
    
    
    
    /**
     * 데이터집계관리 프로시저 실행
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap callDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        	
        	if("SP_CBE_SET_CONVERT_TAB".equals(searchMap.get("procId"))){
        		returnMap = insertData("cbe.eval.evalDeptConvertScore.callSpSetConvertTab", searchMap);
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
     * 인사정보 초기화
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap initDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	//인사정보 초기화 실행
	        returnMap = insertData("cbe.eval.evalDeptConvertScore.execData", searchMap);
        
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
