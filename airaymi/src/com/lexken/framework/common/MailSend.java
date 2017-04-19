package com.lexken.framework.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.config.CommonConfig;
import com.lexken.framework.util.StaticUtil;


public class MailSend {
	
	private static MailSend instance = null;
	private final Log logger = LogFactory.getLog(getClass());
	
	public static MailSend getInstance() {
		if (instance == null) {
			instance = new MailSend();
		}
		return instance;
	}	
	
	/*************************************************
	* Templet 적용 , 첨부파일 메일보내기
	**************************************************/
	
	public String TempletEmailSend
		(
		String id, String to_name, String to_email, String from_email, String from_name, String mail_title, String mail_content, String TempletFile, String AttatchFile, String FilePath
		)  
		throws Exception {
		
		String content = "";
		String ret = "" ;
		String rootPath = "";

		boolean b_send = false;

		/***********************************************************
		* 이메일 내용
		***********************************************************/
		mail_content = StaticUtil.strReplace("\n", "<br>", mail_content);
		TempletManager templetManager = new TempletManager();
		templetManager.setArg("mail_content", mail_content);
		templetManager.setArg("id", id );
		templetManager.setArg("name", to_name);
		templetManager.setArg("title", mail_title);
		templetManager.setArg("from_name", from_name);
		templetManager.setArg("admin_email", from_email);
		templetManager.setArg("domain", CommonConfig.G_CONF_SITE_URL);
		
		rootPath = CommonConfig.SMTP_ROOT_PATH;
		content = templetManager.getTemplet(rootPath + TempletFile);
		content = templetManager.parseTemplet(content);	
		

		/***********************************************************
		* 이메일 발송
		***********************************************************/
		MailSendProc mail = MailSendProc.getInstance();
		mail.setFrom    (from_email);
		mail.setFromName(from_name);
		mail.setTo      (to_email);
		mail.setToName  (to_name);
		mail.setSubject (mail_title);
		mail.setContent (content);
		mail.setFileName(AttatchFile);
		mail.setFilePath(FilePath);

		b_send = mail.send();

		if ( b_send ) {
			ret = "Y" ;
		} else {
			ret = "N" ;
		}
		
		if(logger.isDebugEnabled()){
			logger.debug("===============================");
			logger.debug("id : " + id);
			logger.debug("to_name : " + to_name);
			logger.debug("to_email : " + to_email);
			logger.debug("from_email : " + from_email);
			logger.debug("from_name : " + from_name);
			logger.debug("mail_title : " + mail_title);
			logger.debug("mail_content : " + mail_content);
			logger.debug("TempletFile : " + TempletFile);
			logger.debug("AttatchFile : " + AttatchFile);
			logger.debug("FilePath : " + FilePath);
			logger.debug("returnValue : " + ret);
			logger.debug("===============================");
		}

		return ret ;
	}


}
