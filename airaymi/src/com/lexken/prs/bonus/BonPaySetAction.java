/*************************************************************************
* CLASS 명      : BonPaySetAction
* 작 업 자      : 안요한
* 작 업 일      : 2013년 6월 17일
* 기    능      : 직원지급기준 설정
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    안요한      2013년 6월 17일             최 초 작 업
**************************************************************************/
package com.lexken.prs.bonus;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.core.CommonService;

public class BonPaySetAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 직원지급기준 설정 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap bonPaySetModify(SearchMap searchMap) {

    	searchMap.addList("detail", getDetail("prs.bonus.bonPaySet.getList", searchMap));

        return searchMap;
    }

    /**
     * 직원지급기준 설정 등록/수정/삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap bonPaySetProcess(SearchMap searchMap) {
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
        if("INIT".equals(stMode)) {
            searchMap = initialAggregateDB(searchMap);
        }else if("VERI".equals(stMode)){
        	searchMap = verificationAggregateDB(searchMap);
        }else if("CONF".equals(stMode)){
        	searchMap = ConfirmDB(searchMap);
        }else if("CANCEL".equals(stMode)){
        	searchMap = ConfirmCancelDB(searchMap);
        }

        /**********************************
         * Return
         **********************************/
        return searchMap;
    }

    /**
     * 직원지급기준 초기집계
     * @param
     * @return String
     * @throws
     */
    public SearchMap initialAggregateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
        	setStartTransaction();

        	returnMap = updateData("prs.bonus.bonPaySet.updateInitReset", searchMap);		 	// 설정초기화

        	returnMap = deleteData("prs.bonus.bonPaySet.deleteBonDir", searchMap, true); 	// 임원 성과연봉 삭제
        	returnMap = insertData("prs.bonus.bonPaySet.insertBonDirInit", searchMap);		// 임원 성과연봉 초기집계
        	returnMap = insertData("prs.bonus.bonPaySet.callInitialAggregate", searchMap);	// 직원 차체성과급,정평성과급 집계

        	returnMap = updateData("prs.bonus.bonPaySet.updateInitAggDt", searchMap);		 	// 초기집계 시간 저장

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
     * 직원지급기준 검증집계
     * @param
     * @return String
     * @throws
     */
    public SearchMap verificationAggregateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();
        String sumBolSal = "";

        try {
        	setStartTransaction();

        	returnMap = updateData("prs.bonus.bonPaySet.updateRateDate", searchMap);		 	// 지급기준 지급율 저장

        	sumBolSal = getStr("prs.bonus.bonPaySet.getSumBolSal", searchMap);
        	searchMap.put("sumBolSal", sumBolSal);

        	returnMap = insertData("prs.bonus.bonPaySet.callInitialAggregate", searchMap);	// 직원 차체성과급,정평성과급 집계
        	returnMap = updateData("prs.bonus.bonPaySet.updateBonDirVeri", searchMap);		// 임원 성과연봉 검증집계

        	returnMap = updateData("prs.bonus.bonPaySet.updateDate", searchMap);		 	// 지급기준 검증집계 시간 저장

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
     * 확정
     * @param
     * @return String
     * @throws
     */
    public SearchMap ConfirmDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();
        String sumBolSal = "";

        try {
        	setStartTransaction();
        	returnMap = updateData("prs.bonus.bonPaySet.updateConfirm", searchMap);		 	// 확정 시간 저장

        	returnMap = updateData("prs.bonus.bonPaySet.deleteDirConf", searchMap, true);
        	returnMap = updateData("prs.bonus.bonPaySet.deleteEmpConf", searchMap, true);
        	
        	returnMap = updateData("prs.bonus.bonPaySet.insertDirConf", searchMap);
        	returnMap = updateData("prs.bonus.bonPaySet.insertEmpConf", searchMap);
        	
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
     * 확정취소
     * @param
     * @return String
     * @throws
     */
    public SearchMap ConfirmCancelDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();
        String sumBolSal = "";

        try {
        	setStartTransaction();
        	returnMap = updateData("prs.bonus.bonPaySet.updateConfirmCancel", searchMap);		 	// 확정 시간 저장

        	returnMap = updateData("prs.bonus.bonPaySet.deleteDirConf", searchMap, true);
        	returnMap = updateData("prs.bonus.bonPaySet.deleteEmpConf", searchMap, true);       	
        	
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

        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;
    }

}
