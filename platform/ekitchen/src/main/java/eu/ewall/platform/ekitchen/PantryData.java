package eu.ewall.platform.ekitchen;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;
import eu.ewall.platform.idss.utils.json.DateTimeFromIsoDateTimeDeserializer;
import eu.ewall.platform.idss.utils.json.IsoDateTimeSerializer;
import org.joda.time.DateTime;

/**
 * Example data object. Data objects to store in the database should extend
 * {@link AbstractDatabaseObject AbstractDatabaseObject}. Each field should be
 * annotated with @DatabaseField. Provide public get/set methods for each
 * private field.
 *
 * <p>Data objects that are returned by a REST endpoint should support JSON
 * mapping. You may need to add annotations @JsonSerialize and @JsonDeserialize
 * to fields with data types that are not supported or not mapped correctly
 * by the Jackson ObjectMapper.</p>
 *
 * Created by apne on 17/10/2016.
 */
public class PantryData extends AbstractDatabaseObject {
    @DatabaseField(value=DatabaseType.STRING, index=true)
    private String username;

    // Store times as LONG if you need queries with greater than/less than
    // or queries with sorting by time.
    // ISOTIME does not work for that because of time zones
    @DatabaseField(value=DatabaseType.LONG, index=true)
    private long timestamp;

    @DatabaseField(value=DatabaseType.ISOTIME)
    @JsonSerialize(using=IsoDateTimeSerializer.class)
    @JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
    private DateTime isoTime;

    @DatabaseField(value=DatabaseType.STRING)
    private String inPantry;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public DateTime getIsoTime() {
        return isoTime;
    }

    public void setIsoTime(DateTime isoTime) {
        this.isoTime = isoTime;
    }

    public String getInPantry() {
        return inPantry;
    }

    public void setInPantry(String inPantry) {
        this.inPantry = inPantry;
    }
}
