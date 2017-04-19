/*************************************************************************
* CLASS 명      : OrgExcelLoadAction
* 작 업 자      : 박선혜
* 작 업 일      : 2013년 6월 14일 
* 기    능      : 엑셀로드
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    박선혜      2013년 6월 14일             최 초 작 업    -- 다운로드, 업로드, 연계실행 부분 수정필요
**************************************************************************/
package com.lexken.prs.org;
    
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelUpload;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.StringConstants;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.util.HtmlHelper;
import com.lexken.framework.util.StaticUtil;
import com.lexken.framework.login.LoginVO;

public class OrgExcelLoadAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 세부평가항목 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap orgExcelLoadList(SearchMap searchMap) {
    	
        return searchMap;
    }
    
    
    /**
     * 세부평가항목 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap orgExcelLoadList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("prs.org.orgExcelLoad.getList", searchMap));

        return searchMap;
    }
    
 
    /**
     * 세부평가항목 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap orgExcelLoadProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 무결성 체크
         **********************************/
        if("ADD".equals(stMode) || "MOD".equals(stMode) ) {
            returnMap = this.validChk(searchMap);
            
            if((Integer)returnMap.get("ErrorNumber") < 0 ){
                searchMap.addList("returnMap", returnMap);
                return searchMap;
            }
        }
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("ADD".equals(stMode)) {
            searchMap = insertDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 세부평가항목 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap   = new HashMap();        
    	LoginVO loginVO 	= (LoginVO)searchMap.get("loginVO");        

    	searchMap.put("UserId", loginVO.getUser_id());
        searchMap.put("UserNm", loginVO.getUser_nm());
        
        try {
        	setStartTransaction();
        	
        	String[] orgConnItemIdExec = searchMap.getString("orgConnItemIdExec").split("\\|", 0); 
        	
        	if(null != orgConnItemIdExec && 0 < orgConnItemIdExec.length) {
        		for (int i = 0; i < orgConnItemIdExec.length; i++) {        			        			
        			searchMap.put("orgConnItemIdExec", orgConnItemIdExec[i]);
        			returnMap = insertData("prs.org.orgExcelLoad.execInterface", searchMap);
        			
        			if(!loginVO.chkAuthGrp("01")){
        				returnMap = updateData("prs.org.orgExcelLoad.updateUser", searchMap);
        			}
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
    * 세부평가항목 삭제 
    * @param      
    * @return String  
    * @throws
    */
   public SearchMap deleteDB(SearchMap searchMap) {
       HashMap returnMap = new HashMap(); 

       try {
	        String orgEvalItemIds = searchMap.getString("orgEvalItemIds");
	        String[] keyArray = orgEvalItemIds.split("\\|", 0);
	 
	        setStartTransaction();
	        
	        if(null != keyArray && 0 < keyArray.length) {
		        for (int i=0; i<keyArray.length; i++) {
		            searchMap.put("orgEvalItemId", keyArray[i]);
		            returnMap = updateData("prs.org.orgExcelLoad.deleteData", searchMap);
		            
		        	/**********************************
		             * 실적산식 삭제 
		             **********************************/
			        returnMap = updateData("prs.org.orgExcelLoad.updateCalTypeCol", searchMap, true);
			        
			        /**********************************
		             * 구간대 삭제
		             **********************************/
			        returnMap = updateData("prs.org.orgExcelLoad.updateEvalSection", searchMap, true);
		            
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
    * 엑셀업로드 팝업
    * @param      
    * @return String  
    * @throws 
    */
   public SearchMap popExcelUpload(SearchMap searchMap) {
   	
	   searchMap.addList("detail", getDetail("prs.org.orgExcelLoad.getDetail", searchMap));
   	
       return searchMap;
   }
   
   /**
    * 엑셀업로드 팝업
    * @param      
    * @return String  
    * @throws 
    */
   public SearchMap popExcelTargetUpload(SearchMap searchMap) {
   	
	   searchMap.addList("detail", getDetail("bsc.tam.excelInterface.getDetail", searchMap));
   	
       return searchMap;
   }   
   
   /**
    * 엑셀양식 다운로드
    * @param      
    * @return String  
    * @throws 
    */  
   public SearchMap excelTemplateDown(SearchMap searchMap) {
	   	String itemNm = searchMap.getString("itemNm");
	   	
	   	String excelFileName = itemNm;
	   	
	   	/****************************************************************************************************
	        * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
	    ****************************************************************************************************/
	   	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
	   	excelInfoList.add(new ExcelVO("평가년도", "YEAR", "left"));
	   	excelInfoList.add(new ExcelVO("조직코드", "DEPT_CD", "center"));
	   	excelInfoList.add(new ExcelVO("조직명", "DEPT_NM", "left"));
	   	excelInfoList.add(new ExcelVO("세부평가항목코드", "ORG_EVAL_ITEM_ID", "center"));
	   	excelInfoList.add(new ExcelVO("세부평가항목명", "ORG_EVAL_ITEM_NM", "left"));
	   	excelInfoList.add(new ExcelVO("산식항목", "CAL_TYPE_COL", "center"));
	   	excelInfoList.add(new ExcelVO("산식항목명", "CAL_TYPE_COL_NM", "left"));
	   	excelInfoList.add(new ExcelVO("실적값", "VALUE", "left"));
	
	   	searchMap.put("excelFileName", excelFileName);
	   	searchMap.put("excelInfoList", excelInfoList);
	   	searchMap.put("excelDataList", (ArrayList)getList("prs.org.orgExcelLoad.getExcelTemplate", searchMap));
	   	
	    return searchMap;
   }
   
   /**
    * 엑셀양식 다운로드
    * @param      
    * @return String  
    * @throws 
    */  
   public SearchMap excelTargetTemplateDown(SearchMap searchMap) {
	   	String itemNm = searchMap.getString("itemNm");
	   	
	   	String excelFileName = itemNm;
	   	
	   	/****************************************************************************************************
	        * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
	    ****************************************************************************************************/
	   	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
	   	excelInfoList.add(new ExcelVO("지표코드", "METRIC_ID", "center"));
	   	excelInfoList.add(new ExcelVO("지표명", "METRIC_NM", "left"));
	   	excelInfoList.add(new ExcelVO("조직명", "SC_DEPT_NM", "left"));
	   	excelInfoList.add(new ExcelVO("월", "MON", "center"));
	   	excelInfoList.add(new ExcelVO("목표", "TGT_VALUE", "left"));
	
	   	searchMap.put("excelFileName", excelFileName);
	   	searchMap.put("excelInfoList", excelInfoList);
	   	searchMap.put("excelDataList", (ArrayList)getList("bsc.tam.excelInterface.getExcelTargetTemplate", searchMap));
	   	
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
     * 엑셀로딩관리 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excelInterfaceProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
		 * 파일복사
		 **********************************/
		searchMap.fileCopy("/temp", "/excelUpload"); 
		
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("ADD".equals(stMode)) {
            searchMap = insertExcelDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    
    /**
     * 엑셀로딩관리 등록
     * @param      
     * @return String
     * @throws 
     */
    public SearchMap insertExcelDB(SearchMap searchMap) {
        HashMap returnMap = new HashMap();    
        ArrayList excelDataList = new ArrayList();
        int inputValCnt = 0;
        try {
        	setStartTransaction();
        	
        	ExcelUpload excel = ExcelUpload.getInstance();
        	excelDataList = excel.execlOrgUpload(searchMap);
        	
        	if(null != excelDataList && 0 < excelDataList.size()) {
                String[] strYear = (String[]) excelDataList.get(0);
    			String[] strDeptCd = (String[]) excelDataList.get(1);
    			String[] strOrgEvalItemId = (String[]) excelDataList.get(2);
    			String[] strCalTypeCol = (String[]) excelDataList.get(3);
    			String[] strValue = (String[]) excelDataList.get(4);
            
                for(int i=0; i<strDeptCd.length; i++) {
                	if(!"".equals(StaticUtil.nullToBlank(strDeptCd[i])) && !"".equals(StaticUtil.nullToBlank(strValue[i]))) {
	                	searchMap.put("strYear", strYear[i]);
	                	searchMap.put("strDeptCd", strDeptCd[i]);
	                	searchMap.put("strOrgEvalItemId", strOrgEvalItemId[i]);
	                	searchMap.put("strCalTypeCol", strCalTypeCol[i]);
	                	searchMap.put("strValue", strValue[i]);
	                	
	                	returnMap = insertData("prs.org.orgExcelLoad.insertEvalItem", searchMap);
	                	inputValCnt++;
                	}
                }
                
                searchMap.put("findYear", strYear[0]);
        	}        	
        } catch (Exception e) {
        	logger.error(e.toString());
        	setRollBackTransaction();
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        	setCommitTransaction();
        }
        
        /**********************************
         * Return
         **********************************/
        searchMap.addList("returnMap", returnMap);
        return searchMap;    
    }
    
}
