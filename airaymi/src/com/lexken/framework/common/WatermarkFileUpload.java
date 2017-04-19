package com.lexken.framework.common;

import java.io.File;
import java.util.Enumeration;
import java.util.List;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.config.FileConfig;
import com.lexken.framework.util.FileHelper;
import com.lexken.framework.util.HtmlHelper;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import java.util.HashMap;


public class WatermarkFileUpload {
	// Logger
	private final Log logger = LogFactory.getLog(getClass());
	
	private int FILE_COUNT				= 0;					// 업로드 파일 갯수
	private ArrayList FILE_OBJECT_NAME  = new ArrayList();		// 파일 오브젝트 네임
	private ArrayList FILE_UPLOAD_PATH	= new ArrayList();		// 파일업로드 경로
	private ArrayList REAL_FILE_NAME	= new ArrayList();		// 원본파일명
	private ArrayList MASK_FILE_NAME	= new ArrayList();		// 변경된 파일 명
	private ArrayList FILE_URL			= new ArrayList();		// 파일 URL	
	
	private HttpServletRequest REQUEST 	= null;					// request	
	private int SIZE_LIMIT 				= 2 * 1024 * 1024;		// 제한용량
	private String ORG_FILE_TYPE		= "";					// 허용 파일타입
	private String ADD_PATH				= "";					// 추가 경로
	private boolean TEMP_PATH			= false;				// temp 에 넣을것인지 여부
	
	private int    MESSAGE_NUMBER		= 0;
	private String MESSAGE_STRING		= "";		// 메세지
	
	private HashMap<String, Integer>  FILE_OBJECT_MAP	= new HashMap<String, Integer>();
	
