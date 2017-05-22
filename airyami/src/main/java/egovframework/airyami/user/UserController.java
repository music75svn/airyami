/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package egovframework.airyami.user;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.airyami.cmm.service.CmmService;
import egovframework.airyami.cmm.service.CommCodeService;
import egovframework.airyami.cmm.util.CommonUtils;
import egovframework.airyami.cmm.util.PageInfo;
import egovframework.airyami.cmm.util.ValueMap;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovProperties;



/**
 * 
 * 사용자 Controller를 정의한다.
 * @author 유연주
 * @since 2017.05.09
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *     수정일                  수정자                              수정내용
 *	-----------    ---------    ---------------------------
 *  2017.05.17      유연주             최초 생성
 *
 * </pre>
 */

@Controller
public class UserController {
	protected Log log = LogFactory.getLog(this.getClass());
	
	/** CommCodeService */
    @Resource(name = "commCodeService")
    private CommCodeService commCodeService;
	
	/** CmmService */
    @Resource(name = "cmmService")
    private CmmService cmmService;
    
    /** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
    
    /** Transaction */    
    @Resource(name="txManager")
    PlatformTransactionManager transactionManager;
    
    /**
     * 사용자 리스트 이동 
     */
    @RequestMapping(value="/user/userList.do")
    public String codeGroupList(HttpServletRequest request, HttpServletResponse response, 
    		ModelMap model) throws Exception {
    	
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	CommonUtils.setModelByParams(model, params);	// 전달받은 내용 다른 페이지에 전달할때 사용
    	
    	List<ValueMap> typeRoleList = cmmService.getCommDbList(params, "user.getTypeRoleList");
    	model.put("ds_typeRoleList", typeRoleList);
    	
    	return "/user/userList";
    }
    
    /**
	 * 사용자리스트 조회
	 * @param 
	 * @param model
	 * @return 
	 * @exception Exception
	 */
    @RequestMapping(value="/user/selectUserList.do")
    public String selectUserList(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
    	
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	log.debug("param :: " + params);
    	
    	
    	// 이건 체크박스로 리스트에서 값 가지고 오는거..
    	// 일단 리스트에선 패스..
    	/*
    	if(!CommonUtils.isNull((String)params.get("rowDatas"))){
    		List<Object> rParams = JsonUtil.parseToList( (String)params.get("rowDatas") );
    		
    		for(int i = 0 ; i < rParams.size() ; i++){
    			Map<String,Object> rowData = (Map<String, Object>) rParams.get(i);
    			log.debug("rowData.CD :: " + rowData.get("CD"));
    		}
    	}
    	*/
    	
    	
    	boolean success = true;
    	ValueMap result = new ValueMap();
    	
    	
    	PageInfo pageInfo = null;
    	
    	// 엑셀 여부..( 전체조회인지.. 아닌지)
    	if(!params.containsKey("EXCEL_YN"))
    		params.put("EXCEL_YN", "N");
    	 
    	String excelYn = (String)params.get("EXCEL_YN");
    	
    	try{
    		if("N".equals(excelYn)){
    			int pageNo = Integer.parseInt( CommonUtils.NVL((String)params.get("pageNo"), "0")  );
    			int totCnt = cmmService.getCommDbInt(params, "user.getUserCount");
    			
    			if("".equals( pageNo ) || pageNo < 1 ) {
    				pageInfo = new PageInfo(1, totCnt, EgovProperties.getProperty("Globals.DbType"));
    			} else {
    				pageInfo = new PageInfo(pageNo, totCnt, EgovProperties.getProperty("Globals.DbType"));
    			}
    			
    			if( !CommonUtils.isNull( (String)params.get("pageRowCnt") ) ){
    				pageInfo.setPerUnit( Integer.valueOf( (String)params.get("pageRowCnt") ));
    				pageInfo.calculate();
    			}
    			
    			params.put( "STARTUNIT", pageInfo.getStartUnit() );
    			params.put( "ENDUNIT", pageInfo.getEndUnit() );
    			params.put( "TOTCNT", totCnt);
    			result.put( "totCnt", totCnt);
    		}
    	
    		List<ValueMap> list = cmmService.getCommDbList(params, "user.getUserList");
    		
    		if("Y".equals(excelYn)){
    			model.put( "excel_name", "GROUPCODE" );
    			model.put( "data", list );
    			if(params.containsKey("SEARCH_CONDITION"))
    				model.put( "excel_condition", params.get("SEARCH_CONDITION") );
    	        
    	        log.debug("excelView Call!!!!!!!!!!!!!!!!!!!!!!!!!!");
    	        
    	        return "excelView";
    		}
    		
    		result.put("ds_list", list);
    		result.put("pageInfo", pageInfo);
    	}
    	catch(Exception e){
    		success = false;
    		e.printStackTrace();
    		System.out.println(e.getMessage());
    	}
        
    	result.put("success", success);
    	response.setContentType("text/xml;charset=UTF-8");
    	response.getWriter().println(CommonUtils.setJsonResult(result));
    	
        return null;
    }
    
