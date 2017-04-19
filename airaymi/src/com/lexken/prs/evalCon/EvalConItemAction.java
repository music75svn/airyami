/*************************************************************************
* CLASS 명      : EvalConItemAction
* 작 업 자      : 김효은
* 작 업 일      : 2014년 3월 19일 
* 기    능      : 직원개인기여도평가 평가항목
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

public class EvalConItemAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 직원개인기여도평가 평가항목 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalConItemList(SearchMap searchMap) {
    	searchMap.addList("weightSum", getStr("prs.evalCon.evalConItem.getEvalWeight", searchMap)); // 저장된 가중치 가져오기.
    	searchMap.addList("gbnCount", getStr("prs.evalCon.evalConItem.getGbnCount", searchMap)); // 저장된 가중치 가져오기.
        return searchMap;
    }
    
    /**
     * 직원개인기여도평가 평가항목 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalConItemList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("prs.evalCon.evalConItem.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 직원개인기여도평가 평가항목 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalConItemModify(SearchMap searchMap) {
        String stMode = searchMap.getString("mode");
        
        searchMap.addList("weightSum", getStr("prs.evalCon.evalConItem.getEvalWeight", searchMap)); // 저장된 가중치 가져오기.
        
        if("MOD".equals(stMode)) {
            searchMap.addList("detail", getDetail("prs.evalCon.evalConItem.getDetail", searchMap));
        }
        
        return searchMap;
    }
    
    /**
     * 직원개인기여도평가 평가항목 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalConItemProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 무결성 체크
         **********************************/
        if(!"DEL".equals(stMode)) {
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
        } else if("MOD".equals(stMode)) {
            searchMap = updateDB(searchMap);
        } else if("DEL".equals(stMode)) {
            searchMap = deleteDB(searchMap);
        } else if("ORDER".equals(stMode)) {
        	searchMap = orderDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 직원개인기여도평가 평가항목 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
            setStartTransaction();
        
            returnMap = insertData("prs.evalCon.evalConItem.insertData", searchMap);
        
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
     * 직원개인기여도평가 평가항목 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
            setStartTransaction();
            
            returnMap = updateData("prs.evalCon.evalConItem.updateData", searchMap);
            
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
     * 직원개인기여도평가 평가항목 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
        
        try {
        	String[] evalItemIds = searchMap.getString("evalItemIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != evalItemIds && 0 < evalItemIds.length) {
		        for (int i = 0; i < evalItemIds.length; i++) {
		            searchMap.put("evalItemId", evalItemIds[i]);
		            returnMap = updateData("prs.evalCon.evalConItem.deleteData", searchMap);
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
    
 public SearchMap orderDB(SearchMap searchMap) {
    	
    	HashMap returnMap    = new HashMap();    
    	
    		/**********************************
    		 * Parameter setting
    		 **********************************/
    		String[] evalItemOrderIds = searchMap.getStringArray("evalItemOrderIds");
    		String[] sortOrders = searchMap.getStringArray("sortOrders");
    		String[] weights = searchMap.getStringArray("weights");
    		
    		try {
    			setStartTransaction();
    			
    			if(null != evalItemOrderIds && 0 < evalItemOrderIds.length) {
    				for (int i = 0; i < evalItemOrderIds.length; i++) {
    					searchMap.put("evalItemId", evalItemOrderIds[i]);
    					searchMap.put("sortOrder", sortOrders[i]);
    					searchMap.put("weight", weights[i]);
    					
    					returnMap = updateData("prs.evalCon.evalConItem.orderData", searchMap);
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
     * 직원개인기여도평가 평가항목 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap evalConItemListExcel(SearchMap searchMap) {
        String excelFileName = "직원개인기여도평가 평가항목";
        String excelTitle = "직원개인기여도평가 평가항목 리스트";
        
        /************************************************************************************
         * 조회조건 설정
         ************************************************************************************/
        ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
        
        excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
        
        /****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
        ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
        excelInfoList.add(new ExcelVO("평가구분", "EVAL_GBN_NM", "center"));
    	excelInfoList.add(new ExcelVO("평가항목명", "EVAL_ITEM_NM", "left"));
    	excelInfoList.add(new ExcelVO("가중치", "WEIGHT", "center"));
    	excelInfoList.add(new ExcelVO("정렬순서", "SORT_ORDER", "center"));
    	
        
        searchMap.put("excelFileName", excelFileName);
        searchMap.put("excelTitle", excelTitle);
        searchMap.put("excelSearchInfoList", excelSearchInfoList);
        searchMap.put("excelInfoList", excelInfoList);
        searchMap.put("excelDataList", (ArrayList)getList("prs.evalCon.evalConItem.getList", searchMap));
        
        return searchMap;
    }
    
}
