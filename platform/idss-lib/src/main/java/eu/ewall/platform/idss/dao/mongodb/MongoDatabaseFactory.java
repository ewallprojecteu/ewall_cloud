package eu.ewall.platform.idss.dao.mongodb;

import eu.ewall.platform.idss.dao.DatabaseConnection;
import eu.ewall.platform.idss.dao.DatabaseFactory;

import java.io.IOException;

/**
 * This database factory creates instances of {@link MongoDatabase
 * MongoDatabase}.
 * 
 * @author Dennis Hofs (RRD)
 */
public class MongoDatabaseFactory extends DatabaseFactory {
	private String host = "localhost";
	private int port = 27017;
	
	/**
	 * Constructs a new factory with the default host name (localhost) and port
	 * number (27017) for the MongoDB server.
	 */
	public MongoDatabaseFactory() {
	}
	
	/**
	 * Constructs a new factory.
	 * 
	 * @param host the host name of the MongoDB server (default: localhost)
	 * @param port the port number of the MongoDB server (default: 27017)
	 */
	public MongoDatabaseFactory(String host, int port) {
		this.host = host;
		this.port = port;
	}

	/**
	 * Returns the host name of the MongoDB server. Default: localhost.
	 * 
	 * @return the host name of the MongoDB server
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Sets the host name of the MongoDB server. Default: localhost.
	 * 
	 * @param host the host name of the MongoDB server
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * Returns the port number of the MongoDB server. Default: 27017.
	 * 
	 * @return the port number of the MongoDB server
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Sets the port number of the MongoDB server. Default: 27017.
	 * 
	 * @param port the port number of the MongoDB server
	 */
	public void setPort(int port) {
		this.port = port;
	}

	@Override
	protected DatabaseConnection doConnect() throws IOException {
		return new MongoDatabaseConnection(host, port);
	}
}
