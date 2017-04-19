/*************************************************************************
* CLASS 명      : EvalDegreeAction
* 작 업 자      : 양익수
* 작 업 일      : 2014년 12월 18일 
* 기    능      : 간부개인업적평가 평가가중치
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    양익수      2014년 12월 18일         최 초 작 업 
**************************************************************************/
package com.lexken.prs.eval;
    
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
import com.lexken.framework.login.LoginVO;
import com.lexken.framework.util.StaticUtil;

public class EvalDegreeAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 간부개인업적평가 평가가중치 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalDegreeList(SearchMap searchMap) {

    	/*
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
		searchMap.put("createById", loginVO.getUser_id());
		searchMap.put("modifyById", loginVO.getUser_id());
    	*/
    	
        return searchMap;
    }
    
    /**
     * 간부개인업적평가 평가가중치 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalDegreeList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("prs.eval.evalDegree.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 간부개인업적평가 평가가중치 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalDegreeModify(SearchMap searchMap) {
        String stMode = searchMap.getString("mode");
        
        if("MOD".equals(stMode)) {
            searchMap.addList("detail", getDetail("prs.eval.evalDegree.getDetail", searchMap));
        }
        
        return searchMap;
    }
    
    /**
     * 간부개인업적평가 평가가중치 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalDegreeProcess(SearchMap searchMap) {
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
        /*
        if("ADD".equals(stMode)) {
            searchMap = insertDB(searchMap);
        } else if("MOD".equals(stMode)) {
            searchMap = updateDB(searchMap);
        } else if("DEL".equals(stMode)) {
            searchMap = deleteDB(searchMap);
        }
        */
        LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
		searchMap.put("createById", loginVO.getUser_id());
		searchMap.put("modifyById", loginVO.getUser_id());
		
        searchMap = processDB(searchMap);
        
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    
    
    /**
     * 간부개인업적평가 평가가중치 처리
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap processDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();            
        String stMode = "";
                
        try {
  
        	String[] evalDegreeIds = searchMap.getString("evalDegreeIds").split("\\|", 0);
            String[] Weights = searchMap.getString("Weights").split("\\|", 0);
            String[] jobTypes = searchMap.getString("jobTypes").split("\\|", 0);
            
            setStartTransaction();
            
            if(null != evalDegreeIds && 0 < evalDegreeIds.length) {
                for (int i = 0; i < evalDegreeIds.length; i++) {
                    searchMap.put("evalDegreeId", evalDegreeIds[i]);
                    searchMap.put("weight", Weights[i]);
                    stMode = jobTypes[i];
                    if("ADD".equals(stMode)) {
                        searchMap = insertDB(searchMap);
                    } else if("MOD".equals(stMode)) {
                        searchMap = updateDB(searchMap);
                    } else if("DEL".equals(stMode)) {
                        searchMap = deleteDB(searchMap);
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
     * 간부개인업적평가 평가가중치 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {      	
            setStartTransaction();
            returnMap = insertData("prs.eval.evalDegree.insertData", searchMap);
           
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
     * 간부개인업적평가 평가가중치 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
            setStartTransaction();           
            returnMap = updateData("prs.eval.evalDegree.updateData", searchMap);
                        
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
     * 간부개인업적평가 평가가중치 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
        
        try {
            setStartTransaction();
            returnMap = updateData("prs.eval.evalDegree.deleteData", searchMap);
            
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
        
        returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("weight"), "가중치");
        if((Integer)returnMap.get("ErrorNumber") < 0) {
            return returnMap;
        }


        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }

    /**
     * 간부개인업적평가 평가가중치 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap evalDegreeListExcel(SearchMap searchMap) {
        String excelFileName = "간부개인업적평가 평가가중치";
        String excelTitle = "간부개인업적평가 평가가중치 리스트";
        
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
        excelInfoList.add(new ExcelVO("기준년도", "YEAR", "left"));
    	excelInfoList.add(new ExcelVO("평가차수 코드", "EVAL_DEGREE_ID", "left"));
    	excelInfoList.add(new ExcelVO("코드명", "CODE_NM", "left"));
    	excelInfoList.add(new ExcelVO("가중치", "WEIGHT", "left"));
    	excelInfoList.add(new ExcelVO("지표정의", "CONTENT", "left"));
    	
        
        searchMap.put("excelFileName", excelFileName);
        searchMap.put("excelTitle", excelTitle);
        searchMap.put("excelSearchInfoList", excelSearchInfoList);
        searchMap.put("excelInfoList", excelInfoList);
        searchMap.put("excelDataList", (ArrayList)getList("prs.eval.evalDegree.getList", searchMap));
        
        return searchMap;
    }
    
}
