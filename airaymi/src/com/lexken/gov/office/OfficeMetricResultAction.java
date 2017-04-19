/*************************************************************************
* CLASS 명   : OfficeMetricResultAction
* 작 업 자      : 김민주
* 작 업 일      : 2013년 06월 25일 
* 기    능      : 공공기관 경영평가지표 결과 입력
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    김민주      2013년 06월 25일         최 초 작 업 
**************************************************************************/
package com.lexken.gov.office;
    
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

public class OfficeMetricResultAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 공공기관 경영평가지표 목록 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap officeMetricList(SearchMap searchMap) {
    	//공공기관 평가군 검색 목록
    	searchMap.addList("officeGrplist", getList("gov.office.officeMetricResult.getOfficeGrpList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 공공기관 경영평가지표 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap officeMetricList_xml(SearchMap searchMap) {
        searchMap.addList("list", getList("gov.office.officeMetricResult.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 공공기관 경영평가지표 ajax 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap officeGrpList_ajax(SearchMap searchMap) {
    	searchMap.addList("list", getList("gov.office.office.getOfficeGrpList", searchMap));

        return searchMap;
    }
    
    /**
     * 공공기관 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap officeMetricResultList(SearchMap searchMap) {
    	searchMap.addList("officeDetail", getDetail("gov.office.office.getDetail", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 공공기관 경영평가지표 결과 입력 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap officeMetricResultList_xml(SearchMap searchMap) {
    	searchMap.addList("list", getList("gov.office.officeMetricResult.getResultList", searchMap));

        return searchMap;
    }
    
    /**
     * 공공기관 경영평가지표 결과 입력 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap officeMetricResultProcess(SearchMap searchMap) {
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
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 공공기관 경영평가지표 결과 입력 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	String[] govOffMetricIds = searchMap.getString("govOffMetricIds").split("\\|", 0);
        	String[] govOffWeights = searchMap.getString("govOffWeights").split("\\|", 0);
        	String[] govOffScores = searchMap.getString("govOffScores").split("\\|", 0);
        	String[] govOffActs = searchMap.getString("govOffActs").split("\\|", 0);
        	
        	if(null != govOffMetricIds && 0 < govOffMetricIds.length) {
		        for (int i = 0; i < govOffMetricIds.length; i++) {
		            searchMap.put("govOffMetricId", govOffMetricIds[i].replaceAll("none", "") );
		            searchMap.put("weight", govOffWeights[i].trim().replaceAll("none", "") );
		            searchMap.put("score", govOffScores[i].trim().replaceAll("none", "") );
		            searchMap.put("act", govOffActs[i].trim().replaceAll("none", "") );
		            
		            returnMap = updateData("gov.office.officeMetricResult.deleteData", searchMap);
		            returnMap = updateData("gov.office.officeMetricResult.updateData", searchMap);
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
     * 공공기관 정평지표 결과입력 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap officeMetricListExcel(SearchMap searchMap) {
    	String excelFileName = "공공기관 정평지표 결과입력";
    	String excelTitle = "공공기관 정평지표 결과입력 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("공공기관 평가군", StaticUtil.nullToDefault((String)searchMap.get("officeEvalGrpNm"), "전체")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("공기업평가군명", "OFFICE_EVAL_GRP_NM", "left", "OG_CNT"));
    	excelInfoList.add(new ExcelVO("기관코드", "OFFICE_ID", "center"));
    	excelInfoList.add(new ExcelVO("기관명", "OFFICE_NM", "left"));
    	excelInfoList.add(new ExcelVO("공공기관 정평지표POOL명", "GOV_METRIC_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("가중치", "WEIGHT", "center"));
    	excelInfoList.add(new ExcelVO("점수", "ACT", "center"));
    	excelInfoList.add(new ExcelVO("실적", "SCORE", "center"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("gov.office.officeMetricResult.getExcelList", searchMap));
    	
        return searchMap;
    }
    
}