/*************************************************************************
* CLASS 명      : OrgEvalItemAction
* 작 업 자      : 박선혜
* 작 업 일      : 2013년 6월 10일
* 기    능      : 세부평가항목
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    박선혜      2013년 6월 10일             최 초 작 업
**************************************************************************/
package com.lexken.prs.org;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.StringConstants;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.util.HtmlHelper;
import com.lexken.framework.util.StaticUtil;
import com.lexken.framework.login.LoginVO;

public class OrgEvalItemAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 세부평가항목 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap orgEvalItemList(SearchMap searchMap) {

        return searchMap;
    }


    /**
     * 세부평가항목 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap orgEvalItemList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("prs.org.orgEvalItem.getList", searchMap));

        return searchMap;
    }

    /**
     * 세부평가항목 상세화면
     * @param
     * @return String
     * @throws
     */
    public SearchMap orgEvalItemModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");

    	/**********************************
         * 로그인정보 (권한 체크)
         **********************************/
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");

    	HashMap detail = new HashMap();

    	if("MOD".equals(stMode)) {
	    	/**********************************
	         * 세부평가항목 상세조회
	         **********************************/
	    	detail = getDetail("prs.org.orgEvalItem.getDetail", searchMap);
	        searchMap.addList("detail", detail);
    	}

        /**********************************
         * 산식컬럼 조회
         **********************************/
        ArrayList calTypeColList = new ArrayList();
        calTypeColList = (ArrayList)getList("prs.org.orgEvalItem.calTypeColList", searchMap);
        searchMap.addList("calTypeColList", calTypeColList);

        /**********************************
         * 득점산식조회
         **********************************/
        searchMap.addList("scoreCalTypeList", getList("prs.org.orgEvalItem.scoreCalTypeList", searchMap));

        /**********************************
         * 평가구간대 조회
         **********************************/
        List evalSectionList = getList("prs.org.orgEvalItem.evalSectionList", searchMap);
        searchMap.addList("evalSectionList", evalSectionList);
        if(evalSectionList.size() != 0) {
        	searchMap.put("upDownGbnCheck", ((HashMap)evalSectionList.get(0)).get("UPDOWN_GBN"));
        } else {
        	searchMap.put("upDownGbnCheck", "03");
        }



        /**********************************
         * 평가구간대 등급 조회
         **********************************/
        searchMap.addList("gradeList", getList("prs.org.orgEvalItem.gradeList", searchMap));

        if("MOD".equals(stMode)) {

	        String actCalTypeNm = (String)detail.get("ACT_CAL_TYPE");
	        HashMap<String, String> calTyepColValueMap = new HashMap<String, String>();

	        if(null != calTypeColList && 0 < calTypeColList.size()) {
		        for (int i = 0; i < calTypeColList.size(); i++) {
		        	HashMap<String, String> t = (HashMap<String, String>)calTypeColList.get(i);
					calTyepColValueMap.put((String)t.get("CAL_TYPE_COL"), (String)t.get("CAL_TYPE_COL_NM"));
				}
	        }

	        String calTypeColDesc = (String)HtmlHelper.changeCalNmToCalDesc(actCalTypeNm, calTyepColValueMap);
	        searchMap.addList("calTypeColDesc", calTypeColDesc);
        }

        return searchMap;
    }

    /**
     * 세부평가항목 등록/수정/삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap orgEvalItemProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");

        /**********************************
         * 무결성 체크
         **********************************/
        if("ADD".equals(stMode) || "MOD".equals(stMode) ) {
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
        } else if("DISTRIBUTE".equals(stMode)) {
            searchMap = distributeDB(searchMap);
        } else if("DELETE".equals(stMode)) {
            searchMap = deletesDB(searchMap);
        }else if("GET".equals(stMode)) {
        	searchMap = getInserDB(searchMap);
        }

        /**********************************
         * Return
         **********************************/
        return searchMap;
    }

    /**
     * 전년도데이터복사
     * @param
     * @return String
     * @throws
     */
    public SearchMap getInserDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
	        setStartTransaction();

	        returnMap = deleteData("prs.org.orgEvalItem.DeleteLastYearDetailData", searchMap, true);	//세부평가항목삭제
	        returnMap = deleteData("prs.org.orgEvalItem.DeleteLastYearUseData", searchMap, true);		//사용개수의 세부평가항목삭제
	        returnMap = deleteData("prs.org.orgEvalItem.DeleteLastYearUseCalData", searchMap, true);	//사용개수의 세부평가항목 산식 삭제
	        returnMap = deleteData("prs.org.orgEvalItem.DeleteLastYearCalData", searchMap, true);		//세부평가항목 산식 삭제
	        returnMap = deleteData("prs.org.orgEvalItem.DeleteLastYearSectionData", searchMap, true);	//세부평가항목 구간 삭제

	        returnMap = insertData("prs.org.orgEvalItem.InsertLastYearDetailData", searchMap);			//전년도세부평가항목 입력
	        returnMap = insertData("prs.org.orgEvalItem.InsertLastYearCalData", searchMap);				//전년도세부평가항목 산식 입력
	        returnMap = insertData("prs.org.orgEvalItem.InsertLastYearSectionData", searchMap);			//전년도세부평가항목 구간 입력

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
     * 세부평가항목 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();
        String kpiPoolGubun = "MOD";


        try {
        	setStartTransaction();

        	/**********************************
             * 세부평가항목코드 채번
             **********************************/
	        String orgEvalItemId = getStr("prs.org.orgEvalItem.getOrgEvalItemId", searchMap);
	        searchMap.put("orgEvalItemId", orgEvalItemId);

	        /**********************************
             * 세부평가항목 입력
             **********************************/
        	returnMap = insertData("prs.org.orgEvalItem.insertData", searchMap);

        	/**********************************
             * 실적산식 등록
             **********************************/
	        returnMap = insertCalTypeCol(searchMap);

	        /**********************************
             * 구간대 등록
             **********************************/
	        returnMap = insertEvalSection(searchMap);

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
     * 세부평가항목 수정
     * @param
     * @return String
     * @throws
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        try {
	        setStartTransaction();

	        /**********************************
	         * 세부평가항목 수정
	         **********************************/
	        returnMap = updateData("prs.org.orgEvalItem.updateData", searchMap);

	        /**********************************
	         * 실적산식 등록
	         **********************************/
	        returnMap = insertCalTypeCol(searchMap);

	        /**********************************
	         * 구간대 등록
	         **********************************/
	        returnMap = insertEvalSection(searchMap);

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
    * 세부평가항목 삭제
    * @param
    * @return String
    * @throws
    */
   public SearchMap deleteDB(SearchMap searchMap) {
       HashMap returnMap = new HashMap();

       try {
	        String orgEvalItemIds = searchMap.getString("orgEvalItemIds");
	        String[] keyArray = orgEvalItemIds.split("\\|", 0);

	        setStartTransaction();

	        if(null != keyArray && 0 < keyArray.length) {
		        for (int i=0; i<keyArray.length; i++) {
		            searchMap.put("orgEvalItemId", keyArray[i]);
		            returnMap = updateData("prs.org.orgEvalItem.deleteData", searchMap);

		        	/**********************************
		             * 실적산식 삭제
		             **********************************/
			        returnMap = updateData("prs.org.orgEvalItem.updateCalTypeCol", searchMap, true);

			        /**********************************
		             * 구간대 삭제
		             **********************************/
			        returnMap = updateData("prs.org.orgEvalItem.updateEvalSection", searchMap, true);

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
    * 조직별 세부평가항목 삭제
    * @param
    * @return String
    * @throws
    */
   public SearchMap deletesDB(SearchMap searchMap) {
	   HashMap returnMap = new HashMap();

	   try {
		   String orgEvalItemIds = searchMap.getString("orgEvalItemIds");
		   String deptCds = searchMap.getString("deptCds");
		   String[] orgEvalItemId = orgEvalItemIds.split("\\|", 0);
		   String[] deptCd = deptCds.split("\\|", 0);

		   setStartTransaction();

		   if(null != orgEvalItemId && 0 < orgEvalItemId.length) {
			   for (int i=0; i<orgEvalItemId.length; i++) {
				   searchMap.put("orgEvalItemId", orgEvalItemId[i]);
				   searchMap.put("deptCd", deptCd[i]);

				   /**********************************
				    * 조직별 세부평가 삭제
				    **********************************/
				   returnMap = deleteData("prs.org.orgEvalItem.deletesData", searchMap);

				   /**********************************
				    * 조직별 실적산식 삭제
				    **********************************/
				   returnMap = deleteData("prs.org.orgEvalItem.deleteDeptColAct", searchMap, true);

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
    * 세부평가항목 사용 조직 팝업
    * @param
    * @return String
    * @throws
    */
   public SearchMap popEvalItemCopy(SearchMap searchMap) {

   	searchMap.addList("treeList", getList("prs.org.orgEvalItem.getDeptList1", searchMap));

   	searchMap.addList("useDeptList", getList("prs.org.orgEvalItem.getUseDeptList", searchMap));

       return searchMap;
   }


   /**
    * 세부평가항목배포 기능
    * @param
    * @return String
    * @throws
    */
   public SearchMap distributeDB(SearchMap searchMap) {
       HashMap returnMap = new HashMap();

       try {
	        String[] deptIds = searchMap.getString("deptIds").split("\\|", 0);

	        setStartTransaction();

        	/**********************************
             * 조직별 세부항목삭제
             **********************************/
	        returnMap = deleteData("prs.org.orgEvalItem.deleteDeptEvalItem", searchMap, true);

	        /**********************************
             * 조직별 산식실적  삭제
             **********************************/
	        returnMap = deleteData("prs.org.orgEvalItem.deleteDeptEvalItemColAct", searchMap, true);

	        for (int i = 0; i < deptIds.length; i++) {
	            searchMap.put("deptId", deptIds[i]);
	            returnMap = insertData("prs.org.orgEvalItem.distributeData", searchMap);
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
     * 실적산식 등록
     * @param
     * @return String
     * @throws
     */
    public HashMap insertCalTypeCol(SearchMap searchMap) {
        HashMap returnMap = new HashMap();

        /**********************************
         * Parameter setting
         **********************************/
        String evalType = searchMap.getString("evalType");

        String[] calTypeCols = searchMap.getStringArray("calTypeCols");
        String[] calTypeColNms = searchMap.getStringArray("calTypeColNms");
        String[] insertGubuns = searchMap.getStringArray("insertGubuns");
        String[] units = searchMap.getStringArray("units");
        String[] itemCds = searchMap.getStringArray("itemCds");
        String[] sourceSystems = searchMap.getStringArray("sourceSystems");

        try {
        	/**********************************
             * 실적산식 삭제
             **********************************/
	        returnMap = updateData("prs.org.orgEvalItem.deleteCalTypeCol", searchMap, true);

	        /**********************************
             * 실적산식 등록
             **********************************/
        	if(null != calTypeCols && 0 < calTypeCols.length) {
		        for (int i = 0; i < calTypeCols.length; i++) {
		            searchMap.put("calTypeCol", calTypeCols[i]);
		            searchMap.put("calTypeColNm", calTypeColNms[i]);
		            searchMap.put("insertGubun", insertGubuns[i]);
		            searchMap.put("unit", units[i]);
		            searchMap.put("itemCd", itemCds[i]);
		            searchMap.put("sourceSystem", sourceSystems[i]);
		            returnMap = insertData("prs.org.orgEvalItem.insertCalTypeCol", searchMap);
		        }
        	}

        } catch (Exception e) {
        	logger.error(e.toString());
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        }
        return returnMap;
    }

    /**
     * 구간대등록
     * @param
     * @return String
     * @throws
     */
    public HashMap insertEvalSection(SearchMap searchMap) {
        HashMap returnMap = new HashMap();

        /**********************************
         * Parameter setting
         **********************************/
        String evalType = searchMap.getString("evalType");
        String scoreCalTypeGubun = searchMap.getString("scoreCalTypeGubun");

        /**********************************
         * 구간대
         **********************************/
        String[] evalSectionIds = searchMap.getStringArray("evalSectionIds");
        String[] fromValues = searchMap.getStringArray("fromValues");
        String[] toValues = searchMap.getStringArray("toValues");
        String[] conversionScores = searchMap.getStringArray("conversionScores");

        try {
        	/**********************************
             * 구간대 삭제
             **********************************/
	        returnMap = updateData("prs.org.orgEvalItem.deleteEvalSection", searchMap, true);

	        /**********************************
	         * 구간대 등록
	         **********************************/
    		if(null != evalSectionIds && 0 < evalSectionIds.length) {
		        for (int i = 0; i < evalSectionIds.length; i++) {
		            searchMap.put("evalSectionId", evalSectionIds[i]);
		            searchMap.put("fromValue", fromValues[i]);
		            searchMap.put("toValue", toValues[i]);
		            searchMap.put("conversionScore", conversionScores[i]);
		            returnMap = insertData("prs.org.orgEvalItem.insertEvalSection", searchMap);
		        }
    		}
        } catch (Exception e) {
        	logger.error(e.toString());
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        }
        return returnMap;
    }

    /**
     *  Validation 체크(무결성 체크)
     * @param SearchMap
     * @return HashMap
     */
    private HashMap validChk(SearchMap searchMap) {
        HashMap returnMap         = new HashMap();
        int     resultValue        = 0;
        String[] weights = null;
        float weightsSum = 0;

        {

	        returnMap = ValidationChk.lengthCheck(searchMap.getString("orgEvalItemNm"), "세부평가항목명", 1, 100);
	        if((Integer)returnMap.get("ErrorNumber") < 0 ){
	        	return returnMap;
	        }

	        returnMap = ValidationChk.lengthCheck(searchMap.getString("content"), "설명", 0, 1500);
	        if((Integer)returnMap.get("ErrorNumber") < 0 ){
	        	return returnMap;
	        }

        	returnMap = ValidationChk.lengthCheck(searchMap.getString("actCalType"), "실적산식", 1, 500);
            if((Integer)returnMap.get("ErrorNumber") < 0 ){
            	return returnMap;
            }

	        /**********************************
	         * 실적산식 Parameter setting
	         **********************************/
	        String[] calTypeCols   = searchMap.getStringArray("calTypeCols");
	        String[] calTypeColNms = searchMap.getStringArray("calTypeColNms");
	        String[] insertGubuns  = searchMap.getStringArray("insertGubuns");
	        String[] units		   = searchMap.getStringArray("units");
	        String[] itemCds 	   = searchMap.getStringArray("itemCds");
	        String[] sourceSystems = searchMap.getStringArray("sourceSystems");

    		for( int i = 0; i<calTypeCols.length; i++) {
    			returnMap = ValidationChk.lengthCheck(calTypeColNms[i], "산식항목명", 1, 300);
                if((Integer)returnMap.get("ErrorNumber") < 0 ){
                	return returnMap;
                }

                returnMap = ValidationChk.lengthCheck(insertGubuns[i], "실적입력구분", 1, 300);
                if((Integer)returnMap.get("ErrorNumber") < 0 ){
                	return returnMap;
                }

                returnMap = ValidationChk.lengthCheck(units[i], "단위", 1, 7);
                if((Integer)returnMap.get("ErrorNumber") < 0 ){
                	return returnMap;
                }
/*
    	        if("01".equals(searchMap.getString("insertGubuns")) || "07".equals(searchMap.getString("insertGubuns"))){
    	        	returnMap = ValidationChk.selEmptyCheck(searchMap.getString("itemCd"), "연계항목");
    	            if((Integer)returnMap.get("ErrorNumber") < 0 ){
    	            	returnMap.put("ErrorNumber",  (Integer)returnMap.get("ErrorNumber"));
    	        		returnMap.put("ErrorMessage", "실적입력구분이 시스템 또는 엑셀일 경우에는 연계항목을 입력해야 합니다.");

    	            	return returnMap;
    	            }
    	        }
*/
                returnMap = ValidationChk.lengthCheck(itemCds[i], "연계항목", 0, 8);
                if((Integer)returnMap.get("ErrorNumber") < 0 ){
                	return returnMap;
                }

                returnMap = ValidationChk.lengthCheck(sourceSystems[i], "원천데이터", 0, 200);
                if((Integer)returnMap.get("ErrorNumber") < 0 ){
                	return returnMap;
                }

    		}

        }
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;
    }

    /**
     * 세부평가항목 사용중인 세부평가항목 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap orgEvalItemUseList(SearchMap searchMap) {

    	/************************************************************************************
    	 * 디폴트 세부평가항목 조회
    	 ************************************************************************************/
    	searchMap.put("findOrgEvalItemId", searchMap.getString("orgEvalItemId"));

        return searchMap;
    }

    /**
     * 세부평가항목 사용중인 세부평가항목 조회 json 데이터 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap orgEvalItemUseList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("prs.org.orgEvalItem.getUseList", searchMap));

        return searchMap;
    }

    /**
     * 세부평가항목관리 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap orgEvalItemListExcel(SearchMap searchMap) {
    	String excelFileName = StringConstants.ORG_EVAL_ITEM_NM;
    	String excelTitle = StringConstants.ORG_EVAL_ITEM_NM + "리스트";

    	if("".equals(StaticUtil.nullToBlank((String)searchMap.get("findYear")))) {
			searchMap.put("findYear", (String)searchMap.get("year"));
		}
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.get("findOrgEvalItemNm")))) {
    		searchMap.put("findOrgEvalItemNm", (String)searchMap.get("orgEvalItemNm"));
    	}

    	if("".equals(StaticUtil.nullToBlank((String)searchMap.get("findUseYn")))) {
			searchMap.put("findUseYn", (String)searchMap.get("useYn"));
		}

    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO(StringConstants.ORG_EVAL_ITEM_NM +"명", StaticUtil.nullToDefault((String)searchMap.get("orgEvalItemNm"), "전체")));
    	excelSearchInfoList.add(new ExcelVO("사용여부", (String)searchMap.get("useYnNm")));

    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO(StringConstants.ORG_EVAL_ITEM_NM + "코드", "ORG_EVAL_ITEM_ID", "center", "CNT", 6000));
    	excelInfoList.add(new ExcelVO("평가분야", "EVAL_TYPE_NM", "center", "CNT"));
    	excelInfoList.add(new ExcelVO(StringConstants.ORG_EVAL_ITEM_NM + "명", "ORG_EVAL_ITEM_NM", "center", "CNT", 8500));
    	excelInfoList.add(new ExcelVO("설명", "CONTENT", "left", "CNT"));
    	excelInfoList.add(new ExcelVO("사용개수", "COUNT", "left", "CNT"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("prs.org.orgEvalItem.getExcelList", searchMap));

        return searchMap;
    }


}
