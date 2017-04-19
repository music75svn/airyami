/*************************************************************************
* CLASS 명      : EvalDegreeAction
* 작 업 자      : 정철수
* 작 업 일      : 2012년 11월 26일 
* 기    능      : 평가구분
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    정철수      2012년 11월 26일         최 초 작 업 
**************************************************************************/
package com.lexken.bsc.common;
    
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

public class EvalDegreeAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 평가구분 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalDegreeList(SearchMap searchMap) {

        return searchMap;
    }
    
    /**
     * 평가구분 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalDegreeList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.common.evalDegree.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가구분 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalDegreeModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.common.evalDegree.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 평가구분 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalDegreeProcess(SearchMap searchMap) {
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
     * 평가구분 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("bsc.common.evalDegree.insertData", searchMap);
        
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
     * 평가구분 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("bsc.common.evalDegree.updateData", searchMap);
	        
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
     * 평가구분 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        
	    	String[] evalDegreeIds = searchMap.getString("evalDegreeIds").split("\\|", 0);
	        setStartTransaction();
	        
	        if(null != evalDegreeIds && 0 < evalDegreeIds.length) {
		        for (int i = 0; i < evalDegreeIds.length; i++) {
		        	searchMap.put("evalDegreeId", evalDegreeIds[i]);
		            returnMap = updateData("bsc.common.evalDegree.deleteData", searchMap);
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
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("evalDegreeNm"), "평가구분", 0, 100);
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
        	return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("imponRptRegStartDt"), "비계량보고서등록 시작일자", 1, 8);
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
        	return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("imponRptRegEndDt"), "비계량보고서등록 종료일자", 1, 8);
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
        	return returnMap;
        }
        
        if( Integer.parseInt(searchMap.getString("imponRptRegStartDt")) > Integer.parseInt(searchMap.getString("imponRptRegEndDt"))) {
        	returnMap.put("ErrorNumber",  -1);
			returnMap.put("ErrorMessage", "비계량보고서등록 종료일자는 비계량보고서등록 시작일자 보다 빠를 수 없습니다.");
	    	
	    	return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("imponEvalStartDt"), "비계량평가 시작일자", 1, 8);
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
        	return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("imponEvalEndDt"), "비계량평가 종료일자", 1, 8);
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
        	return returnMap;
        }
        
        if( Integer.parseInt(searchMap.getString("imponEvalStartDt")) > Integer.parseInt(searchMap.getString("imponEvalEndDt"))) {
        	returnMap.put("ErrorNumber",  -1);
			returnMap.put("ErrorMessage", "비계량평가 종료일자는 비계량평가 시작일자 보다 빠를 수 없습니다.");
	    	
	    	return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("sepaEvalBaseRegStartDt"), "별도평가 근거등록 시작일자", 1, 8);
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
        	return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("sepaEvalBaseRegEndDt"), "별도평가 근거등록 종료일자", 1, 8);
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
        	return returnMap;
        }
        
        if( Integer.parseInt(searchMap.getString("sepaEvalBaseRegStartDt")) > Integer.parseInt(searchMap.getString("sepaEvalBaseRegEndDt"))) {
        	returnMap.put("ErrorNumber",  -1);
			returnMap.put("ErrorMessage", "별도평가 근거등록 종료일자는 별도평가 근거등록 시작일자 보다 빠를 수 없습니다.");
	    	
	    	return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("sepaEvalStartDt"), "별도평가 시작일자", 1, 8);
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
        	return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("sepaEvalEndDt"), "별도평가 종료일자", 1, 8);
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
        	return returnMap;
        }
        
        if( Integer.parseInt(searchMap.getString("sepaEvalStartDt")) > Integer.parseInt(searchMap.getString("sepaEvalEndDt"))) {
        	returnMap.put("ErrorNumber",  -1);
			returnMap.put("ErrorMessage", "별도평가 종료일자는 별도평가 시작일자 보다 빠를 수 없습니다.");
	    	
	    	return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("orgStartDt"), "조직성과최종점수 시작일자", 1, 8);
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
        	return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("orgEndDt"), "조직성과최종점수 종료일자", 1, 8);
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
        	return returnMap;
        }
        
        if( Integer.parseInt(searchMap.getString("orgStartDt")) > Integer.parseInt(searchMap.getString("orgEndDt"))) {
        	returnMap.put("ErrorNumber",  -1);
			returnMap.put("ErrorMessage", "조직성과최종점수 종료일자는 조직성과최종점수 시작일자 보다 빠를 수 없습니다.");
	    	
	    	return returnMap;
        }
        
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }

    /**
     * 평가구분 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap evalDegreeListExcel(SearchMap searchMap) {
    	String excelFileName = "평가구분";
    	String excelTitle = "평가구분 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	//excelSearchInfoList.add(new ExcelVO("공통코드명", StaticUtil.nullToDefault((String)searchMap.get("codeGrpNm"), "전체")));
    	//excelSearchInfoList.add(new ExcelVO("사용여부", (String)searchMap.get("useYnNm")));
    	
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가년도", "YEAR", "left"));
    	excelInfoList.add(new ExcelVO("평가차수 코드", "EVAL_DEGREE_ID", "left"));
    	excelInfoList.add(new ExcelVO("평가차수 명", "EVAL_DEGREE_NM", "left"));
    	excelInfoList.add(new ExcelVO("기준월", "MON", "left"));
    	excelInfoList.add(new ExcelVO("비계량보고서 등록 ", "IMPON_RPT_REG_START_DT", "left"));
    	excelInfoList.add(new ExcelVO("비계량보고서 등록 ", "IMPON_RPT_REG_END_DT", "left"));
    	excelInfoList.add(new ExcelVO("비계량평가 시작일자", "IMPON_EVAL_START_DT", "left"));
    	excelInfoList.add(new ExcelVO("비계량평가 종료일자", "IMPON_EVAL_END_DT", "left"));
    	excelInfoList.add(new ExcelVO("별도평가 평가근거 ", "SEPA_EVAL_BASE_REG_START_DT", "left"));
    	excelInfoList.add(new ExcelVO("별도평가 평가근거 ", "SEPA_EVAL_BASE_REG_END_DT", "left"));
    	excelInfoList.add(new ExcelVO("별도평가 시작일자", "SEPA_EVAL_START_DT", "left"));
    	excelInfoList.add(new ExcelVO("별도평가 종료일자", "SEPA_EVAL_END_DT", "left"));
    	excelInfoList.add(new ExcelVO("조직성과최종점수 시작일자", "ORG_START_DT", "left"));
    	excelInfoList.add(new ExcelVO("조직성과최종점수 종료일자", "ORG_END_DT", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.common.evalDegree.getList", searchMap));
    	
        return searchMap;
    }
    
}
