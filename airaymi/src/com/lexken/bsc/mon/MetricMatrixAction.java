/*************************************************************************
* CLASS 명      : MetricMatrixAction
* 작 업 자      : 한봉준
* 작 업 일      : 2012년 8월 29일 
* 기    능      : 지표연계현황
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  --------  -----------------  -------------------------    --------
*   1    한봉준      2012년 8월 29일             최 초 작 업 
**************************************************************************/
package com.lexken.bsc.mon;
    
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
import com.lexken.framework.login.LoginVO;
import com.lexken.framework.util.StaticUtil;

public class MetricMatrixAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 지표연계현황 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricMatrixList(SearchMap searchMap) {

    	/**********************************
         * 로그인정보 (권한 체크)
         **********************************/
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");

    	//지표명
    	searchMap.put("findMetricNm", getStr("bsc.mon.metricMatrix.getMetricNm", searchMap));
    	
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.get("findMetricId")))) {
    		
        	if(!loginVO.chkAuthGrp("01") && !loginVO.chkAuthGrp("60")) {
        		searchMap.put("year", searchMap.get("findYear"));
        		searchMap.put("scDeptId", loginVO.getSc_dept_id());
        		searchMap.put("findPopScDeptNm", getStr("bsc.module.commModule.getScDeptNm", searchMap));
        	}
        	
        	ArrayList metricList = (ArrayList) getList("bsc.module.commModule.getMetricListScDeptSearch", searchMap);
        	if (metricList.size() > 0) {
            	HashMap strategyMap = (HashMap)metricList.get(0);
            	searchMap.put("findMetricId", strategyMap.get("METRIC_ID"));
            	searchMap.put("findMetricNm", strategyMap.get("METRIC_NM"));
            	searchMap.put("findScDeptId", strategyMap.get("SC_DEPT_ID"));
        	}
        	
    	}
    	

        return searchMap;
    }
    
    /**
     * 지표연계현황 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricMatrixList_xml(SearchMap searchMap) {
    	
    	ArrayList list = (ArrayList) getList("bsc.mon.metricMatrix.getList", searchMap);

        searchMap.addList("list", list);

        ArrayList list1 = new ArrayList(0);
        ArrayList list2 = new ArrayList(0);
        ArrayList list3 = new ArrayList(0);
        ArrayList list4 = new ArrayList(0);
        ArrayList list5 = new ArrayList(0);
        ArrayList list6 = new ArrayList(0);
        ArrayList list7 = new ArrayList(0);

        if(list.size() > 0){
        	for(int idx=0 ; idx<list.size() ; idx++){
        		HashMap map = (HashMap)list.get(idx);
        		if(1 == Integer.parseInt(map.get("LEVEL_NUM").toString())){
        			list1.add(map);
        		}else if(2 == Integer.parseInt(map.get("LEVEL_NUM").toString())){
        			list2.add(map);
        		}else if(3 == Integer.parseInt(map.get("LEVEL_NUM").toString())){
        			list3.add(map);
        		}else if(4 == Integer.parseInt(map.get("LEVEL_NUM").toString())){
        			list4.add(map);
        		}else if(5 == Integer.parseInt(map.get("LEVEL_NUM").toString())){
        			list5.add(map);
        		}else if(6 == Integer.parseInt(map.get("LEVEL_NUM").toString())){
        			list6.add(map);
        		}else if(7 == Integer.parseInt(map.get("LEVEL_NUM").toString())){
        			list7.add(map);
        		}
        	}
        }

        searchMap.addList("list1", list1);
        searchMap.addList("list2", list2);
        searchMap.addList("list3", list3);
        searchMap.addList("list4", list4);
        searchMap.addList("list5", list5);
        searchMap.addList("list6", list6);
        searchMap.addList("list7", list7);
        
        ArrayList signalList = (ArrayList)getList("bsc.module.commModule.getSignalList", searchMap);
        HashMap sinalMap = new HashMap();
        if(0<signalList.size()){
        	for(int idx_sig = 0 ; idx_sig<signalList.size() ; idx_sig++){
        		HashMap map = (HashMap)signalList.get(idx_sig);
        		
        		sinalMap.put(map.get("CODE_ID").toString(),map.get("COLOR"));
        	}
        }
        searchMap.addList("sinalMap", sinalMap);

        return searchMap;
    }
}
