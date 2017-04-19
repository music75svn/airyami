package com.lexken.framework.common;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.config.FileConfig;
import com.lexken.framework.util.CalendarHelper;
import com.lexken.framework.util.FileHelper;
import com.lexken.framework.util.FileManager;
import com.lexken.framework.util.HtmlHelper;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

public class FileUpload {
	// Logger
	private final Log logger = LogFactory.getLog(getClass());
	
	private HttpServletRequest REQUEST 	= null;					// request
	private int SIZE_LIMIT 				= 700 * 1024 * 1024;	// 제한용량
	private String ADD_PATH				= "";					// 추가 경로
	private String ORG_FILE_TYPE		= "xls|xlsx|ppt|pptx|doc|docx|hwp|pdf|zip|txt|gif|jpg|png|jpeg";					// 허용 파일타입
	
	private int FILE_COUNT				= 0;					// 업로드 파일 갯수
	private ArrayList FILE_OBJECT_NAME  = new ArrayList();		// 파일 오브젝트 네임
	private ArrayList FILE_UPLOAD_PATH	= new ArrayList();		// 파일업로드 경로
	private ArrayList REAL_FILE_NAME	= new ArrayList();		// 원본파일명
	private ArrayList MASK_FILE_NAME	= new ArrayList();		// 변경된 파일 명
	private ArrayList FILE_URL			= new ArrayList();		// 파일 URL
	private ArrayList FILE_SIZE     	= new ArrayList();		// 파일 크기
	
	private ArrayList SEARCHMAP_FILE_LIST = new ArrayList();	// SEARCHMAP 용 파일 리스트 
	
	private HashMap<String, Integer>  FILE_OBJECT_MAP	= new HashMap<String, Integer>();
	private HashMap  paramMap;						//파라미터 맵
	
	private int    MESSAGE_NUMBER		= 0;
	private String MESSAGE_STRING		= "";		// 메세지
	
	//이미지 
	private ArrayList IMG_WIDTH         = new ArrayList();      // 길이 
	private ArrayList IMG_HEIGHT        = new ArrayList();      // 높이
	
	// 생성자
	public FileUpload (HttpServletRequest req) {
		this.REQUEST = req;
	}
	
	public FileUpload (HttpServletRequest req, HashMap map) {
		this.REQUEST = req;
		this.paramMap = map;
	}
	
	/**
	 * 용량제한
	 * @param sizeLimit
	 */
	public void setSizeLimit(int sizeLimit) { 
		this.SIZE_LIMIT = sizeLimit; 
	}
	
	
	/**
	 * 추가 경로
	 * @param addPath
	 */
	public void setAddPath(String addPath) { 
		this.ADD_PATH = addPath; 
	}
	
	
	/**
	 * 허용파일타입
	 * @param orgFileType
	 */
	public void setOrgFileType(String orgFileType) { 
		this.ORG_FILE_TYPE = orgFileType; 
	}
	
	
	/**
	 * 업로드 파일 갯수
	 * @return
	 */
	public int getFileCount() { 
		return this.FILE_COUNT; 
	}
	
	
	/**
	 * 업로드 파일 경로
	 * @return
	 */
	public List getFileUploadPath() { 
		return this.FILE_UPLOAD_PATH; 
	}
	
	/**
	 * 실제파일 이름
	 * @return
	 */
	public List getRealFileName() { 
		return this.REAL_FILE_NAME; 
	}
	
	/**
	 * 변경파일 이름
	 * @return
	 */
	public List getMaskFileName() { 
		return this.MASK_FILE_NAME; 
	}
	
	/**
	 * File Object명
	 * @return
	 */
	public List getFileObjectName()	{ 
		return this.FILE_OBJECT_NAME; 
	}
	
	/**
	 * 파일 URL
	 * @return
	 */
	public List getFileUrl() { 
		return this.FILE_URL; 
	}
	
	/**
	 * 메세지 번호
	 * @return
	 */
	public int getMessageNumber() { 
		return this.MESSAGE_NUMBER; 
	}
	
