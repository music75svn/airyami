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

	///////////////////////////////////////////////////////////////////////////////
	//공통 DAO by 유연주
	///////////////////////////////////////////////////////////////////////////////
	/***
	 * 공통 DAO 리스트 반환
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public List<ValueMap> getCommDbList ( Map<String,Object> params, String sql) throws Exception{
		return getSqlMapClientTemplate().queryForList(sql, params);
		
	};
	/***
	 * 공통 DAO Map 반환
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public ValueMap getCommDbMap ( Map<String,Object> params, String sql) throws Exception{
		return (ValueMap)getSqlMapClientTemplate().queryForObject(sql, params);
		
	};
	/**
	 * 공통 DAO Integer 반환
	 */
	public int getCommDbInt(Map<String,Object> params, String sql) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject(sql, params);
		
	};
	/**
	 * 공통 DAO String 반환
	 */
	public String getCommDbString(Map<String,Object> params, String sql) throws Exception{
		return (String)getSqlMapClientTemplate().queryForObject(sql, params);
		
	};
	
	/**
	 * 공통 DAO 등록
	 */
	public void insertCommDb( Map<String,Object> paramMap, String sql )  throws Exception{
		// TODO Auto-generated method stub
		 getSqlMapClientTemplate().insert(sql, paramMap);
	}
	
	/**
	 * 공통 DAO 수정
	 */
	public int updateCommDb( Map<String,Object> paramMap, String sql )  throws Exception{
		// TODO Auto-generated method stub
		return (Integer)getSqlMapClientTemplate().update(sql, paramMap);
	}
	
	/**
	 * 공통 DAO 삭제
	 */
	public int deleteCommDb( Map<String,Object> paramMap, String sql )  throws Exception{
		// TODO Auto-generated method stub
		return (Integer)getSqlMapClientTemplate().delete(sql, paramMap);
	}
}
