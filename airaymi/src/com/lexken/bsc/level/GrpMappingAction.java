/*************************************************************************
* CLASS 명      : GrpMappingAction
* 작 업 자      : 현걸욱
* 작 업 일      : 2012년 10월 24일 
* 기    능      : 평가단-평가자 매핑
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    현걸욱      2012년 10월 24일         최 초 작 업 
**************************************************************************/
package com.lexken.bsc.level;
    
import java.util.ArrayList;
import java.util.HashMap;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.struts2.IsBoxActionSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.util.StaticUtil;

public class GrpMappingAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 평가단-평가자 매핑 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap grpMappingList(SearchMap searchMap) {

        return searchMap;
    }
    
    /**
     * 평가단-평가자 매핑 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap grpMappingList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.level.grpMapping.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가단-평가자 매핑 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap grpMappingModify(SearchMap searchMap) {
    	//String stMode = searchMap.getString("mode");
        
    	searchMap.addList("treeList", getList("bsc.module.commModule.getDeptList", searchMap));
		searchMap.addList("userList", getList("bsc.level.grpMapping.getUserList", searchMap));
    	
		/*
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.level.grpMapping.getDetail", searchMap));
    	}
        
        */
        return searchMap;
    }
    
    /**
     * 평가단-평가자 매핑 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap grpMappingProcess(SearchMap searchMap) {
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
     * 평가단-평가자 매핑 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("bsc.level.grpMapping.insertData", searchMap);
        
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
     * 평가단-평가자 매핑 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
    	
    	HashMap returnMap = new HashMap(); 
	    
	    try {
	        String evalUserGrpId = searchMap.getString("evalUserGrpId");
			String[] evalUserIds = searchMap.getString("evalUserIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        searchMap.put("evalUserGrpId", evalUserGrpId);
	        
	        /**********************************
	         * 기존평가단 삭제
	         **********************************/
	        returnMap = updateData("bsc.level.grpMapping.deleteData", searchMap, true);
	        
	        /**********************************
	         * 평가단 등록
	         **********************************/
	        if(null != evalUserIds && 0 < evalUserIds.length) {
		        for (int i = 0; i < evalUserIds.length; i++) {
		        	if(!"".equals(StaticUtil.nullToBlank(evalUserIds[i]))){
			        	searchMap.put("evalUserGrpId", evalUserGrpId);
			            searchMap.put("evalUserId", evalUserIds[i]);
			            returnMap = insertData("bsc.level.grpMapping.insertData", searchMap);
		        	}
		        }
		    }
	        
	        /**********************************
	         * 기존권한 삭제
	         **********************************/
	        returnMap = updateData("bsc.level.grpMapping.deleteAuthData", searchMap, true);
	        
	        /**********************************
	         * 평가자 권한 등록
	         **********************************/
	        returnMap = updateData("bsc.level.grpMapping.insertAuthData", searchMap, true);
	        
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
     * 평가단-평가자 매핑 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] evalUserGrpIds = searchMap.getString("evalUserGrpIds").split("\\|", 0);
			String[] evalUserIds = searchMap.getString("evalUserIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != evalUserGrpIds && 0 < evalUserGrpIds.length) {
		        for (int i = 0; i < evalUserGrpIds.length; i++) {
		            searchMap.put("evalUserGrpId", evalUserGrpIds[i]);
		            searchMap.put("evalUserId", evalUserIds[i]);
		            returnMap = updateData("bsc.level.grpMapping.deleteData", searchMap);
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

    /**
     * 평가단-평가자 매핑 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap grpMappingListExcel(SearchMap searchMap) {
    	String excelFileName = "평가단-평가자 매핑";
    	String excelTitle = "평가단-평가자 매핑 리스트";
    	
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
    	excelInfoList.add(new ExcelVO("평가단 코드", "EVAL_USER_GRP_ID", "left"));
    	excelInfoList.add(new ExcelVO("평가자 사번", "EVAL_USER_ID", "left"));
    	excelInfoList.add(new ExcelVO("생성일자", "CREATE_DT", "left"));
    	excelInfoList.add(new ExcelVO("수정일자", "MODIFY_DT", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.level.grpMapping.getList", searchMap));
    	
        return searchMap;
    }
    
}
