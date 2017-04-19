/*************************************************************************
* CLASS 명      : MemFormUploadAction
* 작 업 자      : 유연주
* 작 업 일      : 2017년 03월 21일 
* 기    능      : 양식업로드
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    유연주      2017년 03월 21일          최 초 작 업 
**************************************************************************/
package com.lexken.mem.base;
    
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.FileInfoVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.core.CommonService;

public class MemFormUploadAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 양식업로드 화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memFormUpload(SearchMap searchMap) {
    	
    	searchMap.addList("fileList", getList("mem.base.memFormUpload.getAttachFileList", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 양식업로드
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memFormUploadProcess(SearchMap searchMap) {
    	HashMap returnMap = new HashMap();
    	
        /**********************************
		 * 파일복사
		 **********************************/
		searchMap.fileCopy("/temp", "/memform");
		
		logger.debug("searchMap : "+searchMap);
		
    	/**********************************
         * 첨부파일 등록
         **********************************/
    	ArrayList fileInfoList = new ArrayList();
    	fileInfoList = (ArrayList)searchMap.get("FileInfoList");
        FileInfoVO fileInfoVO = new FileInfoVO();
        
        int fileIdx = 0;
        if(null != fileInfoList) {
	        if("Y".equals(searchMap.getString("file03Exist"))){
	        	fileInfoVO = (FileInfoVO)fileInfoList.get(fileIdx);
				if(fileInfoVO != null){
					searchMap.put("attachFileFnm", 	fileInfoVO.getMaskFileName());
					searchMap.put("attachFileNm", 	fileInfoVO.getRealFileName());
					searchMap.put("attachFileSufix",fileInfoVO.getFileExtension());
					searchMap.put("attachFilePath", fileInfoVO.getFileUploadPath());
					searchMap.put("formCd", "03");
					logger.debug("searchMap : "+searchMap);
					returnMap = insertData("mem.base.memFormUpload.insertData", searchMap);
					
					fileIdx++;
				}
	        }
	        if("Y".equals(searchMap.getString("file02Exist"))){
	        	fileInfoVO = (FileInfoVO)fileInfoList.get(fileIdx);
				if(fileInfoVO != null){
					searchMap.put("attachFileFnm", 	fileInfoVO.getMaskFileName());
					searchMap.put("attachFileNm", 	fileInfoVO.getRealFileName());
					searchMap.put("attachFileSufix",fileInfoVO.getFileExtension());
					searchMap.put("attachFilePath", fileInfoVO.getFileUploadPath());
					searchMap.put("formCd", "02");
					logger.debug("searchMap : "+searchMap);
					returnMap = insertData("mem.base.memFormUpload.insertData", searchMap);
					
					fileIdx++;
				}
	        }
	        if("Y".equals(searchMap.getString("file01Exist"))){
	        	fileInfoVO = (FileInfoVO)fileInfoList.get(fileIdx);
				if(fileInfoVO != null){
					searchMap.put("attachFileFnm", 	fileInfoVO.getMaskFileName());
					searchMap.put("attachFileNm", 	fileInfoVO.getRealFileName());
					searchMap.put("attachFileSufix",fileInfoVO.getFileExtension());
					searchMap.put("attachFilePath", fileInfoVO.getFileUploadPath());
					searchMap.put("formCd", "01");
					logger.debug("searchMap : "+searchMap);
					returnMap = insertData("mem.base.memFormUpload.insertData", searchMap);
					
					fileIdx++;
				}
	        }
        }
        
    	return searchMap;
    }
}
