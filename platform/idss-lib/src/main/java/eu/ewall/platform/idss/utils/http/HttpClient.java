package eu.ewall.platform.idss.utils.http;

import com.fasterxml.jackson.core.type.TypeReference;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.ewall.platform.idss.utils.AppComponents;

import eu.ewall.platform.idss.utils.io.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;

/**
 * This class can be used to run HTTP requests. At construction it takes a URL.
 * You may configure the client further by setting the HTTP method (default
 * GET) and adding headers and query parameters (if you didn't include them in
 * the URL). After that there are various methods to write data (optional) and
 * finally to get the response and read data. When you no longer need the
 * client, you should call {@link #close() close()}.
 * 
 * <p>Any strings will be read and written as UTF-8.</p>
 * 
 * @author Dennis Hofs (RRD)
 */
public class HttpClient {
	private String method = "GET";
	private String url;
	private Map<String,String> queryParams = new LinkedHashMap<String,String>();
	private Map<String,String> headers = new LinkedHashMap<String,String>();
	private boolean wrotePostParam = false;

	private HttpURLConnection connection = null;
	private InputStream input = null;
	private Reader reader = null;
	private OutputStream output = null;
	private Writer writer = null;
	private final Object lock = new Object();
	private boolean closed = false;

	/**
	 * Constructs a new HTTP client. If you want to use query parameters in the
	 * HTTP request, you can specify the URL without query parameters and then
	 * call {@link #addQueryParam(String, String) addQueryParam()}.
	 * Alternatively you can include the query parameters in the URL.
	 * 
	 * @param url the URL
	 */
	public HttpClient(String url) {
		this.url = url;
	}

	/**
	 * Closes this client. You should always call this method when you no
	 * longer need the client.
	 */
	public void close() {
		synchronized (lock) {
			if (closed)
				return;
			Logger logger = AppComponents.getLogger(getClass().getSimpleName());
			closed = true;
			try {
				if (reader != null)
					reader.close();
				else if (input != null)
					input.close();
			} catch (IOException ex) {
				logger.error("Can't close input: " + ex.getMessage());
			}
			try {
				if (writer != null)
					writer.close();
				else if (output != null)
					output.close();
			} catch (IOException ex) {
				logger.error("Can't close output: " + ex.getMessage());
			}
			if (connection != null)
				connection.disconnect();
		}
	}

	/**
	 * Sets the HTTP method. The default is GET.
	 * 
	 * @param method the HTTP method
	 * @return this client (so you can chain method calls)
	 */
	public HttpClient setMethod(String method) {
		this.method = method;
		return this;
	}

	/**
	 * Adds a query parameter. This will be appended to the request URL. You
	 * should only call this method if you didn't include query parameters in
	 * the URL at construction.
	 * 
	 * @param name the parameter name
	 * @param value the parameter value
	 * @return this client (so you can chain method calls)
	 */
	public HttpClient addQueryParam(String name, String value) {
		queryParams.put(name, value);
		return this;
	}

	/**
	 * Sets a map with query parameters. They will be appended to the request
	 * URL. You should only call this method if you didn't include query
	 * parameters in the URL at construction. This method overwrites any
	 * parameters you have added with {@link #addQueryParam(String, String)
	 * addQueryParam()}.
	 * 
	 * @param params the query parameters
	 * @return this client (so you can chain method calls)
	 */
	public HttpClient setQueryParams(Map<String,String> params) {
		this.queryParams = params;
		return this;
	}

	/**
	 * Adds a header to the HTTP request. Note that some of the write methods
	 * can automatically set the Content-Type header, so you don't need to
	 * specify it here.
	 * 
	 * @param name the header name
	 * @param value the header value
	 * @return this client (so you can chain method calls)
	 */
	public HttpClient addHeader(String name, String value) {
		headers.put(name, value);
		return this;
	}

	/**
	 * Sets a map with the HTTP headers. Note that some of the write methods
	 * can automatically set the Content-Type header, so you don't need to
	 * specify it here. This method overwrites any headers you have added with
	 * {@link #addHeader(String, String) addHeader()}.
	 * 
	 * @param headers the headers
	 * @return this client (so you can chain method calls)
	 */
	public HttpClient setHeaders(Map<String,String> headers) {
		this.headers = headers;
		return this;
	}

