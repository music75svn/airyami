/*************************************************************************
* CLASS 명      : AnnSalAction
* 작 업 자      : 안요한
* 작 업 일      : 2013년 06월 19일
* 기    능      : 성과연봉
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일				변 경 내 용        	 비고
* ----  ---------   -----------------  	-------------------------  --------
*   1    안 요 한    2013년 06월 19일        	최 초 작 업
**************************************************************************/
package com.lexken.prs.annSal;

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

public class AnnSalAction extends CommonService {
	private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 성과연봉 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap annSalList(SearchMap searchMap) {

    	InsaAction insaDept = new InsaAction();
    	
    	 String confYn 		= getStr("prs.annSal.annSal.getConfYn", searchMap);
    	 int modYn 		 	= getInt("prs.annSal.annSal.getModYn", searchMap);
    	 int countYn		= getInt("prs.annSal.annSal.getCountYn", searchMap);
         
         searchMap.put("confYn", confYn);
         searchMap.put("modYn", modYn);
         searchMap.put("countYn", countYn);
    	
        return insaDept.insaList(searchMap);
    	
    }

    /**
     * 성과연봉 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap annSalList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("prs.annSal.annSal.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 성과연봉 일괄수정 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap annSalReviseList(SearchMap searchMap) {
    	
    	InsaAction insaDept = new InsaAction();
    	
    	String confYn = getStr("prs.annSal.annSal.getConfYn", searchMap);
    	
    	searchMap.put("confYn", confYn);
    	
    	return insaDept.insaList(searchMap);
    	
    }
    
    /**
     * 성과연봉 일괄수정 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap annSalReviseList_xml(SearchMap searchMap) {
    	
    	searchMap.addList("list", getList("prs.annSal.annSal.getList", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 성과연봉 상세화면
     * @param
     * @return String
     * @throws
     */
    public SearchMap annSalModify(SearchMap searchMap) {

    	searchMap.addList("detail", getList("prs.annSal.annSal.getDetail", searchMap));
    	searchMap.addList("basic",  getDetail("prs.annSal.annSal.getBasic", searchMap));
    	searchMap.addList("totalCount",  getDetail("prs.annSal.annSal.getTotalCount", searchMap));
    	
    	String year = Integer.toString(Integer.parseInt((String)searchMap.get("findYear"))+1);
    	
    	String yearPlus =  StaticUtil.nullToDefault((String)searchMap.getString("yearPlus"), year);	
    	searchMap.put("yearPlus", yearPlus);
    	
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
     * 성과연봉 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap annSalExcel(SearchMap searchMap) {
        String excelFileName = "성과연봉양식다운로드";
        String excelTitle = "성과연봉";

        /************************************************************************************
         * 조회조건 설정
         ************************************************************************************/
        ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();

        excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));

