/*************************************************************************
* CLASS 명      : StagnationDeptAction
* 작 업 자      : 지원길
* 작 업 일      : 2013년 9월 13일
* 기    능      : 부진부서관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    지원길      2013년 9월 13일            최 초 작 업
**************************************************************************/
package com.lexken.bsc.system;

import java.util.HashMap;

import com.lexken.framework.codeUtil.CodeUtil;
import com.lexken.framework.codeUtil.CodeUtilReLoad;
import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.struts2.IsBoxActionSupport;
import com.lexken.framework.util.StaticUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginManager;

public class StagnationDeptAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 부진부서 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap stagnationDeptList(SearchMap searchMap) {

        return searchMap;
    }

    /**
     * 부진부서 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap stagnationDeptList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("bsc.system.stagnationDept.getList", searchMap));

        return searchMap;
    }

    /**
     * 부진부서 상세화면
     * @param
     * @return String
     * @throws
     */
    public SearchMap stagnationDeptModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");


        if("MOD".equals(stMode)) {
        	searchMap.addList("detail", getDetail("bsc.system.stagnationDept.getDetail", searchMap));
        }

        return searchMap;
    }

    /**
     * 부진부서 등록/수정/삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap stagnationDeptProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");


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
     * 부진부서 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
        	setStartTransaction();

        	returnMap = updateData("bsc.system.stagnationDept.insertData", searchMap);

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
     * 부진부서 수정
     * @param
     * @return String
     * @throws
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
        	setStartTransaction();

        	returnMap = updateData("bsc.system.stagnationDept.updateData", searchMap);

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
     * 부진부서 삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
        	setStartTransaction();

	        String[] scDeptIds = searchMap.getString("scDeptIds").split("\\|", 0);

	        for (int i=0; i < scDeptIds.length; i++) {
	            searchMap.put("scDeptId", scDeptIds[i]);
	            returnMap = updateData("bsc.system.stagnationDept.deleteData", searchMap);
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

}
