/*************************************************************************
* CLASS 명      : EmpEvalConResultStatusAction
* 작 업 자      : 김효은
* 작 업 일      : 2014년 3월 19일 
* 기    능      : 직원개인기여도평가 평가현황
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    김효은      2014년 3월 19일         최 초 작 업 
**************************************************************************/
package com.lexken.prs.evalCon;
    
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

public class EmpEvalConResultStatusAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 직원개인기여도평가 평가현황 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap empEvalConResultStatusList(SearchMap searchMap) {
    	String findNameEmpn = (String)searchMap.get("findNameEmpn");
    	String findEvalSubmitYn = (String)searchMap.get("findEvalSubmitYn");
    	
    	if("findName".equals(findNameEmpn)){
    		searchMap.put("findName", findNameEmpn);
    	}else{
    		searchMap.put("findEmpn", findNameEmpn);
    	}

    	if("01".equals(findEvalSubmitYn)){
    		searchMap.put("01", findEvalSubmitYn);
    	}else if("02".equals(findEvalSubmitYn)){
    		searchMap.put("02", findEvalSubmitYn);
    	}else if ("03".equals(findEvalSubmitYn)) {
    		searchMap.put("03", findEvalSubmitYn);
    	}else {
    		searchMap.put("", findEvalSubmitYn);
    	}
    	
		searchMap.put("nameEmpn", findNameEmpn);
		searchMap.put("evalSubmitYn", findEvalSubmitYn);

        return searchMap;
    }
    
    /**
     * 직원개인기여도평가 평가현황 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap empEvalConResultStatusList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("prs.evalCon.empEvalConResultStatus.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 직원개인기여도평가 평가현황 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap empEvalConResultStatusListExcel(SearchMap searchMap) {
        String excelFileName = "직원개인기여도평가 평가현황";
        String excelTitle = "직원개인기여도평가 평가현황 리스트";
        
        /************************************************************************************
         * 조회조건 설정
         ************************************************************************************/
        ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
        
        excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
        excelSearchInfoList.add(new ExcelVO("평가여부", StaticUtil.nullToDefault((String)searchMap.get("evalSubmit"), "전체")));
        
        
        /****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
        ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("부서", "DEPT_KOR_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가자 사번", "ASSESSOR_EMPN", "center"));
    	excelInfoList.add(new ExcelVO("평가자", "KOR_NM", "center"));
    	excelInfoList.add(new ExcelVO("평가제출 여부", "EVAL_SUBMIT_YN", "center"));
    	
        
        searchMap.put("excelFileName", excelFileName);
        searchMap.put("excelTitle", excelTitle);
        searchMap.put("excelSearchInfoList", excelSearchInfoList);
        searchMap.put("excelInfoList", excelInfoList);
        searchMap.put("excelDataList", (ArrayList)getList("prs.evalCon.empEvalConResultStatus.getList", searchMap));
        
        return searchMap;
    }
    
}
