/*************************************************************************
* CLASS 명      : CasualtyInspAction
* 작 업 자      : 김상용
* 작 업 일      : 2013년 8월 19일 
* 기    능      : 시스템 연계데이터 조회
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    김상용		  2013년 8월 19일		 최 초 작 업 
**************************************************************************/
package com.lexken.bsc.popup;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.util.StaticUtil;

public class CasualtyAction extends CommonService {

	private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 시스템 연계데이터 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap casualtyList(SearchMap searchMap) {
    	
    	if ("".equals(StaticUtil.nullToBlank((String)searchMap.get("findDeptId")))) {
			searchMap.put("findDeptId", getStr("bsc.popup.casualty.getScDeptMapping", searchMap));
		}
    	
    	//최상위 평가조직 조회
        searchMap.addList("deptInfo", getList("bsc.popup.casualty.getDeptInfo", searchMap));
        
        searchMap.addList("itemInfo", getList("bsc.popup.casualty.getItem", searchMap));

        return searchMap;
    }
    
    /**
     * 시스템 연계데이터 조회 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap casualtyList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.popup.casualty.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap excel(SearchMap searchMap) {
    	String excelFileName = "가스사고인명피해율감소성과";
    	String excelTitle = "가스사고인명피해율감소성과";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO("년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("월", (String)searchMap.get("monNm")));
    	excelSearchInfoList.add(new ExcelVO("부서", (String)searchMap.get("deptNm")));
    	excelSearchInfoList.add(new ExcelVO("항목", (String)searchMap.get("itemNm")));
    	
    	/****************************************************************************************************
    	 * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
    	 ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("발생일","ACCN_DATE", "center"));
    	excelInfoList.add(new ExcelVO("발생시간", "ACCN_HOUR", "center"));
    	excelInfoList.add(new ExcelVO("사건번호", "ACCN_NO", "left"));
    	excelInfoList.add(new ExcelVO("사건명", "ACCN_NM", "left"));
    	excelInfoList.add(new ExcelVO("주소1", "ADDR1", "left"));
    	excelInfoList.add(new ExcelVO("주소2", "ADDR2", "left"));
    	excelInfoList.add(new ExcelVO("사망자수", "DAMA_CNT", "left"));
    	excelInfoList.add(new ExcelVO("부상자수", "WOU_DEMA_CNT", "left"));
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.casualty.getList", searchMap));
    	
    	return searchMap;
    }
}
