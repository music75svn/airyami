/*************************************************************************
* CLASS 명      : DeptGovWeightAction
* 작 업 자      : 안요한
* 작 업 일      : 2013년 07월 05일 
* 기    능      : 부서별 정평지표 점수반영 목록
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    안요한      2013년 07월 05일         최 초 작 업 
**************************************************************************/
package com.lexken.bsc.dept;
    
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.FileInfoVO;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.StringConstants;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.config.FileConfig;
import com.lexken.framework.struts2.IsBoxActionSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.util.StaticUtil;

public class DeptGovStdAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 부서별 정평지표 책임가중치 목록 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deptGovStdList(SearchMap searchMap) {
    	//searchMap.addList("govMetricList", getList("bsc.dept.deptGovStd.getGovMetricList", searchMap));
    	
    	//searchMap.addList("cnt", getDetail("bsc.dept.deptGovStd.getMeticCount", searchMap)); 
    	
        return searchMap;
    }
    
    /**
     * 부서별 정평지표  데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deptGovStdList_xml(SearchMap searchMap) {
    	searchMap.addList("list", getList("bsc.dept.deptGovStd.getGovMetricList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 정부평가 지표별 책임가중치 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap deptGovStdListExcel(SearchMap searchMap) {
    	String excelFileName = "정평지표  반영점수";
    	String excelTitle = "정평지표 반영점수LIST";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("year")));
    	
    	/****************************************************************************************************
    	 * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
    	 ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("지표명","GOV_METRIC_NM", "left"));
    	excelInfoList.add(new ExcelVO("지표유형","GBN_NM", "center"));
    	excelInfoList.add(new ExcelVO("달성도","ACHIEVEMENT", "center"));
    	excelInfoList.add(new ExcelVO("개선도","IMPROVEMENT", "center"));
    	excelInfoList.add(new ExcelVO("반영점수","SCORE", "center"));
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.dept.deptGovStd.getGovMetricList", searchMap));
    	
    	return searchMap;
    }
    
    
 
    /**
     * 직원지급기준 설정 등록/수정/삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap deptGovStdProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");

    

        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("STD".equals(stMode)) { //정평지표 점수 집계
            searchMap = updateDeptGovStdDB(searchMap);      
        }

        /**********************************
         * Return
         **********************************/
        return searchMap;
    }

	
    
    /**
     * 정평지표 점수 집계
     * @param
     * @return String
     * @throws
     */
    public SearchMap updateDeptGovStdDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
        	setStartTransaction();

        	returnMap = insertData("bsc.dept.deptGovStd.callDeptGovStd", searchMap);	// 직원 차체성과급,정평성과급 집계

        } catch (Exception e) {
        	logger.error(e.toString());
        	setRollBackTransaction();
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        	setEndTransaction();
        }

        /**********************************
         * Return
         **********************************/
        searchMap.addList("returnMap", returnMap);
        return searchMap;
    }

	
    
}
