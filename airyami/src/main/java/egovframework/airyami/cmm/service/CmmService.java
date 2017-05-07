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
package egovframework.airyami.cmm.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import egovframework.airyami.cmm.util.ValueMap;


/**  
 * @Class Name : FileService.java
 *  Copyright (C) by MOPAS All right reserved.
 */
public interface CmmService {
	
	/*
	 * 메뉴 클릭시 접속 로그 남기기
	 */
	int insertMenuLog( Map<String,Object> paramMap )  throws Exception;
	
	/*
	 * 컨텐츠 로그 남기기
	 */
	int insertContentLog( Map<String,Object> paramMap )  throws Exception;
	
	
	/***
	 * 메뉴 > 상단메뉴 목록조회
	 * @param request
	 * @return
	 * @throws Exception
	 */
	List<ValueMap> getHeadMenuList ( Map<String,Object> params ) throws Exception;
	
	/***
	 * 메뉴 > 왼쪽메뉴 목록조회
	 * @param request
	 * @return
	 * @throws Exception
	 */
	List<ValueMap> getLeftMenuList ( Map<String,Object> params ) throws Exception;
	
	
	/***
	 * 메뉴 > 사이트별 조회
	 * @param request
	 * @return
	 * @throws Exception
	 */
	List<ValueMap> getMenuList ( Map<String,Object> params ) throws Exception;
	
}
