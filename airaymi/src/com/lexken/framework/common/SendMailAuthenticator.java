package com.lexken.framework.common;

import javax.mail.*;

public final class SendMailAuthenticator extends javax.mail.Authenticator {
	javax.mail.PasswordAuthentication pa;

	public SendMailAuthenticator(String smtp_id, String smtp_passwd) {
		pa = new PasswordAuthentication(smtp_id, smtp_passwd);
	}

	public javax.mail.PasswordAuthentication getPasswordAuthentication() {
		return pa;
	}
}
		
