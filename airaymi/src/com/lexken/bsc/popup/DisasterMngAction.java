/*************************************************************************
* CLASS 명      : disasterMng
* 작 업 자      : 안요한
* 작 업 일      : 2013년 08월 13일
* 기    능      : 양성교육 3년 평균수입+(3년 평균수입*10%)
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

public class DisasterMngAction extends CommonService {

    private static final long serialVersionUID = 1L;

    /**
     * 양성교육 3년 평균수입+(3년 평균수입*10%) 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap disasterMngList(SearchMap searchMap) {

    	if ("".equals(StaticUtil.nullToBlank((String)searchMap.get("findDeptId")))) {
        	
			searchMap.put("findDeptId", getStr("bsc.popup.disasterMng.getScDeptMapping", searchMap));
		}

        searchMap.addList("deptInfo", getList("bsc.popup.disasterMng.getDeptInfo", searchMap));

    	searchMap.addList("itemInfo", getList("bsc.popup.disasterMng.getItem", searchMap));

        return searchMap;
    }

    /**
     * 양성교육 3년 평균수입+(3년 평균수입*10%) 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap disasterMngList_xml(SearchMap searchMap) {

    	String itemCd = searchMap.getString("findItemCd");

    		searchMap.addList("list", getList("bsc.popup.disasterMng.getList", searchMap));

    	
        return searchMap;
    }


    /**
     * 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap excel(SearchMap searchMap) {
    	String excelFileName = "재난관리시설 안전점검 건수 실적";
    	String excelTitle = "재난관리시설 안전점검 건수 실적";

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

    	excelInfoList.add(new ExcelVO("업소번호", "CO_NO", "center"));
    	excelInfoList.add(new ExcelVO("시설코드", "EQUI_NO", "center"));
    	excelInfoList.add(new ExcelVO("업소명", "CO_NM", "center"));
    	excelInfoList.add(new ExcelVO("점검일자", "INSP_DATE", "center"));
    	excelInfoList.add(new ExcelVO("점검자", "KOR_NM", "center"));
    	excelInfoList.add(new ExcelVO("등급판정", "DANG_GRDE_TC", "center"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);

    	searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.disasterMng.getList", searchMap));
    	return searchMap;
    }
    
}
