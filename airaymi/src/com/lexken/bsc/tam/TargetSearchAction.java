/*************************************************************************
* CLASS 명      : TargetSearchAction
* 작 업 자      : 정철수
* 작 업 일      : 2012년 11월 20일 
* 기    능      : 지표목표조회
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    정철수      2012년 11월 20일         최 초 작 업 
**************************************************************************/
package com.lexken.bsc.tam;
    
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
import com.lexken.framework.util.HtmlHelper;
import com.lexken.framework.util.StaticUtil;

public class TargetSearchAction extends CommonService {

private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 목표조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap targetSearchList(SearchMap searchMap) {
    	
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
     * 목표조회 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap targetSearchList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.tam.targetSearch.getList", searchMap));

        return searchMap;
    }
    
    
    /**
     * 목표 월별상세조회 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap targetSearchDetailList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.tam.targetSearch.getDetailList", searchMap));

        return searchMap;
    }
    
    /**
     * 목표 히스토리 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap targetSearchHistoryList(SearchMap searchMap) {
    	
    	searchMap.addList("metricDetail", getDetail("bsc.tam.targetSearch.getDetail", searchMap));
    	
    	String scDeptId = searchMap.getString("scDeptId");
    	searchMap.put("findSearchCodeId", scDeptId);
    	searchMap.put("findScDeptId", scDeptId);
    	
    	/**********************************
         * 평가조직 트리조회
         **********************************/
    	searchMap.addList("treeList", getList("bsc.module.commModule.getScDeptList", searchMap));
    	
        /**********************************
         * 목표산식 조회
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
         * 목표산식명 가져오기
         **********************************/
        String calTypeColDesc = (String)HtmlHelper.changeCalNmToCalDesc(actCalTypeNm, calTyepColValueMap);
        searchMap.addList("calTypeColDesc", calTypeColDesc);


        return searchMap;
    }
    
    /**
     * 목표조회 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap targetSearchHistoryList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.tam.targetSearch.getHistoryList", searchMap));

        return searchMap;
    }
    
    
    /**
     * 목표조회 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap targetSearchModify(SearchMap searchMap) {
    	
    	searchMap.addList("metricDetail", getDetail("bsc.tam.targetSearch.getDetail", searchMap));
    	
    	String scDeptId = searchMap.getString("scDeptId");
    	searchMap.put("findSearchCodeId", scDeptId);
    	searchMap.put("findScDeptId", scDeptId);
    	
    	/**********************************
         * 평가조직 트리조회
         **********************************/
    	searchMap.addList("treeList", getList("bsc.module.commModule.getScDeptList", searchMap));
    	
    	/**********************************
         * 월별목표조회
         **********************************/
        searchMap.addList("list", getList("bsc.tam.actualMng.getList", searchMap));
        
        /**********************************
         * 목표, 목표, 달성율 조회
         **********************************/
        searchMap.addList("scoreList", getList("bsc.tam.actualMng.getScoreList", searchMap));
        
        /**********************************
         * 목표산식 조회
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
         * 목표산식명 가져오기
         **********************************/
        String calTypeColDesc = (String)HtmlHelper.changeCalNmToCalDesc(actCalTypeNm, calTyepColValueMap);
        searchMap.addList("calTypeColDesc", calTypeColDesc);

        /**********************************
         * 목표입력상태 가져오기
         **********************************/
        searchMap.addList("actStatusId", getStr("bsc.tam.actualMng.getActStatusId", searchMap));
    	
        
        return searchMap;
    }
    
    
    /**
     * 목표조회 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap targetSearchListExcel(SearchMap searchMap) {
    	String excelFileName = "목표조회";
    	String excelTitle = "목표조회 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	/*
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("공통코드명", StaticUtil.nullToDefault((String)searchMap.get("codeGrpNm"), "전체")));
    	excelSearchInfoList.add(new ExcelVO("사용여부", (String)searchMap.get("useYnNm")));
    	*/
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	/*
    	excelInfoList.add(new ExcelVO("코드그룹", "CODE_GRP_ID", "left"));
    	excelInfoList.add(new ExcelVO("코드그룹명", "CODE_GRP_NM"));
    	excelInfoList.add(new ExcelVO("코드부여구분", "CODE_DEF_ID"));
    	excelInfoList.add(new ExcelVO("YEAR_YN", "YEAR_YN"));
    	excelInfoList.add(new ExcelVO("비고", "CONTENT"));
    	excelInfoList.add(new ExcelVO("생성일자", "CREATE_DT"));
    	excelInfoList.add(new ExcelVO("삭제일자", "DELETE_DT"));
    	*/
    	
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.tam.targetSearch.getList", searchMap));
    	
        return searchMap;
    }

}
