/*************************************************************************
* CLASS 명      : MetricGrpJHAction
* 작 업 자      : 김준호
* 작 업 일      : 2013년 3월 8일 
* 기    능      : 임시지표POOL
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    김준호      2013년 3월 8일         최 초 작 업 
**************************************************************************/
package com.lexken.bsc.base;
    
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

public class MetricGrpJHAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 임시지표POOL 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricGrpJHList(SearchMap searchMap) {

        return searchMap;
    }
    
    /**
     * 임시지표POOL 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricGrpJHList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.base.metricGrpJH.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 임시지표POOL 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricGrpJHModify(SearchMap searchMap) {
        String stMode = searchMap.getString("mode");
        
        if("MOD".equals(stMode)) {
            searchMap.addList("detail", getDetail("bsc.base.metricGrpJH.getDetail", searchMap));
        }
        
        return searchMap;
    }
    
    /**
     * 임시지표POOL 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricGrpJHProcess(SearchMap searchMap) {
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
     * 임시지표POOL 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
            setStartTransaction();
        
            returnMap = insertData("bsc.base.metricGrpJH.insertData", searchMap);
        
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
     * 임시지표POOL 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
            setStartTransaction();
            
            returnMap = updateData("bsc.base.metricGrpJH.updateData", searchMap);
            
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
     * 임시지표POOL 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
        
        try {
            
            
            /*setStartTransaction();
            
            if(null !=  && 0 < .length) {
                for (int i = 0; i < .length; i++) {
                    
                    returnMap = updateData("bsc.base.metricGrpJH.deleteData", searchMap);
                }
            }*/
            
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
     * 임시지표POOL 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap metricGrpJHListExcel(SearchMap searchMap) {
        String excelFileName = "임시지표POOL";
        String excelTitle = "임시지표POOL 리스트";
        
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
    	excelInfoList.add(new ExcelVO("지표그룹ID", "METRIC_GRP_ID", "left"));
    	excelInfoList.add(new ExcelVO("KPI POOL 명", "METRIC_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("설명", "DESCRIPTION", "left"));
    	excelInfoList.add(new ExcelVO("TYPE_NM", "TYPE_NM", "left"));
    	excelInfoList.add(new ExcelVO("KPI 유형", "TYPE_ID", "left"));
    	excelInfoList.add(new ExcelVO("UNIT_NM", "UNIT_NM", "left"));
    	excelInfoList.add(new ExcelVO("단위", "UNIT", "left"));
    	excelInfoList.add(new ExcelVO("EVAL_CYCLE_NM", "EVAL_CYCLE_NM", "left"));
    	excelInfoList.add(new ExcelVO("주기", "EVAL_CYCLE", "left"));
    	excelInfoList.add(new ExcelVO("METRIC_CNT", "METRIC_CNT", "left"));
    	
        
        searchMap.put("excelFileName", excelFileName);
        searchMap.put("excelTitle", excelTitle);
        searchMap.put("excelSearchInfoList", excelSearchInfoList);
        searchMap.put("excelInfoList", excelInfoList);
        searchMap.put("excelDataList", (ArrayList)getList("bsc.base.metricGrpJH.getList", searchMap));
        
        return searchMap;
    }
    
}
