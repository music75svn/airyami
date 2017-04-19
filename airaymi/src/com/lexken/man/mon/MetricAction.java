/*************************************************************************
* CLASS 명 		: MetricAction
* 작 업 자			:  이상민
* 작 업 일			: 2013년07월05일 
* 기    능			: 이행성과지표정의서
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    이상민      2013년07월05일         최 초 작 업 
**************************************************************************/
package com.lexken.man.mon;
    
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
import com.lexken.framework.util.HtmlHelper;
import com.lexken.framework.util.StaticUtil;

public class MetricAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * KGS2020KPI 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricList(SearchMap searchMap) {

    	searchMap.addList("managementList", getList("man.mon.Metric.getManagement", searchMap));
    	
    	searchMap.addList("stratSubjectList", getList("man.mon.Metric.getStratSubjectList", searchMap));
    	
    	searchMap.addList("csfList", getList("man.mon.Metric.getCsfList", searchMap));
    	
    	
        return searchMap;
    }
    
    /**
     * KGS2020KPI 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricList_xml(SearchMap searchMap) {
    	
    	searchMap.addList("list", getList("man.mon.Metric.getList", searchMap));
    	
    	return searchMap;
    }

    /**
     * 경영목표 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap manList_ajax(SearchMap searchMap) {
        
    	searchMap.addList("manList", getList("man.mon.Metric.getManagement", searchMap));

        return searchMap;
    }

    /**
     * 전략과제 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap stratSubjectList_ajax(SearchMap searchMap) {
    	
    	searchMap.addList("stratSubjectList", getList("man.mon.Metric.getStratSubjectList", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * CSF 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap csfList_ajax(SearchMap searchMap) {
        
    	searchMap.addList("csfList", getList("man.mon.Metric.getCsfList", searchMap));

        return searchMap;
    }
    
    /**
     * 이행성과지표정의서 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popMetricList(SearchMap searchMap) {

    	/**********************************
         * 세부과제 상세조회
         **********************************/
    	HashMap detail = new HashMap();
    	detail = getDetail("man.mon.Metric.getDetail", searchMap);
        searchMap.addList("detail", detail);
    	
    	ArrayList calTypeColList = new ArrayList();
        calTypeColList = (ArrayList)getList("man.mon.Metric.calTypeColList", searchMap); 
        searchMap.addList("calTypeColList", calTypeColList);
      
        /*지표 산식이 계량일 때 산식을 구성해서 calTypeColDesc에 담아준다.*/
	String typeId = "01";
	    /*KGS2020KPI는 계량 산식 단일 값으로 01이 값이 들어간다.*/
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
    	
        return searchMap;
    }
    
    /**
     * KGS2020KPI 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap metricListExcel(SearchMap searchMap) {
    	String excelFileName = "KGS2020KPI";
    	String excelTitle = "KGS2020KPI 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	excelSearchInfoList.add(new ExcelVO("평가년월", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("경영목표", StaticUtil.nullToDefault((String)searchMap.get("managementNm"), "전체")));
    	excelSearchInfoList.add(new ExcelVO("전략과제", StaticUtil.nullToDefault((String)searchMap.get("stratSubjectNm"), "전체")));
    	excelSearchInfoList.add(new ExcelVO("CSF", StaticUtil.nullToDefault((String)searchMap.get("csfNm"), "전체")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("경영목표", 	"MAN_NM", 					"left"));
    	excelInfoList.add(new ExcelVO("전략과제", 	"STRAT_NM", 				"left"));
    	excelInfoList.add(new ExcelVO("CSF", 		"CSF_NM", 					"left"));
    	excelInfoList.add(new ExcelVO("KGS2020KPI", "MAN_KPI_NM", 				"left"));
    	excelInfoList.add(new ExcelVO("누적기여도", "CUM_CONTRIBUTION", 		"center"));
    	excelInfoList.add(new ExcelVO("1월",  		"MON01", 					"center"));
    	excelInfoList.add(new ExcelVO("2월",  		"MON02", 					"center"));
    	excelInfoList.add(new ExcelVO("3월",  		"MON03", 					"center"));
    	excelInfoList.add(new ExcelVO("4월",  		"MON04", 					"center"));
    	excelInfoList.add(new ExcelVO("5월",  		"MON05", 					"center"));
    	excelInfoList.add(new ExcelVO("6월",  		"MON06", 					"center"));
    	excelInfoList.add(new ExcelVO("7월",  		"MON07", 					"center"));
    	excelInfoList.add(new ExcelVO("8월",  		"MON08", 					"center"));
    	excelInfoList.add(new ExcelVO("9월",  		"MON09", 					"center"));
    	excelInfoList.add(new ExcelVO("10월", 		"MON10", 					"center"));
    	excelInfoList.add(new ExcelVO("11월", 		"MON11", 					"center"));
    	excelInfoList.add(new ExcelVO("12월", 		"MON12", 					"center"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("man.mon.Metric.getList", searchMap));
    	
        return searchMap;
    }
}
