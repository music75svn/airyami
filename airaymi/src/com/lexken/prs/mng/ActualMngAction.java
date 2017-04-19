/*************************************************************************
* CLASS 명      : ActualMngAction
* 작 업 자      : 남기영
* 작 업 일      : 2013년 12월 9일 
* 기    능      : 성과실적 관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    남기영      2013년 12월 9일         최 초 작 업 
**************************************************************************/
package com.lexken.prs.mng;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.FileInfoVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginVO;
import com.lexken.framework.util.StaticUtil;

public class ActualMngAction extends CommonService{

private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    private final String EVAL_METHOD_ID = "G000003";
    
    /**
     * 성과계획서 관리 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalMngList(SearchMap searchMap) {
    	
    	searchMap.addList("actInTermList", getList("prs.mng.actInputTerm.getList", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 성과계획서 실적관리 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalMngList_xml(SearchMap searchMap) {
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	if(loginVO.chkAuthGrp("01")) searchMap.put("isAdmin", "Y");
    	
    	searchMap.addList("list", getList("prs.mng.actualMng.getList", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 성과계획서 실적관리 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap actualMngList(SearchMap searchMap) {
    	searchMap.put("findEvalMethodId", EVAL_METHOD_ID);
    	searchMap.addList("membersTable", getDetail("prs.mng.planMng.getFindList", searchMap));//목록리스트
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("mon")))) {
    		searchMap.put("mon",  (String)searchMap.getString("findMon"));
    	}
    	searchMap.addList("actInTerm", getDetail("prs.mng.actInputTerm.getDetail", searchMap));
    	searchMap.addList("gradeList", getList("bsc.common.evalGradeScore.getList", searchMap));
    	
    	/**********************************
         * 첨부파일
         **********************************/
    	searchMap.addList("fileList", getList("prs.mng.actualMng.getFileList", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 성과계획서 지표실적관리 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap actualMngList_xml(SearchMap searchMap) {
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	if(loginVO.chkAuthGrp("01")) searchMap.put("isAdmin", "Y");
    	
    	searchMap.addList("list", getList("prs.mng.actualMng.getActualList", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 성과계획서 실적 관리 등록/수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap actualMngProcess(SearchMap searchMap) {
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
		 * 파일복사
		 **********************************/
		searchMap.fileCopy("/temp", "/actualMng");
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("SAVE".equals(stMode)) {
            searchMap = saveDB(searchMap);
        } else if("APC".equals(stMode)) {
        	searchMap = approveDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 성과계획서 관리 확인요청
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap saveDB(SearchMap searchMap) {
    	HashMap returnMap    = new HashMap();    
    	
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
    	try {
    		setStartTransaction();
    		
    		String[] targetIds = searchMap.getStringArray("targetIds");
    		String[] values = searchMap.getStringArray("values");
    		String[] scores = searchMap.getStringArray("scores");
    		String[] evalGrades = searchMap.getStringArray("evalGrades");
    		String metricGubun = "";

    		if(loginVO.chkAuthGrp("01")) searchMap.put("isAdmin", "Y");
    		
    		for(int i = 0; i < targetIds.length; i++){

    			if(targetIds != null){
    				searchMap.put("targetId", targetIds[i]);
    			}
        		if(values != null){
        			searchMap.put("value", values[i]);
        		}
    			
        		if(scores != null){
            		searchMap.put("score", scores[i]);
        		}
        		
        		if(evalGrades != null){
        			searchMap.put("evalGrade", evalGrades[i]);
        		}
        		
        		metricGubun = getStr("prs.mng.actualMng.selectMetricGubun", searchMap);
        		
        		if("01".equals(metricGubun)){
        			returnMap = updateData("prs.mng.actualMng.updateData", searchMap);
        		}else if("02".equals(metricGubun)){
        			returnMap = updateData("prs.mng.actualMng.updateDownData", searchMap);
        		}
        		
        		returnMap = updateData("prs.mng.actualMng.updateScoreData", searchMap);
        		
        		returnMap = updateData("prs.mng.actualMng.updateCalScoreData", searchMap);


    		}
    		
    		/**********************************
	         * 첨부파일 정보 등록
	         **********************************/
	        returnMap = insertFileInfo(searchMap);
    		
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
     * 성과계획서실적 관리 확인요청
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap approveDB(SearchMap searchMap) {
    	HashMap returnMap    = new HashMap();    
    	
    	try {
    		setStartTransaction();
    		
    		returnMap = updateData("prs.mng.actualMng.updateStatusData", searchMap);
    		
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
     * 첨부파일 정보등록
     * @param      
     * @return String  
     * @throws 
     */
    public HashMap insertFileInfo(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        
        try {
        	String[] delAttachFiles = searchMap.getStringArray("delAttachFiles");
        	
        	/**********************************
             * 삭제체크된 첨부파일 삭제
             **********************************/
        	if(null != delAttachFiles) {
	        	for(int i = 0; i < delAttachFiles.length; i++){
	        		searchMap.put("seq", delAttachFiles[i]);
					returnMap = insertData("prs.mng.actualMng.deleteFileInfo", searchMap);
	        	}
        	}
        	
        	/**********************************
             * 첨부파일 등록
             **********************************/
        	ArrayList fileInfoList = new ArrayList();
        	fileInfoList = (ArrayList)searchMap.get("FileInfoList");
	        FileInfoVO fileInfoVO = new FileInfoVO();
	        
	        if(null != fileInfoList) {
				for(int i = 0; i < fileInfoList.size(); i++){
					fileInfoVO = (FileInfoVO)fileInfoList.get(i);
					if(fileInfoVO != null){
						searchMap.put("attachFileFnm", 	fileInfoVO.getMaskFileName());
						searchMap.put("attachFileNm", 	fileInfoVO.getRealFileName());
						searchMap.put("attachFileSufix",fileInfoVO.getFileExtension());
						searchMap.put("attachFilePath", fileInfoVO.getFileUploadPath());
						returnMap = insertData("prs.mng.actualMng.insertFileInfo", searchMap);
					}
				}
	        }
	        
        } catch (Exception e) {
        	logger.error(e.toString());
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        }
        return returnMap;    
    }
    
    /**
     * 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap evalMngListExcel(SearchMap searchMap) {
    	String excelFileName = "간부업적평가 실적등록";
    	String excelTitle = "간부업적평가 실적등록 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	excelSearchInfoList.add(new ExcelVO("년도", (String)searchMap.get("findYear")));
    	excelSearchInfoList.add(new ExcelVO("분기", (String)searchMap.get("findMonNm")));

    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("사번","EVAL_MEMB_EMPN","center"));
    	excelInfoList.add(new ExcelVO("이름","KOR_NM","center"));
    	excelInfoList.add(new ExcelVO("부서명","DEPT_FULL_NM","left"));
    	excelInfoList.add(new ExcelVO("직무수행시작일","FROM_DT","left"));
    	excelInfoList.add(new ExcelVO("직무수행종료일","TO_DT","left"));
    	excelInfoList.add(new ExcelVO("설정방향","DIRECTION_NM","center"));
    	excelInfoList.add(new ExcelVO("지표구분","METRIC_GUBUN","center"));
    	excelInfoList.add(new ExcelVO("성과목표","TARGET_NM","left"));
    	excelInfoList.add(new ExcelVO("성과지표","METRIC_NM","left"));
    	excelInfoList.add(new ExcelVO("연간목표","TARGET_VALUE","center"));
    	excelInfoList.add(new ExcelVO("단위","UNIT","center"));
    	excelInfoList.add(new ExcelVO("가중치","WEIGHT","center"));
    	excelInfoList.add(new ExcelVO("난이도","DFFLY_NM","center"));
    	excelInfoList.add(new ExcelVO("누계목표","TARGET_VALUE","center"));
    	excelInfoList.add(new ExcelVO("연간목표(분기별)","TARGET_VALUE_Q","center"));
    	excelInfoList.add(new ExcelVO("실적(누계)","VALUE","center"));
    	excelInfoList.add(new ExcelVO("달성도","SCORE","center"));
    	excelInfoList.add(new ExcelVO("평가등급","EVAL_GRADE","center"));
    	excelInfoList.add(new ExcelVO("환산점수","EVAL_SCORE","center"));
    	excelInfoList.add(new ExcelVO("계획서상태","PLAN_STATUS_NM","center"));
    	excelInfoList.add(new ExcelVO("실적상태","ACT_STATUS_NM","center"));
    	
    	if("08".equals((String)searchMap.get("findMon"))){
    		searchMap.put("findMon","20");
    	}	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("prs.mng.actualMng.getListExcel", searchMap));
    	
    	
        return searchMap;
    }
}
