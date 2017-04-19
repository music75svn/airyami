/*************************************************************************
* CLASS 명      : ExcepReportAction
* 작 업 자      : 박경태
* 작 업 일      : 2012년 12월 13일 
* 기    능      : 별도평가근거
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    박경태      2012년 12월 13일         최 초 작 업 
**************************************************************************/
package com.lexken.cbe.excep;
    
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

public class ExcepReportAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 별도평가근거 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excepReportList(SearchMap searchMap) {
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
    	/**********************************
         * 평가구분 조회
         **********************************/
    	searchMap.addList("evalDegreeList", getList("cbe.excep.excepDept.getDegreeList", searchMap));	
    	
    	String findEvalDegreeId = (String)searchMap.get("findEvalDegreeId");
    	if("".equals(StaticUtil.nullToBlank(findEvalDegreeId))) {
    		searchMap.put("findEvalDegreeId", (String)searchMap.getDefaultValue("evalDegreeList", "EVAL_DEGREE_ID", 0));
    	}
    	
    	/**********************************
         * 평가근거담당자 조회
         **********************************/
    	searchMap.addList("evalUserList", getList("cbe.excep.excepReport.getUserList", searchMap));
    	
    	/**********************************
         * 권한별 처리
         **********************************/
    	if(!loginVO.chkAuthGrp("01") && !loginVO.chkAuthGrp("60")) {
    		searchMap.put("findEvalBaseChargeId", searchMap.get("loginUserId"));
    	}	
    	
    	searchMap.addList("getEvalPeriodYn", getStr("cbe.excep.excepReport.getEvalPeriodYn", searchMap));
    	
    	/**********************************
         * 마감조회
         **********************************/
    	searchMap.addList("closeYn", getStr("cbe.excep.excepStatus.getFindCloseYn", searchMap));

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
    	searchMap.addList("evalUserList", getList("cbe.excep.excepReport.getUserList", searchMap));

        return searchMap;
    }
    
    /**
     * 별도평가근거 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excepReportList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("cbe.excep.excepReport.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 별도평가근거 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excepReportModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	
    	searchMap.addList("detailGubun", getDetail("cbe.excep.excepReport.getDetailGubun", searchMap));
    	
    	searchMap.addList("detailUser", getDetail("cbe.excep.excepReport.getDetailUser", searchMap));
    	
    	searchMap.addList("getEvalPeriodYn", getStr("cbe.excep.excepReport.getEvalPeriodYn", searchMap));
    	
    	/**********************************
         * 마감,확정 조회
         **********************************/
    	searchMap.addList("closeYn", getStr("cbe.excep.excepStatus.getCloseYn", searchMap));
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("cbe.excep.excepReport.getDetail", searchMap));
    		
    		/**********************************
             * 첨부파일 조회
             **********************************/
        	searchMap.addList("fileList", getList("cbe.excep.excepReport.getFileList", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 별도평가근거 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excepReportProcess(SearchMap searchMap) {
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
		searchMap.fileCopy("/temp", "/cbe");
        
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
     * 별도평가근거 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("cbe.excep.excepReport.insertData", searchMap);
        
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
     * 별도평가근거 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("cbe.excep.excepReport.deleteData", searchMap, true);
	        
        	returnMap = insertData("cbe.excep.excepReport.insertData", searchMap);
        	
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
     * 별도평가근거 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] evalDegreeIds = searchMap.getString("evalDegreeIds").split("\\|", 0);
			String[] scDeptIds = searchMap.getString("scDeptIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != evalDegreeIds && 0 < evalDegreeIds.length) {
		        for (int i = 0; i < evalDegreeIds.length; i++) {
		            searchMap.put("evalDegreeId", evalDegreeIds[i]);
			searchMap.put("scDeptId", scDeptIds[i]);
		            returnMap = updateData("cbe.excep.excepReport.deleteData", searchMap);
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
     * 별도평가근거 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap excepReportListExcel(SearchMap searchMap) {
    	String excelFileName = "별도평가근거";
    	String excelTitle = "별도평가근거 리스트";
    	
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
    	excelInfoList.add(new ExcelVO("평가조직ID", "SC_DEPT_ID", "left"));
    	excelInfoList.add(new ExcelVO("평가조직명", "SC_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("EVAL_BASE_CHARGE_USER_NM", "EVAL_BASE_CHARGE_USER_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가근거", "EVAL_BASE", "left"));
    	excelInfoList.add(new ExcelVO("FCOUNT", "FCOUNT", "left"));
    	excelInfoList.add(new ExcelVO("순번", "SEQ", "left"));
    	excelInfoList.add(new ExcelVO("첨부파일 명", "ATTACH_FILE_NM", "left"));
    	excelInfoList.add(new ExcelVO("첨부파일 저장명", "ATTACH_FILE_FNM", "left"));
    	excelInfoList.add(new ExcelVO("첨부파일 확장자", "ATTACH_FILE_SUFFIX", "left"));
    	excelInfoList.add(new ExcelVO("첨부파일 전체경로", "ATTACH_FILE_PATH", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("cbe.excep.excepReport.getList", searchMap));
    	
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
	        		returnMap = insertData("cbe.excep.excepReport.deleteFileInfo", searchMap, true);
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
						returnMap = insertData("cbe.excep.excepReport.insertFileInfo", searchMap);
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
