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
package com.lexken.mem.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.FileInfoVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.StringConstants;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.util.HtmlHelper;
import com.lexken.framework.util.StaticUtil;
import com.lexken.framework.login.LoginVO;

public class MemMetricAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 조직별KPI관리 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap memMetricList(SearchMap searchMap) {

    	/**********************************
         * 로그인정보 (권한 체크)
         **********************************/
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");

    	/************************************************************************************
    	 * 평가조직 트리 조회
    	 ************************************************************************************/
        /*
    	if(loginVO.chkAuthGrp("01") || loginVO.chkAuthGrp("60")) {  //전체관리자
        	searchMap.addList("treeList", getList("bsc.module.commModule.getScDeptList", searchMap));
        } else {
        	searchMap.addList("treeList", getList("bsc.module.commModule.getScDeptAuthList", searchMap));
        }
        */

        /************************************************************************************
    	 * 지표입력 기한조회
    	 ************************************************************************************/
    	//searchMap.addList("kpiClosingYn", getStr("mem.base.memMetric.getKpiClosingYn", searchMap));

    	/************************************************************************************
    	 * 지표입력 마감조회
    	 ************************************************************************************/
    	searchMap.addList("kpiInputTermYn", getStr("mem.base.memMetric.getKpiInputTermYn", searchMap));
    	
    	String findEmpNo = searchMap.getString("findEmpNo");
    	String findEmpNm = searchMap.getString("findEmpNm");
    	
    	if(loginVO.chkAuthGrp("01")){
    		if("".equals(StaticUtil.nullToDefault(findEmpNo, ""))){
        		HashMap hmap = getDetail("mem.base.memMetric.getEmpInfo", searchMap);
        		if(null != hmap){
        			searchMap.put("findEmpNo", hmap.get("EMP_NO"));
        			searchMap.put("findEmpNm", hmap.get("EMP_NM"));
        		}
        	}else if("".equals(StaticUtil.nullToDefault(findEmpNm, ""))){
        		String empNm = getStr("mem.base.memMetric.getEmpNmInfo", searchMap);
        		searchMap.put("findEmpNm", empNm);
        	}
    	}else if(loginVO.chkAuthGrp("51")){
    		searchMap.put("evalEmpNo",loginVO.getUser_id());
    		searchMap.addList("empNoList", getList("mem.base.memMetric.getEmpNoList", searchMap));
    		
    		if("".equals(StaticUtil.nullToDefault(findEmpNo, ""))){
    			searchMap.put("findEmpNo", searchMap.getDefaultValue("empNoList", "EMP_NO", 0));
    			searchMap.put("findEmpNm", searchMap.getDefaultValue("empNoList", "EMP_NM", 0));
        	}else if("".equals(StaticUtil.nullToDefault(findEmpNm, ""))){
        		String empNm = getStr("mem.base.memMetric.getEmpNmInfo", searchMap);
        		searchMap.put("findEmpNm", empNm);
        	}
    		
    		searchMap.put("evalYn",getStr("mem.base.memMetric.getEvalYn", searchMap));
    		
    	}else{
    		searchMap.put("findEmpNo", loginVO.getUser_id());
			searchMap.put("findEmpNm", loginVO.getUser_nm());
    	}

    	/************************************************************************************
    	 * 디폴트 평가조직 조회
    	 ************************************************************************************/
    	//String scDeptId = searchMap.getString("scDeptId");
    	/*
    	searchMap.put("findSearchCodeId", scDeptId);
    	if("".equals(scDeptId)) {
    		searchMap.put("scDeptId", searchMap.getDefaultValue("treeList", "CODE_ID", 0));
    		searchMap.put("scDeptNm", searchMap.getDefaultValue("treeList", "CODE_NM", 0));
    		searchMap.put("findSearchCodeId", searchMap.getDefaultValue("treeList", "CODE_ID", 0));
    	}
    	*/

