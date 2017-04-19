/*************************************************************************
* CLASS 명      : foreignExpProof
* 작 업 자      : 안요한
* 작 업 일      : 2013년 08월 13일
* 기    능      : 해외 방폭 인증 매출액
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

public class ForeignExpProofAction extends CommonService {

    private static final long serialVersionUID = 1L;

    /**
     * 해외 방폭 인증 매출액 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap foreignExpProofList(SearchMap searchMap) {

    	if ("".equals(StaticUtil.nullToBlank((String)searchMap.get("findDeptId")))) {
        	
			searchMap.put("findDeptId", getStr("bsc.popup.foreignExpProof.getScDeptMapping", searchMap));
		}

        searchMap.addList("deptInfo", getList("bsc.popup.foreignExpProof.getDeptInfo", searchMap));

    	searchMap.addList("itemInfo", getList("bsc.popup.foreignExpProof.getItem", searchMap));

        return searchMap;
    }

    /**
     * 해외 방폭 인증 매출액 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap foreignExpProofList_xml(SearchMap searchMap) {

    	String itemCd = searchMap.getString("findItemCd");

    		searchMap.addList("list", getList("bsc.popup.foreignExpProof.getList", searchMap));

    	
        return searchMap;
    }


    /**
     * 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap excel(SearchMap searchMap) {
    	String excelFileName = "해외 방폭 인증 매출액 실적";
    	String excelTitle = "해외 방폭 인증 매출액 실적";

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
    	excelInfoList.add(new ExcelVO("신청 금액", "CUM_REQ_AMT", "center"));
    	excelInfoList.add(new ExcelVO("신청 수량", "CUM_REQ_QTY", "center"));
    	excelInfoList.add(new ExcelVO("검사 수량", "CUM_INSP_QTY", "center"));
    	excelInfoList.add(new ExcelVO("합격 수량", "CUM_PASS_QTY", "center"));
    	excelInfoList.add(new ExcelVO("불합격 수량", "CUM_UNPASS_QTY", "center"));
    	excelInfoList.add(new ExcelVO("검사불능 수량", "CUM_UNINSP_QTY", "center"));
    	excelInfoList.add(new ExcelVO("잔량", "CUM_DIF_QTY", "center"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);

    	searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.foreignExpProof.getList", searchMap));
    	return searchMap;
    }
    
}
