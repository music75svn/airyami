/*************************************************************************
* CLASS 명      : MetricAction
* 작 업 자      : 하윤식
* 작 업 일      : 2012년 6월 26일
* 기    능      : 조직별KPI관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    하윤식      2012년 6월 26일             최 초 작 업
**************************************************************************/
package com.lexken.bsc.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.StringConstants;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.util.HtmlHelper;
import com.lexken.framework.util.StaticUtil;
import com.lexken.framework.login.LoginVO;

public class MetricAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 조직별KPI관리 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap metricList(SearchMap searchMap) {

    	/**********************************
         * 로그인정보 (권한 체크)
         **********************************/
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");

    	/************************************************************************************
    	 * 평가조직 트리 조회
    	 ************************************************************************************/
        if(loginVO.chkAuthGrp("01") || loginVO.chkAuthGrp("60")) {  //전체관리자
        	searchMap.addList("treeList", getList("bsc.module.commModule.getScDeptList", searchMap));
        } else {
        	searchMap.addList("treeList", getList("bsc.module.commModule.getScDeptAuthList", searchMap));
        }

        /************************************************************************************
    	 * 지표입력 기한조회
    	 ************************************************************************************/
    	searchMap.addList("kpiClosingYn", getStr("bsc.base.metric.getKpiClosingYn", searchMap));

    	/************************************************************************************
    	 * 지표입력 마감조회
    	 ************************************************************************************/
    	searchMap.addList("kpiInputTermYn", getStr("bsc.base.metric.getKpiInputTermYn", searchMap));

    	/************************************************************************************
    	 * 디폴트 평가조직 조회
    	 ************************************************************************************/
    	String scDeptId = searchMap.getString("scDeptId");
    	searchMap.put("findSearchCodeId", scDeptId);
    	if("".equals(scDeptId)) {
    		searchMap.put("scDeptId", searchMap.getDefaultValue("treeList", "CODE_ID", 0));
    		searchMap.put("scDeptNm", searchMap.getDefaultValue("treeList", "CODE_NM", 0));
    		searchMap.put("findSearchCodeId", searchMap.getDefaultValue("treeList", "CODE_ID", 0));
    	}

        return searchMap;
    }

    /**
     * 조직별KPI 가중치 합계 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap metricWeightSum_ajax(SearchMap searchMap) {

    	searchMap.addList("weightSum", getStr("bsc.base.metric.getDeptWeight", searchMap));

        return searchMap;
    }

    /**
     * 조직별 담당자 및 KPI 승인상태 가져오기
     * @param
     * @return String
     * @throws
     */
    public SearchMap scDeptInfo_ajax(SearchMap searchMap) {

    	searchMap.addList("status", getDetail("bsc.base.metric.getScDeptInfo", searchMap));

        return searchMap;
    }

    /**
     * 조직별KPI관리 일괄수정 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap metricBatchList(SearchMap searchMap) {
    	/**********************************
         * 로그인정보 (권한 체크)
         **********************************/
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");

    	String scDeptId = searchMap.getString("scDeptId");
    	searchMap.put("findSearchCodeId", scDeptId);

    	/**********************************
         * 평가조직 트리 조회
         **********************************/
    	if(loginVO.chkAuthGrp("01") || loginVO.chkAuthGrp("60")) {  //전체관리자
        	searchMap.addList("treeList", getList("bsc.module.commModule.getScDeptList", searchMap));
        } else {
        	searchMap.addList("treeList", getList("bsc.module.commModule.getScDeptAuthList", searchMap));
        }

        return searchMap;
    }

    /**
     * 조직별KPI관리 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap metricList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("bsc.base.metric.getList", searchMap));

        return searchMap;
    }

    /**
     * 조직별KPI관리 상세화면
     * @param
     * @return String
     * @throws
     */
    public SearchMap metricModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");

    	/**********************************
         * 로그인정보 (권한 체크)
         **********************************/
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");

    	String scDeptId = searchMap.getString("scDeptId");
    	searchMap.put("findSearchCodeId", scDeptId);
    	searchMap.put("findScDeptId", scDeptId);

    	/**********************************
         * 조직별 가중치 합계 조회
         **********************************/
    	searchMap.addList("weightSum", getStr("bsc.base.metric.getDeptWeight", searchMap));

    	/**********************************
         * 조직별 지표 승인상태 조회
         **********************************/
    	searchMap.addList("status", getDetail("bsc.base.metric.getScDeptInfo", searchMap));

    	HashMap detail = new HashMap();

    	if("MOD".equals(stMode)) {
	    	/**********************************
	         * 지표 상세조회
	         **********************************/
	    	detail = getDetail("bsc.base.metric.getDetail", searchMap);
	        searchMap.addList("detail", detail);
    	}

    	/**********************************
         * 측정주기월 조회
         **********************************/
        ArrayList regMonList = new ArrayList();
        regMonList = (ArrayList)getList("bsc.base.metric.regMonList", searchMap);
        String[] monArray = new String[0];
        if(null != regMonList && 0 < regMonList.size()) {
        	monArray = new String[regMonList.size()];
        	for (int i = 0; i < regMonList.size(); i++) {
	        	HashMap<String, String> t = (HashMap<String, String>)regMonList.get(i);
				monArray[i] = (String)t.get("MON");
        	}
        }

        searchMap.addList("regMonDesc", monArray);
        
        /***************************************
         * BSC관점 조회
         ***************************************/
        searchMap.addList("perspectiveList", getList("bsc.base.metric.getPerspectiveIdList", searchMap));

        /***************************************
         * 전략과제 조회
         ***************************************/
        searchMap.addList("strategyIdList", getList("bsc.base.metric.getStrategyIdList", searchMap));

        /***************************************
         * CSF 조회
         ***************************************/
        searchMap.addList("csfList", getList("bsc.base.metric.getCsfList", searchMap));

        /**********************************
         * 평가조직 트리 조회
         **********************************/
        if(loginVO.chkAuthGrp("01") || loginVO.chkAuthGrp("60")) {  //전체관리자
        	searchMap.addList("treeList", getList("bsc.module.commModule.getScDeptList", searchMap));
        } else {
        	searchMap.addList("treeList", getList("bsc.module.commModule.getScDeptAuthList", searchMap));
        }

        /**********************************
         * 전략과제 조회
         **********************************/
