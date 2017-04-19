/*************************************************************************
* CLASS 명      : TargetMngAction
* 작 업 자      : 박경태
* 작 업 일      : 2012년 10월 30일
* 기    능      : 목표입력
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    박경태      2012년 10월 30일         최 초 작 업
**************************************************************************/
package com.lexken.man.tam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.Iterator;


import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.FileInfoVO;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.struts2.IsBoxActionSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.login.LoginVO;
import com.lexken.framework.util.HtmlHelper;
import com.lexken.framework.util.StaticUtil;

public class TargetMngAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 목표입력 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap targetMngList(SearchMap searchMap) {
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");

    	String actualType = searchMap.getString("findActualType");

    	String get_flag	 = searchMap.getString("flag");
    	String findTargetStatusId = searchMap.getString("findTargetStatusId");

    	String findUserId = searchMap.getString("findUserId");
    	
    	if( get_flag == "" && findTargetStatusId == "" ) {
    		searchMap.put("flag","1");
    		
    	}else if( "1".equals(get_flag) && "ALLSTATUS".equals(findTargetStatusId) ) {
    		searchMap.put("findTargetStatusId","01");
    	}

    	if( get_flag == "" ) {
    		searchMap.put("flag","1");
    	}
    	
	    if(!loginVO.chkAuthGrp("01") || !loginVO.chkAuthGrp("60")) {

	    	if(actualType.equals("input")) {

	    		if("".equals(findUserId)){

	    			if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("findTargetStatusId")))) {
	    				searchMap.put("findTargetStatusId", "01");
	    			}

	    		}

			} else if (actualType.equals("check")) {

				if("".equals(findUserId)) {

					if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("findTargetStatusId")))) {
						searchMap.put("findTargetStatusId", "06");
						
					}

				}

			} else if (actualType.equals("approve")) {

				if("".equals(findUserId)) {

					if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("findTargetStatusId")))) {
						searchMap.put("findTargetStatusId", "03");
						searchMap.put("findUserId","ALL");
					}

				}
			}

	    }
	    
    	/**********************************
         * 목표 입력, 승인자 조회
         **********************************/
    	searchMap.addList("userList", getList("man.tam.targetMng.getUserList", searchMap));

    	/**********************************
         * 목표입력기한 여부 가져오기
         **********************************/
        searchMap.addList("targetInputTermYn", getStr("man.tam.targetMng.getTargetInputTermYn", searchMap));

        /**********************************
         * 년마감 여부 가져오기
         **********************************/
        searchMap.addList("yearClosingYn", getStr("man.system.closingYear.getYearClosingYn", searchMap));

        /**********************************
         * 권한별 처리
         **********************************/
    	if(loginVO.chkAuthGrp("01") || loginVO.chkAuthGrp("60")) {
    		/*if("".equals(StaticUtil.nullToBlank((String)searchMap.get("findUserId")))) {
    			searchMap.put("findUserId", searchMap.getDefaultValue("userList", "USER_ID", 0));
    		}*/
    	} else {
    		searchMap.put("findUserId", searchMap.get("loginUserId"));
    	}

        /************************************
         * 입력대상 세부과제 지표조회
         ************************************/
    	searchMap.put("paramGubun", "1");
		searchMap.addList("mngTreeList1", getList("man.tam.targetMng.getMngTargetTree", searchMap));
		searchMap.put("paramGubun", "2");
		searchMap.addList("mngTreeList2", getList("man.tam.targetMng.getMngTargetTree", searchMap));
		searchMap.put("paramGubun", "3");
        searchMap.addList("mngTreeList3", getList("man.tam.targetMng.getMngTargetTree", searchMap));
        searchMap.put("paramGubun", "4");
        searchMap.addList("mngTreeList4", getList("man.tam.targetMng.getMngTargetTree", searchMap));

        /**********************************
         * 디폴트 입력 지표 조회
         **********************************/
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("csfId")))) {
    		searchMap.put("csfId", (String)searchMap.getString("findCsfId"));
    	}

    	if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("csfId")))) {
    		searchMap.put("csfId", searchMap.getDefaultValue("mngTreeList4", "CSF_ID", 0));
    	}

        return searchMap;
    }


    /**
     * 목표 입력, 승인자 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap userList_ajax(SearchMap searchMap) {

    	/**********************************
         * 목표 입력, 승인자 조회
         **********************************/
    	searchMap.addList("userList", getList("man.tam.targetMng.getUserList", searchMap));

        return searchMap;
    }


    /**
     * 목표관리 상세데이터 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap targetMngDetailList(SearchMap searchMap) {

    	String csfId = searchMap.getString("csfId");
    	String kpiId = searchMap.getString("kpiId");

    	if(!"".equals(csfId) || !"".equals(kpiId)) {
	    	/**********************************
	         * 월별목표조회
	         **********************************/
	        searchMap.addList("list", getList("man.tam.targetMng.getList", searchMap));
    	}
    	
    	/**********************************
         * 지표여부 여부 가져오기
         **********************************/
        searchMap.addList("getCount", getStr("man.tam.targetMng.getCount", searchMap));

        return searchMap;
    }


    /**
     * 목표입력 등록/수정/삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap targetMngProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");

        /**********************************
         * 무결성 체크
         **********************************/
        if("ADD".equals(stMode)) {
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
            searchMap = insertDB(searchMap);/*목표입력*/
            searchMap = rollUpDB(searchMap);/*기간롤업*/
        } else if("MOD".equals(stMode)) {
            //searchMap = updateDB(searchMap);
        } else if("DEL".equals(stMode)) {
            //searchMap = deleteDB(searchMap);
        } else if("STATUS".equals(stMode)) {
            searchMap = statusDB(searchMap);

            String targetStatusId = searchMap.getString("targetStatusId");
            if(targetStatusId.equals("06")||targetStatusId.equals("05")||targetStatusId.equals("03")) {
            	statusProcDB(searchMap);
            }
        }

        /**********************************
         * Return
         **********************************/
        return searchMap;
    }


    /**
     *  Validation 체크(무결성 체크)
     * @param SearchMap
     * @return HashMap
     */
    private HashMap validChk(SearchMap searchMap) {
        HashMap returnMap	= new HashMap();
        int     resultValue	= 0;
        String[] colValues	= searchMap.getStringArray("colValues");

        for(int i=0; i < colValues.length; i++) {
        	returnMap = ValidationChk.checkCommaPointNumber(colValues[i], "목표값");

    		if((Integer)returnMap.get("ErrorNumber") < 0) {
    			return returnMap;
    		}
        }

        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;
    }


    /**
     * 목표입력 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        String[] manKpiIds	= searchMap.getStringArray("manKpiIds");
        String[] mons	 	= searchMap.getStringArray("mons");
        String[] colValues 	= searchMap.getStringArray("colValues");
        String[] tgtStatusId 	= searchMap.getStringArray("tgtStatusId");

        try {
        	setStartTransaction();

        	/**********************************
             * 월별 목표입력
             **********************************/
        	if(manKpiIds != null && 0 < manKpiIds.length) {
        		for (int i = 0; i < manKpiIds.length; i++) {
	        		searchMap.put("manKpiId", manKpiIds[i]);

		            returnMap = updateData("man.tam.targetMng.deleteData", searchMap, true);
		        }

	        	for (int i = 0; i < manKpiIds.length; i++) {
	        		searchMap.put("manKpiId", manKpiIds[i]);
	        		searchMap.put("mon", mons[i]);
		            searchMap.put("target", colValues[i]);
		            searchMap.put("tgtStatusId", tgtStatusId[i]);

		            returnMap = insertData("man.tam.targetMng.insertData", searchMap);
		            returnMap = updateData("man.tam.targetMng.updateData", searchMap);
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
     * 목표입력 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap rollUpDB(SearchMap searchMap) {
        HashMap returnMap	= new HashMap();
        TreeSet tSet		= new TreeSet();

        String[] manKpiIds	= searchMap.getStringArray("manKpiIds");

        try {
        	setStartTransaction();

        	/**********************************
             * 목표 기간RollUp
             **********************************/
        	for( int i = 0; i < manKpiIds.length; i++ ) {
            	tSet.add( manKpiIds[i] ); /*중복 제거*/
            }
            Iterator it = tSet.iterator();

        	while ( it.hasNext() ) {
        		searchMap.put("manKpiId", it.next());
        		returnMap = insertData("man.tam.targetMng.callSpManTargetProc", searchMap);
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
     * 목표상태 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap statusDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();

        /**********************************
         * Parameter setting
         **********************************/
        searchMap.put("orgManKpiId", searchMap.getString("manKpiId"));
        String[] manKpiIds = searchMap.getString("manKpiId").split("\\|", 0);
        String   returnReason = searchMap.getString("returnReason");

        try {
        	setStartTransaction();

        	if(null != manKpiIds && 0 < manKpiIds.length) {
		        for (int i=0; i < manKpiIds.length; i++) {
		            searchMap.put("manKpiId", manKpiIds[i]);
		            if(returnReason.equals("")){
		            	returnMap = updateData("man.tam.targetMng.updateStatusData", searchMap, true);
		            }else{
		            	returnMap = updateData("man.tam.targetMng.updateReturnStatusData", searchMap, true);
		            }
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
     * 쪽지 보내기
     * @param
     * @return String
     * @throws
     */
    public void statusProcDB(SearchMap searchMap) {
        /**********************************
         * Parameter setting
         **********************************/
        String[] manKpiIds = searchMap.getString("orgManKpiId").split("\\|", 0);

        try {
        	setStartTransaction();

        	searchMap.put("procParam1", searchMap.getString("findYear"));
        	searchMap.put("procParam2", "MAN_TARGET");
        	searchMap.put("procParam3", searchMap.getString("targetStatusId"));

        	if(null != manKpiIds && 0 < manKpiIds.length && !"".equals(searchMap.getString("findYear")) && !"".equals(searchMap.getString("targetStatusId"))) {
		        for (int i=0; i < manKpiIds.length; i++) {
		            searchMap.put("procParam4", manKpiIds[i]);
		            insertData("bsc.message.message.insertNoteProc", searchMap, true);
		        }
        	}
        } catch (Exception e) {
        	logger.error(e.toString());
        	setRollBackTransaction();
        } finally {
        	setEndTransaction();
        }
    }


    /**
     * 반려조회 팝업
     * @param
     * @return String
     * @throws
     */
    public SearchMap popReturnView(SearchMap searchMap) {

    	/**********************************
         * 반려 내용 가져오기
         **********************************/
        searchMap.addList("returnReason", getStr("man.tam.targetMng.getReturnReason", searchMap));

    	return searchMap;
    }


    /**
     * 반려입력 팝업
     * @param
     * @return String
     * @throws
     */
    public SearchMap popReturnTarget(SearchMap searchMap) {
    	return searchMap;
    }
}
