package eu.ewall.platform.idss.core.service;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.ILoggerFactory;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseConnection;
import eu.ewall.platform.idss.dao.DatabaseCriteria;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseFactory;
import eu.ewall.platform.idss.dao.DatabaseSort;
import eu.ewall.platform.idss.dao.DatabaseTableDef;
import eu.ewall.platform.idss.service.ScheduledService;
import eu.ewall.platform.idss.service.exception.UserNotFoundException;
import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.idss.service.model.state.StateModel;
import eu.ewall.platform.idss.service.model.state.context.ContextModel;
import eu.ewall.platform.idss.service.model.state.context.ContextModelTable;
import eu.ewall.platform.idss.service.model.state.domain.PhysicalActivityStateModel;
import eu.ewall.platform.idss.service.model.state.domain.PhysicalActivityStateModelTable;
import eu.ewall.platform.idss.service.model.state.interaction.InteractionModel;
import eu.ewall.platform.idss.service.model.state.interaction.InteractionModelTable;
import eu.ewall.platform.idss.service.model.state.user.UserModel;
import eu.ewall.platform.idss.service.model.state.user.UserModelTable;
import eu.ewall.platform.idss.utils.AppComponents;
import eu.ewall.platform.idss.utils.datetime.VirtualClock;
import eu.ewall.platform.idss.utils.exception.FatalException;

/**
 * Scheduled core service for the IDSS and lifestyle reasoners. After
 * construction you must call {@link #setPullInputProvider(PullInputProvider)
 * setPullInputProvider()}.
 * 
 * <p>You must configure a {@link DatabaseFactory DatabaseFactory} in the
 * {@link AppComponents AppComponents}. You may also configure an {@link
 * ILoggerFactory ILoggerFactory}.</p>
 * 
 * <p>The service gets the time from the {@link VirtualClock VirtualClock} so
 * it supports quick simulations.</p>
 * 
 * @author Dennis Hofs (RRD)
 */
public class IDSSCoreService extends ScheduledService {
	public static final String LOGTAG = "IDSSCore";
	
	private PullInputProvider pullInputProvider;
	private final Object dbLock = new Object();
	private String dbName;
	
	/**
	 * Constructs a new instance.
	 * 
	 * @param dbName the database name
	 */
	public IDSSCoreService(String dbName) {
		this.dbName = dbName;
	}

	/**
	 * Sets the pull input provider. This must be called before starting the
	 * service.
	 * 
	 * @param pullInputProvider the input provider
	 */
	public void setPullInputProvider(PullInputProvider pullInputProvider) {
		this.pullInputProvider = pullInputProvider;
	}

	@Override
	public void runTask() throws FatalException, Exception {
		logger.info("Start task: IDSSCore");
		DatabaseConnection dbConn = openDatabaseConnection();
		try {
			Database database = getDatabase(dbConn);
			List<IDSSUserProfile> users = pullInputProvider.getUsers();
			for (IDSSUserProfile user : users) {
				updateUserModel(database, user);
				updateContextModel(database, user);
				updatePhysicalActivityStateModel(database, user);
				updateInteractionModel(database, user);
			}
		} catch(FatalException fe) {
			logger.error(fe.getMessage());
		} catch(Exception e) {
			logger.error(e.getMessage());
			
		} finally {
			closeDatabaseConnection(dbConn);
		}
		logger.info("End task: IDSSCore");
	}
	
	/**
	 * Returns the latest user model for the specified user from the database.
	 * If there is no model yet, this method will create a new model with the
	 * latest values obtained from the pull input provider.
	 * 
	 * <p>If you set one or more attributes in 'updateAttrs', this method will
	 * always update those attributes with the latest values from the pull
	 * input provider.</p>
	 * 
	 * @param user the user name
	 * @param updateAttrs if not null or empty, this method will always update
	 * these attributes with the latest values from the pull input provider
	 * @return the user model
	 * @throws UserNotFoundException if the user doesn't exist
	 * @throws Exception if an error occurs while communicating with the
	 * database or the pull input provider
	 */
	public UserModel getUserModel(String user, List<String> updateAttrs)
			throws UserNotFoundException, Exception {
		return readOrCreateStateModel(user, new UserModelTable(),
				UserModel.class, new UserModelAttributeReader(), updateAttrs);
	}

