/*************************************************************************
* CLASS 명      : ExcEvalUserAction
* 작 업 자      : 김상용
* 작 업 일      : 2013년 07월 13일
* 기    능      : 내평 별도평가대상자
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    김상용      2013년 07월 13일         최 초 작 업
**************************************************************************/
package com.lexken.cbe.exc;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.util.StaticUtil;

public class ExcEvalUserAction extends CommonService {
	private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 별도평가대상자 조회 화면
     * @param searchMap
     * @return
     */
    public SearchMap excEvalUserList(SearchMap searchMap) {
    	/**********************************
         * 평가구분 조회
         **********************************/
    	searchMap.addList("evalDegreeList", getList("cbe.excep.excepDept.getDegreeList", searchMap));

    	searchMap.addList("excDeptList", getList("cbe.exc.excEvalUser.getExcDeptList", searchMap));


    	String findEvalDegreeId = (String)searchMap.get("findEvalDegreeId");
    	if("".equals(StaticUtil.nullToBlank(findEvalDegreeId))) {
    		searchMap.put("findEvalDegreeId", (String)searchMap.getDefaultValue("evalDegreeList", "EVAL_DEGREE_ID", 0));
    	}

    	//확정여부 확인
    	searchMap.addList("confirmYn", getDetail("cbe.exc.excGrade.getConfirmYn", searchMap));

    	searchMap.addList("evalUserList", getList("cbe.exc.excEvalUser.evalUserList", searchMap));

    	return searchMap;
    }

    /**
     * 별도평가대상자 조회 화면(xml)
     * @param searchMap
     * @return
     */
    public SearchMap excEvalUserList_xml(SearchMap searchMap) {
    	searchMap.addList("list", getList("cbe.exc.excEvalUser.evalUserList", searchMap));
    	return searchMap;
    }

    /**
     * 별도평가부서 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap excEvalList_ajax(SearchMap searchMap) {

    	searchMap.addList("excEvalList", getList("cbe.exc.excEvalUser.getExcDeptList", searchMap));

    	return searchMap;
    }

    /**
     * 별도평가대상자 수정
     * @param searchMap
     * @return
     */
    public SearchMap excEvalUserModify(SearchMap searchMap) {
    	//확정여부 확인
    	searchMap.addList("confirmYn", getDetail("cbe.exc.excGrade.getConfirmYn", searchMap));

    	searchMap.addList("detail", getList("cbe.exc.excEvalUser.getEvalMethod", searchMap));
    	
    	searchMap.addList("details", getDetail("cbe.exc.excEvalUser.getEvalMethod", searchMap));

    	searchMap.addList("excDeptList", getList("cbe.exc.excEvalUser.getExcDeptList", searchMap));

    	searchMap.put("excUserId", (String)searchMap.getDefaultValue("detail", "EMPN", 0));
    	searchMap.put("excUserNm", (String)searchMap.getDefaultValue("detail", "KOR_NM", 0));
    	searchMap.put("excUserDeptId", (String)searchMap.getDefaultValue("detail", "DEPT_CD", 0));
    	searchMap.put("excUserDeptNm", (String)searchMap.getDefaultValue("detail", "DEPT_KOR_NM", 0));

    	return searchMap;
    }

