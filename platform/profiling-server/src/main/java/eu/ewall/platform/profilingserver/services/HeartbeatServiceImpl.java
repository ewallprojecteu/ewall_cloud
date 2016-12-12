package eu.ewall.platform.profilingserver.services;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import eu.ewall.platform.commons.datamodel.ewallsystem.Heartbeat;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;
import eu.ewall.platform.middleware.datamanager.dao.ewallsystem.HeartbeatDao;

/**
 * The Class HeartbeatServiceImpl.
 * 
 * @author 
 *
 */
@Service("heartbeatService")
public class HeartbeatServiceImpl implements DisposableBean {

	/** The log. */
	Logger log = LoggerFactory
			.getLogger(HeartbeatServiceImpl.class);

	/** The heartbeat dao. */
	private HeartbeatDao heartbeatDao;


	/**
	 * Instantiates a new heartbeat service impl.
	 */
	public HeartbeatServiceImpl() {
		heartbeatDao = new HeartbeatDao();
	}

	public Heartbeat getLastHeartbeat(String username) {
		log.debug("getLastHeartbeat");

		List<Heartbeat> heartbeats = heartbeatDao.getHeartbeats(username, 1);
		if(!heartbeats.isEmpty()){
			return heartbeats.get(0);
		}  else {
			return null;
		}
	}

	public List<Heartbeat> getLastNHeartbeats(String username, int last) {
		log.debug("getLastNHeartbeats");

		List<Heartbeat> heartbeats = heartbeatDao.getHeartbeats(username, last);
		return heartbeats;
		
	}

	public List<Heartbeat> getHeartbeats(String username, Date from, Date to) {
		List<Heartbeat> heartbeats = heartbeatDao.getHeartbeats(username, from, to);
		return heartbeats;
	}
	
	public boolean addHeartbeat(String username, String ip, int updateFrequency) {
		log.debug("addHeartbeat for user " + username + " from " + ip);
		
		try {
			heartbeatDao.addHeartbeat(new Heartbeat(username, ip, updateFrequency));
			return true;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}

	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	@Override
	public void destroy() throws Exception {
		MongoDBFactory.close();
	}

}
