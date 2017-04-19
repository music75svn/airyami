/*************************************************************************
* CLASS 명      : BonEmpAction
* 작 업 자      : 지원길
* 작 업 일      : 2013년 05월 10일
* 기    능      : 직원성과급
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일				변 경 내 용        	 비고
* ----  ---------   -----------------  	-------------------------  --------
*   1    지 원 길    2013년 06월 15일        	최 초 작 업
**************************************************************************/
package com.lexken.prs.bonus;

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

public class BonEmpAction extends CommonService {
	private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 직원성과급 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap bonEmpList(SearchMap searchMap) {

    	// 최상위 평가조직 조회
    	HashMap topScDept = (HashMap)getDetail("prs.bonus.bonEmp.getTopDeptInfo", searchMap);


    	// 파라미터가 null로 들어올 때 디폴트값 셋팅.
    	if(topScDept==null){
    		topScDept =new HashMap();
    		topScDept.put("DEPT_CD", "");
    		topScDept.put("DEPT_KOR_NM", "");
    	}

    	// 파라미터가 null로 들어올 때 디폴트값 셋팅.
    	String findDeptCd =  StaticUtil.nullToDefault((String)searchMap.getString("findDeptCd"), (String)topScDept.get("DEPT_CD"));	// 조직코드가 없으면 전사조직코드를 셋팅.
    	String findUpDeptName =  StaticUtil.nullToDefault((String)searchMap.getString("findUpDeptName"), (String)topScDept.get("DEPT_KOR_NM")) ; ;	// 조직명이 없으면 전사조직명을 셋팅.

    	// 디폴트 조회조건 설정
    	searchMap.put("findDeptCd", findDeptCd);	// 검색버튼 조회시 조직트리에 기존 선택한 조직이 표시되도록 설정.(findSearchCodeId에 넣어주면 우선 적용됨. 트리와 검색조건을 별도로 사용할 경우에 한함.)
    	searchMap.put("findUpDeptName", findUpDeptName);

    	searchMap.addList("deptTree", getList("prs.bonus.bonEmp.getDeptList", searchMap)); //미사용은 제외한 인사조직

        return searchMap;
    }

    /**
     * 직원성과급 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap bonEmpList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("prs.bonus.bonEmp.getList", searchMap));

        return searchMap;
    }

    /**
     * 직원성과급 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap bonEmpInfoList_xml(SearchMap searchMap) {

    	searchMap.addList("list", getList("prs.bonus.bonEmp.getDetail", searchMap));

    	return searchMap;
    }

    /**
     * 직원성과급 상세화면
     * @param
     * @return String
     * @throws
     */
    public SearchMap bonEmpModify(SearchMap searchMap) {

    	searchMap.addList("deptTree", getList("prs.bonus.bonEmp.getDeptList", searchMap)); //미사용, 임원은 제외한 인사조직

        return searchMap;

    }

