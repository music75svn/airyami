/*************************************************************************
* CLASS 명      : AhpGrpMappingAction
* 작 업 자      : 하윤식
* 작 업 일      : 2012년 11월 29일 
* 기    능      : ahp 평가단 매핑
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    하윤식      2012년 11월 29일         최 초 작 업 
**************************************************************************/
package com.lexken.bsc.ahp;
    
import java.util.ArrayList;
import java.util.HashMap;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
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

public class AhpGrpMappingAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * ahp 평가단 매핑 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap ahpGrpMappingList(SearchMap searchMap) {

        return searchMap;
    }
    
    /**
     * ahp 평가단 매핑 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap ahpGrpMappingList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.ahp.ahpGrpMapping.getList", searchMap));

        return searchMap;
    }
    
    /**
     * ahp 평가단 매핑 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap ahpGrpMappingModify(SearchMap searchMap) {
    	
    	/**********************************
    	 * 평가단명 조회
    	 **********************************/
    	searchMap.addList("evalUserGrpNm", getStr("bsc.ahp.ahpGrpMapping.getEvalUserGrpNm", searchMap));
    	
    	searchMap.addList("treeList", getList("bsc.module.commModule.getDeptList", searchMap));
		searchMap.addList("userList", getList("bsc.ahp.ahpGrpMapping.getUserList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * ahp 평가단 매핑 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap ahpGrpMappingProcess(SearchMap searchMap) {
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
     * ahp 평가단 매핑 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("bsc.ahp.ahpGrpMapping.insertData", searchMap);
        
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
     * ahp 평가단 매핑 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	String evalUserGrpId = searchMap.getString("evalUserGrpId");
			String[] evalUserIds = searchMap.getString("evalUserIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        searchMap.put("evalUserGrpId", evalUserGrpId);
	        
	        /**********************************
	         * 기존평가단 삭제
	         **********************************/
	        returnMap = updateData("bsc.ahp.ahpGrpMapping.deleteData", searchMap, true);
	        
	        /**********************************
	         * 평가단 등록
	         **********************************/
	        if(null != evalUserIds && 0 < evalUserIds.length) {
		        for (int i = 0; i < evalUserIds.length; i++) {
		        	if(!"".equals(StaticUtil.nullToBlank(evalUserIds[i]))){
			        	searchMap.put("evalUserGrpId", evalUserGrpId);
			            searchMap.put("evalUserId", evalUserIds[i]);
			            returnMap = insertData("bsc.ahp.ahpGrpMapping.insertData", searchMap);
		        	}
		        }
		    }
	        
	        /**********************************
	         * 기존권한 삭제
	         **********************************/
	        returnMap = updateData("bsc.ahp.ahpGrpMapping.deleteAuthData", searchMap, true);
	        
	        /**********************************
	         * 평가자 권한 등록
	         **********************************/
	        returnMap = updateData("bsc.ahp.ahpGrpMapping.insertAuthData", searchMap, true);
	        
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
     * ahp 평가단 매핑 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        setStartTransaction();
	        
	        
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
     * ahp 평가단 매핑 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap ahpGrpMappingListExcel(SearchMap searchMap) {
    	String excelFileName = "ahp 평가단 매핑";
    	String excelTitle = "ahp 평가단 매핑 리스트";
    	
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
    	excelInfoList.add(new ExcelVO("설명", "CONTENT", "left"));
    	excelInfoList.add(new ExcelVO("생성일자", "CREATE_DT", "left"));
    	excelInfoList.add(new ExcelVO("삭제일자", "DELETE_DT", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.ahp.ahpGrpMapping.getList", searchMap));
    	
        return searchMap;
    }
    
}
