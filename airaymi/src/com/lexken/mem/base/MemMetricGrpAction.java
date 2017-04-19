/*************************************************************************
* CLASS 명      : memMetricGrpAction
* 작 업 자      : 하윤식
* 작 업 일      : 2012년 6월 13일
* 기    능      : 지표Pool관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    하윤식      지표Pool관리            최 초 작 업
**************************************************************************/
package com.lexken.mem.base;

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

public class MemMetricGrpAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 지표Pool관리 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap memMetricGrpList(SearchMap searchMap) {

        return searchMap;
    }

    /**
     * 지표Pool관리 json 데이터 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap memMetricGrpList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("mem.base.memMetricGrp.getList", searchMap));

        return searchMap;
    }

    /**
     * 지표Pool 사용 조직 팝업
     * @param
     * @return String
     * @throws
     */
    public SearchMap popMetricCopy(SearchMap searchMap) {

    	searchMap.addList("treeList", getList("mem.module.commModule.getScDeptList", searchMap));

    	searchMap.addList("useScDeptList", getList("mem.base.memMetricGrp.getUseScDeptList", searchMap));

        return searchMap;
    }

    /**
     * 지표Pool 사용중인 지표 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap memMetricGrpUseList(SearchMap searchMap) {

    	/************************************************************************************
    	 * 디폴트 지표Pool 조회
    	 ************************************************************************************/
    	/*
    	String findmemMetricGrpId = searchMap.getString("findmemMetricGrpId");

    	if("".equals(findmemMetricGrpId)) {
    		searchMap.put("findmemMetricGrpId", searchMap.getString("memMetricGrpId"));
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
    public SearchMap memMetricGrpUseList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("mem.base.memMetricGrp.getUseList", searchMap));

        return searchMap;
    }
    

    /**
     * 지표Pool관리 상세화면
     * @param
     * @return String
     * @throws
     */
    public SearchMap memMetricGrpModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");

    	HashMap detail = new HashMap();
    	HashMap detailTarget = new HashMap();

    	if("MOD".equals(stMode)) {
	    	/**********************************
	         * 지표그룹 상세조회
	         **********************************/
	    	detail = getDetail("mem.base.memMetricGrp.getDetail", searchMap);
	        searchMap.addList("detail", detail);
	        
	        detailTarget = getDetail("mem.base.memMetricGrp.getDetailTarget", searchMap);
	        searchMap.addList("detailTarget", detailTarget);
	        
	    }

    	/**********************************
         * 측정주기월 조회
         **********************************/
        ArrayList regMonList = new ArrayList();
        regMonList = (ArrayList)getList("mem.base.memMetricGrp.regMonList", searchMap);
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
        calTypeColList = (ArrayList)getList("mem.base.memMetricGrp.calTypeColList", searchMap);
        searchMap.addList("calTypeColList", calTypeColList);

        /**********************************
         * 득점산식조회
         **********************************/
        searchMap.addList("scoreCalTypeList", getList("mem.base.memMetricGrp.scoreCalTypeList", searchMap));

        /**********************************
         * 평가구간대 조회
         **********************************/
        searchMap.addList("evalSectionList", getList("mem.base.memMetricGrp.evalSectionList", searchMap));

        /**********************************
         * 평가구간대 등급 조회
         **********************************/
        searchMap.addList("gradeList", getList("mem.base.memMetricGrp.gradeList", searchMap));

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
     * 직원개인평가(평가대상제외자) 상세화면
     * @param
     * @return String
     * @throws
     */
    public SearchMap memMetricGrpUseModify(SearchMap searchMap) {
        String stMode = searchMap.getString("mode");

     // 최상위 평가조직 조회
    	HashMap topScDept = (HashMap)getDetail("mem.base.memMetricGrp.getTopDeptInfo", searchMap);

    	// 파라미터가 null로 들어올 때 디폴트값 셋팅.
    	if (topScDept == null) {
    		topScDept = new HashMap();
    		topScDept.put("DEPT_CD", "");
    		topScDept.put("DEPT_KOR_NM", "");
    	}

    	// 파라미터가 null로 들어올 때 디폴트값 셋팅.
    	String findDeptCd =  StaticUtil.nullToDefault((String)searchMap.getString("findDeptCd"), (String)topScDept.get("DEPT_CD"));	// 조직코드가 없으면 전사조직코드를 셋팅.
    	String findUpDeptName =  StaticUtil.nullToDefault((String)searchMap.getString("findUpDeptName"), (String)topScDept.get("DEPT_KOR_NM")) ; ;	// 조직명이 없으면 전사조직명을 셋팅.

    	// 디폴트 조회조건 설정
    	searchMap.put("findDeptCd", findDeptCd);	// 검색버튼 조회시 조직트리에 기존 선택한 조직이 표시되도록 설정.(findSearchCodeId에 넣어주면 우선 적용됨. 트리와 검색조건을 별도로 사용할 경우에 한함.)
    	searchMap.put("findUpDeptName", findUpDeptName);

    	searchMap.addList("deptTree", getList("mem.base.memMetricGrp.getDeptList", searchMap)); //인사조직


        searchMap.addList("metricUserList", getList("mem.base.memMetricGrp.getMetricUserList", searchMap));
        

        return searchMap;
    }

    /**
     * 직원개인평가(평가대상제외자) 상세화면
     * @param
     * @return String
     * @throws
     */
    public SearchMap memMetricGrpUserInfo_ajax(SearchMap searchMap) {
    	 searchMap.addList("userList", getList("mem.base.memMetricGrp.selectUserList", searchMap));
    	return searchMap;
    }
    
    
    
    /**
     * 지표POOL 데이터 조회후 셋팅(AJAX) - 기본정보
     * @param
     * @return String
     * @throws
     */
    public SearchMap memDetailEvalGbnData_ajax(SearchMap searchMap) {

        searchMap.addList("list", getList("mem.base.memMetricGrp.getMemDetailEvalGbnData", searchMap));

        return searchMap;
    }
    
    /**
     * 지표POOL 데이터 조회후 셋팅(AJAX) - 구간대
     * @param
     * @return String
     * @throws
     */
    public SearchMap memMetricGrpEvalSectionGrp_ajax(SearchMap searchMap) {

        searchMap.addList("list", getList("mem.base.memMetricGrp.getMetricGrpEvalSectionGrpAjax", searchMap));

        return searchMap;
    }

    /**
     * 지표Pool관리 등록/수정/삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap memMetricGrpProcess(SearchMap searchMap) {
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
        } 
        /*else if("DISTRIBUTE".equals(stMode)) {
            searchMap = distributeDB(searchMap);
        }*/

        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 지표Pool관리 등록/수정/삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap memMetricGrpUseProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");

        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("DISTRIBUTE".equals(stMode)) {
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
	        //String[] scDeptIds = searchMap.getString("scDeptIds").split("\\|", 0);
	        String[] empns = searchMap.getString("empns").split("\\|", 0);

	        setStartTransaction();

	        returnMap = deleteData("mem.base.memMetricGrp.deleteDistributeData", searchMap, true);

	        if( !"".equals(empns[0]) && empns[0] != null) {

	        	for (int i = 0; i < empns.length; i++) {
	        		searchMap.put("empNo", empns[i]);

	        		int cnt = getInt("mem.base.memMetricGrp.getUserCount", searchMap);

	        		if(cnt == 0) {
	        			returnMap = insertData("mem.base.memMetricGrp.distributeData", searchMap);
	        		}else if(cnt > 0){
	        			returnMap = updateData("mem.base.memMetricGrp.updateDistributeData", searchMap);
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
         * 목표 Parameter setting
         **********************************/
    	String[] mons = searchMap.getStringArray("mons");
    	String[] colValues = searchMap.getStringArray("colValues");
    	
    	/**********************************
         * 실적산식 Parameter setting
         **********************************/
        String[] calTypeCols = searchMap.getStringArray("calTypeCols");
        String[] calTypeColNms = searchMap.getStringArray("calTypeColNms");
        String[] units = searchMap.getStringArray("units");

        /**********************************
         * 구간대 Parameter setting
         **********************************/
        String[] evalSectionIds = searchMap.getStringArray("evalSectionIds");
        String[] fromValues = searchMap.getStringArray("fromValues");
        String[] toValues = searchMap.getStringArray("toValues");
        String[] conversionScores = searchMap.getStringArray("conversionScores");


        /**********************************
         * 측정주기 Parameter setting
         **********************************/
        String[] actMons = searchMap.getString("actMons").split("\\|", 0);

        try {
	        setStartTransaction();

	        /**********************************
	         * 지표풀 코드 채번
	         **********************************/
	        String metricGrpId = getStr("mem.base.memMetricGrp.getMetricGrpId", searchMap);
	        searchMap.put("metricGrpId", metricGrpId);

	        /**********************************
	         * 지표풀 등록
	         **********************************/
	        returnMap = insertData("mem.base.memMetricGrp.insertData", searchMap);

	        /**********************************
	         * 측정주기 삭제 & 등록
	         **********************************/
	        //if("01".equals(typeId)) { //정량지표
		        returnMap = updateData("mem.base.memMetricGrp.deleteRegMon", searchMap, true);

		        if(actMons!=null){
			        for (int i = 0; i < actMons.length; i++) {
			        	searchMap.put("mon", actMons[i]);
			        	returnMap = insertData("mem.base.memMetricGrp.insertRegMon", searchMap);
			        }
		        }
	        //}
		        
	        /**********************************
	         * 성과목표 삭제 & 등록
	         **********************************/
	        //if("01".equals(typeId)) { //정량지표
		        returnMap = updateData("mem.base.memMetricGrp.deleteTarget", searchMap, true);

		        if(colValues!=null){
			        for (int i = 0; i < mons.length; i++) {
			        	searchMap.put("mon", mons[i]);
			        	searchMap.put("tgtValue", colValues[i]);
			        	returnMap = insertData("mem.base.memMetricGrp.insertTarget", searchMap);
			        }
		        }
	        //}

	        /**********************************
	         * 실적산식 삭제
	         **********************************/
	        returnMap = updateData("mem.base.memMetricGrp.deleteCalTypeCol", searchMap, true);

	        /**********************************
	         * 실적산식 등록
	         **********************************/
	        //if("01".equals(typeId)) { //정량지표
		        for (int i = 0; i < calTypeCols.length; i++) {
		            searchMap.put("calTypeCol", calTypeCols[i]);
		            searchMap.put("calTypeColNm", calTypeColNms[i]);
		            searchMap.put("unit", units[i]);
		            returnMap = insertData("mem.base.memMetricGrp.insertCalTypeCol", searchMap);
		        }
	        //}

	        /**********************************
	         * 구간대 삭제
	         **********************************/
	        returnMap = updateData("mem.base.memMetricGrp.deleteEvalSection", searchMap, true);

	        /**********************************
	         * 구간대 등록
	         **********************************/
	        if("01".equals(typeId)) { //정량지표
	        	
		        for (int i = 0; i < evalSectionIds.length; i++) {
		            searchMap.put("evalSectionId", evalSectionIds[i]);
		            searchMap.put("fromValue", fromValues[i]);
		            searchMap.put("toValue", toValues[i]);
		            searchMap.put("conversionScore", conversionScores[i]);
		            returnMap = insertData("mem.base.memMetricGrp.insertEvalSection", searchMap);
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
        String[] units = searchMap.getStringArray("units");

        /**********************************
         * 구간대
         **********************************/
        String[] evalSectionIds = searchMap.getStringArray("evalSectionIds");
        String[] fromValues = searchMap.getStringArray("fromValues");
        String[] toValues = searchMap.getStringArray("toValues");
        String[] conversionScores = searchMap.getStringArray("conversionScores");

        /**********************************
         * 측정주기 Parameter setting
         **********************************/
        String[] actMons = searchMap.getString("actMons").split("\\|", 0);
        
        /**********************************
         * 목표 Parameter setting
         **********************************/
    	String[] mons = searchMap.getStringArray("mons");
    	String[] colValues = searchMap.getStringArray("colValues");

        try {
	        setStartTransaction();

	        /**********************************
	         * 지표풀 수정
	         **********************************/
	        returnMap = updateData("mem.base.memMetricGrp.updateData", searchMap);


	        /**********************************
	         * 측정주기 삭제 & 등록
	         **********************************/
	        returnMap = updateData("mem.base.memMetricGrp.deleteRegMon", searchMap, true);

	        if("01".equals(typeId) || "02".equals(typeId)) { //계량, 비계량
		        if(actMons!=null){
			        for (int i = 0; i < actMons.length; i++) {
			        	searchMap.put("mon", actMons[i]);
			        	returnMap = insertData("mem.base.memMetricGrp.insertRegMon", searchMap, true);
			        }
			        
			        updateData("mem.base.memMetricGrp.deleteRegMonMetric", searchMap, true);
			        
			        insertData("mem.base.memMetricGrp.insertRegMonMetric", searchMap, true);
			        
		        }
	        }
	        
	        /**********************************
	         * 성과목표 삭제 & 등록
	         **********************************/
	        returnMap = updateData("mem.base.memMetricGrp.deleteTarget", searchMap, true);

	        if("01".equals(typeId) || "02".equals(typeId)) { //계량, 비계량
		        if(mons!=null){
			        for (int i = 0; i < mons.length; i++) {
			        	searchMap.put("mon", mons[i]);
			        	searchMap.put("tgtValue", colValues[i]);
			        	returnMap = insertData("mem.base.memMetricGrp.insertTarget", searchMap, true);
			        }
			        
			        updateData("mem.base.memMetricGrp.deleteTargetMetric", searchMap, true);
			        
			        insertData("mem.base.memMetricGrp.insertTargetMetric", searchMap, true);
			        
		        }
	        }

	        /************************************
	         * 실적산식이 변경이 되었는지 체크
	         ************************************/
	        
	        if(actCalType.equals(orgActCalType)) {
		        //if("01".equals(typeId)) {
		        	if(null != calTypeCols && 0 < calTypeCols.length) {
				        for (int i = 0; i < calTypeCols.length; i++) {
				            searchMap.put("calTypeCol", calTypeCols[i]);
				            searchMap.put("calTypeColNm", calTypeColNms[i]);
				            searchMap.put("unit", units[i]);
				            returnMap = insertData("mem.base.memMetricGrp.updateCalTypeCol", searchMap, true);
				        }
		        	}
		        //}
	        } else {

		        returnMap = updateData("mem.base.memMetricGrp.deleteCalTypeCol", searchMap, true);


		        //if("01".equals(typeId)) {
		        	if(null != calTypeCols && 0 < calTypeCols.length) {
				        for (int i = 0; i < calTypeCols.length; i++) {
				            searchMap.put("calTypeCol", calTypeCols[i]);
				            searchMap.put("calTypeColNm", calTypeColNms[i]);
				            searchMap.put("unit", units[i]);
				            returnMap = insertData("mem.base.memMetricGrp.insertCalTypeCol", searchMap, true);
				        }
		        	}
		        //}
	        }
	        
	        

	        /**********************************
	         * 구간대 삭제
	         **********************************/
	        returnMap = updateData("mem.base.memMetricGrp.deleteEvalSection", searchMap, true);

	        /**********************************
	         * 구간대 등록
	         **********************************/
	        if("01".equals(typeId)) {
        		if(null != evalSectionIds && 0 < evalSectionIds.length) {
			        for (int i = 0; i < evalSectionIds.length; i++) {
			            searchMap.put("evalSectionId", evalSectionIds[i]);
			            searchMap.put("fromValue", fromValues[i]);
			            searchMap.put("toValue", toValues[i]);
			            searchMap.put("conversionScore", conversionScores[i]);
			            returnMap = insertData("mem.base.memMetricGrp.insertEvalSection", searchMap);
			        }
        		}
	        }

	        // 동일 지표 하위 사용여부 확인

	        int cnt = getInt("mem.base.memMetricGrp.getMetricCount", searchMap);

	        if(0 < cnt){
		        /********************************************
		         * 동일 지표POOL을 가진 지표 기본정보 update
		         ********************************************/
		        returnMap = updateData("mem.base.memMetricGrp.updateMetricData", searchMap, true);

		        /********************************************
		         * 연계항목 수정시 하위 지표 연계항목 수정
		         ********************************************/
		        returnMap = updateData("mem.base.memMetricGrp.updateCalTypeColItemData", searchMap, true);

		        /************************************
		         * 실적산식이 변경이 되었는지 체크
		         ************************************
		        if(actCalType.equals(orgActCalType)) {
		        	/************************************
			         * 지표산식 수정(only 산식명만 수정)
			         ***********************************
			        returnMap = updateData("mem.base.memMetricGrp.updateMetricDataOnlyName", searchMap, true);
		        } else {*/
		        	/************************************
			         * 지표산식 수정
			         ************************************/
			        returnMap = updateData("mem.base.memMetricGrp.deleteMetricCalTypeColData", searchMap, true);
			        returnMap = updateData("mem.base.memMetricGrp.insertMetricCalTypeColData", searchMap, true);
		        //}

		        /************************************
		         * 지표 구간대 수정
		         ************************************/

		        int cnt2 = getInt("mem.base.memMetricGrp.getMetricCount2", searchMap);
		        if(cnt2 > 0){
			        returnMap = updateData("mem.base.memMetricGrp.deleteMetricEvalSectionData", searchMap, true);
			        returnMap = updateData("mem.base.memMetricGrp.insertMetricEvalSectionData", searchMap, true);
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
	            returnMap = updateData("mem.base.memMetricGrp.deleteData", searchMap);
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

        returnMap = ValidationChk.selEmptyCheck(searchMap.getString("evalCycleId"), "주기");
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

                returnMap = ValidationChk.lengthCheck(units[i], "단위", 1, 7);
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
    public SearchMap memMetricGrpListExcel(SearchMap searchMap) {
    	String excelFileName = StringConstants.METRIC_GRP_NM;
    	String excelTitle = StringConstants.METRIC_GRP_NM + "리스트";

    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO(StringConstants.METRIC_GRP_NM +"명", StaticUtil.nullToDefault((String)searchMap.get("memMetricGrpNm"), "전체")));
    	excelSearchInfoList.add(new ExcelVO("평가항목", (String)searchMap.get("evalGbnNm")));
    	excelSearchInfoList.add(new ExcelVO("세부평가항목", (String)searchMap.get("detailEvalGbnNm")));
    	excelSearchInfoList.add(new ExcelVO("사용여부", (String)searchMap.get("useYnNm")));

    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO(StringConstants.METRIC_GRP_NM + "코드", "METRIC_GRP_ID", "center", "CNT"));
    	excelInfoList.add(new ExcelVO(StringConstants.METRIC_GRP_NM + "명", "METRIC_GRP_NM", "left", "CNT", 8000));
    	excelInfoList.add(new ExcelVO("성과목표", "PERFORMANCE_GOAL", "center", "CNT"));
    	excelInfoList.add(new ExcelVO("평가항목", "EVAL_GBN_NM", "center", "CNT"));
    	excelInfoList.add(new ExcelVO("평가유형", "DETAIL_EVAL_GBN_NM", "center", "CNT"));
    	excelInfoList.add(new ExcelVO("부서지표", "DEPT_METRIC_NM", "center", "CNT"));
    	excelInfoList.add(new ExcelVO("단위", "UNIT_NM", "center", "CNT"));
    	excelInfoList.add(new ExcelVO("주기", "EVAL_CYCLE_NM", "center", "CNT"));
    	excelInfoList.add(new ExcelVO(StringConstants.METRIC_NM + "산식", "ACT_CAL_TYPE", "center", "CNT", 6000));
    	excelInfoList.add(new ExcelVO("산식컬럼", "CAL_TYPE_COL", "center"));
    	excelInfoList.add(new ExcelVO("산식컬럼명", "CAL_TYPE_COL_NM", "left"));
    	excelInfoList.add(new ExcelVO("단위", "CAL_TYPE_COL_UNIT_NM", "center"));
    	excelInfoList.add(new ExcelVO("득점산식구분", "SCORE_CAL_TYPE_GUBUN_NM", "center", "CNT"));
    	excelInfoList.add(new ExcelVO("득점산식", "CAL_TYPE_NM", "left", "CNT", 6000));
    	excelInfoList.add(new ExcelVO(StringConstants.METRIC_NM + "목적", "KPI_PURPOSE", "left", "CNT", 10000));
    	excelInfoList.add(new ExcelVO("사용개수", "COUNT", "center", "CNT"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("mem.base.memMetricGrp.getExcelList", searchMap));

        return searchMap;
    }

}
