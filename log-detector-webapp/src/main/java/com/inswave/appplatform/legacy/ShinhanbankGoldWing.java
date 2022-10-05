package com.inswave.appplatform.legacy;

import com.inswave.appplatform.Config;
import com.inswave.appplatform.util.StringUtil;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.HttpStatus;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;

public class ShinhanbankGoldWing {

	private String host;
	private String port;
	private String id;
	private String password;
	private String name;
	private String sender;

	public ShinhanbankGoldWing() {
	}

	public void sendMailExchangeServer(String to, String subject, String content) throws Exception {

		host=Config.getInstance().getSmtpHost();
		port=Config.getInstance().getSmtpPort();
		id=Config.getInstance().getSmtpId();
		password=Config.getInstance().getSmtpPassword();

		String charSet = "EUC-KR";

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

		message.setSubject(new String(subject.getBytes(charSet)), charSet);
		message.setText(new String(content.getBytes(charSet)), charSet);

		Transport transport = session.getTransport("smtp");
		transport.connect(host, id, password);
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();
	}

	private String requestData;

	public String sendMessage(String uniqueId, String senderId, String senderName, String content) throws IOException {

		String charSet = "EUC-KR";
		this.requestData = content;

		org.apache.commons.httpclient.HttpClient client = new org.apache.commons.httpclient.HttpClient(new MultiThreadedHttpConnectionManager());
		client.setConnectionTimeout(10000);
		client.setTimeout(10000);

		GetMethod getMethod = new GetMethod(Config.getInstance().getUrl());
		getMethod.setRequestHeader("Connection","close");
		getMethod.getParams().setParameter("Content-Type","application/x-www-form-urlencoded; charset="+charSet);

		requestData = Config.getInstance().getUrlData();
		requestData = requestData.replace("#uniqueId#",uniqueId);
		requestData = requestData.replace("#content#", StringUtil.getShinhanbankGoldwingEUCKRString(content));
		requestData = requestData.replace("#senderId#",senderId);
		requestData = requestData.replace("#senderName#",senderName);

		NameValuePair nameValuePair = new NameValuePair("data",requestData);
		getMethod.setQueryString(new NameValuePair[] {nameValuePair});

		int statusCod = client.executeMethod(getMethod);

		String rcvData = "";
		if(statusCod== HttpStatus.SC_OK) {
			rcvData = getMethod.getResponseBodyAsString();
		} else {
			rcvData = getMethod.getResponseBodyAsString();
		}
		return rcvData;
	}

	public String getRequestData() {
		return requestData;
	}
}
