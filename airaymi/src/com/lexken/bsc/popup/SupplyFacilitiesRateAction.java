/*************************************************************************
* CLASS 명      : SupplyFacilitiesRateAction
* 작 업 자      : 김기현
* 작 업 일      : 2012년 6월 18일
* 기    능      : 시스템항목관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    김기현      시스템항목관리            최 초 작 업
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

public class SupplyFacilitiesRateAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 시스템항목관리 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap supplyFacilitiesRateList(SearchMap searchMap) {
    	if ("".equals(StaticUtil.nullToBlank((String)searchMap.get("findDeptId")))) {
			searchMap.put("findDeptId", getStr("bsc.popup.supplyFacilitiesRate.getScDeptMapping", searchMap));
		}

    	searchMap.addList("getDeptList", getList("bsc.popup.supplyFacilitiesRate.getDeptList", searchMap));

    	searchMap.addList("itemInfo", getList("bsc.popup.supplyFacilitiesRate.getItem", searchMap));

        return searchMap;
    }

    /**
     * 시스템항목관리 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap supplyFacilitiesRateList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("bsc.popup.supplyFacilitiesRate.getList", searchMap));

        return searchMap;
    }

    /**
     * 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap excel(SearchMap searchMap) {
    	String excelFileName = "허가및공급시설검사처리율";
    	String excelTitle = "허가및공급시설검사처리율";

    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();

    	excelSearchInfoList.add(new ExcelVO("년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("월", (String)searchMap.get("monNm")));
    	excelSearchInfoList.add(new ExcelVO("부서", (String)searchMap.get("deptNm")));
    	excelSearchInfoList.add(new ExcelVO("항목", (String)searchMap.get("itemNm")));

/*
		colNames     :['년', '월', '부서코드', '부서', '검사구분', '허가구분', '업소번호', '시설명', '주소', '검사예정일', '검사실시일', '비고'],
		colModel     :[
						{name:'YEAR'  		 		 ,index:'YEAR'  		   ,width:50   ,align:'center' },
						{name:'MON'  		 		 ,index:'MON'  			   ,width:50   ,align:'center' },
						{name:'DEPT_CD'  		 	 ,index:'DEPT_CD'  		   ,width:50   ,align:'center' },
						{name:'DEPT_NM'  		 	 ,index:'DEPT_NM'  		   ,width:50   ,align:'center' },
						{name:'INSP_NM'  			 ,index:'INSP_NM'  	 	   ,width:50   ,align:'center' },
						{name:'PERM_TC_NM'  		 ,index:'PERM_TC_NM'  	   ,width:50   ,align:'center' },
						{name:'CO_NO'      		 	 ,index:'CO_NO'		       ,width:60   ,align:'center' },
                        {name:'EQUI_NM'      		 ,index:'EQUI_NM'	       ,width:60   ,align:'center' },
                        {name:'ADDR'      		 	 ,index:'ADDR'	     	   ,width:90   ,align:'left' },
                        {name:'INSP_PLAN_DATE'       ,index:'INSP_PLAN_DATE'   ,width:50   ,align:'center' },
                        {name:'REAL_INSP_DATE'       ,index:'REAL_INSP_DATE'   ,width:50   ,align:'center' },
						{name:'RMK' 			      ,index:'RMK'   ,width:50   ,align:'center' }
*/

    	/****************************************************************************************************
    	 * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
    	 ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("년도","YEAR", "center"));
    	excelInfoList.add(new ExcelVO("월", "MON", "center"));
		excelInfoList.add(new ExcelVO("검사구분", "INSP_NM", "center"));
		excelInfoList.add(new ExcelVO("허가구분", "PERM_TC_NM", "center"));
		excelInfoList.add(new ExcelVO("업소번호", "CO_NO", "center"));
		excelInfoList.add(new ExcelVO("시설명", "EQUI_NM", "left"));
		excelInfoList.add(new ExcelVO("주소", "ADDR", "left"));
		excelInfoList.add(new ExcelVO("검사예정일", "INSP_PLAN_DATE", "center"));
		excelInfoList.add(new ExcelVO("검사실시일", "REAL_INSP_DATE", "center"));
		excelInfoList.add(new ExcelVO("비고", "RMK", "left"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);

    	searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.supplyFacilitiesRate.getList", searchMap));

    	return searchMap;
    }

}
