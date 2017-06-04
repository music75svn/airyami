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
package egovframework.airyami.cmm.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.archivers.zip.ZipUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.airyami.cmm.service.FileService;
import egovframework.airyami.cmm.util.CommonUtils;
import egovframework.airyami.cmm.util.ValueMap;
import egovframework.com.cmm.service.EgovProperties;



/**  
 * @Class Name : DownloadController.java
 */

@Controller
public class DownloadController {
	protected Log log = LogFactory.getLog(this.getClass());
	
	/** FileService */
    @Resource(name = "fileService")
    private FileService fileService;
   
    
    /**
     * FILE DOWNLOAD 
     * @return (ValueMap)result
     * @exception Exception
     */
    @RequestMapping(value="/COM/download.do")
    public String download(HttpServletRequest request, HttpServletResponse response, 
    		ModelMap model) throws Exception {
    	
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	log.info("param :: " + params);
    	
    	String strSavePath = "";
    	String strRealNm = "";
    	
    	try{
    		if(params.containsKey("FILE_MST_SEQ")){
    			List<ValueMap> fileInfos = fileService.selectFileList(params);
    			if(fileInfos == null){
    				return null;
    			}    			
    			
    			if(fileInfos.size() > 1)
    			{
    				//EgovProperties.getProperty("Globals.fileStorePath") + "download" + File.separator + "download" + CommonUtils.getTimeStamp() + ".zip";
    				//여러파일 => zip으로 압축
    				strSavePath = EgovProperties.getProperty("Globals.fileStorePath") + File.separator + "download";
    				
    				String strSaveFile = "download_" + CommonUtils.getTimeStamp() + ".zip";
    				
    				List<String> fileList = new ArrayList();
    				
    				for(ValueMap fileInfo : fileInfos)
    				{
    					fileList.add(fileInfo.getString("FILE_PATH"));
    				}
    				
    				//ZipUtil.zip(fileList, strSavePath, strSaveFile);
    				
    				strSavePath += File.separator + strSaveFile;
    						
    				strRealNm = "download.zip";
    			}
    			else
    			{
	    			strSavePath = fileInfos.get(0).getString("FILE_PATH");
	    			strRealNm	= fileInfos.get(0).getString("ORG_FILE_NAME");
    			}
    		}
    		else{
    			strSavePath = (String)params.get("FILE_PATH");
    			strRealNm	= (String)params.get("ORG_FILE_NAME");
    		}
    		
    		
    		// log 남기기..
    		fileService.insertFileDownLoadLog(params);
    		
    		File file = new File(strSavePath);
    		model.addAttribute("downloadFile", file);
    		model.addAttribute("realFileName", strRealNm);
    	}
    	catch(Exception e){
    		e.printStackTrace();
    		System.out.println(e.getMessage());
    	}
    	
    	return "downloadView";
    }
    
    /**
     * FILE DOWNLOAD 
     * @return (ValueMap)result
     * @exception Exception
     */
    @RequestMapping(value="/COM/prodImgDownload.do")
    public String prodImgDownload(HttpServletRequest request, HttpServletResponse response, 
    		ModelMap model) throws Exception {
    	
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	log.info("param :: " + params);
    	
    	String strSavePath = "";
    	String strRealNm = "";
    	
    	try{
    		// 하나의 상품에 관련된 모든 이미지 다운로드.. 일단 패스
//    		if(params.containsKey("PROD_NO")){
//    			List<ValueMap> fileInfos = fileService.selectFileList(params);
//    			if(fileInfos == null){
//    				return null;
//    			}    			
//    			
//    			if(fileInfos.size() > 1)
//    			{
//    				//EgovProperties.getProperty("Globals.fileStorePath") + "download" + File.separator + "download" + CommonUtils.getTimeStamp() + ".zip";
//    				//여러파일 => zip으로 압축
//    				strSavePath = EgovProperties.getProperty("Globals.fileStorePath") + File.separator + "download";
//    				
//    				String strSaveFile = "download_" + CommonUtils.getTimeStamp() + ".zip";
//    				
//    				List<String> fileList = new ArrayList();
//    				
//    				for(ValueMap fileInfo : fileInfos)
//    				{
//    					fileList.add(fileInfo.getString("FILE_PATH"));
//    				}
//    				
//    				//ZipUtil.zip(fileList, strSavePath, strSaveFile);
//    				
//    				strSavePath += File.separator + strSaveFile;
//    				
//    				strRealNm = "download.zip";
//    			}
//    			else
//    			{
//    				strSavePath = fileInfos.get(0).getString("FILE_PATH");
//    				strRealNm	= fileInfos.get(0).getString("ORG_FILE_NAME");
//    			}
//    		}
    		//else{
    			strSavePath = (String)params.get("FILE_PATH");
    			strRealNm	= (String)params.get("ORG_FILE_NAME");
    		//}
    		
    		
    		// log 남기기..
    		//fileService.insertFileDownLoadLog(params);
    		
    		File file = new File(strSavePath);
    		model.addAttribute("downloadFile", file);
    		model.addAttribute("realFileName", strRealNm);
    	}
    	catch(Exception e){
    		e.printStackTrace();
    		System.out.println(e.getMessage());
    	}
    	
    	return "downloadView";
    }

