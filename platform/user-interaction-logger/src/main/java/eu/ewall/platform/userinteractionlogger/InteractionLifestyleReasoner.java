package eu.ewall.platform.userinteractionlogger;

import java.io.IOException;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseCriteria;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseSort;
import eu.ewall.platform.idss.dao.DatabaseTableDef;
import eu.ewall.platform.idss.service.lr.DefaultLRDataClassifier;
import eu.ewall.platform.idss.service.lr.LRDataClassifier;
import eu.ewall.platform.idss.service.lr.WeekDayLifestyleReasoner;
import eu.ewall.platform.idss.service.lr.WeekPatternParameter;
import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.idss.service.model.common.AverageDataParameter;
import eu.ewall.platform.idss.service.model.common.DataUpdated;
import eu.ewall.platform.userinteractionlogger.model.LRAveragesTable;
import eu.ewall.platform.userinteractionlogger.model.LRSourceUpdatedTable;
import eu.ewall.platform.userinteractionlogger.model.LRWeekPatternUpdatedTable;
import eu.ewall.platform.userinteractionlogger.model.UserInteractionLog;
import eu.ewall.platform.userinteractionlogger.model.UserInteractionLogTable;

public class InteractionLifestyleReasoner extends
WeekDayLifestyleReasoner<InteractionLRSourceDayData> {
	public static final String LOGTAG = "InteractionLifestyleReasoner";

	@Override
	protected String getLogTag() {
		return LOGTAG;
	}

	@Override
	protected LRDataClassifier<InteractionLRSourceDayData> getClassifier() {
		return new DefaultLRDataClassifier<InteractionLRSourceDayData>();
	}

	@Override
	protected LocalDate getStartDate(IDSSUserProfile user, Database database,
			DateTime now) throws DatabaseException {
		DatabaseCriteria criteria = new DatabaseCriteria.Equal("user",
				user.getUsername());
		UserInteractionLogTable table = getInteractionLogTable(user, database);
		if (table == null)
			return now.toLocalDate();
		criteria = new DatabaseCriteria.Equal("user", user.getUsername());
		DatabaseSort[] sort = new DatabaseSort[] {
				new DatabaseSort("utcTime", true)
		};
		UserInteractionLog interactionLog = database.selectOne(table, criteria,
				sort);
		if (interactionLog == null)
			return now.toLocalDate();
		return interactionLog.getTzTime().toLocalDate();
	}

	@Override
	protected LocalDate getLastCompleteSourceDate(IDSSUserProfile user,
			Database database, DateTime now) throws DatabaseException {
		return now.toLocalDate().minusDays(1);
	}

	@Override
	protected InteractionLRSourceDayData readSourceData(IDSSUserProfile user,
			Database database, LocalDate date) throws DatabaseException,
			IOException {
		// TODO implement readSourceData()
		return null;
	}

	@Override
	protected DatabaseTableDef<DataUpdated> getSourceDataUpdatedTable() {
		return new LRSourceUpdatedTable();
	}

	@Override
	protected DatabaseTableDef<InteractionLRSourceDayData>
			getSourceDataTable() {
		// TODO implement getSourceDataTable()
		return null;
	}

	@Override
	protected DatabaseTableDef<DataUpdated> getWeekPatternUpdatedTable() {
		return new LRWeekPatternUpdatedTable();
	}

	@Override
	protected DatabaseTableDef<AverageDataParameter> getAveragesTable() {
		return new LRAveragesTable();
	}

	@Override
	protected List<WeekPatternParameter<InteractionLRSourceDayData>>
			getWeekPatternParameters() {
		// TODO implement getWeekPatternParameters()
		return null;
	}

	private UserInteractionLogTable getInteractionLogTable(
			IDSSUserProfile user, Database database) throws DatabaseException {
		DatabaseCriteria criteria = new DatabaseCriteria.Equal("user",
				user.getUsername());
		UserKey userKey = database.selectOne(new UserKeyTable(), criteria, null);
		if (userKey == null)
			return null;
		return new UserInteractionLogTable(userKey.getKey());
	}
}
