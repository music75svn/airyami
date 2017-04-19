/*************************************************************************
* CLASS 명  	: FileDownloadAction
* 작 업 자  	: 하윤식
* 작 업 일  	: 2012.08.31
* 기    능  	: 파일다운로드
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작  업   일          변 경 내 용               비고
* ----  --------  -----------------  -------------------------    --------
*   1    하윤식		 2012.08.31			  최 초 작 업 
**************************************************************************/
package com.lexken.bsc.module;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.FileDownload;
import com.lexken.framework.common.HttpRequest;
import com.lexken.framework.common.RefererUrlCheck;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.config.CommonConfig;
import com.lexken.framework.config.FileConfig;
import com.lexken.framework.login.SecureService;
import com.lexken.framework.struts2.IsBoxActionSupport;
import com.lexken.framework.util.StaticUtil;

public class FileDownloadAction extends IsBoxActionSupport {

	// Logger
	private final Log logger = LogFactory.getLog(getClass());
	
	private FileDownloadService fileDownloadService = new FileDownloadService();
	
	/**
	 * 파일 다운로드
	 * @return String
	 * @throws IOException, ServletException
	 */	
	public String fileDownload() throws IOException, ServletException {
		
		/**********************************
		 * 정의
		 **********************************/
		String	fileName	= "";				// 파일경로 및 이름
		String  download	= "";				// 다운로드 Full Path
		String	realFileName= "";				// 다운받을 파일 명
		
		FileConfig fileConfig	= FileConfig.getInstance();
		String  stRealRootPath	= fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");
		
		/**********************************
		 * 불법접근 확인
		 **********************************/
		String headerReferer = req.getHeader("referer");
		
		if(!RefererUrlCheck.chkRefererUrl(headerReferer)){
			String errorMsg = ErrorMessages.format(ErrorMessages.REFERER_ERROR_MESSAGE, "");
			StaticUtil.jsAlertRedirect(StaticUtil.kscToAsc(errorMsg), req.getContextPath() +  "/main/main.vw", this.res.getOutputStream());
			return null;
		}
		
		/**********************************
		 * Request값 읽기
		 **********************************/
		HttpRequest request  = new HttpRequest(req);
		fileName  = request.getString("file_name", "");
		realFileName = request.getString("real_file_name", "");

//		/**********************************
//		 * path 암호화키 읽기
//		 **********************************/
//		CommonConfig commonConfig   = CommonConfig.getInstance();
//		String       encryptionCode = commonConfig.getProperty("ENCRYPTION_CODE");
//		
//		/**********************************
//		 * 암호화키를 사용하여 path 복호화
//		 **********************************/
//		SearchMap searchMap = new SearchMap(req, res);
//		searchMap.put("encryptionCode", encryptionCode);
//		searchMap.put("fileName", fileName);
//		String decryptRealFileName = fileDownloadService.getFiledownloadPath(searchMap);
//		
//		fileName = decryptRealFileName;
		
		String accptLng = req.getHeader("accept-language");
		if(accptLng.indexOf("ko-KR") == 0 || accptLng.indexOf("ko-kr") == 0 || accptLng.indexOf("ko") == 0 || accptLng.indexOf("KO") == 0){
			//fileName = new String(fileName.getBytes("8859_1"), "UTF-8");
			//realFileName = new String(realFileName.getBytes("8859_1"), "UTF-8");
		}
		
		if(fileName.endsWith("vm")){
			return null;
		} else if(fileName.endsWith("java")){
			return null;
		} else if(fileName.endsWith("class")){
			return null;
		} else if(fileName.indexOf("../") > 0){
			return null;
		} else if(fileName.indexOf("..\\") > 0){
			return null;	
		}
		
		download = stRealRootPath + fileName;
		
		File file = new File(download);

		if(realFileName != null && !"".equals(realFileName)){
			FileDownload.download(req, res, file, realFileName);
		}else{
			FileDownload.download(req, res, file);
		}

		return null;
	}
	
