/*************************************************************************
* CLASS 명      : MatrixStatusAction
* 작 업 자      : 박경태
* 작 업 일      : 2012년 10월 25일 
* 기    능      : 평가진행현황
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    박경태      2012년 10월 25일         최 초 작 업 
**************************************************************************/
package com.lexken.bsc.matrix;
    
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

public class MatrixStatusAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 평가진행현황 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap matrixStatusList(SearchMap searchMap) {
    	
    	/**********************************
         * 평가단 조회
         **********************************/
    	searchMap.addList("evalGrpList", getList("bsc.matrix.matrixGrp.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가진행현황 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap matrixStatusList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.matrix.matrixStatus.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가진행현황 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap matrixStatusModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.matrix.matrixStatus.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 평가진행현황 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap matrixStatusProcess(SearchMap searchMap) {
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
        } else if("CANCEL".equals(stMode)) {
            searchMap = cancelDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 평가진행현황 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("bsc.matrix.matrixStatus.insertData", searchMap);
        
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
     * 평가군 마감 취소
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap cancelDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        //평가마감 실행
	        returnMap = updateData("bsc.matrix.matrixStatus.updateData", searchMap);
	        
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
     * 평가진행현황 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        //평가마감 실행
	        returnMap = insertData("bsc.matrix.matrixStatus.execData", searchMap);
	        
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
     * 평가군 마감정보 가져오기
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalUserInfo_ajax(SearchMap searchMap) {
    	
    	searchMap.addList("status", getDetail("bsc.matrix.matrixStatus.getEvalUserInfo", searchMap));

        return searchMap;
    }
    
    /**
     * 평가진행현황 삭제 
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
		            returnMap = updateData("bsc.matrix.matrixStatus.deleteData", searchMap);
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
     * 평가진행현황 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap matrixStatusListExcel(SearchMap searchMap) {
    	String excelFileName = "평가진행현황";
    	String excelTitle = "평가진행현황 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("평가단", (String)searchMap.get("evalUserGrpNm")));
    	excelSearchInfoList.add(new ExcelVO("평가상태", (String)searchMap.get("evalSubmitNm")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("사용자명", "USER_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가조직", "DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("직급명", "JIKGUB_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가단", "EVAL_USER_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가상태", "SUBMIT_YN", "left"));
    	
    	if(!"".equals(StaticUtil.nullToBlank((String)searchMap.get("evalUserGrpId")))) {
			searchMap.put("findEvalUserGrpId", (String)searchMap.get("evalUserGrpId"));
		}
    	
    	if(!"".equals(StaticUtil.nullToBlank((String)searchMap.get("evalSubmitYn")))) {
			searchMap.put("findEvalSubmitYn", (String)searchMap.get("evalSubmitYn"));
		}
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.matrix.matrixStatus.getList", searchMap));
    	
        return searchMap;
    }
    
}
