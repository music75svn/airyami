/*************************************************************************
* CLASS 명      : ResultCompareAction
* 작 업 자      : 박경태
* 작 업 일      : 2012년 11월 6일 
* 기    능      : 전년도실적비교
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    박경태      2012년 11월 6일         최 초 작 업 
**************************************************************************/
package com.lexken.gov.unmeasure;
    
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
import com.lexken.framework.login.LoginVO;
import com.lexken.framework.util.StaticUtil;

public class ResultCompareAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 전년도실적비교 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap resultCompareList(SearchMap searchMap) {
    	
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
    	/**********************************
         * 권한별 처리
         **********************************/
    	if(loginVO.chkAuthGrp("01") || loginVO.chkAuthGrp("60") || loginVO.chkAuthGrp("10")) {
    		searchMap.addList("govMetricList", getList("gov.unmeasure.resultCompare.getMetricList", searchMap));
    		
    		if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("findGovMetric")))) {
        		searchMap.put("findGovMetric", searchMap.getDefaultValue("govMetricList", "GOV_METRIC_ID", 0));
        	}
    		
    		searchMap.addList("govUserList", getList("gov.unmeasure.resultCompare.getUserList", searchMap));
    	} else {
    		searchMap.put("findInsertUser", searchMap.get("loginUserId"));
    		
    		searchMap.addList("govMetricList", getList("gov.unmeasure.resultCompare.getMetricUserList", searchMap));
    	}
    	
    	

        return searchMap;
    }
    
    /**
     * 전년도실적비교 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap resultCompareList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("gov.unmeasure.resultCompare.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 년도별 경평지표 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap getGovMetricList_ajax(SearchMap searchMap) {
    	
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
    	/**********************************
         * 권한별 처리
         **********************************/
    	if(loginVO.chkAuthGrp("01") || loginVO.chkAuthGrp("60")) {
    		searchMap.addList("govMetricList", getList("gov.unmeasure.resultCompare.getMetricList", searchMap));
    	} else {
    		searchMap.addList("govMetricList", getList("gov.unmeasure.resultCompare.getMetricUserList", searchMap));
    	}

        return searchMap;
    }
    
    /**
     * 경평지표별 담당자 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap getUserList_ajax(SearchMap searchMap) {
    	
    	searchMap.addList("govUserList", getList("gov.unmeasure.resultCompare.getUserList", searchMap));

        return searchMap;
    }
    
    /**
     * 전년도실적비교 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap resultCompareModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("gov.unmeasure.resultCompare.getDetail", searchMap));
    	}
    	//바로 위 쿼리에서 아래 쿼리 값을 불러오게 수정하였습니다. 13.07.09
    	
    	searchMap.addList("govMetricDetail", getDetail("gov.system.govMetric.getDetail", searchMap));
    	searchMap.addList("govUserDetail", getDetail("gov.unmeasure.resultCompare.getUserNm", searchMap));
        
        return searchMap;
    }
    
    /**
     * 전년도실적비교 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap resultCompareProcess(SearchMap searchMap) {
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
     * 전년도실적비교 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("gov.unmeasure.resultCompare.insertData", searchMap);
        
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
     * 전년도실적비교 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("gov.unmeasure.resultCompare.updateData", searchMap);
	        
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
     * 전년도실적비교 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] govMetricIds = searchMap.getString("govMetricIds").split("\\|", 0);
			String[] compareIds = searchMap.getString("compareIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != govMetricIds && 0 < govMetricIds.length) {
		        for (int i = 0; i < govMetricIds.length; i++) {
		            searchMap.put("govMetricId", govMetricIds[i]);
		            searchMap.put("compareId", compareIds[i]);
		            returnMap = updateData("gov.unmeasure.resultCompare.deleteData", searchMap);
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
        
		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("sortOrder"), "정렬순서");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}


        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }

    /**
     * 전년도실적비교 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap resultCompareListExcel(SearchMap searchMap) {
    	String excelFileName = "전년도실적비교";
    	String excelTitle = "전년도실적비교 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("경영지표", (String)searchMap.get("govMetricNm")));
    	excelSearchInfoList.add(new ExcelVO("담당자", (String)searchMap.get("insertUserNm")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	int width = 400;
    	
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("세부평가", "FIELD", "left"));
    	excelInfoList.add(new ExcelVO("전년도까지의 주요실적", "RES_OF_1_BEF_DESC", "left"));
    	excelInfoList.add(new ExcelVO("현재년도 주요실적", "RES_OF_PROPER_DESC", "left"));
    	excelInfoList.add(new ExcelVO("개선효과", "IMPROVEMENT", "left"));
    	excelInfoList.add(new ExcelVO("담당자", "INSERT_USER_NM", "left"));
    	excelInfoList.add(new ExcelVO("정렬순서", "SORT_ORDER", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("gov.unmeasure.resultCompare.getList", searchMap));
    	
        return searchMap;
    }
    
}
