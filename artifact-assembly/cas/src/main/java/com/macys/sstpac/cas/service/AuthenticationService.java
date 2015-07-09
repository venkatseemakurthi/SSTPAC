package com.macys.sstpac.cas.service;

import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.macys.sstpac.cas.constants.AuthStatus;

@Component("authService")
public class AuthenticationService {
  
	@Autowired
	private LdapAuthentication ldapAuthentication;
	
	public LdapAuthentication getLdapAuthentication() {
		return ldapAuthentication;
	}

	public void setLdapAuthentication(LdapAuthentication ldapAuthentication) {
		this.ldapAuthentication = ldapAuthentication;
	}

	private AuthStatus authStatus;
	
	/**
	 * Tests for the Login of the user w.r.t the passed username and password.
	 * 
	 * @param credentials
	 *            to authenticate, with the username and password set as given
	 *            by the user
	 * @return LoginStatus which set to SUCCESS if password match
	 *         otherwise sets it to FAILED.
	 */
	public AuthStatus login(UsernamePasswordCredentials credentials) {	
		if(validateCredentials(credentials)){
			return authStatus;
		}
		authStatus =authenticateUser(credentials);
		return authStatus;
	}
	
	/**
	 * Validate whether UserName or Password are empty 
	 * 
	 * @param credentials
	 * @return boolean
	 */
	private boolean validateCredentials(UsernamePasswordCredentials credentials){
		boolean isValid = false;
		if(credentials.getUsername() == null || credentials.getUsername().equals("")){
			authStatus = AuthStatus.USERNAME_EMPTY;
			isValid = true;
		}
		if(credentials.getPassword() == null || credentials.getPassword().equals("")){
			authStatus = AuthStatus.PASSWORD_EMPTY;
			isValid = true;
		}
		return isValid;
	}
	
	/**
	 * Tests for the authentication of the user w.r.t the passed username and password using LDAP Service.
	 * 
	 * @param credentials
	 *            to authenticate, with the username and password set as given
	 *            by the user
	 * @return LoginStatus which set to SUCCESS if password match
	 *         otherwise sets it to AUTHENTICATION_FAILED.
	 */
	public AuthStatus authenticateUser(UsernamePasswordCredentials credentials) {
		AuthStatus authStatus = AuthStatus.AUTHENTICATION_FAILED;
		try {
			if(ldapAuthentication.authenticate(credentials)&&ldapAuthentication.groupAuthorization(credentials)){
				authStatus = AuthStatus.SUCESSS;
			}
			
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}
		return authStatus;
	}
	
}
