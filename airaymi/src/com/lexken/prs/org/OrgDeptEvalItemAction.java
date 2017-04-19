/*************************************************************************
* CLASS 명      : OrgDeptEvalItemAction
* 작 업 자      : 박선혜
* 작 업 일      : 2013년 6월 13일 
* 기    능      : 조직별 세부평가항목 
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    박선혜      2013년 6월 13일             최 초 작 업 
**************************************************************************/
package com.lexken.prs.org;
    
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.StringConstants;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.util.HtmlHelper;
import com.lexken.framework.util.StaticUtil;
import com.lexken.framework.login.LoginVO;

public class OrgDeptEvalItemAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 조직별세부평가항목 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap orgDeptEvalItemList(SearchMap searchMap) {
    	
    	/**********************************
         * 로그인정보 (권한 체크)
         **********************************/
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
    	/************************************************************************************
    	 * 평가조직 트리 조회
    	 ************************************************************************************/
        searchMap.addList("treeList", getList("prs.org.orgDeptEvalItemAct.getDeptList1", searchMap));
        
    	/************************************************************************************
    	 * 디폴트 평가조직 조회
    	 ************************************************************************************/
    	String deptCd = searchMap.getString("deptCd");
    	searchMap.put("findSearchCodeId", deptCd);
    	if("".equals(deptCd)) {
    		searchMap.put("deptCd", searchMap.getDefaultValue("treeList", "CODE_ID", 0));
    		searchMap.put("deptNm", searchMap.getDefaultValue("treeList", "CODE_NM", 0));
    		searchMap.put("findSearchCodeId", searchMap.getDefaultValue("treeList", "CODE_ID", 0));
    	}
    	
