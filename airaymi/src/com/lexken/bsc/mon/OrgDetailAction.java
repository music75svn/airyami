/*************************************************************************
* CLASS 명      : OrgDetailAction
* 작 업 자      : 정철수
* 작 업 일      : 2012년 8월 20일 
* 기    능      : 조직상세
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  --------  -----------------  -------------------------    --------
*   1    정철수      2012년 8월 20일             최 초 작 업 
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
import com.lexken.framework.util.StaticUtil;

public class OrgDetailAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 조직상세 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap orgDetailList(SearchMap searchMap) {
    	
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	String findScDeptId = (String)searchMap.getString("findScDeptId", "");
    	String findBscMetricGbn = (String)searchMap.getString("findBscMetricGbn", "01");
    	
    	if("".equals(findScDeptId)){
	    	searchMap.put("findScDeptId", loginVO.getSc_dept_id());
	    	searchMap.put("findScDeptNm", loginVO.getSc_dept_nm());
    	}
    	
    	searchMap.put("findBscMetricGbn", findBscMetricGbn);
    	
    	/************************************************************************************
    	 * 평가조직 트리 조회
    	 ************************************************************************************/
        searchMap.addList("treeList", getList("bsc.module.commModule.getScDeptList", searchMap));
    	
        /*
    	ArrayList tmpList = (ArrayList)getList("bsc.mon.orgDetail.getAttachList", searchMap);
    	
    	if(null != tmpList && 0 < tmpList.size() ){
    		HashMap tmpHash = (HashMap)tmpList.get(0);
    		searchMap.put("attachFileCnt", 1 );
    		/*
    		try {
    		searchMap.put("attachFileNm", URLEncoder.encode( (String)tmpHash.get("ATTACH_FILE_NM") , "utf-8") );
    		searchMap.put("attachFileSuffix", URLEncoder.encode( (String)tmpHash.get("ATTACH_FILE_SUFFIX") , "utf-8") );
    		searchMap.put("attachFilePath", URLEncoder.encode( (String)tmpHash.get("ATTACH_FILE_PATH") , "utf-8") );
    		}catch( UnsupportedEncodingException e ){
    			searchMap.put("attachFileNm", (String)tmpHash.get("ATTACH_FILE_NM") );
        		searchMap.put("attachFileSuffix", (String)tmpHash.get("ATTACH_FILE_SUFFIX") );
        		searchMap.put("attachFilePath", (String)tmpHash.get("ATTACH_FILE_PATH") );
    			e.printStackTrace();
    		}
    		*/
    		
    	/*	searchMap.put("attachFileNm", (String)tmpHash.get("ATTACH_FILE_NM") );
    		searchMap.put("attachFileSuffix", (String)tmpHash.get("ATTACH_FILE_SUFFIX") );
    		searchMap.put("attachFilePath", (String)tmpHash.get("ATTACH_FILE_PATH") );
    		
    	} else {
    		searchMap.put("attachFileCnt", 0 );
    	}
    	*/
        searchMap.addList("signalList", getList("bsc.base.signalStatus.getList", searchMap));
        
        return searchMap;
    }
    
    /**
     * 조직상세 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap orgDetailList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.mon.orgDetail.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 조직상세 목표합의서 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap orgDetailAttachList_xml(SearchMap searchMap) {
        
        searchMap.addList("attachList", getList("bsc.mon.orgDetail.getAttachList", searchMap));
        
        return searchMap;
    }
    
    /**
     * 조직상세 조직 KPI 상태신호 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap orgDetailSignalCnt_xml(SearchMap searchMap) {
    
    	searchMap.addList("signalCntList", getList("bsc.mon.orgDetail.getOrgSignalCnt", searchMap));
    	String temp = searchMap.getDefaultValue("signalCntList", "TOT_CNT", 0);
    	
    	searchMap.put("signalTotalCnt", temp);

        return searchMap;
    }
    
    /**
     * 조직상세 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap orgDetailListExcel(SearchMap searchMap) {
    	String excelFileName = "조직상세";
    	String excelTitle = "조직상세 리스트";
    	
    	String year = (String)searchMap.getString("year", "");
    	String mon = (String)searchMap.getString("mon", "");
    	String scDeptId = (String)searchMap.getString("scDeptId", "");
    	String scDeptNm = (String)searchMap.getString("scDeptNm", "");
    	String analCycle = (String)searchMap.getString("analCycle", "");
    	String bscMetricGbn = (String)searchMap.getString("bscMetricGbn", "");
    	
    	searchMap.put("findYear", year);
    	searchMap.put("findMon", mon);
	    searchMap.put("findScDeptId", scDeptId);
	    searchMap.put("findScDeptNm", scDeptNm);
	    searchMap.put("findAnalCycle", analCycle);
	    searchMap.put("findBscMetricGbn", bscMetricGbn);
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEARMON, (String)searchMap.get("yearNm")+(String)searchMap.get("monNm") ));
    	excelSearchInfoList.add(new ExcelVO(StringConstants.SC_DEPT_NM, (String)searchMap.get("findScDeptNm") ));
    	excelSearchInfoList.add(new ExcelVO(StringConstants.METRIC_NM + "유형", (String)searchMap.get("bscMetricGbnNm") ));
    	excelSearchInfoList.add(new ExcelVO("분석주기", StaticUtil.nullToDefault((String)searchMap.get("analCycleNm"), "누적") ));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	
    	excelInfoList.add(new ExcelVO(StringConstants.PERSPECTIVE_NM, "PERSPECTIVE_NM", "left"));
    	excelInfoList.add(new ExcelVO(StringConstants.STRATEGY_NM, "STRATEGY_NM", "left", 7000));
    	excelInfoList.add(new ExcelVO(StringConstants.METRIC_NM, "METRIC_F_NM", "left", 8000));
    	excelInfoList.add(new ExcelVO("주기", "EVAL_CYCLE_NM", "center"));
    	excelInfoList.add(new ExcelVO("가중치", "WEIGHT", "center"));
    	excelInfoList.add(new ExcelVO("단위", "UNIT_NM", "center"));
    	excelInfoList.add(new ExcelVO("목표", "TARGET", "right"));
        excelInfoList.add(new ExcelVO("실적", "ACTUAL", "right"));
    	excelInfoList.add(new ExcelVO("점수", "FINAL_SCORE", "right"));
    	excelInfoList.add(new ExcelVO("최종점수", "EXCSCORE", "right"));
    	excelInfoList.add(new ExcelVO("상태", "STATUS_NM", "center"));
    	
    	searchMap.put("excelFileName", excelFileName );
    	searchMap.put("excelTitle", excelTitle );
    	searchMap.put("excelSearchInfoList", excelSearchInfoList );
    	searchMap.put("excelInfoList", excelInfoList );
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.mon.orgDetail.getList", searchMap));
    	
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
