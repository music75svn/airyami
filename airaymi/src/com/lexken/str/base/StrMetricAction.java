package com.lexken.str.base;

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
import com.lexken.framework.core.StaticFactory;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.login.LoginVO;
import com.lexken.framework.util.StaticUtil;

public class StrMetricAction extends CommonService {

	private static final long serialVersionUID = 1L;
	    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 이행성과지표 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap strMetricList(SearchMap searchMap) {
    	
    	String findUseYn = searchMap.getString("findUseYn");
    	if("".equals(StaticUtil.nullToBlank(findUseYn))){
    		searchMap.addList("findUseYn", findUseYn);
    	}
    	
    	searchMap.addList("managementList", getList("str.base.strMetric.getManagement", searchMap));
    	
    	searchMap.addList("stratSubjectList", getList("str.base.strMetric.getStrStratSubjectList", searchMap));
    	
    	searchMap.addList("csfList", getList("str.base.strMetric.getCsfList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 이행성과지표 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap strMetricList_xml(SearchMap searchMap) {
    	
    	searchMap.addList("list", getList("str.base.strMetric.getList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 경영목표 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap managementList_ajax(SearchMap searchMap) {
    	
    	searchMap.addList("managementList", getList("str.base.strMetric.getManagement", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 전략과제 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap stratSubjectList_ajax(SearchMap searchMap) {
        
    	searchMap.addList("stratSubjectList", getList("str.base.strMetric.getStrStratSubjectList", searchMap));

        return searchMap;
    }
    
    /**
     * CSF 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap csfList_ajax(SearchMap searchMap) {
        
    	searchMap.addList("csfList", getList("str.base.strMetric.getCsfList", searchMap));

        return searchMap;
    }
    
    
    /**
     * 이행성과지표관리 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap strMetricModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		
    		searchMap.addList("csfList1", getList("str.base.strMetric.getCsfList1", searchMap));
    		searchMap.addList("fileList", getList("str.base.strMetric.getFileList", searchMap));
    		searchMap.addList("detail", getDetail("str.base.strMetric.getDetail", searchMap));
    	}else{
    		
    		searchMap.addList("csfList1", getList("str.base.strMetric.getCsfList1", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 이행성과지표관리 관리 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap strMetricProcess(SearchMap searchMap) {
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
		searchMap.fileCopy("/temp", "/strMetric");
        
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
     *  Validation 체크(무결성 체크)
     * @param SearchMap
     * @return HashMap
     */
    private HashMap validChk(SearchMap searchMap) {
        HashMap returnMap         = new HashMap();
        int     resultValue        = 0;
        
        //Validation 체크 추가
        
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }

    
    /**
     * 난이도평가 평가단 관리 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        	
        	/**********************************
             * 지표코드 채번 
             **********************************/
	        String metricId = getStr("str.base.strMetric.getMetricId", searchMap);
	        searchMap.put("subjectMetricId", metricId);
        
        	returnMap = insertData("str.base.strMetric.insertData", searchMap);
        
        	/**********************************
	         * 목표정보  생성
	         **********************************/
	        returnMap = updateData("str.base.strMetric.insertTargetData", searchMap);
	        
	        /**********************************
	         * 권한 정보 등록
	         **********************************/
			returnMap = insertAdmin(searchMap);
			
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
     * 이행성과지표관리 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("str.base.strMetric.updateData", searchMap);
	        
	        /**********************************
	         * 권한 정보 등록
	         **********************************/
			returnMap = insertAdmin(searchMap);
			
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
     * 지표권한등록
     * 08 : 담당자(목표/실적입력자)
     * 09 : 승인자(목표/실적승인자)
     * @param      
     * @return String  
     * @throws 
     */
    public HashMap insertAdmin(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        
        try {
        	
        	/**********************************
             * 기존 권한삭제 
             **********************************/
 			returnMap = updateData("str.base.strMetric.deleteAdmin", searchMap, true);
	        
	        /**********************************
             * KPI담당자(실적입력자) 입력 
             **********************************/
 			returnMap = updateData("str.base.strMetric.insertChargeUserAdmin", searchMap);
	        
	        /**********************************
             * 실적승인자 입력 
             **********************************/
 			returnMap = updateData("str.base.strMetric.insertApproveUserAdmin", searchMap);
	        
        } catch (Exception e) {
        	logger.error(e.toString());
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        }
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
					returnMap = deleteData("str.base.strMetric.deleteFileInfo", searchMap);
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
						returnMap = insertData("str.base.strMetric.insertFileInfo", searchMap);
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
     * 이행성과지표 관리 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] subjectMetricIds = searchMap.getString("subjectMetricIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != subjectMetricIds && 0 < subjectMetricIds.length) {
		        for (int i = 0; i < subjectMetricIds.length; i++) {
		            searchMap.put("subjectMetricId", subjectMetricIds[i]);
		            returnMap = updateData("str.base.strMetric.deleteData", searchMap);
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
     * 평가실시 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap strMetricListExcel(SearchMap searchMap) {
    	String excelFileName = "이행성과지표";
    	String excelTitle = "이행성과지표 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("findYear")));
    	excelSearchInfoList.add(new ExcelVO("전략과제", StaticUtil.nullToDefault((String)searchMap.get("findStratSubjectNm"), "전체")));
    	excelSearchInfoList.add(new ExcelVO("세부과제", StaticUtil.nullToDefault((String)searchMap.get("findSubjectNm"), "전체")));
    	excelSearchInfoList.add(new ExcelVO("사용여부", StaticUtil.nullToDefault((String)searchMap.get("findUseYnNm"), "전체")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("전략과제", "STRAT_SUBJECT_NM", "left","STRAT_CNT"));
    	excelInfoList.add(new ExcelVO("세부과제", "SUBJECT_NM", "left","SUB_CNT"));
    	excelInfoList.add(new ExcelVO("이행성과지표", "SUBJECT_METRIC_NM", "left"));
    	excelInfoList.add(new ExcelVO("담당부서", "CHARGE_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("담당자", "CHARGE_USER_NM", "left"));
    	excelInfoList.add(new ExcelVO("주기", "CYCLE_NM", "left"));
    	excelInfoList.add(new ExcelVO("단위", "UNIT_NM", "left"));
    	excelInfoList.add(new ExcelVO("내부평가지표", "METRIC_NM", "left"));
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("str.base.strMetric.getList", searchMap));
    	
        return searchMap;
    }
	
}

