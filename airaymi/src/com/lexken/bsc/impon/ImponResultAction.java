/*************************************************************************
* CLASS 명      : ImponResultAction
* 작 업 자      : 박경태
* 작 업 일      : 2012년 12월 5일 
* 기    능      : 평가결과
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    박경태      2012년 12월 5일         최 초 작 업 
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
import com.lexken.framework.login.LoginVO;
import com.lexken.framework.util.StaticUtil;

public class ImponResultAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 평가결과 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap imponResultList(SearchMap searchMap) {
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
    	/**********************************
         * 권한별 처리
         **********************************/
    	if(!loginVO.chkAuthGrp("01") && !loginVO.chkAuthGrp("60")) {
    		searchMap.put("findInsertUserId", searchMap.get("loginUserId"));
    		
    		searchMap.addList("evalDeptList", getList("bsc.impon.imponResult.getDeptList", searchMap));
    		
    		searchMap.put("findScDeptId", (String)searchMap.getDefaultValue("evalDeptList", "SC_DEPT_ID", 0));
    	}
    	
    	/**********************************
         * 평가구분 조회
         **********************************/
    	searchMap.addList("evalDegreeList", getList("bsc.impon.imponItem.getDegreeList", searchMap));
    	
    	
    	
    	return searchMap;
    }
    
    /**
     * 평가결과 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap imponResultList_xml(SearchMap searchMap) {
    	
        searchMap.addList("list", getList("bsc.impon.imponResult.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 년도별 평가군 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap getCodeList_ajax(SearchMap searchMap) {
    	
    	/**********************************
         * 평가구분 조회
         **********************************/
    	searchMap.addList("evalCodeList", getList("bsc.impon.imponResult.getCodeList", searchMap));

        return searchMap;
    }
    
    /**
     * 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popFeedDetail(SearchMap searchMap) {
    	
    	ArrayList evalFeedList = new ArrayList();
    	evalFeedList = (ArrayList)getList("bsc.impon.imponResult.getFeedList", searchMap); 
        searchMap.addList("evalFeedList", evalFeedList);
        
        return searchMap;
    }
    
    /**
     * 평가결과 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap imponResultModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.impon.imponResult.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 평가결과 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap imponResultProcess(SearchMap searchMap) {
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
     * 평가결과 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("bsc.impon.imponResult.insertData", searchMap);
        
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
     * 평가결과 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("bsc.impon.imponResult.updateData", searchMap);
	        
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
     * 평가결과 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] evalDegreeIds = searchMap.getString("evalDegreeIds").split("\\|", 0);
			String[] metricGrpIds = searchMap.getString("metricGrpIds").split("\\|", 0);
			String[] metricIds = searchMap.getString("metricIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != evalDegreeIds && 0 < evalDegreeIds.length) {
		        for (int i = 0; i < evalDegreeIds.length; i++) {
		            searchMap.put("evalDegreeId", evalDegreeIds[i]);
			searchMap.put("metricGrpId", metricGrpIds[i]);
			searchMap.put("metricId", metricIds[i]);
		            returnMap = updateData("bsc.impon.imponResult.deleteData", searchMap);
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
        
		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("evalMetricScore"), "평가지표점수");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("adjustScore"), "가감점");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("finalMetricScore"), "최종지표점수");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}


        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }

    /**
     * 평가결과 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap imponResultListExcel(SearchMap searchMap) {
    	String excelFileName = "평가결과";
    	String excelTitle = "평가결과 리스트";
    	
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
    	excelInfoList.add(new ExcelVO("평가년도", "YEAR", "left"));
    	excelInfoList.add(new ExcelVO("평가차수 코드", "EVAL_DEGREE_ID", "left"));
    	excelInfoList.add(new ExcelVO("KPI POOL I", "METRIC_GRP_ID", "left"));
    	excelInfoList.add(new ExcelVO("KPI ID", "METRIC_ID", "left"));
    	excelInfoList.add(new ExcelVO("성과조직명", "SC_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("KPI 명", "METRIC_NM", "left"));
    	excelInfoList.add(new ExcelVO("최종지표점수", "FINAL_METRIC_SCORE", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.impon.imponResult.getList", searchMap));
    	
        return searchMap;
    }
    
}
