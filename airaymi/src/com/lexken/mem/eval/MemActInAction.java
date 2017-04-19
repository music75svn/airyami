/*************************************************************************
* CLASS 명      : MemActInAction
* 작 업 자      : 유연주
* 작 업 일      : 2017년 03월 13일 
* 기    능      : 실적입력
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    유연주      2017년 03월 13일          최 초 작 업 
**************************************************************************/
package com.lexken.mem.eval;
    
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.FileInfoVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginVO;
import com.lexken.framework.util.HtmlHelper;

public class MemActInAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 실적입력 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memActInList(SearchMap searchMap) {
    	
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
    	searchMap.addList("periodInfo", getDetail("mem.eval.memActIn.getPeriodInfo", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 실적입력 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memActInList_xml(SearchMap searchMap) {
    	
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
    	if(!loginVO.chkAuthGrp("01")) {
    		searchMap.put("sEmpNo", loginVO.getUser_id());
    	}
        
        searchMap.addList("list", getList("mem.eval.memActIn.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 실적조회 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memActSearchList(SearchMap searchMap) {
    	
    	searchMap.addList("periodInfo", getDetail("mem.eval.memActIn.getPeriodInfo", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 실적조회 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memActSearchList_xml(SearchMap searchMap) {

    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
    	searchMap.put("mode", "SEARCH");
    	searchMap.put("sEmpNo", loginVO.getUser_id());
    	
        searchMap.addList("list", getList("mem.eval.memActIn.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 실적입력 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memActInModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	
    	// 실적입력기간 조회
    	searchMap.addList("periodInfo", getDetail("mem.eval.memActIn.getPeriodInfo", searchMap));
    	
    	// 면담여부 조회
    	searchMap.addList("meetYn", getDetail("mem.eval.memActIn.getMeetYn", searchMap));
    	
    	// 자기성과기술서 상세
    	searchMap.addList("detail", getDetail("mem.eval.memJobDesc.getDetail", searchMap));
    	
    	// 평가요청유무 상세
    	searchMap.addList("evalYn", getDetail("mem.eval.memActIn.getEvalReqYn", searchMap));
        return searchMap;
    }
    
    /**
     * 실적입력 평가항목 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memActInEvalItemList_xml(SearchMap searchMap) {
    	
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
        
        searchMap.addList("list", getList("mem.eval.memActIn.getEvalItemList", searchMap));

        return searchMap;
    }
    
    /**
     * 실적입력 엑셀변환다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap memActInExcel(SearchMap searchMap) {
    	String excelFileName = "실적입력";
    	String excelTitle = "실적입력목록";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO("기준년도", (String)searchMap.get("yearNm")));
    	
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	if(loginVO.chkAuthGrp("01") || "SEARCH".equals((String)searchMap.get("mode"))) {
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
    	excelInfoList.add(new ExcelVO("계획서상태", "METRIC_STATUS_NM", "center"));
    	excelInfoList.add(new ExcelVO("최종실적입력월", "FINAL_AC_IN_MON", "center"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	
    	
    	if(!loginVO.chkAuthGrp("01")) {
    		searchMap.put("sEmpNo", loginVO.getUser_id());
    	}
    	
    	searchMap.put("excelDataList", (ArrayList)getList("mem.eval.memActIn.getList", searchMap));
    	
    	return searchMap;
    	
    }
    
    /**
     * 실적관리 상세데이터 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memActualMngDetailList(SearchMap searchMap) {
	        
    	String metricId = searchMap.getString("metricId");
    	
    	if(!"".equals(metricId)) {
	    	/**********************************
	         * 월별실적조회
	         **********************************/
	        searchMap.addList("list", getList("mem.eval.memActualMng.getList", searchMap));
	        
	        /**********************************
	         * 목표, 실적, 달성율 조회
	         **********************************/
	        searchMap.addList("scoreList", getList("mem.eval.memActualMng.getScoreList", searchMap));
	        
	        /**********************************
	         * 실적산식 조회
	         **********************************/
	        HashMap detail = (HashMap)getDetail("mem.eval.memActualMng.getCalTypeInfo", searchMap);
	        searchMap.addList("detail", detail);
	        String actCalTypeNm = (String)detail.get("ACT_CAL_TYPE");
	        HashMap<String, String> calTyepColValueMap = new HashMap<String, String>();
	        ArrayList calTypeColList = (ArrayList)getList("mem.eval.memActualMng.calTypeColList", searchMap);
	        
	        if(null != calTypeColList && 0 < calTypeColList.size()) {
		        for (int i = 0; i < calTypeColList.size(); i++) {
		        	HashMap<String, String> t = (HashMap<String, String>)calTypeColList.get(i);
					calTyepColValueMap.put((String)t.get("CAL_TYPE_COL"), (String)t.get("CAL_TYPE_COL_NM"));
				}
	        }
	        
	        /**********************************
	         * 실적산식명 가져오기
	         **********************************/
	        String calTypeColDesc = "";
	        if(null != calTypeColList && 0 < calTypeColList.size()) {
		        calTypeColDesc = (String)HtmlHelper.changeCalNmToCalDesc(actCalTypeNm, calTyepColValueMap);
		        searchMap.addList("calTypeColDesc", calTypeColDesc);
	        }
    	}
    	
        /**********************************
         * 실적입력기한 여부 가져오기
         **********************************/
        searchMap.addList("actInputTermYn", getStr("mem.eval.memActualMng.getActInputTermYn", searchMap));
        
        /**********************************
         * 조회 월이 실적입력 월인지 확인
         **********************************/
        searchMap.addList("actRegDefMonYn", getStr("mem.eval.memActualMng.getActRegDefMonYn", searchMap));
        
        /**********************************
         * 파일목록조회
         **********************************/
        searchMap.addList("fileList", getList("mem.eval.memActIn.getAttachFileList", searchMap));
        
        return searchMap;
    }
    
    /**
     * ActionPlan 목록조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memActionPlanList(SearchMap searchMap) {
    	
    	List actionPlanList = getList("mem.eval.memActIn.getActionPlanList", searchMap);
    	logger.debug("actionPlanList size : "+actionPlanList.size());
    	logger.debug("actionPlanList : "+actionPlanList);
    	searchMap.addList("actionPlanlist", actionPlanList);
    	
        return searchMap;
    }

    /**
     * ActionPlan 상세 팝업화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popMemActionPlanList(SearchMap searchMap) {
    	
    	searchMap.addList("info", getDetail("mem.eval.memActIn.getPopActionPlanInfo", searchMap));
    	
    	// 달 조회
    	searchMap.put("findCodeGrpId", "024");
    	searchMap.addList("monList", getList("bsc.base.code.getList", searchMap));
    	
    	
        return searchMap;
    }
    
    /**
     * ActionPlan 상세 팝업 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popMemActionPlanList_xml(SearchMap searchMap) {
    	
        searchMap.addList("list", getList("mem.eval.memActIn.getPopActionPlanDetailList", searchMap));

        return searchMap;
    }
    
    /**
     * ActionPlan 저장
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popMemActionPlanProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * ActionPlan 저장
         **********************************/
        searchMap = saveActionPlanDB(searchMap);
        
        return searchMap;
    }
    
    /**
     * ActionPlan 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap saveActionPlanDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
	        // ActionPlan 삭제
	        returnMap = deleteData("mem.eval.memActIn.deleteActionPlanList", searchMap, true);
	        
	        // ActionPlan 등록
	        String[] startMons = searchMap.getStringArray("startMon");
	        String[] endMons = searchMap.getStringArray("endMon");
	        String[] actContents = searchMap.getStringArray("actContent");
	        String[] procRates = searchMap.getStringArray("procRate");
	        
	        if(null != startMons && 0 < startMons.length) {
		        for (int i = 0; i < startMons.length; i++) {
		            searchMap.put("startMon", startMons[i]);
		            searchMap.put("endMon", endMons[i]);
		            searchMap.put("actContent", actContents[i]);
		            searchMap.put("procRate", procRates[i]);
		            searchMap.put("actionPlanSeq", (i+1)+"");
		            
		            logger.debug("searchMap : "+searchMap);
		            returnMap = insertData("mem.eval.memActIn.insertActionPlanList", searchMap, true);
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
     * 실적 저장
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memActInProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 무결성 체크
         **********************************/
        if(!"DEL".equals(stMode) && !"SAVE".equals(stMode) && !"MODUSER".equals(stMode) ) {
            returnMap = this.validChk(searchMap);
            
            if((Integer)returnMap.get("ErrorNumber") < 0 ){
                searchMap.addList("returnMap", returnMap);
                return searchMap;
            }
        }
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("MOD".equals(stMode)) {
            searchMap = saveActDB(searchMap);
        }else if("MEET".equals(stMode)) {
        	searchMap = insertMeetDB(searchMap);
        }else if("CANCEL".equals(stMode)) {
        	searchMap = deleteMeetDB(searchMap);
        }else if("EVAL_CANCEL".equals(stMode)) {
        	searchMap = updateEvalReqDB(searchMap);
        }
        
        return searchMap;
    }
    
    /**
     * 평가요청취소
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateEvalReqDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();

        	// 평가요청취소
        	searchMap.put("evalReqYn", "N");
        	searchMap.put("findEmpNo", searchMap.get("empNo"));
	        returnMap = insertData("mem.eval.memActIn.updateEvalReqData", searchMap);
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
     * 면담요청 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertMeetDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();

        	// 면담요청 등록
	        returnMap = insertData("mem.eval.memActIn.insertMeetData", searchMap);
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
     * 면담취소
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteMeetDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
	        // 면담취소
	        returnMap = deleteData("mem.eval.memActIn.deleteMeetData", searchMap);
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
     * ActionPlan 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap saveActDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
        
        try {
        	setStartTransaction();
        
            /**********************************
    		 * 파일복사
    		 **********************************/
    		searchMap.fileCopy("/temp", "/memActual");
    		
            /**********************************
    		 * 실적등록
    		 **********************************/
            /**********************************
             * Parameter setting
             **********************************/
            String[] colNames = searchMap.getStringArray("colNames");
            String[] colValues = searchMap.getStringArray("colValues");
            String[] colMons = searchMap.getStringArray("colMons");
            
    		
        	/**********************************
             * 산식항목별 실적입력
             **********************************/
        	if(colNames != null && 0 < colNames.length) {
        		if(colValues != null && 0 < colValues.length) {
		        	for (int i = 0; i < colNames.length; i++) {
			            searchMap.put("colName", colNames[i]);
			            searchMap.put("colValue", colValues[i]);
			            searchMap.put("mon", colMons[i]);
			            
			            searchMap.put("insertEmpId", loginVO.getUser_id());
			            searchMap.put("insertEmpNm", loginVO.getUser_nm());
			            searchMap.put("analCycle", searchMap.getString("evalCycleId"));
			            searchMap.put("seq", (i+1)+"");
			            searchMap.put("actStatusId", "");
			            
			            logger.debug("================================================================");
			            logger.debug("searchMap : "+searchMap);
			            logger.debug("================================================================");
			            logger.debug("year : "+searchMap.getString("year"));
			            logger.debug("mon : "+searchMap.getString("mon"));
			            logger.debug("analCycle : "+searchMap.getString("analCycle"));
			            logger.debug("seq : "+searchMap.getString("seq"));
			            logger.debug("metricId : "+searchMap.getString("metricId"));
			            logger.debug("colName : "+searchMap.getString("colName"));
			            logger.debug("colValue : "+searchMap.getString("colValue"));
			            logger.debug("actStatusId : "+searchMap.getString("actStatusId"));
			            logger.debug("insertEmpId : "+searchMap.getString("insertEmpId"));
			            logger.debug("insertEmpNm : "+searchMap.getString("insertEmpNm"));
			            returnMap = insertData("mem.eval.memActualMng.insertData", searchMap);
			        }
        		}
        	}
    		
        	String[] delAttachFiles = searchMap.getStringArray("delAttachFiles");
        	
        	/**********************************
             * 삭제체크된 첨부파일 삭제
             **********************************/
        	if(null != delAttachFiles) {
	        	for(int i = 0; i < delAttachFiles.length; i++){
	        		searchMap.put("seq", delAttachFiles[i]);
					returnMap = insertData("mem.eval.memActIn.deleteFileInfo", searchMap);
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
						
						if(!"fileDesc".equals(fileInfoVO.getFileObjectName())){
							returnMap = insertData("mem.eval.memActIn.insertFileInfo", searchMap);
						}else{
							if(!"".equals(fileInfoVO.getFileUploadPath())){
								returnMap = updateData("mem.eval.memJobDesc.updateData", searchMap);
							}
						}
					}
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
     * 실적입력 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popMemSelfEvalList(SearchMap searchMap) {
    	
        searchMap.addList("gradeList", getList("mem.eval.memActIn.getSelfGradeList", searchMap));
        
    	return searchMap;
    }
    
    /**
     * 실적입력 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popMemSelfEvalList_xml(SearchMap searchMap) {
    	
        searchMap.addList("list", getList("mem.eval.memActIn.popMemSelfEvalList", searchMap));

        return searchMap;
    }
    
    /**
     * 자기평가 저장
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popMemSelfEvalProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 자기평가 저장
         **********************************/
        searchMap = saveSelfEvalDB(searchMap);
        
        return searchMap;
    }
    
    /**
     * 자기평가 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap saveSelfEvalDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
	        
	        // 자기평가 등록
	        String[] evalGradeId = searchMap.getStringArray("evalGradeId");
            searchMap.put("evalGradeId", evalGradeId[0]);
            
	        String[] opinion = searchMap.getStringArray("opinion");
	        searchMap.put("excellentOpinion", opinion[0]);
	        searchMap.put("unsufficientOpinion", opinion[1]);
	        searchMap.put("etcOpinion", opinion[2]);
	        
		    returnMap = insertData("mem.eval.memActIn.insertSelfEvalData", searchMap, true);
		    
		    searchMap.put("evalReqYn", "Y");
		    returnMap = updateData("mem.eval.memActIn.updateEvalReqData", searchMap, true);
        
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
        String stMode = searchMap.getString("mode");
        
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }
}
