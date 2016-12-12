package eu.ewall.common.authentication.jwt;

import io.jsonwebtoken.Jwts;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;

import eu.ewall.common.filter.FilterOrder;

/**
 * The Class JwtFilter.
 */
@Order(FilterOrder.JWT)
public class JwtFilter implements Filter {
	
	/** The log. */
	private Logger log = LoggerFactory.getLogger(JwtFilter.class);

	/** The secret. */
	private String secret;
	
	/** The service name. */
	private String serviceName;
	
	/** The exclusions. */
	private List<String> exclusions;
	
	/** The path matcher. */
	private PathMatcher pathMatcher = new AntPathMatcher();

	/**
	 * Sets the secret.
	 *
	 * @param secret the new secret
	 */
	public void setSecret(String secret) {
		this.secret = secret;
	}

	/**
	 * Sets the service name.
	 *
	 * @param serviceName the new service name
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	/**
	 * Sets the exclusions.
	 *
	 * @param exclusions the new exclusions
	 */
	public void setExclusions(List<String> exclusions) {
		this.exclusions = exclusions;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		if (isCorsPreflightRequest(httpRequest)	|| isExcluded(httpRequest.getServletPath())) {
			filterChain.doFilter(request, response);
			return;
		}
		String token = httpRequest.getHeader("X-Auth-Token");
		log.debug("token: " + token);
		if (token != null && TokenUtils.verifyToken(token, serviceName, secret)) {
			request.setAttribute(TokenUtils.ROLE, Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody().get(TokenUtils.ROLE));
			filterChain.doFilter(request, response);
		} else {
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}
	
	/**
	 * Checks if is cors preflight request.
	 *
	 * @param httpRequest the http request
	 * @return true, if is cors preflight request
	 */
	private boolean isCorsPreflightRequest(HttpServletRequest httpRequest) {
		return httpRequest.getMethod().equals(RequestMethod.OPTIONS.toString()) 
				&& httpRequest.getHeader("Access-Control-Request-Method") != null;
	}
	
	/**
	 * Checks if is excluded.
	 *
	 * @param path the path
	 * @return true, if is excluded
	 */
	private boolean isExcluded(String path) {
		if (exclusions != null) {
			for (String exclusion : exclusions) {
				if (pathMatcher.match(exclusion, path)) {
					return true;
				}
			}
		}
		return false;
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