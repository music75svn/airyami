/*************************************************************************
* CLASS 명      : @CLASSNAME@Action
* 작 업 자      : @USERNAME@
* 작 업 일      : @TODAY@
* 기    능      : @DESC@
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    @USERNAME@      @TODAY@        최 초 작 업 
**************************************************************************/
package com.lexken.@SYSNAME@.@PKGNAME@;
    
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

public class @CLASSNAME@Action extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * @DESC@ 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap @CLASSSMALLNAME@List(SearchMap searchMap) {

        return searchMap;
    }
    
    /**
     * @DESC@ 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap @CLASSSMALLNAME@List_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("@SYSNAME@.@PKGNAME@.@CLASSSMALLNAME@.getList", searchMap));

        return searchMap;
    }
    
    /**
     * @DESC@ 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap @CLASSSMALLNAME@Modify(SearchMap searchMap) {
        String stMode = searchMap.getString("mode");
        
        if("MOD".equals(stMode)) {
            searchMap.addList("detail", getDetail("@SYSNAME@.@PKGNAME@.@CLASSSMALLNAME@.getDetail", searchMap));
        }
        
        return searchMap;
    }
    
    /**
     * @DESC@ 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap @CLASSSMALLNAME@Process(SearchMap searchMap) {
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
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * @DESC@ 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
            setStartTransaction();
        
            returnMap = insertData("@SYSNAME@.@PKGNAME@.@CLASSSMALLNAME@.insertData", searchMap);
        
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
     * @DESC@ 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
            setStartTransaction();
            
            returnMap = updateData("@SYSNAME@.@PKGNAME@.@CLASSSMALLNAME@.updateData", searchMap);
            
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
     * @DESC@ 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
        
        try {
            @KEYCOLNAMESACTION1@
            
            setStartTransaction();
            
            if(null != @KEYMAINCOLNAMESACTION@ && 0 < @KEYMAINCOLNAMESACTION@.length) {
                for (int i = 0; i < @KEYMAINCOLNAMESACTION@.length; i++) {
                    @KEYCOLNAMESACTION2@
                    returnMap = updateData("@SYSNAME@.@PKGNAME@.@CLASSSMALLNAME@.deleteData", searchMap);
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
        
@NOTCOLUMNACTION@
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }

    /**
     * @DESC@ 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap @CLASSSMALLNAME@ListExcel(SearchMap searchMap) {
        String excelFileName = "@DESC@";
        String excelTitle = "@DESC@ 리스트";
        
        /************************************************************************************
         * 조회조건 설정
         ************************************************************************************/
        ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
        
        excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
        //excelSearchInfoList.add(new ExcelVO("공통코드명", StaticUtil.nullToDefault((String)searchMap.get("codeGrpNm"), "전체")));
        //excelSearchInfoList.add(new ExcelVO("사용여부", (String)searchMap.get("useYnNm")));
        
        
        /****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
        ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
        @COLEXCEL@
        
        searchMap.put("excelFileName", excelFileName);
        searchMap.put("excelTitle", excelTitle);
        searchMap.put("excelSearchInfoList", excelSearchInfoList);
        searchMap.put("excelInfoList", excelInfoList);
        searchMap.put("excelDataList", (ArrayList)getList("@SYSNAME@.@PKGNAME@.@CLASSSMALLNAME@.getList", searchMap));
        
        return searchMap;
    }
    
}
