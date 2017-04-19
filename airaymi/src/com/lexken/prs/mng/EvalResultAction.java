/*************************************************************************
* CLASS 명      : EvalResultAction
* 작 업 자      : 김상용
* 작 업 일      : 2013년 06월 18일 
* 기    능      : 간부업적평가 평가결과
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    김 상 용      2013년 06월 80일    최 초 작 업 
**************************************************************************/
package com.lexken.prs.mng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.StringConstants;
import com.lexken.framework.core.CommonService;

public class EvalResultAction extends CommonService {
	private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 간부개인업적평가 화면
     * @param searchMap
     * @return
     */
    public SearchMap evalResultList(SearchMap searchMap) {
    	searchMap.put("findEvalGrpType", "02");
    	searchMap.addList("evalGrpList", getList("bsc.module.commModule.getEvalGrpList", searchMap));
    	// 마감여부
    	searchMap.addList("mngEvalYn", getDetail("prs.mng.evalResult.getMngEvalClosing", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 간부개인업적평가 xml
     * @param searchMap
     * @return
     */
    public SearchMap evalResultList_xml(SearchMap searchMap) {
    	searchMap.addList("list", getList("prs.mng.evalResult.getList", searchMap));
    	return searchMap;
    }
    
    /**
     * 평가결과 가져오기/수정 처리
     * @param searchMap
     * @return
     */
    public SearchMap evalResultProcess(SearchMap searchMap) {
    	HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("GET".equals(stMode)) {
            searchMap = batchEvalResult(searchMap);
        }  else if("CON".equals(stMode)) {
        	searchMap = updateDB(searchMap);
        }
        
         return searchMap;
    }
    
    /**
     * 간부개인업적평가 마감
     * @param searchMap
     * @return
     */
    public SearchMap updateDB(SearchMap searchMap) {
    	HashMap returnMap    = new HashMap();
    	
    	try {
        	setStartTransaction();
        	
        	returnMap = updateData("prs.mng.evalResult.updateMngEvalConfirm", searchMap);
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
     * 평가결과 가져오기
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap batchEvalResult(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = insertData("prs.mng.evalResult.deleteEvalResult", searchMap, true);
	        returnMap = insertData("prs.mng.evalResult.insertEvalResult", searchMap);
	        
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
     * 평가결과 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap evalResultListExcel(SearchMap searchMap) {
    	String excelFileName = "간부개인업적평가결과";
    	String excelTitle = "간부개인업적평가결과 리스트";
    	
    	List assessorList = getList("prs.mng.evalResult.getAssessor", searchMap);
    	
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
    	excelInfoList.add(new ExcelVO("사번", 				"EVAL_MEMB_EMPN", 				"center"));
    	excelInfoList.add(new ExcelVO("이름", 				"KOR_NM", 				"center"));
    	excelInfoList.add(new ExcelVO("부서", 				"DEPT_FULL_NM", 				"center"));
    	excelInfoList.add(new ExcelVO("직급", 				"CAST_NM", 				"center"));
    	excelInfoList.add(new ExcelVO("직위", 				"POS_NM", 				"center"));
    	excelInfoList.add(new ExcelVO("평가군", 				"EVAL_GRP_NM", 			"center"));
    	excelInfoList.add(new ExcelVO("근무월수", 				"WORK_MONS", 				"center"));

    	// 평가자 설정
    	String[] itemArray = new String[0]; 
    	
    	if (null != assessorList && 0 < assessorList.size()) {
    		itemArray = new String[assessorList.size()];
    	}
    	
    	for (int i = 0; i < assessorList.size(); i++) {
    		HashMap hm = (HashMap)assessorList.get(i);
    		String empn = "SCORE_" + (String)hm.get("EMPN");
    		excelInfoList.add(new ExcelVO((String)hm.get("KOR_NM"), empn, "center"));
    		itemArray[i] = (String)hm.get("EMPN");
    	}

    	searchMap.put("itemArray", itemArray);
    	
    	excelInfoList.add(new ExcelVO("평균평점",			"AVG_SCORE", 			"center"));
    	excelInfoList.add(new ExcelVO("표준편차",			"DEVIATION", 			"center"));
    	excelInfoList.add(new ExcelVO("총점",				"TOTAL_SCORE", 			"center"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("prs.mng.evalResult.getExcelList", searchMap));
    	
        return searchMap;
    }
    
    
    /**
     * 1차 평가 + MBO 실적(추정)엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap evalResultListExcel2(SearchMap searchMap) {
    	String excelFileName = "1차 평가 + MBO 실적(추정) 엑셀다운로드";
    	String excelTitle = "1차 평가 + MBO 실적(추정)엑셀다운로드 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();

    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("year")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가군", "EVAL_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("사번", "EVAL_MEMB_EMPN", "left"));
    	excelInfoList.add(new ExcelVO("성명", "KOR_NM", "left"));
    	excelInfoList.add(new ExcelVO("직급", "CAST_TC_NM", "left"));
    	excelInfoList.add(new ExcelVO("직위", "POS_TC_NM", "left"));
    	excelInfoList.add(new ExcelVO("소속부서", "DEPT_FULL_NM", "left"));
    	excelInfoList.add(new ExcelVO("1차평가점수", "FIRST_SCORE", "left"));
    	excelInfoList.add(new ExcelVO("MBO점수", "MBO_SCORE", "left"));
  
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("prs.mng.evalResult.getExcelList2", searchMap));
    	
        return searchMap;
    }
    
}
