/*************************************************************************
* CLASS 명      : GovMetricAction
* 작 업 자      : 박경태
* 작 업 일      : 2012년 11월 1일 
* 기    능      : 정평지표관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    박경태      2012년 11월 1일         최 초 작 업 
**************************************************************************/
package com.lexken.gov.system;
    
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

public class GovMetricAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 정평지표관리 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap govMetricList(SearchMap searchMap) {

        return searchMap;
    }
    
    /**
     * 정평지표관리 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap govMetricList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("gov.system.govMetric.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가범주 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap getCatList_ajax(SearchMap searchMap) {
    	
    	searchMap.addList("govCatList", getList("gov.system.evalCat.getCatList", searchMap));

        return searchMap;
    }
    
    /**
     * 정평지표관리 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap govMetricModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	HashMap detail = new HashMap();
    	
    	if(!"".equals(StaticUtil.nullToBlank((String)searchMap.get("year")))) {
			searchMap.put("findYear", (String)searchMap.get("year"));
		}
    	/**********************************
         * 정평지표POOL 조회
         **********************************/
    	searchMap.put("findUseYn","Y");
    	searchMap.addList("govMetricGrpList", getList("gov.system.govMetricGrp.getList", searchMap));
    	
    	/**********************************
         * 대표지표 조회
         **********************************/
    	searchMap.addList("mainMetricList", getList("gov.system.govMetric.getMainList", searchMap));
    	
    	if("MOD".equals(stMode)) {
    		/**********************************
	         * 지표그룹 상세조회 
	         **********************************/
	    	detail = getDetail("gov.system.govMetric.getDetail", searchMap);
	        searchMap.addList("detail", detail);
    		
    		/**********************************
             * 산식컬럼 조회 
             **********************************/
            ArrayList calTypeColList = new ArrayList();
            calTypeColList = (ArrayList)getList("gov.system.govMetric.calTypeColList", searchMap); 
            searchMap.addList("calTypeColList", calTypeColList);
            
            
            /**********************************
             * 세부평가내용 조회 
             **********************************/
            ArrayList detailEvalItemList = new ArrayList();
            detailEvalItemList = (ArrayList)getList("gov.system.govMetric.detailEvalItemList", searchMap); 
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
     * 정평지표관리 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap govMetricProcess(SearchMap searchMap) {
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
        if ("ADD".equals(stMode)) {
            searchMap = insertDB(searchMap);
        } else if ("MOD".equals(stMode)) {
            searchMap = updateDB(searchMap);
        } else if ("DEL".equals(stMode)) {
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
        	String gbnId = searchMap.getString("gbnId");
        	
        	/**********************************
             * 실적산식 Parameter setting
             **********************************/
            String[] calTypeCols = searchMap.getStringArray("calTypeCols");
            String[] calTypeColNms = searchMap.getStringArray("calTypeColNms");
            String[] insertGubuns = searchMap.getStringArray("insertGubuns");
            String[] units = searchMap.getStringArray("units");
            String[] itemCds = searchMap.getStringArray("itemCds");
            String[] timeRollups = searchMap.getStringArray("timeRollups");
            String[] itemRollups = searchMap.getStringArray("itemRollups");
            
        	setStartTransaction();
        	
        	/**********************************
	         * 지표 코드 채번 
	         **********************************/
	        String govMetricId = getStr("gov.system.govMetric.getMetricId", searchMap);
	        searchMap.put("govMetricId", govMetricId);
	        
	        /**********************************
	         * 평가범주, 평가부문 받아오기
	         **********************************/
	        List list = getList("gov.system.govMetric.getEvalCatList", searchMap);
            
            for (int i = 0 ; i < list.size(); i++) {
                String evalCatGrpId = (String)((HashMap)list.get(i)).get("EVAL_CAT_GRP_ID");
                String evalCatId = (String)((HashMap)list.get(i)).get("EVAL_CAT_ID");
                searchMap.put("evalCatGrpId",evalCatGrpId);
                searchMap.put("evalCatId",evalCatId);
            }
        
        	returnMap = insertData("gov.system.govMetric.insertData", searchMap);
        	
      
        	/**********************************
	         * 실적산식 등록 
	         **********************************/
	        if("01".equals(gbnId)) { //계량지표 
	        	/**********************************
		         * 실적산식 삭제 
		         **********************************/
		        returnMap = updateData("gov.system.govMetric.deleteCalTypeCol", searchMap, true);
		        
		        for (int i = 0; i < calTypeCols.length; i++) {
		            searchMap.put("calTypeCol", calTypeCols[i]);
		            searchMap.put("calTypeColNm", calTypeColNms[i]);
		            searchMap.put("insertGubun", insertGubuns[i]);
		            searchMap.put("unit", units[i]);
		            searchMap.put("itemCd", itemCds[i]);
		            searchMap.put("timeRollup", timeRollups[i]);
		            searchMap.put("itemRollup", itemRollups[i]);
		            returnMap = insertData("gov.system.govMetric.insertCalTypeCol", searchMap, true);
		        }
		        
		        /**********************************
		         * 계량실적 입력자 권한설정 
		         **********************************/
		        searchMap.put("adminGubun", "11");
		        returnMap = updateData("gov.system.govMetric.deleteAdminData", searchMap, true);//권한삭제
		        returnMap = insertData("gov.system.govMetric.insertAdminData", searchMap);//권한등록
	        }
	        
	        /**********************************
	         * 세부평가내용 등록 
	         **********************************/
	        if("02".equals(gbnId)) { //비계량지표
	        	returnMap = saveDetailEvalItem(searchMap);
	        	
	        	/**********************************
		         * 비계량세부평가 담당자 권한설정
		         **********************************/
		        searchMap.put("adminGubun", "13");
		        returnMap = updateData("gov.system.govMetric.deleteAdminData", searchMap, true);//권한삭제
		        returnMap = insertData("gov.system.govMetric.insertAdminData", searchMap);//권한등록
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
        	String gbnId = searchMap.getString("gbnId");
        	
        	/**********************************
             * 실적산식 Parameter setting
             **********************************/
            String[] calTypeCols = searchMap.getStringArray("calTypeCols");
            String[] calTypeColNms = searchMap.getStringArray("calTypeColNms");
            String[] insertGubuns = searchMap.getStringArray("insertGubuns");            
            String[] units = searchMap.getStringArray("units");
            String[] itemCds = searchMap.getStringArray("itemCds");
            String[] timeRollups = searchMap.getStringArray("timeRollups");
            String[] itemRollups = searchMap.getStringArray("itemRollups");
            
        	setStartTransaction();
        	
        	/**********************************
	         * 평가범주, 평가부문 받아오기
	         **********************************/
	        List list = getList("gov.system.govMetric.getEvalCatList", searchMap);
            
            for (int i = 0 ; i < list.size(); i++) {
                String evalCatGrpId = (String)((HashMap)list.get(i)).get("EVAL_CAT_GRP_ID");
                String evalCatId = (String)((HashMap)list.get(i)).get("EVAL_CAT_ID");
                searchMap.put("evalCatGrpId",evalCatGrpId);
                searchMap.put("evalCatId",evalCatId);
            }
	        
	        returnMap = updateData("gov.system.govMetric.updateData", searchMap);
	        
	        
	        /**********************************
	         * 실적산식 삭제 
	         **********************************/
	        returnMap = updateData("gov.system.govMetric.deleteCalTypeCol", searchMap, true);
	        
	        
	        /**********************************
	         * 실적산식 등록 
	         **********************************/
	        if ("01".equals(gbnId)) { //계량지표 
	        	
		        for (int i = 0; i < calTypeCols.length; i++) {
		            searchMap.put("calTypeCol", calTypeCols[i]);
		            searchMap.put("calTypeColNm", calTypeColNms[i]);
		            searchMap.put("insertGubun", insertGubuns[i]);
		            searchMap.put("unit", units[i]);
		            searchMap.put("itemCd", itemCds[i]);
		            searchMap.put("timeRollup", timeRollups[i]);
		            searchMap.put("itemRollup", itemRollups[i]);
		            returnMap = insertData("gov.system.govMetric.insertCalTypeCol", searchMap);
		        }
		        
		        /**********************************
		         * 계량실적 입력자 권한설정 
		         **********************************/
		        searchMap.put("adminGubun", "11");
		        returnMap = updateData("gov.system.govMetric.deleteAdminData", searchMap, true);//권한삭제
		        returnMap = insertData("gov.system.govMetric.insertAdminData", searchMap);//권한등록
	        }
	        
	        /**********************************
	         * 세부평가내용 등록 
	         **********************************/
	        if ("02".equals(gbnId)) { //비계량지표
	        	returnMap = saveDetailEvalItem(searchMap);
	        		        	
	        	/**********************************
		         * 비계량세부평가 담당자 권한설정
		         **********************************/
		        searchMap.put("adminGubun", "13");
		        returnMap = updateData("gov.system.govMetric.deleteAdminData", searchMap, true);//권한삭제
		        returnMap = insertData("gov.system.govMetric.insertAdminData", searchMap);//권한등록
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
        List delConceptList = getList("gov.system.govMetric.getGovDetailEvalConcept", searchMap);
        List delDetailItemList = getList("gov.system.govMetric.getGovDetailEvalItem", searchMap);
        
        String[] delConcept = new String[delConceptList.size()];
        String[] delDetailItem = new String[delDetailItemList.size()];
        
        String maxConceptId = getStr("gov.system.govMetric.getMaxConceptId", searchMap);
        
        for (int i = 0; i < delConceptList.size(); i++) {
        	delConcept[i] = "('" + ((HashMap)delConceptList.get(i)).get("DETAIL_EVAL_ID") + "', '" + ((HashMap)delConceptList.get(i)).get("CONCEPT_ID") + "')";
        }
        
        for (int i = 0; i < delDetailItemList.size(); i++) {
        	delDetailItem[i] = "'" + ((HashMap)delDetailItemList.get(i)).get("DETAIL_EVAL_ID") + "'";
        }
        
        if (delConcept.length > 0) {
        	searchMap.put("delConcept", delConcept);
        	returnMap = deleteData("gov.system.govMetric.deleteGovDetailEvalConcept", searchMap, true);
        }
        
        if (delDetailItem.length > 0) {
        	searchMap.put("delDetailItem", delDetailItem);
        	returnMap = deleteData("gov.system.govMetric.deleteGovDetailEvalItem", searchMap, true);
        }
        
        if (detailEvalIds != null) {
	    	for (int i = 0; i < detailEvalIds.length; i++) {
	    		String detailEvalId = "";
	    		String detailEvalItemNm = "";
	    		
	    		if (detailEvalIds[i].startsWith("GD")) {
	    			detailEvalId = detailEvalIds[i];
	    			detailEvalItemNm = searchMap.getString("DETAIL_" + detailEvalIds[i]);
	    		} else {
	    			detailEvalId = getStr("gov.system.govMetric.getDetailEvalItemId", searchMap);
	    			detailEvalItemNm = searchMap.getString(detailEvalIds[i]);
	    		}
	    		
				String userId = searchMap.getString("USER_" + detailEvalIds[i]);
		        if (userId == null) userId = "";
		        
				searchMap.put("detailEvalId", detailEvalId);
				searchMap.put("detailEvalNm", detailEvalItemNm);
				searchMap.put("sortOrder", i);
				searchMap.put("userId", userId);
				
				// 세부평가내용 저장
				returnMap = insertData("gov.system.govMetric.saveGovDetailEvalItem", searchMap, true);
				
				// 평가착안사항 처리
				String[] conceptIds = searchMap.getStringArray("CONCEPT_" + detailEvalIds[i]);
				
				for (int j = 0; j < conceptIds.length;  j++) {
					if (conceptIds[j].split("_").length >= 2) {
						String conceptId = "";
						
						searchMap.put("maxConceptId", maxConceptId);
						
						if (conceptIds[j].split("_").length == 2) {
							conceptId = getStr("gov.system.govMetric.getDetailEvalConceptId", searchMap);
						} else if (!conceptIds[j].split("_")[2].startsWith("GC")) {
							conceptId = getStr("gov.system.govMetric.getDetailEvalConceptId", searchMap);
						} else {
							conceptId = conceptIds[j].split("_")[2];
						}
						String conceptNm = searchMap.getString(conceptIds[j]);
						
						searchMap.put("conceptId", conceptId);
						searchMap.put("conceptNm", conceptNm);
						searchMap.put("sortOrder", j);
						
						returnMap = insertData("gov.system.govMetric.saveGovDetailEvalConcept", searchMap, true);
					}
				}
	    	}
        }
    	
    	return returnMap;
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
	        String[] govMetricIds = searchMap.getString("govMetricIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != govMetricIds && 0 < govMetricIds.length) {
		        for (int i = 0; i < govMetricIds.length; i++) {
		            searchMap.put("govMetricId", govMetricIds[i]);
		            returnMap = updateData("gov.system.govMetric.deleteData", searchMap);
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
     * 정평지표관리 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popArchiveMonList(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	HashMap detail = new HashMap();
    	
    	if(!"".equals(StaticUtil.nullToBlank((String)searchMap.get("year")))) {
			searchMap.put("findYear", (String)searchMap.get("year"));
		}
    	
    	
		/**********************************
         * 지표정의서 상세조회 
         **********************************/
    	detail = getDetail("gov.system.govMetric.getMetricDefinePop", searchMap);
        searchMap.addList("detail", detail);
		
		/**********************************
         * 산식컬럼 조회 
         **********************************/
        ArrayList calTypeColList = new ArrayList();
        calTypeColList = (ArrayList)getList("gov.system.govMetric.calTypeColList", searchMap); 
        searchMap.addList("calTypeColList", calTypeColList);
        
        
        /**********************************
         * 세부평가내용 조회 
         **********************************/
        ArrayList detailEvalItemList = new ArrayList();
        detailEvalItemList = (ArrayList)getList("gov.system.govMetric.detailEvalItemList", searchMap); 
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
    	
    	
    	
    	String mainMetricYn = (String)detail.get("MAIN_METRIC_YN");
    	if("Y".equals(mainMetricYn)) {	
	    	/**********************************
	         * 세부지표 조회 
	         **********************************/
	        ArrayList subMetricList = new ArrayList();
	        subMetricList = (ArrayList)getList("gov.system.govMetric.getMetricDefinePopSubMetricList", searchMap); 
	        searchMap.addList("subMetricList", subMetricList);
    	}
    	
    	if("02".equals(typeId)) {	
    		/*비계량성과목표 데이터 조회(xml)*/
    		searchMap.addList("measList", getList("gov.system.govMetric.getMeasList", searchMap));
    		searchMap.addList("nonMeasList", getList("gov.system.govMetric.getNonMeasList", searchMap));
    		
    		/** 비계량지적및조치사항 데이터 조회(xml)*/
    		searchMap.addList("actlist", getList("gov.system.govMetric.getActList", searchMap));
    		searchMap.addList("govlist", getList("gov.system.govMetric.getGovList", searchMap));
    		searchMap.addList("maslist", getList("gov.system.govMetric.getMasList", searchMap));
    		
    		/* 전년도실적비교 데이터 조회(xml)*/
    		searchMap.addList("comparelist", getList("gov.system.govMetric.getCompareList", searchMap));
    	}
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
    	
    	searchMap.addList("detail", getDetail("gov.system.govMetric.getGovMetric", searchMap));
    	searchMap.addList("itemDetail", getDetail("gov.system.govMetric.getDetailEval", searchMap));
    	searchMap.addList("conceptDetail", getDetail("gov.system.govMetric.getConcept", searchMap));
    	searchMap.addList("actionPlanList", getList("gov.system.govMetric.getActionPlanList", searchMap));

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
        
		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("baseValue"), "기준치");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("bestTarget"), "최고목표");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("lowestTarget"), "최저목표");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("stadConst"), "표준편차");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("sortOrder"), "정렬순서");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("weight"), "가중치");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}


        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }

    /**
     * 정평지표관리 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap govMetricListExcel(SearchMap searchMap) {
    	String excelFileName = "정평지표관리";
    	String excelTitle = "정평지표관리 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("year")));
    	excelSearchInfoList.add(new ExcelVO("사용여부", (String)searchMap.get("useYnNm")));
    	/*
    	excelSearchInfoList.add(new ExcelVO("공통코드명", StaticUtil.nullToDefault((String)searchMap.get("codeGrpNm"), "전체")));
    	
    	*/
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가범주", "EVAL_CAT_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가부문", "EVAL_CAT_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가지표", "GOV_METRIC_NM", "left"));
    	excelInfoList.add(new ExcelVO("지표유형", "GBN_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가방법", "EVAL_METHOD_NM", "left"));
    	excelInfoList.add(new ExcelVO("지표방향성", "DIREC_NM", "left"));
    	excelInfoList.add(new ExcelVO("주관부서", "SC_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("담당자", "INSERT_USER_NM", "left"));
    	excelInfoList.add(new ExcelVO("연계지표", "MAPPED_CNT", "left"));
    	
    	if(!"".equals(StaticUtil.nullToBlank((String)searchMap.get("useYn")))) {
			searchMap.put("findUseYn", (String)searchMap.get("useYn"));
		}
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("gov.system.govMetric.getList", searchMap));
    	
        return searchMap;
    }
    
}
