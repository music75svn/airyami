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

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import egovframework.airyami.cmm.service.CmmService;
import egovframework.airyami.cmm.service.CommCodeService;
import egovframework.airyami.cmm.util.CommonUtils;
import egovframework.airyami.cmm.util.UrlUtil;
import egovframework.com.cmm.EgovMessageSource;



/**  
 * @Class Name : InterceptorController.java
 */

public class InterceptorController extends HandlerInterceptorAdapter{
	protected Log log = LogFactory.getLog(this.getClass());
	
	@Resource(name = "egovMessageSource")
	private EgovMessageSource egovMessageSource;
	
	/** CmmService */
    @Resource(name = "cmmService")
    private CmmService cmmService;
    
    /** CommCodeService */
    @Resource(name = "commCodeService")
    private CommCodeService commCodeService;
    
//    @Resource(name="ehcache")
//    Ehcache gCache ;
	
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (log.isDebugEnabled()) {
        	log.debug("======================================          START         ======================================");
        	log.debug(" Request URI \t:  " + request.getRequestURI());
        }
        
        // 금지어 체크
//        String sFilter = checkFilterParams(request);
//        if(!CommonUtils.isNull(sFilter)){
//        	ValueMap result = new ValueMap();
//        	
//        	result.put("success", false);
//        	result.put("errMsg", sFilter + " " + egovMessageSource.getMessage("errors.forbid", CommonUtils.getLocale(request)));
////        	result.put("errMsg", sFilter + " 금지어를 사용하였습니다!!");
////        	result.put("msg", sFilter + " 금지어를 사용하였습니다!!");
//        	
//        	if( CommonUtils.isNull(request.getParameter("SID")) )
//            	return false;
//        	
//        	response.setContentType("text/xml;charset=UTF-8");
//        	response.getWriter().println(CommonUtils.setJsonResult(result));
//        	return false;
//        }
        //-- 금지어 체크
        
        

        // SITE_ID 에 따른 언어 변경시 셋팅 작업
//        String SITE_ID = (String)request.getParameter("SITE_ID");
//        if( !CommonUtils.isNull(SITE_ID) ){
//        	Locale myLocale = CommonUtils.getLocale(request);
//        	String sLocale = myLocale + "";
//        	log.debug(" sLocale :  " + sLocale);
//        	
//        	String tempLocale = "ko_KR";
//        	
//        	if("|GEW_EN|M_GEW_EN|".indexOf("|" + SITE_ID + "|") > -1){
//        		tempLocale = "en_US";
//        	}
//        	log.debug(" tempLocale :  " + tempLocale);
//        	if(!sLocale.equals(tempLocale)){
//        		HttpSession session = request.getSession();
//        		Locale locales = null;
//        		if (tempLocale.equals("ko_KR")) {
//        			locales = new Locale("ko", "KR");
//        		} else {
//        			locales = new Locale("en", "US");
//        		}
//        		
//        		session.setAttribute(
//        				SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locales);
//        	}
//        }
        
