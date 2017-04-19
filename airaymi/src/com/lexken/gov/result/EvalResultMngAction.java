/*************************************************************************
* CLASS 명      : EvalResultMngAction
* 작 업 자      : 정철수
* 작 업 일      : 2012년 11월 6일 
* 기    능      : 경영평가결과입력
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    정철수      2012년 11월 6일         최 초 작 업 
**************************************************************************/
package com.lexken.gov.result;
    
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
import com.lexken.framework.util.StaticUtil;

public class EvalResultMngAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 경영평가결과입력 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalResultMngList(SearchMap searchMap) {

    	searchMap.addList("fileList", getList("gov.result.evalResultMng.getAttachFileList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 경영평가결과입력 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalResultMngList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("gov.result.evalResultMng.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 경영평가결과입력 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalResultMngModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("gov.result.evalResultMng.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 경영평가결과입력 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalResultMngProcess(SearchMap searchMap) {
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
		searchMap.fileCopy("/temp", "/govEvalResult");
        
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
     * 경영평가결과입력 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	String[] govMetricIds = searchMap.getString("govMetricIds").split("\\|", 0);
        	String[] govGrades = searchMap.getString("govGrades").split("\\|", 0);
        	String[] govScores = searchMap.getString("govScores").split("\\|", 0);
        	
	        
	        if(null != govMetricIds && 0 < govMetricIds.length) {
	        	returnMap = updateData("gov.result.evalResultMng.deleteData", searchMap, true);
	        	
		        for (int i = 0; i < govMetricIds.length; i++) {
		            searchMap.put("govMetricId", govMetricIds[i].replaceAll("none", "") );
		            searchMap.put("grade", govGrades[i].trim().replaceAll("none", "") );
		            searchMap.put("score", govScores[i].trim().replaceAll("none", "") );
		            
		            returnMap = insertData("gov.result.evalResultMng.insertData", searchMap);
		        }
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
					returnMap = insertData("gov.result.evalResultMng.deleteFileInfo", searchMap);
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
						returnMap = insertData("gov.result.evalResultMng.insertFileInfo", searchMap);
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
     * 경영평가결과입력 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap evalResultMngListExcel(SearchMap searchMap) {
    	String excelFileName = "경영평가결과입력";
    	String excelTitle = "경영평가결과입력 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();

    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가범주명", "EVAL_CAT_GRP_NM", "left", "ECG_CNT"));
    	excelInfoList.add(new ExcelVO("평가부문명", "EVAL_CAT_NM", "left", "EC_CNT"));
    	excelInfoList.add(new ExcelVO("정평지표명", "GOV_METRIC_NM", "left"));
    	excelInfoList.add(new ExcelVO("지표유형", "GM_GBN_NM", "center"));
    	excelInfoList.add(new ExcelVO("평가방법", "EVAL_METHOD_NM", "center"));
    	excelInfoList.add(new ExcelVO("가중치", "GM_WEIGHT", "center"));
    	excelInfoList.add(new ExcelVO("비계량등급", "GM_GRADE", "center"));
    	excelInfoList.add(new ExcelVO("점수", "GM_SCORE", "right"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("gov.result.evalResultMng.getList", searchMap));
    	
        return searchMap;
    }
    
}
