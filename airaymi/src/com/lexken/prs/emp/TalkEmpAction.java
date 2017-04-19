/*************************************************************************
* CLASS 명      : TalkEmpAction
* 작 업 자      : 심준혁
* 작 업 일      : 2015년 1월 22일 
* 기    능      : 직원개인성과 중간면담
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    심준혁      2015년 1월 22일         최 초 작 업 
**************************************************************************/
package com.lexken.prs.emp;
    
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

public class TalkEmpAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 직원개인성과 중간면담 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap talkEmpList(SearchMap searchMap) {

        return searchMap;
    }
    
    /**
     * 직원개인성과 중간면담 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap talkEmpList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("prs.emp.talkEmp.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 직원개인성과 중간면담 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap talkEmpModify(SearchMap searchMap) {
        String stMode = searchMap.getString("mode");
        
        if("MOD".equals(stMode)) {
            searchMap.addList("detail", getDetail("prs.emp.talkEmp.getDetail", searchMap));
        }
        
        return searchMap;
    }
    
    /**
     * 직원개인성과 중간면담 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap talkEmpProcess(SearchMap searchMap) {
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
     * 직원개인성과 중간면담 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
            setStartTransaction();
        
            returnMap = insertData("prs.emp.talkEmp.insertData", searchMap);
        
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
     * 직원개인성과 중간면담 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
            setStartTransaction();
            
            returnMap = updateData("prs.emp.talkEmp.updateData", searchMap);
            
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
     * 직원개인성과 중간면담 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
        
        try {
            
            
            setStartTransaction();
            
//            if(null !=  && 0 < .length) {
//                for (int i = 0; i < .length; i++) {
//                    
//                    returnMap = updateData("prs.emp.talkEmp.deleteData", searchMap);
//                }
//            }
            
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
     * 직원개인성과 중간면담 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap talkEmpListExcel(SearchMap searchMap) {
        String excelFileName = "직원개인성과 중간면담";
        String excelTitle = "직원개인성과 중간면담 리스트";
        
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
        excelInfoList.add(new ExcelVO("평가년도", "YEAR", "left"));
    	excelInfoList.add(new ExcelVO("평가대상자 사번", "EVAL_MEMB_EMPN", "left"));
    	excelInfoList.add(new ExcelVO("평가대상자 순번", "EVAL_MEMB_EMPN_SEQ", "left"));
    	excelInfoList.add(new ExcelVO("면담 순번", "TALK_SEQ", "left"));
    	excelInfoList.add(new ExcelVO("면담자 사번", "TALK_USER_ID", "left"));
    	excelInfoList.add(new ExcelVO("면담자 성명", "TALK_USER_NM", "left"));
    	excelInfoList.add(new ExcelVO("면담자 부서코드", "TALK_DEPT_CD", "left"));
    	excelInfoList.add(new ExcelVO("면담자 부서명", "TALK_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("면담자 직급코드", "TALK_CAST_TC", "left"));
    	excelInfoList.add(new ExcelVO("면담자 직급명", "TALK_CAST_NM", "left"));
    	excelInfoList.add(new ExcelVO("면담자 직위코드", "TALK_POS_TC", "left"));
    	excelInfoList.add(new ExcelVO("면담자 직위명", "TALK_POS_NM", "left"));
    	excelInfoList.add(new ExcelVO("면담일자", "TALK_DT", "left"));
    	excelInfoList.add(new ExcelVO("면담내용", "TALK_CONTENT", "left"));
    	excelInfoList.add(new ExcelVO("답변날짜", "ANSWER_DT", "left"));
    	excelInfoList.add(new ExcelVO("ANSWER_CONTENT", "ANSWER_CONTENT", "left"));
    	excelInfoList.add(new ExcelVO("생성자", "CREATE_ID", "left"));
    	excelInfoList.add(new ExcelVO("생성일", "CREATE_DT", "left"));
    	excelInfoList.add(new ExcelVO("수정자", "MODIFY_ID", "left"));
    	excelInfoList.add(new ExcelVO("수정일", "MODIFY_DT", "left"));
    	
        
        searchMap.put("excelFileName", excelFileName);
        searchMap.put("excelTitle", excelTitle);
        searchMap.put("excelSearchInfoList", excelSearchInfoList);
        searchMap.put("excelInfoList", excelInfoList);
        searchMap.put("excelDataList", (ArrayList)getList("prs.emp.talkEmp.getList", searchMap));
        
        return searchMap;
    }
    
}
