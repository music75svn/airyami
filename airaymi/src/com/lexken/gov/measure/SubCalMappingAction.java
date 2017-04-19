/*************************************************************************
* CLASS 명      : SubCalMappingAction
* 작 업 자      : 현걸욱
* 작 업 일      : 2012년 11월 6일 
* 기    능      : 정평계량 세부요소매핑
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    현걸욱      2012년 11월 6일         최 초 작 업 
**************************************************************************/
package com.lexken.gov.measure;
    
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

public class SubCalMappingAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 정평계량 세부요소매핑 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap subCalMappingList(SearchMap searchMap) {

        return searchMap;
    }
    
    /**
     * 정평계량 세부요소매핑 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap subCalMappingList_xml(SearchMap searchMap) {
        
    	String[][] convertArray =  {{"YYY", ""} , {"NNN","┗"}};
    	
    	searchMap.addList("list", getList("gov.measure.subCalMapping.getList", searchMap));
        searchMap.addList("CONVERT_ARRAY", convertArray);
    	
        return searchMap;
    }
    
    /**
     * 정평계량 세부요소매핑 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap subCalMappingModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("gov.measure.subCalMapping.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 정평계량 세부요소매핑 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popSubCalMappingModify(SearchMap searchMap) {
    	
    	searchMap.addList("subCalList", getList("gov.measure.subCalMapping.getSubCalList", searchMap));
        
    	searchMap.addList("actCalTypeColNm", searchMap.getDefaultValue("subCalList", "ACT_CAL_TYPE_COL_NM", 0));
        
        return searchMap;
    }
    
    /**
     * 정평계량 세부요소매핑 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popSubCalMappingModify_xml(SearchMap searchMap) {
        
    	searchMap.addList("subCalList", getList("gov.measure.subCalMapping.getSubCalList", searchMap));
        
        return searchMap;
    }
    
    /**
     * 정평계량 세부요소매핑 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap subCalMappingProcess(SearchMap searchMap) {
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
        if("MOD".equals(stMode)) {
            searchMap = updateDB(searchMap);
        } 
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    
    /**
     * 정평계량 세부요소매핑 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	    	String year = searchMap.getString("year");
	        String govMetricId = searchMap.getString("govMetricId");
			String actCalTypeColId = searchMap.getString("actCalTypeColId");
			String[] subCalIds = searchMap.getString("subCalIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        returnMap = updateData("gov.measure.subCalMapping.deleteData", searchMap, true);
	        
	        if(null != subCalIds && 0 < subCalIds.length) {
		        for (int i = 0; i < subCalIds.length; i++) {
		        	if(!"".equals(StaticUtil.nullToBlank(subCalIds[i]))){
			        	searchMap.put("year", year);
			            searchMap.put("govMetricId", govMetricId);
			            searchMap.put("actCalTypeColId", actCalTypeColId);
			            searchMap.put("subCalId", subCalIds[i]);
			            returnMap = updateData("gov.measure.subCalMapping.insertData", searchMap, true);
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
     * 정평계량 세부요소실적 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popSubCalTypeColMappingList(SearchMap searchMap) {

    	searchMap.put("govMetricNm", getStr("gov.measure.subCalMapping.govMetricNm", searchMap));
    	searchMap.put("govMetricCalTypeNm", getStr("gov.measure.subCalMapping.govCalTypeNm", searchMap));
    	
        return searchMap;
    }
    
    
    
    
    /**
     * 정평계량 세부요소실적 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    
    public SearchMap popSubCalTypeColMappingList_xml(SearchMap searchMap) {
    	
    	searchMap.addList("list", getList("gov.measure.subCalMapping.popSubCalTypeColMappingList_xml", searchMap));
    	
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
     * 정평계량 세부요소매핑 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap subCalMappingListExcel(SearchMap searchMap) {
    	String excelFileName = "정평계량 세부요소매핑";
    	String excelTitle = "정평계량 세부요소매핑 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();

    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가지표", 	 "GOV_METRIC_NM", "left", "CAL_CNT"));
    	excelInfoList.add(new ExcelVO("평가방법", 	 "EVAL_METHOD_NM", "center", "CAL_CNT"));
    	excelInfoList.add(new ExcelVO("단위", 	 "UNIT_NM", "right", "CAL_CNT"));
    	excelInfoList.add(new ExcelVO("가중치", 	 "WEIGHT", "right", "CAL_CNT"));
    	excelInfoList.add(new ExcelVO("주관부서", 	 "SC_DEPT_NM", "right", "CAL_CNT"));
    	excelInfoList.add(new ExcelVO("담당자", 	 "INSERT_USER_NM", "right", "CAL_CNT"));
    	excelInfoList.add(new ExcelVO("평가요소", 	 "ACT_CAL_TYPE_COL_NM", "right"));
    	excelInfoList.add(new ExcelVO("세부요소수", "ACT_CAL_CNT", "right"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("gov.measure.subCalMapping.getList", searchMap));
    	
        return searchMap;
    }
    
    
    
    /**
     * 정평계량 세부요소실적조회 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap popSubCalTypeColMappingExcel(SearchMap searchMap) {
    	String excelFileName = "정평계량 세부요소실적";
    	String excelTitle = "정평계량 세부요소실적 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("year") + "년" ));
    	excelSearchInfoList.add(new ExcelVO("평가지표", (String)searchMap.get("govMetricNm")));
    	excelSearchInfoList.add(new ExcelVO("평가요소", (String)searchMap.get("govMetricCalTypeColNm")));
    	
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가지표", "SUB_CAL_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가방법", "UNIT_NM", "center"));
    	excelInfoList.add(new ExcelVO("1월", "MON_01", "right"));
    	excelInfoList.add(new ExcelVO("2월", "MON_02", "right"));
    	excelInfoList.add(new ExcelVO("3월", "MON_03", "right"));
    	excelInfoList.add(new ExcelVO("4월", "MON_04", "right"));
    	excelInfoList.add(new ExcelVO("5월", "MON_05", "right"));
    	excelInfoList.add(new ExcelVO("6월", "MON_06", "right"));
    	excelInfoList.add(new ExcelVO("7월", "MON_07", "right"));
    	excelInfoList.add(new ExcelVO("8월", "MON_08", "right"));
    	excelInfoList.add(new ExcelVO("9월", "MON_09", "right"));
    	excelInfoList.add(new ExcelVO("10월", "MON_10", "right"));
    	excelInfoList.add(new ExcelVO("11월", "MON_11", "right"));
    	excelInfoList.add(new ExcelVO("12월", "MON_12", "right"));
    	excelInfoList.add(new ExcelVO("담당자", "INSERT_USER_NM", "center"));
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("gov.measure.subCalMapping.popSubCalTypeColMappingList_xml", searchMap));
    	
        return searchMap;
    }
}
