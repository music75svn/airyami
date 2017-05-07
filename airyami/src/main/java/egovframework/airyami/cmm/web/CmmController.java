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
package egovframework.airyami.cmm.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.airyami.cmm.service.CmmService;
import egovframework.airyami.cmm.util.CommonUtils;
import egovframework.airyami.cmm.util.ValueMap;
import egovframework.com.cmm.EgovMessageSource;



/**  
 * @Class Name : CmmController.java
 */
@Controller
public class CmmController {
	protected Log log = LogFactory.getLog(this.getClass());
	
	@Resource(name = "egovMessageSource")
	private EgovMessageSource egovMessageSource;
	
	@Resource(name = "cmmService")
	private CmmService cmmService;
	

	/**
     * 상단 메뉴 목록조회 
     */
    @RequestMapping(value="/CMM/hMenuList.do")
    public String getHeadMenuList(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		boolean success = true;
    	ValueMap result = new ValueMap();
    	//*****************************************************************************
		// 입력부 로그 출력 
		//*****************************************************************************
    	log.debug("===================================================================");
    	log.debug("[MenuMngController.getHeadMenuList() ================== start]");
		
    	
		//*****************************************************************************
		// 변수 및 객체 선언 및 초기화 
		//*****************************************************************************
		
		Map<String,Object> params = CommonUtils.getRequestMap(request);
		log.info("param >>>>> :: " + params);
//		if(!params.containsKey("MENU_SITE_ID") || CommonUtils.isNull((String)params.get("MENU_SITE_ID"))) {
//			params.put("MENU_SITE_ID", params.get("SITE_ID"));
//		}
		//*****************************************************************************
		// 비지니스 처리
		//*****************************************************************************		
    	
    	try{  	        
    		List<ValueMap> ds_head_list = cmmService.getHeadMenuList(params);
    		result.put("ds_head_list", ds_head_list);
    		
    		if(ds_head_list.size() > 0) {
	    		if(!params.containsKey("H_MENU_CD")) {  
	    			params.put("H_MENU_CD", ds_head_list.get(0).getString("MENU_CODE"));
	    		} else if(CommonUtils.isNull((String)params.get("H_MENU_CD"))) {
	    			params.remove("H_MENU_CD");
	    			params.put("H_MENU_CD", ds_head_list.get(0).getString("MENU_CODE"));
	    		}
	    		
	    		List<ValueMap> ds_left_list = cmmService.getLeftMenuList(params);
	    		result.put("ds_left_list", ds_left_list);
    		}
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
    
    /**
     * 왼쪽 메뉴 목록조회 
     */
    @RequestMapping(value="/CMM/lMenuList.do")
    public String getLeftMenuList(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		boolean success = true;
    	ValueMap result = new ValueMap();
    	//*****************************************************************************
		// 입력부 로그 출력 
		//*****************************************************************************
    	log.debug("===================================================================");
    	log.debug("[MenuMngController.getLeftMenuList() ================== start]");
		
    	
		//*****************************************************************************
		// 변수 및 객체 선언 및 초기화 
		//*****************************************************************************
		
		Map<String,Object> params = CommonUtils.getRequestMap(request);
		log.info("param >>>>> :: " + params);

//		if(!params.containsKey("MENU_SITE_ID") || CommonUtils.isNull((String)params.get("MENU_SITE_ID"))) {
//			params.put("MENU_SITE_ID", params.get("SITE_ID"));
//		}
		//*****************************************************************************
		// 비지니스 처리
		//*****************************************************************************		
    	
    	try{  	        
    		List<ValueMap> ds_left_list = cmmService.getLeftMenuList(params);
    		result.put("ds_left_list", ds_left_list);
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
    
    
    /**
     * 사이트 전체 메뉴 조회 
     */
    @RequestMapping(value="/CMM/menuList.do")
    public String getMenuList(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
    	
    	boolean success = true;
    	ValueMap result = new ValueMap();
    	//*****************************************************************************
    	// 입력부 로그 출력 
    	//*****************************************************************************
    	log.debug("===================================================================");
    	log.debug("[MenuMngController.getLeftMenuList() ================== start]");
    	
    	
    	//*****************************************************************************
    	// 변수 및 객체 선언 및 초기화 
    	//*****************************************************************************
    	
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	log.info("param >>>>> :: " + params);
    	
    	//*****************************************************************************
    	// 비지니스 처리
    	//*****************************************************************************
    	
    	try{  	        
    		List<ValueMap> ds_menu_list = cmmService.getMenuList(params);
    		result.put("ds_menu_list", ds_menu_list);
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