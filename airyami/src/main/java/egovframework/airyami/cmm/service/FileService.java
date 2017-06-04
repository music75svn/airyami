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

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import egovframework.airyami.cmm.util.ValueMap;


/**  
 * @Class Name : FileService.java
 *  Copyright (C) by MOPAS All right reserved.
 */
public interface FileService {
	
	
	BigDecimal selectNewMasterId(Map<String,Object> paramMap)  throws Exception;
    
    void insertFile(ValueMap paramMap) throws RuntimeException;
    
    void insertFileDownLoadLog(Map<String,Object> paramMap) throws Exception;
    
    List<ValueMap> selectFileList( Map<String,Object> paramMap )  throws Exception;
    
    void deleteFile( Map<String,Object> paramMap )  throws Exception;
    
    void deleteFileByMst( String sMasterId, String sDtlIds ) throws Exception;
    
    BigDecimal insertAttachFiles(List<ValueMap> listFileVO, BigDecimal pMasterId, Map<String, Object> paramMap) throws Exception;
    
    BigDecimal copyFilesByMstId(Map<String, Object> paramMap) throws Exception;
    
    List<ValueMap> parseFileInfo(Map<String, MultipartFile> files, BigDecimal pMasterId, Map<String,Object> paramMap) throws Exception;
    
    ValueMap attachFiles(Map<String, MultipartFile> files, String strMasterId, Map<String,Object> paramMap, ValueMap ds_boardInfo);
    
    ValueMap attachImgFiles(Map<String, MultipartFile> files, String strMasterId, Map<String,Object> paramMap);
    
}
