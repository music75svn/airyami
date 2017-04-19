/*************************************************************************
* CLASS 명		: BonDirAction
* 작 업 자      : 지원길
* 작 업 일      : 2013년 06월 11일
* 기    능      : 임원성과연봉 설정
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    지원길      2013년 06월 11일         최 초 작 업
**************************************************************************/
package com.lexken.prs.bonus;

import java.util.ArrayList;
import java.util.HashMap;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelUpload;
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

public class BonDirAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 임원성과연봉 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap bonDirList(SearchMap searchMap) {

    	return searchMap;
    }

    /**
     * 임원성과연봉 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap bonDirList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("prs.bonus.bonDir.getList", searchMap));

        return searchMap;
    }

    /**
     * 엑셀업로드 팝업
     * @param
     * @return String
     * @throws
     */
    public SearchMap popBonDirExcelUpload(SearchMap searchMap) {

        return searchMap;
    }

    /**
     * 임원연봉설정 등록/삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap bonDirProcess(SearchMap searchMap) {
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
     * 임원연봉설정 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
        	setStartTransaction();
        	//설정에서 임원성과연봉 유무 확인
        	int cnt = getInt("prs.bonus.bonDirSet.getCount", searchMap);
        		if(0 == cnt){
        			returnMap.put("ErrorNumber", -16);
    	            returnMap.put("ErrorMessage",  "등록된 임원성과연봉이 없습니다.");
        		}

        	String[] empns = searchMap.getStringArray("empns");
        	String[] deptCds = searchMap.getStringArray("deptCds");
        	String[] castTcs = searchMap.getStringArray("castTcs");
        	String[] posTcs = searchMap.getStringArray("posTcs");
        	String[] salarys = searchMap.getStringArray("salarys");
        	String[] rates = searchMap.getStringArray("rates");
        	String[] workMons = searchMap.getStringArray("workMons");
        	String[] totBonSals = searchMap.getStringArray("totBonSals");

            for(int i=0; i<empns.length; i++){
            	if(null != empns[i]){
            		searchMap.put("empn", empns[i]);
            		searchMap.put("deptCds", deptCds[i]);
            		searchMap.put("castTc", castTcs[i]);
            		searchMap.put("posTc", posTcs[i]);
            		searchMap.put("salarys", salarys[i]);
            		searchMap.put("rates", rates[i]);
            		searchMap.put("workMon", workMons[i]);
            		searchMap.put("totBonSals", totBonSals[i]);


            		String empn = empns[i];

        			if(!"".equals(empn) ){
        				returnMap = deleteData("prs.bonus.bonDir.deleteData", searchMap, true);
        			}
            		returnMap = insertData("prs.bonus.bonDir.insertData", searchMap);
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
        	excelDataList = excel.execlBonDirUpload(searchMap);


        	if(null != excelDataList && 0 < excelDataList.size()) {
				String[] strEmpn 			= (String[]) excelDataList.get(0);
				String[] strDeptCd 			= (String[]) excelDataList.get(1);
				String[] strCastTc          = (String[]) excelDataList.get(2);
				String[] strPosTc           = (String[]) excelDataList.get(3);
				String[] strSalary		    = (String[]) excelDataList.get(4);
				String[] strRate		    = (String[]) excelDataList.get(5);
				String[] strWorkMon   	    = (String[]) excelDataList.get(6);
				String[] strTotBonSal  	    = (String[]) excelDataList.get(7);


                /**********************************
                 * 기존 등록된 실적 삭제
                 **********************************/

                returnMap = insertData("prs.bonus.bonDir.deleteData", searchMap);

                for(int i=0; i<strEmpn.length; i++){
                	if(null != strEmpn[i]){
                		//String strStartPcmtDates = strStartPcmtDate[i];
                		//strStartPcmtDates = strStartPcmtDates.replace("/", "");

                		searchMap.put("empn", strEmpn[i]);
                		searchMap.put("deptCd", strDeptCd[i]);
                		searchMap.put("castTc", strCastTc[i]);
                		searchMap.put("posTc", strPosTc[i]);
                		searchMap.put("salary", strSalary[i]);
                		searchMap.put("rate", strRate[i]);
                		searchMap.put("workMon", strWorkMon[i]);
                		searchMap.put("totBonSal", strTotBonSal[i]);

                		returnMap = insertData("prs.bonus.bonDir.insertData", searchMap);
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
     * 임원성과연봉 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap bonDirListExcel(SearchMap searchMap) {
    	String excelFileName = "임원연봉설정";
    	String excelTitle = "임원연봉 리스트";

    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();

    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));

    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("사번", "EMPN", "center"));
    	excelInfoList.add(new ExcelVO("부서코드", "DEPT_CD", "center"));
    	excelInfoList.add(new ExcelVO("부서명", "DEPT_KOR_NM", "center"));
    	excelInfoList.add(new ExcelVO("이름", "KOR_NM", "center"));
    	excelInfoList.add(new ExcelVO("직급코드", "CAST_TC", "center"));
    	excelInfoList.add(new ExcelVO("직급", "CAST_TC_NM", "center"));
    	excelInfoList.add(new ExcelVO("직위코드", "POS_TC", "center"));
    	excelInfoList.add(new ExcelVO("직위", "POS_TC_NM", "center"));
    	excelInfoList.add(new ExcelVO("입사일", "ORG_PCMT_DATE", "center"));
    	excelInfoList.add(new ExcelVO("평가근무시작일", "START_PCMT_DATE", "center"));
    	excelInfoList.add(new ExcelVO("평가근무종료", "END_PCMT_DATE", "center"));
    	excelInfoList.add(new ExcelVO("기본연봉", "SALARY", "center"));
    	excelInfoList.add(new ExcelVO("지급률", "RATE", "center"));
    	excelInfoList.add(new ExcelVO("근무월수", "WORK_MON", "center"));
    	excelInfoList.add(new ExcelVO("총지급액", "TOT_BON_SAL", "center"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("prs.bonus.bonDir.getList", searchMap));

        return searchMap;
    }

}
