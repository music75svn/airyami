package com.lexken.framework.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.config.CommonConfig;
import com.lexken.framework.util.StaticUtil;

public class MailSendManager
{	
	private static MailSendManager instance = null;
	private final Log logger = LogFactory.getLog(getClass());
	
	public static MailSendManager getInstance() {
		if (instance == null) {
			instance = new MailSendManager();
		}
		return instance;
	}

	/*************************************************
	* 일반 메일보내기
	**************************************************/
	public int EmailSend(String to_name, String to_email, String from_email, 
							String from_name, String mail_title, String mail_content) 
	throws Exception {

		boolean b_send = false;
		int	ret 	   = 0 ;
		
		mail_content = StaticUtil.strReplace("\n", "<br>", mail_content);
		
		/***********************************************************
		* 이메일 발송
		***********************************************************/
		MailSendProc mail = MailSendProc.getInstance();
		
		logger.debug("============================================================");			
		logger.debug("from_email : " + from_email);
		logger.debug("from_name : " + from_name);
		logger.debug("to_email : " + to_email);
		logger.debug("to_name : " + to_name);
		logger.debug("mail_title : " + mail_title);
		logger.debug("mail_content : " + mail_content);
		logger.debug("============================================================");		

		mail.setFrom    (from_email);
		mail.setFromName(from_name);
		mail.setTo      (to_email);
		mail.setToName  (to_name);
		mail.setSubject (mail_title);
		mail.setContent (mail_content);

		b_send = mail.send();
		
		if ( b_send ) {
			ret = 0 ;
		} else {
			ret = -1 ;
		}
		
		return ret;
	}

	/*************************************************
	* Templet 적용  메일보내기(1건)
	**************************************************/
	public int TempletEmailSend (
			String to_id,
			String from_name, 
			String from_email,  
			String to_name, 
			String to_email, 
			String email_title, 
			String email_content, 
			String tpl_Filepath, 
			String AttachFile, 
			String AttachFilePath
			) throws Exception {
		
		String		content = "";
		int			ret = 0 ;
		boolean		b_send = false;		
		/***********************************************************
		* 이메일 내용
		***********************************************************/		
		email_content = StaticUtil.strReplace("\n", "<br>", email_content);	
		
		TempletManager templetManager = new TempletManager();
		
		templetManager.setArg("id", to_id);
		templetManager.setArg("title", email_title);
		templetManager.setArg("mail_content", email_content);
		templetManager.setArg("name", to_name);		
		templetManager.setArg("from_name", from_name);
		templetManager.setArg("domain", CommonConfig.G_CONF_SITE_URL);		
		
		if (!"".endsWith(tpl_Filepath)){
			
			logger.debug("================= 템플릿 ==================================");			
			logger.debug("tpl_Filepath : " + CommonConfig.SMTP_ROOT_PATH + tpl_Filepath);
			logger.debug("============================================================");	
			
			content = templetManager.getTemplet(CommonConfig.SMTP_ROOT_PATH + tpl_Filepath);
			content = templetManager.parseTemplet(content);					
		} else {
			
			logger.debug("================= No 템플릿 ==================================");			
			logger.debug("email_content : " + email_content);
			logger.debug("============================================================");
			
			content = email_content;			
		}		
		
		/***********************************************************
		* 이메일 발송
		***********************************************************/
		MailSendProc mail = MailSendProc.getInstance();
		
		logger.debug("============================================================");			
		logger.debug("from_email : " + from_email);
		logger.debug("from_name : " + from_name);
		logger.debug("to_email : " + to_email);
		logger.debug("to_name : " + to_name);
		logger.debug("email_title : " + email_title);
		logger.debug("tpl_Filepath : " + tpl_Filepath);
		logger.debug("AttachFile : " + AttachFile);
		logger.debug("AttachFilePath : " + AttachFilePath);
		logger.debug("============================================================");
		
		mail.setFrom(from_email);
		mail.setFromName(from_name);
		mail.setTo(to_email);
		mail.setToName(to_name);
		mail.setSubject(email_title);
		mail.setContent(content);
		mail.setFileName(AttachFile);
		mail.setFilePath(CommonConfig.SMTP_ROOT_PATH + AttachFilePath);
		b_send = mail.send();
		
		logger.debug("=================이메일 발송==================================");
		logger.debug("결과  : " + b_send);
		logger.debug("ErrorMessage : " + mail.getErrorMessage());
		logger.debug("============================================================");
		
		if ( b_send ) {
			ret = 0 ;
		} else {
			ret = -1 ;
		}
		
		return ret;

	}

}
