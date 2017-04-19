/*************************************************************************
* CLASS 명      : MetricGrpAction
* 작 업 자      : 하윤식
* 작 업 일      : 2012년 6월 13일
* 기    능      : 지표Pool관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    하윤식      지표Pool관리            최 초 작 업
**************************************************************************/
package com.lexken.bsc.base;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.StringConstants;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.util.HtmlHelper;
import com.lexken.framework.util.StaticUtil;

public class MetricGrpAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 지표Pool관리 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap metricGrpList(SearchMap searchMap) {

        return searchMap;
    }

    /**
     * 지표Pool관리 json 데이터 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap metricGrpList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("bsc.base.metricGrp.getList", searchMap));

        return searchMap;
    }

    /**
     * 지표Pool 사용 조직 팝업
     * @param
     * @return String
     * @throws
     */
    public SearchMap popMetricCopy(SearchMap searchMap) {

    	searchMap.addList("treeList", getList("bsc.module.commModule.getScDeptList", searchMap));

    	searchMap.addList("useScDeptList", getList("bsc.base.metricGrp.getUseScDeptList", searchMap));

        return searchMap;
    }

    /**
     * 지표Pool 사용중인 지표 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap metricGrpUseList(SearchMap searchMap) {

    	/************************************************************************************
    	 * 디폴트 지표Pool 조회
    	 ************************************************************************************/
    	/*
    	String findMetricGrpId = searchMap.getString("findMetricGrpId");

    	if("".equals(findMetricGrpId)) {
    		searchMap.put("findMetricGrpId", searchMap.getString("metricGrpId"));
    	}
    	*/
    	searchMap.put("findMetricGrpId", searchMap.getString("metricGrpId"));

        return searchMap;
    }

    /**
     * 지표Pool 사용중인 지표 조회 json 데이터 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap metricGrpUseList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("bsc.base.metricGrp.getUseList", searchMap));

        return searchMap;
    }


    /**
     * 지표Pool관리 상세화면
     * @param
     * @return String
     * @throws
     */
    public SearchMap metricGrpModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");

    	HashMap detail = new HashMap();

    	if("MOD".equals(stMode)) {
	    	/**********************************
	         * 지표그룹 상세조회
	         **********************************/
	    	detail = getDetail("bsc.base.metricGrp.getDetail", searchMap);
	        searchMap.addList("detail", detail);

	        /**********************************
	         * 관리조직 조회
	         **********************************/
	        searchMap.addList("chargeList", getList("bsc.base.metricGrp.chargeList", searchMap));
	    }

    	/**********************************
         * 측정주기월 조회
         **********************************/
        ArrayList regMonList = new ArrayList();
        regMonList = (ArrayList)getList("bsc.base.metricGrp.regMonList", searchMap);
        String[] monArray = new String[0];
        if(null != regMonList && 0 < regMonList.size()) {
        	monArray = new String[regMonList.size()];
        	for (int i = 0; i < regMonList.size(); i++) {
	        	HashMap<String, String> t = (HashMap<String, String>)regMonList.get(i);
				monArray[i] = (String)t.get("MON");
        	}
        }

        searchMap.addList("regMonDesc", monArray);

        /**********************************
         * 산식컬럼 조회
         **********************************/
        ArrayList calTypeColList = new ArrayList();
        calTypeColList = (ArrayList)getList("bsc.base.metricGrp.calTypeColList", searchMap);
        searchMap.addList("calTypeColList", calTypeColList);

        /**********************************
         * 득점산식조회
         **********************************/
        searchMap.addList("scoreCalTypeList", getList("bsc.base.metricGrp.scoreCalTypeList", searchMap));

        /**********************************
         * 평가구간대 조회
         **********************************/
        searchMap.addList("evalSectionList", getList("bsc.base.metricGrp.evalSectionList", searchMap));

        /**********************************
         * 평가구간대 등급 조회
         **********************************/
        searchMap.addList("gradeList", getList("bsc.base.metricGrp.gradeList", searchMap));

        /**********************************
         * 전략과제 조회
         **********************************/
//        searchMap.addList("strategyList", getList("bsc.base.metricGrp.getStrategyList", searchMap));

        /**********************************
         * CSF 조회
         **********************************/
        searchMap.addList("csfList", getList("bsc.base.metricGrp.getCsfList", searchMap));

        /**********************************
         * 전략과제 조회
         **********************************/
