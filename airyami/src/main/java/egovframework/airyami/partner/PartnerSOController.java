package egovframework.airyami.partner;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.airyami.cmm.service.CmmService;
import egovframework.airyami.cmm.service.CommCodeService;
import egovframework.airyami.cmm.util.CommonUtils;
import egovframework.airyami.cmm.util.ValueMap;
import egovframework.com.cmm.EgovMessageSource;

/**
 * 
 * 파트너 관련 프로세스를 관리한다. S/O
 * 
 * @author 배수한
 * @since 2017.06.19 
 * @version 1.0
 * @see
 *
 * <pre>
 *   
 *     수정일                  수정자                              수정내용
 *	-----------    ---------    ---------------------------
 *  2017.06.19      배수한             최초 생성
 *
 * </pre>
 */

@Controller
public class PartnerSOController {
	protected Log log = LogFactory.getLog(this.getClass());
	private Logger logger = Logger.getLogger(this.getClass());

	/** CommCodeService */
    @Resource(name = "commCodeService")
    private CommCodeService commCodeService;
    
	/**
	 *  공통 Service 
	 */ 	
	@Resource(name="egovMessageSource")
	protected EgovMessageSource egovMessageSource;
	
	/** CmmService */
    @Resource(name = "cmmService")
    private CmmService cmmService; 
    
    /** Transaction */    
    @Resource(name="txManager")
    PlatformTransactionManager transactionManager;
    
    /**
     * S/O 목록조회 페이지 이동 
     */
    @RequestMapping(value="/partner/soHList.do")
    public String soList(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	log.info("param >>>>> :: " + params);
    	CommonUtils.setModelByParams(model, params, request);

    	params.put( "CODE_GROUP_ID", "MENU_TYPE" ); //
		List<ValueMap> code_MENU_TYPE = commCodeService.selectCommCode(params);
		model.put("ds_cd_MENU_TYPE", code_MENU_TYPE);
		
		params.put( "CODE_GROUP_ID", "USER_TYPE" ); //
		List<ValueMap> code_USER_TYPE = commCodeService.selectCommCode(params);
		model.put("ds_cd_USER_TYPE", code_USER_TYPE);
        
    	return "/partner/soHList";
    }
    
    /**
     * P/O Header 목록조회 
     */
    @RequestMapping(value="/partner/selectSOHList.do")
    public String getMenuList(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		boolean success = true;
    	ValueMap result = new ValueMap();
    	//*****************************************************************************
		// 입력부 로그 출력 
		//*****************************************************************************
		logger.debug("===================================================================");
		logger.debug("[MenuMngController.getMenuList() ================== start]");
		
    	
		//*****************************************************************************
		// 변수 및 객체 선언 및 초기화 
		//*****************************************************************************
		
		Map<String,Object> params = CommonUtils.getRequestMap(request);
		log.info("param >>>>> :: " + params);
		
		//*****************************************************************************
		// 비지니스 처리
		//*****************************************************************************		
    	
    	try{  	        
    		List<ValueMap> ds_list = cmmService.getCommDbList(params, "menu.getMenuList");
    		result.put("ds_menulist", ds_list);
    	} catch(Exception e){
    		
    		success = false;
    		e.printStackTrace();
    		System.out.println(e.getMessage());
    	}
    	
    	result.put("success", success);
    	response.setContentType("text/xml;charset=UTF-8");
    	response.getWriter().println(CommonUtils.setJsonResult(result));
    	
    	return null;
    }
    
}