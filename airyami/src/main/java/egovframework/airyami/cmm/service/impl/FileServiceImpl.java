package egovframework.airyami.cmm.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import egovframework.airyami.cmm.service.FileService;
import egovframework.airyami.cmm.service.KeyService;
import egovframework.airyami.cmm.util.CommonUtils;
import egovframework.airyami.cmm.util.Constants;
import egovframework.airyami.cmm.util.FileUtil;
import egovframework.airyami.cmm.util.ThumbnailUtil;
import egovframework.airyami.cmm.util.ValueMap;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

@Service("fileService")
public class FileServiceImpl extends AbstractServiceImpl implements FileService
{
	protected Log log = LogFactory.getLog(this.getClass());
	private Logger logger = Logger.getLogger(this.getClass());
	
	/** KeyService */
    @Resource(name = "keyService")
    private KeyService keyService;

	/** DAO */
    @Resource(name="fileDAO")
    private FileDAO fileDAO;
    
    @Resource(name = "egovMessageSource")
	private EgovMessageSource egovMessageSource;
    
    
    public ValueMap attachFiles(Map<String, MultipartFile> files, String strMasterId, Map<String, Object> paramMap, ValueMap ds_boardInfo){
		// TODO Auto-generated method stub
		ValueMap result = new ValueMap();
		BigDecimal masterId = null;
		List<ValueMap> parseResult = null;
		
		Map<String,Object> params = new HashMap();
		
		try{
			
			boolean checkFileSize = true;
			boolean checkFileExtension = true;
			
			logger.debug("ds_boardInfo :: " + ds_boardInfo);
			// validation 체크
			if(ds_boardInfo != null){
				logger.debug("FILE_ALLOW_EXT :: " + (String)ds_boardInfo.get("FILE_ALLOW_EXT"));
				logger.debug("FILE_LIMIT_SIZE :: " + ds_boardInfo.getInteger("FILE_LIMIT_SIZE"));
				if( !CommonUtils.isNull( ds_boardInfo.getInteger("FILE_LIMIT_SIZE").toString() ) ){
					if(Long.parseLong( ds_boardInfo.getInteger("FILE_LIMIT_SIZE").toString()) > 0)
						checkFileSize = FileUtil.checkFileSize(files, Long.parseLong(ds_boardInfo.get("FILE_LIMIT_SIZE").toString()));
				}
				if( !CommonUtils.isNull((String)ds_boardInfo.get("FILE_ALLOW_EXT")) && !"*".equals((String)ds_boardInfo.get("FILE_ALLOW_EXT"))){
					checkFileExtension = FileUtil.checkFileExtension(files, (String)ds_boardInfo.get("FILE_ALLOW_EXT"));
				}
					
				if(!checkFileExtension || !checkFileSize){
					if(!checkFileExtension){
						result.put("message", egovMessageSource.getMessage("fail.common.file.extension"));
					}else if(!checkFileSize){
						result.put("message", egovMessageSource.getMessage("fail.common.file.size"));
					}
	//				if("Y".equals(boardManagementInfo.getCategory_yn())){
	//		    		AdminBoardCategory boardCategory = new AdminBoardCategory();
	//		    		boardCategory.setSite_id(adminBoard.getSite_id());
	//		    		boardCategory.setBoard_id(adminBoard.getBoard_id());
	//		    		List boardCategoryList = adminBoardCategoryService.selectBoardCategory(boardCategory);
	//		    		model.addAttribute("boardCategoryList", boardCategoryList);
	//	        	}
					
					return result;
				
				}
			}	
			
			// MasterId 가 없으면 새로 구한다.
			if( CommonUtils.isNull(strMasterId) || strMasterId.equals("0")){
				masterId = selectNewMasterId(paramMap);
			}
			else{
				masterId = new BigDecimal( strMasterId );
			}
			
			logger.debug("masterId :: " + masterId);
			parseResult = parseFileInfo(files, masterId, paramMap);
//			logger.info( "parseResult" +  parseResult);
			
			for(int i = 0; i < parseResult.size(); i++) {
				if(!(masterId == null)) {
					params.clear();
					params.put( "FILE_MST_SEQ", masterId);
					params.put( "FILE_DTL_SEQ", parseResult.get( i ).get( "FILE_DTL_SEQ" ) );
					deleteFile( params );
				}
			}
			
			insertAttachFiles(parseResult, masterId, paramMap);
			result.put("success", true);
			result.put("URL_PATH", parseResult.get(0).get("URL_PATH"));
			result.put("FILE_MST_SEQ", masterId);
		}
		catch(Exception e){
			result.put("success", false);
			logger.debug(e.getMessage());
			logger.debug(e.getStackTrace());
		}
		
		logger.debug("result :: " + result);
		
		return result;
	}
    
