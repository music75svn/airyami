/*************************************************************************
* CLASS 명      : targetAction
* 작 업 자      : 정용훈
* 작 업 일      : 2013년 06월 22일
* 기    능      : 경영목표관리
* -------------------------------- 변 경 이 력 --------------------------------
* 번 호		작 업 자      	 작   업   일        변 경 내 용              비고
* -----		---------  		---------------  -------------------------	-------
*   1   	정 용 훈 		2013년 06월 22일      최 초 작 업
**************************************************************************/
package com.lexken.man.man;

import java.util.ArrayList;
import java.util.HashMap;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.struts2.IsBoxActionSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.util.StaticUtil;

public class TargetAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 경영목표 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap targetList(SearchMap searchMap) {

        return searchMap;
    }

    /**
     * 경영목표 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap targetList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("man.man.target.getList", searchMap));

        return searchMap;
    }

    /**
     * 경영목표 상세화면
     * @param
     * @return String
     * @throws
     */
    public SearchMap targetModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");

    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("man.man.target.getDetail", searchMap));
    	}

        return searchMap;
    }

    /**
     * 경영목표 등록/수정/삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap targetProcess(SearchMap searchMap) {
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
     * 경영목표 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
        	setStartTransaction();

        	returnMap = insertData("man.man.target.insertData", searchMap);

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
     * 경영목표 수정
     * @param
     * @return String
     * @throws
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
	        setStartTransaction();

	        returnMap = updateData("man.man.target.updateData", searchMap);

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
     * 경영목표 삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap deleteDB(SearchMap searchMap) {

        HashMap returnMap = new HashMap();

	    try {
	        String[] manIds = searchMap.getString("manIds").split("\\|", 0);

	        setStartTransaction();

	        if(null != manIds && 0 < manIds.length) {
		        for (int i = 0; i < manIds.length; i++) {
		            searchMap.put("manId", manIds[i]);
		            returnMap = updateData("man.man.target.deleteData", searchMap);
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

		returnMap = ValidationChk.lengthCheck(searchMap.getString("manNm"), "경영목표", 1, 150);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.lengthCheck(searchMap.getString("manWeight"), "가중치", 1, 10);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.onlyNumberPoint(searchMap.getString("manWeight"), "가중치");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.onlyNumber(searchMap.getString("sortOrder"), "정렬순서");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}


        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;
    }

    /**
     * 경영목표 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap targetListExcel(SearchMap searchMap) {
    	String excelFileName = "경영목표";
    	String excelTitle = "경영목표 리스트";

    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("사용여부", (String)searchMap.get("useYnNm")));

    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("경영목표ID", "MAN_ID", "left"));
    	excelInfoList.add(new ExcelVO("경영목표", "MAN_NM", "left"));
    	excelInfoList.add(new ExcelVO("가중치", "MAN_WEIGHT", "left"));
    	excelInfoList.add(new ExcelVO("정렬순서", "SORT_ORDER", "left"));

    	if(!"".equals(StaticUtil.nullToBlank((String)searchMap.get("useYn")))) {
			searchMap.put("findUseYn", (String)searchMap.get("useYn"));
		}

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("man.man.target.getList", searchMap));

        return searchMap;
    }

}
