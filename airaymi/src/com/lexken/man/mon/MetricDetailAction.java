/*************************************************************************
* CLASS 명      : MetricDetailAction
* 작 업 자      : 이상민
* 작 업 일      : 2013년 7월 04일 
* 기    능      : 이행성과지표 상세
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    이상민      2013년 7월 04일        최 초 작 업 
**************************************************************************/
package com.lexken.man.mon;

import java.util.ArrayList;
import java.util.HashMap;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.StringConstants;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.struts2.IsBoxActionSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.util.StaticUtil;

public class MetricDetailAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 이행성과지표 상세 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricDetailList(SearchMap searchMap) {

    	searchMap.addList("managementList", getList("man.mon.MetricDetail.getManagement", searchMap));
    	
    	searchMap.addList("stratSubjectList", getList("man.mon.MetricDetail.getStratSubjectList", searchMap));
    	
    	searchMap.addList("csfList", getList("man.mon.MetricDetail.getCsfList", searchMap));
    	
    	
        return searchMap;
    }
    
    /**
     * 경영목표 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap manList_ajax(SearchMap searchMap) {
    	
    	searchMap.addList("manList", getList("man.mon.MetricDetail.getManagement", searchMap));
    	
    	return searchMap;
    }
   
    /**
     * 전략과제 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap stratSubjectList_ajax(SearchMap searchMap) {
        
    	searchMap.addList("stratSubjectList", getList("man.mon.MetricDetail.getStratSubjectList", searchMap));

        return searchMap;
    }
    
    /**
     * CSF 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap csfList_ajax(SearchMap searchMap) {
        
    	searchMap.addList("csfList", getList("man.mon.MetricDetail.getCsfList", searchMap));

        return searchMap;
    }
    
    /**
     * 이행성과지표 상세 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricDetailList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("man.mon.MetricDetail.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 이행성과지표 상세 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap metricDetailListExcel(SearchMap searchMap) {
    	String excelFileName = "KPI2020KPI 상세";
    	String excelTitle = "KPI2020KPI 상세 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	excelSearchInfoList.add(new ExcelVO("평가년월", (String)searchMap.get("yearNm") + " " + (String)searchMap.get("monNm")));
    	excelSearchInfoList.add(new ExcelVO("전략과제", StaticUtil.nullToDefault((String)searchMap.get("stratSubjectNm"), "전체")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("경영목표", "MAN_NM", "left"));
    	excelInfoList.add(new ExcelVO("전략과제", "STRAT_NM", "left"));
    	excelInfoList.add(new ExcelVO("CSF", "CSF_NM", "left"));
    	excelInfoList.add(new ExcelVO("KGS2020KPI", "MAN_KPI_NM", "left"));
    	excelInfoList.add(new ExcelVO("담당조직", "DEPT_KOR_NM", "left"));
    	excelInfoList.add(new ExcelVO("담당자", "INSERT_USER_NM", "left"));
    	excelInfoList.add(new ExcelVO("주기", "CYCLE_NM", "left"));
    	excelInfoList.add(new ExcelVO("단위", "UNIT_NM", "left"));
    	excelInfoList.add(new ExcelVO("목표", "TGT_VALUE", "left"));
    	excelInfoList.add(new ExcelVO("실적", "VALUE", "left"));
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("man.mon.MetricDetail.getList", searchMap));
    	
        return searchMap;
    }
    
}
