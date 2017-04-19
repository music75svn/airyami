/*************************************************************************
* CLASS 명      : MatrixKpiAction
* 작 업 자      : 박경태
* 작 업 일      : 2012년 10월 23일 
* 기    능      : 평가단별지표
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    박경태      2012년 10월 23일         최 초 작 업 
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

public class MatrixKpiAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 평가단별지표 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap matrixKpiList(SearchMap searchMap) {
    	
    	if(!"".equals(StaticUtil.nullToBlank((String)searchMap.get("evalUserGrpId")))) {
			searchMap.put("findEvalUserGrpId", (String)searchMap.get("evalUserGrpId"));
		}
    	
    	if(!"".equals(StaticUtil.nullToBlank((String)searchMap.get("pgmId")))) {
			searchMap.put("findPgmId", (String)searchMap.get("pgmId"));
		}
    	
    	/**********************************
         * 평가단 조회
         **********************************/
    	searchMap.addList("evalGrpList", getList("bsc.matrix.matrixKpi.getMatEvalList", searchMap));
    	
    	/**********************************
         * 선택 평가단 명조회
         **********************************/
    	searchMap.addList("evalUserGrpDetail", getList("bsc.matrix.matrixGrp.getDetail", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 년도별 평가단 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap getUserGroupList_ajax(SearchMap searchMap) {
    	
    	/**********************************
         * 평가단 조회
         **********************************/
    	searchMap.addList("evalGrpList", getList("bsc.matrix.matrixKpi.getMatEvalList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가단별지표 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap matrixKpiList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.matrix.matrixKpi.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가단별지표 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap matrixKpiModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
       
    	if(!"".equals(StaticUtil.nullToBlank((String)searchMap.get("evalUserGrpId")))) {
			searchMap.put("findEvalUserGrpId", (String)searchMap.get("evalUserGrpId"));
		}
    	
    	if(!"".equals(StaticUtil.nullToBlank((String)searchMap.get("findEvalUserGrpId")))) {
			searchMap.put("evalUserGrpId", (String)searchMap.get("findEvalUserGrpId"));
		}
    	
    	if(!"".equals(StaticUtil.nullToBlank((String)searchMap.get("findYear")))) {
			searchMap.put("year", (String)searchMap.get("findYear"));
		}
    	
    	/**********************************
         * 선택 평가단 명조회
         **********************************/
    	searchMap.addList("evalUserGrpDetail", getDetail("bsc.matrix.matrixGrp.getDetail", searchMap));
    	
    	/**********************************
         * 평가대상지표 전체 POOL
         **********************************/
    	searchMap.addList("totPoolList", getList("bsc.matrix.matrixKpi.getTotPoolList", searchMap));
    	
    	/**********************************
         * 선택된 평가대상지표 전체 POOL
         **********************************/
    	searchMap.addList("subPoolList", getList("bsc.matrix.matrixKpi.getSubPoolList", searchMap));
    	
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.matrix.matrixKpi.getDetail", searchMap));
    	}
    	if("".equals(stMode)) {
    		searchMap.put("mode", "ADD");
    	}
        return searchMap;
    }
    
    /**
     * 평가단별지표 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap matrixKpiProcess(SearchMap searchMap) {
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
     * 평가단별지표 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	String[] metricSubGrpIds = searchMap.getString("metricIds").split("\\|", 0);
        	
        	setStartTransaction();
        	
        	returnMap = updateData("bsc.matrix.matrixKpi.deleteAllData", searchMap, true); //기존KPI삭제
        
        	
        	if(null != metricSubGrpIds) {
		        for (int i = 0; i < metricSubGrpIds.length; i++) {
		        	if(!"".equals(StaticUtil.nullToBlank(metricSubGrpIds[i]))) {
			        	searchMap.put("metricGrpId", metricSubGrpIds[i]);
			        	returnMap = insertData("bsc.matrix.matrixKpi.insertData", searchMap);
		        	}
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
     * 평가단별지표 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("bsc.matrix.matrixKpi.updateData", searchMap);
	        
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
     * 평가단별지표 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] evalUserGrpIds = searchMap.getString("evalUserGrpIds").split("\\|", 0);
			String[] metricGrpIds = searchMap.getString("metricGrpIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != evalUserGrpIds && 0 < evalUserGrpIds.length) {
		        for (int i = 0; i < evalUserGrpIds.length; i++) {
		            searchMap.put("evalUserGrpId", evalUserGrpIds[i]);
		            searchMap.put("metricGrpId", metricGrpIds[i]);
		            returnMap = updateData("bsc.matrix.matrixKpi.deleteData", searchMap);
		            
		            returnMap = updateData("bsc.matrix.matrixKpi.deleteEvalData", searchMap, true);
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
     * 평가단별지표 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap matrixKpiListExcel(SearchMap searchMap) {
    	String excelFileName = "평가단별지표";
    	String excelTitle = "평가단별지표 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("평가단", (String)searchMap.get("evalUserGrpNm")));
    	excelSearchInfoList.add(new ExcelVO("POOL구분", (String)searchMap.get("poolGubunNm")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가단 명", "EVAL_USER_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("POOL구분", "POOL_GUBUN_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가대상지표 POOL명", "METRIC_GRP_NM", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.matrix.matrixKpi.getList", searchMap));
    	
        return searchMap;
    }
    
}
