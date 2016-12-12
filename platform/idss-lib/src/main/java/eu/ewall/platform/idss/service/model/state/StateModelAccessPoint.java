package eu.ewall.platform.idss.service.model.state;

import org.joda.time.DateTime;

import eu.ewall.platform.idss.service.model.state.context.ContextModel;
import eu.ewall.platform.idss.service.model.state.domain.PhysicalActivityStateModel;
import eu.ewall.platform.idss.service.model.state.interaction.InteractionModel;
import eu.ewall.platform.idss.service.model.state.user.UserModel;

/**
 * Interface with easy methods to read the latest state models and to update
 * state models through the IDSS Core service.
 * 
 * @author Dennis Hofs (RRD)
 */
public interface StateModelAccessPoint {
	
	/**
	 * Retrieves the latest user model. It will update the specified attributes
	 * to the latest values.
	 * 
	 * @param user the user name
	 * @param updateAttrs the attributes to update to the latest value
	 * @return the user model
	 * @throws Exception if an error occurs while communicating with the IDSS
	 * Core service
	 */
	UserModel readUserModel(String user, String... updateAttrs)
			throws Exception;
	
	/**
	 * Sets a user model attribute's value, source reliability and update time.
	 * The value must be the string representation of the attribute's type. It
	 * should be possible to convert the value to that type using the Jackson
	 * ObjectMapper. The value cannot be null.
	 * 
	 * @param user the user name
	 * @param attribute the attribute name
	 * @param value the attribute value (not null)
	 * @param sourceReliability the source reliability (0 is very unreliable,
	 * 1 is certain) or null
	 * @param updated the time when the value was last obtained or null
	 * @return the updated user model
	 * @throws Exception if an error occurs while communicating with the IDSS
	 * Core service
	 */
	UserModel setUserModelAttribute(String user, String attr, String value,
			Double sourceReliability, DateTime updated) throws Exception;
	
	/**
	 * Retrieves the latest context model. It will update the specified
	 * attributes to the latest values.
	 * 
	 * @param user the user name
	 * @param updateAttrs the attributes to update to the latest value
	 * @return the context model
	 * @throws Exception if an error occurs while communicating with the IDSS
	 * Core service
	 */
	ContextModel readContextModel(String user, String... updateAttrs)
			throws Exception;
	
	/**
	 * Sets a context model attribute's value, source reliability and update
	 * time. The value must be the string representation of the attribute's
	 * type. It should be possible to convert the value to that type using the
	 * Jackson ObjectMapper. The value cannot be null.
	 * 
	 * @param user the user name
	 * @param attribute the attribute name
	 * @param value the attribute value (not null)
	 * @param sourceReliability the source reliability (0 is very unreliable,
	 * 1 is certain) or null
	 * @param updated the time when the value was last obtained or null
	 * @return the updated context model
	 * @throws Exception if an error occurs while communicating with the IDSS
	 * Core service
	 */
	ContextModel setContextModelAttribute(String user, String attr,
			String value, Double sourceReliability, DateTime updated)
			throws Exception;

	/**
	 * Retrieves the latest health domain model for physical activity.
	 * 
	 * @param user the user name
	 * @param updateAttrs the attributes to update to the latest value
	 * @return the health domain model for physical activity
	 * @throws Exception if an error occurs while communicating with the IDSS
	 * Core service
	 */
	PhysicalActivityStateModel readPhysicalActivityStateModel(String user,
			String... updateAttrs) throws Exception;
	
	/**
	 * Sets a physical activity state model attribute's value, source
	 * reliability and update time. The value must be the string representation
	 * of the attribute's type. It should be possible to convert the value to
	 * that type using the Jackson ObjectMapper. The value cannot be null.
	 * 
	 * @param user the user name
	 * @param attribute the attribute name
	 * @param value the attribute value (not null)
	 * @param sourceReliability the source reliability (0 is very unreliable,
	 * 1 is certain) or null
	 * @param updated the time when the value was last obtained or null
	 * @return the updated physical activity state model
	 * @throws Exception if an error occurs while communicating with the IDSS
	 * Core service
	 */
	PhysicalActivityStateModel setPhysicalActivityStateModelAttribute(
			String user, String attr, String value, Double sourceReliability,
			DateTime updated) throws Exception;
	
	/**
	 * Retrieves the latest interaction model. It will update the specified
	 * attributes to the latest values.
	 * 
	 * @param user the user name
	 * @param updateAttrs the attributes to update to the latest value
	 * @return the interaction model
	 * @throws Exception if an error occurs while communicating with the IDSS
	 * Core service
	 */
	InteractionModel readInteractionModel(String user, String... updateAttrs)
			throws Exception;
	
	/**
	 * Sets an interaction model attribute's value, source reliability and
	 * update time. The value must be the string representation of the
	 * attribute's type. It should be possible to convert the value to that
	 * type using the Jackson ObjectMapper. The value cannot be null.
	 * 
	 * @param user the user name
	 * @param attribute the attribute name
	 * @param value the attribute value (not null)
	 * @param sourceReliability the source reliability (0 is very unreliable,
	 * 1 is certain) or null
	 * @param updated the time when the value was last obtained or null
	 * @return the updated interaction model
	 * @throws Exception if an error occurs while communicating with the IDSS
	 * Core service
	 */
	InteractionModel setInteractionModelAttribute(String user, String attr,
			String value, Double sourceReliability, DateTime updated)
			throws Exception;
}
