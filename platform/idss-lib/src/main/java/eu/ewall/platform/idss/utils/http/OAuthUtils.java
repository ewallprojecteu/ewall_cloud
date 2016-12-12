package eu.ewall.platform.idss.utils.http;

import java.io.UnsupportedEncodingException;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import java.security.SecureRandom;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.SecretKey;

import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * Helper methods for OAuth.
 *
 * @author Dennis Hofs (RRD)
 */
public class OAuthUtils {
	private String consumerKey;
	private String consumerSecret;

	/**
	 * Constructs a new instance.
	 *
	 * @param consumerKey the consumer key of the application
	 * @param consumerSecret the consumer secret of the application
	 */
	public OAuthUtils(String consumerKey, String consumerSecret) {
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
	}
	
	/**
	 * Builds a URL from the specified base URL and parameters. It adds all
	 * oauth_* parameters including the signature.
	 * 
	 * @param baseUrl the base URL
	 * @param params the parameters (can be empty or null)
	 * @param token the oauth token
	 * @param tokenSecret the oauth token secret
	 * @return the URL
	 */
	public URL buildUrl(String baseUrl, Map<String,String> params,
			String token, String tokenSecret) {
		Map<String,String> allParams = new LinkedHashMap<String,String>();
		if (params != null)
			allParams.putAll(params);
		allParams.putAll(getOAuthParams(token));
		addOAuthSignature(allParams, baseUrl, tokenSecret);
		String urlStr = baseUrl + "?" + URLParameters.getParameterString(
				allParams);
		try {
			return new URL(urlStr);
		} catch (MalformedURLException ex) {
			throw new RuntimeException("Invalid URL: " + urlStr + ": " +
					ex.getMessage(), ex);
		}
	}

	/**
	 * Gets oauth_* parameters with oauth_signature.
	 * 
	 * @param baseUrl the base URL (before parameters)
	 * @param token the OAuth token
	 * @param tokenSecret the OAuth token secret
	 * @return the oauth_* parameters with oauth_signature
	 */
	public Map<String,String> getSignedOAuthParams(String baseUrl,
			String token, String tokenSecret) {
		Map<String,String> params = getOAuthParams(token);
		addOAuthSignature(params, baseUrl, tokenSecret);
		return params;
	}

	/**
	 * Gets oauth_* parameters without oauth_signature. You can sign it
	 * afterwards with {@link #addOAuthSignature(Map, String, String)
	 * addOAuthSignature()}. You may add other oauth parameters in between.
	 * If you don't want to add other oauth parameters, you can use
	 * {@link #getSignedOAuthParams(String, String, String)
	 * getSignedOAuthParams()}.
	 * 
	 * @param token the token or null
	 * @return the oauth_* parameters
	 */
	public Map<String,String> getOAuthParams(String token) {
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("oauth_consumer_key", consumerKey);
		params.put("oauth_nonce", generateHexKey(128));
		params.put("oauth_signature_method", "HMAC-SHA1");
		params.put("oauth_timestamp", Long.toString(
				System.currentTimeMillis() / 1000));
		if (token != null)
			params.put("oauth_token", token);
		params.put("oauth_version", "1.0");
		return params;
	}

	/**
	 * Adds parameter oauth_signature to the specified oauth_* parameters.
	 * 
	 * @param params the oauth parameters
	 * @param baseUrl the base URL (before parameters)
	 * @param tokenSecret the token secret
	 */
	public void addOAuthSignature(Map<String,String> params, String baseUrl,
			String tokenSecret) {
		String paramStr = URLParameters.getSortedParameterString(params);
		String baseString;
		try {
			baseString = "GET" +
					"&" + URLEncoder.encode(baseUrl, "UTF-8") +
					"&" + URLEncoder.encode(paramStr, "UTF-8");
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException("UTF-8 not supported: " +
					ex.getMessage(), ex);
		}
		String secret = consumerSecret + "&" + tokenSecret;
		String signature = getSignature(baseString, secret);
		params.put("oauth_signature", signature);
	}

	/**
	 * Gets a signature for the specified text.
	 * 
	 * @param text the text
	 * @param secret the secret
	 * @return the Base64-encoded signature
	 */
	private String getSignature(String text, String secret) {
		SecretKey key = new SecretKeySpec(secret.getBytes(), "HmacSHA1");
		Mac mac;
		try {
			mac = Mac.getInstance("HmacSHA1");
			mac.init(key);
		} catch (Exception ex) {
			throw new RuntimeException("Can't init hash HmacSHA1: " +
					ex.getMessage(), ex);
		}
		byte[] bs = mac.doFinal(text.getBytes());
		return new String(Base64.encodeBase64(bs));
	}

	/**
	 * Generates a hexadecimal string key of the specified number of bits.
	 *
	 * @param bits the number of bits (multiple of 8)
	 * @return the hexadecimal string key
	 */
	private String generateHexKey(int bits) {
		StringBuilder builder = new StringBuilder();
		byte[] bs = new byte[bits / 8];
		SecureRandom random = new SecureRandom();
		random.nextBytes(bs);
		for (byte b : bs) {
			builder.append(String.format("%02x", b & 0xff));
		}
		return builder.toString();
	}
}
