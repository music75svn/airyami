/*************************************************************************
* CLASS 명      : ArchiveTotMonAction
* 작 업 자      : 하윤식
* 작 업 일      : 2012년 11월 13일 
* 기    능      : 계량평가지표현황표
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    하윤식      2012년 11월 13일         최 초 작 업 
**************************************************************************/
package com.lexken.gov.measure;
    
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

public class ArchiveTotMonAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 계량평가지표현황표 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap archiveTotMonList(SearchMap searchMap) {
    	
    	/**********************************
         * 매핑 평가조직 가져오기
         **********************************/
    	searchMap.addList("mappingDeptList", getList("gov.measure.archiveTotMon.getMappingDeptList", searchMap));
    	
    	/**********************************
         * 조직평가그룹 정보 가져오기
         **********************************/
    	searchMap.addList("groupInfoList", getList("gov.measure.archiveTotMon.getGroupInfoList", searchMap));

        return searchMap;
    }
    
    /**
     * 계량평가지표현황표 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap archiveTotMonList_xml(SearchMap searchMap) {
    	
    	/**********************************
         * 매핑 평가조직 가져오기
         **********************************/
    	ArrayList mappingDeptList = (ArrayList)getList("gov.measure.archiveTotMon.getMappingDeptList", searchMap);
     	 
    	String[] deptArray = new String[0]; 
    	if(null != mappingDeptList && 0 < mappingDeptList.size()) {
    		deptArray = new String[mappingDeptList.size()];
    		for(int i=0; i<mappingDeptList.size(); i++) {
    			HashMap map = (HashMap)mappingDeptList.get(i);
    			deptArray[i] = (String)map.get("SC_DEPT_ID"); 
    		}
    	}
    	
    	searchMap.put("deptArray", deptArray);
        
    	searchMap.addList("mappingDeptList", mappingDeptList);
        searchMap.addList("list", getList("gov.measure.archiveTotMon.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 계량평가지표현황표 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap archiveTotMonModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("gov.measure.archiveTotMon.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 계량평가지표현황표 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap archiveTotMonProcess(SearchMap searchMap) {
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
     * 계량평가지표현황표 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("gov.measure.archiveTotMon.insertData", searchMap);
        
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
     * 계량평가지표현황표 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("gov.measure.archiveTotMon.updateData", searchMap);
	        
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
     * 계량평가지표현황표 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap();
        
	    /* 
	    try {
	        
	        
	        setStartTransaction();
	        
	        if(null !=  && 0 < .length) {
		        for (int i = 0; i < .length; i++) {
		            
		            returnMap = updateData("gov.measure.archiveTotMon.deleteData", searchMap);
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
	    */
        
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
     * 계량평가지표현황표 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap archiveTotMonListExcel(SearchMap searchMap) {
    	String excelFileName = "계량지표현황표";
    	String excelTitle = "계량지표현황표";
    	
    	/**********************************
         * 매핑 평가조직 가져오기
         **********************************/
    	ArrayList mappingDeptList = (ArrayList)getList("gov.measure.archiveTotMon.getMappingDeptList", searchMap);
     	 
    	String[] deptArray = new String[0]; 
    	if(null != mappingDeptList && 0 < mappingDeptList.size()) {
    		deptArray = new String[mappingDeptList.size()];
    		for(int i=0; i<mappingDeptList.size(); i++) {
    			HashMap map = (HashMap)mappingDeptList.get(i);
    			deptArray[i] = (String)map.get("SC_DEPT_ID"); 
    		}
    	}
    	
    	searchMap.put("deptArray", deptArray);
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("기준월", (String)searchMap.get("monNm")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가범주명", "EVAL_CAT_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("지표명", "GOV_METRIC_NM", "left"));
    	excelInfoList.add(new ExcelVO("관리부서", "SC_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("담당자", "INSERT_USER_NM", "left"));
    	excelInfoList.add(new ExcelVO("실적", "ACTUAL", "center"));
    	excelInfoList.add(new ExcelVO("점수", "SCORE", "center"));
    	excelInfoList.add(new ExcelVO("상태", "STATUS_ID", "center"));
    	
    	if(null != mappingDeptList && 0 < mappingDeptList.size()) {
    		for(int i = 0;i < mappingDeptList.size(); i++) {
    			HashMap tmap = (HashMap)mappingDeptList.get(i);
    			excelInfoList.add(new ExcelVO((String)tmap.get("SC_DEPT_NM"), (String)tmap.get("DEPT_COL_NM"), "center"));
    		}
    	}
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("gov.measure.archiveTotMon.getList", searchMap));
    	
        return searchMap;
    }
    
}
