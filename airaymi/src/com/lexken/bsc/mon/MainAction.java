/*************************************************************************
 * CLASS 명  	: MainAction.java
 * 작 업 자  	: 하윤식
 * 작 업 일  	: 2012년 8월 23일 
 * 기    능  	: 메인대쉬보드
 * ---------------------------- 변 경 이 력 --------------------------------
 * 번호  작 업 자     작     업     일        변 경 내 용                 비고
 * ----  --------  -----------------  -------------------------    --------
 *   1    하윤식      2012년 8월 23일  		  최 초 작 업 
 **************************************************************************/
package com.lexken.bsc.mon;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import ksign.pkix1.ocsp.Request;

import com.lexken.framework.codeUtil.CodeUtilReLoad;
import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.struts2.IsBoxActionSupport;
import com.lexken.framework.util.CalendarHelper;
import com.lexken.framework.util.StaticUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.login.LoginVO;

public class MainAction extends CommonService {

	private static final long serialVersionUID = 1L;

	// Logger
	private final Log logger = LogFactory.getLog(getClass());

	//모니터링 년월 설정
	private static String YEAR = (new CalendarHelper()).modifyDate(0, 0, 0,"yyyy");
	private static String MON = (new CalendarHelper()).modifyDate(0, 0, 0,"MM");
	//private static final String YEAR = "2013";
	//private static final String MON = "12";
	private static final String ANAL_CYCLE = "Y";
	
	public MainAction(){
		SearchMap searchMap = new SearchMap();
		ArrayList list = (ArrayList) getList("bsc.system.closingYear.getList", searchMap);
		if(null != list){
			for(int i = 0; i < list.size(); i++){
				HashMap hMap = (HashMap)list.get(i);
				if(i == 0){
					YEAR = (String) hMap.get("YEAR");
				} else {
					String closingYn = (String) hMap.get("CLOSING_YN");
					if(null == closingYn || "N".equals(closingYn)){
						YEAR = (String) hMap.get("YEAR");
					} else {
						break;
					}
				}
			}
			String year = (new CalendarHelper()).modifyDate(0, 0, 0,"yyyy");
			
			if(!year.equals(YEAR)){
				MON = "12";
			}
			
		}
		
		//YEAR = "2016";
		//MON = "04";

	}
	
	
	/**
	 * 메인화면(과거)
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap mainList(SearchMap searchMap) {
		searchMap.put("year", YEAR);
		searchMap.put("mon", MON);
		searchMap.put("analCycle", ANAL_CYCLE);
		
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	searchMap.put("userId", loginVO.getUser_id());
    	searchMap.put("scDeptId", loginVO.getSc_dept_id());
		
		/**********************************
         * 성과비율 목록
         **********************************/
		searchMap.addList("ratelist", getList("bsc.mon.main.getSignalRate", searchMap));
		
		/**********************************
		 * 공지사항 목록
		 **********************************/
		searchMap.addList("noticelist", getList("bsc.mon.main.getNoticeList", searchMap));
		
		/**********************************
		 * QnA 목록
		 **********************************/
		searchMap.put("findBbsId", "FREE");
		searchMap.addList("qnaList", getList("bsc.mon.main.getBbsList", searchMap));
		
		/**********************************
		 * 자료실 목록
		 **********************************/
		searchMap.put("findBbsId", "DOWNLOAD");
		searchMap.addList("boardList", getList("bsc.mon.main.getBbsList", searchMap));
		
		/**********************************
         * 성과조직종합현황 목록
         **********************************/
		searchMap.addList("totalStatusList", getList("bsc.mon.main.getTotalStatusList", searchMap));
		
		/**********************************
         * 공지사항 팝업
         **********************************/
		if(!"".equals(searchMap.get("loginId"))){ //최초 로그인 시에만 나타나도록 처리
			searchMap.addList("popupList", getList("bsc.mon.main.getPopupList", searchMap));
		}
		
		/**********************************
		 * 사용자지정 지표 목록
		 **********************************/
		searchMap.addList("userMetricList", getList("bsc.mon.main.getUserMetric", searchMap));
		