    public BigDecimal copyFilesByMstId(Map<String, Object> paramMap)
			throws Exception {
		// TODO Auto-generated method stub
    	// mstid 존재 확인
    	if(!paramMap.containsKey("FILE_MST_SEQ"))
    		return null;
    	
    	// mstid 채번
    	BigDecimal masterId = selectNewMasterId(paramMap);
    	paramMap.put("NEW_FILE_MST_SEQ", masterId);
    	
    	fileDAO.copyFilesByMstId(paramMap);
    	
    	// 디비만 복사
		return masterId;
	}
	
    
    // file master seq 를 채번한다.
    public BigDecimal selectNewMasterId(Map<String, Object> paramMap) throws Exception {
    	paramMap.put("KEY_KIND", "TB_FILE");
        return keyService.getKey(paramMap);
    }
    
    public BigDecimal insertAttachFiles(List<ValueMap> listFileVO, BigDecimal masterId, Map<String, Object> paramMap) throws Exception {
    	String userId = (String)paramMap.get("LOGIN_ID");
        BigDecimal fileMasterId = null;
        
//        ValueMap aFile = null;
        
        if(!listFileVO.isEmpty()){
            for (int i = 0; i < listFileVO.size(); i++) {
            	ValueMap aFile = listFileVO.get(i);
                aFile.put( "FILE_MST_SEQ", masterId );
                aFile.put( "LOGIN_ID", userId );
                
                logger.info( "fileService::" + listFileVO.toString() );
                logger.info( "aFile::" + aFile );
                
                fileDAO.insertFile(aFile);
            }
        }
                
        return fileMasterId;
    }
    
    public List<ValueMap> selectFileList( Map<String,Object> paramMap ) throws Exception {
    	return fileDAO.selectFileList(paramMap);
    }
    
    public void deleteFile( Map<String,Object> paramMap ) throws Exception  {
        fileDAO.deleteFile(paramMap);
    }

	public void insertFile(ValueMap paramMap) throws RuntimeException {
		// TODO Auto-generated method stub
		
	}
	
	public void insertFileDownLoadLog(Map<String,Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		fileDAO.insertFileDownLoadLog(paramMap);
	}

	public void deleteFileByMst(String sMasterId, String sDtlIds) throws Exception {
		// TODO Auto-generated method stub
		if(CommonUtils.isNull(sDtlIds)){
			return;
		}
		
		String[] sDtlArr = sDtlIds.split("\\|");
		Map<String,Object> params = new HashMap();
		
		for( int i = 0; i < sDtlArr.length; i++) {
			params.clear();
			params.put( "FILE_MST_SEQ", sMasterId);
			params.put( "FILE_DTL_SEQ", sDtlArr[i] );
			deleteFile( params );
		}
	}
	
