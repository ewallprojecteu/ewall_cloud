package eu.ewall.platform.idss;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.web.client.RestOperations;

import eu.ewall.platform.idss.service.model.state.StateModel;
import eu.ewall.platform.idss.service.model.state.StateModelAccessPoint;
import eu.ewall.platform.idss.service.model.state.context.ContextModel;
import eu.ewall.platform.idss.service.model.state.domain.PhysicalActivityStateModel;
import eu.ewall.platform.idss.service.model.state.interaction.InteractionModel;
import eu.ewall.platform.idss.service.model.state.user.UserModel;

/**
 * This class provides easy methods to get the latest state models or to update
 * state models through the IDSS Core service.
 * 
 * @author Dennis Hofs (RRD)
 */
public class SpringStateModelAccessPoint implements StateModelAccessPoint {
	private RestOperations ewallClient;
	private String idssCoreUrl;

	/**
	 * Constructs a new state model access point.
	 * 
	 * @param ewallClient the client
	 * @param idssCoreUrl the URL to the IDSS Core service, without trailing
	 * slash
	 */
	public SpringStateModelAccessPoint(RestOperations ewallClient, String idssCoreUrl) {
		this.ewallClient = ewallClient;
		this.idssCoreUrl = idssCoreUrl;
	}
	
	@Override
	public UserModel readUserModel(String user, String... updateAttrs)
			throws Exception {
		StringBuilder url = new StringBuilder(idssCoreUrl +
				"/usermodel?userid={userid}");
		for (String attr : updateAttrs) {
			url.append("&updateAttrs=" + URLEncoder.encode(attr, "UTF-8"));
		}
		return ewallClient.getForObject(url.toString(), UserModel.class, user);
	}
	
	@Override
	public UserModel setUserModelAttribute(String user, String attr,
			String value, Double sourceReliability, DateTime updated)
			throws Exception {
		return setStateModelAttribute(UserModel.class, "usermodel", user, attr,
				value, sourceReliability, updated);
	}

	@Override
	public ContextModel readContextModel(String user, String... updateAttrs)
			throws Exception {
		StringBuilder url = new StringBuilder(idssCoreUrl +
				"/contextmodel?userid={userid}");
		for (String attr : updateAttrs) {
			url.append("&updateAttrs=" + URLEncoder.encode(attr, "UTF-8"));
		}
		return ewallClient.getForObject(url.toString(), ContextModel.class, user);
	}
	
	@Override
	public ContextModel setContextModelAttribute(String user, String attr,
			String value, Double sourceReliability, DateTime updated)
			throws Exception {
		return setStateModelAttribute(ContextModel.class, "contextmodel", user,
				attr, value, sourceReliability, updated);
	}
	
	@Override
	public PhysicalActivityStateModel readPhysicalActivityStateModel(
			String user, String... updateAttrs) throws Exception {
		StringBuilder url = new StringBuilder(idssCoreUrl +
				"/physactstatemodel?userid={userid}");
		for (String attr : updateAttrs) {
			url.append("&updateAttrs=" + URLEncoder.encode(attr, "UTF-8"));
		}
		return ewallClient.getForObject(url.toString(),
				PhysicalActivityStateModel.class, user);
	}
	
	@Override
	public PhysicalActivityStateModel setPhysicalActivityStateModelAttribute(
			String user, String attr, String value, Double sourceReliability,
			DateTime updated) throws Exception {
		return setStateModelAttribute(PhysicalActivityStateModel.class,
				"physactstatemodel", user, attr, value, sourceReliability,
				updated);
	}

	@Override
	public InteractionModel readInteractionModel(String user,
			String... updateAttrs) throws Exception {
		StringBuilder url = new StringBuilder(idssCoreUrl +
				"/interactionmodel?userid={userid}");
		for (String attr : updateAttrs) {
			url.append("&updateAttrs=" + URLEncoder.encode(attr, "UTF-8"));
		}
		return ewallClient.getForObject(url.toString(), InteractionModel.class,
				user);
	}
	
	@Override
	public InteractionModel setInteractionModelAttribute(String user,
			String attr, String value, Double sourceReliability,
			DateTime updated) throws Exception {
		return setStateModelAttribute(InteractionModel.class,
				"interactionmodel", user, attr, value, sourceReliability,
				updated);
	}
	
	private <T extends StateModel> T setStateModelAttribute(
			Class<T> stateModelClass, String endpoint, String user,
			String attr, String value, Double sourceReliability,
			DateTime updated) throws Exception {
		String url = idssCoreUrl + "/" + endpoint +
				"?userid={userid}&attr={attr}&value={value}";
		if (sourceReliability != null)
			url += "&sourceReliability={sourceReliability}";
		if (updated != null)
			url += "&updated={updated}";
		List<Object> params = new ArrayList<Object>();
		params.add(user);
		params.add(attr);
		params.add(value);
		if (sourceReliability != null)
			params.add(sourceReliability);
		if (updated != null)
			params.add(updated.toString("yyyy-MM-dd'T'HH:mm:ss.SSSZZ"));
		Object[] paramArray = params.toArray(new Object[params.size()]);
		return ewallClient.postForObject(url, null, stateModelClass,
				paramArray);
	}

}