	/**
	 * Returns the URL including query parameters.
	 * 
	 * @return the URL including query parameters
	 */
	private String getUrl() {
		String url = this.url;
		if (queryParams != null && !queryParams.isEmpty())
			url += "?" + URLParameters.getParameterString(queryParams);
		return url;
	}

	/**
	 * Opens the HTTP connection to the specified URL and possible query
	 * parameters. It sets the specified HTTP method and headers. Before
	 * writing or reading you can configure the connection further.
	 * 
	 * @return the connection
	 * @throws IOException if an error occurs while communicating with the HTTP
	 * server
	 */
	private HttpURLConnection getConnection() throws IOException {
		synchronized (lock) {
			if (closed)
				throw new IOException("HttpClient closed");
			if (this.connection != null)
				return this.connection;
		}
		URL url = new URL(getUrl());
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod(method);
		for (String header : headers.keySet()) {
			conn.setRequestProperty(header, headers.get(header));
		}
		synchronized (lock) {
			if (closed) {
				conn.disconnect();
				throw new IOException("HttpClient closed");
			}
			if (this.connection == null)
				this.connection = conn;
			else
				conn.disconnect();
		}
		return this.connection;
	}

	/**
	 * Returns the output stream to write data to the HTTP content. Before
	 * calling this method you must have configured the client (method,
	 * headers, query parameters). This method will initialise the connection
	 * and open the output stream if that wasn't done yet. The output stream
	 * will be closed automatically when you read the response.
	 * 
	 * @return the output stream
	 * @throws IOException if an error occurs while communicating with the HTTP
	 * server
	 */
	public OutputStream getOutputStream() throws IOException {
		HttpURLConnection conn = getConnection();
		synchronized (lock) {
			if (closed)
				throw new IOException("HttpClient closed");
			if (output != null)
				return output;
		}
		conn.setDoOutput(true);
		OutputStream output = conn.getOutputStream();
		synchronized (lock) {
			if (closed) {
				output.close();
				throw new IOException("HttpClient closed");
			}
			if (this.output == null)
				this.output = output;
			else if (this.output != output)
				output.close();
			return this.output;
		}
	}

	/**
	 * Returns a writer to write data to the HTTP content. Before calling this
	 * method you must have configured the client (method, headers, query
	 * parameters). This method will initialise the connection and open the
	 * output if that wasn't done yet. The writer will be closed automatically
	 * when you read the response.
	 * 
	 * @return the writer
	 * @throws IOException if an error occurs while communicating with the HTTP
	 * server
	 */
	public Writer getWriter() throws IOException {
		synchronized (lock) {
			if (closed)
				throw new IOException("HttpClient closed");
			if (writer != null)
				return writer;
		}
		Writer writer = new OutputStreamWriter(getOutputStream(), "UTF-8");
		synchronized (lock) {
			if (closed) {
				writer.close();
				throw new IOException("HttpClient closed");
			}
			if (this.writer == null)
				this.writer = writer;
			else if (this.writer != writer)
				writer.close();
			return this.writer;
		}
	}
	
	/**
	 * Writes a POST parameter to the HTTP content. It writes POST parameters
	 * as a URL-encoded parameter string. Before calling this method you must
	 * have configured the client (method, headers, query parameters). This
	 * method will initialise the connection, set header Content-Type to
	 * application/x-www-form-urlencoded, and open the output if that wasn't
	 * done yet. You can repeat this method for multiple parameters. The output
	 * will be closed automatically when you read the response.
	 * 
	 * @param name the parameter name
	 * @param value the parameter value
	 * @return this client (so you can chain method calls)
	 * @throws IOException if an error occurs while communicating with the HTTP
	 * server
	 */
	public HttpClient writePostParam(String name, String value)
			throws IOException {
		HttpURLConnection conn = getConnection();
		if (output == null) {
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
		}
		Writer writer = getWriter();
		if (wrotePostParam)
			writer.write("&");
		wrotePostParam = true;
		writer.write(name + "=" + URLEncoder.encode(value, "UTF-8"));
		return this;
	}
	
