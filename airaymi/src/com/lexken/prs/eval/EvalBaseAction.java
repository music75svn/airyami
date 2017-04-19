/*************************************************************************
* CLASS 명      : EvalBaseAction
* 작 업 자      : 박경태
* 작 업 일      : 2012년 11월 27일 
* 기    능      : 평가요소
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    박경태      2012년 11월 27일         최 초 작 업 
**************************************************************************/
package com.lexken.prs.eval;
    
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
import com.lexken.framework.util.StaticUtil;

public class EvalBaseAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 평가요소 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalBaseList(SearchMap searchMap) {
    	
    	/**********************************
         * 평가구분 조회
         **********************************/
    	searchMap.addList("evalDegreeList", getList("prs.eval.evalGroup.getDegreeList", searchMap));
    	
    	ArrayList evalDegreeList = new ArrayList();
    	evalDegreeList = (ArrayList)getList("prs.eval.evalGroup.getDegreeList", searchMap); 
        searchMap.addList("evalDegreeList", evalDegreeList);
        
        String evalDegreeId = "";
        if(null != evalDegreeList && 0 < evalDegreeList.size()) {
	        for (int i = 0; i < evalDegreeList.size(); i++) {
	        	HashMap<String, String> t = (HashMap<String, String>)evalDegreeList.get(i);
	        	if(i==0){
	        		evalDegreeId = (String)t.get("EVAL_DEGREE_ID");
	        	}
			}
        }
        
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.get("findEvalDegreeId")))) {
			searchMap.put("findEvalDegreeId", evalDegreeId);
		}
    	
    	searchMap.addList("detail", getDetail("prs.eval.evalBase.getList", searchMap));
    	
    	/**********************************
         * 첨부파일 조회
         **********************************/
    	
        searchMap.addList("fileList", getList("prs.eval.evalBase.getFileList", searchMap));
        
        /**********************************
         * 마감,확정 조회
         **********************************/
    	searchMap.addList("closeYn", getStr("prs.eval.evalStatus.getCloseYn", searchMap));
    	searchMap.addList("confirmYn", getStr("prs.eval.evalStatus.getConfirmYn", searchMap));

        return searchMap;
    }
    
    /**
     * 평가요소 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popEvalBaseList(SearchMap searchMap) {
    	
    	
    	searchMap.put("evalDegreeNm", getStr("prs.eval.evalBase.getEvalDegreeNm", searchMap));
    	
    	searchMap.addList("detail", getDetail("prs.eval.evalBase.getList", searchMap));
    	
    	/**********************************
         * 첨부파일 조회
         **********************************/
    	
        searchMap.addList("fileList", getList("prs.eval.evalBase.getFileList", searchMap));
        
        return searchMap;
    }
    
    /**
     * 평가요소 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalBaseProcess(SearchMap searchMap) {
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
		searchMap.fileCopy("/temp", "/prs");
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("ADD".equals(stMode)) {
            searchMap = insertDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 평가요소 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        	
        	returnMap = updateData("prs.eval.evalBase.deleteData", searchMap, true);
        
        	returnMap = insertData("prs.eval.evalBase.insertData", searchMap);
        	
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
     * 평가요소 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap evalBaseListExcel(SearchMap searchMap) {
    	String excelFileName = "평가요소";
    	String excelTitle = "평가요소 리스트";
    	
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
    	excelInfoList.add(new ExcelVO("설명", "CONTENT", "left"));
    	excelInfoList.add(new ExcelVO("첨부파일 명", "ATTACH_FILE_NM", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("prs.eval.evalBase.getList", searchMap));
    	
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
	        		returnMap = insertData("prs.eval.evalBase.deleteFileInfo", searchMap, true);
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
						returnMap = insertData("prs.eval.evalBase.insertFileInfo", searchMap);
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
