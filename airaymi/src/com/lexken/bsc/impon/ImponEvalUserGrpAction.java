/*************************************************************************
 * CLASS 명      : ImponEvalUserGrpAction
 * 작 업 자      : 정철수
 * 작 업 일      : 2012년 11월 30일 
 * 기    능      : 비계량평가단
 * ---------------------------- 변 경 이 력 --------------------------------
 * 번호   작 업 자      작   업   일        변 경 내 용              비고
 * ----  ---------  -----------------  -------------------------    --------
 *   1    정철수      2012년 11월 30일         최 초 작 업 
 **************************************************************************/
package com.lexken.bsc.impon;

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

public class ImponEvalUserGrpAction extends CommonService {

	private static final long serialVersionUID = 1L;

	// Logger
	private final Log logger = LogFactory.getLog(getClass());

	/**
	 * 비계량평가단 조회
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap imponEvalUserGrpList(SearchMap searchMap) {

		return searchMap;
	}

	/**
	 * 비계량평가단 데이터 조회(xml)
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap imponEvalUserGrpList_xml(SearchMap searchMap) {

		String[][] convertArray = { { "Y", "적용" }, { "N", "미적용" } };
		searchMap.addList("CONVERT_ARRAY", convertArray);

		searchMap.addList("list", getList("bsc.impon.imponEvalUserGrp.getList",
				searchMap));

		return searchMap;
	}

	/**
	 * 비계량평가단 상세화면
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap imponEvalUserGrpModify(SearchMap searchMap) {
		String stMode = searchMap.getString("mode");

		if ("MOD".equals(stMode)) {
			searchMap.addList("detail", getDetail("bsc.impon.imponEvalUserGrp.getDetail", searchMap));
		}

		return searchMap;
	}

	/**
	 * 평가자 추가화면
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap imponEvalUserModify(SearchMap searchMap) {

		/***********************************************************************
		 * 선택 평가단 명조회
		 **********************************************************************/
		searchMap.addList("evalUserGrpDetail", getDetail("bsc.impon.imponEvalUserGrp.getDetail", searchMap));

		searchMap.addList("treeList", getList("bsc.module.commModule.getDeptList", searchMap));
		searchMap.addList("userList", getList("bsc.impon.imponEvalUserGrp.getUserList", searchMap));

