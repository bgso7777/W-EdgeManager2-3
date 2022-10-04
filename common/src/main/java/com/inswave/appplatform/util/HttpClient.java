package com.inswave.appplatform.util;

import java.io.*;
import java.net.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class HttpClient {

	public static final String SERVER_IP = "127.0.0.1";
	public static final String SERVER_POINT = "http://"+SERVER_IP+"/json.cmd";
	private String requestData = "";

	public HttpClient() {
	}
	
	/**
	 * 
	 * @param urlString
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	public String sendRequestByJSON2(String urlString, String data, long timeOut, String contentType) throws IOException, NoSuchAlgorithmException, KeyManagementException {
		String resData = "";
		if(urlString.indexOf("https://")!=-1) {
			resData = sendRequestHttpsByJSON(urlString, data, timeOut, contentType);
		} else { 
			resData = sendRequestHttpByJSON(urlString, data, timeOut, contentType);
		}
		return resData;
	}

	public String sendRequestHttpByJSON(String serverIp, String data, long timeOut, String contentType) throws ClientProtocolException, IOException {
		
		HttpPost post = new HttpPost(serverIp);
		post.setHeader("Content-Type", "application/json");
		post.setEntity(new StringEntity(data, contentType));
		HttpParams params = new BasicHttpParams();
		
		if(timeOut>0) {
			HttpConnectionParams.setConnectionTimeout(params, 3000);
			HttpConnectionParams.setSoTimeout(params, 5000);
		}
		
//		params.setParameter("data", data);
//		post.setParams(params);
		
		DefaultHttpClient httpClient = new DefaultHttpClient();
		ResponseHandler<String> basicResponseHandler = new BasicResponseHandler();
		String responseString = httpClient.execute(post, basicResponseHandler);
		
		return responseString;
	}

	public String sendRequestHttpsByJSON(String urlString, String data, long timeOut, String contentType) throws IOException, NoSuchAlgorithmException, KeyManagementException {
			
		StringBuffer resData = new StringBuffer();
		
		// Get HTTPS URL connection
		URL url = new URL(urlString);
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST"); 
		conn.setFollowRedirects(true); 
		conn.setRequestProperty("Content-length",String.valueOf (data.length())); 
		if(contentType.equals(""))
			conn.setRequestProperty("Content-Type","application/x-www- form-urlencoded");
		else
			conn.setRequestProperty("Content-Type",contentType);
		conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows 98; DigExt)"); 
		conn.setRequestProperty("Content-Language", "UTF-8");
		if(timeOut>0)
			conn.setConnectTimeout(3000);
		
		// Set Hostname verification
		conn.setHostnameVerifier(new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				// Ignore host name verification. It always returns true.
				return true;
			}
		});
		
		// SSL setting
		SSLContext context = SSLContext.getInstance("TLS");
		context.init(null, new TrustManager[] { new X509TrustManager() {
			@Override
			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
				// client certification check
			}
			@Override
			public void checkServerTrusted(X509Certificate[] chain,	String authType) throws CertificateException {
				// Server certification check
				try {
					// Get trust store
					KeyStore trustStore = KeyStore.getInstance("JKS");
					//if(System.getProperty("os.name").toLowerCase().indexOf("win")!=-1) {
					//if(Environment.getInstance().getRunMode1().equals(Environment.ITCMS_SERVICE_MODE)) {
						trustStore.load(new FileInputStream("truststore.jks"),"changeit".toCharArray());
					//} else {
					//	String cacertPath = System.getProperty("java.home") + "/lib/security/cacerts"; // Trust store path should be different by system platform.  
					//	trustStore.load(new FileInputStream(cacertPath), "changeit".toCharArray()); // Use default certification validation
					//}

					// Get Trust Manager
					TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
					tmf.init(trustStore);
					TrustManager[] tms = tmf.getTrustManagers();
					((X509TrustManager) tms[0]).checkServerTrusted(chain, authType);
					
				} catch (KeyStoreException e) {
					e.printStackTrace();
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		} }, null);
		conn.setSSLSocketFactory(context.getSocketFactory());

		// Connect to host
		conn.connect();
		conn.setInstanceFollowRedirects(true);
		
		// open up the output stream of the connection 
		DataOutputStream output = new DataOutputStream( conn.getOutputStream() );
		
		// write out the data 
		//int queryLength = data.length(); 
		output.writeBytes(data.toString()); 
		
		// Print response from host
		InputStream in = conn.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
		String line = null;
		while ((line = reader.readLine()) != null)
			resData.append(line+"\n");
		reader.close();
		return resData.toString();
	}

	public String excuteGet(String stringUrl, String charSet) throws IOException {

		StringBuffer response = new StringBuffer();
		HttpURLConnection connection = null;

		// Create connection
		URL url = new URL(stringUrl);
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		if(!charSet.equals(""))
			connection.setRequestProperty("Content-Language",charSet);
		connection.setUseCaches(false);
		connection.setDoInput(true);
		connection.setDoOutput(true);

		// Get Response
		InputStream is = connection.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(is,charSet);
		BufferedReader rd = new BufferedReader(inputStreamReader);
		String line;

		while ((line = rd.readLine()) != null) {
			response.append(line);
			response.append('\r');
		}
		rd.close();

		return response.toString();
	}

	public String getRequestData() {
		return requestData;
	}

	public static void main(String[] args) throws Exception {
	}

	/**
	 * 
	 * @param args
	 * @throws Exception
	 */ 
	public static void main_(String[] args) throws Exception {
		System.out.println("===========================");
		HttpClient test = new HttpClient();
		String url = "http://127.0.0.1:8083/api/wem/log";
		StringBuffer sndData = new StringBuffer("{  \"header\":{    \"source\":\"manager\",    \"service\":\"LogInfomationSelect\",    \"destination\":\"ServerResourceLog\"  },  \"body\":{} }");
		System.out.println(sndData.toString());
		System.out.println("===========================");
		String rcvData = test.sendRequestByJSON2(url,sndData.toString(),3000,"utf-8");
		System.out.println(rcvData.toString());
		System.out.println("===========================");
	}
	


}
