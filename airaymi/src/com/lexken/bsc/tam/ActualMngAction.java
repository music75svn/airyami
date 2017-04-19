/*************************************************************************
* CLASS 명      : ActualMngAction
* 작 업 자      : 하윤식
* 작 업 일      : 2012년 7월 24일 
* 기    능      : 실적관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    하윤식      2012년 7월 24일             최 초 작 업 
**************************************************************************/
package com.lexken.bsc.tam;
    
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.FileInfoVO;
import com.lexken.framework.common.OpnionSendManager;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginVO;
import com.lexken.framework.util.HtmlHelper;
import com.lexken.framework.util.StaticUtil;

public class ActualMngAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 실적관리 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap actualMngList(SearchMap searchMap) {
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
    	String actualType = searchMap.getString("findActualType");
    	
    	String get_flag	 = searchMap.getString("flag");
    	String get_tgt_sId	 = searchMap.getString("findActStatusId");
    	
    	String findMon = searchMap.getString("findMon");
    	
    	if( get_flag == "" && get_tgt_sId == "" ) {
    		searchMap.put("flag","1");
    		searchMap.put("findActStatusId","01");
    	}else if( "1".equals(get_flag) && "ALLSTATUS".equals(get_tgt_sId) ) {
    		searchMap.put("findActStatusId","01");
    	}
    	
    	if( get_flag == "" ) {
    		searchMap.put("flag","1");
    	}
    	
    	//findMon이 공백인 경우 01을 디폴트로 입력합니다. 실적등록(입력)기간에서 평가년도 변경시 findMon이 초기화되는 예외처리
    	if("".equals(findMon)){
    		searchMap.put("findMon", "01");
    	}
    	
    	/**********************************
         * KPI 입력자 조회
         **********************************/
    	searchMap.addList("insertUserList", getList("bsc.tam.actualMng.getInsertUserList", searchMap));
    	
    	/**********************************
         * KPI 승인자 조회
         **********************************/
    	searchMap.addList("approveUserList", getList("bsc.tam.actualMng.getApproveUserList", searchMap));

    	/**********************************
         * 권한별 처리
         **********************************/
    	if(loginVO.chkAuthGrp("01") || loginVO.chkAuthGrp("60")) {
    		if("".equals(StaticUtil.nullToBlank((String)searchMap.get("findInsertUserId")))) {
    			if("".equals(StaticUtil.nullToBlank((String)searchMap.get("findScDeptId")))) {
    			//	searchMap.put("findInsertUserId", searchMap.getDefaultValue("insertUserList", "INSERT_USER_ID", 0));
    				searchMap.put("findInsertUserId","");
    			}
    		}
    		
    		if("".equals(StaticUtil.nullToBlank((String)searchMap.get("findApproveUserId")))) {
    			if("".equals(StaticUtil.nullToBlank((String)searchMap.get("findScDeptId")))) {
    				
    				if("check".equals(actualType) ){
    					//searchMap.put("findApproveUserId", searchMap.getDefaultValue("approveUserList", "APPROVE_USER_ID", 0));
    					searchMap.put("findApproveUserId","");
					}else{
						searchMap.put("findApproveUserId","");
					}
    				
    				
    			}
    		}
    	} else {
    		searchMap.put("findInsertUserId", searchMap.get("loginUserId"));
    		searchMap.put("findApproveUserId", searchMap.get("loginUserId"));
    	}
    	
    	/************************************
         * 입력대상 평가조직 및 조직 트리조회
         ************************************/
    	if("input".equals(actualType)) {
	    	searchMap.addList("scDeptTreeList", getList("bsc.tam.actualMng.getInsertScDeptTree", searchMap));
	    	searchMap.addList("metricTreeList", getList("bsc.tam.actualMng.getInsertMetricTree", searchMap));
    	} else {
    		searchMap.addList("scDeptTreeList", getList("bsc.tam.actualMng.getApproveScDeptTree", searchMap));
	    	searchMap.addList("metricTreeList", getList("bsc.tam.actualMng.getApproveMetricTree", searchMap));
    	}
    	