    /**
     * 파견직설정 등록/삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap bonEmpProcess(SearchMap searchMap) {
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
        if("ADD".equals(stMode)) {
            searchMap = insertDB(searchMap);
        }else if("ADDEXCEL".equals(stMode)){
        	searchMap = insertExcelDB(searchMap);
        }

        /**********************************
         * Return
         **********************************/
        return searchMap;
    }

    /**
     * 직원급여 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
        	setStartTransaction();
        	//설정에서 파견직 격려금 유무 확인
        	int cnt = getInt("prs.bonus.bonEmp.getCount", searchMap);
        		if(0 == cnt){
        			returnMap.put("ErrorNumber", -16);
    	            returnMap.put("ErrorMessage",  "등록된 파견직 격려금이 없습니다.");
        		}

        	String[] seqs = searchMap.getStringArray("seq");
        	String[] empns = searchMap.getStringArray("empn");
        	String[] insidePays = searchMap.getStringArray("insidePay");
        	String[] govPays = searchMap.getStringArray("govPay");
        	String[] totPays = searchMap.getStringArray("totPay");

            for(int i=0; i<empns.length; i++){
            	if(null != empns[i]){
            		searchMap.put("seq", seqs[i]);
            		searchMap.put("empn", empns[i]);
            		searchMap.put("insidePay", insidePays[i]);
            		searchMap.put("govPays", govPays[i]);
            		searchMap.put("totPay", totPays[i]);

            		String empn = empns[i];

        			if(!"".equals(empn) ){
        				returnMap = deleteData("prs.bonus.bonEmp.deleteData", searchMap, true);
        			}
            		returnMap = insertData("prs.bonus.bonEmp.insertData", searchMap);
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
        	excelDataList = excel.execlBonEmpUpload(searchMap);


        	if(null != excelDataList && 0 < excelDataList.size()) {
				String[] strSeq 			= (String[]) excelDataList.get(0);
				String[] strEmpn    	    = (String[]) excelDataList.get(1);
				String[] strInsidePay    	= (String[]) excelDataList.get(2);
				String[] strGovPay		    = (String[]) excelDataList.get(3);
				String[] strTotPay          = (String[]) excelDataList.get(4);

                /**********************************
                 * 기존 등록된 실적 삭제
                 **********************************/

                returnMap = insertData("prs.bonus.bonEmp.deleteData", searchMap);

                for(int i=0; i<strEmpn.length; i++){
                	if(null != strEmpn[i]){
                		//String strStartPcmtDates = strStartPcmtDate[i];
                		//strStartPcmtDates = strStartPcmtDates.replace("/", "");

                		searchMap.put("seq", strSeq[i]);
                		searchMap.put("empn", strEmpn[i]);
                		searchMap.put("insidePay", strInsidePay[i]);
                		searchMap.put("govPay", strGovPay[i]);
                		searchMap.put("totPay", strTotPay[i]);

                		returnMap = insertData("prs.bonus.bonEmp.insertData", searchMap);
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

    /**
     * 엑셀업로드 팝업
     * @param
     * @return String
     * @throws
     */
    public SearchMap popBonEmpExcelUpload(SearchMap searchMap) {

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
     * 직원성과급 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap bonEmpExcel(SearchMap searchMap) {
        String excelFileName = "직원성과급";
        String excelTitle = "직원성과급";

        /************************************************************************************
         * 조회조건 설정
         ************************************************************************************/
        ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();

        excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));

        /****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
        ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
        excelInfoList.add(new ExcelVO("일련번호","SEQ", "center"));
        excelInfoList.add(new ExcelVO("사원번호","EMPN", "center"));
        excelInfoList.add(new ExcelVO("성명", "KOR_NM", "center"));
        excelInfoList.add(new ExcelVO("부서코드", "DEPT_CD", "center"));
        excelInfoList.add(new ExcelVO("소속부서명", "DEPT_KOR_NM", "left"));
        excelInfoList.add(new ExcelVO("직급명", "CAST_TC_NM", "center"));
        excelInfoList.add(new ExcelVO("직위명", "POS_TC_NM", "center"));
        excelInfoList.add(new ExcelVO("근무기간", "WORK_MON", "center"));
        excelInfoList.add(new ExcelVO("기본급", "PAY", "center"));
        excelInfoList.add(new ExcelVO("자체지급률", "INSIDE_REAL_RATE", "center"));
        excelInfoList.add(new ExcelVO("정평지급률", "GOV_EMP_REAL_RATE", "center"));
        excelInfoList.add(new ExcelVO("차등지급률", "ORG_GRA_RATE", "center"));
        excelInfoList.add(new ExcelVO("자체성과급", "INSIDE_PAY", "center"));
        excelInfoList.add(new ExcelVO("정평성과급", "GOV_PAY", "center"));
        excelInfoList.add(new ExcelVO("총지급액", "TOT_PAY", "center"));


        searchMap.put("excelFileName", excelFileName);
        searchMap.put("excelTitle", excelTitle);
        searchMap.put("excelSearchInfoList", excelSearchInfoList);
        searchMap.put("excelInfoList", excelInfoList);

        searchMap.put("excelDataList", (ArrayList)getList("prs.bonus.bonEmp.getBonEmpExcelList", searchMap));

        return searchMap;

    }

}
