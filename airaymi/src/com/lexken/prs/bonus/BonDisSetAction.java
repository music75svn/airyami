/*************************************************************************
* CLASS 명		: BonDisSetAction
* 작 업 자      : 지원길
* 작 업 일      : 2013년 06월 11일
* 기    능      : 파견직 설정
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

public class BonDisSetAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 파견직 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap bonDisSetList(SearchMap searchMap) {

    	searchMap.addList("detail", getDetail("prs.bonus.bonDisSet.getConfirm", searchMap));

    	return searchMap;
    }

    /**
     * 파견직 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap bonDisSetList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("prs.bonus.bonDisSet.getList", searchMap));

        return searchMap;
    }

    /**
     * 파견직설정 등록/삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap bonDisSetProcess(SearchMap searchMap) {
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
     * 파견직설정 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
        	setStartTransaction();
        	//설정에서 파견직 격려금 유무 확인
        	int cnt = getInt("prs.bonus.bonDisSet.getCount", searchMap);
        		if(0 == cnt){
        			returnMap.put("ErrorNumber", -16);
    	            returnMap.put("ErrorMessage",  "등록된 파견직 격려금이 없습니다.");
        		}

        	String[] empns = searchMap.getStringArray("empns");
        	String[] korNms = searchMap.getStringArray("korNms");
        	String[] deptNms = searchMap.getStringArray("deptNms");
        	String[] deptDetailNms = searchMap.getStringArray("deptDetailNms");
        	String[] castTcNms = searchMap.getStringArray("castTcNms");
        	String[] posTcNms = searchMap.getStringArray("posTcNms");
        	String[] startPcmtDates = searchMap.getStringArray("startPcmtDates");
        	String[] workMons = searchMap.getStringArray("workMons");
            //String[] sepPays = searchMap.getStringArray("contents");

            for(int i=0; i<empns.length; i++){
            	if(null != empns[i]){
            		searchMap.put("empn", empns[i]);
            		searchMap.put("korNm", korNms[i]);
            		searchMap.put("deptNm", deptNms[i]);
            		searchMap.put("deptDetailNm", deptDetailNms[i]);
            		searchMap.put("castTcNm", castTcNms[i]);
            		searchMap.put("posTcNm", posTcNms[i]);
            		searchMap.put("startPcmtDate", startPcmtDates[i]);
            		searchMap.put("workMon", workMons[i]);
            		//searchMap.put("sepPay", sepPays[i]);


            		String empn = empns[i];

        			if(!"".equals(empn) ){
        				returnMap = deleteData("prs.bonus.bonDisSet.deleteData", searchMap, true);
        			}
            		returnMap = insertData("prs.bonus.bonDisSet.insertData", searchMap);
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
        	excelDataList = excel.execlBonDisSetUpload(searchMap);


        	if(null != excelDataList && 0 < excelDataList.size()) {
				String[] strEmpn 			= (String[]) excelDataList.get(0);
				String[] strKorNm    	    = (String[]) excelDataList.get(1);
				String[] strDeptNm    	    = (String[]) excelDataList.get(2);
				String[] strDeptDetailNm    = (String[]) excelDataList.get(3);
				String[] strCastTcNm        = (String[]) excelDataList.get(4);
				String[] strPosTcNm         = (String[]) excelDataList.get(5);
				String[] strStartPcmtDate   = (String[]) excelDataList.get(6);
				String[] strWorkMon   	    = (String[]) excelDataList.get(7);
				//String[] strSepPay  	    = (String[]) excelDataList.get(8);

                /**********************************
                 * 기존 등록된 실적 삭제
                 **********************************/

                returnMap = insertData("prs.bonus.bonDisSet.deleteData", searchMap);

                for(int i=0; i<strEmpn.length; i++){
                	if(null != strEmpn[i]){
                		String strStartPcmtDates = strStartPcmtDate[i];
                		strStartPcmtDates = strStartPcmtDates.replace("/", "");

                		searchMap.put("empn", strEmpn[i]);
                		searchMap.put("korNm", strKorNm[i]);
                		searchMap.put("deptNm", strDeptNm[i]);
                		searchMap.put("deptDetailNm", strDeptDetailNm[i]);
                		searchMap.put("castTcNm", strCastTcNm[i]);
                		searchMap.put("posTcNm", strPosTcNm[i]);
                		searchMap.put("startPcmtDate", strStartPcmtDates);
                		searchMap.put("workMon", strWorkMon[i]);
                		//searchMap.put("sepPay", strSepPay[i]);

                		returnMap = insertData("prs.bonus.bonDisSet.insertData", searchMap);
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
    public SearchMap popBonDisSetExcelUpload(SearchMap searchMap) {

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
    public SearchMap bonDisSetListExcel(SearchMap searchMap) {
    	String excelFileName = "파견직설정";
    	String excelTitle = "파견직설정 리스트";

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
    	excelInfoList.add(new ExcelVO("부서명", "DEPT_NM", "center"));
    	excelInfoList.add(new ExcelVO("부(팀)명", "DEPT_DETAIL_NM", "center"));
    	excelInfoList.add(new ExcelVO("직급", "CAST_TC_NM", "center"));
    	excelInfoList.add(new ExcelVO("직위", "POS_TC_NM", "center"));
    	excelInfoList.add(new ExcelVO("입사일", "START_PCMT_DATE", "center"));
    	excelInfoList.add(new ExcelVO("근무개월", "WORK_MON", "center"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("prs.bonus.bonDisSet.getList", searchMap));

        return searchMap;
    }

}
