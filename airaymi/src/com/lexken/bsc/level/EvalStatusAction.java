/*************************************************************************
* CLASS 명      : EvalStatusAction
* 작 업 자      : 하윤식
* 작 업 일      : 2012년 10월 25일 
* 기    능      : 평가현황
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    하윤식      2012년 10월 25일         최 초 작 업 
**************************************************************************/
package com.lexken.bsc.level;
    
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

public class EvalStatusAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 평가현황 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalStatusList(SearchMap searchMap) {
    	
    	searchMap.addList("evalUserGrpList", getList("bsc.level.evalStatus.getEvalUserGrpList", searchMap));
    	
    	if("".equals(searchMap.getString("findEvalUserGrpId"))) {
        	searchMap.put("findEvalUserGrpId", searchMap.getDefaultValue("evalUserGrpList", "EVAL_USER_GRP_ID", 0));
        } 
    	
    	searchMap.addList("status", getDetail("bsc.level.evalStatus.getEvalStatusInfo", searchMap));

        return searchMap;
    }
    
    /**
     * 평가현황 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalStatusList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.level.evalStatus.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가단 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap userGroupList_ajax(SearchMap searchMap) {
    	
    	searchMap.addList("evalUserGrpList", getList("bsc.level.evalStatus.getEvalUserGrpList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가군 마감정보 가져오기
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalStatusInfo_ajax(SearchMap searchMap) {
    	
    	searchMap.addList("status", getDetail("bsc.level.evalStatus.getEvalStatusInfo", searchMap));

        return searchMap;
    }    
    
    /**
     * 평가현황 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalStatusModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.level.evalStatus.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 평가현황 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalStatusProcess(SearchMap searchMap) {
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
        if("CLOSE".equals(stMode)) {
            searchMap = closeDB(searchMap);
        } 
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 평가마감 및 취소
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap closeDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();
        String closeYn = searchMap.getString("closeYn");
        
        try {
        	setStartTransaction();
        
        	/**********************************
             * 평가마감 및 취소
             **********************************/
        	returnMap = insertData("bsc.level.evalStatus.closeData", searchMap);
        	
        	/**********************************
             * 마감시 집계 실행
             **********************************/
        	if("Y".equals(closeYn)) {
        		returnMap = insertData("bsc.level.evalStatus.execData", searchMap);
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
     * 평가현황 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap evalStatusListExcel(SearchMap searchMap) {
    	String excelFileName = "평가현황";
    	String excelTitle = "평가현황 리스트";
    	
    	/************************************************************************************
    	 * Parameter setting
    	 ************************************************************************************/
    	String evalUserGrpId = searchMap.getString("evalUserGrpId");
    	searchMap.put("findEvalUserGrpId", evalUserGrpId);
    	
    	String evalSubmitYn = searchMap.getString("evalSubmitYn");
    	searchMap.put("findEvalSubmitYn", evalSubmitYn);
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("평가단",  (String)searchMap.get("evalUserGrpNm")));
    	excelSearchInfoList.add(new ExcelVO("평가상태", (String)searchMap.get("evalSubmitYnNm")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	
    	excelInfoList.add(new ExcelVO("사용자명", "USER_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가조직", "DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("직급명", "POS_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가단", "EVAL_USER_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가상태", "EVAL_SUBMIT_YN_NM", "left"));
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.level.evalStatus.getList", searchMap));
    	
        return searchMap;
    }
    
}
