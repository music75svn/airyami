/*************************************************************************
* CLASS 명      : ResultTargetAction
* 작 업 자      : 현걸욱
* 작 업 일      : 2012년 11월 7일 
* 기    능      : 비계량성과목표
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

public class ResultTargetAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 비계량성과목표 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap resultTargetList(SearchMap searchMap) {
    	
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	if(!loginVO.chkAuthGrp("01") && !loginVO.chkAuthGrp("60") && !loginVO.chkAuthGrp("10")) {
    		searchMap.put("authInsertUserId", loginVO.getUser_id());
    		searchMap.put("findInsertUserId", loginVO.getUser_id());
    	}
    	
    	searchMap.addList("govMetricList", getList("gov.unmeasure.resultTarget.getMetricList", searchMap));
    	
    	/**********************************
         * 디폴트 지표 조회
         **********************************/
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("findGovMetricId")))) {
    		searchMap.put("findGovMetricId", searchMap.getDefaultValue("govMetricList", "GOV_METRIC_ID", 0));
    	}
    	
    	searchMap.addList("govUserList", getList("gov.unmeasure.resultTarget.getUserList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 비계량성과목표 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap resultTargetMeasList_xml(SearchMap searchMap) {
        
        searchMap.addList("measList", getList("gov.unmeasure.resultTarget.getMeasList", searchMap));

        return searchMap;
    }
    
    /**
     * 비계량성과목표 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws s
     */
    public SearchMap resultTargetNonMeasList_xml(SearchMap searchMap) {
        
        searchMap.addList("nonMeasList", getList("gov.unmeasure.resultTarget.getNonMeasList", searchMap));

        return searchMap;
    }
    
    /**
     * 년도별 정평지표 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap getGovMetricList_ajax(SearchMap searchMap) {
    	
    	searchMap.addList("govMetricList", getList("gov.unmeasure.resultTarget.getMetricList", searchMap));

        return searchMap;
    }
    
    /**
     * 정평지표별 담당자 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap getUserList_ajax(SearchMap searchMap) {
    	
    	searchMap.addList("govUserList", getList("gov.unmeasure.resultTarget.getUserList", searchMap));

        return searchMap;
    }
    
    /**
     * 비계량성과목표 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap resultMeasTargetModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MMOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("gov.unmeasure.resultTarget.getMeasDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 비계량성과목표 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap resultNonMeasTargetModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("NMMOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("gov.unmeasure.resultTarget.getNonMeasDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 비계량성과목표 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap resultTargetProcess(SearchMap searchMap) {
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
        if("MADD".equals(stMode)) {
            searchMap = insertMeasDB(searchMap);
        } else if("NMADD".equals(stMode)) {
            searchMap = insertNonMeasDB(searchMap);    
        } else if("MMOD".equals(stMode)) {
            searchMap = updateMeasDB(searchMap);
        } else if("NMMOD".equals(stMode)) {
            searchMap = updateNonMeasDB(searchMap);    
        } else if("MDEL".equals(stMode)) {
            searchMap = deleteMeasDB(searchMap);
        } else if("NMDEL".equals(stMode)) {
            searchMap = deleteNonMeasDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 비계량성과목표 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertMeasDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("gov.unmeasure.resultTarget.insertMeasData", searchMap);
        
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
     * 비계량성과목표 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertNonMeasDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("gov.unmeasure.resultTarget.insertNonMeasData", searchMap);
        
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
     * 비계량성과목표 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateMeasDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("gov.unmeasure.resultTarget.updateMeasData", searchMap);
	        
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
     * 비계량성과목표 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateNonMeasDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("gov.unmeasure.resultTarget.updateNonMeasData", searchMap);
	        
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
     * 비계량성과목표 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteMeasDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] govMetricIds = searchMap.getString("govMetricIds").split("\\|", 0);
			String[] measIds = searchMap.getString("measIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != govMetricIds && 0 < govMetricIds.length) {
		        for (int i = 0; i < govMetricIds.length; i++) {
		            searchMap.put("govMetricId", govMetricIds[i]);
		            searchMap.put("measId", measIds[i]);
		            returnMap = updateData("gov.unmeasure.resultTarget.deleteMeasData", searchMap);
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
     * 비계량성과목표 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteNonMeasDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] govMetricIds = searchMap.getString("govMetricIds").split("\\|", 0);
			String[] nonMeasIds = searchMap.getString("nonMeasIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != govMetricIds && 0 < govMetricIds.length) {
		        for (int i = 0; i < govMetricIds.length; i++) {
		            searchMap.put("govMetricId", govMetricIds[i]);
			searchMap.put("nonMeasId", nonMeasIds[i]);
		            returnMap = updateData("gov.unmeasure.resultTarget.deleteNonMeasData", searchMap);
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
     * 비계량성과목표 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap resultTargetListExcel(SearchMap searchMap) {
    	String excelFileName = "비계량성과목표";
    	String excelTitle = "비계량성과목표 리스트";
    	
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
    	excelInfoList.add(new ExcelVO("정량성과 코드", "MEAS_ID", "left"));
    	excelInfoList.add(new ExcelVO("정량성과 명", "MEAS_NM", "left"));
    	excelInfoList.add(new ExcelVO("단위", "UNIT", "left"));
    	excelInfoList.add(new ExcelVO("Y년도 실적_439", "RES_OF_PROPER", "left"));
    	excelInfoList.add(new ExcelVO("Y-1년도 실적", "RES_OF_1_BEF_438", "left"));
    	excelInfoList.add(new ExcelVO("Y+1년도 목표", "RES_OF_1_AFT", "left"));
    	excelInfoList.add(new ExcelVO("목표설정 근거", "TARGET_BASE", "left"));
    	excelInfoList.add(new ExcelVO("입력자 사번", "INSERT_USER_ID", "left"));
    	excelInfoList.add(new ExcelVO("등록자", "CREATE_ID", "left"));
    	excelInfoList.add(new ExcelVO("생성일", "CREATE_DT", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("gov.unmeasure.resultTarget.getList", searchMap));
    	
        return searchMap;
    }
    
}
