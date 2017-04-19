/*************************************************************************
* CLASS 명      : budExcEffect
* 작 업 자      : 김기현
* 작 업 일      : 2012년 6월 18일
* 기    능      : 시스템항목관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    김기현      시스템항목관리            최 초 작 업
**************************************************************************/
package com.lexken.bsc.popup;

import java.sql.SQLException;
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

public class GovBridgeAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 정부경영평가 세부항목 연계
     * @param
     * @return String
     * @throws
     */
    public SearchMap govBridgeList(SearchMap searchMap) {

        searchMap.addList("govInfo", getDetail("bsc.popup.govBridge.getDeptMetricInfo", searchMap));

        return searchMap;
    }
}