	/**
	 * 메세지
	 * @return
	 */
	public String getMessageString() { 
		return HtmlHelper.txt2html(this.MESSAGE_STRING); 
	}
	
	/**
	 * 이미지 가로 
	 * @return
	 */
	public List getIMG_WIDTH() {
		return IMG_WIDTH;
	}

	/**
	 * 이미지 세로 
	 * @return
	 */
	public List getIMG_HEIGHT() {
		return IMG_HEIGHT;
	}
	
	/**
	 * 파일 크기 
	 * @return
	 */
	public List getFileSize() {
		return this.FILE_SIZE;
	}
	
	/**
	 * 파일 정보  
	 * @return
	 */
	public ArrayList getFileInfoList() {
		return this.SEARCHMAP_FILE_LIST;
	}
	
	/**
	 * 허용 파일타입 체크
	 * @param chkFileType 		: 체크할 파일 타입
	 * @return boolean			: 허용 여부
	*/
	private boolean chkOrgFileType(String chkFileType) {
		String[] arrOrgFileType = null;
		int		 iCount			= 0;
		int		 iListSize		= 0;
		
		if (this.ORG_FILE_TYPE.equals("") ) return true;
		
		chkFileType = chkFileType.toUpperCase();
		arrOrgFileType = this.ORG_FILE_TYPE.split("[|]");
		iListSize = arrOrgFileType.length;
		
		for(iCount=0; iCount<iListSize; iCount++) {
			arrOrgFileType[iCount] = arrOrgFileType[iCount].toUpperCase();
			
			if( chkFileType.equals(arrOrgFileType[iCount]) ) return true;
		}
		
		return false;
	}

	
	/**
	 * 파일정보저장
	 * @param fileUploadPath
	 * @param realFileName
	 * @param maskFileName
	 * @param fileObjectName
	 * @param width
	 * @param height
	 */
	private void setFileInfoList(    String fileUploadPath
			                       , String realFileName
			                       , String maskFileName
			                       , String fileObjectName
			                       , int width
			                       , int height
			                       , long size) {
		this.FILE_COUNT++;
		this.FILE_UPLOAD_PATH.add(fileUploadPath);
		this.REAL_FILE_NAME.add(realFileName);
		this.MASK_FILE_NAME.add(maskFileName);
		this.FILE_OBJECT_NAME.add(fileObjectName);
		
		this.FILE_URL.add(fileUploadPath + maskFileName);
		this.FILE_OBJECT_MAP.put(fileObjectName, FILE_COUNT);
		
		this.IMG_HEIGHT.add(height);
		this.IMG_WIDTH.add(width);
		
		this.FILE_SIZE.add(size);
	}
	
	/**
	 * 파일정보저장
	 * @param fileUploadPath
	 * @param realFileName
	 * @param maskFileName
	 * @param fileObjectName
	 * @param width
	 * @param height
	 */
	private void setFileInfoListSearchMap(    String fileUploadPath
			                       , String realFileName
			                       , String realFileExt
			                       , String maskFileName
			                       , String fileObjectName
			                       , int width
			                       , int height
			                       , long size) {
		
		FileInfoVO fileInfoVO = new FileInfoVO(); 
		fileInfoVO.setRealFileName(realFileName);
		fileInfoVO.setFileExtension(realFileExt.toLowerCase());
		fileInfoVO.setMaskFileName(maskFileName);
		
		fileInfoVO.setFileUploadPath(fileUploadPath + maskFileName);
		fileInfoVO.setFileObjectName(fileObjectName);
		fileInfoVO.setHeight(height);
		fileInfoVO.setWidth(width);
		fileInfoVO.setSize(size);
		
		SEARCHMAP_FILE_LIST.add(fileInfoVO);
	}

	
	
