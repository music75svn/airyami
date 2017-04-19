/*************************************************************************
* CLASS 명      : SystemDataExecAction
* 작 업 자      : 박종호
* 작 업 일      : 2013년 08월 11일 
* 기    능      : 시스템연계 실행
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    박종호      2013년 08월 11일         최 초 작 업 
**************************************************************************/
package com.lexken.bsc.system;
    
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

import com.lexken.framework.login.LoginVO;

public class SystemDataExecAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 시스템연계 실행 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap systemDataExecList(SearchMap searchMap) {

        return searchMap;
    }
    
    /**
     * 시스템연계 실행 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap systemDataExecList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.system.systemDataExec.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 시스템연계 실행 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap systemDataExecModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.system.systemDataExec.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 시스템연계 실행 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap systemDataExecProcess(SearchMap searchMap) {
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
        }else if("MOD".equals(stMode)) {
            searchMap = updateDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 시스템연계 실행 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        ArrayList list = new ArrayList();
        
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
        
    	searchMap.put("UserId", loginVO.getUser_id());
        searchMap.put("UserNm", loginVO.getUser_nm());

        String systemGbn = searchMap.getString("findSystemGbn");        
        
        try {
        	setStartTransaction();        		        	        
        	
        	String[] itemCdProc = searchMap.getString("itemCdProc").split("\\|", 0);        	                         	
        	
        	if(null != itemCdProc && 0 < itemCdProc.length) {
        		for (int i = 0; i < itemCdProc.length; i++) {
        			if(!list.contains(itemCdProc[i]))list.add(itemCdProc[i]);	
        		}
        	}
        	
        	String itemProcCds[] = new String[list.size()];
        	list.toArray(itemProcCds);
        	
        	if(null != itemProcCds && 0 < itemProcCds.length) {
        		for (int i = 0; i < itemProcCds.length; i++) {        			        			
        			searchMap.put("itemProcCds", itemProcCds[i]);
        			returnMap = insertData("bsc.system.systemDataExec.execInterface", searchMap);
        			
        			if(!loginVO.chkAuthGrp("01")){
        				returnMap = updateData("bsc.system.systemDataExec.updateUser", searchMap);
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
     * 시스템연계 연계여부 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        String[] itemCds = searchMap.getStringArray("itemCds");
	        
	        for(int i=0; i<itemCds.length; i++){
            	if(null != itemCds[i]){
            		searchMap.put("itemCd", itemCds[i]);            		            	                        			        			        		
            		searchMap.put("useYn", searchMap.getString("useYn" + (i+1)));
            		
        			returnMap = updateData("bsc.system.systemDataExec.updateData", searchMap);            		
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
     * 시스템연계 실행 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap systemDataExecListExcel(SearchMap searchMap) {
    	String excelFileName = "시스템연계 실행";
    	String excelTitle = "시스템연계 실행 리스트";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("기준월", (String)searchMap.get("monNm")));
    	excelSearchInfoList.add(new ExcelVO("항목", StaticUtil.nullToDefault((String)searchMap.get("itemNm"), "")));
    	
    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("항목코드", "ITEM_CD", "center"));
    	excelInfoList.add(new ExcelVO("항목명", "ITEM_NM", "left"));
    	excelInfoList.add(new ExcelVO("최종연계일시", "INTERFACE_DT", "left"));
    	excelInfoList.add(new ExcelVO("기준년월", "YM", "center"));
    	excelInfoList.add(new ExcelVO("성공여부", "RESULT_YN", "center"));
    	excelInfoList.add(new ExcelVO("자동연계여부", "USE_YN", "center"));
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.system.systemDataExec.getList", searchMap));
    	
        return searchMap;
    }
    
}