	/**
	 * 파일 다운로드(레퍼러채크 안함)
	 * @return String
	 * @throws IOException, ServletException
	 */	
	public String fileDownloadExcel() throws IOException, ServletException {
		
		/**********************************
		 * 정의
		 **********************************/
		String	fileName	= "";					// 파일경로 및 이름
		String  download	= "";					// 다운로드 Full Path
		String	realFileName	= "";				// 다운받을 파일 명
		
		FileConfig fileConfig		= FileConfig.getInstance();
		String  stRealRootPath      = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");
		//String  stRealMipssRootPath      = fileConfig.getProperty("FILE_REAL_MIPSS_FILE_ROOT_PATH");
		
		/**********************************
		 * Request값 읽기
		 **********************************/
		
		HttpRequest request  = new HttpRequest(req);
		fileName  = request.getString("file_name", "");
		realFileName  = request.getString("real_file_name", "");
		
		
		String accptLng = req.getHeader("accept-language");

		
		if(accptLng == null || accptLng.indexOf("ko-KR") == 0 || accptLng.indexOf("ko-kr") == 0){
			fileName = new String(fileName.getBytes("8859_1"), "UTF-8");
			realFileName = new String(realFileName.getBytes("8859_1"), "UTF-8");
		}
		
		
		if(fileName.endsWith("vm")){
			return null;
		} else if(fileName.endsWith("java")){
			return null;
		} else if(fileName.endsWith("class")){
			return null;
		} else if(fileName.indexOf("../") > 0){
			return null;
		} else if(fileName.indexOf("..\\") > 0){
			return null;	
		}
		
		download = stRealRootPath + fileName;
		
		File file = new File(download);
		
		logger.debug("download: " + download);
		
		if(realFileName != null && !"".equals(realFileName)){
			FileDownload.download(req, res, file, realFileName);
		} else {
			FileDownload.download(req, res, file);
		}

		return null;
	}
	
	/**
	 * 파일 다운로드(App: ActiveX, Client 애플리케이션)
	 * @return String
	 * @throws IOException, ServletException
	 */	
	public String fileDownloadToApp() throws IOException, ServletException {
		
		/**********************************
		 * 정의
		 **********************************/
		String	fileName	= "";					// 파일경로 및 이름
		String  download	= "";					// 다운로드 Full Path
		String	realFileName	= "";				// 다운받을 파일 명
		
		FileConfig fileConfig		= FileConfig.getInstance();
		String  stRealRootPath      = fileConfig.getProperty("FILE_REAL_FILE_ROOT_PATH");
		
		/**********************************
		 * Request값 읽기
		 **********************************/
		HttpRequest request  = new HttpRequest(req);
		fileName  = request.getString("file_name", "");
		realFileName  = request.getString("real_file_name", "");
		
		String accptLng = req.getHeader("accept-language");
		
		if(accptLng == null || accptLng.indexOf("ko-KR") == 0 || accptLng.indexOf("ko-kr") == 0){
			fileName = new String(fileName.getBytes("8859_1"), "UTF-8");
			realFileName = new String(realFileName.getBytes("8859_1"), "UTF-8");
		}
		
		if(fileName.endsWith("vm")){
			return null;
		} else if(fileName.endsWith("java")){
			return null;
		} else if(fileName.endsWith("class")){
			return null;
		} else if(fileName.indexOf("../") > 0){
			return null;
		} else if(fileName.indexOf("..\\") > 0){
			return null;	
		}
		
		download = stRealRootPath + fileName;
		
		File file = new File(download);
		if(realFileName != null && !"".equals(realFileName)){
			FileDownload.download(req, res, file, realFileName);
		}else{
			FileDownload.download(req, res, file);
		}
		
		return null;
	}
	 
	/**
	 * toKorean
	 * @param str
	 * @return String
	 * @throws   
	*/
	public String toKorean(String str) {		
		String m_method = req.getMethod();
		
		if (str == null) return str;
		String result = null;
		String userCharset = req.getCharacterEncoding();

		try {
			String usrAgent = req.getHeader("user-agent");
			String[] acceptLang = req.getHeader("accept-language").split(",");
			if("GET".equals(m_method) && (usrAgent.indexOf("MSIE") > -1 || usrAgent.indexOf("Chrome") > -1) && !"ko-KR".equals(acceptLang[0])) {
				result = java.net.URLDecoder.decode(str, "UTF-8"); 
			} else {
				if ("UTF-8".equalsIgnoreCase(userCharset)) {
					result = java.net.URLDecoder.decode(str, "UTF-8");
				} else {
					result = new String(str.getBytes("8859_1"), "UTF-8");
				}
			}
		} catch (Exception e) {
			//dummy
		}
		return result;
	}
	    
}
