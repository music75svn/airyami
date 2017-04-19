/*************************************************************************
* CLASS 명	: MetricGrpAction
* 작 업 자		: 김민주
* 작 업 일		: 2013년 06월 26일
* 기    능		: KGS2020KPI POOL
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    김민주      2013년 06월 26일         최 초 작 업
**************************************************************************/

package com.lexken.man.metric;

import java.util.ArrayList;
import java.util.HashMap;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.FileInfoVO;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.StringConstants;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.struts2.IsBoxActionSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.core.StaticFactory;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.login.LoginVO;
import com.lexken.framework.util.HtmlHelper;
import com.lexken.framework.util.StaticUtil;

public class MetricGrpAction extends CommonService {

	private static final long serialVersionUID = 1L;
	    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * KGS2020KPI POOL 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricGrpList(SearchMap searchMap) {
    	
        return searchMap;
    }
    
    /**
     * KGS2020KPI POOL 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricGrpList_xml(SearchMap searchMap) {
    	
    	searchMap.addList("list", getList("man.metric.metricGrp.getList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * KGS2020KPI POOL 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricGrpModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	HashMap detail = new HashMap();
    	
    	if("MOD".equals(stMode)) {
	    	/**********************************
	         * KGS2020KPI POOL 상세조회 
	         **********************************/
    		detail = getDetail("man.metric.metricGrp.getDetail", searchMap);
    		searchMap.addList("detail", detail);
    	}
    	
    	/**********************************
         * 산식컬럼 조회 
         **********************************/
        ArrayList calTypeColList = new ArrayList();
        calTypeColList = (ArrayList)getList("man.metric.metricGrp.calTypeColList", searchMap); 
        searchMap.addList("calTypeColList", calTypeColList);
        
        /**********************************
         * 득점산식조회 
         **********************************/
        searchMap.addList("scoreCalTypeList", getList("man.metric.metricGrp.scoreCalTypeList", searchMap));
        
        /**********************************
         * 평가구간대 조회 
         **********************************/
        searchMap.addList("evalSectionList", getList("man.metric.metricGrp.evalSectionList", searchMap));
        
        if("MOD".equals(stMode)) {
    		/**********************************
	         * KPI 산식명 조회 
	         **********************************/
        	String typeId = (String)detail.get("TYPE_ID");
        	
        	if("01".equals(typeId)) {	
		        String actCalTypeNm = (String)detail.get("ACT_CAL_TYPE");
		        HashMap<String, String> calTyepColValueMap = new HashMap<String, String>();
		        
		        if(null != calTypeColList && 0 < calTypeColList.size()) {
			        for (int i = 0; i < calTypeColList.size(); i++) {
			        	HashMap<String, String> t = (HashMap<String, String>)calTypeColList.get(i);
						calTyepColValueMap.put((String)t.get("CAL_TYPE_COL"), (String)t.get("CAL_TYPE_COL_NM"));
					}
		        }		       		       
		        
		        String calTypeColDesc = HtmlHelper.changeCalNmToCalDesc(actCalTypeNm, calTyepColValueMap);
		        
		        searchMap.addList("calTypeColDesc", calTypeColDesc);
        	}
	    }
        
        return searchMap;
    }
    
    /**
     * KGS2020KPI POOL 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricGrpProcess(SearchMap searchMap) {
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
     * 난이도평가 평가단 관리 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        String typeId = searchMap.getString("typeId");
        
        /**********************************
         * 실적산식 Parameter setting
         **********************************/
        String[] calTypeCols = searchMap.getStringArray("calTypeCols");
        String[] calTypeColNms = searchMap.getStringArray("calTypeColNms");
        String[] insertGubuns = searchMap.getStringArray("insertGubuns");
        String[] units = searchMap.getStringArray("units");
        
        /**********************************
         * 구간대 Parameter setting
         **********************************/
        String[] evalSectionIds = searchMap.getStringArray("evalSectionIds");
        String[] fromValues = searchMap.getStringArray("fromValues");
        String[] toValues = searchMap.getStringArray("toValues");
        String[] conversionScores = searchMap.getStringArray("conversionScores");
        
        try {
        	setStartTransaction();
        	
        	/**********************************
             * 지표코드 채번 
             **********************************/
	        String manKpiGrpId = getStr("man.metric.metricGrp.getManKpiGrpId", searchMap);
	        searchMap.put("manKpiGrpId", manKpiGrpId);
        
	        /**********************************
	         * 지표풀 등록 
	         **********************************/
        	returnMap = insertData("man.metric.metricGrp.insertData", searchMap);
        
        	/**********************************
	         * 실적산식 삭제 
	         **********************************/
	        returnMap = updateData("man.metric.metricGrp.deleteCalTypeCol", searchMap, true);

	        /**********************************
	         * 실적산식 등록 
	         **********************************/
	        if("01".equals(typeId)) { //정량지표 
		        for (int i = 0; i < calTypeCols.length; i++) {
		            searchMap.put("calTypeCol", calTypeCols[i]);
		            searchMap.put("calTypeColNm", calTypeColNms[i]);
		            searchMap.put("insertGubun", insertGubuns[i]);
		            searchMap.put("unit", units[i]);
		            returnMap = insertData("man.metric.metricGrp.insertCalTypeCol", searchMap);
		        }
	        }
	        
	        /**********************************
	         * 구간대 삭제 
	         **********************************/
	        returnMap = updateData("man.metric.metricGrp.deleteEvalSection", searchMap, true);
	        
	        /**********************************
	         * 구간대 등록 
	         **********************************/
	        String scoreCalTypeGubun = searchMap.getString("scoreCalTypeGubun");
	        
	        if("01".equals(typeId)) { //정량지표
	        	if("02".equals(scoreCalTypeGubun) || "03".equals(scoreCalTypeGubun) || "04".equals(scoreCalTypeGubun)) { 
			        for (int i = 0; i < evalSectionIds.length; i++) {
			            searchMap.put("evalSectionId", evalSectionIds[i]);
			            searchMap.put("fromValue", fromValues[i]);
			            searchMap.put("toValue", toValues[i]);
			            searchMap.put("conversionScore", conversionScores[i]);
			            returnMap = insertData("man.metric.metricGrp.insertEvalSection", searchMap);
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
     * KGS2020KPI POOL 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        String typeId = searchMap.getString("typeId");
        String actCalType = searchMap.getString("actCalType");
        String orgActCalType = searchMap.getString("orgActCalType");
        
        /**********************************
         * 실적산식 Parameter setting
         **********************************/
        String[] calTypeCols = searchMap.getStringArray("calTypeCols");
        String[] calTypeColNms = searchMap.getStringArray("calTypeColNms");
        String[] insertGubuns = searchMap.getStringArray("insertGubuns");
        String[] units = searchMap.getStringArray("units");
        
        /**********************************
         * 구간대
         **********************************/
        String[] evalSectionIds = searchMap.getStringArray("evalSectionIds");
        String[] fromValues = searchMap.getStringArray("fromValues");
        String[] toValues = searchMap.getStringArray("toValues");
        String[] conversionScores = searchMap.getStringArray("conversionScores");
        
        try {
	        setStartTransaction();
	        
	        /**********************************
	         * 지표풀 수정 
	         **********************************/
	        returnMap = updateData("man.metric.metricGrp.updateData", searchMap);
	        
	        /************************************
	         * 실적산식이 변경이 되었는지 체크
	         ************************************/
	        if(actCalType.equals(orgActCalType)) {
	        	/**********************************
		         * 실적산식 수정 
		         **********************************/
		        if("01".equals(typeId)) {
		        	if(null != calTypeCols && 0 < calTypeCols.length) {
				        for (int i = 0; i < calTypeCols.length; i++) {
				            searchMap.put("calTypeCol", calTypeCols[i]);
				            searchMap.put("calTypeColNm", calTypeColNms[i]);
				            searchMap.put("insertGubun", insertGubuns[i]);
				            searchMap.put("unit", units[i]);
				            returnMap = insertData("man.metric.metricGrp.updateCalTypeCol", searchMap);
				        }
		        	}
		        }
	        } else {
	        	/**********************************
		         * 실적산식 삭제 
		         **********************************/
		        returnMap = updateData("man.metric.metricGrp.deleteCalTypeCol", searchMap, true);
		        
		        /**********************************
		         * 실적산식 등록 
		         **********************************/
		        if("01".equals(typeId)) {
		        	if(null != calTypeCols && 0 < calTypeCols.length) {
				        for (int i = 0; i < calTypeCols.length; i++) {
				            searchMap.put("calTypeCol", calTypeCols[i]);
				            searchMap.put("calTypeColNm", calTypeColNms[i]);
				            searchMap.put("insertGubun", insertGubuns[i]);
				            searchMap.put("unit", units[i]);
				            returnMap = insertData("man.metric.metricGrp.insertCalTypeCol", searchMap);
				        }
		        	}
		        }
	        }
	        
	        /**********************************
	         * 구간대 삭제 
	         **********************************/
	        returnMap = updateData("man.metric.metricGrp.deleteEvalSection", searchMap, true);

	        /**********************************
	         * 구간대 등록 
	         **********************************/
	        
	        String scoreCalTypeGubun = searchMap.getString("scoreCalTypeGubun");
	        
	        if("01".equals(typeId)) {
	        	if("02".equals(scoreCalTypeGubun) || "03".equals(scoreCalTypeGubun) || "04".equals(scoreCalTypeGubun)) {
	        		if(null != evalSectionIds && 0 < evalSectionIds.length) {
				        for (int i = 0; i < evalSectionIds.length; i++) {
				            searchMap.put("evalSectionId", evalSectionIds[i]);
				            searchMap.put("fromValue", fromValues[i]);
				            searchMap.put("toValue", toValues[i]);
				            searchMap.put("conversionScore", conversionScores[i]);
				            returnMap = insertData("man.metric.metricGrp.insertEvalSection", searchMap);
				        }
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
     * 지표Pool관리 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 

        try {
	        String manKpiGrpIds = searchMap.getString("manKpiGrpIds");
	        String[] keyArray = manKpiGrpIds.split("\\|", 0);
	 
	        setStartTransaction();
	        
	        for (int i=0; i<keyArray.length; i++) {
	            searchMap.put("manKpiGrpId", keyArray[i]);
	            returnMap = updateData("man.metric.metricGrp.deleteData", searchMap);
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
     * KGS2020KPI 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap metricGrpListExcel(SearchMap searchMap) {
    	String excelFileName = "KGS2020KPI";
    	String excelTitle = "KGS2020KPI리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("findYear")));
    	excelSearchInfoList.add(new ExcelVO("사용여부", StaticUtil.nullToDefault((String)searchMap.get("findUseYnNm"), "전체")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("POOL코드", "MAN_KPI_GRP_ID", "left"));
    	excelInfoList.add(new ExcelVO("KGS2020 POOL명", "MAN_KPI_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("설명", "CONTENT", "left"));
    	excelInfoList.add(new ExcelVO("평가유형", "TYPE_ID", "center"));
    	excelInfoList.add(new ExcelVO("단위", "UNIT", "center"));
    	excelInfoList.add(new ExcelVO("주기", "EVAL_CYCLE", "center"));
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("man.metric.metricGrp.getList", searchMap));
    	
        return searchMap;
    }
}