    /**
     * 사용자 detail 호출 
     */
    @RequestMapping(value="/user/userDetail.do")
    public String codeGroupDetail(HttpServletRequest request, HttpServletResponse response, 
    		ModelMap model) throws Exception {
    	
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	CommonUtils.setModelByParams(model, params, request);
    	
    	List<ValueMap> typeRoleList = cmmService.getCommDbList(params, "user.getTypeRoleList");
    	model.put("ds_typeRoleList", typeRoleList);
    	
    	// 국가코드 조회
    	params.put( "CODE_GROUP_ID", "COUNTRY" ); //국가 대분류
    	List<ValueMap> addrCountryList = commCodeService.selectCommCode(params);
    	model.put("ds_addrCountryList", addrCountryList);
    	
    	// 사용언어 조회
    	params.put( "CODE_GROUP_ID", "LANG" ); //언어 대분류
    	List<ValueMap> useLanguageList = commCodeService.selectCommCode(params);
    	model.put("ds_useLanguageList", useLanguageList);
    	
    	// SNS유형 조회
    	params.put( "CODE_GROUP_ID", "SNS" ); //SNS유형 대분류
    	List<ValueMap> snsTypeList = commCodeService.selectCommCode(params);
    	model.put("ds_snsTypeList", snsTypeList);
    	
    	
    	
    	// 출생년월일 목록 생성
    	List<ValueMap> birthYearList = new ArrayList();
    	List<ValueMap> birthMonthList = new ArrayList();
    	List<ValueMap> birthDayList = new ArrayList();
    	
    	Date now = new Date(); 
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy"); 
    	int nowYear = Integer.parseInt(sdf.format(now));
    	for(int i = nowYear; i >= nowYear - 80; i--){
    		ValueMap map = new ValueMap();
    		map.put("YEAR", i);
    		birthYearList.add(map);
    	}
    	for(int i = 1; i <= 12; i++){
    		ValueMap map = new ValueMap();
    		map.put("MONTH", i);
    		birthMonthList.add(map);
    	}
    	for(int i = 1; i <= 31; i++){
    		ValueMap map = new ValueMap();
    		map.put("DAY", i);
    		birthDayList.add(map);
    	}
    	model.put("ds_birthYearList", birthYearList);
    	model.put("ds_birthMonthList", birthMonthList);
    	model.put("ds_birthDayList", birthDayList);
    	
    	return "/user/userDetail";
    }
    
    /**
     * 사용자 detail 상세조회 
     */
    @RequestMapping(value="/user/getUserDetail.do")
    public String getUserDetail(HttpServletRequest request, HttpServletResponse response, 
    		ModelMap model) throws Exception {
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	log.debug("param :: " + params);
    	
    	boolean success = true;
    	ValueMap result = new ValueMap();
    	
    	try{
    		ValueMap ds_detail = cmmService.getCommDbMap(params, "user.getUserDetail");
    		
    		result.put("ds_detail", ds_detail);
    	}
    	catch(Exception e){
    		success = false;
    		e.printStackTrace();
    		System.out.println(e.getMessage());
    	}
    	
    	result.put("success", success);
    	response.setContentType("text/xml;charset=UTF-8");
    	response.getWriter().println(CommonUtils.setJsonResult(result));
    	
    	
    	return null;
    }
    
    /**
     * 사용자 detail 저장 
     */
    @RequestMapping(value="/user/saveUser.do")
    public String saveUser(HttpServletRequest request, HttpServletResponse response, 
    		ModelMap model) throws Exception {
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	log.debug("param :: " + params);
    	
    	boolean success = true;
    	ValueMap result = new ValueMap();
    	
    	try{
    		String userTypeRole = (String)params.get("USER_TYPE_ROLE");
    		params.put("USER_TYPE", userTypeRole.substring(0, 1));
    		params.put("USER_ROLE", userTypeRole.substring(1, 4));
    		if("CREATE".equals(params.get("PROC_MODE"))){
    			// 중복체크
    			String existYn = cmmService.getCommDbString(params, "user.getUserExistYn");
    			
    			// 사용자 중복
    			if("Y".equals(existYn)){
    				// TODO 예외처리
    		    	result.put("msg", egovMessageSource.getMessage("fail.exist.msg", CommonUtils.getLocale(request)) );
    		    	throw new Exception();
    			}

    			cmmService.insertCommDb(params, "user.insertUser");
    		}else if("UPDATE".equals(params.get("PROC_MODE"))){
    			cmmService.updateCommDb(params, "user.updateUser");
    		}else if("DELETE".equals(params.get("PROC_MODE"))){
    			cmmService.deleteCommDb(params, "user.deleteUser");
    		}
    	}
    	catch(Exception e){
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
