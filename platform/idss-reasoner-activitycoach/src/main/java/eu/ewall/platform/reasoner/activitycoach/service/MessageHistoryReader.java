package eu.ewall.platform.reasoner.activitycoach.service;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseCriteria;
import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.idss.service.model.common.message.PhysicalActivityMotivationalMessage;

import java.util.Collections;
import java.util.List;

/**
 * This class can read an {@link MessageHistory MessageHistory} from the
 * database.
 * 
 * @author Dennis Hofs
 */
public class MessageHistoryReader {
	
	/**
	 * Reads the message history for the specified user from the database.
	 * 
	 * @param database the database
	 * @param user the user
	 * @return the message history
	 * @throws Exception if an error occurs while reading from the database
	 */
	public static MessageHistory readMessageHistory(Database database,
			IDSSUserProfile user) throws Exception {
		DatabaseCriteria criteria = new DatabaseCriteria.Equal("user",
				user.getUsername());
		List<PhysicalActivityMotivationalMessage> history = database.select(
				new MotivationalMessageTable(), criteria, 0, null);
		Collections.sort(history, new MessageTimeComparator());
		return new MessageHistory(user, history);
	}
}
