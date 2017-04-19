/*************************************************************************
* CLASS 명      : ActualMngAction
* 작 업 자      : 정철수
* 작 업 일      : 2012년 11월 1일 
* 기    능      : 실적입력/승인
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    정철수      2012년 11월 1일         최 초 작 업 
**************************************************************************/
package com.lexken.str.tam;
    
import java.util.ArrayList;
import java.util.HashMap;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.FileInfoVO;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.struts2.IsBoxActionSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.login.LoginVO;
import com.lexken.framework.util.StaticUtil;

public class ActualMngAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 실적입력/승인 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap actualMngList(SearchMap searchMap) {
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
    	String actualType = searchMap.getString("findActualType");
    	
    	/**********************************
         * 실적 입력자 조회
         **********************************/
    	searchMap.addList("insertUserList", getList("str.tam.actualMng.getInsertUserList", searchMap));
    	
    	/**********************************
         * 실적 승인자 조회
         **********************************/
    	searchMap.addList("approveUserList", getList("str.tam.actualMng.getApproveUserList", searchMap));
    	
    	/**********************************
         * 실적입력기한 여부 가져오기
         **********************************/
        searchMap.addList("actualInputTermYn", getStr("str.tam.actualMng.getActualInputTermYn", searchMap));
        
        /**********************************
         * 년마감 여부 가져오기
         **********************************/
        searchMap.addList("yearClosingYn", getStr("str.system.closingYear.getYearClosingYn", searchMap));
        
        /**********************************
         * 권한별 처리
         **********************************/
    	if(loginVO.chkAuthGrp("01") || loginVO.chkAuthGrp("60") || loginVO.chkAuthGrp("14")) {
    		if("".equals(StaticUtil.nullToBlank((String)searchMap.get("findInsertUserId")))) {
    			searchMap.put("findInsertUserId", searchMap.getDefaultValue("insertUserList", "CHARGE_USER_ID", 0));
    		}
    		
    		if("".equals(StaticUtil.nullToBlank((String)searchMap.get("findApproveUserId")))) {
    			searchMap.put("findApproveUserId", searchMap.getDefaultValue("approveUserList", "APPROVE_USER_ID", 0));
    		}
    	} else {
    		searchMap.put("findInsertUserId", searchMap.get("loginUserId"));
    		searchMap.put("findApproveUserId", searchMap.get("loginUserId"));
    	}
    	
    	/************************************
         * 입력대상 세부과제 지표조회
         ************************************/
    	if("input".equals(actualType)) {
    		searchMap.addList("managementTreeList", getList("str.tam.actualMng.getInsertManagementTree", searchMap));
    		searchMap.addList("strStratSubjectTreeList", getList("str.tam.actualMng.getInsertStrStratSubjectTree", searchMap));
	        searchMap.addList("subjectTreeList", getList("str.tam.actualMng.getInsertSubjectTree", searchMap));
	        searchMap.addList("metricTreeList", getList("str.tam.actualMng.getInsertMetricTree", searchMap));
    	}else{
    		searchMap.addList("managementTreeList", getList("str.tam.actualMng.getApproveManagementTree", searchMap));
    		searchMap.addList("strStratSubjectTreeList", getList("str.tam.actualMng.getApproveStrStartTree", searchMap));
    		searchMap.addList("subjectTreeList", getList("str.tam.actualMng.getApproveSubjectTree", searchMap));
	        searchMap.addList("metricTreeList", getList("str.tam.actualMng.getApproveMetricTree", searchMap));
    	}
    	
