/*************************************************************************
* CLASS 명      : CommModuleAction
* 작 업 자      : 하윤식
* 작 업 일      : 2012년 6월 20일
* 기    능      : 공통모듈
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용            비고
* ----  --------  -----------------  -------------------------    --------
*   1    하윤식      2012년 6월 20일         최 초 작 업
**************************************************************************/
package com.lexken.bsc.module;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.codeUtil.CodeUtil;
import com.lexken.framework.codeUtil.CodeUtilVO;
import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.MailSendManager;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginVO;
import com.lexken.framework.util.StaticUtil;

public class CommModuleAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());


    /**
     * 실조직 트리조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap popDeptTree(SearchMap searchMap) {

    	searchMap.addList("treeList", getList("bsc.module.commModule.getDeptList", searchMap));

        return searchMap;
    }

    /**
     * 평가조직 트리조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap popScDeptTree(SearchMap searchMap) {

    	searchMap.addList("treeList", getList("bsc.module.commModule.getScDeptList", searchMap));

        return searchMap;
    }

    /**
     * KGS인사조직 트리조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap popScDeptTree1(SearchMap searchMap) {

    	searchMap.addList("treeList", getList("bsc.module.commModule.getDeptList1", searchMap));

    	return searchMap;
    }

    /**
     * KGS실조직 트리조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap popDeptTree2(SearchMap searchMap) {

    	searchMap.addList("treeList", getList("bsc.module.commModule.getDeptList2", searchMap));

    	return searchMap;
    }

    /**
     * 직위조회 팝업
     * @param
     * @return String
     * @throws
     */
    public SearchMap popPosTcSearch(SearchMap searchMap) {

        return searchMap;
    }

    /**
     * 직위조회 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap posTcList_xml(SearchMap searchMap) {

    	searchMap.addList("list", getList("bsc.module.commModule.getPosTcList", searchMap));

        return searchMap;
    }

    /**
     * 사람찾기 팝업(v_deptinfo)기준
     * @param
     * @return String
     * @throws
     */
    public SearchMap popSearchUser(SearchMap searchMap) {

    	searchMap.addList("treeList", getList("bsc.module.commModule.getDeptList", searchMap));

        return searchMap;
    }
    
    /**
     * 사람찾기 팝업(v_deptinfo)기준
     * @param
     * @return String
     * @throws
     */
    public SearchMap popSearchDeptMetric(SearchMap searchMap) {

    	searchMap.addList("treeList", getList("bsc.module.commModule.getScDeptList", searchMap));

        return searchMap;
    }
    
    /**
     * 조직별 사용자 리스트 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap commonDeptMetricInfo_ajax(SearchMap searchMap) {

    	searchMap.addList("userList", getList("bsc.module.commModule.getDeptMetricList", searchMap));

        return searchMap;
    }
    
    /**
     * 사람찾기 팝업(v_deptinfo)기준
     * @param
     * @return String
     * @throws
     */
    public SearchMap popMemSearchUser(SearchMap searchMap) {

    	searchMap.addList("treeList", getList("bsc.module.commModule.getDeptList1", searchMap));

        return searchMap;
    }
    
    /**
     * 조직별 사용자 리스트 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap commonUserInfo_ajax(SearchMap searchMap) {

    	searchMap.addList("userList", getList("bsc.module.commModule.getUserList", searchMap));

        return searchMap;
    }
    
    /**
     * 조직별 사용자 리스트 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap commonMemUserInfo_ajax(SearchMap searchMap) {

    	searchMap.addList("userList", getList("bsc.module.commModule.getMemUserList", searchMap));

        return searchMap;
    }
    
    /**
     * 사용자정보 전체 조회(auto complete json)
     * @param
     * @return String
     * @throws
     */
    public SearchMap commonUserInfoAll_ajax(SearchMap searchMap) {

    	searchMap.addList("userInfoList", getList("bsc.module.commModule.getUserInfoAllList", searchMap));

        return searchMap;
    }


    /**
     * 지표풀 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap popMetricGrpSearch(SearchMap searchMap) {

        return searchMap;
    }
    
    /**
     * 지표풀 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap popMemMetricGrpSearch(SearchMap searchMap) {

        return searchMap;
    }

    /**
     * 지표풀 데이터 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap metricGrpList_xml(SearchMap searchMap) {

    	searchMap.addList("list", getList("bsc.module.commModule.getMetricGrpList", searchMap));

        return searchMap;
    }
    
    /**
     * 지표풀 데이터 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap memMetricGrpList_xml(SearchMap searchMap) {

    	searchMap.addList("list", getList("bsc.module.commModule.getMemMetricGrpList", searchMap));

        return searchMap;
    }

    /**
     * 지표풀 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap popMetricGrpMultiSearch(SearchMap searchMap) {

        return searchMap;
    }

    /**
     * 지표 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap popMetricSearch(SearchMap searchMap) {

        return searchMap;
    }

    /**
     * 지표 데이터 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap metricList_xml(SearchMap searchMap) {

    	String findMetricNm = (String)searchMap.get("findMetricNm");

    	if(!"".equals(findMetricNm)) {
    		searchMap.addList("list", getList("bsc.module.commModule.getMetricList", searchMap));
    	}

        return searchMap;
    }

    /**
     * KPI 지표풀 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap popKpiGrpMultiSearch(SearchMap searchMap) {

        return searchMap;
    }


    /**
     * KPI 지표풀 데이터 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap kpiGrpList_xml(SearchMap searchMap) {

    	searchMap.addList("list", getList("bsc.module.commModule.getKpiGrpList", searchMap));

        return searchMap;
    }

    /**
     * 시스템항목 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap popSystemItemSearch(SearchMap searchMap) {

        return searchMap;
    }

    /**
     * 시스템항목 데이터 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap systemItemSearch_xml(SearchMap searchMap) {

    	searchMap.addList("list", getList("bsc.module.commModule.getSystemItemList", searchMap));

        return searchMap;
    }

    /**
     * 반려사유 입력
     * @param
     * @return String
     * @throws
     */
    public SearchMap popReturnInput(SearchMap searchMap) {

        return searchMap;
    }

    /**
     * 반려사유 입력 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap popReturnView(SearchMap searchMap) {

    	searchMap.addList("list", getList("bsc.base.metric.getReturnCause", searchMap));

        return searchMap;
    }

    /**
     * 지표복사 대상 조직조회 팝업
     * @param
     * @return String
     * @throws
     */
    public SearchMap popScDeptList(SearchMap searchMap) {

        return searchMap;
    }

    /**
     * 지표복사 대상 조직조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap popScDeptList_xml(SearchMap searchMap) {

    	searchMap.addList("list", getList("bsc.base.metric.getMetricCopyDeptList", searchMap));

        return searchMap;
    }


    /**
     * 지표복사 대상 조직조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap commMetricList_ajax(SearchMap searchMap) {

    	searchMap.addList("metriclist", getList("bsc.module.commModule.getCommMetricList", searchMap));

        return searchMap;
    }



    /**
     * 전략과제 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap popStrategyList(SearchMap searchMap) {

    	/**********************************
         * 로그인정보 (권한 체크)
         **********************************/
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");

    	/************************************************************************************
    	 * 평가조직 트리 조회
    	 ************************************************************************************/
        if(loginVO.chkAuthGrp("01") || loginVO.chkAuthGrp("60")) {  //전체관리자
        	searchMap.addList("treeList", getList("bsc.module.commModule.getScDeptList", searchMap));
        }

    	/************************************************************************************
    	 * 초기 검색 조건 설정 - 성과조직정보
    	 ************************************************************************************/
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.get("findScDeptId")))) {
        	if(!"SELECT".equals(StaticUtil.nullToBlank((String)searchMap.get("findType")))) {
            	if(loginVO.chkAuthGrp("01") || loginVO.chkAuthGrp("60")) {
            		searchMap.put("findPopScDeptNm", searchMap.getDefaultValue("treeList", "CODE_NM", 0));
            	} else {
            		searchMap.put("year", searchMap.get("findYear"));
            		searchMap.put("scDeptId", loginVO.getSc_dept_id());
            		searchMap.put("findPopScDeptNm", getStr("bsc.module.commModule.getScDeptNm", searchMap));
            	}
    		}
    	} else{
    		searchMap.put("year", searchMap.get("findYear"));
    		searchMap.put("scDeptId", searchMap.get("findScDeptId"));
    		searchMap.put("findPopScDeptNm", getStr("bsc.module.commModule.getScDeptNm", searchMap));
    	}

    	/************************************************************************************
    	 * 초기 검색 조건 설정 - 전략과제
    	 ************************************************************************************/
    	if(!"".equals(StaticUtil.nullToBlank((String)searchMap.get("findStrategyId")))) {
    		searchMap.put("findPopStrategyNm", getStr("bsc.mon.strategyMatrix.getStrategyNm", searchMap));
    	}

        return searchMap;
    }



    /**
     * 전략과제 조회 그리드
     * @param
     * @return String
     * @throws
     */
    public SearchMap popStrategyList_xml(SearchMap searchMap) {

    	searchMap.addList("list", getList("bsc.module.commModule.getStrategyList", searchMap));

        return searchMap;
    }

    /**
     * 지표 데이터 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap popMetricList(SearchMap searchMap) {

    	/**********************************
         * 로그인정보 (권한 체크)
         **********************************/
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");

    	/************************************************************************************
    	 * 평가조직 트리 조회
    	 ************************************************************************************/
        if(loginVO.chkAuthGrp("01") || loginVO.chkAuthGrp("60")) {  //전체관리자
        	searchMap.addList("treeList", getList("bsc.module.commModule.getScDeptList", searchMap));
        }

    	/************************************************************************************
    	 * 초기 검색 조건 설정 - 성과조직정보
    	 ************************************************************************************/
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.get("findScDeptId")))) {
        	if(!"SELECT".equals(StaticUtil.nullToBlank((String)searchMap.get("findType")))) {
            	if(loginVO.chkAuthGrp("01") || loginVO.chkAuthGrp("60")) {
            		searchMap.put("findPopScDeptNm", searchMap.getDefaultValue("treeList", "CODE_NM", 0));
            	} else {
            		searchMap.put("year", searchMap.get("findYear"));
            		searchMap.put("scDeptId", loginVO.getSc_dept_id());
            		searchMap.put("findPopScDeptNm", getStr("bsc.module.commModule.getScDeptNm", searchMap));
            	}
    		}
    	} else{
    		searchMap.put("year", searchMap.get("findYear"));
    		searchMap.put("scDeptId", searchMap.get("findScDeptId"));
    		searchMap.put("findPopScDeptNm", getStr("bsc.module.commModule.getScDeptNm", searchMap));
    	}

    	/************************************************************************************
    	 * 초기 검색 조건 설정 - 지표
    	 ************************************************************************************/
    	if(!"".equals(StaticUtil.nullToBlank((String)searchMap.get("findMetricId")))) {
    		searchMap.put("year", searchMap.get("findYear"));
    		searchMap.put("metricId", searchMap.get("findMetricId"));
    		searchMap.put("findPopMetricNm", getStr("bsc.module.commModule.getMetricNm", searchMap));
    	}
        return searchMap;
    }

    /**
     * 지표 데이터 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap popMetricList_xml(SearchMap searchMap) {

    	String findScDeptNm = (String)searchMap.get("findPopScDeptNm");
    	String findMetricNm = (String)searchMap.get("findPopMetricNm");

    	if(!"".equals(findScDeptNm) ||!"".equals(findMetricNm)) {
    		searchMap.addList("list", getList("bsc.module.commModule.getMetricListScDeptSearch", searchMap));
    	}

        return searchMap;
    }

    /**
     * 산식입력기 팝업
     * @param
     * @return String
     * @throws
     */
    public SearchMap popCalTypeCol(SearchMap searchMap) {

    	/**********************************
         * 실적산식 함수 조회(코드141)
         **********************************/
    	searchMap.addList("funcList", getList("bsc.module.commModule.getCalTypeFuncList", searchMap));

    	/**********************************
         * 실적산식 히스토리
         **********************************/
    	searchMap.addList("historyList", getList("bsc.module.commModule.getCalTypeColList", searchMap));

        return searchMap;
    }

    /**
     * 산식입력기 검증
     * @param
     * @return String
     * @throws
     */
    public SearchMap actCalTypeConfirm(SearchMap searchMap) {

    	String actCalType = (String)searchMap.get("actCalType");
    	actCalType = actCalType.replaceAll("[|]", "+");
    	searchMap.put("actCalType", actCalType);

    	/**********************************
         * 실적산식 검증
         **********************************/
    	searchMap.addList("isOk", getQuerySuccess("bsc.module.commModule.getActCalTypeConfirm", searchMap));

        return searchMap;
    }

    /**
     * 성과조직명 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap getScDeptNm_ajax(SearchMap searchMap) {

    	/**********************************
         * 실적산식 검증
         **********************************/
    	searchMap.addList("scDeptNm", getStr("bsc.module.commModule.getScDeptNm", searchMap));

        return searchMap;
    }
    
    /**
     * 정부경영평가지표명 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap getGovMetricNm_ajax(SearchMap searchMap) {

    	/**********************************
         * 실적산식 검증
         **********************************/
    	searchMap.addList("metricNm", getStr("bsc.module.commModule.getGovMetricNm", searchMap));

        return searchMap;
    }    

    /**
     * 연계항목 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap popOrgConnItemSearch(SearchMap searchMap) {

        return searchMap;
    }

    /**
     * 연계항목 데이터 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap popOrgConnItemSearch_xml(SearchMap searchMap) {

    	searchMap.addList("list", getList("bsc.module.commModule.getOrgConnItemList", searchMap));

        return searchMap;
    }




    /**
     * 메일전송 폼 팝업
     * @param
     * @return String
     * @throws
     */
    public SearchMap popSendMail(SearchMap searchMap) {

        return searchMap;
    }

    /**
     * 메일전송 실행
     * @param
     * @return String
     * @throws
     */
    public SearchMap popSendMailProcess(SearchMap searchMap) {
    	HashMap returnMap    = new HashMap();

        MailSendManager mail = MailSendManager.getInstance();
        int send = 0;

        try {
        	String subject = searchMap.getString("subject");
        	String content = searchMap.getString("content");
        	String fromMail = searchMap.getString("fromMail");
        	String fromMailNm = searchMap.getString("fromMailNm");

        	String[] toMail = searchMap.getString("toMail").split("\\|", 0);
        	String[] toName = searchMap.getString("toName").split("\\|", 0);

        	if(null != toMail && 0 < toMail.length) {
        		for(int i = 0; i < toMail.length; i++) {
        			if(!"".equals(StaticUtil.nullToBlank(toMail[i]))) {
        				send = mail.EmailSend(toName[i], toMail[i], fromMail, fromMailNm, subject, content);

        				if(send < 0) {
        		        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
        		            returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        		            break;
        				}
        			}
		        }
        	}

        	logger.error("메일발송 결과.:" + send);
        } catch (Exception e) {
        	e.printStackTrace();
        	logger.error("메일발송 에러입니다.:" + e.toString());
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
            returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        }

        /*
        //메일발송 테스트
        try{
        	int aaa= mail.EmailSend("하윤식", "ys.ha@lexken.co.kr", "hays99@naver.com",
				                    "하윤식", "제목입니다.", "내용입니다.");
        	logger.error("메일발송 결과.:" + aaa);
        }catch (Exception e){
        	e.printStackTrace();
        	logger.error("메일발송 에러입니다.:" + e.toString());
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
            returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        }
        */

        /**********************************
         * Return
         **********************************/
        searchMap.addList("returnMap", returnMap);
        return searchMap;
    }

    /**
     * 에러 페이지 호출
     * @param
     * @return String
     * @throws
     */
    public SearchMap errorPage(SearchMap searchMap) {

        return searchMap;
    }

    /**
     * 파일첨부 에러 페이지 호출
     * @param
     * @return String
     * @throws
     */
    public SearchMap errorFilePage(SearchMap searchMap) {

        return searchMap;
    }

    /**
     * 404에러 페이지 호출
     * @param
     * @return String
     * @throws
     */
    public SearchMap notFoundPage(SearchMap searchMap) {

        return searchMap;
    }

    /**
     * context 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap getContext(SearchMap searchMap) {

        return searchMap;
    }
    
    /**
     * 코드정보 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap getCodeList_ajax(SearchMap searchMap) {
    	searchMap.addList("list", getList("bsc.base.code.getList", searchMap));
        return searchMap;
    }
    
    /**
     * 평가군(간부) 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap evalGrpList_ajax(SearchMap searchMap) {
    	searchMap.addList("list", getList("bsc.module.commModule.getEvalGrpList", searchMap));
        return searchMap;
    }

}
