/*************************************************************************
* CLASS 명      : MemEvalResultAction
* 작 업 자      : 유연주
* 작 업 일      : 2017년04월06일 
* 기    능      : 업무수행평가결과
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    유연주     2017년04월06일           최 초 작 업 
**************************************************************************/
package com.lexken.mem.eval;
    
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginVO;

public class MemEvalResultAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 업무수행평가결과
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memEvalResultList(SearchMap searchMap) {
    	
    	searchMap.addList("evalRateInfo", getDetail("mem.eval.memEvalResult.getEvalRateInfo", searchMap));
    	
    	searchMap.addList("evalConfirmInfo", getDetail("mem.eval.memEvalResult.getEvalConfirmInfo", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 업무수행평가결과 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memEvalResultList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("mem.eval.memEvalResult.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 업무수행평가결과 엑셀변환다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap memEvalResultExcel(SearchMap searchMap) {
    	String excelFileName = "업무수행평가결과";
    	String excelTitle = "업무수행평가결과목록";
    	
    	Map evalRateMap = getDetail("mem.eval.memEvalResult.getEvalRateInfo", searchMap);
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO("기준년도", (String)searchMap.get("yearNm")));
    	if(!"SEARCH".equals((String)searchMap.get("mode"))){
	    	excelSearchInfoList.add(new ExcelVO("부서", (String)searchMap.get("findDeptNm")));
	    	excelSearchInfoList.add(new ExcelVO("지급", (String)searchMap.get("castTcNm")));
	    	excelSearchInfoList.add(new ExcelVO("직위", (String)searchMap.get("posTcNm")));
	    	excelSearchInfoList.add(new ExcelVO("이름", (String)searchMap.get("findEmpNm")));
    	}
    	
    	/****************************************************************************************************
    	 * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
    	 ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("사번", "EMP_NO", "center"));
    	excelInfoList.add(new ExcelVO("이름", "EMP_NM", "center"));
    	excelInfoList.add(new ExcelVO("부서", "DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("직급", "CAST_NM", "center"));
    	excelInfoList.add(new ExcelVO("직위", "POS_NM", "center"));
    	excelInfoList.add(new ExcelVO("1차평가자("+evalRateMap.get("EVAL_1_RATE")+"%)", "EVAL_1_SCORE", "center"));
    	excelInfoList.add(new ExcelVO("2차평가자("+evalRateMap.get("EVAL_2_RATE")+"%)", "EVAL_2_SCORE", "center"));
    	excelInfoList.add(new ExcelVO("동료평가 점수("+evalRateMap.get("PEER_RATE")+"%)", "PEER_SCORE", "center"));
    	excelInfoList.add(new ExcelVO("동료평가 조정점수", "PEER_ADJUST_SCORE", "center"));
    	excelInfoList.add(new ExcelVO("총점", "TOT_SCORE", "center"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	
    	searchMap.put("excelDataList", (ArrayList)getList("mem.eval.memEvalResult.getList", searchMap));
    	
    	return searchMap;
    	
    }
    
    /**
     * 업무수행평가결과 처리
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memEvalResultProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 무결성 체크
         **********************************/
        if(!"CONFIRM".equals(stMode) && !"CANCEL".equals(stMode)) {
            returnMap = this.validChk(searchMap);
            
            if((Integer)returnMap.get("ErrorNumber") < 0 ){
                searchMap.addList("returnMap", returnMap);
                return searchMap;
            }
        }
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("CONFIRM".equals(stMode)) {
        	searchMap = evalConfirmDB(searchMap);
        } else if("CANCEL".equals(stMode)) {
        	searchMap = evalCancelDB(searchMap);
        } else if("CALCULATE".equals(stMode)) {
        	searchMap = calculateDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 업무수행평가결과 확정 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalConfirmDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        setStartTransaction();
	        
	        // 업무수행평가결과 확정
	    	searchMap.put("evalConfirmYn", "Y");
	        returnMap = insertData("mem.eval.memEvalResult.confirmEvalResultData", searchMap);
	        
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
     * 업무수행평가결과  확정취소 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalCancelDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        setStartTransaction();
	        
	        // 업무수행평가결과 확정취소
	    	searchMap.put("evalConfirmYn", "N");
	        returnMap = insertData("mem.eval.memEvalResult.confirmEvalResultData", searchMap);
	        
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
     * 업무수행평가결과  점수계산 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap calculateDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        setStartTransaction();
	        
	        // 전체평균, 전체표준편차 저장
	        returnMap = insertData("mem.eval.memEvalResult.confirmEvalResultData", searchMap);
	        
	        // 점수결과 전체 삭제
	        returnMap = deleteData("mem.eval.memEvalResult.deleteResultAllData", searchMap, true);
	        
	        // 동료평가결과 전체 삭제
	        returnMap = deleteData("mem.eval.memEvalResult.deletePeerResultAllData", searchMap, true);
	        
	        // 동표평과결과 등록
	        returnMap = insertData("mem.eval.memEvalResult.insertPeerResultData", searchMap);
	        
	        // 점수계산 등록
	        returnMap = insertData("mem.eval.memEvalResult.insertCalculateResultData", searchMap);
	        
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
     * 업무수행평가결과 동료평가팝업
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popMemPeerResultList(SearchMap searchMap) {
    	
    	// 대상자명 조회
    	searchMap.put("year", searchMap.get("findYear"));
    	searchMap.put("empNo", searchMap.get("findEmpNo"));
    	searchMap.addList("empInfo", getDetail("mem.eval.memEval.getEmpNm", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 업무수행평가결과 동료평가팝업 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popMemPeerResultList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("mem.eval.memEvalResult.getPeerList", searchMap));

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
        
		returnMap = ValidationChk.lengthCheck(searchMap.getString("allAverage"), "동료평가조정 평균", 1, 5);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
		
		returnMap = ValidationChk.lengthCheck(searchMap.getString("allStdDeviation"), "동료평가조정 표준편차", 1, 5);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
        
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }
}