	public UserModel setUserModelAttribute(String user, String attr,
			Object value, Double sourceReliability, DateTime updated)
			throws UserNotFoundException, Exception {
		return setStateModelAttribute(user, new UserModelTable(),
				UserModel.class, new UserModelAttributeReader(), attr,
				value, sourceReliability, updated);
	}
	
	/**
	 * Returns the latest context model for the specified user from the
	 * database. If there is no model yet, this method will create a new model
	 * with the latest values obtained from the pull input provider.
	 * 
	 * <p>If you set one or more attributes in 'updateAttrs', this method will
	 * always update those attributes with the latest values from the pull
	 * input provider.</p>
	 * 
	 * @param user the user name
	 * @param updateAttrs if not null or empty, this method will always update
	 * these attributes with the latest values from the pull input provider
	 * @return the context model
	 * @throws UserNotFoundException if the user doesn't exist
	 * @throws Exception if an error occurs while communicating with the
	 * database or the pull input provider
	 */
	public ContextModel getContextModel(String user, List<String> updateAttrs)
			throws UserNotFoundException, Exception {
		return readOrCreateStateModel(user, new ContextModelTable(),
				ContextModel.class, new ContextModelAttributeReader(),
				updateAttrs);
	}

	public ContextModel setContextModelAttribute(String user, String attr,
			Object value, Double sourceReliability, DateTime updated)
			throws UserNotFoundException, Exception {
		return setStateModelAttribute(user, new ContextModelTable(),
				ContextModel.class, new ContextModelAttributeReader(), attr,
				value, sourceReliability, updated);
	}
	
	/**
	 * Returns the latest health domain model for physical activity for the
	 * specified user from the database. If there is no model yet, this method
	 * will create a new model with the latest values obtained from the pull
	 * input provider.
	 * 
	 * <p>If you set one or more attributes in 'updateAttrs', this method will
	 * always update those attributes with the latest values from the pull
	 * input provider.</p>
	 * 
	 * @param user the user name
	 * @param updateAttrs if not null or empty, this method will always update
	 * these attributes with the latest values from the pull input provider
	 * @return the health domain model for physical activity
	 * @throws UserNotFoundException if the user doesn't exist
	 * @throws Exception if an error occurs while communicating with the
	 * database or the pull input provider
	 */
	public PhysicalActivityStateModel getPhysicalActivityStateModel(
			String user, List<String> updateAttrs)
			throws UserNotFoundException, Exception {
		return readOrCreateStateModel(user,
				new PhysicalActivityStateModelTable(),
				PhysicalActivityStateModel.class,
				new PhysicalActivityStateModelAttributeReader(), updateAttrs);
	}
	
	public PhysicalActivityStateModel setPhysicalActivityStateModelAttribute(
			String user, String attr, Object value, Double sourceReliability,
			DateTime updated) throws UserNotFoundException, Exception {
		return setStateModelAttribute(user,
				new PhysicalActivityStateModelTable(),
				PhysicalActivityStateModel.class,
				new PhysicalActivityStateModelAttributeReader(), attr, value,
				sourceReliability, updated);
	}
	
	/**
	 * Returns the latest interaction model for the specified user from the
	 * database. If there is no model yet, this method will create a new model
	 * with the latest values obtained from the pull input provider.
	 * 
	 * <p>If you set one or more attributes in 'updateAttrs', this method will
	 * always update those attributes with the latest values from the pull
	 * input provider.</p>
	 * 
	 * @param user the user name
	 * @param updateAttrs if not null or empty, this method will always update
	 * these attributes with the latest values from the pull input provider
	 * @return the interaction model
	 * @throws UserNotFoundException if the user doesn't exist
	 * @throws Exception if an error occurs while communicating with the
	 * database or the pull input provider
	 */
	public InteractionModel getInteractionModel(String user,
			List<String> updateAttrs) throws UserNotFoundException, Exception {
		return readOrCreateStateModel(user, new InteractionModelTable(),
				InteractionModel.class, new InteractionModelAttributeReader(),
				updateAttrs);
	}

