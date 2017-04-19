/*************************************************************************
* CLASS 명      : GovQuaMetricAction
* 작 업 자      : 박종호
* 작 업 일      : 2013년 9월 5일 
* 기    능      : 비계량지표
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    박종호      비계량지표            최 초 작 업 
**************************************************************************/
package com.lexken.gov.unmeasure;
    
import java.util.ArrayList;
import java.util.HashMap;

import com.lexken.framework.common.ErrorMessages;
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

public class GovQuaMetricAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 비계량지표 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap govQuaMetricList(SearchMap searchMap) {
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
    	//비계량지표
    	searchMap.addList("govMetricNmList", getList("gov.unmeasure.govQuaMetric.getGovMetricNm", searchMap));
    	//지표담당자
//    	searchMap.addList("insertUserlist", getList("gov.unmeasure.govQuaMetric.getInsertUserNm", searchMap));
    	//권한조회
    	searchMap.put("insertUserId",loginVO.getUser_id());
    	int InsertUserCnt = getInt("gov.unmeasure.govQuaMetric.getInsertUserYn", searchMap);
    	
    	if(0 < InsertUserCnt){
    		searchMap.put("insertUserYn","Y");
    	}	
    	
        return searchMap;
    }
    
    /**
     * 비계량지표 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap govQuaMetricList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("gov.unmeasure.govQuaMetric.getList", searchMap));
    	return searchMap;
    }
    
    /**
     * 비계량지표 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap govQuaMetricModify(SearchMap searchMap) {
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
    	String stMode = searchMap.getString("mode");
    	
    	//권한조회
    	if(!loginVO.chkAuthGrp("01") && !loginVO.chkAuthGrp("60")) {
    		searchMap.put("insertUserId",loginVO.getUser_id());	
    	}
    	
    	//비계량지표
    	searchMap.addList("govMetricNmList", getList("gov.unmeasure.govQuaMetric.getGovMetricNm", searchMap));    	
    	
    	if("MOD".equals(stMode)) {
    		// 상세내용
    		searchMap.addList("detail", getDetail("gov.unmeasure.govQuaMetric.getDetail", searchMap));
    		
            //첨부파일
        	searchMap.addList("fileList", getList("gov.unmeasure.govQuaMetric.getFileList", searchMap));            
    	}
    	
    	return searchMap;
    }
    
    /**
     * 비계량지표 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap govQuaMetricProcess(SearchMap searchMap) {
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
     * 비계량지표 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        //SEQ 취득
	        String seq = getStr("gov.unmeasure.govQuaMetric.getSeq", searchMap);
	        
	        searchMap.put("seq",seq);
	        
	        returnMap = insertData("gov.unmeasure.govQuaMetric.insertData", searchMap);

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
     * 비계량지표 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("gov.unmeasure.govQuaMetric.updateData", searchMap);

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
	        		searchMap.put("fileSeq", delAttachFiles[i]);
					returnMap = insertData("gov.unmeasure.govQuaMetric.deleteFileInfo", searchMap);
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
						returnMap = insertData("gov.unmeasure.govQuaMetric.insertFileInfo", searchMap);
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
     * 비계량지표 삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("gov.unmeasure.govQuaMetric.deleteData", searchMap);
	        
	        returnMap = insertData("gov.unmeasure.govQuaMetric.deleteFile", searchMap, true);
	        

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
        
        //Validation 체크 추가
        returnMap = ValidationChk.lengthCheck(searchMap.getString("quaMetricNm"), "제목", 1, 200);
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
            return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("content"), "내용", 1, 3000);
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
        	return returnMap;
        }
        
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }
    
    
}
