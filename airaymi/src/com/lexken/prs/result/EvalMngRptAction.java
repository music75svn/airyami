/*************************************************************************
* CLASS 명      : EvalMngRptAction
* 작 업 자      : 박선혜
* 작 업 일      : 2013년 06월 18일
* 기    능      : 간부개인성과 평가결과 통지서
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    박선혜      2013년 06월 18일    최 초 작 업
**************************************************************************/
package com.lexken.prs.result;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.SearchMap;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginVO;
import com.lexken.framework.util.StaticUtil;
import com.lexken.prs.mng.EvalGrpAssessorAction;

public class EvalMngRptAction extends CommonService {
	private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    private final EvalGrpAssessorAction action = new EvalGrpAssessorAction();

    /**
     * 평가대상자 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap evalMngRptList(SearchMap searchMap) {

    	// 최상위 평가조직 조회
    	HashMap topScDept = getDetail("prs.mng.evalMng.getTopDeptInfo", searchMap);
    	
    	// 파라미터가 null로 들어올 때 디폴트값 셋팅.
    	if (topScDept == null) {
    		topScDept = new HashMap();
    		topScDept.put("DEPT_CD", "");
    		topScDept.put("DEPT_KOR_NM", "");
    	}

    	// 파라미터가 null로 들어올 때 디폴트값 셋팅.
    	String findDeptCd =  StaticUtil.nullToDefault(searchMap.getString("findDeptCd"), (String)topScDept.get("DEPT_CD"));	// 조직코드가 없으면 전사조직코드를 셋팅.
    	String findUpDeptName =  StaticUtil.nullToDefault(searchMap.getString("findUpDeptName"), (String)topScDept.get("DEPT_KOR_NM")) ; ;	// 조직명이 없으면 전사조직명을 셋팅.

    	// 디폴트 조회조건 설정
    	searchMap.put("findDeptCd", findDeptCd);	// 검색버튼 조회시 조직트리에 기존 선택한 조직이 표시되도록 설정.(findSearchCodeId에 넣어주면 우선 적용됨. 트리와 검색조건을 별도로 사용할 경우에 한함.)
    	searchMap.put("findUpDeptName", findUpDeptName);

    	searchMap.addList("objectionSchedule", getDetail("prs.result.evalMngRpt.getObjectionSchedule", searchMap)); 
    	
    	searchMap.addList("deptTree", getList("prs.mng.evalMng.getDeptList", searchMap)); //인사조직
    	
    	searchMap.put("findEvalGrpType", "02");
    	searchMap.addList("evalGrpList", getList("bsc.module.commModule.getEvalGrpList", searchMap));

    	return searchMap;
    }

    /**
     * 평가결과 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap evalMngRptList_xml(SearchMap searchMap) {
    	/**********************************
    	 * 로그인정보 (권한 체크)
    	 **********************************/
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	String flag = "";
    	String insaFlag = "";
    	
    	if( loginVO.chkAuthGrp("60") == true ) {
			searchMap.put("flag", "1");
		}else if( loginVO.chkAuthGrp("84") == true ) {
			searchMap.put("flag", "2");
		}else {
			searchMap.put("flag", "");
		}

    	searchMap.put("empn", searchMap.getString("userId", loginVO.getUser_id()));
        searchMap.addList("list", getList("prs.result.evalMngRpt.getList", searchMap));

