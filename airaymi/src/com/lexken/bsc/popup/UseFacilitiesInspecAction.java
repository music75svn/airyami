/*************************************************************************
* CLASS 명      : UseFacilitiesInspecAction
* 작 업 자      : 안요한
* 작 업 일      : 2013년 7월 30일
* 기    능      : 가스사용시설 정기검사 횟수
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    안요한      가스사용시설 정기검사            최 초 작 업
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

public class UseFacilitiesInspecAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 사용시설 정기검사 실적 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap useFacilitiesInspecList(SearchMap searchMap) {
    	
    	
    	if ("".equals(StaticUtil.nullToBlank((String)searchMap.get("findDeptId")))) {
			searchMap.put("findDeptId", getStr("bsc.popup.useFacilitiesInspec.getScDeptMapping", searchMap));
		}

    	searchMap.addList("getDeptList", getList("bsc.popup.useFacilitiesInspec.getDeptList", searchMap));
    	
    	searchMap.addList("itemInfo", getList("bsc.popup.certificationProcess.getItem", searchMap));

        return searchMap;
    }

    /**
     * 시스템항목관리 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap useFacilitiesInspecList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("bsc.popup.useFacilitiesInspec.getList", searchMap));

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

        if((Integer)returnMap.get("ErrorNumber") < 0 ){
        	return returnMap;
        }

        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;
    }
    
    /**
     * 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap excel(SearchMap searchMap) {
    	String excelFileName = "취약시설점검실적";
    	String excelTitle = "취약시설점검실적";
    	
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
    	excelInfoList.add(new ExcelVO("년도","INSP_YY", "center"));
    	excelInfoList.add(new ExcelVO("월", "INSP_MON", "center"));
    	excelInfoList.add(new ExcelVO("부서명", "DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("점검구분", "INSP_KIND_TC_NM", "left"));
    	excelInfoList.add(new ExcelVO("계획명", "INSP_NM", "left"));
    	excelInfoList.add(new ExcelVO("적합수", "CON_CNT", "left"));
    	excelInfoList.add(new ExcelVO("부적합수", "INCON_CNT", "left"));
    	excelInfoList.add(new ExcelVO("기타건수", "ETC_CNT", "left"));
    	excelInfoList.add(new ExcelVO("점검건수", "TOTAL_CNT", "left"));
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.useFacilitiesInspec.getList", searchMap));
    	
    	return searchMap;
    }

    /**
     * 사용시설 정기검사 실적 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap useFacilitiesInspecList2(SearchMap searchMap) {
    	
    	
    	if ("".equals(StaticUtil.nullToBlank((String)searchMap.get("findDeptId")))) {
			searchMap.put("findDeptId", getStr("bsc.popup.useFacilitiesInspec.getScDeptMapping", searchMap));
		}

    	searchMap.addList("getDeptList", getList("bsc.popup.useFacilitiesInspec.getDeptList", searchMap));
    	
    	searchMap.addList("itemInfo", getList("bsc.popup.certificationProcess.getItem", searchMap));

        return searchMap;
    }

    /**
     * 시스템항목관리 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap useFacilitiesInspecList2_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("bsc.popup.useFacilitiesInspec.getList2", searchMap));

        return searchMap;
    }
    
    /**
     * 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap excel2(SearchMap searchMap) {
    	String excelFileName = "취약시설점검실적";
    	String excelTitle = "취약시설점검실적";
    	
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
    	excelInfoList.add(new ExcelVO("년도","INSP_YY", "center"));
    	excelInfoList.add(new ExcelVO("월", "INSP_MON", "center"));
    	excelInfoList.add(new ExcelVO("부서명", "DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("점검구분", "INSP_KIND_TC_NM", "left"));
    	excelInfoList.add(new ExcelVO("계획명", "INSP_NM", "left"));
    	excelInfoList.add(new ExcelVO("적합수", "CON_CNT", "left"));
    	excelInfoList.add(new ExcelVO("부적합수", "INCON_CNT", "left"));
    	excelInfoList.add(new ExcelVO("기타건수", "ETC_CNT", "left"));
    	excelInfoList.add(new ExcelVO("점검건수", "TOTAL_CNT", "left"));
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.useFacilitiesInspec.getList2", searchMap));
    	
    	return searchMap;
    }

}
