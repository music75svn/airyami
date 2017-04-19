/*************************************************************************
* CLASS 명      : ImponReportAction
* 작 업 자      : 박경태
* 작 업 일      : 2012년 12월 3일 
* 기    능      : 실적보고서
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    박경태      2012년 12월 3일         최 초 작 업 
**************************************************************************/
package com.lexken.bsc.impon;
    
import java.util.ArrayList;
import java.util.HashMap;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.FileInfoVO;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.StringConstants;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.struts2.IsBoxActionSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.login.LoginVO;
import com.lexken.framework.util.StaticUtil;

public class ImponReportAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 실적보고서 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap imponReportList(SearchMap searchMap) {
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
    	/**********************************
         * 평가구분 조회
         **********************************/
    	searchMap.addList("evalDegreeList", getList("bsc.impon.imponItem.getDegreeList", searchMap));
    	
    	String findEvalDegreeId = (String)searchMap.get("findEvalDegreeId");
    	if("".equals(StaticUtil.nullToBlank(findEvalDegreeId))) {
    		searchMap.put("findEvalDegreeId", (String)searchMap.getDefaultValue("evalDegreeList", "EVAL_DEGREE_ID", 0));
    	}
    	
    	/**********************************
         * 실적담당자 조회
         **********************************/
    	searchMap.addList("evalUserList", getList("bsc.impon.imponReport.getUserList", searchMap));
    	
    	/**********************************
         * 권한별 처리
         **********************************/
    	if(!loginVO.chkAuthGrp("01") && !loginVO.chkAuthGrp("60")) {
    		searchMap.put("findInsertUserId", searchMap.get("loginUserId"));
    	}
    	
    	searchMap.addList("getEvalPeriodYn", getStr("bsc.impon.imponReport.getEvalPeriodYn", searchMap));

        return searchMap;
    }
    
    /**
     * 년도별 비계량지표 실적담당자
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap getUserList_ajax(SearchMap searchMap) {
    	
    	/**********************************
         * 실적담당자 조회
         **********************************/
    	searchMap.addList("evalUserList", getList("bsc.impon.imponReport.getUserList", searchMap));

        return searchMap;
    }
    
    /**
     * 실적보고서 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap imponReportList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.impon.imponReport.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 실적보고서 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap imponReportModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	searchMap.addList("detailGubun", getDetail("bsc.impon.imponReport.getDetailGubun", searchMap));
    	
    	searchMap.addList("detailUser", getDetail("bsc.impon.imponReport.getDetailUser", searchMap));
    	
    	searchMap.addList("detailMetricGrp", getDetail("bsc.impon.imponReport.getDetailMetricGrp", searchMap));
    	
    	searchMap.addList("detailMetric", getDetail("bsc.impon.imponReport.getDetailMetric", searchMap));
    	
    	searchMap.addList("getEvalPeriodYn", getStr("bsc.impon.imponReport.getEvalPeriodYn", searchMap));
    	
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.impon.imponReport.getDetail", searchMap));
    		
    		/**********************************
             * 첨부파일 조회
             **********************************/
        	searchMap.addList("fileList", getList("bsc.impon.imponReport.getFileList", searchMap));
    	}
        
        return searchMap;
    }
    
    
    /**
     * 실적보고서 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popActRptList(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	
    	searchMap.addList("detailMetricGrp", getDetail("bsc.impon.imponReport.getDetailMetricGrp", searchMap));
    	
    	searchMap.addList("detailMetric", getDetail("bsc.impon.imponReport.getDetailMetric", searchMap));
    	
		searchMap.addList("detail", getDetail("bsc.impon.imponReport.getDetail", searchMap));
		
		/**********************************
         * 첨부파일 조회
         **********************************/
    	searchMap.addList("fileList", getList("bsc.impon.imponReport.getFileList", searchMap));
        
        return searchMap;
    }
    
    
    
    /**
     * 실적보고서 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap imponReportProcess(SearchMap searchMap) {
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
		searchMap.fileCopy("/temp", "/impon");
        
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
     * 실적보고서 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("bsc.impon.imponReport.insertData", searchMap);
        
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
     * 실적보고서 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        searchMap.put("insertUserId", searchMap.get("loginUserId"));
	        
	        if(((String)searchMap.get("itemId")).substring(0,1).equals("M")){
	        	searchMap.put("itemGbn", "01");
	        }else{
	        	searchMap.put("itemGbn", "02");
	        }
	        
	        returnMap = updateData("bsc.impon.imponReport.deleteData", searchMap, true);
	        
        	returnMap = insertData("bsc.impon.imponReport.insertData", searchMap);
        	
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
     * 실적보고서 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] evalDegreeIds = searchMap.getString("evalDegreeIds").split("\\|", 0);
			String[] metricGrpIds = searchMap.getString("metricGrpIds").split("\\|", 0);
			String[] itemIds = searchMap.getString("itemIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != evalDegreeIds && 0 < evalDegreeIds.length) {
		        for (int i = 0; i < evalDegreeIds.length; i++) {
		            searchMap.put("evalDegreeId", evalDegreeIds[i]);
			searchMap.put("metricGrpId", metricGrpIds[i]);
			searchMap.put("itemId", itemIds[i]);
		            returnMap = updateData("bsc.impon.imponReport.deleteData", searchMap);
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
        

        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }

    /**
     * 실적보고서 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap imponReportListExcel(SearchMap searchMap) {
    	String excelFileName = "실적보고서";
    	String excelTitle = "실적보고서 리스트";
    	
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
    	excelInfoList.add(new ExcelVO("실적담당자", "USER_NM", "center"));
    	excelInfoList.add(new ExcelVO("지표POOL명", "METRIC_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가조직", "ITEM_NM", "center"));
    	excelInfoList.add(new ExcelVO("비고", "ACTUAL_BASE", "left"));
    	excelInfoList.add(new ExcelVO("첨부파일", "ATTACH_FILE_NM", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.impon.imponReport.getList", searchMap));
    	
        return searchMap;
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
	        		returnMap = insertData("bsc.impon.imponReport.deleteFileInfo", searchMap, true);
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
						returnMap = insertData("bsc.impon.imponReport.insertFileInfo", searchMap);
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
    
}
