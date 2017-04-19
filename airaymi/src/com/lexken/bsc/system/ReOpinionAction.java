/*************************************************************************
* CLASS 명      : ReOpinionAction
* 작 업 자      : 현걸욱
* 작 업 일      : 2012년 12월 31일 
* 기    능      : 이의신청
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    현걸욱      2012년 12월 31일         최 초 작 업 
**************************************************************************/
package com.lexken.bsc.system;
    
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

public class ReOpinionAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 이의신청 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap reOpinionList(SearchMap searchMap) {

        return searchMap;
    }
    
    /**
     * 이의신청 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap reOpinionList_xml(SearchMap searchMap) {
        
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
    	if(!loginVO.chkAuthGrp("01") && !loginVO.chkAuthGrp("60")){
    		searchMap.put("loginId", loginVO.getUser_id());
    	}
    	
        searchMap.addList("list", getList("bsc.system.reOpinion.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 이의신청 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap reOpinionModify(SearchMap searchMap) {
        
    	searchMap.addList("detail", getDetail("bsc.system.reOpinion.getDetail", searchMap));
    	
    	searchMap.addList("fileList", getList("bsc.system.reOpinion.getAttachFileList", searchMap));
    	
    	searchMap.addList("fileAnsList", getList("bsc.system.reOpinion.getAttachFileAnsList", searchMap));
        
        return searchMap;
    }
    
    /**
     * 이의신청 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popOpinionModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.system.reOpinion.getDetail", searchMap));
    		searchMap.addList("fileList", getList("bsc.system.reOpinion.getAttachFileList", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 이의신청 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap reOpinionProcess(SearchMap searchMap) {
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
		searchMap.fileCopy("/temp", "/reOpinion");
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("ADD".equals(stMode)) {
            searchMap = insertDB(searchMap);
        } else if("READD".equals(stMode)) {
            searchMap = insertReplyDB(searchMap);    
        } else if("MOD".equals(stMode)) {
            searchMap = updateDB(searchMap);
        } else if("DEL".equals(stMode)) {
            searchMap = deleteDB(searchMap);
        } else if("REDEL".equals(stMode)) {
            searchMap = deleteReplyDB(searchMap);    
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 이의신청 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();

        	searchMap.put("cbId",(String) getStr("bsc.system.reOpinion.getCbId", searchMap));
        	
        	returnMap = insertData("bsc.system.reOpinion.insertData", searchMap);
        	
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
     * 이의신청 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertReplyDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();   
        String replyId = StaticUtil.nullToDefault(searchMap.get("replyId"), "");
        
        try {
        	setStartTransaction();

        	if("".equals(replyId)){
        		searchMap.put("replyId",(String) getStr("bsc.system.reOpinion.getReplyId", searchMap));
        	}
        	
        	searchMap.put("status","003");
        	
        	returnMap = updateData("bsc.system.reOpinion.deleteReplyData", searchMap, true);
        	returnMap = insertData("bsc.system.reOpinion.insertReplyData", searchMap);
        	
        	returnMap = updateData("bsc.system.reOpinion.updateStatusData", searchMap, true);
        	
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
					returnMap = insertData("bsc.system.reOpinion.deleteFileInfo", searchMap);
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
						returnMap = insertData("bsc.system.reOpinion.insertFileInfo", searchMap);
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
     * 이의신청 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("bsc.system.reOpinion.updateData", searchMap);
	        
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
     * 이의신청 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        
	        setStartTransaction();
	        
	        returnMap = updateData("bsc.system.reOpinion.deleteData", searchMap);
	        returnMap = insertData("bsc.system.reOpinion.deleteAllAttachFile", searchMap);
	        
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
     * 이의신청 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteReplyDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        
	        setStartTransaction();
	        
	        searchMap.put("status","002");
	        
	        returnMap = updateData("bsc.system.reOpinion.deleteReplyData", searchMap);
	        returnMap = insertData("bsc.system.reOpinion.deleteAllAttachFile", searchMap);
	        
	        returnMap = updateData("bsc.system.reOpinion.updateStatusData", searchMap, true);
	        
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
        
        if("READD".equals(searchMap.get("mode"))) {
			returnMap = ValidationChk.lengthCheck(searchMap.getString("replyContents"), "내용", 1, 3000);
			if((Integer)returnMap.get("ErrorNumber") < 0) {
				return returnMap;
			}
		} else if("ADD".equals(searchMap.get("mode")) || "MOD".equals(searchMap.get("mode"))) {
			returnMap = ValidationChk.lengthCheck(searchMap.getString("title"), "제목", 1, 200);
			if((Integer)returnMap.get("ErrorNumber") < 0) {
				return returnMap;
			}
			
			returnMap = ValidationChk.selEmptyCheck(searchMap.getString("typeId"), "요청구분");
			if((Integer)returnMap.get("ErrorNumber") < 0) {
				return returnMap;
			}
			
			returnMap = ValidationChk.selEmptyCheck(searchMap.getString("replyStatus"), "답변상태");
			if((Integer)returnMap.get("ErrorNumber") < 0) {
				return returnMap;
			}

			returnMap = ValidationChk.lengthCheck(searchMap.getString("contents"), "내용", 0, 3000);
			if((Integer)returnMap.get("ErrorNumber") < 0) {
				return returnMap;
			}
		}
		
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }

    /**
     * 이의신청 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap reOpinionListExcel(SearchMap searchMap) {
    	String excelFileName = "이의신청";
    	String excelTitle = "이의신청 리스트";
    	
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
    	excelInfoList.add(new ExcelVO("코드그룹", "CODE_GRP_ID", "left"));
    	excelInfoList.add(new ExcelVO("코드그룹명", "CODE_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("코드부여구분", "CODE_DEF_ID", "left"));
    	excelInfoList.add(new ExcelVO("YEAR_YN", "YEAR_YN", "left"));
    	excelInfoList.add(new ExcelVO("비고", "CONTENT", "left"));
    	excelInfoList.add(new ExcelVO("생성일자", "CREATE_DT", "left"));
    	excelInfoList.add(new ExcelVO("삭제일자", "DELETE_DT", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.system.reOpinion.getList", searchMap));
    	
        return searchMap;
    }
    
}
