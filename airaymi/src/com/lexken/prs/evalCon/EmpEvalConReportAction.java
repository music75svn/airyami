/*************************************************************************
* CLASS 명      : EmpEvalConReportAction
* 작 업 자      : 심준혁
* 작 업 일      : 2014년 12월 22일 
* 기    능      : 업무성과기술서
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    심준혁      2014년 12월 22일         최 초 작 업 
**************************************************************************/
package com.lexken.prs.evalCon;
    
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.FileInfoVO;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.StringConstants;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.config.FileConfig;
import com.lexken.framework.struts2.IsBoxActionSupport;

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

import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.util.HtmlHelper;
import com.lexken.framework.util.StaticUtil;

public class EmpEvalConReportAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 업무성과기술서 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap empEvalConReportList(SearchMap searchMap) {
    	
    	// 최상위 평가조직 조회
    	HashMap topScDept = (HashMap)getDetail("prs.evalCon.empEvalConMember.getTopDeptInfo", searchMap);

    	String findNameEmpn = (String)searchMap.get("findNameEmpn");
    	String findEvalSubmitYn = (String)searchMap.get("findEvalSubmitYn");
    	
    	if("findName".equals(findNameEmpn)){
    		searchMap.put("findName", findNameEmpn);
    	}else{
    		searchMap.put("findEmpn", findNameEmpn);
    	}
    	
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
		searchMap.put("nameEmpn", findNameEmpn);
		
    	searchMap.addList("deptTree", getList("prs.evalCon.empEvalConMember.getDeptList", searchMap)); //인사조직

