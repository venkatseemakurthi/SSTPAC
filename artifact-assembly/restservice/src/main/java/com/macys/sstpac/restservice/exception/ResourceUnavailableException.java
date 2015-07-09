package  com.macys.sstpac.restservice.exception;

import com.macys.sstpac.restservice.RestServiceResponse;

/**
 * Thrown for 204
 */
@SuppressWarnings("serial")
public class ResourceUnavailableException extends RestResourceInvocationException {

	static int statusCode = 204;

	public ResourceUnavailableException(RestServiceResponse restResponse) {
		super(statusCode);
		super.restResponse = restResponse;
	}
}
