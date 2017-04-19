/*************************************************************************
* CLASS 명      : StrMetricAction
* 작 업 자      : 하윤식
* 작 업 일      : 2012년 11월 1일 
* 기    능      : 이행성과지표정의서
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    하윤식      2012년 11월 1일         최 초 작 업 
**************************************************************************/
package com.lexken.str.mon;
    
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
import com.lexken.framework.util.StaticUtil;

public class StrMetricAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 이행성과지표정의서 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popStrMetricList(SearchMap searchMap) {

    	/**********************************
         * 세부과제 상세조회
         **********************************/
    	searchMap.addList("detail", getDetail("str.mon.strMetric.getDetail", searchMap));
    	
    	/**********************************
         * 세부과제 첨부파일 조회
         **********************************/
    	searchMap.addList("fileList", getList("str.mon.strMetric.getFileList", searchMap));
    	
    	/**********************************
         * 연계 내부평가지표 조회
         **********************************/
    	searchMap.addList("metricMappingList", getList("str.mon.strMetric.getMetricMappingList", searchMap));
    	
        return searchMap;
    }
}