    /**
     * 별도평가대상자 등록/수정/삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap excEvalUserProcess(SearchMap searchMap) {
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
        } else if ("CONF".equals(stMode)) {
        	searchMap = updateSubmitDB(searchMap);
        } else if ("GET".equals(stMode)) {
        	searchMap = insertUserDB(searchMap);
        }

        /**********************************
         * Return
         **********************************/
        return searchMap;
    }

    /**
     * 별도평가대상자 가져오기 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap insertUserDB(SearchMap searchMap) {
    	HashMap returnMap    = new HashMap();

    	try {
    		setStartTransaction();

    		/**********************************
    		 * 해당 별도평가대상자 삭제
    		 **********************************/
    		 returnMap = deleteData("cbe.exc.excEvalUser.deleteUserData", searchMap, true); //데이터 삭제

    		 returnMap = insertData("cbe.exc.excEvalUser.insertUserData", searchMap);
    		 //returnMap = insertData("cbe.exc.excEvalUser.insertUserWorkData", searchMap);
    		 //returnMap = insertData("cbe.exc.excEvalUser.insertUserDirData", searchMap);

    		 returnMap = updateData("cbe.exc.excEvalUser.deleteAllAdminsData", searchMap, true); //권한삭제

		     returnMap = insertData("cbe.exc.excEvalUser.insertAllAdminData", searchMap); //권한등록


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
     * 별도평가대상자 등록
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
	        int cnt = getInt("cbe.exc.excEvalUser.evalExistUserList", searchMap);

	        if(0 < cnt) {
	            returnMap.put("ErrorNumber", ErrorMessages.FAILURE_DUP2_CODE);
	            returnMap.put("ErrorMessage", ErrorMessages.format(ErrorMessages.FAILURE_DUP2_MESSAGE, "평가대상"));
	        } else {
	        	returnMap = insertData("cbe.exc.excEvalUser.insertData", searchMap);

	        	returnMap = updateData("cbe.exc.excEvalUser.deleteAdminData", searchMap, true); //권한삭제

		        returnMap = insertData("cbe.exc.excEvalUser.insertAdminBaseData", searchMap); //권한등록
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

    public SearchMap updateDB(SearchMap searchMap) {
    	HashMap returnMap    = new HashMap();

    	try {
    		setStartTransaction();

//    		int cnt = getInt("cbe.exc.excEvalUser.evalExistUserList", searchMap);

    		
        	returnMap = insertData("cbe.exc.excEvalUser.updateData", searchMap);

        	returnMap = updateData("cbe.exc.excEvalUser.deleteAdminDataUpdate", searchMap, true); //권한삭제

	        returnMap = insertData("cbe.exc.excEvalUser.insertAdminBaseDataUpdate", searchMap); //권한등록
	        

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
     * 평가실시 삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap deleteDB(SearchMap searchMap) {

        HashMap returnMap = new HashMap();

	    try {
	        String[] empnIds = searchMap.getString("empnIds").split("\\|", 0);
			String[] evalDegreeIds = searchMap.getString("evalDegreeIds").split("\\|", 0);

	        setStartTransaction();

	        if(null != evalDegreeIds && 0 < empnIds.length) {
		        for (int i = 0; i < evalDegreeIds.length; i++) {
		            searchMap.put("empnIds", empnIds[i]);
		            searchMap.put("evalDegreeIds", evalDegreeIds[i]);
		            returnMap = updateData("cbe.exc.excEvalUser.deleteData", searchMap);
		            returnMap = updateData("cbe.exc.excEvalUser.deleteAdminsData", searchMap, true); //권한삭제
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
     * 별도평가대상자 확정하기
     * @param
     * @return String
     * @throws
     */
    public SearchMap updateSubmitDB(SearchMap searchMap) {
    	HashMap returnMap    = new HashMap();

    	try {
    		setStartTransaction();

    		returnMap = updateData("cbe.exc.excEvalUser.insertDeptYnData", searchMap); //확정여부 수정
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
     *  Validation 체크(무결성 체크)
     * @param SearchMap
     * @return HashMap
     */
    private HashMap validChk(SearchMap searchMap) {
        HashMap returnMap         = new HashMap();
        int     resultValue        = 0;
        String stMode = searchMap.getString("mode");

//        if(!"CONF".equals(stMode)) {
//
//	        returnMap = ValidationChk.lengthCheck(searchMap.getString("excUserId"), "별도평가대상자", 1, 50);
//	        if((Integer)returnMap.get("ErrorNumber") < 0 ){
//	        	return returnMap;
//	        }
//        }

        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;
    }

}
