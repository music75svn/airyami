/*************************************************************************
* CLASS 명      : excludeUserAction
* 작 업 자      : 박종호
* 작 업 일      : 2013년 09월 04일
* 기    능      : 간부 별도평가군 별도평가제외자
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    박종호      2013년 05월 31일         최 초 작 업
**************************************************************************/
package com.lexken.cbe.exc;

import java.util.ArrayList;
import java.util.HashMap;

import com.lexken.framework.common.ErrorMessages;
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

public class ExcludeUserAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 간부 별도평가군 별도평가제외자 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap excludeUserList(SearchMap searchMap) {

    	return searchMap;
    }

    /**
     * 간부 별도평가군 별도평가제외자 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap excludeUserList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("cbe.exc.excludeUser.getList", searchMap));

        return searchMap;
    }

    /**
     * 간부 별도평가군 별도평가제외자 상세화면
     * @param
     * @return String
     * @throws
     */
    public SearchMap excludeUserModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");

    	HashMap detail = new HashMap();

    	if("MOD".equals(stMode)) {
    		detail = getDetail("cbe.exc.excludeUser.getDetail", searchMap);
    		searchMap.addList("detail", detail);
    		//관리조직 조회
	        searchMap.addList("deptList", getList("cbe.exc.excludeUser.getDeptList", searchMap));
	        searchMap.addList("posTcList", getList("cbe.exc.excludeUser.getPosTcList", searchMap));
    	}

        return searchMap;
    }

    /**
     * 간부 별도평가군 별도평가제외자 등록/대상자자동등록/수정/삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap excludeUserProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");

        /**********************************
         * 무결성 체크
         **********************************/
        if(!"DEL".equals(stMode) && !"AUTO".equals(stMode)) {
        	returnMap = this.validChk(searchMap);

        	if((Integer)returnMap.get("ErrorNumber") < 0 ){
                searchMap.addList("returnMap", returnMap);
                return searchMap;
            }
        }

        /**********************************
         * 등록/대상자자동등록/수정/삭제
         **********************************/
        if("AUTO".equals(stMode)) {
        	searchMap = insertAutoDB(searchMap);
        }

        /**********************************
         * Return
         **********************************/
        return searchMap;
    }


    /**
     * 간부 별도평가군 별도평가제외자 대상자 자동등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap insertAutoDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

     try {
          setStartTransaction();

	      /**********************************
	       * 대상자 자동등록
	       **********************************/

            returnMap = insertData("cbe.exc.excludeUser.deleteUseDataAuto", searchMap);
		  	returnMap = insertData("cbe.exc.excludeUser.insertUseDataAuto", searchMap);

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

		returnMap = ValidationChk.lengthCheck(searchMap.getString("excGrpNm"), "별도평가군명", 1, 100);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		if("".equals(searchMap.getString("deptId")) && "".equals(searchMap.getString("posTc"))){
			returnMap.put("ErrorNumber",  -1);
    		returnMap.put("ErrorMessage", "부서와 직위 중 한가지는 필수 입력입니다.");
    		return returnMap;
		}

		if("02".equals(searchMap.getString("evalType"))){
			int bscRate = 0;
			int prsRate = 0;
			int orgRate = 0;
			int effRate = 0;

			if("0".equals(searchMap.get("bscRate"))){
				searchMap.put("bscRate", "");
			}else if(!"".equals(searchMap.get("bscRate"))){
				bscRate = searchMap.getInt("bscRate");
			}

			if("0".equals(searchMap.get("prsRate"))){
				searchMap.put("prsRate", "");
			}else if(!"".equals(searchMap.get("prsRate"))){
				prsRate = searchMap.getInt("prsRate");
			}

			if("0".equals(searchMap.get("orgRate"))){
				searchMap.put("orgRate", "");
			}else if(!"".equals(searchMap.get("orgRate"))){
				orgRate = searchMap.getInt("orgRate");
			}

			if("0".equals(searchMap.get("effRate"))){
				searchMap.put("effRate", "");
			}else if(!"".equals(searchMap.get("effRate"))){
				effRate = searchMap.getInt("effRate");
			}

			int sumRate = bscRate + prsRate + orgRate + effRate;

			if(100 != sumRate){
				returnMap.put("ErrorNumber",  -1);
        		returnMap.put("ErrorMessage", "비율의 합은 100이여야 합니다.");

            	return returnMap;
			}
		}


        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;
    }



    /**
     * 간부 별도평가군 별도평가제외자 대상자관리 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap excludeUserUseList(SearchMap searchMap) {

        searchMap.addList("excGrpUseDatail", getDetail("cbe.exc.excludeUser.getExcGrpUseDatail", searchMap));

    	return searchMap;
    }

    /**
     * 간부 별도평가군 별도평가제외자 대상자관리 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap excludeUserUseList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("cbe.exc.excludeUser.getUseList", searchMap));

        return searchMap;
    }

    /**
     * 간부 별도평가군 별도평가제외자 대상자관리 상세보기
     * @param
     * @return String
     * @throws
     */
    public SearchMap excludeUserUseModify(SearchMap searchMap) {

    	searchMap.addList("excGrpUseDatail", getDetail("cbe.exc.excludeUser.getExcGrpUseDatail", searchMap));
    	searchMap.addList("insaUseList", getList("cbe.exc.excludeUser.getInsaUseList", searchMap));
        searchMap.addList("userList", getList("cbe.exc.excludeUser.getUseList", searchMap));
        searchMap.addList("excDeptList", getList("cbe.exc.excludeUser.getExcDeptList", searchMap));

        return searchMap;
    }

    /**
     * 간부 별도평가군 별도평가제외자 대상자관리 등록/삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap excludeUserUseProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");

        if ("".equals(stMode)) stMode = "ADD";

        /**********************************
         * 등록/삭제
         **********************************/
        if("ADD".equals(stMode)) {
            searchMap = insertUseDB(searchMap);
        }else if("DEL".equals(stMode)) {
            searchMap = deleteUseDB(searchMap);
        }

        /**********************************
         * Return
         **********************************/
        return searchMap;
    }

    /**
     * 간부 별도평가군 별도평가제외자 대상자관리 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap insertUseDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        setStartTransaction();

        String empn    = searchMap.getString("empns");
        String[] empns = searchMap.getString("empns").split("\\|", 0);

        returnMap = updateData("cbe.exc.excludeUser.deleteUseData", searchMap, true);

        if(!"".equals(empn)) {
	        for (int i = 0; i < empns.length; i++) {
	        	searchMap.put("empn", empns[i]);
	        	returnMap = insertData("cbe.exc.excludeUser.insertUseData", searchMap, true);
	        }
        }

        setEndTransaction();

        /**********************************
         * Return
         **********************************/
        searchMap.addList("returnMap", returnMap);
        return searchMap;
    }

    /**
     * 간부 별도평가군 별도평가제외자 대상자관리 삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap deleteUseDB(SearchMap searchMap) {

        HashMap returnMap = new HashMap();

	    try {
			String[] empns = searchMap.getString("empns").split("\\|", 0);

	        setStartTransaction();

	        if(null != empns && 0 < empns.length) {
		        for (int i = 0; i < empns.length; i++) {
		        	logger.debug("empn :"+empns[i]);
		            searchMap.put("empn", empns[i]);
		            returnMap = updateData("cbe.exc.excludeUser.deleteUseData", searchMap);
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

}
