/*************************************************************************
 * CLASS 명      : MemEvalProcMonitorAction
 * 작 업 자      : 유연주
 * 작 업 일      : 2017년 04월 13일 
 * 기    능      : 평가진행사항모니터링
 * ---------------------------- 변 경 이 력 --------------------------------
 * 번호   작 업 자      작   업   일        변 경 내 용              비고
 * ----  ---------  -----------------  -------------------------    --------
 *   1    유연주      2017년 04월 13일           최 초 작 업 
 **************************************************************************/
package com.lexken.mem.monitor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.SearchMap;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginVO;

public class MemEvalProcMonitorAction extends CommonService {

	private static final long serialVersionUID = 1L;

	// Logger
	private final Log logger = LogFactory.getLog(getClass());
	
	/**
	 * 평가진행사항모니터링 조회
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap memEvalProcMonitor(SearchMap searchMap) {
		LoginVO loginVO = (LoginVO)searchMap.get("loginVO");

		searchMap.put("year", searchMap.getString("findYear"));
        // 평가그룹별(1차평가자) 평가여부 조회
    	String evallYnByGroup = "";
        searchMap.put("evalUserGubunId", "01");
        evallYnByGroup = (String)getDetail("mem.base.memUser.getEvalYnByGroup", searchMap).get("EVAL_YN_ID");
        searchMap.put("eval1YnByGroup",evallYnByGroup);
    	
        // 평가그룹별(2차평가자) 평가여부 조회
    	String eval2YnByGroup = "";
        searchMap.put("evalUserGubunId", "02");
        eval2YnByGroup = (String)getDetail("mem.base.memUser.getEvalYnByGroup", searchMap).get("EVAL_YN_ID");
        searchMap.put("eval2YnByGroup",eval2YnByGroup);
        
        // 평가그룹별(동료평가자) 평가여부 조회
    	String peerYnByGroup = "";
        searchMap.put("evalUserGubunId", "03");
        peerYnByGroup = (String)getDetail("mem.base.memUser.getEvalYnByGroup", searchMap).get("EVAL_YN_ID");
        searchMap.put("peerYnByGroup",peerYnByGroup);
		
		// 1차평가진행사항 모니터링
		searchMap.addList("evalMonitor1", getDetail("mem.monitor.memEvalProcMonitor.get1EvalInfo", searchMap));
		
		// 2차평가진행사항 모니터링
		searchMap.addList("evalMonitor2", getDetail("mem.monitor.memEvalProcMonitor.get2EvalInfo", searchMap));
		
		// 동료평가진행사항 모니터링
		searchMap.addList("evalMonitor3", getDetail("mem.monitor.memEvalProcMonitor.get3EvalInfo", searchMap));
		
		// 이의신청현황 모니터링
		searchMap.addList("dissentMonitor", getDetail("mem.monitor.memEvalProcMonitor.getDissentInfo", searchMap));
		
		return searchMap;
	}
	
	/**
	 * 1차평가진행사항 모니터링
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap memEvalProcMonitorChart_1_xml(SearchMap searchMap) {
		LoginVO loginVO = (LoginVO)searchMap.get("loginVO");

		searchMap.put("year", searchMap.getString("findYear"));
        // 평가그룹별(1차평가자) 평가여부 조회
    	String evallYnByGroup = "";
        searchMap.put("evalUserGubunId", "01");
        evallYnByGroup = (String)getDetail("mem.base.memUser.getEvalYnByGroup", searchMap).get("EVAL_YN_ID");
        searchMap.put("eval1YnByGroup",evallYnByGroup);
		
		searchMap.addList("evalMonitor1", getDetail("mem.monitor.memEvalProcMonitor.get1EvalInfo", searchMap));
		
		return searchMap;
	}
	
	/**
	 * 2차평가진행사항 모니터링
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap memEvalProcMonitorChart_2_xml(SearchMap searchMap) {
		LoginVO loginVO = (LoginVO)searchMap.get("loginVO");

		searchMap.put("year", searchMap.getString("findYear"));
		
        // 평가그룹별(2차평가자) 평가여부 조회
    	String eval2YnByGroup = "";
        searchMap.put("evalUserGubunId", "02");
        eval2YnByGroup = (String)getDetail("mem.base.memUser.getEvalYnByGroup", searchMap).get("EVAL_YN_ID");
        searchMap.put("eval2YnByGroup",eval2YnByGroup);
		
		searchMap.addList("evalMonitor2", getDetail("mem.monitor.memEvalProcMonitor.get2EvalInfo", searchMap));
		
		return searchMap;
	}
	
	/**
	 * 동료평가진행사항 모니터링
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap memEvalProcMonitorChart_3_xml(SearchMap searchMap) {
		LoginVO loginVO = (LoginVO)searchMap.get("loginVO");

		searchMap.put("year", searchMap.getString("findYear"));
       
        // 평가그룹별(동료평가자) 평가여부 조회
    	String peerYnByGroup = "";
        searchMap.put("evalUserGubunId", "03");
        peerYnByGroup = (String)getDetail("mem.base.memUser.getEvalYnByGroup", searchMap).get("EVAL_YN_ID");
        searchMap.put("peerYnByGroup",peerYnByGroup);
		
		searchMap.addList("evalMonitor3", getDetail("mem.monitor.memEvalProcMonitor.get3EvalInfo", searchMap));
		
		return searchMap;
	}
	
	/**
	 * 모니터링 직원목록 조회
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap popMemEvalEmpList(SearchMap searchMap) {

		return searchMap;
	}
	
	/**
	 * 모니터링 직원목록 데이터 조회(xml)
	 * 
	 * @param
	 * @return String
	 * @throws
	 */
	public SearchMap popMemEvalEmpList_xml(SearchMap searchMap) {
		
		String gubun = searchMap.getString("gubun");
		
		searchMap.put("year", searchMap.getString("findYear"));
        // 평가그룹별(1차평가자) 평가여부 조회
    	String evallYnByGroup = "";
        searchMap.put("evalUserGubunId", "01");
        evallYnByGroup = (String)getDetail("mem.base.memUser.getEvalYnByGroup", searchMap).get("EVAL_YN_ID");
        searchMap.put("eval1YnByGroup",evallYnByGroup);
    	
        // 평가그룹별(2차평가자) 평가여부 조회
    	String eval2YnByGroup = "";
        searchMap.put("evalUserGubunId", "02");
        eval2YnByGroup = (String)getDetail("mem.base.memUser.getEvalYnByGroup", searchMap).get("EVAL_YN_ID");
        searchMap.put("eval2YnByGroup",eval2YnByGroup);
        
        // 평가그룹별(동료평가자) 평가여부 조회
    	String peerYnByGroup = "";
        searchMap.put("evalUserGubunId", "03");
        peerYnByGroup = (String)getDetail("mem.base.memUser.getEvalYnByGroup", searchMap).get("EVAL_YN_ID");
        searchMap.put("peerYnByGroup",peerYnByGroup);
		
		if("E1_COM".equals(gubun)){
			// 1차평가자 평가완료
			searchMap.addList("list", getList("mem.monitor.memEvalProcMonitor.getEval1EmpList", searchMap));
		}else if("E1_ING".equals(gubun)){
			// 1차평가자 평가중
			searchMap.addList("list", getList("mem.monitor.memEvalProcMonitor.getEval1EmpList", searchMap));
		}else if("E1_NOT".equals(gubun)){
			// 1차평가자 미평가
			searchMap.addList("list", getList("mem.monitor.memEvalProcMonitor.getEval1EmpList", searchMap));
		}else if("E2_COM".equals(gubun)){
			// 2차평가자 평가완료
			searchMap.addList("list", getList("mem.monitor.memEvalProcMonitor.getEval2EmpList", searchMap));
		}else if("E2_ING".equals(gubun)){
			// 2차평가자 평가중
			searchMap.addList("list", getList("mem.monitor.memEvalProcMonitor.getEval2EmpList", searchMap));
		}else if("E2_NOT".equals(gubun)){
			// 2차평가자 미평가
			searchMap.addList("list", getList("mem.monitor.memEvalProcMonitor.getEval2EmpList", searchMap));
		}else if("E3_COM".equals(gubun)){
			// 동료평가자 평가완료
			searchMap.addList("list", getList("mem.monitor.memEvalProcMonitor.getEval3EmpList", searchMap));
		}else if("E3_ING".equals(gubun)){
			// 동료평가자 평가중
			searchMap.addList("list", getList("mem.monitor.memEvalProcMonitor.getEval3EmpList", searchMap));
		}else if("E3_NOT".equals(gubun)){
			// 동료평가자 미평가
			searchMap.addList("list", getList("mem.monitor.memEvalProcMonitor.getEval3EmpList", searchMap));
		}
		return searchMap;
	}
}
