/*************************************************************************
* CLASS 명      : ImponEvalStatusAction
* 작 업 자      : 정철수
* 작 업 일      : 2012년 12월 5일 
* 기    능      : 평가진행현황
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    정철수      2012년 12월 5일         최 초 작 업 
**************************************************************************/
package com.lexken.bsc.impon;
    
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

public class ImponEvalStatusAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 평가진행현황 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap imponEvalStatusList(SearchMap searchMap) {

    	
    	searchMap.addList("evalDegreeList", getList("bsc.impon.evalUserGrpMappingEvalItem.getEvalDegreeList", searchMap));
    	
    	String findEvalDegreeId = (String)searchMap.get("findEvalDegreeId");
    	if("".equals(StaticUtil.nullToBlank(findEvalDegreeId))) {
    		searchMap.put("findEvalDegreeId", searchMap.getDefaultValue("evalDegreeList", "EVAL_DEGREE_ID", 0));
    	}
    	
    	searchMap.addList("evalUserGrpList", getList("bsc.impon.evalUserGrpMappingEvalItem.getEvalUserGrpList", searchMap));
    	
    	
    	searchMap.addList("isEvalUsersSummitYn", getDetail("bsc.impon.imponEvalStatus.isEvalUsersSummitYn", searchMap));
    	
    	searchMap.addList("isEvalCloseYn", getStr("bsc.impon.imponEvalStatus.isEvalCloseYn", searchMap));
    	
    	
        return searchMap;
    }
    
    /**
     * 평가진행현황 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap imponEvalStatusList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.impon.imponEvalStatus.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가진행현황 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap imponEvalStatusModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.impon.imponEvalStatus.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 평가진행현황 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap imponEvalStatusProcess(SearchMap searchMap) {
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
        if("EVALCLOSE".equals(stMode)) {
            searchMap = evalColseDB(searchMap);
        } else if( "EVALCANCEL".equals(stMode) ){
        	searchMap = evalCancelDB(searchMap);
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
        
        	returnMap = insertData("bsc.impon.imponEvalStatus.insertData", searchMap);
        
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
    public SearchMap evalColseDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        searchMap.put("imponEvalClosingYn", "Y");
	        returnMap = updateData("bsc.impon.imponEvalStatus.setEvalClosingData", searchMap, true);
	        
        	returnMap = insertData("bsc.impon.imponEvalStatus.execIPEScore", searchMap);
	        
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
    
    public SearchMap evalCancelDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        searchMap.put("imponEvalClosingYn", "N");
	        returnMap = updateData("bsc.impon.imponEvalStatus.setEvalClosingData", searchMap);
	        
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
     * 평가진행현황 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap imponEvalStatusListExcel(SearchMap searchMap) {
    	String excelFileName = "평가진행현황";
    	String excelTitle = "평가진행현황 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("평가구분", (String)searchMap.get("evalDegreeNm")));
    	excelSearchInfoList.add(new ExcelVO("평가단", (String)searchMap.get("evalUserGrpNm")));
    	excelSearchInfoList.add(new ExcelVO("평가상태", StaticUtil.nullToDefault((String)searchMap.get("evalSubmitYnNm"), "전체")));
    	
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가단", "EVAL_USER_GRP_NM", "left", "UG_ROW_CNT"));
    	excelInfoList.add(new ExcelVO("평가자", "USER_NM", "left", "EU_ROW_CNT"));
    	excelInfoList.add(new ExcelVO("평가대상POOL", "METRIC_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가상태", "EVAL_SUBMIT_YN_NM", "center"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.impon.imponEvalStatus.getList", searchMap));
    	
        return searchMap;
    }
    
}
