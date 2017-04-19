/*************************************************************************
* CLASS 명      : InsaAction
* 작 업 자      : 김상용
* 작 업 일      : 2013년 05월 10일 
* 기    능      : 인사정보
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일				변 경 내 용        	 비고
* ----  ---------   -----------------  	-------------------------  --------
*   1    김 상 용    2013년 05월 10일        	최 초 작 업 
**************************************************************************/
package com.lexken.prs.insa;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

public class InsaAction extends CommonService {
	private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 인사정보 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insaList(SearchMap searchMap) {
    	
    	//확정여부 조회
    	searchMap.addList("confirmYn", getDetail("prs.insa.insa.getConfirmYn", searchMap)); 
    	
    	// 최상위 평가조직 조회
    	HashMap topScDept = (HashMap)getDetail("prs.insa.insa.getTopDeptInfo", searchMap);


    	// 파라미터가 null로 들어올 때 디폴트값 셋팅.
    	if (topScDept == null) {
    		topScDept = new HashMap();
    		topScDept.put("DEPT_CD", "");
    		topScDept.put("DEPT_KOR_NM", "");
    	}
    	
    	// 파라미터가 null로 들어올 때 디폴트값 셋팅.
    	String findDeptCd =  StaticUtil.nullToDefault((String)searchMap.getString("findDeptCd"), (String)topScDept.get("DEPT_CD"));	// 조직코드가 없으면 전사조직코드를 셋팅.
    	String findUpDeptName =  StaticUtil.nullToDefault((String)searchMap.getString("findUpDeptName"), (String)topScDept.get("DEPT_KOR_NM")) ; ;	// 조직명이 없으면 전사조직명을 셋팅.
    	
    	// 디폴트 조회조건 설정
    	searchMap.put("findDeptCd", findDeptCd);	// 검색버튼 조회시 조직트리에 기존 선택한 조직이 표시되도록 설정.(findSearchCodeId에 넣어주면 우선 적용됨. 트리와 검색조건을 별도로 사용할 경우에 한함.)
    	searchMap.put("findUpDeptName", findUpDeptName);
    	
    	searchMap.addList("deptTree", getList("prs.insa.insa.getDeptList", searchMap)); //미사용은 제외한 인사조직
    	
        return searchMap;
    }
    
