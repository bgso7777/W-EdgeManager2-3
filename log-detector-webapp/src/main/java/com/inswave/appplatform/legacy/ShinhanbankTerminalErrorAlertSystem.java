package com.inswave.appplatform.legacy;

import com.inswave.appplatform.Config;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.HttpStatus;

import java.io.IOException;

public class ShinhanbankTerminalErrorAlertSystem {

	private String requestData = "";

	public ShinhanbankTerminalErrorAlertSystem() {
	}

	public String sendIntegrityAlert(String netClient5, String userId, String phoneNo, String content) throws IOException {

		String charSet = "UTF-8";
		this.requestData = content;

		// 단말 예측 시스템 발송
		String stringUrl = Config.getInstance().getShinhanbankatopIntegrityAlertUrl();

		requestData = Config.getInstance().getShinhanbankatopIntegrityAlertUrlData();
		if(netClient5!=null)
			requestData = requestData.replace("#EQUIP_ID#", netClient5);
		if(userId!=null)
			requestData = requestData.replace("#REQ_EMP_NO#", userId);
		if(phoneNo!=null)
			requestData = requestData.replace("#PHONE_NO#", phoneNo);
		requestData = requestData.replace("#TRBM_TYPE#", "2011");
		requestData = requestData.replace("#CONTENTS#", content);

		org.apache.commons.httpclient.HttpClient client = new org.apache.commons.httpclient.HttpClient(new MultiThreadedHttpConnectionManager());
		client.setConnectionTimeout(10000);
		client.setTimeout(10000);

		PostMethod postMethod = new PostMethod(stringUrl);
		postMethod.setFollowRedirects(false);
		postMethod.setRequestHeader("Connection","close");
		postMethod.setRequestHeader("Content-Type","text/xml; charset="+charSet);
		postMethod.setRequestBody(requestData);

		int statusCod = client.executeMethod(postMethod);

		String rcvData = "";
		if(statusCod== HttpStatus.SC_OK) {
			rcvData = postMethod.getResponseBodyAsString();
		} else {
			rcvData = postMethod.getResponseBodyAsString();
		}
		return rcvData;
	}

	public String getRequestData() {
		return requestData;
	}

}
