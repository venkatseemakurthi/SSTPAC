package  com.macys.sstpac.restservice.exception;

import com.macys.sstpac.restservice.RestServiceResponse;

/**
 * Thrown for 429
 */
@SuppressWarnings("serial")
public class ResourceThrottledException extends RestResourceInvocationException {

	static int statusCode = 429;

	public ResourceThrottledException(RestServiceResponse restResponse) {
		super(statusCode);
		super.restResponse = restResponse;
	}
}
