/*************************************************************************
* CLASS 명      : MetricRankAction
* 작 업 자      : 정철수
* 작 업 일      : 2012년 8월 14일
* 기    능      : KPI순위
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  --------  -----------------  -------------------------    --------
*   1    정철수      2012년 8월 14일             최 초 작 업
**************************************************************************/
package com.lexken.bsc.mon;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.StringConstants;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginVO;
import com.lexken.framework.util.CalendarHelper;
import com.lexken.framework.util.StaticUtil;

public class MetricRankAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    //지표순위 월 설정
    //private static final String MON = (new CalendarHelper()).modifyDate(0, 0, 0,"MM");

    /**
     * KPI순위 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap metricRankList(SearchMap searchMap) {
			//searchMap.put("findMon", MON);
			searchMap.put("findMon",getStr("bsc.mon.metricRank.getMon", searchMap));

    	/**********************************
         * 로그인정보 (권한 체크)
         **********************************/
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");

    	searchMap.put("findMetricGrpNm",getStr("bsc.mon.metricRank.getMetricGrpNm", searchMap));

        return searchMap;
    }

    /**
     * KPI순위 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap metricRankList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("bsc.mon.metricRank.getList", searchMap));

        return searchMap;
    }

    /**
     * KPI순위 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap metricRankListExcel(SearchMap searchMap) {
    	String excelFileName = StringConstants.METRIC_NM + "순위";
    	String excelTitle = StringConstants.METRIC_NM + "순위 리스트";

    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();

    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEARMON, (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO(StringConstants.METRIC_NM, StaticUtil.nullToDefault((String)searchMap.get("findMetricNm"),"")));
    	excelSearchInfoList.add(new ExcelVO("평가그룹", (String)searchMap.get("scDeptGrpNm")));
    	excelSearchInfoList.add(new ExcelVO("조회구분", StaticUtil.nullToDefault((String)searchMap.get("schGbnNm"), "점수") ));
    	excelSearchInfoList.add(new ExcelVO("분석주기", StaticUtil.nullToDefault((String)searchMap.get("analCycleNm"), "당월") ));

    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("순위", "RANK", "center"));
    	excelInfoList.add(new ExcelVO(StringConstants.SC_DEPT_NM, "SC_DEPT_NM", "left"));
    	if( "SCORE".equals(StaticUtil.nullToDefault((String)searchMap.get("findSchGbn"), "SCORE")) ){
	    	excelInfoList.add(new ExcelVO("1월", "SCORE01", "center"));
	    	excelInfoList.add(new ExcelVO("2월", "SCORE02", "center"));
	    	excelInfoList.add(new ExcelVO("3월", "SCORE03", "center"));
	    	excelInfoList.add(new ExcelVO("4월", "SCORE04", "center"));
	    	excelInfoList.add(new ExcelVO("5월", "SCORE05", "center"));
	    	excelInfoList.add(new ExcelVO("6월", "SCORE06", "center"));
	    	excelInfoList.add(new ExcelVO("7월", "SCORE07", "center"));
	    	excelInfoList.add(new ExcelVO("8월", "SCORE08", "center"));
	    	excelInfoList.add(new ExcelVO("9월", "SCORE09", "center"));
	    	excelInfoList.add(new ExcelVO("10월", "SCORE10", "center"));
	    	excelInfoList.add(new ExcelVO("11월", "SCORE11", "center"));
	    	excelInfoList.add(new ExcelVO("12월", "SCORE12", "center"));
    	} else {
	    	excelInfoList.add(new ExcelVO("1월", "STATUS_01_NM", "center"));
	    	excelInfoList.add(new ExcelVO("2월", "STATUS_02_NM", "center"));
	    	excelInfoList.add(new ExcelVO("3월", "STATUS_03_NM", "center"));
	    	excelInfoList.add(new ExcelVO("4월", "STATUS_04_NM", "center"));
	    	excelInfoList.add(new ExcelVO("5월", "STATUS_05_NM", "center"));
	    	excelInfoList.add(new ExcelVO("6월", "STATUS_06_NM", "center"));
	    	excelInfoList.add(new ExcelVO("7월", "STATUS_07_NM", "center"));
	    	excelInfoList.add(new ExcelVO("8월", "STATUS_08_NM", "center"));
	    	excelInfoList.add(new ExcelVO("9월", "STATUS_09_NM", "center"));
	    	excelInfoList.add(new ExcelVO("10월", "STATUS_10_NM", "center"));
	    	excelInfoList.add(new ExcelVO("11월", "STATUS_11_NM", "center"));
	    	excelInfoList.add(new ExcelVO("12월", "STATUS_12_NM", "center"));
    	}

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.mon.metricRank.getList", searchMap));

        return searchMap;
    }

    /**
     * 평가그룹 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap scDeptGrp_ajax(SearchMap searchMap) {
    	searchMap.addList("scDeptGrpList", getList("bsc.mon.orgRank.getScDeptGrp", searchMap));

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


}