    	/**********************************
         * 디폴트 입력 지표 조회
         **********************************/
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("subjectMetricId")))) {
    		searchMap.put("subjectMetricId", (String)searchMap.getString("findSubjectMetricId"));
    	}
    	
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("subjectMetricId")))) {
    		searchMap.put("subjectMetricId", searchMap.getDefaultValue("metricTreeList", "SUBJECT_METRIC_ID", 0));
    	}

        return searchMap;
    }
    
    
    
    public SearchMap insertUserList_ajax(SearchMap searchMap) {

    	/**********************************
         * 목표 입력자 조회
         **********************************/
    	searchMap.addList("insertUserList", getList("str.tam.actualMng.getInsertUserList", searchMap));
    	
        return searchMap;
    }
    
    public SearchMap approveUserList_ajax(SearchMap searchMap) {

    	/**********************************
         * 목표 승인자 조회
         **********************************/
    	searchMap.addList("approveUserList", getList("str.tam.actualMng.getApproveUserList", searchMap));
    	
        return searchMap;
    }
    
    
    /**
     * 실적관리 상세데이터 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap actualMngDetailList(SearchMap searchMap) {
	        
    	String subjectMetricId = searchMap.getString("subjectMetricId");
    	
    	
    	if(!"".equals(subjectMetricId)) {
	    	/**********************************
	         * 월별목표조회
	         **********************************/
	        searchMap.addList("list", getList("str.tam.actualMng.getList", searchMap));
	        
	        /**********************************
	         * 실적입력상태 가져오기
	         **********************************/
	        searchMap.addList("detail", getDetail("str.tam.actualMng.getActualStatusId", searchMap));
	        
	        /**********************************
	         * 첨부파일 조회
	         **********************************/
	        searchMap.addList("fileList", getList("str.tam.actualMng.getFileList", searchMap));
	   
    	}
    	
        /**********************************
         * 목표입력기한 여부 가져오기
         **********************************/
    	searchMap.addList("actualInputTermYn", getStr("str.tam.actualMng.getActualInputTermYn", searchMap));
        
        
        
        return searchMap;
    }
    
    /**
     * 실적입력/승인 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap actualMngList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("str.tam.actualMng.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 실적입력/승인 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap actualMngModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("str.tam.actualMng.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 실적입력/승인 등록/수정/삭제
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
		searchMap.fileCopy("/temp", "/str/target");
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("ADD".equals(stMode)) {
            searchMap = insertDB(searchMap);
        } else if("MOD".equals(stMode)) {
            searchMap = updateDB(searchMap);
        } else if("DEL".equals(stMode)) {
            searchMap = deleteDB(searchMap);
        } else if("STATUS".equals(stMode)) {
            searchMap = statusDB(searchMap);
        }  
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 실적입력/승인 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        	
        	
        	/**********************************
             * 첨부파일 정보 등록
             **********************************/
	        returnMap = insertFileInfo(searchMap);
        	
        	/**********************************
             * 월별 실적입력
             **********************************/
        	returnMap = updateData("str.tam.actualMng.deleteData", searchMap, true);
        	
        	searchMap.put("insertUserId", searchMap.get("loginUserId"));
        	returnMap = insertData("str.tam.actualMng.insertData", searchMap);
        	
        	
        	/**********************************
             * 월별 점수입력
             **********************************/
        	returnMap = updateData("str.tam.actualMng.deleteScoreData", searchMap, true);
        	
        	returnMap = insertData("str.tam.actualMng.insertScoreData", searchMap);
        	
        	
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
     * 실적상태 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap statusDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();
        
        /**********************************
         * Parameter setting
         **********************************/
        String[] subjectMetricIds = searchMap.getString("subjectMetricIds").split("\\|", 0);
        String   returnReason = searchMap.getString("returnReason");
        
        try {
        	setStartTransaction();
	        
        	if(null != subjectMetricIds && 0 < subjectMetricIds.length) {
		        for (int i=0; i < subjectMetricIds.length; i++) {
		            searchMap.put("subjectMetricId", subjectMetricIds[i]);
		            if(returnReason.equals("")){
		            	returnMap = updateData("str.tam.actualMng.updateStatusData", searchMap, true);
		            }else{
		            	returnMap = updateData("str.tam.actualMng.updateReturnStatusData", searchMap, true);
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
     * 실적입력/승인 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("str.tam.actualMng.updateData", searchMap);
	        
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
     * 실적입력/승인 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] mons = searchMap.getString("mons").split("\\|", 0);
			String[] subjectMetricIds = searchMap.getString("subjectMetricIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != mons && 0 < mons.length) {
		        for (int i = 0; i < mons.length; i++) {
		            searchMap.put("mon", mons[i]);
			searchMap.put("subjectMetricId", subjectMetricIds[i]);
		            returnMap = updateData("str.tam.actualMng.deleteData", searchMap);
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
        
		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("actual"), "실적");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}


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
        	if(null != delAttachFiles && 0 < delAttachFiles.length) {
	        	for(int i = 0; i < delAttachFiles.length; i++){
	        		searchMap.put("seq", delAttachFiles[i]);
					returnMap = insertData("str.tam.actualMng.deleteFileInfo", searchMap);
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
						searchMap.put("attachFileSuffix",fileInfoVO.getFileExtension());
						searchMap.put("attachFilePath", fileInfoVO.getFileUploadPath());
						returnMap = insertData("str.tam.actualMng.insertFileInfo", searchMap);
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
     * 반려입력 팝업
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popReturnActual(SearchMap searchMap) {
    	return searchMap;
    }
    
    /**
     * 반려조회 팝업
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popReturnView(SearchMap searchMap) {
    	
    	/**********************************
         * 반려 내용 가져오기
         **********************************/
        searchMap.addList("returnReason", getStr("str.tam.actualMng.getReturnReason", searchMap));
    	
    	return searchMap;
    }

    /**
     * 실적입력/승인 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap actualMngListExcel(SearchMap searchMap) {
    	String excelFileName = "실적입력/승인";
    	String excelTitle = "실적입력/승인 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	/*
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("공통코드명", StaticUtil.nullToDefault((String)searchMap.get("codeGrpNm"), "전체")));
    	excelSearchInfoList.add(new ExcelVO("사용여부", (String)searchMap.get("useYnNm")));
    	*/
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("LV", "LV", "left"));
    	excelInfoList.add(new ExcelVO("MON01", "MON01", "left"));
    	excelInfoList.add(new ExcelVO("MON02", "MON02", "left"));
    	excelInfoList.add(new ExcelVO("MON03", "MON03", "left"));
    	excelInfoList.add(new ExcelVO("MON04", "MON04", "left"));
    	excelInfoList.add(new ExcelVO("MON05", "MON05", "left"));
    	excelInfoList.add(new ExcelVO("MON06", "MON06", "left"));
    	excelInfoList.add(new ExcelVO("MON07", "MON07", "left"));
    	excelInfoList.add(new ExcelVO("MON08", "MON08", "left"));
    	excelInfoList.add(new ExcelVO("MON09", "MON09", "left"));
    	excelInfoList.add(new ExcelVO("MON10", "MON10", "left"));
    	excelInfoList.add(new ExcelVO("MON11", "MON11", "left"));
    	excelInfoList.add(new ExcelVO("MON12", "MON12", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("str.tam.actualMng.getList", searchMap));
    	
        return searchMap;
    }
    
}
