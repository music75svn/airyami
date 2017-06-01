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

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;


/**  
 * @Class Name : FileDAO.java
 *  Copyright (C) by MOPAS All right reserved.
 */

@Repository("keyDAO")
public class KeyDAO extends EgovAbstractDAO {
    
    
	/**
	 * 채번테이블에서 해당하는 키값을 읽어온다.  select for update 
	 */
	public BigDecimal selectForKey(Map<String, Object> paramMap) throws Exception {
		return (BigDecimal)getSqlMapClientTemplate().queryForObject("autokey.selectForKey", paramMap);
	}
	
	/**
	 * 신규 키종류일경우 신규로 입력한다.
	 */
	public int insertKey(Map paramMap) throws Exception {
		return (Integer)getSqlMapClientTemplate().update("autokey.insertKey", paramMap);
	}
	
	/**
	 * 키종류가 존재하는 체크한다.
	 */
	public int checkKey(Map paramMap) throws Exception {
		return (Integer)getSqlMapClientTemplate().queryForObject("autokey.checkKey", paramMap);
	}
	
	/**
	 * 채번테이블에 사용된 순번을 업데이트 한다.
	 */
	public int updateKey(Map paramMap) throws Exception {
		return (Integer)getSqlMapClientTemplate().update("autokey.updateKey", paramMap);
	}
	
	
    
}
