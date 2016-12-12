package eu.ewall.platform.idss.dao.memdb;

import eu.ewall.platform.idss.dao.DatabaseConnection;
import eu.ewall.platform.idss.dao.DatabaseFactory;

import java.io.IOException;

/**
 * This database factory provides access to a single {@link
 * MemoryDatabaseConnection MemoryDatabaseConnection}.
 * 
 * @author Dennis Hofs (RRD)
 */
public class MemoryDatabaseFactory extends DatabaseFactory {
	private MemoryDatabaseConnection connection =
			new MemoryDatabaseConnection();

	@Override
	protected DatabaseConnection doConnect() throws IOException {
		return connection;
	}
}
