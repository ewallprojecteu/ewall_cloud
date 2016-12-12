package eu.ewall.common.authentication.jwt;

import java.io.IOException;




import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

/**
 * The Class TokenHeaderInterceptor.
 */
public class TokenHeaderInterceptor implements ClientHttpRequestInterceptor {

	/** The service name. */
	private String serviceName;
	
	/** The secret. */
	private String secret;
	
	/** The token. */
	String token;
	
	/**
	 * Instantiates a new token header interceptor.
	 *
	 * @param serviceName the service name
	 * @param secret the secret
	 */
	public TokenHeaderInterceptor(String serviceName, String secret) {
		this.serviceName=serviceName;
		this.secret=secret;
		this.token=token=TokenUtils.createSystemToken(serviceName, secret);
	}
	
 	/* (non-Javadoc)
	  * @see org.springframework.http.client.ClientHttpRequestInterceptor#intercept(org.springframework.http.HttpRequest, byte[], org.springframework.http.client.ClientHttpRequestExecution)
	  */
	 @Override
    public ClientHttpResponse intercept(
            HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {

        HttpHeaders headers = request.getHeaders();
        headers.add("X-Auth-Token", token);
         return execution.execute(request, body);
    }
}