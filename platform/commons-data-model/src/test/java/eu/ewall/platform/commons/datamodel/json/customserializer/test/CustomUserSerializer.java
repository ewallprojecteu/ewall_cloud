/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.json.customserializer.test;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import eu.ewall.platform.commons.datamodel.profile.User;

/**
 * The Class CustomUserSerializer.
 */
public class CustomUserSerializer extends JsonSerializer<User> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.fasterxml.jackson.databind.JsonSerializer#serialize(java.lang.Object,
	 * com.fasterxml.jackson.core.JsonGenerator,
	 * com.fasterxml.jackson.databind.SerializerProvider)
	 */
	@Override
	public void serialize(User user, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeStartObject();
		jgen.writeStringField("UserFirstName", user.getFirstName());
		jgen.writeStringField("UserRole", user.getUserRole().name());
		jgen.writeStringField("UserNickName", user.getUserProfile()
				.getvCardSubProfile().getNickname());

		jgen.writeEndObject();
	}

}