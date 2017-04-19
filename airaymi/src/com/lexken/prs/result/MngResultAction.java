/*************************************************************************
* CLASS 명      : MngResultAction
* 작 업 자      : 김상용
* 작 업 일      : 2013년 06월 18일 
* 기    능      : 평가결과-간부개인성과 평가결과
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    김 상 용      2013년 06월 10일    최 초 작 업 
**************************************************************************/
package com.lexken.prs.result;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.util.StaticUtil;

public class MngResultAction extends CommonService {
	private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    public SearchMap mngResultList(SearchMap searchMap) {
    	searchMap.put("findEvalGrpType", "02");
    	searchMap.addList("evalGrpList", getList("bsc.module.commModule.getEvalGrpList", searchMap));
    	return searchMap;
    }
    
    public SearchMap mngResultList_xml(SearchMap searchMap) {
    	searchMap.addList("list", getList("prs.result.mngResult.getList", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 평가결과 불러오기/수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap mngResultProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("GET".equals(stMode)) {
            searchMap = batchMngResult(searchMap);
        }  else if("MOD".equals(stMode)) {
        	searchMap = updateMngResult(searchMap);
        }
        
         return searchMap;
    }
    
    /**
     * 평가결과 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateMngResult(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        	
        	returnMap = this.validChk(searchMap);
            
            if((Integer)returnMap.get("ErrorNumber") < 0 ){
                searchMap.addList("returnMap", returnMap);
                return searchMap;
            }
        
        	returnMap = updateData("prs.result.mngResult.updateData", searchMap);
        
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
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("content"), "설명", 0, 2000);
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
        	return returnMap;
        }
        
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }
    
    /**
     * 평가결과 가져오기
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap batchMngResult(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        //사용안함 returnMap = insertData("prs.result.mngResult.deleteMngResult", searchMap, true);
	        //사용안함 returnMap = insertData("prs.result.mngResult.insertMngResult", searchMap, true);
	        returnMap = insertData("prs.mng.evalResult.deleteEvalResult", searchMap, true);
	        returnMap = insertData("prs.mng.evalResult.insertEvalResult", searchMap, true);
	        
	        String evalMethodId = getStr("prs.result.mngResult.getEvalMethodId", searchMap);
	        
	        if (!"".equals(evalMethodId)) {
	        	searchMap.put("evalMethodId", evalMethodId);
	        	returnMap = insertData("prs.result.mngResult.execMngEvalFinalResult", searchMap);
	        }
        } catch (Exception e) {
        	setRollBackTransaction();
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
     * 평가결과 상세
     * @param searchMap
     * @return
     */
    public SearchMap mngResultModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");

        if ("MOD".equals(stMode)) {
        	searchMap.addList("detail", getDetail("prs.result.mngResult.getDetail", searchMap));
        }

    	
    	return searchMap;
    }
    
    /**
     * 평가결과 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap mngResultListExcel(SearchMap searchMap) {
    	String excelFileName = "간부개인성과평가결과";
    	String excelTitle = "간부개인성과평가결과 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	//excelSearchInfoList.add(new ExcelVO("직급", (String)searchMap.get("castTcNm")));
    	//excelSearchInfoList.add(new ExcelVO("직급", (String)searchMap.get("evalCastTcNm")));
    	excelSearchInfoList.add(new ExcelVO("평가군", (String)searchMap.get("evalGrpNm")));
    	excelSearchInfoList.add(new ExcelVO("이름", (String)searchMap.get("userNm")));

    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("사번", 				"EMPN", 				"center"));
    	excelInfoList.add(new ExcelVO("이름", 				"KOR_NM", 				"center"));
    	excelInfoList.add(new ExcelVO("부서", 				"DEPT_NM",		 		"left"));
    	excelInfoList.add(new ExcelVO("직급", 				"CAST_TC_NM", 			"center"));
    	excelInfoList.add(new ExcelVO("직위", 				"POS_TC_NM", 			"center"));
    	excelInfoList.add(new ExcelVO("평가군", 				"EVAL_GRP_NM", 			"center"));
    	excelInfoList.add(new ExcelVO("현부서", 			"CUR_DEPT_NM",		 	"left"));
    	excelInfoList.add(new ExcelVO("현직급", 			"CUR_CAST_TC_NM", 		"center"));
    	excelInfoList.add(new ExcelVO("현직위", 			"CUR_POS_TC_NM", 		"center"));
    	excelInfoList.add(new ExcelVO("승진여부", 			"PROMO_YN", 			"center"));
    	excelInfoList.add(new ExcelVO("조직업적점수", 	"BSC_TOTAL_SCORE", 		"center"));
    	excelInfoList.add(new ExcelVO("개인업적점수", 	"PRS_TOTAL_SCORE", 		"center"));
    	excelInfoList.add(new ExcelVO("개인MBO점수", 	"PRS_MBO_TOTAL_SCORE", 		"center"));
    	if("2012".equals(searchMap.getString("findYear")))
    		excelInfoList.add(new ExcelVO("조직관리역량평가점수", "ORG_TOTAL_SCORE", 	"center"));
    	excelInfoList.add(new ExcelVO("총점", 				"TOTAL_SCORE", 			"center"));
    	excelInfoList.add(new ExcelVO("순위", 				"RANKING", 				"center"));
    	excelInfoList.add(new ExcelVO("등급", 				"GRADE", 				"center"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("prs.result.mngResult.getExcelList", searchMap));
    	
        return searchMap;
    }
}
