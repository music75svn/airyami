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

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import egovframework.airyami.cmm.service.CommCodeService;
import egovframework.airyami.cmm.util.ValueMap;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

/**
 * 공통코드에 대한 서비스 구현클래스를 정의한다
 * @author 배수한
 * @since 2015.08.06
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *     수정일                  수정자                              수정내용
 *	-----------    ---------    ---------------------------
 *  2015.08.06      배수한             최초 생성
 *
 * </pre>
 */

@Service("commCodeService")
public class CommCodeServiceImpl extends AbstractServiceImpl implements
        CommCodeService {
	protected Log log = LogFactory.getLog(this.getClass());
	
	/** DAO */
    @Resource(name="commCodeDAO")
    private CommCodeDAO commCodeDAO;
	

	/**
	 * 공통코드 목록을 총건수를 조회한다.
	 */
	public int listCommCodeCnt(Map<String, Object> params)
			throws Exception {
		return commCodeDAO.listCommCodeCnt(params);
	}
	
	/**
	 * 공통코드 목록을 조회한다.
	 */
	public List<ValueMap> listCommCode(Map<String, Object> params)
			throws Exception {
		return commCodeDAO.listCommCode(params);
	}
	
	/**
	 * 공통코드 목록을 조회한다.
	 */
	public List<ValueMap> selectCommCode(Map<String, Object> params)
			throws Exception {
		return commCodeDAO.selectCommCode(params);
	}
	
	/**
	 * 공통코드 목록을 조회한다.
	 */
	public ValueMap getCommCode(Map<String, Object> params)
			throws Exception {
		return commCodeDAO.getCommCode(params);
	}
	
	/**
     * 공통코드Grp 상세를 조회한다.
     */
    public ValueMap getCodeGrpDetail(Map<String,Object> params) throws Exception{
    	return commCodeDAO.getCodeGrpDetail(params);
    }
	
	/**
	 * 공통코드 목록을 조회한다.
	 */
	public List<ValueMap> getCommcodeCombo(Map<String, Object> params)
			throws Exception {
		return commCodeDAO.getCommcodeCombo(params);
	}
	
	/**
     * 코드그룹 테이블 저장
     */
    public int saveCodeGrp(Map<String,Object> params) throws Exception{
    	return commCodeDAO.saveCodeGrp(params);
    }
    
}