		return searchMap;
	}
	
	/**
	 * 메인화면(임원)
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap mainLev1List(SearchMap searchMap) {
		
		if("".equals(searchMap.getString("findYear"))) {
			searchMap.put("findYear", YEAR);
		}
		
		if("".equals(searchMap.getString("findMon"))) {
			searchMap.put("findMon", MON);
		}
		
		if("".equals(searchMap.getString("findAnalCycle"))) {
			searchMap.put("findAnalCycle", ANAL_CYCLE);
		}
		
		String topScDeptId = (String)getStr("bsc.module.commModule.getTopScDeptId", searchMap);
		searchMap.put("topScDeptId", topScDeptId);
		
		/**********************************
         * 로그인 사용자 조회
         **********************************/
		LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	searchMap.put("userId", loginVO.getUser_id());
    	searchMap.put("scDeptId", loginVO.getSc_dept_id());
    	searchMap.put("scDeptGrpId", loginVO.getSc_dept_grp_id());

    	/**********************************
         * 전체현황
         **********************************/
		searchMap.put("topScDeptId", topScDeptId);
		searchMap.addList("perspectiveScoreList", getList("bsc.mon.main.getPerspectiveScoreList", searchMap));

		/**********************************
		 * 사용자지정 지표 목록
		 **********************************/
		searchMap.addList("userMetricList", getList("bsc.mon.main.getUserMetric", searchMap));
		
		searchMap.addList("findYear", searchMap.get("findYear"));
		searchMap.addList("findMon", searchMap.get("findMon"));
		searchMap.addList("findAnalCycle", searchMap.get("findAnalCycle"));
		
		String govMetricNm = (String)getStr("bsc.mon.main.getGovMetricNm", searchMap);
		searchMap.put("govMetricNm", govMetricNm);

		String govMetricId = (String)getStr("bsc.mon.main.getGovMetricId", searchMap);
		searchMap.put("govMetricId", govMetricId);
		/**********************************
         * 공지사항 팝업
         **********************************/
		if(!"".equals(searchMap.get("loginId"))){ //최초 로그인 시에만 나타나도록 처리
			searchMap.addList("popupList", getList("bsc.mon.main.getPopupList", searchMap));
		}
		
		return searchMap;
	}
	
	/**
	 * 메인화면(처실장)
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap mainLev2List(SearchMap searchMap) {
		
		if("".equals(searchMap.getString("findYear"))) {
			searchMap.put("findYear", YEAR);
		}
		
		if("".equals(searchMap.getString("findMon"))) {
			searchMap.put("findMon", MON);
		}
		
		if("".equals(searchMap.getString("findAnalCycle"))) {
			searchMap.put("findAnalCycle", ANAL_CYCLE);
		}
		
		/**********************************
         * 로그인 사용자 조회
         **********************************/
		LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	searchMap.put("userId", loginVO.getUser_id());
    	searchMap.put("scDeptId", loginVO.getSc_dept_id());
    	searchMap.put("scDeptGrpId", loginVO.getSc_dept_grp_id());

		/**********************************
		 * 사용자지정 지표 목록
		 **********************************/
		searchMap.addList("userMetricList", getList("bsc.mon.main.getUserMetric", searchMap));
		
		searchMap.addList("findYear", searchMap.get("findYear"));
		searchMap.addList("findMon", searchMap.get("findMon"));
		searchMap.addList("findAnalCycle", searchMap.get("findAnalCycle"));
		
		String scDeptCompNm = (String)getStr("bsc.mon.main.getScDeptCompNm", searchMap);
		searchMap.put("scDeptCompNm", scDeptCompNm);
		
		/**********************************
         * 공지사항 팝업
         **********************************/
		if(!"".equals(searchMap.get("loginId"))){ //최초 로그인 시에만 나타나도록 처리
			searchMap.addList("popupList", getList("bsc.mon.main.getPopupList", searchMap));
		}
		
		return searchMap;
	}
	
	/**
	 * 메인화면(2차사업소장)
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap mainLev3List(SearchMap searchMap) {
		
		if("".equals(searchMap.getString("findYear"))) {
			searchMap.put("findYear", YEAR);
		}
		
		if("".equals(searchMap.getString("findMon"))) {
			searchMap.put("findMon", MON);
		}
		
		if("".equals(searchMap.getString("findAnalCycle"))) {
			searchMap.put("findAnalCycle", ANAL_CYCLE);
		}
		
		/**********************************
         * 로그인 사용자 조회
         **********************************/
		LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	searchMap.put("userId", loginVO.getUser_id());
    	searchMap.put("scDeptId", loginVO.getSc_dept_id());
    	searchMap.put("scDeptGrpId", loginVO.getSc_dept_grp_id());

		/**********************************
		 * 사용자지정 지표 목록
		 **********************************/
		searchMap.addList("userMetricList", getList("bsc.mon.main.getUserMetric", searchMap));
		
		searchMap.addList("findYear", searchMap.get("findYear"));
		searchMap.addList("findMon", searchMap.get("findMon"));
		searchMap.addList("findAnalCycle", searchMap.get("findAnalCycle"));
		
		String scDeptCompNm = (String)getStr("bsc.mon.main.getScDeptCompNm", searchMap);
		searchMap.put("scDeptCompNm", scDeptCompNm);
		/**********************************
         * 공지사항 팝업
         **********************************/
		if(!"".equals(searchMap.get("loginId"))){ //최초 로그인 시에만 나타나도록 처리
			searchMap.addList("popupList", getList("bsc.mon.main.getPopupList", searchMap));
		}
		
		return searchMap;
	}
	
	/**
	 * 메인화면(본사/부설기관 - 부장이하 직원)
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap mainLev4List(SearchMap searchMap) {
		
		System.out.println("YEAR============>"+YEAR);
		System.out.println("MON============>"+MON);
		
		if("".equals(searchMap.getString("findYear"))) {
			searchMap.put("findYear", YEAR);
		}
		
		if("".equals(searchMap.getString("findMon"))) {
			searchMap.put("findMon", MON);
		}
		
		if("".equals(searchMap.getString("findAnalCycle"))) {
			searchMap.put("findAnalCycle", ANAL_CYCLE);
		}
		
		/**********************************
         * 로그인 사용자 조회
         **********************************/
		LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	searchMap.put("userId", loginVO.getUser_id());
    	searchMap.put("scDeptId", loginVO.getSc_dept_id());
    	searchMap.put("scDeptGrpId", loginVO.getSc_dept_grp_id());

		/**********************************
		 * 사용자지정 지표 목록
		 **********************************/
		searchMap.addList("userMetricList", getList("bsc.mon.main.getUserMetric", searchMap));
		
		searchMap.addList("findYear", searchMap.get("findYear"));
		searchMap.addList("findMon", searchMap.get("findMon"));
		searchMap.addList("findAnalCycle", searchMap.get("findAnalCycle"));
		
		String scDeptCompNm = (String)getStr("bsc.mon.main.getScDeptCompNm", searchMap);
		searchMap.put("scDeptCompNm", scDeptCompNm);
		
		/**********************************
         * 공지사항 팝업
         **********************************/
		if(!"".equals(searchMap.get("loginId"))){ //최초 로그인 시에만 나타나도록 처리
			searchMap.addList("popupList", getList("bsc.mon.main.getPopupList", searchMap));
		}
		
		return searchMap;
	}
	
	/**
	 * 조직점수 게이지 차트 데이터 조회(xml)
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap deptScoreChart_xml(SearchMap searchMap) {
		if("".equals(searchMap.getString("findYear"))) {
			searchMap.put("findYear", searchMap.getString("year"));
		}
		
		String topScDeptId = (String)getStr("bsc.module.commModule.getTopScDeptId", searchMap);
		searchMap.put("scDeptId", topScDeptId);
		
		searchMap.addList("signals", getList("bsc.mon.main.getSignalList", searchMap));
		searchMap.addList("deptScore", getStr("bsc.mon.main.getDeptScore", searchMap));
		return searchMap;
	}
	
	/**
	 * 조직성과현황
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap govMetricScoreList_xml(SearchMap searchMap) {

		/**********************************
         * 전사지표 현황
         **********************************/
		searchMap.put("scDeptId", getStr("bsc.module.commModule.getTopScDeptId", searchMap));
		
		searchMap.addList("list", getList("bsc.mon.main.getGovScoreList", searchMap));
		
		return searchMap;
	}

	/**
	 * 비교조직 차트 데이터 조회(xml)
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap compareDeptChart_xml(SearchMap searchMap) {
		String searchType = searchMap.getString("searchType");
		
		if("lev2".equals(searchType)){
			/**********************************
			 * 2차사업소/팀
			 **********************************/
			String secondCnt = StaticUtil.nullToDefault((String)getStr("bsc.mon.main.getSecondCompareCnt", searchMap),"0");
			String teamCnt = StaticUtil.nullToDefault((String)getStr("bsc.mon.main.getTeamSecondCompareCnt", searchMap),"0");
			
			if(!"0".equals(secondCnt)){
				searchMap.addList("deptCompare", getList("bsc.mon.main.getSecondCompare", searchMap));
			}else{
				searchMap.addList("deptCompare", getList("bsc.mon.main.getTeamSecondCompare", searchMap));
			}
		}else if("lev3f".equals(searchType)){
			searchMap.addList("deptCompare", getList("bsc.mon.main.getSecondCompare", searchMap));
		}else if("lev3s".equals(searchType)){
			searchMap.addList("deptCompare", getList("bsc.mon.main.getTeamSecondCompare", searchMap));
		}else if("lev4f".equals(searchType)){
			searchMap.addList("deptCompare", getList("bsc.mon.main.getSecondCompare", searchMap));
		}else if("lev4s".equals(searchType)){
			searchMap.addList("deptCompare", getList("bsc.mon.main.getTeamCompare", searchMap));
		}else if("SC_DEPT_GRP".equals(searchType)) {
			searchMap.addList("deptCompare", getList("bsc.mon.main.getDeptCompare", searchMap));
		} else if("UP_SC_DEPT_ID".equals(searchType)) {
			searchMap.addList("deptCompare", getList("bsc.mon.main.getSubDeptCompare", searchMap));
		} else if("SECOND_SC_DEPT_GRP".equals(searchType)) {
			searchMap.addList("deptCompare", getList("bsc.mon.main.getSecondDeptCompare", searchMap));
		} else if ("GOV_METRIC".equals(searchType)) {
			searchMap.addList("deptCompare", getList("bsc.mon.main.getSecondDeptCompare", searchMap));
		}
		
        		
		return searchMap;
	}
	
	/**
	 * 정부경영평가 5년 차트 데이터 조회(xml)
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap govMetricChart_xml(SearchMap searchMap) {
		
		if("".equals(searchMap.getString("findGovMetricId")) || null == searchMap.getString("findGovMetricId")){
			searchMap.put("findGovMetricId", getStr("bsc.mon.main.getGovMetricGraphId", searchMap));
		}
		
		searchMap.addList("govMetric", getDetail("bsc.mon.main.getGovMetric", searchMap));
		
		return searchMap;
	}

	/**
	 * 사용자지정 지표실적 차트 데이터 조회(xml)
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap userMetricChart_xml(SearchMap searchMap) {
		
		//searchMap.addList("metricActual", getDetail("bsc.mon.main.getUserMetricActual", searchMap));
		searchMap.addList("metricActual", getDetail("bsc.mon.main.getUserMetricActualMain", searchMap));
		
		searchMap.addList("metricInfo", getDetail("bsc.module.commModule.getMetricInfo", searchMap));
		
		return searchMap;
	}
	
	/**
	 * 사용자지정 정평지표실적 차트 데이터 조회(xml)
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap userGovMetricChart_xml(SearchMap searchMap) {
		
		searchMap.addList("metricActual", getDetail("bsc.mon.main.getUserGovMetricActual", searchMap));
		
		searchMap.addList("metricInfo", getDetail("bsc.module.commModule.getGovMetricInfo", searchMap));
		
		return searchMap;
	}
	
	
	/**
	 * 조직별 지표 리스트 팝업
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap popDeptMetricList(SearchMap searchMap) {
		/**********************************
         * 부서명 조회
         **********************************/
		searchMap.addList("scDeptNm", getStr("bsc.mon.main.getScDeptNm", searchMap));
		
		return searchMap;
	}
	
	/**
	 * 조직별 지표 리스트 팝업
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap deptScoreList_xml(SearchMap searchMap) {

		/**********************************
         * 부서지표 현황
         **********************************/
		searchMap.addList("list", getList("bsc.mon.main.getScDeptScoreList", searchMap));
		
		return searchMap;
	}

	/**
	 * 조직별 비교지표 리스트 팝업
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap deptScoreCompList_xml(SearchMap searchMap) {

		/**********************************
         * 부서지표 현황
         **********************************/
		searchMap.addList("list", getList("bsc.mon.main.getScDeptScoreCompList", searchMap));
		
		return searchMap;
	}
	
	/**
	 * 연계지표 리스트 팝업
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap popMetricMatrixList(SearchMap searchMap) {
		
		return searchMap;
	}
	
	/**
	 * 연계지표 리스트 조회(xml)
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap popMetricMatrixList_xml(SearchMap searchMap) {
		
		searchMap.addList("list", getList("bsc.mon.main.getMetricMatrixList", searchMap));
		
		return searchMap;
	}	
	
	/**
	 * 상위조직별 지표 리스트 팝업
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap upDeptScoreList_xml(SearchMap searchMap) {
		
		LoginVO loginVO = (LoginVO)searchMap.get("loginVO");		
		
		String upScDeptId = searchMap.getString("upScDeptId");    	
		searchMap.put("scDeptId", upScDeptId);

		if("".equals(searchMap.getString("scDeptId"))){
			searchMap.put("scDeptId", loginVO.getSc_dept_id());			
		}
		
		/**********************************
         * 부서지표 현황
         **********************************/
		searchMap.addList("list", getList("bsc.mon.main.getScDeptScoreList", searchMap));
		
		return searchMap;
	}
	
	/**
	 * 상위조직별 지표 리스트 팝업
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap upDeptScore2List_xml(SearchMap searchMap) {
		
		LoginVO loginVO = (LoginVO)searchMap.get("loginVO");		
		
		searchMap.put("scDeptId", loginVO.getUp_sc_dept_id());			
		
		/**********************************
         * 부서지표 현황
         **********************************/
		searchMap.addList("list", getList("bsc.mon.main.getScDeptScoreList", searchMap));
		
		return searchMap;
	}	
	
	/**
	 * 조직성과현황
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap topDeptScoreList_xml(SearchMap searchMap) {
		if("".equals(searchMap.getString("findYear"))) {
			searchMap.put("findYear", YEAR);
		}
		
		if("".equals(searchMap.getString("findMon"))) {
			searchMap.put("findMon", MON);
		}
		
		searchMap.put("analCycle", ANAL_CYCLE);

		/**********************************
         * 전사지표 현황
         **********************************/
		searchMap.put("scDeptId", getStr("bsc.module.commModule.getTopScDeptId", searchMap));
		//searchMap.put("scDeptId", "D005000"); //임시
		searchMap.addList("list", getList("bsc.mon.main.getScDeptScoreList", searchMap));
		
		return searchMap;
	}
	
    /**
     * QnA 자료실 팝업
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap boardPopup(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.board.board.getDetail", searchMap));
    	}
    	
    	/**********************************
         * 첨부파일
         **********************************/
    	searchMap.addList("fileList", getList("bsc.board.board.getFileList", searchMap));
        
        return searchMap;
    }	
	
	/**
	 * 더보기 팝업
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap moreList(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("NOTICE".equals(stMode)) {
    		searchMap.addList("moreList",getList("bsc.mon.main.getNoticeList", searchMap));
    		
    	}else{
    		searchMap.addList("bbsList", getList("bsc.mon.main.getBbsList", searchMap));
    		
    	}
		
		return searchMap;
	}
	
	/**
	 * 비교조직선택 팝업
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap compareDeptList(SearchMap searchMap) {
		
		return searchMap;
	}
	
	/**
	 * 비교조직선택 팝업 조회(xml)
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap compareDeptList_xml(SearchMap searchMap) {
		
		searchMap.addList("scDeptList",getList("bsc.mon.main.getCompareDeptList", searchMap));
		return searchMap;
	}
	
	/**
	 * 주요지표선택 팝업
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap userMetricList(SearchMap searchMap) {
		return searchMap;
	}
	
	/**
	 * 주요지표선택 팝업 조회(xml)
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap userMetricList_xml(SearchMap searchMap) {
		
		searchMap.addList("metricList",getList("bsc.mon.main.getMetricList", searchMap));
		
		return searchMap;
	}

    /**
     * 사용자지정지표 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap mainProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        searchMap.put("mode", stMode);

        /**********************************
         * 등록/삭제
         **********************************/
        if("ADD_METRIC".equals(stMode)) {
            searchMap = insertDB(searchMap);
        } 

        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 사용자지정지표 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = insertData("bsc.mon.main.deleteMetricData", searchMap);
	        
        	String[] metricIds = searchMap.getString("metricIds").split("\\|", 0);
        	
        	if(metricIds[0] == null || "".equals(metricIds[0])) {
        		
        	}else {
	        	if(metricIds != null && 0 < metricIds.length) {
		        	for (int i = 0; i < metricIds.length; i++) {
		        		searchMap.put("metricId", metricIds[i]);
		        		returnMap = insertData("bsc.mon.main.insertMetricData", searchMap);
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
     * 사용자지정지표 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    

        try {
        	setStartTransaction();

        	if("ADD_DEPT".equals(searchMap.getString("mode"))){
        		returnMap = updateData("bsc.mon.main.deleteDeptData", searchMap, true);
        	}else{
        		returnMap = updateData("bsc.mon.main.deleteMetricData", searchMap, true);
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
     * 계산된 지표관리 하위 지표 조회 팝업
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popUserMetric(SearchMap searchMap) {
        
    	searchMap.addList("treeList", getList("bsc.module.commModule.getScDeptList", searchMap));
        
    	searchMap.addList("topCodeInfo", getDetail("bsc.module.commModule.getTopScDeptInfo", searchMap));
    	
    	//searchMap.addList("userMetricList", getList("bsc.mon.main.getUserMetricList", searchMap));
    	searchMap.addList("userMetricList", getList("bsc.mon.main.getUserMetricListMain", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 계산된 지표관리 하위 지표 조회 팝업
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popUserGovMetric(SearchMap searchMap) {
        
    	searchMap.addList("topCodeInfo", getDetail("bsc.module.commModule.getTopScDeptInfo", searchMap));
    	
    	searchMap.addList("govMetricList", getList("bsc.mon.main.getGovMetricList", searchMap));
    	
    	searchMap.addList("userGovMetricList", getList("bsc.mon.main.getUserGovMetricList", searchMap));
    	
        return searchMap;
    }
    
    /**
	 * 인사 개인KPI 리스트 조회(xml)
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap insaUserKpiList_xml(SearchMap searchMap) {
		
		searchMap.addList("list",getList("bsc.mon.main.getInsaUserKpiList", searchMap));
		
		return searchMap;
	}
    
	/**
	 * 인사 개인KPI 상세 리스트 팝업
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap popInsaUserKpiDetailList(SearchMap searchMap) {
		
		searchMap.addList("taskNm", (String)getStr("bsc.mon.main.getUserTaskNm", searchMap));
		
		return searchMap;
	}
	
	/**
	 * 인사 개인KPI 리스트 조회(xml)
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap insaUserKpiDetailList_xml(SearchMap searchMap) {
		
		searchMap.addList("list",getList("bsc.mon.main.getInsaUserKpiDetailList", searchMap));
		
		return searchMap;
	}
	
	/**
	 * 조직성과 상세 팝업 조회 - 지표연계표(xml)
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap deptScoreMatrixList_xml(SearchMap searchMap) {
		
		searchMap.addList("list",getList("bsc.mon.main.getScDeptMatrixScoreList", searchMap));
		return searchMap;
	}
    
	public SearchMap RotateCube(SearchMap searchMap) {
		return searchMap;
	}
	
	/**
	 * 임원 조직성과 대쉬보드
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap scDeptDiagMngMain_xml(SearchMap searchMap) {
		searchMap.addList("scDeptDiagMngMain", getList("bsc.mon.main.scDeptDiagMngMain", searchMap));
		return searchMap;
	}

}