	/**
	 * Writes the specified object as a JSON string using the Jackson {@link
	 * ObjectMapper ObjectMapper}. Before calling this method you must have
	 * configured the client (method, headers, query parameters). This method
	 * will initialise the connection, set header Content-Type to
	 * application/json, and open the output if that wasn't done yet. The
	 * output will be closed automatically when you read the response.
	 * 
	 * @param obj the object
	 * @return this client (so you can chain method calls)
	 * @throws IOException if a writing error occurs
	 */
	public HttpClient writeJson(Object obj) throws IOException {
		HttpURLConnection conn = getConnection();
		if (output == null)
			conn.setRequestProperty("Content-Type", "application/json");
		Writer writer = getWriter();
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(writer, obj);
		return this;
	}
	
	/**
	 * Writes a string to the HTTP content. Before calling this method you must
	 * have configured the client (method, headers, query parameters). This
	 * method will initialise the connection and open the output if that wasn't
	 * done yet. You can repeat this method if you want to write more. The
	 * output will be closed automatically when you read the response.
	 * 
	 * @param content the string content
	 * @return this client (so you can chain method calls)
	 * @throws IOException if an error occurs while communicating with the HTTP
	 * server
	 */
	public HttpClient writeString(String content) throws IOException {
		Writer writer = getWriter();
		writer.write(content);
		return this;
	}

	/**
	 * Writes a byte array to the HTTP content. Before calling this method you
	 * must have configured the client (method, headers, query parameters).
	 * This method will initialise the connection and open the output if that
	 * wasn't done yet. You can repeat this method if you want to write more.
	 * The output will be closed automatically when you read the response.
	 * 
	 * @param bs the byte array
	 * @return this client (so you can chain method calls)
	 * @throws IOException if an error occurs while communicating with the HTTP
	 * server
	 */
	public HttpClient writeBytes(byte[] bs) throws IOException {
		OutputStream output = getOutputStream();
		output.write(bs);
		return this;
	}
	
	/**
	 * Gets the HTTP response and then returns the HTTP connection from which
	 * details about the response can be obtained. You should call this method
	 * after configuring the client (method, headers, query parameters) and
	 * optionally writing data. This method will initialise the connection, get
	 * the response and open the input if that wasn't done yet.
	 * 
	 * <p>If the response code is 4xx or 5xx, it will throw a {@link
	 * HttpClientException HttpClientException}. Redirects are not
	 * supported.</p>
	 * 
	 * <p>This class has methods to read data from the response, which you can
	 * call instead of or along with this method.</p>
	 * 
	 * @return the HTTP connection
	 * @throws HttpClientException if the HTTP request returned an error
	 * response
	 * @throws IOException if an error occurs while communicating with the HTTP
	 * server
	 */
	public HttpURLConnection getResponse() throws HttpClientException,
	IOException {
		getInputStream();
		return getConnection();
	}
	
	/**
	 * Returns the input stream to read data from the HTTP response. You should
	 * call this method after configuring the client (method, headers, query
	 * parameters) and optionally writing data. This method will initialise the
	 * connection, get the response and open the input stream if that wasn't
	 * done yet.
	 * 
	 * <p>If the response code is 4xx or 5xx, it will throw a {@link
	 * HttpClientException HttpClientException}. Redirects are not
	 * supported.</p>
	 * 
	 * @return the input stream
	 * @throws HttpClientException if the HTTP request returned an error
	 * response
	 * @throws IOException if an error occurs while communicating with the HTTP
	 * server
	 */
	public InputStream getInputStream() throws HttpClientException,
	IOException {
		synchronized (lock) {
			if (closed)
				throw new IOException("HttpClient closed");
			if (this.input != null)
				return this.input;
			if (writer != null)
				writer.close();
			else if (output != null)
				output.close();
		}
		HttpURLConnection conn = getConnection();
		int respCode = conn.getResponseCode();
		String respMessage = conn.getResponseMessage();
		if (respCode / 100 == 4 || respCode / 100 == 5) {
			InputStream errorStream = conn.getErrorStream();
			String errorContent;
			try {
				errorContent = FileUtils.readFileString(errorStream);
			} finally {
				errorStream.close();
			}
			throw new HttpClientException(respCode, respMessage, errorContent);
		}
		InputStream input = conn.getInputStream();
		synchronized (lock) {
			if (closed) {
				input.close();
				throw new IOException("HttpClient closed");
			}
			if (this.input == null)
				this.input = input;
			else if (this.input != input)
				input.close();
			return this.input;
		}
	}
	
