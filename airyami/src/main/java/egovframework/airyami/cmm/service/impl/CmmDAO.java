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
package egovframework.airyami.cmm.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.airyami.cmm.util.ValueMap;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;


/**  
 * @Class Name : CmmDAO.java
 *  Copyright (C) by MOPAS All right reserved.
 */

@Repository("cmmDAO")
public class CmmDAO extends EgovAbstractDAO {
    
	/**
	 * 
	 */
	public int insertMenuLog(Map paramMap) throws Exception {
		return (Integer)getSqlMapClientTemplate().update("cmm.insertMenuLog", paramMap);
	}
	
	/**
	 * 컨텐츠 로그 남기기
	 */
	public int insertContentLog(Map paramMap) throws Exception {
		return (Integer)getSqlMapClientTemplate().update("cmm.insertContentLog", paramMap);
	}
	
	
	/***
	 * 메뉴 > 메뉴 목록조회
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public List<ValueMap> getHeadMenuList ( Map<String,Object> params ) throws Exception {
		return (List<ValueMap>) list("cmm.getHeadMenuList", params );
		
	};

	/***
	 * 메뉴 > 메뉴 목록조회
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public List<ValueMap> getLeftMenuList ( Map<String,Object> params ) throws Exception {
		return (List<ValueMap>) list("cmm.getLeftMenuList", params );
		
	};
	
	/***
	 * 메뉴 > 메뉴 목록조회
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public List<ValueMap> getMenuList ( Map<String,Object> params ) throws Exception {
		return (List<ValueMap>) list("cmm.getMenuList", params );
		
	};

}
