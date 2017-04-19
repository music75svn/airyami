/*************************************************************************
* CLASS 명      : ExcelInterfaceAction
* 작 업 자      : 김윤기
* 작 업 일      : 2012년 7월 26일 
* 기    능      : 엑셀로딩관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    김윤기      2012년 7월 26일             최 초 작 업 
**************************************************************************/
package com.lexken.bsc.tam;
    
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelUtil;
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
import com.lexken.framework.common.ExcelUpload;

public class ExcelInterfaceAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 엑셀로딩관리 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excelInterfaceList(SearchMap searchMap) {
    	
    	searchMap.addList("userList", getList("bsc.tam.excelInterface.getUserList", searchMap));

        return searchMap;
    }
    
    /**
     * @throws UnsupportedEncodingException 
     * 엑셀업로드 팝업
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popExcelUpload(SearchMap searchMap) throws UnsupportedEncodingException {
    	
    	//get방식 한글 파라미터 전송시 깨짐현상 수정 2013.10.23 
    	String temp = searchMap.getString("findItemNm");
    	String findItemNm = null;
    	findItemNm = new String(temp.getBytes("8859_1"), "EUC-KR");
    	searchMap.put("findItemNm", findItemNm);
    	searchMap.addList("detail", getDetail("bsc.tam.excelInterface.getDetail", searchMap));
    	
        return searchMap;
    }
    
    /**
     * @throws UnsupportedEncodingException 
     * 엑셀업로드 팝업
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popExcelTargetUpload(SearchMap searchMap) throws UnsupportedEncodingException {
    	
    	//get방식 한글 파라미터 전송시 깨짐현상 수정 2013.10.23 
    	String temp = searchMap.getString("findItemNm");
    	String findItemNm = null;
    	findItemNm = new String(temp.getBytes("8859_1"), "EUC-KR");
    	searchMap.put("findItemNm", findItemNm);
    	
    	searchMap.addList("detail", getDetail("bsc.tam.excelInterface.getDetail", searchMap));
    	
        return searchMap;
    }
    
    
    /**
     * 엑셀로딩관리 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excelInterfaceList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.tam.excelInterface.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 엑셀로딩 데이터 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excelInterfaceDetailList(SearchMap searchMap) {
    	
        return searchMap;
    }
    
    /**
     * 엑셀로딩 상세 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excelInterfaceDetailList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.tam.excelInterface.getDetailList", searchMap));

        return searchMap;
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
		 * 파일복사
		 **********************************/
		searchMap.fileCopy("/temp", "/excelUpload"); 
		
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("ADD".equals(stMode)) {
            searchMap = insertDB(searchMap);
        }else if("ADDTGT".equals(stMode)){
        	searchMap = insertTargetDB(searchMap);
        }
        
        searchMap.put("findYear", searchMap.getString("year"));
        
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
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap = new HashMap();    
        ArrayList excelDataList = new ArrayList();
        int inputValCnt = 0;
        try {
        	setStartTransaction();
        	
        	ExcelUpload excel = ExcelUpload.getInstance();
        	excelDataList = excel.execlUpload(searchMap);
        	
        	if(null != excelDataList && 0 < excelDataList.size()) {
        		String[] strCode = (String[]) excelDataList.get(0);
                String[] strValue = (String[]) excelDataList.get(1);
            
                /**********************************
                 * 기존 등록된 실적 삭제
                 **********************************/
                returnMap = insertData("bsc.tam.excelInterface.deleteData", searchMap);
                
                for(int i=0; i<strCode.length; i++) {
                	if(!"".equals(StaticUtil.nullToBlank(strCode[i])) && !"".equals(StaticUtil.nullToBlank(strValue[i]))) {
	                	searchMap.put("deptId", strCode[i]);
	                	searchMap.put("actual", strValue[i]);
	                	returnMap = insertData("bsc.tam.excelInterface.insertData", searchMap);
	                	inputValCnt++;
                	}
                }
        	}
        	
        	/**********************************
             * 로그등록
             **********************************/
            searchMap.put("resultYn", "Y");
            searchMap.put("inputValue", inputValCnt);
            returnMap = deleteData("bsc.tam.excelInterface.deleteLog", searchMap, true);
            returnMap = insertData("bsc.tam.excelInterface.insertLog", searchMap);
        	
            /**********************************
             * 등록 실패시 작업
             **********************************/
            Integer chkVal = (Integer)searchMap.get("chkVal");
    		if(null != chkVal) {
    			if(chkVal.intValue() < 0){
    				searchMap.put("resultYn", "N");
    	            searchMap.put("inputValue", 0);
    	            returnMap = deleteData("bsc.tam.excelInterface.deleteLog", searchMap, true, true);
    	            returnMap = insertData("bsc.tam.excelInterface.insertLog", searchMap, true, true);
    	            returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
    				returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
    			} 
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
    
    /**
     * 엑셀로딩관리 등록
     * @param      
     * @return String
     * @throws 
     */
    public SearchMap insertTargetDB(SearchMap searchMap) {
        HashMap returnMap = new HashMap();    
        ArrayList excelDataList = new ArrayList();
        int inputValCnt = 0;
        try {
        	setStartTransaction();
        	
        	ExcelUpload excel = ExcelUpload.getInstance();
        	excelDataList = excel.execlTgtUpload(searchMap);
        	
        	if(null != excelDataList && 0 < excelDataList.size()) {
        		String[] strCode = (String[]) excelDataList.get(0);
        		String[] strMon = (String[]) excelDataList.get(1);
                String[] strValue = (String[]) excelDataList.get(2);
                
                /**********************************
                 * 기존 등록된 실적 삭제
                 **********************************/
                
                returnMap = insertData("bsc.tam.excelInterface.deleteTgtData", searchMap);
                
                String tempMetricId = "";
                
                for(int i=0; i<strCode.length; i++) {
                	
                	if(!"".equals(StaticUtil.nullToBlank(strCode[i])) && !"".equals(StaticUtil.nullToBlank(strMon[i]))) {
	                	searchMap.put("metricId", strCode[i]);
	                	searchMap.put("mon", strMon[i]);
	                	searchMap.put("target", strValue[i]);
	                	
	                	returnMap = insertData("bsc.tam.excelInterface.insertTgtData", searchMap);
	                	
	                	inputValCnt++;
                	}
                }
        	}
        	
        	
        	ArrayList tgtList = (ArrayList)getList("bsc.tam.excelInterface.getExcelTempTgtList", searchMap);
        	ArrayList tgtDisList = (ArrayList)getList("bsc.tam.excelInterface.getExcelTempDistinctList", searchMap);
        	
        	HashMap tgt1Map = new HashMap();
        	HashMap tgt2Map = new HashMap();
        	HashMap tgt3Map = new HashMap();
        	
        	if(0<tgtDisList.size()){
        		
        		for(int a=0; a<tgtDisList.size(); a++){
        			tgt1Map = (HashMap)tgtDisList.get(a);
        			
        			searchMap.put("metricId", tgt1Map.get("METRIC_ID"));
        			
        			returnMap = insertData("bsc.tam.excelInterface.deleteTgtValueData", searchMap);
        			
        		}
        		
        	}
        	
        	if(0<tgtList.size()){
        		
        		for(int b=0; b<tgtList.size(); b++){
        			tgt2Map = (HashMap)tgtList.get(b);
        			
        			searchMap.put("metricId", tgt2Map.get("METRIC_ID"));
        			searchMap.put("mon", tgt2Map.get("MON"));
        			searchMap.put("analCycle", "M");
        			searchMap.put("target", (String) tgt2Map.get("TGT_VALUE"));
                	searchMap.put("targetStatusId", "04");
                	searchMap.put("content", " ");
                	
        			returnMap = insertData("bsc.tam.targetMng.insertDataProc", searchMap);
        			
        		}
        		
        	}
        	
        	if(0<tgtDisList.size()){
        		
        		for(int c=0; c<tgtDisList.size(); c++){
        			tgt3Map = (HashMap)tgtDisList.get(c);
        			
        			searchMap.put("metricId", tgt3Map.get("METRIC_ID"));
        			
        			returnMap = insertData("bsc.tam.targetMng.targetTimeRollup", searchMap);
        			
        		}
        		
        		for(int c=0; c<tgtDisList.size(); c++){
        			tgt3Map = (HashMap)tgtDisList.get(c);
        			
        			searchMap.put("metricId", tgt3Map.get("METRIC_ID"));
        			searchMap.put("targetStatusId", "04");
        			searchMap.put("returnReason", "");
        			
        			returnMap = updateData("bsc.tam.targetMng.updateStatusData", searchMap, true);
        			
        		}
        		
        	}
        	
        	//returnMap = insertData("bsc.tam.targetMng.insertDataProc", searchMap);
        	
        	//returnMap = insertData("bsc.tam.targetMng.targetTimeRollup", searchMap);
        	
        	/**********************************
             * 로그등록
             **********************************/
            searchMap.put("resultYn", "Y");
            searchMap.put("inputValue", inputValCnt);
            returnMap = deleteData("bsc.tam.excelInterface.deleteLog", searchMap, true);
            returnMap = insertData("bsc.tam.excelInterface.insertLog", searchMap);
        	
            /**********************************
             * 등록 실패시 작업
             **********************************/
            Integer chkVal = (Integer)searchMap.get("chkVal");
    		if(null != chkVal) {
    			if(chkVal.intValue() < 0){
    				searchMap.put("resultYn", "N");
    	            searchMap.put("inputValue", 0);
    	            returnMap = deleteData("bsc.tam.excelInterface.deleteLog", searchMap, true, true);
    	            returnMap = insertData("bsc.tam.excelInterface.insertLog", searchMap, true, true);
    	            returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
    				returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
    			} 
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
    	excelInfoList.add(new ExcelVO("조직코드", "DEPT_ID", "center"));
    	excelInfoList.add(new ExcelVO("조직명", "DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("실적", "", "left", 4000));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.tam.excelInterface.getExcelTemplate", searchMap));
    	
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
        
        //Validation 체크 추가
        
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }
    
    
}
