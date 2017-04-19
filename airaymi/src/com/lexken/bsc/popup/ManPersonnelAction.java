/*************************************************************************
* CLASS 명      : manPersonnel
* 작 업 자      : 안요한
* 작 업 일      : 2013년 08월 13일
* 기    능      : 공장 등록 심사 노력 실적
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    안요한      해외사업 수입            최 초 작 업
**************************************************************************/
package com.lexken.bsc.popup;

import java.sql.SQLException;
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

public class ManPersonnelAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 공장 등록 심사 노력 실적 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap manPersonnelList(SearchMap searchMap) {
    	
    	if ("".equals(StaticUtil.nullToBlank((String)searchMap.get("findDeptId")))) {
			searchMap.put("findDeptId", getStr("bsc.popup.manPersonnel.getScDeptMapping", searchMap));
		}
    	
        searchMap.addList("deptInfo", getList("bsc.popup.manPersonnel.getDeptInfo", searchMap));
    	
    	searchMap.addList("itemInfo", getList("bsc.popup.manPersonnel.getItem", searchMap));

        return searchMap;
    }

    /**
     * 공장 등록 심사 노력 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap manPersonnelList_xml(SearchMap searchMap) {
    	
    	
    	searchMap.addList("list", getList("bsc.popup.manPersonnel.getList", searchMap));
    		
    	
        return searchMap;
    }
    
    public SearchMap manPersonnelList1_xml(SearchMap searchMap) {
    	
    	
    	searchMap.addList("list", getList("bsc.popup.manPersonnel.getTotalList", searchMap));
    	
    	
    	return searchMap;
    }
    
    /**
     * 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap excel(SearchMap searchMap) {
    	String excelFileName = "공장등록심사노력";
    	String excelTitle = "공장등록심사노력";
    	
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
    	excelInfoList.add(new ExcelVO("년도","YEAR", "center"));
    	excelInfoList.add(new ExcelVO("월", "MON", "center"));
    	excelInfoList.add(new ExcelVO("부서명", "DEPT_NM", "left"));
    	
    	if (searchMap.get("findItemCd").equals("S040D001")) {	
    		excelInfoList.add(new ExcelVO("분야", "DIV", "center"));
    	} else {												// 배정액
    		excelInfoList.add(new ExcelVO("접수일자", "RCPT_DATE", "center"));
    		excelInfoList.add(new ExcelVO("구분", "INSP_KIND_TC_NM", "center"));
    		excelInfoList.add(new ExcelVO("가스법 구분", "LAW_TC_NM", "center"));
    		excelInfoList.add(new ExcelVO("시설명", "EQUI_NM", "center"));
    		excelInfoList.add(new ExcelVO("공장 주소", "LOCT_ADDR", "center"));
    		excelInfoList.add(new ExcelVO("부서명", "DEPT_NM", "center"));
    		excelInfoList.add(new ExcelVO("최종승인자", "LAST_INSP_EMPN", "center"));
    		excelInfoList.add(new ExcelVO("승인자명", "LAST_NM", "center"));
    		excelInfoList.add(new ExcelVO("최종승인일자", "LAST_INSP_DATE", "center"));
    		excelInfoList.add(new ExcelVO("인원", "COUNT", "center"));
    	}
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	
    	if (searchMap.get("findItemCd").equals("S040D001")) {
    		searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.manPersonnel.getList", searchMap));
    	} else {
    		searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.manPersonnel.getTotalList", searchMap));
    	}
    	
    	return searchMap;
    }

}
