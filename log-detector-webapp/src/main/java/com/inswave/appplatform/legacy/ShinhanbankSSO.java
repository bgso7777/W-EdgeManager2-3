package com.inswave.appplatform.legacy;

import com.inswave.appplatform.Config;
import com.inswave.appplatform.util.Crypto;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class ShinhanbankSSO {

	public ShinhanbankSSO() {}

	public String login(String userid, String password) {

		try{
			password = Crypto.encryptSHA256(userid+password);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String parameters = "userid="+userid+"&password="+password;

		StringBuffer response = new StringBuffer();
		try {
			TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}
				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}
				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			}};
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			HostnameVerifier allHostsValid = new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			String url = Config.getInstance().getShinhanbankatopLoginUrl();
			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			con.setHostnameVerifier(allHostsValid);

			//add reuqest header
			con.setRequestMethod("POST");

			String urlParameters = parameters;

			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.write(urlParameters.getBytes("UTF-8"));
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
//			System.out.println("\nSending 'POST' request to URL : " + url);
//			System.out.println("Post parameters : " + urlParameters);
//			System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		} catch(Exception e ) {
			e.printStackTrace();
		}
		return response.toString().trim();
	}

}