    	/**********************************
         * 디폴트 입력 지표 조회
         **********************************/
    	/*
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("metricId")))) {
    		searchMap.put("metricId", (String)searchMap.getString("findMetricId"));
    	}
    	*/
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("metricId")))) {
    		searchMap.put("metricId", searchMap.getDefaultValue("metricTreeList", "METRIC_ID", 0));
    	}
    	
    	/**********************************
         * 실적입력기한 여부 가져오기
         **********************************/
        searchMap.addList("actInputTermYn", getStr("bsc.tam.actualMng.getActInputTermYn", searchMap));
        
        /**********************************
         * 월마감 여부 가져오기
         **********************************/
        searchMap.addList("monCloseYn", getStr("bsc.tam.actualMng.getMonCloseYn", searchMap));
        
        /**********************************
         * 년마감 여부 가져오기
         **********************************/
        searchMap.addList("yearCloseYn", getStr("bsc.tam.actualMng.getYearCloseYn", searchMap));
        
        
        return searchMap;
    }
    
    /**
     * 실적관리 상세데이터 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap actualMngDetailList(SearchMap searchMap) {
	        
    	String metricId = searchMap.getString("metricId");
    	
    	if(!"".equals(metricId)) {
	    	/**********************************
	         * 월별실적조회
	         **********************************/
	        searchMap.addList("list", getList("bsc.tam.actualMng.getList", searchMap));
	        
	        /**********************************
	         * 목표, 실적, 달성율 조회
	         **********************************/
	        searchMap.addList("scoreList", getList("bsc.tam.actualMng.getScoreList", searchMap));
	        
	        /**********************************
	         * 실적산식 조회
	         **********************************/
	        HashMap detail = (HashMap)getDetail("bsc.tam.actualMng.getCalTypeInfo", searchMap);
	        searchMap.addList("detail", detail);
	        String actCalTypeNm = (String)detail.get("ACT_CAL_TYPE");
	        HashMap<String, String> calTyepColValueMap = new HashMap<String, String>();
	        ArrayList calTypeColList = (ArrayList)getList("bsc.tam.actualMng.calTypeColList", searchMap);
	        
	        if(null != calTypeColList && 0 < calTypeColList.size()) {
		        for (int i = 0; i < calTypeColList.size(); i++) {
		        	HashMap<String, String> t = (HashMap<String, String>)calTypeColList.get(i);
					calTyepColValueMap.put((String)t.get("CAL_TYPE_COL"), (String)t.get("CAL_TYPE_COL_NM"));
				}
	        }
	        
	        /**********************************
	         * 실적산식명 가져오기
	         **********************************/
	        String calTypeColDesc = "";
	        if(null != calTypeColList && 0 < calTypeColList.size()) {
		        calTypeColDesc = (String)HtmlHelper.changeCalNmToCalDesc(actCalTypeNm, calTyepColValueMap);
		        searchMap.addList("calTypeColDesc", calTypeColDesc);
	        }
	
	        /**********************************
	         * ACTIVITY 조회
	         **********************************/
	        searchMap.addList("activityDetail", getDetail("bsc.tam.actualMng.getActivityDetail", searchMap));
	        
	        /**********************************
	         * 미진사유 조회
	         **********************************/
	        searchMap.addList("causeDetail", getDetail("bsc.tam.actualMng.getCause", searchMap));
	        
	        /**********************************
	         * 첨부파일 조회
	         **********************************/
	        searchMap.addList("fileList", getList("bsc.tam.actualMng.getFileList", searchMap));
	        
	        /**********************************
	         * 실적입력상태 가져오기
	         **********************************/
	        searchMap.addList("actStatusId", getStr("bsc.tam.actualMng.getActStatusId", searchMap));
        
    	}
    	
        /**********************************
         * 실적입력기한 여부 가져오기
         **********************************/
        searchMap.addList("actInputTermYn", getStr("bsc.tam.actualMng.getActInputTermYn", searchMap));
        
        /**********************************
         * 월마감 여부 가져오기
         **********************************/
        searchMap.addList("monCloseYn", getStr("bsc.tam.actualMng.getMonCloseYn", searchMap));
        
        /**********************************
         * 년마감 여부 가져오기
         **********************************/
        searchMap.addList("yearCloseYn", getStr("bsc.tam.actualMng.getYearCloseYn", searchMap));
        
        /**********************************
         * 조회 월이 실적입력 월인지 확인
         **********************************/
        searchMap.addList("actRegDefMonYn", getStr("bsc.tam.actualMng.getActRegDefMonYn", searchMap));
        
        return searchMap;
    }
    
    /**
     * 실적관리 상세데이터 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popMetricAttachList(SearchMap searchMap) {
	        
    	String metricId = searchMap.getString("metricId");
    	
    	if(!"".equals(metricId)) {
    		
    		searchMap.put("metricNm", getStr("bsc.tam.actualMng.getMetricNm", searchMap));
	        
    		/**********************************
	         * 실적입력자 조회
	         **********************************/
    		String userId = getStr("bsc.tam.actualMng.getUserId", searchMap);
        	
    		searchMap.put("userId", userId);
    		
	        /**********************************
	         * 미진사유 조회
	         **********************************/
	        searchMap.addList("causeDetail", getDetail("bsc.tam.actualMng.getCause", searchMap));
	        
	        /**********************************
	         * 첨부파일 조회
	         **********************************/
	        searchMap.addList("fileList", getList("bsc.tam.actualMng.getFileList", searchMap));
	        
    	}
    	
        return searchMap;
    }
    
    /**
     * 반려입력 팝업
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popReturnActual(SearchMap searchMap) {
    	return searchMap;
    }
    
    /**
     * 반려조회 팝업
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popReturnView(SearchMap searchMap) {
    	
    	/**********************************
         * 반려 내용 가져오기
         **********************************/
        searchMap.addList("returnReason", getStr("bsc.tam.actualMng.getReturnReason", searchMap));
    	
    	return searchMap;
    }
    
    
    /**
     * 실적관리 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap actualMngProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 무결성 체크
         **********************************/
        if(!"DEL".equals(stMode)) {
            returnMap = this.validChk(searchMap);
            System.out.println("ErrorNumber:::::::::::::::::::::::::::::" + (Integer)returnMap.get("ErrorNumber"));
            if((Integer)returnMap.get("ErrorNumber") < 0 ){
                searchMap.addList("returnMap", returnMap);
                return searchMap;
            }
        }
        
        /**********************************
		 * 파일복사
		 **********************************/
		searchMap.fileCopy("/temp", "/actual");
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("ADD".equals(stMode)) {
            searchMap = insertDB(searchMap);
        } else if("DEL".equals(stMode)) {
            searchMap = deleteDB(searchMap);
        } else if("STATUS".equals(stMode)) {
            searchMap = statusDB(searchMap);
            
            String actStatusId = searchMap.getString("actStatusId");
            if(actStatusId.equals("06")||actStatusId.equals("05")||actStatusId.equals("03")) {
            	statusProcDB(searchMap);
            }
        }  
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 실적관리 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();
        
        /**********************************
         * Parameter setting
         **********************************/
        String[] colNames = searchMap.getStringArray("colNames");
        String[] colValues = searchMap.getStringArray("colValues");
        
        try {
        	setStartTransaction();
        	
        	/**********************************
             * 산식항목별 실적입력
             **********************************/
        	if(colNames != null && 0 < colNames.length) {
        		if(colValues != null && 0 < colValues.length) {
		        	for (int i = 0; i < colNames.length; i++) {
			            searchMap.put("colName", colNames[i]);
			            searchMap.put("colValue", colValues[i]);
			            returnMap = insertData("bsc.tam.actualMng.insertData", searchMap);
			        }
        		}
        	}
        	
        	/**********************************
             * ACTIVITY 등록
             **********************************/
        	returnMap = insertActivity(searchMap);
        	
        	if(!"06".equals(searchMap.getString("actStatusId"))){
	        	/**********************************
	             * 미진사유 등록
	             **********************************/
	        	returnMap = insertCause(searchMap);
	        	
	        	/**********************************
	             * 첨부파일 정보 등록
	             **********************************/
		        returnMap = insertFileInfo(searchMap);
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
     * 실적상태 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap statusDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();
        
        /**********************************
         * Parameter setting
         **********************************/
        String[] metricIds = searchMap.getString("metricIds").split("\\|", 0);
        
        try {
        	setStartTransaction();
	        
        	if(null != metricIds && 0 < metricIds.length) {
		        for (int i=0; i < metricIds.length; i++) {
		            searchMap.put("metricId", metricIds[i]);
		            returnMap = updateData("bsc.tam.actualMng.updateStatusData", searchMap, true);
		            returnMap = updateData("bsc.tam.actualMng.updateStatusActivityData", searchMap, true);
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
     * 쪽지 보내기
     * @param
     * @return String
     * @throws
     */
    public void statusProcDB(SearchMap searchMap) {
        /**********************************
         * Parameter setting
         **********************************/
        String[] metricIds = searchMap.getString("metricIds").split("\\|", 0);
        
        try {
        	setStartTransaction();
        	
        	searchMap.put("procParam1", searchMap.getString("year"));
        	searchMap.put("procParam2", "BSC_ACTUAL");
        	searchMap.put("procParam3", searchMap.getString("actStatusId"));
	        
        	if(null != metricIds && 0 < metricIds.length && !"".equals(searchMap.getString("year")) && !"".equals(searchMap.getString("actStatusId"))) {
		        for (int i=0; i < metricIds.length; i++) {
		            searchMap.put("procParam4", metricIds[i]);
		            insertData("bsc.message.message.insertNoteProc", searchMap, true);
		        }
        	}
	        
        } catch (Exception e) {
        	logger.error(e.toString());
        	setRollBackTransaction();
        } finally {
        	setEndTransaction();
        }    
    }
    
    
    /**
     * ACTIVITY 등록
     * @param      
     * @return String  
     * @throws 
     */
    public HashMap insertActivity(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        
        try {
        	/**********************************
	         * 데이터 등록여부 확인
	         **********************************/
	        int cnt = getInt("bsc.tam.actualMng.getActivityCount", searchMap);
	        
	        if(0 < cnt) {
	        	if("06".equals(searchMap.getString("actStatusId"))){
	        		returnMap = updateData("bsc.tam.actualMng.updateActivityData2", searchMap);
	        	} else{
	        		returnMap = updateData("bsc.tam.actualMng.updateActivityData1", searchMap);
	        	}
	        } else {
	            returnMap = insertData("bsc.tam.actualMng.insertActivityData", searchMap);
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
     * 미진사유 등록
     * @param      
     * @return String  
     * @throws 
     */
    public HashMap insertCause(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        
        try {
        	/**********************************
	         * 데이터 등록여부 확인
	         **********************************/
	        int cnt = getInt("bsc.tam.actualMng.getCauseCount", searchMap);
	        
	        if(0 < cnt) {
	        	returnMap = updateData("bsc.tam.actualMng.updateCauseData", searchMap);
	        } else {
	            returnMap = insertData("bsc.tam.actualMng.insertCauseData", searchMap);
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
					returnMap = insertData("bsc.tam.actualMng.deleteFileInfo", searchMap);
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
						returnMap = insertData("bsc.tam.actualMng.insertFileInfo", searchMap);
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
     * 실적삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();
        
        try {
        	setStartTransaction();
        	
        	/**********************************
             * 상태변경
             **********************************/
            returnMap = updateData("bsc.tam.actualMng.updateStatusData", searchMap, true);
            returnMap = updateData("bsc.tam.actualMng.updateStatusActivityData", searchMap, true);
            /**********************************
             * 실적삭제
             **********************************/
            returnMap = updateData("bsc.tam.actualMng.deleteData", searchMap, true);
	        
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
     * 월별지표실적상세 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popMetricAttachProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("OPNION".equals(stMode)) {
        	searchMap = updateOpnionDB(searchMap);
        	searchMap = sendOpnionDB(searchMap);
        }else if("DESC".equals(stMode)) {
        	searchMap = updateDescDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 조직성과도 의견 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateOpnionDB(SearchMap searchMap) {
    	HashMap returnMap    = new HashMap();
    	OpnionSendManager send = new OpnionSendManager();
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	searchMap.put("userId", loginVO.getUser_id());
    	
    	String content = searchMap.getString("opnion");
    	
    	try {
    		setStartTransaction();
    		
    		returnMap = insertData("bsc.mon.scDeptDiagram.updateOpnionDB", searchMap);
    		
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
     * 의견 쪽지보내기 처리 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap sendOpnionDB(SearchMap searchMap) {
    	HashMap returnMap    = new HashMap();
    	OpnionSendManager send = new OpnionSendManager();
    	
    	String content = searchMap.getString("opnion");
    	String messageTitle = "지표관련의견-성과모니터링/실적상세/미진사유 확인"+"["+searchMap.getString("year")+searchMap.getString("mon") + "] " + searchMap.getString("metricNm");
    	
    	try {
    		
	    	ArrayList UserList = new ArrayList();
	    	UserList = (ArrayList)getList("bsc.mon.scDeptDiagram.getUserList", searchMap);
	        String[] targetId = new String[0];
	        if(null != UserList && 0 < UserList.size()) {
	        	targetId = new String[UserList.size()];
	        	for (int i = 0; i < UserList.size(); i++) {
		        	HashMap<String, String> t = (HashMap<String, String>)UserList.get(i);
		        	
		        	targetId[i] = (String)t.get("SEND_USER");
		        	searchMap.put("targetId", targetId[i]);
		        	searchMap.put("message", content);
		        	searchMap.put("messageTitle", messageTitle);
		        	
		        	send.OpnionSend(searchMap);
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
     * 조직성과도 의견 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDescDB(SearchMap searchMap) {
    	HashMap returnMap    = new HashMap();
    	OpnionSendManager send = new OpnionSendManager();
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	searchMap.put("userId", loginVO.getUser_id());
    	
    	String content = searchMap.getString("causeDesc");
    	
    	try {
    		setStartTransaction();
    		
    		returnMap = insertData("bsc.mon.scDeptDiagram.updateDescDB", searchMap);
    		
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
    
    
    
}