//        searchMap.addList("strategyList", getList("bsc.base.metric.getStrategyList", searchMap));

        /**********************************
         * CSF 조회
        searchMap.addList("csfList", getList("bsc.base.metricGrp.getCsfList", searchMap));
         **********************************/

        /**********************************
         * 전략과제 조회
         **********************************/
//        searchMap.addList("directionList", getList("bsc.base.metricGrp.getDirectionList", searchMap));

        /**********************************
         * 산식컬럼 조회
         **********************************/
        ArrayList calTypeColList = new ArrayList();
        calTypeColList = (ArrayList)getList("bsc.base.metric.calTypeColList", searchMap);
        searchMap.addList("calTypeColList", calTypeColList);

        /**********************************
         * 득점산식조회
         **********************************/
        searchMap.addList("scoreCalTypeList", getList("bsc.base.metric.scoreCalTypeList", searchMap));

        /**********************************
         * 평가구간대 조회
         **********************************/
        searchMap.addList("evalSectionList", getList("bsc.base.metric.evalSectionList", searchMap));

        /**********************************
         * 평가구간대 등급 조회
         **********************************/
        searchMap.addList("gradeList", getList("bsc.base.metric.gradeList", searchMap));

        if("MOD".equals(stMode)) {
	        /**********************************
	         * 목표 조회
	         **********************************/
	        searchMap.addList("targetList", getList("bsc.base.metric.getTargetList", searchMap));

	        /**********************************
	         * 년목표 조회
	         **********************************/
	        searchMap.addList("targetY", getStr("bsc.base.metric.getTargetY", searchMap));

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

		        String calTypeColDesc = (String)HtmlHelper.changeCalNmToCalDesc(actCalTypeNm, calTyepColValueMap);
		        searchMap.addList("calTypeColDesc", calTypeColDesc);
	        }
        }

        /**********************************
         * 지표입력 기한조회
         **********************************/
    	searchMap.addList("kpiClosingYn", getStr("bsc.base.metric.getKpiClosingYn", searchMap));

    	/**********************************
         * 지표입력 마감조회
         **********************************/
    	searchMap.addList("kpiInputTermYn", getStr("bsc.base.metric.getKpiInputTermYn", searchMap));

        return searchMap;
    }

    /**
     * 조직별KPI관리 등록/수정/삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap metricProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");

        /**********************************
         * 무결성 체크
         **********************************/
        if("ADD".equals(stMode) || "MOD".equals(stMode) || "BATCH".equals(stMode) ) {
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
        } else if("BATCH".equals(stMode)) {  //가중치, 담당자 일괄수정
            searchMap = batchDB(searchMap);
        } else if("STATUS".equals(stMode)) { //지표승인상태
            searchMap = statusDB(searchMap);
        } else if("COPY".equals(stMode)) {   //지표복사
            searchMap = copyDB(searchMap);
        } else if("MOVE".equals(stMode)) {   //지표이동
            searchMap = moveDB(searchMap);
        }

        /**********************************
         * 지표연계 속성 수정
         **********************************/
        //searchMap = metricPropertySet(searchMap);

        /**********************************
         * 조직코드 설정
         **********************************/
        String scDeptId = searchMap.getString("scDeptId");
    	searchMap.put("findSearchCodeId", scDeptId);

        /**********************************
         * Return
         **********************************/
        return searchMap;
    }

    /**
     * 조직별KPI관리 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();
        String metricGrpId = searchMap.getString("metricGrpId");
        String kpiPoolGubun = "MOD";

        /**********************************
         * Parameter setting
         **********************************/
        if("".equals(metricGrpId)) {
        	metricGrpId = getStr("bsc.base.metricGrp.getMetricGrpId", searchMap); //지표풀 코드 채번
        	kpiPoolGubun = "ADD";
            searchMap.put("metricGrpId", metricGrpId);
    	}
        searchMap.put("kpiPoolGubun", kpiPoolGubun);

        try {
        	setStartTransaction();

        	/**********************************
             * 지표코드 채번
             **********************************/
	        String metricId = getStr("bsc.base.metric.getMetricId", searchMap);
	        searchMap.put("metricId", metricId);

	        /**********************************
             * 지표 입력
             **********************************/
        	returnMap = insertData("bsc.base.metric.insertData", searchMap);

        	/**********************************
	         * 측정주기 Parameter setting
	         **********************************/
	        String[] actMons = searchMap.getString("actMons").split("\\|", 0);
	        String typeId = searchMap.getString("typeId");

	        /**********************************
	         * 측정주기 삭제 & 등록
	         **********************************/
	        //if("01".equals(typeId)) { //정량지표
		        returnMap = updateData("bsc.base.metric.deleteRegMon", searchMap, true);

		        if(actMons!=null && !actMons.equals("")){
			        for (int i = 0; i < actMons.length; i++) {
			        	searchMap.put("mon", actMons[i]);
			        	returnMap = insertData("bsc.base.metric.insertRegMon", searchMap);
			        }
		        }
	        //}

        	/**********************************
             * 실적산식 등록
             **********************************/
	        returnMap = insertCalTypeCol(searchMap);

	        /**********************************
             * 구간대 등록
             **********************************/
	        returnMap = insertEvalSection(searchMap);

	        /**********************************
             * 지표 목표 등록
             *********************************
	        returnMap = insertTarget(searchMap);*/

	        /**********************************
             * 권한설정
             **********************************/
	        returnMap = insertAdmin(searchMap);



	        /**********************************
             * 지표Pool 정보 수정
             *********************************
	        returnMap = kpiPoolModify(searchMap);*/

	        /**********************************
             * 목표/실적 공유 걸린 지표 수정
             **********************************/
	        returnMap = insertData("bsc.base.metric.execTamShareMetric", searchMap);

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
     * 조직별KPI관리 수정
     * @param
     * @return String
     * @throws
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
	        setStartTransaction();

	        /**********************************
	         * KPI 수정
	         **********************************/
	        returnMap = updateData("bsc.base.metric.updateData", searchMap);

	        /**********************************
	         * 측정주기 Parameter setting
	         **********************************/
	        String[] actMons = searchMap.getString("actMons").split("\\|", 0);
	        String typeId = searchMap.getString("typeId");

	        /**********************************
	         * 측정주기 삭제 & 등록
	         **********************************/
	        if("01".equals(typeId)) { //정량지표
		        returnMap = updateData("bsc.base.metric.deleteRegMon", searchMap, true);

		        if(actMons!=null && !actMons.equals("")){
			        for (int i = 0; i < actMons.length; i++) {
			        	searchMap.put("mon", actMons[i]);
			        	returnMap = insertData("bsc.base.metric.insertRegMon", searchMap);
			        }
		        }
	        }

	        /**********************************
	         * 실적산식 등록
	         **********************************/
	        returnMap = insertCalTypeCol(searchMap);

	        /**********************************
	         * 구간대 등록
	         **********************************/
	        returnMap = insertEvalSection(searchMap);

	        /**********************************
	         * 지표 목표 등록
	         *********************************
	        returnMap = insertTarget(searchMap);*/

	        /**********************************
	         * 권한설정
	         **********************************/
	        returnMap = insertAdmin(searchMap);

	        /**********************************
	         * 지표Pool 정보 수정
	         *********************************
	        returnMap = kpiPoolModify(searchMap);*/

	        /**********************************
	         * 목표/실적 공유 걸린 지표 수정
	         **********************************/
	        returnMap = insertData("bsc.base.metric.execTamShareMetric", searchMap);

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
     * 실적산식 등록
     * @param
     * @return String
     * @throws
     */
    public HashMap insertCalTypeCol(SearchMap searchMap) {
        HashMap returnMap = new HashMap();

        /**********************************
         * Parameter setting
         **********************************/
        String typeId = searchMap.getString("typeId");

        String[] calTypeCols = searchMap.getStringArray("calTypeCols");
        String[] calTypeColNms = searchMap.getStringArray("calTypeColNms");
        String[] insertGubuns = searchMap.getStringArray("insertGubuns");
        String[] units = searchMap.getStringArray("units");
        String[] itemCds = searchMap.getStringArray("itemCds");
        String[] sourceSystems = searchMap.getStringArray("sourceSystems");

        try {
        	/**********************************
             * 실적산식 삭제
             **********************************/
	        returnMap = updateData("bsc.base.metric.deleteCalTypeCol", searchMap, true);

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
			            returnMap = insertData("bsc.base.metric.insertCalTypeCol", searchMap);
			        }
	        	}
	        }

        } catch (Exception e) {
        	logger.error(e.toString());
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        }
        return returnMap;
    }

    /**
     * 구간대등록
     * @param
     * @return String
     * @throws
     */
    public HashMap insertEvalSection(SearchMap searchMap) {
        HashMap returnMap = new HashMap();

        /**********************************
         * Parameter setting
         **********************************/
        String typeId = searchMap.getString("typeId");
        String scoreCalTypeGubun = searchMap.getString("scoreCalTypeGubun");

        /**********************************
         * 구간대
         **********************************/
        String[] evalSectionIds = searchMap.getStringArray("evalSectionIds");
        String[] fromValues = searchMap.getStringArray("fromValues");
        String[] toValues = searchMap.getStringArray("toValues");
        String[] conversionScores = searchMap.getStringArray("conversionScores");

        try {
        	/**********************************
             * 구간대 삭제
             **********************************/
	        returnMap = updateData("bsc.base.metric.deleteEvalSection", searchMap, true);

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
				            returnMap = insertData("bsc.base.metric.insertEvalSection", searchMap);
				        }
	        		}
	        	}
	        }
        } catch (Exception e) {
        	logger.error(e.toString());
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        }
        return returnMap;
    }

    /**
     * 목표등록
     * @param
     * @return String
     * @throws
     */
    public HashMap insertTarget(SearchMap searchMap) {
        HashMap returnMap = new HashMap();

        /**********************************
         * Parameter setting
         **********************************/
        String typeId = searchMap.getString("typeId");

        String[] tgtMons = searchMap.getStringArray("tgtMons");     //목표 기준월
        String[] tgtValues = searchMap.getStringArray("tgtValues"); //목표값

        try {
        	/**********************************
             * 목표, 기준월 삭제
             **********************************/
	        returnMap = updateData("bsc.base.metric.deleteTarget", searchMap, true);       //목표삭제
	        returnMap = updateData("bsc.base.metric.deleteActRegDefMon", searchMap, true); //기준월삭제

	        /**********************************
             * 목표, 기준월 등록
             **********************************/
	        if("01".equals(typeId)) {
	        	if(tgtMons != null && 0 < tgtMons.length) {
			        for (int i = 0; i < tgtMons.length; i++) {
			            searchMap.put("mon", tgtMons[i]);
			            searchMap.put("tgtValue", tgtValues[i]);
			            returnMap = insertData("bsc.base.metric.insertTarget", searchMap);        //목표등록
			            returnMap = insertData("bsc.base.metric.insertActRegDefMon", searchMap);  //기준월등록
			            returnMap = insertData("bsc.base.metric.insertTargetHistory", searchMap); //목표 history 등록
			        }
	        	}
	        }

	        /**********************************
             * 목표 롤업
             **********************************/
	        returnMap = insertData("bsc.base.metric.execTargetTimeRollup", searchMap);

        } catch (Exception e) {
        	logger.error(e.toString());
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        }
        return returnMap;
    }

    /**
     * 지표권한등록
     * 04 : KPI담당자(실적입력자)
     * 05 : 실적승인자
     * @param
     * @return String
     * @throws
     */
    public HashMap insertAdmin(SearchMap searchMap) {
        HashMap returnMap = new HashMap();

        try {
        	/**********************************
             * 기존 권한삭제
             **********************************/
	        returnMap = updateData("bsc.base.metric.deleteAdmin", searchMap, true);

	        /**********************************
             * 실적입력자 입력
             **********************************/
	        returnMap = insertData("bsc.base.metric.insertInsertUserAdmin", searchMap);

	        /**********************************
             * 실적승인자 입력
             **********************************/
	        returnMap = insertData("bsc.base.metric.insertApproveUserAdmin", searchMap);

        } catch (Exception e) {
        	logger.error(e.toString());
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        }
        return returnMap;
    }

    /**
     * 지표Pool 등록 및 수정
     * @param
     * @return String
     * @throws
     */
    public HashMap kpiPoolModify(SearchMap searchMap) {
        HashMap returnMap = new HashMap();

        /**********************************
         * Parameter setting
         **********************************/
        String scDeptId = searchMap.getString("scDeptId");
        String metricNm = searchMap.getString("metricNm");
        String typeId = searchMap.getString("typeId");
        String scoreCalTypeGubun = searchMap.getString("scoreCalTypeGubun");
        String kpiPoolGubun = searchMap.getString("kpiPoolGubun");

        searchMap.put("chargeScDeptId", scDeptId);
        searchMap.put("metricGrpNm", metricNm);

        /**********************************
         * 실적산식
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

        try {
        	/**********************************
             * 지표Pool 등록 및 수정
             **********************************/
        	if("ADD".equals(kpiPoolGubun)) {
        		returnMap = insertData("bsc.base.metricGrp.insertData", searchMap);  //지표Pool 등록
        	} else {
        		returnMap = updateData("bsc.base.metricGrp.updateData", searchMap);  //지표Pool 수정
        	}

        	/******************************************
             * 지표 신규 입력일때 지표풀에 산식등록
             ******************************************/
        	if("ADD".equals(kpiPoolGubun)) {

        		/******************************************
                 * 실적산식 삭제
                 ******************************************/
		        returnMap = updateData("bsc.base.metricGrp.deleteCalTypeCol", searchMap, true);

		        /******************************************
                 * 실적산식 등록
                 ******************************************/
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

        	/******************************************
             * 구간대 삭제
             ******************************************/
	        returnMap = updateData("bsc.base.metricGrp.deleteEvalSection", searchMap, true);

	        /******************************************
             * 구간대 등록
             ******************************************/
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

        } catch (Exception e) {
        	logger.error(e.toString());
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        }

        return returnMap;
    }

    /**
     * 조직별KPI관리 삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        HashMap returnMap = new HashMap();

        try {
	        String metricIds = searchMap.getString("metricIds");
	        String[] keyArray = metricIds.split("\\|", 0);

	        setStartTransaction();

	        if(null != keyArray && 0 < keyArray.length) {
		        for (int i=0; i<keyArray.length; i++) {
		            searchMap.put("metricId", keyArray[i]);
		            returnMap = updateData("bsc.base.metric.deleteData", searchMap);
		        }
	        }

	        /******************************************
             * 권한설정
             ******************************************/
	        returnMap = insertAdmin(searchMap);

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
     * 조직별KPI관리 일괄수정
     * @param
     * @return String
     * @throws
     */
    public SearchMap batchDB(SearchMap searchMap) {
        HashMap returnMap = new HashMap();

        /**********************************
         * Parameter setting
         **********************************/
        String[] metricIds = searchMap.getStringArray("metricIds");
        String[] insertUserIds = searchMap.getStringArray("insertUserIds");
        String[] approveUserIds = searchMap.getStringArray("approveUserIds");
        //String[] sortOrders = searchMap.getStringArray("sortOrders");
        String[] weights = searchMap.getStringArray("weights");

        try {
	        setStartTransaction();

	        if(null != metricIds && 0 < metricIds.length) {
		        for (int i = 0; i < metricIds.length; i++) {
		            searchMap.put("metricId", metricIds[i]);
		            searchMap.put("insertUserId", insertUserIds[i]);
		            searchMap.put("approveUserId", approveUserIds[i]);
		            //searchMap.put("sortOrder", sortOrders[i]);
		            searchMap.put("sortOrder", i + 1);
		            searchMap.put("weight", weights[i]);

		            returnMap = updateData("bsc.base.metric.updateBatchData", searchMap);

		        	/**********************************
		             * 목표 입력(측정주기월 분리)
		             **********************************/
		            ArrayList regMonList = new ArrayList();
		            regMonList = (ArrayList)getList("bsc.base.metric.regMonList", searchMap);
		            String[] monArray = new String[0];
		            if(null != regMonList && 0 < regMonList.size()) {
		            	monArray = new String[regMonList.size()];
		            	for (int k = 0; k < regMonList.size(); k++) {
		    	        	HashMap<String, String> t = (HashMap<String, String>)regMonList.get(k);
		    	        	searchMap.put("mon", (String)t.get("MON"));

				            returnMap = updateData("bsc.base.metric.insertAllTarget", searchMap, true);
		            	}
		            }

		        }
	        }

	        /******************************************
             * 권한설정
             ******************************************/
	        returnMap = insertAdmin(searchMap);

	        /**********************************
	         * 지표 목표 등록
	         *********************************
	        returnMap = insertTarget(searchMap);	*/

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
     * 조직별KPI관리 승인상태 관리
     * @param
     * @return String
     * @throws
     */
    public SearchMap statusDB(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String status = searchMap.getString("status");
        String scDeptId = searchMap.getString("scDeptId");
    	searchMap.put("findSearchCodeId", scDeptId);

        try {
	        setStartTransaction();

	        /**********************************
	         * 데이터 등록여부 확인
	         **********************************/
	        int cnt = getInt("bsc.base.metric.getStatusCount", searchMap);

	        if(0 < cnt) {
	        	returnMap = updateData("bsc.base.metric.updateStatusData", searchMap);
	        } else {
	            returnMap = insertData("bsc.base.metric.insertStatusData", searchMap);
	        }

	        /**********************************
	         * 반려일 경우 반려사유 입력
	         **********************************/
	        if("05".equals(status)) {
	        	returnMap = insertData("bsc.base.metric.insertReturnCauseStatusData", searchMap);
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
     * KPI 복사
     * @param
     * @return String
     * @throws
     */
    public SearchMap copyDB(SearchMap searchMap) {
        HashMap returnMap = new HashMap();

        /**********************************
         * Parameter setting
         **********************************/
        String metricIds = searchMap.getString("metricIds");
        String targetScDeptIds = searchMap.getString("targetScDeptIds");

        String[] metricIdArray = metricIds.split("\\|", 0);
        String[] targetScDeptIdArray = targetScDeptIds.split("\\|", 0);

        try {
	        setStartTransaction();

	        if(null != metricIdArray && 0 < metricIdArray.length) {
		        for (int i = 0; i < metricIdArray.length; i++) {
		            searchMap.put("metricId", metricIdArray[i]);

		            if(null != targetScDeptIdArray && 0 < targetScDeptIdArray.length) {
			            for (int j = 0; j < targetScDeptIdArray.length; j++) {
			            	searchMap.put("targetScDeptId", targetScDeptIdArray[j]);
			            	returnMap = insertData("bsc.base.metric.execMetricCopy", searchMap);
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
     * KPI 이동
     * @param
     * @return String
     * @throws
     */
    public SearchMap moveDB(SearchMap searchMap) {
        HashMap returnMap = new HashMap();

        /**********************************
         * Parameter setting
         **********************************/
        String metricIds = searchMap.getString("metricIds");
        String targetScDeptId = searchMap.getString("targetScDeptId");

        String[] metricIdArray = metricIds.split("\\|", 0);

        try {
	        setStartTransaction();

	        if(null != metricIdArray && 0 < metricIdArray.length) {
		        for (int i = 0; i < metricIdArray.length; i++) {
		            searchMap.put("metricId", metricIdArray[i]);
		            returnMap = insertData("bsc.base.metric.moveMetric", searchMap);
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
     * 지표POOL 데이터 조회후 셋팅(AJAX) - 기본정보
     * @param
     * @return String
     * @throws
     */
    public SearchMap metricGrpData_ajax(SearchMap searchMap) {

        searchMap.addList("list", getList("bsc.base.metric.getMetricGrpDataAjax", searchMap));

        searchMap.addList("listMon", getList("bsc.base.metric.regGrpMonList", searchMap));

        return searchMap;
    }

    /**
     * 지표POOL 데이터 조회후 셋팅(AJAX) - 산식컬럼
     * @param
     * @return String
     * @throws
     */
    public SearchMap metricGrpCalTypeCol_ajax(SearchMap searchMap) {

        searchMap.addList("list", getList("bsc.base.metric.getMetricGrpCalTypeColAjax", searchMap));

        return searchMap;
    }

    /**
     * 지표POOL 데이터 조회후 셋팅(AJAX) - 구간대
     * @param
     * @return String
     * @throws
     */
    public SearchMap metricGrpEvalSectionGrp_ajax(SearchMap searchMap) {

        searchMap.addList("list", getList("bsc.base.metric.getMetricGrpEvalSectionGrpAjax", searchMap));

        return searchMap;
    }

    /**
     * 상위KPI 데이터 조회후 셋팅(AJAX) - 기본정보
     * @param
     * @return String
     * @throws
     */
    public SearchMap upMetricData_ajax(SearchMap searchMap) {

        searchMap.addList("list", getList("bsc.base.metric.getDetail", searchMap));

        searchMap.addList("listMon", getList("bsc.base.metric.regMonList", searchMap));

        return searchMap;
    }

    /**
     * 상위KPI 데이터 조회후 셋팅(AJAX) - 산식컬럼
     * @param
     * @return String
     * @throws
     */
    public SearchMap upMetricCalTypeCol_ajax(SearchMap searchMap) {

        searchMap.addList("list", getList("bsc.base.metric.calTypeColList", searchMap));

        return searchMap;
    }

    /**
     * 상위KPI 데이터 조회후 셋팅(AJAX) - 구간대
     * @param
     * @return String
     * @throws
     */
    public SearchMap upMetricEvalSection_ajax(SearchMap searchMap) {

        searchMap.addList("list", getList("bsc.base.metric.evalSectionList", searchMap));

        return searchMap;
    }

    /**
     * 상위KPI 데이터 조회후 셋팅(AJAX) - 목표
     * @param
     * @return String
     * @throws
     */
    public SearchMap upMetricTarget_ajax(SearchMap searchMap) {

        searchMap.addList("list", getList("bsc.base.metric.getTargetList", searchMap));

        return searchMap;
    }

    /**
     * 목표조회 팝업
     * @param
     * @return String
     * @throws
     */
    public SearchMap popTargetInsert(SearchMap searchMap) {

    	String metricId = searchMap.getString("metricId");

    	if(!"".equals(metricId)) {
        	searchMap.addList("targetList", getList("bsc.base.metric.getTargetList", searchMap));
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
        String[] weights = null;
        float weightsSum = 0;

        if("BATCH".equals(searchMap.getString("mode"))) {
        	weights = searchMap.getStringArray("weights");

        	if(weights != null) {
        		for( int i = 0; i < weights.length; i++) {
        			if(!"".equals(weights[i])) {
        				weightsSum += Float.parseFloat(weights[i]);
        			}
        		}

        		if(weightsSum != 100){
        			returnMap.put("ErrorNumber",  -1);
            		returnMap.put("ErrorMessage", "가중치의 합은 100이여야 합니다.");

                	return returnMap;
        		}
        	}

        } else {

	        returnMap = ValidationChk.lengthCheck(searchMap.getString("metricNm"), "지표Pool명", 1, 300);
	        if((Integer)returnMap.get("ErrorNumber") < 0 ){
	        	return returnMap;
	        }

	        returnMap = ValidationChk.lengthCheck(searchMap.getString("description"), "설명", 0, 300);
	        if((Integer)returnMap.get("ErrorNumber") < 0 ){
	        	return returnMap;
	        }

	        returnMap = ValidationChk.selEmptyCheck(searchMap.getString("tgtInsertGubun"), "목표입력구분");
	        if((Integer)returnMap.get("ErrorNumber") < 0 ){
	        	return returnMap;
	        }

	        if("01".equals(searchMap.getString("tgtInsertGubun")) || "07".equals(searchMap.getString("tgtInsertGubun"))){
	        	returnMap = ValidationChk.selEmptyCheck(searchMap.getString("itemCd"), "연계항목");
	            if((Integer)returnMap.get("ErrorNumber") < 0 ){
	            	returnMap.put("ErrorNumber",  (Integer)returnMap.get("ErrorNumber"));
	        		returnMap.put("ErrorMessage", "목표입력구분이 시스템 또는 엑셀일 경우에는 연계항목을 입력해야 합니다.");

	            	return returnMap;
	            }
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
        }
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;
    }

    /**
     * 지표 공통/고유 설정
     * @param
     * @return String
     * @throws
     */
    public SearchMap metricPropertySet(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String status = searchMap.getString("status");
        String scDeptId = searchMap.getString("scDeptId");
    	searchMap.put("findSearchCodeId", scDeptId);

        try {
	        setStartTransaction();

	        /**********************************
	         * 지표개수 확인
	         **********************************/
	        int cnt = getInt("bsc.base.metric.getMetricPoolCount", searchMap);

	        /**********************************
	         * 지표연계 속성 수정
	         **********************************/
	        if(1 < cnt) {
	        	returnMap = updateData("bsc.base.metric.updateMetricGrpProperty", searchMap);
	        	returnMap = updateData("bsc.base.metric.updateMetricProperty", searchMap);
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
     * 지표Pool관리 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap metricListExcel(SearchMap searchMap) {
    	String excelFileName = StringConstants.METRIC_NM;
    	String excelTitle = StringConstants.METRIC_NM + "리스트";

    	if("".equals(StaticUtil.nullToBlank((String)searchMap.get("findYear")))) {
			searchMap.put("findYear", (String)searchMap.get("year"));
		}

    	if("".equals(StaticUtil.nullToBlank((String)searchMap.get("findScDeptId")))) {
			searchMap.put("findScDeptId", (String)searchMap.get("scDeptId"));
		}

    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO(StringConstants.METRIC_NM +"명", StaticUtil.nullToDefault((String)searchMap.get("metricNm"), "전체")));
    	excelSearchInfoList.add(new ExcelVO("사용여부", (String)searchMap.get("useYnNm")));

    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO(StringConstants.SC_DEPT_NM, "SC_DEPT_NM", "left", "CNT", 6000));
    	excelInfoList.add(new ExcelVO(StringConstants.PERSPECTIVE_NM, "PERSPECTIVE_NM", "left", "CNT", 6000));
    	excelInfoList.add(new ExcelVO(StringConstants.STRATEGY_NM, "STRATEGY_NM", "left", "CNT", 8000));
    	excelInfoList.add(new ExcelVO(StringConstants.METRIC_NM + "코드", "METRIC_ID", "center", "CNT", 4000));
    	excelInfoList.add(new ExcelVO(StringConstants.METRIC_NM + "명", "METRIC_NM", "left", "CNT", 8500));
    	excelInfoList.add(new ExcelVO(StringConstants.METRIC_GRP_NM + "명", "METRIC_GRP_NM", "left", "CNT", 8500));
    	excelInfoList.add(new ExcelVO("단위", "UNIT_NM", "center", "CNT"));
    	excelInfoList.add(new ExcelVO("주기", "EVAL_CYCLE_NM", "center", "CNT"));
    	excelInfoList.add(new ExcelVO(StringConstants.WEIGHT, "WEIGHT", "center", "CNT"));
    	excelInfoList.add(new ExcelVO("실적담당자", "INSERT_USER_NM", "center", "CNT"));
    	excelInfoList.add(new ExcelVO("실적승인자", "APPROVE_USER_NM", "center", "CNT"));
    	excelInfoList.add(new ExcelVO("평가유형", "TYPE_NM", "center", "CNT"));
    	excelInfoList.add(new ExcelVO("실적집계방법", "TIME_ROLLUP_NM", "center", "CNT", 5000));
    	excelInfoList.add(new ExcelVO(StringConstants.METRIC_NM + "산식", "ACT_CAL_TYPE", "left", "CNT", 6000));
    	excelInfoList.add(new ExcelVO("득점산식구분", "SCORE_CAL_TYPE_GUBUN_NM", "center", "CNT"));
    	excelInfoList.add(new ExcelVO("득점산식", "CAL_TYPE_NM", "left", "CNT", 8000));
    	excelInfoList.add(new ExcelVO("수기여부", "CNT_YN", "center", "CNT"));

    	//excelInfoList.add(new ExcelVO("상위" + StringConstants.METRIC_NM, "UP_METRIC_NM", "left", "CNT", 8500));
    	//excelInfoList.add(new ExcelVO("목표/실적 공유여부", "TAM_SHARE_YN_NM", "center", "CNT"));
    	//excelInfoList.add(new ExcelVO(StringConstants.METRIC_NM + "유형", "BSC_METRIC_GBN_NM", "center", "CNT"));
    	//excelInfoList.add(new ExcelVO("목표입력구분", "TGT_INSERT_GUBUN_NM", "center", "CNT"));
    	//excelInfoList.add(new ExcelVO("연계항목", "TGT_ITEM_NM", "center", "CNT", 5000));
    	//excelInfoList.add(new ExcelVO(StringConstants.METRIC_NM + "목적", "KPI_PURPOSE", "left", "CNT", 10000));
    	//excelInfoList.add(new ExcelVO(StringConstants.METRIC_NM + "상세정의", "CONTENT", "left", "CNT", 10000));
    	//excelInfoList.add(new ExcelVO("산식컬럼명", "CAL_TYPE_COL_NM", "left"));
    	//excelInfoList.add(new ExcelVO("산식컬럼", "CAL_TYPE_COL", "center"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.base.metric.getExcelList", searchMap));

        return searchMap;
    }


}