        return searchMap;
    }

    /**
     * 평가결과 상세화면
     * @param
     * @return String
     * @throws
     */
    public SearchMap evalMngRptModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");

    	/**********************************
         * 로그인정보 (권한 체크)
         **********************************/
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	String flag = "";
    	String insaFlag = "";
    	
    	if( loginVO.chkAuthGrp("60") ) {
			searchMap.put("flag", "1");
		}else if( loginVO.chkAuthGrp("84") ) {
			searchMap.put("flag", "2");
		}else {
			searchMap.put("flag", "");
		}
    	
    	String userId = searchMap.getString("userId", loginVO.getUser_id());
    	String empn = getStr("prs.result.evalMngRpt.getInsaLdId", searchMap);
    	
    	/*로그인한 사용자가 인사부장인지 체크
    	if(userId.equals(empn)){
    		searchMap.put("insaFlag", "1");
    		searchMap.put("insaEmpn", empn);
    	}else {
    		searchMap.put("insaFlag", "");
    	}
		*/
    	
    	searchMap.put("insaEmpn", searchMap.getString("userId", loginVO.getUser_id()));
    	/**********************************
    	 * 평가결과 상세조회
    	 **********************************/
    	searchMap.addList("basic", getDetail("prs.result.evalMngRpt.getDetail", searchMap));

    	if("RPT".equals(stMode)) {
	        searchMap.addList("detail", getList("prs.result.evalMngRpt.getDetailList", searchMap));
	        searchMap.addList("total", getDetail("prs.result.evalMngRpt.getTotalDetail", searchMap));
    	}

    	searchMap.addList("objectionSchedule", getDetail("prs.result.evalMngRpt.getObjectionSchedule", searchMap)); 

        return searchMap;
    }

    /**
     * 평가결과 이의제기 신청서
     * @param
     * @return String
     * @throws
     */
    public SearchMap evalMngObjectionReg(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");

    	/**********************************
         * 로그인정보 (권한 체크)
         **********************************/
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
    	searchMap.put("chkUserId", loginVO.getUser_id());

    	/**********************************
    	 * 평가결과 상세조회
    	 **********************************/
    	searchMap.addList("basic", getDetail("prs.result.evalMngRpt.getDetail", searchMap));
    	searchMap.addList("objList", getList("prs.result.evalMngRpt.getObjList", searchMap));

    	if("REG".equals(stMode)){
    		searchMap.addList("detail",	getDetail("prs.result.evalMngRpt.getObjDetail", searchMap));
        	/**********************************
             * 이의제기항목 조회
             **********************************/
            ArrayList regObjList = new ArrayList();
            regObjList = (ArrayList)getList("prs.result.evalMngRpt.getObjDetailList", searchMap);
            String[] objArray = new String[0];
            if(null != regObjList && 0 < regObjList.size()) {
            	objArray = new String[regObjList.size()];
            	for (int i = 0; i < regObjList.size(); i++) {
    	        	HashMap<String, String> t = (HashMap<String, String>)regObjList.get(i);
    	        	objArray[i] = t.get("OBJ_TYPE");
            	}
            }

            searchMap.addList("detailList", objArray);
    	}
    	
    	searchMap.addList("objectionSchedule", getDetail("prs.result.evalMngRpt.getObjectionSchedule", searchMap)); 
    	
        return searchMap;
    }

    /**
     * 이의제기 신청서 등록/수정
     * @param
     * @return String
     * @throws
     */
    public SearchMap evalMngObjextionRegProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");

        /**********************************
         * 등록/삭제
         **********************************/
        if("REG".equals(stMode)) {
            searchMap = insertRegDB(searchMap);
        }else if("ANS".equals(stMode)) {
            searchMap = updateAnsDB(searchMap);
        }else if("SUCCESS".equals(stMode)) {
        	searchMap = updateAnsSuccessDB(searchMap);
        }

        /**********************************
         * Return
         **********************************/
        return searchMap;
    }

    /**
     * 이의제기 신청서 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap insertRegDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();
        setStartTransaction();

        String[] objTypes = searchMap.getString("objTypes").split("\\|", 0);
	    returnMap = deleteData("prs.result.evalMngRpt.deleteRegObjType", searchMap, true);

        if(objTypes!=null && !objTypes.equals("")){
	        for (int i = 0; i < objTypes.length; i++) {
	        	searchMap.put("objType", objTypes[i]);
	        	returnMap = insertData("prs.result.evalMngRpt.insertRegObjType", searchMap);
	        }
        }
        returnMap = insertData("prs.result.evalMngRpt.insertRegData", searchMap);
        setEndTransaction();

        /**********************************
         * Return
         **********************************/
        searchMap.addList("returnMap", returnMap);
        return searchMap;
    }

    /**
     * 이의제기 답변서 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap updateAnsDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();
        setStartTransaction();
        
        String [] objTypes = searchMap.getStringArray("objType");
        String [] objTypeAnswers = searchMap.getStringArray("objTypeAnswer");
        
        if(objTypes!=null && !objTypes.equals("")){
	        for (int i = 0; i < objTypes.length; i++) {
	        	searchMap.put("objType", objTypes[i]);
	        	searchMap.put("objTypeAnswer", objTypeAnswers[i]);
	        	returnMap = updateData("prs.result.evalMngRpt.updateAnsData", searchMap);
	        	returnMap = updateData("prs.result.evalMngRpt.updateObjAnswerData", searchMap);
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
     * 이의제기 답변일자 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap updateAnsSuccessDB(SearchMap searchMap) {
    	/**********************************
    	 * 로그인정보 (권한 체크)
    	 **********************************/
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	String flag = "";
    	
    	if( loginVO.chkAuthGrp("60") ) {
			searchMap.put("flag", "1");
		}else if( loginVO.chkAuthGrp("84") ) {
			searchMap.put("flag", "2");
		}else {
			searchMap.put("flag", "");
		}
    	
    	HashMap returnMap    = new HashMap();
    	setStartTransaction();
    	
    	String [] objTypes = searchMap.getStringArray("objType");
    	String [] objTypeAnswers = searchMap.getStringArray("objTypeAnswer");
    	
    	if(objTypes!=null && !objTypes.equals("")){
    		for (int i = 0; i < objTypes.length; i++) {
    			searchMap.put("objType", objTypes[i]);
    			returnMap = updateData("prs.result.evalMngRpt.updateAnsSuccessData", searchMap);
    			returnMap = updateData("prs.result.evalMngRpt.updateObjAnswerSuccessData", searchMap);
    			
    			searchMap.put("objTypeAnswer", objTypeAnswers[i]);
	        	returnMap = updateData("prs.result.evalMngRpt.updateAnsData", searchMap);
	        	returnMap = updateData("prs.result.evalMngRpt.updateObjAnswerData", searchMap);
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
     * 평가결과 상세화면
     * @param
     * @return String
     * @throws
     */
    public SearchMap evalMngObjectionAns(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");

    	/**********************************
    	 * 로그인정보 (권한 체크)
    	 **********************************/
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	String flag = "";

    	/**********************************
    	 * 평가결과 상세조회
    	 **********************************/
    	searchMap.addList("basic", getDetail("prs.result.evalMngRpt.getDetail", searchMap));
    	searchMap.addList("objList", getList("prs.result.evalMngRpt.getObjList", searchMap));

    	if("ANS".equals(stMode)){
    		if( loginVO.chkAuthGrp("60") ) {
    			searchMap.put("flag", "1");
    		}else if( loginVO.chkAuthGrp("84") ) {
    			searchMap.put("flag", "2");
    		}else {
    			searchMap.put("flag", "");
    		}
    		searchMap.addList("objSum",	getDetail("prs.result.evalMngRpt.getObjSumDetail", searchMap));
    		searchMap.addList("detail",	getList("prs.result.evalMngRpt.getObjDetail", searchMap));
    		searchMap.addList("detailList",	getList("prs.result.evalMngRpt.getObjDetailList", searchMap));
    		int cnt = getInt("prs.result.evalMngRpt.getObjCount", searchMap);
    		
    		searchMap.put("findCnt", cnt);
    	}
    	return searchMap;
    }

}