        return searchMap;
    }
    
    /**
     * 업무성과기술서 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap empEvalConReportList_xml(SearchMap searchMap) {
    	
        searchMap.addList("list", getList("prs.evalCon.empEvalConReport.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 업무성과기술서 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap empEvalConReportModify(SearchMap searchMap) {
        String stMode = searchMap.getString("mode");
        int rptDetailListSize = 0;
        
        if("MOD".equals(stMode)) {

        	// 자기성과기술서 성과목표리스트 가져온다.
        	List rptDetail = getList("prs.evalCon.empEvalCon.getRptDetail", searchMap); // 상세정보

        	rptDetailListSize = rptDetail.size();
        	
        	searchMap.put("listSize", rptDetailListSize);

        	// 자기성과기술서 정보를 가져온다.
        	HashMap rpt = getDetail("prs.evalCon.empEvalCon.getRptMember", searchMap);

//        	if(rpt == null){
//        		rpt = getDetail("prs.evalCon.empEvalCon.getRptReMember", searchMap);//대상자의 정보
//        	}else{
//    			Clob clob = ((Clob)rpt.get("CONTENT"));
//    			String content = "";
//    			Reader reader = null;
//    			char[] cbuf;
//    			
//    			try {
//    				reader = clob.getCharacterStream();
//    				cbuf = new char[(int)(clob.length())];
//    				reader.read(cbuf);
//    				content = new String(cbuf);
//    			} catch (SQLException e) {
//    				e.printStackTrace();
//    			} catch (IOException ioe) {
//    				ioe.printStackTrace();
//    			} finally {
//    				rpt.put("CONTENT", content);
//    			}
//        	}
        	
        	searchMap.addList("fileList", getList("prs.evalCon.empEvalConReport.getFileList", searchMap));
        	
        	searchMap.addList("rpt", rpt);
        	searchMap.addList("rptDetail", rptDetail);
            
        	return searchMap;
        }
        
        return searchMap;
    }
    
    /**
     * 업무성과기술서 등록화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap empEvalConReportMetricModify(SearchMap searchMap) {
        String stMode = searchMap.getString("mode");
        
    	// 자기성과기술서 정보를 가져온다.
    	HashMap rpt = getDetail("prs.evalCon.empEvalCon.getRptMember", searchMap);
        
        searchMap.addList("rpt", rpt);
        
        return searchMap;
    }
    
    /**
     * 업무성과기술서 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap empEvalConReportProcess(SearchMap searchMap) {
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
		searchMap.fileCopy("/temp", "/empEvalCon");
		
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("ADD".equals(stMode)) {
            searchMap = insertDB(searchMap);
        } else if("MOD".equals(stMode)) {
            searchMap = updateDB(searchMap);
        } else if("DEL".equals(stMode)) {
            searchMap = deleteDB(searchMap);
        } else if("METRIC".equals(stMode)){
        	searchMap = insertMetricDB(searchMap);
        } else if("METRICDEL".equals(stMode)){
        	searchMap = deleteMetricDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 계량성과 지표 삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap deleteMetricDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();
        
        try {
        	setStartTransaction();
        	
	        String[] metricIds = searchMap.getString("metricIds").split("\\|", 0);

	        for (int i=0; i < metricIds.length; i++) {
	            searchMap.put("metricId", metricIds[i]);
	            
	            returnMap = updateData("prs.evalCon.empEvalConReport.deleteTargetData", searchMap);
	            returnMap = updateData("prs.evalCon.empEvalConReport.deleteActualData", searchMap);
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
     * 계량성과지표 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertMetricDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
            setStartTransaction();
            
            String metricId = "";
            
            metricId = getStr("prs.evalCon.empEvalConReport.getMetricId", searchMap);
            
            searchMap.put("metricId", metricId);
        
            returnMap = insertData("prs.evalCon.empEvalConReport.insertMetricData", searchMap);
            returnMap = insertData("prs.evalCon.empEvalConReport.insertScoreData", searchMap);
        
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
     * 업무성과기술서 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
            setStartTransaction();
        
            returnMap = insertData("prs.evalCon.empEvalConReport.insertData", searchMap);
        
        } catch (Exception e) {
            logger.error(e.toString());
            setRollBackTransaction();
            returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
            returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
            setEndTransaction();
        }
        

        /**********************************
         * 첨부파일 정보 등록
         **********************************/
        returnMap = insertFileInfo(searchMap);
        
        /**********************************
         * Return
         **********************************/
        searchMap.addList("returnMap", returnMap);
        return searchMap;    
    }
    
    /**
     * 업무성과기술서 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
            setStartTransaction();
            
            returnMap = updateData("prs.evalCon.empEvalConReport.updateData", searchMap);
            
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
     * 업무성과기술서 삭제 
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
//                    returnMap = updateData("bsc.evalCon.empEvalConReport.deleteData", searchMap);
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
     * 업무성과기술서 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap empEvalConReportListExcel(SearchMap searchMap) {
        String excelFileName = "업무성과기술서";
        String excelTitle = "업무성과기술서 리스트";
        
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
    	excelInfoList.add(new ExcelVO("사번", "EMPN", "left"));
    	excelInfoList.add(new ExcelVO("성명", "KOR_NM", "left"));
    	excelInfoList.add(new ExcelVO("조직코드", "DEPT_CD", "left"));
    	excelInfoList.add(new ExcelVO("인사조직명", "DEPT_KOR_NM", "left"));
    	excelInfoList.add(new ExcelVO("DEPT_FULL_NM", "DEPT_FULL_NM", "left"));
    	excelInfoList.add(new ExcelVO("직급코드", "CAST_TC", "left"));
    	excelInfoList.add(new ExcelVO("직급명", "CAST_TC_NM", "left"));
    	excelInfoList.add(new ExcelVO("직위코드", "POS_TC", "left"));
    	excelInfoList.add(new ExcelVO("직위명", "POS_TC_NM", "left"));
    	excelInfoList.add(new ExcelVO("별도평가 확정여부", "EVAL_YN", "left"));
    	excelInfoList.add(new ExcelVO("입사일", "START_PCMT_DATE", "left"));
    	excelInfoList.add(new ExcelVO("근무종료일", "END_PCMT_DATE", "left"));
    	excelInfoList.add(new ExcelVO("부서장", "MANAGER_USER_ID", "left"));
    	excelInfoList.add(new ExcelVO("MANAGER_USER_NM", "MANAGER_USER_NM", "left"));
    	
        
        searchMap.put("excelFileName", excelFileName);
        searchMap.put("excelTitle", excelTitle);
        searchMap.put("excelSearchInfoList", excelSearchInfoList);
        searchMap.put("excelInfoList", excelInfoList);
        searchMap.put("excelDataList", (ArrayList)getList("prs.evalCon.empEvalConReport.getList", searchMap));
        
        return searchMap;
    }
    
    /**
     * 업무성과기술서 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap empEvalConReportExampleListExcel(SearchMap searchMap) {
    	
    	ArrayList<HashMap> excelList = new ArrayList<HashMap>();
    	
    	String excelFileName = "업무성과기술서";
    	searchMap.put("excelFileName", excelFileName);
    	
    	HashMap excelMap = null;
    	ArrayList<ExcelVO> excelSearchInfoList = null;
    	ArrayList<ExcelVO> excelInfoList = null;
    	
    	//멀티시트 시작
    	excelMap = new HashMap();
        
        /************************************************************************************
         * 조회조건 설정
         ************************************************************************************/
        excelSearchInfoList = new ArrayList<ExcelVO>();
        
        /****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
        excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("사번", "EMPN", "left"));
    	excelInfoList.add(new ExcelVO("성명", "KOR_NM", "left"));
    	excelInfoList.add(new ExcelVO("부서코드", "DEPT_CD", "left"));
    	excelInfoList.add(new ExcelVO("인사조직명", "DEPT_FULL_NM", "left"));
    	excelInfoList.add(new ExcelVO("지표특성\n (10.상향식, 20.하향식)", "METRIC_GUBUN", "left"));
    	excelInfoList.add(new ExcelVO("지표명", "METRIC_NM", "left"));
    	excelInfoList.add(new ExcelVO("지표구분\n (01.개인고유지표, 02.부서전략지표)", "DIRECTION_CD", "left"));
    	excelInfoList.add(new ExcelVO("단위", "UNIT", "left"));
    	excelInfoList.add(new ExcelVO("목표", "TARGET_VALUE", "left"));
    	excelInfoList.add(new ExcelVO("실적", "VALUE", "left"));
    	excelInfoList.add(new ExcelVO("달성도", "SCORE", "left"));
    	excelInfoList.add(new ExcelVO("정렬순서", "SORT_ORDER", "left"));
    	
        
        
    	excelMap.put("excelTitle", "계량적성과");
        excelMap.put("excelInfoList", excelInfoList);
        excelMap.put("excelDataList", (ArrayList)getList("prs.evalCon.empEvalConReport.getListExcel", searchMap));
        
        excelList.add(excelMap);
        //멀티시트 끝
        
        //멀티시트 시작
    	excelMap = new HashMap();
        
        /************************************************************************************
         * 조회조건 설정
         ************************************************************************************/
        excelSearchInfoList = new ArrayList<ExcelVO>();
        
        /****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
        excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("사번", "EMPN", "left"));
    	excelInfoList.add(new ExcelVO("성명", "KOR_NM", "left"));
    	excelInfoList.add(new ExcelVO("부서코드", "DEPT_CD", "left"));
    	excelInfoList.add(new ExcelVO("인사조직명", "DEPT_KOR_NM", "left"));
    	excelInfoList.add(new ExcelVO("개인 고유지표 달성노력", "METRIC_GUBUN", "left"));
    	excelInfoList.add(new ExcelVO(" 부서 전략지표 달성노력", "METRIC_GUBUN", "left"));
    	excelInfoList.add(new ExcelVO("부서 협업 수행노력", "METRIC_GUBUN", "left"));
    	
        
        
    	excelMap.put("excelTitle", "비계량적성과");
        excelMap.put("excelInfoList", excelInfoList);
        excelMap.put("excelDataList", (ArrayList)getList("prs.evalCon.empEvalConReport.getList", searchMap));
        
        excelList.add(excelMap);
        //멀티시트 끝
        
        searchMap.put("excelList", excelList);
        
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
    	
        return searchMap;
    }
    
    /**
     * 엑셀로딩관리 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excelUploadProcess(SearchMap searchMap) {
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
            searchMap = empEvalConReportUpExcel(searchMap);
        }  
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    public SearchMap empEvalConReportUpExcel(SearchMap searchMap) {

    	HashMap returnMap = new HashMap();
        ArrayList excelDataList = new ArrayList();
        ArrayList excelDataList1 = new ArrayList();
        int inputValCnt = 0;
        try {
        	setStartTransaction();

        	int evalMembEmpnCol 			= 0;
        	int deptCdCol        			= 2;
        	int metricGubunCol    			= 4;
        	int metricNmCol 				= 5;
        	int directionCdCol 				= 6;
        	int unitCol 					= 7;
        	int targetValueCol 				= 8;
        	int valueCol 					= 9;
        	int scoreCol 					= 10;
        	int sortOrderCol 				= 11;
        	
        	int evalMembEmpnCol1 			= 0;
        	int deptCdCol1        			= 2;
        	int contentCol1                 = 4;
        	int contentCol2                 = 5;
        	int contentCol3                 = 6;
        	
        	

        	searchMap.put("evalMembEmpnCol", evalMembEmpnCol);
        	searchMap.put("deptCdCol", deptCdCol);
        	searchMap.put("metricGubunCol", metricGubunCol);
        	searchMap.put("metricNmCol", metricNmCol);
        	searchMap.put("directionCdCol", directionCdCol);
        	searchMap.put("unitCol", unitCol);
        	searchMap.put("targetValueCol", targetValueCol);
        	searchMap.put("valueCol", valueCol);
        	searchMap.put("scoreCol", scoreCol);
        	searchMap.put("sortOrderCol", sortOrderCol);
        	
        	searchMap.put("evalMembEmpnCol1", evalMembEmpnCol1);
        	searchMap.put("deptCdCol1", deptCdCol1);
        	searchMap.put("contentCol1", contentCol1);
        	searchMap.put("contentCol2", contentCol2);
        	searchMap.put("contentCol3", contentCol3);

        	excelDataList = execlUpload(searchMap, returnMap);
        	excelDataList1 = execlUpload1(searchMap, returnMap);
        	
        	if(null != excelDataList && 0 < excelDataList.size()) {
        		String tmpVal = "";

            	String upYear = "";
            	String upEvalKindId = "";
            	
            	String[] strEvalMembEmpn 		= (String[]) excelDataList.get(0);
            	String[] strDeptCd       		= (String[]) excelDataList.get(1);
        		String[] strMetricGubun 		= (String[]) excelDataList.get(2);
                String[] strMetricNm 			= (String[]) excelDataList.get(3);
                String[] strDirectionCd 		= (String[]) excelDataList.get(4);
                String[] strUnit 				= (String[]) excelDataList.get(5);
                String[] strTargetValue 		= (String[]) excelDataList.get(6);
                String[] strValue 				= (String[]) excelDataList.get(7);
                String[] strScore 				= (String[]) excelDataList.get(8);
                String[] strSortOrder 			= (String[]) excelDataList.get(9);
                String metricId                 = "";
                int targetCount					= 0;
                int actualCount					= 0;
                
                for(int i=0; i< strEvalMembEmpn.length ; i++) {
                	searchMap.put("strEvalMembEmpn", strEvalMembEmpn[i]);
                	targetCount = getInt("prs.evalCon.empEvalConReport.getTargetCount", searchMap);
                	
                	if(targetCount > 0){
                		returnMap = updateData("prs.evalCon.empEvalConReport.deleteReportTargetData", searchMap);
                	}
                	
                	actualCount = getInt("prs.evalCon.empEvalConReport.getActualCount", searchMap);
                	
                	if(actualCount > 0){
                		returnMap = updateData("prs.evalCon.empEvalConReport.deleteReportActualData", searchMap);
                	}
                }
                
                for(int i=0; i< strEvalMembEmpn.length ; i++) {

                	if( 0 <= i ){ // 데이터 등록부분
                		
	                	if(!"".equals(StaticUtil.nullToBlank(strEvalMembEmpn[i]))) {
	                		searchMap.put("evalMembEmpn", strEvalMembEmpn[i]);
	                		searchMap.put("deptCd", strDeptCd[i]);
		                	searchMap.put("metricGubun", strMetricGubun[i].substring(0, 2));
		                	searchMap.put("metricNm", strMetricNm[i]);
		                	searchMap.put("directionCd", strDirectionCd[i].substring(0, 2));
		                	searchMap.put("unit", strUnit[i]);
		                	searchMap.put("targetValue", strTargetValue[i]);
		                	searchMap.put("sortOrder", strSortOrder[i]);
		                	
		                	metricId = getStr("prs.evalCon.empEvalConReport.getMetricId", searchMap);
		                	searchMap.put("metricId", metricId);
		                	
		                	returnMap = insertData("prs.evalCon.empEvalConReport.insertEmpEvalConReportTarget", searchMap);
		                	inputValCnt++;
	                	}
	                	
	                	if(!"".equals(StaticUtil.nullToBlank(strEvalMembEmpn[i]))) {
		                	searchMap.put("evalMembEmpn", strEvalMembEmpn[i]);
		                	searchMap.put("metricId", metricId);
		                	searchMap.put("value", strValue[i]);
		                	searchMap.put("score", strScore[i]);

		                	returnMap = insertData("prs.evalCon.empEvalConReport.insertEmpEvalConReportActual", searchMap);
		                	inputValCnt++;
	                	}
	                	
                	}
                }
        	}
        	
        	if(null != excelDataList1 && 0 < excelDataList1.size()){
        		
        		String[] strEvalMembEmpn1 		= (String[]) excelDataList1.get(0);
            	String[] strDeptCd1       		= (String[]) excelDataList1.get(1);
        		String[] strContent1 			= (String[]) excelDataList1.get(2);
        		String[] strContent2 			= (String[]) excelDataList1.get(3);
        		String[] strContent3 			= (String[]) excelDataList1.get(4);
        		int rptCount					= 0;
        		
        		for(int i=0; i< strEvalMembEmpn1.length ; i++) {
        			searchMap.put("strEvalMembEmpn1", strEvalMembEmpn1[i]);
        			rptCount = getInt("prs.evalCon.empEvalConReport.getReportRptCount", searchMap);
                	
        			if(rptCount > 0){
        				returnMap = updateData("prs.evalCon.empEvalConReport.deleteReportRptData", searchMap);
        			}
                }
        		
        		for(int i=0; i< strEvalMembEmpn1.length ; i++) {

                	if( 0 <= i ){ // 데이터 등록부분
                		
	                	if(!"".equals(StaticUtil.nullToBlank(strEvalMembEmpn1[i]))) {
	                		searchMap.put("evalMembEmpn", strEvalMembEmpn1[i]);
	                		searchMap.put("deptCd", strDeptCd1[i]);
		                	searchMap.put("content", strContent1[i]);
		                	searchMap.put("content2", strContent2[i]);
		                	searchMap.put("content3", strContent3[i]);
		                	
		                	returnMap = insertData("prs.evalCon.empEvalConReport.insertEmpEvalConReportUngauged", searchMap);
		                	inputValCnt++;
	                	}
                	}
                }
        	}

        	/**********************************
             * 로그등록
             **********************************/
            searchMap.put("resultYn", "Y");
            searchMap.put("inputValue", inputValCnt);

            /**********************************
             * 등록실패시작업
             **********************************/
            Integer chkVal = (Integer)searchMap.get("chkVal");
    		if(null != chkVal) {
    			if(chkVal.intValue() < 0){
    				searchMap.put("resultYn", "N");
    	            searchMap.put("inputValue", 0);
    	            returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
    				returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
    				System.out.println("ErrorNumber : " + ErrorMessages.FAILURE_PROCESS_CODE);
    				System.out.println("ErrorMessage : " + ErrorMessages.FAILURE_PROCESS_MESSAGE);
    				setRollBackTransaction();
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
    
    /*************************************************
	* excel 실적업로드
	**************************************************/
	public ArrayList execlUpload(SearchMap searchMap, HashMap returnMap) throws Exception {
		ArrayList list = new ArrayList();

		FileConfig fileConfig = FileConfig.getInstance();

		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");

		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String extension = ""; //파일확장자

		if(fileInfoVO != null) {
			extension = fileInfoVO.getFileExtension();
			if("xlsx".equals(extension)) { //엑셀2007
				list = execlParsing2(searchMap, returnMap);
			} else { //엑셀2003
				list = execlParsing(searchMap, returnMap);
			}
		}

		return list;
	}
	
	/*************************************************
	* excel 실적업로드
	**************************************************/
	public ArrayList execlUpload1(SearchMap searchMap, HashMap returnMap) throws Exception {
		ArrayList list = new ArrayList();

		FileConfig fileConfig = FileConfig.getInstance();

		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");

		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String extension = ""; //파일확장자

		if(fileInfoVO != null) {
			extension = fileInfoVO.getFileExtension();
			if("xlsx".equals(extension)) { //엑셀2007
				list = execlParsingSheet2(searchMap, returnMap);
			} else { //엑셀2003
				list = execlParsingSheet(searchMap, returnMap);
			}
		}

		return list;
	}
	
	/*************************************************
	* Excel Parsing (Excel ver 2007, Sheet2)
	**************************************************/
	public ArrayList execlParsingSheet2(SearchMap searchMap, HashMap returnMap) {
		FileConfig fileConfig = FileConfig.getInstance();
		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");
		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String pathNFileName = stRealRootPath + fileInfoVO.getFileUploadPath();

//		int fixGradeCol = searchMap.getInt("evalTargetCol");
//		int evalUserCol = searchMap.getInt("evalUserCol");

		int evalMembEmpnCol1 	= searchMap.getInt("evalMembEmpnCol1");
		int deptCdCol1		 	= searchMap.getInt("deptCdCol1");
		int contentCol1	 		= searchMap.getInt("contentCol1");
		int contentCol2	 		= searchMap.getInt("contentCol2");
		int contentCol3	 		= searchMap.getInt("contentCol3");
    	
		int rowCnt1 = 0;

	    boolean isDataExist = false; //데이터존재확인변수

	    ArrayList list = new ArrayList();

	    System.out.println("원본파일===>" + pathNFileName);

		try {
	       	//업로드된 엑셀파일 읽는다
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(new File(pathNFileName)));
	       	XSSFSheet sheet1 = wb.getSheetAt(1);

	       	rowCnt1 = sheet1.getLastRowNum() + 1; //실제row수

	       	System.out.println("getLastRowNum1: "+sheet1.getLastRowNum());
	       	System.out.println("iCnt1: "+rowCnt1);

	       	if(rowCnt1 > 1) {
	       		isDataExist = true;
	       	}

	       	String[] strMembEmpnList1 		= new String[rowCnt1-1];
	       	String[] strDeptCdList1	 		= new String[rowCnt1-1];
	       	String[] strContentList1		= new String[rowCnt1-1];
	       	String[] strContentList2		= new String[rowCnt1-1];
	       	String[] strContentList3		= new String[rowCnt1-1];
            
	       	for(int i = 1; i < rowCnt1; i++) { //행
	       		XSSFRow currRow = sheet1.getRow(i);
	       		
	       		XSSFCell currEvalMembEmpnCol1 = currRow.getCell((short)evalMembEmpnCol1);
	       		
       			if( null != currEvalMembEmpnCol1 ){
       				
	       			if( currEvalMembEmpnCol1.getCellType() == XSSFCell.CELL_TYPE_NUMERIC ){
	       				strMembEmpnList1[i-1] = String.valueOf(currEvalMembEmpnCol1.getNumericCellValue());
	       			}else if( currEvalMembEmpnCol1.getCellType() == XSSFCell.CELL_TYPE_STRING ){
	       				strMembEmpnList1[i-1] = String.valueOf(currEvalMembEmpnCol1.getStringCellValue());
	       			}
	       			
	       			XSSFCell currDeptCdCol1= currRow.getCell((short)deptCdCol1);
	       			
	       			if( currDeptCdCol1.getCellType() == XSSFCell.CELL_TYPE_NUMERIC ){
	       				strDeptCdList1[i-1] = String.valueOf(currDeptCdCol1.getNumericCellValue());
	       			}else if( currDeptCdCol1.getCellType() == XSSFCell.CELL_TYPE_STRING ){
	       				strDeptCdList1[i-1] = String.valueOf(currDeptCdCol1.getStringCellValue());
	       			}
	       			
	       			XSSFCell currContentCol1= currRow.getCell((short)contentCol1);
	       			
	       			if( currContentCol1.getCellType() == XSSFCell.CELL_TYPE_NUMERIC ){
	       				strContentList1[i-1] = String.valueOf(currContentCol1.getNumericCellValue());
	       			}else if( currContentCol1.getCellType() == XSSFCell.CELL_TYPE_STRING ){
	       				strContentList1[i-1] = String.valueOf(currContentCol1.getStringCellValue());
	       			}
	       			
	       			XSSFCell currContentCol2= currRow.getCell((short)contentCol2);
	       			
	       			if( currContentCol2.getCellType() == XSSFCell.CELL_TYPE_NUMERIC ){
	       				strContentList2[i-1] = String.valueOf(currContentCol2.getNumericCellValue());
	       			}else if( currContentCol2.getCellType() == XSSFCell.CELL_TYPE_STRING ){
	       				strContentList2[i-1] = String.valueOf(currContentCol2.getStringCellValue());
	       			}
	       			
	       			XSSFCell currContentCol3= currRow.getCell((short)contentCol3);
	       			
	       			if( currContentCol3.getCellType() == XSSFCell.CELL_TYPE_NUMERIC ){
	       				strContentList3[i-1] = String.valueOf(currContentCol3.getNumericCellValue());
	       			}else if( currContentCol3.getCellType() == XSSFCell.CELL_TYPE_STRING ){
	       				strContentList3[i-1] = String.valueOf(currContentCol3.getStringCellValue());
	       			}
       			}
	       	}
	       	
	       	if(rowCnt1 > 1) {
	       		isDataExist = true;
	       	}
	       	
	       	list.add(strMembEmpnList1);
	       	list.add(strDeptCdList1);
	       	list.add(strContentList1);
	       	list.add(strContentList2);
	       	list.add(strContentList3);
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.toString());
			returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
		}

		return list;
	}
	
	/*************************************************
	* Excel Parsing (Excel ver 2007, Sheet2)
	**************************************************/
	public ArrayList execlParsingSheet(SearchMap searchMap, HashMap returnMap) {
		FileConfig fileConfig = FileConfig.getInstance();
		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");
		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String pathNFileName = stRealRootPath + fileInfoVO.getFileUploadPath();

//		int fixGradeCol = searchMap.getInt("evalTargetCol");
//		int evalUserCol = searchMap.getInt("evalUserCol");

		int evalMembEmpnCol1 	= searchMap.getInt("evalMembEmpnCol1");
		int deptCdCol1		 	= searchMap.getInt("deptCdCol1");
		int contentCol1	 		= searchMap.getInt("contentCol1");
		int contentCol2	 		= searchMap.getInt("contentCol2");
		int contentCol3	 		= searchMap.getInt("contentCol3");
    	
		int rowCnt1 = 0;

	    boolean isDataExist = false; //데이터존재확인변수

	    ArrayList list = new ArrayList();

	    System.out.println("원본파일===>" + pathNFileName);

		try {
	       	//업로드된 엑셀파일 읽는다
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(new File(pathNFileName)));
			HSSFSheet sheet1 = wb.getSheetAt(1);

	       	rowCnt1 = sheet1.getLastRowNum() + 1; //실제row수

	       	System.out.println("getLastRowNum1: "+sheet1.getLastRowNum());
	       	System.out.println("iCnt1: "+rowCnt1);

	       	if(rowCnt1 > 1) {
	       		isDataExist = true;
	       	}

	       	String[] strMembEmpnList1 		= new String[rowCnt1-1];
	       	String[] strDeptCdList1	 		= new String[rowCnt1-1];
	       	String[] strContentList1		= new String[rowCnt1-1];
	       	String[] strContentList2		= new String[rowCnt1-1];
	       	String[] strContentList3		= new String[rowCnt1-1];
            
	       	for(int i = 1; i < rowCnt1; i++) { //행
	       		HSSFRow currRow = sheet1.getRow(i);
	       		
	       		HSSFCell currEvalMembEmpnCol1 = currRow.getCell((short)evalMembEmpnCol1);
	       		
       			if( null != currEvalMembEmpnCol1 ){
       				
	       			if( currEvalMembEmpnCol1.getCellType() == HSSFCell.CELL_TYPE_NUMERIC ){
	       				strMembEmpnList1[i-1] = String.valueOf(currEvalMembEmpnCol1.getNumericCellValue());
	       			}else if( currEvalMembEmpnCol1.getCellType() == HSSFCell.CELL_TYPE_STRING ){
	       				strMembEmpnList1[i-1] = String.valueOf(currEvalMembEmpnCol1.getStringCellValue());
	       			}
	       			
	       			HSSFCell currDeptCdCol1= currRow.getCell((short)deptCdCol1);
	       			
	       			if( currDeptCdCol1.getCellType() == HSSFCell.CELL_TYPE_NUMERIC ){
	       				strDeptCdList1[i-1] = String.valueOf(currDeptCdCol1.getNumericCellValue());
	       			}else if( currDeptCdCol1.getCellType() == HSSFCell.CELL_TYPE_STRING ){
	       				strDeptCdList1[i-1] = String.valueOf(currDeptCdCol1.getStringCellValue());
	       			}
	       			
	       			HSSFCell currContentCol1= currRow.getCell((short)contentCol1);
	       			
	       			if( currContentCol1.getCellType() == HSSFCell.CELL_TYPE_NUMERIC ){
	       				strContentList1[i-1] = String.valueOf(currContentCol1.getNumericCellValue());
	       			}else if( currContentCol1.getCellType() == HSSFCell.CELL_TYPE_STRING ){
	       				strContentList1[i-1] = String.valueOf(currContentCol1.getStringCellValue());
	       			}
	       			
	       			HSSFCell currContentCol2= currRow.getCell((short)contentCol2);
	       			
	       			if( currContentCol2.getCellType() == HSSFCell.CELL_TYPE_NUMERIC ){
	       				strContentList2[i-1] = String.valueOf(currContentCol2.getNumericCellValue());
	       			}else if( currContentCol2.getCellType() == HSSFCell.CELL_TYPE_STRING ){
	       				strContentList2[i-1] = String.valueOf(currContentCol2.getStringCellValue());
	       			}
	       			
	       			HSSFCell currContentCol3= currRow.getCell((short)contentCol3);
	       			
	       			if( currContentCol3.getCellType() == HSSFCell.CELL_TYPE_NUMERIC ){
	       				strContentList3[i-1] = String.valueOf(currContentCol3.getNumericCellValue());
	       			}else if( currContentCol3.getCellType() == HSSFCell.CELL_TYPE_STRING ){
	       				strContentList3[i-1] = String.valueOf(currContentCol3.getStringCellValue());
	       			}
       			}
	       	}
	       	
	       	if(rowCnt1 > 1) {
	       		isDataExist = true;
	       	}
	       	
	       	list.add(strMembEmpnList1);
	       	list.add(strDeptCdList1);
	       	list.add(strContentList1);
	       	list.add(strContentList2);
	       	list.add(strContentList3);
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.toString());
			returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
		}

		return list;
	}
	
	/*************************************************
	* Excel Parsing (Excel ver 2007)
	**************************************************/
	public ArrayList execlParsing2(SearchMap searchMap, HashMap returnMap) {
		FileConfig fileConfig = FileConfig.getInstance();
		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");
		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String pathNFileName = stRealRootPath + fileInfoVO.getFileUploadPath();

//		int fixGradeCol = searchMap.getInt("evalTargetCol");
//		int evalUserCol = searchMap.getInt("evalUserCol");

		int evalMembEmpnCol 	= searchMap.getInt("evalMembEmpnCol");
		int deptCdCol		 	= searchMap.getInt("deptCdCol");
		int metricGubunCol	 	= searchMap.getInt("metricGubunCol");
		int metricNmCol 		= searchMap.getInt("metricNmCol");
    	int directionCdCol 		= searchMap.getInt("directionCdCol");
    	int unitCol 			= searchMap.getInt("unitCol");
    	int targetValueCol 		= searchMap.getInt("targetValueCol");
    	int valueCol 			= searchMap.getInt("valueCol");
    	int scoreCol 			= searchMap.getInt("scoreCol");
    	int sortOrderCol 		= searchMap.getInt("sortOrderCol");
    	
		int rowCnt = 0;
		int rowCnt1 = 0;

	    boolean isDataExist = false; //데이터존재확인변수

	    ArrayList list = new ArrayList();

	    System.out.println("원본파일===>" + pathNFileName);

		try {
	       	//업로드된 엑셀파일 읽는다
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(new File(pathNFileName)));
	       	XSSFSheet sheet = wb.getSheetAt(0);
	       	XSSFSheet sheet1 = wb.getSheetAt(1);

	       	rowCnt = sheet.getLastRowNum() + 1; //실제row수
	       	rowCnt1 = sheet1.getLastRowNum() + 1; //실제row수

	       	System.out.println("getLastRowNum: "+sheet.getLastRowNum());
	       	System.out.println("iCnt: "+rowCnt);
	       	System.out.println("getLastRowNum1: "+sheet1.getLastRowNum());
	       	System.out.println("iCnt1: "+rowCnt1);

	       	if(rowCnt > 1) {
	       		isDataExist = true;
	       	}

	       	String[] strMembEmpnList 		= new String[rowCnt-1];
	       	String[] strDeptCdList	 		= new String[rowCnt-1];
	       	String[] strMetricGubunList 	= new String[rowCnt-1];
            String[] strMetricNmList 		= new String[rowCnt-1];
            String[] strDirectionCdList 	= new String[rowCnt-1];
            String[] strUnitList 			= new String[rowCnt-1];
            String[] strTargetValueList 	= new String[rowCnt-1];
            String[] strValueList 			= new String[rowCnt-1];
            String[] strScoreList 			= new String[rowCnt-1];
            String[] strSortOrderList 		= new String[rowCnt-1];
            
	       	for(int i = 1; i < rowCnt; i++) { //행
	       		XSSFRow currRow = sheet.getRow(i);
	       		
	       		XSSFCell currEvalMembEmpnCol = currRow.getCell((short)evalMembEmpnCol);
	       		
       			if( null != currEvalMembEmpnCol ){
       				
	       			if( currEvalMembEmpnCol.getCellType() == XSSFCell.CELL_TYPE_NUMERIC ){
	       				strMembEmpnList[i-1] = String.valueOf(currEvalMembEmpnCol.getNumericCellValue());
	       			}else if( currEvalMembEmpnCol.getCellType() == XSSFCell.CELL_TYPE_STRING ){
	       				strMembEmpnList[i-1] = String.valueOf(currEvalMembEmpnCol.getStringCellValue());
	       			}
	       			
	       			XSSFCell currDeptCdCol= currRow.getCell((short)deptCdCol);
	       			if( currDeptCdCol.getCellType() == XSSFCell.CELL_TYPE_NUMERIC ){
	       				strDeptCdList[i-1] = String.valueOf(currDeptCdCol.getNumericCellValue());
	       			}else if( currDeptCdCol.getCellType() == XSSFCell.CELL_TYPE_STRING ){
	       				strDeptCdList[i-1] = String.valueOf(currDeptCdCol.getStringCellValue());
	       			}
	       			
	       			XSSFCell currMetricGubunCol= currRow.getCell((short)metricGubunCol);
	       			if( currMetricGubunCol.getCellType() == XSSFCell.CELL_TYPE_NUMERIC ){
	       				strMetricGubunList[i-1] = String.valueOf(currMetricGubunCol.getNumericCellValue());
	       			}else if( currMetricGubunCol.getCellType() == XSSFCell.CELL_TYPE_STRING ){
	       				strMetricGubunList[i-1] = String.valueOf(currMetricGubunCol.getStringCellValue());
	       			}
	       			
	       			XSSFCell currMetricNmCol = currRow.getCell((short)metricNmCol);
	       			if( currMetricNmCol.getCellType() == XSSFCell.CELL_TYPE_NUMERIC ){
	       				strMetricNmList[i-1] = String.valueOf(currMetricNmCol.getNumericCellValue());
	       			}else if( currMetricNmCol.getCellType() == XSSFCell.CELL_TYPE_STRING ){
	       				strMetricNmList[i-1] = String.valueOf(currMetricNmCol.getStringCellValue());
	       			}
	       			
	       			XSSFCell currDirectionCdCol = currRow.getCell((short)directionCdCol);
	       			if( currDirectionCdCol.getCellType() == XSSFCell.CELL_TYPE_NUMERIC ){
	       				strDirectionCdList[i-1] = String.valueOf(currDirectionCdCol.getNumericCellValue());
	       			}else if( currDirectionCdCol.getCellType() == XSSFCell.CELL_TYPE_STRING ){
	       				strDirectionCdList[i-1] = String.valueOf(currDirectionCdCol.getStringCellValue());
	       			}
	       			
	       			XSSFCell currUnitCol = currRow.getCell((short)unitCol);
	       			if( currUnitCol.getCellType() == XSSFCell.CELL_TYPE_NUMERIC ){
	       				strUnitList[i-1] = String.valueOf(currUnitCol.getNumericCellValue());
	       			}else if( currUnitCol.getCellType() == XSSFCell.CELL_TYPE_STRING ){
	       				strUnitList[i-1] = String.valueOf(currUnitCol.getStringCellValue());
	       			}
	       			
	       			XSSFCell currTargetValueCol = currRow.getCell((short)targetValueCol);
	       			if( currTargetValueCol.getCellType() == XSSFCell.CELL_TYPE_NUMERIC ){
	       				strTargetValueList[i-1] = String.valueOf(currTargetValueCol.getNumericCellValue());
	       			}else if( currTargetValueCol.getCellType() == XSSFCell.CELL_TYPE_STRING ){
	       				strTargetValueList[i-1] = String.valueOf(currTargetValueCol.getStringCellValue());
	       			}
	       			
	       			XSSFCell currValueCol = currRow.getCell((short)valueCol);
	       			if( currValueCol.getCellType() == XSSFCell.CELL_TYPE_NUMERIC ){
	       				strValueList[i-1] = String.valueOf(currValueCol.getNumericCellValue());
	       			}else if( currValueCol.getCellType() == XSSFCell.CELL_TYPE_STRING ){
	       				strValueList[i-1] = String.valueOf(currValueCol.getStringCellValue());
	       			}
	       			
	       			XSSFCell currScoreCol = currRow.getCell((short)scoreCol);
	       			if( currScoreCol.getCellType() == XSSFCell.CELL_TYPE_NUMERIC ){
	       				strScoreList[i-1] = String.valueOf(currScoreCol.getNumericCellValue());
	       			}else if( currScoreCol.getCellType() == XSSFCell.CELL_TYPE_STRING ){
	       				strScoreList[i-1] = String.valueOf(currScoreCol.getStringCellValue());
	       			}
	       			
	       			XSSFCell currSortOrderCol = currRow.getCell((short)sortOrderCol);
	       			if( currSortOrderCol.getCellType() == XSSFCell.CELL_TYPE_NUMERIC ){
	       				strSortOrderList[i-1] = String.valueOf(currSortOrderCol.getNumericCellValue());
	       			}else if( currSortOrderCol.getCellType() == XSSFCell.CELL_TYPE_STRING ){
	       				strSortOrderList[i-1] = String.valueOf(currSortOrderCol.getStringCellValue());
	       			}
	       			
       			}
	       	}
	       	
	       	if(rowCnt1 > 1) {
	       		isDataExist = true;
	       	}
	       	
	       	list.add(strMembEmpnList);
	       	list.add(strDeptCdList);
	       	list.add(strMetricGubunList);
	       	list.add(strMetricNmList);
	       	list.add(strDirectionCdList);
	       	list.add(strUnitList);
	       	list.add(strTargetValueList);
	       	list.add(strValueList);
	       	list.add(strScoreList);
	       	list.add(strSortOrderList);
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.toString());
			returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
		}

		return list;
	}

	/*************************************************
	* Excel Parsing (Excel ver 2003)
	**************************************************/
	public ArrayList execlParsing(SearchMap searchMap, HashMap returnMap) {
		FileConfig fileConfig = FileConfig.getInstance();
		String stRealRootPath = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");
		FileInfoVO fileInfoVO = (FileInfoVO)searchMap.get("file1");
		String pathNFileName = stRealRootPath + fileInfoVO.getFileUploadPath();

		int evalMembEmpnCol 	= searchMap.getInt("evalMembEmpnCol");
		int deptCdCol		 	= searchMap.getInt("deptCdCol");
		int metricGubunCol	 	= searchMap.getInt("metricGubunCol");
		int metricNmCol 		= searchMap.getInt("metricNmCol");
    	int directionCdCol 		= searchMap.getInt("directionCdCol");
    	int unitCol 			= searchMap.getInt("unitCol");
    	int targetValueCol 		= searchMap.getInt("targetValueCol");
    	int valueCol 			= searchMap.getInt("valueCol");
    	int scoreCol 			= searchMap.getInt("scoreCol");
    	int sortOrderCol 		= searchMap.getInt("sortOrderCol");

	    int rowCnt = 0;

	    boolean isDataExist = false; //데이터 존재확인 변수

	    ArrayList list = new ArrayList();

	    System.out.println("원본파일===>" + pathNFileName);

		try {
	       	//업로드된 엑셀 파일 읽는다
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(new File(pathNFileName)));
	       	HSSFSheet sheet = wb.getSheetAt(0);

	       	rowCnt = sheet.getLastRowNum() + 1; //실제row수

	       	System.out.println("getLastRowNum: "+sheet.getLastRowNum());
	       	System.out.println("iCnt: "+rowCnt);

	       	if(rowCnt > 1) {
	       		isDataExist = true;
	       	}

	       	String[] strMembEmpnList 		= new String[rowCnt-1];
	       	String[] strDeptCdList	 		= new String[rowCnt-1];
	       	String[] strMetricGubunList 	= new String[rowCnt-1];
            String[] strMetricNmList 		= new String[rowCnt-1];
            String[] strDirectionCdList 	= new String[rowCnt-1];
            String[] strUnitList 			= new String[rowCnt-1];
            String[] strTargetValueList 	= new String[rowCnt-1];
            String[] strValueList 			= new String[rowCnt-1];
            String[] strScoreList 			= new String[rowCnt-1];
            String[] strSortOrderList 		= new String[rowCnt-1];


	       	for(int i = 1; i < rowCnt; i++) { //행

	       		HSSFRow currRow = sheet.getRow(i);

       			HSSFCell currEvalMembEmpnCol = currRow.getCell((short)evalMembEmpnCol);

       			if( null != currEvalMembEmpnCol ){


       				if( currEvalMembEmpnCol.getCellType() == HSSFCell.CELL_TYPE_NUMERIC ){
	       				strMembEmpnList[i-1] = String.valueOf(currEvalMembEmpnCol.getNumericCellValue());
	       			}else if( currEvalMembEmpnCol.getCellType() == HSSFCell.CELL_TYPE_STRING ){
	       				strMembEmpnList[i-1] = String.valueOf(currEvalMembEmpnCol.getStringCellValue());
	       			}
       				
       				HSSFCell currDeptCdCol= currRow.getCell((short)deptCdCol);
	       			if( currDeptCdCol.getCellType() == HSSFCell.CELL_TYPE_NUMERIC ){
	       				strDeptCdList[i-1] = String.valueOf(currDeptCdCol.getNumericCellValue());
	       			}else if( currDeptCdCol.getCellType() == HSSFCell.CELL_TYPE_STRING ){
	       				strDeptCdList[i-1] = String.valueOf(currDeptCdCol.getStringCellValue());
	       			}
       				
       				HSSFCell currMetricGubunCol= currRow.getCell((short)metricGubunCol);
	       			if( currMetricGubunCol.getCellType() == HSSFCell.CELL_TYPE_NUMERIC ){
	       				strMetricGubunList[i-1] = String.valueOf(currMetricGubunCol.getNumericCellValue());
	       			}else if( currMetricGubunCol.getCellType() == HSSFCell.CELL_TYPE_STRING ){
	       				strMetricGubunList[i-1] = String.valueOf(currMetricGubunCol.getStringCellValue());
	       			}

	       			HSSFCell currMetricNmCol = currRow.getCell((short)metricNmCol);
	       			if( currMetricNmCol.getCellType() == HSSFCell.CELL_TYPE_NUMERIC ){
	       				strMetricNmList[i-1] = String.valueOf(currMetricNmCol.getNumericCellValue());
	       			}else if( currMetricNmCol.getCellType() == HSSFCell.CELL_TYPE_STRING ){
	       				strMetricNmList[i-1] = String.valueOf(currMetricNmCol.getStringCellValue());
	       			}

	       			HSSFCell currDirectionCdCol = currRow.getCell((short)directionCdCol);
	       			if( currDirectionCdCol.getCellType() == HSSFCell.CELL_TYPE_NUMERIC ){
	       				strDirectionCdList[i-1] = String.valueOf(currDirectionCdCol.getNumericCellValue());
	       			}else if( currDirectionCdCol.getCellType() == HSSFCell.CELL_TYPE_STRING ){
	       				strDirectionCdList[i-1] = String.valueOf(currDirectionCdCol.getStringCellValue());
	       			}

	       			HSSFCell currUnitCol = currRow.getCell((short)unitCol);
	       			if( currUnitCol.getCellType() == HSSFCell.CELL_TYPE_NUMERIC ){
	       				strUnitList[i-1] = String.valueOf(currUnitCol.getNumericCellValue());
	       			}else if( currUnitCol.getCellType() == HSSFCell.CELL_TYPE_STRING ){
	       				strUnitList[i-1] = String.valueOf(currUnitCol.getStringCellValue());
	       			}

	       			HSSFCell currTargetValueCol = currRow.getCell((short)targetValueCol);
	       			if( currTargetValueCol.getCellType() == HSSFCell.CELL_TYPE_NUMERIC ){
	       				strTargetValueList[i-1] = String.valueOf(currTargetValueCol.getNumericCellValue());
	       			}else if( currTargetValueCol.getCellType() == HSSFCell.CELL_TYPE_STRING ){
	       				strTargetValueList[i-1] = String.valueOf(currTargetValueCol.getStringCellValue());
	       			}

	       			HSSFCell currValueCol = currRow.getCell((short)valueCol);
	       			if( currValueCol.getCellType() == HSSFCell.CELL_TYPE_NUMERIC ){
	       				strValueList[i-1] = String.valueOf(currValueCol.getNumericCellValue());
	       			}else if( currValueCol.getCellType() == HSSFCell.CELL_TYPE_STRING ){
	       				strValueList[i-1] = String.valueOf(currValueCol.getStringCellValue());
	       			}

	       			HSSFCell currScoreCol = currRow.getCell((short)scoreCol);
	       			if( currScoreCol.getCellType() == HSSFCell.CELL_TYPE_NUMERIC ){
	       				strScoreList[i-1] = String.valueOf(currScoreCol.getNumericCellValue());
	       			}else if( currScoreCol.getCellType() == HSSFCell.CELL_TYPE_STRING ){
	       				strScoreList[i-1] = String.valueOf(currScoreCol.getStringCellValue());
	       			}

	       			HSSFCell currSortOrderCol = currRow.getCell((short)sortOrderCol);
	       			if( currSortOrderCol.getCellType() == HSSFCell.CELL_TYPE_NUMERIC ){
	       				strSortOrderList[i-1] = String.valueOf(currSortOrderCol.getNumericCellValue());
	       			}else if( currSortOrderCol.getCellType() == HSSFCell.CELL_TYPE_STRING ){
	       				strSortOrderList[i-1] = String.valueOf(currSortOrderCol.getStringCellValue());
	       			}

       			}

	       	}

	       	list.add(strMembEmpnList);
	       	list.add(strDeptCdList);
	       	list.add(strMetricGubunList);
	       	list.add(strMetricNmList);
	       	list.add(strDirectionCdList);
	       	list.add(strUnitList);
	       	list.add(strTargetValueList);
	       	list.add(strValueList);
	       	list.add(strScoreList);
	       	list.add(strSortOrderList);
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.toString());
			searchMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			searchMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
		}

		return list;
	}
    
	/**
     * 첨부파일 정보등록
     * @param
     * @return String
     * @throws
     */
    public HashMap insertFileInfo(SearchMap searchMap) {
        HashMap returnMap = new HashMap();

        try {
        	String[] delAttachFiles = searchMap.getStringArray("delAttachFiles");

        	/**********************************
             * 삭제체크된 첨부파일 삭제
             **********************************/
        	if(null != delAttachFiles && 0 < delAttachFiles.length) {
	        	for(int i = 0; i < delAttachFiles.length; i++){
	        		searchMap.put("seq", delAttachFiles[i]);
					returnMap = insertData("prs.evalCon.empEvalConReport.deleteFileInfo", searchMap);
	        	}
        	}

        	/**********************************
             * 첨부파일 등록
             **********************************/
        	ArrayList fileInfoList = new ArrayList();
        	fileInfoList = (ArrayList)searchMap.get("FileInfoList");
	        FileInfoVO fileInfoVO = new FileInfoVO();

	        if(null != fileInfoList) {
				for(int i = 0; i < fileInfoList.size(); i++){
					fileInfoVO = (FileInfoVO)fileInfoList.get(i);
					if(fileInfoVO != null){
						searchMap.put("attachFileFnm", 	fileInfoVO.getMaskFileName());
						searchMap.put("attachFileNm", 	fileInfoVO.getRealFileName());
						searchMap.put("attachFileSuffix",fileInfoVO.getFileExtension());
						searchMap.put("attachFilePath", fileInfoVO.getFileUploadPath());
						returnMap = insertData("prs.evalCon.empEvalConReport.insertFileInfo", searchMap);
					}
				}
	        }

        } catch (Exception e) {
        	logger.error(e.toString());
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        }
        return returnMap;
    }
    
}