//        searchMap.addList("directionList", getList("bsc.base.metricGrp.getDirectionList", searchMap));


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
     * 지표Pool관리 등록/수정/삭제
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
        if(!"DEL".equals(stMode) && !"DISTRIBUTE".equals(stMode)) {
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
        } else if("DISTRIBUTE".equals(stMode)) {
            searchMap = distributeDB(searchMap);
        }

        /**********************************
         * Return
         **********************************/
        return searchMap;
    }

    /**
     * 지표배포 기능
     * @param
     * @return String
     * @throws
     */
    public SearchMap distributeDB(SearchMap searchMap) {
        HashMap returnMap = new HashMap();

        try {
	        String[] scDeptIds = searchMap.getString("scDeptIds").split("\\|", 0);

	        setStartTransaction();

	        returnMap = deleteData("bsc.base.metricGrp.deleteDistributeData", searchMap, true);

	        if( !"".equals(scDeptIds[0]) && scDeptIds[0] != null) {

	        	for (int i = 0; i < scDeptIds.length; i++) {
	        		searchMap.put("scDeptId", scDeptIds[i]);

	        		int cnt = getInt("bsc.base.metricGrp.getDeptCount", searchMap);

	        		if(cnt == 0) {
	        			returnMap = insertData("bsc.base.metricGrp.distributeData", searchMap);
	        		}else if(cnt > 0){
	        			returnMap = updateData("bsc.base.metricGrp.updateDistributeData", searchMap);
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
     * 지표Pool관리 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap insertDB(SearchMap searchMap) {
    	HashMap returnMap    = new HashMap();
    	String typeId = searchMap.getString("typeId");
    	String scoreCalTypeGubun = searchMap.getString("scoreCalTypeGubun");

    	/**********************************
         * 실적산식 Parameter setting
         **********************************/
        String[] calTypeCols = searchMap.getStringArray("calTypeCols");
        String[] calTypeColNms = searchMap.getStringArray("calTypeColNms");
        String[] insertGubuns = searchMap.getStringArray("insertGubuns");
        String[] units = searchMap.getStringArray("units");
        String[] itemCds = searchMap.getStringArray("itemCds");
        String[] sourceSystems = searchMap.getStringArray("sourceSystems");

        /**********************************
         * 구간대 Parameter setting
         **********************************/
        String[] evalSectionIds = searchMap.getStringArray("evalSectionIds");
        String[] fromValues = searchMap.getStringArray("fromValues");
        String[] toValues = searchMap.getStringArray("toValues");
        String[] conversionScores = searchMap.getStringArray("conversionScores");

        /**********************************
         * 관리조직 Parameter setting
         **********************************/
        String[] chargeScDeptIds = searchMap.getStringArray("chargeScDeptId");

        /**********************************
         * 측정주기 Parameter setting
         **********************************/
        String[] actMons = searchMap.getString("actMons").split("\\|", 0);

        try {
	        setStartTransaction();

	        /**********************************
	         * 지표풀 코드 채번
	         **********************************/
	        String metricGrpId = getStr("bsc.base.metricGrp.getMetricGrpId", searchMap);
	        searchMap.put("metricGrpId", metricGrpId);

	        /**********************************
	         * 지표풀 등록
	         **********************************/
	        returnMap = insertData("bsc.base.metricGrp.insertData", searchMap);

	        /**********************************
	         * 관리조직 삭제 & 등록
	         **********************************/
	        returnMap = updateData("bsc.base.metricGrp.deleteCharge", searchMap, true);

	        for (int i = 0; i < chargeScDeptIds.length; i++) {
	        	if(!chargeScDeptIds[i].equals("")){
	        		searchMap.put("chargeScDeptId", chargeScDeptIds[i]);
	        		returnMap = insertData("bsc.base.metricGrp.insertCharge", searchMap);
	        	}
	        }

	        /**********************************
	         * 측정주기 삭제 & 등록
	         **********************************/
	        //if("01".equals(typeId)) { //정량지표
		        returnMap = updateData("bsc.base.metricGrp.deleteRegMon", searchMap, true);

		        if(actMons!=null){
			        for (int i = 0; i < actMons.length; i++) {
			        	searchMap.put("mon", actMons[i]);
			        	returnMap = insertData("bsc.base.metricGrp.insertRegMon", searchMap);
			        }
		        }
	        //}

	        /**********************************
	         * 실적산식 삭제
	         **********************************/
	        returnMap = updateData("bsc.base.metricGrp.deleteCalTypeCol", searchMap, true);

	        /**********************************
	         * 실적산식 등록
	         **********************************/
	        if("01".equals(typeId)) { //정량지표
		        for (int i = 0; i < calTypeCols.length; i++) {
		            searchMap.put("calTypeCol", calTypeCols[i]);
		            searchMap.put("calTypeColNm", calTypeColNms[i]);
		            searchMap.put("insertGubun", insertGubuns[i]);
		            searchMap.put("unit", units[i]);
		            searchMap.put("itemCd", itemCds[i]);
		            searchMap.put("sourceSystem", sourceSystems[i]);
		            returnMap = insertData("bsc.base.metricGrp.insertCalTypeCol", searchMap);
		        }
	        }

	        /**********************************
	         * 구간대 삭제
	         **********************************/
	        returnMap = updateData("bsc.base.metricGrp.deleteEvalSection", searchMap, true);

	        /**********************************
	         * 구간대 등록
	         **********************************/
	        if("01".equals(typeId)) { //정량지표
	        	if("02".equals(scoreCalTypeGubun) || "03".equals(scoreCalTypeGubun) || "04".equals(scoreCalTypeGubun)) {
			        for (int i = 0; i < evalSectionIds.length; i++) {
			            searchMap.put("evalSectionId", evalSectionIds[i]);
			            searchMap.put("fromValue", fromValues[i]);
			            searchMap.put("toValue", toValues[i]);
			            searchMap.put("conversionScore", conversionScores[i]);
			            returnMap = insertData("bsc.base.metricGrp.insertEvalSection", searchMap);
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
     * 지표Pool관리 수정
     * @param
     * @return String
     * @throws
     */
    public SearchMap updateDB(SearchMap searchMap) {
    	HashMap returnMap    = new HashMap();
        String typeId = searchMap.getString("typeId");
        String scoreCalTypeGubun = searchMap.getString("scoreCalTypeGubun");
        String actCalType = searchMap.getString("actCalType");
        String orgActCalType = searchMap.getString("orgActCalType");

        /**********************************
         * 실적산식 Parameter setting
         **********************************/
        String[] calTypeCols = searchMap.getStringArray("calTypeCols");
        String[] calTypeColNms = searchMap.getStringArray("calTypeColNms");
        String[] insertGubuns = searchMap.getStringArray("insertGubuns");
        String[] units = searchMap.getStringArray("units");
        String[] itemCds = searchMap.getStringArray("itemCds");
        String[] sourceSystems = searchMap.getStringArray("sourceSystems");

        /**********************************
         * 구간대
         **********************************/
        String[] evalSectionIds = searchMap.getStringArray("evalSectionIds");
        String[] fromValues = searchMap.getStringArray("fromValues");
        String[] toValues = searchMap.getStringArray("toValues");
        String[] conversionScores = searchMap.getStringArray("conversionScores");

        /**********************************
         * 관리조직 Parameter setting
         **********************************/
        String[] chargeScDeptIds = searchMap.getStringArray("chargeScDeptId");

        /**********************************
         * 측정주기 Parameter setting
         **********************************/
        String[] actMons = searchMap.getString("actMons").split("\\|", 0);

        try {
	        setStartTransaction();

	        /**********************************
	         * 지표풀 수정
	         **********************************/
	        returnMap = updateData("bsc.base.metricGrp.updateData", searchMap);

	        /**********************************
	         * 관리조직 삭제 & 등록
	         **********************************/
	        returnMap = updateData("bsc.base.metricGrp.deleteCharge", searchMap, true);

	        for (int i = 0; i < chargeScDeptIds.length; i++) {
	        	if(!chargeScDeptIds[i].equals("")){
	        		searchMap.put("chargeScDeptId", chargeScDeptIds[i]);
	        		returnMap = insertData("bsc.base.metricGrp.insertCharge", searchMap);
	        	}
	        }

	        /**********************************
	         * 측정주기 삭제 & 등록
	         **********************************/
	        returnMap = updateData("bsc.base.metricGrp.deleteRegMon", searchMap, true);

	        if("01".equals(typeId) || "02".equals(typeId)) { //계량, 비계량
		        if(actMons!=null){
			        for (int i = 0; i < actMons.length; i++) {
			        	searchMap.put("mon", actMons[i]);
			        	returnMap = insertData("bsc.base.metricGrp.insertRegMon", searchMap);
			        }
			        
			        updateData("bsc.base.metricGrp.deleteRegMonMetric", searchMap, true);
			        
			        insertData("bsc.base.metricGrp.insertRegMonMetric", searchMap, true);
			        
		        }
	        }

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
				            searchMap.put("itemCd", itemCds[i]);
				            searchMap.put("sourceSystem", sourceSystems[i]);
				            returnMap = insertData("bsc.base.metricGrp.updateCalTypeCol", searchMap);
				        }
		        	}
		        }
	        } else {
	        	/**********************************
		         * 실적산식 삭제
		         **********************************/
		        returnMap = updateData("bsc.base.metricGrp.deleteCalTypeCol", searchMap, true);

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
				            searchMap.put("itemCd", itemCds[i]);
				            searchMap.put("sourceSystem", sourceSystems[i]);
				            returnMap = insertData("bsc.base.metricGrp.insertCalTypeCol", searchMap);
				        }
		        	}
		        }
	        }

	        /**********************************
	         * 구간대 삭제
	         **********************************/
	        returnMap = updateData("bsc.base.metricGrp.deleteEvalSection", searchMap, true);

	        /**********************************
	         * 구간대 등록
	         **********************************/
	        if("01".equals(typeId)) {
	        	if("02".equals(scoreCalTypeGubun) || "03".equals(scoreCalTypeGubun) || "04".equals(scoreCalTypeGubun)) {
	        		if(null != evalSectionIds && 0 < evalSectionIds.length) {
				        for (int i = 0; i < evalSectionIds.length; i++) {
				            searchMap.put("evalSectionId", evalSectionIds[i]);
				            searchMap.put("fromValue", fromValues[i]);
				            searchMap.put("toValue", toValues[i]);
				            searchMap.put("conversionScore", conversionScores[i]);
				            returnMap = insertData("bsc.base.metricGrp.insertEvalSection", searchMap);
				        }
	        		}
	        	}
	        }

	        // 동일 지표 하위 사용여부 확인

	        int cnt = getInt("bsc.base.metricGrp.getMetricCount", searchMap);

	        if(0 < cnt){
		        /********************************************
		         * 동일 지표POOL을 가진 지표 기본정보 update
		         ********************************************/
		        returnMap = updateData("bsc.base.metricGrp.updateMetricData", searchMap, true);

		        /********************************************
		         * 연계항목 수정시 하위 지표 연계항목 수정
		         ********************************************/
		        returnMap = updateData("bsc.base.metricGrp.updateCalTypeColItemData", searchMap, true);

		        /************************************
		         * 실적산식이 변경이 되었는지 체크
		         ************************************
		        if(actCalType.equals(orgActCalType)) {
		        	/************************************
			         * 지표산식 수정(only 산식명만 수정)
			         ***********************************
			        returnMap = updateData("bsc.base.metricGrp.updateMetricDataOnlyName", searchMap, true);
		        } else {*/
		        	/************************************
			         * 지표산식 수정
			         ************************************/
			        returnMap = updateData("bsc.base.metricGrp.deleteMetricCalTypeColData", searchMap, true);
			        returnMap = updateData("bsc.base.metricGrp.insertMetricCalTypeColData", searchMap, true);
		        //}

		        /************************************
		         * 지표 구간대 수정
		         ************************************/

		        int cnt2 = getInt("bsc.base.metricGrp.getMetricCount2", searchMap);
		        if(cnt2 > 0){
			        returnMap = updateData("bsc.base.metricGrp.deleteMetricEvalSectionData", searchMap, true);
			        returnMap = updateData("bsc.base.metricGrp.insertMetricEvalSectionData", searchMap, true);
		        }
	        }//if

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
	        String metricGrpIds = searchMap.getString("metricGrpIds");
	        String[] keyArray = metricGrpIds.split("\\|", 0);

	        setStartTransaction();

	        for (int i=0; i<keyArray.length; i++) {
	            searchMap.put("metricGrpId", keyArray[i]);
	            returnMap = updateData("bsc.base.metricGrp.deleteData", searchMap);
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
        returnMap = ValidationChk.lengthCheck(searchMap.getString("metricGrpNm"), "지표Pool명", 1, 300);
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
        	return returnMap;
        }

        returnMap = ValidationChk.lengthCheck(searchMap.getString("description"), "설명", 0, 300);
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
        	return returnMap;
        }

        returnMap = ValidationChk.selEmptyCheck(searchMap.getString("evalCycle"), "주기");
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
        	return returnMap;
        }

        returnMap = ValidationChk.selEmptyCheck(searchMap.getString("timeRollup"), "기간합산");
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
        	return returnMap;
        }

        if( "01".equals(searchMap.getString("typeId"))){
        	returnMap = ValidationChk.selEmptyCheck(searchMap.getString("unit"), "단위");
            if((Integer)returnMap.get("ErrorNumber") < 0 ){
            	return returnMap;
            }
        }

        /*returnMap = ValidationChk.selEmptyCheck(searchMap.getString("strategyId"), "전략과제");
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
        	return returnMap;
        }*/

        String[] actMon = searchMap.getStringArray("actMon");
        if( actMon == null) {
        	returnMap.put("ErrorNumber",  -1);
    		returnMap.put("ErrorMessage", "측정주기를 선택하여 주십시오.");

        	return returnMap;

        }

        returnMap = ValidationChk.lengthCheck(searchMap.getString("kpiPurpose"), "지표정의", 0, 3000);
        if((Integer)returnMap.get("ErrorNumber") < 0 ){
        	return returnMap;
        }

        if( "01".equals(searchMap.getString("typeId"))){
        	returnMap = ValidationChk.lengthCheck(searchMap.getString("actCalType"), "실적산식", 1, 500);
            if((Integer)returnMap.get("ErrorNumber") < 0 ){
            	return returnMap;
            }
        }

        /**********************************
         * 실적산식 Parameter setting
         **********************************/
        String[] calTypeCols   = searchMap.getStringArray("calTypeCols");
        String[] calTypeColNms = searchMap.getStringArray("calTypeColNms");
        String[] insertGubuns  = searchMap.getStringArray("insertGubuns");
        String[] units		   = searchMap.getStringArray("units");
        String[] itemCds 	   = searchMap.getStringArray("itemCds");
        String[] sourceSystems = searchMap.getStringArray("sourceSystems");

    	if( "01".equals(searchMap.getString("typeId"))) {
    		for( int i = 0; i<calTypeCols.length; i++) {
    			returnMap = ValidationChk.lengthCheck(calTypeColNms[i], "산식항목명", 1, 300);
                if((Integer)returnMap.get("ErrorNumber") < 0 ){
                	return returnMap;
                }

                returnMap = ValidationChk.lengthCheck(insertGubuns[i], "실적입력구분", 1, 300);
                if((Integer)returnMap.get("ErrorNumber") < 0 ){
                	return returnMap;
                }

                returnMap = ValidationChk.lengthCheck(units[i], "단위", 1, 7);
                if((Integer)returnMap.get("ErrorNumber") < 0 ){
                	return returnMap;
                }

                returnMap = ValidationChk.lengthCheck(itemCds[i], "연계항목", 0, 8);
                if((Integer)returnMap.get("ErrorNumber") < 0 ){
                	return returnMap;
                }

                returnMap = ValidationChk.lengthCheck(sourceSystems[i], "원천데이터", 0, 200);
                if((Integer)returnMap.get("ErrorNumber") < 0 ){
                	return returnMap;
                }

    		}
    	}

    	if("01".equals(searchMap.get("typeId"))) {
    		if( "01".equals(searchMap.get("scoreCalTypeGubun")) || "03".equals(searchMap.get("scoreCalTypeGubun")) ) {
    			returnMap = ValidationChk.emptyCheck(searchMap.getString("scoreCalTypeId"), "득점산식");
    			if((Integer)returnMap.get("ErrorNumber") < 0 ){
    				return returnMap;
    			}
    		}
    	}


        /**********************************
         * 등급구간 Parameter setting
         **********************************/
        String[] fromValues 		= searchMap.getStringArray("fromValues");
        String[] toValues   		= searchMap.getStringArray("toValues");
        String[] conversionScores   = searchMap.getStringArray("conversionScores");

        if( "02".equals(searchMap.get("scoreCalTypeGubun")) || "03".equals(searchMap.get("scoreCalTypeGubun")) || "04".equals(searchMap.get("scoreCalTypeGubun")) ) {
        	for( int i = 0; i < fromValues.length; i++){

        		returnMap = ValidationChk.emptyCheck(fromValues[i], "점수구간");
                if((Integer)returnMap.get("ErrorNumber") < 0 ){
                	return returnMap;
                }

                returnMap = ValidationChk.emptyCheck(toValues[i], "점수구간");
                if((Integer)returnMap.get("ErrorNumber") < 0 ){
                	return returnMap;
                }

            	if( Float.parseFloat(fromValues[i]) >= Float.parseFloat(toValues[i]) ){
            		returnMap.put("ErrorNumber",  -1);
            		returnMap.put("ErrorMessage", "상/하향 구간 점수가 잘못 되었습니다.");

                	return returnMap;
            	}

                returnMap = ValidationChk.emptyCheck(conversionScores[i], "환산점수");
                if((Integer)returnMap.get("ErrorNumber") < 0 ){
                	return returnMap;
                }

                returnMap = ValidationChk.emptyCheck(conversionScores[i], "환산점수");
                if((Integer)returnMap.get("ErrorNumber") < 0 ){
                	return returnMap;
                }

        	}

        }

        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;
    }

    /**
     * 지표Pool관리 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap metricGrpListExcel(SearchMap searchMap) {
    	String excelFileName = StringConstants.METRIC_GRP_NM;
    	String excelTitle = StringConstants.METRIC_GRP_NM + "리스트";

    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO(StringConstants.METRIC_GRP_NM +"명", StaticUtil.nullToDefault((String)searchMap.get("metricGrpNm"), "전체")));
    	excelSearchInfoList.add(new ExcelVO("사용여부", (String)searchMap.get("useYnNm")));

    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO(StringConstants.METRIC_GRP_NM + "코드", "METRIC_GRP_ID", "center", "CNT"));
    	excelInfoList.add(new ExcelVO(StringConstants.METRIC_GRP_NM + "명", "METRIC_GRP_NM", "left", "CNT", 8000));
    	excelInfoList.add(new ExcelVO("평가유형", "TYPE_NM", "center", "CNT"));
    	excelInfoList.add(new ExcelVO("연계유형", "METRIC_PROPERTY_NM", "center", "CNT"));
    	excelInfoList.add(new ExcelVO("단위", "UNIT_NM", "center", "CNT"));
    	excelInfoList.add(new ExcelVO("주기", "EVAL_CYCLE_NM", "center", "CNT"));
    	excelInfoList.add(new ExcelVO("기간합산", "TIME_ROLLUP_NM", "center", "CNT", 3500));
    	excelInfoList.add(new ExcelVO(StringConstants.METRIC_NM + "산식", "ACT_CAL_TYPE", "center", "CNT", 6000));
    	excelInfoList.add(new ExcelVO("산식컬럼", "CAL_TYPE_COL", "center"));
    	excelInfoList.add(new ExcelVO("산식컬럼명", "CAL_TYPE_COL_NM", "left"));
    	excelInfoList.add(new ExcelVO("단위", "CAL_TYPE_COL_UNIT_NM", "center"));
    	excelInfoList.add(new ExcelVO("입력구분", "INSERT_GUBUN_NM", "center"));
    	excelInfoList.add(new ExcelVO("원천시스템", "SOURCE_SYSTEM", "left"));
    	excelInfoList.add(new ExcelVO("연계항목명", "ITEM_NM", "left"));
    	excelInfoList.add(new ExcelVO("득점산식구분", "SCORE_CAL_TYPE_GUBUN_NM", "center", "CNT"));
    	excelInfoList.add(new ExcelVO("득점산식", "CAL_TYPE_NM", "left", "CNT", 6000));
    	excelInfoList.add(new ExcelVO(StringConstants.METRIC_NM + "목적", "KPI_PURPOSE", "left", "CNT", 10000));
    	excelInfoList.add(new ExcelVO(StringConstants.METRIC_NM + "상세정의", "CONTENT", "left", "CNT", 10000));
    	excelInfoList.add(new ExcelVO("사용개수", "COUNT", "center", "CNT"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.base.metricGrp.getExcelList", searchMap));

        return searchMap;
    }

}
