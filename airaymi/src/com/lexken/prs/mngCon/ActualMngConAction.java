/*************************************************************************
* CLASS 명      : ActualMngConAction
* 작 업 자      : 김효은
* 작 업 일      : 2014년 4월 4일 
* 기    능      : 직원개인업무성과 실적등록
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    김효은      2014년 4월 4일         최 초 작 업 
**************************************************************************/
package com.lexken.prs.mngCon;
    
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

public class ActualMngConAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 직원개인업무성과 실적등록 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalMngList(SearchMap searchMap) {
    	
    	return searchMap;
    }
    
    /**
     * 직원개인업무성과 실적등록 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalMngList_xml(SearchMap searchMap) {
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	if(loginVO.chkAuthGrp("01")) searchMap.put("isAdmin", "Y");
    	
    	searchMap.addList("list", getList("prs.mngCon.actualMngCon.getActualList", searchMap));
    	
    	return searchMap;
    }
    
    
    /**
     * 직원개인업무성과 실적등록 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap actualMngConList(SearchMap searchMap) {
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("mon")))) {
    		searchMap.put("mon","12");
    	}
    	searchMap.addList("membersTable", getDetail("prs.mngCon.planMngCon.getFindList", searchMap));//목록리스트
    	
    	/**********************************
         * 첨부파일
         **********************************/
    	searchMap.addList("fileList", getList("prs.mngCon.actualMngCon.getFileList", searchMap));
    	

        return searchMap;
    }
    
    /**
     * 직원개인업무성과 실적등록 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap actualMngConList_xml(SearchMap searchMap) {
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	if(loginVO.chkAuthGrp("01")) searchMap.put("isAdmin", "Y");
        
        searchMap.addList("list", getList("prs.mngCon.actualMngCon.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 직원개인업무성과 실적등록 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap actualMngConProcess(SearchMap searchMap) {
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
		searchMap.fileCopy("/temp", "/actualMngCon");
        
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
		if("SAVE".equals(stMode)) {
            searchMap = saveDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    /**
     * 직원개인업무성과 실적등록 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalMngProcess(SearchMap searchMap) {
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
    	if("APC".equals(stMode)) {
    		searchMap = approveDB(searchMap);
    	}
    	
    	/**********************************
    	 * Return
    	 **********************************/
    	return searchMap;
    }
    
    /**
     * 직원개인업무성과 실적등록 확인요청
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap saveDB(SearchMap searchMap) {
    	HashMap returnMap    = new HashMap();    
    	
    	try {
    		setStartTransaction();
    		
    		String[] targetIds = searchMap.getStringArray("targetIds");
    		String[] values = searchMap.getStringArray("values");
    		String[] scores = searchMap.getStringArray("scores");
    		String[] actualFValues = searchMap.getStringArray("actualFValues");
    		String[] actualSValues = searchMap.getStringArray("actualSValues");
    		String[] actualTValues = searchMap.getStringArray("actualTValues");
    		String[] actualFFValues = searchMap.getStringArray("actualFFValues");
    		
    		for(int i = 0; i < targetIds.length; i++){
    			searchMap.put("targetId", targetIds[i]);
    			searchMap.put("value", values[i]);
    			searchMap.put("score", scores[i]);
    			searchMap.put("actualFValue", actualFValues[i]);
    			searchMap.put("actualSValue", actualSValues[i]);
    			searchMap.put("actualTValue", actualTValues[i]);
    			searchMap.put("actualFFValue", actualFFValues[i]);
    			searchMap.put("mon","12");
    			
    			returnMap = updateData("prs.mngCon.actualMngCon.updateData", searchMap);
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
     * 직원개인업무성과 실적등록 확인요청
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap approveDB(SearchMap searchMap) {
    	HashMap returnMap    = new HashMap();    
    	
    	try {
    		setStartTransaction();
    		String[] targetIds = searchMap.getStringArray("targetIds");
    		String[] values = searchMap.getStringArray("values");
    		String[] scores = searchMap.getStringArray("scores");
    		String[] actualFValues = searchMap.getStringArray("actualFValues");
    		String[] actualSValues = searchMap.getStringArray("actualSValues");
    		String[] actualTValues = searchMap.getStringArray("actualTValues");
    		String[] actualFFValues = searchMap.getStringArray("actualFFValues");
    		searchMap.put("mon","12");
    		
    		for(int i = 0; i < targetIds.length; i++){
    			searchMap.put("targetId", targetIds[i]);
    			searchMap.put("value", values[i]);
    			searchMap.put("score", scores[i]);
    			searchMap.put("actualFValue", actualFValues[i]);
    			searchMap.put("actualSValue", actualSValues[i]);
    			searchMap.put("actualTValue", actualTValues[i]);
    			searchMap.put("actualFFValue", actualFFValues[i]);
    			
    			returnMap = updateData("prs.mngCon.actualMngCon.updateData", searchMap);
    		}	
    		
    		returnMap = updateData("prs.mngCon.actualMngCon.updateStatusData", searchMap);
    		
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
	        		returnMap = insertData("prs.mngCon.actualMngCon.deleteFileInfo", searchMap);
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
						returnMap = insertData("prs.mngCon.actualMngCon.insertFileInfo", searchMap);
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
    
}
