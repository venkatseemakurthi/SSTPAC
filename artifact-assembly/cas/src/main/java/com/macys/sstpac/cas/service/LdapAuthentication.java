package com.macys.sstpac.cas.service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.jasig.cas.adaptors.ldap.FastBindLdapAuthenticationHandler;
import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.ContextSource;
import org.springframework.stereotype.Component;


@Component
public class LdapAuthentication implements InitializingBean {
	private FastBindLdapAuthenticationHandler authenticationHandler;

	@Value("${ldap.filter.MCOM}")
	private String ldapFilter;

	@Autowired
	private ContextSource contextSource;

	/**
	 * Initialize connection to LDAP while startup for authenticcation
	 * 
	 */
	public void afterPropertiesSet() throws Exception {
		authenticationHandler = new FastBindLdapAuthenticationHandler();
		authenticationHandler.setFilter(ldapFilter);
		authenticationHandler.setContextSource(contextSource);
		authenticationHandler.afterPropertiesSet();
	}

	public String getLdapFilter() {
		return ldapFilter;
	}

	public void setLdapFilter(String ldapFilter) {
		this.ldapFilter = ldapFilter;
	}

	public ContextSource getContextSource() {
		return contextSource;
	}

	public void setContextSource(ContextSource contextSource) {
		this.contextSource = contextSource;
	}

	/**
	 * @param credentials
	 * @return
	 * @throws AuthenticationException
	 */
	public boolean authenticate(Credentials credentials)
			throws AuthenticationException {
		return authenticationHandler.authenticate(credentials);
	}

	public boolean groupAuthorization(UsernamePasswordCredentials credentials) {
		DataOutputStream dataOutputStream;
		DataInputStream dataInputStream;
		boolean authorization = false;
		String userName ="";
		try {

			URL authenticateURL = new URL(
					"http://vcscoral/sfsb/getUserInfoFromLDAP?username="
							+ credentials.getUsername());

			URLConnection authenticatURLConnection = authenticateURL
					.openConnection();
			authenticatURLConnection.setDoInput(true);
			authenticatURLConnection.setDoOutput(true);
			authenticatURLConnection.setUseCaches(false);
			authenticatURLConnection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			dataOutputStream = new DataOutputStream(authenticatURLConnection
					.getOutputStream());
			dataOutputStream.writeBytes("USERNAME="
					+ URLEncoder.encode(credentials.getUsername())
					+ "&PASSWORD="
					+ URLEncoder.encode(credentials.getPassword()));
			dataOutputStream.flush();
			dataOutputStream.close();

			dataInputStream = new DataInputStream(authenticatURLConnection
					.getInputStream());
			
			String inputLine;
			boolean printflag = false;

			List<String> groups = new ArrayList<String>();
			while ((inputLine = dataInputStream.readLine()) != null) {
				if (inputLine.endsWith("GROUPS:")) {
					printflag = true;
				} else if (inputLine.endsWith("</pre>")) {
					printflag = false;
				}
				
				if (inputLine.startsWith("email")){
					userName = inputLine.substring(inputLine.indexOf(":")+1, inputLine.length());
					userName = userName.trim();
					System.out.println("userName"+userName);
				}
					
				if (printflag) {
					groups.add(inputLine.toString());
				}
				if (inputLine.toString().trim().equalsIgnoreCase(
						"MacysDotComUsers")) {
					authorization = true;
				}
			}

			String str;
			FileOutputStream fos = new FileOutputStream("postto.txt");
			while (null != ((str = dataInputStream.readLine()))) {
				if (str.length() > 0) {
					fos.write(str.getBytes());
					fos.write(new String("\n").getBytes());
				}
			}
			fos.close();
			dataInputStream.close();
			
			if(authorization)
				credentials.setUsername(userName);
		} catch (MalformedURLException mue) {
			System.out.println(mue);
		} catch (IOException ioe) {
			System.out.println(ioe);
		}

		return authorization;

	}
}