	/**
	 * Returns the reader to read data from the HTTP response. You should call
	 * this method after configuring the client (method, headers, query
	 * parameters) and optionally writing data. This method will initialise the
	 * connection, get the response and open the input if that wasn't done yet.
	 * 
	 * <p>If the response code is 4xx or 5xx, it will throw a {@link
	 * HttpClientException HttpClientException}. Redirects are not
	 * supported.</p>
	 * 
	 * @return the reader
	 * @throws HttpClientException if the HTTP request returned an error
	 * response
	 * @throws IOException if an error occurs while communicating with the HTTP
	 * server
	 */
	public Reader getReader() throws HttpClientException, IOException {
		synchronized (lock) {
			if (closed)
				throw new IOException("HttpClient closed");
			if (reader != null)
				return reader;
		}
		Reader reader = new InputStreamReader(getInputStream(), "UTF-8");
		synchronized (lock) {
			if (closed) {
				reader.close();
				throw new IOException("HttpClient closed");
			}
			if (this.reader == null)
				this.reader = reader;
			else if (this.reader != reader)
				reader.close();
			return this.reader;
		}
	}

	/**
	 * Reads the HTTP response as a string. You should call this method after
	 * configuring the client (method, headers, query parameters) and
	 * optionally writing data. This method will initialise the connection, get
	 * the response and open the input if that wasn't done yet.
	 * 
	 * @return the response string
	 * @throws HttpClientException if the HTTP request returned an error
	 * response
	 * @throws IOException if an error occurs while communicating with the HTTP
	 * server
	 */
	public String readString() throws HttpClientException, IOException {
		return FileUtils.readFileString(getReader());
	}
	
	/**
	 * Reads the HTTP response as a JSON string and converts it to an object of
	 * the specified class using the Jackson {@link ObjectMapper ObjectMapper}.
	 * You should call this method after configuring the client (method,
	 * headers, query parameters) and optionally writing data. This method will
	 * initialise the connection, get the response and open the input if that
	 * wasn't done yet.
	 * 
	 * @param clazz the result class
	 * @return the result object
	 * @throws HttpClientException if the HTTP request returned an error
	 * response
	 * @throws IOException if an error occurs while communicating with the HTTP
	 * server
	 */
	public <T> T readJson(Class<T> clazz) throws HttpClientException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(getReader(), clazz);
	}
	
	/**
	 * Reads the HTTP response as a JSON string and converts it to an object of
	 * the specified result type using the Jackson {@link ObjectMapper
	 * ObjectMapper}. You should call this method after configuring the client
	 * (method, headers, query parameters) and optionally writing data. This
	 * method will initialise the connection, get the response and open the
	 * input if that wasn't done yet.
	 * 
	 * <p>If you want to convert to MyObject, you can specify:<br />
	 * new TypeReference&lt;MyObject&gt;() {}</p>
	 * 
	 * @param typeRef the result type
	 * @return the result object
	 * @throws HttpClientException if the HTTP request returned an error
	 * response
	 * @throws IOException if an error occurs while communicating with the HTTP
	 * server
	 */
	public <T> T readJson(TypeReference<T> typeRef) throws HttpClientException,
	IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(getReader(), typeRef);
	}

	/**
	 * Reads the HTTP response as a byte array. You should call this method
	 * after configuring the client (method, headers, query parameters) and
	 * optionally writing data. This method will initialise the connection, get
	 * the response and open the input if that wasn't done yet.
	 * 
	 * @return the response bytes
	 * @throws HttpClientException if the HTTP request returned an error
	 * response
	 * @throws IOException if an error occurs while communicating with the HTTP
	 * server
	 */
	public byte[] readBytes() throws HttpClientException, IOException {
		return FileUtils.readFileBytes(getInputStream());
	}
}
