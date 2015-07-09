package com.macys.sstpac.restservice.framework.client.api;

import javax.ws.rs.client.Client;

import com.macys.sstpac.restservice.framework.client.exception.RestClientException;

public interface RestClientFactory {

	/**
	 * This will have the dynamic/runtime implementation of pool of
	 * objects/connections
	 * 
	 * @author Kazim
	 *
	 */
	static interface JaxRSWrapperPool {

	}

	/**
	 * This interface is being used to get the Client connection object
	 * 
	 * @author Kazim
	 *
	 */
	static interface JaxRSClientPool {
		/**
		 * 
		 * @return the hostName of the service
		 */
		public String getHostName();

		/**
		 * 
		 * @return Return the basePath of the service
		 */
		public String getBasePath();

		public Client getClient(String poolName) throws RestClientException;
	}

}