        return searchMap;
    }
    
    public SearchMap popTransHis(SearchMap searchMap) {

    	/**********************************
         * 로그인정보 (권한 체크)
         **********************************/
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");

        return searchMap;
    }
    
    /**
     * 조직별KPI관리 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap popTransHis_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("mem.base.memMetric.getTransList", searchMap));

        return searchMap;
    }

    /**
     * 조직별KPI 가중치 합계 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap memMetricWeightSum_ajax(SearchMap searchMap) {

    	searchMap.addList("weightSum", getStr("mem.base.memMetric.getUserWeight", searchMap));

        return searchMap;
    }

    /**
     * 조직별 담당자 및 KPI 승인상태 가져오기
     * @param
     * @return String
     * @throws
     */
    
    public SearchMap approveInfo_ajax(SearchMap searchMap) {

    	searchMap.addList("status", getDetail("mem.base.memMetric.getApproveInfo", searchMap));

        return searchMap;
    }
    

    /**
     * 조직별KPI관리 일괄수정 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap memMetricBatchList(SearchMap searchMap) {
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
    public SearchMap memMetricList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("mem.base.memMetric.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 조직별KPI관리 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap memMetricHisList_xml(SearchMap searchMap) {

        searchMap.addList("hisList", getList("mem.base.memMetric.getHisList", searchMap));

        return searchMap;
    }

    /**
     * 조직별KPI관리 상세화면
     * @param
     * @return String
     * @throws
     */
    public SearchMap memMetricModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");

    	/**********************************
         * 로그인정보 (권한 체크)
         **********************************/
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");

    	/**********************************
         * 조직별 가중치 합계 조회
         **********************************/
    	searchMap.addList("weightSum", getStr("mem.base.memMetric.getUserWeight", searchMap));

    	/**********************************
         * 조직별 지표 승인상태 조회
         **********************************/
    	searchMap.addList("status", getDetail("mem.base.memMetric.getApproveInfo", searchMap));

    	HashMap detail = new HashMap();
    	HashMap detailTarget = new HashMap();

    	if("MOD".equals(stMode)) {
	    	/**********************************
	         * 지표 상세조회
	         **********************************/
	    	detail = getDetail("mem.base.memMetric.getDetail", searchMap);
	        searchMap.addList("detail", detail);
	        
	        detailTarget = getDetail("mem.base.memMetric.getDetailTarget", searchMap);
	        searchMap.addList("detailTarget", detailTarget);
    	}

    	/**********************************
         * 측정주기월 조회
         **********************************/
        ArrayList regMonList = new ArrayList();
        regMonList = (ArrayList)getList("mem.base.memMetric.regMonList", searchMap);
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
        calTypeColList = (ArrayList)getList("mem.base.memMetric.calTypeColList", searchMap);
        searchMap.addList("calTypeColList", calTypeColList);

        /**********************************
         * 득점산식조회
         **********************************/
        searchMap.addList("scoreCalTypeList", getList("mem.base.memMetric.scoreCalTypeList", searchMap));

        /**********************************
         * 평가구간대 조회
         **********************************/
        searchMap.addList("evalSectionList", getList("mem.base.memMetric.evalSectionList", searchMap));

        /**********************************
         * 평가구간대 등급 조회
         **********************************/
        searchMap.addList("gradeList", getList("mem.base.memMetric.gradeList", searchMap));

        if("MOD".equals(stMode)) {
	       

	        /**********************************
	         * 년목표 조회
	         **********************************/
	        searchMap.addList("targetY", getStr("mem.base.memMetric.getTargetY", searchMap));

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
	        
	        /**********************************
	         * 파일목록조회
	         **********************************/
	        searchMap.addList("fileList", getList("mem.base.memMetric.getAttachFileList", searchMap));
	        
	        searchMap.addList("actionplanList", getList("mem.base.memMetric.getActionplanList", searchMap));
	        
        }

        
    	/**********************************
         * 지표입력 마감조회
         **********************************/
    	searchMap.addList("kpiInputTermYn", getStr("mem.base.memMetric.getKpiInputTermYn", searchMap));

        return searchMap;
    }
    
    /**
     * 조직별KPI관리 상세화면
     * @param
     * @return String
     * @throws
     */
    public SearchMap popTransModify(SearchMap searchMap) {

    	/**********************************
         * 로그인정보 (권한 체크)
         **********************************/
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");

    	HashMap detail = new HashMap();
    	HashMap detailTarget = new HashMap();

    	/**********************************
         * 지표 상세조회
         **********************************/
    	detail = getDetail("mem.base.memMetric.getTransDetail", searchMap);
        searchMap.addList("detail", detail);
        
        detailTarget = getDetail("mem.base.memMetric.getTransDetailTarget", searchMap);
        searchMap.addList("detailTarget", detailTarget);

    	/**********************************
         * 측정주기월 조회
         **********************************/
        ArrayList regMonList = new ArrayList();
        regMonList = (ArrayList)getList("mem.base.memMetric.transRegMonList", searchMap);
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
        calTypeColList = (ArrayList)getList("mem.base.memMetric.transCalTypeColList", searchMap);
        searchMap.addList("calTypeColList", calTypeColList);

        /**********************************
         * 득점산식조회
         **********************************/
        searchMap.addList("scoreCalTypeList", getList("mem.base.memMetric.scoreCalTypeList", searchMap));

        /**********************************
         * 평가구간대 조회
         **********************************/
        searchMap.addList("evalSectionList", getList("mem.base.memMetric.transEvalSectionList", searchMap));

        /**********************************
         * 평가구간대 등급 조회
         **********************************/
        searchMap.addList("gradeList", getList("mem.base.memMetric.gradeList", searchMap));

        /**********************************
         * 년목표 조회
         **********************************/
        searchMap.addList("targetY", getStr("mem.base.memMetric.getTransTargetY", searchMap));

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
        
        searchMap.addList("actionplanList", getList("mem.base.memMetric.getTransActionplanList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 지표POOL 데이터 조회후 셋팅(AJAX) - 기본정보
     * @param
     * @return String
     * @throws
     */
    public SearchMap popReturnInput(SearchMap searchMap) {

        //searchMap.addList("list", getList("mem.base.memMetric.getMemDetailEvalGbnData", searchMap));

        return searchMap;
    }
    
    /**
     * 지표POOL 데이터 조회후 셋팅(AJAX) - 기본정보
     * @param
     * @return String
     * @throws
     */
    public SearchMap memDetailEvalGbnData_ajax(SearchMap searchMap) {

        searchMap.addList("list", getList("mem.base.memMetric.getMemDetailEvalGbnData", searchMap));

        return searchMap;
    }

    /**
     * 조직별KPI관리 등록/수정/삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap memMetricProcess(SearchMap searchMap) {
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
        } else if("ALLMOD".equals(stMode)) {   //지표이동
            searchMap = allUpdateDB(searchMap);    
        }
        

        /**********************************
         * 지표연계 속성 수정
         **********************************/
        //searchMap = metricPropertySet(searchMap);

        /**********************************
         * 조직코드 설정
         **********************************/
        //String scDeptId = searchMap.getString("scDeptId");
    	//searchMap.put("findSearchCodeId", scDeptId);

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
    	
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
        HashMap returnMap    = new HashMap();
        String metricGrpId = searchMap.getString("metricGrpId");
        String kpiPoolGubun = "MOD";

        /**********************************
         * Parameter setting
         **********************************/
        /*
        if("".equals(metricGrpId)) {
        	metricGrpId = getStr("mem.base.memMetricGrp.getMetricGrpId", searchMap); //지표풀 코드 채번
        	kpiPoolGubun = "ADD";
            searchMap.put("metricGrpId", metricGrpId);
    	}
        */

        try {
        	setStartTransaction();
        	
        	/**********************************
    		 * 파일복사
    		 **********************************/
    		searchMap.fileCopy("/temp", "/memMetric");
        	
        	/**********************************
             * 지표코드 채번
             **********************************/
	        String metricId = getStr("mem.base.memMetric.getMetricId", searchMap);
	        searchMap.put("metricId", metricId);

	        /**********************************
             * 지표 입력
             **********************************/
        	returnMap = insertData("mem.base.memMetric.insertData", searchMap);

        	/**********************************
	         * 측정주기 Parameter setting
	         **********************************/
	        String[] actMons = searchMap.getString("actMons").split("\\|", 0);
	        String typeId = searchMap.getString("typeId");
	        
	        /**********************************
	         * 목표 Parameter setting
	         **********************************/
	    	String[] mons = searchMap.getStringArray("mons");
	    	String[] colValues = searchMap.getStringArray("colValues");

	        /**********************************
	         * 측정주기 삭제 & 등록
	         **********************************/
	        //if("01".equals(typeId)) { //정량지표
		        returnMap = updateData("mem.base.memMetric.deleteRegMon", searchMap, true);

		        if(actMons!=null && !actMons.equals("")){
			        for (int i = 0; i < actMons.length; i++) {
			        	searchMap.put("mon", actMons[i]);
			        	returnMap = insertData("mem.base.memMetric.insertRegMon", searchMap);
			        }
		        }
	        //}
		        
		        returnMap = updateData("mem.base.memMetric.deleteTarget", searchMap, true);

		        if(colValues!=null){
			        for (int i = 0; i < mons.length; i++) {
			        	searchMap.put("mon", mons[i]);
			        	searchMap.put("tgtValue", colValues[i]);
			        	returnMap = insertData("mem.base.memMetric.insertTarget", searchMap);
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
             * 권한설정
             **********************************/
	        //returnMap = insertAdmin(searchMap);
	        
	        /**********************************
             * 첨부파일 정보 등록
             **********************************/
	        returnMap = insertFileInfo(searchMap);
	        
	        String[] actionplanIds = searchMap.getStringArray("actionplanIds"); 
	        String[] planStartYears = searchMap.getStringArray("planStartYears");
	        String[] planStartMons = searchMap.getStringArray("planStartMons");
	        String[] planEndYears = searchMap.getStringArray("planEndYears");
	        String[] planEndMons = searchMap.getStringArray("planEndMons");
	        String[] planDescs = searchMap.getStringArray("planDescs");
	        
	        returnMap = updateData("mem.base.memMetric.deleteActionplan", searchMap, true);
	        
	        if(actionplanIds != null){
	        	String actionplanId = "";
	        	
	        	for(int i=0 ; i<actionplanIds.length ; i++){
	        		if("new".equals(actionplanIds[i])){
	        			actionplanId = getStr("mem.base.memMetric.getActionplanId", searchMap); //지표풀 코드 채번
	        		}else{
	        			actionplanId = actionplanIds[i];
	        		}
	        		searchMap.put("actionplanId", actionplanId);
	        		searchMap.put("planDesc", planDescs[i]);
	        		searchMap.put("planStartDt", planStartYears[i]+planStartMons[i]);
	        		searchMap.put("planEndDt", planEndYears[i]+planEndMons[i]);
	        		
	        		returnMap = insertData("mem.base.memMetric.insertActionplan", searchMap);
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
    		 * 파일복사
    		 **********************************/
    		searchMap.fileCopy("/temp", "/memMetric");

	        /**********************************
	         * KPI 수정
	         **********************************/
	        returnMap = updateData("mem.base.memMetric.updateData", searchMap);

	        /**********************************
	         * 측정주기 Parameter setting
	         **********************************/
	        String[] actMons = searchMap.getString("actMons").split("\\|", 0);
	        String typeId = searchMap.getString("typeId");
	        
	        /**********************************
	         * 목표 Parameter setting
	         **********************************/
	    	String[] mons = searchMap.getStringArray("mons");
	    	String[] colValues = searchMap.getStringArray("colValues");

	        /**********************************
	         * 측정주기 삭제 & 등록
	         **********************************/
	        //if("01".equals(typeId)) { //정량지표
		        returnMap = updateData("mem.base.memMetric.deleteRegMon", searchMap, true);

		        if(actMons!=null && !actMons.equals("")){
			        for (int i = 0; i < actMons.length; i++) {
			        	searchMap.put("mon", actMons[i]);
			        	returnMap = insertData("mem.base.memMetric.insertRegMon", searchMap);
			        }
		        }
	        //}
		        
		        returnMap = updateData("mem.base.memMetric.deleteTarget", searchMap, true);

		        if(colValues!=null){
			        for (int i = 0; i < mons.length; i++) {
			        	searchMap.put("mon", mons[i]);
			        	searchMap.put("tgtValue", colValues[i]);
			        	returnMap = insertData("mem.base.memMetric.insertTarget", searchMap);
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
	        //returnMap = insertAdmin(searchMap);

	        /**********************************
	         * 지표Pool 정보 수정
	         *********************************
	        returnMap = kpiPoolModify(searchMap);*/

	        /**********************************
	         * 목표/실적 공유 걸린 지표 수정
	         **********************************/
	        //returnMap = insertData("mem.base.memMetric.execTamShareMetric", searchMap);
	        
	        /**********************************
             * 첨부파일 정보 등록
             **********************************/
	        returnMap = insertFileInfo(searchMap);
	        
	        String[] actionplanIds = searchMap.getStringArray("actionplanIds"); 
	        String[] planStartYears = searchMap.getStringArray("planStartYears");
	        String[] planStartMons = searchMap.getStringArray("planStartMons");
	        String[] planEndYears = searchMap.getStringArray("planEndYears");
	        String[] planEndMons = searchMap.getStringArray("planEndMons");
	        String[] planDescs = searchMap.getStringArray("planDescs");
	        
	        returnMap = updateData("mem.base.memMetric.deleteActionplan", searchMap, true);
	        
	        if(actionplanIds != null){
	        	String actionplanId = "";
	        	
	        	for(int i=0 ; i<actionplanIds.length ; i++){
	        		if("new".equals(actionplanIds[i])){
	        			actionplanId = getStr("mem.base.memMetric.getActionplanId", searchMap); //지표풀 코드 채번
	        		}else{
	        			actionplanId = actionplanIds[i];
	        		}
	        		searchMap.put("actionplanId", actionplanId);
	        		searchMap.put("planDesc", planDescs[i]);
	        		searchMap.put("planStartDt", planStartYears[i]+planStartMons[i]);
	        		searchMap.put("planEndDt", planEndYears[i]+planEndMons[i]);
	        		
	        		returnMap = insertData("mem.base.memMetric.insertActionplan", searchMap);
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
        String[] units = searchMap.getStringArray("units");

        try {
        	/**********************************
             * 실적산식 삭제
             **********************************/
	        returnMap = updateData("mem.base.memMetric.deleteCalTypeCol", searchMap, true);

	        /**********************************
             * 실적산식 등록
             **********************************/
	        //if("01".equals(typeId)) {
	        	if(null != calTypeCols && 0 < calTypeCols.length) {
			        for (int i = 0; i < calTypeCols.length; i++) {
			            searchMap.put("calTypeCol", calTypeCols[i]);
			            searchMap.put("calTypeColNm", calTypeColNms[i]);
			            searchMap.put("unit", units[i]);
			            returnMap = insertData("mem.base.memMetric.insertCalTypeCol", searchMap);
			        }
	        	}
	        //}

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
	        returnMap = updateData("mem.base.memMetric.deleteEvalSection", searchMap, true);

	        /**********************************
	         * 구간대 등록
	         **********************************/
	        if("01".equals(typeId)) {
	        	if("01".equals(scoreCalTypeGubun) || "02".equals(scoreCalTypeGubun)) {
	        		if(null != evalSectionIds && 0 < evalSectionIds.length) {
				        for (int i = 0; i < evalSectionIds.length; i++) {
				            searchMap.put("evalSectionId", evalSectionIds[i]);
				            searchMap.put("fromValue", fromValues[i]);
				            searchMap.put("toValue", toValues[i]);
				            searchMap.put("conversionScore", conversionScores[i]);
				            returnMap = insertData("mem.base.memMetric.insertEvalSection", searchMap);
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
     * 첨부파일 정보등록
     * @param      
     * @return String  
     * @throws 
     */
    public HashMap insertFileInfo(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        
        try {
        	String[] delAttachFiles = searchMap.getStringArray("delAttachFiles");
        	
        	/**********************************
             * 삭제체크된 첨부파일 삭제
             **********************************/
        	if(null != delAttachFiles && 0 < delAttachFiles.length) {
	        	for(int i = 0; i < delAttachFiles.length; i++){
	        		searchMap.put("seq", delAttachFiles[i]);
					returnMap = insertData("mem.base.memMetric.deleteFileInfo", searchMap);
	        	}
        	}
        	
        	/**********************************
             * 첨부파일 등록
             **********************************/
        	ArrayList fileInfoList = new ArrayList();
        	fileInfoList = (ArrayList)searchMap.get("FileInfoList");
	        FileInfoVO fileInfoVO = new FileInfoVO();
	        
	        if(null != fileInfoList) {
				for(int i = 0; i < fileInfoList.size(); i++){
					fileInfoVO = (FileInfoVO)fileInfoList.get(i);
					if(fileInfoVO != null){
						searchMap.put("attachFileFnm", 	fileInfoVO.getMaskFileName());
						searchMap.put("attachFileNm", 	fileInfoVO.getRealFileName());
						searchMap.put("attachFileSuffix",fileInfoVO.getFileExtension());
						searchMap.put("attachFilePath", fileInfoVO.getFileUploadPath());
						returnMap = insertData("mem.base.memMetric.insertFileInfo", searchMap);
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
		            returnMap = updateData("mem.base.memMetric.deleteData", searchMap);
		        }
	        }

	        /******************************************
             * 권한설정
             ******************************************/
	        //returnMap = insertAdmin(searchMap);

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

		            returnMap = updateData("mem.base.memMetric.updateBatchData", searchMap);

		        	/**********************************
		             * 목표 입력(측정주기월 분리)
		             **********************************/
		            ArrayList regMonList = new ArrayList();
		            regMonList = (ArrayList)getList("mem.base.memMetric.regMonList", searchMap);
		            String[] monArray = new String[0];
		            if(null != regMonList && 0 < regMonList.size()) {
		            	monArray = new String[regMonList.size()];
		            	for (int k = 0; k < regMonList.size(); k++) {
		    	        	HashMap<String, String> t = (HashMap<String, String>)regMonList.get(k);
		    	        	searchMap.put("mon", (String)t.get("MON"));

				            returnMap = updateData("mem.base.memMetric.insertAllTarget", searchMap, true);
		            	}
		            }

		        }
	        }

	        /******************************************
             * 권한설정
             ******************************************/
	        //returnMap = insertAdmin(searchMap);

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
     * 조직별KPI관리 일괄수정
     * @param
     * @return String
     * @throws
     */
    public SearchMap allUpdateDB(SearchMap searchMap) {
        HashMap returnMap = new HashMap();

        /**********************************
         * Parameter setting
         **********************************/
        String[] metricIds = searchMap.getStringArray("allMetricIds");
        String[] weights = searchMap.getStringArray("weights");
        String[] sortOrders = searchMap.getStringArray("sortOrders");

        try {
	        setStartTransaction();

	        if(null != metricIds && 0 < metricIds.length) {
		        for (int i = 0; i < metricIds.length; i++) {
		            searchMap.put("metricId", metricIds[i]);
		            searchMap.put("weight", weights[i]);
		            searchMap.put("sortOrder", sortOrders[i]);

		            returnMap = updateData("mem.base.memMetric.updateAllData", searchMap);

		        }
	        }

	        /******************************************
             * 권한설정
             ******************************************/
	        //returnMap = insertAdmin(searchMap);

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
    	
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
        HashMap returnMap = new HashMap();
        String status = searchMap.getString("status");
        String empNo = searchMap.getString("empNo");

        try {
	        setStartTransaction();
	        
	        /**********************************
    		 * 파일복사
    		 **********************************/
    		searchMap.fileCopy("/temp", "/memMetricStatus");
	        
	        /**********************************
	         * 데이터 등록여부 확인
	         **********************************/
	        int cnt = getInt("mem.base.memMetric.getStatusCount", searchMap);
	        
	        String statusSeq = getStr("mem.base.memMetric.getStatusSeq", searchMap);
	        
	        searchMap.put("loginUserId",loginVO.getUser_id());
	        searchMap.put("statusSeq",statusSeq);

	        /*
	        if(0 < cnt) {
	        	returnMap = updateData("mem.base.memMetric.updateStatusData", searchMap);
	        } else {
	            returnMap = insertData("mem.base.memMetric.insertStatusData", searchMap);
	        }
	        */
	        
	        returnMap = updateData("mem.base.memMetric.updateStatusData", searchMap);
	        
	        returnMap = insertData("mem.base.memMetric.insertStatusDescData", searchMap);

	        /**********************************
	         * 반려일 경우 반려사유 입력
	         **********************************/
	        /*
	        if("05".equals(status)) {
	        	returnMap = insertData("mem.base.memMetric.insertReturnCauseStatusData", searchMap);
	        }
	        */
	        
	        String[] delAttachFiles = searchMap.getStringArray("delAttachFiles");
        	
        	/**********************************
             * 삭제체크된 첨부파일 삭제
             **********************************/
        	/*
	        if(null != delAttachFiles && 0 < delAttachFiles.length) {
	        	for(int i = 0; i < delAttachFiles.length; i++){
	        		searchMap.put("seq", delAttachFiles[i]);
					returnMap = insertData("mem.base.memMetric.deleteHisFileInfo", searchMap);
	        	}
        	}
        	*/
        	
        	returnMap = insertData("mem.base.memMetric.deleteHisFileInfo", searchMap);
        	
        	/**********************************
             * 첨부파일 등록
             **********************************/
        	ArrayList fileInfoList = new ArrayList();
        	fileInfoList = (ArrayList)searchMap.get("FileInfoList");
	        FileInfoVO fileInfoVO = new FileInfoVO();
	        
	        if(null != fileInfoList) {
				for(int i = 0; i < fileInfoList.size(); i++){
					fileInfoVO = (FileInfoVO)fileInfoList.get(i);
					if(fileInfoVO != null){
						searchMap.put("attachFileFnm", 	fileInfoVO.getMaskFileName());
						searchMap.put("attachFileNm", 	fileInfoVO.getRealFileName());
						searchMap.put("attachFileSuffix",fileInfoVO.getFileExtension());
						searchMap.put("attachFilePath", fileInfoVO.getFileUploadPath());
						returnMap = insertData("mem.base.memMetric.insertHisFileInfo", searchMap);
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
        String targetEmpNo = searchMap.getString("targetEmpNo");

        String[] metricIdArray = metricIds.split("\\|", 0);

        try {
	        setStartTransaction();

	        if(null != metricIdArray && 0 < metricIdArray.length) {
		        for (int i = 0; i < metricIdArray.length; i++) {
		            searchMap.put("metricId", metricIdArray[i]);
		            returnMap = insertData("mem.base.memMetric.execMetricCopy", searchMap);
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
         * Parameter setting
         **********************************/
        /*
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
			            	returnMap = insertData("mem.base.memMetric.execMetricCopy", searchMap);
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
        */

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
        String targetEmpNo = searchMap.getString("targetEmpNo");

        String[] metricIdArray = metricIds.split("\\|", 0);

        try {
	        setStartTransaction();

	        if(null != metricIdArray && 0 < metricIdArray.length) {
		        for (int i = 0; i < metricIdArray.length; i++) {
		            searchMap.put("metricId", metricIdArray[i]);
		            returnMap = insertData("mem.base.memMetric.moveMetric", searchMap);
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
    public SearchMap memMetricGrpData_ajax(SearchMap searchMap) {

        searchMap.addList("list", getList("mem.base.memMetric.getMetricGrpDataAjax", searchMap));

        searchMap.addList("listMon", getList("mem.base.memMetric.regGrpMonList", searchMap));

        return searchMap;
    }

    /**
     * 지표POOL 데이터 조회후 셋팅(AJAX) - 산식컬럼
     * @param
     * @return String
     * @throws
     */
    public SearchMap memMetricGrpCalTypeCol_ajax(SearchMap searchMap) {

        searchMap.addList("list", getList("mem.base.memMetric.getMetricGrpCalTypeColAjax", searchMap));

        return searchMap;
    }

    /**
     * 지표POOL 데이터 조회후 셋팅(AJAX) - 구간대
     * @param
     * @return String
     * @throws
     */
    public SearchMap memMetricGrpEvalSectionGrp_ajax(SearchMap searchMap) {

        searchMap.addList("list", getList("mem.base.memMetric.getMetricGrpEvalSectionGrpAjax", searchMap));

        return searchMap;
    }
    
    /**
     * 지표POOL 데이터 조회후 셋팅(AJAX) - 구간대
     * @param
     * @return String
     * @throws
     */
    public SearchMap memMetricGrpTargetGrp_ajax(SearchMap searchMap) {

        searchMap.addList("list", getList("mem.base.memMetric.getMetricGrpTargetGrpAjax", searchMap));

        return searchMap;
    }
    
    /**
     * 지표POOL 데이터 조회후 셋팅(AJAX) - 구간대
     * @param
     * @return String
     * @throws
     */
    public SearchMap memMetricGrpEvalSection_ajax(SearchMap searchMap) {

        searchMap.addList("list", getList("mem.base.memMetric.getMetricGrpEvalSectionAjax", searchMap));

        return searchMap;
    }

    /**
     * 상위KPI 데이터 조회후 셋팅(AJAX) - 기본정보
     * @param
     * @return String
     * @throws
     */
    public SearchMap upMetricData_ajax(SearchMap searchMap) {

        searchMap.addList("list", getList("mem.base.memMetric.getDetail", searchMap));

        searchMap.addList("listMon", getList("mem.base.memMetric.regMonList", searchMap));

        return searchMap;
    }

    /**
     * 상위KPI 데이터 조회후 셋팅(AJAX) - 산식컬럼
     * @param
     * @return String
     * @throws
     */
    public SearchMap upMetricCalTypeCol_ajax(SearchMap searchMap) {

        searchMap.addList("list", getList("mem.base.memMetric.calTypeColList", searchMap));

        return searchMap;
    }

    /**
     * 상위KPI 데이터 조회후 셋팅(AJAX) - 구간대
     * @param
     * @return String
     * @throws
     */
    public SearchMap upMetricEvalSection_ajax(SearchMap searchMap) {

        searchMap.addList("list", getList("mem.base.memMetric.evalSectionList", searchMap));

        return searchMap;
    }

    /**
     * 상위KPI 데이터 조회후 셋팅(AJAX) - 목표
     * @param
     * @return String
     * @throws
     */
    public SearchMap upMetricTarget_ajax(SearchMap searchMap) {

        searchMap.addList("list", getList("mem.base.memMetric.getTargetList", searchMap));

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
        	searchMap.addList("targetList", getList("mem.base.memMetric.getTargetList", searchMap));
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
	        String[] units		   = searchMap.getStringArray("units");

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

	        if( "01".equals(searchMap.get("scoreCalTypeGubun")) || "02".equals(searchMap.get("scoreCalTypeGubun"))) {
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
	        int cnt = getInt("mem.base.memMetric.getMetricPoolCount", searchMap);

	        /**********************************
	         * 지표연계 속성 수정
	         **********************************/
	        if(1 < cnt) {
	        	returnMap = updateData("mem.base.memMetric.updateMetricGrpProperty", searchMap);
	        	returnMap = updateData("mem.base.memMetric.updateMetricProperty", searchMap);
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
    	searchMap.put("excelDataList", (ArrayList)getList("mem.base.memMetric.getExcelList", searchMap));

        return searchMap;
    }


}
