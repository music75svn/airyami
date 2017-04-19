/*************************************************************************
* CLASS 명      : BasicSalAction
* 작 업 자      : 안요한
* 작 업 일      : 2013년 06월 20일
* 기    능      : 기초연봉산출
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일				변 경 내 용        	 비고
* ----  ---------   -----------------  	-------------------------  --------
*   1    안 요 한    2013년 06월 20일        	최 초 작 업
**************************************************************************/
package com.lexken.prs.basicSal;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.FileInfoVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.StringConstants;
import com.lexken.framework.config.FileConfig;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.util.StaticUtil;
import com.lexken.framework.common.ExcelUpload;
import com.lexken.prs.insa.InsaAction;

public class BasicSalAction extends CommonService {
	private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 기초연봉 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap basicSalList(SearchMap searchMap) {

    	InsaAction insaDept = new InsaAction();
    	
    	searchMap.addList("detail", getDetail("prs.basicSal.basicSal.getDataCount", searchMap));
    	searchMap = insaDept.insaList(searchMap);

        return searchMap;
    	
    }

    /**
     * 기초연봉 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap basicSalList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("prs.basicSal.basicSal.getList", searchMap));
        
        return searchMap;
    }
    
    /**
     * 엑셀업로드 팝업
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popExcelUpload(SearchMap searchMap) {
    	
        return searchMap;
    }

    /**
     * 기초연봉 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap basicSalExcel(SearchMap searchMap) {
        String excelFileName = "기초연봉양식다운로드";
        String excelTitle = "기초연봉";

        /************************************************************************************
         * 조회조건 설정
         ************************************************************************************/
        ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();

        excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));

        /****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
        ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
        excelInfoList.add(new ExcelVO("평가년도", "YEAR", "center"));
        excelInfoList.add(new ExcelVO("사원번호","EMPN", "center"));
        excelInfoList.add(new ExcelVO("성명", "KOR_NM", "center"));
        excelInfoList.add(new ExcelVO("부서코드", "DEPT_CD", "center"));
        excelInfoList.add(new ExcelVO("소속부서", "DEPT_FULL_NM", "left"));
        excelInfoList.add(new ExcelVO("직급명", "CAST_TC_NM", "center"));
        excelInfoList.add(new ExcelVO("직위명", "POS_TC_NM", "center"));
        excelInfoList.add(new ExcelVO("순위", "RANK", "center"));

        searchMap.put("excelFileName", excelFileName);
        searchMap.put("excelTitle", excelTitle);
        searchMap.put("excelSearchInfoList", excelSearchInfoList);
        searchMap.put("excelInfoList", excelInfoList);

        searchMap.put("excelDataList", (ArrayList)getList("prs.basicSal.basicSal.getBasicSalSetExcelList", searchMap));

        return searchMap;

    }
    
    /**
     * 기초연봉 엑셀변환다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap basicSalChangeExcel(SearchMap searchMap) {
    	String excelFileName = "기초연봉엑셀변환";
    	String excelTitle = "기초연봉목록";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	
    	/****************************************************************************************************
    	 * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
    	 ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("사원번호","EMPN", "center"));
    	excelInfoList.add(new ExcelVO("성명", "KOR_NM", "center"));
    	excelInfoList.add(new ExcelVO("부서코드", "DEPT_CD", "center"));
    	excelInfoList.add(new ExcelVO("소속부서", "DEPT_KOR_NM", "left"));
    	excelInfoList.add(new ExcelVO("입사일", "JCOM_DATE", "center"));
    	excelInfoList.add(new ExcelVO("직급명", "CAST_TC_NM", "center"));
    	excelInfoList.add(new ExcelVO("직위명", "POS_TC_NM", "center"));
    	excelInfoList.add(new ExcelVO("순위", "RANK", "center"));
    	excelInfoList.add(new ExcelVO("등급", "GRADE", "center"));
    	excelInfoList.add(new ExcelVO("차등율", "DIFF_RATE", "center"));
    	excelInfoList.add(new ExcelVO("전년도기초연봉", "BF_BASIC_SAL", "center"));
    	excelInfoList.add(new ExcelVO("연봉증가율", "GR_RATE", "center"));
    	excelInfoList.add(new ExcelVO("평가년도기초연봉", "ST_BASIC_SAL", "center"));
    	excelInfoList.add(new ExcelVO("증가액", "GROWTHPAY", "center"));
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	
    	searchMap.put("excelDataList", (ArrayList)getList("prs.basicSal.basicSal.getBasicSalChangeExcelList", searchMap));
    	
    	return searchMap;
    	
    }
    
    /**
     * 성과연봉 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap basicSalProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 무결성 체크
         **********************************/
        if(!"DEL".equals(stMode)  ) {
            returnMap = this.validChk(searchMap);
            
            if((Integer)returnMap.get("ErrorNumber") < 0 ){
                searchMap.addList("returnMap", returnMap);
                return searchMap;
            }
        }
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("ADDEXCEL".equals(stMode)){
        	searchMap = insertExcelDB(searchMap);
        } else if("COUNT".equals(stMode)){
        	 returnMap = insertData("prs.basicSal.basicSal.basicSalSet", searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * Validation 체크(무결성 체크)
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
    public SearchMap insertExcelDB(SearchMap searchMap) {
        HashMap returnMap = new HashMap();    
        ArrayList excelDataList = new ArrayList();
        
        try {
        	setStartTransaction();
        	
        	ExcelUpload excel = ExcelUpload.getInstance();
        	excelDataList = excel.execlBasicSalUpload(searchMap);
        	
        	if(null != excelDataList && 0 < excelDataList.size()) {
        		String[] strYear = (String[]) excelDataList.get(0);
        		String[] strEmpn = (String[]) excelDataList.get(1);
                String[] strRank = (String[]) excelDataList.get(2);
                
                /**********************************
                 * 기존 등록된 실적 삭제
                 **********************************/
                
                returnMap = insertData("prs.basicSal.basicSal.deleteData", searchMap, true);
                
                for(int i=0; i<strEmpn.length; i++){
                	if(null != strEmpn[i]){
                		searchMap.put("year", strYear[i]);
                		searchMap.put("empn", strEmpn[i]);
                		searchMap.put("rank", strRank[i]);
                		
                		returnMap = insertData("prs.basicSal.basicSal.insertData", searchMap);
                	}
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

}
