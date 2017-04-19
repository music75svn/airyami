/*************************************************************************
* CLASS 명      : GovScoreYearAction
* 작 업 자      : 방승현
* 작 업 일      : 2013년 07월 01일 
* 기    능      : 정평결과확인
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    방승현      2013년 07월 01일         최 초 작 업 
**************************************************************************/
package com.lexken.prs.dir;
    
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

public class GovScoreYearAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 정평결과확인 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap govScoreYearList(SearchMap searchMap) {
    	
    	/**********************************
         * 정평지표 소유한 임원 조회
         **********************************/
    	searchMap.addList("dirList", getList("prs.dir.govScoreYear.getGovScoreYearList", searchMap));
    	
    	

        return searchMap;
    }
    
    /**
     * 정평결과확인 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap govScoreYearList_xml(SearchMap searchMap) {
        
    	 String findEmpn = searchMap.getString("findEmpn");
    	 String findPosTc = null;
    	 String[] findEmpnArr = null;
    	
    	 if(!"".equals(findEmpn)){
    		 findEmpnArr = findEmpn.split("\\|");
    		 findEmpn = findEmpnArr[0];
    		 findPosTc = findEmpnArr[1];
    	 }
    	 
    	 searchMap.put("findEmpn", findEmpn);
    	 searchMap.put("findPosTc", findPosTc);
    	/**********************************
         * 지표점수 유무 조회 (임원들이 GOV_SCORE를 가지고 있으면 cnt = 1 없으면 cnt = 0)
         **********************************/
    	int cnt = getInt("prs.dir.govScoreYear.getGovScoreCnt", searchMap);
    	
    	if (0 < cnt) {
    		searchMap.addList("list", getList("prs.dir.govScoreYear.getList", searchMap));
    	}
    	
        return searchMap;
    }
    
    /**
     * 정평결과확인 점수합계 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap govScoreYearProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("GET".equals(stMode)) {
        	searchMap = insertGovTotScore(searchMap);
        }
        
         return searchMap;
    }
    
    /**
     * 정평결과확인 점수합계 등록
     * @param      
     * @return String  
     * @throws 
     */ 
    public SearchMap insertGovTotScore(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("prs.dir.govScoreYear.insertGovTotScore", searchMap);
	        
        } catch (Exception e) {
        	setRollBackTransaction();
        	logger.error(e.toString());
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

    /**
     * 정평지표관리 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap govScoreYearListExcel(SearchMap searchMap) {
    	String excelFileName = "정평지표관리";
    	String excelTitle = "정평지표관리 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("findYear")));
    	excelSearchInfoList.add(new ExcelVO("이름", (String)searchMap.get("korNm")));
    	excelSearchInfoList.add(new ExcelVO("직위", (String)searchMap.get("posTcNm")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가범주", "EVAL_CAT_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가부문", "EVAL_CAT_NM", "left"));
    	excelInfoList.add(new ExcelVO("정부경영평가 지표", "GOV_METRIC_NM", "left"));
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("prs.dir.govScoreYear.getExcelList", searchMap));
    	
        return searchMap;
    }
}
