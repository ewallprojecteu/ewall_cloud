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
 * Created by apne on 17/10/2016.
 */
public class ShoppingData extends AbstractDatabaseObject {
    @DatabaseField(value= DatabaseType.STRING, index=true)
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
    private String inShopping;

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

    public String getInShopping() {
        return inShopping;
    }

    public void setInShopping(String inShopping) {
        this.inShopping = inShopping;
    }
}
