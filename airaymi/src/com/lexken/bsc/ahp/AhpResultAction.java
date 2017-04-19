/*************************************************************************
* CLASS 명      : AhpResultAction
* 작 업 자      : 하윤식
* 작 업 일      : 2012년 11월 29일
* 기    능      : AHP 평가결과
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    하윤식      2012년 11월 29일         최 초 작 업
**************************************************************************/
package com.lexken.bsc.ahp;

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

public class AhpResultAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * AHP 평가결과 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap ahpResultList(SearchMap searchMap) {

    	/**********************************
         * 평가단 조회
         **********************************/
    	searchMap.addList("evalUserGrpList", getList("bsc.ahp.ahpResult.getEvalUserGrpList", searchMap));

    	/**********************************
         * 평가대상그룹 조회
         **********************************/
    	searchMap.addList("itemGrpList", getList("bsc.ahp.ahpResult.getItemGrpList", searchMap));

        return searchMap;
    }

    /**
     * AHP 평가결과 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap ahpResultList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("bsc.ahp.ahpResult.getList", searchMap));

        return searchMap;
    }

    /**
     * AHP 평가결과 상세화면
     * @param
     * @return String
     * @throws
     */
    public SearchMap ahpResultModify(SearchMap searchMap) {

    	searchMap.addList("evalUserGrpNm", getStr("bsc.ahp.ahpResult.getEvalUserGrpNm", searchMap));

    	searchMap.addList("itemGrpNm", getStr("bsc.ahp.ahpResult.getItemGrpNm", searchMap));

    	searchMap.addList("totWeight", getStr("bsc.ahp.ahpResult.getTotWeight", searchMap));

        return searchMap;
    }

    /**
     * AHP 평가결과 상세 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap ahpResultDetailList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("bsc.ahp.ahpResult.getDetailList", searchMap));

        return searchMap;
    }

    /**
     * AHP 평가결과 등록/수정/삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap ahpResultProcess(SearchMap searchMap) {
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
        if("CLOSE".equals(stMode)) {
            searchMap = closeDB(searchMap);
        } else if("MOD".equals(stMode)) {
            searchMap = updateDB(searchMap);
        }

        /**********************************
         * Return
         **********************************/
        return searchMap;
    }

    /**
     * AHP 평가마감
     * @param
     * @return String
     * @throws
     */
    public SearchMap closeDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
        	String[] itemGrpIds = searchMap.getString("itemGrpIds").split("\\|", 0);
        	String[] evalUserGrpIds = searchMap.getString("evalUserGrpIds").split("\\|", 0);

	        setStartTransaction();

	        if(null != itemGrpIds && 0 < itemGrpIds.length) {
		        for (int i = 0; i < itemGrpIds.length; i++) {
		            searchMap.put("itemGrpId", itemGrpIds[i]);
		            searchMap.put("evalUserGrpId", evalUserGrpIds[i]);
		            returnMap = updateData("bsc.ahp.ahpResult.closeData", searchMap);

		            returnMap = insertData("bsc.ahp.ahpResult.execAhpFinalScore", searchMap);
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
     * AHP 조정가중치 적용
     * @param
     * @return String
     * @throws
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
        	String[] itemIds = searchMap.getStringArray("itemIds");
        	String[] adjustWeights = searchMap.getStringArray("adjustWeights");

	        setStartTransaction();

	        /**********************************
	         * 평가대상그룹 총 가중치 저장
	         **********************************/
	        returnMap = updateData("bsc.ahp.ahpResult.updateTotWeight", searchMap);

	        /**********************************
	         * AHP평가 총가중치별 환산
	         **********************************/
	        returnMap = insertData("bsc.ahp.ahpResult.execAhpWeightConvert", searchMap);

	        if(null != itemIds && 0 < itemIds.length) {
		        for (int i = 0; i < itemIds.length; i++) {
		            searchMap.put("itemId", itemIds[i]);
		            searchMap.put("adjustWeight", adjustWeights[i]);
		            returnMap = updateData("bsc.ahp.ahpResult.updateData", searchMap, true);
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


        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;
    }

    /**
     * AHP 평가결과 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap ahpResultListExcel(SearchMap searchMap) {
    	String excelFileName = "AHP 평가결과";
    	String excelTitle = "AHP 평가결과 리스트";

    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();

    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	//excelSearchInfoList.add(new ExcelVO("공통코드명", StaticUtil.nullToDefault((String)searchMap.get("codeGrpNm"), "전체")));

    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가년도", "YEAR", "left"));
    	excelInfoList.add(new ExcelVO("평가단 코드", "EVAL_USER_GRP_ID", "left"));
    	excelInfoList.add(new ExcelVO("평가단 명", "EVAL_USER_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가대상 그룹 명", "ITEM_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("총가중치", "TOT_WEIGHT", "left"));
    	excelInfoList.add(new ExcelVO("평가 마감 여부", "EVAL_CLOSING_YN", "left"));
    	excelInfoList.add(new ExcelVO("평가대상 수", "ITEM_CNT", "left"));
    	excelInfoList.add(new ExcelVO("TOT_CNT", "TOT_CNT", "left"));
    	excelInfoList.add(new ExcelVO("EVAL_CNT", "EVAL_CNT", "left"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.ahp.ahpResult.getList", searchMap));

        return searchMap;
    }

    /**
     * AHP 평가결과상세화면 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap ahpResultModifyExcel(SearchMap searchMap) {
    	String excelFileName = "AHP 평가결과";
    	String excelTitle = "AHP 평가결과 상세리스트";

    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();

    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	//excelSearchInfoList.add(new ExcelVO("공통코드명", StaticUtil.nullToDefault((String)searchMap.get("codeGrpNm"), "전체")));

    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가년도", "YEAR", "left"));
    	excelInfoList.add(new ExcelVO("평가대상", "ITEM_NM", "left"));
    	excelInfoList.add(new ExcelVO("AHP계수", "AHP_CORFFI", "left"));
    	excelInfoList.add(new ExcelVO("환산가중치", "CONVERT_WEIGHT", "left"));
    	excelInfoList.add(new ExcelVO("조정가중치", "ADJUST_WEIGHT", "left"));
    	excelInfoList.add(new ExcelVO("조정가중반영치", "ADJUST_REFL_WEIGHT", "left"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("bsc.ahp.ahpResult.getDetailList", searchMap));

        return searchMap;
    }

}
