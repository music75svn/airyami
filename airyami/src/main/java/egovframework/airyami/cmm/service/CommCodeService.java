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

import egovframework.airyami.cmm.util.ValueMap;


/**
 * 공통코드에 관한 서비스 인터페이스 클래스를 정의한다.
 * @author 배수한
 * @since 2017.04.27
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *     수정일                  수정자                              수정내용
 *	-----------    ---------    ---------------------------
 *  2017.04.27      배수한             최초 생성
 *
 * </pre>
 */

public interface CommCodeService {
	
	
	/**
	 * 공통코드 목록을 총건수를 조회한다.
	 */
    int listCommCodeCnt(Map<String,Object> params) throws Exception;
    
    /**
     * 공통코드 목록을 조회한다(paging).
     */
    List<ValueMap> listCommCode(Map<String,Object> params) throws Exception;
    
    /**
     * 공통코드 목록을 조회한다.
     */
    List<ValueMap> selectCommCode(Map<String,Object> params) throws Exception;
    
    /**
     * 공통코드 상세를 조회한다.
     */
    ValueMap getCommCode(Map<String,Object> params) throws Exception;
    
    /**
     * 공통코드 목록을 조회한다.
     */
    List<ValueMap> getCommcodeCombo(Map<String,Object> params) throws Exception;
    
}
