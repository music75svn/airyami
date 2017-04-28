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
package egovframework.airyami.login.service;

import java.util.Map;

import egovframework.airyami.cmm.util.ValueMap;


/**
 * 
 * 어드민 사이트의 로그인 / 로그아웃을 관리한다.
 * 템플릿을 위한 추가코드도 반영되어 있음.
 * 
 * @author 배수한
 * @since 2015.08.07 
 * @version 1.0
 * @see
 *
 * <pre>
 *   
 *     수정일                  수정자                              수정내용
 *	-----------    ---------    ---------------------------
 *  2015.08.07      배수한             최초 생성
 *
 * </pre>
 */

public interface LoginService {
	
	
	/**
	 * admin 로그인 처리한다.
	 * @param params - 조회할 정보가 담긴 Map
	 * @return 글 목록
	 * @exception Exception
	 */
    ValueMap getAdminLoginInfo(Map<String,Object> params) throws Exception;
    
    /**
     * 로그인 처리한다.
     * @param params - 조회할 정보가 담긴 Map
     * @return 글 목록
     * @exception Exception
     */
    ValueMap getLoginInfo(Map<String,Object> params) throws Exception;
    
    /**
     * test
     * @param params - 조회할 정보가 담긴 Map
     * @return 글 목록
     * @exception Exception
     */
    ValueMap getTest(Map<String,Object> params) throws Exception;
    
    /**
     * 패스워드 실패 횟수를 저장한다.
     * @param params - 조회할 정보가 담긴 Map
     * @return 글 목록
     * @exception Exception
     */
    int updateFailCnt(Map<String,Object> params) throws Exception;
    
    /*
     * 로그인 정보를 저장한다.
     */
    int insertLoginLog(Map<String,Object> params) throws Exception;
    
}