	// 생성자
	public WatermarkFileUpload (HttpServletRequest req) {
		this.REQUEST = req;
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
	 * temp 디렉토리내에 watermark 폴더 만들어서 처리
	 * @param addPath
	 */
	public void setTempPath(boolean tempPath) { 
		this.TEMP_PATH = tempPath; 
	}	
	
	/**
	 * 허용파일타입
	 * @param orgFileType
	 */
	public void setOrgFileType(String orgFileType) { 
		this.ORG_FILE_TYPE = orgFileType; 
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
		
		if (this.ORG_FILE_TYPE.equals("")) return true;

		chkFileType = chkFileType.toUpperCase();
		arrOrgFileType = chkFileType.split("|");
		iListSize = arrOrgFileType.length;
		
		for(iCount=0; iCount<iListSize; iCount++) {
			arrOrgFileType[iCount] = arrOrgFileType[iCount].toUpperCase();
			
			if( chkFileType.equals(arrOrgFileType[iCount]) ) return true;
		}
		
		return false;
	}
	
	/**
	 * 업로드 파일 경로
	 * @return
	 */
	public List getFileUploadPath() { 
		return this.FILE_UPLOAD_PATH; 
	}
	
	/**
	 * 변경파일 이름
	 * @return
	 */
	public List getMaskFileName() { 
		return this.MASK_FILE_NAME; 
	}
	
	/**
	 * 실제파일 이름
	 * @return
	 */
	public List getRealFileName() { 
		return this.REAL_FILE_NAME; 
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
	 * 파일정보저장
	 * @param fileUploadPath 	: 파일경로
	 * @param realFileName		: 실제파일 이름
	 * @param maskFileName		: 변경된 파일일름
	 * @param fileObjectName	: file object 명
	 * @return 
	*/
	private void setFileInfoList(String fileUploadPath, String realFileName, String maskFileName, String fileObjectName) {
		this.FILE_COUNT++;
		this.FILE_UPLOAD_PATH.add(fileUploadPath);
		this.REAL_FILE_NAME.add(realFileName);
		this.MASK_FILE_NAME.add(maskFileName);
		this.FILE_OBJECT_NAME.add(fileObjectName);
		
		this.FILE_URL.add(fileUploadPath + maskFileName);
		this.FILE_OBJECT_MAP.put(fileObjectName, FILE_COUNT);
	}
	
	
	/**
	 * 파일 업로드
	 * @return boolean
	*/
	public boolean upload() {
		FileConfig fileConfig		= FileConfig.getInstance();
		FileHelper csFileHelper 	= new FileHelper();
		File 	csFile      		= null;
		
		// ================================
		// getProperty
		// ================================
		String	stFileRootPath 		= fileConfig.getProperty("FILE_ROOT_PATH");
		String	stFileTempPath 		= fileConfig.getProperty("FILE_TEMP_PATH");	
		
		// ================================		
		// Real Local Path
		// ================================
		ServletContext servletContext = this.REQUEST.getSession().getServletContext();	
		String	stLocalFileRootPath   = "";
		
		String 	stRealFileName		  = "";			// 원본파일명
		String 	stMaskFileName 		  = "";			// 변경된파일명	
		String 	stCreateFolder		  = "";			// 생성될폴더
		String 	stLocalCreateFolder	  = "";			// 생성될폴더( 경로를 포함한 폴더명 )		
		
		// '/data/temp' 에 넣을지, '/data' 에 넣을지 분기 
		if (this.TEMP_PATH == true) {
			stLocalFileRootPath = stFileTempPath;		
		} else {
			stLocalFileRootPath = stFileRootPath;			
		}				
		stLocalFileRootPath = servletContext.getRealPath(stLocalFileRootPath);
		stLocalFileRootPath = stLocalFileRootPath.replace("\\", "/");	
		
		logger.debug("stLocalFileRootPath : " + stLocalFileRootPath);
		
		String	stLocalFileTempPath = stFileTempPath;
		stLocalFileTempPath = servletContext.getRealPath(stLocalFileTempPath);
		stLocalFileTempPath = stLocalFileTempPath.replace("\\", "/");
		
		// ================================
		// MultipartRequest
		// ================================		
		MultipartRequest multi 		= null;		
		
		try {
			multi = new MultipartRequest(this.REQUEST, stLocalFileTempPath, this.SIZE_LIMIT, 
					 "EUC-KR", new DefaultFileRenamePolicy());
			
			Enumeration filenames  = multi.getFileNames();
			
			// upload할 파일이 2개 이상이면 배열로 받으면 됩니다.
			while( filenames.hasMoreElements() ) {
				String stFormName 	= (String)filenames.nextElement();
			 	stRealFileName		= multi.getOriginalFileName(stFormName);
			 	stMaskFileName 		= fileConfig.getProperty("WM_FILE_NAEM");	

				// ================================
				// 폴더 생성
				// ================================
				// '/data/temp' 에 넣을지, '/data' 에 넣을지 분기 
				if (this.TEMP_PATH == true) {
					stCreateFolder  	= stFileTempPath + this.ADD_PATH + "/";		
				} else {
					stCreateFolder  	= stFileRootPath + this.ADD_PATH + "/";
				}				
	            stLocalCreateFolder	= stLocalFileRootPath + this.ADD_PATH + "/";
	            
	            //logger.debug(" : stCreateFolder : " + stCreateFolder);
	            //logger.debug(" : stLocalCreateFolder : " + stLocalCreateFolder);
	            
	            csFile      		= new File(stLocalCreateFolder);
	            csFile.mkdir();
	            
				// ================================
	            // 파일 타입추출
				// ================================
	            String stOrgFileType = "";

	            if( stRealFileName != null && !stRealFileName.equals("") ) {

	            	int nFileNameLastNum = stRealFileName.lastIndexOf(".");
		            
		            if( nFileNameLastNum != -1 ) {
		                stOrgFileType = stRealFileName.substring( nFileNameLastNum, stRealFileName.length() );
		            } else {
		                stOrgFileType = "";
		            }
		            
		            if(!this.chkOrgFileType(stOrgFileType)) {
		            	this.MESSAGE_NUMBER = -1;
		            	this.MESSAGE_STRING = "허용되지 않은 파일형식입니다.";
		    			logger.error(this.MESSAGE_STRING);
		    			return false;
		            }	

		            stMaskFileName = csFileHelper.copyFile(
	            			stLocalFileTempPath + "/" + stRealFileName,
    						stLocalCreateFolder + fileConfig.getProperty("WM_FILE_NAEM"),
                          	true, true );		            
	            	
		    		if( logger.isDebugEnabled()) {
		    			logger.debug("============================================================");
	    				logger.debug("stLocalFileTempPath : " + stLocalFileTempPath);
	    				logger.debug("stRealFileName : " + stRealFileName);
	    				logger.debug("stLocalCreateFolder : " + stLocalCreateFolder);
	    				logger.debug("stOrgFileType : " + stOrgFileType);
	    				logger.debug("stMaskFileName : " + stMaskFileName);
		    			logger.debug("============================================================");
		    		}
		    		
	            	this.setFileInfoList(stCreateFolder, stRealFileName, stMaskFileName, stFormName);
	            }		    		
			}
			
		} catch(Exception e) {
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
