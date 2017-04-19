/*************************************************************************
* CLASS 명   : OfficeMetricAction
* 작 업 자      : 김민주
* 작 업 일      : 2013년 06월 24일
* 기    능      : 공공기관 경영평가지표
* -------------------------------- 변 경 이 력 --------------------------------
* 번 호		작 업 자      	 작   업   일        변 경 내 용              비고
* -----		---------  		---------------  -------------------------	-------
*   1   	김 민 주		2013년 06월 24일      최 초 작 업
**************************************************************************/
package com.lexken.gov.office;

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

public class OfficeMetricAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 공공기관 경영평가지표 상세화면
     * @param
     * @return String
     * @throws
     */
    public SearchMap officeMetricModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");

    	//경영평가지표 pool 조회
    	searchMap.addList("getMetricGrpList", getList("gov.office.officeMetric.getMetricGrpList", searchMap));
    	//선택된 경영평가지표 조회
        searchMap.addList("officeMetricList", getList("gov.office.officeMetric.officeMetricList", searchMap));

        return searchMap;
    }

    /**
     * 공공기관 경영평가지표 등록/수정/삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap officeMetricProcess(SearchMap searchMap) {
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
        }

        /**********************************
         * Return
         **********************************/
        return searchMap;
    }

    /**
     * 공공기관 경영평가지표 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        setStartTransaction();

        String govMetricGrpId    = searchMap.getString("govMetricGrpIds");
        String[] govMetricGrpIds = searchMap.getString("govMetricGrpIds").split("\\|", 0);

        returnMap = deleteData("gov.office.officeMetric.deleteData", searchMap, true);

        if(!"".equals(govMetricGrpId)) {
	        for (int i = 0; i < govMetricGrpIds.length; i++) {
	        	searchMap.put("govMetricGrpId", govMetricGrpIds[i]);
	        	returnMap = insertData("gov.office.officeMetric.insertData", searchMap);
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
     *  Validation 체크(무결성 체크)
     * @param SearchMap
     * @return HashMap
     */
    private HashMap validChk(SearchMap searchMap) {
        HashMap returnMap         = new HashMap();
        int     resultValue        = 0;

        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;
    }

}