	public InteractionModel setInteractionModelAttribute(String user,
			String attr, Object value, Double sourceReliability,
			DateTime updated) throws UserNotFoundException, Exception {
		return setStateModelAttribute(user, new InteractionModelTable(),
				InteractionModel.class, new InteractionModelAttributeReader(),
				attr, value, sourceReliability, updated);
	}

	/**
	 * Reads the latest model from the specified state model table. If there is
	 * no model in the table, this method will create a new model with the
	 * latest values obtained from the pull input provider.
	 * 
	 * <p>If you set one or more attributes in 'updateAttrs', this method will
	 * always update those attributes with the latest values from the pull
	 * input provider.</p>
	 * 
	 * @param user the user name
	 * @param tableDef the state model table
	 * @param modelClass the state model class
	 * @param reader the reader from the pull input provider
	 * @param updateAttrs if not null or empty, this method will always update
	 * these attributes with the latest values from the pull input provider
	 * @return the state model
	 * @throws UserNotFoundException if the user doesn't exist
	 * @throws Exception if an error occurs while communicating with the
	 * database or the pull input provider
	 */
	private <T extends StateModel> T readOrCreateStateModel(String user,
			DatabaseTableDef<T> tableDef, Class<T> modelClass,
			StateModelAttributeReader<T> reader, List<String> updateAttrs)
			throws UserNotFoundException, Exception {
		IDSSUserProfile profile = findUser(user);
		DatabaseConnection dbConn = openDatabaseConnection();
		try {
			Database database = getDatabase(dbConn);
			T model = getStateModel(database, user, tableDef);
			boolean doUpdate = updateAttrs != null && !updateAttrs.isEmpty();
			if (model != null && !doUpdate)
				return model;
			T result = updateStateModel(database, profile, tableDef, modelClass,
					model, reader, updateAttrs);
			return result;
		} finally {
			closeDatabaseConnection(dbConn);
		}
	}
	
	/**
	 * Tries to find the user with the specified user name. If the user does
	 * not exist, this method throws an exception.
	 * 
	 * @param user the user name
	 * @return the user profile
	 * @throws UserNotFoundException if the user doesn't exist
	 * @throws Exception if the user profiles can't be retrieved
	 */
	private IDSSUserProfile findUser(String user) throws UserNotFoundException,
	Exception {
		List<IDSSUserProfile> users = pullInputProvider.getUsers();
		for (IDSSUserProfile profile : users) {
			if (profile.getUsername().equals(user))
				return profile;
		}
		throw new UserNotFoundException("User \"" + user + "\" not found");
	}
	
	/**
	 * Reads the latest model from the specified state model table. If there is
	 * no model in the table, this method returns null.
	 * 
	 * @param database the database
	 * @param user the user name
	 * @param tableDef the state model table
	 * @return the state model or null
	 * @throws Exception if an error occurs while reading from the database
	 */
	private <T extends StateModel> T getStateModel(Database database,
			String user, DatabaseTableDef<T> tableDef) throws Exception {
		DatabaseCriteria criteria = new DatabaseCriteria.Equal("user",
				user);
		DatabaseSort[] sort = new DatabaseSort[] {
				new DatabaseSort("created", false)
		};
		synchronized (dbLock) {
			return database.selectOne(tableDef, criteria, sort);
		}
	}
	
	/**
	 * Updates the user model with the latest values obtained from the pull
	 * input provider. If a data attribute value has changed, it will insert a
	 * new model into the database. Otherwise it updates the metadata of the
	 * current model.
	 * 
	 * @param database the database
	 * @param user the user
	 * @throws Exception if an error occurs while communicating with the
	 * database or the pull input provider
	 */
	private void updateUserModel(Database database, IDSSUserProfile user)
			throws Exception {
		UserModelTable table = new UserModelTable();
		UserModel prevModel = getStateModel(database, user.getUsername(),
				table);
		updateStateModel(database, user, table, UserModel.class, prevModel,
				new UserModelAttributeReader(), null);
	}
	
