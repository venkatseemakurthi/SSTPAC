package com.macys.sstpac.splunk.framework.services;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import org.springframework.stereotype.Service;

import com.macys.sstpac.splunk.framework.certificate.SplunkTrustManager;

@Service
public class SplunkService {


	private SSLContext getSSLContext() throws Exception {
		SSLContext sslctx = SSLContext.getInstance("SSL");
		sslctx.init(null, new X509TrustManager[] { new SplunkTrustManager() },
				null);
		return sslctx;
	}

	public StringBuffer postHttpURLConnection(String splunkQuery,String requestMethod)
			throws Exception {
		
	    StringBuffer xmlResponse = new StringBuffer();
	    HttpsURLConnection httpsURLConnection = null;
		try {	
			DataOutputStream postOut = new DataOutputStream(httpsURLConnection.getOutputStream());
			postOut.writeBytes("search=search " + splunkQuery);
			postOut.flush();
			postOut.close();
			httpsURLConnection.connect();
			System.out.println("Response Code"+httpsURLConnection.getResponseCode());
			if (httpsURLConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						httpsURLConnection.getInputStream()));
				String line;
				while ((line = br.readLine()) != null) {
					xmlResponse.append(line);
				}
				br.close();
			}
			httpsURLConnection.disconnect();
            System.out.println("xmlResponse"+xmlResponse);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return xmlResponse;
	}

}
