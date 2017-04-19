/*************************************************************************
* CLASS 명      : MemJobDescAction
* 작 업 자      : 유연주
* 작 업 일      : 2017년 03월 13일 
* 기    능      : 업무성과기술서
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    유연주      2017년 03월 13일          최 초 작 업 
**************************************************************************/
package com.lexken.mem.eval;
    
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.FileInfoVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginVO;

public class MemJobDescAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 업무성과기술서 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memJobDescList(SearchMap searchMap) {
    	
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
    	searchMap.addList("periodInfo", getDetail("mem.eval.memJobDesc.getPeriodInfo", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 업무성과기술서 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memJobDescList_xml(SearchMap searchMap) {
    	
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
    	if(!loginVO.chkAuthGrp("01")) {
    		searchMap.put("findEmpNo", loginVO.getUser_id());
    	}
        
        searchMap.addList("list", getList("mem.eval.memJobDesc.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 업무성과기술서 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memJobDescModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("mem.eval.memJobDesc.getDetail", searchMap));
    	}
    	
    	searchMap.addList("periodInfo", getDetail("mem.eval.memJobDesc.getPeriodInfo", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 업무성과기술서 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memJobDescProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 무결성 체크
         **********************************/
        if(!"DEL".equals(stMode) && !"SAVE".equals(stMode) && !"MODUSER".equals(stMode) ) {
            returnMap = this.validChk(searchMap);
            
            if((Integer)returnMap.get("ErrorNumber") < 0 ){
                searchMap.addList("returnMap", returnMap);
                return searchMap;
            }
        }
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("MOD".equals(stMode)) {
            searchMap = updateDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 업무성과기술서 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        /**********************************
			 * 파일복사
			 **********************************/
			searchMap.fileCopy("/temp", "/memJobDesc");
	        
	    	/**********************************
	         * 첨부파일 등록
	         **********************************/
	    	ArrayList fileInfoList = new ArrayList();
	    	fileInfoList = (ArrayList)searchMap.get("FileInfoList");
	        FileInfoVO fileInfoVO = new FileInfoVO();
	        
	        if(null != fileInfoList) {
	        	fileInfoVO = (FileInfoVO)fileInfoList.get(0);
				if(fileInfoVO != null){
					searchMap.put("attachFileFnm", 	fileInfoVO.getMaskFileName());
					searchMap.put("attachFileNm", 	fileInfoVO.getRealFileName());
					searchMap.put("attachFileSufix",fileInfoVO.getFileExtension());
					searchMap.put("attachFilePath", fileInfoVO.getFileUploadPath());
				}
	        }
	        
	        // 업무성과기술서 수정
	        returnMap = updateData("mem.eval.memJobDesc.updateData", searchMap);
	        
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
        String stMode = searchMap.getString("mode");
        
	   	returnMap = ValidationChk.lengthCheck(searchMap.getString("content"), "주요내용", 0, 4000);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }
}
