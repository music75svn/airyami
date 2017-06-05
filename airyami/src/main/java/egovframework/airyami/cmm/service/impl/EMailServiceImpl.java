package egovframework.airyami.cmm.service.impl;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import egovframework.airyami.cmm.service.EMailService;
import egovframework.airyami.cmm.util.ValueMap;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

@Service("emailService")
public class EMailServiceImpl extends AbstractServiceImpl implements EMailService
{
	protected Log log = LogFactory.getLog(this.getClass());
	private Logger logger = Logger.getLogger(this.getClass());
	
	
	
	@Override
	public boolean send(ValueMap paramMap) throws Exception {
		// TODO Auto-generated method stub
		
//		EgovProperties.getProperty("Globals.imgFileStorePath");
		Properties p = System.getProperties();
        p.put("mail.smtp.starttls.enable", "true");     // gmail은 무조건 true 고정
        p.put("mail.smtp.host", "smtp.gmail.com");      // smtp 서버 주소
        p.put("mail.smtp.auth","true");                 // gmail은 무조건 true 고정
        p.put("mail.smtp.port", "587");                 // gmail 포트
        
        Authenticator auth = new MyAuthentication();

        //session 생성 및  MimeMessage생성
        Session session = Session.getDefaultInstance(p, auth);
        MimeMessage msg = new MimeMessage(session);
         
        try{
            //편지보낸시간
            msg.setSentDate(new Date());
             
            InternetAddress from = new InternetAddress() ;
             
             
            from = new InternetAddress("zayou20000@gmail.com");
             
            // 이메일 발신자
            msg.setFrom(from);
             
             
            // 이메일 수신자
            InternetAddress to = new InternetAddress(paramMap.getString("addTo"));
            msg.setRecipient(Message.RecipientType.TO, to);
             
            // 이메일 제목
            msg.setSubject(paramMap.getString("subject"), "UTF-8");
             
            // 이메일 내용
            msg.setText(paramMap.getString("msg"), "UTF-8");
             
            // 이메일 헤더
            msg.setHeader("content-Type", "text/html");
             
            //메일보내기
            javax.mail.Transport.send(msg);
             
            return true;
        }catch (AddressException addr_e) {
            addr_e.printStackTrace();
            return false;
        }catch (MessagingException msg_e) {
            msg_e.printStackTrace();
            return false;
        }
	}   
}

class MyAuthentication extends Authenticator {
    
    PasswordAuthentication pa;
    
 
    public MyAuthentication(){
         
        String id = "zayou20000";       // 구글 ID
        String pw = "limalove74";          // 구글 비밀번호
 
        // ID와 비밀번호를 입력한다.
        pa = new PasswordAuthentication(id, pw);
      
    }
 
    // 시스템에서 사용하는 인증정보
    public PasswordAuthentication getPasswordAuthentication() {
        return pa;
    }
}