		return searchMap;
	}

	/**
	 * 비계량평가단 등록/수정/삭제
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap imponEvalUserGrpProcess(SearchMap searchMap) {
		HashMap returnMap = new HashMap();
		String stMode = searchMap.getString("mode");

		/***********************************************************************
		 * 무결성 체크
		 **********************************************************************/
		if (!"DEL".equals(stMode) && !"MODUSER".equals(stMode)) {
			returnMap = this.validChk(searchMap);

			if ((Integer) returnMap.get("ErrorNumber") < 0) {
				searchMap.addList("returnMap", returnMap);
				return searchMap;
			}
		}

		/***********************************************************************
		 * 등록/수정/삭제
		 **********************************************************************/
		if ("ADD".equals(stMode)) {
			searchMap = insertDB(searchMap);
		} else if ("MOD".equals(stMode)) {
			searchMap = updateDB(searchMap);
		} else if ("DEL".equals(stMode)) {
			searchMap = deleteDB(searchMap);
		} else if ("MODUSER".equals(stMode)) {
			searchMap = updateUserDB(searchMap);
		}

		/***********************************************************************
		 * Return
		 **********************************************************************/
		return searchMap;
	}

	/**
	 * 비계량평가단 등록
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap insertDB(SearchMap searchMap) {
		HashMap returnMap = new HashMap();

		try {
			setStartTransaction();

			returnMap = insertData("bsc.impon.imponEvalUserGrp.insertData",searchMap);

		} catch (Exception e) {
			logger.error(e.toString());
			setRollBackTransaction();
			returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap
					.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
		} finally {
			setEndTransaction();
		}

		/***********************************************************************
		 * Return
		 **********************************************************************/
		searchMap.addList("returnMap", returnMap);
		return searchMap;
	}

	/**
	 * 비계량평가단 수정
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap updateDB(SearchMap searchMap) {
		HashMap returnMap = new HashMap();

		try {
			setStartTransaction();

			returnMap = updateData("bsc.impon.imponEvalUserGrp.updateData",searchMap);

		} catch (Exception e) {
			logger.error(e.toString());
			setRollBackTransaction();
			returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap
					.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
		} finally {
			setEndTransaction();
		}

		/***********************************************************************
		 * Return
		 **********************************************************************/
		searchMap.addList("returnMap", returnMap);
		return searchMap;
	}

	/**
	 * 평가단-평가자 매핑 수정
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap updateUserDB(SearchMap searchMap) {
		HashMap returnMap = new HashMap();

		try {
			String evalUserGrpId = searchMap.getString("evalUserGrpId");
			String[] evalUserIds = searchMap.getString("evalUserIds").split("\\|", 0);

			setStartTransaction();

			searchMap.put("evalUserGrpId", evalUserGrpId);

			returnMap = updateData("bsc.impon.imponEvalUserGrp.deleteUserData",searchMap, true);

			if (!searchMap.getString("evalUserIds").equals("")) {
				if (null != evalUserIds && 0 < evalUserIds.length) {
					for (int i = 0; i < evalUserIds.length; i++) {
						searchMap.put("evalUserGrpId", evalUserGrpId);
						searchMap.put("evalUserId", evalUserIds[i]);
						returnMap = insertData("bsc.impon.imponEvalUserGrp.insertUserData",searchMap);
					}
				}
			}
			// returnMap =
			// updateData("bsc.impon.imponEvalUserGrp.deleteEvalData",
			// searchMap, true);
			returnMap = updateData(
					"bsc.impon.imponEvalUserGrp.deleteAdminData", searchMap,true);// 권한삭제
			returnMap = insertData(
					"bsc.impon.imponEvalUserGrp.insertAdminData", searchMap);// 권한등록

		} catch (Exception e) {
			logger.error(e.toString());
			setRollBackTransaction();
			returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap
					.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
		} finally {
			setEndTransaction();
		}

		/***********************************************************************
		 * Return
		 **********************************************************************/
		searchMap.addList("returnMap", returnMap);
		return searchMap;
	}

	/**
	 * 비계량평가단 삭제
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap deleteDB(SearchMap searchMap) {

		HashMap returnMap = new HashMap();

		try {
			String[] evalUserGrpIds = searchMap.getString("evalUserGrpIds").split("\\|", 0);

			setStartTransaction();

			if (null != evalUserGrpIds && 0 < evalUserGrpIds.length) {
				for (int i = 0; i < evalUserGrpIds.length; i++) {
					searchMap.put("evalUserGrpId", evalUserGrpIds[i]);
					returnMap = updateData("bsc.impon.imponEvalUserGrp.deleteData", searchMap);
				}
			}

		} catch (Exception e) {
			logger.error(e.toString());
			setRollBackTransaction();
			returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap
					.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
		} finally {
			setEndTransaction();
		}

		/***********************************************************************
		 * Return
		 **********************************************************************/
		searchMap.addList("returnMap", returnMap);
		return searchMap;
	}

	/**
	 * Validation 체크(무결성 체크)
	 * 
	 * @param SearchMap
	 * @return HashMap
	 */
	private HashMap validChk(SearchMap searchMap) {
		HashMap returnMap = new HashMap();
		int resultValue = 0;

		returnMap = ValidationChk.lengthCheck(searchMap.getString("evalUserGrpNm"), "평가단 명", 1, 33);
		if ((Integer) returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("weight"), "가중치");
		if ((Integer) returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("sortOrder"), "정렬순서");
		if ((Integer) returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap.put("ErrorNumber", resultValue);
		return returnMap;
	}

	/**
	 * 비계량평가단 엑셀다운로드
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap imponEvalUserGrpListExcel(SearchMap searchMap) {
		String excelFileName = "비계량평가단";
		String excelTitle = "비계량평가단 리스트";

		/***********************************************************************
		 * 조회조건 설정
		 **********************************************************************/
		ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();

		excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR,
				(String) searchMap.get("yearNm")));

		/***********************************************************************
		 * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN
		 * COLUMN', '셀너비'
		 **********************************************************************/
		ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
		// excelInfoList.add(new ExcelVO("평가년도", "YEAR", "left"));
		// excelInfoList.add(new ExcelVO("평가단 코드", "EVAL_USER_GRP_ID", "left"));
		excelInfoList.add(new ExcelVO("평가단", "EVAL_USER_GRP_NM", "left"));
		excelInfoList.add(new ExcelVO("가중치", "WEIGHT", "left"));
		excelInfoList.add(new ExcelVO("표준편차적용여부", "DEVIA_YN", "left"));
		// excelInfoList.add(new ExcelVO("평가단 구분 코드", "EVAL_USER_GRP_GBN_ID","left"));
		excelInfoList.add(new ExcelVO("평가단그룹", "EVAL_USER_GRP_GBN_NM", "left"));
		excelInfoList.add(new ExcelVO("대상인원", "EVAL_USER_CNT", "left"));
		excelInfoList.add(new ExcelVO("정렬순서", "SORT_ORDER", "left"));

		searchMap.put("excelFileName", excelFileName);
		searchMap.put("excelTitle", excelTitle);
		searchMap.put("excelSearchInfoList", excelSearchInfoList);
		searchMap.put("excelInfoList", excelInfoList);
		searchMap.put("excelDataList", (ArrayList) getList(
				"bsc.impon.imponEvalUserGrp.getList", searchMap));

		return searchMap;
	}

}
