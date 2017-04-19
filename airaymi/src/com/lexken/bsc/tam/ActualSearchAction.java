/*************************************************************************
* CLASS 명      : ActualSearchAction
* 작 업 자      : 하윤식
* 작 업 일      : 2012년 8월 16일 
* 기    능      : 실적조회
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  --------  -----------------  -------------------------    --------
*   1    하윤식      2012년 8월 16일             최 초 작 업 
**************************************************************************/
package com.lexken.bsc.tam;
    
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

public class ActualSearchAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 실적조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap actualSearchList(SearchMap searchMap) {
    	
    	searchMap.addList("treeList", getList("bsc.module.commModule.getScDeptList", searchMap));
    	
    	/************************************************************************************
    	 * 디폴트 평가조직 조회
    	 ************************************************************************************/
    	String scDeptId = searchMap.getString("scDeptId");
    	searchMap.put("findSearchCodeId", scDeptId);
    	if("".equals(scDeptId)) {
    		searchMap.put("scDeptId", searchMap.getDefaultValue("treeList", "CODE_ID", 0));
    		searchMap.put("scDeptNm", searchMap.getDefaultValue("treeList", "CODE_NM", 0));
    		searchMap.put("findSearchCodeId", searchMap.getDefaultValue("treeList", "CODE_ID", 0));
    	}

        return searchMap;
    }
    
    /**
     * 실적조회 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap actualSearchList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.tam.actualSearch.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 실적 히스토리 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap actualSearchHistoryList(SearchMap searchMap) {
    	
    	searchMap.addList("metricDetail", getDetail("bsc.tam.actualSearch.getDetail", searchMap));
    	
    	String scDeptId = searchMap.getString("scDeptId");
    	searchMap.put("findSearchCodeId", scDeptId);
    	searchMap.put("findScDeptId", scDeptId);
    	
    	/**********************************
         * 평가조직 트리조회
         **********************************/
    	searchMap.addList("treeList", getList("bsc.module.commModule.getScDeptList", searchMap));
    	
        /**********************************
         * 실적산식 조회
         **********************************/
        HashMap detail = (HashMap)getDetail("bsc.tam.actualMng.getCalTypeInfo", searchMap);
        searchMap.addList("detail", detail);
        String actCalTypeNm = (String)detail.get("ACT_CAL_TYPE");
        HashMap<String, String> calTyepColValueMap = new HashMap<String, String>();
        ArrayList calTypeColList = (ArrayList)getList("bsc.tam.actualMng.calTypeColList", searchMap);
        
        for (int i = 0; i < calTypeColList.size(); i++) {
        	HashMap<String, String> t = (HashMap<String, String>)calTypeColList.get(i);
			calTyepColValueMap.put((String)t.get("CAL_TYPE_COL"), (String)t.get("CAL_TYPE_COL_NM"));
		}
        
        /**********************************
         * 실적산식명 가져오기
         **********************************/
        String calTypeColDesc = (String)HtmlHelper.changeCalNmToCalDesc(actCalTypeNm, calTyepColValueMap);
        searchMap.addList("calTypeColDesc", calTypeColDesc);


        return searchMap;
    }
    
    /**
     * 실적조회 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap actualSearchHistoryList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.tam.actualSearch.getHistoryList", searchMap));

        return searchMap;
    }
    
    
    /**
     * 실적조회 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap actualSearchModify(SearchMap searchMap) {
    	
    	searchMap.addList("metricDetail", getDetail("bsc.tam.actualSearch.getDetail", searchMap));
    	
    	String scDeptId = searchMap.getString("scDeptId");
    	searchMap.put("findSearchCodeId", scDeptId);
    	searchMap.put("findScDeptId", scDeptId);
    	
    	/**********************************
         * 평가조직 트리조회
         **********************************/
    	searchMap.addList("treeList", getList("bsc.module.commModule.getScDeptList", searchMap));
    	
    	/**********************************
         * 월별실적조회
         **********************************/
        searchMap.addList("list", getList("bsc.tam.actualMng.getList", searchMap));
        
        /**********************************
         * 목표, 실적, 달성율 조회
         **********************************/
        searchMap.addList("scoreList", getList("bsc.tam.actualMng.getScoreList", searchMap));
        
        /**********************************
         * 실적산식 조회
         **********************************/
        HashMap detail = (HashMap)getDetail("bsc.tam.actualMng.getCalTypeInfo", searchMap);
        searchMap.addList("detail", detail);
        String actCalTypeNm = (String)detail.get("ACT_CAL_TYPE");
        HashMap<String, String> calTyepColValueMap = new HashMap<String, String>();
        ArrayList calTypeColList = (ArrayList)getList("bsc.tam.actualMng.calTypeColList", searchMap);
        
        for (int i = 0; i < calTypeColList.size(); i++) {
        	HashMap<String, String> t = (HashMap<String, String>)calTypeColList.get(i);
			calTyepColValueMap.put((String)t.get("CAL_TYPE_COL"), (String)t.get("CAL_TYPE_COL_NM"));
		}
        
        /**********************************
         * 실적산식명 가져오기
         **********************************/
        String calTypeColDesc = (String)HtmlHelper.changeCalNmToCalDesc(actCalTypeNm, calTyepColValueMap);
        searchMap.addList("calTypeColDesc", calTypeColDesc);

        /**********************************
         * ACTIVITY 조회
         **********************************/
        searchMap.addList("activityDetail", getDetail("bsc.tam.actualMng.getActivityDetail", searchMap));
        
        /**********************************
         * 미진사유 조회
         **********************************/
        searchMap.addList("causeDetail", getDetail("bsc.tam.actualMng.getCause", searchMap));
        
        /**********************************
         * 첨부파일 조회
         **********************************/
        searchMap.addList("fileList", getList("bsc.tam.actualMng.getFileList", searchMap));
        
        /**********************************
         * 실적입력상태 가져오기
         **********************************/
        searchMap.addList("actStatusId", getStr("bsc.tam.actualMng.getActStatusId", searchMap));
    	
        
        return searchMap;
    }
    
    
    /**
     * 실적조회 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap actualSearchListExcel(SearchMap searchMap) {
    	String excelFileName = "실적조회";
    	String excelTitle = "실적조회 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("year")));
    	excelSearchInfoList.add(new ExcelVO("평가월", (String)searchMap.get("mon")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();

    	excelInfoList.add(new ExcelVO("평가조직", "SC_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("전략과제", "STRATEGY_NM", "left"));
    	excelInfoList.add(new ExcelVO("CSF", "CSF_NM", "left"));
    	excelInfoList.add(new ExcelVO("지표", "METRIC_NM", "left"));
    	excelInfoList.add(new ExcelVO("주기", "EVAL_CYCLE_NM", "center"));
    	excelInfoList.add(new ExcelVO("단위", "UNIT_NM", "center"));
    	excelInfoList.add(new ExcelVO("담당자", "INSERT_USER_NM", "center"));
    	excelInfoList.add(new ExcelVO("목표", "TGT_VALUE", "center"));
    	excelInfoList.add(new ExcelVO("실적", "VALUE", "center"));
    	excelInfoList.add(new ExcelVO("점수", "SCORE", "center"));
    	excelInfoList.add(new ExcelVO("처리상태", "ACT_STATUS_NM", "center"));
    	

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.tam.actualSearch.getListExcel", searchMap));
    	
        return searchMap;
    }

}
