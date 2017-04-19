/*************************************************************************
* CLASS 명      : ActivityAction
* 작 업 자      : 김윤기
* 작 업 일      : 2012년 7월 30일 
* 기    능      : Activity관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  --------  -----------------  -------------------------    --------
*   1    김윤기      2012년 7월 30일             최 초 작 업 
**************************************************************************/
package com.lexken.bsc.base;
    
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

public class ActivityAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * Activity관리 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap activityList(SearchMap searchMap) {
        return searchMap;
    }
    
    /**
     * Activity관리 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap activityList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.base.activity.getList", searchMap));

        return searchMap;
    }
    
    
    /**
     * Activity관리 상세화면(조회용)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap activityDetailView(SearchMap searchMap) {
    	
    	//년간ACTIVITY계획목록
		searchMap.addList("detailList", getList("bsc.base.activity.getDetailList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * Activity관리 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap activityDetailList(SearchMap searchMap) {
    	
    	searchMap.addList("detailList", getList("bsc.base.activity.getDetailList", searchMap));
    	
    	return searchMap;
    }
    
    
    /**
     * Activity관리 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap activityModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	
    	searchMap.addList("initiativeList", getList("bsc.base.activity.getInitList", searchMap));
    	
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.base.activity.getDetail", searchMap));
    	}
    	
    	/**********************************
         * 첨부파일
         **********************************/
    	searchMap.addList("fileList", getList("bsc.base.activity.getFileList", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * Activity관리 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap activityProcess(SearchMap searchMap) {
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
		searchMap.fileCopy("/temp", "/activity");
		
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
     * Activity관리 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        String statusId="";
        try {
        	setStartTransaction();
        
        	statusId = getStr("bsc.base.activity.getActivityStatus", searchMap);
        	searchMap.put("statusId", statusId);
        	
        	/**********************************
	         * Activity ID 채번
	         **********************************/
	        String initiativeId = getStr("bsc.base.activity.getActivityId", searchMap);
	        searchMap.put("initiativeId", initiativeId);
	        
	        /**********************************
	         * Activity 저장
	         **********************************/
        	returnMap = insertData("bsc.base.activity.insertData", searchMap);
        	
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
     * Activity관리 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("bsc.base.activity.updateData", searchMap);
	        
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
					returnMap = insertData("bsc.base.activity.deleteFileInfo", searchMap);
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
						returnMap = insertData("bsc.base.activity.insertFileInfo", searchMap);
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
     * Activity관리 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    try {
	        String[] initiativeIds = searchMap.getString("initiativeIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        for (int i = 0; i < initiativeIds.length; i++) {
	            searchMap.put("initiativeId", initiativeIds[i]);
	            returnMap = updateData("bsc.base.activity.deleteData", searchMap);
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
     * Activity관리 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap activityListExcel(SearchMap searchMap) {
    	String excelFileName = StringConstants.ACTIVITY_NM + "관리";
    	String excelTitle = StringConstants.ACTIVITY_NM + "관리 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO(StringConstants.SC_DEPT_NM + "명", StaticUtil.nullToDefault((String)searchMap.get("findScDeptNm"), "전체")));
    	excelSearchInfoList.add(new ExcelVO(StringConstants.METRIC_NM + " 유형", (String)searchMap.get("bscMetricGbnNm")));
    	
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO(StringConstants.STRATEGY_NM + "명", "STRATEGY_NM", "left"));
    	excelInfoList.add(new ExcelVO(StringConstants.METRIC_NM + "명", "METRIC_NM", "left"));
    	excelInfoList.add(new ExcelVO("목표", "TARGET_Y", "left"));
    	excelInfoList.add(new ExcelVO("가중치", "WEIGHT", "center"));
    	excelInfoList.add(new ExcelVO("주기", "EVAL_CYCLE", "center"));
    	excelInfoList.add(new ExcelVO(StringConstants.ACTIVITY_NM +" 실행계획", "PLAN_CNT", "center"));
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.base.activity.getList", searchMap));
    	
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
        
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }
    
    
}