	public List<ValueMap> parseFileInfo(Map<String, MultipartFile> files, BigDecimal pMasterId, Map<String,Object> paramMap) throws Exception {
        BigDecimal detailId = null;

        String separator = "";
        String osName = System.getProperty("os.name").toLowerCase();
        if(osName.indexOf("win")>=0)
        	separator = "/";
        else
        	separator = java.io.File.separator;
        
        
        String storePathString = "";
        
        
        Iterator<Entry<String, MultipartFile>> itr = files.entrySet().iterator();
        MultipartFile file;
        String filePath = "";
        List<ValueMap> result =new ArrayList<ValueMap>();
        ValueMap fvo;
        int idx = 0;
        
        String mainFolderId = "";
        if(paramMap.containsKey("FOLDER_NM"))
        	mainFolderId = (String)paramMap.get("FOLDER_NM");
        
        if(paramMap.containsKey("COMMUNITY_SEQ")){
        	String sSeparator = "";
        	if(!CommonUtils.isNull(mainFolderId))
        		sSeparator = File.separator;
        	
        	mainFolderId += sSeparator + (String)paramMap.get("COMMUNITY_SEQ");
        }
        
        if(paramMap.containsKey("BOARD_ID")){
        	String sSeparator = "";
        	if(!CommonUtils.isNull(mainFolderId))
        		sSeparator = File.separator;
        	
        		mainFolderId += sSeparator + (String)paramMap.get("BOARD_ID");
        }
        
        if(CommonUtils.isNull(mainFolderId))
        	mainFolderId = "etc";
        
        
        while(itr.hasNext()) {
            idx++;
            
            Entry<String, MultipartFile> entry = itr.next();
            
            file = entry.getValue();
            String originalFileName = file.getOriginalFilename();
            
            if("".equals(originalFileName)) {
            	continue;
            }
            
            
            String fileType = "";
            logger.debug("file.getName() :: " + file.getName());
            String sfileKindC = "";
            String sUrlPath = "";
            
            if(file.getName().indexOf("title_img_file") > -1){
            	fileType = "2";
            	sfileKindC = "0";
            }
            else if(file.getName().indexOf("sub_img_file") > -1){
            	fileType = "2";
            	sfileKindC = "9";
            } else if(file.getName().indexOf("img_file") > -1){
            	fileType = "2";
            } else if(file.getName().indexOf("video_file") > -1){
            	fileType = "3";
            } else{
            	fileType = "1";
            }
            
            
            if("2".equals(fileType)){
            	storePathString = EgovProperties.getProperty("Globals.imgFileStorePath") + mainFolderId + File.separator;
            	sUrlPath = File.separator + "upload" + File.separator + "img" + File.separator + mainFolderId + File.separator;
            }else if("3".equals(fileType)){
            	storePathString = EgovProperties.getProperty("Globals.uccFileStorePath") + mainFolderId + File.separator;
            }else{
            	storePathString = EgovProperties.getProperty("Globals.fileStorePath") + mainFolderId + File.separator;
            }
            
            //storePathString = "/upload2/";
            
            logger.debug("storePathString :: " + storePathString);
            File fSavedFolder = new File(storePathString);
            if(!fSavedFolder.exists() || fSavedFolder.isFile()) {
            	fSavedFolder.mkdirs();
            	logger.debug("storePathString :: " + storePathString);
            }
            
            
            
            int index = originalFileName.lastIndexOf(".");
            String fileExt = originalFileName.substring(index + 1);
            String newName = CommonUtils.getTimeStamp()+"."+fileExt;
            long fileSize = file.getSize();

            if(!"".equals(originalFileName)) {
                filePath = storePathString + newName;
                if("2".equals(fileType)){
                	logger.debug(" File.separator ::  " + File.separator);
                	if("0".equals(sfileKindC))	//대표이미지일 경우 thumbnail prefix 생성
                		sUrlPath = (sUrlPath + Constants.thumbnailPrefix + newName).replaceAll(Matcher.quoteReplacement(File.separator), "/");
                	else
                		sUrlPath = (sUrlPath + newName).replaceAll(Matcher.quoteReplacement(File.separator), "/");
                	logger.debug(" sUrlPath :: " + sUrlPath);
                }
                file.transferTo(new File(filePath));
                
                /* 대표이미지일 경우 thumbnail 생성 */
                if("0".equals(sfileKindC))
                {
                	String thumbnailFilePath = storePathString + Constants.thumbnailPrefix + newName;
                
                	ThumbnailUtil.imageScale(filePath, thumbnailFilePath, Constants.thumbnailWidth, Constants.thumbnailHeight);
                }
            }
            
            if(detailId == null){
            	detailId = fileDAO.selectNewDetailId(pMasterId);
            }
            else{
            	detailId = detailId.add(new BigDecimal(1));
            }
            
            logger.debug("detailId :: " + detailId);
            
            fvo = new ValueMap();
            fvo.put( "FILE_MST_SEQ",  pMasterId);
            fvo.put( "FILE_DTL_SEQ",  detailId);
            fvo.put( "ATTACH_FILE_KIND_C",  (CommonUtils.isNull(sfileKindC)) ? fileType : sfileKindC);
            fvo.put( "SAVE_FILE_NAME", newName );
            fvo.put( "ORG_FILE_NAME", originalFileName );
            fvo.put( "FILE_PATH", filePath);
            fvo.put( "URL_PATH", sUrlPath);
            fvo.put( "FILE_EXT", fileExt );
            fvo.put( "FILE_SIZE", fileSize );
            result.add(fvo);
        }
        
        return result;
    }
	
}