        return searchMap;
    }
    
    /**
     * 조직별세부평가항목 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap orgDeptEvalItemList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("prs.org.orgDeptEvalItem.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 조직별세부평가항목 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap orgDeptEvalItemModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	
    	/**********************************
         * 로그인정보 (권한 체크)
         **********************************/
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
    	String deptCd = searchMap.getString("deptCd");
    	searchMap.put("findSearchCodeId", deptCd);
    	searchMap.put("findDeptCd", deptCd);
    	
    	HashMap detail = new HashMap();
    	
    	if("MOD".equals(stMode)) {
	    	/**********************************
	         * 지표 상세조회 
	         **********************************/
	    	detail = getDetail("prs.org.orgDeptEvalItem.getDetail", searchMap);
	        searchMap.addList("detail", detail);
    	}
    	
        /**********************************
         * 평가조직 트리 조회 
         **********************************/
       	searchMap.addList("treeList", getList("bsc.module.commModule.getDeptList1", searchMap));
        
        /**********************************
         * 산식컬럼 조회 
         **********************************/
        ArrayList calTypeColList = new ArrayList();
        calTypeColList = (ArrayList)getList("prs.org.orgEvalItem.calTypeColList", searchMap); 
        searchMap.addList("calTypeColList", calTypeColList);
        
        /**********************************
         * 득점산식조회 
         **********************************/
        searchMap.addList("scoreCalTypeList", getList("prs.org.orgEvalItem.scoreCalTypeList", searchMap));
        
        /**********************************
         * 평가구간대 조회 
         **********************************/
        searchMap.addList("evalSectionList", getList("prs.org.orgEvalItem.evalSectionList", searchMap));
        
        /**********************************
         * 평가구간대 등급 조회 
         **********************************/
        searchMap.addList("gradeList", getList("prs.org.orgEvalItem.gradeList", searchMap));
        
        if("MOD".equals(stMode)) {

	        String actCalTypeNm = (String)detail.get("ACT_CAL_TYPE");
	        HashMap<String, String> calTyepColValueMap = new HashMap<String, String>();
	        
	        if(null != calTypeColList && 0 < calTypeColList.size()) {
		        for (int i = 0; i < calTypeColList.size(); i++) {
		        	HashMap<String, String> t = (HashMap<String, String>)calTypeColList.get(i);
					calTyepColValueMap.put((String)t.get("CAL_TYPE_COL"), (String)t.get("CAL_TYPE_COL_NM"));
				}
	        }
	        
	        String calTypeColDesc = (String)HtmlHelper.changeCalNmToCalDesc(actCalTypeNm, calTyepColValueMap);
	        searchMap.addList("calTypeColDesc", calTypeColDesc);
        }
        
        return searchMap;
    }
    
    /**
     * 조직별세부평가항목 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap orgDeptEvalItemProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 무결성 체크
         **********************************/
        if("MOD".equals(stMode)) {
            returnMap = this.validChk(searchMap);
            
            if((Integer)returnMap.get("ErrorNumber") < 0 ){
                searchMap.addList("returnMap", returnMap);
                return searchMap;
            }
        }
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("MOD".equals(stMode)) {
            searchMap = updateDB(searchMap);
        } else if("DEL".equals(stMode)) {
            searchMap = deleteDB(searchMap);
        }
        
        /**********************************
         * 조직코드 설정
         **********************************/
        String deptCd = searchMap.getString("deptCd");
    	searchMap.put("findSearchCodeId", deptCd);
    	
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 조직별세부평가항목 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        /**********************************
	         * 세부평가항목 수정 
	         **********************************/
	        returnMap = updateData("prs.org.orgDeptEvalItem.updateData", searchMap);
	        
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
    
    /**
     * 조직별세부평가항목 삭제 
     * @param      
     * @return String  
     * @throws
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        HashMap returnMap = new HashMap(); 

        try {

        	String deptCd = searchMap.getString("deptCd");
        	searchMap.put("findDeptCd", deptCd);
        	String orgEvalItemIds = searchMap.getString("orgEvalItemIds");
	        String[] keyArray = orgEvalItemIds.split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != keyArray && 0 < keyArray.length) {
		        for (int i=0; i<keyArray.length; i++) {
		            searchMap.put("orgEvalItemId", keyArray[i]);
		            returnMap = updateData("prs.org.orgDeptEvalItem.deleteData", searchMap);
		        }
	        }
	        
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
    
    /**
     *  Validation 체크(무결성 체크)
     * @param SearchMap
     * @return HashMap
     */
    private HashMap validChk(SearchMap searchMap) {
        HashMap returnMap         = new HashMap();
        int     resultValue        = 0;
               
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }
    
    /**
     * 지표Pool관리 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap orgDeptEvalItemListExcel(SearchMap searchMap) {
    	String excelFileName = "조직별 "+StringConstants.ORG_EVAL_ITEM_NM;
    	String excelTitle = "조직별 "+StringConstants.ORG_EVAL_ITEM_NM + "리스트";
    	
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.get("findYear")))) {
			searchMap.put("findYear", (String)searchMap.get("year"));
		}
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.get("findDeptCd")))) {
			searchMap.put("findDeptCd", (String)searchMap.get("deptCd"));
		}
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("소속조직", (String)searchMap.get("deptNm")));
    	excelSearchInfoList.add(new ExcelVO("사용여부", (String)searchMap.get("useYnNm")));

    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO(StringConstants.ORG_EVAL_ITEM_NM + "코드", "ORG_EVAL_ITEM_ID", "center", "CNT", 6000));
    	excelInfoList.add(new ExcelVO("평가분야", "EVAL_TYPE_NM", "center", "CNT"));
    	excelInfoList.add(new ExcelVO(StringConstants.ORG_EVAL_ITEM_NM + "명", "ORG_EVAL_ITEM_NM", "center", "CNT", 8500));
    	excelInfoList.add(new ExcelVO("설명", "CONTENT", "left", "CNT"));
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("prs.org.orgDeptEvalItem.getExcelList", searchMap));
    	
        return searchMap;
    }
    
    
}
