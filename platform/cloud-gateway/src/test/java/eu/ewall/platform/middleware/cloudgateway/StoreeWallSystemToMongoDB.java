package eu.ewall.platform.middleware.cloudgateway;

import java.util.UUID;

import org.springframework.data.mongodb.core.MongoOperations;
import eu.ewall.platform.commons.datamodel.ewallsystem.SensingEnvironment;
import eu.ewall.platform.commons.datamodel.marshalling.json.DM2JsonObjectMapper;

/**
 * The Class StoreeWallSystemToMongoDB.
 */
public class StoreeWallSystemToMongoDB {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		MongoOperations mongoOps = MongoDBFactoryForTesting.getMongoOperations();


		SensingEnvironment env1 = new SensingEnvironment(
				UUID.fromString("00000000-0000-0000-0000-000000000000"));
		
		//User user = new User("John", "Doe", "johndoe0", null, UserRole.PRIMARY_USER);
	//	env1.setPrimaryUser(user);
		
		

		//system1.addSensingEnvironment(env1);
		
		String env1String = DM2JsonObjectMapper.writeValueAsString(env1);
		

	//	mongoOps.save(system1, EWallSystemMongo.EWALLSYSTEMS_COLLECTION);

		//mongoOps.save(env1, SensingEnvironmentMongo.SENSINGENV_COLLECTION);

		
		System.out.println("Test systems and env added to mongodb:" + env1String);

	}
}
