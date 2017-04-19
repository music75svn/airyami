/*************************************************************************
* CLASS 명      : budExcEffect
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
import com.lexken.framework.common.StringConstants;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.struts2.IsBoxActionSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.util.StaticUtil;

public class BudExcEffectAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 시스템항목관리 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap budExcEffectList(SearchMap searchMap) {

    	if ("".equals(StaticUtil.nullToBlank((String)searchMap.get("findDeptId")))) {
			searchMap.put("findDeptId", getStr("bsc.popup.budExcEffect.getScDeptMapping", searchMap));
		}

    	if ("".equals(StaticUtil.nullToBlank((String)searchMap.get("findQuarter")))) {
    		if ("01".equals(searchMap.get("findMon")) || "02".equals(searchMap.get("findMon")) || "03".equals(searchMap.get("findMon"))) {
    			searchMap.put("findQuarter", "01");
    		} else if ("04".equals(searchMap.get("findMon")) || "05".equals(searchMap.get("findMon")) || "06".equals(searchMap.get("findMon"))) {
    			searchMap.put("findQuarter", "02");
    		} else if ("07".equals(searchMap.get("findMon")) || "08".equals(searchMap.get("findMon")) || "09".equals(searchMap.get("findMon"))) {
    			searchMap.put("findQuarter", "03");
    		} else if ("10".equals(searchMap.get("findMon")) || "11".equals(searchMap.get("findMon")) || "12".equals(searchMap.get("findMon"))) {
    			searchMap.put("findQuarter", "04");
    		}
		}

    	//최상위 평가조직 조회
        searchMap.addList("deptInfo", getList("bsc.popup.budExcEffect.getDeptInfo", searchMap));

        searchMap.addList("itemInfo", getList("bsc.popup.budExcEffect.getItem", searchMap));

        return searchMap;
    }

    /**
     * 시스템항목관리 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap budExcEffectList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("bsc.popup.budExcEffect.getList", searchMap));

        return searchMap;
    }

    /**
     * 부서 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap deptList_ajax(SearchMap searchMap) {

    	searchMap.addList("deptList", getList("bsc.popup.budExcEffect.getDeptList", searchMap));

    	if("".equals(searchMap.getString("findDeptId"))) {
        	searchMap.put("findDeptId", searchMap.getDefaultValue("deptList", "DEPT_ID", 0));
        }

        return searchMap;
    }

    /**
     * 시스템 연계데이터 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap budExcEffectProcess(SearchMap searchMap) {
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
    	String excelFileName = "예산집행효율성";
    	String excelTitle = "예산집행효율성";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO("년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("분기", (String)searchMap.get("quarterNm")));
    	excelSearchInfoList.add(new ExcelVO("부서", (String)searchMap.get("deptNm")));
    	excelSearchInfoList.add(new ExcelVO("항목", (String)searchMap.get("itemNm")));
    	
    	/****************************************************************************************************
    	 * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
    	 ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("부서명", "DEPT_NM", "left"));
		excelInfoList.add(new ExcelVO("계정과목", "ACCT_NM", "center"));
		excelInfoList.add(new ExcelVO("총예산", "BGT_TOT_AMT", "center"));
		excelInfoList.add(new ExcelVO("분기배정", "BGT_AMT", "center"));
		excelInfoList.add(new ExcelVO("분기집행", "BGT_RSLT", "center"));
		excelInfoList.add(new ExcelVO("분기집행률", "RATE", "center"));
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.budExcEffect.getList", searchMap));
    	
    	return searchMap;
    }

}
