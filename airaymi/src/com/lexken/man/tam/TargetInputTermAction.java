/*************************************************************************
* CLASS 명      : TargetInputTermAction
* 작 업 자      :  이상민
* 작 업 일      : 2012년 6월 24일
* 기    능      : 목표입력기간
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1     이상민      2012년 06월 24일         최 초 작 업
**************************************************************************/
package com.lexken.man.tam;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.core.CommonService;

public class TargetInputTermAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 목표입력기간 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap targetInputTermList(SearchMap searchMap) {

        return searchMap;
    }

    /**
     * 목표입력기간 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap targetInputTermList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("man.tam.targetInputTerm.getList", searchMap));

        return searchMap;
    }

    /**
     * 목표입력기간 상세화면
     * @param
     * @return String
     * @throws
     */
    public SearchMap targetInputTermModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("stMode");

    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("man.tam.targetInputTerm.getDetail", searchMap));
    	}

        return searchMap;
    }

    /**
     * 목표입력기간 등록/수정/삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap targetInputTermProcess(SearchMap searchMap) {
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
        if("ADD".equals(stMode)) {
            searchMap = insertDB(searchMap);
        } else if("MOD".equals(stMode)) {
            searchMap = updateDB(searchMap);
        } else if("DEL".equals(stMode)) {
            searchMap = deleteDB(searchMap);
        }

        /**********************************
         * Return
         **********************************/
        return searchMap;
    }

    /**
     * 목표입력기간 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
        	setStartTransaction();

        	/**********************************
	         * 해당 년도 등록여부 확인
	         **********************************/
	        int cnt = getInt("man.tam.targetInputTerm.getCount", searchMap);

	        if(0 < cnt) {
	            returnMap.put("ErrorNumber", ErrorMessages.FAILURE_DUP2_CODE);
	            returnMap.put("ErrorMessage", ErrorMessages.format(ErrorMessages.FAILURE_DUP2_MESSAGE, "평가년도"));
	        } else {
	        	returnMap = insertData("man.tam.targetInputTerm.insertData", searchMap);
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
     * 목표입력기간 수정
     * @param
     * @return String
     * @throws
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
	        setStartTransaction();

	        returnMap = updateData("man.tam.targetInputTerm.updateData", searchMap);

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
     * 목표입력기간 삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap deleteDB(SearchMap searchMap) {

        HashMap returnMap = new HashMap();

	    try {
	    	String[] years = searchMap.getString("years").split("\\|", 0);

	        setStartTransaction();

	        if(null != years && 0 < years.length) {
	        	 for (int i = 0; i < years.length; i++) {
	 	            searchMap.put("year", years[i]);
		            returnMap = updateData("man.tam.targetInputTerm.deleteData", searchMap);
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
     *  Validation 체크(무결성 체크)
     * @param SearchMap
     * @return HashMap
     */
    private HashMap validChk(SearchMap searchMap) {
        HashMap returnMap         = new HashMap();
        int     resultValue        = 0;

        //Validation 체크 추가

        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;
    }

    /**
     * 목표입력기간 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap targetInputTermListExcel(SearchMap searchMap) {
    	String excelFileName = "목표입력기간";
    	String excelTitle = "목표입력기간 리스트";

    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	/*
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("공통코드명", StaticUtil.nullToDefault((String)searchMap.get("codeGrpNm"), "전체")));
    	excelSearchInfoList.add(new ExcelVO("사용여부", (String)searchMap.get("useYnNm")));
    	*/

    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가년도", "YEAR", "left"));
    	excelInfoList.add(new ExcelVO("시작일자", "START_DT", "left"));
    	excelInfoList.add(new ExcelVO("종료일자", "END_DT", "left"));


    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", getList("man.tam.targetInputTerm.getList", searchMap));

        return searchMap;
    }

}