    /**
     * FILE DOWNLOAD 
     * @return (ValueMap)result
     * @exception Exception
     */
    @RequestMapping(value="/COM/filemove.do")
    public String filemove(HttpServletRequest request, HttpServletResponse response, 
    		ModelMap model) throws Exception {
    	
    	boolean success = true;
    	ValueMap result = new ValueMap();
    	
    	Map<String,Object> params = CommonUtils.getRequestMap(request);
    	log.info("param :: " + params);
    	
    	String strSavePath = "";
    	String strRealNm = "";
    	String strImagePath = "";
    	String strImageNm = "";
    	
    	try{
    		if(params.containsKey("SCREEN_FILE_ID")){
    			params.put("FILE_MST_SEQ", (String)params.get("SCREEN_FILE_ID"));
    			List<ValueMap> fileInfos = fileService.selectFileList(params);
    			if(fileInfos == null){
    				return null;
    			}
    			
    			// dtl 이 한개일경우   여러개짜린 다시 생각하자.
    			strSavePath = fileInfos.get(0).getString("FILE_PATH");
    			strRealNm	= fileInfos.get(0).getString("ORG_FILE_NAME");
    			
    			strImagePath = "/home/jboss/jboss-6.1.0.Final/server/default/deploy/ROOT.war/tempimages/";
    			//strImagePath = "/kisaPrj/workspace/KPM/src/main/webapp/upload/";
    			
    			// img 시간별 이름으로 생성
    			int index = strRealNm.lastIndexOf(".");
                String fileExt = strRealNm.substring(index + 1);
                strImageNm = CommonUtils.getTimeStamp()+"."+fileExt;
    			
    			log.debug("strImagePath1 :: " + strImagePath);
    			fileDelete(strImagePath);
    			fileMove(strSavePath, strImagePath + strImageNm);
    			
    			// jboss경로로 변경
    			strImagePath = strImagePath.replaceAll("/home/jboss/jboss-6.1.0.Final/server/default/deploy/ROOT.war", "");
    			log.debug("strImagePath2 :: " + strImagePath);
    			
    			result.put("imgPath", strImagePath + strImageNm);
    		}
    		else{
    			strSavePath = (String)params.get("FILE_PATH");
    			strRealNm	= (String)params.get("ORG_FILE_NAME");
    		}
    		
    		
    		/*File file = new File(strSavePath);
    		model.addAttribute("downloadFile", file);
    		model.addAttribute("realFileName", strRealNm);*/
    	}
    	catch(Exception e){
    		e.printStackTrace();
    		System.out.println(e.getMessage());
    	}
    	
    	result.put("success", success);
    	response.setContentType("text/xml;charset=UTF-8");
    	response.getWriter().println(CommonUtils.setJsonResult(result));
    	
    	return null;
    }
    
    
    // 특정일 이전 파일 삭제
    public void fileDelete(String strDir) {
    	File f1 = new File(strDir);
        String[] list = f1.list();
        String strToday = CommonUtils.getToday("yyyyMMdd");
        
        log.debug("strToday :: " + strToday);
        
        for(int i =0; i<list.length; i++){
           File f2 = new File(strDir, list[i]);
           
           
           if(f2.isDirectory()){
        	   log.debug("디렉토리 : " + list[i]);
           }else{
        	   String fileNm = f2.getName();
        	   
        	   if( fileNm.indexOf(strToday) == -1){
        		   if(f2.delete()){
        			   log.debug(list[i] + " 파일 삭제 성공!!!");
        		   }
        		   else{
        			   log.debug(list[i] + " 파일 삭제 실패!!!");
        		   }
        	   }
        		   
           }
       }
    }
    
    // 파일을 이동하는 메소드
	public void fileMove(String inFileName, String outFileName) {
		try {
			
			File outFile = new File(outFileName);

			FileInputStream fis = new FileInputStream(inFileName);
			FileOutputStream fos = new FileOutputStream(outFile);

			int data = 0;
			while ((data = fis.read()) != -1) {
				fos.write(data);
			}
			fis.close();
			fos.close();

			// 복사한뒤 원본파일을 삭제함
			//fileDelete(inFileName);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    

}
