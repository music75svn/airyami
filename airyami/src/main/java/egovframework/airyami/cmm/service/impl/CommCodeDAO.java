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


@Repository("commCodeDAO")
public class CommCodeDAO extends EgovAbstractDAO {

	
	/**
	 * 공통코드 목록을 총건수를 조회한다.
	 */
    public int listCommCodeCnt(Map params) throws Exception {
    	return (Integer)getSqlMapClientTemplate().queryForObject("commcode.listCommCodeCnt", params);
    }
    
    /**
     * 공통코드 목록을 조회한다.(paging)
     */
    public List<ValueMap> listCommCode(Map params) throws Exception {
    	return getSqlMapClientTemplate().queryForList("commcode.listCommCode", params);
    }
    
    /**
     * 공통코드 목록을 조회한다.
     */
    public List<ValueMap> selectCommCode(Map params) throws Exception {
    	return getSqlMapClientTemplate().queryForList("commcode.selectCommCode", params);
    }
    
    /**
     * 공통코드 목록 상세를 조회한다.
     */
    public ValueMap getCommCode(Map params) throws Exception {
    	return (ValueMap)getSqlMapClientTemplate().queryForObject("commcode.getCommCode", params);
    }
    
    /**
     * 공통코드Grp 상세를 조회한다.
     */
    public ValueMap getCodeGrpDetail(Map<String,Object> params) throws Exception{
    	return (ValueMap)getSqlMapClientTemplate().queryForObject("commcode.getCodeGrpDetail", params);
    }
    
    /**
     * 공통코드 목록을 조회한다.
     */
    public List<ValueMap> getCommcodeCombo(Map params) throws Exception {
		return (List<ValueMap>) list("commcode.getCommcodeCombo", params );
    }
    
    /**
     * 코드그룹 테이블 저장
     */
    public int saveCodeGrp(Map<String,Object> params) throws Exception{
    	return getSqlMapClientTemplate().update("commcode.saveCodeGrp" ,params);
    }
    
}
