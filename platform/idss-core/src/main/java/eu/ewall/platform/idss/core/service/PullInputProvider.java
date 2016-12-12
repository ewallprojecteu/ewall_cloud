package eu.ewall.platform.idss.core.service;

import java.io.IOException;
import java.util.List;

import org.joda.time.DateTime;

import eu.ewall.platform.idss.service.RemoteMethodException;
import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.idss.service.model.state.StateModel;
import eu.ewall.platform.idss.service.model.state.context.ContextModel;
import eu.ewall.platform.idss.service.model.state.domain.PhysicalActivityStateModel;
import eu.ewall.platform.idss.service.model.state.interaction.InteractionModel;
import eu.ewall.platform.idss.service.model.state.user.UserModel;

/**
 * Interface for the pull input provider for the {@link IDSSCoreService
 * IDSSCoreService}. The service uses this provider to pull input from other
 * components in the system. We call them remote components, although they may
 * run in the same process. An implementation must be set before starting the
 * service.
 * 
 * @author Dennis Hofs (RRD)
 */
public interface PullInputProvider {

	/**
	 * Returns all users that the service should work on.
	 * 
	 * @return the users
	 * @throws IOException if an error occurs while communicating with the
	 * remote component
	 * @throws RemoteMethodException if the remote component returned an error
	 * @throws Exception if any other error occurs
	 */
	List<IDSSUserProfile> getUsers()
			throws IOException, RemoteMethodException, Exception;

	/**
	 * Retrieves the current value for the specified user model attribute and
	 * writes it to the user model annotated with a reliability and update
	 * time. This can be done using {@link
	 * StateModel#setAttribute(String, Object, Double, DateTime)
	 * model.setAttribute()}.
	 * 
	 * @param user the user
	 * @param attribute the attribute name
	 * @param model the user model to which the value should be written
	 * @throws IOException if an error occurs while communicating with the
	 * remote component
	 * @throws RemoteMethodException if the remote component returned an error
	 * @throws Exception if any other error occurs
	 */
	void getUserModelAttribute(IDSSUserProfile user, String attribute,
			UserModel model) throws IOException, RemoteMethodException,
			Exception;
	
	/**
	 * Retrieves the value for the specified context model attribute and writes
	 * it to the context model annotated with a reliability and update time.
	 * This can be done using {@link
	 * StateModel#setAttribute(String, Object, Double, DateTime)
	 * model.setAttribute()}.
	 * 
	 * @param user the user
	 * @param attribute the attribute name
	 * @param model the context model to which the value should be written
	 * @throws IOException if an error occurs while communicating with the
	 * remote component
	 * @throws RemoteMethodException if the remote component returned an error
	 * @throws Exception if any other error occurs
	 */
	void getContextModelAttribute(IDSSUserProfile user, String attribute,
			ContextModel model) throws IOException, RemoteMethodException,
			Exception;
	
	/**
	 * Retrieves the value for the specified health domain model attribute for
	 * physical activity and writes it to the model annotated with a
	 * reliability and update time. This can be done using {@link
	 * StateModel#setAttribute(String, Object, Double, DateTime)
	 * model.setAttribute()}.
	 * 
	 * @param user the user
	 * @param attribute the attribute name
	 * @param model the health domain model for physical activity to which the
	 * value should be written
	 * @throws IOException if an error occurs while communicating with the
	 * remote component
	 * @throws RemoteMethodException if the remote component returned an error
	 * @throws Exception if any other error occurs
	 */
	void getPhysicalActivityStateModelAttribute(IDSSUserProfile user,
			String attribute, PhysicalActivityStateModel model)
			throws IOException, RemoteMethodException, Exception;
	
	/**
	 * Retrieves the value for the specified interaction model attribute and
	 * writes it to the interaction model annotated with a reliability and
	 * update time. This can be done using {@link
	 * StateModel#setAttribute(String, Object, Double, DateTime)
	 * model.setAttribute()}.
	 * 
	 * @param user the user
	 * @param attribute the attribute name
	 * @param model the interaction model to which the value should be written
	 * @throws IOException if an error occurs while communicating with the
	 * remote component
	 * @throws RemoteMethodException if the remote component returned an error
	 * @throws Exception if any other error occurs
	 */
	void getInteractionModelAttribute(IDSSUserProfile user, String attribute,
			InteractionModel model) throws IOException, RemoteMethodException,
			Exception;
}
