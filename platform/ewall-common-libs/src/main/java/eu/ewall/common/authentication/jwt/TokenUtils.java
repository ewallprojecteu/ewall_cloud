package eu.ewall.common.authentication.jwt;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class TokenUtils.
 */
public class TokenUtils
{
	
	/** The log. */
	static Logger log = LoggerFactory.getLogger(TokenUtils.class);

	/** The Constant ROLE. */
	public static final String ROLE = "role";
	

	/**
	 * Creates the token.
	 *
	 * @param username the username
	 * @param roles the roles
	 * @param applications the applications
	 * @param services the services
	 * @param durationMins the duration mins
	 * @param secret the secret
	 * @return the string
	 */
	public static String createToken(String username, ArrayList<String> roles, ArrayList<String> applications, ArrayList<String> services, int durationMins, String secret) {
		
		Map<String,Object> jwsHeader = new HashMap<String, Object>();
		jwsHeader.put("typ", "JWT");
		jwsHeader.put("alg", "HS256");
		
		Claims claims = Jwts.claims();
		
		//In eWALL we only have one role per user. 
		//Anyway, for sake of generality we iterate on roles and set them into a semi-colon separated values string
		String role="";
		for(String roleItem : roles){
			role=role+roleItem+";";
		}
		//Remove last semi-colon
		role=role.substring(0,role.lastIndexOf(";"));
		
		//Add role
		claims.put("role", role);
		
		//Add applications
		for(String applicationName : applications){
			claims.put(applicationName, true);
		}
		
		//Add services
		for(String serviceName : services){
			claims.put(serviceName, true);
		}
		//I put issue date in the claims, because it is needed to manage token expiration (clients might have different clock form the server)
		Date now = new Date();
		//get timestamp in seconds
		long issuedTimestamp = now.getTime()/1000;
		claims.put("iss", String.valueOf(issuedTimestamp));
		
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE,  durationMins);
		claims.setExpiration(c.getTime());
		String compact = Jwts.builder().setHeader(jwsHeader).setIssuer("ewall").setId("ewall"+UUID.randomUUID().toString()).setIssuedAt(new Date()).setExpiration(c.getTime()).setClaims(claims).setSubject(username).signWith(HS256, secret.getBytes()).compact();
		
		log.info("Created token for user: " + username + ", role: " + role);
		log.info("Created token: " + compact);
		log.info("Expires at: " + c.getTime());
		
		return compact;
	}

	
	/**
	 * Creates the system token.
	 *
	 * @param serviceName the service name
	 * @param secret the secret
	 * @return the string
	 */
	public static String createSystemToken(String serviceName, String secret) {
		
		Map<String,Object> jwsHeader = new HashMap<String, Object>();
		jwsHeader.put("typ", "JWT");
		jwsHeader.put("alg", "HS256");
		
		Claims claims = Jwts.claims();
		
		String role="SYSTEM";

		//Add role
		claims.put("role", role);
		
		claims.put("allservices", true);
		
		Calendar c = Calendar.getInstance();
		//System token never expires
		c.add(Calendar.YEAR,  100);
		claims.setExpiration(c.getTime());
		String compact = Jwts.builder().setHeader(jwsHeader).setIssuer("ewall").setId("ewall"+UUID.randomUUID().toString()).setIssuedAt(new Date()).setExpiration(c.getTime()).setClaims(claims).setSubject(serviceName).signWith(HS256, secret.getBytes()).compact();
		
		log.info("System token: " + compact);
		log.info("Expires at: " + c.getTime());
		return compact;
	}


	/**
	 * Gets the user name from token.
	 *
	 * @param token the token
	 * @param secret the secret
	 * @return the user name from token
	 */
	public static String getUserNameFromToken(String token, String secret){
		try {
			String subject = Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody().getSubject();
			return subject;
		} catch (ExpiredJwtException e) {
			log.info("Token has expired", e);
		} catch (UnsupportedJwtException e) {
			log.info("Unsupported JWT", e);
		} catch (MalformedJwtException e) {
			log.info("Malformed JWT", e);
		} catch (SignatureException e) {
			log.info("Signature error", e);
		} catch (IllegalArgumentException e) {
			log.info("Illegal argument", e);
		}
			
		return null;
	}

	

	/**
	 * Verify token.
	 *
	 * @param token the token
	 * @param serviceName the service name
	 * @param secret the secret
	 * @return true, if successful
	 */
	public static boolean  verifyToken(String token, String serviceName, String secret){
		try {
			Claims claims = Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
			for(String key : claims.keySet()){
				log.debug("claim " + key + ": "+claims.get(key));
			}
			
			String role = (String)claims.get(ROLE);
			log.debug("Role: " + role);
			if(role.equals("SYSTEM")){
				return true;
			}
			log.debug("Expires at: " + claims.getExpiration());
			if(claims.containsKey(serviceName) && ((Boolean)claims.get(serviceName))){
				log.debug("Access to service " + serviceName + " allowed");
				return true;
			}
			
			log.info("Access to service " + serviceName + "  DENIED (not allowed by claims)!");
			return false;
		} catch (ExpiredJwtException e) {
			log.info("Access to service " + serviceName + "  DENIED - passed expiration time", e);
		} catch (UnsupportedJwtException e) {
			log.info("Access to service " + serviceName + "  DENIED - unsupported JWT", e);
		} catch (MalformedJwtException e) {
			log.info("Access to service " + serviceName + "  DENIED - malformed JWT", e);
		} catch (SignatureException e) {
			log.info("Access to service " + serviceName + "  DENIED - signature error", e);
		} catch (IllegalArgumentException e) {
			log.info("Access to service DENIED - illegal argument", e);
		}
		return false;
		
	}

	/**
	 * Validate token.
	 *
	 * @param token the token
	 * @param secret the secret
	 * @return true, if successful
	 */
	public static boolean validateToken(String token, String secret){
		try {
			Claims claims = Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
			for(String key : claims.keySet()){
				log.debug("claim " + key + ": "+claims.get(key));
			}
			
			String role = (String)claims.get("role");
			log.debug("Role: " + role);
			log.debug("Expires at: " + claims.getExpiration());
			return true;
		} catch (ExpiredJwtException e) {
			log.info("Token has expired", e);
		} catch (UnsupportedJwtException e) {
			log.info("Unsupported JWT", e);
		} catch (MalformedJwtException e) {
			log.info("Malformed JWT", e);
		} catch (SignatureException e) {
			log.info("Signature error", e);
		} catch (IllegalArgumentException e) {
			log.info("Illegal argument", e);
		}
		return false;
		
	}

}