        return super.preHandle(request, response, handler);
    }
     
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    	log.debug("======================================           END          ======================================\n");
    	
    	try{
    		if (modelAndView != null) {
    			log.debug("modelAndView :: " + modelAndView.getViewName() );
    			if( "|excelView|downloadView|".indexOf("|" + modelAndView.getViewName() + "|") == -1){
    				log.debug("modelAndView ::"  + modelAndView);
    				Map model = modelAndView.getModel();
    				log.debug("model ::"  + model);
    				// 메뉴로그
    				Map<String,Object> paramMap = new HashMap<String,Object>();
    				String MENU_TYPE = (String)model.get("MENU_TYPE");
    				
//    				if( CommonUtils.isNull(SITE_ID) )
//    					SITE_ID = UrlUtil.getActionRoot(request); 
    				
    				// 메뉴로그
    				if(model.containsKey("MENUON")){
    					log.debug("menulog 남기자!!");
    					
    					String L_MENU_CD = (String)model.get("L_MENU_CD");
    					
    					paramMap.put("MENU_TYPE", MENU_TYPE);
    					paramMap.put("MENU_CODE", L_MENU_CD);
    					if(model.containsKey("LOGIN_ID"))
    						paramMap.put("LOGIN_ID", model.get("LOGIN_ID"));
    					
    					cmmService.insertCommDb(paramMap, "cmm.insertMenuLog");
    					
    					log.debug("modelAndView.getModel() ::"  + model.get("L_MENU_CD"));
    				}
    				
//    				// 상품별 접근 로그
//    				paramMap.clear();
//    				paramMap.put("SITE_ID", SITE_ID);
//    				paramMap.put("PAGE_PATH", modelAndView.getViewName());
//    				if(model.containsKey("LOGIN_ID"))
//    					paramMap.put("LOGIN_ID", model.get("LOGIN_ID"));
//    				
//    				//String ip = CommonUtils.getClientIP(request);
//    				
//    				paramMap.put("VISIT_IP", ip);
//    				cmmService.insertContentLog(paramMap);
//    				//-- 컨텐츠로그
//    				
//    				// 커뮤니티 방문 로그
//    				if(model.containsKey("COMMUNITY_SEQ")){
//    					log.debug("커뮤니티 방문 로그 대상!!");
//    					paramMap.put("COMMUNITY_SEQ", model.get("COMMUNITY_SEQ"));
//    					
//    					insertCommunityLog(paramMap);
//    				}
//    				//-- 커뮤니티 방문 로그
    			}
    		}
//    		else{
//    	    	log.debug("==============================response=====================================");
//    			ServletOutputStream outputStream = response.getOutputStream();
//    			
////    	    	log.debug("response.getOutputStream() : " + response.getWriter());
////    	    	log.debug("response.getOutputStream() : " + response.getOutputStream().toString());
//    		}
    		
    	}
    	catch(Exception e){
    		log.info(e.getMessage());
    	}
    }
    
    // 금지어가 포함되어 있는지 검색한다.
    // 존재하면 금지어를.. 없으면 공백을 리턴한다.
//    private String checkFilterParams(HttpServletRequest request) throws Exception{
//    	// 금지어 관련 내용을 cache 에 담아서 쓰기
//        String sFilters = "";		// 금지어 목록
//        Ehcache cache = gCache.getCacheManager().getCache("filterMem");
//        boolean bSearchNeed = true;
//        if (cache.isKeyInCache("FILTER")) {
//        	Element element = cache.get("FILTER");
//        	
//        	if(element != null){
//        		element.updateAccessStatistics();
//        		sFilters = (String)element.getObjectValue();
//        		bSearchNeed = false;
//        	} 
//        }
//        
//        if(bSearchNeed){
//        	Map<String,Object> paramMap = new HashMap<String,Object>();
//        	paramMap.put("GROUP_CODE", "FILTER");
//        	List<ValueMap> list = commCodeService.selectCommCode(paramMap);
//        	if(list != null){
//        		for(int i = 0; i < list.size(); i++){
//        			sFilters+= list.get(i).getString("CD_NM")+",";
//        		}
//        		
//        		Element element = new Element("FILTER", sFilters);
//        		cache.put(element);
//        	}
//        }
//        
//        Enumeration<String> parameter = request.getParameterNames();
//        String value = "";
//        while (parameter.hasMoreElements())
//        {
//            String name = parameter.nextElement();
//            value += request.getParameter(name) + ",";
//        }
//        
//        if(!CommonUtils.isNull(value) && !CommonUtils.isNull(sFilters)){
//        	String[] sFilterArr = sFilters.split(",");
//        	
//        	for(int i = 0; i < sFilterArr.length; i++){
//        		if(value.contains(sFilterArr[i]))
//        			return sFilterArr[i];
//        	}
//        }
//        
//        
//    	return "";
//    }
    
    
//    // 커뮤니티 방문 로그 작성
//    // 1. 하나의 사용자는 하루에 한번 가능하다.
//    // 2. 로그인 상황이 아니면 아이피당 하루에 한번 가능하다.
//    private void insertCommunityLog(Map<String,Object> paramMap){
//    	// 로그인 여부
//    	// 사용자 또는 아이피 존재 여부 확인
//		try{
//    		// 같은 사용자로 같은 커뮤니티애 로그가 있는지 확인 or 같은 아이피 존재여부 확인 - 미존재시 로그 생성
//    		int vCount = cmmService.getVisitCnt(paramMap);
//    		if(vCount == 0){
//    			// 방문 로그를 남기자
//    			cmmService.insertCommunityLog(paramMap);
//    		}
//		}
//    	catch(Exception e){
//    		log.info(e.getMessage());
//    	}
//    }

}
