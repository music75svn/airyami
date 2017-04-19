/*************************************************************************
* CLASS 명      : BscDataInterfaceAction
* 작 업 자      : 신인수
* 작 업 일      : 2012년 7월 9일 
* 기    능      : 전년데이터 일괄적용
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    신인수      2012년 7월 9일             최 초 작 업 
**************************************************************************/
package com.lexken.bsc.system;
    
import java.util.HashMap;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.struts2.IsBoxActionSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.util.StaticUtil;

public class BscDataInterfaceAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 전년데이터 일괄적용 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap bscDataInterfaceList(SearchMap searchMap) {

        return searchMap;
    }
    
    /**
     * 전년데이터 일괄적용 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap bscDataInterfaceList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.system.bscDataInterface.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 전년데이터 일괄적용 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap bscDataInterfaceProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("MOD".equals(stMode)) {
            searchMap = execDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 전년데이터 일괄적용
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap execDB(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        
        try {
	        String[] tableIds = searchMap.getString("tableIds").split("\\|", 0);
	        String[] ubmu_gubuns = searchMap.getString("ubmu_gubuns").split("\\|", 0);
	        String[] chks = searchMap.getString("chks").split("\\|", 0);
	        String temps = "";
	        String[] temps2 = null;
	        
	        setStartTransaction();

	        for (int i = 0; i < chks.length; i++) { 
	        	for(int j = 0; j < tableIds.length; j++){
	        		if(ubmu_gubuns[j].equals(chks[i])){
	        			temps += tableIds[j] + "|";
	        		}
	        	}
	        }
        	
	        temps2 = temps.split("\\|", 0);

	        for (int k = 0; k < temps2.length; k++) { 
	        	searchMap.put("temp2", temps2[k]);
	            returnMap = insertData("bsc.system.bscDataInterface.execData", searchMap);
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

    
    
}