        /****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
        ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
        excelInfoList.add(new ExcelVO("부서코드", "DEPT_CD", "center"));
        excelInfoList.add(new ExcelVO("소속부서", "DEPT_KOR_NM", "left"));
        excelInfoList.add(new ExcelVO("사원번호","EMPN", "center"));
        excelInfoList.add(new ExcelVO("성명", "KOR_NM", "center"));
        //excelInfoList.add(new ExcelVO("직급명", "CAST_TC_NM", "center"));
        excelInfoList.add(new ExcelVO("평가군", "EVAL_GRP_NM", "left"));
        excelInfoList.add(new ExcelVO("근무월수", "WORK_MON", "center"));
        excelInfoList.add(new ExcelVO("연차일수", "ANN_DAY", "center"));
        excelInfoList.add(new ExcelVO("기초연봉", "BASIC_SAL", "right"));
        excelInfoList.add(new ExcelVO("직무연봉", "JOB_SAL", "right"));
        excelInfoList.add(new ExcelVO("연차수당", "ANN_ALLO", "right"));
        excelInfoList.add(new ExcelVO("성과연봉", "ANN_SAL", "right"));
        excelInfoList.add(new ExcelVO("평가점수", "SCORE", "center"));
        excelInfoList.add(new ExcelVO("평가등급", "GRADE", "center"));
        excelInfoList.add(new ExcelVO("지급률", "RATE", "center"));
        excelInfoList.add(new ExcelVO("차등반영성과연봉", "DIF_ANN_SAL", "right"));
        excelInfoList.add(new ExcelVO("중식보조비", "LUNCH_ALLO", "right"));
        excelInfoList.add(new ExcelVO("자체성과급", "INSIDE_PAY", "right"));
        excelInfoList.add(new ExcelVO("정평성과급", "GOV_PAY", "right"));
        excelInfoList.add(new ExcelVO("별도지급액", "SEP_PAY", "right"));
        excelInfoList.add(new ExcelVO("인건비지급액", "LABOR_PAY", "right"));

        searchMap.put("excelFileName", excelFileName);
        searchMap.put("excelTitle", excelTitle);
        searchMap.put("excelSearchInfoList", excelSearchInfoList);
        searchMap.put("excelInfoList", excelInfoList);

        searchMap.put("excelDataList", (ArrayList)getList("prs.annSal.annSal.getAnnSalSetExcelList", searchMap));

        return searchMap;

    }
    
    /**
     * 성과연봉 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap annSalProcess(SearchMap searchMap) {
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
        if("COUNT".equals(stMode)){
        	 returnMap = insertData("prs.annSal.annSal.annSalSet", searchMap);
        } else if ("CONF".equals(stMode)) {
        	searchMap = updateSubmitDB(searchMap);
        } else if ("MOD".equals(stMode)) {
        	searchMap = updateDB(searchMap);
        } else if ("REVISE".equals(stMode)) {
        	searchMap = updateAnnSalConf(searchMap);
        } else if ("DEL".equals(stMode)) {
        	searchMap = deleteAnnSalConf(searchMap);
        } 
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     *  확정하기
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateSubmitDB(SearchMap searchMap) {
    	HashMap returnMap    = new HashMap();    
		String confYn = searchMap.getString("confYn");
    	try {
    		setStartTransaction();
    		
    		if("N".equals(confYn)) {
    			returnMap = updateData("prs.annSal.annSal.insertConfYn", searchMap, true); //확정여부 수정
    			returnMap = deleteData("prs.annSal.annSal.deleteData", searchMap, true); //ANN_SAN_CONF 수정
    			
    		} else {
    			
    			returnMap = updateData("prs.annSal.annSal.insertConfYn", searchMap, true); //확정여부 수정
    		}
    	} catch (Exception e) {
    		setRollBackTransaction();
    		logger.error(e.toString());
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
     *  성과연봉 통보서 수정하기
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
    	HashMap returnMap    = new HashMap();    
    	String confYn = searchMap.getString("confYn");
    	try {
    		setStartTransaction();
    		
    		if("N".equals(confYn)) {
    			returnMap = updateData("prs.annSal.annSal.insertConfYn", searchMap); //확정여부 수정
    			returnMap = deleteData("prs.annSal.annSal.deleteData", searchMap); //ANN_SAN_CONF 수정
    			
    		} else {
    			
    			returnMap = updateData("prs.annSal.annSal.insertConfYn", searchMap); //확정여부 수정
    		}
    	} catch (Exception e) {
    		setRollBackTransaction();
    		logger.error(e.toString());
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
     *  성과연봉 일괄수정하기
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateAnnSalConf(SearchMap searchMap) {
    	HashMap returnMap    = new HashMap();    
    	try {
    		setStartTransaction();
    		
    		String[] empns = searchMap.getStringArray("empn");
        	String[] evalGrpIds = searchMap.getStringArray("evalGrpId");
        	String[] basicSals = searchMap.getStringArray("basicSal");
        	String[] jobSals = searchMap.getStringArray("jobSal");
        	String[] difAnnSals = searchMap.getStringArray("difAnnSal");
        	String[] workMons = searchMap.getStringArray("workMon");
        	String[] lunchAllos = searchMap.getStringArray("lunchAllo");
            String[] insidePays = searchMap.getStringArray("insidePay");
            String[] govPays = searchMap.getStringArray("govPay");
            String[] laborPays = searchMap.getStringArray("laborPay");
     //       String[] years = searchMap.getStringArray("year");

            for(int i=0; i<empns.length; i++){
            	if(null != empns[i]){
            		searchMap.put("empn", empns[i]);
            		searchMap.put("evalGrpId", evalGrpIds[i]);
            		searchMap.put("basicSal", basicSals[i].replace(",", ""));
            		searchMap.put("jobSal", jobSals[i].replace(",", ""));
            		searchMap.put("difAnnSal", difAnnSals[i].replace(",", ""));
            		searchMap.put("workMon", workMons[i]);
            		searchMap.put("lunchAllo", lunchAllos[i].replace(",", ""));
            		searchMap.put("insidePay", insidePays[i].replace(",", ""));
            		searchMap.put("govPay", govPays[i].replace(",", ""));
            		searchMap.put("laborPay", laborPays[i].replace(",", ""));
            	//	searchMap.put("year", years[i]);

            		returnMap = updateData("prs.annSal.annSal.updateAnnSalConf", searchMap); //성과연봉 일괄수정
            	}
            }
    			
    	} catch (Exception e) {
    		setRollBackTransaction();
    		logger.error(e.toString());
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
     * 성과연봉 일괄수정 데이터 삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteAnnSalConf(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 

        try {
        	String empns = searchMap.getString("empns");
        	String evalGrpIds = searchMap.getString("evalGrpIds");
	        String[] keyArray1 = empns.split("\\|", 0);
	        String[] keyArray2 = evalGrpIds.split("\\|", 0);
	 
	        setStartTransaction();
	        
	        for (int i=0; i<keyArray1.length; i++) {
	            searchMap.put("empn", keyArray1[i]);
	            searchMap.put("evalGrpId", keyArray2[i]);
	            returnMap = deleteData("prs.annSal.annSal.deleteAnnSalConf", searchMap);
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
    
}