	/**
	 * Updates the context model with the latest values obtained from the pull
	 * input provider. If a data attribute value has changed, it will insert a
	 * new model into the database. Otherwise it updates the metadata of the
	 * current model.
	 * 
	 * @param database the database
	 * @param user the user
	 * @throws Exception if an error occurs while communicating with the
	 * database or the pull input provider
	 */
	private void updateContextModel(Database database, IDSSUserProfile user) throws Exception {
		ContextModelTable table = new ContextModelTable();
		ContextModel prevModel = getStateModel(database, user.getUsername(),table);
		updateStateModel(database, user, table, ContextModel.class, prevModel,
				new ContextModelAttributeReader(), null);
	}
	
	/**
	 * Updates the health domain model for physical activity with the latest
	 * values obtained from the pull input provider. If a data attribute value
	 * has changed, it will insert a new model into the database. Otherwise it
	 * updates the metadata of the current model.
	 * 
	 * @param database the database
	 * @param user the user
	 * @throws Exception if an error occurs while communicating with the
	 * database or the pull input provider
	 */
	private void updatePhysicalActivityStateModel(Database database,
			IDSSUserProfile user) throws Exception {
		PhysicalActivityStateModelTable table =
				new PhysicalActivityStateModelTable();
		PhysicalActivityStateModel prevModel = getStateModel(database,
				user.getUsername(), table);
		updateStateModel(database, user, table,
				PhysicalActivityStateModel.class, prevModel,
				new PhysicalActivityStateModelAttributeReader(), null);
	}
	
	/**
	 * Updates the interaction model with the latest values obtained from the
	 * pull input provider. If a data attribute value has changed, it will
	 * insert a new model into the database. Otherwise it updates the metadata
	 * of the current model.
	 * 
	 * @param database the database
	 * @param user the user
	 * @throws Exception if an error occurs while communicating with the
	 * database or the pull input provider
	 */
	private void updateInteractionModel(Database database,
			IDSSUserProfile user) throws Exception {
		InteractionModelTable table = new InteractionModelTable();
		InteractionModel prevModel = getStateModel(database,
				user.getUsername(), table);
		updateStateModel(database, user, table, InteractionModel.class,
				prevModel, new InteractionModelAttributeReader(), null);
	}
	
	/**
	 * Updates a state model with the latest values obtained from the pull
	 * input provider. It takes the current state model. If the new model has
	 * the same attribute values as the current one, this method will just
	 * update the metadata in the current model, save it to the database and
	 * return it. Otherwise it will insert the new model into the database and
	 * return it.
	 * 
	 * <p>You can set parameter 'attributes' so that only some attributes are
	 * updated. But if the current state model is null, this method will always
	 * retrieve all attributes from the pull input provider.</p>
	 * 
	 * @param database the database
	 * @param user the user
	 * @param tableDef the state model table
	 * @param modelClass the state model class
	 * @param prevModel the current state model or null
	 * @param reader the reader from the pull input provider
	 * @param attributes the attributes to update. If null or empty, all
	 * attributes will be updated. If 'prevModel' is null, all attributes are
	 * retrieved and this parameter is ignored.
	 * @return the new state model
	 * @throws Exception if an error occurs while reading from the pull input
	 * provider or writing to the database
	 */
	private <T extends StateModel> T updateStateModel(
			Database database, IDSSUserProfile user, DatabaseTableDef<T> tableDef,
			Class<T> modelClass, T prevModel,
			StateModelAttributeReader<T> reader, List<String> attributes)
			throws Exception {
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(user.getTimeZone());
		T currModel = modelClass.newInstance();
		currModel.setCreated(now);
		currModel.setUser(user.getUsername());
		if (prevModel != null)
			prevModel.copyAttributesInto(currModel);
		if (prevModel == null || attributes == null || attributes.isEmpty())
			attributes = currModel.getAttributes();
		for (String att : attributes) {
			try {
				reader.getStateModelAttribute(user, att, currModel);
			} catch (Exception ex) {
				logger.warn("Failed to get value for state model attribute \"{}\": {}",
						att, ex.getMessage());
			}
		}
		synchronized (dbLock) {
			if (prevModel != null && currModel.isEqualAttributeValues(prevModel)) {
				currModel.copyAttributesInto(prevModel);
				database.update(tableDef.getName(), prevModel);
				return prevModel;
			} else {
				database.insert(tableDef.getName(), currModel);
				logger.info("Updated {} for user '{}'.",
						modelClass.getSimpleName(), user.getUsername());
				return currModel;
			}
		}
	}
	
