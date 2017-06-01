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
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.airyami.cmm.util.ValueMap;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;


/**  
 * @Class Name : FileDAO.java
 *  Copyright (C) by MOPAS All right reserved.
 */

@Repository("fileDAO")
public class FileDAO extends EgovAbstractDAO {
    
    
//	/**
//	 * 파일 마스터 시퀀스를 읽어온다.
//	 */
//	public BigDecimal selectNewMasterId() throws Exception {
//		return (BigDecimal)getSqlMapClientTemplate().queryForObject("file.selectNewMasterId");
//	}
	
	/**
	 * 파일 세부 번호를  읽어온다.
	 */
	public BigDecimal selectNewDetailId(BigDecimal pMasterId) throws Exception {
		return (BigDecimal)getSqlMapClientTemplate().queryForObject("file.selectNewDetailId", pMasterId);
	}
	
	
	/**
	 * 파일정보를 등록한다.
	 */
	public int insertFile(Map<String,Object> params) throws Exception {
		return (Integer)getSqlMapClientTemplate().update("file.insertFile", params);
	}
	
	/**
	 * 파일정보를 복사한다.
	 */
	public int copyFilesByMstId(Map<String,Object> params) throws Exception {
		return (Integer)getSqlMapClientTemplate().update("file.copyFilesByMstId", params);
	}
	
    /**
     * 파일을 삭제한다.
     */
    public int deleteFile(Map<String,Object> params) throws Exception {
    	return (Integer)getSqlMapClientTemplate().update("file.deleteFile", params);
    }
        
    /**
     * file 정보를 읽어온다.
     */
    public List<ValueMap> selectFileList(Map<String,Object> params) throws Exception {
    	return getSqlMapClientTemplate().queryForList("file.selectFileList", params);
    }
    
    /**
     * file download log 를 저장한다..
     */
    public void insertFileDownLoadLog(Map<String,Object> params) throws Exception {
    	getSqlMapClientTemplate().insert("file.insertFileDownLoadLog", params);
    }
    
}
