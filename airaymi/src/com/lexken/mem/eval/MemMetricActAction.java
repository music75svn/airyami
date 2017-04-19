/*************************************************************************
* CLASS 명      : MemMetricActAction
* 작 업 자      : 유연주
* 작 업 일      : 2017년 04월03일 
* 기    능      : 공통지표실적
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    유연주      2017년 04월03일           최 초 작 업 
**************************************************************************/
package com.lexken.mem.eval;
    
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelUpload;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginVO;
import com.lexken.framework.util.StaticUtil;

public class MemMetricActAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 공통지표실적
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memMetricActList(SearchMap searchMap) {

        searchMap.addList("metricGrpList", getList("mem.eval.memMetricAct.getMetricGrpList", searchMap));
        
    	return searchMap;
    }
    
    /**
     * 공통지표실적 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memMetricActList_xml(SearchMap searchMap) {
    	
        searchMap.addList("list", getList("mem.eval.memMetricAct.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 엑셀양식 다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap excelTemplateDown(SearchMap searchMap) {
 	   	String excelFileName = "공통지표실적엑셀업로드샘플";
 	   
	   	/****************************************************************************************************
	        * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
	    ****************************************************************************************************/
	   	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
	   	excelInfoList.add(new ExcelVO("평가년도", "YEAR", "center"));
	   	excelInfoList.add(new ExcelVO("평가월", "MON", "center"));
	   	excelInfoList.add(new ExcelVO("지표코드", "METRIC_ID", "center"));
	   	excelInfoList.add(new ExcelVO("지표명", "METRIC_NM", "left"));
	   	excelInfoList.add(new ExcelVO("산식항목코드", "CAL_TYPE_COL", "center"));
	   	excelInfoList.add(new ExcelVO("산식항목명", "CAL_TYPE_COL_NM", "left"));
	   	excelInfoList.add(new ExcelVO("직원번호", "EMP_NO", "center"));
	   	excelInfoList.add(new ExcelVO("직원명", "EMP_NM", "center"));
	   	excelInfoList.add(new ExcelVO("실적값", "VALUE", "left"));
	
	   	searchMap.put("excelFileName", excelFileName);
	   	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("mem.eval.memMetricAct.getExcelTemplate", searchMap));
 	   	
 	    return searchMap;
    }
    
    /**
     * 엑셀업로드 팝업
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popExcelUpload(SearchMap searchMap) {
    	
        return searchMap;
    }
    
    /**
     * 공통지표실적 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memMetricActProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
		 * 파일복사
		 **********************************/
		searchMap.fileCopy("/temp", "/excelUpload"); 
		
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("ADD".equals(stMode)) {
            searchMap = insertExcelDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 공통지표실적 등록
     * @param      
     * @return String
     * @throws 
     */
    public SearchMap insertExcelDB(SearchMap searchMap) {
        HashMap returnMap = new HashMap();    
        ArrayList excelDataList = new ArrayList();
        LoginVO loginVO = (LoginVO)searchMap.get("loginVO");

        searchMap.put("insertEmpId", loginVO.getUser_id());
        searchMap.put("insertEmpNm", loginVO.getUser_nm());
        
        try {
        	setStartTransaction();
        	
        	ExcelUpload excel = ExcelUpload.getInstance();
        	excelDataList = excel.execlActUpload(searchMap);
        	
        	logger.debug("excelDataList.size() : "+excelDataList.size());
        	
        	if(null != excelDataList && 0 < excelDataList.size()) {
                String[] strYear = (String[]) excelDataList.get(0);
                String[] strMon = (String[]) excelDataList.get(1);
    			String[] strMetricId = (String[]) excelDataList.get(2);
    			String[] strCalTypeCol = (String[]) excelDataList.get(3);
    			String[] strEmpNo = (String[]) excelDataList.get(4);
    			String[] strValue = (String[]) excelDataList.get(5);
            
                for(int i=0; i<strMetricId.length; i++) {
                	if(!"".equals(StaticUtil.nullToBlank(strMetricId[i])) && !"".equals(StaticUtil.nullToBlank(strValue[i]))) {
	                	searchMap.put("year", strYear[i]);
	                	searchMap.put("mon", strMon[i]);
	                	searchMap.put("analCycle", "");
	                	searchMap.put("seq", "");
	                	searchMap.put("metricId", strMetricId[i]);
	                	searchMap.put("colName", strCalTypeCol[i]);
	                	searchMap.put("colValue", strValue[i]);
	                	searchMap.put("actStatusId", "");
			            
			            logger.debug("================================================================");
			            logger.debug("searchMap : "+searchMap);
			            logger.debug("================================================================");
			            logger.debug("year : "+searchMap.getString("year"));
			            logger.debug("mon : "+searchMap.getString("mon"));
			            logger.debug("metricId : "+searchMap.getString("metricId"));
			            logger.debug("colName : "+searchMap.getString("colName"));
			            logger.debug("colValue : "+searchMap.getString("colValue"));
			            logger.debug("insertEmpId : "+searchMap.getString("insertEmpId"));
			            logger.debug("insertEmpNm : "+searchMap.getString("insertEmpNm"));
			            returnMap = insertData("mem.eval.memActualMng.insertData", searchMap);
                	}
                }
        	}        	
        } catch (Exception e) {
        	logger.error(e.toString());
        	setRollBackTransaction();
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        	setCommitTransaction();
        }
        
        /**********************************
         * Return
         **********************************/
        searchMap.addList("returnMap", returnMap);
        return searchMap;    
    }
}
