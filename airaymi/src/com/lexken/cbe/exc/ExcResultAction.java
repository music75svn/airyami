/**************************************************************************
* CLASS 명      : ExcResultAction
* 작 업 자      : 안요한
* 작 업 일      : 2013년 07월 18일 
* 기    능      : 평가결과현황
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    안 요 한      2013년 07월 18일    최 초 작 업 
**************************************************************************/
package com.lexken.cbe.exc;

import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.FileInfoVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.util.StaticUtil;

public class ExcResultAction extends CommonService {
	private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 평가결과현황 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excResultList(SearchMap searchMap) {
    	
    	searchMap.addList("evalYn", getDetail("cbe.exc.excResult.getSubmityn", searchMap));
    	
		searchMap.addList("Userlist", getList("cbe.exc.excResult.getUserList", searchMap));
	
		searchMap.addList("Deptlist", getList("cbe.exc.excResult.getDeptList", searchMap));
	
		searchMap = updateDB(searchMap);
    		
    	return searchMap;
    }
    
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("cbe.exc.excResult.updaDeptData", searchMap);
	        
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
    
}
