/*************************************************************************
* CLASS 명      : ResearchResultImproveSysAction
* 작 업 자      : 김기현
* 작 업 일      : 2013년 8월 11일 
* 기    능      : 시스템 연계데이터 조회
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    김기현      2013년 8월 11일             최 초 작 업 
**************************************************************************/
package com.lexken.bsc.popup;
    
import java.util.ArrayList;
import java.util.HashMap;

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
import com.lexken.framework.util.StaticUtil;

public class ResearchResultImproveSysAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 시스템 연계데이터 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap researchResultImproveSysList(SearchMap searchMap) {
    	
    	String deptId = "";
    	if("".equals(StaticUtil.nullToBlank(deptId))) {
    		searchMap.addList("dept", getList("bsc.popup.researchResultImproveSys.getDept", searchMap));
    		
    		if("".equals(searchMap.getString("findDeptId"))){
    			searchMap.put("findDeptId", searchMap.getDefaultValue("dept", "DEPT_ID", 0));
    		}
    	}
    	
    	String flag = searchMap.getString("findFlag");
    	
    	if( "".equals(flag) ){
    		searchMap.put("findFlag", "01");
    	}
    	
    	//최상위 평가조직 조회
        if( "01".equals(searchMap.getString("findFlag")) ){
        	searchMap.addList("deptInfo", getList("bsc.popup.researchResultImproveSys.getDeptInfo", searchMap));
        }else {
        	searchMap.addList("deptInfo", getList("bsc.popup.researchResultImproveSys.getDeptTechInfo", searchMap));
        }
    	
    	searchMap.addList("itemInfo", getList("bsc.popup.researchResultImproveSys.getItem", searchMap));

        return searchMap;
    }
    
    /**
     * 시스템 연계데이터 제도개선 조회 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap researchResultImproveSysList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.popup.researchResultImproveSys.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 시스템 연계데이터 현장기술지원 조회 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap researchResultTechSuptList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.popup.researchResultImproveSys.getTechList", searchMap));

        return searchMap;
    }
    
    /**
     * 시스템 연계데이터 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap researchResultImproveSysProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("GET".equals(stMode)) {
        }
        
         return searchMap;
    }
    
    /**
     * 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap excel(SearchMap searchMap) {
    	String excelFileName = "연구성과활용건수(건)";
    	String excelTitle = "연구성과활용건수(건)";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO("년도", (String)searchMap.get("yearNm")));
    	//excelSearchInfoList.add(new ExcelVO("월", (String)searchMap.get("monNm")));
    	excelSearchInfoList.add(new ExcelVO("부서", (String)searchMap.get("deptNm")));
    	
    	if("01".equals((String)searchMap.get("findFlag"))){
    		excelSearchInfoList.add(new ExcelVO("연구활용", "제도개선"));
    	}else{
    		excelSearchInfoList.add(new ExcelVO("연구활용", "현장기술지원"));
    	}
    	
    	/****************************************************************************************************
    	 * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
    	 ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("년도","YEAR", "center"));
    	excelInfoList.add(new ExcelVO("월", "MON", "center"));
    	
    	if("01".equals((String)searchMap.get("findFlag"))){
    		excelInfoList.add(new ExcelVO("부서명", "DEPT_NM", "left"));
    		excelInfoList.add(new ExcelVO("활용일련번호", "REQ_SER", "center"));
    		excelInfoList.add(new ExcelVO("기술내용", "CJGT_CNTN", "center"));
    		excelInfoList.add(new ExcelVO("과제명", "SUBJ_NM", "center"));
    		excelInfoList.add(new ExcelVO("점수", "SCORE", "center"));
    		excelInfoList.add(new ExcelVO("기술담당자", "CJGT_EMPN_MN_NM", "center"));
    		excelInfoList.add(new ExcelVO("예상시작일", "CJGT_ST_DATE", "center"));
    		excelInfoList.add(new ExcelVO("예상종료일", "CJGT_END_DATE", "center"));
    	} else {										
    		excelInfoList.add(new ExcelVO("지원명", "AID_NM", "center"));
    		excelInfoList.add(new ExcelVO("지원요청부서", "RQST_DEPT_NM", "center"));
    		excelInfoList.add(new ExcelVO("요청담당자", "RQST_EMPN", "center"));
    		excelInfoList.add(new ExcelVO("지원부서", "AID_DEPT_NM", "center"));
    		excelInfoList.add(new ExcelVO("지원시작일", "AID_ST_DATE", "center"));
    		excelInfoList.add(new ExcelVO("지원종료일", "AID_END_DATE", "center"));
    		excelInfoList.add(new ExcelVO("지원시간", "AID_HOUR", "center"));
    		excelInfoList.add(new ExcelVO("점수", "SCORE", "center"));
    		excelInfoList.add(new ExcelVO("지원연구원", "AID_RSRC_EMPN", "center"));
    		excelInfoList.add(new ExcelVO("지원내용", "REVI_CNTN", "center"));
    		excelInfoList.add(new ExcelVO("지원주제", "SUBJ_NM", "center"));
    		excelInfoList.add(new ExcelVO("지원주제번호", "SUBJ_NO", "center"));
    	}
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	
    	if("01".equals((String)searchMap.get("findFlag"))){
    		searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.researchResultImproveSys.getList", searchMap));
    	} else {
    		searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.researchResultImproveSys.getTechList", searchMap));
    	}
    		
    	return searchMap;
    }
    
}
