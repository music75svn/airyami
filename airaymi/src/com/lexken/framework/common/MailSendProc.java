package com.lexken.framework.common;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.config.CommonConfig;


public class MailSendProc implements java.io.Serializable {

	private static MailSendProc instance = null;
	
	// Logger
	private static final Log logger = LogFactory.getLog(MailSendProc.class);

	public static MailSendProc getInstance() {
		if (instance == null) {
			instance = new MailSendProc();
		}
		return instance;
	}
	
	private String content;			/* 메일 내용		*/
	private String from;			/* 보내는 주소		*/
	private String from_name;		/* 보내는 사람		*/
	private String to;				/* 받을 사람 주소	*/
	private String to_name;			/* 받을 사람		*/
	private String subject;			/* 제목				*/
	private String file_path;		/* 첨부파일경로		*/
	private String file_name;		/* 첨부파일명		*/
	private int    error_code;		/* 에러코드(0:정상)	*/
	private String error_message;	/* 메일 전송 상태	*/

	public MailSendProc() 
	{
		super();
		content      	= "";
		from          	= "";
		from_name   	= "";
		to            	= "";
		to_name       	= "";
		subject       	= "";
		file_path	  	= "";
		file_name	  	= "";
		error_code   	= 0;
		error_message 	= "";
	}

	public String getContent() 
	{
		return content;
	}

	public void setContent(String newContent) 
	{
		content = newContent;
	}

	public String getSubject() 
	{
		return subject;
	}
	public void setSubject(String newSubject) 
	{
		subject = newSubject;
	}

	public String getTo() 
	{
		return to;
	}
	public void setTo(String newTo) 
	{
		to = newTo;
	}

	public String getToName() 
	{
		return to_name;
	}
	public void setToName(String newToName) 
	{
		to_name = newToName;
	}

	public String getFrom() 
	{
		return from;
	}
	public void setFrom(String newFrom) 
	{
		from = newFrom;
	}	

	public String getFromName() 
	{
		return from_name;
	}
	public void setFromName(String newFromName) 
	{
		from_name = newFromName;
	}	

	public void setFilePath(String filePath) 
	{
		file_path = filePath;
	}	

	public void setFileName(String fileName) 
	{
		file_name = fileName;
	}

	public int getErrorCode()
	{
		return error_code;
	}

	public String getErrorMessage() 
	{
		return error_message;
	}

	public boolean send() 
	{
		try {
			//JavaMail 세션을 만든다.
			java.util.Properties properties = System.getProperties();
			properties.put("mail.smtp.host",  CommonConfig.SMTP_HOST );
			
			// 인증을 거칠 경우
			properties.put("mail.smtp.auth",  "false" );
			SendMailAuthenticator auth = new SendMailAuthenticator(CommonConfig.SMTP_USER_ID, CommonConfig.SMTP_USER_PWD);
		
			// 세션 객체를 생성한다. 
			//javax.mail.Session session = javax.mail.Session.getInstance(properties, null);
			javax.mail.Session session = javax.mail.Session.getInstance(properties, auth);
			session.setDebug(false);
		
			//객체생성한다.
			MimeMessage message = new MimeMessage(session);
		
			// 메일보내는사람의 From속성값을 설정한다.
			message.setFrom(new InternetAddress(from,from_name,"EUC-KR"));
		
			//메일받는사람의 To 속성값을 설정한다.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to,to_name,"EUC-KR"));
		
			//메일제목을 설정한다.
			//message.setSubject(subject, "EUC-KR");
			message.setSubject(MimeUtility.encodeText(subject, "EUC-KR","B"));

			if ( file_path != "" && file_name != "" && file_path != null && file_name != null) {
				//파일이 있을 경우를 생각해서 MimeBodyPart객체를 생성합니다.
				Multipart mp = new MimeMultipart();
				MimeBodyPart mbp = new MimeBodyPart();
				mbp.setContent( content, "text/html;charset=euc-kr" );
				mp.addBodyPart( mbp );
				mbp = null;

				StringTokenizer str2 = new StringTokenizer( file_name, "|" );
				while( str2.hasMoreTokens() ) {
					String file_name = str2.nextToken();
					try {
						if( file_name.trim().length() != 0 ) {
							MimeBodyPart mbp2 =  new MimeBodyPart();
							//FileDataSource fds = new FileDataSource( file_path + "/" + file_name );	//090224 수정
							FileDataSource fds = new FileDataSource( file_path );
							mbp2.setDataHandler( new DataHandler(fds) );
							//mbp2.setFileName( new String(fds.getName().getBytes("KSC5601"), "8859_1") );	//090224 수정
							mbp2.setFileName( new String(file_name.getBytes("KSC5601"), "8859_1") );
							mp.addBodyPart( mbp2 );
							mbp2 = null;
						}
					}catch( Exception e ) {
						error_message = e.getMessage();
						error_code = 5;
						return false;
					}
				}
				message.setContent(mp);
			} else {
				//메일내용을 설정한다.
				message.setContent(content,"text/html;charset=euc-kr");
				message.saveChanges();
			}

			//보낸날짜 셋팅 
			message.setSentDate(new java.util.Date());
			Transport.send(message);
			return true;

		} catch (AddressException e) {
			error_code = 1;
			error_message = "주소가 잘못되었습니다.";
			e.printStackTrace();
			logger.error("메일발송에러 : " + e.toString());
			return false;
		} catch (SendFailedException e) {
			error_code = 2;
			error_message = e.getMessage();
			e.printStackTrace();
			logger.error("메일발송에러 : " + e.toString());
			return false;
		} catch (MessagingException e) {
			error_code = 3;
			error_message = e.getMessage();
			e.printStackTrace();
			logger.error("메일발송에러 : " + e.toString());
			return false;
		}  catch (java.io.UnsupportedEncodingException e) {
			error_code = 4;
			error_message = e.getMessage();
			e.printStackTrace();
			logger.error("메일발송에러 : " + e.toString());
			return false;
		}  catch (Exception e) {
			e.printStackTrace();
			logger.error("메일발송에러 : " + e.toString());
			return false;	
		}
	}
	
}

/*
class SendMailAuthenticator extends javax.mail.Authenticator {
	javax.mail.PasswordAuthentication pa;

	public SendMailAuthenticator(String smtp_id, String smtp_passwd) {
		pa = new PasswordAuthentication(smtp_id, smtp_passwd);
	}

	public javax.mail.PasswordAuthentication getPasswordAuthentication() {
		return pa;
	}
}
*/
		
