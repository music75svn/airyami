/*************************************************************************
 * CLASS 명      : MemProcMonitorAction
 * 작 업 자      : 유연주
 * 작 업 일      : 2017년 04월 12일 
 * 기    능      : 업무진행사항모니터링
 * ---------------------------- 변 경 이 력 --------------------------------
 * 번호   작 업 자      작   업   일        변 경 내 용              비고
 * ----  ---------  -----------------  -------------------------    --------
 *   1    유연주      2017년 03월 15일          최 초 작 업 
 **************************************************************************/
package com.lexken.mem.monitor;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.FileInfoVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginVO;

public class MemProcMonitorAction extends CommonService {

	private static final long serialVersionUID = 1L;

	// Logger
	private final Log logger = LogFactory.getLog(getClass());
	
	/**
	 * 업무진행사항모니터링 조회
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap memProcMonitor(SearchMap searchMap) {
		LoginVO loginVO = (LoginVO)searchMap.get("loginVO");

		// 업무진행사항 모니터링 지표관리현황
		searchMap.addList("metricMonitor", getDetail("mem.monitor.memProcMonitor.getMetricInfo", searchMap));
		
		// 업무진행사항 모니터링 실적입력현황
		searchMap.addList("actMonitor", getDetail("mem.monitor.memProcMonitor.getActInfo", searchMap));

		// 업무진행사항 모니터링 면담관리현황
		searchMap.addList("meetMonitor", getDetail("mem.monitor.memProcMonitor.getMeetInfo", searchMap));
		
		return searchMap;
	}
	
	/**
	 * 업무진행사항모니터링 조회>지표관리현황
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap memProcMonitorChart_1_xml(SearchMap searchMap) {
		LoginVO loginVO = (LoginVO)searchMap.get("loginVO");

		searchMap.addList("metricMonitor", getDetail("mem.monitor.memProcMonitor.getMetricInfo", searchMap));
		
		return searchMap;
	}
	
	/**
	 * 업무진행사항모니터링 조회>지표관리현황
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap memProcMonitorChart_2_xml(SearchMap searchMap) {
		LoginVO loginVO = (LoginVO)searchMap.get("loginVO");

		searchMap.addList("actMonitor", getDetail("mem.monitor.memProcMonitor.getActInfo", searchMap));
		
		return searchMap;
	}
	
	/**
	 * 모니터링 직원목록 조회
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap popMemEmpList(SearchMap searchMap) {

		return searchMap;
	}
	
	/**
	 * 모니터링 직원목록 데이터 조회(xml)
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap popMemEmpList_xml(SearchMap searchMap) {
		
		String gubun = searchMap.getString("gubun");
		
		if("M_CON".equals(gubun)){
			// 지표관리현황 승인완료
			searchMap.put("metricStatusId", "03");
			searchMap.addList("list", getList("mem.monitor.memProcMonitor.getMetricEmpList", searchMap));
		}else if("M_NOT".equals(gubun)){
			// 지표관리현황 미입력/입력중
			searchMap.put("metricStatusId", "01");
			searchMap.addList("list", getList("mem.monitor.memProcMonitor.getMetricEmpList", searchMap));
		}else if("M_REQ".equals(gubun)){
			// 지표관리현황 승인요청수
			searchMap.put("metricStatusId", "02");
			searchMap.addList("list", getList("mem.monitor.memProcMonitor.getMetricEmpList", searchMap));
		}else if("M_MOD".equals(gubun)){
			// 지표관리현황 변경요청수
			searchMap.put("metricStatusId", "05");
			searchMap.addList("list", getList("mem.monitor.memProcMonitor.getMetricEmpList", searchMap));
		}else if("M_RET".equals(gubun)){
			// 지표관리현황 반려수
			searchMap.put("metricStatusId", "04");
			searchMap.addList("list", getList("mem.monitor.memProcMonitor.getMetricEmpList", searchMap));
		}else if("ME_REQ_Q1".equals(gubun)){
			// 1분기 면담요청
			searchMap.put("meetState", "01");
			searchMap.put("quarter", "1");
			searchMap.addList("list", getList("mem.monitor.memProcMonitor.getMeetEmpList", searchMap));
		}else if("ME_COM_Q1".equals(gubun)){
			// 1분기 면담완료
			searchMap.put("meetState", "02");
			searchMap.put("quarter", "1");
			searchMap.addList("list", getList("mem.monitor.memProcMonitor.getMeetEmpList", searchMap));
		}else if("ME_REQ_Q2".equals(gubun)){
			// 2분기 면담요청
			searchMap.put("meetState", "01");
			searchMap.put("quarter", "2");
			searchMap.addList("list", getList("mem.monitor.memProcMonitor.getMeetEmpList", searchMap));
		}else if("ME_COM_Q2".equals(gubun)){
			// 2분기 면담완료
			searchMap.put("meetState", "02");
			searchMap.put("quarter", "2");
			searchMap.addList("list", getList("mem.monitor.memProcMonitor.getMeetEmpList", searchMap));
		}else if("ME_REQ_Q3".equals(gubun)){
			// 3분기 면담요청
			searchMap.put("meetState", "01");
			searchMap.put("quarter", "3");
			searchMap.addList("list", getList("mem.monitor.memProcMonitor.getMeetEmpList", searchMap));
		}else if("ME_COM_Q3".equals(gubun)){
			// 3분기 면담완료
			searchMap.put("meetState", "02");
			searchMap.put("quarter", "3");
			searchMap.addList("list", getList("mem.monitor.memProcMonitor.getMeetEmpList", searchMap));
		}else if("ME_REQ_Q4".equals(gubun)){
			// 4분기 면담요청
			searchMap.put("meetState", "01");
			searchMap.put("quarter", "4");
			searchMap.addList("list", getList("mem.monitor.memProcMonitor.getMeetEmpList", searchMap));
		}else if("ME_COM_Q4".equals(gubun)){
			// 4분기 면담완료
			searchMap.put("meetState", "02");
			searchMap.put("quarter", "4");
			searchMap.addList("list", getList("mem.monitor.memProcMonitor.getMeetEmpList", searchMap));
		}else if("A_IN".equals(gubun)){
			// 실적입력 지행사항 입력
			searchMap.addList("list", getList("mem.monitor.memProcMonitor.getActInEmpList", searchMap));
		}else if("A_NOT".equals(gubun)){
			// 실적입력 지행사항 미입력
			searchMap.addList("list", getList("mem.monitor.memProcMonitor.getActNotInEmpList", searchMap));
		}else if("D_IN".equals(gubun)){
			// 자기성과기술서 작성현황 제출
			searchMap.addList("list", getList("mem.monitor.memProcMonitor.getDescInEmpList", searchMap));
		}else if("D_NOT".equals(gubun)){
			// 자기성과기술서 작성현황 미제출
			searchMap.addList("list", getList("mem.monitor.memProcMonitor.getDescNotInEmpList", searchMap));
		}else if("DI_REQ".equals(gubun)){
			// 이의신청 신청자
			searchMap.put("procStatusId", "");
			searchMap.addList("list", getList("mem.monitor.memProcMonitor.getDissentEmpList", searchMap));
			
		}else if("DI_ALO".equals(gubun)){
			// 이의신청 수용
			searchMap.put("procStatusId", "02");
			searchMap.addList("list", getList("mem.monitor.memProcMonitor.getDissentEmpList", searchMap));
		}else if("DI_PRT".equals(gubun)){
			// 이의신청 부분수용
			searchMap.put("procStatusId", "03");
			searchMap.addList("list", getList("mem.monitor.memProcMonitor.getDissentEmpList", searchMap));
		}else if("DI_NO".equals(gubun)){
			// 이의신청 수용불가
			searchMap.put("procStatusId", "04");
			searchMap.addList("list", getList("mem.monitor.memProcMonitor.getDissentEmpList", searchMap));
		}else if("DI_NOT".equals(gubun)){
			// 이의신청 미검토
			searchMap.put("procStatusId", "01");
			searchMap.addList("list", getList("mem.monitor.memProcMonitor.getDissentEmpList", searchMap));
		}
		return searchMap;
	}
}
