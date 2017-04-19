/*************************************************************************
* CLASS 명      : EmpPaySetAction
* 작 업 자      : 안요한
* 작 업 일      : 2013년 06월 13일
* 기    능      : 직원연봉-인사정보
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일				변 경 내 용        	 비고
* ----  ---------   -----------------  	-------------------------  --------
*   1    안 요 한    2013년 06월 13일        	최 초 작 업
**************************************************************************/
package com.lexken.prs.bonus;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelUpload;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.StringConstants;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.util.StaticUtil;

public class EmpPaySetAction extends CommonService {
	private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 직원정보 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap empPaySetList(SearchMap searchMap) {

    	searchMap.addList("detail", getDetail("prs.bonus.empPaySet.getConfirm", searchMap));

    	// 최상위 평가조직 조회
    	HashMap topScDept = getDetail("prs.bonus.empPaySet.getTopDeptInfo", searchMap);


    	// 파라미터가 null로 들어올 때 디폴트값 셋팅.
    	if(topScDept==null){
    		topScDept =new HashMap();
    		topScDept.put("DEPT_CD", "");
    		topScDept.put("DEPT_KOR_NM", "");
    	}

    	// 파라미터가 null로 들어올 때 디폴트값 셋팅.
    	String findDeptCd =  StaticUtil.nullToDefault(searchMap.getString("findDeptCd"), (String)topScDept.get("DEPT_CD"));	// 조직코드가 없으면 전사조직코드를 셋팅.
    	String findUpDeptName =  StaticUtil.nullToDefault(searchMap.getString("findUpDeptName"), (String)topScDept.get("DEPT_KOR_NM")) ; ;	// 조직명이 없으면 전사조직명을 셋팅.

    	// 디폴트 조회조건 설정
    	searchMap.put("findDeptCd", findDeptCd);	// 검색버튼 조회시 조직트리에 기존 선택한 조직이 표시되도록 설정.(findSearchCodeId에 넣어주면 우선 적용됨. 트리와 검색조건을 별도로 사용할 경우에 한함.)
    	searchMap.put("findUpDeptName", findUpDeptName);

    	searchMap.addList("deptTree", getList("prs.bonus.empPaySet.getDeptList", searchMap)); //미사용, 임원은 제외한 인사조직

        return searchMap;
    }

    /**
     * 직원정보 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap empPaySetList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("prs.bonus.empPaySet.getList", searchMap));

        return searchMap;
    }

    /**
     * 개인급여 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap empPaySetInfoList_xml(SearchMap searchMap) {

    	searchMap.addList("list", getList("prs.bonus.empPaySet.getDetail", searchMap));

    	return searchMap;
    }

    /**
     * 개인급여 상세화면
     * @param
     * @return String
     * @throws
     */
    public SearchMap empPaySetModify(SearchMap searchMap) {

    	searchMap.addList("deptTree", getList("prs.bonus.empPaySet.getDeptList", searchMap)); //미사용, 임원은 제외한 인사조직

    	searchMap.addList("detail", getDetail("prs.bonus.empPaySet.getConfirm", searchMap));
    	
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
     * 직원급여 등록/수정/삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap empPaySetProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");

        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("ADD".equals(stMode)) {
        	searchMap = insertDB(searchMap);
        } else if("ADDEXCEL".equals(stMode)){
        	searchMap = insertExcelDB(searchMap);
        }

         return searchMap;
    }

    /**
     * 직원급여 수
     * @param
     * @return String
     * @throws
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
        	setStartTransaction();

        	String[] seqs = searchMap.getStringArray("seqs");
            String[] empns = searchMap.getStringArray("empns");
            String[] workMons = searchMap.getStringArray("workMons");
            String[] pays = searchMap.getStringArray("pays");
            String[] contents = searchMap.getStringArray("contents");

            for(int i=0; i<empns.length; i++){
            	if(null != empns[i]){
            		searchMap.put("seq", seqs[i]);
            		searchMap.put("empn", empns[i]);
            		searchMap.put("pay", pays[0]);
            		searchMap.put("workMon", workMons[i]);
            		searchMap.put("content", contents[i]);

            		returnMap = updateData("prs.bonus.empPaySet.updateData", searchMap);
            		returnMap = updateData("prs.bonus.empPaySet.updatePayData", searchMap);
            	}
            }

         // 총 근무월 계산
            if(null != searchMap.get("empn")){
            	returnMap = updateData("prs.bonus.empPaySet.insertTotMonData", searchMap);
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
        	excelDataList = excel.execlEmpPayUpload(searchMap);

        	if(null != excelDataList && 0 < excelDataList.size()) {
        		String[] strEmpn = (String[]) excelDataList.get(0);
                String[] strPay = (String[]) excelDataList.get(1);

                /**********************************
                 * 기존 등록된 실적 삭제
                 **********************************/
                returnMap = deleteData("prs.bonus.empPaySet.deleteData", searchMap, true);	//bon_emp_set 삭제

                for(int i=0; i<strEmpn.length; i++){
                	if(null != strEmpn[i]){
                		searchMap.put("empn", strEmpn[i]);
                		returnMap = insertData("prs.bonus.empPaySet.insertData", searchMap); //bon_emp_set에 저장
                	}//if

                }//for

                returnMap = deleteData("prs.bonus.empPaySet.deletePayData", searchMap, true); //bon_emp_pay_set 삭제

                for(int i=0; i<strEmpn.length; i++){
                	if(null != strEmpn[i]){
                		searchMap.put("empn", strEmpn[i]);
                		searchMap.put("pay", strPay[i]);

                		returnMap = insertData("prs.bonus.empPaySet.insertPayData", searchMap); //bon_emp_pay_set 저장

                	}//if

                }//for

        	}//if

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
     * 직원급여 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap empPayExcel(SearchMap searchMap) {
    	String excelFileName = "직원급여_입력_양식";
    	String excelTitle = "직원급여_입력_양식";

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
    	excelInfoList.add(new ExcelVO("소속부서명", "DEPT_KOR_NM", "left"));
    	excelInfoList.add(new ExcelVO("직급명", "CAST_TC_NM", "center"));
    	excelInfoList.add(new ExcelVO("직위명", "POS_TC_NM", "center"));
    	excelInfoList.add(new ExcelVO("기본급", "PAY", "center", "CNT", 6000));
    	excelInfoList.add(new ExcelVO("비고", "CONTENT", "center"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);

    	searchMap.put("excelDataList", getList("prs.bonus.empPaySet.getPayExcelList", searchMap));

    	return searchMap;
    }

}
