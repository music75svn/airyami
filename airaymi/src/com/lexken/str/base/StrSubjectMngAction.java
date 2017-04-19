/*************************************************************************
* CLASS 명      : StrSubjectMngAction
* 작 업 자      : 현걸욱
* 작 업 일      : 2012년 11월 2일 
* 기    능      : 전략과제도 관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    현걸욱      2012년 11월 2일         최 초 작 업 
**************************************************************************/
package com.lexken.str.base;
    
import java.util.ArrayList;
import java.util.HashMap;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.FileInfoVO;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.config.FileConfig;
import com.lexken.framework.struts2.IsBoxActionSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.util.FileHelper;
import com.lexken.framework.util.StaticUtil;

public class StrSubjectMngAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 전략과제도 관리 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap strSubjectMngList(SearchMap searchMap) {

    	searchMap.addList("detail", getDetail("str.base.strSubjectMng.getDetail", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 전략과제도 관리 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap strSubjectMngList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("str.base.strSubjectMng.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 전략과제도 관리 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap strSubjectMngModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("str.base.strSubjectMng.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 전략과제도 관리 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap strSubjectMngProcess(SearchMap searchMap) {
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
         * 등록/수정/삭제
         **********************************/
        if("DEL".equals(stMode)) {
            searchMap = deleteFileInfo(searchMap);
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
        

        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }

    /**
     * 전략과제도 관리 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap strSubjectMngListExcel(SearchMap searchMap) {
    	String excelFileName = "전략과제도 관리";
    	String excelTitle = "전략과제도 관리 리스트";
    	
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
    	excelInfoList.add(new ExcelVO("평가년도", "YEAR", "left"));
    	excelInfoList.add(new ExcelVO("첨부파일 명", "ATTACH_FILE_NM", "left"));
    	excelInfoList.add(new ExcelVO("첨부파일 저장명", "ATTACH_FILE_FNM", "left"));
    	excelInfoList.add(new ExcelVO("첨부파일 확장자", "ATTACH_FILE_SUFFIX", "left"));
    	excelInfoList.add(new ExcelVO("첨부파일 전체경로", "ATTACH_FILE_PATH", "left"));
    	excelInfoList.add(new ExcelVO("생성일", "CREATE_DT", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("str.base.strSubjectMng.getList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 조직성과도 배경화면 업로드 팝업창 호출
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popAttachStrSubject(SearchMap searchMap) {
    	
    	return searchMap;
    	
    }
    
    /**
     * 조직성과도 배경화면 업로드
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popAttachStrSubjectProcess(SearchMap searchMap) {
    	String findYear = (String)searchMap.getString("findYear");
		
    	/**********************************
		 * 파일복사
		 **********************************/
		searchMap.fileCopyBackground("/temp", "StrSubject", findYear, "bg");
		
		insertFileInfo(searchMap);
		
        return searchMap;
    }
    
    /**
     * 첨부파일 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteFileInfo(SearchMap searchMap) {
        
    	HashMap returnMap = new HashMap();
    	FileHelper csFileHelper = new FileHelper();
        
        try {
        	
        	String year = searchMap.getString("findYear");
            
            /**********************************
             * 파일경로 설정
             **********************************/
            FileConfig fileConfig		= FileConfig.getInstance();
    		String stRealRootPath       = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");  // D:/00.workspace/00.bsc_workspace/BSC/WebContent
    		String stBackgroundPath 	= fileConfig.getProperty("FILE_BACKGROUND_ROOT_PATH"); // /images/flash/VBOXML
    		String saveFile = stRealRootPath + stBackgroundPath;
    		
    		saveFile = saveFile + "/" + "StrSubject_" + year +"bg" + ".jpg";
    		
    		
    		csFileHelper.deleteFile(saveFile);
        	
        	setStartTransaction();
        	
        	returnMap = deleteData("str.base.strSubjectMng.deleteFileInfo", searchMap, true);
        	
        } catch (Exception e) {
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
     * 첨부파일 정보등록
     * @param      
     * @return String  
     * @throws 
     */
    public HashMap insertFileInfo(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        
        
        try {
        	
        	 setStartTransaction();
        	
        	/**********************************
             * 삭제체크된 첨부파일 삭제
             **********************************/
        	returnMap = deleteData("str.base.strSubjectMng.deleteFileInfo", searchMap, true);
        	
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
						returnMap = insertData("str.base.strSubjectMng.insertFileInfo", searchMap);
					}
				}
	        }
	        
        } catch (Exception e) {
        	logger.error(e.toString());
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        	setEndTransaction();
        }
        return returnMap;    
    }
    
}
