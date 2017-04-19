/*************************************************************************
* CLASS 명      : NoticeAction
* 작 업 자      : 김윤기
* 작 업 일      : 2012년 6월 14일 
* 기    능      : 공지사항관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    김윤기      공지사항관리            최 초 작 업 
**************************************************************************/
package com.lexken.bsc.system;
    
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
import com.lexken.framework.util.StaticUtil;

public class NoticeAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 공지사항관리 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap noticeList(SearchMap searchMap) {

        return searchMap;
    }
    
    /**
     * 공지사항관리 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap noticeList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.system.notice.getList", searchMap));
    	return searchMap;
    }
    
    /**
     * 공지사항관리 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap noticeModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.system.notice.getDetail", searchMap));
    	}
    	
        //첨부파일
    	searchMap.addList("fileList", getList("bsc.system.notice.getFileList", searchMap));
        return searchMap;
    }
    
    /**
     * 공지사항관리 팝업화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap noticePopup(SearchMap searchMap) {
    	
		searchMap.addList("detail", getDetail("bsc.system.notice.getDetail", searchMap));
    	
    	//첨부파일
    	searchMap.addList("fileList", getList("bsc.system.notice.getFileList", searchMap));
    	return searchMap;
    }
    
    /**
     * 공지사항관리 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap noticeProcess(SearchMap searchMap) {
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
     * 공지사항관리 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        String id = getStr("bsc.system.notice.getId", searchMap);
	        
	        searchMap.put("id",id);
	        
	        returnMap = insertData("bsc.system.notice.insertData", searchMap);

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
     * 공지사항관리 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("bsc.system.notice.updateData", searchMap);

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
					returnMap = insertData("bsc.system.notice.deleteFileInfo", searchMap);
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
						returnMap = insertData("bsc.system.notice.insertFileInfo", searchMap);
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
     * 공지사항관리 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
        
        //PK 부분 추가
        String noticeIds = searchMap.getString("ids");
        String[] keyArray = noticeIds.split("\\|", 0);
 
        setStartTransaction();
        
        for (int i=0; i<keyArray.length; i++) {
            searchMap.put("noticeId", keyArray[i]);
            returnMap = updateData("bsc.system.notice.deleteData", searchMap);
        }
        
        setEndTransaction();
        
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
        returnMap = ValidationChk.lengthCheck(searchMap.getString("subject"), "제목", 1, 300);
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
            return returnMap;
        }
        
        returnMap = ValidationChk.lengthCheck(searchMap.getString("content"), "공지내용", 1, 3500);
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
        	return returnMap;
        }
        
        returnMap = ValidationChk.selEmptyCheck(searchMap.getString("popupGbn"), "팝업사용여부");
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
        	return returnMap;
        }
        if( "Y".equals(searchMap.getString("popupGbn")) ) {
        	returnMap = ValidationChk.lengthCheck(searchMap.getString("fromDt"), "팝업시작일자", 1, 8);
            if((Integer)returnMap.get("ErrorNumber") < 0 ){
            	return returnMap;
            }
            
            returnMap = ValidationChk.lengthCheck(searchMap.getString("toDt"), "팝업종료일자", 1, 8);
            if((Integer)returnMap.get("ErrorNumber") < 0 ){
            	return returnMap;
            }
            
            returnMap = ValidationChk.lengthCheck(searchMap.getString("width"), "팝업가로", 1, 5);
            if((Integer)returnMap.get("ErrorNumber") < 0 ){
            	return returnMap;
            }
            
            returnMap = ValidationChk.lengthCheck(searchMap.getString("height"), "팝업세로", 1, 5);
            if((Integer)returnMap.get("ErrorNumber") < 0 ){
            	return returnMap;
            }
        	
        }
        
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }
    
    
}
