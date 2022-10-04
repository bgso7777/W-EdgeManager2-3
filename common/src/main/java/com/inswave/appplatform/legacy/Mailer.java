package com.inswave.appplatform.legacy;

import com.inswave.appplatform.Config;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.*;
import javax.mail.internet.*;

public class Mailer {

	private String host;
	private String port;
	private String id;
	private String password;
	private String name;
	private String sender;

	public Mailer() {
		host=Config.getInstance().getSmtpHost();
		port=Config.getInstance().getSmtpPort();
		id=Config.getInstance().getSmtpId();
		password=Config.getInstance().getSmtpPassword();
	}

	/**
	 *
	 * @param subject
	 * @param content
	 * @param toEmail
	 */
	public void sendMail(String subject, String content, String toEmail, String charSet) throws UnsupportedEncodingException, MessagingException {

		//String bodyEncoding = "UTF-8"; //콘텐츠 인코딩

		StringBuffer sb = new StringBuffer();
		sb.append(content);
		String html = sb.toString();

		// 메일 옵션 설정
		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", port);
		props.put("mail.smtp.auth", "true");

		props.put("mail.smtp.quitwait", "false");
		props.put("mail.smtp.socketFactory.port", port);
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		//props.put("mail.smtp.ssl.protocols", "TLSv1.2");
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.socketFactory.fallback", "false");

		// 메일 서버  인증 계정 설정
		Authenticator auth = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(id,password);
			}
		};

		// 메일 세션 생성
		Session session = Session.getInstance(props, auth);

		// 메일 송/수신 옵션 설정
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(id,name));
		message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(toEmail, false));
		message.setSubject(subject);
		message.setSentDate(new Date());

		// 메일 콘텐츠 설정
		Multipart mParts = new MimeMultipart();
		MimeBodyPart mTextPart = new MimeBodyPart();
		MimeBodyPart mFilePart = null;

		// 메일 콘텐츠 - 내용
		mTextPart.setText(html, charSet, "html");
		mParts.addBodyPart(mTextPart);

		// 메일 콘텐츠 설정
		message.setContent(mParts);

		// MIME 타입 설정
		MailcapCommandMap MailcapCmdMap = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
		MailcapCmdMap.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
		MailcapCmdMap.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
		MailcapCmdMap.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
		MailcapCmdMap.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
		MailcapCmdMap.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
		CommandMap.setDefaultCommandMap(MailcapCmdMap);

		// 메일 발송
		Transport.send( message );

	}

	/**
	 *
	 * @param to
	 * @param subject
	 * @param content
	 * @throws Exception
	 */
	public void sendExchangeServer(String to, String subject, String content, String charSet) throws Exception {

		Properties props = System.getProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.user", id);
		props.put("mail.smtp.password", password);
		props.put("mail.smtp.port", port);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.ehlo", "false");
		props.put("mail.debug", "true");

		Session session = Session.getDefaultInstance(props, null);
		MimeMessage message = new MimeMessage(session);

		//message.setFrom(new InternetAddress(from));
		message.setFrom(new InternetAddress(id));

		InternetAddress to_address = new InternetAddress(to);
		message.addRecipient(Message.RecipientType.TO, to_address);

		if(charSet.equals("")) {
			message.setSubject(subject);
			message.setText(content);
		} else {
			message.setSubject(subject, charSet);
			message.setText(content, charSet);
		}

		Transport transport = session.getTransport("smtp");
		transport.connect(host, id, password);
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static void main(String argv[]) {
		try {
			Mailer mailer = new Mailer();
			mailer.setHost("smtp.gmail.com");
			mailer.setPort("465");
			mailer.setId("bgso@inswave.com");
			mailer.setName("zaq1!@2wsx");
			mailer.setPassword("소병규");
			//mailer.sendExchangeServer("bgso@inswave.com", "bgso777@naver.com", "제목", "내용msg", "내용org");
			mailer.sendMail("제목", "<html>내용<html>", "bgso@inswave.com,bgso777@naver.com", "UTF-8");
			//mailer.sendMail2();
		}catch(Exception e) {
			e.printStackTrace();;
		}
	}



}
