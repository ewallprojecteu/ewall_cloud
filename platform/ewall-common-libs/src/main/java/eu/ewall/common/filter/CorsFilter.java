package eu.ewall.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * The Class CorsFilter.
 */
@Order(FilterOrder.CORS)
public class CorsFilter implements Filter {

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		// Check if it's a CORS request
		if (httpRequest.getHeader("Origin") != null) {
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.setHeader("Access-Control-Allow-Origin", "*");
			// Check if it's a preflight request
			if (httpRequest.getMethod().equals(RequestMethod.OPTIONS.toString()) 
					&& httpRequest.getHeader("Access-Control-Request-Method") != null) {
				httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
				httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, X-Auth-Token");
				httpResponse.setHeader("Access-Control-Max-Age", "3600");
			}
		}
		chain.doFilter(request, response);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}
	
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {}

}