    /**
     * 인사정보 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insaList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("prs.insa.insa.getList", searchMap));
        
        return searchMap;
    }
    
    /**
     * 발령정보 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap pcmtInfoList_xml(SearchMap searchMap) {
    	
    	searchMap.addList("list", getList("prs.insa.insa.getDetail", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 인사정보 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insaModify(SearchMap searchMap) {
    	String mode = (String)searchMap.get("mode");
    	
    	searchMap.addList("confirmYn", getDetail("prs.insa.insa.getConfirmYn", searchMap)); //확정여부 조회
    	
    	searchMap.addList("deptTree", getList("prs.insa.insa.getDeptList", searchMap)); //미사용은 제외한 인사조직
    	
    	List posTcList = getList("prs.insa.insa.getPosTc", searchMap);
    	searchMap.addList("posTcList", posTcList);
    	
        return searchMap;
        
    }
    
    /**
     * 엑셀업로드 팝업 - 엑셀업로드는 작업해야함
     * @param
     * @return String
     * @throws
     */
    public SearchMap popExcelUpload(SearchMap searchMap) {

        return searchMap;
    }
    
        
    /**
     * 인사정보 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insaProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("ADD".equals(stMode)) {        	
        	searchMap = insertDB(searchMap);
        } else if("GET".equals(stMode)) {
            searchMap = insertInsaInfo(searchMap);
        } else if("DEL".equals(stMode)) {
            searchMap = deleteDB(searchMap); 
        } else if ("CONF".equals(stMode)) {
        	searchMap = updateSubmitDB(searchMap);
        }
        
         return searchMap;
    }
    
    /**
     * 발령정보 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
            String[] empns = searchMap.getStringArray("empns");
            String[] sers = searchMap.getStringArray("sers");
            String[] orgStartDts = searchMap.getStringArray("orgStartDts");
            String[] korNms = searchMap.getStringArray("korNms");
            String[] deptCds = searchMap.getStringArray("deptCds");
            String[] deptNms = searchMap.getStringArray("deptNms");
            String[] pcmtStartDts = searchMap.getStringArray("pcmtStartDts");
            String[] pcmtEndDts = searchMap.getStringArray("pcmtEndDts");
            String[] workMons = searchMap.getStringArray("workMons");
            String[] posTcs = searchMap.getStringArray("posTcs");
            String[] castTcs = searchMap.getStringArray("castTcs");
            String[] pcmtTcs = searchMap.getStringArray("pcmtTcs");
            String[] empmKindTcs = searchMap.getStringArray("empmKindTcs");
            
            for(int i=0; i<empns.length; i++){
            	if(null != empns[i]){
            		searchMap.put("empn", empns[i]);
            		searchMap.put("ser", sers[i]);
            		searchMap.put("orgStartDt", orgStartDts[i]);
            		searchMap.put("korNm", korNms[i]);
            		searchMap.put("deptCd", deptCds[i]);
            		searchMap.put("deptNm", deptNms[i]);
            		searchMap.put("pcmtStartDt", pcmtStartDts[i]);
            		searchMap.put("pcmtEndDt", pcmtEndDts[i]);
            		searchMap.put("workMon", workMons[i]);
            		searchMap.put("posTc", posTcs[i]);
            		searchMap.put("castTc", castTcs[i]);
            		searchMap.put("pcmtTc", pcmtTcs[i]);
            		searchMap.put("empmKindTc", empmKindTcs[i]);
            		
            		String empn = empns[i];
            		String orgStartDt = orgStartDts[i];
            		String ser = sers[i];
            		
        			if(!"".equals(empn) && !"".equals(orgStartDt) && !"".equals(ser)){	
        				returnMap = deleteData("prs.insa.insa.deletePcmtInfoData", searchMap);
        			}
            		returnMap = insertData("prs.insa.insa.insertPcmtInfoData", searchMap);
            	}
            }
            // 총 근무월 계산 - 1명
            if(null != searchMap.get("empn")){
            	returnMap = updateData("prs.insa.insa.insertotMonData", searchMap);
            	
            //인사정보 갱신
            	returnMap = insertData("prs.insa.insa.insertInsaInfo", searchMap);
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
     * 인사정보 가져오기
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertInsaInfo(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = insertData("prs.insa.insa.insertInsaDept", searchMap);
	        returnMap = insertData("prs.insa.insa.insertPcmtInfo", searchMap);
	        returnMap = insertData("prs.insa.insa.insertInsaInfo", searchMap);
	        
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
     * 발령정보 삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap deleteDB(SearchMap searchMap) {

        HashMap returnMap = new HashMap();

        try {
            setStartTransaction();
            
            String[] chks = searchMap.getStringArray("chk");
            String[] empns = searchMap.getStringArray("empns");
            String[] orgStartDts = searchMap.getStringArray("orgStartDts");
            String[] sers = searchMap.getStringArray("sers");
            
            for(int i=0; i < chks.length; i++) {
            	if(!chks[i].equals("") && chks[i] != null) {
	            	searchMap.put("empn", empns[Integer.parseInt(chks[i])-1]);
	        	    searchMap.put("ser", sers[Integer.parseInt(chks[i])-1]);
		            searchMap.put("orgStartDt", orgStartDts[Integer.parseInt(chks[i])-1]);
	            
		            if(null != empns[Integer.parseInt(chks[i])-1] && null != orgStartDts[Integer.parseInt(chks[i])-1] && null != sers[Integer.parseInt(chks[i])-1]){
		            	returnMap = updateData("prs.insa.insa.deletePcmtInfoData", searchMap);
		            }
            	}
	        }
            
            returnMap = insertData("prs.insa.insa.insertInsaInfo", searchMap);
            
            
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
     *  확정하기
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateSubmitDB(SearchMap searchMap) {
    	HashMap returnMap    = new HashMap();    
		String insaYn = searchMap.getString("insaYn");
    	try {
    		setStartTransaction();
    		returnMap = updateData("prs.insa.insa.insertInsaYnData", searchMap); //확정여부 수정
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
     * 인사정보 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap insaExcel(SearchMap searchMap) {
        String excelFileName = "인사정보_입력_양식";
        String excelTitle = "인사정보_입력_양식";

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
        excelInfoList.add(new ExcelVO("소속부서명", "DEPT_KOR_NM", "left"));
        excelInfoList.add(new ExcelVO("직급코드", "CAST_TC", "center"));
        excelInfoList.add(new ExcelVO("직급명", "CAST_TC_NM", "center"));
        excelInfoList.add(new ExcelVO("직위코드", "POS_TC", "center"));
        excelInfoList.add(new ExcelVO("직위명", "POS_TC_NM", "center"));
        excelInfoList.add(new ExcelVO("근무개월수", "MON", "center"));
        excelInfoList.add(new ExcelVO("고용형태", "EMPM_KIND_NM", "center"));

        searchMap.put("excelFileName", excelFileName);
        searchMap.put("excelTitle", excelTitle);
        searchMap.put("excelSearchInfoList", excelSearchInfoList);
        searchMap.put("excelInfoList", excelInfoList);

        searchMap.put("excelDataList", (ArrayList)getList("prs.insa.insa.getInsaExcelList", searchMap));

        return searchMap;
        
    }
    /**
     * 발령정보 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap pcmtExcel(SearchMap searchMap) {
    	String excelFileName = "발령정보_입력_양식";
    	String excelTitle = "발령정보_입력_양식";
    	
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
//		excelInfoList.add(new ExcelVO("상위부서코드", "UP_DEPT_CD", "center"));
//    	excelInfoList.add(new ExcelVO("상위부서명", "UP_DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("소속부서명", "DEPT_KOR_NM", "left"));
    	excelInfoList.add(new ExcelVO("직급명", "CAST_TC_NM", "center"));
    	excelInfoList.add(new ExcelVO("직위명", "POS_TC_NM", "center"));
    	excelInfoList.add(new ExcelVO("발령코드", "PCMT_TC", "center"));
    	excelInfoList.add(new ExcelVO("발령사유", "PCMT_TC_NM", "center"));
    	excelInfoList.add(new ExcelVO("고용형태", "EMPM_KIND_TC_NM", "center"));
    	excelInfoList.add(new ExcelVO("근무시작일", "START_PCMT_DATE", "center"));
    	excelInfoList.add(new ExcelVO("근무종료일", "END_PCMT_DATE", "center"));
    	excelInfoList.add(new ExcelVO("근무일수", "WORK_DAY", "center"));
    	excelInfoList.add(new ExcelVO("근무월수", "WORK_MON", "center"));
    	excelInfoList.add(new ExcelVO("총근무월수", "TOT_MON", "center"));
    	
    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	
    	searchMap.put("excelDataList", (ArrayList)getList("prs.insa.insa.getPcmtExcelList", searchMap));
    	
    	return searchMap;
    }
        
}
