/*************************************************************************
* CLASS 명		: DirSalSetAction
* 작 업 자      : 안요한
* 작 업 일      : 2013년 06월 10일
* 기    능      : 임원연봉 설정
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    안요한      2013년 06월 10일         최 초 작 업
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

public class DirSalSetAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 임원연봉 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap dirSalSetList(SearchMap searchMap) {

    	searchMap.addList("detail", getDetail("prs.bonus.dirSalSet.getConfirm", searchMap));
    	
    	return searchMap;
    }

    /**
     * 임원연봉 데이터 조회(xml)
     * @param
     * @return String  SSSSS
     * @throws
     */
    public SearchMap dirSalSetList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("prs.bonus.dirSalSet.getList", searchMap));

        return searchMap;
    }

    /**
     * 임원연봉 수정화면
     * @param
     * @return String
     * @throws
     */
    public SearchMap dirSalSetModify(SearchMap searchMap) {

        return searchMap;
    }

    /**
     * 임원연봉 수정 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap dirSalSetModify_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("prs.bonus.dirSalSet.getList", searchMap));

        return searchMap;
    }



    /**
     * 임원연봉설정 등록/수정/삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap dirSalSetProcess(SearchMap searchMap) {
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
        }else if("MOD".equals(stMode)) {
            searchMap = updateDB(searchMap);
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

        	String[] empns = searchMap.getStringArray("empns");
        	String[] deptCds = searchMap.getStringArray("deptCds");
        	String[] castTcs = searchMap.getStringArray("castTcs");
        	String[] posTcs = searchMap.getStringArray("posTcs");
        	String[] workMons = searchMap.getStringArray("workMons");
            String[] rates = searchMap.getStringArray("rates");
            String[] salarys = searchMap.getStringArray("salarys");
            String[] contents = searchMap.getStringArray("contents");

            returnMap = deleteData("prs.bonus.dirSalSet.deleteData", searchMap, true); //임원연봉 전체 삭제

            for(int i=0; i<empns.length; i++){
            	if(null != empns[i]){
            		searchMap.put("empn", empns[i]);
            		searchMap.put("deptCd", deptCds[i]);
            		searchMap.put("castTc", castTcs[i]);
            		searchMap.put("posTc", posTcs[i]);
            		searchMap.put("workMon", workMons[i]);
            		searchMap.put("rate", rates[i]);
            		searchMap.put("salary", salarys[i]);
            		searchMap.put("content", contents[i]);

            		returnMap = insertData("prs.bonus.dirSalSet.insertData", searchMap);
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
     * 임원연봉설정 수정
     * @param
     * @return String
     * @throws
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
        	setStartTransaction();

        	String[] empns = searchMap.getStringArray("empns");
        	String[] deptCds = searchMap.getStringArray("deptCds");
        	String[] castTcs = searchMap.getStringArray("castTcs");
        	String[] posTcs = searchMap.getStringArray("posTcs");
        	String[] workMons = searchMap.getStringArray("workMons");
            String[] rates = searchMap.getStringArray("rates");
            String[] salarys = searchMap.getStringArray("salarys");
            String[] contents = searchMap.getStringArray("contents");


            for(int i=0; i<empns.length; i++){
            	if(null != empns[i]){
            		searchMap.put("empn", empns[i]);
            		searchMap.put("deptCd", deptCds[i]);
            		searchMap.put("castTc", castTcs[i]);
            		searchMap.put("posTc", posTcs[i]);
            		searchMap.put("workMon", workMons[i]);
            		searchMap.put("rate", rates[i]);
            		searchMap.put("salary", salarys[i]);
            		searchMap.put("content", contents[i]);

            		returnMap = updateData("prs.bonus.dirSalSet.updateData", searchMap);
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
        	excelDataList = excel.execlDirSalUpload(searchMap);

        	if(null != excelDataList && 0 < excelDataList.size()) {
        		String[] strEmpn = (String[]) excelDataList.get(0);
        		String[] strDeptCd = (String[]) excelDataList.get(1);
                String[] strCastTc = (String[]) excelDataList.get(2);
                String[] strPosTc = (String[]) excelDataList.get(3);
                String[] strWorkMon = (String[]) excelDataList.get(4);
                String[] strRate = (String[]) excelDataList.get(5);
                String[] strSalary = (String[]) excelDataList.get(6);
                String[] strContent = (String[]) excelDataList.get(7);

                /**********************************
                 * 기존 등록된 실적 삭제
                 **********************************/

                returnMap = insertData("prs.bonus.dirSalSet.deleteData", searchMap);

                for(int i=0; i<strEmpn.length; i++){
                	if(null != strEmpn[i]){
                		searchMap.put("empn", strEmpn[i]);
                		searchMap.put("deptCd", strDeptCd[i]);
                		searchMap.put("castTc", strCastTc[i]);
                		searchMap.put("posTc", strPosTc[i]);
                		searchMap.put("workMon", strWorkMon[i]);
                		searchMap.put("rate", strRate[i]);
                		searchMap.put("salary", strSalary[i]);
                		searchMap.put("content", strContent[i]);

                		returnMap = insertData("prs.bonus.dirSalSet.insertData", searchMap);
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
    public SearchMap popExcelUpload(SearchMap searchMap) {

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
     * 임원연봉설정 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap dirSalSetListExcel(SearchMap searchMap) {
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
    	excelInfoList.add(new ExcelVO("이름", "KOR_NM", "center"));
    	excelInfoList.add(new ExcelVO("부서코드", "DEPT_CD", "center"));
    	excelInfoList.add(new ExcelVO("직급코드", "CAST_TC", "center"));
    	excelInfoList.add(new ExcelVO("직급", "CAST_TC_NM", "center"));
    	excelInfoList.add(new ExcelVO("직위코드", "POS_TC", "center"));
    	excelInfoList.add(new ExcelVO("직위", "POS_TC_NM", "center"));
    	excelInfoList.add(new ExcelVO("입사일", "ORG_PCMT_DATE", "center"));
    	excelInfoList.add(new ExcelVO("평가근무시작일", "START_PCMT_DATE", "center"));
    	excelInfoList.add(new ExcelVO("평가근무종료일", "END_PCMT_DATE", "center"));
    	excelInfoList.add(new ExcelVO("퇴직일", "ORG_END_DATE", "center"));
    	excelInfoList.add(new ExcelVO("재직구분", "PCMT_TC_NM", "center"));
    	excelInfoList.add(new ExcelVO("근무기간", "WORK_MON", "center"));
    	excelInfoList.add(new ExcelVO("지급률", "RATE", "center"));
    	excelInfoList.add(new ExcelVO("기본연봉", "SALARY", "center"));
    	excelInfoList.add(new ExcelVO("비고", "CONTENT", "center"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("prs.bonus.dirSalSet.getList", searchMap));

        return searchMap;
    }

}
