/*************************************************************************
* CLASS 명      : ResultActualAction
* 작 업 자      : 현걸욱
* 작 업 일      : 2012년 11월 7일 
* 기    능      : 비계량지적및조치사항
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    현걸욱      2012년 11월 7일         최 초 작 업 
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

public class ResultActualAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 비계량지적및조치사항 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap resultActualList(SearchMap searchMap) {

    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	if(!loginVO.chkAuthGrp("01") && !loginVO.chkAuthGrp("60")  && !loginVO.chkAuthGrp("10")) {
    		searchMap.put("authInsertUserId", loginVO.getUser_id());
    		searchMap.put("findInsertUserId", loginVO.getUser_id());
    	}
    	
    	searchMap.addList("govMetricList", getList("gov.unmeasure.resultActual.getMetricList", searchMap));
    	
    	/**********************************
         * 디폴트 지표 조회
         **********************************/
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("findGovMetricId")))) {
    		searchMap.put("findGovMetricId", searchMap.getDefaultValue("govMetricList", "GOV_METRIC_ID", 0));
    	}
    	
    	searchMap.addList("govUserList", getList("gov.unmeasure.resultActual.getUserList", searchMap));
    	
    	
    	
        return searchMap;
    }
    
    /**
     * 비계량지적및조치사항 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap resultActualActList_xml(SearchMap searchMap) {
    	
    	searchMap.addList("actlist", getList("gov.unmeasure.resultActual.getActList", searchMap));

        return searchMap;
    	
    }
    
    /**
     * 비계량지적및조치사항 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap resultActualGovList_xml(SearchMap searchMap) {
        
        searchMap.addList("govlist", getList("gov.unmeasure.resultActual.getGovList", searchMap));

        return searchMap;
    }
    
    /**
     * 비계량지적및조치사항 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap resultActualMasList_xml(SearchMap searchMap) {
        
        searchMap.addList("maslist", getList("gov.unmeasure.resultActual.getMasList", searchMap));

        return searchMap;
    }
    
    /**
     * 년도별 정평지표 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap getGovMetricList_ajax(SearchMap searchMap) {
    	
    	searchMap.addList("govMetricList", getList("gov.unmeasure.resultActual.getMetricList", searchMap));

        return searchMap;
    }
    
    /**
     * 정평지표별 담당자 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap getUserList_ajax(SearchMap searchMap) {
    	
    	searchMap.addList("govUserList", getList("gov.unmeasure.resultActual.getUserList", searchMap));

        return searchMap;
    }
    
    /**
     * 비계량지적및조치사항 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap resultActualModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("gov.unmeasure.resultActual.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 비계량지적및조치사항 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap resultActualProcess(SearchMap searchMap) {
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
     * 비계량지적및조치사항 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("gov.unmeasure.resultActual.insertData", searchMap);
        
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
     * 비계량지적및조치사항 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("gov.unmeasure.resultActual.updateData", searchMap);
	        
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
     * 비계량지적및조치사항 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] govMetricIds = searchMap.getString("govMetricIds").split("\\|", 0);
			String[] pointOutIds = searchMap.getString("pointOutIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != govMetricIds && 0 < govMetricIds.length) {
		        for (int i = 0; i < govMetricIds.length; i++) {
		            searchMap.put("govMetricId", govMetricIds[i]);
			searchMap.put("pointOutId", pointOutIds[i]);
		            returnMap = updateData("gov.unmeasure.resultActual.deleteData", searchMap);
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
        

        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }

    /**
     * 비계량지적및조치사항 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap resultActualListExcel(SearchMap searchMap) {
    	String excelFileName = "비계량지적및조치사항";
    	String excelTitle = "비계량지적및조치사항 리스트";
    	
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
    	excelInfoList.add(new ExcelVO("정평지표코드", "GOV_METRIC_ID", "left"));
    	excelInfoList.add(new ExcelVO("지적사항 코드", "POINT_OUT_ID", "left"));
    	excelInfoList.add(new ExcelVO("지적사항구분", "POINT_OUT_GBN_ID", "left"));
    	excelInfoList.add(new ExcelVO("지적사항", "POINT_OUT_CONTENT", "left"));
    	excelInfoList.add(new ExcelVO("조치및 개선실적", "IMPROVEMENT_396", "left"));
    	excelInfoList.add(new ExcelVO("비고", "COMMENTS", "left"));
    	excelInfoList.add(new ExcelVO("입력자 사번", "INSERT_USER_ID", "left"));
    	excelInfoList.add(new ExcelVO("등록자", "CREATE_ID", "left"));
    	excelInfoList.add(new ExcelVO("생성일", "CREATE_DT", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("gov.unmeasure.resultActual.getList", searchMap));
    	
        return searchMap;
    }
    
}