	private <T extends StateModel> T setStateModelAttribute(String user,
			DatabaseTableDef<T> tableDef, Class<T> modelClass,
			StateModelAttributeReader<T> reader, String attr, Object value,
			Double sourceReliability, DateTime updated)
			throws UserNotFoundException, Exception {
		DateTime now = new DateTime();
		IDSSUserProfile profile = findUser(user);
		DatabaseConnection dbConn = openDatabaseConnection();
		try {
			Database database = getDatabase(dbConn);
			T model = getStateModel(database, user, tableDef);
			boolean modelExists = model != null;
			if (!modelExists) {
				model = updateStateModel(database, profile, tableDef,
						modelClass, model, reader, null);
			}
			model.setAttribute(attr, value, sourceReliability, updated);
			if (modelExists) {
				model.setId(null);
				model.setCreated(now);
				database.insert(tableDef.getName(), model);
			} else {
				database.update(tableDef.getName(), model);
			}
			return model;
		} finally {
			closeDatabaseConnection(dbConn);
		}
	}
	
	/**
	 * Interface for reading an attribute value for a state model from the pull
	 * input provider.
	 */
	private interface StateModelAttributeReader<T extends StateModel> {
		
		/**
		 * Reads the specified attribute from the pull input provider and
		 * writes it to the specified state model.
		 * 
		 * @param user the user
		 * @param attribute the attribute name
		 * @param model the state model
		 * @throws Exception if an error occurs while reading the value
		 */
		void getStateModelAttribute(IDSSUserProfile user, String attribute, T model)
				throws Exception;
	}
	
	/**
	 * State model attribute reader for the user model.
	 */
	private class UserModelAttributeReader implements
	StateModelAttributeReader<UserModel> {
		@Override
		public void getStateModelAttribute(IDSSUserProfile user,
				String attribute, UserModel model) throws Exception {
			pullInputProvider.getUserModelAttribute(user, attribute, model);
		}
	}
	
	/**
	 * State model attribute reader for the context model.
	 */
	private class ContextModelAttributeReader implements
	StateModelAttributeReader<ContextModel> {
		@Override
		public void getStateModelAttribute(IDSSUserProfile user,
				String attribute, ContextModel model) throws Exception {
			pullInputProvider.getContextModelAttribute(user, attribute, model);
		}
	}
	
	/**
	 * State model attribute reader for the health domain model for physical
	 * activity.
	 */
	private class PhysicalActivityStateModelAttributeReader implements
	StateModelAttributeReader<PhysicalActivityStateModel> {
		@Override
		public void getStateModelAttribute(IDSSUserProfile user,
				String attribute, PhysicalActivityStateModel model)
				throws Exception {
			pullInputProvider.getPhysicalActivityStateModelAttribute(user,
					attribute, model);
		}
	}

	/**
	 * State model attribute reader for the interaction model.
	 */
	private class InteractionModelAttributeReader implements
	StateModelAttributeReader<InteractionModel> {
		@Override
		public void getStateModelAttribute(IDSSUserProfile user,
				String attribute, InteractionModel model) throws Exception {
			pullInputProvider.getInteractionModelAttribute(user, attribute,
					model);
		}
	}

	/**
	 * Returns the database for this service. It will create or upgrade the
	 * database if needed.
	 * 
	 * @param dbConn the database connection
	 * @return the database
	 * @throws DatabaseException if a database error occurs
	 */
	private Database getDatabase(DatabaseConnection dbConn)
			throws DatabaseException {
		synchronized (dbLock) {
			List<DatabaseTableDef<?>> tableDefs =
					new ArrayList<DatabaseTableDef<?>>();
			tableDefs.add(new UserModelTable());
			tableDefs.add(new ContextModelTable());
			tableDefs.add(new PhysicalActivityStateModelTable());
			tableDefs.add(new InteractionModelTable());
			return dbConn.initDatabase(dbName, tableDefs, false);
		}
	}
}
