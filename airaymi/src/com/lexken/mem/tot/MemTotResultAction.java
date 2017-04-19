/*************************************************************************
* CLASS 명      : MemTotResultAction
* 작 업 자      : 김상용
* 작 업 일      : 2013년 06월 18일 
* 기    능      : 평가결과-간부개인성과 평가결과
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    김 상 용      2013년 06월 10일    최 초 작 업 
**************************************************************************/
package com.lexken.mem.tot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginVO;
import com.lexken.framework.util.HtmlHelper;
import com.lexken.framework.util.StaticUtil;

public class MemTotResultAction extends CommonService {
	private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    public SearchMap memTotResultList(SearchMap searchMap) {
    	
    	searchMap.addList("evalGunList", getList("mem.tot.memTotResult.getEvalGunList", searchMap));
    	searchMap.addList("evalGradeList", getList("mem.tot.memTotResult.getEvalGradeList", searchMap));
    	
    	return searchMap;
    }
    


    public SearchMap memTotResultList_xml(SearchMap searchMap) {
    	
    	String totCntStr = getStr("mem.tot.memTotResult.selectAllRecords", searchMap);
    	int totCnt = Integer.parseInt(totCntStr);
    	String pageStr = searchMap.getString("page");
    	int page = Integer.parseInt(pageStr);
    	String rowsStr = searchMap.getString("rows");
    	int rows = Integer.parseInt(rowsStr);
    	
    	//System.out.println("================================="+pageStr);
    	
    	searchMap.addList("page", page);
    	searchMap.addList("total", (int)(totCnt/rows)+1);
    	searchMap.addList("records", totCnt);
    	searchMap.put("startRow", (15*(page-1))+1);
    	searchMap.put("endRow", (15*page)+1);
    	
    	searchMap.addList("list", getList("mem.tot.memTotResult.getList", searchMap));
    	
    	return searchMap;
    }
    
    public SearchMap evalGunList_ajax(SearchMap searchMap) {
    	
    	searchMap.addList("list", getList("mem.tot.memTotResult.getEvalGunList", searchMap));
    	
    	return searchMap;
    }
    
    public SearchMap memTotResultModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");

    	/**********************************
         * 로그인정보 (권한 체크)
         **********************************/
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	List list = getList("mem.tot.memTotResult.getDeptList", searchMap);

    	searchMap.addList("deptList", list);
    	searchMap.addList("deptRowSize", list.size()+2);

    	/**********************************
         * 조직별 지표 승인상태 조회
         **********************************/
    	searchMap.addList("evalFirstComment", getStr("mem.tot.memTotResult.getEvalFirstComment", searchMap));
    	searchMap.addList("evalSecondComment", getStr("mem.tot.memTotResult.getEvalSecondComment", searchMap));

    	HashMap detail = new HashMap();

    	detail = getDetail("mem.tot.memTotResult.getDetail", searchMap);
        searchMap.addList("detail", detail);

        return searchMap;
    }
    
    /**
     * 평가결과 불러오기/수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memTotResultProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("GET".equals(stMode)) {
            searchMap = batchMngResult(searchMap);
        }  else if("SAVESCORE".equals(stMode)) {
        	searchMap = saveScoreDB(searchMap);
        }  else if("SAVEGRADE".equals(stMode)) {
        	searchMap = saveGradeDB(searchMap);
        }  else if("REGET".equals(stMode)) {
        	searchMap = rebatchMngResult(searchMap);
        }
        
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
    public SearchMap saveScoreDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        String year = searchMap.getString("year");
	        String[] empNos = searchMap.getStringArray("empNoArray");
	        String[] totFinalScores = searchMap.getStringArray("totFinalScoreArray");
	        
	        if(empNos != null && 0<empNos.length){
	        	for(int i=0 ; i<empNos.length ; i++){
	        		searchMap.put("year", year);
	        		searchMap.put("empNo", empNos[i]);
	        		searchMap.put("totFinalScore", totFinalScores[i]);
	        		
	        		returnMap = updateData("mem.tot.memTotResult.updateScoreData", searchMap);
	        		
	        	}
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
     * 평가결과 가져오기
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap saveGradeDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        String year = searchMap.getString("year");
	        String[] empNos = searchMap.getStringArray("empNoArray");
	        String[] totFinalGradeIds = searchMap.getStringArray("totFinalGradeArray");
	        
	        if(empNos != null && 0<empNos.length){
	        	for(int i=0 ; i<empNos.length ; i++){
	        		searchMap.put("year", year);
	        		searchMap.put("empNo", empNos[i]);
	        		searchMap.put("totFinalGradeId", totFinalGradeIds[i]);
	        		
	        		returnMap = updateData("mem.tot.memTotResult.updateGradeData", searchMap);
	        		
	        	}
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
     * 평가결과 가져오기
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap batchMngResult(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        //사용안함 returnMap = insertData("mem.tot.memTotResult.deleteMngResult", searchMap, true);
	        //사용안함 returnMap = insertData("mem.tot.memTotResult.insertMngResult", searchMap, true);
	        //returnMap = insertData("prs.mng.evalResult.deleteEvalResult", searchMap, true);
	        //returnMap = insertData("prs.mng.evalResult.insertEvalResult", searchMap, true);
	        
	        //String evalMethodId = getStr("mem.tot.memTotResult.getEvalMethodId", searchMap);
	        
	        returnMap = insertData("mem.tot.memTotResult.execMngEvalFinalScore", searchMap);
	        returnMap = insertData("mem.tot.memTotResult.execMngEvalFinalGrade", searchMap);
	        
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
     * 평가결과 가져오기
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap rebatchMngResult(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        //사용안함 returnMap = insertData("mem.tot.memTotResult.deleteMngResult", searchMap, true);
	        //사용안함 returnMap = insertData("mem.tot.memTotResult.insertMngResult", searchMap, true);
	        //returnMap = insertData("prs.mng.evalResult.deleteEvalResult", searchMap, true);
	        //returnMap = insertData("prs.mng.evalResult.insertEvalResult", searchMap, true);
	        
	        //String evalMethodId = getStr("mem.tot.memTotResult.getEvalMethodId", searchMap);
	        
	        returnMap = insertData("mem.tot.memTotResult.execMngEvalFinalGrade", searchMap);
	        
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
        	searchMap.addList("detail", getDetail("mem.tot.memTotResult.getDetail", searchMap));
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
    	searchMap.put("excelDataList", (ArrayList)getList("mem.tot.memTotResult.getExcelList", searchMap));
    	
        return searchMap;
    }
}
