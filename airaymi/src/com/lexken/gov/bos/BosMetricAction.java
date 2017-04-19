/*************************************************************************
* CLASS 명      : BosMetricAction
* 작 업 자      : 박경태
* 작 업 일      : 2012년 11월 7일 
* 기    능      : 기관장지표
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    박경태      2012년 11월 7일         최 초 작 업 
**************************************************************************/
package com.lexken.gov.bos;
    
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

public class BosMetricAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 기관장지표 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap bosMetricList(SearchMap searchMap) {

        return searchMap;
    }
    
    /**
     * 기관장지표 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap bosMetricList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("gov.bos.bosMetric.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 지표 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popMetricSearch(SearchMap searchMap) {
    	
        return searchMap;
    }
    
    /**
     * 경평지표관리 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popMetricList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("gov.bos.bosMetric.getGovList", searchMap));

        return searchMap;
    }
    
    /**
     * 경평지표 상세조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap govMetricInfo_ajax(SearchMap searchMap) {
    	
    	searchMap.addList("govMetricDetail", getList("gov.system.govMetric.getDetail", searchMap));
    	
    	searchMap.addList("govCalTypeColList", getList("gov.system.govMetric.calTypeColList", searchMap));
    	
    	searchMap.addList("govCalTypeDetailList", getList("gov.system.govMetric.calTypeDetailList", searchMap));

        return searchMap;
    }
    
    /**
     * 기관장지표 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap bosMetricModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	HashMap detail = new HashMap();
        
    	if(!"".equals(StaticUtil.nullToBlank((String)searchMap.get("year")))) {
			searchMap.put("findYear", (String)searchMap.get("year"));
		}
    	
    	/**********************************
         * 평가범주 조회
         **********************************/
    	searchMap.addList("evalCatGrpList", getList("gov.bos.evalCatGrp.getList", searchMap));
    	
    	if("MOD".equals(stMode)) {
    		/**********************************
	         * 지표그룹 상세조회 
	         **********************************/
	    	detail = getDetail("gov.bos.bosMetric.getDetail", searchMap);
	        searchMap.addList("detail", detail);
	        
	        /**********************************
             * 산식컬럼 조회 
             **********************************/
            ArrayList calTypeColList = new ArrayList();
            calTypeColList = (ArrayList)getList("gov.bos.bosMetric.calTypeColList", searchMap); 
            searchMap.addList("calTypeColList", calTypeColList);
            
            /**********************************
             * 세부평가내용 조회 
             **********************************/
            ArrayList detailEvalItemList = new ArrayList();
            detailEvalItemList = (ArrayList)getList("gov.bos.bosMetric.detailEvalItemList", searchMap); 
            searchMap.addList("detailEvalItemList", detailEvalItemList);
            
            /**********************************
	         * KPI 산식명 조회 
	         **********************************/
        	String typeId = (String)detail.get("GBN_ID");
        	
        	if("01".equals(typeId)) {	
		        String actCalTypeNm = (String)detail.get("ACT_CAL_TYPE");
		        
		        if(actCalTypeNm!=null){
			        HashMap<String, String> calTyepColValueMap = new HashMap<String, String>();
			        
			        if(null != calTypeColList && 0 < calTypeColList.size()) {
				        for (int i = 0; i < calTypeColList.size(); i++) {
				        	HashMap<String, String> t = (HashMap<String, String>)calTypeColList.get(i);
							calTyepColValueMap.put((String)t.get("ACT_CAL_TYPE_COL_ID"), (String)t.get("ACT_CAL_TYPE_COL_NM"));
						}
			        }
			        
			        String calTypeColDesc = HtmlHelper.changeCalNmToCalDesc(actCalTypeNm, calTyepColValueMap);
			        
			        searchMap.addList("calTypeColDesc", calTypeColDesc);
		        }
        	}
    	}
        
        return searchMap;
    }
    
    /**
     * 기관장지표 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap bosMetricProcess(SearchMap searchMap) {
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
     * 기관장지표 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	String gbnId = searchMap.getString("gbnId");
        	
        	/**********************************
             * 실적산식 Parameter setting
             **********************************/
            String[] calTypeCols = searchMap.getStringArray("calTypeCols");
            String[] calTypeColNms = searchMap.getStringArray("calTypeColNms");
            String[] units = searchMap.getStringArray("units");
            
        	setStartTransaction();
        	
        	/**********************************
	         * 지표 코드 채번 
	         **********************************/
	        String govMetricId = getStr("gov.bos.bosMetric.getMetricId", searchMap);
	        searchMap.put("bossMetricId", govMetricId);
        
        	returnMap = insertData("gov.bos.bosMetric.insertData", searchMap);
        	
        	/**********************************
	         * 실적산식 등록 
	         **********************************/
	        if("01".equals(gbnId)) { //계량지표 
	        	/**********************************
		         * 실적산식 삭제 
		         **********************************/
		        returnMap = updateData("gov.bos.bosMetric.deleteCalTypeCol", searchMap, true);
		        
		        for (int i = 0; i < calTypeCols.length; i++) {
		            searchMap.put("calTypeCol", calTypeCols[i]);
		            searchMap.put("calTypeColNm", calTypeColNms[i]);
		            searchMap.put("unit", units[i]);
		            returnMap = insertData("gov.bos.bosMetric.insertCalTypeCol", searchMap);
		        }
	        }
	        
	        /**********************************
	         * 세부평가내용 등록 
	         **********************************/
	        if("02".equals(gbnId)) { //비계량지표 
	        	returnMap = saveDetailEvalItem(searchMap);
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
     * 기관장지표 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	String gbnId = searchMap.getString("gbnId");
        	
        	/**********************************
             * 실적산식 Parameter setting
             **********************************/
            String[] calTypeCols = searchMap.getStringArray("calTypeCols");
            String[] calTypeColNms = searchMap.getStringArray("calTypeColNms");
            String[] units = searchMap.getStringArray("units");
            
	        setStartTransaction();
	        
	        returnMap = updateData("gov.bos.bosMetric.updateData", searchMap);
	        
	        /**********************************
	         * 실적산식 삭제 
	         **********************************/
	        returnMap = updateData("gov.bos.bosMetric.deleteCalTypeCol", searchMap, true);
	        
	        
	        /**********************************
	         * 실적산식 등록 
	         **********************************/
	        if("01".equals(gbnId)) { //계량지표 
	        	
		        for (int i = 0; i < calTypeCols.length; i++) {
		            searchMap.put("calTypeCol", calTypeCols[i]);
		            searchMap.put("calTypeColNm", calTypeColNms[i]);
		            searchMap.put("unit", units[i]);
		            returnMap = insertData("gov.bos.bosMetric.insertCalTypeCol", searchMap);
		        }
	        }
	        
	        /**********************************
	         * 세부평가내용 삭제 
	         **********************************/
	        returnMap = updateData("gov.bos.bosMetric.deleteDetailItem", searchMap, true);
	        
	        
	        /**********************************
	         * 세부평가내용 등록 
	         **********************************/
	        if("02".equals(gbnId)) { //비계량지표 
	        	returnMap = saveDetailEvalItem(searchMap); 
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
     * 세부평가내용을 저장
     * @param searchMap
     * @return
     * @throws Exception
     */
    private HashMap saveDetailEvalItem(SearchMap searchMap) throws Exception {
    	HashMap returnMap = new HashMap();
    	
        /**********************************
         * 세부평가내용 Parameter setting
         **********************************/
        String[] detailEvalIds 		= searchMap.getStringArray("detailEvalIds");
        
        // 실행계획이 없는 세부평가내용, 평가착안사항 삭제
        List delConceptList = getList("gov.bos.bosMetric.getBosDetailEvalConcept", searchMap);
        List delDetailItemList = getList("gov.bos.bosMetric.getBosDetailEvalItem", searchMap);
        
        String[] delConcept = new String[delConceptList.size()];
        String[] delDetailItem = new String[delDetailItemList.size()];
        
        String maxConceptId = getStr("gov.bos.bosMetric.getMaxConceptId", searchMap);
        
        for (int i = 0; i < delConceptList.size(); i++) {
        	delConcept[i] = "('" + ((HashMap)delConceptList.get(i)).get("DETAIL_EVAL_ID") + "', '" + ((HashMap)delConceptList.get(i)).get("CONCEPT_ID") + "')";
        }
        
        for (int i = 0; i < delDetailItemList.size(); i++) {
        	delDetailItem[i] = "'" + ((HashMap)delDetailItemList.get(i)).get("DETAIL_EVAL_ID") + "'";
        }
        
        if (delConcept.length > 0) {
        	searchMap.put("delConcept", delConcept);
        	returnMap = deleteData("gov.bos.bosMetric.deleteBosDetailEvalConcept", searchMap, true);
        }
        
        if (delDetailItem.length > 0) {
        	searchMap.put("delDetailItem", delDetailItem);
        	returnMap = deleteData("gov.bos.bosMetric.deleteBosDetailEvalItem", searchMap, true);
        }
        
        if (detailEvalIds != null) {
	    	for (int i = 0; i < detailEvalIds.length; i++) {
	    		String detailEvalId = "";
	    		String detailEvalItemNm = "";
	    		
	    		if (detailEvalIds[i].startsWith("BD")) {
	    			detailEvalId = detailEvalIds[i];
	    			detailEvalItemNm = searchMap.getString("DETAIL_" + detailEvalIds[i]);
	    		} else {
	    			detailEvalId = getStr("gov.bos.bosMetric.getDetailEvalItemId", searchMap);
	    			detailEvalItemNm = searchMap.getString(detailEvalIds[i]);
	    		}
	    		
				String userId = searchMap.getString("USER_" + detailEvalIds[i]);
		        if (userId == null) userId = "";
		        
				searchMap.put("detailEvalId", detailEvalId);
				searchMap.put("detailEvalNm", detailEvalItemNm);
				searchMap.put("sortOrder", i);
				searchMap.put("userId", userId);
				
				// 세부평가내용 저장
				returnMap = insertData("gov.bos.bosMetric.saveBosDetailEvalItem", searchMap, true);
				
				// 평가착안사항 처리
				String[] conceptIds = searchMap.getStringArray("CONCEPT_" + detailEvalIds[i]);
				
				for (int j = 0; j < conceptIds.length;  j++) {
					if (conceptIds[j].split("_").length >= 2) {
						String conceptId = "";
						
						searchMap.put("maxConceptId", maxConceptId);
						
						if (conceptIds[j].split("_").length == 2) {
							conceptId = getStr("gov.bos.bosMetric.getDetailEvalConceptId", searchMap);
						} else if (!conceptIds[j].split("_")[2].startsWith("BC")) {
							conceptId = getStr("gov.bos.bosMetric.getDetailEvalConceptId", searchMap);
						} else {
							conceptId = conceptIds[j].split("_")[2];
						}
						String conceptNm = searchMap.getString(conceptIds[j]);
						
						searchMap.put("conceptId", conceptId);
						searchMap.put("conceptNm", conceptNm);
						searchMap.put("sortOrder", j);
						
						returnMap = insertData("gov.bos.bosMetric.saveBosDetailEvalConcept", searchMap, true);
					}
				}
	    	}
        }
    	
    	return returnMap;
    }
    
    /**
     * 기관장지표 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] bossMetricIds = searchMap.getString("bossMetricIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != bossMetricIds && 0 < bossMetricIds.length) {
		        for (int i = 0; i < bossMetricIds.length; i++) {
		            searchMap.put("bossMetricId", bossMetricIds[i]);
		            returnMap = updateData("gov.bos.bosMetric.deleteData", searchMap);
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
     * 기관장지표 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popMetricDetail(SearchMap searchMap) {
    	HashMap detail = new HashMap();
        
    	if(!"".equals(StaticUtil.nullToBlank((String)searchMap.get("year")))) {
			searchMap.put("findYear", (String)searchMap.get("year"));
		}
    	
    	/**********************************
         * 지표그룹 상세조회 
         **********************************/
    	detail = getDetail("gov.bos.bosMetric.getDetailPop", searchMap);
        searchMap.addList("detail", detail);
        
        /**********************************
         * 산식컬럼 조회 
         **********************************/
        ArrayList calTypeColList = new ArrayList();
        calTypeColList = (ArrayList)getList("gov.bos.bosMetric.calTypeColList", searchMap); 
        searchMap.addList("calTypeColList", calTypeColList);
        
        /**********************************
         * 세부평가내용 조회 
         **********************************/
        ArrayList detailEvalItemList = new ArrayList();
        detailEvalItemList = (ArrayList)getList("gov.bos.bosMetric.detailEvalItemList", searchMap); 
        searchMap.addList("detailEvalItemList", detailEvalItemList);
        
        /**********************************
         * KPI 산식명 조회 
         **********************************/
    	String typeId = (String)detail.get("GBN_ID");
    	
    	if("01".equals(typeId)) {	
	        String actCalTypeNm = (String)detail.get("ACT_CAL_TYPE");
	        
	        if(actCalTypeNm!=null){
		        HashMap<String, String> calTyepColValueMap = new HashMap<String, String>();
		        
		        if(null != calTypeColList && 0 < calTypeColList.size()) {
			        for (int i = 0; i < calTypeColList.size(); i++) {
			        	HashMap<String, String> t = (HashMap<String, String>)calTypeColList.get(i);
						calTyepColValueMap.put((String)t.get("ACT_CAL_TYPE_COL_ID"), (String)t.get("ACT_CAL_TYPE_COL_NM"));
					}
		        }
		        
		        String calTypeColDesc = HtmlHelper.changeCalNmToCalDesc(actCalTypeNm, calTyepColValueMap);
		        
		        searchMap.addList("calTypeColDesc", calTypeColDesc);
	        }
    	}
    	
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
        
		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("weight"), "가중치");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("baseTarget"), "기준치");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("stdev"), "표준편차");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("sortOrder"), "정렬순서");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}


        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }

    /**
     * 기관장지표 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap bosMetricListExcel(SearchMap searchMap) {
    	String excelFileName = "기관장지표";
    	String excelTitle = "기관장지표 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("year")));
    	excelSearchInfoList.add(new ExcelVO("사용여부", (String)searchMap.get("useYnNm")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가범주", "EVAL_CAT_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가지표", "BOSS_METRIC_NM", "left"));
    	excelInfoList.add(new ExcelVO("지표유형", "GBN_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가방법", "EVAL_METHOD_NM", "left"));
    	excelInfoList.add(new ExcelVO("주관부서", "SC_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("담당자", "INSERT_USER_NM", "left"));
    	
    	if(!"".equals(StaticUtil.nullToBlank((String)searchMap.get("useYn")))) {
			searchMap.put("findUseYn", (String)searchMap.get("useYn"));
		}
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("gov.bos.bosMetric.getList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 실행계획 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popActionPlanList(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	HashMap detail = new HashMap();
    	
    	if(!"".equals(StaticUtil.nullToBlank((String)searchMap.get("year")))) {
			searchMap.put("findYear", (String)searchMap.get("year"));
		}
    	
    	searchMap.addList("detail", getDetail("gov.bos.bosMetric.getBossMetric", searchMap));
    	searchMap.addList("itemDetail", getDetail("gov.bos.bosMetric.getDetailEval", searchMap));
    	searchMap.addList("conceptDetail", getDetail("gov.bos.bosMetric.getConcept", searchMap));
    	searchMap.addList("actionPlanList", getList("gov.bos.bosMetric.getActionPlanList", searchMap));

        return searchMap;
    }
}
