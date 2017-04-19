/*************************************************************************
* CLASS 명      : DirGovMetricAction
* 작 업 자      : 방승현
* 작 업 일      : 2013년 06월 26일 
* 기    능      : 정평지표관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    방승현      2013년 06월 26일         최 초 작 업 
**************************************************************************/
package com.lexken.prs.dir;
    
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
import com.lexken.framework.util.HtmlHelper;
import com.lexken.framework.util.StaticUtil;

public class DirGovMetricAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 정평지표관리 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap dirGovMetricList(SearchMap searchMap) {

        return searchMap;
    }
    
    /**
     * 정평지표관리 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap dirGovMetricList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("prs.dir.dirGovMetric.getList", searchMap));

        return searchMap;
    }

    /**
     * 정평지표관리 상세 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap dirGovMetricModify_xml(SearchMap searchMap) {
    	
    	searchMap.addList("detail", getList("prs.dir.dirGovMetric.getDetailList", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 정평지표관리 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap dirGovMetricModify(SearchMap searchMap) {
        return searchMap;
    }
    
    /**
     * 정평지표관리 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap dirGovMetricProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 무결성 체크
         *********************************
        if(!"DEL".equals(stMode)) {
            returnMap = this.validChk(searchMap);
            
            if((Integer)returnMap.get("ErrorNumber") < 0 ){
                searchMap.addList("returnMap", returnMap);
                return searchMap;
            }
        }*/
        
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
     * 정평지표관리 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	
        	setStartTransaction();
            
            String[] chks = searchMap.getStringArray("chk");
            
            if(chks != null && !"".equals(chks) ) {
	            returnMap = deleteData("prs.dir.dirGovMetric.deleteDGMData", searchMap, true);
	            for(int i=0; i < chks.length; i++) {
	            	if(!chks[i].equals("") && chks[i] != null) {
			            searchMap.put("govMetricId", chks[i]);
			   		    returnMap = insertData("prs.dir.dirGovMetric.insertData", searchMap);
			        }
	            }
            }else if( chks == null || "".equals(chks) ) {
            	returnMap = deleteData("prs.dir.dirGovMetric.deleteDGMData", searchMap, true);
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
     * 정평지표관리 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	
	        
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
     * 정평지표관리 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        
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
     * 정평지표관리 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap dirGovMetricListExcel(SearchMap searchMap) {
    	String excelFileName = "정평지표관리";
    	String excelTitle = "정평지표관리 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("findYear")));
    	excelSearchInfoList.add(new ExcelVO("이름", (String)searchMap.get("korNm")));
    	excelSearchInfoList.add(new ExcelVO("직위", (String)searchMap.get("posTcNm")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가범주", "EVAL_CAT_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가부문", "EVAL_CAT_NM", "left"));
    	excelInfoList.add(new ExcelVO("정부경영평가 지표", "GOV_METRIC_NM", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("prs.dir.dirGovMetric.getExcelList", searchMap));
    	
        return searchMap;
    }

   
    
}
