package eu.ewall.platform.idss.service.model.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;
import eu.ewall.platform.idss.service.model.type.MoodCategory;
import eu.ewall.platform.idss.service.model.type.MoodTrend;
import eu.ewall.platform.idss.utils.json.SqlDateDeserializer;
import eu.ewall.platform.idss.utils.json.SqlDateSerializer;

import org.joda.time.LocalDate;

public class MoodData extends AbstractDatabaseObject{
	
	@JsonIgnore
	private String id;

	@DatabaseField(value=DatabaseType.STRING)
	private String user;
	
	@DatabaseField(value=DatabaseType.DATE)
	@JsonSerialize(using=SqlDateSerializer.class)
	@JsonDeserialize(using=SqlDateDeserializer.class)
	private LocalDate date;
	
	@DatabaseField(value=DatabaseType.STRING)
	private MoodCategory mood;
	
	@DatabaseField(value=DatabaseType.STRING)
	private MoodTrend trend;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public MoodCategory getMood() {
		return mood;
	}

	public void setMood(MoodCategory mood) {
		this.mood = mood;
	}

	public MoodTrend getTrend() {
		return trend;
	}

	public void setTrend(MoodTrend trend) {
		this.trend = trend;
	}
}