	/**
	 * 파일오브젝트와 인덱스맵핑
	 * @param fileObjectName 	: file object 명
	 * @return int				: index
	*/
	public int getFileListIndex(String fileObjectName) {
		int iReturnValue = 0;
		
		try {
			if( this.FILE_OBJECT_MAP.containsKey(fileObjectName) ) {
				iReturnValue = this.FILE_OBJECT_MAP.get(fileObjectName);
			} else {
				iReturnValue = 0;
			}
		} catch (Exception e) {
			iReturnValue = 0;
			logger.error(e.toString());
		}
		
		// index는 0부터 시작
		// -1는 오류발생
		iReturnValue--;
		return iReturnValue;
	}

	
	
	/**
	 * 파일 업로드
	 * @return boolean
	 */
	public boolean upload() {
		CalendarHelper csCal		= new CalendarHelper();
		FileHelper csFileHelper 	= new FileHelper();
		FileConfig fileConfig		= FileConfig.getInstance();
		File 	csFile      		= null;
		String 	stRealFileName		= "";					// 원본파일명
		String 	stMaskFileName 		= "";					// 변경된파일명
		String 	stCreateFolder		= "";					// 생성될폴더
		String 	stLocalCreateFolder	= "";					// 생성될폴더( 경로를 포함한 폴더명 )
		
		int width  = 0;
		int height = 0;
		// ================================
		// getProperty
		// ================================
		String  stRealRootPath      = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");
		String	stFileTempPath 		= fileConfig.getProperty("FILE_TEMP_PATH");
		String  stFileRootPath      = fileConfig.getProperty("FILE_ROOT_PATH");
		
		// ================================		
		// Real Local Path
		// ================================
		//ServletContext servletContext = this.REQUEST.getSession().getServletContext();
		
		String	stLocalFileRootPath = stRealRootPath + stFileRootPath;
		//stLocalFileRootPath = servletContext.getRealPath(stLocalFileRootPath);	
		stLocalFileRootPath = stLocalFileRootPath.replace("\\", "/");
	
		String	stLocalFileTempPath = stRealRootPath + stFileTempPath;
		//stLocalFileTempPath = servletContext.getRealPath(stLocalFileTempPath);	
		stLocalFileTempPath = stLocalFileTempPath.replace("\\", "/");		
		
		// ================================
		// MultipartRequest
		// ================================
		MultipartRequest multi 		= null;
		try {

			multi = new MultipartRequest(this.REQUEST, stLocalFileTempPath, this.SIZE_LIMIT, 
					 "UTF-8", new DefaultFileRenamePolicy());
			
			Enumeration filenames  = multi.getFileNames();
			Enumeration paramNames = multi.getParameterNames();
			String paramName = "";
			if(paramMap != null){
				while( paramNames.hasMoreElements() ) {
					paramName 	= (String)paramNames.nextElement();
					try{
						String[] temparr = multi.getParameterValues(paramName);
						if(temparr.length == 1){
							paramMap.put(paramName, temparr[0]);
						}else{
							paramMap.put(paramName, temparr);
						}
					}catch(Exception ex){
						ex.printStackTrace();
						try{
							
						}catch(Exception ex2){
							logger.error("파일 업로드 오류 : " + ex2.toString());
							ex2.printStackTrace();
							this.MESSAGE_NUMBER = -1;
							this.MESSAGE_STRING =  ex2.toString();
							return false;
						}
					}
				}
			}
			//Enumeration filenames  = multi.getFileNames();
			
			boolean isPermission = true;
			
			// upload할 파일이 2개 이상이면 배열로 받으면 됩니다.
			while( filenames.hasMoreElements() ) {
				String stFormName 	= (String)filenames.nextElement();
			 	stRealFileName		= multi.getOriginalFileName(stFormName);
			 	stMaskFileName 		= multi.getFilesystemName(stFormName);
				
				//stRealFileName		= URLEncoder.encode(multi.getOriginalFileName(stFormName));
			 	//stMaskFileName 		= URLEncoder.encode(multi.getFilesystemName(stFormName));
			 	
				// ================================
				// 폴더 생성
				// ================================
	            stCreateFolder  	= stFileRootPath + this.ADD_PATH + "/";
	            stLocalCreateFolder	= stLocalFileRootPath + this.ADD_PATH + "/";
	            FileManager.mkdirs(stLocalCreateFolder);

				// ================================
	            // 파일 타입추출
				// ================================
	            String 	stOrgFileType = "";
	            String	stOrgFileName = "";
	            if( stRealFileName != null && !stRealFileName.equals("") ) {
		            int nFileNameLastNum = stRealFileName.lastIndexOf(".");
		            
		            if( nFileNameLastNum != -1 ) {
		                stOrgFileType = stRealFileName.substring( nFileNameLastNum + 1, stRealFileName.length() );
		                stOrgFileName = stRealFileName.substring( 0, nFileNameLastNum);
		            } else {
		                stOrgFileType = "";
		            }
		            if(!this.chkOrgFileType(stOrgFileType)) {
		            	csFileHelper.deleteFile(stLocalCreateFolder + "/" + stMaskFileName);
		            	isPermission = false;
		            	/*
		            	this.MESSAGE_NUMBER = -21;
		            	this.MESSAGE_STRING = ErrorMessages.FAILURE_FILE_MESSAGE;
		    			logger.error(this.MESSAGE_STRING);
		    			return false;
		    			*/
		            }
		    		// 파일 크기 구함 
		            long fileSize = 0;
		            File file     = (File)multi.getFile(stFormName);
		            fileSize      = file.length();
		            // ================================
		            // 이미지 파일일 경우 그 크기  체크 
		            // jpg, jpeg, gif, png, tif
		            // ================================
		            if("jpg".equals(stOrgFileType.toLowerCase()) ||"jpeg".equals(stOrgFileType.toLowerCase()) 
		            		||"gif".equals(stOrgFileType.toLowerCase()) ||"png".equals(stOrgFileType.toLowerCase()) ||"tif".equals(stOrgFileType.toLowerCase())){
			            BufferedImage bi = ImageIO.read(file);
						width = 0;
						height = 0;
						bi = null;
		            } else {
						width = 0;
						height =0;
		            } //if
							            
		    		// ================================
		    		// 실경로로 파일 이동
		    		// ================================
		            /*
	            	stMaskFileName = csFileHelper.copyFile(
	            			stLocalFileTempPath + "/" + stRealFileName,
    						stLocalCreateFolder + stOrgFileName + "." + stOrgFileType,
                          	false, true );
	            	*/
	            	/*
		    		if( logger.isDebugEnabled()) {
		    			logger.debug("============================================================");
	    				logger.debug("stLocalFileTempPath : " + stLocalFileTempPath);
	    				logger.debug("stRealFileName : " + stRealFileName);
	    				logger.debug("stLocalCreateFolder : " + stLocalCreateFolder);
	    				logger.debug("nTime : " + nTime);
	    				logger.debug("stOrgFileType : " + stOrgFileType);
		    			logger.debug("============================================================");
		    		}
		    		*/
		            
	            	this.setFileInfoList(stCreateFolder, stRealFileName, stMaskFileName, stFormName, width, height,fileSize);
	            	this.setFileInfoListSearchMap(stCreateFolder, stRealFileName, stOrgFileType, stMaskFileName, stFormName, width, height,fileSize);
	            	
	            }
			}
			
			if(!isPermission) {
				this.MESSAGE_NUMBER = -21;
            	this.MESSAGE_STRING = ErrorMessages.FAILURE_FILE_MESSAGE;
    			logger.error(this.MESSAGE_STRING);
    			return false;
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			logger.error("파일 업로드 오류 : " + e.toString());
			this.MESSAGE_NUMBER = -1;
			this.MESSAGE_STRING =  e.toString();
			return false;
		}
		this.MESSAGE_NUMBER = 0;
		this.MESSAGE_STRING = "파일 업로드가 완료되었습니다.";
		
		return true;
	}
	
}
